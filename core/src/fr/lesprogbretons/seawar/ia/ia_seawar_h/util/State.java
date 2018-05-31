package fr.lesprogbretons.seawar.ia.ia_seawar_h.util;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import fr.lesprogbretons.seawar.controller.Controller;
import fr.lesprogbretons.seawar.model.Partie;
import fr.lesprogbretons.seawar.model.Player;
import fr.lesprogbretons.seawar.model.actions.*;
import fr.lesprogbretons.seawar.model.boat.Boat;
import fr.lesprogbretons.seawar.model.cases.Case;
import fr.lesprogbretons.seawar.model.cases.CaseEau;
import fr.lesprogbretons.seawar.model.map.Grille;

import java.util.ArrayList;
import fr.lesprogbretons.seawar.ia.ia_seawar_h.IA;


public class State {
    private String nameIA;
    private String nameToPlay;
    private Controller controller;
    private int n;
    private boolean isTerminal;
    private ArrayList<State> fils;
    private double beta;
    private double alpha;
    private double utilite;
    private boolean isRacine = false;
    private Action actionToDo;

    private static ArrayList<Case> phares;

    private final int coefDist = 1;
    private final int coefPrise = 300;
    private final int coefAttack = 600;


    private static double hIA;
    private static double hOther;

    public State(int n, boolean t, String name, Action action, Controller controller){
        this.nameIA = name;
        this.n = n;
        this.actionToDo = action;
        this.controller = controller;
        fils = new ArrayList<State>();
        isTerminal = t;
        calcUtilite();
    }

    public State(String name, Controller controller){
        this.nameIA = name;
        this.n = 0;
        this.controller=controller;
        fils = new ArrayList<State>();
        isTerminal = false;
        alpha = -999;
        beta = 999;
        isRacine = true;
    }


    private double heuristique() {
        ArrayList<Case> phares = getPhares();
        Case position = controller.getPartie().getBateauSelectionne().getPosition();
        Case positionOther = controller.getPartie().getOtherPlayer().getBoats().get(0).getPosition();
        double [] distances = new double [phares.size()];
        for(int i = 0; i<distances.length ;i++){
            distances[i] = 999;
        }
        Case c;
        int x, y, index = 0;
        double dist;
        while (index < phares.size()) {
            c = phares.get(index);
            //Si le phare en nous appartient pas
            if (c.getPossedePhare() == null || c.getPossedePhare().getName() != nameIA) {
                x = position.getX() - c.getX();
                y = position.getY() - c.getY();
                dist = Math.sqrt(x * x + y * y);
                distances[index] = dist;
            } else {
                //Si on est sur le phare
                if (c.getX() == position.getX() && c.getY() == position.getY()){
                    distances[index] = 0.1;
                }
            }
            index++;
        }

        double minDist = 999;
        for(double d : distances){
            if(d<minDist){
                minDist = d;
            }
        }
        return minDist;
    }

    //Distance à l'ennemi
    private double heuristique2() {
        Case position = controller.getPartie().getBateauSelectionne().getPosition();
        Case positionOther = controller.getPartie().getOtherPlayer().getBoats().get(0).getPosition();
        double minDist;
        int x = position.getX() - positionOther.getX();
        int y = position.getY() - positionOther.getY();
        minDist = Math.sqrt(x * x + y * y);
        return minDist;
    }


    private void calcUtilite(){
        int nbPharePossedes=0, nbPhareAdverses=0;
        Player ia = getIA(controller.getPartie(), nameIA);
        Player player = getOther(controller.getPartie(), nameIA);
        double utilitePrise = 0;
        double utiliteAttack = 0;
        double utilitePass = 0;
        if(isTerminal){
            if(actionToDo instanceof MoveBoat) {
                nbPharePossedes = ia.getPharesPossedes();
                nbPhareAdverses = player.getPharesPossedes();
                utilitePrise = (Math.abs(nbPharePossedes- nbPhareAdverses));
            }else if(actionToDo instanceof Attack){
                utiliteAttack = 500;
            }else{
                    utilitePass = 0;
                }
        utilite = utilitePrise*coefPrise + utiliteAttack*coefAttack +  utilitePass;

        }
    }


    private ArrayList<Case> getPhares(){
        Case[][] cases = controller.getPartie().getMap().getCases();
        ArrayList<Case>phares = new ArrayList<Case>();
        int lignes = cases.length;
        int colonnes = cases[0].length;
        for(int i=0; i<lignes;i++) {
            for (int j = 0; j < colonnes; j++) {
                if (cases[i][j].isPhare()) {
                    phares.add(cases[i][j]);

                }
            }
        }
        return phares;
    }

