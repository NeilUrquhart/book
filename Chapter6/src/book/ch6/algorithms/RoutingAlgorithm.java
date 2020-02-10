package book.ch6.algorithms;

import java.util.ArrayList;

import book.ch6.data.Graph;
import book.ch6.data.LatLon;

public interface RoutingAlgorithm {
	
	public abstract void setEnds(long startID, long destID);

	public abstract void setData(Graph aGraph);

	public abstract void findRoute();

	public abstract ArrayList<LatLon> getLocations();

	public abstract ArrayList<String> getStreets();

	public abstract double getDist();

}