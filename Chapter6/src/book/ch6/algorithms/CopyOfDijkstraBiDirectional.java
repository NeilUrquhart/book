package book.ch6.algorithms;

import java.util.ArrayList;

import book.ch6.data.Graph;
import book.ch6.data.LatLon;
import book.ch6.data.Node;
import book.ch6.data.Route;

public class CopyOfDijkstraBiDirectional extends RoutingAlgorithm {	
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
			ArrayList<Node> fCurrent = forward.step();			
			ArrayList<Node> rCurrent = reverse.step();
			for (Node join: fCurrent) {
				if (rCurrent.contains(join)){
					forward.updateFinish(join);
					reverse.updateFinish(join);
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
