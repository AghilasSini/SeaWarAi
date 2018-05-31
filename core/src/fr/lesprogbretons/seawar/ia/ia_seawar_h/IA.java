package fr.lesprogbretons.seawar.ia.ia_seawar_h;

import fr.lesprogbretons.seawar.controller.Controller;
import fr.lesprogbretons.seawar.ia.AbstractIA;
import fr.lesprogbretons.seawar.model.actions.Action;
import fr.lesprogbretons.seawar.model.boat.Boat;
import fr.lesprogbretons.seawar.ia.ia_seawar_h.util.State;

import java.util.List;

public class IA extends AbstractIA {

    public IA(int number){
        super(number);
    }
    public IA(int number, String name){
        super(number, name);
    }
    public IA(int number, String name, List<Boat> boats){
        super(number, name, boats);
    }



    @Override
    public Action chooseAction(Controller controleur) {
        Action a = null;
        for(int i=1; i<=10;i++){
            a = getAPossibleAction(controleur, i);
            this.memoriseAction(a);
        }

        return a;
    }

    private Action getAPossibleAction(Controller controleur, int prof){
        State state = new State(this.getName(), controleur);
        state.createArbre(prof);
        state.alphabeta(-99999, 99999);
        Action a = state.getActionToDo();
        return a;
    }




}
