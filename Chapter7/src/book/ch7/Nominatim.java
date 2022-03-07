package book.ch7;

import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

import book.ch1.LatLon;



public class Nominatim implements Geocoder{
	private String nominatimBaseURL = "https://nominatim.openstreetmap.org";

	@Override
	public LatLon geocode(String label) {
		LatLon p = null;
		String request = nominatimBaseURL+ "/search?q="+label+"&format=xml";
		try {

			URL url = new URL(request);
			HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF8"));

			String response="";
			String buffer;
			while ((buffer =  in.readLine()) != null) {
				response += buffer;
			}
			p = extractLocation(response);
			in.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return p;
	}

	private LatLon extractLocation(String xml){
		/*
		 * A function that very crudely extracts the lat and lon from 
		 * the supplied xml and returns them as a Point2D object.
		 * 
		 * If anything goes wrong, null is returned.
		 */
		try {
			double lat=0,lon=0;
			String[] data = xml.split(" ");
			for (String section: data) {
				if (section.contains("lat='")) {
					//extract lat
					lat = Double.parseDouble(section.split("'")[1]);
				}
				if (section.contains("lon='")) {
					//extract lat
					lon = Double.parseDouble(section.split("'")[1]);
				}
			}
			return new LatLon(lat,lon);

		}catch(Exception e) {
			return null;
		}	
	}
	
	@Override
	public String reverseGeocode(LatLon p) {
		
		String request = nominatimBaseURL+ "/reverse?lat="+p.getLat()+ "&lon="+p.getLon()+ "&format=xml";
		try {

			URL url = new URL(request);
			HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF8"));

			String response="";
			String buffer;
			while ((buffer =  in.readLine()) != null) {
				response += buffer +"\n";
			}
			in.close();
			return response;
		}catch(Exception e) {
			e.printStackTrace();
			return "";
		}
	}


}


