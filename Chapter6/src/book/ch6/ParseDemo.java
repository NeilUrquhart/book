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
	private static ArrayList<Long> nodeIDIndex = new ArrayList<Long>();
	private static ArrayList<Way> wayList = new ArrayList<Way>();
	private static Way[][] links;

	public static void main(String[] args) {

		JSONParser parser = new JSONParser();

		try (Reader reader = new FileReader("Edin.dat")) {

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
					processNode(obj);
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

			// for (Way w : wayList.values()){
			// 	System.out.println(w);
			//}
			links = new Way[nodeIDIndex.size()][];
			for (int c=0; c < nodeIDIndex.size();c++)
				links[c] = new Way[nodeIDIndex.size()];

			System.out.println("Stats:");
			System.out.println("Nodes = " + nodeList.size());
			System.out.println("Ways = " + wayList.size());
			
			//Add ways  to links
			for (Way w : wayList){
				long[] nodes = w.getNodeIDs();
				for (int x=0; x < nodes.length; x++){
					for (int y=0; y < nodes.length; y++){
						if (x != y){
							System.out.println(w.getName() + ":"+ nodes[x] + ":" +nodes[y]);
							int idx1 = nodeIDIndex.indexOf(nodes[x]);
							int idx2 = nodeIDIndex.indexOf(nodes[y]);
							System.out.println(idx1 +":"+idx2);
							links[idx1][idx2] = w;
							links[idx2][idx1] = w;
						}
					}	 
				}
			}
			
			/*
			//pick a node..
			System.out.println("Testing node");
			Node start = nodeList.get((long)14015492);
			System.out.println("Start node" + start);
			for (Node n : getNeighbours(start.getId())){
				System.out.println(n);
			}*/
			
			dijkstra();
			
			

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}
	
	private static void dijkstra(){
		//test Dijkstra
		Node start = nodeList.get((long)14015492);
		/*
		 * 
		 *  1  function Dijkstra(Graph, source):
 2
 3      create vertex set Q
 4
 5      for each vertex v in Graph:             
 6          dist[v] ← INFINITY                  
 7          prev[v] ← UNDEFINED                 
 8          add v to Q                      
10      dist[source] ← 0                        
11      
12      while Q is not empty:
13          u ← vertex in Q with min dist[u]    
14                                              
15          remove u from Q 
16          
17          for each neighbor v of u:           // only v that are still in Q
18              alt ← dist[u] + length(u, v)
19              if alt < dist[v]:               
20                  dist[v] ← alt 
21                  prev[v] ← u 
22
23      return dist[], prev[]

1  S ← empty sequence
2  u ← target
3  if prev[u] is defined or u = source:          // Do something only if the vertex is reachable
4      while u is defined:                       // Construct the shortest path with a stack S
5          insert u at the beginning of S        // Push the vertex onto the stack
6          u ← prev[u]                           // Traverse from target to source
		 */
		
	}
	private static ArrayList<Node> getNeighbours(long node) {
		int id = nodeIDIndex.indexOf(node);
		ArrayList<Node> result = new ArrayList<Node>();
		Way[] linkedTo = links[id];
		for (int c=0; c < linkedTo.length; c++ ){
			if (linkedTo[c] != null){
				//System.out.println(linkedTo[c].getName());
				result.addAll(linkedTo[c].getNodes());
			}
		}
		return result;
	}
	
	public static void processNode(JSONObject node){
		Long id = (Long) node.get("id");
		System.out.println("Loading " + id);
		Double lat= (Double) node.get("lat");
		Double lon = (Double) node.get("lon");
		JSONObject tags = (JSONObject) node.get("tags");
		Node newNode = new Node(id,lat,lon);
		nodeList.put(id, newNode);		
		nodeIDIndex.add(id);
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
