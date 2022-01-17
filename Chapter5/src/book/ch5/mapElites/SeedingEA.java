package book.ch5.mapElites;


import java.util.ArrayList;

import book.ch2.RandomSingleton;
import book.ch2.VRPSolver;

import book.ch3.rep2.Domination;


/*
 * Neil Urquhart 2020
 * A simple Evolutionary Algorithm to solve the CVRPTW based on EliteIndividuals 
 * 

 */
public class SeedingEA extends VRPSolver {
	private ArrayList <SupermarketSolution> population = new ArrayList<SupermarketSolution>();
	
	//population stores our pool of potential solutions
	private RandomSingleton rnd = RandomSingleton.getInstance();
	//Note that we use the RandomSingleton object to generate random numbers

	//EA Parameters
	private int POP_SIZE = 100;
	private int TOUR_SIZE = 2;
	private double XO_RATE = 0.7;
	private int evalsBudget = 20000;
	
	//Reference to the best individual in the population
	private SupermarketSolution bestSoFar; 
	
	@Override
	public void solve() {

		//Initialise population and keep track of best individual found so far
		bestSoFar = InitialisePopution();
		while(evalsBudget >0) {
			//if (evalsBudget %5000 ==0)
			//	System.out.println(evalsBudget + " : " + bestSoFar.evaluate() );
			
			//Create child
			SupermarketSolution child = null;
			if (rnd.getRnd().nextDouble() < XO_RATE){
				//Create a new Individual using recombination, randomly selecting the parents
				child = new SupermarketSolution(super.theProblem, tournamentSelection(TOUR_SIZE),tournamentSelection(TOUR_SIZE));				
			}
			else{
				//Create a child by copying a single parent
				child = (SupermarketSolution) tournamentSelection(TOUR_SIZE).copy();
			}
			child.mutate();
			child.evaluate();
			evalsBudget --;
			
			//Select an Individual with a poor fitness to be replaced
			SupermarketSolution poor = tournamentSelectWorst(TOUR_SIZE);
			if (poor.evaluate() > child.evaluate()){
				//Only replace if the child is an improvement
				
				if (child.evaluate() < bestSoFar.evaluate()){
					bestSoFar = child;
					
				}
				population.remove(poor);
				population.add(child);
	
			}
		}		
		super.theProblem.setSolution((ArrayList)bestSoFar.getPhenotype());
	}

	

	public SupermarketSolution  getBest(){
		return bestSoFar;
	}
	
	public ArrayList<SupermarketSolution>  getFinalPop(){
		return population;
	}
	
	private SupermarketSolution InitialisePopution() {
		//Initialise population with random solutions
		SupermarketSolution best = null;
		for (int count=0; count < POP_SIZE; count++){
			SupermarketSolution i = new SupermarketSolution(super.theProblem);
			
			if (best == null) 
				best = i;
			if (i.evaluate() < best.evaluate()) 
				best = i;
			population.add(i);
			evalsBudget--;
		}
		return best;
	}

	private SupermarketSolution tournamentSelection(int poolSize){
		//Return the best individual from a randomly selected pool of individuals
		SupermarketSolution bestI = null;
		double bestFit = Double.MAX_VALUE;
		for (int tries=0; tries < poolSize; tries++){
			SupermarketSolution i = population.get(rnd.getRnd().nextInt(population.size()));
			if (i.evaluate() < bestFit){
				bestFit = i.evaluate();
				bestI = i;
			}
		}
		return bestI;
	}

	private SupermarketSolution tournamentSelectWorst(int poolSize){
		//Return the worst individual from a randomly selected pool of individuals
		SupermarketSolution bestI = null;
		double bestFit = 0;
		for (int tries=0; tries < poolSize; tries++){
			SupermarketSolution i = population.get(rnd.getRnd().nextInt(population.size()));
			if (i.evaluate() >= bestFit){
				bestFit = i.evaluate();
				bestI = i;
			}
		}
		return bestI;
	}
}
