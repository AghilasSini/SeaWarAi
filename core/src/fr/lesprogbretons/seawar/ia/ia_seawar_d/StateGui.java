package fr.lesprogbretons.seawar.ia.ia_seawar_d;

import fr.lesprogbretons.seawar.controller.Controller;
import fr.lesprogbretons.seawar.model.Player;

/**
 * Created by camillejuigne on 25/04/2018.
 */
public abstract class StateGui {
	Controller controller;
	
	public StateGui (Controller controller) {
		this.controller = controller;
	}
	protected abstract Controller getController();
	
    public static double BEST_SCORE = 999999999;

    protected abstract double getScore();
    
    protected abstract boolean isMin (Player current_turn_player);
    //Cette methode retourne vrai si il faut au prochain �tage du tableau d�terminer le min
    //ceci en fonction du joueur dont il faut d�terminer la meilleure action
    
    abstract protected boolean isFinalState();
}