package book.ch6.data;

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
		//RouterNode[] res = new RouterNode[osmWay.getNodes().size()];
		//for (int c=0; c < res.length; c++){
		//	res[c] = new RouterNode(osmWay.getNodes().get(c));
		//}
		//return res;
		return nodes;
	}
	
	
	
	public String getName(){
		String name=osmWay.getTags().get("name");
		return name;
	}
	
	public long[] getNodeIDs(){
		long[] result = new long[osmWay.getNodes().size()];
		int c=0;
		for (Node n : osmWay.getNodes()){
			result[c]= Long.parseLong(n.getId());
			c++;
		}
		return result;
	}
	
	public String toString(){
		String res = id + " " + this.getName()+"\n";
		
		for (RouterNode n : this.getNodes())
			res = res + n + "\n";
		
		return res;
	}
	
	
}
//
//public class RouterWay {
//	private long id;
//	
//	
//	public RouterWay(long id, String name, String highway, Node[] someNodes){
//		this.id = id;
//		this.name = name;
//		this.highway = highway;
//		nodes = someNodes;
//	}
//	
//	public long getID(){
//		return id;
//	}
//	
//	public Node[] getNodes(){
//		return nodes;
//	}
//	
//	
//	
//	public String getName(){
//		return name;
//	}
//	
//	public long[] getNodeIDs(){
//		long[] result = new long[nodes.length];
//		int c=0;
//		for (Node n : nodes){
//			result[c]=n.getId();
//			c++;
//		}
//		return result;
//	}
//	
//	public String toString(){
//		String res = id + " " + name+"\n";
//		
//		for (Node n : nodes)
//			res = res + n + "\n";
//		
//		return res;
//	}
//	
//	public boolean equal(RouterWay other){
//		if (this.id != other.id){
//			System.out.println("1");
//			return false;
//		}
//		if ((this.name == null )&&(other.name != null)){
//			System.out.println("2");
//			return false;
//		}
//		if ((other.name == null )&&(this.name != null)){
//			System.out.println("3");
//			return false;
//		}
//				
//		if ((this.name !=null) && (other.name!=null))
//			if (!this.name.equals(other.name)){
//				System.out.println("4");
//				return false;
//			}
//		if (!this.highway.equals(other.highway)){
//			System.out.println("5\n" + this.highway + "\n"+other.highway);
//			return false;
//		}
//		
//		if (nodes.length != other.nodes.length){
//			System.out.println("6");
//			return false;
//		}
//		
//		if (!Arrays.equals(this.getNodeIDs(), other.getNodeIDs())){
//			System.out.println("7");
//			return false;
//		}
//					
//		return true;
//	}
//}