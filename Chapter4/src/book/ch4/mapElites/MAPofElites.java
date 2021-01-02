package book.ch4.mapElites;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import book.ch2.RandomSingleton;
import book.ch4.FabFoodCostModel;

public class MAPofElites extends MAP<Elite> {

	private RandomSingleton rnd = RandomSingleton.getInstance();
	//Note that we use the RandomSingleton object to generate random numbers
	
	
	public MAPofElites(int _dimensions, int _buckets) {
		super(_dimensions, _buckets);
	}

	public boolean put(Elite e) throws InvalidMAPKeyException{
		
		Elite existing = super.get(e.getKey());
		
		if (existing==null) {
			super.put(e, e.getKey());
			return true;
			}
		
		else {
			if (e.getFitness() < existing.getFitness()) {
				
				super.put(e, e.getKey());
				return true;
			}
		}
		return false;
	}
	
	public Elite getRandom() {
		Elite res = null;
		
		if (this.getUsed()==0)
			return res;
			
		int i = rnd.getRnd().nextInt(this.getCapacity());
		res = this.getByIndex(i);
		while (res== null) {
			i++;
			if (i== this.store.length)
				i=0;
			res = this.getByIndex(i);
		}
		return res;
	}
	
	public void exportToElVis(String fName) {
		
		try {
		      FileWriter elvis = new FileWriter(fName);
		      elvis.write("Dimensions,"+KeyGenerator.getInstance().getDimensions()+"\n" + 
		      		"Normalised,"+KeyGenerator.getInstance().getBuckets()+"\n" + 
		      		"Evals,500000\n");																
		      		
		      elvis.write("key,dist,FixedVehCost,Cost,CostperDel,VehicleCost,Idle,StaffCost,VehRunCost,actual emissions,actual byMd,Actual MDs in use,Actual Time,Actual WalkDels,Actual CycleDels,Actual EvanDels\n"); 
		      
		      for (Object o : super.store) {
		    	  if (o != null) {
		    		  EliteIndividual e = (EliteIndividual) o;
		    		  String key = fixKey(e.printKey()); 
		    		  elvis.write(key+",");
		    		  elvis.write(e.getDistance()+",");
		    		  key = key.replace(":", ",");
		    		  elvis.write(key+",");

		    		  elvis.write(fixZero(FabFoodCostModel.getInstance().getFixedVehCost(e))+",");
		    		  elvis.write(fixZero(e.getCost())+",");
		    		  elvis.write(fixZero(e.getCostDel())+",");
		    		  elvis.write(fixZero(FabFoodCostModel.getInstance().getVehicleCost(e))+",");
		    		  elvis.write(fixZero(e.getIdle())+",");
		    		  elvis.write(fixZero(FabFoodCostModel.getInstance().getStaffCost(e))+",");
		    		  elvis.write(fixZero(FabFoodCostModel.getInstance().getVehRunningCost(e))+"\n");
		    	  }
		      }

		      elvis.close();
		    } catch (IOException e) {
		      System.out.println("Error writing to ElVis file");
		      e.printStackTrace();
		    }
		
	
	}
	
	private double fixZero(double old) {
		/*
		 * ElVis does not accept 0s, so any 0s are modified to 0.000001;
		 * 
		 */
		if (old==0)
			old =0.000001;
		return old;
	}
	private String fixKey(String key) {
		/*
		 * ElVis likes keys with the range 1 to <buckets> rather than 0 to <buckets-1>
		 * This function fixes the keys so that ElVis will accept them
		 */
		
		String[] data = key.split(":");
		key = "";
		for (int i =0; i < data.length-1;i++) {
			int d = Integer.parseInt(data[i]);
			d++;
			key = key + d + ":";
		}
		int d = Integer.parseInt(data[data.length-1]);
		d++;
		key = key + d;
		return key;
		
	}
}
