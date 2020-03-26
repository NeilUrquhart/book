package book.ch6.hierarchy;

import java.util.ArrayList;

import book.ch6.data.Graph;
import book.ch6.data.Route;



public class MultiRoute extends Route {

	public MultiRoute(Graph myGraph, long start, long finish) {
		super(myGraph, start, finish);
	}
	
	public MultiRoute(Route r){
		super(r.getGraph(),r.getStart().getId(), r.getFinish().getId());
		this.append(r);
	}
	public void append(Route appendRoute){
		super.locations.addAll(reverse(appendRoute.getLocations()));
		super.ways.addAll(reverse(appendRoute.getWays()));
		super.setDist(super.getDist()+appendRoute.getDist());
		super.finish = appendRoute.getFinish();
	}
	
	private ArrayList reverse(ArrayList in){
		ArrayList res = new ArrayList();
		while(in.size() >0)
			res.add(in.remove(in.size()-1));
		
		return res;
	}

}
