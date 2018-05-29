package fr.lesprogbretons.seawar.model.boat;

import fr.lesprogbretons.seawar.model.Player;
import fr.lesprogbretons.seawar.model.cases.Case;

import java.io.Serializable;

/**
 * Classe Fregate : sous-classe de Boat
 */
public class Fregate extends Boat implements Serializable {

    public Fregate(Case c, Player p) {
        super(c, p);
        move = 5;
        moveAvailable = move;
        maxHp = 50;
        hp = maxHp;
        dmgMainCanon = 30;
        reloadMainCanon = 2;
        dmgSecCanon = 10;
        reloadSecCanon = 1;
    }

    public Object clone(Case c) {
        Fregate clone = new Fregate(c, null); // le joueur ne semble pas servir dans le modele
        clone.setAlive(this.isAlive());
        clone.setEstBloque(this.isEstBloque());
        clone.setCanonSelectionne(this.getCanonSelectionne());
        clone.setMoveAvailable(this.getMoveAvailable());
        clone.setShootTaken(this.getShootTaken());
        clone.setOrientation(this.getOrientation());
        clone.setMainCD(this.getMainCD());
        clone.setSecCD(this.getSecCD());
        clone.setHp(this.getHp());
        clone.joueur = (Player) this.joueur.clone();

        return clone;
    }

    @Override
    public String toString() {
        return "Fregate";
    }
}
