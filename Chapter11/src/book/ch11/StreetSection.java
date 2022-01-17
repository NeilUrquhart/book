package book.ch11;

import java.util.ArrayList;

import book.ch1.LatLon;

/*
 * Neil Urquhart 2021
 * 
 * This class Represents a single SBR street sectiom
 */

public class StreetSection {
	String name;        //EG High Street, Edinburgh 
	private LatLon j1;  //If the street is singled ended (e.g. a cul-de-sac) j1 and j2 
	private LatLon j2;  // will be the same
	
	/*
	 * Caches that hold references to houses in the appropriate order
	 * We always assume that the lowest numberd houses are located at the j1
	 * end of the street and the highest at j2
	 */
	
	//The houses in "crossover order" from j1 to j2
	private ArrayList<House> bothsides = new ArrayList<House>();
	
	//The houses in single-sided order from J1 to J2 for each side
	private ArrayList<House> side1 = new ArrayList<House>();
	private ArrayList<House> side2 = new ArrayList<House>();
	
    //Delivery distances pre-computed for each delivery pattern
	double oddDist;
	double evenDist;
	double allDist;

	private boolean side=false;//Flag to show if one side has been delivered already
	
	public StreetSection(String name, int[] side1Houses , int[] Side2Houses, LatLon j1, LatLon j2) {
		this.name = name;
		this.j1 = j1;
		this.j2 = j2;
		
		//Add houses to each side and sort into order from j1
		for (int odd : side1Houses)
			side1.add(new House(odd,this));
		side1 = StreetUtils.nnSort(j1,side1);
		
		for (int even : Side2Houses)	
			side2.add(new House(even,this));
		side2 = StreetUtils.nnSort(j1,side2);
		
		//Add houses for both sides and sort into order from j1
		bothsides.addAll(side1);
		bothsides.addAll(side2);
		bothsides = StreetUtils.nnSort(j1,bothsides);	
	}

	public String getName() {
		return name;
	}
	public LatLon getJ1() {
		return j1;
	}
	public LatLon getJ2() {
		return j2;
	}
	public ArrayList<House> getOdds(){
		return side1;
	}
	public ArrayList<House> getEvens(){
		return side2;
	}
	public ArrayList<House> getAll(){
		return bothsides;
	}

	public boolean linked(StreetSection other) {
		//Return True if this street has a common junction wity <other>
		return((this.getJ1().same(other.getJ1())) || (this.getJ2().same(other.getJ2()))  ||
				(this.getJ1().same(other.getJ2()))  ||  (this.getJ2().same(other.getJ1()))) ;
	}
	
	public LatLon[] applyPattern(LatLon startend, boolean bothSides, StreetSection prev, StreetSection next, ArrayList<House> res) {
	/*
	 * Apply the appropriate SBR pattern to street, based on <prev>, <next> and both sides
	 * The deliveries are appended to the <res> collection
	 * The returned array contains the two street junctions in the order start[0] and end[1]
	 */
		LatLon startJ; 
		LatLon endJ;
		LatLon[] jucts = new LatLon[2];

		if (next == null)
			endJ = startend;
		else
			endJ= getClosest(next);

		if (prev == null)
			startJ = getClosest(startend);
		else
			startJ= getClosest(prev);

		if (bothSides) {
			if (startJ != endJ)
				if (startJ==j1) {
					res.addAll(bothsides);
				}
				else {
					res.addAll(StreetUtils.reverse(bothsides));
				}
			else {
				if (startJ==j1) {
					res.addAll(side1);
					res.addAll(StreetUtils.reverse(side2));
				}else {
					res.addAll(StreetUtils.reverse(side1));
					res.addAll(side2);
				}
			}
		}else {//Oneside
			if((side1.size() ==0)||(side2.size()==0) ){
				if (startJ==j1) {
					res.addAll(bothsides);
				}
				else {
					res.addAll(StreetUtils.reverse(bothsides));
				}
			}
			else if (side) 
				if (startJ==j1) {
					res.addAll(side1);
				}
				else {
					res.addAll(StreetUtils.reverse(side1));
				}
			else 
				if (startJ==j1) {
					res.addAll(side2);
				}
				else {
					res.addAll(StreetUtils.reverse(side2));	
				}
			side = !side;
		}
		jucts[0] = startJ;
		if (bothSides)
			jucts[1] = endJ;
		else
			jucts[1] = opposite(jucts[0]);
		return jucts;
	}

	private LatLon opposite(LatLon j) {
		//Return the opposite junction to <j>
		if (j == j1)
			return j2;
		else
			return j1;
	}
	
	private LatLon getClosest(LatLon otherJ) {
		//Return the closest junction of the current street to <otherj>
		double d = j1.getDist(otherJ);
		if (j2.getDist(otherJ)>d)
			return j1;
		else
			return j2;
	}

	private LatLon getClosest(StreetSection other) {
		//Return the junction that is closest to <other>
		double d1 = j1.getDist(other.getJ1());
		double d2 = j1.getDist(other.getJ2());
		double d3 = j2.getDist(other.getJ1());
		double d4 = j2.getDist(other.getJ2());

		double min = Math.min(Math.min(d1, d2), Math.min(d3, d4));

		if (min==d1) return j1;
		if (min==d2) return j1;
		return j2;
	}

	public boolean twoSides() {
		//Return True if this street has deliveries on both sides
		return (this.side2.size() >0) &&(this.side1.size()>0);
	}
}