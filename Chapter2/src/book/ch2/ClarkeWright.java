package book.ch2;

import java.util.ArrayList;
import java.util.Collections;

import book.ch1.Visit;

/*
 * Neil Urquhart 2019
 * An implementation of the Clarke and Wright savings algorithm
 * 
 */
public class ClarkeWright extends VRPSolver {

	@Override
	public void solve() {
		//1.Create the initial solution (1 vehicle per customer)
		ArrayList<ArrayList<VRPVisit>> solution = createDefaultSolution();
		//2.Find Savings
		ArrayList<Saving> savings = findSavings();
		//3.Sort the savings into the order of largest saving first
		Collections.sort(savings);
		applySavinngs(solution, savings);
		//3.Check solution
		checkSolution(solution);
		//4.Adopt solution 
		super.theProblem.setSolution(solution);
	}

	private ArrayList<Saving> findSavings() {
		ArrayList<Saving> savings = new ArrayList<Saving>();//Store savings in this list
		//Check the savings to be made for each pair of customers
		for (int countX =0; countX <  super.theProblem.getSize(); countX ++){
			for (int countY = countX +1; countY <  super.theProblem.getSize(); countY++){
				VRPVisit x = (VRPVisit)super.theProblem.getVisits().get(countX);
				VRPVisit y = (VRPVisit)super.theProblem.getVisits().get(countY);
				double cost = super.theProblem.getDistance(x, super.theProblem.getStart()) +super.theProblem.getDistance(y, super.theProblem.getStart());
				double direct = super.theProblem.getDistance(x,y);
				double saving = cost - direct;
				savings.add(new Saving(x,y,saving));
			}
		}
		return savings;
	}

	private void applySavinngs(ArrayList<ArrayList<VRPVisit>> solution, ArrayList<Saving> savings) {
		//Search through the savings and see which can be implemented
		for (Saving s : savings){
			ArrayList<VRPVisit> routeX = null, routeY =null;
			boolean found = false;
			//Find the routes that contain the customers
			//at the start/end
			for (ArrayList<VRPVisit> route : solution){
				if (end(route)==s.x)
					routeX = route;
				if (start(route)== s.y)
					routeY = route;	
			}
			if ((routeX != null)&&(routeY != null)){
				found = joinRoutes(solution, routeX, routeY);
			}
			if (!found){//Check for the reverse (y to x)
				for (ArrayList<VRPVisit> route : solution){
					if (end(route)==s.y)
						routeX = route;
					if (start(route)== s.x)
						routeY = route;	
				}
				if ((routeX != null)&&(routeY != null)){
					joinRoutes(solution, routeX, routeY);
				}
			}
		}
	}



	
	private void checkSolution(ArrayList<ArrayList<VRPVisit>> solution) {
	    //Check the validity of <solution>
		int size = super.theProblem.getSize();
		ArrayList<VRPVisit> checkList = new ArrayList<VRPVisit>();
		for (ArrayList<VRPVisit> route: solution){
			for (VRPVisit v : route){
				if (checkList.contains(v)){
					System.out.println("Duplicate visit error");
				}
			}
			if (routeDemand(route) > super.theProblem.getCapacity()){
				System.out.println("Capacity error!");
			}
			size = size - route.size();
		}
		if (size != 0 )
			System.out.println("Size error");
	}
	private boolean joinRoutes(ArrayList<ArrayList<VRPVisit>> solution,ArrayList<VRPVisit> routeX, ArrayList<VRPVisit> routeY) {
		/*
		 * Join route Y to the end of route X.
		 * Return false, if join not possible due to capacities
		 */
		if(routeX != routeY){
			//Check capacities
			if ((routeDemand(routeX)+ routeDemand(routeY))<=super.theProblem.getCapacity()){
				routeX.addAll(routeY);
				solution.remove(routeY);
				return true;
			}
		}
		return false;
	}

	private ArrayList<ArrayList<VRPVisit>> createDefaultSolution() {
		/*
		 * Create a solution that has one route/vehicle per customer
		 */
		ArrayList<ArrayList<VRPVisit>> solution = new ArrayList<ArrayList<VRPVisit>> ();
		for (Object oV : super.theProblem.getVisits()){
			ArrayList<VRPVisit> route = new ArrayList<VRPVisit>();
			route.add((VRPVisit)oV);
			solution.add(route);
		}
		return solution;
	}

	public int routeDemand(ArrayList<VRPVisit> route){
		/*
		 * Return the total demand of the customers in <route>
		 */
		int demand=0;
		for (VRPVisit visit: route){
			demand += visit.getDemand();
		}
		return demand;
	}
	public VRPVisit end(ArrayList<VRPVisit> route){
		//Return the last customer in <route>
		if (route.size()==0)
			return null;
		
		return route.get(route.size()-1);
	}
	
	public VRPVisit start(ArrayList<VRPVisit> route){
		//Retunr the first customer in route
		if (route.size()==0)
			return null;
		
		return route.get(0);
	}
	
	/*
	 * Saving is an inner class used to hold details of specific saving between 2 visits
	 */
    class Saving implements Comparable{
    	private VRPVisit x,y;
    	private double saving;
    	
	  	public Saving(VRPVisit x, VRPVisit y, double saving){
	  		this.x = x;
	  		this.y = y;
	  		this.saving = saving;
	  	}
	  	
	  	public Visit getX(){
	  		return x;
	  	}
	  	
	  	public Visit getY(){
	  		return y;
	  	}
	  	
	  	public double getSaving(){
	  		return saving;
	  	}

		@Override
		public int compareTo(Object o) {
			Saving other = (Saving)o;
			if (other.saving > this.saving)
				return 1;
			if (other.saving < this.saving)
				return -1;
			return 0;
		}
	}
}
