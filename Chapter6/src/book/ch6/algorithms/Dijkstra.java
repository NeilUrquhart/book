package book.ch6.algorithms;

import java.util.ArrayList;

import book.ch6.data.Graph;
import book.ch6.data.LatLon;
import book.ch6.data.Node;
import book.ch6.data.Way;

public class Dijkstra implements RoutingAlgorithm {
	protected Graph myGraph;
	protected Node source;
	protected Node dest;
	protected Node previous[] ;
	protected double dists[];
	protected ArrayList<Node> current;
	
		@Override
	public  void initialise(Graph aGraph){
		myGraph = aGraph;
	}
	
	public void setSRoute(Node source, Node dest) {
		if (!myGraph.nodeExists(source)){
			System.out.println("Source not used");
			return;
		}
		
		if (!myGraph.nodeExists(dest)){
			System.out.println("Dest not used");
			return;
		}
		
		initialise(source, dest);            

		
	}

	@Override
	public  void findRoute(){
	
		
		while(current.size() >0){
			step();
		}
		
	}

	private void step() {
		Node u = findMin(current,dists);
		current.remove(u);

		for (Node v : myGraph.getNeighbours(u)){
			if (current.indexOf(v)>-1){
				double alt = dists[u.getIndex()] + u.getDist(v);
				if (alt < dists[v.getIndex()]){
					dists[v.getIndex()] = alt;
					previous[v.getIndex()] = u;
				}
			}
		}
	}

	protected void initialise(Node aSource, Node aDest) {
		source = aSource;
		dest = aDest;
		dists = new double[myGraph.nodeCount()];
		previous = new Node[myGraph.nodeCount()];
		current = new ArrayList<Node>();

		for (int v=0; v < myGraph.nodeCount(); v++){
			dists[v]    =  Double.MAX_VALUE;              
			previous[v] = null;                 
		}
		
		current.addAll(myGraph.getNodes());
		dists[source.getIndex()] = 0;
	}

	/* (non-Javadoc)
	 * @see book.ch6.algorithms.RoutingAlgorithm#getLocations()
	 */
	@Override
	public ArrayList<LatLon> getLocations(){
		ArrayList<LatLon> res = new ArrayList<LatLon>();
		Node current = dest;
		while (current != source){
			res.add(res.size(),current.getLocation());//add at end to reverse order
			current = previous[current.getIndex()];
		}
		return res;
	}
	
	/* (non-Javadoc)
	 * @see book.ch6.algorithms.RoutingAlgorithm#getStreets()
	 */
	@Override
	public ArrayList<String> getStreets(){
		ArrayList<String> res = new ArrayList<String>();
		Node old = null;
		Node current = dest;
		while (current != source){
			Way currentWay = myGraph.getWay(current,old);
			if (currentWay != null)
				res.add(res.size(),currentWay.getName());
			old = current;
			current = previous[current.getIndex()];
		}
		return res;
	}
	
	/* (non-Javadoc)
	 * @see book.ch6.algorithms.RoutingAlgorithm#getDist()
	 */
	@Override
	public double getDist(){
		double res=0;
		Node old = dest;
		Node current = dest;
		while (current != source){
			res = res + current.getDist(old);
			old = current;
			current = previous[current.getIndex()];
		}
		res = res + current.getDist(old);
		return res;
	}
	
	protected  Node findMin(ArrayList<Node> data, double[] dists ){
		double best = Double.MAX_VALUE;
		Node res = null;
		for (Node current : data){
			if (dists[current.getIndex()] <= best){
				res = current;
				best = dists[current.getIndex()];
			}
		}
		return res;
	}
	
	public void setFinish(Node d){
	this.dest = d;
}

	@Override
	public void setRoute(Node start, Node finish) {
		// TODO Auto-generated method stub
		
	}
}
