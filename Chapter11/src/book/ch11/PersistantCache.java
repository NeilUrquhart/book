package book.ch11;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import book.ch1.LatLon;
import book.ch9.Cache;
import book.ch9.Geocoder;


/*
 * An implementation of a Persistent Cache for use with a 
 * Geocoder  (book.ch7.Geocoder) acting as the base object
 * The cache is a text file that is stored as ./cache.txt
 * 
 * Please note that the cache file uses % as a separator so as
 * to allow for , to be used in address strings
 */

public class PersistantCache extends Cache {
	
	private String CACHE_FILE = "./cache.txt";
	//The file used to store the cache between sessions

	public PersistantCache(Geocoder baseCoder) {
		super(baseCoder);
		//Load existing cache (if it exists)
		try {
			File f = new File(CACHE_FILE);
			BufferedReader b = new BufferedReader(new FileReader(f));
			String line = "";

			while ((line = b.readLine()) != null) {
				String[] data = line.split("%");
				String label = data[0];
				double lat = Double.parseDouble(data[1]);
				double lon = Double.parseDouble(data[2]);
				Entry e = new Entry(new LatLon(lat,lon),label);
				cache.add(e);
			}
		} catch (IOException e) {
			/*
			 * Called if cache file is not found or not read
			 * for any other reason.  Print message and
			 * continue.
			 */
			e.printStackTrace();
		}

	}

	public void save() {
		/*
		 * Write cache contents to ./cache.txt
		 */
		try {
			FileWriter myWriter = new FileWriter(CACHE_FILE);
			for (Entry e : cache) {
				myWriter.write(e.toTXT());
			}
			myWriter.close();
		} catch (IOException i) {
			i.printStackTrace();
		}
	}
}
