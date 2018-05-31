package fr.lesprogbretons.seawar.model.map;

import fr.lesprogbretons.seawar.ia.IAAleatoire;
import fr.lesprogbretons.seawar.ia.ia_seawar_a.PerfectAI;
import fr.lesprogbretons.seawar.ia.ia_seawar_b.AlphaBeta;
import fr.lesprogbretons.seawar.ia.ia_seawar_b.RechercheEltIA;
import fr.lesprogbretons.seawar.ia.ia_seawar_c.IAAlphaBeta;
import fr.lesprogbretons.seawar.ia.ia_seawar_d.AB;
import fr.lesprogbretons.seawar.ia.ia_seawar_e.GitGud;
import fr.lesprogbretons.seawar.ia.ia_seawar_f.IAGroupeF;
import fr.lesprogbretons.seawar.ia.ia_seawar_g.IA_Groupe_G;
import fr.lesprogbretons.seawar.ia.ia_seawar_h.IA;
import fr.lesprogbretons.seawar.ia.ia_seawar_i.IAPhare;
import fr.lesprogbretons.seawar.ia.ia_seawar_j.IACreole;
import fr.lesprogbretons.seawar.ia.ia_seawar_k.IA_K;
import fr.lesprogbretons.seawar.ia.ia_seawar_l.IAAdvanced;
import fr.lesprogbretons.seawar.model.Orientation;
import fr.lesprogbretons.seawar.model.Player;
import fr.lesprogbretons.seawar.model.boat.Amiral;
import fr.lesprogbretons.seawar.model.boat.Boat;
import fr.lesprogbretons.seawar.model.boat.Fregate;
import fr.lesprogbretons.seawar.model.cases.Case;
import fr.lesprogbretons.seawar.model.cases.CaseEau;
import fr.lesprogbretons.seawar.model.cases.CaseTerre;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Classe Grille
 */
public class Grille implements Serializable {
	 

    //Bateaux
    ArrayList<Boat> bateaux1;
    ArrayList<Boat> bateaux2;
	
	//public static Player defaultPlayer1 = new IAAleatoire(1,"IAA",new ArrayList<Boat>());
	//public static Player defaultPlayer2 = new IAAleatoire(2,"IAA",new ArrayList<Boat>());
	
	

    //Joueurs
    //    private Player joueur1 = new Player(1);
	private Player joueur1 = new IAAleatoire(1, "IAA", new ArrayList<Boat>());
	/* Groupe  A */
	//private Player joueur1 = new PerfectAI(1, "IAA", new ArrayList<Boat>());
	/* Groupe  B  */
	//private Player joueur1 = new RechercheEltIA(1, "IAA", new ArrayList<Boat>());
	/* Groupe  C */
	// private Player joueur1 = new IAAlphaBeta(1, "IAA", new ArrayList<Boat>());
	/* Groupe  D */
	//private Player joueur1 = new AB(1, "IAA", new ArrayList<Boat>());
	/* Groupe  E */
	//private Player joueur1 = new GitGud(1, "IAA", new ArrayList<Boat>());
	/* Groupe  F */
    //private Player joueur1 = new IAGroupeF(1, "IAA", new ArrayList<Boat>());
	/* Groupe  G */
    //private Player joueur1 = new IA_Groupe_G(1, "IAA", new ArrayList<Boat>());
	/* Groupe  H */
	//private Player joueur1 = new IA(1, "IAA", new ArrayList<Boat>());
	/* Groupe  I */
	//private Player joueur1 = new IAPhare(1,"IAA",new ArrayList<Boat>());
	/* Groupe  J */
	//private Player joueur1 = new IACreole(1, "IAA", new ArrayList<Boat>());
	/* Groupe  K */
	//private Player joueur1 = new IA_K(1, "IAA", new ArrayList<Boat>());
	/* Groupe  L */
	//private Player joueur1 = new IAAdvanced(1,"IAA",new ArrayList<Boat>());

    
      
      
      //private Player joueur2 = new IAAleatoire(2,"IAA",new ArrayList<Boat>());
	/* Groupe  A */
//    private Player joueur2 = new PerfectAI(2,"IAA",new ArrayList<Boat>());
  	/* Groupe  B  */
//      private Player joueur2 = new AlphaBeta(2,"IAA",new ArrayList<Boat>());
  	/* Groupe  C */
      private Player joueur2 = new IAAlphaBeta(2,"IAA",new ArrayList<Boat>());
  	/* Groupe  D */
//      private Player joueur2 = new AB(2,"IAA",new ArrayList<Boat>());
  	/* Groupe  E */
//     private Player joueur2 = new GitGud(2,"IAA",new ArrayList<Boat>());
  	/* Groupe  F */
//      private Player joueur2 = new IAGroupeF(2,"IAA",new ArrayList<Boat>());
  	/* Groupe  G */
//      private Player joueur2 = new IA_Groupe_G(2,"IAA",new ArrayList<Boat>());
  	/* Groupe  H */
//      private Player joueur2 = new IA(2,"IAA",new ArrayList<Boat>());
  	/* Groupe  I */
//      private Player joueur2 = new IAPhare(2,"IAA",new ArrayList<Boat>());
  	/* Groupe  J */
//      private Player joueur2 = new IACreole(2,"IAA",new ArrayList<Boat>());
  	/* Groupe  K */
//      private Player joueur2 = new IA_K(2,"IAA",new ArrayList<Boat>());
  	/* Groupe  L */
//      private Player joueur2 = new IAAdvanced(2,"IAA",new ArrayList<Boat>());
      
      
      
      
      
//    private Player joueur1 = defaultPlayer1;
//    private Player joueur2 = defaultPlayer2;
//    private Player joueur1;
//    private Player joueur2;


