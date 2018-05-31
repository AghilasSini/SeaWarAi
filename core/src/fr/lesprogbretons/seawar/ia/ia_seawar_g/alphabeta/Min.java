package fr.lesprogbretons.seawar.ia.ia_seawar_g.alphabeta;


import fr.lesprogbretons.seawar.ia.ia_seawar_g.etat.Etat;
import fr.lesprogbretons.seawar.model.actions.Action;
import fr.lesprogbretons.seawar.model.actions.PassTurn;

import java.util.List;

public class Min extends Noeud {

    public Min(Etat etat){
        super(etat);
    }

    @Override
    public void genererFils() {
        List<Action> actions = etat.getPossibleActions();//getActionsPossible();
        if(actions!=null) {
            for (Action action : actions) {
                Noeud nextAction;
                if(action instanceof PassTurn) { // Marche dans l'hypothèse qu'il n'y a qu'un seul bateau
                    if (((PassTurn) action).getBoat().getMove() != ((PassTurn) action).getBoat().getMoveAvailable()) {
                        nextAction = new Max(etat.clone());
                        nextAction.setAction(action);
                        action.apply(nextAction.getEtat().getController());
                        nextAction.getEtat().getController().endTurn();
                        fils.add(nextAction);
                    } //Sinon on ne fait pas l'action de passTurn
                } else {
                    nextAction = new Max(etat.clone());//Mettre à Min si plusieurs actions par tour
                    nextAction.setAction(action);
                    action.apply(nextAction.getEtat().getController());
                    nextAction.getEtat().getController().endTurn();// retirer endTurn si plusieurs actions par tour
                    fils.add(nextAction);
                }
            }
        } else {
            System.out.println("Aucunes actions possibles ?");
        }
    }




    @Override
    public int alphabeta(int alpha, int beta) {

        if(fils == null || fils.isEmpty()) {
            val=utilite();
            //System.out.println("min "+etat.getController().getPartie().getCurrentPlayer().getNumber()+" "+val);
            return val;
        } else {
            //System.out.println("else min ");
            for (Noeud noeud : fils) {
                int filsVal = noeud.alphabeta(alpha, beta);
                if (beta > filsVal) {
                    beta = filsVal;
                    bestNoeud = noeud;
                }
                if (beta <= alpha) break;
            }
            val=beta;
            return beta;
        }
    }

    @Override
    public Noeud getBestNoeud() {
        return bestNoeud;
    }

}
