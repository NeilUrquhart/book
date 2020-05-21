package book.ch8.humanitarian;

import java.util.ArrayList;

import book.ch1.Visit;
import book.ch2.CVRPProblem;
import book.ch2.Individual;
import book.ch2.VRPVisit;
import book.ch7.OSMAccessHelper;

public class HudIndividual extends Individual {
	private static int evals=0;
	
	
	public HudIndividual(CVRPProblem prob) {
		super(prob);
	}
	
	
	public HudIndividual(CVRPProblem prob, ArrayList<Visit> exampleSol) {
		this(prob);
		this.genotype = new ArrayList<VRPVisit>();
		for (Visit v :exampleSol) {
			this.genotype.add((VRPVisit)v);
		}
	}
	
	public static int getEvals() {
		return evals;
	}
	
	public HudIndividual (CVRPProblem prob, Individual parent1, Individual parent2){
	    super(prob,parent1,parent2);
	}

		
	@Override
	public double evaluate() {
		/*
		 * Build a phenotype based upon the genotype
		 * Only build the genotyoe if the phenotype has been set to null
		 * Return the fitness (distance)
		 * 
		 * This method has been overriden to allow a custom fitness function
		 * 
		 * Add in code to ensure that route is not over the allowed time
		 * 
		 */
        double timeLimit = ((HudProblem)problem).getTimeLimitMS();
        double deliveryTime = ((HudProblem)problem).getDeliveryTimeMS();
       HudProblem fProblem = (HudProblem)problem;
       if (phenotype == null) {
			phenotype = new ArrayList<ArrayList<VRPVisit>> ();
			evals++;
			
			Visit depot = problem.getStart();
			Visit prev= depot;
			Visit curr = null;
			ArrayList<VRPVisit> newRoute = new ArrayList<VRPVisit>();
			double time = 0;
			boolean first = true;
			for (VRPVisit v : genotype){
					curr = v;
					if (first ) {
						
						time = (OSMAccessHelper.getJourney(prev, curr, fProblem.getMode()).getTravelTimeMS());
						first = false;
					}
					double timeToReturn = deliveryTime +  OSMAccessHelper.getJourney(curr,depot, fProblem.getMode()).getTravelTimeMS();
			
					if ((v.getDemand() + routeDemand(newRoute) > problem.getCapacity())|| ((time + timeToReturn)> timeLimit)){
						//If next visit cannot be added  due to capacity constraint then
						//start new route.
						phenotype.add(newRoute);
						newRoute = new ArrayList<VRPVisit>();
						time = 0;
						prev = depot;
					}
					time = time + OSMAccessHelper.getJourney(prev, curr, fProblem.getMode()).getTravelTimeMS();
					time = time + deliveryTime;
					newRoute.add(v);
				
			}
			prev = curr;
			phenotype.add(newRoute);
       }
       	//Get weighted distance
        double fit = problem.getDistance(phenotype);
     

		return fit;
	}
}
