package book.ch6.data;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import book.ch6.parse.Parser;

public class Graph {
	private  HashMap<Long,Node> nodeList = new HashMap<Long,Node>();
	private HashMap<Long,Way> wayList = new HashMap<Long,Way>();
	private Way[][] links;
	private int nodeCount;
	
	public Graph(String[] files){
		for (String fName: files)
			Parser.loadJSON(fName,this);


		System.out.println("Nodes = " + nodeList.size());
		nodeList = removeUnused();
		System.out.println("Nodes = " + nodeList.size());

		nodeCount = nodeList.size();
		
		links = new Way[nodeCount][];
		for (int c=0; c < nodeCount;c++)
			links[c] = new Way[nodeCount];

		System.out.println("Stats:");
		System.out.println("Nodes = " + nodeList.size());
		System.out.println("Ways = " + wayList.size());

		//Add ways  to links
		String key = "";
		for (Way w : wayList.values()){
			System.out.println(w.getName());
			Node[] nodes = w.getNodes();
			for (int x=0; x < nodes.length; x++){
				System.out.println(w.getName());
				
				for (int y=0; y < (nodes.length-x); y++){
					if (x != y){
					
						links[nodes[x].getIndex()][nodes[y].getIndex()] = w;
					}
				}	 
			}
			
		}
	}
	
	public  int getNodeCount(){
		return nodeCount;
	}
	
	public  void addNode(Node n){
		if (nodeList.containsKey(n.getId()))
			return;
		
		nodeList.put(n.getId(), n);
		nodeCount++;
	}
	
	public  void addWay(Way w){
		if (wayList.containsKey(w.getID()))
			return;
		wayList.put(w.getID(),w);
	}
	
	public boolean nodeExists(long id){
		return nodeList.containsKey(id);
	}

	public Node getNode(long id){
		return nodeList.get(id);
	}
	
	public  int nodeCount(){
		return nodeCount;
	}
	
	public Way getWay(Node x, Node y){
		if ((x==null)||(y==null))
			return null;
		if (x.getIndex()<y.getIndex())
			return links[x.getIndex()][y.getIndex()];
		return links[y.getIndex()][x.getIndex()];
	}
	
	public Collection<Node> getNodes(){
		return nodeList.values();
	}
	private HashMap<Long,Node>removeUnused(){
		HashMap<Long,Node> res= new HashMap<Long,Node>();
		int index=0;
		for(Node n: nodeList.values()){
			if (n.getUsed()){
				n.setIndex(index);
				res.put(n.getId(),n);
				index++;
			}
		}
		return res;
	}
	
	public ArrayList<Node> getNeighbours(Node node) {
		ArrayList<Node> result = new ArrayList<Node>();
		Way[] linkedTo = links[node.getIndex()];
		for (int c=0; c < linkedTo.length; c++ ){
			if (linkedTo[c] != null){
				result.addAll(Arrays.asList(linkedTo[c].getNodes()));
			}
		}
		return result;
	}

	
}
