package book.ch5;

import java.util.ArrayList;

import book.ch3.rep2.Domination;

public class MultiObjTest {

	public static void main(String[] args) {
		String[] problems = {"A-n32-k5.vrp" ,"A-n33-k5.vrp","A-n33-k6.vrp","A-n34-k5.vrp","A-n36-k5.vrp","A-n37-k5.vrp",
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

		System.out.println("Single,prob,tw len,,CostDel:CostDel,CostDel:Routes,CostDel:Time,CostDel:Dist,,,Routes:CostDel,Routes:Routes,Routes:Time,Routes:Dist,,Time:CostDel,Time:Routes,Time:Time,Time:Dist,,Dist:CostDel,Dist:Routes,Dist:Time,Dist:Dist");
		System.out.println("Grand Front,tw,prob,n,size,,dist min,,dist max,,time min,,time max,,cost min,,cost max,,routes min,,routes max");
		System.out.println("GF Member,prob,size,demand,avg bags,winlen,dist,time,costDel,getRoutes,getCost,dels/route,loadFactor");


		for (String prob : problems) {
			//Create opt for each objective
			run("./data/",prob,1);
			run("./data/",prob,14);
		}

	}

	private static NonDominatedPop run(String path,String probName,int winLen) {
		//Solve the instance named in  <probName>
		VRPTWProblem myVRP = VRPTWProblemFactory.buildProblem(path, probName,winLen);//Load instance from file
		NonDominatedPop solutionPool= new NonDominatedPop();//Create an empty population
		
		//Solve using the Evolutionary Algorithm 4 times, once for each objective
		//1 . cost_del
		MObjTWIndividual best = solve(myVRP,MObjTWIndividual.Objective.COST_DEL);
		solutionPool.add(best);
		//2 . routes
		best = solve(myVRP,MObjTWIndividual.Objective.ROUTES);
		solutionPool.add(best);
		//3 . time
		best = solve(myVRP,MObjTWIndividual.Objective.TIME);
		solutionPool.add(best);
		//4 . dist
		best = solve(myVRP,MObjTWIndividual.Objective.DISTANCE);
		solutionPool.add(best);
		
		//now run pareto front
		String pSizeS = probName.split("-")[1];
		pSizeS = pSizeS.replace("n","");
		int size = Integer.parseInt(pSizeS);
		double avgBags = myVRP.getTotalDemand()/size;
		for (int x=0; x < 10; x++){
			NonDomEA eaSolve = new NonDomEA();
			myVRP.solve(eaSolve);
			solutionPool.addAll(eaSolve.getNonDom());
		}
		//extract GrandFront
		return  solutionPool.extractNonDom();
	}
	
	private static void verboserun(String path,String probName,int winLen) {
		//As for the run method, but this method prints a commentry to stdout
		//Solve the instance named in  <probName>
		VRPTWProblem myVRP = VRPTWProblemFactory.buildProblem(path, probName,winLen);//Load instance from file
		NonDominatedPop grandFront= new NonDominatedPop();//Create an empty population
		
		//Solve using the Evolutionary Algorithm 4 times, once for each objective
		System.out.print("SINGLE,"+probName +","+winLen);
		
		//1 . cost_del
		MObjTWIndividual best = solve(myVRP,MObjTWIndividual.Objective.COST_DEL);
		System.out.print(",COST_DEL,");
		System.out.print(best.getCostDel() +","+(best.getRoutes()) +","+(best.getTime())+","+(best.getDistance()));
		grandFront.add(best);
		
		//2 . routes
		best = solve(myVRP,MObjTWIndividual.Objective.ROUTES);
		System.out.print(","+ ",ROUTES,");
		System.out.print(best.getCostDel() +","+(best.getRoutes()) +","+(best.getTime())+","+(best.getDistance()));
		grandFront.add(best);
		

		//3 . time
		best = solve(myVRP,MObjTWIndividual.Objective.TIME);
		System.out.print( ",TIME,");
		System.out.print(best.getCostDel() +","+(best.getRoutes()) +","+(best.getTime())+","+(best.getDistance()));
		grandFront.add(best);
		
		//4 . dist
		best = solve(myVRP,MObjTWIndividual.Objective.DISTANCE);
		System.out.print( ",DIST,");
		System.out.println(best.getCostDel() +","+(best.getRoutes()) +","+(best.getTime())+","+(best.getDistance()));
		grandFront.add(best);
		
		//now run pareto front
		String fName  = probName;
		String pSizeS = probName.split("-")[1];
		pSizeS = pSizeS.replace("n","");
		int size = Integer.parseInt(pSizeS);
		myVRP = VRPTWProblemFactory.buildProblem("./data/",fName,winLen);//Load instance from file
		int demand = myVRP.getTotalDemand();
		double avgBags = demand/size;
		for (int x=0; x < 10; x++){
			System.out.print(x + ",");
			NonDomEA eaSolve = new NonDomEA();
			myVRP.solve(eaSolve);
			grandFront.addAll(eaSolve.getNonDom());
		}

		//extract GrandFront
		NonDominatedPop grandFrontA = grandFront.extractNonDom();
		System.out.println("\nGrand front ,"+winLen+","+fName + "," + grandFrontA.getStats());
		
		for (Domination d : grandFrontA){
			MObjTWIndividual i = (MObjTWIndividual) d;
			double loadFactor;
			double totalCapacity = myVRP.getCapacity() * i.getRoutes();
			loadFactor = ((myVRP.getTotalDemand()/totalCapacity)*100);
			System.out.println("GF Member,"+probName+","+size+ ","+ demand+ ","+avgBags+","+ winLen+"," +i.getDistance()+ "," + i.getTime()+","+i.getCostDel() +","+i.getRoutes()+","+i.getCost()+","+(size/i.getRoutes())+","+loadFactor);
		}	
	}
	
	private static MObjTWIndividual solve(VRPTWProblem myVRP, MObjTWIndividual.Objective obj) {
			MObjTWIndividual best = null;
			for (int x=0; x < 10; x++) {
				VRPTWEA eaSolve = new VRPTWEA();
				MObjTWIndividual.setObjective(obj);

				myVRP.solve(eaSolve);
				if (best == null) best = myVRP.best;
				if (myVRP.best.getCostDel() < best.getCostDel()) {
					best = myVRP.best;
				}
			}
			return best;
	}
}
