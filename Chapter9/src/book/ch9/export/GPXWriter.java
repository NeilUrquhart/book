package book.ch9.export;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import book.ch1.LatLon;

/*
 * Neil Urquhart 2021
 * A simple implementation of an ExportService
 * that exports to a GPX file. See also ExportService.java
 */
public class GPXWriter implements ExportService  {
	private  String segments = "",waypoints ="";

	@Override
	public  void addTrack(ArrayList<LatLon> locs) {
		for (LatLon l : locs){
			segments += "<trkpt lat=\"" + l.getLat() + "\" lon=\"" + l.getLon() + "\">"+"</trkpt>\n";
		}
	}
	@Override
	public void addWaypoint(LatLon l,String caption, String desc) {
		desc = caption +" " + desc;
		waypoints +=
				"<wpt lat=\""+l.getLat()+" \" lon=\" "+l.getLon()+"\">"+" <name>"+desc+ "</name></wpt>";
	}
	@Override
	public  void write(String file, String name) {
		file = file +".gpx";
		name = "<name>" + name+ "</name>\n";
		try {
			FileWriter writer = new FileWriter(file, false);
			writer.append(header() +name + waypoints + "<trk><trkseg>" + segments + footer());
			writer.flush();writer.close();
			waypoints = "";segments = "";
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private String header() {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>\n"

			+ "<gpx xmlns=\"http://www.topografix.com/GPX/1/1\" "
			+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"  xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 "
			+ "http://www.topografix.com/GPX/1/1/gpx.xsd\">\n";
	}
	private String footer() {
		return "</trkseg></trk></gpx>";
	}
}