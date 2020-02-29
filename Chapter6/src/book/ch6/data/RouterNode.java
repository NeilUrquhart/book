package book.ch6.data;

import info.pavie.basicosmparser.model.Node;

import java.util.ArrayList;

public class RouterNode {
	private int index;
	private Node osmNode;
	
	public RouterNode(Node aNode){
		osmNode = aNode;
	}

	
	public int getIndex(){
		return index;
	}
	public void setIndex(int indx){
		this.index = indx;
	}
	
	public long getId(){
		
		return Long.parseLong(this.osmNode.getId().substring(1));
	}

	public LatLon getLocation(){
		LatLon location = new LatLon(this.osmNode.getLat(), this.osmNode.getLon());
		return location;
	}
	
	
	public double getDist(RouterNode other){
		return this.getLocation().getDist(other.getLocation());
	}

	public String toString(){
		return this.getId()+ "(" + this.getLocation()+")";
	}
	
}


//public class Node {
//	private long id;
//	private LatLon location;
//	private boolean used;
//	private int index;
//	
//	public Node(long id, LatLon loc){
//		this.id = id;
//		this.location = loc;
//		used = false;
//	}
//
//	
//	public int getIndex(){
//		return index;
//	}
//	public void setIndex(int indx){
//		this.index = indx;
//	}
//	public long getId(){
//		return this.id;
//	}
//
//	public LatLon getLocation(){
//		return location;
//	}
//	public void setUsed(){
//		used = true;
//	}
//
//	public boolean getUsed(){
//		return used;
//	}
//	
//	public double getDist(Node other){
//		return this.location.getDist(other.location);
//	}
//
//	public String toString(){
//		return id + "(" + location+")";
//	}
//	
//	public boolean equal(Node other){
//		if (id != other.id)
//			return false;
//		if (!location.equal(other.location))
//			return false;
//
//		return true;
//	}
//
//	
//}
