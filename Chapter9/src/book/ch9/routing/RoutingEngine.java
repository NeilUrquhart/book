package book.ch9.routing;

import java.util.HashMap;

import book.ch1.Visit;
import book.ch9.Geocoder;
import book.ch1.LatLon;

/*
 * Neil Urquhart 2021
 * 
 * A Very simple Routing Engine class
 * Please call  setGeocoder must be called before findRoute is called
 * 
 */

public abstract class RoutingEngine {
	/*
	 * Define some mode keys that can be used within the
	 * options structure
	 */
	public static String OPTION_DATA_DIR = "dataDir";
	public static String OPTION_OSM_FILE = "osmFile";
	public static String OPTION_MODE = "mode";
		
	public abstract Journey findRoute(LatLon start, LatLon end, HashMap<String,String> options);
	/*
	 * Options holds Key,Value strings that allow options pertaining to a specific 
	 * RoutingEngine to be set.
	 */
}
