package book.ch2;

import java.util.ArrayList;
import java.util.Collections;

import book.ch1.Visit;

/*
 * Neil Urquhart 2019
 * 
 * This (simple) solver splits a grand tour contained in VRPSolver.theProblem into a set of sub tours
 * that respects the capacity constraints.
 * 
 */

public class GrandTour extends VRPSolver {

	@Override
	public void solve() {
		ArrayList<ArrayList<VRPVisit>> solution = new ArrayList<ArrayList<VRPVisit>> ();
		ArrayList<VRPVisit> currentRoute = new ArrayList<VRPVisit>();
		
		for (Visit v : super.theProblem.getSolution()){
			VRPVisit currentVisit = (VRPVisit)v;
			//Can current visit be appended to the current route?
			if (currentVisit.getDemand() + routeDemand(currentRoute) >  this.theProblem.getCapacity()){
				//Add current route to solution and begin a new route
				solution.add(currentRoute);
				currentRoute = new ArrayList<VRPVisit>();
			}
			//Add visit to the vurrent route
			currentRoute.add(currentVisit);
		}
		solution.add(currentRoute);
		super.theProblem.setSolution(solution);


	}

	public int routeDemand(ArrayList<VRPVisit> route){
		//Calculate the total demand for <route>
		int demand=0;
		for (VRPVisit visit: route){
			demand += visit.getDemand();
		}
		return demand;
	}
}
