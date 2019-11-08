package book.ch2;

import java.util.ArrayList;
import java.util.Collections;

import book.ch1.Visit;

public class HillClimber extends VRPSolver {

	
	@Override
	public void solve() {
		
		//Hill climber
		
		Individual i = new Individual(super.theProblem);
		int evalsBudget = 1000000;
		while(evalsBudget >0) {
			Individual newi = i.copy();
			newi.mutate();
			if (newi.getDistance()<i.getDistance()) {
				i = newi;
			}
			evalsBudget --;
		}
		
		super.theProblem.setSolution(i.getPhenotype());
	}

}
