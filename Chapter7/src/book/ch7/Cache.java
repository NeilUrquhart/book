package book.ch7;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import book.ch1.Visit;
/*
 * Neil Urquhart 2021
 * 
 * An implementation of a Geocoder cache.
 * 
 * Calls  are checked against the local cache, if
 * there is no entry then the baseCoder is called.
 * 
 */
public class Cache implements Geocoder{
	private ArrayList<Entry> cache = new ArrayList<Entry>();
	private Geocoder baseCoder = null;
	private final double REVERSE_THRESHOLD = 0.01;//Threashhold for reverse geocoding
	
	public Cache(Geocoder baseCoder) {
		/* 
		 * Any entries not in the cache are resolved using
		 * the baseCoder and then cached 
		 */
		this.baseCoder = baseCoder;	
		}
	
	@Override
	public Point2D.Double geocode(String label) {
		for (Entry e : cache) {
			if (e.label.equals(label))
				return e.location;
		}
		/*
		 * We can assume the label has not been found
		 * 
		 * Resolve the label using the baseCoder
		 */
		System.out.println("Adding to cache");
		Point2D.Double  p = baseCoder.geocode(label);
		cache.add(new Entry(p,label));
		return p;
	}

	@Override
	public String reverseGeocode(Point2D.Double p) {
		double dist = Double.MAX_VALUE;
		String best = "";
		
		for (Entry e : cache) {
			double cDist = p.distance(e.location);
			if (cDist < dist) {
				dist = cDist;
				best = e.getLabel();
			}
		}
		if (dist <= REVERSE_THRESHOLD)
			return best;
		//Else cache miss
		String result = baseCoder.reverseGeocode(p);
		cache.add(new Entry(p,result));
		return result;
	}
	
	public void showCache() {
		/*
		 * Display the contents of the cache
		 */
		
		for (Entry e : cache) {
			System.out.println(e.location +"\n\t"+e.label);
		}
	}
	
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
}


