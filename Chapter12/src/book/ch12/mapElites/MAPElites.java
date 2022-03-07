package book.ch12.mapElites;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import book.ch12.problem.Results;
import book.ch12.problem.MDProblem;
import book.ch12.problem.Courier;
import book.ch12.utils.ArchiveWriter;

public class MAPElites {
	/*
	 * Neil Urquhart 2021
	 * 
	 * An implementation of the MAP-Elites illumination algorithm
	 * for the microdepot problem.
	 * 
	 * Command line params:
	 * <scenario file> <init evals> <timeout> <outfile>
	 * 
	 * e.g.
	 * edinburghTest 500 1000 out.txt
	 * 
	 * You will also need to download a copy of scotland-lates.osm.pbf to place within the osm folder
	 * 
	 */

	

	private static Random rnd = new Random();


	private static int INIT_EVALS; //Length of the initialisation phase 
	private static int TIME_OUT;  //Number of evals to run for
	//private static String outName = "";
	private static int evals_sinceCH=0;
	private static int totEvals=0;

	public static void main(String[] args){
		String scenarioFile = args[0];
		INIT_EVALS=Integer.parseInt(args[1]);
		TIME_OUT= Integer.parseInt(args[2]);
		String outName = args[3];
		HashMap<String,Solution> overallMap = new HashMap<String,Solution>();

		for (int c=9; c <= 10; c++) {
			totEvals =0;
			HashMap<String,Solution> currentMap = run(new File(scenarioFile+c+".csv"), outName+c+".csv");
			merge(overallMap,currentMap);
		}
		
		writeRes(overallMap,999999999,"final");
		ArchiveWriter.createZip("final",overallMap);
	}
	
	private static void merge(HashMap<String,Solution>  to, HashMap<String,Solution>  from) {
		for (String key : from.keySet()) {
			if (!to.containsKey(key)) {
				to.put(key, from.get(key));
			}else {
				double fromFit = from.get(key).getMyResults().getDistance();
				double toFit = to.get(key).getMyResults().getDistance();
				
				if (fromFit < toFit) {
					to.remove(key);
					to.put(key, from.get(key));
				}
			}
		}
	}
	
	private static HashMap<String,Solution>  run(File scenarioFile, String outName) {
		HashMap<String,Solution> map = new HashMap<String,Solution>();
		MDProblem  prob = MDProblem.getInstance();
		prob.setup(scenarioFile);
		KeyGen.initialise();

		//Initilisation phase
		initialise(prob,map);

		writeRes(map,0,outName);
		//Run MAP-Elites
		System.out.println("Solutions -" +map.size());
		evals_sinceCH=0;
		while( totEvals < TIME_OUT) {
			totEvals++;
			if ((totEvals%10000)==0)
				writeRes(map,totEvals,outName);
			
			Chromosome ch =null;
			if (rnd.nextFloat()>0.7) 
				ch = new Chromosome(randomEntry(map).getMyChromo(),randomEntry(map).getMyChromo());//Recombination	
			else 
				ch = (Chromosome) randomEntry(map).getMyChromo().clone();
				
			Results res= prob.evaluate(ch,true,"");
			evals_sinceCH++;
			boolean done =false;
			int tries = 0;
			while(!done) {

				String key = KeyGen.getKey(res);
				if (addToMap(map,key,new Solution(res,ch))) {
					evals_sinceCH=0;
					done= true;
				}
				tries++;
				if (tries == 10)
					done=true;
				ch.mutate();
			}
			
			if((totEvals%1000)==0)
				System.out.println("Total" +totEvals+ " LastCh " +evals_sinceCH +" Arch " + map.size());
		} 
		writeRes(map,totEvals,"FINAL-"+outName);
		ArchiveWriter.createZip(outName,map);
		return map;
	}

