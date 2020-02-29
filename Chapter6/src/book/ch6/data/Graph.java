package book.ch6.data;

import info.pavie.basicosmparser.model.Node;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;



import book.ch6.parse.Parser;

public class Graph {
	private  HashMap<Long,RouterNode> nodeList = new HashMap<Long,RouterNode>();
	private HashMap<Long,RouterWay> wayList = new HashMap<Long,RouterWay>();
	private Matrix links;
	private int nodeCount;
	
	public Graph(String fName){
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
//public class Graph {
//	private  HashMap<Long,RouterNode> nodeList = new HashMap<Long,RouterNode>();
//	private HashMap<Long,Way> wayList = new HashMap<Long,Way>();
//	private Matrix links;
//	private int nodeCount;
//	
//	public Graph(String[] files){
//		for (String fName: files)
//			Parser.loadJSON(fName,this);
//
//
//		System.out.println("Nodes = " + nodeList.size());
//		nodeList = removeUnused();
//		System.out.println("Nodes = " + nodeList.size());
//
//		nodeCount = nodeList.size();
//		
//		links = new Matrix(nodeCount);//Way[nodeCount][];
//	
//		System.out.println("Stats:");
//		System.out.println("Nodes = " + nodeList.size());
//		System.out.println("Ways = " + wayList.size());
//
//		//Add ways  to links
//		for (Way w : wayList.values()){
//			RouterNode[] nodes = w.getNodes().;
//			for (int x=0; x < nodes.length; x++){
//				
//				for (int y=0; y < nodes.length; y++){
//					if (x != y){
//					
//						links.put(nodes[x].getIndex(),nodes[y].getIndex(),w);
//					}
//				}	 
//			}
//			
//		}
//	}
//	
//	public  int getNodeCount(){
//		return nodeCount;
//	}
//	
//	public  void addNode(RouterNode n){
//		if (nodeList.containsKey(n.getId()))
//			return;
//		n.setIndex(nodeCount);
//		nodeList.put(n.getId(), n);
//		nodeCount++;
//	}
//	
//	public boolean wayExists(String id){
//		if (wayList.containsKey(id))
//			return true;
//		else
//			return false;
//	}
//	
//	public Way getWay(String id){
//		return wayList.get(id);
//	}
//	public  void addWay(Way w){
//		if (wayList.containsKey(w.getID()))
//			return;
//		wayList.put(w.getID(),w);
//	}
//	
//	public boolean nodeExists(String id){
//		return nodeList.containsKey(id);
//	}
//
//	public boolean nodeExists(RouterNode n){
//		return nodeList.containsValue(n);
//	}
//
//	public RouterNode getNode(String id){
//		return nodeList.get(id);
//	}
//	
//	public  int nodeCount(){
//		return nodeCount;
//	}
//	
//	public Way getWay(RouterNode x, RouterNode y){
//		if ((x==null)||(y==null))
//			return null;
//		
//		return links.get(x.getIndex(),y.getIndex());
//	}
//	
//	public Collection<RouterNode> getNodes(){
//		return nodeList.values();
//	}
//	private HashMap<Long,RouterNode>removeUnused(){
//		HashMap<Long,RouterNode> res= new HashMap<Long,RouterNode>();
//		int index=0;
//		for(RouterNode n: nodeList.values()){
//			if (n.getUsed()){
//				n.setIndex(index);
//				res.put(n.getId(),n);
//				index++;
//			}
//		}
//		return res;
//	}
//	
//	public ArrayList<RouterNode> getNeighbours(RouterNode node) {
//		ArrayList<RouterNode> result = new ArrayList<RouterNode>();
//		Way[] linkedTo = links.getNeighbours(node.getIndex());
//		for (int c=0; c < linkedTo.length; c++ ){
//			if (linkedTo[c] != null){
//				for(RouterNode n : linkedTo[c].getNodes())
//					if (result.indexOf(n)==-1)
//						result.add(n);
//				//result.addAll(Arrays.asList(linkedTo[c].getNodes()));
//			}
//		}
//		return result;
//	}
//
//	
//}
