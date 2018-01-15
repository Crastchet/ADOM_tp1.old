package kroa100;

import java.awt.Point;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class Main {
	
	public static int NBVILLES = 0;
	public static final PrintStream SYSTEMOUT = System.out;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		//File coordonates_file = new File("kroA100.txt");

		//HashMap<String,Point> coordonates = lireCoordonnees(coordonates_file));
		
		//DistanceMatrix matrix = new DistanceMatrix(NBVILLES);
		//matrix.generateDistance(coordonates);
		
		String[] test = new String[10];
		test[0] = "aaa";
		test[1] = "bbb";
		test[2] = "ccc";
		test[3] = "ddd";
		int tmp =1;
		test[tmp] = test[--tmp];
		System.out.println(test[0] + " " + test[1]);
		
		//NNHeuristic(matrice);
	}

	
	
	public static HashMap<String,Point> lireCoordonnees(File file) {
		Scanner sc = null;
		try {
			sc = new Scanner(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String line;
		HashMap<String,Point> coordonates = new HashMap<String,Point>();
		
		NBVILLES = 0;
		while( sc.hasNextLine() ) {
			line = sc.nextLine();
			if( line.compareTo("EOF") == 0 )
				break;
			NBVILLES++;
			String cityNb = line.split(" ")[0];
			int cityX = Integer.parseInt( line.split(" ")[1] );
			int cityY = Integer.parseInt( line.split(" ")[2] );
			coordonates.put( cityNb , new Point(cityX, cityY) );
		}
		return coordonates;
	}
	
	/*
	public static int[][] creerMatrice(HashMap<Integer,Point> coordonates) {
		int[][] matrice = new int[NBVILLES+1][NBVILLES+1];
		for(int a=1; a<=NBVILLES; a++) {
			for(int b=a; b<=NBVILLES; b++) {
				double x = ( (double) coordonates.get(a).x - (double) coordonates.get(b).x );
				double y = ( (double) coordonates.get(a).y - (double) coordonates.get(b).y );
				matrice[a][b] = (int) Math.round( Math.sqrt( x * x + y * y ) );
			}
		}
		return matrice;
	}
	*/
	/*
	public static int calculerCout(int[][] matrice, int[] parcours) {
		int nbUniqueValues = (int) Arrays.stream(parcours).distinct().count();
		int nbValues = parcours.length;
		
		if(nbUniqueValues != nbValues) {
			System.err.println("Une ville ne doit être parcourue qu'une seule fois");
			return 0;
		}
		if(nbValues != NBVILLES) {
			System.err.println("Le nombre de villes à parcourir est différent du nombre de villes total");
			return 0;
		}
		
		int coutCumule = 0;
		//coût du parcours des villes en partant de la première jusqu'à la dernière ville
		for(int a=0; a<NBVILLES-1; a++)
			coutCumule += matrice[parcours[a]][parcours[a+1]];
		//coût du derniers parcours : de la dernière ville à la première ville (retour point départ)
		coutCumule += matrice[parcours[NBVILLES-1]][parcours[0]];
		
		return coutCumule;
	}
	*/
	
	public static String[] generateRandomParcours(Set cities) {
		Random rd = new Random();
		String[] parcours = new String[NBVILLES];
		ArrayList<String> arrayCities = new ArrayList<>(cities);
		
		//Généaration aléatoire parcours
		int tmp=NBVILLES, nbRandom;
		while(tmp > 0)
			parcours[tmp-1] = arrayCities.remove(rd.nextInt(tmp--));
		return parcours;
	}
	
	
	/** Calcule le parcours "le plus court" avec la méthode du voisin le plus proche
	 * Toutes les villes sont testées en point de départ, chaque résultat est enregistré en fichier
	 * Le parcours le moins coûteux est indiqué
	 * 
	 * return le parcours le moins coûteux
	 */
	public static int[] NNHeuristic(int[][] matrice) {
		
		//on garde en mémoire une liste avec tous les numéros des villes
		ArrayList<Integer> allCities = new ArrayList<>();
		for(int i=1; i<=NBVILLES; i++)
			allCities.add(i);
		
		ArrayList<Integer> remainingCities;
		LinkedList<Integer> aGoodPath;
		//changeSystemOutToFile("goodpaths.txt");
		int costOfPath, costOfBestPath = -1;
		int[] bestPathArray = null;
		for(int departureCity=1; departureCity<=NBVILLES; departureCity++) {	//on parcours chacune des villes
			remainingCities = (ArrayList<Integer>) allCities.clone();			//on initialise les villes restantes en "toutes les villes"
			remainingCities.remove( new Integer(departureCity) );				//on enlève la ville de départ des villes restantes
			
			aGoodPath = NNHeuristicREC(matrice, departureCity, remainingCities);
			
			int[] aGoodPathArray = new int[NBVILLES];							//on récupère l'ArrayList dans un tableau
			aGoodPathArray[0] = departureCity;									//on oublie pas d'ajouter la ville de départ manuellement
			for(int i=1; i<NBVILLES; i++)
				aGoodPathArray[i] = aGoodPath.pop();	
			
			System.out.println("Path : " + pathToString(aGoodPathArray));		//on écrit en fichier le parcours
			costOfPath = calculerCout(matrice, aGoodPathArray);					//on calcule son coût
			System.out.println("Cost : " + costOfPath + "\n");					//on écrit en fichier le coût
			
			if(costOfBestPath == -1) {											//si c'est le premier tour
				costOfBestPath = costOfPath;									//meilleur coût = premier coût
				bestPathArray = aGoodPathArray;									//meilleur parcours = premier parcours
			}
			if(costOfBestPath > costOfPath) {									//si meilleur coût plus grand que dernier coût trouvé
				costOfBestPath = costOfPath;									//meilleur coût = dernier coût trouvé
				bestPathArray = aGoodPathArray;									//meilleur parcours = dernier parcours trouvé
			}
		}
		changeSystemOutToConsole();
		System.out.println("BEST PATH : " + pathToString(bestPathArray));						//on écrit en fichier le meilleur parcours
		System.out.println("COST : " + costOfBestPath);							//on écrit en fichier son coût
		return bestPathArray;													//on retourne le meilleur parcours trouvé
	}
	
	public static void changeSystemOutToFile(String file) {
		try {
			System.setOut(new PrintStream(new File("goodpaths.txt")));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void changeSystemOutToConsole() {
		System.setOut(SYSTEMOUT);
	}
		
	public static String pathToString(int[] path) {
		String p = path[0] + "";
		for(int i=1; i<path.length; i++)
			p += "-" + path[i];
		return p;
	}
		
	/**
	 * 
	 * @param matrice  La matrice modifiée au fur et à mesure 
	 * @param departureCity  La ville à laquelle nous sommes arrivés
	 * @param remainingCities  Liste indiquant les villes qui sont encore non parcourues
	 * @return Une pile représentant le parcours optimale à suivre, avec au sommet la ville de départ
	 */
	public static LinkedList<Integer> NNHeuristicREC(int[][] matrice, int departureCity, List<Integer> remainingCities) {
		
		if( remainingCities.isEmpty() )						//si on est à la dernière ville
			return new LinkedList<>();						//on retourne une liste vide;
		
		int[] distances =  matrice[departureCity];			//on récupère la liste des distances pour cette ville de départ
		int closestCity = remainingCities.get(0);			//on initialise une ville la plus proche
		int shortestDistance = matrice[departureCity][ closestCity ];	//on initialise une plus courte distance (on se base sur la ville choisie au desus)
		if(shortestDistance == 0)
			shortestDistance = matrice[ closestCity ][ departureCity ];
		for(int city : remainingCities) {					//on parcourt les villes restantes
			if( matrice[city][departureCity] < shortestDistance && matrice[city][departureCity] !=0 ) {		//si on trouve une ville  plus proche
				shortestDistance = matrice[city][departureCity];			//on sauvegarde la distance
				closestCity = city;							//on sauvegarde la ville
			}
			if( matrice[departureCity][city] < shortestDistance && matrice[departureCity][city] !=0 ) {		//si on trouve une ville  plus proche
				shortestDistance = matrice[departureCity][city];			//on sauvegarde la distance
				closestCity = city;							//on sauvegarde la ville
			}
		}
		
		remainingCities.remove( new Integer(closestCity) );	//on supprime la ville la plus proche des villes restantes
		LinkedList<Integer> list = NNHeuristicREC(matrice, closestCity, remainingCities);
		list.push(closestCity);
		return list;
	}
	
	
	public static void printMatrice(int[][] matrice) {
		//PRINT MATRICE
		for(int a=0; a<=NBVILLES; a++) {
			System.out.print("| ");
			for(int b=0; b<NBVILLES; b++) {
				if(matrice[a][b] > 999)
					System.out.print(matrice[a][b] + " | ");
				else if(matrice[a][b] > 99)
					System.out.print(matrice[a][b] + "  | ");
				else if(matrice[a][b] > 9)
					System.out.print(matrice[a][b] + "   | ");
				else
					System.out.print(matrice[a][b] + "    | ");
			}
			System.out.println();
		}
	}
	
	
	public static void printDistances(int[][] matrice) {
		//PRINT DISTANCES
		for(int a=1; a<=NBVILLES; a++)
			for(int b=1; b<=NBVILLES; b++)
				System.out.println("Distance ville " + a + " -- " + b + " : " + matrice[a][b]);
	}
	
}
