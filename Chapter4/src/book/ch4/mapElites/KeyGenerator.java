package book.ch4.mapElites;

import book.ch2.CVRPProblem;
import book.ch4.CVRPTWProblem;


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
	
	private final int buckets = 5;
//
//	private  double MAX_COST =0;
//	private  double MIN_COST =0;
	
	private  double MAX_COSTDEL =0;
	private  double MIN_COSTDEL =0;
	
	private  double MAX_CYCLEDELS =0;
	private  double MIN_CYCLEDELS =0;
	
	private  double MAX_CYCLEDIST =0;
	private  double MIN_CYCLEDIST =0;
	
	private  double MAX_EMISSIONS =0;
	private  double MIN_EMISSIONS =0;
	
//	private  double MAX_IDLE =0;
//	private  double MIN_IDLE =0;
	
	private  double MAX_FIXEDVEHCOST =0;
	private  double MIN_FIXEDVEHCOST  =0;
	
//	private  double MAX_VEHCOST =0;
//	private  double MIN_VEHCOST  =0;
	
	private  double MAX_VEHRUNCOST =0;
	private  double MIN_VEHRUNCOST  =0;
	
	
	private  double MAX_STAFFCOST =0;
	private  double MIN_STAFFCOST  =0;
	
	private int MAX_CYCLES=0;
	private int MIN_CYCLES=0;

	private int MAX_VANS=0;
	private int MIN_VANS=0;

	
	public int getDimensions() { return 9; }
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
			
			i.evaluate();
			
			if (ModalCostModel.getInstance().getFixedVehCost(i)>MAX_FIXEDVEHCOST)
				MAX_FIXEDVEHCOST = ModalCostModel.getInstance().getFixedVehCost(i);
			
//			if (CycleCostModel.getInstance().getVehicleCost(i)>MAX_VEHCOST)
//				MAX_VEHCOST = CycleCostModel.getInstance().getVehicleCost(i);
			
			
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
			
		
			
//			if (i.getCost() > MAX_COST)
//				MAX_COST = i.getCost();
//			
			if (i.getCostDel() > MAX_COSTDEL)
				MAX_COSTDEL = i.getCostDel();
			
//			if (i.getIdle() > MAX_IDLE)
//				MAX_IDLE = i.getIdle();
//			
//			
//			if (i.getRoutes()> MAX_ROUTES)
//				MAX_ROUTES = i.getRoutes();
//
//			if (i.getTime() > MAX_TIME)
//				MAX_TIME = i.getTime();
		
		//	System.out.println(MAX_COST + "\t"+ MAX_COSTDEL+ "\t"+ MAX_IDLE + "\t"+ MAX_ +"\t"+ MAX_TIME);
		}
		
		MAX_CYCLEDELS =1;
		MIN_CYCLEDELS =0;
		
		MAX_CYCLEDIST =1;
		MIN_CYCLEDIST =0;
		
		MIN_EMISSIONS =0;
		//MIN_COST=(MAX_COST*0.5);
		MIN_COSTDEL=0;//(MAX_COSTDEL*0.5);
	//	MIN_IDLE=0;//(MAX_IDLE*0.5);
	//	MIN_TIME=(MAX_TIME*0.5);
	//	MIN_ROUTES =1;//(MIN_ROUTES*0.5);
		
		MIN_FIXEDVEHCOST = 0;//(MAX_FIXEDVEHCOST*0.25 );
		//MIN_VEHCOST =	(MAX_VEHCOST *0.5 );
		MIN_STAFFCOST	=0;//(MAX_STAFFCOST *0.25 );
		MIN_VEHRUNCOST	=0;//(MAX_VEHRUNCOST *0.25 );
		MIN_CYCLES =0;
		MIN_VANS=0;

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
}
