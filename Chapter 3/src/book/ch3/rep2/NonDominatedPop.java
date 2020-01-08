package book.ch3.rep2;

import java.util.ArrayList;

/*
 * Copyright Neil Urquhart 2020
 * 
 * Implements a population structure used when finding a non-dominated population.
 * The individuals within the population must implement Domination. 
 * 
 */

public class NonDominatedPop extends ArrayList<Domination> {

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
			res = res + ",obj"+pos +"Max," + max[pos] + ",obj"+pos +"Min,"+ min[pos];
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
}