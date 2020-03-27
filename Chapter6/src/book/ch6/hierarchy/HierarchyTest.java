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

public class HierarchyTest {
	private static Random rnd = new Random();
	//static private ArrayList<Graph> level1 = new ArrayList<Graph>();
	//static private ArrayList<Long[]> links = new ArrayList<Long[]>();
	static Graph level0;
	static Graph level1;
	//static Long[] links;
	
	public static void main(String[] args) {
		setup();
		
		//get random start 
		int count =0;
		while (count <100){
			System.out.println("Test "+count);
			long start = getRandom().getId();//43816295;//edin
			long end = getRandom().getId();//3908466938l;//glas
			Route r = buildRoute(start, end);	
			if (r != null){
				KMLWriter out = new KMLWriter();
				out.addRoute(r.getLocations(), "", "", "red");
				out.writeFile(count+".kml");	
			}
			count++;
		}
	}

	private static RouterNode getRandom(){
		//Graph g = level1.get(rnd.nextInt(level1.size()));
		//RouterNode r = (RouterNode) new ArrayList(g.getNodes()).get(rnd.nextInt(g.getNodes().size()));
		
		return (RouterNode) new ArrayList(level1.getNodes()).get(rnd.nextInt(level1.getNodes().size())) ;
	}
//	private static Route buildRoute(long start, long end) {
//		Graph startG=null;
//		Graph endG = null;
//		RouterNode startNode = null;
//		RouterNode endNode = null;
//		RouterNode startLink = null;
//		RouterNode endLink = null;
//		Long[] startLinks=null;
//		Long[] endLinks=null;
//		int index=0;
//		for (Graph g : level1){
//			if (g.getNode(start) != null){
//				startG =g;
//				startNode = startG.getNode(start);
//				startLinks =links.get(index);
//				}
//			if (g.getNode(end) != null){
//				endG = g;
//				endNode = endG.getNode(end);
//				endLinks =links.get(index);
//			}
//
//			if ((startG != null)&&(endG != null))
//				break;
//			index++;
//		}
//		
//
//		System.out.print("Start" + startNode);
//		System.out.print("End  " + endNode);
//
//		
//		Route firstLeg = new Route(startG,startNode.getId(),0);
//		DijkstraFindUpLink r  = new DijkstraFindUpLink();
//		r.setlinks(startLinks);
//		try{
//		firstLeg.buildRoute(r);
//		}catch(Exception e){
//			System.out.println("Failed start route");
//			return null;
//		}
//		startLink = firstLeg.getFinish();
//		
//		System.out.print(" Slink " + startLink);
//
//		Route endLeg = new Route(endG,endNode.getId(),0);
//		
//		 r  = new DijkstraFindUpLink();
//		r.setlinks(endLinks);
//		try{
//		endLeg.buildRoute(r);
//		}catch(Exception e){
//			System.out.println("Failed end route");
//			return null;
//		}
//		endLink = endLeg.getFinish();
//		System.out.println(" Elink " + endLink);
//		
//		if (startLink.getId() == endLink.getId()){
//			Route testRoute = new Route(startG,startNode.getId(),endNode.getId());
//			try{
//			  testRoute.buildRoute(new AStar());
//			}catch(Exception e){
//				System.out.println("Failed on local route");
//				return null;
//			}
//			System.out.println("Start/End in same level 1");
//			return testRoute;
//		}
//				
//		Route middle = new Route(level0,startLink.getId(),endLink.getId());
//		try{
//			middle.buildRoute(new Dijkstra());
//		}
//		catch(Exception e){
//			System.out.println("Failed middle route");
//			return null;
//		}
//		MultiRoute result = new MultiRoute(firstLeg);
//		result.append(middle);
//		result.append(endLeg);
//		
//		return result;
//		
//	}

