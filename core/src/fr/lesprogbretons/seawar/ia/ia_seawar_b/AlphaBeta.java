package fr.lesprogbretons.seawar.ia.ia_seawar_b;

import fr.lesprogbretons.seawar.controller.Controller;
import fr.lesprogbretons.seawar.ia.AbstractIA;
//import fr.lesprogbretons.seawar.ia.groupe_b.RechercheEltIA;
import fr.lesprogbretons.seawar.model.Partie;
import fr.lesprogbretons.seawar.model.actions.Action;
import fr.lesprogbretons.seawar.model.actions.MoveBoat;
import fr.lesprogbretons.seawar.model.boat.Boat;
import fr.lesprogbretons.seawar.model.cases.Case;
import fr.lesprogbretons.seawar.model.cases.CaseTerre;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AlphaBeta extends AbstractIA {
    private int action;
    private int phare;

    public AlphaBeta(int number){
        super(number,"AlphaBeta");
    }

    public AlphaBeta (int number, String name){
        super(number,name);
    }

    public AlphaBeta (int number, String name, List<Boat> list){
        super(number,name,list);
    }

    @Override
    //Fonction permettant au controller de demander a l'IA qu'elle action elle veut effectuer
    public Action chooseAction(Controller controller) {
        int i;
        //Les valeurs de alpha et beta sont inverse par rapport au poly car on va prend l'heuristique la plus basse et non le coup de plus grande valeur
        alphaBeta(controller,1000,-1000,0);
        return controller.getPossibleActions().get(action);
    }

    //Fonction permettant de savoir si notre bateau se trouve sur une case phare qui n'est pas a lui de façon a rester sur celle ci lors du déroulement d'alphaBeta
    public boolean isLighthouseGoal(Partie partie){
        if(partie.getBateauSelectionne().getPosition().isPhare()){
            if(partie.getBateauSelectionne().getPosition().getPossedePhare() != null){
                if(partie.getBateauSelectionne().getPosition().getPossedePhare().getNumber() != partie.getCurrentPlayer().getNumber()){
                    //On est sur un phare posseder par l'adversaire
                    return true;
                }
            }
            else{
                //on est sur un phare qui n'a pas ete pris
                return true;
            }
        }
        return false;
    }

    //Fonction utillisant l'algorithme alphaBeta vu en cours afin de determiner la meilleur action a effectuer en fonction de notre heuristique
    public int alphaBeta(Controller controller, int alpha, int beta, int n) {
        Partie partie = controller.getPartie();
        ArrayList<Action> actions = new ArrayList<>();
        ArrayList<Action> actionsPossible = new ArrayList<>();
        Controller controllerFils;
        actionsPossible.addAll(controller.getPossibleActions());
        int a;
        int b;
        int i;
        int temp;
        MoveBoat tempMove;
        if (n <= 2) {
            for (i = 0; i < actionsPossible.size(); i++) {
                if (actionsPossible.get(i) instanceof MoveBoat) {
                    actions.add(actionsPossible.get(i));
                }
            }

            if (n > 0 && isLighthouseGoal(partie)) {
                //On se trouve sur un phare a capture
                return 0;
            } else {
                //Noeud fils
                i = 0;
                //if (partie.getCurrentPlayer().getNumber() == this.getNumber()) {
                if(partie.getBateauSelectionne().getJoueur().getNumber() == this.getNumber()){
                //if(joueur == 1){
                    //Noeud Max
                    a = alpha;
                    while (i < actions.size() && a > beta) {
                        controllerFils = (Controller) controller.clone();
                        actions.get(i).apply(controllerFils);
                        temp = alphaBeta(controllerFils, a, beta, n + 1);
                        temp = temp + 1;

                        //Ordre inverse du poly
                        if (a > temp) {
                            a = temp;

                            if (n == 0) {
                                //listAction.set(n,i);
                                action = i;
                                this.memoriseAction(actions.get(i));
                            }
                        }
                        i++;
                    }
                    return a;
                } else {
                    //Noeud Min
                    b = beta;
                    while (i < actions.size() && alpha > b) {
                        controllerFils = (Controller) controller.clone();
                        actions.get(i).apply(controllerFils);
                        temp = alphaBeta(controllerFils, alpha, b, n + 1);
                        //Ordre inverse du poly
                        if (b < temp) {
                            b = temp;
                            if (n == 0) {
                                //listAction.set(n,i);
                                action = i;
                                this.memoriseAction(actions.get(i));
                            }
                        }
                        i++;
                    }
                    return b;
                }
            }
        }
        else {
            //Noeud terminal
            //return heuristic(partie, partie.getBateauSelectionne().getPosition());
            return heuristicV2(partie, partie.getBateauSelectionne().getPosition());
        }
    }

    //fonction calculant l'heuristique de la case c passer en parametre
    public int heuristic(Partie p, Case c){
        int h =-1 ;
        int i;
        ArrayList<Case> posPhare = RechercheEltIA.researchLighthouse(p);
        int d;
        int dAdv;
        int temp = 1000;
        int lenPosPhare;
        lenPosPhare = posPhare.size();

        for(i = 0 ; i<lenPosPhare; i++){
            d = getDistance(c, posPhare.get(i),p);
            if (h<0 || d<h){
                h = d;
                phare = i;
            }
            else if (d==h){
                if(phare != i) {
                    h=d + 1;
                }
            }
        }
        //On renvoie la  plus petite distance obtenue
        return h;
    }

    //Amelioration de la fonction heuristique afin de ne pas aller prendre un phare si l'adversaire va pouvoir le reprendre les tours qui suivent
    public int heuristicV2(Partie p, Case c){
        int pharePlusProcheAdv = -1;
        int res;
        int i;
        ArrayList<Integer> distanceMoi = new ArrayList<>();
        ArrayList<Integer> distanceAdv = new ArrayList<>();
        ArrayList<Case> posPhare = RechercheEltIA.researchLighthouse(p);
        Case caseBateauAdv = p.getJoueur2().getBoats().get(0).getPosition();

        int d;
        int dAdv;
        int temp = 1000;
        int lenPosPhare;
        lenPosPhare = posPhare.size();

        for(i = 0 ; i<lenPosPhare; i++){
            d = getDistance(c, posPhare.get(i),p);
            dAdv = getDistance(caseBateauAdv,posPhare.get(i),p);

            if(distanceAdv.size() == 0) {
                pharePlusProcheAdv = i;
            }
            else{
                if (Collections.min(distanceAdv) > dAdv) {
                    pharePlusProcheAdv = i;
                }
            }
            //On conserve la distance vers tout les phare pour notre bateau et celui de l'adversaire
            distanceAdv.add(dAdv);
            distanceMoi.add(d);
        }

        if(lenPosPhare > 1) {
            if (distanceAdv.get(pharePlusProcheAdv) > distanceMoi.get(pharePlusProcheAdv)) {
                //On supprime cette possibilité car l'adversaire reprendra le phares dans les tours suivant
                distanceMoi.remove(distanceMoi.get(pharePlusProcheAdv));
            }

        }

        //On retourne la distance minimal
        if(posPhare.size()!=0) {
            if (distanceMoi.size() == 0) {
                return distanceMoi.get(0);
            } else {
                return Collections.min(distanceMoi);
            }
        }
        //On possède deja tout les phares on renvoie -1 pour montrer une erreur
        return -1;
    }


    @SuppressWarnings("Duplicates")
    // Renvoie le nombre de cases séparant deux cases
    public int getDistance(Case c1, Case c2,Partie partie){
        int d = 0;
        int x1 = c1.getX();
        int x2 = c2.getX();
        int y1 = c1.getY();
        int y2 = c2.getY();
        while (x1 != x2 || y1 != y2){
            if (y1 == y2) {             //Les deux cases sont sur la même colonne
                if (x1 < x2) {
                    x1 = x1 + 1;
                    d = d + 1;
                } else {
                    x1 = x1 - 1;
                    d = d + 1;
                }
            }
            else{                      //Les cases ne sont pas sur la même colonne
                if (x1 == x2){
                    if (y1 < y2) {
                        y1 = y1 + 1;
                        d = d + 1;
                    } else {
                        y1 = y1 - 1;
                        d = d + 1;
                    }
                }
                else{                           //Les deux cases ne sont ni sur la meme ligne ni sur la meme colonne
                    if (x1 < x2 && y1 < y2 ){   //La case 2 est au nord est de l'autre
                        if (y1%2 ==0){          //Colonne pair
                            y1 = y1+1;
                            d = d + 1;
                        }
                        else{
                            y1 = y1+1;
                            x1 = x1+1;
                            d = d + 1;
                        }
                    }
                    else if(x1 < x2 && y1 > y2){    //La case 2 est au nord ouest de l'autre
                        if (y1%2 == 0){
                            y1 = y1-1;
                            d = d+1;
                        }
                        else {
                            y1 = y1-1;
                            x1 = x1+1;
                            d=d+1;
                        }
                    }
                    else if(x1 > x2 && y1 < y2){    //La case 2 est au sud est de l'autre
                        if (y1%2 ==0){
                            y1 = y1+1;
                            x1 = x1-1;
                            d = d+1;
                        }
                        else{
                            y1 = y1+1;
                            d = d+1;
                        }
                    }
                    else {                          //La case 2 est au sud ouest de l'autre
                        if(y1%2 ==0){
                            y1=y1-1;
                            x1=x1-1;
                            d=d+1;
                        }
                        else{
                            y1=y1-1;
                            d=d+1;
                        }
                    }
                }
            }
            if(partie.getMap().getCase(x1,y1) instanceof CaseTerre){
                d = d + 1;
            }
        }

        return d;
    }
}
