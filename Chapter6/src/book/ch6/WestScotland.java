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
		String[] files = {"WestScotCTown.json","WestScotTrunk.json","WestScotPrimary.json","WestScotMWay.json","WestScotLuss.json","WestScotInveraray"};
		//String[] files = {"Edin1.json","Edin2.json","Edin4.json","Edin5.json","Edin3.json"};
		
		Graph myGraph = new Graph(files);
		
		RoutingAlgorithm[] testAlgs = {/*new AStarBiDirectional(), new DijkstraBiDirectional(), new AStar(),*/ new Dijkstra()/*,new DijkstraFlood()*/};
		testRouter(myGraph, 3878896391L,  18927439L, testAlgs);  //Last known good 2254810100
		
		//testRouter(myGraph, 3984466166L, 2407072781L, testAlgs); for edin
		
		
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
