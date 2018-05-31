package fr.lesprogbretons.seawar.ia.ia_seawar_c;

import fr.lesprogbretons.seawar.controller.Controller;
import fr.lesprogbretons.seawar.ia.AbstractIA;
import fr.lesprogbretons.seawar.model.Partie;
import fr.lesprogbretons.seawar.model.actions.Action;
import fr.lesprogbretons.seawar.model.actions.MoveBoat;
import fr.lesprogbretons.seawar.model.boat.Boat;

import java.util.ArrayList;

import static fr.lesprogbretons.seawar.SeaWar.logger;

public class IAAlphaBeta extends AbstractIA {

    private Action memorisedAction;

    public IAAlphaBeta(int identifier, String name, ArrayList<Boat> boats) {
        super(identifier, name, boats);
    }

    @Override
    public Action chooseAction(Controller controller) {

        NodeAlphaBeta algo = new NodeAlphaBeta((Controller)controller.clone(),true,1,-9999,9999);
        int indice_case = algo.alphabeta();
        //logger.debug("On choisi la case n : " + indice_case);
        Action depBat = new MoveBoat(controller.getPartie().getJoueur1().getBoats().get(0),controller.getPartie().getMap().getCasesDisponibles(controller.getPartie().getBateauSelectionne().getPosition(), 1).get(indice_case));
        //logger.debug("Case choisie : " + (controller.getPartie().getMap().getCasesDisponibles(controller.getPartie().getBateauSelectionne().getPosition(), 1).get(algo.alphabeta(algo,-9999,9999))).getX() + " " + (controller.getPartie().getMap().getCasesDisponibles(controller.getPartie().getBateauSelectionne().getPosition(), 1).get(algo.alphabeta(algo,-9999,9999))).getY());
        return depBat;
    }
}
