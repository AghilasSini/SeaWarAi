package fr.lesprogbretons.seawar.ia.ia_seawar_f;

import fr.lesprogbretons.seawar.controller.Controller;
import fr.lesprogbretons.seawar.model.actions.MoveBoat;

public class SecondaryAttackAndMove extends AttackAndMove {

    private SecondaryAttack attack;

    public SecondaryAttackAndMove(SecondaryAttack attack, MoveBoat move) {
        super(attack, move);
        this.attack = attack;
    }

    @Override
    public Object clone() {
        return new SecondaryAttackAndMove((SecondaryAttack) this.attack.clone(),
                (MoveBoat) this.move.clone());
    }

    @Override
    public void apply(Controller controller) {
        attack.apply(controller);
        move.apply(controller);
    }

    @Override
    public String toString() {
        return "On tire du secondaire et on bouge !";
    }
}
