package book.ch5;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


import book.ch1.Visit;
import book.ch2.CVRPProblem;
import book.ch2.RandomSingleton;
import book.ch2.VRPVisit;
import book.ch3.rep2.Domination;
import book.ch3.rep2.EAIndividual;

/*
 * Neil Urquhart 2020
 * This class represents a single CVRPTW solution to be used within an Evolutionary Algorithm.
 * 
 * The basic solution (a grand tour) is stored in the genotype. Once the solution has been
 * evaluated then the solution is stored in the genotype.
 * 
 * The Genotype comprises a set of Gene objects (see private class below) which contain the details of the visit and the newVan flag.
 * 
 */
public class MObjTWIndividual  extends EAIndividual implements Domination  {
	//Use the RandomSingleton object to get access to a seeded random number generator
	protected RandomSingleton rnd =RandomSingleton.getInstance();
	
	protected ArrayList<VRPTWRoute> phenotype;
	
	public enum Objective{
		ROUTES, 	//No of routes in solution
		TIME,   	//Total time in solution
		DISTANCE, 	//Total distance in solution
		IDLE,		//Amount of time vehicle spend idle in solution
		COST_DEL	//The cost per delivery
	}
	//The genotype is a "grand tour" list of visits
	protected ArrayList<Gene> genotype;

	public class Gene {
		/*
		 * Represents a single Gene, 
		 *   visit - the visit being made
		 *   newRoute - true if this visit should be added to the start of a new route
		 */
		private boolean newRoute = false;
		private VRPVisit visit = null;

		public Gene(VRPVisit aVisit){
			this.visit = aVisit;
			this.newRoute = false;
		}
		
		public boolean newRoute(){
			return this.newRoute;
		}
		
		public VRPVisit visit(){
			return this.visit;
		}
		
		public void setNewRoute(boolean val){
			this.newRoute = val;
		}
		
		public void flipNewRoute(){
			this.newRoute = ! this.newRoute;
		}
		
		public String toString(){
			return this.visit.toString() + "New Route "+ this.newRoute;
		}
	}
	
	public MObjTWIndividual( CVRPProblem prob) {
		super();
		/*
		 * Constructor to create a new random genotype
		 */
		boolean all1s = false;
		boolean all0s = false;
		
		//Create some with all newVans set to 1 and some with all set to 0
		if (rnd.getRnd().nextFloat() > 0.8){
			if (rnd.getRnd().nextBoolean()){
			   all1s = true;	
			}else{
				all0s = true;
			}
		}
		
		createRandom(prob, all1s, all0s);
	}
	
	public MObjTWIndividual( CVRPProblem prob, boolean vanBits) {
		super();
		
		if (vanBits == false)
			createRandom(prob, false, true);
		else
			createRandom(prob, true, false);
	}

	protected void createRandom(CVRPProblem prob, boolean all1s, boolean all0s) {
		//Set the genotype randomly.
		//The all1s and all0s are used to specify if newVan bits should be set of all1s (T,F) all0s (F,T) or randomly (F,F)
		problem = prob;
		genotype = new ArrayList<Gene>();
		for (Visit v : prob.getSolution()){
			Gene g = new Gene((VRPVisit)v);
			if (all1s == true)
				g.setNewRoute(true);
			else if (all0s == true)
				g.setNewRoute(false);
			else
			  g.setNewRoute(rnd.getRnd().nextBoolean());
			genotype.add(g);
		}
		genotype = randomize(genotype);
		phenotype = null;
	}
	

	
	private static Objective evalObjective;
	
	public static void setObjective(Objective o){
		evalObjective =o;
	}
			
