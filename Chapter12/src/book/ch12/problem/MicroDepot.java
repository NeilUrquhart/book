package book.ch12.problem;

import book.ch1.LatLon;

/*
 * Neil Urquhart 2021
 * 
 * A simple class that represents a MicroDepot
 * 
 */
public class MicroDepot extends LatLon{

	private String name;
	
	public MicroDepot(double lt, double ln, String name) {
		super(lt, ln);
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
