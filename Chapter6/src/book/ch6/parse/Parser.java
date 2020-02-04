package book.ch6.parse;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import book.ch6.data.Graph;
import book.ch6.data.LatLon;
import book.ch6.data.Node;
import book.ch6.data.Way;

public class Parser {
	public static void loadJSON(String fName, Graph graph) {
		JSONParser parser = new JSONParser();
		try (Reader reader = new FileReader(fName)) {
			JSONObject jsonObject = (JSONObject) parser.parse(reader);
			System.out.println(jsonObject);
			Double ver = (Double) jsonObject.get("version");
			System.out.println(ver);

			// loop array
			System.out.println("Loading Nodes");

			JSONArray elements = (JSONArray) jsonObject.get("elements");
			Iterator<String> iterator = elements.iterator();
			Object current;
			while (iterator.hasNext()) {
				current =  iterator.next();
				org.json.simple.JSONObject obj = (org.json.simple.JSONObject) current;
				String type = (String) obj.get("type");
				if (type.equals("node")){
					graph.addNode(processNode(obj,graph.getNodeCount()));
					
				}
			}

			System.out.println("Loading Ways");
			elements = (JSONArray) jsonObject.get("elements");
			iterator = elements.iterator();
			while (iterator.hasNext()) {
				current =  iterator.next();
				org.json.simple.JSONObject obj = (org.json.simple.JSONObject) current;
				String type = (String) obj.get("type");
				if (type.equals("way"))
					graph.addWay(processWay(obj,graph));
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public static Node processNode(JSONObject node, int index){
		Long id = (Long) node.get("id");
		
		Double lat= (Double) node.get("lat");
		Double lon = (Double) node.get("lon");
		JSONObject tags = (JSONObject) node.get("tags");
		Node newNode = new Node(id,new LatLon(lat,lon));
				
		newNode.setIndex(index);
		return (newNode);
	}

	public static Way processWay(JSONObject way, Graph g){
		Long id = (Long) way.get("id");
		
		JSONObject tags = (JSONObject) way.get("tags");
		String name = (String)tags.get("name");
		String highway = (String)tags.get("highway");
		
		//add nodes
		JSONArray elements = (JSONArray)  way.get("nodes");
		ArrayList<Node> nodes = new ArrayList<Node>();
		for (Object o : elements){
			long nodeId = (long)o;
			Node found = g.getNode(nodeId);
			if (found!= null){
				nodes.add(found);
				found.setUsed();
			}
		}
		Node[] myNodes =  nodes.toArray(new Node[nodes.size()]);
		Way newWay = new Way(id,name,highway,myNodes);
		
		return newWay;
		
	}
}
