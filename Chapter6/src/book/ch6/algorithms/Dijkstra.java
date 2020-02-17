package book.ch6.algorithms;

import java.util.ArrayList;

import book.ch6.data.Graph;
import book.ch6.data.LatLon;
import book.ch6.data.Node;
import book.ch6.data.Route;
import book.ch6.data.Way;

public class Dijkstra implements RoutingAlgorithm {
	protected Node previous[] ;
	protected double dists[];
	protected ArrayList<Node> unVisited;
	protected Route theRoute;
	protected Graph myGraph;
	protected Node start;
	protected Node finish;
	protected Node current;
	
	@Override
	public  void findRoute(){
		while(unVisited.size() >0){
			step();
		}
	}

	public Node step() {
		ArrayList<Node> neighbours = myGraph.getNeighbours(current);
		for (Node v : neighbours){
				double alt = dists[current.getIndex()] + current.getDist(v);
				if (alt < dists[v.getIndex()]){
					dists[v.getIndex()] = alt;
					previous[v.getIndex()] = current;
				}
		}
		current = findMin(unVisited,dists);
		unVisited.remove(current);
		return current;
	}

	@Override
	public void setRoute(Route aRoute) { 
		theRoute = aRoute;
		start = aRoute.getStart();
		finish = aRoute.getFinish();
		myGraph = theRoute.getGraph();
		dists = new double[myGraph.nodeCount()];
		previous = new Node[myGraph.nodeCount()];
		unVisited = new ArrayList<Node>();

		for (int v=0; v < myGraph.nodeCount(); v++){
			dists[v]    =  Double.MAX_VALUE;              
			previous[v] = null;                 
		}
		
		unVisited.addAll(myGraph.getNodes());
		dists[start.getIndex()] = 0;
		current = start;
	}

	@Override
	public ArrayList<LatLon> getLocations(){
		ArrayList<LatLon> res = new ArrayList<LatLon>();
		current = finish;
		while (current != start){
			res.add(res.size(),current.getLocation());//add at end to reverse order
			current = previous[current.getIndex()];
		}
		return res;
	}
	
	@Override
	public ArrayList<String> getStreets(){
		ArrayList<String> res = new ArrayList<String>();
		Node old = null;
		current = finish;
		while (current != start){
			Way currentWay = myGraph.getWay(current,old);
			if (currentWay != null)
				res.add(res.size(),currentWay.getName());
			old = current;
			current = previous[current.getIndex()];
		}
		return res;
	}
	
	/* (non-Javadoc)
	 * @see book.ch6.algorithms.RoutingAlgorithm#getDist()
	 */
	@Override
	public double getDist(){
		double res=0;
		Node old = finish;
		current = old;
		while (current != start){
			res = res + current.getDist(old);
			old = current;
			current = previous[current.getIndex()];
		}
		res = res + current.getDist(old);
		return res;
	}
	
	protected  Node findMin(ArrayList<Node> data, double[] dists ){
		double best = Double.MAX_VALUE;
		Node res = null;
		for (Node current : data){
			if (dists[current.getIndex()] <= best){
				res = current;
				best = dists[current.getIndex()];
			}
		}
		return res;
	}
	
	public void setFinish(Node f){
	  finish = f;
}

	
	
}
