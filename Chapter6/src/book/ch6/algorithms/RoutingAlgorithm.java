package book.ch6.algorithms;

import java.util.ArrayList;

import book.ch6.data.Graph;
import book.ch6.data.LatLon;
import book.ch6.data.RouterNode;
import book.ch6.data.Route;
import book.ch6.data.RouterWay;

public abstract class RoutingAlgorithm {
	protected RouterNode previous[] ;
	protected double dists[];
	protected Route theRoute;
	protected Graph myGraph;
	protected RouterNode start;
	protected RouterNode finish;
	protected RouterNode current;
	
	public abstract void findPath();
	
	public void setRoute(Route aRoute){
		this.theRoute = aRoute;
		this.myGraph = aRoute.getGraph();
		this.start = aRoute.getStart();
		this.finish = aRoute.getFinish();		
	}
	
	public void updateFinish(RouterNode f){
		  finish = f;

	}
	
	public ArrayList<LatLon> getLocations(){
		ArrayList<LatLon> res = new ArrayList<LatLon>();
		RouterNode current = finish;
		while (current != start){
			res.add(res.size(),current.getLocation());//add at end to reverse order
			current = previous[current.getIndex()];
		}
		return res;
	}
	
	public ArrayList<String> getDirections(){
		ArrayList<String> res = new ArrayList<String>();
		RouterNode old = null;
		RouterNode current = finish;
		while (current != start){
			RouterWay currentWay = myGraph.getWay(current,old);
			if (currentWay != null)
				res.add(res.size(),currentWay.getName());
			old = current;
			current = previous[current.getIndex()];
		}
		return res;
	}

	public double getDist(){
		double res=0;
		RouterNode old = finish;
		RouterNode current = old;
		while (current != start){
			res = res + current.getDist(old);
			old = current;
			current = previous[current.getIndex()];
		}
		res = res + current.getDist(old);	
		return res;
	}
}