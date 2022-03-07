package book.ch12.problem;

import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import book.ch1.LatLon;
import book.ch1.Visit;
import book.ch12.utils.MultiModeKMLWriter;
import book.ch12.utils.MarkDownHelper;
import book.ch7.routing.Journey;

public class Model {
	/*
	 * Neil Urquhart 2021
	 * 
	 * The Model class models a solution and allows it to be evaluated
	 * 
	 */
	private ArrayList<Courier> couriers = new ArrayList<Courier>();
	private double totalEmissions;
	private double totalFixedCost;
	private double totalRunningCost;
	private double totalTime;
	private double totalDist;
	private int totalVehicles;
	private boolean silent;
	private double byMD;

	//Evalute this model
	public Results evaluate(boolean silent,String name) 
	//if silent = true, don't print files
	{
		Results res = new Results();
		ArrayList <String> mdUsed = new ArrayList<String>();//keep count of the MDs used

		this.silent = silent;

		//Create a route for the deliveries each vehicle holds in the system
		MarkDownHelper md =null;
		if (!silent )
			md = new MarkDownHelper();

		//Track end time of vehicles to find the total time taken
		double endTime=0;
		for (Courier myVan : couriers)
		{
			String tourRes = "S1";//supply vehicle
			double t = processVehicle(myVan,0,md,tourRes,"S",res);
			if (endTime < t)
				endTime = t;
		}

		//Add in details for RVs
		for (Courier myVan : couriers)
		{
			for (Delivery d : myVan.getDeliveries()) {
				if (d instanceof SubTour ) {
					if (!mdUsed.contains(d.toString()))
						mdUsed.add(d.toString());
					myVan = ((SubTour) d).getVehicle();
					String mode = "";
					if (myVan.getDescription().equals("walk")) {
						res.addWalk();
						res.addMDUse(((SubTour) d).getMD(), true, false, false);
						mode = "W";
					}
					if (myVan.getDescription().equals("bike")) {
						res.addBike();
						res.addMDUse(((SubTour) d).getMD(), false, false,true);
						mode = "B";
					}
					if (myVan.getDescription().equals("electric van")) {
						res.addEVan();
						res.addMDUse(((SubTour) d).getMD(), false,true, false);
						mode = "E";
					}
					byMD = byMD +  myVan.getDeliveries().size();
					String tourString;
					tourString = d.getDescription();//"D" +d.getId();
					
					double t= processVehicle( myVan, ((SubTour) d).getTime(),md, tourString,mode,res);

					if (endTime < t)
						endTime = t;
				}
			}
		}

		PrintWriter pw = null;
		if(!silent){
			try{
				pw = new PrintWriter(MDProblem.getInstance().getWorkingDir()+"/result.md");
				md.setName(name.replace('_','-'));
				pw.write(md.getMarkDown());
				pw.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		totalTime = endTime;

		res.setDistance(this.totalDist);
		res.setEmissions(this.totalEmissions);
		res.setFixedCost(this.totalFixedCost);
		res.setMaxTime(totalTime);
		res.setRunningCost(this.totalRunningCost);
		res.setMicroDepots(mdUsed.size());
		//ByMD should be a %

		res.setByMD((byMD/MDProblem.getInstance().getQtyDeliveries())*100);
		return res;
	}

	//Add the vehicle to an array of vehicles
	public void addVehicle(Courier myVan)
	{
		couriers.add(myVan);
	}
	
	private double processVehicle(Courier myVan, double startTime, MarkDownHelper mdh,String tourString, String mode, Results res) {
		/*
		 * Process all of the deliveries associated with this vehicle
		 * Return the time of the last delivery
		 * 
		 */

		MultiModeKMLWriter kml=null;
		if (!silent)
			kml = new MultiModeKMLWriter();

		double time = startTime;
		double dist = 0;
		double emissions = 0;
		double runningCost=0;
		//Set the starting points for the vehicle
		LatLon previous = myVan.getStart();
		LatLon last = myVan.getStart();
		//An arraylist that holds the deliveries of each vehicle
		ArrayList<Delivery> delivery = myVan.getDeliveries();
		if (!silent)
			kml.addWaypoint(previous, "Start", "Main depot.","rv");

		//Get the destinations for each delivery held by the vehicle


		//Create an arraylist of each location sorted by which destination is closest to the previous
		ArrayList<Delivery> inOrder = optimiseRoute(previous, delivery /*, createpath*/, myVan.getType());
		myVan.setDeliveries(inOrder);

		//Get the destinations for each delivery held by the vehicle
		int idx=0;
		for (Delivery d : inOrder)
		{
			Visit currentlocation = d;//d.getLocation();
			String type = "del";
			if (d instanceof SubTour)
				type = "rv";
			if (!silent)
				kml.addWaypoint(currentlocation,totalVehicles+"."+idx, "("+currentlocation.getLat()+":"+currentlocation.getLon()+")",type);

			idx++;
		}

		//Create a journey that supports multiple points
		ArrayList<Journey> route = new ArrayList<Journey>();

		totalVehicles++;
		//sb.append("Delivery details for vehicle ID: " + myVan.getId() +","+myVan.getDescription());HTML

		//Append return to depot 
		String dels="";
		inOrder.add(new Delivery(last,"depot"));
		//Use an algorithm to create a route between each destination for the deliveries
		for (Delivery nextDel : inOrder)
		{
			if (!(nextDel instanceof SubTour)) {
				if (!nextDel/*.getLocation()*/.getName().equals("depot"))
					tourString = tourString + "C" + nextDel.getId() + mode;
			}

			Visit nextDest = nextDel;//.getLocation();
			dels += nextDest.getName() + "," + nextDest.getLat() + "," +nextDest.getLon() +"<br>";

			Journey fullTravel = Router.getInstance().findRoute(last, nextDest,myVan.getType());
			//Work out the total distance travelled by vehicle and the total emissions of the vehicle
			double currentDist = fullTravel.getDistanceKM();
			dist = dist + currentDist;
			emissions = emissions + (fullTravel.getDistanceKM() * myVan.getEmissions());
			time = time + fullTravel.getTravelTimeMS();//(currentDist / myVan.getSpeed())*60;//Time mins
			nextDel.setTime(time);
			//Add the current route to the journey
			route.add(fullTravel);
			//Append the points travelled to the CSV file
			//The last location travelled to becomes the current one
			last = nextDest;

		}

		//Work out the total amount of time taken to deliver the deliveries and add details of the delivery to the csv file
		totalDist += dist;
		runningCost += (myVan.getCostKM()*dist);
		totalEmissions += emissions;
		totalFixedCost += myVan.getFixedCost();
		totalRunningCost += runningCost;

		DecimalFormat df = new DecimalFormat("#.00"); 
		if(mdh!=null)
			mdh.addVehicle(""+myVan.getId(), myVan.getDescription(), dels, df.format(time),df.format(emissions), df.format(dist));

		ArrayList<LatLon> routes =new ArrayList<LatLon>();
		for (Journey r : route) {
			ArrayList<LatLon> locs = r.getPath();
			routes.addAll(locs);
			//Add walks 
			if (r.getAttribute("walkA")!= null) {
				//System.out.println("Additional walk");
				ArrayList<LatLon> walklocs = (ArrayList) r.getAttribute("walkA");
				if (!silent)
					kml.addTrack(walklocs, myVan.getId() + " (walk) " + myVan.getType(), myVan.getDescription(), "green");
			}
			if (r.getAttribute("walkB")!= null) {
				ArrayList<LatLon> walklocs = (ArrayList) r.getAttribute("walkB");
				if (!silent)
					kml.addTrack(walklocs, myVan.getId() + " (walk) " + myVan.getType(), myVan.getDescription(), "green");
			}
		}

		String colour = "";
		if (myVan.getDescription().equals("ConventionalVan"))
			colour = "red";
		if (myVan.getDescription().equals("bike"))
			colour = "blue";
		if (myVan.getDescription().equals("walk"))
			colour = "green";
		if (myVan.getDescription().equals("electric van"))
			colour = "yellow";

		if(!silent)
			kml.addTrack(routes, myVan.getId() + " " + myVan.getType(), myVan.getDescription(), colour);
		//Add walks

		//Try catch for saving the kml file
		try
		{
			if (!silent){
				String kmlName = MDProblem.getInstance().getWorkingDir()+"/Vehicle " + myVan.getId();
				kml.writeFile( kmlName);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("Error saving KML");
		}
		return time;
	}

	private static ArrayList<Delivery> optimiseRoute(LatLon previous, ArrayList<Delivery> destinations,/* SimpleRouter createpath,*/ String type)
	{
		//Sort into nearest neighbour order from currentLocation
		ArrayList<Delivery> ordered  = new ArrayList<Delivery>();
		//Dummy delivery start
		ordered = MDProblem.getInstance().NNSort(previous, destinations,type);		
		return ordered;
	}
}