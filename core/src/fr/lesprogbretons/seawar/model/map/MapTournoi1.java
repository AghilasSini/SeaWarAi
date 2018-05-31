package fr.lesprogbretons.seawar.model.map;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

import fr.lesprogbretons.seawar.model.Orientation;
import fr.lesprogbretons.seawar.model.boat.Amiral;
import fr.lesprogbretons.seawar.model.boat.Boat;
import fr.lesprogbretons.seawar.model.cases.CaseTerre;

public class MapTournoi1 extends Grille{

	 private final int hauteur = 13;
	    private final int largeur = 11;
	    private final int nbrLightHouse = 3;

	    /**
	     * Amiral / Joueur 1
	     */
	    private final int posAmiralXJ1 = 10;
	    private final int posAmiralYJ1 = 0;
	    /**
	     * Fregate / Joueur 1
	     */
	    private final int posFregXJ1 = 10;
	    private final int posFregYJ1 = 1;

	    /**
	     * Amiral / Joueur 1
	     */
	    private final int posAmiralXJ2 = 1;
	    private final int posAmiralYJ2 = 12;
	    /**
	     * Fregate / Joueur 1
	     */
	    private final int posFregXJ2 = 0;
	    private final int posFregYJ2 = 12;

	    private List<Integer> freeCases;


	    public MapTournoi1(int hauteur, int largeur) {
	        super(hauteur, largeur);
	        this.freeCases = new ArrayList<Integer>();
	        for (int j = 0; j < hauteur * largeur; j++) {
	            freeCases.add(j);
	        }


	        bateaux1.add(new Amiral(tableau[posAmiralXJ1][posAmiralYJ1], getJoueur1()));
	        bateaux1.get(0).setOrientation(Orientation.SUDEST);

	        //bateaux1.add(new Fregate(tableau[posFregXJ1][posFregYJ1], getJoueur1()));
	        //bateaux1.get(1).setOrientation(Orientation.SUDEST);

	        int absPosAmiralJ1 = posAmiralXJ1 + hauteur * posAmiralYJ1;
	        freeCases.remove(absPosAmiralJ1);
	        //int absPosFregJ1=posFregXJ1+hauteur*posFregYJ1;
	        //freeCases.remove(absPosFregJ1);


	        bateaux2.add(new Amiral(tableau[posAmiralXJ2][posAmiralYJ2], getJoueur2()));
	        bateaux2.get(0).setOrientation(Orientation.NORDOUEST);
	        //bateaux2.add(new Fregate(tableau[posFregXJ2][posFregYJ2], getJoueur2()));
	        //bateaux2.get(1).setOrientation(Orientation.NORDOUEST);

	        int absPosAmiralJ2 = hauteur * posAmiralYJ2 + posAmiralXJ2;
	        freeCases.remove(absPosAmiralJ2);
			//int absPosFregJ2=hauteur*posFregYJ2+posFregXJ2;
			//freeCases.remove(absPosFregJ2);
			
	        
	        tableau[hauteur-3][largeur-2] = new CaseTerre(hauteur-3, largeur-2);
	        tableau[hauteur-3][largeur-4] = new CaseTerre(hauteur-3, largeur-4);
	        tableau[hauteur-3][largeur-5] = new CaseTerre(hauteur-3, largeur-5);
	        tableau[hauteur-3][largeur-3] = new CaseTerre(hauteur-3, largeur-3);
	        tableau[hauteur-3][largeur-3] = new CaseTerre(hauteur-3, largeur-3);
	        
	        
	        tableau[2][1] = new CaseTerre(2,1);
	        tableau[2][3] = new CaseTerre(2,3);
	        tableau[2][4] = new CaseTerre(2,4);
	        tableau[2][2] = new CaseTerre(2,2);
	        tableau[2][2] = new CaseTerre(2,2);
	        
	        
//	        tableau[5][6] = new CaseTerre(2,1);
//	        tableau[2][3] = new CaseTerre(2,3);
//	        tableau[2][4] = new CaseTerre(2,4);
//	        tableau[2][2] = new CaseTerre(2,2);
//	        tableau[2][2] = new CaseTerre(2,2);
//	        
	        
	        
//	        tableau[8][9] = new CaseTerre(8, 9);
//	        tableau[5][10] = new CaseTerre(5, 10);
//	        tableau[4][11] = new CaseTerre(4, 11);
//	        tableau[5][11] = new CaseTerre(5, 11);
//	        tableau[5][12] = new CaseTerre(5, 12);
//	        tableau[5][0] = new CaseTerre(5, 0);
//	        tableau[4][1] = new CaseTerre(4, 1);
//	        tableau[5][1] = new CaseTerre(5, 1);
//	        tableau[5][2] = new CaseTerre(5, 2);
//	        tableau[6][2] = new CaseTerre(6, 2);
//	        tableau[5][3] = new CaseTerre(5, 3);
//	        tableau[2][4] = new CaseTerre(2, 4);
//	        tableau[3][4] = new CaseTerre(3, 4);
//	        tableau[8][4] = new CaseTerre(8, 4);
//	        tableau[9][4] = new CaseTerre(9, 4);
//	        tableau[3][5] = new CaseTerre(3, 5);
//	        tableau[7][5] = new CaseTerre(7, 5);
//	        tableau[9][5] = new CaseTerre(9, 5);
//	        tableau[10][5] = new CaseTerre(10, 5);
//	        tableau[4][6] = new CaseTerre(4, 6);
//	        tableau[7][6] = new CaseTerre(7, 6);
//	        tableau[0][7] = new CaseTerre(0, 7);
//	        tableau[1][7] = new CaseTerre(1, 7);
//	        tableau[3][7] = new CaseTerre(3, 7);
//	        tableau[7][7] = new CaseTerre(7, 7);

	        tableau[5][6].setPhare(true);
	        tableau[10][10].setPhare(true);
	        tableau[0][1].setPhare(true);
			
			
			
		}
		
		private void randomizePharePosition() {
			
			// put randomly the lighthouse position on the table 
					
		}
		
		private List<Integer> updateFreeCases(){
			
			
			return freeCases;
		}
		
		
		private Vector2 randomizer(){
			
			
			int x= (int) (Math.random() * (hauteur - 0.01));
			int y= (int) (Math.random() * (largeur - 0.01));
			
			return new Vector2(x,y);
			
		}















}
