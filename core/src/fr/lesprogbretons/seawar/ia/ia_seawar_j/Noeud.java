package fr.lesprogbretons.seawar.ia.ia_seawar_j;


import fr.lesprogbretons.seawar.controller.Controller;
import fr.lesprogbretons.seawar.model.Partie;
import fr.lesprogbretons.seawar.model.Player;
import fr.lesprogbretons.seawar.model.actions.Action;
import fr.lesprogbretons.seawar.model.boat.Boat;
import fr.lesprogbretons.seawar.model.cases.Case;
import fr.lesprogbretons.seawar.model.cases.CaseEau;

import java.lang.reflect.Array;
import java.util.*;

public class Noeud {

    private boolean heuristiqueCalculee = false;
    private float heuristique;
    private Controller etat;
    private ArrayList<Noeud> fils;
    private ArrayList<Action> actionsFils;
    private boolean max;
    private Action actionChoisie;
    private int profondeur;


    public Noeud(Controller etat, int profondeur, boolean max) {
        this.heuristique = -1;
        this.etat = (Controller) etat.clone();
        this.fils = null;
        this.max = max;
        this.actionChoisie = null;
        this.profondeur = profondeur;
    }


    public float getHeuristique() {
        if(heuristiqueCalculee){
            return heuristique;
        }else{
            heuristique = this.heuristique4();
            heuristiqueCalculee = true;
            return heuristique;
        }

    }


    public Action getActionChoisie() {
        return actionChoisie;
    }

