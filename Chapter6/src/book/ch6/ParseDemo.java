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

import book.ch6.algorithms.Dijkstra;
import book.ch6.data.Graph;
import book.ch6.data.Route;

/*
 * https://code.google.com/archive/p/json-simple/
 */

public class ParseDemo {
	
	public static void main(String[] args) {
		String[] files = {/*"Edin1.json","Edin2.json"};//*/"Greenbank.dat"};
		Graph myGraph = new Graph(files);
		
		Route testRoute = new Route(myGraph,/*4032935817L,38827957L*/4242114400L,3676389427L);
		testRoute.buildRoute(new Dijkstra());
		
		System.out.println("Result");
		for (String street : testRoute.getWays())
			System.out.println(street);
		
		System.out.println("Distance =" + testRoute.getDist());
		
		}
}
