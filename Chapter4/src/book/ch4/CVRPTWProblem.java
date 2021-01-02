package book.ch4;

import java.time.LocalTime;
import java.util.ArrayList;

import book.ch1.Visit;
import book.ch2.CVRPProblem;
import book.ch2.VRPVisit;



public class CVRPTWProblem extends CVRPProblem {

	/*
	 * Neil Urquhart 2020
	 * CVRPTWProblem - based on CVRPProblem
	 * 
	 */


	/* Parameters:
	 * 
	 * Normally these would be loaded in from a problem file, rather than defined here
	 * 
	 */

	//Assume a speed of 40kmh = calculate as minutes per km i.e. 0.667 kms per min
	final private double  speed = 0.667;
	//Time per delivery in minutes
	final private int deliveryTime = 5;// mins
	//Start of day
	final private  LocalTime startTime = LocalTime.of(7,30,0);
	
	//Turnaround time - time for a vehicle to be prepared for an addtional route
	final private int turnAroundTime = 30;//mins
	/*
	 * Finished Parameters
	 */
	
	public int getTurnAroundTime() {
		return turnAroundTime;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public int getTravelMinutes(Visit x, Visit y) {
		double dist = super.getDistance(x, y);
		//Assume raw dist is kms
		double timeMins = dist/speed;
		//Not interested in seconds 
		return (int) timeMins;

	}

	public int getTime() {
		/*
		 * Time in mins for ALL routes in this solution
		 */
		int time=0;
		for (Object or : this.getCVRPSolution()) {
			VRPTWRoute r = (VRPTWRoute)or;
			time += r.getTime();
		}
		return time;
	}

	public double getIdle() {
		/*
		 * Calculate the total idle time within this solution
		 * 
		 */
		double idle=0;
		for (Object or : this.getCVRPSolution()) {
			VRPTWRoute r = (VRPTWRoute)or;
			idle += r.getIdle();
		}

		return idle;
	}

	public double getDistance() {
		/*
		 * Calculate the total distance within this solution
		 * 
		 */
		double dist=0;
		for (Object or : this.getCVRPSolution()) {
			VRPTWRoute r = (VRPTWRoute)or;
			dist += r.getDist();
		}

		return dist;
	}

	public int getDeliveryTime() {
		return deliveryTime;
	}
	private int totDemand;
	public int getTotalDemand() {
		if (totDemand==0)
			for (Object ov : this.getVisits()) {
				VRPVisit v = (VRPVisit)ov;
				totDemand += v.getDemand();
			}
		return totDemand;
	}
}