    public double getBeta() {
        return beta;
    }

    public double getAlpha() {
        return alpha;
    }

    public double alphabeta(double a, double b){
        int index=0;
        State fi = null;
        alpha = a;
        beta = b;
        double ab;
        if(isTerminal){
            return utilite;
        }else{
            if(isMax()){
                while(a<b && index<fils.size()){
                    fi = fils.get(index);
                    ab = fi.alphabeta(alpha,beta);
                    if(alpha<ab){
                        alpha = ab;
                        if(isRacine){
                            actionToDo = fi.getActionToDo();
                        }

                    }
                    index++;
                }
                return alpha;
            }else{
                while(a<b && index<fils.size()){
                    fi = fils.get(index);
                    ab = fi.alphabeta(alpha,beta);
                    if(beta>ab){
                        beta = ab;
                        if(isRacine){
                            actionToDo = fi.getActionToDo();
                        }

                    }
                    index++;
                }
                return beta;
            }
        }
    }

    public Action getActionToDo() {
        return actionToDo;
    }

    public void createArbre(int prof){
        Controller controller1, controllerCopie;
        State fi;
        State fiToDevelop = null;
        double heuristique = 9999;
        double fiH = 9999;
        Action aCopie;
        ArrayList<Action> actions;
        boolean isTerminal = false;
        if(prof>0){
            if(prof==1){
                isTerminal = true;
            }
            actions = (ArrayList)controller.getPossibleActions();
            //Pour prise de phare
            for(Action a : actions){
                if(a instanceof MoveBoat || a instanceof Attack){
                    controllerCopie = (Controller)controller.clone();
                    aCopie = (Action)a.clone();
                    aCopie.apply(controllerCopie);
                    controllerCopie.endTurn();
                    //controllerCopie.getPartie().setBateauSelectionne(controllerCopie.getPartie().getCurrentPlayer().getBoats().get(0));

                    fi = new State(n+1, isTerminal, nameIA, aCopie,controllerCopie);
                    if(a instanceof MoveBoat){
                        fiH = fi.heuristique();
                        if(fiH<heuristique){
                            heuristique = fiH;
                            fiToDevelop = fi;
                        }
                    }else{
                        fils.add(fi);
                    }

                }

            }

            if(fiToDevelop!=null){
                fiToDevelop.createArbre(prof-1);
                fils.add(fiToDevelop);
            }
            //Pour destruction ennemi
            for(Action a : actions){
                if(a instanceof MoveBoat){
                    controllerCopie = (Controller)controller.clone();
                    aCopie = (Action)a.clone();
                    aCopie.apply(controllerCopie);
                    controllerCopie.endTurn();
                    fi = new State(n+1, isTerminal, nameIA, aCopie,controllerCopie);
                    fiH = fi.heuristique2();
                    if(fiH<heuristique){
                        heuristique = fiH;
                        fiToDevelop = fi;
                    }
                }
            }
            if(fiToDevelop!=null){
                fiToDevelop.createArbre(prof-1);
                fils.add(fiToDevelop);
            }

        }
    }






    public Player getIA(Partie p, String name){
        if(p.getJoueur1().getName().equals(name)){
            return p.getJoueur1();
        }else{
            return p.getJoueur2();
        }
    }

    public Player getOther(Partie p, String name){
        if(!(p.getJoueur1().getName().equals(name))){
            return p.getJoueur1();
        }else{
            return p.getJoueur2();
        }
    }

    public boolean isMax(){
        return n%2==0;
    }

    public boolean isTerminal(){
        return isTerminal;
    }

    public String toString(){
        String str = "*******************\n";
        str+="n="+n+"\n";
        str+="alpha="+alpha+"\n";
        str+="beta="+beta+"\n";
        str+="isTerminal="+isTerminal+"\n";
        str+="utilite="+utilite+"\n";
        str+="position Actuelle"+getIA(controller.getPartie(), nameIA).getBoats().get(0).getPosition().getX()+","+getIA(controller.getPartie(), nameIA).getBoats().get(0).getPosition().getY()+"\n";
        str+="action="+actionToDo;
        if(actionToDo instanceof Move){
            str+="target :"+((Move)actionToDo).getTarget().getX()+","+((Move)actionToDo).getTarget().getY()+"\n";
        }else{
            str+="\n";
        }

        str+="---------------------\n";
        for(State s : fils){
            str+="pere="+n+"\n";
            str+=s.toString();
        }
        return str;
    }
}
