package book.ch4.mapElites;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import book.ch2.RandomSingleton;

import book.ch3.rep2.Domination;
import book.ch4.BiObjectiveTWIndividual;
import book.ch4.CVRPTWProblem;
import book.ch4.NonDomEA;
import book.ch4.NonDominatedPop;
import book.ch4.VRPTWProblemFactory;

/*
 * Copyright Neil Urquhart 2020
 * 
 * A class for testing the MAP-Elites algorithm
 * 
 */



public class TestSeeding {

	public static void main(String[] args){
		/*
		 * Problem instances from:
			Augerat, P., Belenguer, J., Benavent, E., Corber´an, A., Naddef, D., Rinaldi, G., 1995.
			Computational results with a branch and cut code for the capacitated vehicle routing
			problem. Tech. Rep. 949-M, Universit´e Joseph Fourier, Grenoble, France. 


		 */


		String[] problems = {"A-n32-k5","P-n101-k4","P-n55-k15", "A-n33-k5","A-n33-k6","A-n34-k5","A-n36-k5","A-n37-k5",
				"A-n37-k6","A-n38-k5","A-n39-k5","A-n39-k6","A-n44-k7","A-n45-k6","A-n45-k7",
				"A-n46-k7","A-n48-k7","A-n53-k7","A-n54-k7","A-n55-k9","A-n60-k9","A-n61-k9",
				"A-n62-k8","A-n63-k9","A-n63-k10","A-n64-k9","A-n65-k9","A-n69-k9","P-n16-k8","P-n19-k2","P-n20-k2","P-n21-k2","P-n22-k2","P-n22-k8","P-n23-k8",
				"P-n40-k5","P-n45-k5","P-n50-k7","A-n80-k10",
				"B-n31-k5","B-n34-k5","B-n35-k5","B-n38-k6","B-n39-k5","B-n41-k6","B-n43-k6",
				"B-n44-k7","B-n45-k5","B-n45-k6","B-n50-k7","B-n50-k8","B-n51-k7","B-n52-k7",
				"B-n56-k7","B-n57-k7","B-n57-k9","B-n63-k10","B-n64-k9","B-n66-k9","B-n67-k10",
				"B-n68-k9","B-n78-k10",
				"P-n50-k8","P-n50-k10","P-n51-k10","P-n55-k7",
				"P-n55-k8","P-n55-k10","P-n60-k10","P-n60-k15","P-n65-k10",
				"P-n70-k10","P-n76-k4","P-n76-k5"};

		for (String pName : problems){
			int win = 1;//1 hour windows
			String fName  = pName+".vrp";
			String pSizeS = pName.split("-")[1];
			pSizeS = pSizeS.replace("n","");
			
			String pRouteS = pName.split("-")[2];
			pRouteS = pRouteS.replace("k","");
			int pRoute = Integer.parseInt(pRouteS);
			
			
			CVRPTWModalProblem myVRP = new  CVRPTWModalProblem (VRPTWProblemFactory.buildProblem("./data/",fName,win));
			KeyGenerator.setup(new CVRPKeyGen(myVRP));
			ArrayList<Elite> pool = new ArrayList<Elite>();
			
			CVRPKeyGen.setCycles(0, (pRoute *10));
			CVRPKeyGen.setVans(0, (pRoute *2));
			
			CVRPKeyGen.resetHighLows();
			
			//seeding
			for (EliteIndividual.optimiseCharacteristic objective : EliteIndividual.optimiseCharacteristic.values()) { 
				System.out.print("Obj " + objective);
				EliteIndividual.setSingleObjective(objective);
				for (int x=0; x < 10; x++) {
					System.out.print("-"+x);
					SeedingEA seed = new SeedingEA();
					myVRP.solve(seed);
					ArrayList<EliteIndividual> seeds = seed.getFinalPop();//seed.getBest();
					CVRPKeyGen.updateMaxMin(seeds);
					pool.addAll(seeds);
				}
			}
			System.out.println();
			CVRPKeyGen.printMaxMin();
			
			myVRP = new  CVRPTWModalProblem (VRPTWProblemFactory.buildProblem("./data/",fName,win));
			MAPofElites combined = new MAPofElites(KeyGenerator.getInstance().getDimensions(), KeyGenerator.getInstance().getBuckets());
			for (int x=0; x < 10; x++){
				Logger.getLogger().clear();
				MAPElites me = new MAPElites();
				me.setSeeds(pool);
				myVRP.solve(me);
				try {
					combined.addAll(me.getArchive());
				}catch(Exception e) {
					e.printStackTrace();
				}
				exportToCSV("seeded-"+pName+"-"+win+"-"+x+".map.csv",me.getArchive());
				System.out.println("seeded," + pName+"-"+win+"-"+"," +x+","+ stats(me.getArchive()) );
				Logger.getLogger().write("seeded-"+pName+"-"+win+"-"+x+".log");
			}
			System.out.println("COMBINED SEEDED," + pName+"-"+win+","+ combined.getUsed()+"," +stats(combined.getArchive()) +"\n");
			
			combined = new MAPofElites(KeyGenerator.getInstance().getDimensions(), KeyGenerator.getInstance().getBuckets());
	
			CVRPKeyGen.resetHighLows();
			CVRPKeyGen.defaultHighLows(myVRP);
			CVRPKeyGen.printMaxMin();
			for (int x=0; x < 10; x++){
				Logger.getLogger().clear();
				MAPElites me = new MAPElites();
				myVRP.solve(me);
				combined.addAll(me.getArchive());
				exportToCSV("no seed-"+pName+"-"+win+"-"+x+".map.csv",me.getArchive());
				System.out.println("non-seeded," + pName+"-"+win+"-"+"," +x+","+ stats(me.getArchive()) +"\n");
				Logger.getLogger().write("no seed-"+pName+"-"+win+"-"+x+".log");
			}
			System.out.println("COMBINED NON SEEDED," + pName+"-"+win+","+ combined.getUsed()+"," +stats(combined.getArchive()) +"\n");
			
		}
	}

