package book.ch2;

import java.util.Random;

public class RandomSingleton {
	private static  RandomSingleton instance= null;
	
	
	private RandomSingleton() { rnd = new Random();	}
	
	public static RandomSingleton getInstance() {
		if (instance == null)
			instance = new RandomSingleton();
		return instance;
	}
	
	private Random rnd = null;
	
    public void setSeed(int seed) {
    	rnd = new Random(seed);
    }
    
    public Random getRnd() {
    	return rnd;
    }
    
    
}
