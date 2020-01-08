package book.ch3.rep2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import book.ch1.Visit;
import book.ch2.CVRPProblem;
import book.ch2.VRPVisit;

/*
 * Neil Urquhart 2019
 * This class represents a single CVRP solution to be used within an Evolutionary Algorithm.
 * 
 * The basic solution (a grand tour) is stored in the genotype. Once the solution has been
 * evaluated then the solution is stored in the genotype.
 * 
 */
public abstract class EAIndividual   {
	

	//The phenotype is a set of routes created from the genotype
	protected ArrayList<ArrayList<VRPVisit>> phenotype;

	//THe problem being solved
	protected CVRPProblem problem;

	public EAIndividual(){}
	
	public EAIndividual( CVRPProblem prob) {
		/*
		 * Constructor to create a new random genotype
		 */
		problem = prob;
		
	}

	public  EAIndividual (CVRPProblem prob, EAIndividual parent1, EAIndividual parent2){
		/*
		 * Create a new Individual based on the recombination of genes from <parent1> and <parent2>
		 */
		problem = prob;
		
	}


	
	public abstract void mutate();
		//Mutate the genotype, by randomly moving a gene.

	public abstract double evaluate();
		/*
		 * Build a phenotype based upon the genotype
		 * Only build the genotyoe if the phenotype has been set to null
		 * Return the fitness 
		 */
		
	

	public abstract ArrayList getPhenotype();

	public abstract EAIndividual copy() ;
		//Create a new individual that is a direct copy of this individual
}
