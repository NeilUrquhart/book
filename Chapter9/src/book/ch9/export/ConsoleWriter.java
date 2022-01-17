package book.ch9.export;
import java.util.ArrayList;

import book.ch1.LatLon;

/*
 * Neil Urquhart 2021
 * A simple implementation of ExportService that writes to theConsole
 */
public class ConsoleWriter implements ExportService {
	private StringBuffer buffer = new StringBuffer();
	@Override
	public void addTrack(ArrayList<LatLon> locs) {/*Not implemented*/	}

	@Override
	public void addWaypoint(LatLon loc,String name, String desc) {
		buffer.append(name +","+desc +" ,"+loc.getLat()+","+loc.getLon()+"\n");
	}

	@Override
	public void write(String path, String name) {
		//<path> is ignored
		System.out.println(name);
		System.out.println(buffer);
		buffer = new StringBuffer();
	}
}
