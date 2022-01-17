package book.ch1;
import java.util.ArrayList;
import java.util.Collections;

/*
 * Neil Urquhart 2019
 * An implementaton of the 2-opt TSP heuristic
 * 
 */

public class TwoOptTSPSolver extends TSPSolver{
	
	//Solve the problem
	public void solve(){        
		boolean improved = true;
		//repeat until no improvement is made 
		while(improved == true){
			improved= false;
			double best_distance = theProblem.getDistance();//The starting solution
			
			for(int i=0; i< theProblem.getSize(); i++){
				for (int k = i + 1; k < theProblem.getSize(); k++) {//loop through each pair of visits
					ArrayList<Visit> new_route = twoOptSwap(theProblem.getSolution(),i,k);//Undertake the swap
					double new_distance = theProblem.getDistance(new_route);
					if (new_distance < best_distance) {//If the modified route is shorter (ie the swap has worked)
						theProblem.setRoute(new_route);//The modified route now becomes the current route
						improved = true; 
						best_distance = new_distance;
						System.out.println("Improved ("+i+":"+k+")" + best_distance);
						break;//Break out of the loops
					}
				}
				if (improved)
					break;//Break out of the loops
			}
		}	
	}

	private ArrayList<Visit>  twoOptSwap(ArrayList<Visit> route, int a, int b) {
		//Perform a 2-opt swap based on the visits positions a and b
		//Return the modified route in a new arrayLisy

		ArrayList<Visit> modifiedRoute = new ArrayList<Visit>();
		Visit[] old = new Visit[route.size()];
		old = route.toArray(old);
		for(int c=0; c <= a; c++)
			modifiedRoute.add(old[c]);

		for(int c= b; c > a; c--)
			modifiedRoute.add(old[c]);

		for(int c= b+1; c < route.size(); c++)
			modifiedRoute.add(old[c]);

		return modifiedRoute;
	}

}

