package book.ch5;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class HypervolumeCalc {
	public static void main(String[] args) {
		try (BufferedReader br = Files.newBufferedReader(Paths.get("newresfronts.csv"))) {
			ArrayList<String[]> fileContents = new ArrayList<String[]>();
			br.readLine();//skip header
			// read line by line
			String line;
			String[] prev = null; 
			while ((line = br.readLine()) != null) {
				String[] data = line.split(",");
				if (prev==null) prev = data;
				
				if(!prev[0].equals(data[0])) {
					//System.out.println("break");
					writeFiles(fileContents);
					fileContents.clear();
				}
				fileContents.add(data);
				//System.out.println(line);
				prev = data;
			}
			writeFiles(fileContents);
		} catch (IOException e) {
			System.err.format("IOException: %s%n", e);
		}
	}
	
	public static void writeFiles(ArrayList<String[]> contents) throws IOException {
		String[] line = contents.get(0);
		String fName = line[0];
		String nadir = getNadir(contents);
		System.out.println("echo "+fName);
		System.out.println("./hv -q -r \""+ nadir + "\" " +fName +"-1.txt");
		BufferedWriter writer = new BufferedWriter(new FileWriter("./hypervol/" +fName +"-1.txt"));
	    
		String old = line[1];
		for(String[] data : contents) {
			
			if(!data[1].equals(old)){
				System.out.println("./hv -q -r \""+ nadir + "\" " +fName +"-14.txt");
				writer.close();
				writer = new BufferedWriter(new FileWriter("./hypervol/" +fName +"-14.txt"));
			}
			//System.out.println(data[2]+" " +data[3]+" " +data[4]+" " +data[5]);
			writer.write(data[2]+" " +data[3]+" " +data[4]+" " +data[5] +"\n");
			
			old = data[1];
			
		}
		writer.close();
		
	}
	
	public static String getNadir(ArrayList<String[]> data) {
		double v1=0,v2=0,v3=0,v4=0;
		for (String[] line : data) {
			double a = Double.parseDouble(line[2]);
			double b = Double.parseDouble(line[3]);
			double c = Double.parseDouble(line[4]);
			double d = Double.parseDouble(line[5]);
			
			if ( a > v1 ) v1 = a;
			if ( b > v2 ) v2 = b;
			if ( c >v3 ) v3 = c;
			if ( d > v4 ) v4 = d;
			
			
			
		}
		return ""+ (v1+1)+ " " +( v2+1) + " "+ (v3+1) +" "+(v4+1);
	}
	
}
