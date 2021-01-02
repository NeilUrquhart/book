package book.ch4;

import java.util.ArrayList;

import book.ch2.CVRPProblem;
import book.ch2.VRPProblemFactory;
import book.ch3.rep2.BiObjEA;
import book.ch3.rep2.BiObjectiveIndividual;



/*
 * Neil Urquhart 2020
 * 
 * This example solves the CVRP examples 3 times using Routes, Customer Service and Distance as objectives.
 * 
 */
public class TestWindows {
	

	public static void main(String[] args){
		/*Problem instances from.
		 Augerat, P., Belenguer, J., Benavent, E., Corber´an, A., Naddef, D., Rinaldi, G., 1995.
Computational results with a branch and cut code for the capacitated vehicle routing
problem. Tech. Rep. 949-M, Universit´e Joseph Fourier, Grenoble, France.

		 */

		int len = Integer.parseInt(args[0]);
		
		String[] problems = {/*"A-n32-k5.vrp","A-n33-k5.vrp","A-n33-k6.vrp","A-n34-k5.vrp","A-n36-k5.vrp","A-n37-k5.vrp",
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
				"P-n70-k10.vrp","P-n76-k4.vrp","P-n76-k5.vrp",*/"P-n101-k4.vrp"};


		//Solve each problem 10 times due to the stochastic nature of the EA
		for (String fName : problems)
			
				//for (int x=0; x < 10; x++)
					run("./data/",fName,len);
	}

	private static void run(String path,String probName,int winLen) {
		/*
		 * Solve the instance named in  <probName>
		 */
		CVRPTWProblem myVRP = VRPTWProblemFactory.buildProblem(path, probName,winLen);//Load instance from file
		int dels = myVRP.getSize();
		
		//Solve using the Evolutionary Algorithm 3 times, once for each objective
		
		double routes =0;
		double times = 0;
		double dist = 0;
		double idle =0;
		
		for (int x=0; x < 10; x++) {
			VRPTWEA eaSolve = new VRPTWEA();
			BiObjectiveTWIndividual.setObjective(BiObjectiveTWIndividual.Objective.COST_DEL);

			myVRP.solve(eaSolve);
			routes += myVRP.getVehicles();
			times += myVRP.getTime();
			dist += myVRP.getDistance();
			idle += myVRP.getIdle();
		}
		System.out.print(probName +","+winLen+","+ ",ROUTES,");
		double delsRoute = dels/(routes/10);
		double lenRoute = (dist/10)/dels;
		double timeRoute = (times/10)/dels;
		double idlePC = (idle/10)/(times/10);
		System.out.println((routes/10) +","+(times/10)+","+(dist/10) +","+(idle/10)+","+delsRoute+","+lenRoute+","+timeRoute+","+idlePC);
//		
//		
//		routes =0;
//		times = 0;
//		dist = 0;
//		idle=0;
//		
//		for (int x=0; x < 10; x++) {
//			VRPTWEA eaSolve = new VRPTWEA();
//			BiObjectiveTWIndividual.setObjective(BiObjectiveTWIndividual.Objective.TIME);
//
//			myVRP.solve(eaSolve);
//			routes += myVRP.getVehicles();
//			times += myVRP.getTime();
//			dist += myVRP.getDistance();
//			idle += myVRP.getIdle();
//		}
//		System.out.print(probName +","+winLen+","+ ",TIME,");
//		 delsRoute = dels/(routes/10);
//		lenRoute = (dist/10)/dels;
//		timeRoute = (times/10)/dels;
//		idlePC = (idle/10)/(times/10);
//		System.out.println((routes/10) +","+(times/10)+","+(dist/10) +","+(idle/10)+","+delsRoute+","+lenRoute+","+timeRoute+","+idlePC);
//		
//		routes =0;
//		times = 0;
//		dist = 0;
//		idle =0;
//		
//		for (int x=0; x < 10; x++) {
//			VRPTWEA eaSolve = new VRPTWEA();
//			BiObjectiveTWIndividual.setObjective(BiObjectiveTWIndividual.Objective.DISTANCE);
//
//			myVRP.solve(eaSolve);
//			routes += myVRP.getVehicles();
//			times += myVRP.getTime();
//			dist += myVRP.getDistance();
//			idle += myVRP.getIdle();
//		}
//		System.out.print(probName +","+winLen+","+ ",DIST,");
//		delsRoute = dels/(routes/10);
//		lenRoute = (dist/10)/dels;
//		timeRoute = (times/10)/dels;
//		idlePC = (idle/10)/(times/10);
//		System.out.println((routes/10) +","+(times/10)+","+(dist/10) +","+(idle/10)+","+delsRoute+","+lenRoute+","+timeRoute+","+idlePC);
//		
//		routes =0;
//		times = 0;
//		dist = 0;
//		idle=0;
//		
//		for (int x=0; x < 10; x++) {
//			VRPTWEA eaSolve = new VRPTWEA();
//			BiObjectiveTWIndividual.setObjective(BiObjectiveTWIndividual.Objective.IDLE);
//
//			myVRP.solve(eaSolve);
//			routes += myVRP.getVehicles();
//			times += myVRP.getTime();
//			dist += myVRP.getDistance();
//			idle += myVRP.getIdle();
//		}
//		System.out.print(probName +","+winLen+","+ ",IDLE,");
//		 delsRoute = dels/(routes/10);
//		lenRoute = (dist/10)/dels;
//		 timeRoute = (times/10)/dels;
//		idlePC = (idle/10)/(times/10);
//		System.out.println((routes/10) +","+(times/10)+","+(dist/10) +","+(idle/10)+","+delsRoute+","+lenRoute+","+timeRoute+","+idlePC);
//	
//
//		
		
	}
	
	public static void printSol(CVRPTWProblem myVRP) {
		ArrayList sol = myVRP.getCVRPSolution();
		for (int c=0; c < sol.size(); c++) {
			VRPTWRoute r = (VRPTWRoute)sol.get(c);
			System.out.print(c+1 +"\t" + r.getStart()+"|");
			for (int z=0; z < r.size(); z++) {
				VisitNode v=r.get(z);
				System.out.print("["+ v +"]");
			}
			System.out.println("\t|" + r.getEnd());
		}
		
	}

	public static void checkSol(CVRPTWProblem myVRP) {
		ArrayList sol = myVRP.getCVRPSolution();
		for (int c=0; c < sol.size(); c++) {
			VRPTWRoute r = (VRPTWRoute)sol.get(c);
			VisitNode old=null;;
			for (int z=0; z < r.size(); z++) {
				VisitNode v=r.get(z);
				//System.out.print("["+ v +"]");
				if(old != null) {
					 if (old.getDeliveryTime().compareTo(v.getDeliveryTime())>=0) {
						  System.out.println("error!");
						  System.exit(-1);
						  
					 }
					 
					 		
				}
				old = v;
			}
			
		}
		System.out.println("Done checking");
		
	}

}
