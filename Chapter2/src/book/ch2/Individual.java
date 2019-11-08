package book.ch2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import book.ch1.Visit;

public class Individual   {
	private RandomSingleton rnd =RandomSingleton.getInstance();
	private ArrayList<VRPVisit> genotype;
	private ArrayList<ArrayList<VRPVisit>> phenotype;
	private VRPProblem problem;
	
	public Individual (VRPProblem prob, Individual parent1, Individual parent2){
		problem = prob;
		genotype = new ArrayList<VRPVisit>();
		int xPoint = rnd.getRnd().nextInt(parent1.genotype.size());
		//copy all of p1 to the xover point
		for (int count =0; count < xPoint; count++ ){
			genotype.add(parent1.genotype.get(count));
		}
		//Now add missing genes from p2
		for (int count =0; count < parent2.genotype.size(); count++){
			VRPVisit v = parent2.genotype.get(count);
			if (!genotype.contains(v)){
				genotype.add(v);
			}
			
		}

	}
	
	public Individual( VRPProblem prob) {
	   problem = prob;
	    genotype = new ArrayList<VRPVisit>();
		for (Visit v : prob.getSolution()){
			genotype.add((VRPVisit)v);
		}
		genotype = shuffle(genotype);
		phenotype = null;
	}

	private ArrayList shuffle(ArrayList list) {
		System.out.println(list.size());
		Random  r= rnd.getInstance().getRnd();
		
		for (int c=0; c < list.size()*list.size();c++) {
			Object o = list.remove(r.nextInt(list.size()));
			list.add(r.nextInt(list.size()),o);
		}
		System.out.println(list.size());
		
		return list;
	}
	public void mutate() {
		
		phenotype = null;
		int rndGene = rnd.getRnd().nextInt(genotype.size());
		VRPVisit v = genotype.remove(rndGene);
		int addPoint = rnd.getRnd().nextInt(genotype.size());
		genotype.add(addPoint,v);
	}
	
	public double evaluate() {
		if (phenotype == null) {
			phenotype = new ArrayList<ArrayList<VRPVisit>> ();
			ArrayList<VRPVisit> route = new ArrayList<VRPVisit>();
			for (VRPVisit v : genotype){
				if (v.getDemand() + routeDemand(route) > problem.getCapacity()){
					phenotype.add(route);
					route = new ArrayList<VRPVisit>();
				}
				route.add(v);
			}
			phenotype.add(route);
		}
			
		return problem.getDistance(phenotype);
	}
	
	public ArrayList<ArrayList<VRPVisit>> getPhenotype(){
		return phenotype;
	}
	
	public double getDistance(){
		if (phenotype == null)
			evaluate();
		return problem.getDistance(phenotype);
	}
	
	public int getVehicles() {
		if (phenotype == null)
			evaluate();
		return phenotype.size();
	}
	
	private int routeDemand(ArrayList<VRPVisit> route){
		int demand=0;
		for (VRPVisit visit: route){
			demand += visit.getDemand();
		}
		return demand;
	}
	
	public Individual copy() {
		Individual copy = new Individual(this.problem);
		copy.genotype = (ArrayList<VRPVisit>) this.genotype.clone();
		return copy;
	}
	
	public void check() {
		int targetCusts = this.problem.getCustomers().size();
		
		if (targetCusts != this.genotype.size()) {
			System.out.println("Genotype size error");
		}
		
		int phenocount=0;
		if (phenotype != null) {
			for (ArrayList route: phenotype) {
				phenocount = phenocount + route.size();
			}
			if (targetCusts != phenocount) {
				System.out.println("Phenotype size error");
			}
		}
		
	}
}
