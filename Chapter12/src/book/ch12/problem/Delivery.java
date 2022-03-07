package book.ch12.problem;
import book.ch1.LatLon;
import book.ch1.Visit;

/*
 * Neil Urquhart 2021
 * This class is an extension of Visit to include 
 *   an ID number (allocated within the constructor
 *   the weight of the parcel
 *   the time of delvery
 * 
 */
public class Delivery extends Visit{

	private static int idCounter;//Used to keep track of all IDs issued
	
	private int weight;
	//private Visit destination; //The actual location that the delivering is being made to
	private int id;
	private double time = 0;

	//Automatically increment the ID of the delivery and create a destination for the delivery
	public Delivery(double x, double y,  String description)
	{
		super(description, x, y);
		idCounter++;
		id = idCounter;//set the delivery id
	}
	
	public Delivery(LatLon l,String desc){
		this(l.getLat(),l.getLon(),desc);
	}

	public String getDescription() {
		return super.getName();
	}
	//Getters and setters for the variables
	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getId() {
		return id;
	}

	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}
	
	public String toString(){
		return super.toString() +","+this.time;
	}
}
