package fr.lesprogbretons.seawar.ia.ia_seawar_d;

import fr.lesprogbretons.seawar.controller.Controller;
import fr.lesprogbretons.seawar.ia.AbstractIA;
import fr.lesprogbretons.seawar.model.Player;
import fr.lesprogbretons.seawar.model.actions.Action;
import fr.lesprogbretons.seawar.model.boat.Boat;

import java.util.ArrayList;
import java.util.List;

public class MinimaxGui extends AbstractIA {

    private int nbStatesExploredLastTurn;

    private int nbStatesExplored;

    private int nbTurns = 0;
    
    private Player current_turn_player;
    
    public MinimaxGui(int identifier) {
        super(identifier,"Minimax");
    }

    public MinimaxGui(int identifier,String name,ArrayList<Boat> boats) {
        // TODO Auto-generated constructor stub
        super(identifier,name,boats);
    }

	public Action chooseAction(Controller controller) {
		Action best_action = null;
		int depth = 0;
		long time_begining = System.currentTimeMillis();
		long time_to_think = this.TIME_TO_THINK;
		current_turn_player = controller.getPartie().getCurrentPlayer();
		while((System.currentTimeMillis()-time_begining) < time_to_think)
		{ //Boucle qui s'effectue tant que le temps pour un tour n'est pas �coul� 
			depth++;
			best_action = minimax(controller, depth);
			this.memoriseAction(best_action);
		}
		return best_action;
	}

    public Action minimax(Controller controller, int max_depth) {
        Action best_action = null;
        double best_score = -StateGui.BEST_SCORE;
        nbStatesExploredLastTurn = 0;
        nbTurns++;
        List<Action> possible_actions = controller.getPossibleActions(); //on prend les actions possibles � partir de la partie actuelle

        for (Action action : possible_actions) { //on parcourt chacune de ces actions
        	
        	Controller controllerClone = (Controller) controller.clone();
        	action.apply(controllerClone);    //on applique l'action � un clone de la partie
        	StateGui state = new StateSeaWarGui(controllerClone);  //nous cr�eons ainsi un nouvel �tat
        	double score = best_score;
        	
        	if (state.isMin(current_turn_player)) { //on regarde si � l'issue de notre action il faut d�terminer le min ou le max
        		score = getMinScore(state, 1, max_depth); //puis nous le d�terminons 
        	}
        	else {
        		score = getMaxScore(state, 1, max_depth);
        	}
            if (score > best_score) { //on cherche ensuite le score maximal parmis tous ceux d�termin�s
                best_score = score;
                best_action = action; //puis nous enregistrons l'action associ�e
            }
        }
        return best_action;
    }

    double getMaxScore(StateGui state, int depth, int max_depth) {
        nbStatesExplored++;
        nbStatesExploredLastTurn++;

        if (state.isFinalState() || depth > max_depth) {
            return state.getScore();
        }
        double maxScore = -state.BEST_SCORE;
        
        List<Action> possible_actions = state.controller.getPossibleActions();
        
        for (Action action : possible_actions) {
        	
        	Controller controllerClone = (Controller) state.controller.clone();
        	action.apply(controllerClone);
        	StateGui child_state = new StateSeaWarGui(controllerClone);
        	
        	if (child_state.isMin(current_turn_player)) {
        		maxScore = max(maxScore, getMinScore(child_state, depth + 1, max_depth));
        	}
        	else {
        		maxScore = max(maxScore, getMaxScore(child_state, depth + 1, max_depth));
        	}
        	
        }
        return maxScore;
    }

    double getMinScore(StateGui state, int depth, int max_depth) {
        nbStatesExplored++;
        nbStatesExploredLastTurn++;

        if (state.isFinalState() || depth > max_depth) {
            return state.getScore();
        }
        double minScore = state.BEST_SCORE;
        
        List<Action> possible_actions = state.controller.getPossibleActions();

        for (Action action : possible_actions) {
        	
        	Controller controllerClone = (Controller) state.controller.clone();
        	action.apply(controllerClone);
        	StateGui child_state = new StateSeaWarGui(controllerClone);
        	
        	if (child_state.isMin(current_turn_player)) {
        		minScore = min(minScore, getMinScore(child_state, depth + 1, max_depth));
        	}
        	else {
        		minScore = min(minScore, getMaxScore(child_state, depth + 1, max_depth));
        	}
        	
            
        }
        return minScore;
    }

    double max(double score1, double score2) {
        if (score1 > score2) {
            return score1;
        }
        return score2;
    }

    double min(double score1,double score2) {
        if (score1 < score2) {
            return score1;
        }
        return score2;
    }

    public String getLastTurnStats() {
        return "Nb states explored : " + nbStatesExploredLastTurn;
    }

    public String getStats() {
        return "Average ofnb states explored : " + ((double)nbStatesExplored / nbTurns);
    }

}