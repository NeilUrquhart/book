package old;


import java.util.ArrayList;
import java.util.HashMap;

import book.ch1.Visit;



/*
 * A basic class to represent a journey that links two locations
 * 
 * Neil Urquhart 2020
 */
public class Journey {
	
	/*
	* The journey takes place between A and B 
	*/
	protected Visit locationA;
	protected Visit locationB;
	
	protected double distanceKM=-1; 
	// The distance travelled in KM 
	protected double travelTimeMS=-1; 
	// Traveltime in MSecs
	protected ArrayList<Visit> path;
	
	
	/*
	* Constructor
	*/
	public Journey(Visit a, Visit b){
		locationA = a;
		locationB = b;
	}

	/*
	 * Accessor Methods
	 * 
	 */
	public double getDistanceKM() {
		return distanceKM;
	}

	public void setDistanceKM(double distanceKM) {
		this.distanceKM = distanceKM;
	}

	public double getTravelTimeMS() {
		return travelTimeMS;
	}

	public void setTravelTimeMS(double travelTimeMS) {
		this.travelTimeMS = travelTimeMS;
	}


	public Visit getPointA() {
		return locationA;
	}

	public Visit getPointB() {
		return locationB;
	}
	
	
	public ArrayList<Visit> getPath() {
		return path;
	}

	public void setPath(ArrayList<Visit> path) {
		this.path = path;
	}

	/*
	 * ToString
	 * 
	 */
	public String toString(){
		String buffer ="";
		
		buffer = locationA + " : " + locationB;
		
		
		return buffer;
	}
}
