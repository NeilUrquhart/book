package book.ch4.mapElites;

import book.ch2.CVRPProblem;
import book.ch4.BiObjectiveTWIndividual;

abstract class EliteSolution extends BiObjectiveTWIndividual implements Elite {

	public EliteSolution(CVRPProblem prob) {
		super(prob);
	}
	
	public EliteSolution(CVRPProblem prob,BiObjectiveTWIndividual a, BiObjectiveTWIndividual b) {
		super(prob,a,b);
	}
	
	public EliteSolution(CVRPProblem prob, boolean init) {
		super(prob, init);
	}
}


