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
public class TestNonDominatedEA {

	public static void main(String[] args){
		/*Problem instances from.
		 Augerat, P., Belenguer, J., Benavent, E., Corber´an, A., Naddef, D., Rinaldi, G., 1995.
Computational results with a branch and cut code for the capacitated vehicle routing
problem. Tech. Rep. 949-M, Universit´e Joseph Fourier, Grenoble, France. */



		String[] problems = {"A-n32-k5.vrp","A-n33-k5.vrp","A-n33-k6.vrp","A-n34-k5.vrp","A-n36-k5.vrp","A-n37-k5.vrp",
				"A-n37-k6.vrp","A-n38-k5.vrp","A-n39-k5.vrp","A-n39-k6.vrp","A-n44-k7.vrp","A-n45-k6.vrp","A-n45-k7.vrp",
				"A-n46-k7.vrp","A-n48-k7.vrp","A-n53-k7.vrp","A-n54-k7.vrp","A-n55-k9.vrp","A-n60-k9.vrp","A-n61-k9.vrp",
				"A-n62-k8.vrp","A-n63-k9.vrp","A-n63-k10.vrp","A-n64-k9.vrp","A-n65-k9.vrp","A-n69-k9.vrp","A-n80-k10.vrp",
				"B-n31-k5.vrp","B-n34-k5.vrp","B-n35-k5.vrp","B-n38-k6.vrp","B-n39-k5.vrp","B-n41-k6.vrp","B-n43-k6.vrp",
				"B-n44-k7.vrp","B-n45-k5.vrp","B-n45-k6.vrp","B-n50-k7.vrp","B-n50-k8.vrp","B-n51-k7.vrp","B-n52-k7.vrp",
				"B-n56-k7.vrp","B-n57-k7.vrp","B-n57-k9.vrp","B-n63-k10.vrp","B-n64-k9.vrp","B-n66-k9.vrp","B-n67-k10.vrp",
				"B-n68-k9.vrp","B-n78-k10.vrp",
				"P-n16-k8.vrp","P-n19-k2.vrp","P-n20-k2.vrp","P-n21-k2.vrp","P-n22-k2.vrp","P-n22-k8.vrp","P-n23-k8.vrp",
				"P-n40-k5.vrp","P-n45-k5.vrp","P-n50-k7.vrp","P-n50-k8.vrp","P-n50-k10.vrp","P-n51-k10.vrp","P-n55-k7.vrp",
				"P-n55-k8.vrp","P-n55-k10.vrp","P-n55-k15.vrp","P-n60-k10.vrp","P-n60-k15.vrp","P-n65-k10.vrp",
				"P-n70-k10.vrp","P-n76-k4.vrp","P-n76-k5.vrp","P-n101-k4.vrp"};

		for (String fName : problems){
			NonDominatedPop grandFront= new NonDominatedPop();//Create an empty population
			
			for (int x=0; x < 10; x++)
				grandFront.addAll(run("./data/"+ fName));

			//extract GrandFront
			grandFront = grandFront.extractNonDom();
			System.out.println("Grand front,"+fName + "," + grandFront.getStats());
			
			for (Domination d : grandFront){
				BiObjectiveIndividual i = (BiObjectiveIndividual) d;
				System.out.println("V," +i.getRoutes() + ",CS," + i.getCustService());
			}
		}
	}

	private static ArrayList<Domination> run(String probName) {
		/*
		 * Solve the instance named in  <probName>
		 */
		CVRPProblem myVRP = VRPProblemFactory.buildProblem(probName);//Load instance from file
		//Solve using the Evolutionary Algorithm
		//As the Evolutionary Algorithm is stochastic, we repeat 20 times and report the best and average results
		NonDomEA eaSolve = new NonDomEA();
		
		myVRP.solve(eaSolve);
		return eaSolve.getNonDom();	//Return the non-dominated front
	}


}
