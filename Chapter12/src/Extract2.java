import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Extract2 {

	private static String regex ="";
	private static ArrayList<String> pos = new ArrayList<String>();
	private static ArrayList<String> neg = new ArrayList<String>();


	public static void main(String[] args) {
	//	emissions	MicroDepots	byMD	Bikes	Walkers	EVs	time
		//regex ="[1-2]-.-.-.-.-.-.";//low emissions
		//regex =".-.-.-.-.-.-[1-2]";//low time
		regex ="[1-2]-.-.-.-.-.-[1-2]";//low emissions and low time

		//regex ="[1]-[1]-.-.-.-.-[1]";//low mds


		//regex ="";//Find all
		Extract2 listFiles = new Extract2();
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

		//balance

		while(pos.size()!=neg.size()){
			if (pos.size() > neg.size()) 
				pos.remove(0);
			else
				neg.remove(0);
		}
		
		for (String s : pos) {
			System.out.println(s);
		}
		for (String s : neg) {
			System.out.println(s);
		}
		//System.out.println(pos.size());
		//System.out.println(neg.size());
	}

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
					//System.out.println(key.replace('-', ',')+",0");
					key = key.replace('-', ',')+",0";
					if (!neg.contains(key))
					  neg.add(key);
					//else
					//	System.out.println("dup");
					return;
				}
			}
		}
		//System.out.println(key.replace('-', ',')+",1");
		//pos.add(key.replace('-', ',')+",1");
		key = key.replace('-', ',')+",1";
		if (!pos.contains(key))
		  pos.add(key);
		//else
			//System.out.println("dup");
	}   
}
