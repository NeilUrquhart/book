package book.ch6;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import book.ch6.algorithms.AStar;
import book.ch6.algorithms.AStarBiDirectional;
import book.ch6.algorithms.Dijkstra;
import book.ch6.algorithms.DijkstraBiDirectional;
import book.ch6.algorithms.DijkstraFlood;
import book.ch6.algorithms.RoutingAlgorithm;
import book.ch6.data.Graph;
import book.ch6.data.RouterNode;
import book.ch6.data.Route;

public class WestScotland {

	public static void main(String[] args) {
		
		
//		String[] files = {"w1.json","w2.json","w3.json","w4.json","w5.json","w7.json","w8.json"};
//		//String[] files = {"w1.json","w2.json","w3.json","w4.json","w5.json"};//Edin1.json","Edin2.json","Edin4.json","Edin5.json","Edin3.json"};
//		
//		Graph myGraph = new Graph(files);
		
//		try{
//		PrintWriter writer = new PrintWriter("allNodes.csv");
//		writer.println("lat,lon,label");
//		for (Node n : myGraph.getNodes()){
//			   System.out.println(n.getId())	;
//			   writer.println(n.getLocation().getLat()+","+n.getLocation().getLon()+","+n.getId())	;
//		}
//		writer.close();
//		}catch(IOException e){
//			e.printStackTrace();
//		}
//		
//		KMLWriter allNodes = new KMLWriter();
//		for(Node n : myGraph.getNodes()) {
//			System.out.println(n);
//			allNodes.addPlacemark(n.getLocation(), "", "", "styleRV");
//		}
//		
//		allNodes.writeFile("WESTallNodes");
//		
//		RoutingAlgorithm[] testAlgs = {/*new AStarBiDirectional(), new DijkstraBiDirectional(),*/new AStar()/*,new Dijkstra(),new DijkstraFlood()*/};
//				
//		//2908938499L
//		drawRoute(myGraph,291781127L, 2254922504L,testAlgs);
//	
		
		
		
	}
	
	public static void testRouter(Graph myGraph,long start, long end, RoutingAlgorithm[] routers){
		System.out.print("Testing,"+start+":"+end);
		for (RoutingAlgorithm router : routers) {
			System.out.print(","+router.getClass().getSimpleName());
			long startRun = System.currentTimeMillis();
			double dist=0;
			for (int i=0;i<1;i++ ){
				//System.out.print(".");
				Route testRoute = new Route(myGraph,start,end);
				testRoute.buildRoute(router);
				dist= testRoute.getDist();
			}
			long endRun = System.currentTimeMillis();
			System.out.print(",distance," + dist);
			System.out.print(",Time," +((endRun-startRun)/10)+",");
		}
		System.out.println();
	}
	private static String[] colours = {"green","red","yellow","blue"};
	private static int colIndex=0;
	
	public static void drawRoute(Graph myGraph,long start, long end, RoutingAlgorithm[] routers){
		System.out.print("Testing,"+start+":"+end+",");
		for (RoutingAlgorithm router : routers) {
			System.out.print(","+router.getClass().getSimpleName());
			Route testRoute = new Route(myGraph,start,end);
			testRoute.buildRoute(router);
			KMLWriter out = new KMLWriter();
			out.addRoute(testRoute.getLocations(), router.getClass().getSimpleName(), "", colours[colIndex]);
			out.writeFile("WEST"+router.getClass().getSimpleName()+".kml");		
			colIndex++; 
			if (colIndex==colours.length)
				colIndex=0;
		}
	}

}
