package book.ch12.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import book.ch1.LatLon;
import book.ch1.Visit;
import book.ch9.export.KMLWriter;

public class MultiModeKMLWriter extends KMLWriter{

	/*
	 * Neil Urquhart 2021
	 * A specialisation of KMLWriter for writing multi-modal problems.
	 * 
	 */
	private String routes = "";
	private String places = "";

	public void addWaypoint(LatLon pos, String title, String desc, String type) {
		String style = "style3";
		if (type.equals("del")) {
			style = "styleDel";
		}
		if (type.equals("rv")) {
			style = "styleRV";
		}

		String place = "<Placemark>" +
				"<name>"+ title +"</name>" +
				"<description><![CDATA["+desc+"]]></description>" +
				"<styleUrl>#"+style+"</styleUrl>" +
				"<Polygon>" +
				"<outerBoundaryIs>" +
				"<LinearRing>" +
				"<coordinates>" +
				pos.getLon() +"," + pos.getLat() + ",0.000000 " +
				(pos.getLon() +0.00009) +"," + (pos.getLat()+0.00009) + ",0.000000 " +
				(pos.getLon()+0.00009)+"," + (pos.getLat()-0.00009) + ",0.000000 " +

	          "</coordinates>" +
	          "</LinearRing>" +
	          "</outerBoundaryIs>" +
	          "</Polygon>" +
	          "</Placemark>";
		places = places + place;
	}

	public void addTrack(ArrayList<LatLon> pos, String name, String desc, String colour) {
		String style = "styleBLine";

		if (colour.equals("blue"))
			style = "styleBLine";

		if (colour.equals("green"))
			style = "styleGLine";

		if (colour.equals("red"))
			style = "styleRLine";

		if (colour.equals("yellow"))
			style = "styleYLine";

		if (colour.equals("darkgreen"))
			style = "styleDGLine";

		String place = " <Placemark> " +
				"<name>#"+name+"</name> " +
				"<description><![CDATA["+desc+"]]></description> " +
				"<styleUrl>#"+style+"</styleUrl> " +
				"<LineString> " +
				"<coordinates> ";
		for (LatLon l: pos) {
			place = place + l.getLon()+"," +l.getLat()+ ",0.000000 ";
		}

		place = place +"</coordinates> " +
				"</LineString> " +
				"</Placemark>";
		routes = routes + place;
	}

	public void writeFile(String fName) {
		try {
			String kml = new String();
			kml= kml +(new String(Files.readAllBytes( Paths.get("./kmlHeader.txt"))));
			kml= kml +(routes);
			kml= kml +(places);
			kml= kml +("\n" +new String ( Files.readAllBytes( Paths.get("./kmlFooter.txt"))));
			fName = fName+".kml";
			Files.write(Paths.get(fName), kml.getBytes());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}