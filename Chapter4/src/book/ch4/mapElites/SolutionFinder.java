package book.ch4.mapElites;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class SolutionFinder {

	public static void main(String[] args) {

		SolutionFinder sf; 
		
			
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

		
		System.out.println("----------------------");
		sf = new SolutionFinder();
		setA2(sf);
		setA3(sf);
		setC2(sf); 
		search(sf, problems);
		

		System.out.println("----------------------");
		sf = new SolutionFinder();
		setA1(sf);
		search(sf, problems);
		
		System.out.println("----------------------");
		sf = new SolutionFinder();
		setA1(sf);
		setA2(sf);
		setA3(sf);
		search(sf, problems);

		System.out.println("----------------------");
		sf = new SolutionFinder();
		setA1(sf);
		setC3(sf);
		search(sf, problems);
		
		System.out.println("----------------------");
		sf = new SolutionFinder();
		setA1(sf);
		setC2(sf);
		search(sf, problems);

		System.out.println("----------------------");
		sf = new SolutionFinder();
		setA2(sf);
		setC1(sf);
		search(sf, problems);
		
		System.out.println("----------------------");
		sf = new SolutionFinder();
		setA3(sf);
		setC3(sf);
		search(sf, problems);
		
		System.out.println("----------------------");
		sf = new SolutionFinder();
		setA1(sf);
		setA4(sf);
		setC2(sf);
		search(sf, problems);
	
		System.out.println("----------------------");
		sf = new SolutionFinder();
		setC1(sf);
		setA4(sf);
		setC2(sf);
		search(sf, problems);
		
		System.out.println("----------------------");
		sf = new SolutionFinder();
		setA1(sf);
		setA2(sf);
		setC2(sf);
		search(sf, problems);
		
	}

	private static void search(SolutionFinder sf, String[] problems) {
		for (String problem : problems) {
			problem = problem +"-1";
			//Set criterion 
			
			
			
			
			System.out.print("seeded-"+problem +  ",");
			double avg=0;
			for (int p=0; p < 10; p ++ ) {
			ArrayList<String> found= sf.find("seeded-"+problem +"-" +p+ ".map.csv");
				System.out.print(found.size() + ",");
				avg = avg + found.size();
			}
			System.out.print((avg/10)+",");
			System.out.print("no seed-"+problem +  ",");
			avg = 0;
			for (int p=0; p < 10; p ++ ) {
			ArrayList<String> found= sf.find("no seed-"+problem +"-" +p+ ".map.csv");
				System.out.print(found.size() + ",");
				avg = avg + found.size();
			}
			
			System.out.println((avg/10));
		}
	}

	
	
	private static void setC1(SolutionFinder sf) {
		//c2
		System.out.println("C1");
		sf.setCriterion(characteristic.cycles_used, Op.lessThan, 5);
	}

	private static void setC2(SolutionFinder sf) {
		//c2
		System.out.println("C2");
		sf.setCriterion(characteristic.vans_used, Op.lessThan, 3);
	}
	
	private static void setC3(SolutionFinder sf) {
		//c3
		System.out.println("C3");
		sf.setCriterion(characteristic.cycles_used, Op.equals, 0);
	}
	
	private static void setA1(SolutionFinder sf) {
		//a1
		System.out.println("A1");
		sf.setCriterion(characteristic.cost_per_delivery, quantity.very_low);
	}

	private static void setA2(SolutionFinder sf) {
		//a2
		System.out.println("A2");
		sf.setCriterion(characteristic.vehicle_run_cost, quantity.very_low);
		sf.setCriterion(characteristic.staff_cost, quantity.low);
	}
	private static void setA3(SolutionFinder sf) {
		//a3
		System.out.println("A3");
		sf.setCriterion(characteristic.cycle_deliveries, quantity.very_high);
	}
	
	
	private static void setA4(SolutionFinder sf) {
		//a4
		System.out.println("A4");
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
