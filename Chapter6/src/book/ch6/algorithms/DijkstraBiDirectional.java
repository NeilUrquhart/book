package book.ch6.algorithms;

import java.util.ArrayList;

import book.ch6.data.Graph;
import book.ch6.data.LatLon;
import book.ch6.data.Node;

public class DijkstraBiDirectional implements RoutingAlgorithm {

	private Graph myGraph;
	private Node join;
	
	private DijkstraSlave  forward;
	private DijkstraSlave  reverse;
	
	@Override
	public void initialise(Graph aGraph) {
		myGraph = aGraph;
		
		forward = new DijkstraSlave();
		reverse = new DijkstraSlave();
		
		forward.initialise(myGraph);
		reverse.initialise(myGraph);
		
		
	}
	
	@Override
	public void setRoute(Node start, Node finish ){
		if (!myGraph.nodeExists(start)){
			System.out.println("Start not found");
			return;
		}
		
		if (!myGraph.nodeExists(finish)){
			System.out.println("Finish not found");
			return;
		}
		
		forward.initialise(start, finish);            
		reverse.initialise(finish, start); 
		
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
						forward.setFinish(join);
						reverse.setFinish(join);
						done = true;
						break;
					}
						
				}
		}
		
	}

	@Override
	public ArrayList<LatLon> getLocations() {
		ArrayList<LatLon> res= forward.getLocations();
		
		for (int count = reverse.getLocations().size()-1;count >=0; count--){
			res.add(reverse.getLocations().get(count));
		}
		return res;
	}

	@Override
	public ArrayList<String> getStreets() {
		ArrayList<String> res= forward.getStreets();
		
		for (int count = reverse.getStreets().size()-1;count >=0; count--){
			res.add(reverse.getStreets().get(count));
		}
		return res;
	}

	@Override
	public double getDist() {
		return forward.getDist() + reverse.getDist();
	}
	
	private class DijkstraSlave extends DijkstraMod{
		
		public ArrayList<Node> stepN() {
			Node u = findMin(current,dists);
			current.remove(u);
			ArrayList<Node> neighbours = myGraph.getNeighbours(u);
			for (Node v : neighbours){
				if (current.indexOf(v)>-1){
					double alt = dists[u.getIndex()] + u.getDist(v);
					if (alt < dists[v.getIndex()]){
						dists[v.getIndex()] = alt;
						previous[v.getIndex()] = u;
					}
				}
			}
			return neighbours;
		}
	}
}
