package book.ch4.mapElites;

import java.util.ArrayList;

import book.ch2.CVRPProblem;
import book.ch4.CVRPTWProblem;


public class CVRPKeyGen implements MAPElitesKeyGen {
	/*
	 * Neil Urquhart 2021
	 * An implementation of MAPElitesKeyGen for the Supermarket
	 * home deliveries example
	 * 
	 * 5 buckets over 9 dimensions 
	 */

	private final int buckets = 10;
	@Override
	public  int getDimensions() { return 9; }

	/*
	 * MAX-MIN for each dimension
	 * 
	 */
	private static double MAX_COSTDEL =0;
	private static double MIN_COSTDEL = Double.MAX_VALUE;
	private static double MAX_CYCLEDELS =0;
	private static double MIN_CYCLEDELS = Double.MAX_VALUE;
	private static double MAX_CYCLEDIST =0;
	private static double MIN_CYCLEDIST = Double.MAX_VALUE;
	private static double MAX_EMISSIONS =0;
	private static double MIN_EMISSIONS = Double.MAX_VALUE;
	private static double MAX_FIXEDVEHCOST =0;
	private static double MIN_FIXEDVEHCOST =Double.MAX_VALUE;
	private static double MAX_VEHRUNCOST =0;
	private static double MIN_VEHRUNCOST  = Double.MAX_VALUE;
	private static double MAX_STAFFCOST =0;
	private static double MIN_STAFFCOST  =Double.MAX_VALUE;
	private static int MAX_CYCLES=0;
	private static int MIN_CYCLES= Integer.MAX_VALUE;
	private static int MAX_VANS=0;
	private static int MIN_VANS= Integer.MAX_VALUE;
	
	public static void resetHighLows() {
		MAX_COSTDEL =0;
		MIN_COSTDEL = Double.MAX_VALUE;
		MAX_CYCLEDELS =0;
		MIN_CYCLEDELS = Double.MAX_VALUE;
		MAX_CYCLEDIST =0;
		MIN_CYCLEDIST = Double.MAX_VALUE;
		MAX_EMISSIONS =0;
		MIN_EMISSIONS = Double.MAX_VALUE;
		MAX_FIXEDVEHCOST =0;
		MIN_FIXEDVEHCOST =Double.MAX_VALUE;
		MAX_VEHRUNCOST =0;
		MIN_VEHRUNCOST  = Double.MAX_VALUE;
		MAX_STAFFCOST =0;
		MIN_STAFFCOST  =Double.MAX_VALUE;
		//MAX_CYCLES=0;
		//MIN_CYCLES= Integer.MAX_VALUE;
		//MAX_VANS=0;
		//MIN_VANS= Integer.MAX_VALUE;
	}
	public static void printMaxMin() {
		System.out.println("CostDel " + MIN_COSTDEL + " : " + MAX_COSTDEL);
		System.out.println("CycleDels " + MIN_CYCLEDELS + " : " + MAX_CYCLEDELS);
		System.out.println("CycleDist " + MIN_CYCLEDIST + " : " + MAX_CYCLEDIST);
		System.out.println("Emissions " + MIN_EMISSIONS + " : " + MAX_EMISSIONS);
		System.out.println("Fixed Veh Cost " + MIN_FIXEDVEHCOST + " : " + MAX_FIXEDVEHCOST);
		System.out.println("Veh Run cost " + MIN_VEHRUNCOST + " : " + MAX_VEHRUNCOST);
		System.out.println("Staff Cost " + MIN_STAFFCOST + " : " + MAX_STAFFCOST);
		System.out.println("Cycles " + MIN_CYCLES + " : " + MAX_CYCLES);
		System.out.println("Vans " + MIN_VANS + " : " + MAX_VANS);
		
		
	}
	
    public static void updateMaxMin(ArrayList<EliteIndividual> pool) {
    	for (EliteIndividual e : pool) {
    		updateMaxMin(e);
    	}
    }
	
    public static void setCycles(int  min, int max) {
    	MIN_CYCLES = min;
    	MAX_CYCLES = max;
    }
    
