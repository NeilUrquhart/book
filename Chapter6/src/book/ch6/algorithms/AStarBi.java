package book.ch6.algorithms;

import java.util.ArrayList;

import book.ch6.data.Graph;
import book.ch6.data.LatLon;
import book.ch6.data.Node;

public class AStarBi implements RoutingAlgorithm {

	private Graph myGraph;
	private Node join;
	private Long sourceID;
	private Long destID;
	//private Node dest;
	
	private AStarSlave  forward;
	private AStarSlave  reverse;
	
	@Override
	public void setData(Graph aGraph) {
		myGraph = aGraph;
		
		forward = new AStarSlave();
		reverse = new AStarSlave();
		
		forward.setData(myGraph);
		reverse.setData(myGraph);
		
		
	}
	
	@Override
	public void setEnds(long sourceID, long destID) {
		if (!myGraph.nodeExists(sourceID)){
			System.out.println("Source not used");
			return;
		}
		
		if (!myGraph.nodeExists(destID)){
			System.out.println("Dest not used");
			return;
		}
		this.sourceID = sourceID;
		this.destID = destID;
		
		forward.initialise(sourceID, destID);            
		reverse.initialise(destID, sourceID); 
		
	}

	@Override
	public void findRoute() {
		boolean done = false;
		while(!done){
				ArrayList<Node> fNeighbours = forward.stepN();
								
				ArrayList<Node> rNeighbours= reverse.stepN();
				
				for (Node f : fNeighbours){
					if (rNeighbours.indexOf(f)>-1){
						join = f;
						forward.setDest(join);
						reverse.setDest(join);
						done = true;
						break;
					}
						
				}
				
		}
		
	}

	@Override
	public ArrayList<LatLon> getLocations() {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		ArrayList<LatLon> res= forward.getLocations();
		
		for (int count = reverse.getLocations().size()-1;count >=0; count--){
			res.add(reverse.getLocations().get(count));
		}
		return res;
	}

	@Override
	public ArrayList<String> getStreets() {
		// TODO Auto-generated method stub
		ArrayList<String> res= forward.getStreets();
		
		for (int count = reverse.getStreets().size()-1;count >=0; count--){
			res.add(reverse.getStreets().get(count));
		}
		return res;
	}

	@Override
	public double getDist() {
		// TODO Auto-generated method stub
		
		return forward.getDist() + reverse.getDist();
	}
	
	private class AStarSlave extends AStar{
		
		public ArrayList<Node> stepN() {
			Node u = findClosest(q);
			q.remove(u);
			ArrayList<Node> neighbours = myGraph.getNeighbours(u);
			for (Node v : neighbours){
				if (q.indexOf(v)>-1){
					double alt = dists[u.getIndex()] + u.getDist(v);
					if (alt < dists[v.getIndex()]){
						dists[v.getIndex()] = alt;
						prev[v.getIndex()] = u;
					}
				}
			}
			return neighbours;
		}
	}

}
