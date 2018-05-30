package fr.lesprogbretons.seawar.model.actions;

import fr.lesprogbretons.seawar.controller.Controller;
import fr.lesprogbretons.seawar.model.boat.Boat;

public class ChangeCannon extends Action {
    Boat boat;

    public ChangeCannon(Boat boat){
        this.boat = boat;
    }

    @Override
    public Object clone() {
        return new ChangeCannon(boat);
    }

    @Override
    public void apply(Controller state) {
        state.selection(boat.getPosition().getX(), this.boat.getPosition().getY());
        state.changerCanon();
    }

    @Override
    public String toString() {
        return "Change the canon";
    }
}
