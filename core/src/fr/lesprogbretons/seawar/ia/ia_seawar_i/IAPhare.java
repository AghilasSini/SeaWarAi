package fr.lesprogbretons.seawar.ia.ia_seawar_i;

import fr.lesprogbretons.seawar.controller.Controller;
import fr.lesprogbretons.seawar.ia.AbstractIA;
import fr.lesprogbretons.seawar.model.Partie;
import fr.lesprogbretons.seawar.model.actions.Action;
import fr.lesprogbretons.seawar.model.actions.PassTurn;
import fr.lesprogbretons.seawar.model.boat.Boat;
import fr.lesprogbretons.seawar.model.cases.Case;
import fr.lesprogbretons.seawar.model.map.Grille;

import java.util.ArrayList;
import java.util.List;

public class IAPhare extends AbstractIA{

    private Case phareproche;

    public IAPhare(int identifier) {
        super(identifier,"IAPhare");
    }

    public IAPhare(int identifier, String name, List<Boat> boats) {
        super(identifier, name, boats);
    }

    public Case getPhareproche() {
        return phareproche;
    }

    public void setPhareproche(Case phareproche) {
        this.phareproche = phareproche;
    }

    @Override
    public Action chooseAction(Controller controller) {

        double distance = Double.POSITIVE_INFINITY; //infini
        Partie partie = controller.getPartie();
        Case caseDepart = partie.getBateauSelectionne().getPosition();
        ArrayList<Case> phares = this.getPhares(partie.getMap());
        //Permet de trouver le phare le plus proche du bateau selectionné
        for (Case c : phares) {
            if (c.getPossedePhare() != partie.getCurrentPlayer()) {
                if (distance > this.distance(caseDepart, c)) {
                    this.setPhareproche(c);
                    distance = this.distance(caseDepart, c);
                }
            }
        }

        this.alphaBeta(controller, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 2);
        return this.getMemorizedAction();
    }

    //l'heuristique est la distance au phare, du coup on s'est basé sur beta et non alpha. On cherche à minimiser la distance.
    public double alphaBeta(Controller controller, double alpha, double beta,int n) {
        Partie partie = controller.getPartie();
        if (partie == null || n ==0){
            double distance = Double.POSITIVE_INFINITY; //infini
            Case caseDepart = partie.getBateauSelectionne().getPosition();
            distance=this.distance(caseDepart,this.getPhareproche());
            return distance;
        }
        else{
            if (partie.getCurrentPlayer() instanceof IAPhare) {
                for (Action action : controller.getPossibleActions()) {
                    Controller controllerClone = (Controller) controller.clone();
                    Action actionClone = (Action) action.clone();
                    actionClone.apply(controllerClone);
                    double oldBeta = beta;
                    beta = Math.min(beta, alphaBeta(controllerClone, alpha, beta, n - 1));
                    //on utilise oldbeta pour voir si notre beta a changé pour memoriser l'action
                    if (beta < oldBeta) {
                        if (!(action instanceof PassTurn)){
                            this.memoriseAction(action);
                        }
                    }
                }
                return beta;
            }
            else{
                return alpha;
            }
        }
    }

    //Permet d'avoir la liste de phares sur la grille
    public static ArrayList<Case> getPhares(Grille grille ){
        ArrayList<Case> phares = new ArrayList<Case>();
        for (Case[] ligne: grille.getTableau()) {
            for (Case c: ligne) {
                if (c.isPhare()) {
                    phares.add(c);
                }
            }

        }
        return phares;
    }

    //Permet de calculer la distance euclidienne entre deux cases
    public static double distance(Case caseDepart, Case caseArrivee){
        return Math.sqrt(Math.pow(caseArrivee.getY()-caseDepart.getY(),2) + Math.pow(caseArrivee.getX()-caseDepart.getX(),2));
    }
}
