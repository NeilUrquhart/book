package book.ch12.problem;

import java.util.HashMap;
import book.ch1.LatLon;
import book.ch7.routing.GraphHopper;
import book.ch7.routing.Journey;
/*
 * Neil Urquhart 2021
 * An OSM Helper class, that allows for modes other than car journeys
 * Incorporates a HashMap based cache
 * For car journeys - the start/end points are checked, and a walk segement added if a car cannot complete
 * the journey
 * 
 * Uses the Singleton pattern
 */

public class Router {
	private Router() {
		options = new HashMap<String,String>();
		options.put(gh.OPTION_DATA_DIR, "./osm/");
		options.put(gh.OPTION_OSM_FILE, "scotland-latest.osm.pbf");
	}

	private static Router instance;

	public static Router getInstance() {
		if (instance==null)
			instance = new Router();
		return instance;
	}

	private HashMap<String, Journey> cache = new HashMap<String, Journey>();
	private HashMap<String,String> options;
	private GraphHopper gh = new GraphHopper();

	public Journey findRoute(LatLon start, LatLon end, String mode) {
		String key = start.getLat()+""+start.getLon() +":" + start.getLat() +""+ start.getLon() +":" + mode;
		Journey res = cache.get(key);
		if (res != null)
			return res;

		options.remove(gh.OPTION_MODE);
		options.put(gh.OPTION_MODE, mode);
		res  = gh.findRoute(start, end, options);
		if (mode.equals("car"))
			res = checkForWalk(res);
		cache.put(key, res);
		return res;
	}

	private Journey checkForWalk(Journey j) {
		/*
		 * Need to check if a car route cannot access the actual start or end. If this is the case, add a walk
		 */

		//Check start
		LatLon carStart = j.getPath().get(0);
		if ((j.getPointA().getLat() != carStart.getLat())||(j.getPointA().getLon() != carStart.getLon())){
			Journey walkA = findRoute(j.getPointA() ,carStart,"foot");	
			if (walkA.getDistanceKM() > 0.01) {
				//Get walk path
				j.putAttribute("walkA",  walkA.getPath());
				j.setDistanceKM(j.getDistanceKM() + walkA.getDistanceKM());//Check
				j.setTravelTimeMS(j.getTravelTimeMS() + walkA.getTravelTimeMS());//Check
			}
		}  

		//Check end
		LatLon carEnd  =j.getPath().get(j.getPath().size()-1);
		if ((j.getPointB().getLat() != carEnd.getLat())||(j.getPointB().getLon() != carEnd.getLon())){
			Journey walkB = findRoute(j.getPointB() ,carEnd,"foot");		
			j.putAttribute("walkB",  walkB.getPath());
			j.setDistanceKM(j.getDistanceKM() + walkB.getDistanceKM());//Check
			j.setTravelTimeMS(j.getTravelTimeMS() + walkB.getTravelTimeMS());//Check
		}

		return j;
	}
}
