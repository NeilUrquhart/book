package book.ch11;

import java.util.HashMap;

import book.ch1.LatLon;
import book.ch7.routing.GraphHopper;
import book.ch7.routing.Journey;
import book.ch7.routing.RoutingEngine;

public class RoutingFactory {

	private RoutingEngine router;
	private HashMap<String,String> options;

	/*
	 * Singleton
	 */

	private static RoutingFactory instance = null;

	public static  RoutingFactory getInstance() {
		if (instance==null)
			instance = new RoutingFactory();
		return instance;
	}

	private RoutingFactory() { 
		/* 
		 * Default construction is made private to enforce
		 * singleton.
		 */
		router = new GraphHopper();
		options = new HashMap<String,String>();
		options.put(RoutingEngine.OPTION_OSM_FILE, "./data/map.osm");
		options.put(RoutingEngine.OPTION_DATA_DIR, "./data/");
	}

	public Journey getJourney(LatLon start,LatLon end, String mode) {
		options.put(RoutingEngine.OPTION_MODE, mode);
		return  router.findRoute(start, end, options);
	}
	
	public double getJourneyDist(LatLon start,LatLon end, String mode) {
		long key = cantorPair(start.hashCode(),end.hashCode());
		Double res = cache.get(key);
		if (res == null) {
			options.put(RoutingEngine.OPTION_MODE, mode);
			res = router.findRoute(start, end, options).getDistanceKM();
			cache.put(key, res);
		}
		return res;
	}

	/* 
	 * Local cache based on combined hashmaps
	 */
	private static HashMap<Long,Double> cache = new HashMap<Long,Double>();

	private  long cantorPair(long a, long b) {
		//Use Cantors paring function to generate unique number
		long result = (long) (0.5 * (a + b) * (a + b + 1) + b);
		return result; //Return the result

	}
}
