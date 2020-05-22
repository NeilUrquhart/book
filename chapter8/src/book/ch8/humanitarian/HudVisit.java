package book.ch8.humanitarian;

import book.ch7.IVisit;

@SuppressWarnings("serial")
public class HudVisit extends IVisit {

	public HudVisit(String name, double lat, double lon, int demand) {
		super(name, lat, lon, demand);
	}
	
	public HudVisit(String name, double lat, double lon, int demand, String address, String order, String shipingDate) {
		this(name, lat, lon, demand);
		this.setAddress(address);
		this.setOrder(order);
		this.setShippingDate(shipingDate);
	}
	
	private String address;
	private String order;
	private String shippingDate;
	
	
	
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
	public String getShippingDate() {
		return shippingDate;
	}
	public void setShippingDate(String shippingDate) {
		this.shippingDate = shippingDate;
	}
	
	
}
