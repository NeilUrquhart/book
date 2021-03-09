package book.ch8.humanitarian;

import java.text.SimpleDateFormat;
import java.util.Random;

import book.ch1.Visit;
import old.CSVWriter;
import old.ConsoleWriter;
import old.GPXWriter;
import old.IVisit;
import old.KMLWriter;
import old.OSMAccessHelper;

public class DemonstrationApp {
    private static HudFacade hf = new HudFacade("My Ref"); //Problem Reference
    
	public static void main(String[] args) {

		/*
		 * Initialise hopper
		 * 
		 * The first time this is executed, there will be a delay whilst the .pbf file is unpacked
		 * and the graph is built.
		 * 
		 */
		OSMAccessHelper.init("./data/","scotland-latest.osm.pbf");
		System.out.println("OSM/Graphhopper configured correctly");

		/*
		 * Define a new problem
		 */

		IVisit depot = new IVisit("Depot",55.933020,-3.213618,0);
		hf.setDepot(depot);//Assume start/end are the same
		hf.setStartDateTime("14/05/2020 09:00");
		hf.setRound("car", 30,5,180);//Type, capacity,mins/delivery,max mins per round


		createRandomDels(1000);
 
		hf.solve(30);//halt after a maximum of x mins

		try {
			hf.writeOut(".", new ConsoleWriter());
			hf.writeOut(".", new KMLWriter());
			hf.writeOut(".", new GPXWriter());
			hf.writeOut(".", new CSVWriter());

		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	private static void createRandomDels(int qty) {
		Random rnd = new Random(869);
		int count = 0;
		double lat =55.933020;
		double lon = -3.213618;
		HudVisit base = new HudVisit("depot",lat,lon,1);
		while (count < qty) {
			//Create random address

			double deltaLat = rnd.nextDouble()/100;
			double deltaLon = rnd.nextDouble()/100;

			//Every so often - big jump
			if (rnd.nextDouble()>0.8) {
				lat =55.933020;
				lon = -3.213618;
				deltaLat = rnd.nextDouble()/50;
				deltaLon = rnd.nextDouble()/50;

			}
				

		
			if (rnd.nextBoolean()) {
				lat = lat + deltaLat;
			}else {
				lat = lat - deltaLat;
			}

			if (rnd.nextBoolean()) {
				lon = lon + deltaLon;
			}else {
				lon = lon - deltaLon;
			}
			HudVisit v = new HudVisit("Visit "+count,lat,lon,1,"","","");
			//Check new point is recognised by OSM
			try {
				//OSMAccessHelper.getJourney((IVisit)base,(IVisit)v,"car");
				System.out.println("Random added " + v);
				hf.addVisit(v);
				count++;
			}catch(Exception e) {
				System.out.println("Random failed!");
				e.printStackTrace();
			}
		}
	}

}
