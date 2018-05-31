package fr.lesprogbretons.seawar.ia.ia_seawar_j;

import com.badlogic.gdx.graphics.glutils.FloatTextureData;
import fr.lesprogbretons.seawar.controller.Controller;
import fr.lesprogbretons.seawar.ia.AbstractIA;
import fr.lesprogbretons.seawar.model.Partie;
import fr.lesprogbretons.seawar.model.Player;
import fr.lesprogbretons.seawar.model.actions.Action;
import fr.lesprogbretons.seawar.model.actions.Attack;
import fr.lesprogbretons.seawar.model.actions.PassTurn;
import fr.lesprogbretons.seawar.model.boat.Boat;
import fr.lesprogbretons.seawar.model.cases.Case;
import fr.lesprogbretons.seawar.model.cases.CaseEau;
import fr.lesprogbretons.seawar.model.map.DefaultMap;
import fr.lesprogbretons.seawar.model.map.Grille;

import java.util.ArrayList;
import java.util.List;

public class IACreole extends AbstractIA {

    private int profondeurMax;


    public IACreole(int number) {
        super(number);
    }

    public IACreole(int number, String name) {
        super(number, name);
    }

    public IACreole(int number, String name, List<Boat> boats) {
        super(number, name, boats);
    }

    boolean IAGlouton = true;

    @Override
    public Action chooseAction(Controller controller) {
        if(IAGlouton) {

            Noeud racine1 = new Noeud(controller, 0, false);
            Float heuristique = Float.MAX_VALUE;
            List<Action> actions = controller.getPossibleActions();
            Action actiondiscrete = actions.get(0);

            for (Action ac : actions) {

                if (!(ac instanceof PassTurn)){
                    if(!((ac instanceof Attack))) {
                            Controller nouvelEtat = (Controller) controller.clone();
                            ac.apply(nouvelEtat);
                            Noeud tmp = new Noeud(nouvelEtat, 0, false);
                            Float h1 = tmp.heuristiqueGlouton(nouvelEtat.getPartie());
                            if (h1 < heuristique) {
                                heuristique = h1;
                                actiondiscrete = ac;
                            }
                            //ac.apply(nouvelEtat);
                            //fils.add(new Noeud(nouvelEtat, this.profondeur + 1, !this.max));
                            //actionsFils.add(ac);
                    }
                }
            }
            return actiondiscrete;
        } else {
            int profondeur = 1;
            Noeud racine = new Noeud(controller, 0, false);
            while(profondeur < 4){
                racine.max(Float.MIN_VALUE, Float.MAX_VALUE, profondeur);
                this.memoriseAction(racine.getActionChoisie());
                //System.out.println(this.getMemorizedAction().toString());
                System.out.println(racine.getHeuristique());
                profondeur++;
            }
            return racine.getActionChoisie();
        }
    }






    //@Override
    //public Action chooseAction(Partie partie) {
    //    return null;
    //}

}
