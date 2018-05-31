package fr.lesprogbretons.seawar.ia.ia_seawar_k;

import fr.lesprogbretons.seawar.controller.Controller;
import fr.lesprogbretons.seawar.ia.AbstractIA;
import fr.lesprogbretons.seawar.model.Partie;
import fr.lesprogbretons.seawar.model.Player;
import fr.lesprogbretons.seawar.model.actions.Action;
import fr.lesprogbretons.seawar.model.actions.MoveBoat;
import fr.lesprogbretons.seawar.model.actions.PassTurn;
import fr.lesprogbretons.seawar.model.boat.Amiral;
import fr.lesprogbretons.seawar.model.boat.Boat;
import fr.lesprogbretons.seawar.model.cases.*;

import java.util.ArrayList;
import java.util.List;

public class IA_K extends AbstractIA {
    private Case phareCible = new CaseEau(0,0);

    public IA_K(int identifier) {
        super(identifier,"IA_K");
    }


    public IA_K(int identifier,String name,ArrayList<Boat> boats) {
        // TODO Auto-generated constructor stub
        super(identifier,name,boats);
    }

    public Action chooseAction(Controller controller) {
        double m = 1000;
        int n = 2;
        double dmin = 1000;
        for (int i = 0; i < controller.getPartie().getMap().getHauteur(); i++) {
            for (int j = 0; j < controller.getPartie().getMap().getLargeur(); j++) {
                if ((controller.getPartie().getMap().getCase(i, j).isPhare())) {
                    if ((controller.getPartie().getMap().getCase(i, j).getPossedePhare() == null) || (controller.getPartie().getMap().getCase(i, j).getPossedePhare().getNumber() != controller.getPartie().getCurrentPlayer().getNumber())) {
                        double d = Math.pow((i - controller.getPartie().getBateauSelectionne().getPosition().getX()),2) + Math.pow((j - controller.getPartie().getBateauSelectionne().getPosition().getY()),2);
                        d = Math.sqrt(d);
                        if (d < dmin) {
                            dmin = d;
                            phareCible.setX(i);
                            phareCible.setY(j);
                        }
                    }
                }
            }
        }
        alphabeta(n,controller,m);
        return this.getMemorizedAction();
    }

    public double alphabeta(int n, Controller controller, double m){
        if(n==0){
            double d = Math.pow((phareCible.getY() - controller.getPartie().getBateauSelectionne().getPosition().getY()),2) + Math.pow((phareCible.getX() - controller.getPartie().getBateauSelectionne().getPosition().getX()),2);
            d = Math.sqrt(d);
            return d;
        } else {
            List<Action> actionsPossibles = controller.getPossibleActions();
            double oldm = m;
            for(Action action : actionsPossibles) {
                Controller c = (Controller) controller.clone();
                Action a = (Action) action.clone();
                a.apply(c);
                m = Math.min(m,alphabeta(n-1,c,m));
                if(m < oldm){
                    oldm = m;
                    if(!(action instanceof PassTurn)){
                        this.memoriseAction(action);
                    }
                }
            }
            return m;
        }
    }
}
