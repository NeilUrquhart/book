package book.ch2;

import java.util.ArrayList;

import book.ch1.TSPProblem;
import book.ch1.Visit;

/*
 * Neil Urquhart 2019
 * This class represents a CVRP problem.
 * It extends the basic TSPProblem class.
 * 
 */
public class CVRPProblem extends TSPProblem {
	
	private ArrayList<ArrayList<VRPVisit>> currentVRPSolution = new ArrayList<ArrayList<VRPVisit>> ();
	//Represents the colection  of routes that comprise the solution
	private int capacity;
	//The vehicle capacity
	
	public void setSolution(ArrayList<ArrayList<VRPVisit>> aVRPSolution){
		currentVRPSolution = aVRPSolution;
	}
	
	public ArrayList<ArrayList<VRPVisit>> getCVRPSolution(){
		return this.currentVRPSolution;
	}
	public int getCapacity(){
		return capacity;
	}
	
	public void setCapacity(int capacity){
		this.capacity = capacity;
	}
	
	public void solve(VRPSolver mySolver){
		//Solve the problem using the supplied solver
		mySolver.setProblem(this);
		mySolver.solve(); 
	}

	public int getVehicles(){
		return currentVRPSolution.size();
	}
	
	public double getDistance(){
		//Return the total distance travelled by all of the vehicles
		return getDistance(currentVRPSolution);
	}
	
	
	public double getDistance(ArrayList sol){
		//Get the total distance covered by the solution in <sol>
		ArrayList<ArrayList<VRPVisit>> solution = (ArrayList<ArrayList<VRPVisit>>) sol;
		double dist =0;
		for (ArrayList<VRPVisit> route: solution){
		    ArrayList<Visit> r = new ArrayList<Visit>(route);
			dist = dist + super.getDistance(r);
		}
		return dist;
	}
	
	public ArrayList getVisits(){
		return super.getSolution();
	}
	
	public String toString(){
		//Return a string representation of the solution
		String buffer="";
		for (ArrayList<VRPVisit> route: currentVRPSolution){
			buffer = buffer +"Route: ";
			for (VRPVisit v : route){
				buffer = buffer +(" : "+v);
			}
			buffer = buffer +"\n";
		}
		return buffer;
	}
}
