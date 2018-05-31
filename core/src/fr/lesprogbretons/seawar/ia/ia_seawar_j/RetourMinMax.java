package fr.lesprogbretons.seawar.ia.ia_seawar_j;

import fr.lesprogbretons.seawar.model.actions.Action;

public class RetourMinMax {

    public float getAlphaOuBeta() {
        return alphaOuBeta;
    }

    public Action getAction() {
        return action;
    }

    private float alphaOuBeta;
    private Action action;

    public RetourMinMax(float alphaOuBeta, Action action){
        this.alphaOuBeta = alphaOuBeta;
        this.action = action;
    }


}
