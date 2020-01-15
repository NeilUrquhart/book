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
public class tempNonDomEA extends VRPSolver {
	private RandomSingleton rnd = RandomSingleton.getInstance();
	//Note that we use the RandomSingleton object to generate random numbers

	//EA Parameters
	private int INIT_POP_SIZE = 1000;
	private int CHILDREN = 100;
	private double XO_RATE = 0.7;
	private int evalsBudget = 100000;

	private NonDominatedPop population;
	
	//population stores our pool of potential solutions
	
	@Override
	public void solve() {
		population = new NonDominatedPop (INIT_POP_SIZE, super.theProblem);
		evalsBudget = evalsBudget - INIT_POP_SIZE;//account for initial solutions in pop.
		
		System.out.println("Evals ="+evalsBudget +","+ population.extractNonDom().getStats());
		for (Domination d : population.extractNonDom()){
			BiObjectiveIndividual i = (BiObjectiveIndividual) d;
			System.out.println("v,"+i.getRoutes()+",cs,"+i.getCustService());
		}
		while(evalsBudget >0) {	
			for (int count =0; count < CHILDREN; count ++){
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

				//Domination rip = population.getRIP();//.getDominated();
				//population.remove(rip);
				population.add(child);
			}
			population = population.extractNonDom();
			if ((evalsBudget %10000)==0){
				System.out.println("Evals ="+evalsBudget +","+ population.extractNonDom().getStats());
				for (Domination d : population.extractNonDom()){
					BiObjectiveIndividual i = (BiObjectiveIndividual) d;
					System.out.println("v,"+i.getRoutes()+",cs,"+i.getCustService());
				}
			}
		}

		System.out.println("Final," + population.extractNonDom().getStats());
		
		for (Domination d : population.extractNonDom()){
			BiObjectiveIndividual i = (BiObjectiveIndividual) d;
			System.out.println("v,"+i.getRoutes()+",cs,"+i.getCustService());
		}
	}

	public ArrayList<Domination> getNonDom(){
		//Return the list of non-dominated solutions created by this EA
		return population.extractNonDom();
	}
	

}
