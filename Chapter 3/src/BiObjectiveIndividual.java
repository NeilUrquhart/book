import java.util.ArrayList;

import second.Domination;
import book.ch1.Visit;
import book.ch2.CVRPProblem;
import book.ch2.Individual;
import book.ch2.VRPVisit;


public class BiObjectiveIndividual extends Individual {
	public enum Objective{
		VEHICLES,
		CUST_SERVICE
	}
	
	private static Objective evalObjective;

	
	public BiObjectiveIndividual(CVRPProblem prob) {
		super(prob);
	}
	
	public BiObjectiveIndividual(CVRPProblem cvrpProblem,
			Individual tournamentSelection, Individual tournamentSelection2) {
		super(cvrpProblem,tournamentSelection,  tournamentSelection2);
	}

	public static void setObjective(Objective o){
		evalObjective =o;
	}
	
	//get Vehicles
	public int getVehicles() {
		return super.getVehicles();
	}
	
	//Get Customer Service
	public double getCustService() {
		if (super.phenotype == null)
			//If the genotype has been changed then evaluate
			evaluate();
		
		//Get Dist to customers
		double totTime=0;
		for (ArrayList<VRPVisit> route : super.getPhenotype()){
		  double routeTime=0;
		  Visit prev = super.problem.getStart();
		  for (VRPVisit v : route){
			  double dist = super.problem.getDistance(prev,v);
			  routeTime = routeTime + dist;
			  totTime = totTime + routeTime;
			  
		  }
		}
		return totTime;
	}
	
	public BiObjectiveIndividual copy(){
		//Create a new individual that is a direct copy of this individual
		BiObjectiveIndividual res= new BiObjectiveIndividual(this.problem);
		res.genotype = (ArrayList<VRPVisit>) super.genotype.clone();
		
		return res;

	}
	public boolean dominates(BiObjectiveIndividual other) {
		//True if we dominate the other
		
		if (this.getCustService() > other.getCustService())
			return false;
		if (this.getVehicles() > other.getVehicles())
			return false;
		
		if ((this.getVehicles() < other.getVehicles())||(this.getCustService() < other.getCustService()))
			return true;
		
		return false;
					
	    			
	      
	}
	
	public double evaluate(){
		super.evaluate();//built solution
		if (evalObjective == Objective.VEHICLES)
			return this.getVehicles();
		else
			return this.getCustService();
	}

	
}
