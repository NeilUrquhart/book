package book.ch9.export;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import book.ch1.LatLon;

/*
 * Neil Urquhart 2021
 * A simple implementation of an ExportService
 * that exports to a KML file. See also ExportService.java
 */
public class KMLWriter implements ExportService{
	private String routes = "",places = "";
	
	public KMLWriter()  {}
	@Override
	public void addWaypoint(LatLon loc, String caption, String desc) {
		desc = caption +" " + desc;
		String place = "<Placemark><name>"+ caption +"</name>" +
				"<description><![CDATA["+desc+"]]></description>" +
				"<styleUrl>#delivery</styleUrl><Point> " +
				" <coordinates> "+loc.getLon()+ "," + loc.getLat()+ ",1</coordinates> " +
				" </Point> </Placemark>";
		places = places + place;
	}
	@Override
	public void addTrack(ArrayList<LatLon> locs) {
		String	style = "styleDGLine";
		String place = " <Placemark><name>  </name> " +
				"<description><![CDATA[ Delivery ]]></description> " +
				"<styleUrl>#"+style+"</styleUrl> <LineString> <coordinates> ";
		for (LatLon l: locs) {
			place = place + l.getLon()+"," +l.getLat()+ ",0.000000 ";
		}
		place = place +"</coordinates> </LineString> </Placemark>";
		routes = routes + place;
	}
	@Override
	public void write(String f, String name) {
		try {
			FileWriter writer = new FileWriter(f+".kml", false);
			writer.append(header() + routes + places + "\n" +footer());
			writer.close();
			routes ="";
			places = "";
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	private String header() {//KML header
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
				"<kml xmlns=\"http://earth.google.com/kml/2.2\">\r\n" + 
				"<Document>\r\n <name>Delivery Route;</name>\r\n" + 
				"  <description><![CDATA[Route]]></description>\r\n";
	}
	private String footer() {//KML footer
		return "</Document>\r\n</kml>";
	}
}