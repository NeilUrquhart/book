package book.ch8.humanitarian;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import book.ch1.Visit;
import book.ch2.CVRPProblem;
import book.ch2.VRPVisit;
import book.ch7.OSMAccessHelper;

/*
 * Neil Urquhart 2019
 * This class represents a FoodCVRP problem.
 * It extends the basic CVRPProblem class.
 * 
 */
public class HudProblem extends CVRPProblem {
	private SimpleDateFormat dateOnlyformatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private Visit end;
    private String mode;
	private String reference;
	private double timeLimitMS;
	private double deliveryTimeMS;
	private double speedfactor;
	private double startDate;

	private double startTime =0;
	private double runTime;
	
	
	public double getStartTime() {
		return startTime;
	}

	public double getRunTime() {
		return runTime;
	}


	
	public void setStartTime(double startTime) {
		this.startTime = startTime;
	}

	public void setRunTime(double runTime) {
		this.runTime = runTime;
	}

	
	
	public void setMode(String aMode) {
    	if (aMode.contains("car"))
    		mode = "car";
    	
    	if (aMode.contains("cycle"))
    		mode = "bike";
    	
    	if (aMode.contains("walk"))
    		mode = "foot";
    	
    }
    
    public String getMode() {
    	return mode;
    }

    public Visit getEnd() {
		return end;
	}

	public void setEnd(Visit end) {
		this.end = end;
	}

	
	public SimpleDateFormat getDateOnlyformatter() {
		return dateOnlyformatter;
	}


	public SimpleDateFormat getTimeOnlyformatter() {
		return timeOnlyformatter;
	}

		private SimpleDateFormat timeOnlyformatter = new SimpleDateFormat("HH:mm");

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public double getTimeLimitMS() {
		return timeLimitMS;
	}

	public void setTimeLimitMS(double timeLimitMS) {
		this.timeLimitMS = timeLimitMS;
	}

	public double getDeliveryTimeMS() {
		return deliveryTimeMS;
	}

	public void setDeliveryTimeMS(double deliveryTimeMS) {
		this.deliveryTimeMS = deliveryTimeMS;
	}

	

	public double getStartDate() {
		return startDate;
	}

	public void setStartDate(double startDate) {
		this.startDate = startDate;
	}

	public double getSpeedfactor() {
		return speedfactor;
	}

	public void setSpeedfactor(double speedfactor) {
		this.speedfactor = speedfactor;
	}

	public double getDistance(ArrayList sol){
		//Get the total distance covered by the solution in <sol>
		ArrayList<ArrayList<VRPVisit>> solution = (ArrayList<ArrayList<VRPVisit>>) sol;
		double dist =0;
		//int visitCount = super.getSize();
		for (ArrayList<VRPVisit> route: solution){
		    ArrayList<Visit> r = new ArrayList<Visit>(route);
			dist = dist + getRouteDistance(r);//,visitCount);
		//	visitCount  = visitCount - route.size();			
		}
		return dist;
	}

	public double getRouteDistance(ArrayList<Visit> possibleRoute) {//,int visitcount){
		//Get the distance of the visits within possibleRoute - include the start point as well to complete the circuit
		/* this has to be a weighted distance)*/
		double dist =0;
		int visitCount = possibleRoute.size();
		Visit previousCity = super.getStart();
		for (Visit city : possibleRoute){//go through each city
			double wDist = getDistance(previousCity, city)*(visitCount);//Add weighting
			dist = dist + wDist;
			visitCount --;
			previousCity = city;
		}
		dist = dist + getDistance(previousCity, this.end);
		return dist;
	}
	
	public double getDistance(Visit x, Visit y){
		//Get the distance between two visits
		if ((x == null)||(y==null))
			return 0;
		else			{
			
			return OSMAccessHelper.getJourney(x, y, mode).getDistanceKM();
		}
	}
		
	public String toString() {
		String buffer = "";
		
		for (Object visit : this.getVisits()) {
			buffer = buffer + visit.toString() +"\n";
		}
		return buffer;
	}
}
