package book.ch4.mapElites;

import java.util.ArrayList;

import book.ch2.CVRPProblem;
import book.ch4.CVRPTWProblem;


public class SmdpKeyGenerator implements MAPElitesKeyGen {
	/*
	 * Neil Urquhart 2021
	 * An implementation of MAPElitesKeyGen for the Supermarket home deliveries example
	 * 
	 * 5 buckets over 9 dimensions 
	 */

	private final int buckets = 10;
	private final int dimensions = 9;

	/*
	 * store the range for each dimension
	 */
	private  double MAX_COSTDEL =0;
	private  double MIN_COSTDEL = Double.MAX_VALUE;
	private  double MAX_CYCLEDELS =0;
	private  double MIN_CYCLEDELS = Double.MAX_VALUE;
	private  double MAX_CYCLEDIST =0;
	private  double MIN_CYCLEDIST = Double.MAX_VALUE;
	private  double MAX_EMISSIONS =0;
	private  double MIN_EMISSIONS = Double.MAX_VALUE;
	private  double MAX_FIXEDVEHCOST =0;
	private  double MIN_FIXEDVEHCOST =Double.MAX_VALUE;
	private  double MAX_VEHRUNCOST =0;
	private  double MIN_VEHRUNCOST  = Double.MAX_VALUE;
	private  double MAX_STAFFCOST =0;
	private  double MIN_STAFFCOST  =Double.MAX_VALUE;
	private  int MAX_CYCLES=0;
	private  int MIN_CYCLES= Integer.MAX_VALUE;
	private  int MAX_VANS=0;
	private  int MIN_VANS= Integer.MAX_VALUE;

	@Override
	public  int getDimensions() { 
		return dimensions; 
	}

	public void resetRanges() {
		/*
		 * Reset the dimensions to their default values
		 */
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
	}

