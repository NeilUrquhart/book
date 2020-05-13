package book.ch8.humanitarian;
/*
 * Neil Urquhart 2019
 * This programme tests a set of CVRP problem instances, using a range of solvers to produce solutions.

 * 
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.ArrayList;

import book.ch1.Visit;

import book.ch2.RandomSingleton;
import book.ch2.VRPVisit;
import book.ch7.GPXWriter;
import book.ch7.KMLWriter;
import book.ch7.Journey;
import book.ch7.OSMAccessHelper;


public class HudFacade  {
	private  HudProblem myVRP;
	
	public  void setProblem(HudProblem aProb) {
		myVRP = aProb;
		
	}
	
	public  HudProblem getProblem() {
		return myVRP;
	}

	public  void run()  {
		/*
		 * Solve the instance named in  <probName>
		*/	
		//Solve using the Evolutionary Algorithm
				
		//RandomSingleton rnd = RandomSingleton.getInstance();
		
		HudEA eaSolve = new HudEA();
		myVRP.solve(eaSolve);
		}
	
	public  String writeOut(String outFolder) throws Exception {
		String buffer="";
		int r=1;
			
	     double deliveryTime = myVRP.getDeliveryTimeMS();
		 double time = myVRP.getStartDate();
			
		 ArrayList<ArrayList<VRPVisit>> solution = myVRP.getCVRPSolution();
		 
		 if (myVRP.getInitialVisit()!= null) {
			 ArrayList<VRPVisit> run =solution.get(0);
			 run.add(0,myVRP.getInitialVisit());
		 }
			 
		 
			 
		for(ArrayList<VRPVisit> run :solution){
			int c=0;
			KMLWriter kml = new KMLWriter();
			GPXWriter gpx = new GPXWriter();
			
			File csvF = new File(outFolder+"/Run"+r+".csv");
		    PrintWriter csv = new PrintWriter(csvF);
		    	
		    
			kml.addPlacemark(myVRP.getStart().getX(), myVRP.getStart().getY(), "Start", "", "cafe");
			gpx.addWayPoint(myVRP.getStart().getX(), myVRP.getStart().getY(), "Start");
			
			buffer +=("Run " + r+ "\n");
			csv.println("Run," + r+ "\n");
			csv.println("Route number,Order ID,Products,Shipping Address,Postcode,Estim Dlivery Time");
			Visit prev = new Visit("",myVRP.getStart().getX(),myVRP.getStart().getY());
			buffer +=("Start" + myVRP.getTimeOnlyformatter().format(time)+"\n");
			csv.println("0,Start,,,,,"+myVRP.getStart().getName()+"," + myVRP.getTimeOnlyformatter().format(time));
			
			for (VRPVisit v : run){
				c++;
				String description = c +" ";
				if (v instanceof HudVisit) {
					if (((HudVisit)v).getAddress()!= null) {
					  description += ((HudVisit)v).getAddress().replace("&", " and ");
					description += " ";
					}
					if (((HudVisit)v).getOrder()!= null) 
						description += ((HudVisit)v).getOrder().replace("&", " and ");
				}else
					description += v.getName();
				
			    kml.addPlacemark(v.getX(), v.getY(),description + " " + myVRP.getTimeOnlyformatter().format(time), "", "delivery");
			    gpx.addWayPoint(v.getX(), v.getY(), description);
			    
				Visit curr = new Visit("",v.getX(),v.getY());
				
				Journey j = OSMAccessHelper.getJourney(prev, curr, myVRP.getMode());
				time = time + ( j.getTravelTimeMS()) ;
				buffer +=(c + " " +v.getName() + " " + myVRP.getTimeOnlyformatter().format(time)+"\n");
				if (v instanceof HudVisit){
					HudVisit f = (HudVisit)v;
					csv.println(c + "," +f.getName()+ ","+f.getOrder()+","+f.getAddress()+","+f.getPostCode()+"," + myVRP.getTimeOnlyformatter().format(time));
				}
				else {
					csv.println(c + "," +v.getName() + ",,,," + myVRP.getTimeOnlyformatter().format(time));
				}
				time  = time + deliveryTime;
				
				
				ArrayList<Visit> p = (ArrayList<Visit>) j.getAttribute(Journey.PATH);
				ArrayList<Double> lat = new ArrayList<Double>();
				ArrayList<Double> lon = new ArrayList<Double>();
				
				for (Visit l : p){
					lat.add(l.getX());
					lon.add(l.getY());	
				}
				kml.addRoute(lat, lon, "", "", "red");
				gpx.addPath(lat, lon);
				prev = curr;
			}
			time = time + OSMAccessHelper.getJourney(prev, myVRP.getStart(), myVRP.getMode()).getTravelTimeMS()*1.6;
		
			/*
			 * Add journey to end...
			 */
		
			Journey j = OSMAccessHelper.getJourney(prev, new Visit( "",myVRP.getEnd().getX(),myVRP.getEnd().getY()), myVRP.getMode());
			time = time + ( j.getTravelTimeMS()) ;
			buffer +=("End"  + " " + myVRP.getTimeOnlyformatter().format(time)+"\n");
			
			csv.println( (c+1)+"," +"End"+ ",,,"+myVRP.getEndPCode()+"," + myVRP.getTimeOnlyformatter().format(time));
			time  = time + deliveryTime;
			
			
			ArrayList<Visit> p = (ArrayList<Visit>) j.getAttribute(Journey.PATH);
			ArrayList<Double> lat = new ArrayList<Double>();
			ArrayList<Double> lon = new ArrayList<Double>();
			
			for (Visit l : p){
				lat.add(l.getX());
				lon.add(l.getY());	
			}
			kml.addRoute(lat, lon, "", "", "red");
			gpx.addPath(lat, lon);
		
			kml.addPlacemark(myVRP.getEnd().getX(), myVRP.getEnd().getY(), "End", "", "cafe");
			gpx.addWayPoint(myVRP.getEnd().getX(), myVRP.getEnd().getY(), "End");
		
	
		  //Done end
			kml.writeFile(outFolder+"/"+r);
			gpx.write(outFolder+"/"+r+".gpx", "gpxTest");
			String pageName = myVRP.getReference() +"-"+r+".htm";
	

			r++;
			csv.close();
			
		}
		String fName = myVRP.getReference();
		String pageName = fName +".htm";
		File summaryFile  = new File(outFolder+"/summary.csv");
		PrintWriter summary = new PrintWriter(summaryFile);
		summary.println(buffer);
		summary.close();
		return buffer;
	}
	
	

	
	
	
           
	
}
