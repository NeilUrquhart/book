package book.ch6.parse;

/*
 * A wrapper for the info.pavie.basicosmparser.controller.OSMParser
 * 
 * Copyright Neil Urquhart, 2020
 * 
 */
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
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import org.xml.sax.SAXException;

import book.ch6.data.Graph;
import book.ch6.data.LatLon;
import book.ch6.data.RouterNode;
import book.ch6.data.RouterWay;
public class Parser {

	protected static ArrayList<String> inValidHighways = new ArrayList<String>( 
		Arrays.asList("cycleway","elevator","bus_stop","proposed","no",
		"path","corridor","steps","bridleway","footway","raceway",
		"escape","bus_guideway","pedestrian","construction","service","track","living_street","road")); 
	
	private static ArrayList<String> excludedHighways=null;

	public static void setInvalid(ArrayList<String> ex){
		excludedHighways = ex;
	}
	
	public static void loadOSM(String fName, Graph graph) {
		//Load the contents of <fName> into <graph>
		OSMParser p = new OSMParser();		//Initialise parser
		try {
			System.out.println("Loading Ways");
			Map<String,Element> result = p.parse(new File(fName));		
			//Parse OSM data, and put result in a Map object

			for (String s : result.keySet()){
				Element e = result.get(s);
				//Filter nodes
				String id = e.getId().trim();
				if (id.startsWith("W")){
					Way w = (Way)e;
					RouterWay newR = processWay(w,graph);
					if (newR != null)
						graph.addWay(newR);
				}
			}
		} catch (IOException | SAXException e) {
			e.printStackTrace();//Report any parse errors
		}
	}

	private static RouterWay processWay(Way myWay, Graph g){
		//Create a RouterWay object based upon the <way> object
		String	highway = (String)myWay.getTags().get("highway");
		if(highway==null)
			//Any ways that do not have highway attribute should be disregrded.
			return null;
		if (inValidHighways.contains(highway ))
			//Disregard any highways types that are in our 'invalid list'
			return null; 
		if(excludedHighways != null)
			if (excludedHighways.contains(highway ))
				//Disregard any highways types that are in our 'excluded list'
				return null; 

		
		ArrayList<RouterNode> nodes = new ArrayList<RouterNode>();
		//add nodes associated with myWay to the RouterWay object
		
		for (Node n : myWay.getNodes()){
			long nid = convertId(n.getId());
			if (!g.nodeExists(nid)){//Check node has been loaded.
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


	private static long convertId(String osmId){
		//Remove the first char (e.g. 'W') from an ID
		return Long.parseLong(osmId.substring(1));

	}
	
}
