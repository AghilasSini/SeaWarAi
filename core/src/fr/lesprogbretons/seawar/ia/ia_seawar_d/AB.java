package fr.lesprogbretons.seawar.ia.ia_seawar_d;

import fr.lesprogbretons.seawar.controller.Controller;
import fr.lesprogbretons.seawar.ia.AbstractIA;
import fr.lesprogbretons.seawar.model.Player;
import fr.lesprogbretons.seawar.model.actions.Action;
import fr.lesprogbretons.seawar.model.actions.MoveBoat;
import fr.lesprogbretons.seawar.model.actions.Attack;
import fr.lesprogbretons.seawar.model.boat.Boat;
import fr.lesprogbretons.seawar.model.cases.Case;

import java.util.ArrayList;
import java.util.List;

public class AB extends AbstractIA {

    private Player current_turn_player;
    private ArrayList<Case> phares;

    public AB(int identifier) {
        super(identifier, "alpha_beta");
    }

    public AB(int identifier, String name, ArrayList<Boat> boats) {
        // TODO Auto-generated constructor stub
        super(identifier, name, boats);
    }

    // sélection de l'heuristique
    private double heuristique(Controller controller) {
        return heuristique2(controller); // heuristique à régler ici !
    }

    private void initPhares(Controller controller) {
        Case[][] cas = controller.getPartie().getMap().getCases();
        phares = new ArrayList();
        for (Case[] tab : cas) {
            for (Case c : tab) {
                if (c.isPhare()) {
                    phares.add(c);
                }
            }
        }
    }

    public Action chooseAction(Controller controller) {
        List<Action> possible_actions = controller.getPossibleActions();
        Action best_action = possible_actions.get(0);
        int max_depth = 3; // profondeur à régler ici !
        double scoreTmp = 999, scoreActuel = 999;
        initPhares(controller); // on trouve les phares une seule fois pour la même sélection d'action, comme ça l'heuristique est plus rapide
        // AB : scoreactuel =-999
        // Prendre - distance
        //System.out.println("Je suis " + controller.getPartie().getCurrentPlayer().getBoats().get(0).getPosition().getX()+" ; "+controller.getPartie().getCurrentPlayer().getBoats().get(0).getPosition().getX());


        for (Action action : possible_actions) {
            if (action instanceof MoveBoat || action instanceof Attack) {

                Controller controllerClone = (Controller) controller.clone();
                action.apply(controllerClone);
                // System.out.println(controllerClone.getPartie().getJoueur2()+" "+controllerClone.getPartie().getCurrentPlayer());
                //System.out.println("Je suis " + controllerClone.getPartie().getJoueur2().getBoats().get(0).getPosition().getX()+" ; "+controllerClone.getPartie().getJoueur2().getBoats().get(0).getPosition().getX());
                // System.out.println("Je suis " + controller.getPartie().getCurrentPlayer().getBoats().get(0).getPosition().getX()+" ; "+controller.getPartie().getCurrentPlayer().getBoats().get(0).getPosition().getX());

                scoreTmp = heuristique(controllerClone);
                scoreTmp = Min(controllerClone, scoreTmp, -99999, 99999, 2, max_depth);

                if (scoreActuel > scoreTmp) {
                    scoreActuel = scoreTmp;
                    best_action = action;
                    this.memoriseAction(best_action);
                }
            }
        }

        System.out.println("action choisie : " + best_action);
        return best_action;
    }

    double Max(Controller controller, double scoreParent, double alpha, double beta, int depth, int max_depth) {
        double scoreTmp, scoreActuel = scoreParent;

        // debug joueur courant partie - Ne pas retirer !
        controller.getPartie().setCurrentPlayer(controller.getPartie().getOtherPlayer());

        if (depth > max_depth) {
            return scoreActuel;
        }

        List<Action> possible_actions = controller.getPossibleActions();

        for (Action action : possible_actions) {
            if (action instanceof MoveBoat || action instanceof Attack) {
                Controller controllerClone = (Controller) controller.clone();
                action.apply(controllerClone);

                scoreTmp = heuristique(controllerClone);
                scoreTmp = Min(controllerClone, scoreTmp, alpha, beta, depth+1, max_depth);

                if (scoreActuel > scoreTmp) {
                    scoreActuel = scoreTmp;
                }

                if (scoreActuel > beta) {
                    return scoreActuel;
                }

                alpha = max(beta, scoreTmp);
            }
        }
        return scoreActuel;
    }

