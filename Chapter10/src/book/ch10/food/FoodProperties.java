package book.ch10.food;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FoodProperties {
	
	private static FoodProperties instance = null;
	private Properties properties = null;
	
	private FoodProperties() {
		  try (InputStream input = new FileInputStream("./ea.properties")) {
	            properties = new Properties();
	            // load a properties file
	            properties.load(input);
	        } catch (IOException ex) {
	            ex.printStackTrace();
	        }
		}
	
	public static FoodProperties getInstance() {
		if (instance == null) {
			instance =  new FoodProperties();
		}
		return instance;
	}
	
	public String get(String propName) {
		return (properties.getProperty(propName));
	}
}
