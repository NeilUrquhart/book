package book.ch6.algorithms;

import java.util.ArrayList;

import book.ch6.data.Graph;
import book.ch6.data.LatLon;
import book.ch6.data.RouterNode;
import book.ch6.data.Route;

public class DijkstraBiDirectional extends RoutingAlgorithm {	
	private Dijkstra  forward;
	private Dijkstra  reverse;
	
	@Override
	public void setRoute(Route r){	
		this.finish = r.getFinish();
		this.start = r.getStart();
		forward = new Dijkstra();
		reverse = new Dijkstra();
		forward.setRoute(r);            
		reverse.setRoute(r.reverse()); 
	}

	@Override
	public void findPath() {
		boolean done = false;
		forward.current = this.start;
		reverse.current = this.finish;
		while(!done){
			ArrayList<RouterNode> fCurrent = forward.step();			
			ArrayList<RouterNode> rCurrent = reverse.step();
			for (RouterNode join: fCurrent) {
				if (rCurrent.contains(join)){
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
