package book.ch6.data;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import book.ch6.parse.Parser;

public class Graph {
	private static HashMap<Long,Node> nodeList = new HashMap<Long,Node>();
	private static HashMap<Long,Way> wayList = new HashMap<Long,Way>();
	private static Way[][] links;
	private static int nodeCount=0;

	public Graph(String[] files){
		for (String fName: files)
			Parser.loadJSON(fName,this);


		System.out.println("Nodes = " + nodeList.size());
		nodeList = removeUnused();
		System.out.println("Nodes = " + nodeList.size());

		
		
		links = new Way[nodeCount][];
		for (int c=0; c < nodeCount;c++)
			links[c] = new Way[nodeCount];

		System.out.println("Stats:");
		System.out.println("Nodes = " + nodeList.size());
		System.out.println("Ways = " + wayList.size());

		//Add ways  to links
		for (Way w : wayList.values()){
			ArrayList<Node> nodes = w.getNodes();
			for (int x=0; x < nodes.size(); x++){
				for (int y=0; y < nodes.size(); y++){
					if (x != y){
						System.out.println(w.getName() + ":"+ nodes.get(x) + ":" +nodes.get(y));
						int idx1 = nodes.get(x).getIndex();
						int idx2 = nodes.get(y).getIndex();
						System.out.println(idx1 +":"+idx2);
						links[idx1][idx2] = w;
						links[idx2][idx1] = w;
					}
				}	 
			}
		}
	}
	
	public static int getNodeCount(){
		return nodeCount;
	}
	
	public static void addNode(Node n){
		if (nodeList.containsKey(n.getId()))
			return;
		
		nodeList.put(n.getId(), n);
		nodeCount++;
	}
	
	public static void addWay(Way w){
		if (wayList.containsKey(w.getID()))
			return;
		wayList.put(w.getID(),w);
	}
	
	public static boolean nodeExists(long id){
		return nodeList.containsKey(id);
	}

	public static Node getNode(long id){
		return nodeList.get(id);
	}
	
	public static int nodeCount(){
		return nodeCount;
	}
	
	public static Way getWay(Node x, Node y){
		if ((x==null)||(y==null))
			return null;
		
		return links[x.getIndex()][y.getIndex()];
	}



	
	public Collection<Node> getNodes(){
		return nodeList.values();
	}
	private static HashMap<Long,Node>removeUnused(){
		HashMap<Long,Node> res= new HashMap<Long,Node>();

		for(Node n: nodeList.values()){
			if (n.getUsed())
				res.put(n.getId(),n);
		}
		return res;
	}
	
	public static ArrayList<Node> getNeighbours(Node node) {
		ArrayList<Node> result = new ArrayList<Node>();
		Way[] linkedTo = links[node.getIndex()];
		for (int c=0; c < linkedTo.length; c++ ){
			if (linkedTo[c] != null){
				//System.out.println(linkedTo[c].getName());
				result.addAll(linkedTo[c].getNodes());
			}
		}
		return result;
	}

	
}
