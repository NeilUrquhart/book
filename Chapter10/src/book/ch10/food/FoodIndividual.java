package book.ch10.food;

import java.util.ArrayList;

import book.ch1.Visit;
import book.ch2.CVRPProblem;
import book.ch2.Individual;
import book.ch2.VRPVisit;

public class FoodIndividual extends Individual {
	private static int evals=0;
	private double mutNN = Double.parseDouble(FoodProperties.getInstance().get("mutnn"));
	private double mutInvert = Double.parseDouble(FoodProperties.getInstance().get("mutinvert"));
	private double totalTime =0;//The total time (msec) required to make the deliverires
	
	public FoodIndividual(CVRPProblem prob) {
		super(prob);
	}


	public FoodIndividual(CVRPProblem prob, ArrayList<Visit> exampleSol) {
		this(prob);
		this.genotype = new ArrayList<VRPVisit>();
		for (Visit v :exampleSol) {
			this.genotype.add((VRPVisit)v);
		}
	}

	public double getTimeMins() {
		return totalTime/60000;
	}
	public static int getEvals() {
		return evals;
	}
	
	public static void resetEvals() {
		evals=0;
	}

	public FoodIndividual (CVRPProblem prob, Individual parent1, Individual parent2){
		super(prob,parent1,parent2);
	}

	@Override
	public FoodIndividual copy() {
		//Create a new individual that is a direct copy of this individual
		FoodIndividual copy = new FoodIndividual(this.problem);
		copy.genotype = (ArrayList<VRPVisit>) this.genotype.clone();
		return copy;
	}

	@Override
	public void mutate() {
		float m = rnd.getRnd().nextFloat();
		if ( m <= mutNN)
			nnMutate();
		else if (m <= (mutNN+mutInvert))
			invert();
		else
			super.mutate();//rand

	}

	public void nnMutate() {
		//Nearest neighbour mutate
		int p1 = rnd.getRnd().nextInt(this.genotype.size());
		VRPVisit v = genotype.get(p1);
		VRPVisit n = findN(v);
		genotype.remove(n);
		genotype.add(p1,n);
	}

	private VRPVisit findN(VRPVisit v) {
		double dist = Double.MAX_VALUE;
		VRPVisit n = null;
		for (VRPVisit poss : genotype) {
			if (poss != v) {
				double d = FoodOSMHelper.getInstance().getJourney(v, poss, ((FoodProblem)this.problem).getMode()).getDistanceKM();
				if (d < dist) {
					dist = d;
					n = poss;
				}
			}
		}
		return n;
	}

	public void invert() {
		int p1 = rnd.getRnd().nextInt(this.genotype.size());
		int p2 = p1;
		while(p2==p1) {
			p2 = rnd.getRnd().nextInt(this.genotype.size());
		}
		//sort
		if (p2 < p1) {
			int t = p2;
			p2 = p1;
			p1 = t;
		}
		ArrayList<VRPVisit> temp = new ArrayList<VRPVisit>();
		for (int count = p1; count <= p2; count ++) {
			temp.add(genotype.remove(p1));
		}

		for (int count = p1; count <= p2; count ++) {
			genotype.add(p1,(temp.remove(0)));
		}


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
		double timeLimit = ((FoodProblem)problem).getTimeLimitMS();
		double deliveryTime = ((FoodProblem)problem).getDeliveryTimeMS();
		FoodProblem fProblem = (FoodProblem)problem;
		
		if (phenotype == null) {
			phenotype = new ArrayList<ArrayList<VRPVisit>> ();
			totalTime =0;
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

					time = (FoodOSMHelper.getInstance().getJourney(prev, curr, fProblem.getMode()).getTravelTimeMS());
					first = false;
				}
				double timeToReturn = deliveryTime +  FoodOSMHelper.getInstance().getJourney(curr,depot, fProblem.getMode()).getTravelTimeMS();

				if ((v.getDemand() + routeDemand(newRoute) > problem.getCapacity())|| ((time + timeToReturn)> timeLimit)){
					//If next visit cannot be added  due to capacity constraint then
					//start new route.
					phenotype.add(newRoute);
					newRoute = new ArrayList<VRPVisit>();
					totalTime = totalTime + time + timeToReturn;
					time = 0;
					prev = depot;
				}
				time = time + FoodOSMHelper.getInstance().getJourney(prev, curr, fProblem.getMode()).getTravelTimeMS();
				time = time + deliveryTime;
				newRoute.add(v);

			}
			prev = curr;
			phenotype.add(newRoute);
			totalTime = totalTime + time;
		}
		//Get distance

		return problem.getDistance(phenotype);
	}
}
