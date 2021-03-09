package book.ch4.solutionFinder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class SolutionFinder {

	

	
//	public static void main(String[] args) {
//
//		SolutionFinder sf; 
//		
//			
//		String[] problems = {"A-n32-k5", "A-n33-k5","A-n33-k6","A-n34-k5","A-n36-k5","A-n37-k5",
//				"A-n37-k6","A-n38-k5","A-n39-k5","A-n39-k6","A-n44-k7","A-n45-k6","A-n45-k7",
//				"A-n46-k7","A-n48-k7","A-n53-k7","A-n54-k7","A-n55-k9","A-n60-k9","A-n61-k9",
//				"A-n62-k8","A-n63-k9","A-n63-k10","A-n64-k9","A-n65-k9","A-n69-k9","A-n80-k10",
//				
//				"B-n31-k5","B-n34-k5","B-n35-k5","B-n38-k6","B-n39-k5","B-n41-k6","B-n43-k6",
//				"B-n44-k7","B-n45-k5","B-n45-k6","B-n50-k7","B-n50-k8","B-n51-k7","B-n52-k7",
//				"B-n56-k7","B-n57-k7","B-n57-k9","B-n63-k10","B-n64-k9","B-n66-k9","B-n67-k10",
//				"B-n68-k9","B-n78-k10","P-n16-k8",
//				
//				"P-n19-k2","P-n20-k2","P-n21-k2","P-n22-k2","P-n22-k8","P-n23-k8",
//				"P-n40-k5","P-n45-k5","P-n50-k7",
//				"P-n50-k8","P-n50-k10","P-n51-k10","P-n55-k15","P-n55-k7",
//				"P-n55-k8","P-n55-k10","P-n60-k10","P-n60-k15","P-n65-k10",
//				"P-n70-k10","P-n76-k4","P-n76-k5","P-n101-k4"};
//
//
//		
//		sf = new SolutionFinder();
//		System.out.print(",");
//		
//		
////1
////		setA2(sf);
////		setA3(sf);
////		setC2(sf); 
//
//		
////2
////		setA1(sf);
////		setA2(sf);
//
//
////3
////		setA1(sf);
////		setA2(sf);
////		setA3(sf);
//
////4
////		setA1(sf);
////		setC3(sf);
//
////5		
////		setA1(sf);
////		setC2(sf);
//
////6
////		setA2(sf);
////		setC1(sf);
//
////7		
////		setA3(sf);
////		setC3(sf);
//
////8		
////		setA1(sf);
////		setA4(sf);
////		setC2(sf);
//
////9	
////		setC1(sf);
////		setA4(sf);
////		setC2(sf);
//
////10		
////		setA1(sf);
////		setA2(sf);
////		setC2(sf);
//
//		findStats(sf);
////		search(sf, problems);
//	}

	private static DecimalFormat df = new DecimalFormat("0.00");
	private static String[] theconstraints ={"A1","A2","A3","A4","C1","C2","C3"};

	public static void main(String[] args) {
		String[] aproblems = {"A-n32-k5", "A-n33-k5","A-n33-k6","A-n34-k5","A-n36-k5","A-n37-k5",
				"A-n37-k6","A-n38-k5","A-n39-k5","A-n39-k6","A-n44-k7","A-n45-k6","A-n45-k7",
				"A-n46-k7","A-n48-k7","A-n53-k7","A-n54-k7","A-n55-k9","A-n60-k9","A-n61-k9",
				"A-n62-k8","A-n63-k9","A-n63-k10","A-n64-k9","A-n65-k9","A-n69-k9","A-n80-k10"};

		String[] bproblems = {"B-n31-k5","B-n34-k5","B-n35-k5","B-n38-k6","B-n39-k5","B-n41-k6","B-n43-k6",
				"B-n44-k7","B-n45-k5","B-n45-k6","B-n50-k7","B-n50-k8","B-n51-k7","B-n52-k7",
				"B-n56-k7","B-n57-k7","B-n57-k9","B-n63-k10","B-n64-k9","B-n66-k9","B-n67-k10",
				"B-n68-k9","B-n78-k10","P-n16-k8"};
		
		
		String[] pproblems = {"P-n16-k8","P-n19-k2","P-n20-k2","P-n21-k2","P-n22-k2",
				"P-n22-k8","P-n23-k8","P-n40-k5","P-n45-k5","P-n50-k7",
				"P-n50-k8","P-n50-k10","P-n51-k10","P-n55-k15","P-n55-k7",
				"P-n55-k8","P-n55-k10","P-n60-k10","P-n60-k15","P-n65-k10",
				"P-n70-k10","P-n76-k4","P-n76-k5","P-n101-k4"};
		
		ArrayList<String> constraints = new ArrayList<String>();
		
		for ( int x =0; x < theconstraints.length; x++ ) { 
			constraints.add(""+theconstraints[x]);
		   // System.out.println(constraints); 
			evaluate(constraints,aproblems, bproblems, pproblems);
		    //Second level
		    for ( int y =x+1; y < theconstraints.length; y++ ){ 
		    	constraints.add(""+theconstraints[y]);
			    //System.out.println(constraints); 
			    evaluate(constraints,aproblems, bproblems, pproblems);
			    
			    for ( int z =y+1; z < theconstraints.length; z++ ){ 
			    	constraints.add(""+theconstraints[z]);
				    //System.out.println(constraints); 
			    	evaluate(constraints,aproblems, bproblems, pproblems);
				    constraints.remove(constraints.size()-1);
			    }
			    constraints.remove(constraints.size()-1);
		    }
		    constraints.remove(constraints.size()-1);
		}
	//	ArrayList<String> constraints = new ArrayList<String>();
	//	constraints.add("A1");
	//	constraints.add("A2");
	//	evaluate(constraints,aproblems, bproblems, pproblems);
	}

	private static void evaluate(ArrayList<String> constraints, String[] aproblems, String[] bproblems, String[] pproblems) {
		SolutionFinder sf = new SolutionFinder();
		for (String constraint : constraints) {
		//setA1(sf);setA2(sf);setC2(sf);
			if (constraint.equals("A1"))
				setA1(sf);
			if (constraint.equals("A2"))
				setA2(sf);
			if (constraint.equals("A3"))
				setA3(sf);
			if (constraint.equals("A4"))
				setA4(sf);
			if (constraint.equals("C1"))
				setC1(sf);
			if (constraint.equals("C2"))
				setC2(sf);
			if (constraint.equals("C3"))
				setC3(sf);
				
		}
		getSummaryRes(sf, aproblems);
		getSummaryRes(sf, bproblems);
		getSummaryRes(sf, pproblems);
		System.out.println();
	}

	private static void getSummaryRes(SolutionFinder sf, String[] aproblems) {
		double seededavgsize=0;
		double unseededavgsize=0;
		int seededWins=0;
		int unseededWins=0;
		for (String problem : aproblems) {
			problem = problem +"-1";
			//Set criterion 
		
			double  seededfound = 0;
			double unseededfound = 0;
			double bestseededFit = Double.MAX_VALUE;
			double bestunseededFit = Double.MAX_VALUE;
			
			for (int p=0; p < 10; p ++ ) {
				ArrayList<String> seeded = sf.find("./results-vehicle-max/seeded-"+problem +"-" +p+ ".map.csv");
				ArrayList<String> unseeded = sf.find("./results-vehicle-max/no seed-"+problem +"-" +p+ ".map.csv");
				seededfound =  seededfound  +seeded.size();
				unseededfound = unseededfound+unseeded.size();
				for (String item : seeded) {
					String key = item.split(",")[0];
					double t = getFit(key,"./results-vehicle-max/seeded-"+problem +"-" +p+ ".map.csv" );
					if (t< bestseededFit) bestseededFit = t;
				}
				
				for (String item : unseeded) {
					String key = item.split(",")[0];
					double t = getFit(key,"./results-vehicle-max/no seed-"+problem +"-" +p+ ".map.csv" );
					if (t< bestunseededFit) bestunseededFit = t;
				}
					
				
				

			}
			if (bestseededFit < bestunseededFit)
				seededWins++;
			else if (bestseededFit > bestunseededFit)
				unseededWins++;
		
			seededavgsize = seededavgsize + (seededfound/10);
			unseededavgsize = unseededavgsize + (unseededfound/10);
			
		}
		
		seededavgsize = seededavgsize / aproblems.length;
		unseededavgsize = unseededavgsize / aproblems.length;
		
		System.out.print(","+df.format(seededavgsize) +","+ df.format(unseededavgsize)+","+ seededWins+","+unseededWins);
	}
	
	private static void search(SolutionFinder sf, String[] problems) {
		System.out.println("\n, seeded,,not seeded \n,Avg Choice,Best Fitness,Avg Choice,Best Fitness");
		for (String problem : problems) {
			problem = problem +"-1";
			//Set criterion 
			System.out.print(problem +  ",");
			double avg=0;
			double lowestFit=Double.MAX_VALUE;;
			
			for (int p=0; p < 10; p ++ ) {
			ArrayList<String> found= sf.find("./results-vehicle-max/seeded-"+problem +"-" +p+ ".map.csv");
			    for (String sol : found) {
			    	String key = sol.split(",")[0];
			    	double f =getFit(key,"./results-vehicle-max/seeded-"+problem +"-" +p+ ".log");
			    	if (f < lowestFit)
			    		lowestFit= f;
			    }
				avg = avg + found.size();
			}
			System.out.print((avg/10)+",");
			if (lowestFit == Double.MAX_VALUE)
				System.out.print(" - ,");
			else
			  System.out.print(df.format(lowestFit)+",");

			avg = 0;
			lowestFit=Double.MAX_VALUE;;
			for (int p=0; p < 10; p ++ ) {
				ArrayList<String> found= sf.find("./results-vehicle-max/no seed-"+problem +"-" +p+ ".map.csv");
				for (String sol : found) {
					String key = sol.split(",")[0];
					double f = getFit(key,"./results-vehicle-max/no seed-"+problem +"-" +p+ ".log");
					if (f < lowestFit)
						lowestFit= f;
					avg = avg + found.size();
				}
			}
			System.out.print((avg/10)+",");
			if (lowestFit == Double.MAX_VALUE)
				System.out.println(" - ");
			else
			  System.out.println(df.format(lowestFit));
		}
	}
	private static double getFit(String key, String fName) {
		key = key +":";
		double res=0;
		try {
            File f = new File(fName);
            BufferedReader b = new BufferedReader(new FileReader(f));
            String readLine = "";
            while ((readLine = b.readLine()) != null) {
                if (readLine.split(",")[0].contentEquals(key)) {
                	double fit = Double.parseDouble(readLine.split(",")[2]);
                	res = fit;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
		return res;
	}

	private static void setC1(SolutionFinder sf) {
		//c2
		System.out.print("C1 ");
		sf.setCriterion(characteristic.cycles_used, Op.lessThan, 5);
	}

	private static void setC2(SolutionFinder sf) {
		//c2
		System.out.print("C2 ");
		sf.setCriterion(characteristic.vans_used, Op.lessThan, 3);
	}
	
	private static void setC3(SolutionFinder sf) {
		//c3
		System.out.print("C3 ");
		sf.setCriterion(characteristic.cycles_used, Op.equals, 0);
	}
	
	private static void setA1(SolutionFinder sf) {
		//a1
		System.out.print("A1 ");
		sf.setCriterion(characteristic.cost_per_delivery, quantity.very_low);
	}

	private static void setA2(SolutionFinder sf) {
		//a2
		System.out.print("A2 ");
		sf.setCriterion(characteristic.vehicle_run_cost, quantity.very_low);
		sf.setCriterion(characteristic.staff_cost, quantity.low);
	}
	private static void setA3(SolutionFinder sf) {
		//a3
		System.out.print("A3 ");
		
		sf.setCriterion(characteristic.cycle_deliveries, quantity.very_high);
	}
	
	
	private static void setA4(SolutionFinder sf) {
		//a4
		System.out.print("A4 ");
		sf.setCriterion(characteristic.fixed_vehicle_cost, quantity.very_low);
	}
	
	public enum characteristic{
		fixed_vehicle_cost(1),
		cost_per_delivery(2),
		staff_cost(3),
		vehicle_run_cost(4),
		emissions(5),
		cycle_deliveries(6),
		cycle_dist(7),
		cycles_used(8),
		vans_used(9);
		
		private final int col;
		private characteristic(int col) {
			this.col = col;
			
		}
		
	}
	
	public enum Op{
		equals,
		greaterThan,
		lessThan,
	}
		
	public enum quantity{
		very_low(new int[]{0,1}),
		low(new int[] {2,3}),
		medium(new int[] {4,5}),
		high(new int[] {6,7}),
		very_high(new int[] {8,9});
		
		private final int[] values;
		
		private quantity(int[] val) {
			this.values = val;
		}
	}

	public class Constraint {
		private int col;
		private double value;
		private Op op;
		
		public Constraint(characteristic c, Op op, double value) {
			this.col = c.col;
			this.op = op;
			this.value = value;
		}
		
		public boolean meets(String data) {
			String cell = data.split(",")[this.col];
			double cellVal = Double.parseDouble(cell);
			switch(op) {
			case equals:
				if (value == cellVal)
					return true;
				else 
					return false;
		
				
			case greaterThan:
				if (cellVal>value)
					return true;
				else 
					return false
							;
			case lessThan:
				if ( cellVal < value)
					return true;
				else 
					return false;
		
				
			}
			return true;
		}
		
	}
	
	private int[][] template = new int[9][];
	private  ArrayList<Constraint> constraints = new ArrayList<Constraint>();

	
	
	private void setCriterion(characteristic c, quantity q) {
		template[c.ordinal()] = q.values;
	}

	private  void setCriterion(characteristic c, Op op, double value ) {
		constraints.add(new Constraint(c, op,value));
	}
	

	private  ArrayList<String> find(String fName) {
		ArrayList<String> found = new ArrayList<String>();
		
		try {
			File myObj = new File(fName);
			Scanner myReader = new Scanner(myObj);
			myReader.nextLine();//skip header
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				boolean passedConstraints = checkConstraints(data);

				boolean passedKeyCheck = keyCheck(data);
				if (passedConstraints && passedKeyCheck)
					found.add(data);
	


			}
		      myReader.close();
		    } catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
		return found;
	}

	private boolean keyCheck(String data) {
		//Check key constraints
		boolean keyCheck = true;
		String keystr = data.split(",")[0];
		String[] splitkey = keystr.split(":");
		int[] key = new int[splitkey.length];
		for (int k=0; k < splitkey.length; k++) {
			key[k] = Integer.parseInt(splitkey[k]);
		}
		if (!match(key,template))
			keyCheck = false;
		
		return keyCheck;
	}

	private boolean checkConstraints(String data) {
		//Check constraints
		boolean passedConstraints = true;
		for (Constraint c : constraints) {
			if (!c.meets(data))
				passedConstraints = false;
		}
		return passedConstraints;
	}
	
	private static boolean match(int[] key, int[][] template) {
		//check to see if key matches template

		for (int x=0; x < key.length; x++) {
			if (template[x]!= null) {
				boolean element = false;

				for (int y =0; y < template[x].length;y++) {
					if (key[x]==template[x][y]) {
						element = true;
					}
				}
				if (element == false)
					return false;
			}
		}
		return true;

	}


}
