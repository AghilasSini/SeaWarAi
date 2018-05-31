package fr.lesprogbretons.seawar.ia.ia_seawar_d;

import fr.lesprogbretons.seawar.controller.Controller;
import fr.lesprogbretons.seawar.model.Player;
import fr.lesprogbretons.seawar.model.cases.Case;

import java.util.ArrayList;

public class StateSeaWarGui extends StateGui {
    public StateSeaWarGui(Controller controller) {
		super(controller); //ici on devrait calculer le score de chaque �tat
		this.score = heuristique1();
	}

	double score;
    @Override
	public double getScore() {
        return this.score;
    }

    @Override
    protected boolean isFinalState() {
        return controller.getPartie().isFin();
    }

	protected Controller getController() {
		return this.controller;
	}
	
	protected boolean isMin (Player current_turn_player){
		Player current_player = this.controller.getPartie().getCurrentPlayer();
		if (current_turn_player == current_player) {return true;}
		else {return false;}
	}

	public double heuristique1(){
		/* HEURISTIQUE 1 : DISTANCE AU PHARE */
		Case[][] cas = controller.getPartie().getMap().getCases();
		ArrayList<Case> phares = new ArrayList();
		for (Case[] tab : cas) {
			for (Case c : tab) {
				if (c.isPhare()) {
					phares.add(c);
				}
			}
		}
		double minDist = 9999;
		double distance;
		for (Case c : phares) {
			int x = controller.getPartie().getBateauSelectionne().getPosition().getX() - c.getX();
			int y = controller.getPartie().getBateauSelectionne().getPosition().getY() - c.getY();

			distance = Math.sqrt(x * x + y * y);

			if (c.getPossedePhare() == null || c.getPossedePhare().toString() != "Joueur2") {


				if (distance < minDist) {
					minDist = distance;
				}
			} else {
				if (distance == 0) {
					minDist = 0;
				}
			}
		}
		return minDist;
	}

	public int heuristique2(){
		Player current_player = this.controller.getPartie().getCurrentPlayer();
		return(current_player.getPharesPossedes());
	}

	public int heuristique3(){
		return (1);
	}
}