	public MObjTWIndividual (CVRPProblem prob, MObjTWIndividual parent1, MObjTWIndividual parent2){
		super();
		/*
		 * Create a new Individual based on the recombination of genes from <parent1> and <parent2>
		 */
		super.problem = prob;
		genotype = new ArrayList<Gene>();
		int xPoint = rnd.getRnd().nextInt(parent1.genotype.size());
		
		//copy all of p1 to the xover point
		for (int count =0; count < xPoint; count++ ){
			genotype.add(parent1.genotype.get(count));
		}
		
		//Now add missing genes from p2
		for (int count =0; count < parent2.genotype.size(); count++){
			Gene g = parent2.genotype.get(count);
			if (!genotypeContains(genotype,g)){
				genotype.add(g);
			}
		}
	}

    private boolean genotypeContains(ArrayList<Gene> genome, Gene search){
    	//return True if <genome> contains the gene <search> - based on visit
    	for (Gene g: genome){
    		if (g.visit == search.visit)
    			return true;
    	}
    	return false;
    }
    
	public double getTime() {
		if (phenotype == null)
			//If the genotype has been changed then evaluate
			evaluate();
		double time=0;

		for (VRPTWRoute r : phenotype)
			time += r.getTime();
		return time;
	}
    
	protected ArrayList randomize(ArrayList list) {
		// Randomly shuffle the contents of <list>
		Random  r= rnd.getInstance().getRnd();

		for (int c=0; c < list.size();c++) {
			Object o = list.remove(r.nextInt(list.size()));
			list.add(r.nextInt(list.size()),o);
		}
		return list;
	}
	
	public void mutate() {
		//Mutate the genotype, by randomly moving a gene. or flipping new van
		//With a very low probability set all of the newRoutes to all 1s or  all 0s
		phenotype = null;
		double choice = rnd.getRnd().nextDouble();
		if (choice < 0.45){
			int rndGene = rnd.getRnd().nextInt(genotype.size());
			Gene g = genotype.remove(rndGene);
			int addPoint = rnd.getRnd().nextInt(genotype.size());
			genotype.add(addPoint,g);
		}else if ( choice < 0.999){
			int rndGene = rnd.getRnd().nextInt(genotype.size());
			Gene g = genotype.get(rndGene);
			g.flipNewRoute();	
		}else{
			boolean newRoute = rnd.getRnd().nextBoolean();
			for (Gene g : genotype)
				g.setNewRoute(newRoute);	
		}
	}

	public void reset() {
		phenotype =null;
	}
	protected void decode() {
		/*Build a phenotype based upon the genotype
		 * The solution must respect the delivery time windows */
		 
		if (phenotype == null) {
			phenotype = new ArrayList<VRPTWRoute> ();
			VRPTWRoute newRoute = new VRPTWRoute(((VRPTWProblem)this.problem));
			LocalTime currentTime = ((VRPTWProblem)this.problem).getStartTime();
			Visit previous =  this.problem.getStart();
			boolean addNewRoute = false;
			boolean firstGene = true;
			for (Gene g : genotype){
				VisitNode v = new VisitNode((VRPTWVisit)g.visit());
				int travelTime = ((VRPTWProblem)this.problem).getTravelMinutes(previous,v.getVisit());
				LocalTime proposedTime = currentTime.plusMinutes(travelTime);
			
				//If arrival is before TW
				if (proposedTime.compareTo(v.getVisit().getEarliest())<0) {
					//wait at visit for tw to start
					v.setMinsWaiting((int)ChronoUnit.MINUTES.between(proposedTime,v.getVisit().getEarliest()));
					proposedTime = v.getVisit().getEarliest();
					currentTime = proposedTime;
				}
				else if (v.getVisit().inTimeWindow(proposedTime)) {
					//make current time is >= tw start
					currentTime = proposedTime;
				}
				else //Proposed time is beyond the time window
				{
					addNewRoute = true;
				}

				if ((g.newRoute() && !firstGene)){//Create a new route, if the gene specifies a new route
					addNewRoute = true;
				}
				if (v.getVisit().getDemand() +newRoute.demand() > problem.getCapacity()){
					//If next visit cannot be added  due to capacity constraint then
					//start new route.
					addNewRoute = true;
				}
				if (addNewRoute) {
					//Add a new route to the phenotype
					phenotype.add(newRoute);
					newRoute.setStartEndTimes();
					newRoute = new VRPTWRoute(((VRPTWProblem)this.problem));
					currentTime = ((VRPTWProblem)this.problem).getStartTime();
					proposedTime = currentTime.plusMinutes(((VRPTWProblem)this.problem).getTravelMinutes(this.problem.getStart(),(Visit)v.getVisit()));
					
					if (proposedTime.compareTo(v.getVisit().getEarliest())<0)
						proposedTime = v.getVisit().getEarliest();
				
					currentTime = proposedTime;
					previous = this.problem.getStart();
					addNewRoute = false;
				}
				v.setDeliveryTime(currentTime);
				newRoute.add(v);
				currentTime = currentTime.plusMinutes(((VRPTWProblem)problem).getDeliveryTime());
				previous = v.getVisit();
				firstGene = false;
			}
			phenotype.add(newRoute);
			newRoute.setStartEndTimes();
		}
	}
	

