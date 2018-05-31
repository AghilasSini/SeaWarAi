package fr.lesprogbretons.seawar.ia.ia_seawar_f;

import fr.lesprogbretons.seawar.controller.Controller;
import fr.lesprogbretons.seawar.model.actions.*;
import fr.lesprogbretons.seawar.model.cases.Case;

import java.util.ArrayList;
import java.util.stream.IntStream;

import static fr.lesprogbretons.seawar.SeaWar.logger;

public class Max extends Node {

    public Max(Controller controller, Action action, int etage) {

        super(controller, action);

        //Si on est sur un phare, pas besoin de générer les fils
        boolean isOnLight = false;
        boolean noPass = false;
        for (Case aCase : getPharesAvaillable()) {
            if (controller.getPartie().getBateauSelectionne().getPosition() == aCase) {
                if (myAction != null) {
                    isOnLight = true;
                    logger.debug("Sur un phare : on élague");
                } else {
                    noPass = true;
                }
            }
        }

        if (etage != 0 && !isOnLight) {

            //On récupère les actions
            ArrayList<Action> myActions = (ArrayList<Action>) controller.getPossibleActions();

            //Purge actions attaque
            for (Action a : new ArrayList<>(myActions)) {
                if (a instanceof Attack) {
                    myActions.remove(a);
                }
            }

            //Get change cannon action
            ChangeCannon cc = null;
            for (Action a : myActions) {
                if (a instanceof ChangeCannon) cc = (ChangeCannon) a;
            }
            //Remove from list
            if (cc != null) {
                myActions.remove(cc);
            }

            ArrayList<Attack> myAttacks = new ArrayList<>();
            ArrayList<AttackAndMove> myAttacksAndMove = new ArrayList<>();

            //Spécial pour le tir canon principal
            ArrayList<Case> boatInRange = getBoatInRange(c.getPartie().getBateauSelectionne(),
                    c.getPartie().getOtherPlayer());
            if (!boatInRange.isEmpty() && c.getPartie().getBateauSelectionne().canShoot()) {
                for (Case target : boatInRange) {
                    myAttacks.add(new Attack(c.getPartie().getBateauSelectionne(), target));
                }
            }

            //To be sure
            cc = new ChangeCannon(c.getPartie().getBateauSelectionne());
            cc.apply(controller);

            boatInRange = getBoatInRange(c.getPartie().getBateauSelectionne(),
                    c.getPartie().getOtherPlayer());
            if (!boatInRange.isEmpty() && c.getPartie().getBateauSelectionne().canShoot()) {
                for (Case target : boatInRange) {
                    myAttacks.add(new SecondaryAttack(c.getPartie().getBateauSelectionne(), target));
                }
            }

            //Rechange Cannon
            cc.apply(controller);

            for (Action a : new ArrayList<>(myActions)) {
                if (a instanceof MoveBoat) {
                    //Si on bouge alors pour chaque tir différent
                    for (Attack at : myAttacks) {
                        if (at instanceof SecondaryAttack) {
                            myAttacksAndMove.add(new SecondaryAttackAndMove((SecondaryAttack) at, (MoveBoat) a));
                        } else myAttacksAndMove.add(new AttackAndMove(at, (MoveBoat) a));
                    }
                }
            }

            logger.error("!!!!! J'ai " + myAttacksAndMove.size() + " possibilités d'attaque !!!!!");
            for (AttackAndMove a : myAttacksAndMove) {
                myActions.remove(a.getMove());
            }

            myActions.addAll(myAttacksAndMove);

            if (noPass) {
                for (Action a : new ArrayList<>(myActions)) {
                    if (a instanceof PassTurn && myActions.size() > 1) {
                        myActions.remove(a);
                    }
                }
            }

            if (myActions.size() == 1 && myActions.get(0) instanceof PassTurn) {
                bestNode = this;
                myAction = myActions.get(0);
            } else {

                //Pour chaque action de la liste
                for (Action a : myActions) {
                    //Clone controller
                    Controller myNewController = (Controller) controller.clone();
                    //Apply action to cloned controller
                    a.apply(myNewController);
                    //End turn
                    myNewController.endTurn();
                    //Create new Min node
                    //Min m = new Min(myNewController, a, etage - 1);
                    Max m = new Max(myNewController, a, etage - 1);
                    fils.add(m);
                }
            }
        }
    }

    @Override
    public int alphabeta(int alpha, int beta) {
        if (getFils().size() == 0) {
            updateHeuristic2();
            return heuristic;
        } else {

            for (Node fils : getFils()) {
                int finalAlpha = alpha;
                int sonHeuristic = fils.alphabeta(alpha, beta);

                if (fils.getMyAction() instanceof PassTurn) {
                    sonHeuristic += 1000;
                }

                IntStream.of(alpha, sonHeuristic).min().ifPresent(minAlpha -> {
                    if (minAlpha < finalAlpha) {
                        bestNode = fils;
                    }
                });

                alpha = Math.min(alpha, sonHeuristic);
                heuristic = alpha;

                if (alpha == 0) {

                    break;
                }
            }

            return alpha + 10;
        }
    }
}
