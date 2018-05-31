package fr.lesprogbretons.seawar.ia.ia_seawar_f;

import fr.lesprogbretons.seawar.controller.Controller;
import fr.lesprogbretons.seawar.model.Player;
import fr.lesprogbretons.seawar.model.actions.Action;
import fr.lesprogbretons.seawar.model.actions.PassTurn;
import fr.lesprogbretons.seawar.model.boat.Boat;
import fr.lesprogbretons.seawar.model.cases.Case;
import fr.lesprogbretons.seawar.model.cases.CaseTerre;

import java.util.ArrayList;

public abstract class Node {

    protected Controller c;
    protected Action myAction;
    protected ArrayList<Node> fils = new ArrayList<>();
    protected Node bestNode;
    protected int heuristic;

    public Node(Controller c, Action myAction) {
        this.c = c;
        this.myAction = myAction;
    }

    public Action getMyAction() {
        return myAction;
    }

    public void setMyAction(Action myAction) {
        this.myAction = myAction;
    }

    public ArrayList<Node> getFils() {
        return fils;
    }

    public int getHeuristic() {
        return heuristic;
    }

    private ArrayList<Case> astar(Case c1, Case c2) {
        ArrayList<Case> closed = new ArrayList<>();
        ArrayList<Case> open = new ArrayList<>();
        open.add(c1);
        Case[][] camefrom = new Case[c.getPartie().getMap().getHauteur()][c.getPartie().getMap().getLargeur()];

        int[][] gscore = new int[c.getPartie().getMap().getHauteur()][c.getPartie().getMap().getLargeur()];
        for (int i = 0; i < gscore.length; i++) {
            for (int j = 0; j < gscore[0].length; j++) {
                gscore[i][j] = Integer.MAX_VALUE;
            }
        }
        gscore[c1.getX()][c1.getY()] = 0;

        int[][] fscore = new int[c.getPartie().getMap().getHauteur()][c.getPartie().getMap().getLargeur()];
        for (int i = 0; i < fscore.length; i++) {
            for (int j = 0; j < fscore[0].length; j++) {
                fscore[i][j] = Integer.MAX_VALUE;
            }
        }
        fscore[c1.getX()][c1.getY()] = distanceCases(c1, c2);
        Case current;

        while (!open.isEmpty()) {
            current = open.get(0);

            for (Case cas : open) {
                if (fscore[current.getX()][current.getY()] > fscore[cas.getX()][cas.getY()]) {
                    current = cas;
                }
            }

            open.remove(current);
            closed.add(current);

            if (current == c2) {
                return reconstruct_path(camefrom, current);
            }
            ArrayList<Case> neighbor = new ArrayList<>();
            getCasesDisponible(current, 1, neighbor);
            int tentative_gscore;
            for (Case c : neighbor) {
                if (closed.contains(c)) {
                    continue;
                }
//                if (!open.contains(c)) {
                if (!open.contains(c) && !(c instanceof CaseTerre)) {
                    open.add(c);
                }
                tentative_gscore = gscore[current.getX()][current.getY()] + 1;
                if (tentative_gscore >= gscore[c.getX()][c.getY()]) {
                    continue;
                }
                camefrom[c.getX()][c.getY()] = current;
                gscore[c.getX()][c.getY()] = tentative_gscore;
                fscore[c.getX()][c.getY()] = gscore[c.getX()][c.getY()] + distanceCases(c, c2);


            }

        }
        return new ArrayList<>();
    }

    private ArrayList<Case> reconstruct_path(Case[][] cameFrom, Case current) {
        ArrayList<Case> totalPath = new ArrayList<>();
        totalPath.add(current);
        while (cameFrom[current.getX()][current.getY()] != null) {
            current = cameFrom[current.getX()][current.getY()];
            totalPath.add(current);
        }
        return totalPath;
    }

    private Case getCaseNord(Case cc) {
        if (cc == null) {
            return null;
        }
        int x;
        int y;
        Case cas;
        x = cc.getX();
        y = cc.getY();
        if (x + 1 >= 0 && x + 1 <= c.getPartie().getMap().getHauteur() - 1) {
            cas = c.getPartie().getMap().getCase((x + 1), y);
            return cas;
        }
        return null;
    }

    private Case getCaseSud(Case cc) {
        if (cc == null) {
            return null;
        }
        int x;
        int y;
        Case cas;
        x = cc.getX();
        y = cc.getY();
        if (x - 1 >= 0 && x - 1 <= c.getPartie().getMap().getHauteur() - 1) {
            cas = c.getPartie().getMap().getCase((x - 1), y);
            return cas;
        }
        return null;
    }

