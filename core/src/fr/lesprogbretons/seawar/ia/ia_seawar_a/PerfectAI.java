package fr.lesprogbretons.seawar.ia.ia_seawar_a;

import java.util.ArrayList;
import java.util.List;

import fr.lesprogbretons.seawar.controller.Controller;
import fr.lesprogbretons.seawar.ia.AbstractIA;
import fr.lesprogbretons.seawar.model.Partie;
import fr.lesprogbretons.seawar.model.Player;
import fr.lesprogbretons.seawar.model.actions.Action;
import fr.lesprogbretons.seawar.model.actions.PassTurn;
import fr.lesprogbretons.seawar.model.boat.Boat;
import fr.lesprogbretons.seawar.model.cases.Case;

public class PerfectAI extends AbstractIA {
	
	private Case[] phares;
	
	public PerfectAI(int number) {
		super(number);
		phares = new Case[3];
	}
	
	public PerfectAI(int number, String name) {
		super(number, name);
		phares = new Case[3];
	}
	
	public PerfectAI(int number, String name, List<Boat> boats) {
		super(number, name, boats);
		phares = new Case[3];
	}
	
	private int heuristique(Partie partie) {
		Player adversaire;
		if(this.getNumber() == 1) {
			adversaire = partie.getJoueur2();
		} else {
			adversaire = partie.getJoueur1();
		}
				
		// Initialisation de l'emplacement des phares
		if(phares[0] == null) {
			int phareCount = 0;
			for(int i=0; i< partie.getMap().getHauteur(); i++) {
				for(int j=0; j < partie.getMap().getLargeur(); j++) {
					if(partie.getMap().getCase(i, j).isPhare()) {
						phares[phareCount] = partie.getMap().getCase(i, j);
						phareCount++;
					}
				}
			}
		}
		
		//Récupération de mes coordonnées
		int xPosMoi = this.getBoats().get(0).getPosition().getX();
		int yPosMoi = this.getBoats().get(0).getPosition().getY();
		
		//Récupération des coordonnées de l'adversaire
		int xPosAdv = adversaire.getBoats().get(0).getPosition().getX();
		int yPosAdv = adversaire.getBoats().get(0).getPosition().getY();

		
		// Calcul des distances aux phares moi
		int distPhareMoi[] = new int[3];
		int indPharePlusProcheMoi = 0;

		
		for(int i = 0; i < 3; i++) {
			if(phares[i] != null) {
				if(phares[i].getPossedePhare() != this) {
					distPhareMoi[i] = 20 - java.lang.Math.abs(xPosMoi - phares[i].getX()) - java.lang.Math.abs(yPosMoi - phares[i].getY());
					System.out.println("phare " + i + " dist " + distPhareMoi[i]);
				} else {
					distPhareMoi[i] = 0;
				}
			} else {
				distPhareMoi[i] = 0;
			}
			if(distPhareMoi[i] > 0 && distPhareMoi[indPharePlusProcheMoi] > distPhareMoi[i]) {
				indPharePlusProcheMoi = i;
			}
		}
		
		// Calcule distance aux phares adversaire
		int distPhareAdv[] = new int[3];
		int indPharePlusProcheAdv = 0;

		
		for(int i = 0; i < 3; i++) {
			if(phares[i] != null) {
				if(phares[i].getPossedePhare() != adversaire) {
					distPhareAdv[i] = 20 - java.lang.Math.abs(xPosAdv - phares[i].getX()) - java.lang.Math.abs(yPosAdv - phares[i].getY());
				} else {
					distPhareAdv[i] = 0;
				}
			} else {
				distPhareAdv[i] = 0;
			}
			if(distPhareAdv[i] > 0 && distPhareAdv[indPharePlusProcheAdv] > distPhareAdv[i]) {
				indPharePlusProcheAdv = i;
			}
		}
		
		//Calcul heuristique
		int heur = 0;
		for(int i = 0; i < 3; i++) {
			if(i != indPharePlusProcheMoi) {
				heur += distPhareMoi[i];
				
			} else {
				heur += 3 * distPhareMoi[i];
			}
			
			if(i != indPharePlusProcheAdv) {
				heur -= distPhareAdv[i];
			} else {
				heur -= 3 * distPhareAdv[i];
			}
		}
		return heur;
	}
	
