package fr.lesprogbretons.seawar.ia.ia_seawar_e;

import fr.lesprogbretons.seawar.controller.Controller;
import fr.lesprogbretons.seawar.ia.AbstractIA;
import fr.lesprogbretons.seawar.model.Partie;
import fr.lesprogbretons.seawar.model.actions.*;
import fr.lesprogbretons.seawar.model.boat.Amiral;
import fr.lesprogbretons.seawar.model.boat.Boat;
import fr.lesprogbretons.seawar.model.boat.Fregate;
import fr.lesprogbretons.seawar.model.cases.Case;
import fr.lesprogbretons.seawar.model.cases.CaseTerre;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GitGud extends AbstractIA {
    public GitGud(int number) { super(number); }

    public GitGud(int number, String name) {
        super(number,name);
    }

    public GitGud(int number, String name, List<Boat> boats) {
        super(number, name, boats);
    }

    @Override
    public Action chooseAction(Controller controller) {
        Partie partie = controller.getPartie();

        List<Action> actionsPossibles = new ArrayList<>();
        List<SimPartie> partiesFilles = new ArrayList<>();
        int typeCanon[] = new int[12]; //1:main, 2:sec
        List<Boat> mesBateaux;
        List<Boat> sesBateaux;
        if(this.getNumber()==1){
            mesBateaux=partie.getJoueur1().getBoats();
            sesBateaux=partie.getJoueur2().getBoats();
        } else {
            mesBateaux=partie.getJoueur2().getBoats();
            sesBateaux=partie.getJoueur1().getBoats();
        }
        Boat myB = mesBateaux.get(0);

        SimPartie spf = importPartie(partie);
        SimBoat boatSelect = spf.bateauSurCase(spf.map.tab[myB.getPosition().getX()][myB.getPosition().getY()]);
        Action actionattaque;
        if (sesBateaux.size() != 0) {
            Boat b = sesBateaux.get(0);
            if (myB instanceof Amiral) {
                if (spf.amiralCanShootOnWithMain(boatSelect, spf.bateauSurCase(spf.map.tab[b.getPosition().getX()][b.getPosition().getY()]))) {
                    if (myB.getCanonSelectionne() != 1){
                       return new ChangeCannon(myB);
                    }

                    SimPartie partieTir = (SimPartie) spf.clone();
                    partieTir.shootMain(partieTir.bateauSurCase(partieTir.map.tab[boatSelect.pos.x][boatSelect.pos.y]), partieTir.bateauSurCase(partieTir.map.tab[b.getPosition().getX()][b.getPosition().getY()]));
                    partiesFilles.add(partieTir);
                    actionattaque = new Attack(myB, b.getPosition());
                    actionsPossibles.add(actionattaque);
                    typeCanon[actionsPossibles.indexOf(actionattaque)] = 1;

                } else {
                    if (spf.amiralCanShootOnWithSec(boatSelect, spf.bateauSurCase(spf.map.tab[b.getPosition().getX()][b.getPosition().getY()]))){
                        if (myB.getCanonSelectionne() != 2){
                            return new ChangeCannon(myB);
                        }

                        SimPartie partieTir = (SimPartie) spf.clone();
                        partieTir.shootSec(partieTir.bateauSurCase(partieTir.map.tab[boatSelect.pos.x][boatSelect.pos.y]),partieTir.bateauSurCase(partieTir.map.tab[b.getPosition().getX()][b.getPosition().getY()]));
                        partiesFilles.add(partieTir);
                        actionattaque=new Attack(myB,b.getPosition());
                        actionsPossibles.add(actionattaque);
                        typeCanon[actionsPossibles.indexOf(actionattaque)]=2;

                    }
                }

            }


            /*if (myB instanceof Fregate) {
                if (spf.fregateCanShootOnWithMain(boatSelect, spf.bateauSurCase(spf.map.tab[b.getPosition().getX()][b.getPosition().getY()]))) {
                    if (myB.getCanonSelectionne() != 1){
                        return new ChangeCannon(myB);
                    }
                    SimPartie partieTir = (SimPartie) spf.clone();
                    partieTir.shootMain(partieTir.bateauSurCase(partieTir.map.tab[boatSelect.pos.x][boatSelect.pos.y]), partieTir.bateauSurCase(partieTir.map.tab[b.getPosition().getX()][b.getPosition().getY()]));
                    partiesFilles.add(partieTir);
                    actionattaque = new Attack(myB, b.getPosition());
                    actionsPossibles.add(actionattaque);
                } else {
                    if (spf.fregateCanShootWithSec(boatSelect, spf.bateauSurCase(spf.map.tab[b.getPosition().getX()][b.getPosition().getY()]))) {
                        if (myB.getCanonSelectionne() != 2) {
                            return new ChangeCannon(myB);
                        }
                        SimPartie partieTir = (SimPartie) spf.clone();
                        partieTir.shootSec(partieTir.bateauSurCase(partieTir.map.tab[boatSelect.pos.x][boatSelect.pos.y]), partieTir.bateauSurCase(partieTir.map.tab[b.getPosition().getX()][b.getPosition().getY()]));
                        partiesFilles.add(partieTir);
                        actionattaque = new Attack(myB, b.getPosition());
                        actionsPossibles.add(actionattaque);
                    }
                }
            }*/
        }

        if(myB.getMoveAvailable()>0){
            spf = importPartie(partie);
            boatSelect = spf.bateauSurCase(spf.map.tab[myB.getPosition().getX()][myB.getPosition().getY()]);
            if(spf.canMoveDevant(boatSelect)){
                SimPartie spDevant = (SimPartie) spf.clone();
                SimBoat sbDevant = spDevant.bateauSurCase(spDevant.map.tab[myB.getPosition().getX()][myB.getPosition().getY()]);
                spDevant.moveBoat(sbDevant,spDevant.allerVers(spDevant.map.tab[sbDevant.pos.x][sbDevant.pos.y],sbDevant.or));
                partiesFilles.add(spDevant);
                actionsPossibles.add(new MoveBoat(myB,partie.getMap().getCase(sbDevant.pos.x,sbDevant.pos.y)));
            }
            if(spf.canMoveDroite(boatSelect)){
                SimPartie spDevant = (SimPartie) spf.clone();
                SimBoat sbDevant = spDevant.bateauSurCase(spDevant.map.tab[myB.getPosition().getX()][myB.getPosition().getY()]);
                spDevant.moveBoat(sbDevant,spDevant.allerVers(spDevant.map.tab[sbDevant.pos.x][sbDevant.pos.y],spDevant.allerADroite(sbDevant.or)));
                partiesFilles.add(spDevant);
                actionsPossibles.add(new MoveBoat(myB,partie.getMap().getCase(sbDevant.pos.x,sbDevant.pos.y)));
            }
            if(spf.canMoveGauche(boatSelect)){
                SimPartie spDevant = (SimPartie) spf.clone();
                SimBoat sbDevant = spDevant.bateauSurCase(spDevant.map.tab[myB.getPosition().getX()][myB.getPosition().getY()]);
                spDevant.moveBoat(sbDevant,spDevant.allerVers(spDevant.map.tab[sbDevant.pos.x][sbDevant.pos.y],spDevant.allerAGauche(sbDevant.or)));
                partiesFilles.add(spDevant);
                actionsPossibles.add(new MoveBoat(myB,partie.getMap().getCase(sbDevant.pos.x,sbDevant.pos.y)));
            }
        }

        /*if(spf.canEndTurn()){
            spf.endTurn();
            partiesFilles.add((SimPartie)spf.clone());
            actionsPossibles.add(new PassTurn(myB));
        }*/
        //on a les actions et les parties filles

        for(int n=0;n<600;n++){
            double alphabetamax = Double.NEGATIVE_INFINITY;
            int indexActionChoix = 0;
            for(SimPartie partieF : partiesFilles){
                double newAB = alphabeta(partieF,Double.NEGATIVE_INFINITY,Double.POSITIVE_INFINITY,n);
                if(newAB>alphabetamax){
                    alphabetamax=newAB;
                    indexActionChoix = partiesFilles.indexOf(partieF);
                }
            }
            if(!actionsPossibles.isEmpty()) {
                this.memoriseAction(actionsPossibles.get(indexActionChoix));
            }
        }

        return this.getMemorizedAction();
    }

    public double alphabeta(SimPartie p, double alpha, double beta, int n){
        if(n==0){   //FEUIILE
            return HeuristiqueGitGud.heuristique(p, this.getNumber());
        } else if(p.currentPlayer == this.getNumber()){ //NOEUD MAX
            ArrayList<SimPartie> partiesF = partiesFilles(p);
            int i = 0;
            while(alpha<beta && i<partiesF.size()){
                double a = alphabeta(partiesF.get(i),alpha,beta,n-1);
                if(a>alpha){
                    alpha = a;
                }
                i++;
            }
            return alpha;
        } else { //NOEUD MIN
            ArrayList<SimPartie> partiesF = partiesFilles(p);
            int i = 0;
            while(alpha<beta && i<partiesF.size()){
                double b = alphabeta(partiesF.get(i),alpha,beta,n-1);
                if(b<beta){
                    beta = b;
                }
                i++;
            }
            return beta;
        }
    }

    public ArrayList<SimPartie> partiesFilles(SimPartie p){
        ArrayList<SimPartie> result = new ArrayList<>();
        int other = 1;
        if(p.currentPlayer == 1){
            other = 2;
        }
        for(SimBoat b:p.bateauxDuJoueur(p.currentPlayer)){
            if(b.type==0){  //Amiral
                for(SimBoat sbo:p.bateauxDuJoueur(other)){
                    if(p.amiralCanShootOnWithMain(b,sbo)){
                        SimPartie spf = (SimPartie) p.clone();
                        spf.shootMain(spf.bateauSurCase(spf.map.tab[b.pos.x][b.pos.y]),spf.bateauSurCase(spf.map.tab[sbo.pos.x][sbo.pos.y]));
                        result.add(spf);
                    }
                    if(p.amiralCanShootOnWithSec(b,sbo)){
                        SimPartie spf = (SimPartie) p.clone();
                        spf.shootSec(spf.bateauSurCase(spf.map.tab[b.pos.x][b.pos.y]),spf.bateauSurCase(spf.map.tab[sbo.pos.x][sbo.pos.y]));
                        result.add(spf);
                    }
                }
            } /*else if(b.type==1){ //fregate
                for(SimBoat sbo:p.bateauxDuJoueur(other)){
                    if(p.fregateCanShootOnWithMain(b,sbo)){
                        SimPartie spf = (SimPartie) p.clone();
                        spf.shootMain(spf.bateauSurCase(spf.map.tab[b.pos.x][b.pos.y]),spf.bateauSurCase(spf.map.tab[sbo.pos.x][sbo.pos.y]));
                        result.add(spf);
                    }
                    if(p.fregateCanShootOnWithMain(b,sbo)){
                        SimPartie spf = (SimPartie) p.clone();
                        spf.shootSec(spf.bateauSurCase(spf.map.tab[b.pos.x][b.pos.y]),spf.bateauSurCase(spf.map.tab[sbo.pos.x][sbo.pos.y]));
                        result.add(spf);
                    }
                }
            }*/
            if(p.canMoveDevant(b)){
                SimPartie pf = (SimPartie) p.clone();
                pf.moveBoat(pf.bateauSurCase(pf.map.tab[b.pos.x][b.pos.y]),pf.allerVers(pf.map.tab[b.pos.x][b.pos.y],b.or));
                result.add(pf);
            }
            if(p.canMoveGauche(b)){
                SimPartie pf = (SimPartie) p.clone();
                pf.moveBoat(pf.bateauSurCase(pf.map.tab[b.pos.x][b.pos.y]),pf.allerVers(pf.map.tab[b.pos.x][b.pos.y],pf.allerAGauche(b.or)));
                result.add(pf);
            }
            if(p.canMoveDroite(b)){
                SimPartie pf = (SimPartie) p.clone();
                pf.moveBoat(pf.bateauSurCase(pf.map.tab[b.pos.x][b.pos.y]),pf.allerVers(pf.map.tab[b.pos.x][b.pos.y],pf.allerADroite(b.or)));
                result.add(pf);
            }
        }
        if(p.canEndTurn()){
            result.add((SimPartie) p.clone());
        }
        return result;
    }

    public SimPartie importPartie(Partie p){
        ArrayList<Point> posP = new ArrayList<>();
        SimCase simCases[][] = new SimCase[p.getMap().getHauteur()][p.getMap().getLargeur()];
        for(int i=0;i<p.getMap().getHauteur();i++){
            for(int j=0;j<p.getMap().getLargeur();j++){
                int typeC;
                int owner;
                Case caseSelect = p.getMap().getCase(i,j);
                if(caseSelect instanceof CaseTerre){
                    typeC = 0;
                } else {
                    if(caseSelect.isPhare()){
                        typeC = 2;
                        posP.add(new Point(i,j));
                    } else {
                        typeC = 1;
                    }
                }
                if(caseSelect.getPossedePhare()==null){
                    owner=0;
                } else {
                    owner=caseSelect.getPossedePhare().getNumber();
                }
                simCases[i][j] = new SimCase(typeC,owner,i,j);
            }
        }
        Point pts[] = new Point[posP.size()];
        int i = 0;
        Object[] obpts = posP.toArray();
        for(Object ob:obpts){
            pts[i] = (Point)obpts[i];
            i++;
        }
        SimMap simM = new SimMap(simCases, pts);

        int currentPlayer;
        if(p.getCurrentPlayer()==null){
            currentPlayer=0;
        } else {
            currentPlayer = p.getCurrentPlayer().getNumber();
        }

        int winner;
        if(p.getWinner()==null){
            winner=0;
        } else {
            winner=p.getWinner().getNumber();
        }

        ArrayList<SimBoat> tabBoat = new ArrayList<>();
        int typeBat;
        for(Boat bat1 : p.getMap().getBateaux1()){
            if(bat1 instanceof Amiral){
                typeBat = 0;
            } else if(bat1 instanceof Fregate){
                typeBat = 1;
            } else {
                typeBat = 2;
            }
            boolean peutSeDep = false;
            if(p.getBateauxDejaDeplaces().size() == 0){
                peutSeDep = true;
            } else if(bat1.getMoveAvailable() > 0 && (p.getBateauxDejaDeplaces().get(p.getBateauxDejaDeplaces().size() - 1).equals(bat1))){
                peutSeDep = true;
            } else if(bat1.getMoveAvailable() > 0 && !(p.getBateauxDejaDeplaces().contains(bat1))){
                peutSeDep = true;
            }
            SimBoat newSB = new SimBoat(bat1.getHp(),bat1.getMove(),bat1.getMoveAvailable(),
                    new Point(bat1.getPosition().getX(),bat1.getPosition().getY()),bat1.getOrientation(),bat1.getDmgMainCanon(),
                    bat1.getDmgSecCanon(),bat1.getReloadMainCanon(),bat1.getReloadSecCanon(),
                    bat1.getMainCD(),bat1.getSecCD(),bat1.getJoueur().getNumber(),bat1.isEstBloque(),typeBat,!peutSeDep);
            tabBoat.add(newSB);
        }
        for(Boat bat2 : p.getMap().getBateaux2()){
            if(bat2 instanceof Amiral){
                typeBat = 0;
            } else if(bat2 instanceof Fregate){
                typeBat = 1;
            } else {
                typeBat = 2;
            }
            boolean peutSeDep = false;
            if(p.getBateauxDejaDeplaces().size() == 0){
                peutSeDep = true;
            } else if(bat2.getMoveAvailable() > 0 && (p.getBateauxDejaDeplaces().get(p.getBateauxDejaDeplaces().size() - 1).equals(bat2))){
                peutSeDep = true;
            } else if(bat2.getMoveAvailable() > 0 && !(p.getBateauxDejaDeplaces().contains(bat2))){
                peutSeDep = true;
            }
            SimBoat newSB = new SimBoat(bat2.getHp(),bat2.getMove(),bat2.getMoveAvailable(),
                    new Point(bat2.getPosition().getX(),bat2.getPosition().getY()),bat2.getOrientation(),bat2.getDmgMainCanon(),
                    bat2.getDmgSecCanon(),bat2.getReloadMainCanon(),bat2.getReloadSecCanon(),
                    bat2.getMainCD(),bat2.getSecCD(),bat2.getJoueur().getNumber(),bat2.isEstBloque(),typeBat,!peutSeDep);
            tabBoat.add(newSB);
        }
        return new SimPartie(simM,currentPlayer,winner,tabBoat);
    }
}
