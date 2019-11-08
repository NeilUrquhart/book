package book.ch1;
import java.util.ArrayList;
import java.util.Collections;

/*
 * Neil Urquhart 2019
 * An exhaustive TSP solver. The solver uses a recursive function that generates each possible TSP route by swapping
 * pairs of cities.
 */

public class ExhaustiveTSPSolver extends TSPSolver  {

	private ArrayList<Visit> bestRoute;//Best route found so far
	private double lowestDist = Integer.MAX_VALUE;//Lowest distance found so far


	public ExhaustiveTSPSolver() {
	}

	//Solve the problem
	public void solve(){        
		lowestDist = Integer.MAX_VALUE;
		search(theProblem.getSolution(), 0, theProblem.getSize()-1);//Start generating permutations
		theProblem.setRoute(bestRoute);
	}

	//Recursive search function
	private  void search(ArrayList<Visit> visits, int l, int r)
	{
		if (l == r){//permutation completed - nothing left to search, check length
			theProblem.setRoute(visits);
			double len = theProblem.getDistance();
			if (len < lowestDist){//If current route is the lowest so far
				lowestDist = len;
				bestRoute = (ArrayList<Visit>)visits.clone();
			}
		}
		else
		{
			for (int i = l; i <= r; i++)
			{
				Collections.swap(visits, l, i);//Swap to create new permutation
				search(visits, l+1, r);//Recursively call this function
				Collections.swap(visits, l, i);//Undo swap
			}
		}
	}
}

