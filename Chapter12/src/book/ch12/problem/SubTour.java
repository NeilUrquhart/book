package book.ch12.problem;


import java.util.ArrayList;

import book.ch1.LatLon;
import book.ch1.Visit;

/*
 * Neil Urquhart 2021
 * Represents a courier tour (sub tour) that operates from a Micro Depot
 * Extends Delivery, so that it can be placed into a GrandTour in place of a delivery
 */
public class SubTour extends Delivery {
	//Represents a set of deliveries which are to be transferred en-bloc to another vehicle

	private Courier courier = null;
	private MicroDepot md;
	
	public SubTour(MicroDepot d, Courier v) {
		super(d.getLat(),d.getLon(),"RV Point - Vehicle "+ v.getId() + "(" + v.getDescription()+")");
		courier = v;
		v.setStart(d);
		md = d;
	}
	
	public MicroDepot getMD() {
		return md;
	}
	
//	public SubTour(Visit l, Courier v) {
//		super(l.getLat(),l.getLon(),l.getName());
//		courier = v;
//		courier.setStart(l);
//	}
	
	public String getDescription() {
		return super.getDescription();
	}
	
	public boolean includeDelivery(Delivery d) {
		return courier.addDelivery(d);
	}
	
	public ArrayList<Delivery> getDeliveries(){
		return courier.getDeliveries();
	}
	@Override
	public int getWeight() {
		int w=0;
		for(Delivery d : courier.getDeliveries()) {
			w+= d.getWeight();
		}
		return w;
	}
	
	public void setVehicle(Courier v) {
		this.courier = v;
	}
	
	public Courier getVehicle() {
		return this.courier;
	}
}