	private static Route buildRoute(long start, long end) {
		RouterNode startNode = null;
		RouterNode endNode = null;
		RouterNode startLink = null;
		RouterNode endLink = null;
		//Long[] startLinks=null;
		//Long[] endLinks=null;
		if (level1.getNode(start) != null){
				startNode = level1.getNode(start);
				}
			if (level1.getNode(end) != null){
				endNode = level1.getNode(end);
			}

		System.out.print("Start" + startNode);
		System.out.print("End  " + endNode);
		
		Route firstLeg = new Route(level1,startNode.getId(),0);
		DijkstraFindUpLink r  = new DijkstraFindUpLink();
		//r.setlinks(links);
		try{
			firstLeg.buildRoute(r);
		}catch(Exception e){
			System.out.println("Failed start route");
			return null;
		}
		startLink = firstLeg.getFinish();

		System.out.print(" Slink " + startLink);

		Route endLeg = new Route(level1,endNode.getId(),0);
		
		 r  = new DijkstraFindUpLink();
		//r.setlinks(links);
		try{
		endLeg.buildRoute(r);
		}catch(Exception e){
			System.out.println("Failed end route");
			return null;
		}
		endLink = endLeg.getFinish();
		System.out.println(" Elink " + endLink);
		
		if (startLink.getId() == endLink.getId()){
			Route testRoute = new Route(level1,startNode.getId(),endNode.getId());
			try{
			  testRoute.buildRoute(new AStar());
			}catch(Exception e){
				System.out.println("Failed on local route");
				return null;
			}
			System.out.println("Start/End in same level 1");
			return testRoute;
		}
				
		Route middle = new Route(level0,startLink.getId(),endLink.getId());
		try{
			middle.buildRoute(new Dijkstra());
		}
		catch(Exception e){
			System.out.println("Failed middle route");
			return null;
		}
		MultiRoute result = new MultiRoute(firstLeg);
		result.append(middle,true);
		result.append(endLeg,false);//don't reverse end leg
		
		return result;
		
	}


//	private static void setup() {
//		System.out.println("Loading Mway network ");
//		level0 = new Graph("./data/mways.osm",new ArrayList<String>( Arrays.asList("primary","secondary",
//				"tertiary","unclassified","residential","primary_link","secondary_link","tertiary_link","service","track","living_street","road")));
//		System.out.println("Mway network " + level0.getNodeCount());
//				
//		// Create Edin
//		level1.add(loadLevel1("./data/edinburgh.osm"));
//		links.add(findLinkNodes(level0,level1.get(level1.size()-1)));
//		
//		// Create Glas
//		level1.add(loadLevel1("./data/glasgow.osm"));
//		links.add(findLinkNodes(level0,level1.get(level1.size()-1)));
//			
//		level1.add(loadLevel1("./data/perth.osm"));
//		links.add(findLinkNodes(level0,level1.get(level1.size()-1)));
//		
//		level1.add(loadLevel1("./data/stirling.osm"));
//		links.add(findLinkNodes(level0,level1.get(level1.size()-1)));
//
//	}

	private static void setup() {
		System.out.println("Loading Mway network ");
		level0 = new Graph("./data/roads.osm",new ArrayList<String>( Arrays.asList("primary","secondary",
				"tertiary","unclassified","residential","primary_link","secondary_link","tertiary_link","service","track","living_street","road")));
		System.out.println("Mway network " + level0.getNodeCount());
				
		
		
		level1= loadLevel1("./data/roads.osm");
		findLinkNodes(level0,level1);

	}
//private static RouterNode findLink(RouterNode myNode, ArrayList<RouterNode> linkNodes) {
//	RouterNode res = null;
//	double dist = Double.MAX_VALUE;
//	for(RouterNode poss : linkNodes){
//		if (poss.getDist(myNode)<dist){
//			dist = poss.getDist(myNode);
//			res = poss;
//		}
//	}
//	return res;
//}

	private static void findLinkNodes(Graph mway, Graph edin) {
		//find common nodes
		for (RouterNode en : mway.getNodes()){
			long id = en.getId();
			if (edin.getNode(id) != null)
				edin.getNode(id).setUplink();
		}
		
	}

	private static Graph loadLevel1(String fName) {
		System.out.println("Loading " + fName);//focus on Trunk and primary
		Graph res= new Graph(fName,new ArrayList<String>( Arrays.asList("motorway","motorway_link","trunk","trunk_link")));
		System.out.println(fName + " loaded "  +res.getNodeCount());
		return res;
	}
}




