package book.ch6.algorithms;

import java.util.ArrayList;

import book.ch6.data.Graph;
import book.ch6.data.LatLon;
import book.ch6.data.Node;
import book.ch6.data.Route;

public class AStarBiDirectional extends RoutingAlgorithm {
	private Node join;	
	private AStar  forward;
	private AStar  reverse;
	
	@Override
	public void setRoute(Route r) {
		forward = new AStar();
		reverse = new AStar();
		forward.setRoute(r);            
		reverse.setRoute(r.reverse());
	}

	@Override
	public void findPath() {
		boolean done = false;
		forward.open = new ArrayList<Node>();
		forward.open.add(forward.start);

		reverse.open = new ArrayList<Node>();
		reverse.open.add(reverse.start);

		while(!done){
			Node fCurrent = forward.step();			
			Node rCurrent = reverse.step();

			if (rCurrent==fCurrent){
				forward.updateFinish(rCurrent);
				reverse.updateFinish(fCurrent);
				done = true;
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
	public ArrayList<String> getDirections() {
		ArrayList<String> res= forward.getDirections();
		
		for (int count = reverse.getDirections().size()-1;count >=0; count--){
			res.add(reverse.getDirections().get(count));
		}
		return res;
	}

	@Override
	public double getDist() {
			return forward.getDist() + reverse.getDist();
	}
	

}
