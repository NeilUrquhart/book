package book.ch4.mapElites;

import java.util.ArrayList;

import book.ch2.CVRPProblem;
import book.ch4.BiObjectiveTWIndividual;
import book.ch4.BiObjectiveTWIndividual.Gene;

public class EliteIndividual extends BiObjectiveTWIndividual implements Elite {
	
	public EliteIndividual(CVRPProblem prob) {
		super(prob);
	}
	
	public EliteIndividual(CVRPProblem prob,EliteIndividual a, EliteIndividual b) {
		super(prob,a,b);
	}
	
	public EliteIndividual(CVRPProblem prob, boolean init) {
		super(prob, init);
	}

	@Override
	public double getFitness() {
		return super.getDistance();
	}

	@Override
	public int[] getKey() {
		return KeyGenerator.getInstance(this.problem).getKey(this);
	}
	
	public EliteIndividual copy() {
			//Create a new individual that is a direct copy of this individual
			EliteIndividual copy = new EliteIndividual(this.problem);
			copy.genotype = (ArrayList<Gene>) this.genotype.clone();
			return copy;
	
	}

}
