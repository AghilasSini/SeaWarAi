package fr.lesprogbretons.seawar.ia.ia_seawar_f;

import fr.lesprogbretons.seawar.controller.Controller;
import fr.lesprogbretons.seawar.model.actions.Attack;
import fr.lesprogbretons.seawar.model.actions.ChangeCannon;
import fr.lesprogbretons.seawar.model.boat.Boat;
import fr.lesprogbretons.seawar.model.cases.Case;

public class SecondaryAttack extends Attack {

    public SecondaryAttack(Boat boat, Case target) {
        super(boat, target);
    }

    @Override
    public Object clone() {
        return new SecondaryAttack(this.getBoat(), this.getTarget());
    }

    @Override
    public void apply(Controller controller) {
        ChangeCannon cc = new ChangeCannon(this.getBoat());
        cc.apply(controller);
        super.apply(controller);
        cc.apply(controller);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
