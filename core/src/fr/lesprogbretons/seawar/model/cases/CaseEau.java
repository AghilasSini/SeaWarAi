package fr.lesprogbretons.seawar.model.cases;


import java.io.Serializable;

/**
 * Classe CaseEau : sous-classe de Case
 */
public class CaseEau extends Case implements Serializable {

    /**
     * Constructeur
     *
     * @param x
     * @param y
     */
    public CaseEau(int x, int y) {
        super(x, y);
        phare = false;
    }

    public Object clone() {
        CaseEau clone = new CaseEau(this.getX(), this.getY());
        clone.setPhare(this.isPhare());
        clone.setPossedePhare(this.getPossedePhare());
        //bateauDetruit ne semble pas servir pour le modele
        return clone;
    }

    public boolean isPhare() {
        return phare;
    }

    public void setPhare(boolean phare) {
        this.phare = phare;
    }

    @Override
    public String infoCase() {
        if (isPhare()) {
            return super.infoCase();
        } else {
            return "    Water";
        }
    }

    @Override
    public String toString() {
        return "Water";
    }
}
