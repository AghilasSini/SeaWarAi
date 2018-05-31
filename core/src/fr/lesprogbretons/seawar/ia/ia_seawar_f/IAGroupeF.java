package fr.lesprogbretons.seawar.ia.ia_seawar_f;

import fr.lesprogbretons.seawar.controller.Controller;
import fr.lesprogbretons.seawar.ia.AbstractIA;
import fr.lesprogbretons.seawar.model.actions.Action;
import fr.lesprogbretons.seawar.model.boat.Boat;

import java.util.List;

import static fr.lesprogbretons.seawar.SeaWar.logger;

public class IAGroupeF extends AbstractIA {

    public IAGroupeF(int number) {
        super(number, "IAGoupeF");
    }

    public IAGroupeF(int number, String name, List<Boat> boats) {
        super(number, name, boats);
    }


    @Override
    public Action chooseAction(Controller controller) {
        int etages = 1;

        //Reset action
        memoriseAction(null);

        Max max = new Max(controller, null, etages);

        max.alphabeta(Integer.MAX_VALUE, -1000);

        Action myAction = max.bestNode.getMyAction();
        logger.error(getName() + " J'ai choisi ! " + myAction.toString() + " " + max.bestNode.getHeuristic());
        memoriseAction(myAction);

        return myAction;
    }
}
