package fr.lesprogbretons.seawar.ia.ia_seawar_e;

import java.awt.*;

public class SimMap implements Cloneable {
    public SimCase tab[][];
    public Point posPhares[];
    public int hauteur;
    public int largeur;
    public SimMap(SimCase t[][], Point posP[]){
        tab = t;
        posPhares=posP;
        this.hauteur = tab.length;
        this.largeur = tab[0].length;
    }

    public Object clone(){
        SimCase tabClone[][] = new SimCase[this.tab.length][this.tab[0].length];
        for(int i=0; i<this.tab.length; i++){
            for(int j=0; j<this.tab[0].length; j++){
                tabClone[i][j]=(SimCase)this.tab[i][j].clone();
            }
        }
        Point posPhClone[] = new Point[this.posPhares.length];
        for(int i=0; i<this.posPhares.length; i++){
            posPhClone[i]=new Point(this.posPhares[i].x,this.posPhares[i].y);
        }
        SimMap clone = new SimMap(tabClone,posPhClone);
        return clone;
    }

    public SimCase getCaseNord(SimCase c) {
        if (c == null) {
            return null;
        }
        int x;
        int y;
        SimCase cas;
        x = c.x;
        y = c.y;
        if (x + 1 >= 0 && x + 1 <= hauteur - 1) {
            cas = tab[x + 1][y];
            return cas;
        }
        return null;
    }

    public SimCase getCaseSud(SimCase c) {
        if (c == null) {
            return null;
        }
        int x;
        int y;
        SimCase cas;
        x = c.x;
        y = c.y;
        if (x - 1 >= 0 && x - 1 <= hauteur - 1) {
            cas = tab[(x - 1)][y];
            return cas;
        }
        return null;
    }

    public SimCase getCaseNordEst(SimCase c) {
        if (c == null) {
            return null;
        }
        int x;
        int y;
        SimCase cas;
        x = c.x;
        y = c.y;
        if (y % 2 == 0) {
            if (y + 1 >= 0 && y + 1 <= largeur - 1) {
                cas = tab[x][(y + 1)];
                return cas;
            }
        } else {
            if (x + 1 >= 0 && x + 1 <= hauteur - 1 && y + 1 >= 0 && y + 1 <= largeur - 1) {
                cas = tab[(x + 1)][ (y + 1)];
                return cas;
            }
        }
        return null;
    }

    public SimCase getCaseSudEst(SimCase c) {
        if (c == null) {
            return null;
        }
        int x;
        int y;
        SimCase cas;
        x = c.x;
        y = c.y;
        if (y % 2 == 0) {
            if (x - 1 >= 0 && x - 1 <= hauteur - 1 && y + 1 >= 0 && y + 1 <= largeur - 1) {
                cas = tab[(x - 1)][ (y + 1)];
                return cas;
            }
        } else {
            if (y - 1 >= 0 && y + 1 <= largeur - 1) {
                cas = tab[x][ (y + 1)];
                return cas;
            }
        }
        return null;
    }

    public SimCase getCaseNordOuest(SimCase c) {
        if (c == null) {
            return null;
        }
        int x;
        int y;
        SimCase cas;
        x = c.x;
        y = c.y;
        if (y % 2 == 0) {
            if (y - 1 >= 0 && y - 1 <= largeur - 1) {
                cas = tab[x][ (y - 1)];
                return cas;
            }
        } else {
            if (x + 1 >= 0 && x + 1 <= hauteur - 1 && y - 1 >= 0 && y - 1 <= hauteur - 1) {
                cas = tab[(x + 1)][ (y - 1)];
                return cas;
            }
        }
        return null;
    }

    public SimCase getCaseSudOuest(SimCase c) {
        if (c == null) {
            return null;
        }
        int x;
        int y;
        SimCase cas;
        x = c.x;
        y = c.y;
        if (y % 2 == 0) {
            if (x - 1 >= 0 && x - 1 <= hauteur - 1 && y - 1 >= 0 && y - 1 <= largeur - 1) {
                cas = tab[(x - 1)][ (y - 1)];
                return cas;
            }
        } else {
            if (y - 1 >= 0 && y - 1 <= hauteur - 1) {
                cas = tab[x][ (y - 1)];
                return cas;
            }
        }
        return null;
    }
}
