package book.ch4.mapElites;

import book.ch4.BiObjectiveTWIndividual;
import book.ch4.FabFoodCostModel;
import book.ch4.VRPTWRoute;

public class ModalCostModel {
	
	//Return costs for VAN and CYCLE
	
	public  enum Mode{//Defines the modes that may be fixed in the Genes 
		VAN, 	
		CYCLE
	}

	private double[] fixed_cost = new double[Mode.values().length];
	private double[] run_cost = new double[Mode.values().length];
	private double[] staff_hour  = new double[Mode.values().length];
	private int[] capacity= new int[Mode.values().length];
	private double[] speed  = new double[Mode.values().length];
	private double[] emissions  = new double[Mode.values().length];
	private int[] turnaround  = new int[Mode.values().length];
	
	
	

	//Singleton code
	private ModalCostModel() {
		fixed_cost[Mode.VAN.ordinal()] = 164;
		run_cost[Mode.VAN.ordinal()] = 0.117;
		staff_hour[Mode.VAN.ordinal()] = 12;
		capacity[Mode.VAN.ordinal()] = 100;
		//Assume a speed of 40kmh = calculate as minutes per km i.e. 0.667 kms per min
		speed[Mode.VAN.ordinal()] = 10;//0.667;	
		emissions[Mode.VAN.ordinal()] = 158.4;   //https://www.eea.europa.eu/highlights/average-co2-emissions-from-new-cars-vans-2019
		turnaround[Mode.VAN.ordinal()] =30; //reload
		fixed_cost[Mode.CYCLE.ordinal()] = 16;
		run_cost[Mode.CYCLE.ordinal()] = 0.01;
		staff_hour[Mode.CYCLE.ordinal()] = 12;
		capacity[Mode.CYCLE.ordinal()] = 20;
		//Assume a speed of 10kmh = calculate as minutes per km i.e. 0.1667 kms per min
		speed[Mode.CYCLE.ordinal()] = 1;//0.1667;	
		emissions[Mode.CYCLE.ordinal()] = 0;
		turnaround[Mode.CYCLE.ordinal()] =15;
						
	}
	private static  ModalCostModel instance=null;
	
	public static ModalCostModel getInstance() {
		if (instance == null)
			instance = new ModalCostModel();
		return instance;
	}
	//Done Singleton
	
	public int getCapacity(Mode m) {
		return capacity[m.ordinal()];
	}
	

	public int getCycles(BiObjectiveTWIndividual i) {
		int cycles=0;
		for (VRPTWRoute t : i.getPhenotype()) {
			VRPTWModalRoute r = (VRPTWModalRoute) t;
			if (r.getMode()==Mode.CYCLE)
				cycles++;
			
		}
		return cycles;
	}

	public int getVans(BiObjectiveTWIndividual i) {
		int vans=0;
		for (VRPTWRoute t : i.getPhenotype()) {
			VRPTWModalRoute r = (VRPTWModalRoute) t;
			if (r.getMode()==Mode.VAN)
				vans++;
			
		}
		return vans;
	}
	
	public double getStaffCost(BiObjectiveTWIndividual i) {
		double cost =0;
		for (VRPTWRoute t : i.getPhenotype()) {
			VRPTWModalRoute r = (VRPTWModalRoute) t;
			cost = cost + r.getTime() *(staff_hour[r.getMode().ordinal()]/60);
		}
		return cost;
		//	double perMin = STAFF_HOUR/60;
		//return (i.getTime()* perMin); 
	}
	
	
	public double getFixedVehCost(BiObjectiveTWIndividual i) {
		double cost =0;
		for (VRPTWRoute t : i.getPhenotype()) {
			VRPTWModalRoute r = (VRPTWModalRoute) t;
			cost = cost + fixed_cost[r.getMode().ordinal()];
		}
		return cost;
		//return i.getRoutes() * VEH_FIXED;
	}
	
	public double getVehRunningCost(BiObjectiveTWIndividual i) {
		double cost =0;
		for (VRPTWRoute t : i.getPhenotype()) {
			VRPTWModalRoute r = (VRPTWModalRoute) t;
			cost = cost + r.getDist() *(run_cost[r.getMode().ordinal()]);
		}
		return cost;
		//return i.getDistance()*VEH_RUNNING;
	}
	
	
	public double getSpeed(Mode m) {
		return speed[m.ordinal()];
	}
	
	public int getTurnaround(Mode m) {
		return turnaround[m.ordinal()];
	}
	
	public double getEmissions(BiObjectiveTWIndividual i) {
		double em =0;
		for (VRPTWRoute t : i.getPhenotype()) {
			VRPTWModalRoute r = (VRPTWModalRoute) t;
			em = em+ r.getDist() *(emissions[r.getMode().ordinal()]);
		}
		return em;
		
	}
	
	public double getCycleDels(BiObjectiveTWIndividual i) {
		//Return % of deliveries made by cycle
		double totDels=0;
		double cycleDels=0;
		
		for (VRPTWRoute t : i.getPhenotype()) {
			VRPTWModalRoute r = (VRPTWModalRoute) t;
			totDels = totDels + r.demand();
			if (r.getMode()==Mode.CYCLE)
				cycleDels = cycleDels + r.demand();
		}
		return cycleDels / totDels;
	}
	
	
	public double getCycleDist(BiObjectiveTWIndividual i) {
		double totD=0;
		double cycleD=0;
		//Return % of dist  by cycle
		
		for (VRPTWRoute t : i.getPhenotype()) {
			VRPTWModalRoute r = (VRPTWModalRoute) t;
			totD = totD + r.getDist();
			if (r.getMode()==Mode.CYCLE)
				cycleD = cycleD + r.getDist();
		}
	
	
		return cycleD / totD;
	}

	public double getVehicleCost(BiObjectiveTWIndividual i) {
		return this.getFixedVehCost(i)+this.getVehRunningCost(i);
	}
	
	public double getSolutionCost(BiObjectiveTWIndividual i) {
		return this.getStaffCost(i) + this.getVehicleCost(i);
	}
}
