package book.ch9.routing;


import java.util.ArrayList;
import java.util.HashMap;

import book.ch1.Visit;
import book.ch1.LatLon;



/*
 * A basic class to represent a journey that links two locations
 * 
 * Neil Urquhart 2021
 */
public class Journey {
	
	/*
	* The journey takes place between A and B 
	*/
	protected LatLon locationA;
	protected LatLon  locationB;
	
	protected double distanceKM=-1; 
	// The distance travelled in KM 
	protected double travelTime=-1; 
	// Traveltime in MSecs
	protected ArrayList<LatLon> path;
	
	protected HashMap<String, Object> attributes;
	
	/*
	* Constructor
	*/
	public Journey(LatLon a, LatLon b){
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
		return travelTime;
	}

	public void setTravelTimeMS(double travelTime) {
		this.travelTime = travelTime;
	}


	public LatLon getPointA() {
		return locationA;
	}

	public LatLon getPointB() {
		return locationB;
	}
	
	
	public ArrayList<LatLon> getPath() {
		return path;
	}

	public void setPath(ArrayList<LatLon> path) {
		this.path = path;
	}
	
	public void putAttribute(String k, Object o) {
		if (attributes==null)
			attributes = new HashMap<String,Object>();
		
		attributes.put(k, o);
	}
	
	public Object getAttribute(String k) {
		if (attributes==null)
			return null;
		
		return attributes.get(k);
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