    //Cases de la Grille
    Case tableau[][];
    private int hauteur;
    private int largeur;

    /**
     * Constructeur
     *
     * @param hauteur : nombre de colonnes
     * @param largeur : nombre de lignes
     */
    public Grille(int hauteur, int largeur) {
        this.hauteur = hauteur;
        this.largeur = largeur;
     
        bateaux1 = (ArrayList<Boat>) joueur1.getBoats();
        bateaux2 = (ArrayList<Boat>) joueur2.getBoats();
        //
        //
        joueur1.setBoats(bateaux1);
        joueur2.setBoats(bateaux2);
        tableau = new Case[hauteur][largeur];

        for (int i = 0; i < hauteur; i++) {
            for (int j = 0; j < largeur; j++) {
                tableau[i][j] = new CaseEau(i, j);
            }
        }
    }

    public Grille() {
    }


    public Object clone() {
        Grille clone = new Grille(this.getHauteur(), this.getLargeur());
        for (int i = 0; i < hauteur; i++) {
            for (int j = 0; j < largeur; j++) {
                Case clone1 = (Case) this.tableau[i][j].clone();
                clone.tableau[i][j] = clone1;
            }
        }
        ArrayList<Boat> nav1 = new ArrayList<>();
        for (Boat nav : bateaux1) {
            nav1.add((Boat) nav.clone(clone.tableau[nav.getPosition().getX()][nav.getPosition().getY()]));
        }
        clone.setBateaux1(nav1);
        ArrayList<Boat> nav2 = new ArrayList<>();
        for (Boat nav : bateaux2) {
            nav2.add((Boat) nav.clone(clone.tableau[nav.getPosition().getX()][nav.getPosition().getY()]));
        }
        clone.setBateaux2(nav2);
        clone.setJoueur1((Player) this.joueur1.clone(nav1));
        clone.setJoueur2((Player) this.joueur2.clone(nav2));

        return clone;
    }

    /*---------------------------------------------------*/
    //Getters & Setters
   
   public void setTableau(Case[][] tableau) {
        this.tableau = tableau;
    }
    
    public Case[][] getCases(){
        return tableau;
    }

   
    public Player getJoueur1() {
        return joueur1;
    }

    public void setJoueur1(Player joueur1) {
        this.joueur1 = joueur1;
    }

    public Player getJoueur2() {
        return joueur2;
    }

    public void setJoueur2(Player joueur2) {
        this.joueur2 = joueur2;
    }

    public Case getCase(int hauteur, int largeur) throws ArrayIndexOutOfBoundsException {
        return tableau[hauteur][largeur];
    }

    public ArrayList<Boat> getBateaux1() {
        return bateaux1;
    }

    private void setBateaux1(ArrayList<Boat> bateaux1) {
        this.bateaux1 = bateaux1;
    }

