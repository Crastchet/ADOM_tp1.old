package kroa100;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;

public class Matrix {
	
	private HashSet<City> cities;
	private HashSet<Distance> distances;
	private final int NBVILLES;
	
	public Matrix(int nbVilles) {
		this.NBVILLES = nbVilles;
		this.cities = new HashSet<>();
		this.distances = new HashSet<>();
	}
	
	
	public void generateDistance(HashSet<City> cities) {
		if( cities.size() != NBVILLES ) {
			System.err.println("Nombre de villes incorrect");
			return;
		}
		
		this.cities = cities;
		
		Distance dist;
		for(City c : cities)
			for(City c2 : cities) {
				if(c == c2)
					continue;
				dist = new Distance(c,c2);
				dist.setValue(Math.sqrt( 
						Math.pow(c.x() - c2.x(), 2) 
					  + Math.pow(c.y() - c2.y(), 2) ));
				this.distances.add(dist);
			}
	}
	
	
	public double computeCost(City[] path) {
		if( path.length != NBVILLES ) {
			System.err.println("Nombre de villes incorrect");
			return 0;
		}
		
		if(Arrays.stream(path).distinct().count() != path.length) {
			System.err.println("Une ville ne doit être parcourue qu'une seule fois");
			return 0;
		}
		
		ArrayList<Distance> tmpDistances = new ArrayList<>(this.distances);
		double cost = 0;
		int index;
		for(int i=0; i<NBVILLES; i++) {
			index = tmpDistances.indexOf( new Distance(path[i],path[(i+1)%NBVILLES]) );
			cost += tmpDistances.remove( index ).getValue();
		}
		return cost;
	}
	
	
	public City[] bestPathUsingNNHeurisitic() {
		int counter = 1;
		
		LinkedList<City> foundPath = null;
		double cost, bestCost=-1;
		City[] path, bestPath=null;
		for(City c : this.cities) {
			foundPath = this.NNHeuristic(c, new ArrayList<>(this.distances));
			foundPath.push(c);
			
			path = new City[NBVILLES];
			for(int i=0; i<NBVILLES; i++)
				path[i] = foundPath.pop();
			
			cost = this.computeCost(path);
			if(bestCost == -1 || cost < bestCost) {
				bestCost = cost;
				bestPath = path;
			}
			System.out.println(counter++);
			System.out.println("Path : " + Main.pathToString(path));
			System.out.println("Cost : " + cost + "\n");
		} 
		
		System.out.println("BEST PATH : " + Main.pathToString(bestPath));
		System.out.println("BEST COST : " + bestCost + "\n");
		return bestPath;
	}
	
	
	public LinkedList<City> NNHeuristic(City departure, ArrayList<Distance> remainingDistances) {
		if(remainingDistances.isEmpty())
			return new LinkedList<>();
		
		//générer la liste des distances possibles avec cette ville de départ
		ArrayList<Distance> possibleDistances = new ArrayList<>();
		for(Distance d : remainingDistances)
			if(d.getCityA().equals(departure) || d.getCityB().equals(departure))
				possibleDistances.add(d);
		
		//trouver la distance la plus petite
		Distance shortest = possibleDistances.get(0);
		for(Distance d : possibleDistances)
			if(d.getValue() < shortest.getValue())
				shortest = d;
		
		//retirer les distances liées à notre ville departure
		remainingDistances.removeAll(possibleDistances);
		
		//récupérer la ville de destination
		City destination = shortest.getCityA().equals(departure) ? shortest.getCityB() : shortest.getCityA();
		
		LinkedList<City> path = NNHeuristic(destination, remainingDistances);
		path.push(destination);
		return path;
	}
}
