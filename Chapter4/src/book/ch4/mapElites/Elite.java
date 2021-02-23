package book.ch4.mapElites;

/*
 * Neil Urquhart 2021
 * 
 * Elite is used to define the interface required for solutions that are to be stored in the MAPofElite
 * 
 * getFitness() must return a double value that represents the fitness of the solution (lower == better)
 * getKey() must return an array of int which represents the "key" of the individual (ie the bucket location
 * getSummary must return a string that summerises the invidual (for the log file)
 */

public interface Elite {
	public double getFitness();
	public int[] getKey();
	public String getSummary();
	
	public default String keyToString() {
		/*
		 * Return the key as a string 
		 */
		String res = "";
		for (int i=0; i < getKey().length-1; i++)
			res = res + getKey()[i] +":";
		
		res = res + getKey()[getKey().length-1];
		return res;
	}
	

}
