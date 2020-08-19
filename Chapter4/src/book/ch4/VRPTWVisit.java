package book.ch4;

import java.time.LocalTime;

import book.ch2.VRPVisit;



public class VRPTWVisit extends VRPVisit {
	/*
	 * Time windows...
	 * 
	 * Treat times as long
	 * 
	 */
	
	
	private TimeWindow myTimeWindow;

	public VRPTWVisit(String name, double lat, double lon, int demand, int atwStart, int atwEnd) {
		super(name, lat, lon, demand);
		LocalTime start = LocalTime.of(atwStart,0,0);
		LocalTime end= LocalTime.of(atwEnd,0,0);
		
		myTimeWindow = new TimeWindow(start, end);

	}
	
	public boolean inTimeWindow(LocalTime time) {
		return myTimeWindow.inWindow(time);
	}
	
	public LocalTime getEarliest() {
		return myTimeWindow.getStart();
	}

		
	public String toString() {
		String buffer = this.getName() + "(" + this.getDemand() + ")";
		
		return buffer;
	}
	
//	
}
