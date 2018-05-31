package fr.lesprogbretons.seawar.desktop;

import java.lang.reflect.InvocationTargetException;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import fr.lesprogbretons.seawar.SeaWar;
import fr.lesprogbretons.seawar.model.Player;
import fr.lesprogbretons.seawar.model.map.Grille;


public class DesktopLauncher {
	public static void main (String[] arg) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.addIcon("icon/wheel-128.png", Files.FileType.Internal);
		config.addIcon("icon/wheel-32.png", Files.FileType.Internal);
		config.addIcon("icon/wheel-16.png", Files.FileType.Internal);
		config.title = "SeaWarsAi";
		config.width = 800;
		config.height = 480;
		config.x = 500;
		config.y = 0;
		config.resizable = false;
//System.out.println(arg[0]+arg[1]);
//		@SuppressWarnings("unchecked")
//		Class<Player> class1 = (Class<Player>) Class.forName(arg[0]);
//		@SuppressWarnings("unchecked")
//		Class<Player> class2 = (Class<Player>) Class.forName(arg[1]);
		
		
//		Player j1 = class1.getDeclaredConstructor(Integer.TYPE).newInstance(1);
//		Player j2 = class2.getDeclaredConstructor(Integer.TYPE).newInstance(2);
//		
//		System.out.println(j1.toString());
//
//		System.out.println(j2.toString());
//		
//		Grille.defaultPlayer1=j1;
//		Grille.defaultPlayer2=j2;
		
		new LwjglApplication(new SeaWar(), config);
	}
}
