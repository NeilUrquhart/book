package book.ch8.humanitarian;

import java.util.ArrayList;

import book.ch1.Visit;
import book.ch2.Individual;
import book.ch2.VRPea;

public class HudEA extends VRPea {
	private static int evalsChange = 20000;//default
	private ArrayList<Visit> initSol;
	private int POP_SIZE =100;
	
	public HudEA(ArrayList<Visit> initSol) {
		this.initSol = initSol;
		
	}
	@Override
	protected Individual InitialisePopution() {
		System.out.println("Setting up problem");
		//Initialise population with random solutions
		Individual best = null;
		for (int count=0; count < POP_SIZE; count++){
			if((count%10)==0)
			  System.out.println(count);
			
			Individual i;
			if ((count ==0)&&(initSol!=null))
			  i = new HudIndividual(super.theProblem,initSol);
			else {
			  if (rnd.getRnd().nextDouble()>0.75)
			  {
				i = population.get(0).copy();//Based on pop(0) which is NN based
				for (int c=0;  c < 10; c++);
					i.mutate();
			  }else
			    i = new HudIndividual(super.theProblem);//Random
			}
			
			if (best == null) 
				best = i;
			if (i.evaluate() < best.evaluate()) 
				best = i;
			
			population.add(i);
			evalsBudget--;
		}
		return best;
	}
	
	@Override
	public void solve() {
		int factor =(int) (super.theProblem.getSize()/10);
		evalsChange = evalsChange +(factor*5000);
		System.out.println("Time out = "+ evalsChange);
		int timeOut = evalsChange;
		
		//Reference to the best individual in the population
		Individual bestSoFar = InitialisePopution();
		HudProblem myProb = (HudProblem)theProblem;
		while(timeOut >0) {	
			//Create child
			Individual child = null;
			if (rnd.getRnd().nextDouble() < XO_RATE){
				//Create a new Individual using recombination, randomly selecting the parents
				child = new HudIndividual(super.theProblem, tournamentSelection(TOUR_SIZE),tournamentSelection(TOUR_SIZE));				
			}
			else{
				//Create a child by copying a single parent
				child = tournamentSelection(TOUR_SIZE).copy();
			}
			child.mutate();
			child.evaluate();
			
			//local search
			timeOut --;
			
			//Select an Individual with a poor fitness to be replaced
			Individual poor = tournamentSelectWorst(TOUR_SIZE);
			if (poor.evaluate() > child.evaluate()){
				//Only replace if the child is an improvement
				
				if (child.evaluate() < bestSoFar.evaluate()){
					bestSoFar = child;
					timeOut =evalsChange;
					//System.out.println("New solution found "+bestSoFar.getVehicles() +":"+bestSoFar.getDistance()+":"+ bestSoFar.evaluate());
				}
				//child.check();//Check child contains a valid solution
				population.remove(poor);
				population.add(child);
			}
			if ((timeOut %1000)==0)
			  System.out.println("Remaining "+(timeOut/1000) +"\tDist "+bestSoFar.getDistance() +"\tRounds "+bestSoFar.getVehicles()+"\tEvals" +HudIndividual.getEvals()) ;
		
			//Force exit if time is up
			if((System.currentTimeMillis()-myProb.getStartTime())>myProb.getRunTime())
				timeOut=0;
		}
		

		super.theProblem.setSolution(bestSoFar.getPhenotype());
		System.out.println("Finished! \nThanks for waiting.");
		System.out.println("Fitness = " + bestSoFar.evaluate() + " using " + HudIndividual.getEvals());
	}

}
