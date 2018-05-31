package fr.lesprogbretons.seawar.ia.ia_seawar_a;

import fr.lesprogbretons.seawar.model.actions.Action;

public class Etat {
	public Action action;
	public int utilite;
	
	public Etat(Action a, int u) {
		action  = a;
		utilite = u;
	}
}
