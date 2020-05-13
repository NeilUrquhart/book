package book.ch8.humanitarian;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import book.ch1.Visit;
import book.ch2.CVRPProblem;
import book.ch2.VRPVisit;
import book.ch7.OSMAccessHelper;

/*
 * Neil Urquhart 2019
 * This class represents a FoodCVRP problem.
 * It extends the basic CVRPProblem class.
 * 
 */
public class HudProblem extends CVRPProblem {
	private SimpleDateFormat dateOnlyformatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private String startPCode;
    private String endPCode;
    private Visit end;
    private String mode;
    
    private HudVisit initialVisit;
    
    public HudVisit getInitialVisit() {
		return initialVisit;
	}

	public void setInitialVisit(HudVisit initialVisit) {
		this.initialVisit = initialVisit;
	}

	public void setMode(String aMode) {
    	if (aMode.contains("Car"))
    		mode = "car";
    	
    	if (aMode.contains("cycle"))
    		mode = "bike";
    	
    	if (aMode.contains("Walk"))
    		mode = "foot";
    	
    	
    	//mode = aMode;
    }
    
    public String getMode() {
    	return mode;
    }

    public Visit getEnd() {
		return end;
	}

	public void setEnd(Visit end) {
		this.end = end;
	}

	public void setEndPCode(String pCode) {
    	endPCode = pCode;
    }
    
    public String getEndPCode() {
    	return endPCode;
    }
    
    
    public void setStartPcode(String pc) {
    	this.startPCode = pc;
    }
    
    public String getStartPcode() {
    	return this.startPCode;
    }
    
	public SimpleDateFormat getDateOnlyformatter() {
		return dateOnlyformatter;
	}


	public SimpleDateFormat getTimeOnlyformatter() {
		return timeOnlyformatter;
	}

		private SimpleDateFormat timeOnlyformatter = new SimpleDateFormat("HH:mm");

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public double getTimeLimitMS() {
		return timeLimitMS;
	}

	public void setTimeLimitMS(double timeLimitMS) {
		this.timeLimitMS = timeLimitMS;
	}

	public double getDeliveryTimeMS() {
		return deliveryTimeMS;
	}

	public void setDeliveryTimeMS(double deliveryTimeMS) {
		this.deliveryTimeMS = deliveryTimeMS;
	}

	

	public double getStartDate() {
		return startDate;
	}

	public void setStartDate(double startDate) {
		this.startDate = startDate;
	}
	private String reference;
	private double timeLimitMS;
	private double deliveryTimeMS;
	private double speedfactor;
	private double startDate;
	private String dateTime;
	
//	
//	public double getDistance(){
//		//Return the total distance travelled by all of the vehicles
//		return getDistance(currentVRPSolution);
//	}
//	
	
	public String isValid() {
		//Check to see if the problem contains enough info to solve. If it does return "" else
		//return an error msg
		String result  = "";
		if (reference == null)
			result += "No problem reference\n";
		if (timeLimitMS == 0)
			result += "No round time limit entered\n";
		if (startDate == 0)
			result += "No start date entered\n";
		if (this.getSize() < 2)
			result += "You need at least 2 deliveries\n";
		if (this.getCapacity()==0)
			result += "No delivery capacity\n";
		if (this.getStart() == null)
			result += "You need to enter a start location\n";
		
		return result;
	}
	
	public ArrayList<String>  getPostalAreas(){
		ArrayList<String> result = new ArrayList<String>();
		
		for (Object ov : this.getVisits()) {
			if (ov instanceof HudVisit) {
				HudVisit v = (HudVisit) ov;
				String pCodeArea = v.getPostCode().split(" ")[0];
				if (!result.contains(pCodeArea)) {
					result.add(pCodeArea);
				}
			}
		}
			
		
		return result;
	}
	
