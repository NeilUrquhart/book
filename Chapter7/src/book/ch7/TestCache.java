package book.ch7;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

import book.ch1.LatLon;

public class TestCache {

	public static void main(String[] args) {
		Geocoder gc = new Cache(new Nominatim());
		
		System.out.println(gc.geocode("Edinburgh Castle"));
		System.out.println(gc.reverseGeocode(new LatLon(55.932941,-3.213922)));	
		
		((Cache)gc).showCache();
	}

}
