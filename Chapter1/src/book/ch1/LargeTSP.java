package book.ch1;
import java.util.Random;
/*
 * Neil Urquhart 2019
 * A simple class to demonstrate solving the TSP
 */

public class LargeTSP {
	
	public static void main(String[] args) {
		run(50);
		run(100);
		run(1000);
		run(10000);		
	}
	
	public static void run(int size) {
		TSPProblem problem =setupRandProblemInstance(size);//Create a  TSP problem
		
		long time = System.currentTimeMillis();
		
		System.out.print(size+",Hill,");
		double best= Double.MAX_VALUE;
    	for(int count =0; count < 10; count++) {
    		problem.shuffle();
    		problem.solve(new HillTSPSolver());//Solve the problem
        	if (problem.getDistance()<best)
        		best = problem.getDistance();
    	}
    	time = (System.currentTimeMillis() - time);
		System.out.print(","+best+",Time," +time);
    	
    	
    	time = System.currentTimeMillis();
		problem.solve(new NearestNTSPSolver());//Solve the problem
		time = System.currentTimeMillis() - time;
		
    	System.out.print(",NN," +problem.getDistance()+ ",Time,"+time);
    	time = System.currentTimeMillis();
    	
    	System.out.print(",2opt,");
    	 best= Double.MAX_VALUE;
    	for(int count =0; count < 10; count++) {
    		problem.shuffle();
    		problem.solve(new TwoOptTSPSolver());//Solve the problem
    		if (problem.getDistance()<best)
        		best = problem.getDistance();

    	}
    	time = (System.currentTimeMillis() - time);
		System.out.print(","+best+",Time," +time);
    	problem.shuffle();
    	time = System.currentTimeMillis();
		problem.solve(new Hybrid());//Solve the problem
    	System.out.print(",Hybrid," +problem.getDistance());	
    	time = (System.currentTimeMillis() - time)/10;
		System.out.println(",Time," +time);
	}
	
	private static TSPProblem setupRandProblemInstance(int size) {
		/*
		 * Create a new problem with <size> visits  at random locations on a 100*100 map
		 * The start will be placed at 50,50 
		 */
		TSPProblem problem = new TSPProblem();
		problem.setStart(new Visit("Start",500,500));
		Random rng = new Random();
		
		for (int x=0; x < size; x++) {
			problem.addVisit (new Visit("" +x,rng.nextInt(1000),rng.nextInt(1000)));
		}
		return problem;
	}	
}
