package book.ch8.algorithms;

import java.util.ArrayList;

import book.ch8.data.Graph;
import book.ch8.data.Route;
import book.ch8.data.RouterNode;
import book.ch8.data.RouterWay;

public class AStar  extends RoutingAlgorithm  {
	protected ArrayList<RouterNode> open;
	
	@Override
	public void setRoute(Route aRoute) { 
		theRoute = aRoute;
		start = aRoute.getStart();
		finish = aRoute.getFinish();
		myGraph = theRoute.getGraph();
		dists = new double[myGraph.nodeCount()];
		previous = new RouterNode[myGraph.nodeCount()];

		for (int v=0; v < myGraph.nodeCount(); v++){
			dists[v]    =  Double.MAX_VALUE;              
			previous[v] = null;                 
		}
		
		dists[start.getIndex()] = 0;
		current = start;
		//
		open = new ArrayList<RouterNode>();
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
	
	public RouterNode step() {
		for (RouterNode v : myGraph.getNeighbours(current)){
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

	protected  RouterNode findClosest(ArrayList<RouterNode> data){
		double best = Double.MAX_VALUE;
		RouterNode res = null;
		for (RouterNode current : data){
			double estim = dists[current.getIndex()] + current.getDist(finish);
			if (estim <= best){
				res = current;
				best = estim;
			}
		}
		return res;
	}	
}