  public static void setVans(int  min, int max) {
    	MIN_VANS = min;
    	MAX_VANS = max;
    }
  
  
	public static void updateMaxMin(EliteIndividual i) {
		if (ModalCostModel.getInstance().getFixedVehCost(i)>MAX_FIXEDVEHCOST)
			MAX_FIXEDVEHCOST = ModalCostModel.getInstance().getFixedVehCost(i);
		if (ModalCostModel.getInstance().getStaffCost(i)>MAX_STAFFCOST)
			MAX_STAFFCOST = ModalCostModel.getInstance().getStaffCost(i);
		if (ModalCostModel.getInstance().getVehRunningCost(i)>MAX_VEHRUNCOST)
			MAX_VEHRUNCOST = ModalCostModel.getInstance().getVehRunningCost(i);
		if (ModalCostModel.getInstance().getEmissions(i)>MAX_EMISSIONS)
			MAX_EMISSIONS = ModalCostModel.getInstance().getEmissions(i);
//		if (ModalCostModel.getInstance().getCycles(i)>MAX_CYCLES)
//			MAX_CYCLES = ModalCostModel.getInstance().getCycles(i);
//		if (ModalCostModel.getInstance().getVans(i)>MAX_VANS)
//			MAX_VANS = ModalCostModel.getInstance().getVans(i);
//		if (i.getCostDel() > MAX_COSTDEL)
//			MAX_COSTDEL = i.getCostDel();
		
		MAX_CYCLEDELS =1;//FIX THIS!!
		MIN_CYCLEDELS =0;
		MAX_CYCLEDIST =1;
		MIN_CYCLEDIST =0;

		if (ModalCostModel.getInstance().getFixedVehCost(i)<MIN_FIXEDVEHCOST)
			MIN_FIXEDVEHCOST = ModalCostModel.getInstance().getFixedVehCost(i);
		if (ModalCostModel.getInstance().getStaffCost(i)<MIN_STAFFCOST)
			MIN_STAFFCOST = ModalCostModel.getInstance().getStaffCost(i);
		if (ModalCostModel.getInstance().getVehRunningCost(i)<MIN_VEHRUNCOST)
			MIN_VEHRUNCOST = ModalCostModel.getInstance().getVehRunningCost(i);
		if (ModalCostModel.getInstance().getEmissions(i)<MIN_EMISSIONS)
			MIN_EMISSIONS = ModalCostModel.getInstance().getEmissions(i);
		if (ModalCostModel.getInstance().getCycles(i)<MIN_CYCLES)
			MIN_CYCLES = ModalCostModel.getInstance().getCycles(i);
		if (ModalCostModel.getInstance().getVans(i)<MIN_VANS)
			MIN_VANS = ModalCostModel.getInstance().getVans(i);
		if (i.getCostDel() < MIN_COSTDEL)
			MIN_COSTDEL = i.getCostDel();
	}


	@Override
	public  int getBuckets() { return buckets;}

	//public CVRPKeyGen() {}

	public CVRPKeyGen(CVRPProblem theProblem) {
		/*
		 * Constructor - set the max and minimum values in each dimension
		 * 
		 */
		EliteIndividual i = null;
//		for (int x =0; x < 10; x++) {
			/* Create 10 random solutions and set the max values based on the
			 * worst value seen so far
			 */

//			if (x ==9)
//				i = new EliteIndividual(theProblem, true);
//			else if (x== 8)
//				i = new EliteIndividual(theProblem, false);
//			else
				i = new EliteIndividual(theProblem);

//			i.evaluate();
//
//			if (ModalCostModel.getInstance().getFixedVehCost(i)>MAX_FIXEDVEHCOST)
//				MAX_FIXEDVEHCOST = ModalCostModel.getInstance().getFixedVehCost(i);
//			if (ModalCostModel.getInstance().getStaffCost(i)>MAX_STAFFCOST)
//				MAX_STAFFCOST = ModalCostModel.getInstance().getStaffCost(i);
//			if (ModalCostModel.getInstance().getVehRunningCost(i)>MAX_VEHRUNCOST)
//				MAX_VEHRUNCOST = ModalCostModel.getInstance().getVehRunningCost(i);
//			if (ModalCostModel.getInstance().getEmissions(i)>MAX_EMISSIONS)
//				MAX_EMISSIONS = ModalCostModel.getInstance().getEmissions(i);
//			if (ModalCostModel.getInstance().getCycles(i)>MAX_CYCLES)
//				MAX_CYCLES = ModalCostModel.getInstance().getCycles(i);
//			if (ModalCostModel.getInstance().getVans(i)>MAX_VANS)
//				MAX_VANS = ModalCostModel.getInstance().getVans(i);
//			if (i.getCostDel() > MAX_COSTDEL)
//				MAX_COSTDEL = i.getCostDel();
//		}
//
//		MAX_CYCLEDELS =1;
//		MIN_CYCLEDELS =0;
//		MAX_CYCLEDIST =1;
//		MIN_CYCLEDIST =0;
//		MIN_EMISSIONS =0;
//		MIN_COSTDEL=0;		
//		MIN_FIXEDVEHCOST = 0;
//		MIN_STAFFCOST	=0;
//		MIN_VEHRUNCOST	=0;
//		MIN_CYCLES =0;
//		MIN_VANS=0;
	}

