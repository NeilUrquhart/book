package book.ch11;

import book.ch1.LatLon;
import book.ch7.Geocoder;
import book.ch7.Nominatim;

/* Written by Neil Urquhart, 2021
 * 
 * This class represents an individual house within the SBR scheme
 * 
 */
public class House{
	
	static private Geocoder geocode = new PersistantCache(new Nominatim());
	private int number;
	private LatLon location;
	private StreetSection street;
	
	public House(int num, StreetSection street) {
		/*
		 * The combination of street and num must be 
		 * acceptable to the Geocoder
		 */
		
		this.street = street;
		number = num;
		System.out.println("Adding " + num + " "+ street.getName());
		location = geocode.geocode(num + " "+street.getName());
		((PersistantCache) geocode).save();
	}
	
	public String toString() {
		return number + " " + street.getName() +"("+ location+")";
	}
	
	public int getNumber() {
		return number;
	}
	
	public LatLon getLocation() {
		return location;
	}
}