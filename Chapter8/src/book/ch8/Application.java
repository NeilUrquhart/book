package book.ch8;


import book.ch8.algorithms.AStar;
import book.ch8.algorithms.AStarBiDirectional;
import book.ch8.algorithms.Dijkstra;
import book.ch8.algorithms.DijkstraBiDirectional;
import book.ch8.algorithms.DijkstraFlood;
import book.ch8.algorithms.RoutingAlgorithm;
import book.ch8.data.Graph;
import book.ch8.data.Route;

/*
 * 
 * A very simple demonstration of routing, using the code developed for ch6
 * Copyright Neil Urquhart 2020
 * 
 * Please make sure that the uncompress the .ZIP files in /data  otherwise this example will not
 * work!
 * 
 */
public class Application {

	public static void main(String[] args) {
		Graph myGraph = new Graph("./data/roads.osm",null);
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