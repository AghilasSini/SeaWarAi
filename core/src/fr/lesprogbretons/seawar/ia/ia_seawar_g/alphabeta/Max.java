package fr.lesprogbretons.seawar.ia.ia_seawar_g.alphabeta;


import fr.lesprogbretons.seawar.ia.ia_seawar_g.etat.Etat;
import fr.lesprogbretons.seawar.model.actions.Action;
import fr.lesprogbretons.seawar.model.actions.PassTurn;

import java.util.HashSet;
import java.util.List;

public class Max extends Noeud {

    public Max(Etat etat){
        super(etat);
    }


    @Override
    public void genererFils() {

        List<Action> actions = etat.getPossibleActions();
        if(actions!=null) {
            for (Action action : actions) {
                Noeud nextAction;
                if(action instanceof PassTurn) { // Marche dans l'hypothèse qu'il n'y a qu'un seul bateau
                    if (((PassTurn) action).getBoat().getMove() != ((PassTurn) action).getBoat().getMoveAvailable()) {
                        nextAction = new Min(etat.clone());
                        nextAction.setAction(action);
                        action.apply(nextAction.getEtat().getController());
                        nextAction.getEtat().getController().endTurn();
                        fils.add(nextAction);
                    } //Sinon on ne fait pas l'action de passTurn
                } else {
                    nextAction = new Min(etat.clone()); //Mettre à Max si plusieurs action possible par tour
                    nextAction.setAction(action);
                    action.apply(nextAction.getEtat().getController());
                    nextAction.getEtat().getController().endTurn(); // Retirer le end Turn si plusieurs actions possible
                    fils.add(nextAction);
                }
            }
        } else {
            System.out.println("Aucunes actions possibles ?");
        }
    }


    public void genererRacine() {
        List<Action> actions = etat.getPossibleActions();//getActionsPossible();
        fils = new HashSet<Noeud>();
        if (actions != null) {
            for (Action action : actions) {
                Noeud nextAction;
                if (!(action instanceof PassTurn)) { // Marche dans l'hypothèse qu'il n'y a qu'un seul bateau
                    nextAction = new Max(etat.clone());
                    nextAction.setAction(action);
                    action.apply(nextAction.getEtat().getController());
                    nextAction.getEtat().getController().endTurn();
                    fils.add(nextAction);
                }
            }
        }
    }



    /**
     * Algorithme AlphaBeta
     * @param alpha
     * @param beta
     * @return un entier
     */
    @Override
    public int alphabeta(int alpha, int beta) {
        if(fils == null || fils.isEmpty()) {
            val=utilite();
            return val;
        } else {

            for (Noeud noeud : fils) {
                int filsVal = noeud.alphabeta(alpha, beta);
                if(alpha < filsVal) {
                    alpha = filsVal;
                    bestNoeud = noeud;
                }
                if (beta <= alpha) break;
            }


            //Si l'utilite de ce noeud est superieur a alpha, on retourne l'utilite.
            //Cela permet a l'ia de faire un coup qui satisfait mieux l'heuristique
            //qu'un coup trouve la fin de l'algorithme qui donnerais un moins bon resultat,
            //mais qui serait moins bon que le coup actuel.

            //Si l'utilite du noeud vaut Integer.MIN_VALUE, c'est le pire cas possible,
            //car alors la defaite est assure. Ainsi, l'IA abandonne cette branche si une defaite a lieu a ce tour
            //elle n'essaie donc plus de rattraper une defaire, et ce bat avant

            int u=utilite();
            if(u>alpha || u==Integer.MIN_VALUE){
                val=u;
                return u;
            }else {
                val = alpha;
                return alpha;
            }
        }
    }

    @Override
    public Noeud getBestNoeud() {
        return bestNoeud;
    }




}
