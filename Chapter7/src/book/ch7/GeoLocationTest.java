package book.ch7;

import java.awt.geom.Point2D;
import java.io.UnsupportedEncodingException;

import book.ch1.Visit;

public class GeoLocationTest {
	
	public static void main(String[] args) {
		System.out.println("Napier Locator test ....");
		test(new NapierLocator());
		System.out.println("Nominatim test ....");
		test(new Nominatim());	
	}

	private static void test(Geocoder gl) {
		//Geolocate a String (campus name) to lat/lon coordinates
		String testLocation = "Napier University, Merchiston";
		Point2D campus = gl.geocode(testLocation);
		System.out.println("Geocoding " + testLocation +"\n"+ campus.getX() + ","+ campus.getY());		
		
		Point2D.Double p = new Point2D.Double(55.932941,-3.213922);
		//Find the nearest campus to lat/lon coordinates
		System.out.println("Reverse geocoding " + p + "\n" );
		System.out.println(gl.reverseGeocode(p));
	}
}
