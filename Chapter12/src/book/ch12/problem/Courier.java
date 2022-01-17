package book.ch12.problem;

import java.util.ArrayList;
import book.ch1.LatLon;

public class Courier implements Cloneable{
	/*
	 * Neil Urquhart 2021
	 * 
	 * A class that represents a single courier.
	 * 
	 */
	private String description;
	private double emissions;
	private double fixedCost;
	private double costKM;
	private int speed;
	private int capacity;
	private int myId;
	private LatLon start;
	private String type;//car,bike or walk

	private ArrayList<Delivery> parcels;//Holds the parcels allocated to this vehicle

	public Courier(String type)
	{
		this.type  = type;
		parcels = new ArrayList<Delivery>();
	}

	public String getType(){
		return type;
	}
	
	public void setID(int id){
		myId = id;
	}

	public void clearDeliveries(){
		this.parcels.clear();
	}
	public double getFixedCost() {
		return fixedCost;
	}

	public void setFixedCost(double fixedCost) {
		this.fixedCost = fixedCost;
	}

	public double getCostKM() {
		return costKM;
	}

	public void setCostKM(double costKM) {
		this.costKM = costKM;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean addDelivery(Delivery d) {
		if (this.getSpareCapacity() >= d.getWeight()) {
			parcels.add(d);
			return true;
		}
		return false;
	}

	private int getSpareCapacity() {
		int used=0;
		for(Delivery d : parcels)
			used += d.getWeight();

		return this.capacity - used;
	}

	//Getters and setters for the variables
	public double getEmissions() {
		return emissions;
	}
	public void setEmissions(double emissions) {
		this.emissions = emissions;
	}

	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	public  int getId() {
		return myId;
	}

	public ArrayList<Delivery> getDeliveries() {
		return parcels;
	}

	public void setDeliveries(ArrayList<Delivery> deliveries) {
		this.parcels = deliveries;
	}

	public LatLon getStart() {
		return start;
	}

	public void setStart(LatLon start) {
		this.start = start;
	}

	public Object clone() {
		try {
			Courier res = (Courier) super.clone();
			res.parcels = new ArrayList<Delivery>(this.parcels);
			return res;
		}
		catch (CloneNotSupportedException e) {
			System.out.println("CloneNotSupportedException comes out : "+e.getMessage());
			return null;
		}
	}
}
