package book.ch3.rep2;

import java.util.ArrayList;

import book.ch2.CVRPProblem;
import book.ch2.RandomSingleton;
import book.ch2.VRPProblemFactory;

/*
 * Copyright Neil Urquhart 2020
 * Run the non-dominated EA 10 times on each problem.
 * Take the 10 individual fronts and combine them into 1 "grand" front
 * 
 */
public class tempTestNonDominatedEA {

	public static void main(String[] args){
		/*Problem instances from.
		 Augerat, P., Belenguer, J., Benavent, E., Corber´an, A., Naddef, D., Rinaldi, G., 1995.
Computational results with a branch and cut code for the capacitated vehicle routing
problem. Tech. Rep. 949-M, Universit´e Joseph Fourier, Grenoble, France. */



		String[] problems = {/*"A-n32-k5","A-n33-k5","A-n33-k6","A-n34-k5","A-n36-k5","A-n37-k5",
				"A-n37-k6","A-n38-k5","A-n39-k5","A-n39-k6","A-n44-k7","A-n45-k6","A-n45-k7",
				"A-n46-k7","A-n48-k7","A-n53-k7","A-n54-k7","A-n55-k9","A-n60-k9","A-n61-k9",
				"A-n62-k8","A-n63-k9","A-n63-k10","A-n64-k9","A-n65-k9","A-n69-k9","A-n80-k10",
				"B-n31-k5","B-n34-k5","B-n35-k5","B-n38-k6","B-n39-k5","B-n41-k6","B-n43-k6",
				"B-n44-k7","B-n45-k5","B-n45-k6","B-n50-k7","B-n50-k8","B-n51-k7","B-n52-k7",
				"B-n56-k7","B-n57-k7","B-n57-k9","B-n63-k10","B-n64-k9","B-n66-k9","B-n67-k10",
				"B-n68-k9","B-n78-k10",
				"P-n16-k8","P-n19-k2","P-n20-k2","P-n21-k2","P-n22-k2","P-n22-k8","P-n23-k8",
				"P-n40-k5","P-n45-k5","P-n50-k7","P-n50-k8","P-n50-k10","P-n51-k10","P-n55-k7",
				"P-n55-k8","P-n55-k10","P-n55-k15","P-n60-k10","P-n60-k15","P-n65-k10",
				"P-n70-k10","P-n76-k4","P-n76-k5",*/"P-n101-k4"};
		
		System.out.println("Problems ");

		for (String fName : problems){
			NonDominatedPop grandFront= new NonDominatedPop();//Create an empty population

			String probName = "./data/"+ fName + ".vrp";
			CVRPProblem myVRP = VRPProblemFactory.buildProblem(probName);//Load instance from file
			
			for (int x=0; x < 1; x++){
				System.out.print(x + ",");
				grandFront.addAll(run(myVRP));
				}

//			//extract GrandFront
//			NonDominatedPop grandFrontA = grandFront.extractNonDom();
//			System.out.println("\nGrand front A,"+fName + "," + grandFrontA.getStats());
//
//			for (Domination d : grandFrontA){
//				BiObjectiveIndividual i = (BiObjectiveIndividual) d;
//				System.out.println("V," +i.getRoutes() + ",CS," + i.getCustService());
//			}
//			
//			for (int c=0; c < 10; c++){
//				System.out.print(c +",");
//				BiObjEA eaSolve = new BiObjEA();
//				BiObjectiveIndividual.setObjective(BiObjectiveIndividual.Objective.ROUTES);
//				myVRP.solve(eaSolve);
//				grandFront.add(eaSolve.getBestIndividual());
//
//				eaSolve = new BiObjEA();
//				BiObjectiveIndividual.setObjective(BiObjectiveIndividual.Objective.CUST_SERVICE);
//				myVRP.solve(eaSolve);
//				grandFront.add(eaSolve.getBestIndividual());
//			}
//			grandFront = grandFront.extractNonDom();
//			System.out.println("\nGrand front B,"+fName + "," + grandFront.getStats());
//
//			for (Domination d : grandFront){
//				BiObjectiveIndividual i = (BiObjectiveIndividual) d;
//				System.out.println("V," +i.getRoutes() + ",CS," + i.getCustService());
//			}
//			System.out.println("\nSummary,"+fName + ", Grand front A,"+	 grandFrontA.getStats() + "Grand front B," + grandFront.getStats());
//
		}
	}

	private static ArrayList<Domination> run(CVRPProblem myVRP) {
		/*
		 * Solve the instance named in  <myVRPe>
		 */
		//Solve using the Evolutionary Algorithm
		//As the Evolutionary Algorithm is stochastic, we repeat 20 times and report the best and average results
		tempNonDomEA eaSolve = new tempNonDomEA();
		
		myVRP.solve(eaSolve);
		return eaSolve.getNonDom();	//Return the non-dominated front
	}

	

}
