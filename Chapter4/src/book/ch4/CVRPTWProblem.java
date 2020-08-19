package book.ch4;

import java.util.ArrayList;

import book.ch1.Visit;
import book.ch2.CVRPProblem;
import book.ch4.BiObjectiveTWIndividual.Route;
import book.ch4.BiObjectiveTWIndividual.VisitNode;

public class CVRPTWProblem extends CVRPProblem {

	public int getTravelMinutes(Visit x, Visit y) {
		int t=0;
		//Assume a speed of 40kmh = calculate as minutes per km i.e. 0.667 kms per min
		double v = 0.667;
		//Assume raw dist is kms
		double dist = super.getDistance(x, y);
		
		//t = d / v
		
		double timeMins = dist/v;
		
		//Not interested in seconds 
		return (int) timeMins;
		
	}
	
	public int getTime() {
		ArrayList a  = this.getCVRPSolution();
		ArrayList<Route> res = (ArrayList<Route>)a;
			int time=0;

			for (Route r : res)
				time += r.getTime();
			return time;

	
		
	}
	
	public double getIdle() {
		double idle=0;
		for (Object or : this.getCVRPSolution()) {
			Route r = (Route)or;
			idle += r.getIdle();
		}
		
		return idle;
	}
	
	public double getDistance() {
		double dist=0;
		for (Object or : this.getCVRPSolution()) {
			Route r = (Route)or;
			dist += r.getDist();
		}
		
		return dist;
	}
}
