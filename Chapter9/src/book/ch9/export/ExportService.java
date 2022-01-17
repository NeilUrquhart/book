package book.ch9.export;

import java.util.ArrayList;

import book.ch1.LatLon;

/*
 * Neil Urquhart 2021
 * An interface that defines a data export service.
 */
public interface ExportService {
	void addTrack(ArrayList<LatLon> locs);
	//Draw a track along the lost of LatLons provided
	void addWaypoint(LatLon loc, String caption, String desc);
	//Add a waypoint at <loc>
	void write(String path, String name);
	//Write the contents to a file at <path> using <name>
}