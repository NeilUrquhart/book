package book.ch4;

import java.util.ArrayList;

import book.ch2.RandomSingleton;

import book.ch3.rep2.Domination;

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
		//Header
		System.out.println("Problem,Deliveries,Demand,Avg.Bags.Del,Time Window,Sol. Distance, Sol. Time,Sol. CostDel,Routes,Sol. Cost,Sol. Avg. Dels Route,Sol. Loading Factor");


		String[] problems = {"A-n32-k5","A-n33-k5","A-n33-k6","A-n34-k5","A-n36-k5","A-n37-k5",
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
				"P-n70-k10","P-n76-k4","P-n76-k5","P-n101-k4"};

		for (String pName : problems){
			double[] frontSize = new double[14];
			for (int win = 14; win  >0; win--) {

				String fName  = pName+".vrp";
				String pSizeS = pName.split("-")[1];
				pSizeS = pSizeS.replace("n","");
				int size = Integer.parseInt(pSizeS);
				NonDominatedPop grandFront= new NonDominatedPop();//Create an empty population


				CVRPTWProblem myVRP = VRPTWProblemFactory.buildProblem("./data/",fName,win);//Load instance from file

				int demand = myVRP.getTotalDemand();
				double avgBags = demand/size;


				//Find best for each objective



				for (int x=0; x < 10; x++){
					System.out.print(x + ",");
					grandFront.addAll(run(myVRP));
				}

				//extract GrandFront
				NonDominatedPop grandFrontA = grandFront.extractNonDom();
				System.out.println("\nGrand front ,"+win+","+fName + "," + grandFrontA.getStats());
				//System.out.println(",," + minR +","+maxR+",,"+ minCS +","+maxCS);

				for (Domination d : grandFrontA){
					BiObjectiveTWIndividual i = (BiObjectiveTWIndividual) d;
					double loadFactor;
					double totalCapacity = myVRP.getCapacity() * i.getRoutes();
					loadFactor = ((myVRP.getTotalDemand()/totalCapacity)*100);
					System.out.println(pName+","+size+ ","+ demand+ ","+avgBags+","+ win+"," +i.getDistance()+ "," + i.getTime()+","+i.getCostDel() +","+i.getRoutes()+","+i.getCost()+","+(size/i.getRoutes())+","+loadFactor);
				}	
				frontSize[win-1] = grandFrontA.size();
			}
			
			System.out.print("Front Sizes,"+pName+",");
			for (double s : frontSize)
				System.out.print(s+",");
			
			System.out.println();

		}
	}

	private static ArrayList<Domination> run(CVRPTWProblem myVRP) {
		/*
		 * Solve the instance named in  <myVRPe>
		 */
		//Solve using the Evolutionary Algorithm
		//As the Evolutionary Algorithm is stochastic, we repeat 20 times and report the best and average results
		NonDomEA eaSolve = new NonDomEA();

		myVRP.solve(eaSolve);
		return eaSolve.getNonDom();	//Return the non-dominated front
	}



}