    private void trierFils() {
        Collections.sort(fils, new Comparator<Noeud>() {
            public int compare(Noeud o1, Noeud o2) {
                if (o1.getHeuristique() < o2.getHeuristique()) {
                    return -1;
                } else if (o1.getHeuristique() > o2.getHeuristique()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
    }


    public Float heuristiqueGlouton(Partie partie){
        //ArrayList<Case> cases = new ArrayList<Case>();
        ArrayList<CaseEau> listePhare = Phare.getInstance().getListPhareNonPossede(partie, partie.getCurrentPlayer());
        Boat boat = partie.getJoueur1().getBoats().get(0);
        Float distance = Float.MAX_VALUE;
        CaseEau phareChoisi = null;
        //ArrayList<Object> retour = new ArrayList<>();
        //cases = partie.getMap().getCasesDisponibles(boat.getPosition(), 1);
        if(listePhare.size()>0) {
            Float calcul;
            for (CaseEau phare: listePhare
                    ) {
                calcul = (float) Math.sqrt(Math.pow((phare.getX()-boat.getPosition().getX()),2)+Math.pow((phare.getY()-boat.getPosition().getY()),2));
                    if(distance>calcul) {
                        distance = calcul;
                        phareChoisi = phare;
                    }
                }
            }


        if (phareChoisi!=null) {
            //System.out.println("Distance : " + distance.toString() + " au phare en pos : " + phareChoisi.getX() + "," + phareChoisi.getY() + "................................");
        }
        return distance;
    }

    public Float heuristique(Partie partie) {
        ArrayList<Case> cases = new ArrayList<Case>();
        ArrayList<CaseEau> listePhare = Phare.getInstance().getListPhareNonPossede(partie, partie.getCurrentPlayer());
        //Boat opponentBoat = partie.getOtherPlayer().getBoats().get(0);
        Boat boat = partie.getCurrentPlayer().getBoats().get(0);
        Float distance = Float.MAX_VALUE;
//        Float opponentDistance = Float.MAX_VALUE;
        CaseEau phareChoisi = null; //Pour sout si debuggage
        CaseEau opponentPhareChoisi = null; //Pour sout si debuggage
        //        Case caseChoisie = null;
//        cases = partie.getMap().getCasesDisponibles(boat.getPosition(), 1);
        if (listePhare.size() > 0) {
            Float calcul;
            //Float opponentCalcul;
            for (CaseEau phare : listePhare
                    ) {
                calcul = (float) Math.sqrt(Math.pow((phare.getX() - boat.getPosition().getX()), 2) + Math.pow((phare.getY() - boat.getPosition().getY()), 2));
                //opponentCalcul = (float) Math.sqrt(Math.pow((phare.getX()-opponentBoat.getPosition().getX()),2)+Math.pow((phare.getY()-opponentBoat.getPosition().getY()),2));
                if (distance > calcul) { //Distance du joueur actuel
                    distance = calcul;
                    phareChoisi = phare;
                }
                /*if (opponentDistance>opponentCalcul){ //Distance min de l'adversaire
                    opponentDistance = opponentCalcul;
                    opponentPhareChoisi = phare;

                }*/

            }
        }
        return distance;
    }

    public Float heuristique2(Partie partie) {
        ArrayList<Case> cases = new ArrayList<Case>();
        ArrayList<CaseEau> listePhare = Phare.getInstance().getListPhareNonPossede(partie, partie.getCurrentPlayer());
        ArrayList<CaseEau> listePhareOpponent = Phare.getInstance().getListPhareNonPossede(partie, partie.getOtherPlayer());
        Boat opponentBoat = partie.getOtherPlayer().getBoats().get(0);
        Boat boat = partie.getCurrentPlayer().getBoats().get(0);
        Float distance = 0.0f;
        Float opponentDistance = 0.0f;
        CaseEau phareChoisi = null; //Pour sout si debuggage. Same
        CaseEau opponentPhareChoisi = null; //Pour sout si debuggage. Inutile now
        Float calcul;
        Float opponentCalcul;
        //        Case caseChoisie = null;
//        cases = partie.getMap().getCasesDisponibles(boat.getPosition(), 1);
        if (listePhare.size() > 0) {
            for (CaseEau phare : listePhare
                    ) {
                calcul = (float) Math.sqrt(Math.pow((phare.getX() - boat.getPosition().getX()), 2) + Math.pow((phare.getY() - boat.getPosition().getY()), 2));
                distance += calcul;
            }
        }
        if (listePhareOpponent.size() > 0) {
            for (CaseEau phare : listePhareOpponent) {
                opponentCalcul = (float) Math.sqrt(Math.pow((phare.getX() - opponentBoat.getPosition().getX()), 2) + Math.pow((phare.getY() - opponentBoat.getPosition().getY()), 2));
                opponentDistance += opponentCalcul;
            }
        }

        System.out.println("Phare de l'adversaire choisi : " + opponentPhareChoisi.toString() + ", phare choisi : " + phareChoisi.toString());
        distance = distance - opponentDistance;
        return distance;
    }

    public Float heuristique3(){
        ArrayList<CaseEau> listePhare1 = Phare.getInstance().getListPhareNonPossede(etat.getPartie(), etat.getPartie().getCurrentPlayer());
        ArrayList<CaseEau> listePhare2 = Phare.getInstance().getListPhareNonPossede(etat.getPartie(), etat.getPartie().getOtherPlayer());
        System.out.println("Heuristique = " + (listePhare1.size()-listePhare2.size()));
        return (float)(listePhare1.size()-listePhare2.size());
    }

    public float heuristique4(){
        int heuristique = (etat.getPartie().getJoueur1().getPharesPossedes() - etat.getPartie().getJoueur2().getPharesPossedes());
        if(heuristique != 0){
            System.out.println("OWWOWOWOWOWOWOWOWOWOWOWOWOW");
        }
        return (float)heuristique;
    }

    /**
     * Applique Alpha-Beta met à jour memorisedAction.
     *
     * @return la valeur de l'action choisie
     */
    /*public int chooseAction(){
        ArrayList<Action> actions = (ArrayList<Action>) etat.getPossibleActions();
        for(Action a : actions){
            Partie nouvelEtat = (Partie)etat.clone();
            a.apply(nouvelEtat);
            Noeud suivant = new Noeud(nouvelEtat, alpha, beta, !max, ++profondeur);
            fils.add(suivant);
        }
        return 0;
    }*/

    protected float min(float alpha, float beta, int profondeurMax){
        if (this.profondeur >= profondeurMax) {
            return getHeuristique();
        } else {
            if (this.fils == null) {
                List<Action> actions = etat.getPossibleActions();
                fils = new ArrayList<>();
                actionsFils = new ArrayList<>();
                for (Action ac : actions) {
                    Controller nouvelEtat = (Controller) etat.clone();
                    ac.apply(nouvelEtat);
                    fils.add(new Noeud(nouvelEtat, this.profondeur + 1, !this.max));
                    actionsFils.add(ac);
                }
            }
        }
        float alphaRenvoye = Float.MAX_VALUE;
        int indiceMeilleur = 0;
        float meilleur = Float.MAX_VALUE;
        for(int i=0; i<fils.size(); i++){
            float valeur = fils.get(i).max(alpha, beta, profondeurMax);
            if(alphaRenvoye > valeur){
                alphaRenvoye = valeur;
                indiceMeilleur = i;
            }
            beta = (alphaRenvoye < beta) ? alphaRenvoye : beta;
            if(alpha > beta){
                actionChoisie = actionsFils.get(indiceMeilleur);
                break;
            }
        }
        actionChoisie = actionsFils.get(indiceMeilleur);
        return alphaRenvoye;
    }

    protected float max(float alpha, float beta, int profondeurMax){
        if (this.profondeur >= profondeurMax) {
            return getHeuristique();
        } else {
            if (this.fils == null) {
                List<Action> actions = etat.getPossibleActions();
                fils = new ArrayList<>();
                for (Action ac : actions) {
                    Controller nouvelEtat = (Controller) etat.clone();
                    ac.apply(nouvelEtat);
                    fils.add(new Noeud(nouvelEtat, this.profondeur + 1, !this.max));
                }
            }
        }
        float betaRenvoye = Float.MIN_VALUE;
        int indiceMeilleur = 0;
        float meilleur = Float.MAX_VALUE;
        for(int i=0; i<fils.size(); i++){
            float valeur = fils.get(i).min(alpha, beta, profondeurMax);
            if(betaRenvoye < valeur){
                betaRenvoye = valeur;
                indiceMeilleur = i;
            }
            beta = (betaRenvoye < beta) ? betaRenvoye : beta;
            if(alpha > beta){
                break;
            }
        }
        return betaRenvoye;
    }
/*
    protected float alphaBeta(int alpha, int beta, int profondeurMax) {
        if (this.profondeur >= profondeurMax) {
            return getHeuristique();
        } else {
            if (fils == null) {
                List<Action> actions = etat.getPossibleActions();
                fils = new ArrayList<>();
                for (Action ac : actions) {
                    Controller nouvelEtat = (Controller) etat.clone();
                    ac.apply(nouvelEtat);
                    fils.add(new Noeud(nouvelEtat, alpha, beta, this.profondeur + 1, !this.max, ac));
                }
            }
            System.out.println("Profondeur = " + profondeur + " Taille de fils = " + fils.size());
            float[] valeurs = new float[fils.size()];
            for (int i = 0; i < valeurs.length; i++) {
                valeurs[i] = fils.get(i).alphaBeta(alpha, beta, profondeurMax);
                if (max) {
                    if (valeurs[i] < this.beta) {
                        break;
                    }
                    if (valeurs[i] > this.alpha) {
                        this.alpha = valeurs[i];
                    }
                } else {
                    if (valeurs[i] > this.alpha) {
                        break;
                    }
                    if (valeurs[i] < this.beta) {
                        this.alpha = valeurs[i];
                    }
                }
            }

            float meilleur = valeurs[0];
            int indiceMeilleur = 0;
            if (max) {
                for (int i = 1; i < valeurs.length; i++) {
                    if (valeurs[i] > meilleur) {
                        meilleur = valeurs[i];
                        indiceMeilleur = i;
                    }
                }

            } else {
                for (int i = 1; i < valeurs.length; i++) {
                    if (valeurs[i] < meilleur) {
                        meilleur = valeurs[i];
                        indiceMeilleur = i;
                    }
                }
            }

            if (this.profondeur == 0) {
                this.actionChoisie = this.fils.get(indiceMeilleur).getActionPourVenir();
            }
        }

        return 0;
    }
*/
}
