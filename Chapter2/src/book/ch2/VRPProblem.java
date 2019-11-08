package book.ch2;

import java.util.ArrayList;

import book.ch1.TSPProblem;
import book.ch1.Visit;

public class VRPProblem extends TSPProblem {
	
	private ArrayList<ArrayList<VRPVisit>> currentVRPSolution = new ArrayList<ArrayList<VRPVisit>> ();
	private VRPSolver mySolver = null;
	private int capacity;
	
	public void setSolution(ArrayList<ArrayList<VRPVisit>> aVRPSolution){
		currentVRPSolution = aVRPSolution;
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

		return getDistance(currentVRPSolution);
	}
	
	
	public double getDistance(ArrayList sol){
		ArrayList<ArrayList<VRPVisit>> solution = (ArrayList<ArrayList<VRPVisit>>) sol;
		double dist =0;
		for (ArrayList<VRPVisit> route: solution){
		    ArrayList<Visit> r = new ArrayList<Visit>(route);
			dist = dist + super.getDistance(r);
		}
		return dist;
	}
	
	public ArrayList getCustomers(){
		return super.getSolution();
	}
	

	public String toString(){
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
