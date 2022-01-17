package old;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;

import book.ch1.Visit;


public class GPXWriter implements DataFileWriter  {
	private  String segments = "";
	private String waypoints ="";


	@Override
	public  void addPath(ArrayList<Visit> locs) {
		for (Visit v : locs){
			segments += "<trkpt lat=\"" + v.getX() + "\" lon=\"" + v.getY() + "\">"+"</trkpt>\n";
		}
	}

	@Override
	public void addWayPoint(Visit v, String desc) {
		desc = v.getName() +" " + desc;
		waypoints +=
				"<wpt lat=\""+v.getX()+" \" lon=\" "+v.getY()+"\">"+" <name>"+desc+ "</name></wpt>";
	}

	@Override
	public void addWayPoint(Visit v) {
		addWayPoint(v, "");
	}


	@Override
	public  void write(String file, String name) {
		file = file +".gpx";
		String header = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>"
				+ "<gpx xmlns=\"http://www.topografix.com/GPX/1/1\" creator=\"MapSource 6.15.5\" version=\"1.1\" "
				+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"  xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 "
				+ "http://www.topografix.com/GPX/1/1/gpx.xsd\">\n";
		name = "<name>" + name+ "</name>\n";

		String footer = "</trkseg></trk></gpx>";

		try {

			FileWriter writer = new FileWriter(file, false);
			writer.append(header);
			writer.append(name);

			writer.append(waypoints);
			writer.append("<trk><trkseg>");
			writer.append(segments);
			writer.append(footer);
			writer.flush();
			writer.close();
			waypoints = "";
			segments = "";
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
