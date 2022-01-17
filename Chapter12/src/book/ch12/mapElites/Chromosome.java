package book.ch12.mapElites;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import book.ch12.problem.MDProblem;

/*
 * Neil Urquhart 2021
 * The chromosome contains a list of Gene objects
 * Note the use of the recombination constructor and the mutation operator
 * 
 *
 */
public class Chromosome extends ArrayList<Gene> implements Cloneable,Serializable{
	private Random rnd = new Random();

	public Chromosome(int len){
		// Constructor - create a new Chromosome of <len> random genes 
		super();	
		if(len > MDProblem.getInstance().getMAX_VEHICLES()){
			len = MDProblem.getInstance().getMAX_VEHICLES();
		}

		for (int c=0;c< len; c++){
			super.add(new Gene());
		}
	}

	public Chromosome(Chromosome p1, Chromosome p2){
		// Constructor - create a new chromosome based on p1 and p2
		for (int x=0; x < 10; x++) {
			if (rnd.nextBoolean()){
				if (x < p1.size()) {
					Gene g = p1.get(x);
					this.add((Gene)g.clone());
					if (this.size()>MDProblem.getInstance().getMAX_VEHICLES())
						return;
				}
			}else {
				if (x < p2.size()) {
					Gene g = p2.get(x);
					this.add((Gene)g.clone());
					if (this.size()>MDProblem.getInstance().getMAX_VEHICLES())
						return;
				}	
			}
		}
	}

	public void randomise() {
		//Shuffle the genes within the chromosome
		Collections.shuffle(this);
	}

	public void mutate(){
		//Make a random change to this chromosoe
		if (super.size()==0){//Can only add new gene
			super.add(new Gene());
			return;
		}
		double ch = rnd.nextInt(4);
		if (ch ==0){// mutate a random gene
			Gene g = super.get(rnd.nextInt(super.size()));
			g.mutateGene();

		}else if (ch==1){//Change the gene order
			if (super.size()==1) return;
			Gene g = super.get(rnd.nextInt(super.size()));
			super.remove(g);
			int insert = rnd.nextInt(super.size());
			super.add(insert,g);
		}
		else if (ch==2){//Add new
			if (super.size()<MDProblem.getInstance().getMAX_VEHICLES()){
				Gene g = new Gene();
				super.add(g);
			}
		}
		else{//Remove random gene
			if (super.size() >1){
				Gene g = super.get(rnd.nextInt(super.size()));
				super.remove(g);
			}
		}
	}
	
	public Object clone() {
		Chromosome res = (Chromosome) super.clone();
		return res;
	}
}
