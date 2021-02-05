package book.ch4.mapElites;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import book.ch1.Visit;
import book.ch4.CVRPTWProblem;
import book.ch4.VRPTWVisit;
import book.ch4.mapElites.ModalCostModel.Mode;

public class CVRPTWModalProblem  extends CVRPTWProblem{
	/*
	 * Neil Urquhart 2021
	 * 
	 * Extends CVRPTWProblem to take account of multiple travel modes
	 * 
	 */

	public CVRPTWModalProblem(CVRPTWProblem base) {
		/*
		 * Construct a new object copied from an instance of the base (upcasting)
		 * 
		 */

		this.setCapacity(base.getCapacity());
		this.setStart(base.getStart());
		this.currentSolution  =base.getVisits();
	}
	
	public int getTravelMinutes(Visit x, Visit y, Mode myMode) {
		/*
		 * Return the travel time between <x> and <y> when using <myMode>
		 */
		double dist = super.getDistance(x, y);
		double speed = ModalCostModel.getInstance().getSpeed(myMode);
		//Assume raw dist is kms
		double timeMins = dist/speed;
		//Not interested in seconds 
		return (int) timeMins;
	}

}
