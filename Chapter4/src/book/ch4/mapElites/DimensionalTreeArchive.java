package book.ch4.mapElites;
import java.lang.reflect.Array;
import java.util.ArrayList;

import book.ch2.RandomSingleton;

/*
 * A basic data structure to represent a MAP for use within MAP Elites
 * We store Objects in an array that represents the underlying archive
 * Neil Urquhart 2021
 * 
 */
public class DimensionalTreeArchive{
	
	private int buckets;//The number of buckets used per feature
	private int dimensions;//The dimensions of the feature space
	private int used=0;//The number of objects in the archive
	private RandomSingleton rnd = RandomSingleton.getInstance();//Note that we use the RandomSingleton object to generate random numbers
	private Object[] root;
	
	public DimensionalTreeArchive(int _dimsensions, int _buckets) {
		/*
		 * Create a new archive of <_dimensions> dimensions each of which has <_buckets> buckets.
		 * 
		 */
		dimensions = _dimsensions;
		buckets = _buckets;
	
		root= new Object [buckets];
	}
	
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
	
//	public T getByIndex(int idx){
//		//Access the archive by index
//		return (T) archive[idx];
//	}
//	
//	public  void put (T data, int ... key) throws InvalidMAPKeyException{
//		/* 
//		 * Store an item in the archive based on the keyIndexes. 
//		 * NOTE: any item currently stored in the bucket will be ovewritten
//		 * 
//		 * Throws an InvalidMAPKeyException if the key supplied does not map to an element
//		 * within the archive
//		 */
//		int idx =getIndex(key);
//		
//		if (archive[idx]==null)
//			used++;
//		
//		archive[idx] = data;
//		index.add(idx);
//	}
//	
//	
//	public T get(int ... key) throws InvalidMAPKeyException{
//		/*
//		 * Return the object in the archive that matches the key
//		 * Returns null if that element is empty
//		 * 
//		 * Throws an InvalidMAPKeyException if the key supplied does not map to an element
//		 * within the archive
//		
//		 */
//		return (T)archive[getIndex(key)];
//	}
	
//	private int getIndex(int ... key) throws InvalidMAPKeyException{
//		/*
//		 * Converts a MAP Elites Key into a subscript for the archive array.
//		 * The key is an array of int, the length of the key must match the 
//		 * dimensions of the MAP
//		 * 
//		 */
//		
//		if (key.length != dimensions) {
//			throwErr(key);
//		}
//		int res=0;
//		
//		for (int i=0;i < key.length; i++) {
//			if ((key[i] <0 )||(key[i]>=buckets))
//				throwErr(key);
//			res = (int) (res + (key[i]*Math.pow((double)buckets,(double)i))); 	 
//		}
//		return res;
//	}
	
//	private void throwErr(int ... key) throws InvalidMAPKeyException {
//		/*
//		 * Generate an InvalidMAPKeyException, with an appropriate error message
//		 * 
//		 */
//		
//		String strkey = "";
//		for (int i : key)
//			strkey = strkey + " " +i;
//		throw new InvalidMAPKeyException("Key: " + strkey);
//	}
	
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
}
