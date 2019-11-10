package book.ch2;

import book.ch1.NearestNTSPSolver;
import book.ch1.TSPProblem;
import book.ch1.Visit;

public class AppTest {

	public static void main(String[] args){
		/*Problem instances from...*/

		String[] problems = {"A-n32-k5.vrp",
				"A-n33-k5.vrp",
				"A-n33-k6.vrp",
				"A-n34-k5.vrp",
				"A-n36-k5.vrp",
				"A-n37-k5.vrp",
				"A-n37-k6.vrp",
				"A-n38-k5.vrp",
				"A-n39-k5.vrp",
				"A-n39-k6.vrp",
				"A-n44-k7.vrp",
				"A-n45-k6.vrp",
				"A-n45-k7.vrp",
				"A-n46-k7.vrp",
				"A-n48-k7.vrp",
				"A-n53-k7.vrp",
				"A-n54-k7.vrp",
				"A-n55-k9.vrp",
				"A-n60-k9.vrp",
				"A-n61-k9.vrp",
				"A-n62-k8.vrp",
				"A-n63-k9.vrp",
				"A-n63-k10.vrp",
				"A-n64-k9.vrp",
				"A-n65-k9.vrp",
				"A-n69-k9.vrp",
				"A-n80-k10.vrp",
				"B-n31-k5.vrp",
				"B-n34-k5.vrp",
				"B-n35-k5.vrp",
				"B-n38-k6.vrp",
				"B-n39-k5.vrp",
				"B-n41-k6.vrp",
				"B-n43-k6.vrp",
				"B-n44-k7.vrp",
				"B-n45-k5.vrp",
				"B-n45-k6.vrp",
				"B-n50-k7.vrp",
				"B-n50-k8.vrp",
				"B-n51-k7.vrp",
				"B-n52-k7.vrp",
				"B-n56-k7.vrp",
				"B-n57-k7.vrp",
				"B-n57-k9.vrp",
				"B-n63-k10.vrp",
				"B-n64-k9.vrp",
				"B-n66-k9.vrp",
				"B-n67-k10.vrp",
				"B-n68-k9.vrp",
				"B-n78-k10.vrp",
				"P-n16-k8.vrp",
				"P-n19-k2.vrp",
				"P-n20-k2.vrp",
				"P-n21-k2.vrp",
				"P-n22-k2.vrp",
				"P-n22-k8.vrp",
				"P-n23-k8.vrp",
				"P-n40-k5.vrp",
				"P-n45-k5.vrp",
				"P-n50-k7.vrp",
				"P-n50-k8.vrp",
				"P-n50-k10.vrp",
				"P-n51-k10.vrp",
				"P-n55-k7.vrp",
				"P-n55-k8.vrp",
				"P-n55-k10.vrp",
				"P-n55-k15.vrp",
				"P-n60-k10.vrp",
				"P-n60-k15.vrp",
				"P-n65-k10.vrp",
				"P-n70-k10.vrp",
				"P-n76-k4.vrp",
				"P-n76-k5.vrp",
		"P-n101-k4.vrp"};

		System.out.println("Ch2 tests");
		for (String fName : problems)
			run("./data/"+ fName);


	}

	private static void run(String probName) {
		VRPProblem myVRP = VRPProblemFactory.buildProblem(probName);
		System.out.print(probName + ",");

		//Grand Tour
		double startTime = System.currentTimeMillis();
		NearestNTSPSolver nn = new NearestNTSPSolver();
		((TSPProblem)myVRP).solve(nn);
		GrandTour gt = new GrandTour();
		myVRP.solve(gt);
		System.out.print("GTDist," + myVRP.getDistance());		
		System.out.print(",GTVehicles," + myVRP.getVehicles());
		double elapsedTime = System.currentTimeMillis() - startTime;
		System.out.print(",GTTime," + elapsedTime);


		//ClarkeWright
		startTime = System.currentTimeMillis();
		ClarkeWright cwSolve = new ClarkeWright();
		myVRP.solve(cwSolve);
		System.out.print(",CWDist," + myVRP.getDistance());		
		System.out.print(",CWVehicles," + myVRP.getVehicles());
		elapsedTime = System.currentTimeMillis() - startTime;
		System.out.print(",CWTime," + elapsedTime);


		//HillClimb
		//Repeat 10 times and avg

		double bestDist = Double.MAX_VALUE;
		int bestVehicles = Integer.MAX_VALUE;
		double totDist=0;
		double totVehicles =0;
		startTime = System.currentTimeMillis();
		for (int count = 0; count < 10; count ++){
			RandomSingleton rnd = RandomSingleton.getInstance();
			rnd.setSeed(count);
			System.out.print(",rndSeed," + count);		

			HillClimber hcSolve = new HillClimber();
			myVRP.solve(hcSolve);
			System.out.print(",HCDist," + myVRP.getDistance());		
			totDist = totDist + myVRP.getDistance();
			if (myVRP.getDistance()<bestDist) bestDist = myVRP.getDistance();
			System.out.print(",HCVehicles," + myVRP.getVehicles());
			totVehicles = totVehicles + myVRP.getVehicles();
			if (myVRP.getVehicles()< bestVehicles) bestVehicles = myVRP.getVehicles();

		}
		double avgDist = totDist/10;
		double avgVehicles = totVehicles/10;

		System.out.print(",AVGHCDist," + avgDist);		
		System.out.print(",AVGHCVehicles," + avgVehicles);
		System.out.print(",BestHCDist," + bestDist);		
		System.out.print(",BestHCVehicles," + bestVehicles);
		elapsedTime = System.currentTimeMillis() - startTime;
		System.out.print(",HCAvgTime," + (elapsedTime/10));


		//EA
		startTime = System.currentTimeMillis();

		//Repeat 10 times and avg
		bestDist = Double.MAX_VALUE;
		bestVehicles = Integer.MAX_VALUE;
		totDist=0;
		totVehicles =0;
		for (int count = 0; count < 10; count ++){
			RandomSingleton rnd = RandomSingleton.getInstance();
			rnd.setSeed(count);	
			System.out.print(",rndSeed," + count);		
			EvolAlg eaSolve = new EvolAlg();
			myVRP.solve(eaSolve);
			System.out.print(",EADist," + myVRP.getDistance());		
			totDist = totDist + myVRP.getDistance();
			if (myVRP.getDistance()<bestDist) bestDist = myVRP.getDistance();
			System.out.print(",EAVehicles," + myVRP.getVehicles());
			totVehicles = totVehicles + myVRP.getVehicles();
			if (myVRP.getVehicles()< bestVehicles) bestVehicles = myVRP.getVehicles();

		}
		avgDist = totDist/10;
		avgVehicles = totVehicles/10;

		System.out.print(",AVGEADist," + avgDist);		
		System.out.print(",AVGEAVehicles," + avgVehicles);
		System.out.print(",BestEADist," + bestDist);		
		System.out.print(",BestEAVehicles," + bestVehicles);
		elapsedTime = System.currentTimeMillis() - startTime;
		System.out.print(",HCAvgTime," + (elapsedTime/10));

		System.out.println();
	}
}
