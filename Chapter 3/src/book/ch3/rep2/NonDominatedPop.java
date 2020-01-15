package book.ch3.rep2;

import java.util.ArrayList;

import book.ch2.CVRPProblem;
import book.ch2.RandomSingleton;

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

	public NonDominatedPop() {//Defualt constructor.
		super();
	}
			
	public NonDominatedPop(int size, CVRPProblem theProblem) {
		//Create a new population with <size> random solutions to <theProblem>
		//Initialise population with random solutions
		BiObjectiveIndividual best = null;
		for (int count=0; count < size; count++){
			BiObjectiveIndividual i = new BiObjectiveIndividual(theProblem);

			if (best == null) 
				best = i;
			if (i.evaluate() < best.evaluate()) 
				best = i;
			this.add(i);
			
		}
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
	
	public NonDominatedPop extractNonDom(){
		/*
		 * Return a population containing ONLY the non dominated individuals
		 */
		
		NonDominatedPop result = new NonDominatedPop();
		
		for (Domination current : this){
			boolean dominated =  false;
			for (Domination member : this){
			  if (member.dominates(current))
				  dominated = true;
			  }
			
			if (! dominated)
				result.add(current);
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
	
//	public void delete(Domination d){
//		int index=0;
//		for (Domination current: this){
//			if ((current.getVector()[0]==d.getVector()[0]) &&
//					(current.getVector()[1]==d.getVector()[1])){
//				;
//				break;
//			}
//			index ++;
//		}
//		this.remove(index);
//	}

	public Domination getDominator(){
		/*
		 * Select two individuals at random and if one dominates the other 
		 */
		Domination x = this.get(rnd.getRnd().nextInt(this.size()));
		Domination y = this.get(rnd.getRnd().nextInt(this.size()));
		if (x.dominates(y))
			return x;
		else
			return y;
	}
	public Domination getRIP(){
		/*
		 * Select 2 individuals at random.
		 * If one dominates the other return the dominated individual
		 */
		while(true){
			Domination x = this.get(rnd.getRnd().nextInt(this.size()));
			Domination y = this.get(rnd.getRnd().nextInt(this.size()));

			double[] xv = x.getVector();
			double[] yv = y.getVector();

			if ((xv[0] <= yv[0]))
				if (xv[1] <= yv[1])
					return y;
		}
	}
	
//	public Domination getDominated(){
//		/*
//		 * Select 2 individuals at random.
//		 * If one dominates the other return the dominated individual
//		 */
//		Domination x = this.get(rnd.getRnd().nextInt(this.size()));
//		Domination y = this.get(rnd.getRnd().nextInt(this.size()));
//		
//		if (y.dominates(x))
//			return x;
//		else
//			return y;
//	}
}