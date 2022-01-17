package book.ch5;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ProcessPareto {

	public static void main(String[] args) {
		
		 String path = "pareto.csv";

	        try {

	       
	            List<String> content = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);

	            //System.out.println(content);
	            String dist = null;
	            String time = null;
	            String cBag = null;
	            String prob = null;
	            String size = null;
	            String win = null;

	            System.out.println("Problem ,Size, WinLen , Distance , Time,CostPerBag");

	            
	            for (String line : content) {
	            	String[] tokens = line.split(",");
        			//System.out.println(ine);
	            	if(!tokens[0].equals("0")) {
	            		if (tokens[0].contains("Grand")) {
	            			win = tokens[1];
	            			prob = tokens[2];
	            			String tmp = prob.split("n")[1];
	            			size = tmp.split("-")[0];
	            		
	            		}else
	            		{
	            			
	            			dist = tokens[1];
	            			time = tokens[3];
	            			cBag = tokens[4].split("=")[1];
	            			
	            			System.out.println(prob +","+ size +","+win+","+dist+","+time+","+cBag);


	            		}
	            	}
	            }

	        } catch (IOException e) {
	        	e.printStackTrace();
	        }

}

}
