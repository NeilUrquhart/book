package book.ch10.food;

import book.ch2.VRPVisit;

@SuppressWarnings("serial")
public class FoodVisit extends VRPVisit {
	private static int indexCounter =0;
	
	public FoodVisit(String name, double lat, double lon, int demand) {
		super(name, lat, lon, demand);
		this.index = indexCounter;
		indexCounter++;
	}
	
	public FoodVisit(String name, double lat, double lon, int demand, String address, String order) {
		this(name, lat, lon, demand);
		this.setAddress(address);
		this.setOrder(order);
	}
	
	private String address;
	private String order;
	private int index;
	
	public int getIndex() {
		return index;
	}
	
	public static int getIndexCounter() {
		return indexCounter;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
		
	public void setName(String name) {
		this.theName = name;
	}
	
	public void setDemand(int demand) {
		super.demand = demand;
	}
}