    public ArrayList<Boat> getBateaux2() {
        return bateaux2;
    }

    public void setBateaux2(ArrayList<Boat> bateaux2) {
        this.bateaux2 = bateaux2;
    }

    public int getHauteur() {
        return hauteur;
    }

    public void setHauteur(int hauteur) {
        this.hauteur = hauteur;
    }

    public int getLargeur() {
        return largeur;
    }

    public void setLargeur(int largeur) {
        this.largeur = largeur;
    }

    //modifier une case de la grille
    public void setCase(Case c) throws ArrayIndexOutOfBoundsException {
        tableau[c.getX()][c.getY()] = c;
    }

    //ajouter un bateau joueur 1
    public void ajouterBateauJoueur1(Boat b) {
        ArrayList<Boat> tab = getBateaux1();
        tab.add(b);
        joueur1.addBoat(b);
        setBateaux1(tab);
        joueur1.setBoats(tab);

    }

    public void ajouterBateauJoueur2(Boat b) {
        ArrayList<Boat> tab = getBateaux2();
        tab.add(b);
        joueur2.addBoat(b);
        setBateaux2(tab);


    }

    /*******************************************************/
    //Fonctions permettant d'avoir les cases voisines
    private Case getCaseNord(Case c) {
        if (c == null) {
            return null;
        }
        int x;
        int y;
        Case cas;
        x = c.getX();
        y = c.getY();
        if (x + 1 >= 0 && x + 1 <= hauteur - 1) {
            cas = getCase((x + 1), y);
            return cas;
        }
        return null;
    }

    private Case getCaseSud(Case c) {
        if (c == null) {
            return null;
        }
        int x;
        int y;
        Case cas;
        x = c.getX();
        y = c.getY();
        if (x - 1 >= 0 && x - 1 <= hauteur - 1) {
            cas = getCase((x - 1), y);
            return cas;
        }
        return null;
    }

    private Case getCaseNordEst(Case c) {
        if (c == null) {
            return null;
        }
        int x;
        int y;
        Case cas;
        x = c.getX();
        y = c.getY();
        if (y % 2 == 0) {
            if (y + 1 >= 0 && y + 1 <= largeur - 1) {
                cas = getCase(x, (y + 1));
                return cas;
            }
        } else {
            if (x + 1 >= 0 && x + 1 <= hauteur - 1 && y + 1 >= 0 && y + 1 <= largeur - 1) {
                cas = getCase((x + 1), (y + 1));
                return cas;
            }
        }
        return null;
    }

    private Case getCaseSudEst(Case c) {
        if (c == null) {
            return null;
        }
        int x;
        int y;
        Case cas;
        x = c.getX();
        y = c.getY();
        if (y % 2 == 0) {
            if (x - 1 >= 0 && x - 1 <= hauteur - 1 && y + 1 >= 0 && y + 1 <= largeur - 1) {
                cas = getCase((x - 1), (y + 1));
                return cas;
            }
        } else {
            if (y - 1 >= 0 && y + 1 <= largeur - 1) {
                cas = getCase(x, (y + 1));
                return cas;
            }
        }
        return null;
    }

    private Case getCaseNordOuest(Case c) {
        if (c == null) {
            return null;
        }
        int x;
        int y;
        Case cas;
        x = c.getX();
        y = c.getY();
        if (y % 2 == 0) {
            if (y - 1 >= 0 && y - 1 <= largeur - 1) {
                cas = getCase(x, (y - 1));
                return cas;
            }
        } else {
            if (x + 1 >= 0 && x + 1 <= hauteur - 1 && y - 1 >= 0 && y - 1 <= hauteur - 1) {
                cas = getCase((x + 1), (y - 1));
                return cas;
            }
        }
        return null;
    }

    private Case getCaseSudOuest(Case c) {
        if (c == null) {
            return null;
        }
        int x;
        int y;
        Case cas;
        x = c.getX();
        y = c.getY();
        if (y % 2 == 0) {
            if (x - 1 >= 0 && x - 1 <= hauteur - 1 && y - 1 >= 0 && y - 1 <= largeur - 1) {
                cas = getCase((x - 1), (y - 1));
                return cas;
            }
        } else {
            if (y - 1 >= 0 && y - 1 <= hauteur - 1) {
                cas = getCase(x, (y - 1));
                return cas;
            }
        }
        return null;
    }

