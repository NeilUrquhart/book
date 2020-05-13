package book.ch6.hierarchy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import book.ch6.KMLWriter;
import book.ch6.algorithms.AStar;
import book.ch6.algorithms.Dijkstra;
import book.ch6.algorithms.DijkstraFindUpLink;
import book.ch6.algorithms.RoutingAlgorithm;
import book.ch6.data.Graph;
import book.ch6.data.Route;
import book.ch6.data.RouterNode;
import book.ch6.data.RouterWay;

/* A very simple implementation of  2-tier 
 * hierarchical routing.
 * 
 * Copyright Neil Urquhart 2020
 */
public class HierarchyTest {
	private static final int MAX_LEVELS = 2;
	private static Random rnd = new Random();
	static private Graph[] levels = new Graph[MAX_LEVELS];
	
	public static void main(String[] args) {
		setup("./data/roads.osm");//Load the graphs
		
		int count =0;
		while (count <100){
			//Test on 100 random routes
			System.out.println("Test "+count);
			long start = getRandom(1).getId();
			long end = getRandom(1).getId();
			Route r = buildRoute(start, end);	
			if (r != null){
				/*r is null if the route cannot be
				 * built for some reason.
				 */
				KMLWriter out = new KMLWriter();
				out.addRoute(r.getLocations(), "", "", "red");
				out.writeFile(count+".kml");	
			}
			count++;
		}
	}

		private static RouterNode getRandom(int level){
			/* Return a random node from the graph a
			 * <level>.
			 */
			return (RouterNode) new ArrayList(levels[level].getNodes()).get(rnd.nextInt(levels[level].getNodes().size()));
		}


		private static Route buildRoute(long start, long end) {	
			/*Build A route from <start> to <end>
			 * Assumes that the start and end nodes are
			 * both in level 1
			 */
			RouterNode startNode = levels[1].getNode(start);
			RouterNode endNode = levels[1].getNode(end);

			if ((startNode == null)||(endNode == null))
				return null;

			System.out.print("Start" + startNode+" End  " + endNode);

			Route firstLeg = new Route(levels[1],startNode.getId(),0);
			try{
				firstLeg.buildRoute(new DijkstraFindUpLink());
			}catch(Exception e){
				System.out.println("Failed start route");
				return null;
			}
			RouterNode startLink = firstLeg.getFinish();
			System.out.print(" Slink " + startLink);
			
			Route endLeg = new Route(levels[1],endNode.getId(),0);
			try{
				endLeg.buildRoute(new DijkstraFindUpLink());
			}catch(Exception e){
				System.out.println("Failed end route");
				return null;
			}
			RouterNode endLink = endLeg.getFinish();
			System.out.println(" Elink " + endLink);

			if (startLink.getId() == endLink.getId()){
				//Start and end are directly connected
				Route testRoute = new Route(levels[1],startNode.getId(),endNode.getId());
				try{
					testRoute.buildRoute(new AStar());
				}catch(Exception e){
					System.out.println("Failed on local route");
					return null;
				}
				System.out.println("Start/End in same level");
				return testRoute;
			}
			//Find middle section of route
			Route middle = new Route(levels[0],startLink.getId(),endLink.getId());
			try{
				middle.buildRoute(new Dijkstra());
			}
			catch(Exception e){
				System.out.println("Failed middle route");
				return null;
			}
			//construct multi-route object
			MultiRoute result = new MultiRoute();
			result.append(firstLeg,true);
			result.append(middle,true);
			result.append(endLeg,false);//don't reverse end leg
			return result;
		}

	private static void setup(String dataFile) {
		/*
		 * Setup hierarchy
		 */
		levels[0] =new Graph(dataFile,new ArrayList<String>( Arrays.asList("primary","primary_link","secondary",
				"tertiary","unclassified","residential","secondary_link","tertiary_link","service","track","living_street","road")));
		levels[1]=loadLevel1(dataFile);
		findLinkNodes(levels[0],levels[1]);
	}

	private static void findLinkNodes(Graph lower, Graph upper) {
		/*find common nodes that link <lower> to <upper>
		 */
		for (RouterNode en : upper.getNodes()){
			long id = en.getId();
			if (lower.getNode(id) != null)
				lower.getNode(id).setUplink();
		}
	}

	private static Graph loadLevel1(String fName) {
		/* Create a layer 1 graph */
		System.out.println("Loading " + fName);//focus on Trunk and primary
		Graph res= new Graph(fName,new ArrayList<String>( Arrays.asList("motorway","motorway_link","trunk","trunk_link")));
		System.out.println(fName + " loaded "  +res.getNodeCount());
		return res;
	}
}




