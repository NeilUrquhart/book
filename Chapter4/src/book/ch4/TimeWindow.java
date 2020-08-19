package book.ch4;

import java.time.LocalTime;

public class TimeWindow{
	private LocalTime start;
	private LocalTime  end;
	
	public TimeWindow(LocalTime aStart, LocalTime anEnd) {
		start = aStart;
		end = anEnd;
	}
	
	public boolean inWindow (LocalTime time) {
		if ((time.compareTo(start)>=0)&&(time.compareTo(end)<=0))
			return true;
		else
			return false;
	}
	
	public String toString() {
		return start.toString() + " - " + end.toString();
	}
	
	public LocalTime getStart() {
		return start;
	}
	public LocalTime getEnd() {
		return end;
	}
	
	
}
