package book.ch8.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import book.ch8.parse.Parser;

public class Graph {
	private  HashMap<Long,RouterNode> nodeList = new HashMap<Long,RouterNode>();
	private HashMap<Long,RouterWay> wayList = new HashMap<Long,RouterWay>();
	private Matrix links;
	private int nodeCount;
	
	public Graph(String fName, ArrayList<String> excludedHighways){
		if (excludedHighways != null)
			Parser.setInvalid(excludedHighways);
		
		Parser.loadOSM(fName,this);
		nodeCount = nodeList.size();
		
		links = new Matrix(nodeCount);
		System.out.println("Stats:");
		System.out.println("Nodes = " + nodeList.size());
		System.out.println("Ways = " + wayList.size());
		//Add ways  to links
		for (RouterWay w : wayList.values()){
			RouterNode[] nodes =new RouterNode[w.getNodes().length];

			for (int c=0; c < w.getNodes().length;c++){
				nodes[c]= w.getNodes()[c];
			}
				
			for (int x=0; x < nodes.length; x++){
				
				for (int y=0; y < nodes.length; y++){
					if (x != y){
					
						links.put(nodes[x].getIndex(),nodes[y].getIndex(),w);
						
					}
				}	 
			}
		}
	}
	
	public Collection<RouterWay> getWays() {
		return wayList.values();
	}
	
	public  int getNodeCount(){
		return nodeCount;
	}
	
	public  void addNode(RouterNode n){
		if (nodeList.containsKey(n.getId()))
			return;
		
		n.setIndex(nodeCount);
		nodeList.put(n.getId(), n);
		nodeCount++;
	}
	
	public boolean wayExists(long id){
		if (wayList.containsKey(id))
			return true;
		else
			return false;
	}
	
	public RouterWay getWay(long id){
		return wayList.get(id);
	}
	public  void addWay(RouterWay w){
		if (wayList.containsKey(w.getID()))
			return;
		wayList.put(w.getID(),w);
	}
	
	public boolean nodeExists(long id){
		return nodeList.containsKey(id);
	}

	public boolean nodeExists(RouterNode n){
		return nodeList.containsValue(n);
	}

	public RouterNode getNode(long id){
		return nodeList.get(id);
	}
	
	public  int nodeCount(){
		return nodeCount;
	}
	
	public RouterWay getWay(RouterNode x, RouterNode y){
		if ((x==null)||(y==null))
			return null;
		
		return links.get(x.getIndex(),y.getIndex());
	}
	
	public Collection<RouterNode> getNodes(){
		return nodeList.values();
	}

	
	public ArrayList<RouterNode> getNeighbours(RouterNode node) {
		ArrayList<RouterNode> result = new ArrayList<RouterNode>();
		RouterWay[] linkedTo = links.getNeighbours(node.getIndex());
		
		for (int c=0; c < linkedTo.length; c++ ){
			if (linkedTo[c] != null){
				
				for(RouterNode n : linkedTo[c].getNodes()){
			
					
					if (result.indexOf(n)==-1)
						result.add(n);
				}
			}
		}
		
		return result;
	}

	
}