	public static void defaultHighLows(CVRPProblem theProblem) {
		/*
		 * Constructor - set the max and minimum values in each dimension
		 * 
		 */
		EliteIndividual i = null;
		for (int x =0; x < 10; x++) {
			/* Create 10 random solutions and set the max values based on the
			 * worst value seen so far
			 */

			if (x ==9)
				i = new EliteIndividual(theProblem, true);
			else if (x== 8)
				i = new EliteIndividual(theProblem, false);
			else
				i = new EliteIndividual(theProblem);

			i.evaluate();

			if (ModalCostModel.getInstance().getFixedVehCost(i)>MAX_FIXEDVEHCOST)
				MAX_FIXEDVEHCOST = ModalCostModel.getInstance().getFixedVehCost(i);
			if (ModalCostModel.getInstance().getStaffCost(i)>MAX_STAFFCOST)
				MAX_STAFFCOST = ModalCostModel.getInstance().getStaffCost(i);
			if (ModalCostModel.getInstance().getVehRunningCost(i)>MAX_VEHRUNCOST)
				MAX_VEHRUNCOST = ModalCostModel.getInstance().getVehRunningCost(i);
			if (ModalCostModel.getInstance().getEmissions(i)>MAX_EMISSIONS)
				MAX_EMISSIONS = ModalCostModel.getInstance().getEmissions(i);
			if (ModalCostModel.getInstance().getCycles(i)>MAX_CYCLES)
				MAX_CYCLES = ModalCostModel.getInstance().getCycles(i);
			if (ModalCostModel.getInstance().getVans(i)>MAX_VANS)
				MAX_VANS = ModalCostModel.getInstance().getVans(i);
			if (i.getCostDel() > MAX_COSTDEL)
				MAX_COSTDEL = i.getCostDel();
		}

		MAX_CYCLEDELS =1;
		MIN_CYCLEDELS =0;
		MAX_CYCLEDIST =1;
		MIN_CYCLEDIST =0;
		MIN_EMISSIONS =0;
		MIN_COSTDEL=0;		
		MIN_FIXEDVEHCOST = 0;
		MIN_STAFFCOST	=0;
		MIN_VEHRUNCOST	=0;
		MIN_CYCLES =0;
		MIN_VANS=0;
	}


	@Override
	public int[] getKey(EliteIndividual i) {
		/*
		 * Return the key for the individua <i>
		 */
		int[] key = new int[getDimensions()];
		key[0] = getBucket(ModalCostModel.getInstance().getFixedVehCost(i),MIN_FIXEDVEHCOST,MAX_FIXEDVEHCOST);
		key[1] = getBucket(i.getCostDel(),MIN_COSTDEL,MAX_COSTDEL);
		key[2] = getBucket(ModalCostModel.getInstance().getStaffCost(i),MIN_STAFFCOST,MAX_STAFFCOST);
		key[3] = getBucket(ModalCostModel.getInstance().getVehRunningCost(i),MIN_VEHRUNCOST, MAX_VEHRUNCOST);
		key[4] = getBucket(ModalCostModel.getInstance().getEmissions(i),MIN_EMISSIONS, MAX_EMISSIONS);
		key[5] = getBucket(ModalCostModel.getInstance().getCycleDels(i),MIN_CYCLEDELS, MAX_CYCLEDELS);
		key[6] = getBucket(ModalCostModel.getInstance().getCycleDist(i),MIN_CYCLEDIST, MAX_CYCLEDIST);
		key[7] = getBucket(ModalCostModel.getInstance().getCycles(i),MIN_CYCLES, MAX_CYCLES);
		key[8] = getBucket(ModalCostModel.getInstance().getVans(i),MIN_VANS, MAX_VANS);
		return key;
	}
	private  int getBucket(double actual, double min, double max) {
		/*
		 * Calculate the bucket for a value <actual> in a dimension of the range <min> to <max>
		 */
		double delta = max - min;
		double period = delta/buckets;
		int bucket = (int)((actual-min) / period);
		//What if actual is higher than max, in that case bucket should be retained at the highest value
		if (bucket >= buckets)
			bucket = buckets-1;
		//What if actual is lower than min, in that case bucket should be retained at the lowest value
		if (bucket < 0)
			bucket = 0;

		//bucket--;//EXP
		return bucket;
	}


}
