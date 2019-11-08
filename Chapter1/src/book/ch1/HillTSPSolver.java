package book.ch1;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/*
 * Neil Urquhart 2019
 * An implementaton of the 2-opt TSP heuristic
 * 
 */

public class HillTSPSolver extends TSPSolver{
	
	//Solve the problem
	public void solve(){
		Random rnd = new Random();
		
		//repeat until no improvement is made
		int timeOut =500000;

		TSPSolver nn = new NearestNTSPSolver();
		nn.setProblem(this.theProblem);
		nn.solve();
	
		
		while(timeOut >0){
			double best_distance = theProblem.getDistance();//The starting solution

			int i = rnd.nextInt(theProblem.getSolution().size()-1);
			int k=i;

			while (k <=i) 
				k = rnd.nextInt(theProblem.getSolution().size());

			timeOut --;
			ArrayList<Visit> new_route = /*swap(theProblem.getSolution(),i,k);*/twoOptSwap(theProblem.getSolution(),i,k);//Undertake the swap
			double new_distance = theProblem.getDistance(new_route);
			if (new_distance < best_distance) {//If the modified route is shorter (ie the swap has worked)
				theProblem.setRoute(new_route);//The modified route now becomes the current route
				timeOut =500000;
				best_distance = new_distance;

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

	private ArrayList<Visit>  swap(ArrayList<Visit> route, int a, int b) {
		//Perform a 2-opt swap based on the visits positions a and b
		//Return the modified route in a new arrayLisy

		ArrayList<Visit> modifiedRoute = new ArrayList<Visit>(route);
		Visit v = modifiedRoute.remove(a);
		modifiedRoute.add(b,v);
		return modifiedRoute;
	}
}

