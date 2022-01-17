package book.ch10.food;
/*
 * Neil Urquhart 2019
 * This programme tests a set of CVRP problem instances, using a range of solvers to produce solutions.
 */

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import book.ch1.LatLon;
import book.ch1.Visit;
import book.ch2.VRPVisit;
import book.ch7.Geocoder;
import book.ch7.Nominatim;
import book.ch7.export.ExportService;
import book.ch7.routing.Journey;
import book.ch7.export.CSVWriter;
import book.ch7.export.ConsoleWriter;
import book.ch7.export.GPXWriter;
import book.ch7.export.KMLWriter;

public class FoodFacade  {
	public enum SaveTo{
		CONSOLE(new ConsoleWriter()),
		CSV(new CSVWriter()),
		GPX(new GPXWriter()),
		KML(new KMLWriter());
		public final ExportService save;
		private SaveTo(ExportService e) {
			this.save =e;
		}
	}
	
	public FoodFacade() {
		myVRP= new FoodProblem();
		geocode = new Nominatim();
	}
	public void setReference(String ref) {
		myVRP.setReference(ref);
	}
	public void addVisit(String name, String address, int demand, String order) {
		LatLon loc = geocode.geocode(address);
		if (loc != null) {
			myVRP.addVisit(new FoodVisit(name, loc.getLat(),loc.getLon(),demand,address, order));
			System.out.println(name + ","+address + " geocoded to "+ loc);
		}else {
			System.out.println("Can't geocode "+ name + " " + address);
			System.exit(-1);
		}
	}
	public void setStart(String addr) {
		LatLon loc = geocode.geocode(addr);
		if (loc != null) {
			System.out.println(addr + " geocoded to "+ loc);
			myVRP.setStart(new FoodVisit(addr, loc.getLat(),loc.getLon(),0));
			myVRP.setEnd(myVRP.getStart());
		}else {
			System.out.println("Can't geocode "+ addr);
			System.exit(-1);
		}
	}
	public void solve()  {
		//Solve using the Evolutionary Algorithm
		myVRP.solve(new FoodEA(Integer.parseInt(FoodProperties.getInstance().get("runtime"))*1000));
	}
	public void save( SaveTo save ){
		int r=1;
		ExportService out = save.save;
		double deliveryTime = myVRP.getDeliveryTimeMS();
		double time = myVRP.getStartDate();

		ArrayList<ArrayList<VRPVisit>> solution =  myVRP.getCVRPSolution();
		int c=1;
		for(ArrayList<VRPVisit> run :solution){
			out.addWaypoint((LatLon)myVRP.getStart(),"Start","");			
			Visit prev = myVRP.getStart();
			for (VRPVisit vv : run){
				Visit v = vv;
				String description = "" +c + " ";
				if (v instanceof FoodVisit) {
					if (((FoodVisit)v).getAddress()!= null) {
						description += ((FoodVisit)v).getAddress().replace("&", " and ");
						description += " ";
					}
					if (((FoodVisit)v).getOrder()!= null) 
						description += ((FoodVisit)v).getOrder().replace("&", " and ");
				}else
					description += v.getName();
				
				Journey j = FoodOSMHelper.getInstance().getJourney(prev, v, myVRP.getMode());
				time = time + ( j.getTravelTimeMS()) ;
				description += myVRP.getTimeOnlyformatter().format(time);
				out.addWaypoint(v, v.getName(), description);

				Visit curr = v;
				time  = time + deliveryTime;
				ArrayList<LatLon> p = j.getPath();
				out.addTrack(p);
				prev = curr;
				c++;
			}
			time = time + FoodOSMHelper.getInstance().getJourney(prev, myVRP.getStart(), myVRP.getMode()).getTravelTimeMS()*1.6;

			/*
			 * Add journey to end...
			 */

			Journey j = FoodOSMHelper.getInstance().getJourney(prev, myVRP.getEnd(), myVRP.getMode());
			
			time = time + ( j.getTravelTimeMS()) ;
			time  = time + deliveryTime;

			ArrayList<LatLon> p = j.getPath();
			out.addTrack(p);
			
			out.addWaypoint(myVRP.getEnd(),"Start", ""/* myVRP.getTimeOnlyformatter().format(time)*/);
			out.write("./"+r, "Run "+r);
			r++;
		}
	}
	public void setStartDateTime(String dateTime) {
		try {
			myVRP.setStartDate(new SimpleDateFormat("dd/MM/yyyy hh:mm").parse(dateTime).getTime());
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void setRoundMode(String type) {
		myVRP.setMode(type);
	}
	public void setCapacity(int cap) {
		myVRP.setCapacity(cap);
	}
	public void setMinsDel(int minsDel) {
		myVRP.setDeliveryTimeMS(minsDel*60000);

	}
	public void setMaxMinsRound(int maxMinsRound) {
		myVRP.setTimeLimitMS(maxMinsRound*60000);
	}

	private  FoodProblem myVRP;
	private  Geocoder geocode;
}
