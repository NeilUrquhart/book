package book.ch4.mapElites;

import java.util.ArrayList;
import java.util.HashSet;

import book.ch2.RandomSingleton;


public class MAPofElites {

	private RandomSingleton rnd = RandomSingleton.getInstance();
	//Note that we use the RandomSingleton object to generate random numbers

	private int buckets;//The number of buckets used per feature
	private int dimensions;//The dimensions of the feature space
	private int used=0;//The number of objects in the archive
	private Object[] root;
	

	public  Object get(int ... key) {
		Object[] pointer = root;
		for (int x =0; x < key.length-2; x++) {
			int k = key[x];
			if (pointer[k]== null)
				return null;
			pointer = (Object[]) pointer[k];
		}
		int k = key[key.length-1];
		return pointer[k];
	}
	
	public void put(Object val,int ... key) {
		Object[] pointer =  root;
		for (int x =0; x < key.length-2; x++) {
			int k = key[x];
			if (pointer[k]== null)
				pointer[k] = new Object[buckets];
			pointer = (Object[]) pointer[k];
		}
		int k = key[key.length-1];
		if (pointer[k]==null)
			used++;
		pointer[k] = val;
		
	}
	
	public int getUsed() {
		//Return the number of items in the archive
		return used;
	}
	
	private ArrayList<Object> items;
	
	public ArrayList<Object> printOut() {
		items = new ArrayList<Object>();
		printOut(root);
		return items;
	}
	
	
	public void printOut(Object[] array){
		for (Object o: array) {
			if (o!=null) {
				if (o instanceof Object[]) {
					printOut((Object[] )o);
				}else {
				items.add(o);
				}
			}
		}
	}
	
	public int getCapacity() {
		/*
		 * Return the storage capacity of the archive
		 */
		return (int) Math.pow(buckets,dimensions);//Calculate size of the archive
	}
	
	public Object getRandom() {
		/*
		 * Return a random object from the archive
		 * 
		 * Selects a random entry from index and then uses that
		 * to identify the random object in the archive
		 * 
		 */
		if (this.getUsed()==0)
			return null;
		
		
			Object[] pointer = (Object[]) root;
			for (int x =0; x < dimensions-2; x++) {
				pointer = (Object[]) getRandom(pointer);
			}
			return getRandom(pointer);
		
		
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
	
	public MAPofElites(int _dimensions, int _buckets) {
		
		dimensions = _dimensions;
		buckets = _buckets;
	
		root= new Object [buckets];
	}

	public void addAll(ArrayList<Object> list) {
		for (Object o : list)
			this.put((Elite)o);
	}
	
	public boolean put(Elite e) {

		Elite existing = (Elite) get(e.getKey());
		if (existing==null) {
			put(e, e.getKey());
			return true;
		}
		else {
			if (e.getFitness() < existing.getFitness()) {

				put(e, e.getKey());
				return true;
			}
		}
		return false;
	}

	public ArrayList<Object> getArchive(){
		//Return a list of the objects contained in the archive
		return printOut();
		
	}

	public HashSet<String> getKeys(){
		//Return a list of all of the keys contained within the archive
		HashSet<String> res = new HashSet<String>();
		ArrayList<Object> list =printOut();
		for (Object o : list) {
				EliteIndividual e = (EliteIndividual) o;
				res.add(e.keyToString());
			}
		
		return res;
	}
}