	private Etat alphabeta(Controller controller, int alpha, int beta, int depth) {
		List<Action> possibleActions;
		List<Controller> children;
		Controller child;
		
		// Cas 1 : partie terminée
		if(controller.getPartie().isFin()) {
			if(controller.getPartie().getWinner().getNumber() == this.getNumber()) {
				return new Etat(null, Integer.MAX_VALUE);
			} else {
				return new Etat(null, Integer.MIN_VALUE);
			}
		}
		
		// Cas 2 : profondeur max atteinte
		if(depth == 0) {
			int heur = heuristique(controller.getPartie());
			System.out.println("Heuristique : " +heur);
			return new Etat(null, heur);
		}
		
		// Génération des états fils
		possibleActions = controller.getPossibleActions();
		children = new ArrayList<Controller>();
		for(int i = 0; i < possibleActions.size(); i++) {
			Action action = possibleActions.get(i);
			child = (Controller) controller.clone();
			
			action.apply(child);
			children.add(child);
		}
		
		if(controller.getPartie().getCurrentPlayer().getNumber() == this.getNumber()) { // Noeud MAX
			int i = 0;
			Action action = null;
			while(i < children.size() && alpha < beta) {
				Etat res = alphabeta(children.get(i), alpha, beta, depth - 1);
				if(res.utilite > alpha) {
					action = possibleActions.get(i);
					alpha = res.utilite;
				}
				i++;
			}
			return new Etat(action, alpha);
		} else { // Noeud MIN
			int i = 0;
			Action action = null;
			while(i < children.size() && alpha < beta) {
				Etat res = alphabeta(children.get(i), alpha, beta, depth - 1);
				if(res.utilite < beta) {
					action = possibleActions.get(i);
					beta = res.utilite;
				}
				i++;
			}
			return new Etat(action, beta);
		}
	}

	@Override
	public Action chooseAction(Controller controller) {
		/*
		for(int i = 1; i < Integer.MAX_VALUE; i++) {
			this.memoriseAction(alphabeta(controller, Integer.MIN_VALUE, Integer.MAX_VALUE, i).action);
		}
		return this.getMemorizedAction();
		*/
		
		Partie partie = controller.getPartie();
				
		// Initialisation de l'emplacement des phares
		int phareCount = 0;
		for(int i=0; i< partie.getMap().getHauteur(); i++) {
			for(int j=0; j < partie.getMap().getLargeur(); j++) {
				if(partie.getMap().getCase(i, j).isPhare()) {
					phares[phareCount] = partie.getMap().getCase(i, j);
					phareCount++;
				}
			}
		}
		
		
		//Récupération de mes coordonnées
		int xPosMoi = this.getBoats().get(0).getPosition().getX();
		int yPosMoi = this.getBoats().get(0).getPosition().getY();
		
		// Calcul des distances aux phares moi
		int distPhareMoi[] = new int[3];
		int indPharePlusProcheMoi = 0;
			
		for(int i = 0; i < 3; i++) {
			if(phares[i] != null) {
				if(phares[i].getPossedePhare() == null || phares[i].getPossedePhare().getNumber() != this.getNumber()) {
					distPhareMoi[i] = (xPosMoi - phares[i].getX())*(xPosMoi - phares[i].getX()) + (yPosMoi - phares[i].getY())*(yPosMoi - phares[i].getY());
					System.out.println("Distance phare " + i + " : " + distPhareMoi[i]);
				} else {
					distPhareMoi[i] = 0;
				}
			} else {
				distPhareMoi[i] = 0;
			}
			if(distPhareMoi[i] > 0 && distPhareMoi[indPharePlusProcheMoi] > distPhareMoi[i]) {
				indPharePlusProcheMoi = i;
			}
		}
		System.out.println("Phare plus proche : "+indPharePlusProcheMoi);

		
		// Génération des états fils
		List<Action> possibleActions = controller.getPossibleActions();
		List<Controller> children = new ArrayList<Controller>();
		Controller child;
		for(int i = 0; i < possibleActions.size(); i++) {
			Action action = possibleActions.get(i);
			child = (Controller) controller.clone();
			action.apply(child);
			children.add(child);
		}
		
		//Calcul du fils le plus proche du phare le plus proche
		int plusProche = 0;
		int posXfils;
		int posYfils;
		int distMin = Integer.MAX_VALUE;
		for(int i = 0; i < children.size(); i++) {
			if(! (possibleActions.get(i) instanceof PassTurn)) {
				if(this.getNumber() == 1) {
					posXfils = children.get(i).getPartie().getJoueur1().getBoats().get(0).getPosition().getX();
					posYfils = children.get(i).getPartie().getJoueur1().getBoats().get(0).getPosition().getY();
				} else {
					posXfils = children.get(i).getPartie().getJoueur2().getBoats().get(0).getPosition().getX();
					posYfils = children.get(i).getPartie().getJoueur2().getBoats().get(0).getPosition().getY();
				}
				int dist = (posXfils - phares[indPharePlusProcheMoi].getX())*(posXfils - phares[indPharePlusProcheMoi].getX()) + (posYfils - phares[indPharePlusProcheMoi].getY())*(posYfils - phares[indPharePlusProcheMoi].getY());
				if(dist < distMin) {
					plusProche = i;
					distMin = dist;
				}	
			}
			
		}
		
		
		return possibleActions.get(plusProche);
	}

}
