package book.ch7;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

public class TestCache {

	public static void main(String[] args) {
		Geocoder gc = new Cache(new Nominatim());
		
		System.out.println(gc.geocode("Edinburgh Castle"));
		System.out.println(gc.reverseGeocode(new Point2D.Double(55.932941,-3.213922)));	
		
		((Cache)gc).showCache();
	}

}
