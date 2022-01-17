package book.ch9.export;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import book.ch1.LatLon;
/*
 * Neil Urquhart 2021
 * A simple implementation of an ExportService
 * that exports to a CSV file. See also ExportService.java
 */
public class CSVWriter implements ExportService {
	private String csv;

	public CSVWriter() {
		csv = "ID,Lat,Lon\n";
	}
	@Override
	public void addTrack(ArrayList<LatLon> locs) {
		//There's no concept of a path  within the CSV
	}
	@Override
	public void addWaypoint(LatLon loc, String caption, String desc) {
		csv += caption +"," + loc.getLat() +"," + loc.getLon() + "," + desc +"\n";
	}
	@Override
	public void write(String file, String name) {
		try {
			file = file +".csv";
			FileWriter writer = new FileWriter(file, false);
			writer.append(name +"\n"+csv);
			writer.flush();writer.close();
            csv ="";
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
