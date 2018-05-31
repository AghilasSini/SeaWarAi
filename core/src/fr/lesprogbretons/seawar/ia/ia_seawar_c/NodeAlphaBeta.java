package fr.lesprogbretons.seawar.ia.ia_seawar_c;

import fr.lesprogbretons.seawar.model.actions.Action;
import fr.lesprogbretons.seawar.model.actions.MoveBoat;
import fr.lesprogbretons.seawar.model.boat.Boat;
import fr.lesprogbretons.seawar.model.cases.Case;
import fr.lesprogbretons.seawar.model.map.Grille;
import fr.lesprogbretons.seawar.controller.*;

import javax.naming.ldap.Control;
import java.util.ArrayList;
import java.lang.*;

import java.util.ArrayList;
import java.util.logging.Logger;

import static fr.lesprogbretons.seawar.SeaWar.logger;


public class NodeAlphaBeta {
    final int DEPTH_MAX = 2;
    private Controller control;
    private boolean MAX;
    private int depth, alpha, beta;


    public NodeAlphaBeta(Controller control, boolean MAX, int depth, int alpha, int beta) {
        this.control = control;
        this.MAX = MAX;
        this.depth = depth;
        this.alpha = alpha;
        this.beta = beta;
    }


    public int alphabeta() {
        if (this.getDepth() == DEPTH_MAX) {
            //Calcul de la distance max entre 1 phare et 1 bateau sur la map moins la distance actuelle entre le bateau et le phare
            Grille grille = new Grille();
            //Récupération de la grille de jeu
            grille = (Grille) (this.control.getPartie().getMap().clone());
            //Utilité(n)
            int utilite = 30 - this.nbDeplacementVolOiseau(this.getControl().getPartie().getBateauSelectionne().getPosition(), grille);
            return utilite;

        } else {
            if (this.isMAX()) {
                ArrayList<NodeAlphaBeta> fils = new ArrayList<>();
                //On récupère notre bateau
                Boat boat = this.control.getPartie().getBateauSelectionne();
                ArrayList<Case> cases = new ArrayList<>();
                //Si le bateau à au moins 1 déplacment possible
                if (boat.getMoveAvailable() != 0 && !this.control.getPartie().getMap().getCasesDisponibles(boat.getPosition(), 1).isEmpty()) {
                    //On récupère la liste des déplacements possibles
                    cases = this.control.getPartie().getMap().getCasesDisponibles(boat.getPosition(), 1);
                } else {

                    logger.debug("no case are available");
                }

                if (!cases.isEmpty()) {
                    for (Case c : cases) {
                        //Création des fils
                        Controller control_fils = new Controller();
                        control_fils = (Controller) this.control.clone();
                        control_fils.getPartie().getBateauSelectionne().moveBoat(c);
                        fils.add(new NodeAlphaBeta(control_fils, false, depth + 1, this.alpha, this.beta));
                    }
                }

                int i = 0, indice_case = -1, alpha_bis;
                //On parcourt la liste des fils
                while (i < fils.size() && this.alpha < this.beta) {
                    alpha_bis = this.alpha;
                    this.alpha = (Math.max(this.alpha, fils.get(i).alphabeta()));
                    if (alpha_bis != this.alpha) {
                        //On récupère le meilleur déplacement possible à chaque itération
                        indice_case = i;
                    }
                    i++;
                }
                if (this.depth == 1) {
                    //Si on est à la racine de l'arbre, on renvoit le numéro de la case où le bateau doit se déplacer
                   return indice_case;
                }
                //Sinon on renvoit le alpha au père
                return this.alpha;
            } else {
                ArrayList<NodeAlphaBeta> fils = new ArrayList<>();
                Boat boat = this.control.getPartie().getBateauSelectionne();
                ArrayList<Case> cases = new ArrayList<>();
                //Si le bateau à au moins 1 déplacment possible
                if (boat.getMoveAvailable() != 0 && !this.control.getPartie().getMap().getCasesDisponibles(boat.getPosition(), 1).isEmpty()) {
                    //On récupère la liste des déplacements possibles
                    cases = this.control.getPartie().getMap().getCasesDisponibles(boat.getPosition(), 1);
                } else {
                    logger.debug("no case are available");
                }

                if (!cases.isEmpty()) {
                    for (Case c : cases) {
                        //On clone le controlleur actuel avant de simuler le déplacement de notre bateau
                        Controller control_fils = new Controller();
                        control_fils = (Controller) this.control.clone();
                        control_fils.getPartie().getBateauSelectionne().moveBoat(c);
                        fils.add(new NodeAlphaBeta(control_fils, true, this.depth + 1, this.alpha, this.beta));
                    }
                }

                int i = 0, indice_case = -1, betaBis;
                //On parcourt la liste des fils
                while (i < fils.size() && this.getAlpha() < this.getBeta()) {
                    betaBis = this.beta;
                    //Application de l'algorithme sur le fils nouvellement généré
                    this.beta = (Math.min(this.beta, fils.get(i).alphabeta()));
                    if (betaBis != this.beta) {
                        //On récupère le meilleur déplacement possible à chaque itération
                        indice_case = i;
                    }
                    i++;
                }
                if (this.depth == 1) {
                    //Si on est à la racine de l'arbre, on renvoit le numéro de la case où le bateau doit se déplacer
                    return indice_case;
                }
                //Sinon on renvoit le beta au père
                return this.beta;
            }
        }
    }