    /*----------------------------------------------------------------------------*/

    /**
     * Procédure permettant d'avoir toutes les accessibles depuis une case à une certaine distance
     *
     * @param c     : Case dont on veut les cases voisines
     * @param range : Distance à laquelle on veut les voisins
     * @param tab   : Tableau contenant les cases voisines
     */
    private void getCasesDisponible(Case c, int range, ArrayList<Case> tab) {
        if (c != null) {
            if (!(tab.contains(c)) && !(c instanceof CaseTerre) && !(casePossedeBateaux(c))) {
                tab.add(c);
            }

            if (range != 0 && !(c instanceof CaseTerre) && !(casePossedeBateaux(c))) {
                if (c.getX() > 0) {
                    getCasesDisponible(getCaseSud(c), (range - 1), tab);
                }
                if (c.getY() < largeur - 1) {
                    if ((c.getY() % 2 == 0 && c.getX() > 0) || c.getY() % 2 == 1) {
                        getCasesDisponible(getCaseSudEst(c), (range - 1), tab);
                    }
                }
                if (c.getY() < largeur - 1) {
                    if ((c.getY() % 2 == 1 && c.getX() < hauteur - 1) || c.getY() % 2 == 0) {
                        getCasesDisponible(getCaseNordEst(c), (range - 1), tab);
                    }
                }
                if (c.getY() > 0) {
                    if ((c.getY() % 2 == 0 && c.getX() > 0) || c.getY() % 2 == 1) {
                        getCasesDisponible(getCaseSudOuest(c), (range - 1), tab);
                    }
                }
                if (c.getY() > 0) {
                    if ((c.getY() % 2 == 1 && c.getX() < hauteur - 1) || c.getY() % 2 == 0) {
                        getCasesDisponible(getCaseNordOuest(c), (range - 1), tab);
                    }
                }
                if (c.getX() < hauteur - 1) {
                    getCasesDisponible(getCaseNord(c), (range - 1), tab);
                }
            }
        }
    }

    /**
     * Fonction renvoyant un tableau de cases contenant toutes les cases sur lesquelles un bateau peut se déplacer depuis une case voulue
     *
     * @param c     : case de départ
     * @param range : distance à laquelle on veut se déplacer
     * @return un tableau contenant toutes les cases sur lesquelles on peut se déplacer
     */
    public ArrayList<Case> getCasesDisponibles(Case c, int range) {
        ArrayList<Case> tab = new ArrayList<>();
        if (range > 0) {
            if (bateauSurCase(c).getOrientation() == Orientation.NORD) {
                getCasesDisponible(getCaseNordOuest(c), range - 1, tab);
                getCasesDisponible(getCaseNord(c), range - 1, tab);
                getCasesDisponible(getCaseNordEst(c), range - 1, tab);
            } else if (bateauSurCase(c).getOrientation() == Orientation.NORDOUEST) {
                getCasesDisponible(getCaseSudOuest(c), range - 1, tab);
                getCasesDisponible(getCaseNord(c), range - 1, tab);
                getCasesDisponible(getCaseNordOuest(c), range - 1, tab);
            } else if (bateauSurCase(c).getOrientation() == Orientation.SUDOUEST) {
                getCasesDisponible(getCaseSudOuest(c), range - 1, tab);
                getCasesDisponible(getCaseSud(c), range - 1, tab);
                getCasesDisponible(getCaseNordOuest(c), range - 1, tab);
            } else if (bateauSurCase(c).getOrientation() == Orientation.SUD) {
                getCasesDisponible(getCaseSudOuest(c), range - 1, tab);
                getCasesDisponible(getCaseSud(c), range - 1, tab);
                getCasesDisponible(getCaseSudEst(c), range - 1, tab);
            } else if (bateauSurCase(c).getOrientation() == Orientation.SUDEST) {
                getCasesDisponible(getCaseNordEst(c), range - 1, tab);
                getCasesDisponible(getCaseSud(c), range - 1, tab);
                getCasesDisponible(getCaseSudEst(c), range - 1, tab);
            } else if (bateauSurCase(c).getOrientation() == Orientation.NORDEST) {
                getCasesDisponible(getCaseNordEst(c), range - 1, tab);
                getCasesDisponible(getCaseNord(c), range - 1, tab);
                getCasesDisponible(getCaseSudEst(c), range - 1, tab);
            }
        }
        return tab;
    }

