package book.ch4.mapElites;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

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
	
	
	
	public void exportToCSV(String fName) {
		
		try {
		      FileWriter csv = new FileWriter(fName);
		     

		      csv.write("FixedVehCost,CostperDel,StaffCost,VehRunCost,Emissions,DelsByCycle,DistByCycle,Cycles,Vans\n"); 
		      
		      for (Object o : super.store) {
		    	  if (o != null) {
//		    		  EliteIndividual e = (EliteIndividual) o;
//		    		  String key = e.printKey(); 
//		    		  key = key.replace(":", ",");
//		    		  csv.write(key+"\n");
		    		  EliteIndividual e = (EliteIndividual) o;
		    		  //String key = e.printKey(); 
		    		  //csv.write(key+",");
		    		  ModalCostModel m = ModalCostModel.getInstance();
		    		  csv.write(m.getFixedVehCost(e)+",");
		    		  csv.write(e.getCostDel()+",");
		    		  csv.write(m.getStaffCost(e)+",");
		    		  csv.write(m.getVehRunningCost(e)+",");
		    		  csv.write(m.getEmissions(e)+",");
		    		  csv.write(m.getCycleDels(e)+",");
		    		  csv.write(m.getCycleDist(e)+",");
		    		  csv.write(m.getCycles(e)+",");
		    		  csv.write(m.getVans(e)+",");
		    		  
		    		  csv.write("\n");
		    		  
		    	  }
		      }

		      csv.close();
		    } catch (IOException e) {
		      System.out.println("Error writing to ElVis file");
		      e.printStackTrace();
		    }
		
	
	}
	
	public HashSet<String> getKeys(){
		HashSet<String> res = new HashSet<String>();
		
		for (Object o : super.store) {
			if (o != null) {
				EliteIndividual e = (EliteIndividual) o;
				res.add(e.printKey());
			}
		}
		return res;
	}
	
	public String getBest() {//Find the lowest in each feature
		double fixedCost = Double.MAX_VALUE;
		double costDel = Double.MAX_VALUE;
		double staffCost = Double.MAX_VALUE;
		double runningCost = Double.MAX_VALUE;
		double emissions = Double.MAX_VALUE;
		double cycleDels = Double.MAX_VALUE;
		double cycleDist = Double.MAX_VALUE;
		double cycles = Double.MAX_VALUE;
		double vans = Double.MAX_VALUE;
		

		for (Object o : super.store) {
			if (o != null) {
				EliteIndividual e = (EliteIndividual) o;
				ModalCostModel m = ModalCostModel.getInstance();

				fixedCost = update (fixedCost ,m.getFixedVehCost(e));
				costDel = update ( costDel, e.getCostDel());
				staffCost =update ( staffCost, m.getStaffCost(e));
				runningCost = update ( runningCost, m.getVehRunningCost(e));
				emissions = update ( emissions, m.getEmissions(e));
				cycleDels = update ( cycleDels, m.getCycleDels(e));
				cycleDist = update ( cycleDist, m.getCycleDist(e));
				cycles = update ( cycles, m.getCycles(e));
				vans = update ( vans, m.getVans(e));

			}
		}
		return ",fc," + fixedCost 
		+ ",cd," + costDel 
		+ ",sc," + staffCost 
		+ ",rc," + runningCost
		+ ",em," + emissions 
		+ ",cydels," + cycleDels 
		+ ",cydist," + cycleDist 
		+ ",cycles," + cycles 
		+ ",vans," + vans; 
	}

	private double update(double tot, double newest) {
		if (newest < tot)
			tot = newest;
		return tot;
	}
//public void exportToElVis(String fName) {
//		
//		try {
//		      FileWriter elvis = new FileWriter(fName);
//		      elvis.write("Dimensions,"+KeyGenerator.getInstance().getDimensions()+"\n" + 
//		      		"Normalised,"+KeyGenerator.getInstance().getBuckets()+"\n" + 
//		      		"Evals,500000\n");																
//
//
//		      elvis.write("key,dist,FixedVehCost,CostperDel,StaffCost,VehRunCost,Emissions,DelsByCycle,DistByCycle,Cycles,Vans,A,B,C,D,E,F,G,H,I\n"); 
//		      
//		      for (Object o : super.store) {
//		    	  if (o != null) {
//		    		  EliteIndividual e = (EliteIndividual) o;
//		    		  String key = fixKey(e.printKey()); 
//		    		  elvis.write(key+",");
//		    		  elvis.write(e.getDistance()+",");
//		    		  key = key.replace(":", ",");
//		    		  elvis.write(key+",");
//
//		    		  elvis.write(fixZero(ModalCostModel.getInstance().getFixedVehCost(e))+",");
//		    		  elvis.write(fixZero(ModalCostModel.getInstance().getFixedVehCost(e))+",");
//		    		  elvis.write(fixZero(e.getCostDel())+",");
//		    		  elvis.write(fixZero(e.getIdle())+",");
//		    		  elvis.write(fixZero(ModalCostModel.getInstance().getStaffCost(e))+",");
//		    		  elvis.write(fixZero(ModalCostModel.getInstance().getVehRunningCost(e))+",");
//		    		  elvis.write(fixZero(ModalCostModel.getInstance().getEmissions(e))+",");
//		    		  elvis.write(fixZero(ModalCostModel.getInstance().getCycleDels(e))+",");
//		    		  elvis.write(fixZero(ModalCostModel.getInstance().getCycleDist(e))+"\n");
//		    		  
//		    		  
//		    	  }
//		      }
//
//		      elvis.close();
//		    } catch (IOException e) {
//		      System.out.println("Error writing to ElVis file");
//		      e.printStackTrace();
//		    }
//		
//	
//	}
//	
//	private double fixZero(double old) {
//		/*
//		 * ElVis does not accept 0s, so any 0s are modified to 0.000001;
//		 * 
//		 */
//		if (old==0)
//			old =0.000001;
//		return old;
//	}
//	private String fixKey(String key) {
//		/*
//		 * ElVis likes keys with the range 1 to <buckets> rather than 0 to <buckets-1>
//		 * This function fixes the keys so that ElVis will accept them
//		 */
//		
//		String[] data = key.split(":");
//		key = "";
//		for (int i =0; i < data.length-1;i++) {
//			int d = Integer.parseInt(data[i]);
//			d++;
//			key = key + d + ":";
//		}
//		int d = Integer.parseInt(data[data.length-1]);
//		d++;
//		key = key + d;
//		return key;
//		
//	}
}
