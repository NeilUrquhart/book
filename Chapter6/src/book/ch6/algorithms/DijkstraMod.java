package book.ch6.algorithms;

import java.util.ArrayList;

import book.ch6.data.Graph;
import book.ch6.data.LatLon;
import book.ch6.data.Node;
import book.ch6.data.Way;

public class DijkstraMod extends Dijkstra {

	@Override
	public  void findRoute(){
		while(current.size() >0){
			if (step())
				return;
		}
	}


	public boolean step() {
		Node u = findMin(current,dists);
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
}
