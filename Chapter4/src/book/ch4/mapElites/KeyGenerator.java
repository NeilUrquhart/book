package book.ch4.mapElites;

import book.ch2.CVRPProblem;
import book.ch4.CVRPTWProblem;
import book.ch4.FabFoodCostModel;

public class KeyGenerator {
	/*
	 *Singleton code 
	 */
	
	private KeyGenerator() { }
	private static KeyGenerator instance = null;
	
	public static KeyGenerator getInstance(CVRPProblem theProblem) {
		if (instance == null) {
			instance = new KeyGenerator();
			instance.setup(theProblem);
		}
		return instance;
	}
	
	public static KeyGenerator getInstance() {
	
		return instance;
	}
	
	/*
	 * MAX-MINS
	 * 
	 */
	
	private final int buckets = 10;
//
//	private  double MAX_COST =0;
//	private  double MIN_COST =0;
	
	private  double MAX_COSTDEL =0;
	private  double MIN_COSTDEL =0;
	

	
	private  double MAX_IDLE =0;
	private  double MIN_IDLE =0;
	
	private  double MAX_FIXEDVEHCOST =0;
	private  double MIN_FIXEDVEHCOST  =0;
	
//	private  double MAX_VEHCOST =0;
//	private  double MIN_VEHCOST  =0;
	
	private  double MAX_VEHRUNCOST =0;
	private  double MIN_VEHRUNCOST  =0;
	
	
	private  double MAX_STAFFCOST =0;
	private  double MIN_STAFFCOST  =0;
	
	public int getDimensions() { return 5; }
	public int getBuckets() { return buckets;}
	
	
	
	private  void setup(CVRPProblem theProblem) {
		
		EliteIndividual i = null;
		
		for (int x =0; x < 10; x++) {
			if (x ==9)
				i = new EliteIndividual(theProblem, true);
			else if (x== 8)
				i = new EliteIndividual(theProblem, false);
			else
				i = new EliteIndividual(theProblem);
			
			
			if (FabFoodCostModel.getInstance().getFixedVehCost(i)>MAX_FIXEDVEHCOST)
				MAX_FIXEDVEHCOST = FabFoodCostModel.getInstance().getFixedVehCost(i);
			
//			if (FabFoodCostModel.getInstance().getVehicleCost(i)>MAX_VEHCOST)
//				MAX_VEHCOST = FabFoodCostModel.getInstance().getVehicleCost(i);
			
			
			if (FabFoodCostModel.getInstance().getStaffCost(i)>MAX_STAFFCOST)
				MAX_STAFFCOST = FabFoodCostModel.getInstance().getStaffCost(i);
		
			if (FabFoodCostModel.getInstance().getVehRunningCost(i)>MAX_VEHRUNCOST)
				MAX_VEHRUNCOST = FabFoodCostModel.getInstance().getVehRunningCost(i);
					

//			if (i.getCost() > MAX_COST)
//				MAX_COST = i.getCost();
//			
			if (i.getCostDel() > MAX_COSTDEL)
				MAX_COSTDEL = i.getCostDel();
			
			if (i.getIdle() > MAX_IDLE)
				MAX_IDLE = i.getIdle();
			
//			if (i.getRoutes()> MAX_ROUTES)
//				MAX_ROUTES = i.getRoutes();
//
//			if (i.getTime() > MAX_TIME)
//				MAX_TIME = i.getTime();
		
		//	System.out.println(MAX_COST + "\t"+ MAX_COSTDEL+ "\t"+ MAX_IDLE + "\t"+ MAX_ +"\t"+ MAX_TIME);
		}
		
		//MIN_COST=(MAX_COST*0.5);
		MIN_COSTDEL=0;//(MAX_COSTDEL*0.5);
		MIN_IDLE=0;//(MAX_IDLE*0.5);
	//	MIN_TIME=(MAX_TIME*0.5);
	//	MIN_ROUTES =1;//(MIN_ROUTES*0.5);
		
		MIN_FIXEDVEHCOST = (MAX_FIXEDVEHCOST*0.5 );
		//MIN_VEHCOST =	(MAX_VEHCOST *0.5 );
		MIN_STAFFCOST	=(MAX_STAFFCOST *0.5 );
		MIN_VEHRUNCOST	=(MAX_VEHRUNCOST *0.5 );


	}
	
	private  int getBucket(double actual, double min, double max) {
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
	
	public int[] getKey(EliteIndividual i) {
		
	
		int[] key = new int[getDimensions()];
		key[0] = getBucket(FabFoodCostModel.getInstance().getFixedVehCost(i),MIN_FIXEDVEHCOST,MAX_FIXEDVEHCOST);
		key[1] = getBucket(i.getCostDel(),MIN_COSTDEL,MAX_COSTDEL);
		key[2] = getBucket(i.getIdle(),MIN_IDLE,MAX_IDLE);
		key[3] = getBucket(FabFoodCostModel.getInstance().getStaffCost(i),MIN_STAFFCOST,MAX_STAFFCOST);
		key[4] = getBucket(FabFoodCostModel.getInstance().getVehRunningCost(i),MIN_VEHRUNCOST, MAX_VEHRUNCOST);
		
//		key[0] = getBucket(FabFoodCostModel.getInstance().getFixedVehCost(i),MIN_FIXEDVEHCOST,MAX_FIXEDVEHCOST);
//		key[1] = getBucket(i.getCost(),MIN_COST,MAX_COST);
//		key[2] = getBucket(i.getCostDel(),MIN_COSTDEL,MAX_COSTDEL);
//		key[3] = getBucket(FabFoodCostModel.getInstance().getVehicleCost(i),MIN_VEHCOST,MAX_VEHCOST);
//		key[4] = getBucket(i.getIdle(),MIN_IDLE,MAX_IDLE);
//		key[5] = getBucket(FabFoodCostModel.getInstance().getStaffCost(i),MIN_STAFFCOST,MAX_STAFFCOST);
//		key[6] = getBucket(FabFoodCostModel.getInstance().getVehRunningCost(i),MIN_VEHRUNCOST, MAX_VEHRUNCOST);
		
		return key;
	}
}