    private Case getCaseNordEst(Case cc) {
        if (cc == null) {
            return null;
        }
        int x;
        int y;
        Case cas;
        x = cc.getX();
        y = cc.getY();
        if (y % 2 == 0) {
            if (y + 1 >= 0 && y + 1 <= c.getPartie().getMap().getLargeur() - 1) {
                cas = c.getPartie().getMap().getCase(x, (y + 1));
                return cas;
            }
        } else {
            if (x + 1 >= 0 && x + 1 <= c.getPartie().getMap().getHauteur() - 1 && y + 1 >= 0 && y + 1 <= c.getPartie().getMap().getLargeur() - 1) {
                cas = c.getPartie().getMap().getCase((x + 1), (y + 1));
                return cas;
            }
        }
        return null;
    }

    private Case getCaseSudEst(Case cc) {
        if (cc == null) {
            return null;
        }
        int x;
        int y;
        Case cas;
        x = cc.getX();
        y = cc.getY();
        if (y % 2 == 0) {
            if (x - 1 >= 0 && x - 1 <= c.getPartie().getMap().getHauteur() - 1 && y + 1 >= 0 && y + 1 <= c.getPartie().getMap().getLargeur() - 1) {
                cas = c.getPartie().getMap().getCase((x - 1), (y + 1));
                return cas;
            }
        } else {
            if (y - 1 >= 0 && y + 1 <= c.getPartie().getMap().getLargeur() - 1) {
                cas = c.getPartie().getMap().getCase(x, (y + 1));
                return cas;
            }
        }
        return null;
    }

    private Case getCaseNordOuest(Case cc) {
        if (cc == null) {
            return null;
        }
        int x;
        int y;
        Case cas;
        x = cc.getX();
        y = cc.getY();
        if (y % 2 == 0) {
            if (y - 1 >= 0 && y - 1 <= c.getPartie().getMap().getLargeur() - 1) {
                cas = c.getPartie().getMap().getCase(x, (y - 1));
                return cas;
            }
        } else {
            if (x + 1 >= 0 && x + 1 <= c.getPartie().getMap().getHauteur() - 1 && y - 1 >= 0 && y - 1 <= c.getPartie().getMap().getHauteur() - 1) {
                cas = c.getPartie().getMap().getCase((x + 1), (y - 1));
                return cas;
            }
        }
        return null;
    }

    private Case getCaseSudOuest(Case cc) {
        if (cc == null) {
            return null;
        }
        int x;
        int y;
        Case cas;
        x = cc.getX();
        y = cc.getY();
        if (y % 2 == 0) {
            if (x - 1 >= 0 && x - 1 <= c.getPartie().getMap().getHauteur() - 1 && y - 1 >= 0 && y - 1 <= c.getPartie().getMap().getLargeur() - 1) {
                cas = c.getPartie().getMap().getCase((x - 1), (y - 1));
                return cas;
            }
        } else {
            if (y - 1 >= 0 && y - 1 <= c.getPartie().getMap().getHauteur() - 1) {
                cas = c.getPartie().getMap().getCase(x, (y - 1));
                return cas;
            }
        }
        return null;
    }

    private void getCasesDisponible(Case cas, int range, ArrayList<Case> tab) {
        if (cas != null) {
            if (!(tab.contains(cas)) && !(cas instanceof CaseTerre)) {
                tab.add(cas);
            }

            if (range != 0 && !(cas instanceof CaseTerre)) {
                if (cas.getX() > 0) {
                    getCasesDisponible(getCaseSud(cas), (range - 1), tab);
                }
                if (cas.getY() < c.getPartie().getMap().getLargeur() - 1) {
                    if ((cas.getY() % 2 == 0 && cas.getX() > 0) || cas.getY() % 2 == 1) {
                        getCasesDisponible(getCaseSudEst(cas), (range - 1), tab);
                    }
                }
                if (cas.getY() < c.getPartie().getMap().getLargeur() - 1) {
                    if ((cas.getY() % 2 == 1 && cas.getX() < c.getPartie().getMap().getHauteur() - 1) || cas.getY() % 2 == 0) {
                        getCasesDisponible(getCaseNordEst(cas), (range - 1), tab);
                    }
                }
                if (cas.getY() > 0) {
                    if ((cas.getY() % 2 == 0 && cas.getX() > 0) || cas.getY() % 2 == 1) {
                        getCasesDisponible(getCaseSudOuest(cas), (range - 1), tab);
                    }
                }
                if (cas.getY() > 0) {
                    if ((cas.getY() % 2 == 1 && cas.getX() < c.getPartie().getMap().getHauteur() - 1) || cas.getY() % 2 == 0) {
                        getCasesDisponible(getCaseNordOuest(cas), (range - 1), tab);
                    }
                }
                if (cas.getX() < c.getPartie().getMap().getHauteur() - 1) {
                    getCasesDisponible(getCaseNord(cas), (range - 1), tab);
                }
            }
        }
    }

