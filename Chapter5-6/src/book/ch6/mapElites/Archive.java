package book.ch6.mapElites;

import java.util.ArrayList;
import book.ch2.RandomSingleton;

public class Archive {
	/*
	 * Neil Urquhart 2021
	 * This class implements an archive for use with MAPElites.
	 * Objects to be stored within the archive must implement the Elite
	 * interface.
	 * 
	 * The archive itself is implemented as a tree structure.
	 */

	private RandomSingleton rnd = RandomSingleton.getInstance();
	//Note that we use the RandomSingleton object to generate random numbers

	private int buckets;//The number of buckets used per feature
	private int dimensions;//The dimensions of the feature space
	private int size=0;//The number of objects in the archive
	private Object[] root;//The root of the tree
	private ArrayList<Elite> items;//Used by the toList methods

	public Archive(int _dimensions, int _buckets) {
		dimensions = _dimensions;
		buckets = _buckets;
		root= new Object [buckets];
	}

	public  Elite get(int ... key) {
		/*
		 * Return the object accessed by <key>
		 * If there's no object in the cell, return null
		 */
		Object[] pointer = root;
		for (int x =0; x <= key.length-2; x++) {
			int k = key[x];
			if (pointer[k]== null)
				return null;
			pointer = (Object[]) pointer[k];
		}
		int k = key[key.length-1];
		return (Elite) pointer[k];
	}

	public boolean put(Elite e) {
		/*
		 * Put an Elite object into the archive. If the cell is already
		 * occupied only add the object if it's fitness is < than the 
		 * fitness of the object already in the cell
		 * 
		 * Return true if added or false if not added
		 */
		Elite existing = (Elite) get(e.getKey());
		if (existing==null) {//Cell is empty
			insert(e, e.getKey());
			return true;
		}
		else {
			if (e.getFitness() < existing.getFitness()) {
				insert(e, e.getKey());
				return true;
			}
		}
		return false;
	}

	public void putAll(ArrayList<Elite> list) {
		/*
		 * Attempt to put the contents of <list> into the archive
		 */
		for (Elite e : list)
			this.put(e);
	}

	private void insert(Object val,int ... key) {
		/*
		 * Used to insert an object into the tree structure
		 */
		Object[] pointer =  root;
		for (int x =0; x <= key.length-2; x++) {
			int k = key[x];
			if (pointer[k]== null)
				pointer[k] = new Object[buckets];
			pointer = (Object[]) pointer[k];
		}
		int k = key[key.length-1];
		if (pointer[k]==null)
			size++;
		pointer[k] = val;
	}

	public int size() {
		//Return the number of items in the archive
		return size;
	}

	public Elite getRandom() {
		/*
		 * Return a random solution from the archive
		 * 
		 * Selects a random entry from index and then uses that
		 * to identify the random object in the archive
		 * 
		 */
		if (size==0)
			return null;

		Object[] pointer = (Object[]) root;
		for (int x =0; x < dimensions-2; x++) {
			pointer = (Object[]) getRandom(pointer);
		}
		return (Elite)getRandom(pointer);
	}

	private Object getRandom(Object[] array) {
		//Get a random non null element from this array
		int start = rnd.getRnd().nextInt(array.length-1);
		int indx = start;
		Object res = array[indx];

		while(res==null) {
			indx++;
			if (indx==array.length) indx=0;
			res = array[indx];
			if (indx== start) return null;
		}
		return res;
	}

	public ArrayList<Elite> toList() {
		/*
		 * Recursively traverse the tree and add each item in the tree to
		 * an ArrayList wich is returned.
		 */
		items = new ArrayList<Elite>();
		toList(root);
		return items;
	}
    /*
     * Recursive function used in conjection with toList()
     */
	private void toList(Object[] array){
		for (Object o: array) {
			if (o!=null) {
				if (o instanceof Object[]) {
					toList((Object[] )o);
				}else {
					items.add((Elite)o);
				}
			}
		}
	}
}