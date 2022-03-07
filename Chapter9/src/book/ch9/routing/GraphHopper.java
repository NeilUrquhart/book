package book.ch9.routing;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.PathWrapper;
import com.graphhopper.reader.osm.GraphHopperOSM;
import com.graphhopper.routing.util.EncodingManager;
import com.graphhopper.util.PointList;

import book.ch1.LatLon;
import book.ch9.Geocoder;

/*
 * Neil Urquhart 2021
 * An implementation of RoutingEngine that uses the GraphHopper API
 * You need to have the GraphHopper JAR files on your build path,
 * (see the import statements above).
 */
public class GraphHopper extends RoutingEngine{
	private static GraphHopperOSM hopper=null;

	@Override
	public Journey findRoute(LatLon start, LatLon end, HashMap<String,String> options) {
		
		if (hopper==null)
			init(options.get(RoutingEngine.OPTION_DATA_DIR),options.get(RoutingEngine.OPTION_OSM_FILE));

		GHRequest request = new GHRequest(start.getLat(),start.getLon(),end.getLat(),end.getLon()).setVehicle(options.get(RoutingEngine.OPTION_MODE));
		GHResponse response = hopper.route(request);
		if (response.hasErrors()) {
			throw new IllegalStateException("S= " + start.getLat() +","+start.getLon() + "e= " + end.getLat() +","+end.getLon()  +". GraphHopper gave " + response.getErrors().size()
					+ " errors. First error chained.",
					response.getErrors().get(0)
					);
		}

		PathWrapper pw = response.getBest();
		Journey res = new Journey(start,end);
		res.setDistanceKM(pw.getDistance()/1000);
		res.setTravelTimeMS(pw.getTime());
		ArrayList<LatLon> path = new ArrayList<LatLon>();
		PointList pl = pw.getPoints();
		for (int c=0; c < pl.size();c++){
			path.add(new LatLon(pl.getLatitude(c),pl.getLongitude(c)));
		}
		res.setPath(path);
		return res;
	}

	private static GraphHopperOSM init(String folder, String fileName) {
		/*
		 * Initialise the GraphHopper routing library.
		 */
		hopper = new GraphHopperOSM();
		hopper.setOSMFile(folder+"/"+fileName);
		hopper.setCHEnabled(false); 

		// The GH files will be stored in the subfolder /osm/
		String store = folder +"/osm/";
		File dir = new File(store);
		if (!dir.exists()){
			try{
				Files.createDirectories(Paths.get(store));
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		hopper.setGraphHopperLocation(store );
		hopper.setEncodingManager(new EncodingManager("car,bike,foot"));
		hopper.importOrLoad();// this may take a few minutes!
		return hopper;
	}
}