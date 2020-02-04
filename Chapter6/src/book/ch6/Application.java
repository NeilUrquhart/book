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

public class Application {
	
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		String[] files = {"Edin1.json"/*,"Edin2.json","Greenbank.dat"*/};
		Graph myGraph = new Graph(files);
		long loaded = System.currentTimeMillis();
		
		long startRun = System.currentTimeMillis();
		double dist=0;
		for (int i=0;i<10;i++ ){
			Route testRoute = new Route(myGraph,/*4032935817L,38827957L*/38826274L, 52047461L);
			testRoute.buildRoute(new Dijkstra());
			System.out.println("Result");
			for (String street : testRoute.getWays())
				System.out.println(street);
			dist= testRoute.getDist();
		}
		long endRun = System.currentTimeMillis();
		
		System.out.println("Distance =" + dist);
		System.out.println("Load time =" + (loaded-start));
		System.out.println("Run time =" +((endRun-startRun)/10));
		
		}
}
