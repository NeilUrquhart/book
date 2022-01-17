package book.ch9.routing;

import java.awt.geom.Point2D;

import java.util.ArrayList;
import java.util.HashMap;

import book.ch1.Visit;
import book.ch6.algorithms.AStar;
import book.ch6.data.Graph;
import book.ch1.LatLon;
import book.ch6.data.Route;
import book.ch6.data.RouterNode;
import book.ch9.Geocoder;

/*
 * Neil Urquhart 2021
 * A "home brew" routing engine based on that demonstrated in chapter 6
 */

public class HomeBrew extends RoutingEngine {
	private Graph myGraph;//The OSM Graph
	private RouterNode startNode= null;
	private RouterNode endNode= null;


	@Override
	public Journey findRoute(LatLon start, LatLon end, HashMap<String,String> options) {
		if (myGraph == null) {
			String osmFile = options.get(RoutingEngine.OPTION_DATA_DIR) +'/'+options.get(RoutingEngine.OPTION_OSM_FILE);
			//Build a graph based on the street network in the OSM file
			myGraph = new Graph(osmFile,null);
		}
		
		findNodes(start,end);
		//Load osm data into a street graph
		Route testRoute = new Route(myGraph,startNode.getId(),endNode.getId()); 
		//Create a Route object within the graph based on the start and end nodes
		testRoute.buildRoute(new AStar());
		//Find a path between the start and the end using A*
		Journey result = new Journey(start,end);
		result.setDistanceKM(testRoute.getDist());
		result.path = new ArrayList<LatLon>();
		
		for(LatLon loc :testRoute.getLocations()) {
			result.path.add(0,new LatLon(loc.getLat(),loc.getLon()));
			//Reverse the order by always adding at 0
		}
		return result;
	}
	
	private void findNodes(LatLon startLoc, LatLon endLoc) {
		
		double startDist= Double.MAX_VALUE;
		double endDist= Double.MAX_VALUE;
			
		double d=0;
		//Find closest node in graph
		for(RouterNode cur : myGraph.getNodes() ) {
			d = cur.getLocation().getDist(startLoc);
			if (d < startDist) {
				startDist = d;
				startNode = cur;
			}
			d = cur.getLocation().getDist(endLoc);
			if (d < endDist) {
				endDist = d;
				endNode = cur;
			}
		}
	}
}
