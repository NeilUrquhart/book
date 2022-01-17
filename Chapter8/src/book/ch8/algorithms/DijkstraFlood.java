package book.ch8.algorithms;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import book.ch8.data.Graph;
import book.ch8.data.Route;
import book.ch8.data.RouterNode;
import book.ch8.data.RouterWay;

public class DijkstraFlood extends RoutingAlgorithm {
	protected ArrayList<RouterNode> unVisited;
	
	@Override
	public  void findPath(){
		while(unVisited.size() >0){
			step();
		}

	}

	public ArrayList<RouterNode> step() {
		ArrayList<RouterNode> neighbours = myGraph.getNeighbours(current);
		for (RouterNode v : neighbours){
		
				double alt = dists[current.getIndex()] + current.getDist(v);
		
				if (alt < dists[v.getIndex()]){
					dists[v.getIndex()] = alt;
					previous[v.getIndex()] = current;
				}
		}
				current = findMin(unVisited,dists);
				unVisited.remove(current);
		ArrayList<RouterNode> res = new ArrayList<RouterNode>();
		res.add(current);
		
		return  res;
	}

	@Override
	public void setRoute(Route aRoute) { 
		theRoute = aRoute;
		start = aRoute.getStart();
		finish = aRoute.getFinish();
		myGraph = theRoute.getGraph();
		dists = new double[myGraph.nodeCount()];
		previous = new RouterNode[myGraph.nodeCount()];
		unVisited = new ArrayList<RouterNode>();

		for (int v=0; v < myGraph.nodeCount(); v++){
			dists[v]    =  Double.MAX_VALUE;              
			previous[v] = null;                 
		}
		unVisited.addAll(myGraph.getNodes());
		dists[start.getIndex()] = 0;
		current = start;
	}

	protected  RouterNode findMin(ArrayList<RouterNode> data, double[] dists ){
		double best = Double.MAX_VALUE;
		RouterNode res = null;
		for (RouterNode current : data){
			if (dists[current.getIndex()] <= best){
				res = current;
				best = dists[current.getIndex()];
			}
		}
		return res;
	}
	

	
}
