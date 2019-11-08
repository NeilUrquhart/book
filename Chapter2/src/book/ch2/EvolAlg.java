package book.ch2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import book.ch1.Visit;

public class EvolAlg extends VRPSolver {
	private ArrayList <Individual> population = new ArrayList<Individual>();
	private RandomSingleton rnd = RandomSingleton.getInstance();
	
	@Override
	public void solve() {
		
		//Simple EA
		int POP_SIZE = 500;
		int TOUR_SIZE = 2;
		double XO_RATE = 0.7;
		int evalsBudget = 1000000;
		
			
		Individual best = null;
		
		//init pop
		for (int count=0; count < POP_SIZE; count++){
		   Individual i = new Individual(super.theProblem);
		   i.check();
		   if (best == null) best = i;
		   
		   if (i.evaluate() < best.evaluate()) best = i;
			   
		   //System.out.println(i.evaluate());
		   population.add(i);
		   evalsBudget--;
		}
		//System.out.println("Best = " + best.evaluate());
		while(evalsBudget >0) {
			
			Individual child = null;
			char created= ' ';
			if (rnd.getRnd().nextDouble() < XO_RATE){
				child = new Individual(super.theProblem, tourSelect(TOUR_SIZE),tourSelect(TOUR_SIZE));
				created = 'X';
			}
			else{
				child = tourSelect(TOUR_SIZE).copy();
				created = 'C';
			}
			
			child.mutate();
			child.evaluate();
			evalsBudget --;
			
			Individual poor = tourSelectWorst(TOUR_SIZE);
			if (poor.evaluate() > child.evaluate()){
				//System.out.println(evalsBudget + " Remove " + poor.evaluate() + " Replace "+ child.evaluate() + " " + created + " best =" + best.evaluate());
				if (child.evaluate() < best.evaluate()){
					best = child;
					//System.out.println("Best = " + best.evaluate());
				}
				//TEMP - Check child
				child.check();
				//Done temp
				population.remove(poor);
				population.add(child);
			}
			
		}
		//find best
	//	Individual best = population.get(0);
	////	for (Individual i : population){
		//	if (i.evaluate() < best.evaluate())
			//	best = i;
	//	}
		
		super.theProblem.setSolution(best.getPhenotype());
	//	System.out.println(super.theProblem);
	}
	
	private Individual tourSelect(int size){
		Individual bestI = null;
		double bestFit = Double.MAX_VALUE;
		for (int tries=0; tries < size; tries++){
			
			Individual i = population.get(rnd.getRnd().nextInt(population.size()));
			if (i.getDistance() < bestFit){
				bestFit = i.getDistance();
				bestI = i;
			}
		}
		return bestI;
	}

	private Individual tourSelectWorst(int size){
		Individual bestI = null;
		double bestFit = 0;
		for (int tries=0; tries < size; tries++){
			
			Individual i = population.get(rnd.getRnd().nextInt(population.size()));
			if (i.getDistance() > bestFit){
				bestFit = i.getDistance();
				bestI = i;
			}
		}
		return bestI;
	}
}
