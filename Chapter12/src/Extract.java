import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Extract {

	private static int[] depot = new int[15];
	private static String regex ="";

	public static void main(String[] args) {
		regex ="[1-2]-.-.-.-.-.-.";//low emissions
		//regex =".-.-.-.-.-.-[1-2]";//low time
		//regex ="[1-2]-.-.-.-.-.-[1-2]";//low emissions and low time
		
		//regex ="[1]-[1]-.-.-.-.-[1]";//low mds
		

		//regex ="";//Find all
		Extract listFiles = new Extract();
		listFiles.listAllFiles("./edinTest1.csv");
		listFiles.listAllFiles("./edinTest2.csv");
		listFiles.listAllFiles("./edinTest3.csv");
		listFiles.listAllFiles("./edinTest4.csv");
		listFiles.listAllFiles("./edinTest5.csv");
		listFiles.listAllFiles("./edinTest6.csv");
		listFiles.listAllFiles("./edinTest7.csv");
		listFiles.listAllFiles("./edinTest8.csv");
		listFiles.listAllFiles("./edinTest9.csv");
		listFiles.listAllFiles("./edinTest10.csv");

//		System.out.println("Depot totals");
//		for(int d :depot)
//			System.out.print(","+d);
		System.out.println();
	}
	// Uses listFiles method  

	// Uses Files.walk method   
	public void listAllFiles(String path){
		try(Stream<Path> paths = Files.walk(Paths.get(path))) {
			paths.forEach(filePath -> {
				if (Files.isRegularFile(filePath)) {
					try {
						readContent(filePath);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	public void readContent(Path filePath) throws IOException{
		if (filePath.toAbsolutePath().toString().contains(".kml"))
			return;
		if (filePath.toAbsolutePath().toString().contains(".DS_Store"))
			return;

		String key="";
		List<String> contents = Files.readAllLines(filePath);
		for (String line : contents) {
			if (line.contains("Plan")) {
				key = line.split(":")[1].trim();
				Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
				Matcher matcher = pattern.matcher(key);
				if (!matcher.find()) {
					System.out.println(key.replace('-', ',')+",0");
					return;
				}
					

			}
		}
//		String line = Files.readString(filePath);
//
//		if (line.contains("55.951744,-3.190667"))
//			depot[0]++;
//		if (line.contains("55.953799,-3.182706"))
//			depot[1]++;
//		if (line.contains("55.961186,-3.186546"))
//			depot[2]++;
//		if (line.contains("55.954827,-3.194715"))
//			depot[3]++;
//		if (line.contains("55.951788,-3.203384"))
//			depot[4]++;
//		if (line.contains("55.95067,-3.208985"))
//			depot[5]++;
//		if (line.contains("55.947378,-3.213013"))
//			depot[6]++;
//		if (line.contains("55.947618,-3.209193"))
//			depot[7]++;
//		if (line.contains("55.945886,-3.217218"))
//			depot[8]++;
//		if (line.contains("55.943651,-3.204065"))
//			depot[9]++;
//		if (line.contains("55.94424,-3.194259"))
//			depot[10]++;
//		if (line.contains("55.945754,-3.187242"))
//			depot[11]++;
//		if (line.contains("55.94085,-3.214002"))
//			depot[12]++;
//		if (line.contains("55.939593,-3.204741"))
//			depot[13]++;
//		if (line.contains("55.950571,-3.176596"))
//			depot[14]++;

		System.out.println(key.replace('-', ',')+",1");
		System.out.println();
	}   

	//	public static void main(String[] args) {
	//
	//
	//		try (BufferedReader br = Files.newBufferedReader(Paths.get("./edinTest1.csv/1_1_10_1_1_10_1/result.md"))) {
	//
	//			String line;
	//			String key="";
	//			while ((line = br.readLine()) != null) {
	//
	//				if (line.contains("Plan")) {
	//					key =line.split(":")[1].trim();
	//					System.out.println(key);
	//				}
	//			}
	//
	//		} catch (IOException e) {
	//			System.err.format("IOException: %s%n", e);
	//		}
	//	}
	//		//Write merged
	//		 try {
	//		 FileWriter fw = new FileWriter("merged.csv");
	//			       BufferedWriter bw = new BufferedWriter(fw);
	//			      bw.write("Dimensions,7\n"
	//			      		+ "Normalised,10\n"
	//			      		+ "evals,50000\n"
	//			      		+ "key,dist,emissions,MicroDepots,byMD,Bikes,Walkers,EVs,time,ActualEmissions,ActualMD,ActualByMD,ActualBikes,ActualWalkers,ActualEVs,ActualTime,sol\n");
	//			     
	//			      
	//			      for (String k : merged.keySet()) {
	//			    	  String l = merged.get(k);
	//			    	  bw.write(l +"\n");
	//			      }
	//			 bw.close();
	//		 }catch (IOException e) {
	//				System.err.format("IOException: %s%n", e);
	//			}


}
