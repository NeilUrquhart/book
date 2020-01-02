package second;


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
	private NonDominatedPop population = new NonDominatedPop ();
	//population stores our pool of potential solutions
	private RandomSingleton rnd = RandomSingleton.getInstance();
	//Note that we use the RandomSingleton object to generate random numbers

	//EA Parameters
	private int POP_SIZE = 1000;
	private double XO_RATE = 0.7;
	private int evalsBudget = 100000;
	
	@Override
	public void solve() {
		//Reference to the best individual in the population
		InitialisePopution();
		
		
		
		population.rank();
		
		
		while(evalsBudget >0) {	
			
			//Create child
			BiObjectiveIndividual child = null;
			if (rnd.getRnd().nextDouble() < XO_RATE){
				//Create a new Individual using recombination, randomly selecting the parents
				child = new BiObjectiveIndividual(super.theProblem, (BiObjectiveIndividual) tournamentSelection(),(BiObjectiveIndividual) tournamentSelection());				
			}
			else{
				//Create a child by copying a single parent
				child =  ((BiObjectiveIndividual) tournamentSelection()).copy();
			}
			child.mutate();
			child.evaluate();
			evalsBudget --;
			
			//Select an Individual with a poor fitness to be replaced
			BiObjectiveIndividual poor = (BiObjectiveIndividual)tournamentSelectWorst();
			//if (poor.evaluate() > child.evaluate()){
				//Only replace if the child is an improvement
				
				
				//child.check();//Check child contains a valid solution
				population.remove(poor);
				population.add(child);
		//	}
			//if ((evalsBudget % 100)==0)
			 // System.out.println("e,"+ (evalsBudget+",v,"+bestSoFar.getVehicles() + ",cs," + bestSoFar.getCustService()));
		//		population.rank();
//				System.out.println("Non dom");
//				for (Domination d : population.extractNonDom()){
//					BiObjectiveIndividual i = (BiObjectiveIndividual) d;
//					System.out.println("V," +i.getVehicles() + ",CS," + i.getCustService());
//					
//				}
			
				
		}
		
		System.out.println();
		for (Domination d : population.extractNonDom()){
			BiObjectiveIndividual i = (BiObjectiveIndividual) d;
			System.out.println("v,"+i.getVehicles()+",cs,"+i.getCustService()+",d="+i.getDistance());
		}
			
		//System.out.println("v,"+bestSoFar.getVehicles() +",cs,"+ bestSoFar.getCustService()+",d,"+bestSoFar.getDistance());
	}

	public ArrayList<Domination> getNonDom(){
		return population.extractNonDom();
	}
	private BiObjectiveIndividual InitialisePopution() {
		//Initialise population with random solutions
		BiObjectiveIndividual best = null;
		for (int count=0; count < POP_SIZE; count++){
			BiObjectiveIndividual i = new BiObjectiveIndividual(super.theProblem);
			
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

	private Domination tournamentSelection(){
			Domination x = population.get(rnd.getRnd().nextInt(population.size()));
			Domination y = population.get(rnd.getRnd().nextInt(population.size()));
			if (x.dominates(y))//if (x.getRank() < y.getRank())
				return x;
			else
				return y;
	}

	private Domination tournamentSelectWorst(){
		Domination x = population.get(rnd.getRnd().nextInt(population.size()));
		Domination y = population.get(rnd.getRnd().nextInt(population.size()));
		if (y.dominates(x))
		//if (x.getRank() > y.getRank())
			return x;
		else
			return y;
	}

}
