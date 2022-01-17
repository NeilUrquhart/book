package book.ch5;

/*
 * A class that contains the FabFood costs
 * This class is implemented as a Singleton
 * 
 * In a production system, the fixed costs might be better
 * read in from a .CSV file so that they can be altered.
 * 
 * 
 * Our costs are drawn from https://motortransport.co.uk/wp-content/uploads/2018/12/MT-cost-tables-2018.pdf
 * 
 */
public class FabFoodCostModel {
	private	double VEH_FIXED = 164; //£
	private double VEH_RUNNING = 0.117;//£ per km
	private double STAFF_HOUR = 12;//15;

	//Singleton code
	private FabFoodCostModel() {}
	private static  FabFoodCostModel instance=null;
	
	public static FabFoodCostModel getInstance() {
		if (instance == null)
			instance = new FabFoodCostModel();
		return instance;
	}
	//Done Singleton
	public double getStaffCost(MObjTWIndividual i) {
			double perMin = STAFF_HOUR/60;
		return (i.getTime()* perMin); 
	}
	
	public double getFixedVehCost(MObjTWIndividual i) {
		return i.getRoutes() * VEH_FIXED;
	}
	
	public double getVehRunningCost(MObjTWIndividual i) {
		return i.getDistance()*VEH_RUNNING;
	}
	
	public double getVehicleCost(MObjTWIndividual i) {
		return this.getFixedVehCost(i)+this.getVehRunningCost(i);
	}
	
	public double getSolutionCost(MObjTWIndividual i) {
		return this.getStaffCost(i) + this.getVehicleCost(i);
	}
}
