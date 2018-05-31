package fr.lesprogbretons.seawar.ia.ia_seawar_f;

import fr.lesprogbretons.seawar.controller.Controller;
import fr.lesprogbretons.seawar.model.actions.Action;
import fr.lesprogbretons.seawar.model.cases.Case;

import java.util.ArrayList;
import java.util.stream.IntStream;

import static fr.lesprogbretons.seawar.SeaWar.logger;

public class Min extends Node {


    public Min(Controller controller, Action action, int etage) {
        super(controller, action);

        if (etage != 0) {
            //Select other boat
            Case aCase = controller.getPartie().getJoueur2().getBoats().get(0).getPosition();
            controller.selection(aCase.getX(), aCase.getY());

            logger.error(controller.getPartie().getBateauSelectionne().getJoueur().getName());

            //On récupère les actions
            ArrayList<Action> myActions = (ArrayList<Action>) controller.getPossibleActions();

            //Pour chaque action de la liste
            for (Action a : myActions) {
                //Clone controller
                Controller myNewController = (Controller) controller.clone();
                //Apply action to cloned controller
                a.apply(myNewController);
                //End turn
                myNewController.endTurn();
                //Create new Min node
                Max m = new Max(myNewController, a, etage - 1);
                fils.add(m);
            }
        }
    }

    @Override
    public int alphabeta(int alpha, int beta) {
        if (getFils().size() == 0) {
            updateHeuristic();
            return heuristic;
        } else {
            for (Node fils : getFils()) {
                int finalBeta = beta;
                IntStream.of(alpha, fils.alphabeta(alpha, beta)).min().ifPresent(minBeta -> {
                    if (minBeta != finalBeta) {
                        bestNode = fils;
                    }
                });
                beta = Math.min(beta, fils.alphabeta(alpha, beta));
                if (alpha >= beta) {
                    break;
                }
            }
            return beta;
        }
    }
}
