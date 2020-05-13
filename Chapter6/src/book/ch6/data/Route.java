package book.ch6.data;

import java.util.ArrayList;

import book.ch6.algorithms.RoutingAlgorithm;
/*
 * The Route Object provides a Facade to an application 
 * that wishes to make use of our routing engine.
 */
public class Route {
	protected RouterNode start;
	protected RouterNode finish;
	protected ArrayList<LatLon> locations = new ArrayList<LatLon>();
	protected ArrayList<String> ways = new ArrayList<String>();
	private double dist=0;
	private Graph myGraph;

	public Route(){
		
	}
	public Route(Graph myGraph, long start, long finish){
		//Find a path from <start> to <finish> through <myGraph>
		this.myGraph = myGraph;
		this.start = myGraph.getNode(start);
		this.finish = myGraph.getNode(finish);
	}

	public Route reverse(){
		//Create a new Route object that is the Reverse of this one
		return new Route(this.myGraph, this.getFinish().getId(), this.getStart().getId());
	}

	public void buildRoute(RoutingAlgorithm algorithm){
		//Find the desired path using <algorithm>
		algorithm.setRoute(this);
		algorithm.findPath();
		dist = algorithm.getDist();
		locations = algorithm.getLocations();
		ways = algorithm.getRoadNames();
	}
	//Get/set methods for properties
	public Graph getGraph(){
		return myGraph;
	}
	public ArrayList<LatLon> getLocations() {
		return locations;
	}

	public void setLocations(ArrayList<LatLon> locations) {
		this.locations = locations;
	}

	public ArrayList<String> getWays() {
		return ways;
	}

	public void setWays(ArrayList<String> ways) {
		this.ways = ways;
	}

	public double getDist() {
		return dist;
	}

	public void setDist(double dist) {
		this.dist = dist;
	}

	public RouterNode getStart() {
		return start;
	}

	public RouterNode getFinish() {
		return finish;
	}
	public void setFinish(RouterNode fin){
		finish = fin;
	}
}