    /**
     * Fonction permettant de savoir si un bateau est sur une case
     *
     * @param c : Case dont on veut savoir si elle possède un bateau ou non
     * @return true si la case possède un bateau, false sinon
     */
    public boolean casePossedeBateaux(Case c) {
        for (Boat aBateaux1 : bateaux1) {
            if (aBateaux1.getPosition() == c) {
                return true;
            }
        }

        for (Boat aBateaux2 : bateaux2) {
            if (aBateaux2.getPosition() == c) {
                return true;
            }
        }

        return false;
    }

    /**
     * Fonction permettant de savoir si une case possède un bateau d'un joueur particulier
     *
     * @param c      : case donc on veut savoir si elle possède un bateau d'un joueur
     * @param joueur : joueur
     * @return true si la case possède un bateau du joueur, false sinon
     */
    public boolean casePossedeBateau(Case c, Player joueur) {
        if (joueur.getNumber() == 1) {
            for (Boat aBateaux1 : bateaux1) {
                if (aBateaux1.getPosition() == c) {
                    return true;
                }
            }
        } else {
            for (Boat aBateaux2 : bateaux2) {
                if (aBateaux2.getPosition() == c) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Fonction renvoyant le bateau qui est sur la case
     *
     * @param c : Case
     * @return le bateau qui se situe sur la case
     */
    public Boat bateauSurCase(Case c) {
        for (Boat aBateaux1 : bateaux1) {
            if (aBateaux1.getPosition() == c) {
                return aBateaux1;
            }
        }

        for (Boat aBateaux2 : bateaux2) {
            if (aBateaux2.getPosition() == c) {
                return aBateaux2;
            }
        }

        return null;
    }

    /**
     * Fonction renvoyant un tableau contenant les cases à portée de tir du bateau
     *
     * @param bateauSelectionne
     * @return un tableau contenant les cases a portee de tir du bateau
     */
    public ArrayList<Case> getCasesPortees(Boat bateauSelectionne) {
        ArrayList<Case> casesPorteeTir = new ArrayList<>();
        ArrayList<Case> casesPorteeTirfinal = new ArrayList<>();
        if (bateauSelectionne instanceof Amiral) {
            if (bateauSelectionne.getCanonSelectionne() == 1) {
                if (bateauSelectionne.getOrientation() == Orientation.NORD) {
                    casesPorteeTir.add(this.getCaseNord(bateauSelectionne.getPosition()));
                    casesPorteeTir.add(this.getCaseNord(getCaseNord(bateauSelectionne.getPosition())));
                    casesPorteeTir.add(this.getCaseNord(getCaseNord(getCaseNord(bateauSelectionne.getPosition()))));
                    casesPorteeTir.add(this.getCaseNord(getCaseNord(getCaseNord(getCaseNord(bateauSelectionne.getPosition())))));
                } else if (bateauSelectionne.getOrientation() == Orientation.NORDEST) {
                    casesPorteeTir.add(this.getCaseNordEst(bateauSelectionne.getPosition()));
                    casesPorteeTir.add(this.getCaseNordEst(getCaseNordEst(bateauSelectionne.getPosition())));
                    casesPorteeTir.add(this.getCaseNordEst(getCaseNordEst(getCaseNordEst(bateauSelectionne.getPosition()))));
                    casesPorteeTir.add(this.getCaseNordEst(getCaseNordEst(getCaseNordEst(getCaseNordEst(bateauSelectionne.getPosition())))));
                } else if (bateauSelectionne.getOrientation() == Orientation.SUDEST) {
                    casesPorteeTir.add(this.getCaseSudEst(bateauSelectionne.getPosition()));
                    casesPorteeTir.add(this.getCaseSudEst(getCaseSudEst(bateauSelectionne.getPosition())));
                    casesPorteeTir.add(this.getCaseSudEst(getCaseSudEst(getCaseSudEst(bateauSelectionne.getPosition()))));
                    casesPorteeTir.add(this.getCaseSudEst(getCaseSudEst(getCaseSudEst(getCaseSudEst(bateauSelectionne.getPosition())))));
                } else if (bateauSelectionne.getOrientation() == Orientation.SUD) {
                    casesPorteeTir.add(this.getCaseSud(bateauSelectionne.getPosition()));
                    casesPorteeTir.add(this.getCaseSud(getCaseSud(bateauSelectionne.getPosition())));
                    casesPorteeTir.add(this.getCaseSud(getCaseSud(getCaseSud(bateauSelectionne.getPosition()))));
                    casesPorteeTir.add(this.getCaseSud(getCaseSud(getCaseSud(getCaseSud(bateauSelectionne.getPosition())))));
                } else if (bateauSelectionne.getOrientation() == Orientation.SUDOUEST) {
                    casesPorteeTir.add(this.getCaseSudOuest(bateauSelectionne.getPosition()));
                    casesPorteeTir.add(this.getCaseSudOuest(getCaseSudOuest(bateauSelectionne.getPosition())));
                    casesPorteeTir.add(this.getCaseSudOuest(getCaseSudOuest(getCaseSudOuest(bateauSelectionne.getPosition()))));
                    casesPorteeTir.add(this.getCaseSudOuest(getCaseSudOuest(getCaseSudOuest(getCaseSudOuest(bateauSelectionne.getPosition())))));
                } else if (bateauSelectionne.getOrientation() == Orientation.NORDOUEST) {
                    casesPorteeTir.add(this.getCaseNordOuest(bateauSelectionne.getPosition()));
                    casesPorteeTir.add(this.getCaseNordOuest(getCaseNordOuest(bateauSelectionne.getPosition())));
                    casesPorteeTir.add(this.getCaseNordOuest(getCaseNordOuest(getCaseNordOuest(bateauSelectionne.getPosition()))));
                    casesPorteeTir.add(this.getCaseNordOuest(getCaseNordOuest(getCaseNordOuest(getCaseNordOuest(bateauSelectionne.getPosition())))));
                }
            } else {
                if (bateauSelectionne.getOrientation() == Orientation.NORD) {
                    casesPorteeTir.add(this.getCaseNord(bateauSelectionne.getPosition()));
                    casesPorteeTir.add(this.getCaseNord(getCaseNord(bateauSelectionne.getPosition())));
                    casesPorteeTir.add(this.getCaseNordEst(bateauSelectionne.getPosition()));
                    casesPorteeTir.add(this.getCaseNordOuest(bateauSelectionne.getPosition()));
                    casesPorteeTir.add(this.getCaseNord(getCaseNordEst(bateauSelectionne.getPosition())));
                    casesPorteeTir.add(this.getCaseNord(getCaseNordOuest(bateauSelectionne.getPosition())));
                } else if (bateauSelectionne.getOrientation() == Orientation.NORDEST) {
                    casesPorteeTir.add(this.getCaseNordEst(bateauSelectionne.getPosition()));
                    casesPorteeTir.add(this.getCaseNordEst(getCaseNordEst(bateauSelectionne.getPosition())));
                    casesPorteeTir.add(this.getCaseNord(bateauSelectionne.getPosition()));
                    casesPorteeTir.add(this.getCaseNord(getCaseNordEst(bateauSelectionne.getPosition())));
                    casesPorteeTir.add(this.getCaseSudEst(bateauSelectionne.getPosition()));
                    casesPorteeTir.add(this.getCaseNordEst(getCaseSudEst(bateauSelectionne.getPosition())));
                } else if (bateauSelectionne.getOrientation() == Orientation.SUDEST) {
                    casesPorteeTir.add(this.getCaseSudEst(bateauSelectionne.getPosition()));
                    casesPorteeTir.add(this.getCaseSudEst(getCaseSudEst(bateauSelectionne.getPosition())));
                    casesPorteeTir.add(this.getCaseSud(bateauSelectionne.getPosition()));
                    casesPorteeTir.add(this.getCaseSud(getCaseSudEst(bateauSelectionne.getPosition())));
                    casesPorteeTir.add(this.getCaseNordEst(bateauSelectionne.getPosition()));
                    casesPorteeTir.add(this.getCaseSudEst(getCaseNordEst(bateauSelectionne.getPosition())));
                } else if (bateauSelectionne.getOrientation() == Orientation.SUD) {
                    casesPorteeTir.add(this.getCaseSud(bateauSelectionne.getPosition()));
                    casesPorteeTir.add(this.getCaseSud(getCaseSud(bateauSelectionne.getPosition())));
                    casesPorteeTir.add(this.getCaseSudOuest(bateauSelectionne.getPosition()));
                    casesPorteeTir.add(this.getCaseSudEst(bateauSelectionne.getPosition()));
                    casesPorteeTir.add(this.getCaseSud(getCaseSudOuest(bateauSelectionne.getPosition())));
                    casesPorteeTir.add(this.getCaseSud(getCaseSudEst(bateauSelectionne.getPosition())));
                } else if (bateauSelectionne.getOrientation() == Orientation.SUDOUEST) {
                    casesPorteeTir.add(this.getCaseSudOuest(bateauSelectionne.getPosition()));
                    casesPorteeTir.add(this.getCaseSudOuest(getCaseSudOuest(bateauSelectionne.getPosition())));
                    casesPorteeTir.add(this.getCaseSud(bateauSelectionne.getPosition()));
                    casesPorteeTir.add(this.getCaseSudOuest(getCaseNordOuest(bateauSelectionne.getPosition())));
                    casesPorteeTir.add(this.getCaseNordOuest(bateauSelectionne.getPosition()));
                    casesPorteeTir.add(this.getCaseSudOuest(getCaseSud(bateauSelectionne.getPosition())));
                } else if (bateauSelectionne.getOrientation() == Orientation.NORDOUEST) {
                    casesPorteeTir.add(this.getCaseSudOuest(bateauSelectionne.getPosition()));
                    casesPorteeTir.add(this.getCaseNordOuest(getCaseSudOuest(bateauSelectionne.getPosition())));
                    casesPorteeTir.add(this.getCaseNordOuest(bateauSelectionne.getPosition()));
                    casesPorteeTir.add(this.getCaseNordOuest(getCaseNordOuest(bateauSelectionne.getPosition())));
                    casesPorteeTir.add(this.getCaseNord(bateauSelectionne.getPosition()));
                    casesPorteeTir.add(this.getCaseNordOuest(getCaseNord(bateauSelectionne.getPosition())));
                }
            }

        } else if (bateauSelectionne instanceof Fregate) {
            if (bateauSelectionne.getCanonSelectionne() == 1) {
                if (bateauSelectionne.getOrientation() == Orientation.NORD) {
                    casesPorteeTir.add(getCaseNord(bateauSelectionne.getPosition()));
                    casesPorteeTir.add(getCaseNordOuest(bateauSelectionne.getPosition()));
                    casesPorteeTir.add(getCaseNordEst(bateauSelectionne.getPosition()));
                    casesPorteeTir.add(getCaseSudEst(bateauSelectionne.getPosition()));
                    casesPorteeTir.add(getCaseSudOuest(bateauSelectionne.getPosition()));
                    casesPorteeTir.add(getCaseNord(getCaseNord(bateauSelectionne.getPosition())));
                } else if (bateauSelectionne.getOrientation() == Orientation.NORDEST) {
                    casesPorteeTir.add(getCaseNord(bateauSelectionne.getPosition()));
                    casesPorteeTir.add(getCaseNordOuest(bateauSelectionne.getPosition()));
                    casesPorteeTir.add(getCaseNordEst(bateauSelectionne.getPosition()));
                    casesPorteeTir.add(getCaseSudEst(bateauSelectionne.getPosition()));
                    casesPorteeTir.add(getCaseSud(bateauSelectionne.getPosition()));
                    casesPorteeTir.add(getCaseNordEst(getCaseNordEst(bateauSelectionne.getPosition())));
                } else if (bateauSelectionne.getOrientation() == Orientation.SUDEST) {
                    casesPorteeTir.add(getCaseNord(bateauSelectionne.getPosition()));
                    casesPorteeTir.add(getCaseSudOuest(bateauSelectionne.getPosition()));
                    casesPorteeTir.add(getCaseNordEst(bateauSelectionne.getPosition()));
                    casesPorteeTir.add(getCaseSudEst(bateauSelectionne.getPosition()));
                    casesPorteeTir.add(getCaseSud(bateauSelectionne.getPosition()));
                    casesPorteeTir.add(getCaseSudEst(getCaseSudEst(bateauSelectionne.getPosition())));
                } else if (bateauSelectionne.getOrientation() == Orientation.SUD) {
                    casesPorteeTir.add(getCaseNordOuest(bateauSelectionne.getPosition()));
                    casesPorteeTir.add(getCaseSudOuest(bateauSelectionne.getPosition()));
                    casesPorteeTir.add(getCaseNordEst(bateauSelectionne.getPosition()));
                    casesPorteeTir.add(getCaseSudEst(bateauSelectionne.getPosition()));
                    casesPorteeTir.add(getCaseSud(bateauSelectionne.getPosition()));
                    casesPorteeTir.add(getCaseSud(getCaseSud(bateauSelectionne.getPosition())));
                } else if (bateauSelectionne.getOrientation() == Orientation.SUDOUEST) {
                    casesPorteeTir.add(getCaseNord(bateauSelectionne.getPosition()));
                    casesPorteeTir.add(getCaseSudOuest(bateauSelectionne.getPosition()));
                    casesPorteeTir.add(getCaseNordOuest(bateauSelectionne.getPosition()));
                    casesPorteeTir.add(getCaseSudEst(bateauSelectionne.getPosition()));
                    casesPorteeTir.add(getCaseSud(bateauSelectionne.getPosition()));
                    casesPorteeTir.add(getCaseSudOuest(getCaseSudOuest(bateauSelectionne.getPosition())));
                } else if (bateauSelectionne.getOrientation() == Orientation.NORDOUEST) {
                    casesPorteeTir.add(getCaseNord(bateauSelectionne.getPosition()));
                    casesPorteeTir.add(getCaseNordOuest(bateauSelectionne.getPosition()));
                    casesPorteeTir.add(getCaseNordEst(bateauSelectionne.getPosition()));
                    casesPorteeTir.add(getCaseSudOuest(bateauSelectionne.getPosition()));
                    casesPorteeTir.add(getCaseSud(bateauSelectionne.getPosition()));
                    casesPorteeTir.add(getCaseNordOuest(getCaseNordOuest(bateauSelectionne.getPosition())));
                }
            } else {
                casesPorteeTir.add(getCaseNord(bateauSelectionne.getPosition()));
                casesPorteeTir.add(getCaseNordOuest(bateauSelectionne.getPosition()));
                casesPorteeTir.add(getCaseNordEst(bateauSelectionne.getPosition()));
                casesPorteeTir.add(getCaseSudOuest(bateauSelectionne.getPosition()));
                casesPorteeTir.add(getCaseSud(bateauSelectionne.getPosition()));
                casesPorteeTir.add(getCaseSudEst(bateauSelectionne.getPosition()));
            }
        }

        for (Case aCasesPorteeTir : casesPorteeTir) {
            if (aCasesPorteeTir != null) {
                casesPorteeTirfinal.add(aCasesPorteeTir);
            }
        }
        return casesPorteeTirfinal;
    }

    public ArrayList<Case> getBoatInRange(Boat bateauSelectionne, Player p) {
        ArrayList<Case> myCases = new ArrayList<>();

        for (Case c : getCasesPortees(bateauSelectionne)) {
            if (casePossedeBateau(c, p)) {
                myCases.add(c);
            }
        }
        return myCases;
    }

    /**
     * procédure permettant à un joueur de prendre un phare sur une case
     *
     * @param c      : Case sur laquelle il y a un phare
     * @param joueur : joueur qui prend le phare
     */
    public void prendPhare(Case c, Player joueur) {
        //si personne ne possede le phare
        if (c.getPossedePhare() == null) {
            c.setPossedePhare(joueur);
            joueur.setPharesPossedes(joueur.getPharesPossedes() + 1);

            //si le joueur adverse possede le phare

        } else if (c.getPossedePhare() != joueur) {
            c.getPossedePhare().setPharesPossedes(c.getPossedePhare().getPharesPossedes() - 1);
            c.setPossedePhare(joueur);
            joueur.setPharesPossedes(joueur.getPharesPossedes() + 1);
        }
    }

    public Case[][] getTableau(){
        return this.tableau;
    }
}