	private static void initialise(MDProblem  prob ,HashMap<String,Solution> map ) {
		createBase(map);
		for (int c=0; c< INIT_EVALS;c++ ){
			System.out.println("Init: " +c +" :" + map.size());
			Chromosome ch = new Chromosome(rnd.nextInt(MDProblem.getInstance().getMAX_VEHICLES()));
			Results res= prob.evaluate(ch,true,"");
			int mutates=0;
			while(mutates<10){
				addToMap(map,KeyGen.getKey(res),new Solution(res,ch));
				ch.mutate();
				mutates++;
				res= prob.evaluate(ch,true,"");
			}
		}
	}


	private static void createBase(HashMap<String,Solution> map ) {
		Chromosome ch = new Chromosome(0);
		Results res = MDProblem.getInstance().evaluate(ch,true,"");
		String key = KeyGen.getKey(res);
		addToMap(map,key,new Solution(res,ch));
		System.out.println("Base: " +evals_sinceCH +" : " + map.size());
	}

	public static Solution randomEntry(HashMap<String,Solution> map ) {
		ArrayList<String>temp = new ArrayList<String>(map.keySet());
		String k = temp.get(rnd.nextInt(temp.size()));
		Solution res =  map.get(k);
		return res;
	}

	private static boolean addToMap(HashMap<String,Solution> map,String key, Solution mr){
		if (map.containsKey(key)){
			Solution current = map.get(key);
			if (mr.getMyResults().getTotalCost()<current.getMyResults().getTotalCost()){
				map.remove(key);
				map.put(key, mr);
				return true;
			}else{
				return false;
			}
		}else{
			map.put(key, mr);
			return true;
		}
	}

	private static void writeRes(HashMap<String,Solution> map,int evals,String outName){	
		File file = new File("./"+ outName+ "-"+evals+".csv");
		try{
			DecimalFormat df = new DecimalFormat("#.00");
			PrintWriter pw = new PrintWriter(new FileOutputStream(file, false));
			pw.write("Dimensions,7\nNormalised,10\nevals,"+evals+"\nkey,dist,emissions,MicroDepots,byMD,Bikes,Walkers,EVs,time,ActualEmissions,ActualMD,ActualByMD,ActualBikes,ActualWalkers,ActualEVs,ActualTime,sol");
			pw.write("\n");
			for (String key : map.keySet()){
				Solution me = map.get(key);
				String buffer = key + ","+ df.format(me.getMyResults().getTotalCost()) +","+key.replace(':', ',') ;
				//Bug - ElVis dislikes 0s so put 0.00001
				if (me.getMyResults().getEmissions()>0)
					buffer = buffer + ","+ df.format(me.getMyResults().getEmissions());
				else
					buffer = buffer + ",0.0001";

				if (me.getMyResults().getMicroDepots()>0)
					buffer = buffer + ","+ df.format(me.getMyResults().getMicroDepots());
				else
					buffer = buffer + ",0.00001";

				if (me.getMyResults().getByMD()>0)
					buffer = buffer + ","+ df.format(me.getMyResults().getByMD());
				else
					buffer = buffer + ",0.00001";

				if (me.getMyResults().getBikes()>0)
					buffer = buffer + ","+ df.format(me.getMyResults().getBikes());
				else
					buffer = buffer + ",0.00001";

				if (me.getMyResults().getWalkers()>0)
					buffer = buffer + ","+ df.format(me.getMyResults().getWalkers());
				else
					buffer = buffer + ",0.00001";

				if (me.getMyResults().getEVans()>0)
					buffer = buffer + ","+ df.format(me.getMyResults().getEVans());
				else
					buffer = buffer + ",0.00001";

				if (me.getMyResults().getMaxTime()>0)
					buffer = buffer + ","+ df.format(me.getMyResults().getMaxTime());
				else
					buffer = buffer + ",0.00001";

				
				buffer = buffer + me.getMyResults().getMDstats()+"\n";
				pw.write(buffer);
			}	
			pw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}