    public void setControl(Controller control) {
        this.control = control;
    }

    public Controller getControl() {
        return control;
    }

    public void setGame(Controller control) {
        this.control = control;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getAlpha() {
        return alpha;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    public int getBeta() {
        return beta;
    }

    public void setBeta(int beta) {
        this.beta = beta;
    }


    public boolean isMAX() {
        return MAX;
    }

    public void setMAX(boolean MAX) {
        this.MAX = MAX;
    }

    public Object clone() {

        Controller controlClone = (Controller) this.control.clone();
        return new NodeAlphaBeta(controlClone, MAX, depth, alpha, beta);
    }

    private int nbDeplacementVolOiseau(Case caseBat, Grille grille) {
        int i;
        int l;
        int xb;
        int yb;
        int xp;
        int yp;
        int distX;
        int distY;
        int distMin = 0;
        ArrayList<Case> tabPhare = this.listePhareDispo(grille);
        ArrayList<Integer> distCasePhares = new ArrayList<Integer>();

        xb = caseBat.getX();
        yb = caseBat.getY();

        for (l = 0; l < tabPhare.size(); l++) {
            xp = tabPhare.get(l).getX();
            yp = tabPhare.get(l).getY();
            distX = abs(xb - xp);
            distY = abs(yb - yp);
            for (i = 0; i < distY; i = i + 2) {
                if (distY - i >= 2 && distX > 0) {
                    distX = distX - 1;
                }
            }
            distCasePhares.add(distX + distY);
        }
        for (i = 0; i < distCasePhares.size(); i++) {
            if (i == 0) {
                distMin = distCasePhares.get(i);
            } else {
                if (distCasePhares.get(i) < distMin) {
                    distMin = distCasePhares.get(i);
                }
            }
        }

        return distMin;
    }

    private static int abs(int i) {
        if (i < 0) {
            i = -i;
        }
        return i;
    }

    private ArrayList<Case> listePhareDispo(Grille grille) {
        int i;
        int j;
        ArrayList<Case> tableauDesPhares = new ArrayList<Case>();
        for (i = 0; i < grille.getHauteur(); i++) {
            for (j = 0; j < grille.getLargeur(); j++) {
                if (grille.getCase(i, j).isPhare() && grille.getCase(i, j).getPossedePhare() == null) {
                    tableauDesPhares.add(grille.getCase(i, j));
                }
            }
        }
        return tableauDesPhares;

    }
}
