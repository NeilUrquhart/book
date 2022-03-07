package book.ch8.data;

import info.pavie.basicosmparser.model.Node;
import info.pavie.basicosmparser.model.Way;

import java.util.ArrayList;
import java.util.Arrays;

public class RouterWay {
	private long id;
	private Way osmWay;
	private RouterNode[] nodes;
	
	
	public RouterWay(Way aWay, RouterNode[] someNodes){
		this.id = Long.parseLong(aWay.getId().substring(1));
		osmWay = aWay;
		nodes = someNodes;
		}
	
	public long getID(){
		return id;
	}
	
	public RouterNode[] getNodes(){
		return nodes;
	}
	
	public String getHWayType(){
		
		return (String)osmWay.getTags().get("highway");
	}
	
	
	
	public String getName(){
		String name=osmWay.getTags().get("name");
		return name;
	}
	
//	public long[] getNodeIDs(){
//		long[] result = new long[osmWay.getNodes().size()];
//		int c=0;
//		for (Node n : osmWay.getNodes()){
//			result[c]= Long.parseLong(n.getId());
//			c++;
//		}
//		return result;
//	}
	
	public String toString(){
		String res = id + " " + this.getName()+"\n";
		
		for (RouterNode n : this.getNodes())
			res = res + n + "\n";
		
		return res;
	}
	
	
}
