package fr.lesprogbretons.seawar.ia.ia_seawar_g.alphabeta;

import fr.lesprogbretons.seawar.ia.ia_seawar_g.etat.Etat;
import fr.lesprogbretons.seawar.model.Partie;
import fr.lesprogbretons.seawar.model.Player;
import fr.lesprogbretons.seawar.model.actions.Action;
import fr.lesprogbretons.seawar.model.boat.Boat;
import fr.lesprogbretons.seawar.model.cases.Case;
import fr.lesprogbretons.seawar.model.map.Grille;

import java.util.HashSet;

public abstract class Noeud {

    protected Etat etat;
    protected HashSet<Noeud> fils;
    protected Action action;
    protected Noeud bestNoeud;

    protected int val=Integer.MAX_VALUE;//alpha ou beta selon si c'est un noeud min ou max
    //protected Joueur joueur


    /**
     * Constructeur :
     */
    public Noeud(Etat etat) {
        this.etat = etat;
    }

    public abstract void genererFils();

    public abstract int alphabeta(int alpha, int beta);

    public abstract Noeud getBestNoeud();//TODO ATTENTION! marche uniquement si un joueur fait une unique action



    public int getVal(){
        return val;
    }

    public Action getAction() {
        return action;
    }

    public void setFils(HashSet<Noeud> fils) {
        this.fils = fils;
    }

    public HashSet<Noeud> getFils() {
        return fils;
    }

    /**On cherche a maximiser l'utilite*/
    public int utilite() {
        //TODO: Ameliorer l'heuristique

        Boat nav1 = etat.getPartie().getCurrentPlayer().getBoats().get(0);

        if(nbBoat(etat.getPartie().getCurrentPlayer())==0) {//Defaite par table rase
            return Integer.MIN_VALUE;//C'est le pire coup possible
        }
        if (nbBoat(etat.getPartie().getOtherPlayer())==0) {//victoire par table rase
            return Integer.MAX_VALUE;//Il n'y a pas de meilleur coup possible
        }
        if(getNbPharesPossede(etat.getPartie().getCurrentPlayer())==3) {//victoire par prise de phare
            return Integer.MAX_VALUE;//Il n'y a pas de meilleur coup possible
        }
        if(getNbPharesPossede(etat.getPartie().getOtherPlayer())==3) {//Defaite par prise de phare
            return Integer.MIN_VALUE;//C'est le pire coup possible
        }
        Boat nav2 = etat.getPartie().getOtherPlayer().getBoats().get(0);
        int coeffVieBoat1 = nav1.getHp()/nav1.getMaxHp() * 20;
        int coeffVieBoat2 = nav2.getHp()/nav2.getMaxHp() * 2000;
        int coeffPhare = -distNearestPhare(nav1)+cube(10*getNbPharesPossede(etat.getPartie().getCurrentPlayer()))
                - cube(10*getNbPharesPossede(etat.getPartie().getOtherPlayer()));


            return coeffPhare + coeffVieBoat1 - coeffVieBoat2;//utilite si aucune victoire d'un joueur dans cet etat

    }

    /**Fonction pour calculer un cube*/
    private int cube(int a){
        return a*a*a;
    }

    protected int nbBoat(Player p) {
        return p.getBoats().size();
    }


    public Etat getEtat() {
        return etat;
    }

    public void genererFils(int etage) {
        //System.out.println("generation d'un fils, etage: "+etage);
        if (etage > 0) {
            if (fils == null) {
                fils = new HashSet<Noeud>();
                genererFils();
            }
        }
        if (etage > 0) {
            for (Noeud noeud : fils) {
                noeud.genererFils(etage-1);
            }
        }
    }

    /**
     * Fonction permettant de trouver les 3 phares, utile pour l'heuristique
     */
    protected Case[] getPhares() {
        int i;
        int j;
        int num = 0;
        Partie partie=etat.getController().getPartie();
        Case[] cases;
        cases = new Case[]{null, null, null};
        Grille gril = partie.getMap();
        for (i = 0; i < gril.getHauteur(); i++) {
            for (j = 0; j < gril.getLargeur(); j++) {
                if (gril.getCase(i, j).isPhare()) {
                    cases[num] = gril.getCase(i, j);
                    num += 1;
                }
            }
        }
        return cases;
    }

    /**
     * Fonction indiquant la distance au phare non occupe par soi le plus proche
     * @param nav bateau concerne
     * @return La distance au phare le plus proche du bateau
     */
    protected int distNearestPhare(Boat nav) {
        int i;
        int minDist=Integer.MAX_VALUE;

        Case[] phares=getPhares();
        for(i=0;i<3;i++){//3 car il y toujours 3 phares
            if(phares[i]!=null) {//Il y a parfois moins de 3 phares
                if(phares[i].getPossedePhare()!=null){//Si le phare est possede par un joueur
                    if (phares[i].getPossedePhare().getNumber() != etat.getPartie().getCurrentPlayer().getNumber()) {//On ss'interesent uniquement aux phares n'appartenant pas au joueur courant (soi)
                        int dx = nav.getPosition().getX() - phares[i].getX();
                        int dy = nav.getPosition().getY() - phares[i].getY();
                        int dist = dx * dx + dy * dy;
                        if (dist < minDist) {
                            minDist = dist;
                        }
                    }
                }else{//Si le phare n'est a personne
                    int dx = nav.getPosition().getX() - phares[i].getX();
                    int dy = nav.getPosition().getY() - phares[i].getY();
                    int dist = dx * dx + dy * dy;
                    if (dist < minDist) {
                        minDist = dist;
                    }
                }
            }
        }
        if(minDist==Integer.MAX_VALUE){
            minDist=0;
        }
        return minDist;
    }

    /**
     * Fonction indiquant le nombre de phares possede par le joueur courant
     * @return le nombre de phares possede par le joueur courant
     */
    protected int getNbPharesPossede(Player p) {
        int i;
        int nbPhares=0;

        Case[] phares=getPhares();
        for(i=0;i<3;i++){//3 car le tableau est de taille 3 (nombre de phare en temps normal)
            if(phares[i]!=null) {//Il y a parfois moins de 3 phares
                if (phares[i].getPossedePhare() != null) {//Si le phare est possede par un joueur

                    if (phares[i].getPossedePhare().getNumber() == p.getNumber()) {//On ss'interesent uniquement aux phares appartenant au joueur courant (soi)
                        nbPhares += 1;

                    }
                }
            }
        }
        return nbPhares;
    }


    public void setAction(Action action) {
        this.action=action;
    }
}