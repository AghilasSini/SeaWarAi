package fr.lesprogbretons.seawar.ia.ia_seawar_e;

import fr.lesprogbretons.seawar.model.Orientation;

import java.awt.*;

public class SimBoat implements Cloneable{
    int hp;
    int move;
    int moveDispo;
    Point pos;
    Orientation or;
    int dmgMainC;
    int dmgSecC;
    int reloadMain;
    int reloadSec;
    int cdMain;
    int cdSec;
    int owner;
    boolean estBloque;
    boolean plusLeDroitDeplacer;
    int type;   //0:Amiral , 1:Fregate, 2:Autre
    public SimBoat(int hp, int move, int moveDispo, Point pos, Orientation or,
                   int dmgMainC, int dmgSecC, int reloadMain, int reloadSec,
                   int cdMain, int cdSec, int owner, boolean estBloque, int type, boolean plusLeDroitDeplacer){
        this.hp=hp;
        this.move=move;
        this.moveDispo=moveDispo;
        this.pos = pos;
        this.or = or;
        this.dmgMainC = dmgMainC;
        this.dmgSecC = dmgSecC;
        this.reloadMain = reloadMain;
        this.reloadSec = reloadSec;
        this.cdMain = cdMain;
        this.cdSec = cdSec;
        this.owner = owner;
        this.estBloque = estBloque;
        this.type = type;
        this.plusLeDroitDeplacer = plusLeDroitDeplacer;
    }

    public Object clone(){
        SimBoat clone = new SimBoat(this.hp,this.move,this.moveDispo,new Point(this.pos.x,this.pos.y),
                this.or,this.dmgMainC,this.dmgSecC,this.reloadMain,this.reloadSec,
                this.cdMain, this.cdSec, this.owner, this.estBloque, this.type, this.plusLeDroitDeplacer);

        return clone;
    }
}
