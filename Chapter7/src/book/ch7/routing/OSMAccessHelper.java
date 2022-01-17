package book.ch7.routing;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;

import com.graphhopper.PathWrapper;
import com.graphhopper.reader.osm.GraphHopperOSM;
import com.graphhopper.routing.util.EncodingManager;
import com.graphhopper.util.PointList;

import book.ch1.Visit;

public class OSMAccessHelper {
	private static GraphHopperOSM hopper=null;
	private static String fileName;
	private static String folder;

	public static void setData(String path,String fName) {
		fileName = fName;
		folder = path;
		
	}

	public static Journey getJourney(Visit start, Visit end, String type){
		if (hopper==null)
			init();

		GHRequest request = new GHRequest(start.getX(),start.getY(),end.getX(),end.getY()).setVehicle(type);
		GHResponse response = hopper.route(request);
		if (response.hasErrors()) {
			throw new IllegalStateException("S= " + start + "e= " + end +". GraphHopper gave " + response.getErrors().size()
					+ " errors. First error chained.",
					response.getErrors().get(0)
					);
		}

		PathWrapper pw = response.getBest();
		Journey res = new Journey(start,end);
		res.setDistanceKM(pw.getDistance()/1000);
		res.setTravelTimeMS(pw.getTime());
		ArrayList<Visit> path = new ArrayList<Visit>();
		PointList pl = pw.getPoints();
		for (int c=0; c < pl.size();c++){
			path.add(new Visit("",pl.getLatitude(c),pl.getLongitude(c)));
		}
		res.setPath(path);
		return res;
	}

	private static GraphHopperOSM init() {
		hopper = new GraphHopperOSM();
		hopper.setOSMFile(folder+"/"+fileName);
		hopper.setCHEnabled(false); // CH does not work with shortest weighting (at the moment)

		// where to store GH files?
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
