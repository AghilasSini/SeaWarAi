package fr.lesprogbretons.seawar.ia.ia_seawar_e;

import java.awt.*;

public class HeuristiqueGitGud {

    public static double heuristiquePhares(SimPartie p, int moi){
        int other = 2;
        if (moi == 1){
            other = 1;
        }

        // Un seul bateau pour l'instant
        SimBoat nav = p.bateauxDuJoueur(moi).get(0);
        double posX = nav.pos.x;
        double posY = nav.pos.y;

        if (posY % 2 == 1){
            posY += .5;
        }
        double heuristique = 0;

        Point[] phares = p.map.posPhares;

        double distanceMin = Double.MAX_VALUE;
        for (Point phare : phares){
            if (p.map.tab[phare.x][phare.y].ownerPhare != moi){

                // Distance de manhatan
                //double dist = Math.abs(posX - phare.x) + Math.abs(posY - phare.y);
                double phareY = phare.y;
                if (phareY % 2 == 1){
                    phareY += .5;
                }
                double dist = Math.sqrt(Math.abs(posX - phare.x)*Math.abs(posX - phare.x) + Math.abs(posY - phareY)*Math.abs(posY - phareY));

                if (dist < distanceMin){
                    distanceMin = dist;
                }
            }
            else{
                heuristique+= 50;
            }
        }
        //RAJOUT
        if (p.map.posPhares.length * 50 == heuristique){
            return 1000;
        }

        //return - posX; //pour se faire bloquer l'ia
        return -distanceMin + heuristique;
    }

    public static double heuristiqueSang(SimPartie p, int moi){
        int other = 2;
        if (moi == 2){
            other = 1;
        }

        // Un seul bateau pour l'instant
        SimBoat nav = p.bateauxDuJoueur(moi).get(0);
        double posX = nav.pos.x;
        double posY = nav.pos.y;
        if (posY % 2 == 1){
            posY += .5;
        }

        if (p.bateauxDuJoueur(other).size() != 0) {
            SimBoat navOther = p.bateauxDuJoueur(other).get(0);
            double posX2 = navOther.pos.x;
            double posY2 = navOther.pos.y;
            if (posY2 % 2 == 1) {
                posY2 += .5;
            }

            double dist = Math.sqrt((posX - posX2) * (posX - posX2) + (posY - posY2) * (posY - posY2));

            return -dist - navOther.hp * 50;
        } else {
            return 1000;
        }
    }

    public static double heuristique(SimPartie p, int moi){
        int other = 2;
        if (moi == 2){
            other = 1;
        }

        // Un seul bateau pour l'instant
        if (p.bateauxDuJoueur(moi).size() == 0){
            return -100000;
        }
        SimBoat nav = p.bateauxDuJoueur(moi).get(0);
        double posX = nav.pos.x;
        double posY = nav.pos.y;
        if (posY % 2 == 1){
            posY += .5;
        }

        double pointPhares = 0;

        Point[] phares = p.map.posPhares;

        for (Point phare : phares){
            if (p.map.tab[phare.x][phare.y].ownerPhare == moi) {
                pointPhares += 600;
            } else if (p.map.tab[phare.x][phare.y].ownerPhare == other) {
                pointPhares -= 600;
            }
        }
        //RAJOUT
        if (p.map.posPhares.length * 600 == pointPhares){
            return 100000;
        } else if (p.map.posPhares.length * -600 == pointPhares){
            return -100000;
        }

        if (p.bateauxDuJoueur(other).size() != 0) {
            SimBoat navOther = p.bateauxDuJoueur(other).get(0);
            double posX2 = navOther.pos.x;
            double posY2 = navOther.pos.y;
            if (posY2 % 2 == 1) {
                posY2 += .5;
            }

            double dist = Math.sqrt((posX - posX2) * (posX - posX2) + (posY - posY2) * (posY - posY2));

            return -dist - navOther.hp * 10 + pointPhares;
        } else {
            return 100000;
        }
    }

}
