package book.ch4.mapElites;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Logger {
	
	public enum Action{
		RANDOM,
		CLONE,
		RECOMBINATION,
		INIT
	}
	

    /*
     * Singleton code;
     */
	
	private Logger() {}
	private static Logger instance;
	public static Logger getLogger() {
		if (instance == null)
			instance = new Logger();
		return instance;
	}
	/*
	 *Logger code here 
	 */
	
	private static ArrayList<String> logger = new ArrayList<String>();
	
	public void add(Action act, String message, double fit, int ... key) {
		
		String buffer = "";
		
		for (int x=0; x < key.length; x++) {
			buffer+= key[x]+":";
		}
		buffer +=",";
		if (act == Action.RANDOM) {
			buffer +="random";
			
		}
		
		if (act == Action.CLONE) {
			buffer +="clone";
		}
		
		if (act == Action.RECOMBINATION) {
			buffer +="recombination";
		}
		
		if (act == Action.INIT) {
			buffer +="initialisation";
		}
		buffer = buffer +"," +fit+","+ message;
		
		logger.add(buffer);
		
	}
	
	public void clear() {
		logger.clear();
	}
	
	public void write(String filename) {
		try {
		      FileWriter myWriter = new FileWriter(filename);
		      for (String line : logger)
		    	  myWriter.write(line +"\n");
		      myWriter.close();
		      logger.clear();
		    } catch (IOException e) {
		      System.out.println("Error writing log file.");
		      e.printStackTrace();
		    }
		
	}
	
	
}
