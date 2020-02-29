package book.ch6.algorithms;

import java.util.ArrayList;

import book.ch6.data.Graph;
import book.ch6.data.LatLon;
import book.ch6.data.RouterNode;
import book.ch6.data.RouterWay;

public class Dijkstra extends DijkstraFlood {

	@Override
	public  void findPath(){
		while(unVisited.size() >0){
			if (step()==null)
				return;
		}
	}

	public ArrayList<RouterNode> step() {
		current = findMin(unVisited,dists);
		unVisited.remove(current);
		ArrayList<RouterNode> neighbours = myGraph.getNeighbours(current);
		for (RouterNode v : neighbours ){
			double alt = dists[current.getIndex()] + current.getDist(v);
			if (alt < dists[v.getIndex()]){
				dists[v.getIndex()] = alt;
				previous[v.getIndex()] = current;
				if (v.getId() == finish.getId()) {
					return null;
					
				}
			}
		}
		return neighbours;
	}
}
