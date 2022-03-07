package OSM;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.xml.sax.SAXException;

import info.pavie.basicosmparser.controller.OSMParser;
import info.pavie.basicosmparser.model.Element;
import info.pavie.basicosmparser.model.Node;
import info.pavie.basicosmparser.model.Way;

public class TestOSM {
	
	public static void main(String[] args){
	OSMParser p = new OSMParser();						//Initialization of the parser
	File osmFile = new File("edin.osm");	//Create a file object for your OSM XML file

	try {

		Map<String,Element> result = p.parse(osmFile);		//Parse OSM data, and put result in a Map object
		
		for (String s : result.keySet()){
			System.out.println(s);
			Element e = result.get(s);
			//Filter nodes
			String id = e.getId();
			id.trim();

			if (id.startsWith("W")){
				Way w = (Way)e;
				System.out.println("Way " + w.getId());
//				for (Node n :w.getNodes())
//					System.out.print("\tNode " +n.getId() + " "+ n.getLat() + ","+ n.getLon());
			}
				
		}

	} catch (IOException | SAXException e) {
		e.printStackTrace();								//Input/output errors management
	}
	}
}
