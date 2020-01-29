package book.ch6;

public class Node {
	private long id;
	private double lat;
	private double lon;
	private boolean used;
	private int index;
	
	public Node(long id, double lat, double lon){
		this.id = id;
		this.lat = lat;
		this.lon = lon;
		used = false;
	}
	
	public int getIndex(){
		return index;
	}
	public void setIndex(int indx){
		this.index = indx;
	}
	public long getId(){
		return this.id;
	}
	
	public String getCoords(){
		return lat + "\t" + lon;
	}
	public void setUsed(){
		used = true;
	}
	
	public boolean getUsed(){
		return used;
	}
	
	public String toString(){
		return id + "(" + lat +","+ lon+")";
	}
	
	public double getDist(Node other){
		 double x1 = this.lat; 
		  double y1 = this.lon; 
		  double x2 = other.lat; 
		  double y2= other.lon;        
		    return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
	}
}
