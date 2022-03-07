package book.ch10;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import book.ch10.food.FoodFacade;
import book.ch10.food.FoodFacade.SaveTo;


/*
 * Written by Neil Urquhart 2022
 * 
 * A simple food delivery App
 * 
 * Note!  To use this you will have to download scotland-latest.osm.pbf and
 * place in in /data  
 * 
 * Copies of scotland-latest.osm.pbf may be found at http://download.geofabrik.de/europe/great-britain/scotland.html
 * 
 * 
 */
public class FoodDeliveryApp {
	public static void main(String[] args) {
		//Load a problem from a file
		FoodFacade food = loadProblem(new File("./problem.csv"));
		//solve the problem

		//temp
		int[] times = {20,40,60,80,100,120,140,160,180,200,220,240};
		for (int t: times) {
			System.out.println("times= " +t);
			food.setMaxMinsRound(t);
			food.solve();

			//done temp
			//food.solve();
			//save the solution in a variety of formats
			try {
				System.out.println("time = " +t);
				food.save(SaveTo.CONSOLE);
				food.save(SaveTo.KML);
				food.save(SaveTo.GPX);
				food.save(SaveTo.CSV);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static FoodFacade loadProblem(File file) {
		//Return a new FoodFacade based on the problem within the file
		try { 
			FoodFacade food = new FoodFacade();
			Scanner myReader = new Scanner(file);
			while (myReader.hasNextLine()) {
				processLine(food, myReader.nextLine());
			}
			myReader.close();
			return food;
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
			return null;
		}
	}

	private static void processLine(FoodFacade hf,String line) {
		//Process a single line of the .CSV file
		String[] data = line.split(",");
		data[0] = data[0].toLowerCase().trim();
		if (data[0].equals("ref")) {
			hf.setReference(data[1]);
		}
		if (data[0].equals("commences")) {
			hf.setStartDateTime(data[1]);
		}
		if (data[0].equals("start")) {
			hf.setStart(data[1]);
		}
		if (data[0].equals("visit")) {
			hf.addVisit(data[2],data[3],Integer.parseInt(data[4]),"");
		}
		if (data[0].equals("capacity")) {
			hf.setCapacity(Integer.parseInt(data[1]));
		}
		if (data[0].equals("mins/delivery")) {
			hf.setMinsDel(Integer.parseInt(data[1]));
		}
		if (data[0].equals("time/round")) {
			hf.setMaxMinsRound(Integer.parseInt(data[1]));
		}
		if (data[0].equals("mode")) {
			hf.setRoundMode(data[1]);
		}
	}
}
