package old;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;


import book.ch1.Visit;

public class KMLWriter implements DataFileWriter{

	private String routes = "";
	private String places = "";
	
	public KMLWriter()  {
	}

	@Override
	public void addWayPoint(Visit v, String desc) {
		desc = v.getName() +" " + desc;
		String style = "delivery";	
		String place = "<Placemark>" +
				"<name>"+ v.getName() +"</name>" +
				"<description><![CDATA["+desc+"]]></description>" +
				"<styleUrl>#"+style+"</styleUrl>" +
				"<Point> " +
				" <coordinates> "+v.getY()+ "," + v.getX()+ ",1</coordinates> " +
				" </Point> " +
				"</Placemark>";
		places = places + place;
	}

	@Override
	public void addWayPoint(Visit v) {
		addWayPoint(v,"");
	}
	
	@Override
	public void addPath(ArrayList<Visit> locs) {
	
		String	style = "styleDGLine";

		String place = " <Placemark> " +
				"<name>  </name> " +
				"<description><![CDATA[ Delivery ]]></description> " +
				"<styleUrl>#"+style+"</styleUrl> " +
				"<LineString> " +
				"<coordinates> ";
		for (Visit v: locs) {
			place = place + v.getY()+"," +v.getX()+ ",0.000000 ";
		}

		place = place +"</coordinates> " +
				"</LineString> " +
				"</Placemark>";
		routes = routes + place;
	}

	@Override
	public void write(String f, String name) {
		try {
			f = f +".kml";
			String kml = header();
			kml = kml + routes;
			kml = kml + places;

			kml = kml + "\n" +footer();
			
			FileWriter writer = new FileWriter(f, false);
			writer.append(kml);
			writer.close();
			routes ="";
			places = "";
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private String header() {
		//KML header
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
				"<kml xmlns=\"http://earth.google.com/kml/2.2\">\r\n" + 
				"<Document>\r\n" + 
				"  <name>Delivery Route;</name>\r\n" + 
				"  <description><![CDATA[Food delivery route]]></description>\r\n" + 
				"  <Style id=\"style1\">\r\n" + 
				"    <IconStyle>\r\n" + 
				"      <Icon>\r\n" + 
				"        <href>http://maps.google.com/mapfiles/ms/icons/blue-dot.png</href>\r\n" + 
				"      </Icon>\r\n" + 
				"    </IconStyle>\r\n" + 
				"  </Style>\r\n" + 
				"  <Style id=\"style3\">\r\n" + 
				"    <LineStyle>\r\n" + 
				"      <color>40000000</color>\r\n" + 
				"      <width>3</width>\r\n" + 
				"    </LineStyle>\r\n" + 
				"    <PolyStyle>\r\n" + 
				"      <color>1A0000FF</color>\r\n" + 
				"      <fill>1</fill>\r\n" + 
				"      <outline>1</outline>\r\n" + 
				"    </PolyStyle>\r\n" + 
				"  </Style>\r\n" + 
				"<Style id=\"styleRV\">\r\n" + 
				"    <LineStyle>\r\n" + 
				"      <color>40000000</color>\r\n" + 
				"      <width>3</width>\r\n" + 
				"    </LineStyle>\r\n" + 
				"    <PolyStyle>\r\n" + 
				"      <color>50147800</color>\r\n" + 
				"      <fill>1</fill>\r\n" + 
				"      <outline>1</outline>\r\n" + 
				"    </PolyStyle>\r\n" + 
				"  </Style>\r\n" + 
				"<Style id=\"styleDel\">\r\n" + 
				"    <LineStyle>\r\n" + 
				"      <color>40000000</color>\r\n" + 
				"      <width>3</width>\r\n" + 
				"    </LineStyle>\r\n" + 
				"    <PolyStyle>\r\n" + 
				"      <color>1A0000FF</color>\r\n" + 
				"      <fill>1</fill>\r\n" + 
				"      <outline>1</outline>\r\n" + 
				"    </PolyStyle>\r\n" + 
				"  </Style>\r\n" + 
				"    <Style id=\"style4\">\r\n" + 
				"    <LineStyle>\r\n" + 
				"      <color>A600CC33</color>\r\n" + 
				"      <width>10</width>\r\n" + 
				"    </LineStyle>\r\n" + 
				"  </Style>\r\n" + 
				"  <Style id=\"style5\">\r\n" + 
				"    <LineStyle>\r\n" + 
				"      <color>40000000</color>\r\n" + 
				"      <width>3</width>\r\n" + 
				"    </LineStyle>\r\n" + 
				"    <PolyStyle>\r\n" + 
				"      <color>73FF0000</color>\r\n" + 
				"      <fill>1</fill>\r\n" + 
				"      <outline>1</outline>\r\n" + 
				"    </PolyStyle>\r\n" + 
				"  </Style>\r\n" + 
				"  <Style id=\"style2\">\r\n" + 
				"    <LineStyle>\r\n" + 
				"      <color>E60000FF</color>\r\n" + 
				"      <width>5</width>\r\n" + 
				"    </LineStyle>\r\n" + 
				"  </Style>\r\n" + 
				"  <Style id=\"styleBLine\">\r\n" + 
				"    <LineStyle>\r\n" + 
				"      <color>50000000</color>\r\n" + 
				"      <width>5</width>\r\n" + 
				"    </LineStyle>\r\n" + 
				"  </Style>\r\n" + 
				"    <Style id=\"styleGLine\">\r\n" + 
				"    <LineStyle>\r\n" + 
				"      <color>5014F000</color>\r\n" + 
				"      <width>5</width>\r\n" + 
				"    </LineStyle>\r\n" + 
				"  </Style>\r\n" + 
				"    <Style id=\"styleYLine\">\r\n" + 
				"    <LineStyle>\r\n" + 
				"      <color>5014F0FF</color>\r\n" + 
				"      <width>5</width>\r\n" + 
				"    </LineStyle>\r\n" + 
				"  </Style>\r\n" + 
				"      <Style id=\"styleRLine\">\r\n" + 
				"    <LineStyle>\r\n" + 
				"      <color>501400FF</color>\r\n" + 
				"      <width>5</width>\r\n" + 
				"    </LineStyle>\r\n" + 
				"  </Style>\r\n" + 
				"  <Style id=\"delivery\">\r\n" + 
				"    <IconStyle>\r\n" + 
				"        <Icon>\r\n" + 
				"          <href>https://upload-icon.s3.us-east-2.amazonaws.com/uploads/icons/png/13764952091536837650-512.png</href>\r\n" + 
				"        </Icon>\r\n" + 
				"    </IconStyle>\r\n" + 
				"</Style>\r\n" + 
				"\r\n" + 
				"  <Style id=\"cafe\">\r\n" + 
				"    <IconStyle>\r\n" + 
				"        <Icon>\r\n" + 
				"          <href>https://upload-icon.s3.us-east-2.amazonaws.com/uploads/icons/png/20617935631580622631-512.png</href>\r\n" + 
				"        </Icon>\r\n" + 
				"    </IconStyle>\r\n" + 
				"</Style>";
	}
	
	private String footer() {
		//KML footer
		
		return "</Document>\r\n" + 
				"</kml>";
		
	}
}