	public double evaluate(){
		//Used when not creating a non-domainated front.
		//Evaluate based on the value of evalObjective
		
		if (phenotype == null)  
			decode();
		//build solution
		if (evalObjective == Objective.ROUTES)
			return this.getRoutes();
		else if (evalObjective == Objective.TIME)
			return this.getTime();
		else if (evalObjective == Objective.IDLE)
			return this.getIdle();
		else if (evalObjective == Objective.COST_DEL)
			return (getCost()/((VRPTWProblem)this.problem).getTotalDemand());
		else//dist
			return this.getDistance();
	}


	public ArrayList<VRPTWRoute> getPhenotype(){
		return phenotype;
	}

	public double getCost() {
		return FabFoodCostModel.getInstance().getSolutionCost(this);
	}
	
	public double getCostDel() {
		return FabFoodCostModel.getInstance().getSolutionCost(this)/((VRPTWProblem)this.problem).getTotalDemand();
	}
	
	public double getDistance(){
		if (phenotype == null)
			//If the genotype has been changed then evaluate
			evaluate();
		double dist =0;
		
		for (VRPTWRoute  r : phenotype)
		  dist += r.getDist();
		
		return  dist;
			
	}

	public double getIdle(){
		if (phenotype == null)
			//If the genotype has been changed then evaluate
			evaluate();
		double idle =0;
		
		for (VRPTWRoute  r : phenotype)
		  idle += r.getIdle();
		
		return  idle;
			
	}
	public int getRoutes() {
		if (phenotype == null)
			//If the genotype has been changed then evaluate
			evaluate();
		return phenotype.size();
	}



	public MObjTWIndividual copy() {
		//Create a new individual that is a direct copy of this individual
		MObjTWIndividual copy = new MObjTWIndividual(this.problem);
		copy.genotype = (ArrayList<Gene>) this.genotype.clone();
		return copy;
	}
	
	public String toString() {
		String res="";
		for (Gene g : genotype) {
			res = res + g.toString()+":";
		}
		res = res +"\n";
		
		if (phenotype != null) {
			res = res+"Phenotype\n";
			for (VRPTWRoute route: phenotype) {
				res = res + route.toString();
			}
		}
		return res;
	}
	

	@Override
	public boolean dominates(Domination i) {
		double[] me = this.getVector();
		double[] other = i.getVector();
		
		//Test to see if I am worse in any vector
		if(me[0] > other[0])
			return false;
		if(me[1] > other[1])
			return false;
		//Must be the same or better
		
		if(me[2] > other[2])
			return false;
		//Must be the same or better
		
		if ((me[0]==other[0])&&(me[1]==other[1])&&(me[2]==other[2]))
			return false;
		
		return true;
	}

	@Override
	public double[] getVector() {
		//Return a vector representation
		double[] v = new double[4];
		v[0] = this.getDistance();
		v[1] = this.getTime();
		v[2] = (this.getCost()/((VRPTWProblem)this.problem).getTotalDemand());
		v[3] = this.getRoutes();
		return v;
	}
}
