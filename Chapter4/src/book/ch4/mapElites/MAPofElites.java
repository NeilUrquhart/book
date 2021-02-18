package book.ch4.mapElites;

import java.util.ArrayList;
import java.util.HashSet;

import book.ch2.RandomSingleton;


public class MAPofElites extends  DimensionalTreeArchive{// DimensionalArchive<Elite> {

	private RandomSingleton rnd = RandomSingleton.getInstance();
	//Note that we use the RandomSingleton object to generate random numbers

	public MAPofElites(int _dimensions, int _buckets) {
		super(/*Elite.class ,*/_dimensions, _buckets);
	}

	public void addAll(ArrayList<Object> list) throws InvalidMAPKeyException{
		for (Object o : list)
			this.put((Elite)o);
	}
	
	public boolean put(Elite e) throws InvalidMAPKeyException{

		Elite existing = (Elite) super.get(e.getKey());
		if (existing==null) {
			super.put(e, e.getKey());
			return true;
		}
		else {
			if (e.getFitness() < existing.getFitness()) {

				super.put(e, e.getKey());
				return true;
			}
		}
		return false;
	}

//	public ArrayList<Elite> getArchive(){
//		//Return a list of the objects contained in the archive
//		ArrayList<Elite> result = new ArrayList<Elite>();
//		for (Object o : super.archive) {
//			if (o != null) {
//				result.add((Elite) o);
//			}
//		}
//		return result;
//	}

//	public HashSet<String> getKeys(){
//		//Return a list of all of the keys contained within the archive
//		HashSet<String> res = new HashSet<String>();
//		for (Object o : super.archive) {
//			if (o != null) {
//				EliteIndividual e = (EliteIndividual) o;
//				res.add(e.keyToString());
//			}
//		}
//		return res;
//	}
//
	public ArrayList<Object> getArchive(){
		//Return a list of the objects contained in the archive
		return super.printOut();
		
	}

	public HashSet<String> getKeys(){
		//Return a list of all of the keys contained within the archive
		HashSet<String> res = new HashSet<String>();
		ArrayList<Object> list =super.printOut();
		for (Object o : list) {
				EliteIndividual e = (EliteIndividual) o;
				res.add(e.keyToString());
			}
		
		return res;
	}
}
