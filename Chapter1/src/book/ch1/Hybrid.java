package book.ch1;

public class Hybrid extends TSPSolver {
	/*
	 * Neil Urquhart 2019
	 * A solver that combines the NearestNeighbour and 2-Opt solvers
	 */
	public void solve() {
		//Create a nearest neighbour solution
		TSPSolver nn = new NearestNTSPSolver();
		nn.setProblem(this.theProblem);
		nn.solve();
		
		//Improve the NN solution using 2-opt
		TSPSolver twoOpt = new TwoOptTSPSolver();
		twoOpt.setProblem(this.theProblem);
		twoOpt.solve();
	}
}