    double Min(Controller controller, double scoreParent, double alpha, double beta, int depth, int max_depth) {
        double scoreTmp, scoreActuel = scoreParent;

        // debug joueur courant partie - Ne pas retirer !
        controller.getPartie().setCurrentPlayer(controller.getPartie().getOtherPlayer());

        if (depth > max_depth) {
            return scoreActuel;
        }

        List<Action> possible_actions = controller.getPossibleActions();

        for (Action action : possible_actions) {
            if (action instanceof MoveBoat || action instanceof Attack) {
                Controller controllerClone = (Controller) controller.clone();
                action.apply(controllerClone);

                scoreTmp = heuristique(controllerClone);
                scoreTmp = Max(controllerClone, scoreTmp, alpha, beta,depth+1, max_depth);

                if (scoreActuel < scoreTmp) {
                    scoreActuel = scoreTmp;
                }

                if (scoreActuel < alpha) {
                    return scoreActuel;
                }

                beta = min(beta, scoreTmp);
            }
        }
        return scoreActuel;
    }

    public int AB(Action N) {
        int scor = 1;
        Case pos;
        int temp;

        if (N instanceof MoveBoat) {
            pos = ((MoveBoat) N).getTarget();

            if (pos.isPhare()) {
                return 10; // UN PHARE EST LA
            }
        }
        return scor;

    }

    // min et max
    double max(double score1, double score2) {
        if (score1 > score2) {
            return score1;
        }
        return score2;
    }

    double min(double score1, double score2) {
        if (score1 < score2) {
            return score1;
        }
        return score2;
    }

    private double heuristique1(Controller controller) {
		/* HEURISTIQUE 1 : DISTANCE AU PHARE LE PLUS PRES */
        double minDist = 9999;
        double distance;
        for (Case c : phares) {
            int x = controller.getPartie().getBateauSelectionne().getPosition().getX() - c.getX();
            int y = controller.getPartie().getBateauSelectionne().getPosition().getY() - c.getY();

            distance = Math.sqrt(x * x + y * y);

            if (c.getPossedePhare() == null || c.getPossedePhare().getNumber() != controller.getPartie().getCurrentPlayer().getNumber()) {

                if (distance < minDist) {
                    minDist = distance;
                }
            //} else {
              //  if (distance == 0 ) {
               //     minDist = 0;
               // }
            }
        }
    return minDist;
    }

    private double heuristique2(Controller controller) {
        /* HEURISTIQUE 2 : DISTANCE AU PHARE LE PLUS PRES + POINT DE VIE DE L'ADVERSAIRE + NOMBRE PHARES POSSEDES */
        boolean estNotrePhare = false;
        double distance, minDist = 99, score = 9999; // distance min entre 0 et 12 (en moyenne)
        int nbPharesPossédés = 0; // entre 0 et 3
        int vieBateauEnnemi = controller.getPartie().getOtherPlayer().getBoats().get(0).getHp(); // entre 0 et 100


        for (Case c : phares) {
            estNotrePhare = c.getPossedePhare() != null && (c.getPossedePhare().getNumber() == controller.getPartie().getCurrentPlayer().getNumber());
            int x = controller.getPartie().getBateauSelectionne().getPosition().getX() - c.getX();
            int y = controller.getPartie().getBateauSelectionne().getPosition().getY() - c.getY();

            distance = Math.sqrt(x * x + y * y);

            if (c.getPossedePhare() == null || !estNotrePhare) {
                if (distance < minDist) {
                    minDist = distance;
                }
            } else if(estNotrePhare) nbPharesPossédés++;

            score = vieBateauEnnemi/(nbPharesPossédés + 1) + minDist; // priorités : 1. Détruire bateau adverse, 2. Prendre des phares, 3. s'approcher d'un phare
        }
        return score;
    }
}
