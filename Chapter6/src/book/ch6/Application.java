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

import book.ch6.algorithms.AStar;
import book.ch6.algorithms.AStarBiDirectional;
import book.ch6.algorithms.Dijkstra;
import book.ch6.algorithms.DijkstraBiDirectional;
import book.ch6.algorithms.DijkstraMod;
import book.ch6.algorithms.RoutingAlgorithm;
import book.ch6.data.Graph;
import book.ch6.data.Route;

/*
 * https://code.google.com/archive/p/json-simple/
 */

public class Application {
	
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		String[] files = {"Greenbank.dat","Edin1.json","Edin2.json","Edin3.json","Edin4.json"};
		Graph myGraph = new Graph(files);
		System.out.println("Load time =" + (System.currentTimeMillis()-start));
		
		RoutingAlgorithm[] testAlgs = {new AStarBiDirectional(), new DijkstraBiDirectional(), new AStar(), new DijkstraMod(),new Dijkstra()};
		for (RoutingAlgorithm alg:testAlgs){
			 testRouter(myGraph, 26941173L,3676389428L,alg); 
			 testRouter(myGraph, 38826274L, 52047461L,alg); 
		}

	}
	
	public static void testRouter(Graph myGraph,long start, long end, RoutingAlgorithm router){
		System.out.print("Testing " + router.getClass().getSimpleName());
		long startRun = System.currentTimeMillis();
		double dist=0;
		for (int i=0;i<2;i++ ){
			System.out.print(".");
			Route testRoute = new Route(myGraph,start,end);
			testRoute.buildRoute(router);
			dist= testRoute.getDist();
		}
		long endRun = System.currentTimeMillis();
		System.out.println("\n Results: "+router.getClass().getSimpleName());
		System.out.println("Distance =" + dist);
		System.out.println("Run time =" +((endRun-startRun)/2));
	}
}
