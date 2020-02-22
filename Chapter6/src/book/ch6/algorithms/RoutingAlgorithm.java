package book.ch6.algorithms;

import java.util.ArrayList;

import book.ch6.data.Graph;
import book.ch6.data.LatLon;
import book.ch6.data.Node;
import book.ch6.data.Route;
import book.ch6.data.Way;

public abstract class RoutingAlgorithm {
	protected Node previous[] ;
	protected double dists[];
	protected Route theRoute;
	protected Graph myGraph;
	protected Node start;
	protected Node finish;
	protected Node current;
	
	public abstract void findPath();
	
	public void setRoute(Route aRoute){
		this.theRoute = aRoute;
		this.myGraph = aRoute.getGraph();
		this.start = aRoute.getStart();
		this.finish = aRoute.getFinish();		
	}
	
	public void updateFinish(Node f){
		  finish = f;

	}
	
	public ArrayList<LatLon> getLocations(){
		ArrayList<LatLon> res = new ArrayList<LatLon>();
		Node current = finish;
		while (current != start){
			res.add(res.size(),current.getLocation());//add at end to reverse order
			current = previous[current.getIndex()];
		}
		return res;
	}
	
	public ArrayList<String> getDirections(){
		ArrayList<String> res = new ArrayList<String>();
		Node old = null;
		Node current = finish;
		while (current != start){
			Way currentWay = myGraph.getWay(current,old);
			if (currentWay != null)
				res.add(res.size(),currentWay.getName());
			old = current;
			current = previous[current.getIndex()];
		}
		return res;
	}

	public double getDist(){
		System.out.print("DEBUG: get dist");
		double res=0;
		Node old = finish;
		Node current = old;
		while (current != start){
			System.out.println("Current= "+ current);
			res = res + current.getDist(old);
			old = current;
			current = previous[current.getIndex()];
		}
		res = res + current.getDist(old);
		System.out.print("Done DEBUG: get dist");
		
		return res;
	}
}