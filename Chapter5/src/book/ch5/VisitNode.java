package book.ch5;

import java.time.LocalTime;
/*
 * Neil Urquhart 2020
 * 
 * VisitNode: Used by the Route object to build the VRPTW Phenotype
 * Holds a reference to the VRPVisit object an the time of the actual visit and waitring time
 * 
 */

	public class VisitNode{
		private LocalTime visitTime=null; //The actual time of the arrival (must be within the time window of the visit).
		private int minsWaiting;//If the arrival is before the time window, this holds the number of  minutes that the vehicle must wait for the tw to start
		private VRPTWVisit myVisit;
		
		public VisitNode(VRPTWVisit v) {
			myVisit = v;
		}
		
		public VRPTWVisit getVisit() {
			return myVisit;
			
		}
		public void setDeliveryTime(LocalTime t) {
			visitTime = t;
		}
		
		public LocalTime getDeliveryTime() {
			return visitTime;
		}


		public int getMinsWaiting() {
			return minsWaiting;
		}

		public void setMinsWaiting(int minsWaiting) {
			this.minsWaiting = minsWaiting;
		}
		
		public String toString() {
			return this.myVisit + " T=" + this.getDeliveryTime() + " w="+ this.getMinsWaiting();
		}
	}

