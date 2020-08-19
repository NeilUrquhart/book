package book.ch4;

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
 * This class represents a single CVRP solution to be used within an Evolutionary Algorithm.
 * 
 * The basic solution (a grand tour) is stored in the genotype. Once the solution has been
 * evaluated then the solution is stored in the genotype.
 * 
 * The Genotype comprises a set of Gene objects (see private class below) which contain the details of the visit and the newVan flag.
 * 
 */
public class BiObjectiveTWIndividual  extends EAIndividual implements Domination  {
	private int deliveryTime = 5;//5 mins
	
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
		TIME,
		DISTANCE,
		IDLE
	}
	
	public class VisitNode{
		private LocalTime visitTime=null;
		private int minsWaiting;
		private VRPTWVisit myVisit;
		
		public VisitNode(VRPTWVisit v) {
			myVisit = v;
		}
		
		public VRPTWVisit getVisit() {
			return myVisit;
			
		}
		public void setDeliveryTime(LocalTime t) {
			visitTime = t;
		}
		
		public LocalTime getDeliveryTime() {
			return visitTime;
		}
	
	
		public int getMinsWaiting() {
			return minsWaiting;
		}
	
		public void setMinsWaiting(int minsWaiting) {
			this.minsWaiting = minsWaiting;
		}
		
		public String toString() {
			return this.myVisit + " T=" + this.getDeliveryTime() + " w="+ this.getMinsWaiting();
		}
	}
	
	public class Route extends ArrayList<VisitNode>{
		private LocalTime start;
		private LocalTime end;
		
		public Route() {
			super();
		}
		
		public double getIdle() {
			double idle=0;
			for (VisitNode vn : this) {
				idle += vn.minsWaiting;
			}
			return idle;
		}
		public double getDist() {
			double dist=0;
			Visit p =  problem.getStart();
			for (VisitNode vn : this) {
				dist += problem.getDistance(p, vn.myVisit);
				p = vn.myVisit;
			}
			dist += dist += problem.getDistance(p, problem.getStart()); 
			return dist;
		}
		public String toString() {
			String buffer = start.toString() +",";
			
			for (VisitNode v : this)
				buffer += v.toString() +",";
			
			buffer += end.toString() +"\n";
			return buffer;
		}
		private int demand(){
			//Return the total cumulative demand within <route>
			int demand=0;
			for (VisitNode visit: this){
				demand += visit.getVisit().getDemand();
			}
			return demand;
		}
		
		private void setStartEnd() {
			
			if (this.size()<1)return;
			//set start and end times
			VisitNode v = this.get(0);
			int mins = ((CVRPTWProblem)problem).getTravelMinutes(problem.getStart(), v.getVisit());
			v.setMinsWaiting(0);
			this.start = v.getDeliveryTime().minusMinutes(mins);
			
			v = this.get(this.size()-1);
			mins = ((CVRPTWProblem)problem).getTravelMinutes(problem.getStart(), v.getVisit());
			mins = mins + deliveryTime;
			this.end = v.getDeliveryTime().plusMinutes(mins);
			
			
		}
		
		public long getTime() {//Route timespan in mins
			if ((start==null)||(end==null)) 
				return 0;
				
			//Check for routes that end after midnight
			long m=0;
				
			m =  ChronoUnit.MINUTES.between(start, end);
			
			if (m<0) {//Route ends after midnight
				m =  ChronoUnit.MINUTES.between(start, LocalTime.of(23,59,00));
				m ++;// missing minute at midnight
				m += ChronoUnit.MINUTES.between(LocalTime.of(00,00,00),end);
				
			}
			return m;
		}
		
		public LocalTime getStart() {
			return this.start;
		}
		
		public LocalTime getEnd() {
			return this.end;
		}
			
	}
	
	private ArrayList<Route> phenotype;

	private static Objective evalObjective;
	
	public static void setObjective(Objective o){
		evalObjective =o;
	}
	
