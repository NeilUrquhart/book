import book.ch2.CVRPProblem;
import book.ch2.Individual;

public class BiObjectiveIndividual extends Individual {

	public BiObjectiveIndividual(CVRPProblem prob) {
		super(prob);
	}
	
	//get Vehicles
	public int getVehicles() {
		return super.getVehicles();
	}
	
	//Get Customer Service
	public double getCustService() {
		if (super.phenotype == null)
			//If the genotype has been changed then evaluate
			evaluate();
		
		return 0;
	}
}
