package book.ch3.rep2;


public interface Domination {
	public boolean dominates(Domination other);
	public void setRank(int rank);
	public int getRank();
	public double[] getVector();
}