package book.ch6.data;

import info.pavie.basicosmparser.model.Node;

import java.util.ArrayList;

public class RouterNode {
	private int index;
	private Node osmNode;
	private boolean uplink;//Used for multi-layer graphs
	
	public RouterNode(Node aNode){
		osmNode = aNode;
	}

	public void setUplink(){
		uplink = true;
	}
	
	public boolean isUplink(){
		return uplink;
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
