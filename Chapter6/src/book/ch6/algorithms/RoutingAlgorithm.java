package book.ch6.algorithms;

import java.util.ArrayList;

import book.ch6.data.Graph;
import book.ch6.data.LatLon;
import book.ch6.data.RouterNode;
import book.ch6.data.Route;
import book.ch6.data.RouterWay;
/*
 * An abstract class used as the basis for the implementation 
 * of Routing algorithms.
 * Copyright Neil Urquhart 2020
 */
public abstract class RoutingAlgorithm {
	protected RouterNode previous[];// Pointer to previous node
	protected double dists[];//Distance to each node
	protected Route theRoute;//The Route 
	protected Graph myGraph;//The underlying street graph
	protected RouterNode start;//The node to start from
	protected RouterNode finish;//The node to finish at
	protected RouterNode current;//The node at current step
	
	public abstract void findPath();//Find a path from start to 
									//finish, by calling step() repeatedly
	public abstract Object step();//Run one step of the algorithm
	
	public void setRoute(Route aRoute){
		//aRoute contains details of the path to be found.
		this.theRoute = aRoute;
		this.myGraph = aRoute.getGraph();
		this.start = aRoute.getStart();
		this.finish = aRoute.getFinish();		
	}
	
	public void setFinish(RouterNode f){
		  finish = f;
	}

	public ArrayList<LatLon> getLocations(){
		//Return a list of LatLon objects that represent the path
		ArrayList<LatLon> res = new ArrayList<LatLon>();
		RouterNode current = finish;
		while (current != start){
			res.add(res.size(),current.getLocation());
			//add at end to reverse order
			current = previous[current.getIndex()];
		}
		return res;
	}
	
	public ArrayList<String> getRoadNames(){
		//Return the  road names that comprise the path found 
		ArrayList<String> res = new ArrayList<String>();
		RouterNode old = null;
		RouterNode current = finish;
		while (current != start){
			RouterWay currentWay = myGraph.getWay(current,old);
			if (currentWay != null)
				res.add(res.size(),currentWay.getName());
			//Add in reverse order
			old = current;
			current = previous[current.getIndex()];
		}
		return res;
	}

	public double getDist(){
		//Get the distance of the path found by the algorithm
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