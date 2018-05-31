package fr.lesprogbretons.seawar.ia.ia_seawar_e;

import fr.lesprogbretons.seawar.model.Orientation;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

import static fr.lesprogbretons.seawar.model.Orientation.*;

public class SimPartie implements Cloneable{
    public SimMap map;
    public int currentPlayer;
    public int winner;
    public ArrayList<SimBoat> tabBoat;

    public SimPartie(SimMap map, int currentPlayer, int winner, ArrayList<SimBoat> tabBoat){
        this.map=map;
        this.currentPlayer=currentPlayer;
        this.winner=winner;
        this.tabBoat = tabBoat;
    }

    public Object clone(){
        ArrayList<SimBoat> tabBclone = new ArrayList<>();
        for(SimBoat b : this.tabBoat){
            tabBclone.add((SimBoat)b.clone());
        }
        SimPartie clone = new SimPartie((SimMap)this.map.clone(),this.currentPlayer,this.winner,tabBclone);
        return clone;
    }

    public boolean endTurn(){
        winnerUpdate();
        if(canEndTurn()){

            for(SimBoat b : bateauxDuJoueur(currentPlayer)){
                if(b.estBloque){
                    b.or = allerADroite(allerADroite(allerADroite(b.or)));
                }
                b.plusLeDroitDeplacer=false;
                b.moveDispo=b.move;
                b.estBloque=false;
                b.cdMain--;
                b.cdSec--;
            }
            int otherplayer;
            if(currentPlayer==1){
                 otherplayer=2;
            } else {
                 otherplayer=1;
            }
            for(SimBoat b : bateauxDuJoueur(otherplayer)){
                if(!canMoveDevant(b) && !canMoveDroite(b) && !canMoveGauche(b)){
                    b.estBloque = true;
                }
                for(Point p : map.posPhares){
                    if(b.pos.equals(p)){
                        map.tab[p.x][p.y].ownerPhare=b.owner;
                    }
                }
            }

            winnerUpdate();
            currentPlayer=otherplayer;
            return true;
        }
        return false;
    }
    public boolean moveBoat(SimBoat b, SimCase c){
        //DEBUG BLOCAGE
        //if(!b.estBloque && b.moveDispo>0 && !b.plusLeDroitDeplacer){
        if(b.moveDispo>0 && !b.plusLeDroitDeplacer){
            b.pos = new Point(c.x,c.y);
            //RAJOUT
            for (Point phare : map.posPhares){
                if (phare.x == c.x && phare.y == c.y){
                    map.tab[phare.x][phare.y].ownerPhare = b.owner;
                }
            }
            //
            b.moveDispo--;
            for(SimBoat bo : bateauxDuJoueur(b.owner)){
                if(b!=bo && bo.move>bo.moveDispo){
                    bo.plusLeDroitDeplacer = true;
                }
            }
            return true;
        }
        return false;
    }
    public void winnerUpdate(){
        updateSBoats();
        if(bateauxDuJoueur(1).isEmpty()){
            winner = 2;
        }
        if(bateauxDuJoueur(2).isEmpty()){
            winner = 1;
        }
        boolean all=true;
        for(Point p : map.posPhares){
            if(map.tab[p.x][p.y].ownerPhare!=1){
                all=false;
            }
        }
        if(all){
            winner = 1;
        }
        all=true;
        for(Point p : map.posPhares){
            if(map.tab[p.x][p.y].ownerPhare!=2){
                all=false;
            }
        }
        if(all){
            winner = 2;
        }
    }
    public boolean shootSec(SimBoat origin,SimBoat dest){
        if(origin.cdSec<=0){
            origin.cdSec = origin.reloadSec;
                dest.hp-=origin.dmgSecC;
            if(dest.hp<=0){
                updateSBoats();
            }
            return true;
        }
        return false;
    }
    public boolean shootMain(SimBoat origin,SimBoat dest){
        if(origin.cdMain<=0){
            origin.cdMain = origin.reloadMain;
                dest.hp-=origin.dmgMainC;
            if(dest.hp<=0){
                updateSBoats();
            }
            return true;
        }
        return false;
    }
    public void updateSBoats(){
        Iterator<SimBoat> iter = tabBoat.iterator();

        while (iter.hasNext()) {
            SimBoat sb = iter.next();

            if (sb.hp<=0) {
                iter.remove();
            }
        }
    }
    public boolean fregateCanShootWithSec(SimBoat fregate,SimBoat b){
        if(fregate==null ||fregate.cdSec>0 ||fregate.hp<=0 ){
            return false;
        }
        SimCase posFregate = map.tab[fregate.pos.x][fregate.pos.y];
        SimCase devant = map.getCaseNord(posFregate);
        if(devant!=null && b.pos.x==devant.x && b.pos.y==devant.y){
            return true;
        }
        devant = map.getCaseNordEst(posFregate);
        if(devant!=null && b.pos.x==devant.x && b.pos.y==devant.y){
            return true;
        }
        devant = map.getCaseNordOuest(posFregate);
        if(devant!=null && b.pos.x==devant.x && b.pos.y==devant.y){
            return true;
        }
        devant = map.getCaseSud(posFregate);
        if(devant!=null && b.pos.x==devant.x && b.pos.y==devant.y){
            return true;
        }
        devant = map.getCaseSudEst(posFregate);
        if(devant!=null && b.pos.x==devant.x && b.pos.y==devant.y){
            return true;
        }
        devant = map.getCaseSudOuest(posFregate);
        if(devant!=null && b.pos.x==devant.x && b.pos.y==devant.y){
            return true;
        }
        return false;
    }
    public boolean fregateCanShootOnWithMain(SimBoat fregate,SimBoat b){
        if(fregate==null ||fregate.cdMain>0 ||fregate.hp<=0 ){
            return false;
        }
        SimCase posFregate = map.tab[fregate.pos.x][fregate.pos.y];
        SimCase devant = allerVers(posFregate,fregate.or);
        if(devant!=null && b.pos.x==devant.x && b.pos.y==devant.y){
            return true;
        }
        devant = allerVers(devant,fregate.or);
        if(devant!=null && b.pos.x==devant.x && b.pos.y==devant.y){
            return true;
        }
        devant = allerVers(posFregate,allerADroite(fregate.or));
        if(devant!=null && b.pos.x==devant.x && b.pos.y==devant.y){
            return true;
        }
        devant = allerVers(posFregate,allerADroite(allerADroite(fregate.or)));
        if(devant!=null && b.pos.x==devant.x && b.pos.y==devant.y){
            return true;
        }devant = allerVers(posFregate,allerAGauche(fregate.or));
        if(devant!=null && b.pos.x==devant.x && b.pos.y==devant.y){
            return true;
        }devant = allerVers(posFregate,allerAGauche(allerAGauche(fregate.or)));
        if(devant!=null && b.pos.x==devant.x && b.pos.y==devant.y){
            return true;
        }
        return  false;
    }
    public boolean amiralCanShootOnWithSec(SimBoat amiral,SimBoat b){
        if(amiral==null ||amiral.cdSec>0 ||amiral.hp<=0 ){
            return false;
        }
        SimCase posAmiral = map.tab[amiral.pos.x][amiral.pos.y];
        SimCase devant = allerVers(posAmiral,amiral.or);
        if(devant!=null && b.pos.x==devant.x && b.pos.y==devant.y){
            return true;
        }
        devant = allerVers(devant,amiral.or);
        if(devant!=null && b.pos.x==devant.x && b.pos.y==devant.y){
            return true;
        }
        SimCase droite = allerVers(posAmiral,allerADroite(amiral.or));
        if(droite!=null && b.pos.x==droite.x && b.pos.y==droite.y){
            return true;
        }
        devant = allerVers(droite,amiral.or);
        if(devant!=null && b.pos.x==devant.x && b.pos.y==devant.y){
            return true;
        }
        SimCase gauche = allerVers(posAmiral,allerAGauche(amiral.or));
        if(gauche!=null && b.pos.x==gauche.x && b.pos.y==gauche.y){
            return true;
        }
        devant = allerVers(gauche,amiral.or);
        if(devant!=null && b.pos.x==devant.x && b.pos.y==devant.y){
            return true;
        }
        return false;
    }
    public boolean amiralCanShootOnWithMain(SimBoat amiral,SimBoat b){
        if(amiral==null||amiral.cdMain>0 ||amiral.hp<=0 ){
            return false;
        }
        SimCase posAmiral = map.tab[amiral.pos.x][amiral.pos.y];
        SimCase devant = allerVers(posAmiral,amiral.or);
        if(devant!=null && b.pos.x==devant.x && b.pos.y==devant.y){
            return true;
        }
        devant = allerVers(devant,amiral.or);
        if(devant!=null && b.pos.x==devant.x && b.pos.y==devant.y){
            return true;
        }
        devant = allerVers(devant,amiral.or);
        if(devant!=null && b.pos.x==devant.x && b.pos.y==devant.y){
            return true;
        }
        devant = allerVers(devant,amiral.or);
        if(devant!=null && b.pos.x==devant.x && b.pos.y==devant.y){
            return true;
        }
        return false;
    }
    public boolean canEndTurn(){
        boolean yesHeCan = true;
        for(SimBoat sb : bateauxDuJoueur(currentPlayer)){
            if(sb.move==sb.moveDispo && !sb.estBloque){
                yesHeCan = false;
            }
        }
        return yesHeCan;
    }
    public boolean canMoveDroite(SimBoat b){   //b peut il bouger à droite?
        boolean ilPeut = true;

        if(b.moveDispo==0 || b.plusLeDroitDeplacer){
            ilPeut=false;
        } else {
            SimCase caseADroite = allerVers(map.tab[b.pos.x][b.pos.y],allerADroite(b.or));
            if(caseADroite == null){
                ilPeut=false;
            } else if(caseADroite.typeCase==0){
                ilPeut=false;
            } else if(bateauSurCase(caseADroite)!=null){
                ilPeut=false;
            }
        }
        return ilPeut;
    }
    public boolean canMoveDevant(SimBoat b){   //b peut il bouger à droite?
        boolean ilPeut = true;

        if(b.moveDispo==0 || b.plusLeDroitDeplacer){
            ilPeut=false;
        } else {
            SimCase caseDevant = allerVers(map.tab[b.pos.x][b.pos.y],b.or);
            if(caseDevant == null){
                ilPeut=false;
            } else if(caseDevant.typeCase==0){
                ilPeut=false;
            } else if(bateauSurCase(caseDevant)!=null){
                ilPeut=false;
            }
        }
        return ilPeut;
    }
    public boolean canMoveGauche(SimBoat b){
        boolean ilPeut = true;

        if(b.moveDispo==0 || b.plusLeDroitDeplacer){
            ilPeut=false;
        } else {
            SimCase caseAGauche = allerVers(map.tab[b.pos.x][b.pos.y],allerAGauche(b.or));
            if(caseAGauche == null){
                ilPeut=false;
            } else if(caseAGauche.typeCase==0){
                ilPeut=false;
            } else if(bateauSurCase(caseAGauche)!=null){
                ilPeut=false;
            }
        }
        return ilPeut;
    }
    public ArrayList<SimBoat> bateauxDuJoueur(int number){
        ArrayList<SimBoat> lesBat = new ArrayList<>();
        for(SimBoat sb : this.tabBoat){
            if(sb.owner==number){
                lesBat.add(sb);
            }
        }
        return lesBat;
    }

