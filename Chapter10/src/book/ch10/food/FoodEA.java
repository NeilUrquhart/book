package book.ch10.food;
import book.ch2.Individual;
import book.ch2.VRPea;

public class FoodEA extends VRPea {

	private long runTime;

	public FoodEA(long runTime) {
		this.runTime = runTime;
		this.XO_RATE = Double.parseDouble(FoodProperties.getInstance().get("xo_rate"));	
		this.POP_SIZE = Integer.parseInt(FoodProperties.getInstance().get("population_size"));	
		this.TOUR_SIZE = Integer.parseInt(FoodProperties.getInstance().get("tour_size"));	
	}
	@Override
	protected FoodIndividual InitialisePopution() {
		System.out.println("Setting up problem");
		//Initialise population with random solutions
		FoodIndividual best = null;
		population.clear();
		for (int count=0; count < POP_SIZE; count++){
			if((count%10)==0)
				System.out.println(count);

			FoodIndividual i;
			i = new FoodIndividual(super.theProblem);//Random

			if (best == null) 
				best = i;
			if (i.evaluate() < best.evaluate()) 
				best = i;

			population.add(i);
		}
		return best;
	}

	@Override
	public void solve() {
		FoodOSMHelper.getInstance().inittCache(FoodVisit.getIndexCounter());
		//Reference to the best individual in the population

		FoodIndividual bestEver = null;
		
		for (int run=0; run < Integer.parseInt(FoodProperties.getInstance().get("repeat")) ;run++) {
			long start = System.currentTimeMillis();
			FoodIndividual.resetEvals();
			FoodIndividual bestSoFar = InitialisePopution();
			while((System.currentTimeMillis() - start)<this.runTime) {	
				//Create child
				FoodIndividual child = null;
				if (rnd.getRnd().nextDouble() < XO_RATE){
					//Create a new Individual using recombination, randomly selecting the parents
					child = new FoodIndividual(super.theProblem, tournamentSelection(TOUR_SIZE),tournamentSelection(TOUR_SIZE));				
				}
				else{
					//Create a child by copying a single parent
					child = (FoodIndividual) tournamentSelection(TOUR_SIZE).copy();
				}
				child.mutate();
				child.evaluate();

				//Select an Individual with a poor fitness to be replaced
				Individual poor = tournamentSelectWorst(TOUR_SIZE);
				if (poor.evaluate() > child.evaluate()){
					//Only replace if the child is an improvement

					if (child.evaluate() < bestSoFar.evaluate()){
						bestSoFar = child;
					}
					population.remove(poor);
					population.add(child);
				}
				if ((FoodIndividual.getEvals() %20000)==0) {
					System.out.print("Left " +	(this.runTime-(System.currentTimeMillis() - start)) +"\t");
					System.out.println("Fitness "+bestSoFar.evaluate() +"\tRounds "+bestSoFar.getVehicles()+"\tEvals" +FoodIndividual.getEvals() ) ;
				}
			}
			if (bestEver ==  null)
				bestEver = bestSoFar;
			else if (bestSoFar.evaluate() < bestEver.evaluate())
				bestEver = bestSoFar;
		}

		super.theProblem.setSolution(bestEver.getPhenotype());
		System.out.println("Finished! \nThanks for waiting.");
		System.out.println("Fitness = " + bestEver.evaluate() + " Dist = " + bestEver.getDistance() + " Routes = " + bestEver.getVehicles() + " Time = "+ bestEver.getTimeMins());
	}

}
