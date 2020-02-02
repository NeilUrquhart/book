package book.ch6.data;

import java.util.ArrayList;

public class Way {
	private long id;
	private String name;
	private String highway;
	private ArrayList<Node>  nodes;
	
	
	public Way(long id, String name, String highway){
		this.id = id;
		this.name = name;
		this.highway = highway;
		nodes = new ArrayList<Node>();
	}
	
	public long getID(){
		return id;
	}
	
	public ArrayList<Node> getNodes(){
		return nodes;
	}
	
	public void addNode(Node n){
		nodes.add(n);
	}
	
	public String getName(){
		return name;
	}
	
	public long[] getNodeIDs(){
		long[] result = new long[nodes.size()];
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
