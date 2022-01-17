package book.ch5.mapElites;

import book.ch2.CVRPProblem;

public class SupermarketFactory extends EliteSolutionFactory {
	@Override
	public EliteSolution copy(EliteSolution x) {
		return (SupermarketSolution) x.copy();
	}

	@Override
	public EliteSolution getNew(CVRPProblem p) {
		return new SupermarketSolution(p);
	}

	@Override
	public EliteSolution getChild(CVRPProblem p, EliteSolution p1, EliteSolution p2) {
		return new SupermarketSolution(p,(SupermarketSolution)p1,(SupermarketSolution)p2);
	}

}
