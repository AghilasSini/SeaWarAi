package fr.lesprogbretons.seawar.screen;

import com.badlogic.gdx.ScreenAdapter;
import fr.lesprogbretons.seawar.screen.manager.GameMapManager;

import static fr.lesprogbretons.seawar.SeaWar.game;
import static fr.lesprogbretons.seawar.SeaWar.seaWarController;

/**
 * Classe qui permet d'afficher un menu
 * <p>
 * Inspiré par PixelScientists
 */
public class SeaWarMenuScreen extends ScreenAdapter {
	
	
	
    @Override
    public void show() {
        seaWarController.nouvellePartie();
        game.setScreen(new SeaWarMapScreen(new GameMapManager()));
    }
}