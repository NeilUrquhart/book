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
 * 
 * A very simple demonstration of routing, using the code developed for ch6
 * Copyright Neil Urquhart 2020
 * 
 */
public class Application {

	public static void main(String[] args) {
		Graph myGraph = new Graph("./../../../../deliveryRoutes/ghData/Frankfurt.osm",null);//"./data/roads.osm",null);
		//Load osm data into a street graph
		Route testRoute = new Route(myGraph,33628531L,5620022255L); //ENU - Mardale cres -> SEC
		//Create a Route object within the graph based on the start and end nodes
		testRoute.buildRoute(new AStar());
		//Find a path between the start and the end using A*
		System.out.println("distance," + testRoute.getDist());
		for (String stage : testRoute.getWays())
			System.out.println(stage);
		//Print out the distance of the path found
	}

}