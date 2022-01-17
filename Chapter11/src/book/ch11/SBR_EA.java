package book.ch11;
import java.util.ArrayList;
import java.util.Random;

import book.ch1.LatLon;

/*
 * Neil Urquhart 2021
 * 
 * An EA for solving an SBR problem
 */

public class SBR_EA {
	private ArrayList<SBRIndividual>population;
	private Random rnd  = new Random();
	private int evals=0;
	private SBRIndividual best = null;
	private int[] evalTotals = new int[10];
	private double[] fits = new double[10];
	private double[] times = new double[10];
	private LatLon startEnd;
	private double startTime;
	
	/*
	 * Constants
	 */

	int POP_SIZE = 750;//Population size
	int REPEATS = 10;//Number of times to run
	double XOVER_PRESSURE = 0.3;//0-1 chance of child being created by
								//xover (rather than by clone)
	
	public SBR_EA(LatLon startEnd, SBRIndividual chromo) {
		this.startEnd=startEnd;
		startTime = System.currentTimeMillis();
		initPop(startEnd, chromo);
		System.out.println("Start " +best.evaluate());
	}

	public void runEA() {
		/*
		 * Run the EA 10 times
		 * Print out the stats
		 */
		for (int t=0; t < REPEATS; t++)
			runEA(t);

		System.out.println("Stats for "+ REPEATS+" runs");
		System.out.println("Run,fitness,evals used,time taken");
		
		for (int c=0;c<REPEATS;c++) {
			System.out.println(c+"," + fits[c]+","+evalTotals[c]+","+times[c]);
		}
	}
	
	private void runEA(int tries){
		/*
		 * Run the EA once
		 */
		int timeOut = 100000;
		int gensSinceCh=-1;
		while(gensSinceCh < timeOut) {
			SBRIndividual p1 = tournament();
			SBRIndividual p2 = tournament();
			SBRIndividual child;
			//Create child
			if (rnd.nextFloat()<XOVER_PRESSURE) {
				child = new SBRIndividual(p1,p2);
			}else {
				child = new SBRIndividual(startEnd,p1);
			}
			child.mutate();
			
			/* 
			 * Put child back into population 
			 * 
			 * Select the looser of a tournament - rip()
			 * Replace rip with child, if child is an
			 * improvement on rip
			 * 
			 */
			SBRIndividual rip = rip();
			if (child.evaluate() < rip.evaluate()) {
				population.remove(rip);
				population.add(child);
				
				evals++;
				if (child.evaluate() < best.evaluate()) {
					/*
					 * Check to see if the child represents an
					 * advance on the current best
					 */
					best = child;
					gensSinceCh=-1;
				}
			}
			gensSinceCh++;
			if(((gensSinceCh%10000)==0)|| (gensSinceCh==0))
				System.out.println( tries +"\t" +gensSinceCh +"\t" + best.evaluate());		
		}
		
		best.save(tries);//Write the best solution to a file
		fits[tries] = best.getDistance(); //Store fitness
		evalTotals[tries] = evals++; //Store evals used
		times[tries] = System.currentTimeMillis()-startTime;//Store time taken
	}

	private void initPop(LatLon startEnd, SBRIndividual chromo) {
		/*
		 * Create a new population of SBRIndividul objects
		 * POP_SIZE specifies size of pop 
		 * <chromo> is an valid chromosome from which to copy
		 * StreetSection objects
		 */
		population = new ArrayList<SBRIndividual>();
		for(int count =0; count < POP_SIZE; count ++){
			System.out.print("Init " + count +"\t");
			SBRIndividual i = new SBRIndividual(startEnd,chromo);
			population.add(i);
			i.randomise();
			if (best == null) {
				best =i;
				System.out.print(best.evaluate());
			}else {
				evals++;
				if (i.evaluate() < best.evaluate()) {
					best = i;
					System.out.print(best.evaluate());
				}
			}
			System.out.println();
		}
	}

	private SBRIndividual rip() {
		/*
		 * Run a tournament of size 2 and return the looser
		 */
		SBRIndividual rip1 = population.get(rnd.nextInt(population.size()-1));
		SBRIndividual rip2 = population.get(rnd.nextInt(population.size()-1));			
		SBRIndividual rip = null;
		if (rip1.evaluate() > rip2.evaluate())
			rip = rip1;
		else
			rip = rip2;
		return rip;
	}

	private  SBRIndividual tournament() {
		/*
		 * Run a tournament of size 2 and return the winner
		 */
		SBRIndividual p1 = population.get(rnd.nextInt(population.size()-1));	
		SBRIndividual p2 = population.get(rnd.nextInt(population.size()-1));	
		SBRIndividual p = null;
		if (p1.evaluate() < p2.evaluate())
			p = p1;
		else
			p = p2;
		return p;
	}
}