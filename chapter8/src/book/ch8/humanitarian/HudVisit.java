package book.ch8.humanitarian;



@SuppressWarnings("serial")
public class HudVisit extends book.ch2.VRPVisit {

	public HudVisit(String name, double lat, double lon, int demand) {
		super(name, lat, lon, demand);
		
	}
	
	private String address;
	private String order;
	private String postCode;
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
	public String getPostCode() {
		return postCode;
	}
	public void setPostCode(String postCode) {
		this.postCode = postCode;
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
