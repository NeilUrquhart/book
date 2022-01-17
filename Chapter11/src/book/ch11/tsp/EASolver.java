package book.ch11.tsp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import book.ch1.NearestNTSPSolver;
import book.ch1.TSPSolver;
import book.ch1.Visit;

/*
 * Neil Urquhart 2019
 * An implementaton of the 2-opt TSP heuristic
 * 
 */

public class EASolver extends TSPSolver{
	private ArrayList<Individual> pop = new ArrayList<Individual>();
	private Random rnd = new Random();

	//Solve the problem
	public void solve(){
		//repeat until no improvement is made
		int timeOut =0;
		Individual best = null;
		pop.clear();
		for (int c=0; c < 20;c++) {
			Individual p = new Individual((EnhancedTSP) this.theProblem);
			p.addAll(this.theProblem.getSolution());
			for (int x=0; x < 100; x++) {
				p.mutate();

			}
			if (best==null)
				best = p;
			if(p.fitness()<best.fitness())
				best = p;
			pop.add(p);
		}

		while(timeOut <100000 ){
			Individual child =null;
			if (rnd.nextDouble()>0.4) {
				//Select parents
				Individual p = tournament();

				child = new Individual(p);

			}else {
				Individual p1 = tournament();
				Individual p2 = tournament();
				child = new Individual(p1,p2);
			}
			//System.out.println(child.size());
			child.mutate();
			//System.out.println(child.size());
			Individual rip = ripTournament();
			if (child.fitness() < rip.fitness()) {
				pop.remove(rip);
				pop.add(child);
			}
			if (child.fitness() < best.fitness()) {
				best = child;
				//System.out.println(best.fitness());
				timeOut =0;
			}
			timeOut++;
			if ((timeOut %1000)==0) System.out.println(timeOut +" : " + best.fitness());

		}	
		super.theProblem.setRoute(best);//Return best
	}
	private Individual tournament() {
		Individual i1 = pop.get(rnd.nextInt(pop.size()-1)); 
		Individual i2 = pop.get(rnd.nextInt(pop.size()-1)); 

		if (i1.fitness() < i2.fitness())
			return i1;
		else
			return i2;

	}
	private Individual ripTournament() {
		Individual i1 = pop.get(rnd.nextInt(pop.size()-1)); 
		Individual i2 = pop.get(rnd.nextInt(pop.size()-1)); 

		if (i1.fitness() > i2.fitness())
			return i1;
		else
			return i2;

	}

}

