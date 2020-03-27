package book.ch6.algorithms;

import java.util.ArrayList;

import book.ch6.data.RouterNode;

public class DijkstraFindUpLink extends Dijkstra 
{
	private Long[] uplinks;
	
	public void setlinks(Long[] links){
		uplinks = links;
	}

	private boolean linked (long link){
		for (long n : uplinks)
		  if (n==link)
			  return true;

		 return false;
					  
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
				//if (v.getId() == finish.getId()) {
				//	return null;
				//}
//				if (linked(v.getId())){
//					super.theRoute.setFinish(v);
//					this.setFinish(v);
//					return null;
//				}
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
