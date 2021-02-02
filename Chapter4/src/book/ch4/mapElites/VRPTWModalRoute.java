package book.ch4.mapElites;

import book.ch4.CVRPTWProblem;
import book.ch4.VRPTWRoute;
import book.ch4.VisitNode;
import book.ch4.mapElites.ModalCostModel.Mode;


public class VRPTWModalRoute extends VRPTWRoute {

	private Mode myMode;
	
	public VRPTWModalRoute(CVRPTWProblem prob) {
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
		int mins = ((CVRPTWModalProblem)problem).getTravelMinutes(problem.getStart(), v.getVisit(),myMode);
		v.setMinsWaiting(0);
		this.start = v.getDeliveryTime().minusMinutes(mins);
		
		v = this.get(this.size()-1);
		mins = ((CVRPTWModalProblem)problem).getTravelMinutes(problem.getStart(), v.getVisit(),myMode);
		mins = mins + ((CVRPTWModalProblem)problem).getDeliveryTime();
		this.end = v.getDeliveryTime().plusMinutes(mins);
	}
}
