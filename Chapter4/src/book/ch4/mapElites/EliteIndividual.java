package book.ch4.mapElites;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import book.ch1.Visit;
import book.ch2.CVRPProblem;
import book.ch2.VRPVisit;
import book.ch4.BiObjectiveTWIndividual;
import book.ch4.CVRPTWProblem;
import book.ch4.FabFoodCostModel;
import book.ch4.VRPTWRoute;
import book.ch4.VRPTWVisit;
import book.ch4.VisitNode;
import book.ch4.mapElites.ModalCostModel.Mode;
import book.ch4.BiObjectiveTWIndividual.Gene;


public class EliteIndividual extends BiObjectiveTWIndividual implements Elite {
	/*
	 * Neil Urquhart 2021
	 * An extension of BiObjectiveTWIndividual with 2 enhancements
	 * 1. Implements book.ch4.mapElites.Elite to allow use with book.ch4.mapElitesMAPElites
	 * 2. Uses the inner class ModeGene as the basis
	 * 
	 */

	public  class ModeGene extends Gene{
		private Mode theMode;
		
		public ModeGene(VRPVisit aVisit) {
			super(aVisit);

		}
		
		public void setMode(Mode m) {
			theMode = m;
		}
		
		public Mode getMode() {
			return theMode;
		}
		
		public void flipMode() {
			if (theMode == Mode.CYCLE) theMode = Mode.VAN;
			if (theMode == Mode.VAN) theMode = Mode.CYCLE;
		}
	}
	
	public EliteIndividual(CVRPProblem prob) {
		super(prob);
	}
	
	public EliteIndividual(CVRPProblem prob,EliteIndividual a, EliteIndividual b) {
		super(prob,a,b);
	}
	
	public EliteIndividual(CVRPProblem prob, boolean init) {
		super(prob, init);
	}

	@Override
	public double getFitness() {
		return super.getDistance();
	}

	@Override
	public int[] getKey() {
		return KeyGenerator.getInstance().getKey(this);
	}
	
	public EliteIndividual copy() {
			//Create a new individual that is a direct copy of this individual
			EliteIndividual copy = new EliteIndividual(this.problem);
			copy.genotype = (ArrayList<Gene>) this.genotype.clone();
			return copy;
	
	}
	
	@Override
	public double getCost() {
		return ModalCostModel.getInstance().getSolutionCost(this);
	}
	
	
	@Override
	public double getCostDel() {
		return ModalCostModel.getInstance().getSolutionCost(this)/((CVRPTWProblem)this.problem).getTotalDemand();
	}
	
	
	
	
	
	
	
	@Override
	protected void createRandom(CVRPProblem prob, boolean all1s, boolean all0s) {
		//Set the genotype randomly.
		//The all1s and all0s are used to specify if newVan bits should be set of all1s (T,F) all0s (F,T) or randomly (F,F)
		problem = prob;
		genotype = new ArrayList<Gene>();
		for (Visit v : prob.getSolution()){
			ModeGene g = new ModeGene((VRPVisit)v);
			if (all1s == true)
				g.setNewRoute(true);
			else if (all0s == true)
				g.setNewRoute(false);
			else
			  g.setNewRoute(rnd.getRnd().nextBoolean());
			
			g.setMode(Mode.VAN);
			if (rnd.getRnd().nextBoolean())
				g.setMode(Mode.CYCLE);
			genotype.add(g);
		}
		genotype = randomize(genotype);
		phenotype = null;
	}
	
	@Override
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
			ModeGene g = (ModeGene)genotype.get(rndGene);
			if (rnd.getRnd().nextBoolean())
				g.flipNewRoute();	
			else
				g.flipMode();
		}else{
			boolean newRoute = rnd.getRnd().nextBoolean();
			if (rnd.getRnd().nextBoolean())
				for (Gene g : genotype)
					g.setNewRoute(newRoute);	
			else {
				Mode m = Mode.VAN;
				if (rnd.getRnd().nextBoolean())
					m = Mode.CYCLE;
			
				for (Gene t : genotype) {
						ModeGene g = (ModeGene)t;
						g.setMode(m);
				}
			}
				
		}	
	}
	
	@Override
	protected void decode() {
		if (phenotype == null) {
			
			Mode currentMode = ((ModeGene)genotype.get(0)).getMode();
			
			phenotype = new ArrayList<VRPTWRoute> ();	
			VRPTWModalRoute newRoute = new VRPTWModalRoute(((CVRPTWProblem)this.problem));
			LocalTime currentTime = ((CVRPTWProblem)this.problem).getStartTime();
			
			Visit previous =  this.problem.getStart();
			boolean addNewRoute = false;
			ReloadNode reload = null;
			for (Gene tmp : genotype){
				ModeGene g = (ModeGene)tmp;
				reload = null;

				VisitNode v = new VisitNode((VRPTWVisit)g.visit());
				int travelTime = ((CVRPTWModalProblem)this.problem).getTravelMinutes(previous,v.getVisit(),currentMode);
				LocalTime proposedTime = currentTime.plusMinutes(travelTime);
			
				if (v.getVisit().getDemand() +newRoute.demand() > ModalCostModel.getInstance().getCapacity(currentMode)){
					//If next visit cannot be added  due to capacity constraint then factor in a return to base and re-load
					travelTime = ((CVRPTWModalProblem)this.problem).getTravelMinutes(previous,this.problem.getStart(),currentMode);
					travelTime = travelTime+ModalCostModel.getInstance().getTurnaround(currentMode);
					travelTime = travelTime+ ((CVRPTWModalProblem)this.problem).getTravelMinutes(this.problem.getStart(),v.getVisit(),currentMode);
					proposedTime = currentTime.plusMinutes(travelTime);	
					reload = new ReloadNode();
				}
				
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
					reload = null;
				}
				
				if ((g.newRoute())){//Create a new route, if the gene specifies a new route
						addNewRoute = true;
						//currentMode = g.getMode();
						reload = null;
				}

				if (reload != null) {
					if (newRoute.size()>0)
					newRoute.add(reload);
				}

				if (addNewRoute) {
					/*
					 * Add a new route to the phenotype
					 * 
					 */
		    		newRoute.setMode(currentMode);
					phenotype.add(newRoute);
					newRoute.setStartEndTimes();
					
					newRoute = new VRPTWModalRoute(((CVRPTWProblem)this.problem));
					currentTime = ((CVRPTWProblem)this.problem).getStartTime();
			
					proposedTime = currentTime.plusMinutes(((CVRPTWProblem)this.problem).getTravelMinutes(this.problem.getStart(),(Visit)v.getVisit()));
					
					if (proposedTime.compareTo(v.getVisit().getEarliest())<0)
						proposedTime = v.getVisit().getEarliest();
				
					currentTime = proposedTime;
					previous = this.problem.getStart();
					addNewRoute = false;
					currentMode = g.getMode();
				}
				v.setDeliveryTime(currentTime);
				newRoute.add(v);
				currentTime = currentTime.plusMinutes(((CVRPTWProblem)problem).getDeliveryTime());
				previous = v.getVisit();
	//			firstGene = false;
			}
			newRoute.setMode(currentMode);
			phenotype.add(newRoute);
			newRoute.setStartEndTimes();

		}
	}
	

}
