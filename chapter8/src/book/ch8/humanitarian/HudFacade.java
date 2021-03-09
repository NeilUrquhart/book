package book.ch8.humanitarian;
/*
 * Neil Urquhart 2019
 * This programme tests a set of CVRP problem instances, using a range of solvers to produce solutions.

 * 
 */



import java.io.File;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import book.ch1.Hybrid;
import book.ch1.NearestNTSPSolver;
import book.ch1.Visit;
import book.ch2.VRPVisit;
import old.CSVWriter;
import old.DataFileWriter;
import old.GPXWriter;
import old.IVisit;
import old.Journey;
import old.KMLWriter;
import old.OSMAccessHelper;


public class HudFacade  {
	private  HudProblem myVRP;

	public HudFacade(String ref) {
		myVRP= new HudProblem();
		myVRP.setReference(ref);
	}

	public  void addVisit(HudVisit v) {
		myVRP.addVisit(v);
	}

	public void setDepot(IVisit start) {
		this.setDepot(start,start);
	}
	
	public void setDepot(IVisit start, IVisit end) {
		myVRP.setStart(start);
		myVRP.setEnd(end);
	}
	public  void solve(int runTime)  {
		
		OSMAccessHelper.setCacheSize();
		myVRP.setStartTime(System.currentTimeMillis());
		myVRP.setRunTime(runTime*60000);
		
		//Use NN to seed to EA -
		myVRP.solve(new NearestNTSPSolver());
		//Solve using the Evolutionary Algorithm
		myVRP.solve(new HudEA(myVRP.getSolution()));
	}

	public  void writeOut(String outFile, DataFileWriter out) throws Exception {
		int r=1;

		double deliveryTime = myVRP.getDeliveryTimeMS();
		double time = myVRP.getStartDate();

		ArrayList<ArrayList<VRPVisit>> solution = myVRP.getCVRPSolution();
		for(ArrayList<VRPVisit> run :solution){
			out.addWayPoint(myVRP.getStart(),myVRP.getTimeOnlyformatter().format(time));			
			IVisit prev = (IVisit)myVRP.getStart();//new IVisit("",myVRP.getStart().getX(),myVRP.getStart().getY(),1);
			for (VRPVisit vv : run){
				IVisit v = (IVisit)vv;
				String description = " ";
				if (v instanceof HudVisit) {
					if (((HudVisit)v).getAddress()!= null) {
						description += ((HudVisit)v).getAddress().replace("&", " and ");
						description += " ";
					}
					if (((HudVisit)v).getOrder()!= null) 
						description += ((HudVisit)v).getOrder().replace("&", " and ");
				}else
					description += v.getName();
				description += myVRP.getTimeOnlyformatter().format(time);
				out.addWayPoint(v, description);
				
				//IVisit curr = new IVisit("",v.getX(),v.getY(),1);
				IVisit curr = v;

				Journey j = OSMAccessHelper.getJourney(prev, curr, myVRP.getMode());
				time = time + ( j.getTravelTimeMS()) ;
				if (v instanceof HudVisit){
					HudVisit f = (HudVisit)v;
				}
				time  = time + deliveryTime;
				ArrayList<Visit> p = j.getPath();
				out.addPath(p);
				prev = curr;
			}
			time = time + OSMAccessHelper.getJourney(prev, (IVisit)myVRP.getStart(), myVRP.getMode()).getTravelTimeMS()*1.6;

			/*
			 * Add journey to end...
			 */

			Journey j = OSMAccessHelper.getJourney(prev, (IVisit)myVRP.getEnd(), myVRP.getMode());
			
			time = time + ( j.getTravelTimeMS()) ;
			time  = time + deliveryTime;


			ArrayList<Visit> p = j.getPath();
			out.addPath(p);
			
			out.addWayPoint(myVRP.getEnd(),  myVRP.getTimeOnlyformatter().format(time));
			out.write(outFile+"/"+r, "Run "+r);
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

	public void setRound(String type, int cap, int minsDel, int maxMinsRound) {
		myVRP.setCapacity(cap);
		myVRP.setDeliveryTimeMS(minsDel*60000);
		myVRP.setMode(type);
		myVRP.setTimeLimitMS(maxMinsRound*60000);
	}
}
