package book.ch6.data;

import java.util.ArrayList;

import book.ch6.algorithms.Dijkstra;

public class Route {
	private Node start;
	private Node finish;
	private ArrayList<LatLon> locations = new ArrayList<LatLon>();
	private ArrayList<String> ways = new ArrayList<String>();
	private double dist=0;
	private Graph myGraph;
	
	public Route(Graph myGraph, long start, long finish){
		this.myGraph = myGraph;
	
		this.start = myGraph.getNode(start);
		this.finish = myGraph.getNode(finish);
	}
	
	public void buildRoute(Dijkstra algorithm){
		algorithm.setData(myGraph);
		algorithm.findRoute(start.getId(), finish.getId());
		dist = algorithm.getDist();
		locations = algorithm.getLocations();
		ways = algorithm.getStreets();
		
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

	public Node getStart() {
		return start;
	}

	public Node getFinish() {
		return finish;
	}
	
	
	
}
