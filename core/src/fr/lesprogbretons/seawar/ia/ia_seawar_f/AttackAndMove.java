package fr.lesprogbretons.seawar.ia.ia_seawar_f;

import fr.lesprogbretons.seawar.controller.Controller;
import fr.lesprogbretons.seawar.model.actions.Action;
import fr.lesprogbretons.seawar.model.actions.Attack;
import fr.lesprogbretons.seawar.model.actions.MoveBoat;
import fr.lesprogbretons.seawar.model.boat.Boat;
import fr.lesprogbretons.seawar.model.cases.Case;

public class AttackAndMove extends Attack {

    private Attack attack;
    protected MoveBoat move;

    public AttackAndMove(Attack attack, MoveBoat move) {
        super(attack.getBoat(), move.getTarget());
        this.attack = attack;
        this.move = move;
    }

    public AttackAndMove(Boat boat, Case targetBoat, Case targetMove) {
        super(boat, targetBoat);
        attack = new Attack(boat, targetBoat);
        move = new MoveBoat(boat, targetMove);
    }

    public MoveBoat getMove() {
        return move;
    }

    @Override
    public Object clone() {
        return new AttackAndMove((Attack) this.attack.clone(), (MoveBoat) this.move.clone());
    }

    @Override
    public void apply(Controller controller) {
        attack.apply(controller);
        move.apply(controller);
    }

    @Override
    public String toString() {
        return "On tire et on bouge !";
    }
}
