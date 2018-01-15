package kroa100;

import java.awt.Point;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class DistanceMatrix {
	
	
	private HashSet<Distance> distances;
	private final int NBVILLES;
	
	public DistanceMatrix(int nbVilles) {
		this.NBVILLES = nbVilles;
		this.distances = new HashSet<>();
	}
	
	
	public void generateDistance(HashMap<String,Point> coordonates) {
		if( coordonates.size() != NBVILLES ) {
			System.err.println("Nombre de villes incorrect");
			return;
		}
		
		Distance dist;
		for(String s : coordonates.keySet())
			for(String s2 : coordonates.keySet()) {
				if(s == s2)
					continue;
				dist = new Distance(s,s2);
				dist.setValue(Math.sqrt( 
						Math.pow(coordonates.get(s).x - coordonates.get(s2).x, 2) 
					  + Math.pow(coordonates.get(s).y - coordonates.get(s).y, 2) ));
				distances.add(dist);
			}
	}
	
	
	public double computeCost(String[] path) {
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
	
	
	public String[] bestPathUsingNNHeurisitic() {
		
	}
	
	public String[] NNHeuristic(String departure) {
		
	}
}
