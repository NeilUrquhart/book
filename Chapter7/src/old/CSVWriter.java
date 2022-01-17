package old;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import book.ch1.Visit;

public class CSVWriter implements DataFileWriter {
	private String csv;

	public CSVWriter() {
		csv = "ID,Lat,Lon\n";
	}
	
	@Override
	public void addPath(ArrayList<Visit> locs) {
		//There's no concept of a path  within the CSV
	}

	@Override
	public void addWayPoint(Visit v, String desc) {
		csv += v.getName() +"," + v.getX() +"," + v.getY() + "," + desc +"\n";

	}

	@Override
	public void addWayPoint(Visit v) {
		addWayPoint(v, v.getName());

	}

	@Override
	public void write(String file, String name) {
	
		try {
			file = file +".csv";
			FileWriter writer = new FileWriter(file, false);
			writer.append(name +"\n");
			writer.append(csv);
			writer.flush();
			writer.close();
            csv ="";
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
