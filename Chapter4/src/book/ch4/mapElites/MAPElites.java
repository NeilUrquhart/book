package book.ch4.mapElites;


import java.util.ArrayList;

import book.ch2.RandomSingleton;
import book.ch2.VRPSolver;
import book.ch3.rep2.Domination;


/*
 * Neil Urquhart 2019


 */
public class MAPElites extends VRPSolver {
	private RandomSingleton rnd = RandomSingleton.getInstance();
	//Note that we use the RandomSingleton object to generate random numbers

	private MAPofElites map;
	
	@Override
	public void solve() {
		/*
		 * Create new pop
		 * 
		 */
		System.out.println("MAP-Elites solver");
		//Initialise
      
		KeyGenerator keyGen = KeyGenerator.getInstance(theProblem);
		map = new MAPofElites(keyGen.getDimensions(), keyGen.getBuckets());
		EliteIndividual e = new EliteIndividual(theProblem);
		try {
			System.out.println("Initialising map");
			for (int c=0; c < 5000; c++) {
				e = new EliteIndividual(theProblem);
				
				e.mutate();
				e.evaluate();
				
				if (map.put(e)) {
					System.out.println(c);
					Logger.getLogger().add(Logger.Action.INIT, "",e.getFitness() ,e.getKey());
			}
				
		
			//	System.out.println(map.getUsed() +" " +e.printKey() + " " + e.getFitness() );
				
			}
			System.out.println("Used:"+map.getUsed());
			EliteIndividual  ch;
			String parent="";
			boolean improved = false;
			for (int c=0; c < 500000; c++) {
				if ((c%1000)==0) {
					System.out.println(c + " : "+ map.getUsed()+" : "+improved);
					improved = false;
				}
				Logger.Action action;
				if (rnd.getRnd().nextBoolean()) {
					EliteIndividual p1 = (EliteIndividual)map.getRandom();
					EliteIndividual p2 = (EliteIndividual)map.getRandom();
					
					ch = new EliteIndividual(theProblem, p1,p2);
					parent = p1.printKey() +"," + p2.printKey();
					action = Logger.Action.RECOMBINATION;
					if (rnd.getRnd().nextBoolean()) {
						
						ch.mutate();
						parent = parent + ",MUTATE";
					}
					
				}else {
					ch =  ((EliteIndividual)map.getRandom()).copy();
					parent = ch.printKey();
					ch.mutate();
					action = Logger.Action.CLONE;
				}
					
				if (map.put(ch)) {
					Logger.getLogger().add(action, parent, ch.getFitness(), ch.getKey());
					improved = true;
					//System.out.println(c+ "Used:"+map.getUsed() );
				}
			}


		} catch (InvalidMAPKeyException e1) {
			e1.printStackTrace();
		}
		
		
//		ArrayList<Domination> init = new ArrayList<Domination>();
//		for (int count=0; count < INIT_POP_SIZE; count++){
//			BiObjectiveTWIndividual i = new BiObjectiveTWIndividual(theProblem);
//			i.evaluate();
//			init.add(i);
//		}
//		population = new NonDominatedPop (init);
//		evalsBudget = evalsBudget - INIT_POP_SIZE;//account for initial solutions in pop.
//		while(evalsBudget >0) {	
//			for (int count =0; count < CHILDREN; count ++){
//				//Create child
//				BiObjectiveTWIndividual child = null;
//				if (rnd.getRnd().nextDouble() < XO_RATE){
//					//Create a new Individual using recombination, randomly selecting the parents
//					child = new BiObjectiveTWIndividual(super.theProblem, (BiObjectiveTWIndividual) population.getDominator(),(BiObjectiveTWIndividual) population.getDominator());				
//				}
//				else{
//					//Create a child by copying a single parent
//					child =  ((BiObjectiveTWIndividual) population.getDominator()).copy();
//				}
//				child.mutate();
//				child.evaluate();
//				evalsBudget --;
//
//				population.add(child);
//			}
//			
//			population = population.extractNonDom();	
			
//		}
	}

	public void exportToElVis(String fName) {
		map.exportToElVis(fName);
	}

}
