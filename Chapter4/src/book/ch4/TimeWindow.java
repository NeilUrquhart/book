package book.ch4;


/*
 * Neil Urquhart 2020
 * 
 * A simple implementation of a time window.
 * This uses the Java 1.8 LocalTime class
 * 
 * 
 */

import java.time.LocalTime;


public class TimeWindow{
	private LocalTime start;
	private LocalTime  end;
	
	public TimeWindow(LocalTime aStart, LocalTime anEnd) {
		//Constructor, always need to have s start/end
		start = aStart;
		end = anEnd;
	}
	
	public boolean inWindow (LocalTime time) {
		//Return True if the time specified is 'in' the window
		if ((time.compareTo(start)>=0)&&(time.compareTo(end)<=0))
			return true;
		else
			return false;
	}
	
	public String toString() {
		return start.toString() + " - " + end.toString();
	}
	
	//Accessors
	public LocalTime getStart() {
		return start;
	}
	public LocalTime getEnd() {
		return end;
	}
	
	
}
