package book.ch9.export;

import java.util.HashMap;

import book.ch1.LatLon;
import book.ch9.Geocoder;
import book.ch9.Nominatim;
import book.ch9.routing.GraphHopper;
import book.ch9.routing.Journey;
import book.ch9.routing.RoutingEngine;

/*
 * Neil Urquhart 2021
 * A simple demonstration of the use of ExportService
 */
public class ExportTest {

	public static void main(String[] args) {
		testWriter(new ConsoleWriter());
		testWriter(new KMLWriter());
		testWriter(new GPXWriter());
		testWriter(new CSVWriter());
	}
	
	private static void testWriter(ExportService export) {
		//Demonstrate the exporting of data via <export>
		//1) Create two waypoints
		Geocoder geocode = new Nominatim();
		String startAddress = "10 Colinton Road, Edinburgh";
		LatLon start = geocode.geocode(startAddress);
		String endAddress = "Scottish Vintage Bus Museum, Fife";
		LatLon end = geocode.geocode(endAddress);
		
		//2) Create a route between the waypoints
		RoutingEngine router = new GraphHopper();
		HashMap<String,String> options = new HashMap<String,String>();
		options.put(RoutingEngine.OPTION_OSM_FILE, "roads.osm");
		options.put(RoutingEngine.OPTION_DATA_DIR, "./data/");
		options.put(RoutingEngine.OPTION_MODE, "car");
		Journey route = router.findRoute(start, end, options);
		
		//3) Export the waypoints and route
		export.addWaypoint(start, "Start", "Edinburgh Napier University");
		export.addWaypoint(end, "Destination ", "Scottish Vintage Bus Museum");
		export.addTrack(route.getPath());
		export.write("out", "./");
	}

}