	private static String getBoundaries(ArrayList<EliteIndividual> archive) {//Find the lowest in each feature
		double minfixedCost = Double.MAX_VALUE;
		double mincostDel = Double.MAX_VALUE;
		double minstaffCost = Double.MAX_VALUE;
		double minrunningCost = Double.MAX_VALUE;
		double minemissions = Double.MAX_VALUE;
		double mincycleDels = Double.MAX_VALUE;
		double mincycleDist = Double.MAX_VALUE;
		double mincycles = Double.MAX_VALUE;
		double minvans = Double.MAX_VALUE;


		double maxfixedCost = 0;
		double maxcostDel = 0;
		double maxstaffCost = 0;
		double maxrunningCost = 0;
		double maxemissions = 0;
		double maxcycleDels = 0;
		double maxcycleDist = 0;
		double maxcycles = 0;
		double maxvans = 0;


		for (EliteIndividual e: archive) {

			ModalCostModel m = ModalCostModel.getInstance();

			minfixedCost = updateMin (minfixedCost ,m.getFixedVehCost(e));
			mincostDel = updateMin ( mincostDel, e.getCostDel());
			minstaffCost =updateMin ( minstaffCost, m.getStaffCost(e));
			minrunningCost = updateMin ( minrunningCost, m.getVehRunningCost(e));
			minemissions = updateMin ( minemissions, m.getEmissions(e));
			mincycleDels = updateMin ( mincycleDels, m.getCycleDels(e));
			mincycleDist = updateMin ( mincycleDist, m.getCycleDist(e));
			mincycles = updateMin ( mincycles, m.getCycles(e));
			minvans = updateMin ( minvans, m.getVans(e));

			maxfixedCost = updateMax (maxfixedCost ,m.getFixedVehCost(e));
			maxcostDel = updateMax ( maxcostDel, e.getCostDel());
			maxstaffCost =updateMax ( maxstaffCost, m.getStaffCost(e));
			maxrunningCost = updateMax ( maxrunningCost, m.getVehRunningCost(e));
			maxemissions = updateMax ( maxemissions, m.getEmissions(e));
			maxcycleDels = updateMax ( maxcycleDels, m.getCycleDels(e));
			maxcycleDist = updateMax ( maxcycleDist, m.getCycleDist(e));
			maxcycles = updateMax ( maxcycles, m.getCycles(e));
			maxvans = updateMax ( maxvans, m.getVans(e));


		}
		return ",minfc," + minfixedCost 
				+ ",mincd," + mincostDel 
				+ ",minsc," + minstaffCost 
				+ ",minrc," + minrunningCost
				+ ",minem," + minemissions 
				+ ",mincydels," + mincycleDels 
				+ ",mincydist," + mincycleDist 
				+ ",mincycles," + mincycles 
				+ ",minvans," + minvans
				+ ",maxfc," + maxfixedCost 
				+ ",maxcd," + maxcostDel 
				+ ",maxsc," + maxstaffCost 
				+ ",maxrc," + maxrunningCost
				+ ",maxem," + maxemissions 
				+ ",maxcydels," + maxcycleDels 
				+ ",maxcydist," + maxcycleDist 
				+ ",maxcycles," + maxcycles 
				+ ",maxvans," + maxvans; 
	}