//	//Get Customer Service
//		public double getCustService() {
//			if (super.phenotype == null)
//				//If the genotype has been changed then evaluate
//				evaluate();
//			
//			//Get Dist to customers
//			double totTime=0;
//			for (ArrayList<VRPVisit> route : getPhenotype()){
//			  double routeTime=0;
//			  Visit prev = super.problem.getStart();
//			  for (VRPVisit v : route){
//				  double dist = super.problem.getDistance(prev,v);
//				  routeTime = routeTime + dist;
//				  totTime = totTime + routeTime;
//				  
//			  }
//			}
//			return totTime;
//		}
		
	public double getTime() {
		if (phenotype == null)
			//If the genotype has been changed then evaluate
			evaluate();
		double time=0;

		for (Route r : phenotype)
			time += r.getTime();
		return time;

	}

	//Use the RandomSingleton object to get access to a seeded random number generator
	private RandomSingleton rnd =RandomSingleton.getInstance();

	//The genotype is a "grand tour" list of visits
	protected ArrayList<Gene> genotype;

	public BiObjectiveTWIndividual( CVRPProblem prob) {
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

	public BiObjectiveTWIndividual (CVRPProblem prob, BiObjectiveTWIndividual parent1, BiObjectiveTWIndividual parent2){
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

	public void reset() {
		phenotype =null;
	}
	private void decode() {
		/*
		 * Build a phenotype based upon the genotype
		 * Only build the genotype if the phenotype has been set to null
		 * 
		 * NEW: RESPECT TIME WINDOWS!
		 * 
		 * todo: add start/end for each route
		 */
		if (phenotype == null) {
			LocalTime start = LocalTime.of(7,30,0); //Move to problem
			
			phenotype = new ArrayList<Route> ();
			Route newRoute = new Route();
			LocalTime currentTime = start;
			Visit previous =  this.problem.getStart();
			boolean addNewRoute = false;
			boolean firstGene = true;
			for (Gene g : genotype){
				VisitNode v = new VisitNode((VRPTWVisit)g.visit());
				int travelTime = ((CVRPTWProblem)this.problem).getTravelMinutes(previous,v.getVisit());
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
				else
					//new route	
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
					
					phenotype.add(newRoute);
					newRoute.setStartEnd();
					newRoute = new Route();
					currentTime = start;
					proposedTime = currentTime.plusMinutes(((CVRPTWProblem)this.problem).getTravelMinutes(this.problem.getStart(),(Visit)v.getVisit()));
					
					if (proposedTime.compareTo(v.getVisit().getEarliest())<0)
						proposedTime = v.getVisit().getEarliest();
				
					currentTime = proposedTime;
					previous = this.problem.getStart();
					addNewRoute = false;

				}
						
				v.setDeliveryTime(currentTime);
				//temp
//				if(newRoute.size()>0) {
//				  VRPTWVisit old = newRoute.get(newRoute.size()-1);
//				  if (old.getDeliveryTime().compareTo(currentTime)>=0)
//					  System.out.println("err");
//				}
				//done temp
				newRoute.add(v);
				currentTime = currentTime.plusMinutes(deliveryTime);
				previous = v.getVisit();
				firstGene = false;

			}
			phenotype.add(newRoute);
			newRoute.setStartEnd();
			
			//TEMP - check for empty routes
			for (Route r : phenotype) {
				if (r.size()==0)
					System.out.println("!");
			}
			//doen temp
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
		else//dist
			return this.getDistance();
	}

	public ArrayList<Route> getPhenotype(){
		return phenotype;
	}

	public double getDistance(){
		if (phenotype == null)
			//If the genotype has been changed then evaluate
			evaluate();
		double dist =0;
		
		for (Route  r : phenotype)
		  dist += r.getDist();
		
		return  dist;
			
	}

	public double getIdle(){
		if (phenotype == null)
			//If the genotype has been changed then evaluate
			evaluate();
		double idle =0;
		
		for (Route  r : phenotype)
		  idle += r.getIdle();
		
		return  idle;
			
	}
	public int getRoutes() {
		if (phenotype == null)
			//If the genotype has been changed then evaluate
			evaluate();
		return phenotype.size();
	}

	

	public BiObjectiveTWIndividual copy() {
		//Create a new individual that is a direct copy of this individual
		BiObjectiveTWIndividual copy = new BiObjectiveTWIndividual(this.problem);
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
			for (Route route: phenotype) {
				res = res + route.toString();
				//for (VRPVisit v : route) {
				//	res = res + v.toString()+":";
				//}
				//res = res +"\n";
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
		
		if ((me[0]==other[0])&&(me[1]==other[1]))
			return false;
		
		return true;
	}


	
	@Override
	public double[] getVector() {
		//Return a vector representation
		double[] v = new double[2];
		v[0] = this.getRoutes();
		v[1] = this.getTime();
		return v;
	}
	
}
