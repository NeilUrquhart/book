package book.ch6.algorithms;

import java.util.ArrayList;

import book.ch6.data.Graph;
import book.ch6.data.LatLon;
import book.ch6.data.Node;
import book.ch6.data.Route;
import book.ch6.data.Way;

public class AStar  extends RoutingAlgorithm  {
	protected ArrayList<Node> open;
	
	@Override
	public void setRoute(Route aRoute) { 
		theRoute = aRoute;
		start = aRoute.getStart();
		finish = aRoute.getFinish();
		myGraph = theRoute.getGraph();
		dists = new double[myGraph.nodeCount()];
		previous = new Node[myGraph.nodeCount()];

		for (int v=0; v < myGraph.nodeCount(); v++){
			dists[v]    =  Double.MAX_VALUE;              
			previous[v] = null;                 
		}
		
		dists[start.getIndex()] = 0;
		current = start;
		//
		open = new ArrayList<Node>();
		open.add(current);
				
	}
	
	@Override
	public  void findPath(){
		open.add(start);
		while(open.size() >0){
			if (step()==null)
				return;
		}	
	}
	
	public Node step() {
		for (Node v : myGraph.getNeighbours(current)){
				double alt = dists[current.getIndex()] + current.getDist(v);
				if (alt < dists[v.getIndex()]){
					if (!open.contains(v))
						open.add(v);
					dists[v.getIndex()] = alt;
					previous[v.getIndex()] = current;
					if (v.getId() == finish.getId())
						return null;
				}
		}
		current = findClosest(open);
		open.remove(current);
		return  current;
	}

	protected  Node findClosest(ArrayList<Node> data){
		double best = Double.MAX_VALUE;
		Node res = null;
		for (Node current : data){
			double estim = dists[current.getIndex()] + current.getDist(finish);
			if (estim <= best){
				res = current;
				best = estim;
			}
		}
		return res;
	}	
}
