package book.ch12.mapElites;

import book.ch12.problem.Results;

/*
 * Neil Urquhart 2021
 * 
 * This class represents a single solution
 * as held in the archive
 * 
 */
public class Solution {
	
	private Results myResults;
	private Chromosome myChromo;

	public Solution(Results r, Chromosome c){
		myResults=r;
		myChromo =c;
	}
	
	public Results getMyResults() {
		return myResults;
	}

	public void setMyResults(Results myResults) {
		this.myResults = myResults;
	}

	public Chromosome getMyChromo() {
		return myChromo;
	}

	public void setMyChromo(Chromosome myChromo) {
		this.myChromo = myChromo;
	}

	public String toString() {
		return myResults.getEmissions() +","+myResults.getMicroDepots()+","+myResults.getByMD()+","+myResults.getBikes()+","+myResults.getWalkers()+","+myResults.getEVans()+","+myResults.getMaxTime();
	}
}
