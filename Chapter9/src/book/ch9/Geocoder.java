package book.ch9;

import java.awt.geom.Point2D;

import book.ch1.Visit;
import book.ch1.LatLon;

/*
 * Neil Urquhart 2021
 * 
 * This interface specifies a very simple geolocation service
 */

public interface Geocoder {
	public LatLon geocode(String label);
	//Convert a String label to lat/lon coordinate
	public String reverseGeocode(LatLon p);
	//Convert a lat/lon coordinate to a String label
}

