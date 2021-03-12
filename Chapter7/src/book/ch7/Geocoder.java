package book.ch7;

import java.awt.geom.Point2D;

import book.ch1.Visit;

/*
 * Neil Urquhart 2021
 * 
 * This interface specifies a very simple geolocation service
 */

public interface Geocoder {
	public Point2D.Double geocode(String label);
	//Convert a String label to lat/lon coordinate
	public String reverseGeocode(Point2D.Double p);
	//Convert a lat/lon coordinate to a String label
}

