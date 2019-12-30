package second;
import java.util.ArrayList;

import book.ch1.Visit;
import book.ch2.CVRPProblem;
import book.ch2.Individual;
import book.ch2.VRPVisit;


public class OLDBiObjectiveIndividual extends Individual {
	public enum Objective{
		VEHICLES,
		CUST_SERVICE
	}
	
	private static Objective evalObjective;

	
	public OLDBiObjectiveIndividual(CVRPProblem prob) {
		super(prob);
	}
	
	public OLDBiObjectiveIndividual(CVRPProblem cvrpProblem,
			Individual tournamentSelection, Individual tournamentSelection2) {
		super(cvrpProblem,tournamentSelection,  tournamentSelection2);
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
	
	public OLDBiObjectiveIndividual copy(){
		//Create a new individual that is a direct copy of this individual
		OLDBiObjectiveIndividual res= new OLDBiObjectiveIndividual(this.problem);
		res.genotype = (ArrayList<VRPVisit>) super.genotype.clone();
		
		return res;

	}
	public boolean dominates(OLDBiObjectiveIndividual other) {
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
