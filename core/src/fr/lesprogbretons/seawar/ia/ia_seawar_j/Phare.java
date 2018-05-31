package fr.lesprogbretons.seawar.ia.ia_seawar_j;

import fr.lesprogbretons.seawar.model.Partie;
import fr.lesprogbretons.seawar.model.Player;
import fr.lesprogbretons.seawar.model.cases.Case;
import fr.lesprogbretons.seawar.model.cases.CaseEau;

import java.util.ArrayList;

public class Phare {
    private static Phare phare;
    private ArrayList<CaseEau> listePhare = new ArrayList<>();
    private ArrayList<CaseEau> listePhareIA = new ArrayList<>();
    private Phare() {
    }

    public static synchronized Phare getInstance() {
        if (phare == null) {
            phare = new Phare();
        }
        return phare;
    }

    public ArrayList<CaseEau> getListPhare(Partie partie) {
        listePhare = new ArrayList<>();
        for(int hauteur=0;hauteur<partie.getMap().getHauteur();hauteur++){
            for(int largeur=0; largeur<partie.getMap().getLargeur();largeur++){
                if(partie.getMap().getCase(hauteur,largeur).isPhare()){
                    listePhare.add((CaseEau)partie.getMap().getCase(hauteur,largeur));
                }
            }
        }
        return listePhare;
    }

    public ArrayList<CaseEau> getListPhareNonPossede(Partie partie, Player player){
        listePhareIA = new ArrayList<>();
        listePhare = new ArrayList<>();
        if(listePhare.size()==0){
            listePhare = getListPhare(partie);
        }

        for (CaseEau phare:listePhare) {
            if(phare.getPossedePhare()!=null){
                if(phare.getPossedePhare().toString().equals(partie.getCurrentPlayer().toString())) {
                } else {
                    listePhareIA.add(phare);
                }
            }  else {
                listePhareIA.add(phare);
            }

        }
        return listePhareIA;
    }
}