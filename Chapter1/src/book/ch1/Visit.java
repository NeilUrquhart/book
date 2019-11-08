package book.ch1;

import java.awt.geom.Point2D;

/*
 * Neil Urquhart 2019
 * A simple class representing a visit that extends the Java Point class
 * 
 */
public class Visit extends Point2D.Double{
 	protected String theName;
 	
 	public Visit(String name, double lat, double lon){
 		super(lat,lon);
 		theName = name;
 	}
 	
 	public String toString(){
 	  return theName;	
 	}
 	
 	public double distance(Visit v) {
 		//Return the distance between 2 points based upon the Euclidean distance
 		//as implemented within 
 		return super.distance(v);
 	}

	
 }