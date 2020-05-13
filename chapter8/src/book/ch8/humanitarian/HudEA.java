package book.ch8.humanitarian;

import book.ch2.Individual;
import book.ch2.VRPea;

public class HudEA extends VRPea {
	private static int evalsChange = 20000;//default
	@Override
	protected Individual InitialisePopution() {
		System.out.println("Setting up problem");
		//Initialise population with random solutions
		Individual best = null;
		for (int count=0; count < POP_SIZE; count++){
			if((count%10)==0)
			  System.out.println(count);
			Individual i = new HudIndividual(super.theProblem);
			
			//i.check();//Check individual contains a valid solution
			if (best == null) 
				best = i;
			if (i.evaluate() < best.evaluate()) 
				best = i;
			population.add(i);
			evalsBudget--;
		}
		return best;
	}
	
	@Override
	public void solve() {
		int factor =(int) (super.theProblem.getSize()/10);
		evalsChange = evalsChange +(factor*5000);
		System.out.println("Time out = "+ evalsChange);
		int timeOut = evalsChange;
		
		//Reference to the best individual in the population
		Individual bestSoFar = InitialisePopution();
		while(timeOut >0) {	
			//Create child
			Individual child = null;
			if (rnd.getRnd().nextDouble() < XO_RATE){
				//Create a new Individual using recombination, randomly selecting the parents
				child = new HudIndividual(super.theProblem, tournamentSelection(TOUR_SIZE),tournamentSelection(TOUR_SIZE));				
			}
			else{
				//Create a child by copying a single parent
				child = tournamentSelection(TOUR_SIZE).copy();
			}
			child.mutate();
			child.evaluate();
			timeOut --;
			
			//Select an Individual with a poor fitness to be replaced
			Individual poor = tournamentSelectWorst(TOUR_SIZE);
			if (poor.evaluate() > child.evaluate()){
				//Only replace if the child is an improvement
				
				if (child.evaluate() < bestSoFar.evaluate()){
					bestSoFar = child;
					timeOut =evalsChange;
					System.out.println("New solution found "+bestSoFar.getVehicles() +":"+bestSoFar.getDistance()+":"+ bestSoFar.evaluate());
				}
				//child.check();//Check child contains a valid solution
				population.remove(poor);
				population.add(child);
			}
			if ((timeOut %1000)==0)
			  System.out.println("... "+(timeOut/1000));
		
			
		}
		

		super.theProblem.setSolution(bestSoFar.getPhenotype());
		System.out.println("Finished! \nThanks for waiting.");
	}

}
