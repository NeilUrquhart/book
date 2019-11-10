package book.ch2;

import java.util.ArrayList;
import java.util.Collections;

import book.ch1.Visit;

public class ClarkeWright extends VRPSolver {

	@Override
	public void solve() {
		// TODO Auto-generated method stub
		
		
		//Calculate savings
		
		ArrayList<Saving> savings = new ArrayList<Saving>();
		for (int countX =0; countX <  super.theProblem.getSize(); countX ++){
			Object ox = super.theProblem.getCustomers().get(countX);
			for (int countY = countX +1; countY <  super.theProblem.getSize(); countY++){
				Object oy = super.theProblem.getCustomers().get(countY);
				if (ox != oy){//if(countX != countY){
					VRPVisit x = (VRPVisit)ox;//super.theProblem.getCustomers().get(countX);
					VRPVisit y = (VRPVisit)oy;// super.theProblem.getCustomers().get(countY);
					
					double cost = super.theProblem.getDistance(x, super.theProblem.getStart()) +
							super.theProblem.getDistance(y, super.theProblem.getStart());
					double direct = super.theProblem.getDistance(x,y);

					double saving = cost - direct;
					
					if (saving > 0){
						savings.add(new Saving(x,y,saving));
					}

				}
			}

		}
		Collections.sort(savings);
			//Create default solution
		ArrayList<ArrayList<VRPVisit>> solution = new ArrayList<ArrayList<VRPVisit>> ();
		for (Object oV : super.theProblem.getCustomers()){
			ArrayList<VRPVisit> route = new ArrayList<VRPVisit>();
			route.add((VRPVisit)oV);
			solution.add(route);
		}
		
		for (Saving s : savings){
			ArrayList<VRPVisit> routeX = null, routeY =null;
			boolean found = false;
			for (ArrayList<VRPVisit> route : solution){
				if (end(route)==s.x)
					routeX = route;
				if (start(route)== s.y)
					routeY = route;	
				
			}
			if ((routeX != null)&&(routeY != null)&&(routeX != routeY)){
				//Check capacities
			    //If within capacity - join the routes
				if ((routeDemand(routeX)+ routeDemand(routeY))<=super.theProblem.getCapacity()){
					routeX.addAll(routeY);
					solution.remove(routeY);
					found = true;
				}
			}
			if (!found){
				for (ArrayList<VRPVisit> route : solution){
					if (end(route)==s.y)
						routeX = route;
					if (start(route)== s.x)
						routeY = route;	
					
				}
				if ((routeX != null)&&(routeY != null)&&(routeX != routeY)){
					//Check capacities
				    //If within capacity - join the routes
					if ((routeDemand(routeX)+ routeDemand(routeY))<=super.theProblem.getCapacity()){
						routeX.addAll(routeY);
						solution.remove(routeY);
						found = true;
					}
				}
			}
		}
		super.theProblem.setSolution(solution);
		
			
	}

	public int routeDemand(ArrayList<VRPVisit> route){
		int demand=0;
		for (VRPVisit visit: route){
			demand += visit.getDemand();
		}
		return demand;
	}
	public VRPVisit end(ArrayList<VRPVisit> route){
		if (route.size()==0)
			return null;
		
		return route.get(route.size()-1);
	}
	
	public VRPVisit start(ArrayList<VRPVisit> route){
		if (route.size()==0)
			return null;
		
		return route.get(0);
	}
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
