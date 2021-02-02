package book.ch4.mapElites;

import java.util.ArrayList;
import java.util.HashSet;

import book.ch2.RandomSingleton;

import book.ch3.rep2.Domination;
import book.ch4.BiObjectiveTWIndividual;
import book.ch4.CVRPTWProblem;
import book.ch4.NonDomEA;
import book.ch4.NonDominatedPop;
import book.ch4.VRPTWProblemFactory;

/*
 * Copyright Neil Urquhart 2020
 * 
 */


public class TestMapElites {
	

		public static void main(String[] args){
			/*Problem instances from.
			 Augerat, P., Belenguer, J., Benavent, E., Corber´an, A., Naddef, D., Rinaldi, G., 1995.
	Computational results with a branch and cut code for the capacitated vehicle routing
	problem. Tech. Rep. 949-M, Universit´e Joseph Fourier, Grenoble, France. */
			//Header
			//System.out.println("Problem,Deliveries,Demand,Avg.Bags.Del,Time Window,Sol. Distance, Sol. Time,Sol. CostDel,Routes,Sol. Cost,Sol. Avg. Dels Route,Sol. Loading Factor");


			String[] problems = {/*"P-n101-k4", "A-n32-k5","B-n50-k8","A-n33-k5","A-n33-k6","A-n34-k5","A-n36-k5","A-n37-k5",
					"A-n37-k6","A-n38-k5","A-n39-k5","A-n39-k6","A-n44-k7","A-n45-k6","A-n45-k7",
					"A-n46-k7","A-n48-k7","A-n53-k7","A-n54-k7","A-n55-k9","A-n60-k9","A-n61-k9",
					"A-n62-k8","A-n63-k9","A-n63-k10","A-n64-k9","A-n65-k9","A-n69-k9","P-n16-k8","P-n19-k2","P-n20-k2","P-n21-k2","P-n22-k2","P-n22-k8","P-n23-k8",
					"P-n40-k5","P-n45-k5","P-n50-k7","A-n80-k10",
					"B-n31-k5","B-n34-k5","B-n35-k5","B-n38-k6","B-n39-k5","B-n41-k6","B-n43-k6",
					"B-n44-k7","B-n45-k5","B-n45-k6",*/"B-n50-k7","B-n51-k7","B-n52-k7",
					"B-n56-k7","B-n57-k7","B-n57-k9","B-n63-k10","B-n64-k9","B-n66-k9","B-n67-k10",
					"B-n68-k9","B-n78-k10",
					"P-n50-k8","P-n50-k10","P-n51-k10","P-n55-k7",
					"P-n55-k8","P-n55-k10","P-n55-k15","P-n60-k10","P-n60-k15","P-n65-k10",
					"P-n70-k10","P-n76-k4","P-n76-k5"};

			int[] wins = {1,7};
			for (String pName : problems){
				for (int win : wins){

					String fName  = pName+".vrp";
					String pSizeS = pName.split("-")[1];
					pSizeS = pSizeS.replace("n","");
				
					CVRPTWModalProblem myVRP = CVRPTWModalProblemFactory.buildProblem("./data/",fName,win);//Load instance from file

					HashSet<String> unique = new HashSet<String>();
					
					for (int x=0; x < 10; x++){
						Logger.getLogger().clear();
						MAPElites me = new MAPElites();
						myVRP.solve(me);
						unique.addAll(me.getKeys());
						me.exportToCSV(pName+"-"+win+"-"+x+".map.csv");
						System.out.println("," + pName+"-"+win+"-"+"," +x+","+ me.stats() +"\n");
						Logger.getLogger().write(pName+"-"+win+"-"+x+".log");
					}
					System.out.println("COMBINED," + pName+"-"+win+","+ unique.size() +"\n");
					

				}

			
		}
		}



}
