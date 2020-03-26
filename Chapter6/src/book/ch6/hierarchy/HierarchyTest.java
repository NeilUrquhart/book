package book.ch6.hierarchy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import book.ch6.KMLWriter;
import book.ch6.algorithms.AStar;
import book.ch6.algorithms.RoutingAlgorithm;
import book.ch6.data.Graph;
import book.ch6.data.Route;
import book.ch6.data.RouterNode;
import book.ch6.data.RouterWay;

public class HierarchyTest {
	private static Random rnd = new Random();
	static private ArrayList<Graph> level1 = new ArrayList<Graph>();
	static private ArrayList<ArrayList<RouterNode>> links = new ArrayList<ArrayList<RouterNode>>();
	static Graph level0;
	
	public static void main(String[] args) {
		
		setup();
		
		//get random start 
		int count =0;
		while (count <100){
			System.out.println("Test "+count);
			long start = getRandom().getId();//43816295;//edin
			long end = getRandom().getId();//3908466938l;//glas
			Route r = buildRoute(start, end);		
			KMLWriter out = new KMLWriter();
			out.addRoute(r.getLocations(), "", "", "red");
			out.writeFile(count+".kml");	
			count++;
		}
	}

	private static RouterNode getRandom(){
		Graph g = level1.get(rnd.nextInt(level1.size()));
		RouterNode r = (RouterNode) new ArrayList(g.getNodes()).get(rnd.nextInt(g.getNodes().size()));
		return r;
	}
	private static Route buildRoute(long start, long end) {
		Graph startG=null;
		Graph endG = null;
		RouterNode startNode = null;
		RouterNode endNode = null;
		RouterNode startLink = null;
		RouterNode endLink = null;
		int index=0;
		for (Graph g : level1){
			if (g.getNode(start) != null){
				startG =g;
				startNode = startG.getNode(start);
				startLink = findLink(startNode,links.get(index));
			}
			if (g.getNode(end) != null){
				endG = g;
				endNode = endG.getNode(end);
				endLink = findLink(endNode,links.get(index));
			}

			if ((startG != null)&&(endG != null))
				break;
			index++;
		}
		
		if (startG == endG){
			Route testRoute = new Route(startG,startNode.getId(),endNode.getId());
			testRoute.buildRoute(new AStar());
			System.out.println("Start/End in same level 1");
			return testRoute;
		}
		System.out.println("Start node " + startNode);
		System.out.println("Start link node " + startLink);
		System.out.println("End link node " + endLink);
		System.out.println("End node " + endNode);

		
		Route testRoute = new Route(startG,startNode.getId(),startLink.getId());
		testRoute.buildRoute(new AStar());
		MultiRoute result = new MultiRoute(testRoute);
		
		testRoute = new Route(level0,startLink.getId(),endLink.getId());
		testRoute.buildRoute(new AStar());
		result.append(testRoute);
		
		testRoute = new Route(endG,endLink.getId(),endNode.getId());
		testRoute.buildRoute(new AStar());
		result.append(testRoute);
		
		return result;
		
	}


	private static void setup() {
		System.out.println("Loading Mway network ");
		level0 = new Graph("./data/mways.osm",new ArrayList<String>( Arrays.asList("primary","secondary",
				"tertiary","unclassified","residential","primary_link","secondary_link","tertiary_link","service","track","living_street","road")));
		System.out.println("Mway network " + level0.getNodeCount());
				
		// Create Edin
		level1.add(loadLevel1("./data/edinburgh.osm"));
		links.add(findLinkNodes(level0,level1.get(level1.size()-1)));
		
		// Create Glas
		level1.add(loadLevel1("./data/glasgow.osm"));
		links.add(findLinkNodes(level0,level1.get(level1.size()-1)));
			
		level1.add(loadLevel1("./data/perth.osm"));
		links.add(findLinkNodes(level0,level1.get(level1.size()-1)));
		
		level1.add(loadLevel1("./data/stirling.osm"));
		links.add(findLinkNodes(level0,level1.get(level1.size()-1)));

	}


private static RouterNode findLink(RouterNode myNode, ArrayList<RouterNode> linkNodes) {
	RouterNode res = null;
	double dist = Double.MAX_VALUE;
	for(RouterNode poss : linkNodes){
		if (poss.getDist(myNode)<dist){
			dist = poss.getDist(myNode);
			res = poss;
		}
	}
	return res;
}

	private static ArrayList<RouterNode> findLinkNodes(Graph mway, Graph edin) {
		ArrayList<RouterNode> linkNodes = new ArrayList<RouterNode>();
		//find common nodes
		for (RouterNode en : mway.getNodes()){
			long id = en.getId();
			if (edin.getNode(id) != null)
				linkNodes.add(edin.getNode(id));	
		}
		
		return linkNodes;
	}

	private static Graph loadLevel1(String fName) {
		System.out.println("Loading " + fName);//focus on Trunk and primary
		Graph res= new Graph(fName,new ArrayList<String>( Arrays.asList("motorway","motorway_link","trunk","trunk_link")));
		System.out.println(fName + " loaded "  +res.getNodeCount());
		return res;
	}
}




