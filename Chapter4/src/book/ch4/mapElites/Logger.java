package book.ch4.mapElites;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Logger {
	/*
	 * Neil Urquhart - 2021
	 * A simple logger utility that is used by MAPElites to update a log file.
	 * The log details actions that take place within the run time of ME
	 * 
	 * The logger uses the Singleton pattern to simplify its use
	 * 
	 */



	/*
	 * Singleton code;
	 * 
	 */

	private Logger() {}
	private static Logger instance;
	public static Logger getLogger() {
		if (instance == null)
			instance = new Logger();
		return instance;
	}
	/*
	 * Logger code here 
	 *
	 */

	public enum Action{//The actions that can  be logged
		RANDOM,
		CLONE,
		RECOMBINATION,
		INIT
	}

	private static ArrayList<String> logBuffer = new ArrayList<String>();
	//Data is stored in logBuffer until it is writen to a file

	public void add(Action act, String message, double fit, int ... key) {
		/*
		 * Add an action to the log.  The action should relate to the MapElite bucket
		 * idetified by the key.
		 * 
		 */

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

		logBuffer.add(buffer);

	}

	public void clear() {
		//Remove entries from the buffer
		logBuffer.clear();
	}

	public void write(String filename) {
		/*
		 * Write the buffer to the specified file
		 * 
		 */
		try {
			FileWriter myWriter = new FileWriter(filename);
			for (String line : logBuffer)
				myWriter.write(line +"\n");
			myWriter.close();
			logBuffer.clear();
		} catch (IOException e) {
			System.out.println("Error writing log file.");
			e.printStackTrace();
		}
	}
}
