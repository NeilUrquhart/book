package book.ch2;

import java.util.ArrayList;
import java.util.Collections;

import book.ch1.Visit;

public class GrandTour extends VRPSolver {

	@Override
	public void solve() {
		// TODO Auto-generated method stub
		//Create default solution
		ArrayList<ArrayList<VRPVisit>> solution = new ArrayList<ArrayList<VRPVisit>> ();
		ArrayList<VRPVisit> route = new ArrayList<VRPVisit>();
		for (Visit v : super.theProblem.getSolution()){
			VRPVisit vrpv = (VRPVisit)v;
			if (vrpv.getDemand() + routeDemand(route) >  this.theProblem.getCapacity()){
				solution.add(route);
				route = new ArrayList<VRPVisit>();
			}
			route.add(vrpv);
			
		}
		solution.add(route);
		super.theProblem.setSolution(solution);
		
			
	}

	public int routeDemand(ArrayList<VRPVisit> route){
		int demand=0;
		for (VRPVisit visit: route){
			demand += visit.getDemand();
		}
		return demand;
	}
}
