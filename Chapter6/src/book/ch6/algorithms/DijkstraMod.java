package book.ch6.algorithms;

import java.util.ArrayList;

import book.ch6.data.Graph;
import book.ch6.data.LatLon;
import book.ch6.data.Node;
import book.ch6.data.Way;

public class DijkstraMod implements RoutingAlgorithm {
	private Graph myGraph;
	protected Node source;
	protected Node dest;
	protected Node prev[] ;
	protected double dists[];
	protected ArrayList<Node> q;
	
	/* (non-Javadoc)
	 * @see book.ch6.algorithms.RoutingAlgorithm#setData(book.ch6.data.Graph)
	 */
	@Override
	public  void setData(Graph aGraph){
		myGraph = aGraph;
	}
	
	public void setSource(Node s){
		this.source = s;
	}
	
	public void setDest(Node d){
		this.dest = d;
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
		
		initialise(sourceID, destID);            
		
	}
	
	/* (non-Javadoc)
	 * @see book.ch6.algorithms.RoutingAlgorithm#findRoute(long, long)
	 */
	@Override
	public  void findRoute(){
		
		while(q.size() >0){
			if (step())
				return;
		}
		
	}

	
	public boolean step() {
		Node u = findMin(q,dists);
		q.remove(u);

		for (Node v : myGraph.getNeighbours(u)){
			if (q.indexOf(v)>-1){
				double alt = dists[u.getIndex()] + u.getDist(v);
				if (alt < dists[v.getIndex()]){
					dists[v.getIndex()] = alt;
					prev[v.getIndex()] = u;
					if (v.getId() == dest.getId())
						return true;
				}
			}
		}
		return false;
	}

	public void initialise(long sourceID, long destID) {
		source = myGraph.getNode(sourceID);
		dest = myGraph.getNode(destID);
		dists = new double[myGraph.nodeCount()];
		prev = new Node[myGraph.nodeCount()];
		q = new ArrayList<Node>();

		for (int v=0; v < myGraph.nodeCount(); v++){
			dists[v]    =  Double.MAX_VALUE;              
			prev[v] = null;                 
		}
		
		q.addAll(myGraph.getNodes());
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
			current = prev[current.getIndex()];
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
			current = prev[current.getIndex()];
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
			current = prev[current.getIndex()];
		}
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
}
