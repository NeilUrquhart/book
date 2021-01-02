package book.ch4;


import java.util.ArrayList;

import book.ch2.RandomSingleton;
import book.ch2.VRPSolver;
import book.ch3.rep2.Domination;


/*
 * Neil Urquhart 2019
 * A simple Evolutionary Algorithm to solve the CVRP problem
 * 
 * Use Individual.check() to ensure that Individuals contain valid solutions. Advisable to turn this on when testing
 * modificatins, and comment it out when testing is completed
 */
public class NonDomEA extends VRPSolver {
	private RandomSingleton rnd = RandomSingleton.getInstance();
	//Note that we use the RandomSingleton object to generate random numbers

	//EA Parameters
	private int INIT_POP_SIZE = 1000;
	private double XO_RATE = 0.7;
	private int CHILDREN = 100;
	private int evalsBudget = 100000;

	private NonDominatedPop population;
	
	//population stores our pool of potential solutions
	
	@Override
	public void solve() {
		/*
		 * Create new pop
		 * 
		 */
		ArrayList<Domination> init = new ArrayList<Domination>();
		for (int count=0; count < INIT_POP_SIZE; count++){
			BiObjectiveTWIndividual i = new BiObjectiveTWIndividual(theProblem);
			i.evaluate();
			init.add(i);
		}
		population = new NonDominatedPop (init);
		evalsBudget = evalsBudget - INIT_POP_SIZE;//account for initial solutions in pop.
		while(evalsBudget >0) {	
			for (int count =0; count < CHILDREN; count ++){
				//Create child
				BiObjectiveTWIndividual child = null;
				if (rnd.getRnd().nextDouble() < XO_RATE){
					//Create a new Individual using recombination, randomly selecting the parents
					child = new BiObjectiveTWIndividual(super.theProblem, (BiObjectiveTWIndividual) population.getDominator(),(BiObjectiveTWIndividual) population.getDominator());				
				}
				else{
					//Create a child by copying a single parent
					child =  ((BiObjectiveTWIndividual) population.getDominator()).copy();
				}
				child.mutate();
				child.evaluate();
				evalsBudget --;

				population.add(child);
			}
			
			population = population.extractNonDom();	
			
		}
	}

	public ArrayList<Domination> getNonDom(){
		//Return the list of non-dominated solutions created by this EA
		return population.extractNonDom();
	}
	

}
