package book.ch6;


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

public class ExampleApplication {

	public static void main(String[] args) {
		Graph myGraph = new Graph("westscot.osm");
		
					
		RoutingAlgorithm[] testAlgs = {  new AStar(),new DijkstraBiDirectional(),new AStarBiDirectional(),new Dijkstra(),new DijkstraFlood()};
		
		drawRoute(myGraph, 291781127L,257927392L, testAlgs);//Glasgow  to Campbeltown
		drawRoute(myGraph, 257927392L, 291781127L, testAlgs);//Campbeltown to Glasgow
					
		testRouter(myGraph, 291781127L,257927392L, testAlgs);//Glasgow  to Campbeltown
		testRouter(myGraph, 257927392L, 291781127L, testAlgs);//Campbeltown to Glasgow
		 
	}

	public static void testRouter(Graph myGraph,long start, long end, RoutingAlgorithm[] routers){
		System.out.print("Testing,"+start+":"+end+",");
		for (RoutingAlgorithm router : routers) {
			System.out.print(router.getClass().getSimpleName());
			long startRun = System.currentTimeMillis();
			double dist=0;
			for (int i=0;i<10;i++ ){
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
	
	public static void drawRoute(Graph myGraph,long start, long end, RoutingAlgorithm[] routers){
		System.out.print("drawing,"+start+":"+end+",");
		for (RoutingAlgorithm router : routers) {
			System.out.print(","+router.getClass().getSimpleName());
			Route testRoute = new Route(myGraph,start,end);
			testRoute.buildRoute(router);
			KMLWriter out = new KMLWriter();
			out.addRoute(testRoute.getLocations(), router.getClass().getSimpleName(), "", colours[colIndex]);
			out.writeFile(start+":"+end+"."+router.getClass().getSimpleName()+".kml");		
			colIndex++; 
			if (colIndex==colours.length)
				colIndex=0;
		}
	}
	
	private static String[] colours = {"green","red","yellow","blue"};
	private static int colIndex=0;
}