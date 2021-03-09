package book.ch4.mapElites;

import book.ch2.CVRPProblem;
import book.ch4.CVRPTWProblem;

public abstract class EliteSolutionFactory {
	public abstract EliteSolution getNew(CVRPProblem p);
	public abstract EliteSolution copy(EliteSolution s);
	public abstract EliteSolution getChild(CVRPProblem p, EliteSolution p1, EliteSolution p2);
}
