package book.ch6.algorithms;

import java.util.ArrayList;

import book.ch6.data.Graph;
import book.ch6.data.LatLon;
import book.ch6.data.Node;
import book.ch6.data.Route;

public class DijkstraBiDirectional implements RoutingAlgorithm {	
	private Dijkstra  forward;
	private Dijkstra  reverse;
	
	
	@Override
	public void setRoute(Route r){		
		forward = new Dijkstra();
		reverse = new Dijkstra();
		forward.setRoute(r);            
		reverse.setRoute(r.reverse()); 
		
	}

	@Override
	public void findRoute() {
		boolean done = false;
		while(!done){
			Node fCurrent = forward.step();			
			Node rCurrent = reverse.step();

			if (rCurrent==fCurrent){
				forward.setFinish(rCurrent);
				reverse.setFinish(fCurrent);
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
	

}
