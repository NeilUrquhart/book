package book.ch6;

import book.ch6.algorithms.AStar;
import book.ch6.algorithms.AStarBiDirectional;
import book.ch6.algorithms.Dijkstra;
import book.ch6.algorithms.DijkstraBiDirectional;
import book.ch6.algorithms.DijkstraFlood;
import book.ch6.algorithms.RoutingAlgorithm;
import book.ch6.data.Graph;
import book.ch6.data.Route;

public class WestScotland {

	public static void main(String[] args) {
		String[] files = {"WestScotTrunk.json","WestScotPrimary.json"};
		Graph myGraph = new Graph(files);
		
		RoutingAlgorithm[] testAlgs = {/*new AStarBiDirectional(), new DijkstraBiDirectional(), */new AStar(), new Dijkstra(),new DijkstraFlood()};
		testRouter(myGraph, 3841225793L, 1208895219L, testAlgs);
		testRouter(myGraph, 1208895219L, 3841225793L, testAlgs);
		
		
		
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

}
