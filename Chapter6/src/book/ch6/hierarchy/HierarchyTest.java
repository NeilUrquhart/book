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
	private static final int MAX_LEVELS = 2;
	private static Random rnd = new Random();
		static private Graph[] levels = new Graph[MAX_LEVELS];
	
	//static Long[] links;
	
	public static void main(String[] args) {
		setup();
		
		//get random start 
		int count =0;
		while (count <100){
			System.out.println("Test "+count);
			long start = getRandom().getId();//26941177l;//edin
			long end = getRandom().getId();//5620022255l;//glas
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
			
			return (RouterNode) new ArrayList(levels[1].getNodes()).get(rnd.nextInt(levels[1].getNodes().size()));
		}


	private static Route buildRoute(long start, long end) {
		RouterNode startNode = null;
		RouterNode endNode = null;
		RouterNode startLink = null;
		RouterNode endLink = null;
		if (levels[1].getNode(start) != null){
				startNode = levels[1].getNode(start);
				}
			if (levels[1].getNode(end) != null){
				endNode = levels[1].getNode(end);
			}

		System.out.print("Start" + startNode);
		System.out.print("End  " + endNode);
		
		Route firstLeg = new Route(levels[1],startNode.getId(),0);
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

		Route endLeg = new Route(levels[1],endNode.getId(),0);
		
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
			Route testRoute = new Route(levels[1],startNode.getId(),endNode.getId());
			try{
			  testRoute.buildRoute(new AStar());
			}catch(Exception e){
				System.out.println("Failed on local route");
				return null;
			}
			System.out.println("Start/End in same level 1");
			return testRoute;
		}
				
		Route middle = new Route(levels[0],startLink.getId(),endLink.getId());
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


	private static void setup() {
		System.out.println("Loading Mway network ");
		levels[0] =new Graph("./data/roads.osm",new ArrayList<String>( Arrays.asList("primary","primary_link","secondary",
				"tertiary","unclassified","residential","secondary_link","tertiary_link","service","track","living_street","road")));
				
		levels[1]=loadLevel1("./data/roads.osm");
		findLinkNodes(levels[0],levels[1]);
	}

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




