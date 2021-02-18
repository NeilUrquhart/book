package book.ch4.mapElites;


import java.util.ArrayList;
import java.util.HashSet;

import book.ch2.RandomSingleton;
import book.ch2.VRPSolver;
import book.ch3.rep2.Domination;
import book.ch4.mapElites.ModalCostModel.Mode;


/*
 * Neil Urquhart 2020
 * An implementation of the MAP-Elites algorithm for solving a multi-model CVRPTW problem
 * 
 */
public class MAPElites extends VRPSolver {
	
	private int MAX_EVALS = 100000; //Evaluations per run
	private int INIT_EVALS = 500; //Evals to use for the initialisation
	
	private RandomSingleton rnd = RandomSingleton.getInstance();
	//Note that we use the RandomSingleton object to generate random numbers

	private MAPofElites archive;//The archive of elite solutions
	private ArrayList<Elite> seeds; 
	
	public void setSeeds(ArrayList<Elite> seeds) {
		this.seeds = seeds;
	}

//	public MAPElites() {
//		super();
//		try {
//		KeyGenerator.setup(new CVRPKeyGen(theProblem));
//		archive = new MAPofElites(KeyGenerator.getInstance().getDimensions(), KeyGenerator.getInstance().getBuckets());
//			//Initialise archive by generating 5000 random solutions
//			EliteIndividual e;
//			for (int c=0; c < INIT_EVALS; c++) {
//				e = new EliteIndividual(theProblem);	
//				e.mutate();
//				e.evaluate();
//				if (archive.put(e)) {
//					Logger.getLogger().add(Logger.Action.INIT, "",e.getFitness() ,e.getKey());
//				}
//			}
//		} catch (InvalidMAPKeyException e1) {
//			e1.printStackTrace();
//		}
//
//	}
//	
//	public MAPElites(ArrayList<EliteIndividual> seeds) {
//		super();
//		try {
//			KeyGenerator.setup(new CVRPKeyGen(theProblem));
//			archive = new MAPofElites(KeyGenerator.getInstance().getDimensions(), KeyGenerator.getInstance().getBuckets());
//			for (EliteIndividual e : seeds) {
//				if (archive.put(e)) {
//					Logger.getLogger().add(Logger.Action.INIT, "",e.getFitness() ,e.getKey());
//				}
//			}
//		} catch (InvalidMAPKeyException e1) {
//			e1.printStackTrace();
//		}
//
//	}
	
	@Override
	public void solve() {      
		KeyGenerator.setup(new CVRPKeyGen(theProblem));
		archive = new MAPofElites(KeyGenerator.getInstance().getDimensions(), KeyGenerator.getInstance().getBuckets());
		try {
			
			if (seeds !=null) {
				for(Elite seed : seeds)
					if (archive.put(seed)) {
						Logger.getLogger().add(Logger.Action.INIT, "",seed.getFitness() ,seed.getKey());
						EliteIndividual i = (EliteIndividual) seed;
						//System.out.println( "seeding : "+ archive.getUsed() + " - " + i.getCostDel());
					}
			}
			else {
				//Initialise archive by generating 5000 random solutions
				EliteIndividual e;
				for (int c=0; c < INIT_EVALS; c++) {
					e = new EliteIndividual(theProblem);	
					e.mutate();
					e.evaluate();
					if (archive.put(e)) {
						Logger.getLogger().add(Logger.Action.INIT, "",e.getFitness() ,e.getKey());
						//System.out.println( "random : "+ archive.getUsed());
					}
				}
			}

			//System.out.println( "setup: "+ archive.getUsed());
			EliteIndividual  ch;
			String descForLogger="";
			for (int c=0; c < MAX_EVALS; c++) {
				if ((c%10000)==0) {
					System.out.print(" "+c + " : "+ archive.getUsed());
				}
				Logger.Action action;
				if (rnd.getRnd().nextBoolean()) {
					//Select two random parents from the archive
					EliteIndividual p1 = (EliteIndividual)archive.getRandom();
					EliteIndividual p2 = (EliteIndividual)archive.getRandom();

					//Create a new solution from the parents
					ch = new EliteIndividual(theProblem, p1,p2);
					descForLogger = p1.keyToString() +"," + p2.keyToString();
					action = Logger.Action.RECOMBINATION;
					
					if (rnd.getRnd().nextBoolean()) {
						ch.mutate();		//Mutate the child solution				
						descForLogger = descForLogger + ",MUTATE";
					}					
				}else {//Create a new solution by copying an existing member of the archive
					ch =  ((EliteIndividual)archive.getRandom()).copy();
					ch.evaluate();
					descForLogger = ch.keyToString();
					ch.mutate();
					action = Logger.Action.CLONE;
				}
				ch.evaluate();
				if (archive.put(ch)) {//Put the solution into the Archive. Returns True if this
									  //solution is allowed to join the archive
					Logger.getLogger().add(action, descForLogger, ch.getFitness(), ch.getKey());
				}
			}
			System.out.println();

		} catch (InvalidMAPKeyException e1) {
			e1.printStackTrace();
		}
	}


	public ArrayList<Object> getArchive() {
		/*
		 * Write a summary of the archive to a CSV file
		 * 
		 */
		return archive.getArchive();
	}

	public HashSet<String> getKeys(){
		/*
		 * Return a list of all the keys un he archive
		 */
		return archive.getKeys();
	}
	
	
}
