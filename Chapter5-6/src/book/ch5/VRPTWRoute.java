package book.ch5;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import book.ch1.Visit;
import book.ch6.mapElites.ReloadNode;

/*
 *  Neil Urquhart 2020
 *  
 *  This is a specialisation of ArrayList that represents a route within a VRPTW solution
 * 
 */

public class VRPTWRoute extends ArrayList<VisitNode>{
	
	protected LocalTime start; //Time of leaving the depot
	protected LocalTime end; //Time of returning to the depot
	protected VRPTWProblem problem;
	
	public VRPTWRoute(VRPTWProblem prob) {
		super();
		problem = prob;
	}
	
	public double getIdle() {
		/*
		 * Return the amount of time that the vehicle is waiting for time windows to start
		 */
		double idle=0;
		for (VisitNode vn : this) {
			idle += vn.getMinsWaiting();
		}
		return idle;
	}
	
	public double getDist() {
		/*
		 * Get distance of route
		 */
		double dist=0;
		Visit p =  problem.getStart();
		for (VisitNode vn : this) {
			dist += problem.getDistance(p, vn.getVisit());
			p = vn.getVisit();
		}
		dist += dist += problem.getDistance(p, problem.getStart()); 
		return dist;
	}
	public String toString() {
		String buffer = start.toString() +"NEW ROUTE: \n";
		
		for (VisitNode v : this)
			if (v instanceof ReloadNode)
				buffer += "RELOAD";
			else 
			   buffer += v.toString() +",";
		
		buffer += end.toString() +"\n";
		return buffer;
	}
	
	public int demand(){
		//Return the total cumulative demand within <route>
		int demand=0;
		for (VisitNode visit: this){
			demand += visit.getVisit().getDemand();
		}
		return demand;
	}
	
	public void setStartEndTimes() {
		/*
		 * Set the start and end times from the depot based of route
		 * 
		 */
		if (this.size()<1)return;
		//set start and end times
		VisitNode v = this.get(0);
		int mins = ((VRPTWProblem)problem).getTravelMinutes(problem.getStart(), v.getVisit());
		v.setMinsWaiting(0);
		this.start = v.getDeliveryTime().minusMinutes(mins);
		
		v = this.get(this.size()-1);
		mins = ((VRPTWProblem)problem).getTravelMinutes(problem.getStart(), v.getVisit());
		mins = mins + ((VRPTWProblem)problem).getDeliveryTime();
		this.end = v.getDeliveryTime().plusMinutes(mins);
	}
	
	public long getTime() {//Route timespan in mins
		if ((start==null)||(end==null)) 
			return 0;
			
		//Check for routes that end after midnight
		long m =  ChronoUnit.MINUTES.between(start, end);
		
		if (m<0) {//Route ends after midnight
			m =  ChronoUnit.MINUTES.between(start, LocalTime.of(23,59,00));
			m ++;// missing minute at midnight
			m += ChronoUnit.MINUTES.between(LocalTime.of(00,00,00),end);
			
		}
		return m;
	}
	
	public LocalTime getStart() {
		return this.start;
	}
	
	public LocalTime getEnd() {
		return this.end;
	}
}
