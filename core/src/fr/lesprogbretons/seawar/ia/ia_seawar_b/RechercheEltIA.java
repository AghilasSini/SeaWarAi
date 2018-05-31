package fr.lesprogbretons.seawar.ia.ia_seawar_b;

import fr.lesprogbretons.seawar.model.Partie;
import fr.lesprogbretons.seawar.model.cases.Case;
import fr.lesprogbretons.seawar.model.map.Grille;

import java.util.ArrayList;

public class RechercheEltIA {


    //Permet de renvoyer la liste indiquant la position des différents phrares de la carte
    public static ArrayList<Case> researchLighthouse (Partie partie){
        ArrayList<Case> posPhare = new ArrayList<>();
        Grille map = partie.getMap();
        for (int i=0; i < map.getHauteur(); i++){
            for (int j=0; j< map.getLargeur(); j++){
                if (map.getCase(i,j).isPhare())
                {
                    if(map.getCase(i,j).getPossedePhare() == null) {
                        posPhare.add(map.getCase(i, j));
                    }
                    else{
                        if(map.getCase(i,j).getPossedePhare().getNumber() != partie.getCurrentPlayer().getNumber()){
                            posPhare.add(map.getCase(i, j));
                        }
                    }
                }
            }
        }
        return posPhare;
    }
}

