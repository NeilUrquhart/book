package book.ch4.mapElites;

public class KeyGenerator  {
	/*
	 * A Singleton that is used to manage an instance of MAPElitesKeyGen
	 * object.
	 *
	 */

	private KeyGenerator() { }
	private static MAPElitesKeyGen instance = null;

	public static void setup(MAPElitesKeyGen keyGen) {
		//Set the MapElitesKeyGen object
		instance = keyGen;
	}
	
	public static MAPElitesKeyGen getInstance() {
		return instance;
	}	
}
