package book.ch7;


import book.ch2.VRPVisit;

public class IVisit extends VRPVisit {
	/*
	 * An extension of Visit - each visit has a unique index property, which is set when the object is created
	 */
	private static int counter=0;
	
	private int index;
	
	public IVisit(String name, double lat, double lon, int demand) {
		super(name, lat, lon, demand);
		counter++;
		index=counter;
	}
	
	public int getIndex() {
		return index;
	}
	
	public static int getCounter() {
		return counter;
		
	}

}
