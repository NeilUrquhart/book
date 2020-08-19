package book.ch4;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import book.ch1.Visit;
import book.ch2.Individual;
import book.ch2.RandomSingleton;
import book.ch2.VRPSolver;
import book.ch2.VRPVisit;
import book.ch3.rep2.Domination;
import book.ch4.BiObjectiveTWIndividual.Route;
import book.ch4.BiObjectiveTWIndividual.VisitNode;

/*
 * Neil Urquhart 2020
 * A simple Evolutionary Algorithm to solve the CVRPTW problem
 * 
 * Use Individual.check() to ensure that Individuals contain valid solutions. Advisable to turn this on when testing
 * modificatins, and comment it out when testing is completed
 */
public class BiObjTWEA extends VRPSolver {
	private ArrayList <BiObjectiveTWIndividual> population = new ArrayList<BiObjectiveTWIndividual>();
	//population stores our pool of potential solutions
	private RandomSingleton rnd = RandomSingleton.getInstance();
	//Note that we use the RandomSingleton object to generate random numbers

	//EA Parameters
	private int POP_SIZE = 1000;
	private int TOUR_SIZE = 2;
	private double XO_RATE = 0.7;
	private int evalsBudget = 100000;
	
	//Reference to the best individual in the population
	private BiObjectiveTWIndividual bestSoFar; 
	
	@Override
	public void solve() {
		//Initialise population and keep track of best individual found so far
		bestSoFar = InitialisePopution();
		while(evalsBudget >0) {	
			
			//Create child
			BiObjectiveTWIndividual child = null;
			if (rnd.getRnd().nextDouble() < XO_RATE){
				//Create a new Individual using recombination, randomly selecting the parents
				child = new BiObjectiveTWIndividual(super.theProblem, tournamentSelection(TOUR_SIZE),tournamentSelection(TOUR_SIZE));				
			}
			else{
				//Create a child by copying a single parent
				child = (BiObjectiveTWIndividual) tournamentSelection(TOUR_SIZE).copy();
			}
			child.mutate();
			child.evaluate();
			evalsBudget --;
			
			//Select an Individual with a poor fitness to be replaced
			BiObjectiveTWIndividual poor = tournamentSelectWorst(TOUR_SIZE);
			if (poor.evaluate() > child.evaluate()){
				//Only replace if the child is an improvement
				
				if (child.evaluate() < bestSoFar.evaluate()){
					bestSoFar = child;
				}
				population.remove(poor);
				population.add(child);
			}
		}		
//		//temp
//		if (!checkSol(bestSoFar.getPhenotype())) {
//			bestSoFar.reset();
//			bestSoFar.evaluate();
//			checkSol(bestSoFar.getPhenotype());
//		}
		//done temp
		super.theProblem.setSolution((ArrayList)bestSoFar.getPhenotype());
		//System.out.println("v,"+bestSoFar.getRoutes() +",cs,"+ bestSoFar.getCustService()+",d,"+bestSoFar.getDistance());
	}

//	public static boolean checkSol(ArrayList sol ) {
//		System.out.println();
//		for (int c=0; c < sol.size(); c++) {
//			Route r = (Route)sol.get(c);
//			System.out.print(c +"\t");
//			VisitNode old=null;;
//			for (int z=0; z < r.size(); z++) {
//				VisitNode v=r.get(z);
//				System.out.print("["+ v +"]");
//				if(old != null) {
//					 if (old.getDeliveryTime().compareTo(v.getDeliveryTime())>=0) {
//						  System.out.println("error!");
//						  System.exit(-1);
//						  
//					 }
//					 
//					 		
//				}
//				old = v;
//			}
//			System.out.println();
//			
//		}
//		System.out.println("Done checking");
//		return true;
//		
//	}
	public Domination getBestIndividual(){
		return bestSoFar;
	}
	private BiObjectiveTWIndividual InitialisePopution() {
		//Initialise population with random solutions
		BiObjectiveTWIndividual best = null;
		for (int count=0; count < POP_SIZE; count++){
			BiObjectiveTWIndividual i = new BiObjectiveTWIndividual(super.theProblem);
			
			if (best == null) 
				best = i;
			if (i.evaluate() < best.evaluate()) 
				best = i;
			population.add(i);
			evalsBudget--;
		}
		return best;
	}

	private BiObjectiveTWIndividual tournamentSelection(int poolSize){
		//Return the best individual from a randomly selected pool of individuals
		BiObjectiveTWIndividual bestI = null;
		double bestFit = Double.MAX_VALUE;
		for (int tries=0; tries < poolSize; tries++){
			BiObjectiveTWIndividual i = population.get(rnd.getRnd().nextInt(population.size()));
			if (i.getDistance() < bestFit){
				bestFit = i.getDistance();
				bestI = i;
			}
		}
		return bestI;
	}

	private BiObjectiveTWIndividual tournamentSelectWorst(int poolSize){
		//Return the worst individual from a ransomly selected pool of individuals
		BiObjectiveTWIndividual bestI = null;
		double bestFit = 0;
		for (int tries=0; tries < poolSize; tries++){
			BiObjectiveTWIndividual i = population.get(rnd.getRnd().nextInt(population.size()));
			if (i.getDistance() > bestFit){
				bestFit = i.getDistance();
				bestI = i;
			}
		}
		return bestI;
	}
}
