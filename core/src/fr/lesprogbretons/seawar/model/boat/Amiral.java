package fr.lesprogbretons.seawar.model.boat;

import fr.lesprogbretons.seawar.model.Player;
import fr.lesprogbretons.seawar.model.cases.Case;

import java.io.Serializable;

/**
 * Classe Amiral : sous-classe de Boat
 */
public class Amiral extends Boat implements Serializable {

    public Amiral(Case c, Player p) {
        super(c, p);
        move = 3;
        moveAvailable = move;
        maxHp = 100;
        hp = maxHp;
        dmgMainCanon = 50;
        reloadMainCanon = 4;
        dmgSecCanon = 30;
        reloadSecCanon = 2;
    }

    public Object clone(Case c) {
        Amiral clone = new Amiral(c, null); // le joueur ne semble pas servir dans le modele
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
        return "Amiral";
    }
}
