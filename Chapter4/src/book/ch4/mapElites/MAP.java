package book.ch4.mapElites;

import java.util.ArrayList;
import java.util.HashSet;

import book.ch2.RandomSingleton;

public class MAP <T>{
	
	protected Object[] store;
	private int buckets;
	private int dimensions;
	private int used=0;
	private RandomSingleton rnd = RandomSingleton.getInstance();
	//Note that we use the RandomSingleton object to generate random numbers
	int  temp;
	
	private ArrayList<Integer> index = new ArrayList<Integer>();

	
	public int getUsed() {
		return used;
	}
	
	public T getByIndex(int idx){
		return (T) store[idx];
	}
	public  void put (T data, int ... indexes) throws InvalidMAPKeyException{
		int idx =getIndex(indexes);
		
		if (store[idx]==null)
			used++;
		
		store[idx] = data;
		index.add(idx);
		temp = idx;
	}
	
	
	public T get(int ... indexes) throws InvalidMAPKeyException{
		return (T)store[getIndex(indexes)];
	}
	
	private int getIndex(int ... indexes) throws InvalidMAPKeyException{
		
		if (indexes.length != dimensions) {
			throwErr(indexes);
		}
		int res=0;
		
		for (int i=0;i < indexes.length; i++) {
			if ((indexes[i] <0 )||(indexes[i]>=buckets))
				throwErr(indexes);
			res = (int) (res + (indexes[i]*Math.pow((double)buckets,(double)i))); 	 
		}
		return res;
	}
	
	
	private void throwErr(int ... indexes) throws InvalidMAPKeyException {
		String key = "";
		for (int i : indexes)
			key = key + " " +i;
		throw new InvalidMAPKeyException("Key: " + key);
	}
	
	public MAP(int _dimsensions, int _buckets) {
		dimensions = _dimsensions;
		buckets = _buckets;
		int len =(int) Math.pow(buckets,dimensions);
		store = new Object[len];
	}
	
	public int getCapacity() {
		return store.length;
	}
	
	public T getRandom() {
		
		if (this.getUsed()==0)
			return null;
		
		int i = (int)index.get(rnd.getRnd().nextInt(index.size()-1));
		return (T)store[i];	
	}
	
}
