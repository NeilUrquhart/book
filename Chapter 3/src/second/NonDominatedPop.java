package second;

import java.util.ArrayList;



public class NonDominatedPop extends ArrayList<Domination> {

	public String getStats(){
		String res = "";
		res = "n," + this.size();
		int size = this.get(0).getVector().length;
		double[] max = new double[size];
		double[] min = new double[size];
		
		for (int c=0; c < size; c++){
			max[c] = -1;
			min[c] = Double.MAX_VALUE;
		}
			
		
		for(Domination d : this){
			double[] v = d.getVector();
			for (int pos = 0; pos < size; pos++){
				if (v[pos] < min[pos])
					min[pos] = v[pos];
				if (v[pos] > max[pos])
					max[pos] = v[pos];
			}
		}
		
		for (int pos = 0 ; pos < size; pos++){
			res = res + ",obj"+pos +"Max," + max[pos] + ",obj"+pos +"Min,"+ min[pos];
		}
		return res;
	}
	
	public NonDominatedPop extractNonDom(){
		/*
		 * Remove all of the non dominated members
		 */
		
		NonDominatedPop result = new NonDominatedPop();
		
		
		
		for (Domination current : this){
			boolean dominated =  false;
			for (Domination member : this){
			  if (member.dominates(current))
				  dominated = true;
			  }
			
			if (! dominated)
				result.add(current);
			}
		return result;
		}
		
	public NonDominatedPop removeNonDom(){
		NonDominatedPop nd = this.extractNonDom();
		for (Object b: nd){
			this.remove(b);
		}
		return nd;
	}

	public void rank(){
		NonDominatedPop  temp = new NonDominatedPop();
		temp.addAll(this);
		//this.clear();
		int cRank =0;
		while (temp.size() >0){
			NonDominatedPop rank = temp.removeNonDom();
			for (Domination d : rank){
				d.setRank(cRank);
			}
			cRank++;
		}
	}
}