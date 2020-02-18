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
import book.ch6.algorithms.DijkstraFlood;
import book.ch6.algorithms.DijkstraBiDirectional;
import book.ch6.algorithms.RoutingAlgorithm;
import book.ch6.data.Graph;
import book.ch6.data.Route;

/*
 * https://code.google.com/archive/p/json-simple/
 */

public class Application {
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		String[] files = {"Edin1.json","Edin2.json","Edin4.json","Edin5.json","Edin3.json"};
		Graph myGraph = new Graph(files);
		System.out.println("Load time =" + (System.currentTimeMillis()-start));
		System.out.println("Nodes,"+myGraph.getNodes().size());

		RoutingAlgorithm[] testAlgs = {new AStarBiDirectional(), new DijkstraBiDirectional(), new AStar(), new Dijkstra(),new DijkstraFlood()};
		testRouter(myGraph, 3984466166L, 2407072781L, testAlgs);
		testRouter(myGraph, 38826274L, 52047461L,testAlgs);
		testRouter(myGraph, 3984466166L,3676389428L,testAlgs);
		testRouter(myGraph, 4611819743L,3715514804L,testAlgs);
		testRouter(myGraph, 4756281951L,2508067364L,testAlgs);
		testRouter(myGraph, 320845744L,5363531156L,testAlgs);
		testRouter(myGraph, 2407072781L,29769767L,testAlgs);
		testRouter(myGraph, 3073722023L, 4611819743L,testAlgs);
		testRouter(myGraph, 1631995154L,2420233344L,testAlgs);
		testRouter(myGraph, 1815330260L,3984466166L,testAlgs);                         
                     

	}

	public static void testRouter(Graph myGraph,long start, long end, RoutingAlgorithm[] routers){
		System.out.print("Testing,"+start+":"+end);
		for (RoutingAlgorithm router : routers) {
			System.out.print(router.getClass().getSimpleName());
			long startRun = System.currentTimeMillis();
			double dist=0;
			for (int i=0;i<1/*0*/;i++ ){
				//System.out.print(".");
				Route testRoute = new Route(myGraph,start,end);
				testRoute.buildRoute(router);
				dist= testRoute.getDist();
			}
			long endRun = System.currentTimeMillis();
			System.out.print("distance," + dist);
			System.out.print(",Time," +((endRun-startRun)/10)+",");
		}
		System.out.println();
	}
}