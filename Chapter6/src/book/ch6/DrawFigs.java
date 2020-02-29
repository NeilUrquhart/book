package book.ch6;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;



import book.ch6.algorithms.AStar;
import book.ch6.algorithms.AStarBiDirectional;
import book.ch6.algorithms.Dijkstra;
import book.ch6.algorithms.DijkstraFlood;
import book.ch6.algorithms.DijkstraBiDirectional;
import book.ch6.algorithms.RoutingAlgorithm;
import book.ch6.data.Graph;
import book.ch6.data.RouterNode;
import book.ch6.data.Route;

/*
 * https://code.google.com/archive/p/json-simple/
 * 
 * http://umap.openstreetmap.fr/en/map/new/#17/55.94883/-3.18124
 */

public class DrawFigs {
	
	public static void main(String[] args) {
//		long start = System.currentTimeMillis();
//		String[] files = {/*"Greenbank.dat",*/"Edin1.json","Edin2.json","Edin3.json","Edin4.json"};
//		Graph myGraph = new Graph(files);
//
////		KMLWriter allNodes = new KMLWriter();
////		for(Node n : myGraph.getNodes()) {
////			System.out.println(n);
////			allNodes.addPlacemark(n.getLocation(), "", "", "styleRV");
////		}
////		
////		allNodes.writeFile("allNodes");
//		
//		System.out.println("Nodes,"+myGraph.getNodes().size());
//		RoutingAlgorithm[] testAlgs = {/*new AStar(), new Dijkstra(),new DijkstraFlood(),new AStarBiDirectional(),*/ new DijkstraBiDirectional()};
//		drawRoute(myGraph, 4032301773L,1969997014L,testAlgs);                         
//	}
//
//	private static String[] colours = {"green","red","yellow","blue"};
//	private static int colIndex=0;
//		
//	public static void drawRoute(Graph myGraph,long start, long end, RoutingAlgorithm[] routers){
//		System.out.print("Testing,"+start+":"+end+",");
//		for (RoutingAlgorithm router : routers) {
//			System.out.print(","+router.getClass().getSimpleName());
//			Route testRoute = new Route(myGraph,start,end);
//			testRoute.buildRoute(router);
//			KMLWriter out = new KMLWriter();
//			out.addRoute(testRoute.getLocations(), router.getClass().getSimpleName(), "", colours[colIndex]);
//			out.writeFile(router.getClass().getSimpleName()+".kml");		
//			colIndex++; 
//			if (colIndex==colours.length)
//				colIndex=0;
//		}
}
}