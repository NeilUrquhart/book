package book.ch6.data;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/*
 * The Matrix class keeps track of which nodes are directly linked by a Way.
 * 
 * The representation uses a 2D Array of NodeWay objects 
 */

public class Matrix {
	private class NodeWay{
		public int id;
		public RouterWay way;
	}
	
	private NodeWay[][] links;

	public Matrix(int size){
		//Initialise a matrix for a graph with <size> nodes;
		//Note that NodeWay is not sizexsize, but is initially
		//sizex10. The node IDs used here are the position of 
		//the node within the Graph object and NOT the OSM ids.
		links = new NodeWay[size][];
		for (int c= 0; c < size; c++){
			links[c] = new NodeWay[10];
		}
	}

	public void put(int x, int y, RouterWay w){
		//Aad a link between node<x> and node<y> via Way <w>
		NodeWay nw = new NodeWay();
		nw.id = y;
		nw.way = w;
		//Find the first null entry in the matrix at row <x>
		int oldSize = links[x].length;
		for (int i=0; i < oldSize;i++)
			if (links[x][i]==null){
				links[x][i] = nw;
				return;
			}
		 //if there's no space in row <x> then resize the row by 5.
		 links[x] = Arrays.copyOf(links[x], oldSize + 5);
		 links[x][oldSize+1] = nw;
	}

	//If nodes <x> and <y> are linked by a way, return a reference 
	//to that way, if the nodes are not directly linked, return null.
	public RouterWay get(int x, int y){
		NodeWay[] row = links[x];
		for(NodeWay nw : row){
			if (nw!=null){
				if (nw.id==y)
					return nw.way;
			}
		}
		return null;
	}

	public RouterWay[] getNeighbours(int i){
		//Return a List of RouterWay objects that are
		//connected to Node <i>.
		RouterWay[] res = new RouterWay[links[i].length];	
		for (int x=0; x < links[i].length-1; x++){
			if (links[i][x] != null)
			  res[x] = links[i][x].way;
		}
		return res;
	}
}
