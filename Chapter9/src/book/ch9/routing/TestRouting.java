package book.ch9.routing;

import java.util.HashMap;

import book.ch1.Visit;
import book.ch9.Geocoder;
import book.ch9.Nominatim;
import book.ch1.LatLon;

public class TestRouting {

	/*
	 * A demonstration of the use of  the RoutingEngine class
	 * 
	 * Written by Neil Urquhart 2022
	 * 
	 * Please note! 
	 * You must uncompress roads.osm after clonning the repo.
	 * 
	 */
	public static void main(String[] args) {	
		System.out.println("Homebrew");
		testRouter(new HomeBrew());
		System.out.println("GraphHopper");
		testRouter(new GraphHopper());
		}

	private static void testRouter(RoutingEngine re) {
		/*
		 * Set up some options.
		 * Depending in what class re is some options
		 * may not be supported.
		 */
		HashMap<String,String> options = new HashMap<String,String>();
		options.put(RoutingEngine.OPTION_OSM_FILE, "roads.osm");
		options.put(RoutingEngine.OPTION_DATA_DIR, "./data/");
		options.put(RoutingEngine.OPTION_MODE, "car");
		Geocoder geocode = new Nominatim();
		Journey j = re.findRoute(geocode.geocode("Edinburgh"), geocode.geocode("Glasgow"), options);
		System.out.println(j.getPointA() + " : "+ j.getPointB());
		System.out.println(j.getDistanceKM());
		System.out.println(j.getTravelTimeMS());
//		for (Visit v: j.getPath())
//			System.out.println(v);
	}
}