    public SimBoat bateauSurCase(SimCase sc){
        for(SimBoat sb: this.tabBoat){
            if(sb.pos.x==sc.x && sb.pos.y==sc.y){
                return sb;
            }
        }
        return null;
    }
    public Orientation allerADroite(Orientation o){ //retourne l'orientation si on tourne à droite
        switch(o){
            case SUDEST:
                return SUD;
            case SUD:
                return SUDOUEST;
            case SUDOUEST:
                return NORDOUEST;
            case NORDOUEST:
                return NORD;
            case NORD:
                return NORDEST;
            case NORDEST:
                return SUDEST;

                default: return null;
        }
    }
    public Orientation allerAGauche(Orientation o){ //retourne l'orientation si on tourne à gauche
        switch(o){
            case SUDEST:
                return NORDEST;
            case SUD:
                return SUDEST;
            case SUDOUEST:
                return SUD;
            case NORDOUEST:
                return SUDOUEST;
            case NORD:
                return NORDOUEST;
            case NORDEST:
                return NORD;

            default: return null;
        }
    }
    public SimCase allerVers(SimCase sc, Orientation o){
        switch (o){
            case SUDEST:
                return map.getCaseSudEst(sc);
            case SUD:
                return map.getCaseSud(sc);
            case SUDOUEST:
                return map.getCaseSudOuest(sc);
            case NORDOUEST:
                return  map.getCaseNordOuest(sc);
            case NORD:
                return map.getCaseNord(sc);
            case NORDEST:
                return map.getCaseNordEst(sc);
                default: return null;
        }
    }
    public SimBoat getAmiral(int player){
        for(SimBoat b : tabBoat){
            if(b.owner==player && b.type==0){
                return b;
            }
        }
        return null;
    }
    public SimBoat getFregate(int player){
        for(SimBoat b : tabBoat){
            if(b.owner==player && b.type==1){
                return b;
            }
        }
        return null;
    }
}
