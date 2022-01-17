package book.ch5.mapElites;

import book.ch2.CVRPProblem;
import book.ch5.MObjTWIndividual;

abstract class EliteSolution extends MObjTWIndividual implements Elite {

	public EliteSolution(CVRPProblem prob) {
		super(prob);
	}
	
	public EliteSolution(CVRPProblem prob,MObjTWIndividual a, MObjTWIndividual b) {
		super(prob,a,b);
	}
	
	public EliteSolution(CVRPProblem prob, boolean init) {
		super(prob, init);
	}
}


