package book.ch4.mapElites;

import java.util.ArrayList;
import book.ch2.RandomSingleton;
import book.ch2.VRPSolver;

/*
 * Neil Urquhart 2020
 * An implementation of the MAP-Elites algorithm for solving a multi-model CVRPTW problem
 */
public class MAPElites extends VRPSolver {
	private int MAX_EVALS = 100000; //Evaluations per run
	private int INIT_EVALS = 500; //Evals to use for the initialisation
	
	private EliteSolutionFactory solutionFactory;
	private RandomSingleton rnd = RandomSingleton.getInstance();
	//Note that we use the RandomSingleton object to generate random numbers
	private Archive archive;//The archive of elite solutions
	private ArrayList<Elite> seeds; 

	public void setSeeds(ArrayList<Elite> seeds) {
		this.seeds = seeds;
	}

	public void setSolutionFactory(EliteSolutionFactory f) {
		this.solutionFactory = f;
	}
	
	@Override
	public void solve() {      
		archive = new Archive(KeyGenerator.getInstance().getDimensions(), KeyGenerator.getInstance().getBuckets());
		initialise();
		EliteSolution  ch;
		String descForLogger="";
		for (int c=0; c < MAX_EVALS; c++) {
			if ((c%10000)==0) {
				System.out.print(" "+c + " : "+ archive.size());
			}
			Logger.Action action;
			if (rnd.getRnd().nextBoolean()) {
				//Select two random parents from the archive
				EliteSolution p1 = (EliteSolution)archive.getRandom();
				EliteSolution p2 = (EliteSolution)archive.getRandom();

				//Create a new solution from the parents
				ch = (EliteSolution) solutionFactory.getChild(theProblem, p1,p2);
				descForLogger = p1.keyToString() +"," + p2.keyToString();
				action = Logger.Action.RECOMBINATION;

				if (rnd.getRnd().nextBoolean()) {
					ch.mutate();		//Mutate the child solution				
					descForLogger = descForLogger + ",MUTATE";
				}					
			}else {//Create a new solution by copying an existing member of the archive
				ch =  solutionFactory.copy((EliteSolution)archive.getRandom());
				ch.evaluate();
				descForLogger = ch.keyToString();
				ch.mutate();
				action = Logger.Action.CLONE;
			}
			ch.evaluate();
			if (archive.put(ch)) {//Put the solution into the Archive. Returns True if this
				//solution is allowed to join the archive
				Logger.getLogger().add(action, descForLogger, ch.getFitness(),ch.getSummary(), ch.getKey());
			}
		}
		System.out.println();
	}

	private void initialise() {
		if (seeds !=null) {
			for(Elite seed : seeds)
				if (archive.put(seed)) {
					Logger.getLogger().add(Logger.Action.SEED, "",seed.getFitness(),seed.getSummary() ,seed.getKey());
				}
		}
		else {
			//Initialise archive by generating 5000 random solutions
			EliteSolution e;
			for (int c=0; c < INIT_EVALS; c++) {
				e = solutionFactory.getNew(theProblem);	
				e.mutate();
				e.evaluate();
				if (archive.put(e)) {
					Logger.getLogger().add(Logger.Action.INIT, "",e.getFitness(),e.getSummary() ,e.getKey());
				}
			}
		}
	}

	public ArrayList<Elite> getArchive() {
		/*
		 * Return a list of all Elite objects in the archive
		 */
		return archive.toList();
	}
}