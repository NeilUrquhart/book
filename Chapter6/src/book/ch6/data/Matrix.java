package book.ch6.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Matrix {
	private class NodeWay{
		public int id;
		public Way way;
	}
	private NodeWay[][] links;

	public Matrix(int size){
		links = new NodeWay[size][];
		for (int c= 0; c < size; c++){
			links[c] = new NodeWay[10];
		}
	}

	public void put(int x, int y, Way w){
		
		NodeWay nw = new NodeWay();
		nw.id = y;
		nw.way = w;
		int oldSize = links[x].length;
		for (int i=0; i < oldSize;i++)
			if (links[x][i]==null){
				links[x][i] = nw;
				return;
			}
		 
		 links[x] = Arrays.copyOf(links[x], oldSize + 5);
		 links[x][oldSize] = nw;
	}

	public Way get(int x, int y){
		NodeWay[] row = links[x];
		for(NodeWay nw : row)
			if (nw.id==y)
				return nw.way;
		
		return null;
	}
		

	public Way[] getNeighbours(int i){
		Way[] res = new Way[links[i].length];
		
		for (int x=0; x < links[i].length-2; x++){
			if (links[i][x] != null)
			  res[x] = links[i][x].way;
		}
		return res;
	}

	
}
//public class Matrix {
//	private Way[][] links;
//
//	public Matrix(int size){
//		links = new Way[size][];
//		for (int c=0; c < size; c++){
//			links[c] = new Way[size];
//		}
//	}
//
//	public void put(int x, int y, Way w){
//		links[x][y] =w;
//	}
//
//	public Way get(int x, int y){
//		return links[x][y];
//	}
//
//	public Way[] getNeighbours(int i){
//		return links[i];
//	}
//
//}
