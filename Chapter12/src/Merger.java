import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class Merger {

	private static HashMap<String,String> merged = new HashMap<String,String>();

	public static void main(String[] args) {


		//Read fileS
		for (int c=1;c <=10;c++) {
			try (BufferedReader br = Files.newBufferedReader(Paths.get("FINAL-edinTest"+c+".csv"))) {

				// read line by line
				String line;
				//Skip header
				br.readLine();br.readLine();br.readLine();br.readLine();

				while ((line = br.readLine()) != null) {
					//System.out.println(line);
					String key = line.split(",")[0];

					if(!merged.containsKey(key)) {
						merged.put(key, line);
						System.out.println("Added");
					}else {
						String sFit = line.split(",")[1];
						double nfit = Double.parseDouble(sFit);
						String oLine = merged.get(key);
						String osFit = oLine.split(",")[1];
						double ofit = Double.parseDouble(osFit);

						if (nfit < ofit) {
							merged.remove(key);
							merged.put(key,line);
							System.out.println("Replaced");
						}
					}

				}

			} catch (IOException e) {
				System.err.format("IOException: %s%n", e);
			}
		}
		//Write merged
		 try {
		 FileWriter fw = new FileWriter("merged.csv");
			       BufferedWriter bw = new BufferedWriter(fw);
			      bw.write("Dimensions,7\n"
			      		+ "Normalised,10\n"
			      		+ "evals,50000\n"
			      		+ "key,dist,emissions,MicroDepots,byMD,Bikes,Walkers,EVs,time,ActualEmissions,ActualMD,ActualByMD,ActualBikes,ActualWalkers,ActualEVs,ActualTime,sol\n");
			     
			      
			      for (String k : merged.keySet()) {
			    	  String l = merged.get(k);
			    	  bw.write(l +"\n");
			      }
			 bw.close();
		 }catch (IOException e) {
				System.err.format("IOException: %s%n", e);
			}
	}

}