	private static double updateMin(double tot, double newest) {
		if (newest < tot)
			tot = newest;
		return tot;
	}

	private static double updateMax(double tot, double newest) {
		if (newest > tot)
			tot = newest;
		return tot;
	}

	public static void exportToCSV(String fName, ArrayList<Object> archive) {

		try {
			FileWriter csv = new FileWriter(fName);


			csv.write("Key,FixedVehCost,CostperDel,StaffCost,VehRunCost,Emissions,DelsByCycle,DistByCycle,Cycles,Vans\n"); 

			for (Object el : archive) {
				EliteIndividual e = (EliteIndividual) el;
				ModalCostModel m = ModalCostModel.getInstance();
				csv.write(e.keyToString()+",");
				csv.write(m.getFixedVehCost(e)+",");
				csv.write(e.getCostDel()+",");
				csv.write(m.getStaffCost(e)+",");
				csv.write(m.getVehRunningCost(e)+",");
				csv.write(m.getEmissions(e)+",");
				csv.write(m.getCycleDels(e)+",");
				csv.write(m.getCycleDist(e)+",");
				csv.write(m.getCycles(e)+",");
				csv.write(m.getVans(e)+",");

				csv.write("\n");


			}

			csv.close();
		} catch (IOException e) {
			System.out.println("Error writing to csv file");
			e.printStackTrace();
		}
	}

	public static String stats(ArrayList<Object> archive) {
		/*
		 * Return a String that describes the size of the archive
		 * and the best solutions found for each solution attribute
		 */
		double fit =0;
		for (Object o: archive) {
			EliteIndividual e = (EliteIndividual)o;
			fit = fit + e.getFitness();
		}
		fit = fit / archive.size();
		return archive.size()+",avg f,"+fit+"," + getBest(archive);	
	}

	private static String getBest(ArrayList<Object> archive) {//Find the lowest in each feature
		double fixedCost = Double.MAX_VALUE;
		double costDel = Double.MAX_VALUE;
		double staffCost = Double.MAX_VALUE;
		double runningCost = Double.MAX_VALUE;
		double emissions = Double.MAX_VALUE;
		double cycleDels = Double.MAX_VALUE;
		double cycleDist = Double.MAX_VALUE;
		double cycles = Double.MAX_VALUE;
		double vans = Double.MAX_VALUE;


		for (Object o : archive) {

			EliteIndividual e = (EliteIndividual) o;
			ModalCostModel m = ModalCostModel.getInstance();

			fixedCost = update (fixedCost ,m.getFixedVehCost(e));
			costDel = update ( costDel, e.getCostDel());
			staffCost =update ( staffCost, m.getStaffCost(e));
			runningCost = update ( runningCost, m.getVehRunningCost(e));
			emissions = update ( emissions, m.getEmissions(e));
			cycleDels = update ( cycleDels, m.getCycleDels(e));
			cycleDist = update ( cycleDist, m.getCycleDist(e));
			cycles = update ( cycles, m.getCycles(e));
			vans = update ( vans, m.getVans(e));


		}
		return ",fc," + fixedCost 
				+ ",cd," + costDel 
				+ ",sc," + staffCost 
				+ ",rc," + runningCost
				+ ",em," + emissions 
				+ ",cydels," + cycleDels 
				+ ",cydist," + cycleDist 
				+ ",cycles," + cycles 
				+ ",vans," + vans; 
	}

	private static double update(double tot, double newest) {
		if (newest < tot)
			tot = newest;
		return tot;
	}
}
