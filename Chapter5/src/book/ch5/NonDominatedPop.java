package book.ch5;

import java.util.ArrayList;

import book.ch2.CVRPProblem;
import book.ch2.RandomSingleton;
import book.ch3.rep2.Domination;

/*
 * Copyright Neil Urquhart 2020
 * 
 * Implements a population structure used when finding a non-dominated population.
 * The individuals within the population must implement Domination. 
 * 
 */

public class NonDominatedPop extends ArrayList<Domination> {

	private RandomSingleton rnd = RandomSingleton.getInstance();
	//Note that we use the RandomSingleton object to generate random numbers

	public NonDominatedPop() {super();}//Default constructor.

	public NonDominatedPop( ArrayList<Domination> init) {
		super();
		//Create a new population with <size> random solutions to <theProblem>
		//Initialise population with random solutions
		
		this.addAll(init);

	}

	public Domination getDominator(){
		/*
		 * Select two individuals at random and if one dominates the other return it
		 */
		Domination x = this.get(rnd.getRnd().nextInt(this.size()));
		Domination y = this.get(rnd.getRnd().nextInt(this.size()));
		if (x.dominates(y))
			return x;
		else
			return y;
	}

	public NonDominatedPop extractNonDom(){
		/*
		 * Return a population containing ONLY the non dominated individuals
		 */
		NonDominatedPop result = new NonDominatedPop();
		for (Domination current : this){
			boolean dominated =  false;
			for (Domination member : this){
				if (member.dominates(current)) {
					dominated = true;
					break;
				}		
			}
			if (! dominated) {
				if (!result.containsSol(current.getVector()))
					result.add(current);
			}
		}
		return result;
	}

	public NonDominatedPop removeNonDom(){
		/*
		 * Return a population containing ONLY the non dominated individuals
		 * AND remove them from the current population
		 */

		NonDominatedPop nd = this.extractNonDom();
		for (Object b: nd){
			this.remove(b);
		}
		return nd;
	}	
	
	public boolean containsSol(double[] vect) {
	
		for (Domination d : this) {
			double[] currentVector = d.getVector();
			boolean dup = true;
			for (int x=0; x < currentVector.length; x++)
				if (vect[x] != currentVector[x])
					dup = false;
			
			if(dup)
				return true;
		}
		return false;
	}

	public String getStats(){
		/*
		 * Print out population statistics.
		 */
		String res = "";
		res = "n," + this.size();
		int size = this.get(0).getVector().length;

		double[] max = new double[size];
		double[] min = new double[size];

		for (int c=0; c < size; c++){
			max[c] = -1;
			min[c] = Double.MAX_VALUE;
		}

		//Find the maximum and minimum for each objective
		for(Domination d : this){
			double[] v = d.getVector();
			for (int pos = 0; pos < size; pos++){
				if (v[pos] < min[pos])
					min[pos] = v[pos];
				if (v[pos] > max[pos])
					max[pos] = v[pos];
			}
		}

		for (int pos = 0 ; pos < size; pos++){
			res = res + ",obj"+pos +"Min," + min[pos] + ",obj"+pos +"Max,"+ max[pos];
		}
		return res;
	}
}