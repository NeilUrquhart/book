package book.ch8.algorithms;

import java.util.ArrayList;

import book.ch1.LatLon;
import book.ch8.data.Graph;
import book.ch8.data.Route;
import book.ch8.data.RouterNode;

public class AStarBiDirectional extends RoutingAlgorithm {
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
		forward.open = new ArrayList<RouterNode>();
		forward.open.add(forward.start);

		reverse.open = new ArrayList<RouterNode>();
		reverse.open.add(reverse.start);

		while(!done){
			RouterNode fCurrent = forward.step();			
			RouterNode rCurrent = reverse.step();

			if (rCurrent==fCurrent){
				forward.setFinish(rCurrent);
				reverse.setFinish(fCurrent);
				done = true;
			}
		}
	}

	@Override
	public ArrayList<LatLon> getLocations() {
		ArrayList<LatLon> res= new ArrayList<LatLon>();
		
		for (int count = forward.getLocations().size()-1;count >=0; count--){
			res.add(forward.getLocations().get(count));
		}
		res.addAll(reverse.getLocations());
		
		
		return res;
	}

	@Override
	public ArrayList<String> getRoadNames() {
		ArrayList<String> res= forward.getRoadNames();
		
		for (int count = reverse.getRoadNames().size()-1;count >=0; count--){
			res.add(reverse.getRoadNames().get(count));
		}
		return res;
	}

	@Override
	public double getDist() {
			return forward.getDist() + reverse.getDist();
	}

	@Override
	public RouterNode step() {
		// Step() is not used in bi-directional algorithms
		return null;
	}
	

}
