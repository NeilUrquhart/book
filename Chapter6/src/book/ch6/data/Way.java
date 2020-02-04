package book.ch6.data;

import java.util.ArrayList;

public class Way {
	private long id;
	private String name;
	private String highway;
	private Node[]  nodes;
	
	
	public Way(long id, String name, String highway, Node[] someNodes){
		this.id = id;
		this.name = name;
		this.highway = highway;
		nodes = someNodes;
	}
	
	public long getID(){
		return id;
	}
	
	public Node[] getNodes(){
		return nodes;
	}
	
	
	
	public String getName(){
		return name;
	}
	
	public long[] getNodeIDs(){
		long[] result = new long[nodes.length];
		int c=0;
		for (Node n : nodes){
			result[c]=n.getId();
			c++;
		}
		return result;
	}
	
	public String toString(){
		String res = id + " " + name+"\n";
		
		for (Node n : nodes)
			res = res + n + "\n";
		
		return res;
	}
}
