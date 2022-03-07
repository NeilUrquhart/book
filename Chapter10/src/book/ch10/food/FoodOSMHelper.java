package book.ch10.food;

import java.util.HashMap;


import book.ch1.Visit;
import book.ch9.routing.GraphHopper;
import book.ch9.routing.Journey;
import book.ch9.routing.RoutingEngine;


public class FoodOSMHelper {
	private Journey[][] cache;

	private  RoutingEngine router;
	private HashMap<String,String> options;

	private static FoodOSMHelper instance = null;

	private FoodOSMHelper() { 
		router = new GraphHopper();
		options = new HashMap<String,String>();
		options.put(RoutingEngine.OPTION_OSM_FILE, FoodProperties.getInstance().get("osmfile"));
		options.put(RoutingEngine.OPTION_DATA_DIR, FoodProperties.getInstance().get("datadir"));
	}

	public static FoodOSMHelper getInstance() {
		if (instance == null)
			instance = new FoodOSMHelper();
		return instance;
	}

	public Journey getJourney(Visit start, Visit end, String mode) {
		options.put(RoutingEngine.OPTION_MODE, mode);

		if (cache != null) {//If cache has been setup
			if ((start instanceof FoodVisit) && (end instanceof FoodVisit)) {
				FoodVisit s = (FoodVisit) start;
				FoodVisit e = (FoodVisit) end;
				if (cache[s.getIndex()][e.getIndex()] != null) {
					return cache[s.getIndex()][e.getIndex()] ;
				}
				cache[s.getIndex()][e.getIndex()] = router.findRoute(start, end, options);
				return cache[s.getIndex()][e.getIndex()];
			}
		}
		return router.findRoute(start, end, options);
	}

	public void inittCache(int size) {
		cache = new Journey[size][];
		for (int c=0; c < size; c++) {
			cache[c] = new Journey[size];
		}
	}
}
