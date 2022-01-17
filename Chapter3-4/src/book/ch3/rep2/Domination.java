package book.ch3.rep2;
 /*
  * Copyright Neil Urquhart 2020
  * 
  * This interface is used to define the methods required by a class which reprsents
  * an individual which is using Pareto domination
  * 
  */

public interface Domination {
	public boolean dominates(Domination other);//True if this object dominates the other
	public double[] getVector();//Return the objectives as an array of double
}