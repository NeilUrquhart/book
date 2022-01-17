package book.ch12.mapElites;

import java.util.Random;

import book.ch12.problem.MDProblem;
import book.ch12.problem.MicroDepot;
import book.ch12.problem.Courier;

/*
 * Neil Urquhart 2021
 * 
 * A single gene. Each gene represents one possible sub tour
 * comprising an 
 *    md, 
 *    point at which it is applied to the genome
 *    no of points to add
 *    courier type
 */

public class Gene implements Cloneable{

	public void setCourier(Courier courier) {
		this.courier = courier;
	}

	public void setDelCount(int delCount) {
		this.delCount = delCount;
	}

//	public void applicationPoint(int appPoint) {
//		this.applicationPoint = appPoint;
//	}

	private Courier courier; //Vehicle type
	//private int applicationPoint; //Position in tour
	private int delCount; //No of deliveries to add
	private MicroDepot microDepot; //md
	
	private Random rnd = new Random();
	
	public Courier getCourier() {
		return courier;
	}

//	public int getApplictionPoint() {
//		return applicationPoint;
//	}

	public int getDelCount() {
		return delCount;
	}

	public MicroDepot getMicroDepot() {
		return microDepot;
	}

	public void setMicroDepot(MicroDepot md) {
		this.microDepot =md;
	}
	
//	public void setapplicationPoint(int applicationPoint) {
//		this.applicationPoint = applicationPoint;
//	}

	public Gene(){
		this.randomize();
	}

	public Object clone() {
		try {
			Gene res = (Gene) super.clone();
			return res;
		}
		catch (CloneNotSupportedException e) {
			System.out.println("CloneNotSupportedException comes out : "+e.getMessage());
			return null;
		}
	}

	public void randomize(){
		courier  = MDProblem.getInstance().getRVVehicle(MDProblem.getInstance().getRndRVVehicleIndx());
		delCount = courier.getCapacity();
		microDepot = MDProblem.getInstance().randMicroDepot();
//		applicationPoint = MDProblem.getInstance().getRndInsertPointIndx();
	}

	public void mutateGene(){
		int choice = rnd.nextInt(3);
		if (choice==0){
			if (rnd.nextBoolean()){
				delCount++;
				if (delCount>courier.getCapacity())delCount=courier.getCapacity();
			}else{
				delCount--;
				if (delCount <1) delCount=1;
			}
		}
		else if(choice ==1)
			microDepot = MDProblem.getInstance().randMicroDepot();

		else if(choice ==2)
			courier = MDProblem.getInstance().getRVVehicle(MDProblem.getInstance().getRndRVVehicleIndx());
	}
}
