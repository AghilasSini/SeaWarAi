package fr.lesprogbretons.seawar.ia.ia_seawar_g.etat;

import fr.lesprogbretons.seawar.controller.Controller;
import fr.lesprogbretons.seawar.model.Partie;
import fr.lesprogbretons.seawar.model.actions.Action;
import fr.lesprogbretons.seawar.model.actions.Attack;
import fr.lesprogbretons.seawar.model.actions.MoveBoat;
import fr.lesprogbretons.seawar.model.actions.PassTurn;
import fr.lesprogbretons.seawar.model.boat.Boat;
import fr.lesprogbretons.seawar.model.cases.Case;

import java.util.ArrayList;
import java.util.List;

public class Etat {

    private Controller controller;

    public Etat(Controller c) {
        controller=c;

    }

    public Controller getController() {
        return controller;
    }

    public Etat clone() {
        Etat clone = new Etat(controller);
        clone.setController((Controller) controller.clone());
        return clone;
    }

    public Partie getPartie() {
        return controller.getPartie();
    }


    public void setController(Controller c) {
        this.controller = c;
    }



    public List<Action> getPossibleActions() {
        ArrayList<Action> actions = new ArrayList<>();
        for (Boat boat : getController().getPartie().getCurrentPlayer().getBoats()) {
            if (boat.isAlive()) {
                ArrayList<Case> cases = new ArrayList<>();
                if (boat.getMoveAvailable() != 0 && !controller.getPartie().getMap().getCasesDisponibles(boat.getPosition(), 1).isEmpty()) {
                    cases = controller.getPartie().getMap().getCasesDisponibles(boat.getPosition(), 1);
                }

                if (!cases.isEmpty()) {
                    for (Case target : cases) {
                        actions.add(new MoveBoat(boat, target));
                    }
                }

                // attack possibilities
                ArrayList<Case> boatInRange = controller.getPartie().getMap().getBoatInRange(boat, controller.getPartie().getOtherPlayer());
                if (!boatInRange.isEmpty() && boat.canShoot()) {
                    for (Case target : boatInRange) {
                        actions.add(new Attack(boat, target));
                    }
                }

                // pass the turn without doing nothing
                actions.add(new PassTurn(boat));
            }
        }
        return actions;
    }

}
