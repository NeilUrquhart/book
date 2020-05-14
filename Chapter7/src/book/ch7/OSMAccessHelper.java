package book.ch7;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

//import NGT.Journey;
//import NGT.Location;

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;

import com.graphhopper.PathWrapper;
import com.graphhopper.reader.osm.GraphHopperOSM;
import com.graphhopper.routing.util.EncodingManager;
import com.graphhopper.util.PointList;

import book.ch1.Visit;





public class OSMAccessHelper {
	private static GraphHopperOSM hopper=null;
	private static HashMap<String, Journey> cache = new HashMap<String, Journey>();
	private static String fileName;
	private static String folder;
	private static double  timeFactor = 1.6;
	
	

	
	public static void setFileName(String fName) {
		fileName = fName;
	}
	
	public static void setFolder(String path) {
		folder = path;
	}
	
	public static Journey getJourney(Visit last, Visit next, String type){
		String key = last.getX()+""+last.getY()+":" + next.getX()+""+ next.getY() +":" + type;
		Journey res = cache.get(key);
		if (res != null)
			return res;
		
		res = findJourney(last, next, type);
		cache.put(key, res);
		return res;
	}
	
	private static Journey findJourney(Visit start, Visit end, String type) {
	
		//System.out.println("OSM call "+ start.toString() +":"+end.toString());
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
	    res.setDistanceKM(pw.getDistance()/1000);//Check
	    res.setTravelTimeMS(pw.getTime()*timeFactor);//Check
	    ArrayList<Visit> path = new ArrayList<Visit>();
	    PointList pl = pw.getPoints();
	    //path.add(start);
	    for (int c=0; c < pl.size();c++){
	    		path.add(new Visit("",pl.getLatitude(c),pl.getLongitude(c)));
	    }
	    //path.add(end);
	    res.putAttribute(Journey.PATH, path);
	    
	    /*
	     * Need to check if a car route cannot access the actual start or end. If this is the case, add a walk
	     */
	    
	    if (type.equals("car")){
	    	//Check start
	    	Visit carStart = new Visit("",pw.getPoints().getLat(0),pw.getPoints().getLon(0));
	    	if ((start.getX() != carStart.getY())||(start.getX() != carStart.getY())){
	    		 Journey walkA = getJourney(start ,carStart,"foot");	
	    		 if (walkA.getDistanceKM() > 0.01) {
	    		 //Get walk path
	    		 res.putAttribute("walkA",  walkA.getAttribute(Journey.PATH));
	    		 res.setDistanceKM(res.getDistanceKM() + walkA.getDistanceKM());//Check
	    		 res.setTravelTimeMS(res.getTravelTimeMS() + walkA.getTravelTimeMS());//Check
	    		 }
	    	}  
	    	//Add walk to result?
	    	
	    	//Check end
	    	Visit carEnd = new Visit("",pw.getPoints().getLat(pw.getPoints().getSize()-1),pw.getPoints().getLon(pw.getPoints().getSize()-1));
	    	if ((end.getX() != carEnd.getY())||(end.getX() != carEnd.getY())){
	    		 Journey walkB = getJourney(end ,carEnd,"foot");		
	    		 res.putAttribute("walkB",  walkB.getAttribute(Journey.PATH));
	    		 res.setDistanceKM(res.getDistanceKM() + walkB.getDistanceKM());//Check
	    		 res.setTravelTimeMS(res.getTravelTimeMS() + walkB.getTravelTimeMS());//Check

	    	}
	    	//Add walk to result?
	    	
	    }
	    
	    return res;
	}

	public static GraphHopperOSM init(String folder,String fName) {
		setFileName(fName);
		setFolder(folder);
		return init();
	}
	private static GraphHopperOSM init() {
		//String fileName = "./data/scotland-latest.osm.pbf";//edinburgh.osm";
		hopper = new GraphHopperOSM();
		hopper.setOSMFile(folder+"/"+fileName);
		
		hopper.setCHEnabled(false); // CH does not work with shortest weighting (at the moment)
		
		// where to store GH files?
		String store = folder +"/osm/";
		File dir = new File(store);
		if (!dir.exists()){
			try{
			Path path = Paths.get(store);
			Files.createDirectories(path);
			//dir.mkdir();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		
		hopper.setGraphHopperLocation(store );
		hopper.setEncodingManager(new EncodingManager("car,bike,foot"));

		// this may take a few minutes
		hopper.importOrLoad();
		return hopper;
	}

}
