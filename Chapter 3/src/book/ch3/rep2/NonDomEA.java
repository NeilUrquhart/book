package book.ch3.rep2;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import book.ch1.Visit;
import book.ch2.Individual;
import book.ch2.RandomSingleton;
import book.ch2.VRPSolver;

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
	private int POP_SIZE = 1000;
	private double XO_RATE = 0.7;
	private int evalsBudget = 100000;

	private NonDominatedPop population = new NonDominatedPop (POP_SIZE, super.theProblem);
	
	//population stores our pool of potential solutions
	
	@Override
	public void solve() {
		evalsBudget = evalsBudget - POP_SIZE;//account for initial solutions in pop.
		
		while(evalsBudget >0) {	

			//Create child
			BiObjectiveIndividual child = null;
			if (rnd.getRnd().nextDouble() < XO_RATE){
				//Create a new Individual using recombination, randomly selecting the parents
				child = new BiObjectiveIndividual(super.theProblem, (BiObjectiveIndividual) population.getDominator(),(BiObjectiveIndividual) population.getDominator());				
			}
			else{
				//Create a child by copying a single parent
				child =  ((BiObjectiveIndividual) population.getDominator()).copy();
			}
			child.mutate();
			child.evaluate();
			evalsBudget --;

			//Select a dominated Individual to be replaced
			population.remove(population.getDominated());
			population.add(child);
		}

		System.out.println();
		
		for (Domination d : population.extractNonDom()){
			BiObjectiveIndividual i = (BiObjectiveIndividual) d;
			System.out.println("v,"+i.getRoutes()+",cs,"+i.getCustService()+",d="+i.getDistance());
		}
	}

	public ArrayList<Domination> getNonDom(){
		//Return the list of non-dominated solutions created by this EA
		return population.extractNonDom();
	}
	

}
