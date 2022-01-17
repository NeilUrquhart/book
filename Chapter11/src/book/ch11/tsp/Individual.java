package book.ch11.tsp;

import java.util.ArrayList;
import java.util.Random;

import book.ch1.LatLon;
import book.ch1.Visit;


public class Individual extends ArrayList<Visit> {
	private Random rnd = new Random();
	private double len = -1;
	private EnhancedTSP problem;


	public Individual(EnhancedTSP p) {
		problem = p;
	}

	public Individual (Individual other) {
		//Copy constructor
		this.addAll(other);
		this.len = other.len;
		this.problem = other.problem;

	}


	public Individual (Individual p1, Individual p2) {
		//XOver constructor
		this.addAll(p1);
		this.problem =p1.problem;
		Visit[] temp = new Visit[p1.size()];

		int a = rnd.nextInt(this.size()/2);
		int b = a;
		while (b <= a)
			b = rnd.nextInt(this.size()-1);
		
		
		//copy a-b to temp
		for (int count = a; count <b;count ++)
			temp[count] = p1.get(count);
		
		//Now copy the remainder
		
		int p2count=0;
		for(int c=0; c< temp.length;c++) {
			while (temp[c]== null) {
				Visit poss = p2.get(p2count);
				p2count++;
				if (!contains(poss,temp)) {
					temp[c]= poss;
				}
			}
		}
		for(Visit v : temp)
			this.add(v);
	}
	public boolean contains(Visit v, Visit[] list) {
		for (Visit poss : list)
			if (v==poss)
				return true;
		
		return false;
	}
	public void mutate() {

		swap();
	}

	private void  swap() {
		len=-1;
		//Perform a 2-opt swap based on the visits positions a and b
		//Return the modified route in a new arrayLisy
		int a = rnd.nextInt(this.size()-1);
		int b = rnd.nextInt(this.size()-1);
		Visit v = this.remove(a);
		this.add(b,v);
	}

	public double fitness() {
		if (len ==-1) {
			len=0;
			//Dist
			LatLon prev = this.problem.getStart();
			for (LatLon v : this) {
				len = len + prev.getDist(v);
				prev = v;
			}
			len = len + prev.getDist(problem.getStart());
		}
		return len;

	}
}
