package book.ch11.tsp;

import java.util.ArrayList;
import java.util.HashMap;

import book.ch1.LatLon;
import book.ch7.routing.GraphHopper;
import book.ch7.routing.RoutingEngine;

public class Comparision {
	
	private static RoutingEngine router;
	private static HashMap<String,String> options;
	
	public static double length(ArrayList<LatLon> route) {
		double dist=0;
		LatLon prev = null;

		for (LatLon place: route) {
			if(prev!=null) {
				dist = dist + getJourneyDist (prev,place,"foot");
			}
			prev = place;
		}
		return dist;
	}

	private static double getJourneyDist(LatLon start,LatLon end, String mode) {
		double res=0;
		router = new GraphHopper();
		options = new HashMap<String,String>();
		options.put(RoutingEngine.OPTION_OSM_FILE, "./data/map.osm");
		options.put(RoutingEngine.OPTION_DATA_DIR, "./data/");
		options.put(RoutingEngine.OPTION_MODE, mode);
		res = router.findRoute(start, end, options).getDistanceKM();
		return res;
	}
}
