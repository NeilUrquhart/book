package book.ch1;
import java.util.ArrayList;
import java.util.Collections;

/*
 * Neil Urquhart 2019
 * An implementation of the Nearest Neighbour TSP solver
 * 
 */

public class NearestNTSPSolver extends TSPSolver{
	
	public void solve() {//Solve the problem
		ArrayList<Visit> newRoute = new ArrayList<Visit>();
		ArrayList<Visit> notVisited = (ArrayList<Visit>)theProblem.getSolution().clone();//Visits yet to be added
		Visit current = theProblem.getStart(); //Starting point
		while(notVisited.size()>0){//While there are visits still to add
			Visit next=null;
			double bestD= Double.MAX_VALUE;
			for(Visit possible: notVisited){//Check each unvisited visit
				if (theProblem.getDistance(current, possible)< bestD){
					next = possible;
					bestD = theProblem.getDistance(current, next);
				}
			}
			notVisited.remove(next);//Add the closest visit
			newRoute.add(next);
			current = next;
		}
		theProblem.setRoute(newRoute);
	}
}

