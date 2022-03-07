package book.ch12.problem;

import java.util.HashMap;

/*
 * Neil Urquhart 2021
 * A class to hold the results of a solution.
 * 
 */
public class Results {
	private int vehicles;
	private double fixedCost;
	private double runningCost;
	private double emissions;
	private double maxTime;
	private double distance;
	private int microDepots;
	private int walkers;
	private int bikes;
	private int evans;
	private double byMD;
	
	public class MD_use{
		/*
		 * A simole data structure for holding stats about MD use
		 * 
		 */
		MicroDepot md;
		boolean used = false;
		int walkRoutes=0;
		int evRoutes=0;
		int cycleRoutes=0;
	}
	
	private HashMap <MicroDepot,MD_use>  mdStats = null;
	
	public void addMDUse(MicroDepot md, boolean walk, boolean ev, boolean cycle) {
		if (mdStats == null) {//First usage
			mdStats = new HashMap<MicroDepot,MD_use>();
			for (MicroDepot m : MDProblem.getInstance().getMicroDepots()) {
				mdStats.put(m, new MD_use());
			}
		}
		
		MD_use c = mdStats.get(md);
		c.used = true;
		if (walk)	
			c.walkRoutes++;
		if (cycle)
			c.cycleRoutes++;
		if(ev)
			c.evRoutes++;
	}
	
	public String getMDstats() {
		String buffer="";
		if (mdStats ==  null)
			return buffer;
		
		for (MicroDepot md : mdStats.keySet()) {
			MD_use stats = mdStats.get(md);
			int used=0;
			if(stats.used) used=1;
			buffer = buffer +"," + md.getName() +","+ used +"," + stats.cycleRoutes + "," + stats.evRoutes + "," + stats.walkRoutes;
		}
		
		return buffer;
	}
	public void setByMD(double val) {
		byMD = val;
	}
		
	public double getByMD() {
		return byMD;
	}
	
	public void addWalk() {
		walkers++;
	}
	
	public void addBike() {
		bikes++;
	}
	
	public void addEVan() {
		evans++;
	}
	
	public int getMicroDepots() {
		return microDepots;
	}
	public void setMicroDepots(int microDepots) {
		this.microDepots = microDepots;
	}
	public int getWalkers() {
		return walkers;
	}
	public int getBikes() {
		return bikes;
	}
	public int getEVans() {
		return evans;
	}

	public double getFixedCost() {
		return fixedCost;
	}
	public void setFixedCost(double fixedCost) {
		this.fixedCost = fixedCost;
	}
	public double getRunningCost() {
		return runningCost;
	}
	public void setRunningCost(double runningCost) {
		this.runningCost = runningCost;
	}
	public double getEmissions() {
		return emissions;
	}
	public void setEmissions(double emissions) {
		this.emissions = emissions;
	}
	public double getMaxTime() {
		return maxTime;
	}
	public void setMaxTime(double maxTime) {
		this.maxTime = maxTime;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	public double getTotalCost() {
		return this.getFixedCost() + this.getRunningCost();
	}
	
	public String toString(){
		return "Vehicles = " + vehicles +"\n" +
				"Fixed cost = " + fixedCost +"\n" +
				"Running cost = " + runningCost + "\n" +
				"Emissions = " + emissions + "\n" +
				"Max Time = " + maxTime + "\n" +
				"Distance = " + distance +"\n";
	}
	
	
}
