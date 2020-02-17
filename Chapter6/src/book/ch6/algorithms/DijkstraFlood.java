package book.ch6.algorithms;

import java.util.ArrayList;

import book.ch6.data.Graph;
import book.ch6.data.LatLon;
import book.ch6.data.Node;
import book.ch6.data.Route;
import book.ch6.data.Way;

public class DijkstraFlood extends RoutingAlgorithm {
	protected ArrayList<Node> unVisited;
	
	@Override
	public  void findPath(){
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
	

	
}
