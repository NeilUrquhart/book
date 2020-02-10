package book.ch6.data;

import java.util.ArrayList;


public class Node {
	private long id;
	private LatLon location;
	private boolean used;
	private int index;
	
	public Node(long id, LatLon loc){
		this.id = id;
		this.location = loc;
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

	public LatLon getLocation(){
		return location;
	}
	public void setUsed(){
		used = true;
	}

	public boolean getUsed(){
		return used;
	}
	
	public double getDist(Node other){
		return this.location.getDist(other.location);
	}

	public String toString(){
		return id + "(" + location+")";
	}

	
}
