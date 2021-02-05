package book.ch4.mapElites;

import book.ch4.*;

/*
 * Neil Urquhart 2021
 * 
 * ReloadNode is used within a Phenotype to represent a return to a depop and reloading. The 
 * decoder inserts reload nodes into the Phenotype when a route exceeds the vehicles capcity
 * 
 * We extend VisitNode so that ReloadNode objects can be inserted into routes easily
 * 
 * 
 */
public class ReloadNode extends book.ch4.VisitNode {

	public ReloadNode(VRPTWVisit v) {
		super(v);
	}
	
	public ReloadNode() {
		super(new VRPTWVisit("Depot (reload) ", 0,0,0,0,0));
	}
		public String toString() {
			return "DEPOT T=" + this.getDeliveryTime();
		}

}
