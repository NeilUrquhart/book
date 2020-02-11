package book.ch6.algorithms;

import java.util.ArrayList;

import book.ch6.data.Graph;
import book.ch6.data.LatLon;
import book.ch6.data.Node;
import book.ch6.data.Way;

public class AStar extends DijkstraMod  {

	@Override
	public  void findRoute(){
		while(current.size() >0){
			if (step())
				return;
		}	
	}
	@Override
	public boolean step() {
		Node u = findClosest(current);
		current.remove(u);
		for (Node v : myGraph.getNeighbours(u)){
			if (current.indexOf(v)>-1){
				double alt = dists[u.getIndex()] + u.getDist(v);
				if (alt < dists[v.getIndex()]){
					dists[v.getIndex()] = alt;
					previous[v.getIndex()] = u;
					if (v.getId() == dest.getId())
						return true;
				}
			}
		}
		return false;
	}

	protected  Node findClosest(ArrayList<Node> data){
		double best = Double.MAX_VALUE;
		Node res = null;
		for (Node current : data){
			double estim = dists[current.getIndex()] + current.getDist(dest);
			if (estim <= best){
				res = current;
				best = estim;
			}
		}
		return res;
	}	
}
