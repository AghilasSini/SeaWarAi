package fr.lesprogbretons.seawar.ia.ia_seawar_e;

public class SimCase implements Cloneable {
    public int typeCase;    //0:terre, 1:eau, 2:phare
    public int ownerPhare;  //0:null, 1:p1, 2:p2
    public int x;
    public int y;
    public SimCase(int typeC, int owner, int x, int y){
        ownerPhare = owner;
        typeCase = typeC;
        this.x=x;
        this.y=y;
    }

    public Object clone(){
        SimCase clone = new SimCase(this.typeCase,this.ownerPhare,this.x,this.y);
        return clone;
    }

}
