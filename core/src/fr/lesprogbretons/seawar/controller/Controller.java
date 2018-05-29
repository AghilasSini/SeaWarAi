package fr.lesprogbretons.seawar.controller;



/* Classe Controller qui va gère l'interraction avec l'utilisateur */

import fr.lesprogbretons.seawar.ia.AbstractIA;
import fr.lesprogbretons.seawar.ia.IAAleatoire;
import fr.lesprogbretons.seawar.ia.IAThread;
import fr.lesprogbretons.seawar.model.Partie;
import fr.lesprogbretons.seawar.model.actions.*;
import fr.lesprogbretons.seawar.model.boat.Boat;
import fr.lesprogbretons.seawar.model.cases.Case;
import fr.lesprogbretons.seawar.model.map.Grille;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static fr.lesprogbretons.seawar.SeaWar.logger;

/**
 * Classe Controller
 */
public class Controller {

    private Partie partie;

    public Controller() {
    }

    public Controller(Partie partie) {
        this.partie = partie;
    }

    public void nouvellePartie() {
        partie = new Partie();
    }

    public void nouvellePartie(Grille g) {
        partie = new Partie(g);
    }

    public Object clone() {

        Partie clonePartie = (Partie) this.partie.clone();

        return new Controller(clonePartie);
    }

    /**
     * Procédure qui gère la sélection d'une case quelconque à la souris
     *
     * @param x : colonne de la case sélectionnée
     * @param y : ligne de la case sélectionnée
     */
    public void selection(int x, int y) {
        Case c = partie.getMap().getCase(x, y);

        boolean actionFaite = false;

        //Les actions que veut faire le joueur avec le bateau qu'il a sélectionné
        if (partie.isAnyBateauSelectionne()) {

            ArrayList<Case> casesPorteeTir;
            casesPorteeTir = partie.getMap().getCasesPortees(partie.getBateauSelectionne());


            if (partie.getMap().casePossedeBateau(c, partie.getCurrentPlayer()) && !(partie.getMap().bateauSurCase(c).equals(partie.getBateauSelectionne()))) {
                partie.setBateauSelectionne(partie.getMap().bateauSurCase(c));
                actionFaite = true;
            }

            //Si la case sélectionnée est à portée de tir
            else if (casesPorteeTir.contains(c)) {
                if (partie.getMap().casePossedeBateau(c, partie.getOtherPlayer())) {
                    partie.getBateauSelectionne().shoot(partie.getMap().bateauSurCase(c));
                    if (!partie.getMap().bateauSurCase(c).isAlive()) {
                        c.setBateauDetruit(true);
                        if (partie.getCurrentPlayer().getNumber() == 1) {
                            partie.getMap().getBateaux2().remove(partie.getMap().bateauSurCase(c));
                        } else {
                            partie.getMap().getBateaux1().remove(partie.getMap().bateauSurCase(c));
                        }
                    }
                    partie.setAnyBateauSelectionne(false);
                    actionFaite = true;
                }
            }

            if (!actionFaite) {
                ArrayList<Case> casesDispo = partie.getMap().getCasesDisponibles(partie.getBateauSelectionne().getPosition(), 1);

                //Si la case sélectionnée est à portée de déplacement
                if (partie.getBateauxDejaDeplaces().size() == 0 && casesDispo.contains(c) && partie.getBateauSelectionne().getMoveAvailable() > 0) {
                    partie.getBateauSelectionne().moveBoat(c);
                    partie.ajouterBateauxDejaDeplaces(partie.getBateauSelectionne());
                   /* if (c.isPhare()) {
                        partie.getMap().prendPhare(c, partie.getCurrentPlayer());
                    }*/
                } else if (casesDispo.contains(c) && partie.getBateauSelectionne().getMoveAvailable() > 0 && partie.getBateauxDejaDeplaces().get(partie.getBateauxDejaDeplaces().size() - 1).equals(partie.getBateauSelectionne())) {
                    partie.getBateauSelectionne().moveBoat(c);
                    partie.ajouterBateauxDejaDeplaces(partie.getBateauSelectionne());
                   /* if (c.isPhare()) {
                        partie.getMap().prendPhare(c, partie.getCurrentPlayer());
                    }*/
                } else if (casesDispo.contains(c) && partie.getBateauSelectionne().getMoveAvailable() > 0 && !(partie.getBateauxDejaDeplaces().contains(partie.getBateauSelectionne()))) {
                    partie.getBateauxDejaDeplaces().get(partie.getBateauxDejaDeplaces().size() - 1).setMoveAvailable(0);
                    partie.getBateauSelectionne().moveBoat(c);
                    partie.ajouterBateauxDejaDeplaces(partie.getBateauSelectionne());
                   /* if (c.isPhare()) {
                        partie.getMap().prendPhare(c, partie.getCurrentPlayer());
                    }*/
                } else {
                    partie.setAnyBateauSelectionne(true);
                }

            }


        } else {
            //Si le joueur sélectionne un de ses bateaux
            if (partie.getMap().casePossedeBateau(c, partie.getCurrentPlayer())) {
                partie.setBateauSelectionne(partie.getMap().bateauSurCase(c));
            }

        }
    }