	public void filterPCodeArea(String[] pCodes) {
			int counter =0;
		while (counter < this.getVisits().size()) {
		   Object ov = this.getVisits().get(counter);
			if (ov instanceof HudVisit) {
				HudVisit v = (HudVisit) ov;
				boolean found = false;
				for(String pCode: pCodes)
				  if (v.getPostCode().contains(pCode+' ')) {
					  found = true;
					  break;
				  }
					  
				 if (!found)
					this.getVisits().remove(v);
				else
					counter ++;
			}else {
				counter++;
			}
		}
	}
	
	public ArrayList<String>  getShippingDates(){
		ArrayList<String> result = new ArrayList<String>();
		
		for (Object ov : this.getVisits()) {
			if (ov instanceof HudVisit) {
				HudVisit v = (HudVisit) ov;
				String shipDate = v.getShippingDate();
				if (!result.contains(shipDate)) {
					result.add(shipDate);
				}
			}
		}
			
		
		return result;
	}
	
	public void filterShipDates(String[] dates) {
		int counter =0;
		while (counter < this.getVisits().size()) {
		   Object ov = this.getVisits().get(counter);
			if (ov instanceof HudVisit) {
				HudVisit v = (HudVisit) ov;
				boolean found = false;
				for(String date: dates)
				  if (v.getShippingDate().equals(date)) {
					  found = true;
					  break;
				  }
					  
				 if (!found)
					this.getVisits().remove(v);
				else
					counter ++;
			}else {
				counter++;
			}
		}
	}
	
	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public double getSpeedfactor() {
		return speedfactor;
	}

	public void setSpeedfactor(double speedfactor) {
		this.speedfactor = speedfactor;
	}

	public double getDistance(ArrayList sol){
		//Get the total distance covered by the solution in <sol>
		ArrayList<ArrayList<VRPVisit>> solution = (ArrayList<ArrayList<VRPVisit>>) sol;
		double dist =0;
		int visitCount = super.getSize();
		for (ArrayList<VRPVisit> route: solution){
		    ArrayList<Visit> r = new ArrayList<Visit>(route);
			dist = dist + getRouteDistance(r,visitCount);
			visitCount  = visitCount - route.size();
			
		}
		return dist;
	}

	public double getRouteDistance(ArrayList<Visit> possibleRoute,int visitcount){
		//Get the distance of the visits within possibleRoute - include the start point as well to complete the circuit
		/* this has to be a weighted distance)*/
		double dist =0;
		Visit previousCity = super.getStart();
		for (Visit city : possibleRoute){//go through each city
			double wDist = getDistance(previousCity, city)*(visitcount);//*0.2);//Add weighting
			dist = dist + wDist;
			visitcount --;
			previousCity = city;
		}
		dist = dist + getDistance(previousCity, this.end);//super.getStart());
		return dist;
	}
	public double getDistance(Visit x, Visit y){
		//Get the distance between two visits
		if ((x == null)||(y==null))
			return 0;
		else			{
		//	Location lx = new Location(x.getX(),x.getY());
		//	Location ly = new Location(y.getX(),y.getY());
			
			return OSMAccessHelper.getJourney(x, y, mode).getDistanceKM();
		}
	}

	public void addVisit(HudVisit newVis) {
		//Check for duplicate postcodes
		String pc = newVis.getPostCode();
		
		for (Object ov: super.getVisits()) {
			try {
				HudVisit v = (HudVisit)ov;
				if (v.getPostCode().equals(newVis.getPostCode())) {
					//Combine visits
					v.setOrder(v.getName() +" : " +v.getOrder() +" AND "+ newVis.getName() + " : "+newVis.getOrder());
					v.setName(v.getName() +"+"+newVis.getName());
					v.setDemand(v.getDemand() + newVis.getDemand());
					return;
				}
			}catch(Exception e) {}//Ignore non FoodVisits
		}
		super.addVisit(newVis);
		
	}
		
	
	public String toString() {
		String buffer = "";
		
		for (Object visit : this.getVisits()) {
			buffer = buffer + visit.toString() +"\n";
		}
		
		return buffer;
	}
	

}
