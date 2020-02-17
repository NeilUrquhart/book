package book.ch6.algorithms;

import java.util.ArrayList;

import book.ch6.data.Graph;
import book.ch6.data.LatLon;
import book.ch6.data.Node;
import book.ch6.data.Route;

public interface RoutingAlgorithm {
	
	
	public abstract void setRoute(Route aRoute);
	public abstract void findRoute();
	public abstract ArrayList<LatLon> getLocations();
	public abstract ArrayList<String> getStreets();
	public abstract double getDist();

}