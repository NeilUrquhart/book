package book.ch4.mapElites;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import book.ch2.RandomSingleton;
import book.ch4.FabFoodCostModel;

public class MAPofElites extends MAP<Elite> {

	private RandomSingleton rnd = RandomSingleton.getInstance();
	//Note that we use the RandomSingleton object to generate random numbers

	public MAPofElites(int _dimensions, int _buckets) {
		super(_dimensions, _buckets);
	}

	public boolean put(Elite e) throws InvalidMAPKeyException{

		Elite existing = super.get(e.getKey());
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

	public ArrayList<Elite> getArchive(){
		//Return a list of the objects contained in the archive
		ArrayList<Elite> result = new ArrayList<Elite>();
		for (Object o : super.archive) {
			if (o != null) {
				result.add((Elite) o);
			}
		}
		return result;
	}

	public HashSet<String> getKeys(){
		//Return a list of all of the keys contained within the archive
		HashSet<String> res = new HashSet<String>();
		for (Object o : super.archive) {
			if (o != null) {
				EliteIndividual e = (EliteIndividual) o;
				res.add(e.keyToString());
			}
		}
		return res;
	}

}
