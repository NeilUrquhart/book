package book.ch4.mapElites;

public interface Elite {
	public double getFitness();
	public int[] getKey();

	public default String printKey() {
		String res = "";
		for (int i=0; i < getKey().length-1; i++)
			res = res + getKey()[i] +":";
		
		res = res + getKey()[getKey().length-1];
		return res;
	}
}
