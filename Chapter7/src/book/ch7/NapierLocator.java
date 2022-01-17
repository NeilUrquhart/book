package book.ch7;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import book.ch1.Visit;

public class NapierLocator implements Geocoder{
	/*
	 * Neil Urquhart 2021
	 * 
	 * A very simple Geocoder implementation. 
	 * 
	 * This class implements a simple Geocoder that encodes
	 * the locations of the three campuses of Edinburgh 
	 * Napier University
	 */
	
	private class Entry{
		/*
		 * An inner class used to store a mapping between a label
		 * and a location (stored as a Visit)
		 */
		private Point2D.Double location;
		
		public Point2D getLocation() {
			return location;
		}

		public String getLabel() {
			return label;
		}

		private String label;
		
		public Entry(Point2D.Double loc, String label) {
			this.location =loc;
			this.label = label;
		}
	}
	private ArrayList<Entry> data = new ArrayList<Entry>();
	
	public NapierLocator() {
		/* Initialise.
		 * In a practical implementation this constructor would load the data
		 * in from a CSV file, rather than hard coding  
		 */
		data.add( new Entry(new Visit("Merchiston",55.932941, -3.213922),"Napier University, Merchiston"));
		data.add( new Entry(new Visit("Craiglockhart",55.918029, -3.239706),"Napier University, Craiglockhart"));
		data.add( new Entry(new Visit("Sighthill",55.924190, -3.288473),"Napier University, Sighthill"));
		}
	
	@Override
	public Point2D.Double geocode(String label) {
		for (Entry e : data) {
		if (e.label.equals(label))
			return e.location;
	}
	return null;
	}

	@Override
	public String reverseGeocode(Point2D.Double p) {
		double dist = Double.MAX_VALUE;
		String best = "Not found";
		
		for (Entry e : data) {
			double cDist = p.distance(e.location);
			if (cDist < dist) {
				dist = cDist;
				best = e.getLabel();
			}
		}
		return best;
	}
}


