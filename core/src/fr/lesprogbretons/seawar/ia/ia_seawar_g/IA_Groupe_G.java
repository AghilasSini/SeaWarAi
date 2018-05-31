package fr.lesprogbretons.seawar.ia.ia_seawar_g;

import fr.lesprogbretons.seawar.controller.Controller;
import fr.lesprogbretons.seawar.ia.AbstractIA;
import fr.lesprogbretons.seawar.ia.ia_seawar_g.alphabeta.Max;
import fr.lesprogbretons.seawar.ia.ia_seawar_g.alphabeta.Noeud;
import fr.lesprogbretons.seawar.ia.ia_seawar_g.etat.Etat;
import fr.lesprogbretons.seawar.model.actions.Action;
import fr.lesprogbretons.seawar.model.boat.Boat;

import java.util.ArrayList;


public class IA_Groupe_G extends AbstractIA {

    public IA_Groupe_G(int number) {
        super(number);
    }

    public IA_Groupe_G(int identifier, String name, ArrayList<Boat> boats) {
        super(identifier, name, boats);
    }



    @Override
    public Action chooseAction(Controller controller) {
        System.out.println();
        System.out.println("**** Nouveau Tour de "+controller.getPartie().getCurrentPlayer().getNumber()+"****");
        System.out.println("IA_goupe_g number: "+getNumber());
        System.out.println("Position du nav0 : ("+controller.getPartie().getCurrentPlayer().getBoats().get(0).getPosition().getX()+";"+controller.getPartie().getCurrentPlayer().getBoats().get(0).getPosition().getY()+")");

        Max initial = new Max(new Etat((Controller) controller.clone()));
        int etage = 1;
        int alphabeta = 0;

        boolean process = true;//Tant que la recherche de noeud est possible

        while(process) {
            initial.genererFils(etage);
            alphabeta = initial.alphabeta(Integer.MIN_VALUE, Integer.MAX_VALUE);
            Noeud best = initial.getBestNoeud();

            //Affichage uniquement
            System.out.println("etage: " + etage);
            if (best != null) {
                System.out.println(best.getAction());
                System.out.println("h= " + best.getVal());

            } else {
                System.out.println("Pas d'actions trouve");
            }


            //On memorise l'action, si elle existe
            if(best!=null) {
                memoriseAction(best.getAction());
            }
            etage++;
        }

        System.out.println("*** Fin du Tour ***");

        return getMemorizedAction();

    }


}
