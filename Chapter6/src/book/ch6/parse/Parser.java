package book.ch6.parse;

import info.pavie.basicosmparser.controller.OSMParser;
import info.pavie.basicosmparser.model.Element;
import info.pavie.basicosmparser.model.Node;
import info.pavie.basicosmparser.model.Way;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;


import org.xml.sax.SAXException;

import book.ch6.data.Graph;
import book.ch6.data.LatLon;
import book.ch6.data.RouterNode;
import book.ch6.data.RouterWay;
public class Parser {
	
	private static int nodeCount=0;
	private static ArrayList<String> validHighways = new ArrayList<String>( 
            Arrays.asList("motorway", 
                          "trunk", 
                          "primary",
                          "secondary",
                          "tertiary",
                          "unclassified",
                          "residential",
                          "motorway_link",
                          "trunk_link",
                          "primary_link",
                          "secondary_link",
                          "tertiary_link")); 
	
	private static ArrayList<String> inValidHighways = new ArrayList<String>( 
            Arrays.asList("cycleway",
            		"elevator",
            		"bus_stop",
            		"proposed",
            		"no",
            		"path",
            		"corridor",
            		"steps",
            		"bridleway",
            		"footway",
            		//"road",
            		"raceway",
            		"escape",
            		"bus_guideway",
            		//"track",
            		"pedestrian",
            		//"service",
            		//"living_street",
            		"construction")); 
	
	
	public static void loadOSM(String fName, Graph graph) {
		
		OSMParser p = new OSMParser();						//Initialization of the parser
		File osmFile = new File(fName);	//Create a file object for your OSM XML file

		try {
			System.out.println("Loading Ways");
			Map<String,Element> result = p.parse(osmFile);		//Parse OSM data, and put result in a Map object
			
			for (String s : result.keySet()){
				Element e = result.get(s);
				//Filter nodes
				String id = e.getId();
				id.trim();

				if (id.startsWith("W")){
					Way w = (Way)e;
					//System.out.println("Way " + w.getId());
					RouterWay newR = processWay(w,graph);
					if (newR != null)
					  graph.addWay(newR);
				}
					
			}

		} catch (IOException | SAXException e) {
			e.printStackTrace();								//Input/output errors management
		}
	}
	
//	private static boolean searchNodes(long[] list, long id){
//		for (long poss: list){
//			if (poss==id)
//				return true;
//			
//		}
//		return false;
//	}
//	public static RouterNode processNode(JSONObject node){
//		Long id = (Long) node.get("id");
//		
//		Double lat= (Double) node.get("lat");
//		Double lon = (Double) node.get("lon");
//		JSONObject tags = (JSONObject) node.get("tags");
//		RouterNode newNode = new RouterNode(id,new LatLon(lat,lon));
//				
//		return (newNode);
//	}
//
	