	public void displayRanges() {
		//Display the max-min vales for each dimension
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

	public  void updateRanges(ArrayList<SupermarketSolution> pool) {
		//Update the maximum and minimum values for each dimension.
		//based on the EliteIndividuals in the <pool>

		for (SupermarketSolution e : pool) {
			updateRanges(e);
		}
	}

	public void updateRanges(SupermarketSolution i) {
		/*
		 * Update the ranges based on the values found in the solution<i> if the
		 * value is outwith the current range
		 */
		if (SupermarketCostModel.getInstance().getFixedVehCost(i)>MAX_FIXEDVEHCOST)
			MAX_FIXEDVEHCOST = SupermarketCostModel.getInstance().getFixedVehCost(i);
		if (SupermarketCostModel.getInstance().getStaffCost(i)>MAX_STAFFCOST)
			MAX_STAFFCOST = SupermarketCostModel.getInstance().getStaffCost(i);
		if (SupermarketCostModel.getInstance().getVehRunningCost(i)>MAX_VEHRUNCOST)
			MAX_VEHRUNCOST = SupermarketCostModel.getInstance().getVehRunningCost(i);
		if (SupermarketCostModel.getInstance().getEmissions(i)>MAX_EMISSIONS)
			MAX_EMISSIONS = SupermarketCostModel.getInstance().getEmissions(i);
		if (i.getCostDel() > MAX_COSTDEL)
			MAX_COSTDEL = i.getCostDel();
		if (SupermarketCostModel.getInstance().getFixedVehCost(i)<MIN_FIXEDVEHCOST)
			MIN_FIXEDVEHCOST = SupermarketCostModel.getInstance().getFixedVehCost(i);
		if (SupermarketCostModel.getInstance().getStaffCost(i)<MIN_STAFFCOST)
			MIN_STAFFCOST = SupermarketCostModel.getInstance().getStaffCost(i);
		if (SupermarketCostModel.getInstance().getVehRunningCost(i)<MIN_VEHRUNCOST)
			MIN_VEHRUNCOST = SupermarketCostModel.getInstance().getVehRunningCost(i);
		if (SupermarketCostModel.getInstance().getEmissions(i)<MIN_EMISSIONS)
			MIN_EMISSIONS = SupermarketCostModel.getInstance().getEmissions(i);
		if (SupermarketCostModel.getInstance().getCycles(i)<MIN_CYCLES)
			MIN_CYCLES = SupermarketCostModel.getInstance().getCycles(i);
		if (SupermarketCostModel.getInstance().getVans(i)<MIN_VANS)
			MIN_VANS = SupermarketCostModel.getInstance().getVans(i);
		if (i.getCostDel() < MIN_COSTDEL)
			MIN_COSTDEL = i.getCostDel();
	}

	public void setCycleRange(int  min, int max) {
		//Set the range for cycles
		MIN_CYCLES = min;
		MAX_CYCLES = max;
	}

	public void setVanRange(int  min, int max) {
		//Set the range for vans
		MIN_VANS = min;
		MAX_VANS = max;
	}

	@Override
	public  int getBuckets() { 
		return buckets;
	}

	public void defaultRanges(CVRPProblem theProblem) {
		/*
		 * Set the default ranges based on random solutuons for the max
		 * and 0s for the min
		 * 
		 */
		SupermarketSolution i = null;
		for (int x =0; x < 10; x++) {
			/* Create 10 random solutions and set the max values based on the
			 * worst value seen so far
			 */

			if (x ==9)
				i = new SupermarketSolution(theProblem, true);
			else if (x== 8)
				i = new SupermarketSolution(theProblem, false);
			else
				i = new SupermarketSolution(theProblem);

			i.evaluate();

			if (SupermarketCostModel.getInstance().getFixedVehCost(i)>MAX_FIXEDVEHCOST)
				MAX_FIXEDVEHCOST = SupermarketCostModel.getInstance().getFixedVehCost(i);
			if (SupermarketCostModel.getInstance().getStaffCost(i)>MAX_STAFFCOST)
				MAX_STAFFCOST = SupermarketCostModel.getInstance().getStaffCost(i);
			if (SupermarketCostModel.getInstance().getVehRunningCost(i)>MAX_VEHRUNCOST)
				MAX_VEHRUNCOST = SupermarketCostModel.getInstance().getVehRunningCost(i);
			if (SupermarketCostModel.getInstance().getEmissions(i)>MAX_EMISSIONS)
				MAX_EMISSIONS = SupermarketCostModel.getInstance().getEmissions(i);
			if (SupermarketCostModel.getInstance().getCycles(i)>MAX_CYCLES)
				MAX_CYCLES = SupermarketCostModel.getInstance().getCycles(i);
			if (SupermarketCostModel.getInstance().getVans(i)>MAX_VANS)
				MAX_VANS = SupermarketCostModel.getInstance().getVans(i);
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
	public int[] getKey(SupermarketSolution i) {
		/*
		 * Return the key for the individua <i>
		 */
		int[] key = new int[getDimensions()];
		key[0] = getBucket(SupermarketCostModel.getInstance().getFixedVehCost(i),MIN_FIXEDVEHCOST,MAX_FIXEDVEHCOST);
		key[1] = getBucket(i.getCostDel(),MIN_COSTDEL,MAX_COSTDEL);
		key[2] = getBucket(SupermarketCostModel.getInstance().getStaffCost(i),MIN_STAFFCOST,MAX_STAFFCOST);
		key[3] = getBucket(SupermarketCostModel.getInstance().getVehRunningCost(i),MIN_VEHRUNCOST, MAX_VEHRUNCOST);
		key[4] = getBucket(SupermarketCostModel.getInstance().getEmissions(i),MIN_EMISSIONS, MAX_EMISSIONS);
		key[5] = getBucket(SupermarketCostModel.getInstance().getCycleDels(i),MIN_CYCLEDELS, MAX_CYCLEDELS);
		key[6] = getBucket(SupermarketCostModel.getInstance().getCycleDist(i),MIN_CYCLEDIST, MAX_CYCLEDIST);
		key[7] = getBucket(SupermarketCostModel.getInstance().getCycles(i),MIN_CYCLES, MAX_CYCLES);
		key[8] = getBucket(SupermarketCostModel.getInstance().getVans(i),MIN_VANS, MAX_VANS);
		return key;
	}
	
	private int getBucket(double actual, double min, double max) {
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

		return bucket;
	}
}
