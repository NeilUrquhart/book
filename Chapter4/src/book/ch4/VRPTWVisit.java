package book.ch4;

import java.time.LocalTime;

import book.ch2.VRPVisit;



public class VRPTWVisit extends VRPVisit {
	/*
	 *  Neil Urquhart 2020
	 * 
	 * Adds a time window to VRPVisit
	 */


	private TimeWindow myTimeWindow;

	public VRPTWVisit(String name, double lat, double lon, int demand, int atwStart, int atwEnd) {
		super(name, lat, lon, demand);
		LocalTime start = LocalTime.of(atwStart,0,0);
		LocalTime end= LocalTime.of(atwEnd,0,0);
		myTimeWindow = new TimeWindow(start, end);
	}

	public boolean inTimeWindow(LocalTime time) {
		//Return True is <time> is within the the time window
		return myTimeWindow.inWindow(time);
	}

	public LocalTime getEarliest() {
		//Return the earliest point at which the visit could take place.
		return myTimeWindow.getStart();
	}


	public String toString() {
		String buffer = this.getName() + "(" + this.getDemand() + ")";

		return buffer;
	}	
}
