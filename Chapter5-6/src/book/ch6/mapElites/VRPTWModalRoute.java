package book.ch6.mapElites;

import book.ch5.VRPTWProblem;
import book.ch5.VRPTWRoute;
import book.ch5.VisitNode;
import book.ch6.mapElites.SupermarketCostModel.Mode;


public class VRPTWModalRoute extends VRPTWRoute {
	/*
	 * Neil Urquhart 2021
	 * 
	 * ExtendVRPTWRoute to take into account the mode used
	 * 
	 */

	private Mode myMode; //Mode is defined in ModalCostModel
	
	public VRPTWModalRoute(VRPTWProblem prob) {
		super(prob);
	}
	
	public void setMode(Mode m) {
		myMode = m;
	}
	

	public Mode getMode() {
		return myMode;
	}
	
	public String toString() {
		String buffer ;
		if (myMode == Mode.CYCLE)
			buffer = "CYCLE,";
		else
			buffer = "VAN,";
		
		buffer += start.toString() +",";
		
		for (VisitNode v : this)
			buffer += v.toString() +",";
		
		buffer += end.toString() +"\n";
		return buffer;
	}
	
	public void setStartEndTimes() {
		/*
		 * Set the start and end times from the depot based of route
		 * 
		 */
		if (this.size()<1)
			return;
		//set start and end times
		VisitNode v = this.get(0);
		int mins = ((SupermarketProblem)problem).getTravelMinutes(problem.getStart(), v.getVisit(),myMode);
		v.setMinsWaiting(0);
		this.start = v.getDeliveryTime().minusMinutes(mins);
		
		v = this.get(this.size()-1);
		mins = ((SupermarketProblem)problem).getTravelMinutes(problem.getStart(), v.getVisit(),myMode);
		mins = mins + ((SupermarketProblem)problem).getDeliveryTime();
		this.end = v.getDeliveryTime().plusMinutes(mins);
	}
}
