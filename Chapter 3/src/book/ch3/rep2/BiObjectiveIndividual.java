package book.ch3.rep2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


import book.ch1.Visit;
import book.ch2.CVRPProblem;
import book.ch2.RandomSingleton;
import book.ch2.VRPVisit;

/*
 * Neil Urquhart 2019
 * This class represents a single CVRP solution to be used within an Evolutionary Algorithm.
 * 
 * The basic solution (a grand tour) is stored in the genotype. Once the solution has been
 * evaluated then the solution is stored in the genotype.
 * 
 * The Genotype comprises a set of Gene objects (see private class below) which contain the details of the visit and the newVan flag.
 * 
 */
public class BiObjectiveIndividual  extends EAIndividual implements Domination  {
	
	private class Gene {
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
	public enum Objective{
		ROUTES,
		CUST_SERVICE,
		DISTANCE
	}
	
	private static Objective evalObjective;
	
	public static void setObjective(Objective o){
		evalObjective =o;
	}
	
	//Get Customer Service
		public double getCustService() {
			if (super.phenotype == null)
				//If the genotype has been changed then evaluate
				evaluate();
			
			//Get Dist to customers
			double totTime=0;
			for (ArrayList<VRPVisit> route : getPhenotype()){
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
		
		
	//Use the RandomSingleton object to get access to a seeded random number generator
	private RandomSingleton rnd =RandomSingleton.getInstance();

	//The genotype is a "grand tour" list of visits
	protected ArrayList<Gene> genotype;

	public BiObjectiveIndividual( CVRPProblem prob) {
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

	public BiObjectiveIndividual (CVRPProblem prob, BiObjectiveIndividual parent1, BiObjectiveIndividual parent2){
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
    
	private ArrayList randomize(ArrayList list) {
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

	private void decode() {
		/*
		 * Build a phenotype based upon the genotype
		 * Only build the genotype if the phenotype has been set to null
		 */
		if (phenotype == null) {
			phenotype = new ArrayList<ArrayList<VRPVisit>> ();
			ArrayList<VRPVisit> newRoute = new ArrayList<VRPVisit>();
			for (Gene g : genotype){
				VRPVisit v = g.visit();
				if (g.newRoute()){//Create a new route, if the gene specifies a new route
					phenotype.add(newRoute);
					newRoute = new ArrayList<VRPVisit>();
				}
				if (v.getDemand() + routeDemand(newRoute) > problem.getCapacity()){
					//If next visit cannot be added  due to capacity constraint then
					//start new route.
					phenotype.add(newRoute);
					newRoute = new ArrayList<VRPVisit>();
				}
				newRoute.add(v);
			}
			phenotype.add(newRoute);
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
		else if (evalObjective == Objective.CUST_SERVICE)
			return this.getCustService();
		else//dist
			return this.getDistance();
	}

	public ArrayList<ArrayList<VRPVisit>> getPhenotype(){
		return phenotype;
	}

	public double getDistance(){
		if (phenotype == null)
			//If the genotype has been changed then evaluate
			evaluate();
		return problem.getDistance(phenotype);
	}

	public int getRoutes() {
		if (phenotype == null)
			//If the genotype has been changed then evaluate
			evaluate();
		return phenotype.size();
	}

	private int routeDemand(ArrayList<VRPVisit> route){
		//Return the total cumulative demand within <route>
		int demand=0;
		for (VRPVisit visit: route){
			demand += visit.getDemand();
		}
		return demand;
	}

	public BiObjectiveIndividual copy() {
		//Create a new individual that is a direct copy of this individual
		BiObjectiveIndividual copy = new BiObjectiveIndividual(this.problem);
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
			for (ArrayList<VRPVisit> route: phenotype) {
				for (VRPVisit v : route) {
					res = res + v.toString()+":";
				}
				res = res +"\n";
			}
		}
		return res;
	}
	

	@Override
	public boolean dominates(Domination i) {
		//True if we dominate the <i>, else false
		BiObjectiveIndividual other = (BiObjectiveIndividual) i;
		if (this.getCustService() > other.getCustService())
			return false;
		if (this.getRoutes() > other.getRoutes())
			return false;
		
		if ((this.getRoutes() < other.getRoutes())||(this.getCustService() < other.getCustService()))
			return true;
		return false;
	}

	@Override
	public double[] getVector() {
		//Return a vector representation
		double[] v = new double[2];
		v[0] = this.getRoutes();
		v[1] = this.getCustService();
		return v;
	}
	
}