    public void setPartie(Partie partie) {
        this.partie = partie;
    }

    public Partie getPartie() {
        return partie;
    }

    /**
     * Procédure qui finit le tour du joueur quand on appuie sur le bouton fin de tour
     */
    public boolean endTurn() {
        boolean isOver = false;
        partie.finPartie();

        if (!partie.isFin()) {
            partie.setAnyBateauSelectionne(false);
            isOver = partie.endTurn();
        }

        return isOver;
    }

    public void changerCanon() {
        partie.getBateauSelectionne().setCanonSelectionne(3 - partie.getBateauSelectionne().getCanonSelectionne());

    }

    public void changerCanon(Boat b) {
        b.setCanonSelectionne(3 - b.getCanonSelectionne());

    }

    //** Partie IA **//
    @SuppressWarnings("deprecation")
    public synchronized void jouerIA() {

        if (partie.getCurrentPlayer() instanceof AbstractIA) {
            int identifier = partie.getCurrentPlayer().getNumber();


            ExecutorService executor = Executors.newSingleThreadExecutor();
            AbstractIA ia = (AbstractIA) partie.getCurrentPlayer();
            //TODO Change here
            IAThread calcul = new IAThread(ia, this, executor);
            executor.execute(calcul);
            try {
                if (!executor.awaitTermination(AbstractIA.TIME_TO_THINK, TimeUnit.MILLISECONDS)) {

                    executor.shutdown();
                }


            } catch (InterruptedException ex) {
                logger.error("Erreur arrêt IA", ex);
//                Logger.getLogger(Partie.class.getName()).log(Level.SEVERE, "Erreur arrêt IA", ex);

            }
            try {

                calcul.join();
            } catch (InterruptedException e) {
//                Logger.getLogger(Partie.class.getName()).log(Level.SEVERE, "Erreur IA", e);
                logger.error("Erreur IA", e);

            }
            Action action;


            if (calcul.getActionChoice() == null && ia.getMemorizedAction() == null) {

                logger.debug(Thread.currentThread().getName() + ":"
                        + " Aucune action choisie, aucune action memorisee");
                logger.debug("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                logger.debug("!!!!!!!!!!!!!!!!!!!			HASARD		 !!!!!!!!!!!!");
                logger.debug("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");


                action = (new IAAleatoire(identifier).chooseAction(this));
            } else if (calcul.getActionChoice() == null && ia.getMemorizedAction() != null) {

                logger.debug(Thread.currentThread().getName() + ":"
                        + "Aucune action choisie mais action mémorisée");
                action = ia.getMemorizedAction();


            } else {

                action = calcul.getActionChoice();

            }

            while (!validAction(ia, action)) {
                logger.debug(Thread.currentThread().getName() + ":" + "Action non valide");
                System.exit(0);
                action = (new IAAleatoire(identifier).chooseAction(this));
            }

            logger.error("Action jouee = " + action);
            action.apply(this);

            // Kill remaining IAThread threads
            for (Thread t : Thread.getAllStackTraces().keySet()) {
                for (StackTraceElement ste : t.getStackTrace()) {
                    if (ste.getClassName().equals("fr.lesprogbretons.seawar.ia.IAThread")) {
                        t.stop();
                    }
                }
            }

            try {

                Thread.sleep(200);
            } catch (InterruptedException ex) {


            }

        }


    }


    private boolean validAction(AbstractIA ia, Action action) {
        if (action instanceof PassTurn) {
            return true;
        } else if (action instanceof MoveBoat) {
            return true;
        } else if (action instanceof Attack) {
            return true;
        } else if (action instanceof ChangeCannon) {
            return true;
        } else {
            return false;
        }
    }

    public List<Action> getPossibleActions() {
        ArrayList<Action> actions = new ArrayList<>();
        Boat boat = this.partie.getBateauSelectionne();
        // potential of the seletected boat
        ArrayList<Case> cases = new ArrayList<>();

        if (boat.getMoveAvailable() != 0 && !this.partie.getMap().getCasesDisponibles(boat.getPosition(), 1).isEmpty()) {
            cases = this.partie.getMap().getCasesDisponibles(boat.getPosition(), 1);

        } else {

            System.err.println("no case are available");
        }

        if (!cases.isEmpty()) {
            for (Case target : cases) {
                actions.add(new MoveBoat(boat, target));
            }
        }
        
        // attack possibilities
        ArrayList<Case> boatInRange = this.partie.getMap().getBoatInRange(boat, this.partie.getOtherPlayer());
        if (!boatInRange.isEmpty() && boat.canShoot()) {
            for (Case target : boatInRange) {
                actions.add(new Attack(boat, target));
            }
        }

        // pass the turn without doing nothing
        actions.add(new PassTurn(boat));


        return actions;
    }

    public void launchTurn() {
        Thread t = new Thread(() -> {
            logger.debug("Thread launched");
            //logger.debug("Is there any Information");
            while (partie.getCurrentPlayer() instanceof AbstractIA) {
                logger.debug("Some thing happen or nothing");
                jouerIA();
                endTurn();
                //setCurrentPlayer(getJoueur1());
            }
        });
        t.start();
    }
}