    public abstract int alphabeta(int alpha, int beta);

    private int distanceCases(Case c1, Case c2) {
        int d = 0;

        int x1 = c1.getX(), x2 = c2.getX(), y1 = c1.getY(), y2 = c2.getY();
        while (x1 != x2 || y1 != y2) {
            if (x1 == x2) {
                if (y1 < y2) {
                    y1++;
                } else {
                    y1--;
                }
            } else if (y1 == y2) {
                if (x1 < x2) {
                    x1++;
                } else {
                    x1--;
                }
            } else if (x1 < x2) {
                if (y1 < y2) {
                    if (y1 % 2 == 0) {
                        y1++;
                    } else {
                        y1++;
                        x1++;
                    }
                } else {
                    if (y1 % 2 == 0) {
                        y1--;
                    } else {
                        x1++;
                        y1--;
                    }
                }
            } else {
                if (y1 < y2) {
                    if (y1 % 2 == 0) {
                        x1--;
                        y1++;
                    } else {
                        y1++;
                    }
                } else {
                    if (y1 % 2 == 0) {
                        y1--;
                        x1--;
                    } else {
                        y1--;
                    }
                }
            }
            d++;
        }
        return d;
    }

    public void updateHeuristic2() {
        heuristic = 10000;
        Case position = c.getPartie().getBateauSelectionne().getPosition();
        Player player = c.getPartie().getBateauSelectionne().getJoueur();

        for (Case light : getPharesAvaillable()) {

            if (light == position) {
                heuristic = 0;
            } else if (light.getPossedePhare() == null
                    || light.getPossedePhare().getNumber() != player.getNumber()) {
                int distance = astar(position, light).size();
                if (distance < heuristic) {
                    heuristic = distance;
                }
            }
        }

        if (myAction instanceof PassTurn && heuristic != 0) {
            heuristic += 1000;
        }

        if (heuristic > 0) {
            heuristic += 20 * (2 - player.getPharesPossedes());
        }
    }

    public void updateHeuristic() {
        heuristic = 10000;
        Case position = c.getPartie().getBateauSelectionne().getPosition();
        Player player = c.getPartie().getBateauSelectionne().getJoueur();

        for (Case light : getPharesAvaillable()) {

            if (light == position) {
                heuristic = 0;
            } else if (light.getPossedePhare() == null
                    || light.getPossedePhare().getNumber() != player.getNumber()) {
                int distance = distanceCases(position, light);
                if (distance < heuristic) {
                    heuristic = distance;
                }
            }
        }

        if (myAction instanceof PassTurn && fils.size() > 0) {
            heuristic += 1000;
        }

        if (myAction instanceof SecondaryAttackAndMove) {
            heuristic /= 2;
        } else if (myAction instanceof AttackAndMove) {
            heuristic /= 3;
        }


        if (heuristic > 0) {
            heuristic += 20 * (2 - player.getPharesPossedes());
        }
    }

    protected ArrayList<Case> getPharesAvaillable() {
        ArrayList<Case> myLights = new ArrayList<>();

        for (int i = 0; i < c.getPartie().getMap().getHauteur(); i++) {
            for (int j = 0; j < c.getPartie().getMap().getLargeur(); j++) {
                Case myC = c.getPartie().getMap().getCase(i, j);
                if (myC.isPhare()) {
                    myLights.add(myC);
                }
            }
        }

        return myLights;
    }

    protected ArrayList<Case> getBoatInRange(Boat bateauSelectionne, Player p) {
        ArrayList<Case> myCases = new ArrayList<>();

        for (Case c : c.getPartie().getMap().getCasesPortees(bateauSelectionne)) {
            if (casePossedeBateau(c, p)) {
                myCases.add(c);
            }
        }
        return myCases;
    }

    protected boolean casePossedeBateau(Case aCase, Player joueur) {
        if (joueur.getNumber() == 1) {
            for (Boat aBateaux1 : c.getPartie().getJoueur1().getBoats()) {
                if (aBateaux1.getPosition().getX() == aCase.getX()
                        && aBateaux1.getPosition().getY() == aCase.getY()) {
                    return true;
                }
            }
        } else {
            for (Boat aBateaux2 : c.getPartie().getJoueur2().getBoats()) {
                if (aBateaux2.getPosition().getX() == aCase.getX()
                        && aBateaux2.getPosition().getY() == aCase.getY()) {
                    return true;
                }
            }
        }

        return false;
    }
}
