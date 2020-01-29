package book.ch6;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/*
 * https://code.google.com/archive/p/json-simple/
 */

public class ParseDemo {
	private static HashMap<Long,Node> nodeList = new HashMap<Long,Node>();
	private static ArrayList<Way> wayList = new ArrayList<Way>();
	private static Way[][] links;
	private static int nodeCount=0;

	public static void main(String[] args) {

		JSONParser parser = new JSONParser();

		try (Reader reader = new FileReader("bigEdin.dat.json")) {

			JSONObject jsonObject = (JSONObject) parser.parse(reader);
			System.out.println(jsonObject);

			Double ver = (Double) jsonObject.get("version");
			System.out.println(ver);


			// loop array
			System.out.println("Loading Nodes");
			
			JSONArray elements = (JSONArray) jsonObject.get("elements");
			Iterator<String> iterator = elements.iterator();
			while (iterator.hasNext()) {
				Object current =  iterator.next();

				org.json.simple.JSONObject obj = (org.json.simple.JSONObject) current;
				String type = (String) obj.get("type");
				if (type.equals("node")){
					processNode(obj,nodeCount);
					nodeCount++;
				}


			}
			
			System.out.println("Loading Ways");
			elements = (JSONArray) jsonObject.get("elements");
			iterator = elements.iterator();
			while (iterator.hasNext()) {
				Object current =  iterator.next();

				org.json.simple.JSONObject obj = (org.json.simple.JSONObject) current;
				String type = (String) obj.get("type");
				if (type.equals("way"))
					processWay(obj);

			}

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
			for (Way w : wayList){
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
			//Princes St, Findlay Gdns
			dijkstra(4032301786L,38827957L);
			
			

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}
	
	private static HashMap<Long,Node>removeUnused(){
		HashMap<Long,Node> res= new HashMap<Long,Node>();
		
		for(Node n: nodeList.values()){
			if (n.getUsed())
				res.put(n.getId(),n);
		}
		return res;
	}
	private static void dijkstra(long sourceID, long destID){
		if (!nodeList.containsKey(sourceID)){
			System.out.println("Source not used");
			return;
		}
		if (!nodeList.containsKey(destID)){
			System.out.println("Dest not used");
			return;
		}

		//test Dijkstra
		Node source = nodeList.get(sourceID);
		System.out.println("Running Dijkstra");
		
		double dists[] = new double[nodeCount];
		Node prev[] = new Node[nodeCount];
		
//       create vertex set Q
		ArrayList<Node> q = new ArrayList<Node>();
 
 //     for each vertex v in Graph:             
		for (int v=0; v < nodeCount; v++){
           dists[v]    =  Double.MAX_VALUE;              
           prev[v] = null;                 
               
		}
		q.addAll(nodeList.values());
		
     dists[source.getIndex()] = 0;            
     System.out.println("Done setup");
		
      
//12      while Q is not empty:
     System.out.println("Processing");
     while(q.size() >0){
    	 System.out.println("Q=" + q.size());
//13          u ← vertex in Q with min dist[u]    
    	Node u = findMin(q,dists);
                              
//15          remove u from Q 
    	q.remove(u);

    	ArrayList<Node> neighbours = getNeighbours(u);
    	
//17          for each neighbor v of u:           
    	// only v that are still in Q
    	for (Node v : neighbours){
    		if (q.indexOf(v)>-1){
    	
//18              alt ← dist[u] + length(u, v)
    			double alt = dists[u.getIndex()] + u.getDist(v);
//19              if alt < dist[v]:              
    			if (alt < dists[v.getIndex()]){
//20                  dist[v] ← alt 
    				 dists[v.getIndex()] = alt;
//21                  prev[v] ← u 
    				 prev[v.getIndex()] = u;
    			}
    		}
    	}
//22
     }
//23      return dist[], prev[]
//
//1  S ← empty sequence
//2  u ← target
//3  if prev[u] is defined or u = source:          // Do something only if the vertex is reachable
//4      while u is defined:                       // Construct the shortest path with a stack S
//5          insert u at the beginning of S        // Push the vertex onto the stack
//6          u ← prev[u]                           // Traverse from target to source
//		 
     System.out.println("Finding route");
     
     Node current = nodeList.get(destID);
	 while (current != source){
		 System.out.println(current.getCoords());
		 
		 current = prev[current.getIndex()];

	 }
	 
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
		
	private static ArrayList<Node> getNeighbours(Node node) {
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
	
	public static void processNode(JSONObject node, int index){
		Long id = (Long) node.get("id");
		System.out.println("Loading " + id);
		Double lat= (Double) node.get("lat");
		Double lon = (Double) node.get("lon");
		JSONObject tags = (JSONObject) node.get("tags");
		Node newNode = new Node(id,lat,lon);
		nodeList.put(id, newNode);		
		newNode.setIndex(index);
	}

	public static void processWay(JSONObject way){
		Long id = (Long) way.get("id");
		JSONObject tags = (JSONObject) way.get("tags");
		String name = (String)tags.get("name");
		String highway = (String)tags.get("highway");
		Way newWay = new Way(id,name,highway);
		wayList.add(newWay);
		//add nodes
		JSONArray elements = (JSONArray)  way.get("nodes");
		for (Object o : elements){
			long nodeId = (long)o;
			Node found = nodeList.get(nodeId);
			if (found!= null){
				newWay.addNode(found);
				found.setUsed();
			}
		}
	}

}
