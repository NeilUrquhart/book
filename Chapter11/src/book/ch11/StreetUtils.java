package book.ch11;

import java.util.ArrayList;

import book.ch1.LatLon;

public class StreetUtils {
	/*
	 * Neil Urquhart 2021
	 * 
	 * A Helper class of methods for managing collections
	 * of House objects
	 */
	
	public static ArrayList<House> reverse(ArrayList<House> orig) {
		//Return a new ArrayList of House in reverse order
		ArrayList<House> res = new ArrayList<>();
		for(House h:orig)
		{
			res.add(0,h);
		}
		return  res;
	}
	
	public static ArrayList<House> nnSort(LatLon start, ArrayList<House> list) {
	//Sort the houses in <list> into nearest neighbour order, starting from <start>
		ArrayList<House> sorted = new ArrayList<House>();
		LatLon current = start;

		while(list.size()  >0) {
			double bestDistSoFar= Double.MAX_VALUE;
			House bestHouseSoFar = null;
			for (House h: list) {
				double t = current.getDist(h.getLocation());
				if ( t< bestDistSoFar) {
					bestDistSoFar = t;
					bestHouseSoFar = h;
				}
			}
			list.remove(bestHouseSoFar);
			sorted.add(bestHouseSoFar);
		}
		return sorted;
	}
}