	private static long convertId(String osmId){
		return Long.parseLong(osmId.substring(1));
		
	}
	public static RouterWay processWay(Way myWay, Graph g){
		
		String	highway = (String)myWay.getTags().get("highway");
		if(highway==null)
			return null;
		
		if (inValidHighways.contains(highway ))
			return null;
		
		//if (!validHighways.contains(highway ))
			//System.out.println("h="+highway);//		return null;
		
		//add nodes
		
		ArrayList<RouterNode> nodes = new ArrayList<RouterNode>();
		for (Node n : myWay.getNodes()){
			long nid = convertId(n.getId());
			if (!g.nodeExists(nid)){
				//Add node
				g.addNode(new RouterNode(n));
			}
			
			RouterNode rn = g.getNode(nid);
			nodes.add(rn);
				
			
		}
		RouterNode[] myNodes =  nodes.toArray(new RouterNode[nodes.size()]);
		RouterWay newWay = new RouterWay(myWay, myNodes);
		
		return newWay;
		
	}
}
//public class Parser {
//	public static void loadJSON(String fName, Graph graph) {
//		JSONParser parser = new JSONParser();
//		try (Reader reader = new FileReader(fName)) {
//			JSONObject jsonObject = (JSONObject) parser.parse(reader);
//			System.out.println(jsonObject);
//			Double ver = (Double) jsonObject.get("version");
//			System.out.println(ver);
//
//			// loop array
//			System.out.println("Loading Nodes");
//
//			JSONArray elements = (JSONArray) jsonObject.get("elements");
//			Iterator<String> iterator = elements.iterator();
//			Object current;
//			while (iterator.hasNext()) {
//				current =  iterator.next();
//				org.json.simple.JSONObject obj = (org.json.simple.JSONObject) current;
//				String type = (String) obj.get("type");
//				if (type.equals("node")){
//					RouterNode newNode = processNode(obj);
//					if (!graph.nodeExists(newNode.getId())){
//						graph.addNode(newNode);
//					}else{
//						//Compare old and new
//						RouterNode old = graph.getNode(newNode.getId());
//						if (!old.equal(newNode)){
//							System.out.println("Node mismatch!");
//						}
//					}
//					
//				}
//			}
//
//			System.out.println("Loading Ways");
//			elements = (JSONArray) jsonObject.get("elements");
//			iterator = elements.iterator();
//			while (iterator.hasNext()) {
//				current =  iterator.next();
//				org.json.simple.JSONObject obj = (org.json.simple.JSONObject) current;
//				String type = (String) obj.get("type");
//				if (type.equals("way")){
//					Long id = (Long) obj.get("id");
//					if (!graph.wayExists(id)){
//						RouterWay newWay = processWay(obj,graph);
//						graph.addWay(newWay);
//					}else{
//						//Compare ways
//						RouterWay oldWay = graph.getWay(id);
//						RouterWay newWay = processWay(obj,graph);
//						if (!oldWay.equal(newWay)){
//							System.out.println("Way mistmatch!");
//						}
//					
//						
//					}
//				}
//			}
//
//		} catch (IOException e) {
//			System.out.println("Error processing " + fName + "\n" +e);
//			
//			e.printStackTrace();
//			System.exit(-1);
//		} catch (ParseException e) {
//			System.out.println("Error processing " + fName + "\n" +e);
//			
//			e.printStackTrace();
//			System.exit(-1);
//		}
//	}
//	
//	private static boolean searchNodes(long[] list, long id){
//		for (long poss: list){
//			if (poss==id)
//				return true;
//			
//		}
//		return false;
//	}
//	public static RouterNode processNode(JSONObject node){
//		Long id = (Long) node.get("id");
//		
//		Double lat= (Double) node.get("lat");
//		Double lon = (Double) node.get("lon");
//		JSONObject tags = (JSONObject) node.get("tags");
//		RouterNode newNode = new RouterNode(id,new LatLon(lat,lon));
//				
//		//newNode.setIndex(index);
//		return (newNode);
//	}
//
//	public static RouterWay processWay(JSONObject way, Graph g){
//		Long id = (Long) way.get("id");
//		
//		String name = "";
//		String highway ="";
//		
//		JSONObject tags = (JSONObject) way.get("tags");
//		if (tags != null){
//			name = (String)tags.get("name");
//			highway = (String)tags.get("highway");
//		}
//
//		//add nodes
//		JSONArray elements = (JSONArray)  way.get("nodes");
//		ArrayList<RouterNode> nodes = new ArrayList<RouterNode>();
//		for (Object o : elements){
//			long nodeId = (long)o;
//			RouterNode found = g.getNode(nodeId);
//			if (found!= null){
//				if (nodes.contains(found)){
//					System.out.println("Duplicate node!");
//				}
//				nodes.add(found);
//				found.setUsed();
//			}else{
//				System.out.println("Node not found \n Way: "+id + "\n NodeL:" +nodeId);
//				System.exit(-1);
//			}
//		}
//		RouterNode[] myNodes =  nodes.toArray(new RouterNode[nodes.size()]);
//		RouterWay newWay = new RouterWay(id,name,highway,myNodes);
//		
//		return newWay;
//		
//	}
//}
