package book.ch4.mapElites;

import book.ch4.*;

public class ReloadNode extends book.ch4.VisitNode {

	public ReloadNode(VRPTWVisit v) {
		super(v);
	}
	
	public ReloadNode() {
		super(new VRPTWVisit("Depot", 0,0, 0, 0, 0));
	}
		public String toString() {
			return "DEPOT T=" + this.getDeliveryTime();
		}

}
