package fr.lesprogbretons.seawar.ia.ia_seawar_l;

import fr.lesprogbretons.seawar.controller.Controller;
import fr.lesprogbretons.seawar.ia.AbstractIA;
import fr.lesprogbretons.seawar.model.Partie;
import fr.lesprogbretons.seawar.model.Player;
import fr.lesprogbretons.seawar.model.actions.Action;
import fr.lesprogbretons.seawar.model.actions.MoveBoat;
import fr.lesprogbretons.seawar.model.actions.PassTurn;
import fr.lesprogbretons.seawar.model.boat.Boat;
import fr.lesprogbretons.seawar.model.cases.Case;
import fr.lesprogbretons.seawar.model.map.Grille;


import java.util.ArrayList;
import java.util.List;

public class IAAdvanced extends AbstractIA {

    private List<Action> listeAction;

    private int actionIndex;

    public IAAdvanced(int number) {
        super(number,"IAAdvanced");
    }

    public IAAdvanced(int number, String name, List<Boat> boats) {
        super(number, name, boats);
    }

    /*public int alphabeta(Controller c, int alpha, int beta, int n){
        int alpha2;
        int beta2;
        int truc;
        int j=0;
        if(n > 1){
            return (int)heuristique(c);
        }
        else{
            if(c.getPartie().getCurrentPlayer().getNumber() == c.getPartie().getJoueur1().getNumber()){
                listeAction = c.getPossibleActions();
                while( (alpha<beta) && (j<listeAction.size()) ){
                    Controller c2 = (Controller) c.clone();
                    listeAction.get(j).apply(c2);
                    alpha2 = alpha;
                    truc= alphabeta(c2,alpha,beta,n + 1);
                    if(alpha2 > truc){
                        alpha= truc;
                        if(n == 0) {
                            System.out.println("This is the action"+ j);
                            this.actionIndex = j;
                        }
                    }
                    j++;
                }
                return alpha;
            }
            else{
                listeAction = c.getPossibleActions();
                j=0;
                while( (alpha<beta) && (j<listeAction.size()) ){
                    Controller c2 = (Controller) c.clone();
                    listeAction.get(j).apply(c2);
                    beta2 = beta;
                    truc = alphabeta(c2,alpha,beta, n+ 1);
                    if(beta2 > truc){
                        beta= truc;
                    }
                    else{
                        beta=beta2;
                    }
                    j++;
                }
                return beta;
            }
        }
    }*/

    public float alphabeta(Controller c, float alpha, float beta, int n) {
        float tempAlpha;
        int bestIndex = -1;
        if (n > 0) {
            float test = heuristic(c.getPartie());
            System.out.println(test);
            return test;
        } else if (c.getPartie().getCurrentPlayer().getNumber() == c.getPartie().getJoueur1().getNumber()) {
            listeAction = c.getPossibleActions();
            for(Action action : listeAction) {
                if(!(action instanceof PassTurn)) {
                    bestIndex++;
                    Controller controllerTemp = (Controller) c.clone();
                    System.out.println(controllerTemp.getPartie().getJoueur1().getBoats().get(0).getPosition().getX());
                    System.out.println(controllerTemp.getPartie().getJoueur1().getBoats().get(0).getPosition().getY());
                    action.apply(controllerTemp);
                    System.out.println(controllerTemp.getPartie().getJoueur1().getBoats().get(0).getPosition().getX());
                    System.out.println(controllerTemp.getPartie().getJoueur1().getBoats().get(0).getPosition().getY());
                    tempAlpha = alphabeta(controllerTemp, alpha, beta, n + 1);
                    if (alpha > tempAlpha) {
                        System.out.println("alpha : " + alpha + " tempAlpha : " + tempAlpha);
                        alpha = tempAlpha;
                        if (n == 0) {
                            actionIndex = bestIndex;
                        }
                    }
                }
            }
            System.out.println("fin test");
            return alpha;
        }
        return 0;
    }

    @Override
    public Action chooseAction(Controller controller) {
        List<Action> indexAct = controller.getPossibleActions();
        alphabeta(controller,10000,-10000,0);
        System.out.println("'''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''" +
                "''''''''''''''''''' L'ACTION : " + indexAct.get(actionIndex).toString());
        return indexAct.get(actionIndex);
    }

    public float heuristic(Partie p){
        float heuritic = -1;
        float distance = 0;
        float distanceO = 0;
        float magic;
                List<Case> listPhares = p.loadListPhares();
        System.out.println("IL Y A "+ listPhares + " PHARES !!!!!!!!!!!!!!");
        for(Case casePhare : listPhares){
            distance = distance(p.getBateauSelectionne().getPosition(),casePhare);
            distanceO = distance(p.getOtherPlayer().getBoats().get(0).getPosition(),casePhare);
            if(p.getBateauSelectionne().getPosition().isPhare()){
                heuritic = -1000;
            }
            magic = (float)0.2*(distanceO-distance);
            distance = distance - magic;
            if(heuritic == -1 || distance < heuritic) {
                heuritic = distance;
            }
        }
        return heuritic;
    }

    public float distance(Case cp, Case c){ // retourne distance au carré
        int xp = cp .getX() ;
        int yp = cp .getY() ;
        int xc = c.getX() ;
        int yc = c.getY() ;
        return  hexDistance(xp,yp,xc,yc);

    }


   public int hexDistance(int x1,int y1,int x2,int y2){
        if(x1 == x2){
            return Math.abs(y2 - y1);
        } else if(y1 == y2){
            return Math.abs(x2 - x1);
        } else {
            int dx = Math.abs(x2 - x1);
            int dy = Math.abs(y2 - y1);
            if (y1 < y2){
                return dx + dy - (int)Math.ceil(dx /2);
            } else {
                return dx + dy - (int)Math.floor(dx /2);
            }
        }
   }

}
