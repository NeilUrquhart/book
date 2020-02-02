package book.ch6.algorithms;

import java.util.ArrayList;

import book.ch6.data.Graph;
import book.ch6.data.LatLon;
import book.ch6.data.Node;
import book.ch6.data.Way;

public class Dijkstra {
	private static Graph myGraph;
	private static Node source;
	private static Node dest;
	private static Node prev[] ;
	public static void setData(Graph aGraph){
		myGraph = aGraph;
	}
	
	public static void findRoute(long sourceID, long destID){

		
		if (!myGraph.nodeExists(sourceID)){
			System.out.println("Source not used");
			return;
		}
		
		if (!myGraph.nodeExists(destID)){
			System.out.println("Dest not used");
			return;
		}

		//test Dijkstra
		source = myGraph.getNode(sourceID);
		dest = myGraph.getNode(destID);
		System.out.println("Running Dijkstra");
		double dists[] = new double[myGraph.nodeCount()];
		prev = new Node[myGraph.nodeCount()];
		ArrayList<Node> q = new ArrayList<Node>();

		for (int v=0; v < myGraph.nodeCount(); v++){
			dists[v]    =  Double.MAX_VALUE;              
			prev[v] = null;                 
		}
		
		q.addAll(myGraph.getNodes());
		dists[source.getIndex()] = 0;            
		System.out.println("Done setup");
		System.out.println("Processing");
		while(q.size() >0){
			System.out.println("Q=" + q.size());
			Node u = findMin(q,dists);
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
		}
		
	}

	public ArrayList<LatLon> getLocations(){
		ArrayList<LatLon> res = new ArrayList<LatLon>();
		Node current = dest;
		while (current != source){
			res.add(res.size(),current.getLocation());//add at end to reverse order
			current = prev[current.getIndex()];
		}
		return res;
	}
	
	public ArrayList<String> getStreets(){
		ArrayList<String> res = new ArrayList<String>();
		Node old = null;
		Node current = dest;
		while (current != source){
			Way currentWay = Graph.getWay(current,old);
			if (currentWay != null)
				res.add(res.size(),currentWay.getName());
			old = current;
			current = prev[current.getIndex()];
		}
		return res;
	}
	
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
	
	private static Node findMin(ArrayList<Node> data, double[] dists ){
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
