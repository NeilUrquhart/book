package book.ch8.algorithms;

import java.util.ArrayList;

import book.ch8.data.RouterNode;

public class DijkstraFindUpLink extends Dijkstra 
{
	private Long[] uplinks;
	
	public void setlinks(Long[] links){
		uplinks = links;
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
				if (v.isUplink()){
					super.theRoute.setFinish(v);
					this.setFinish(v);
					return null;
				}
			}
		}
		return neighbours;
	}
}
