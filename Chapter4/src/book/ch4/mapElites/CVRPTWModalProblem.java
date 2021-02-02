package book.ch4.mapElites;

import book.ch1.Visit;
import book.ch4.CVRPTWProblem;
import book.ch4.VRPTWVisit;
import book.ch4.mapElites.ModalCostModel.Mode;

public class CVRPTWModalProblem  extends CVRPTWProblem{

	public int getTravelMinutes(Visit start, Visit visit, Mode myMode) {
		double dist = super.getDistance(start, visit);
		double speed = ModalCostModel.getInstance().getSpeed(myMode);
		//Assume raw dist is kms
		double timeMins = dist/speed;
		//Not interested in seconds 
		return (int) timeMins;
	}

}
