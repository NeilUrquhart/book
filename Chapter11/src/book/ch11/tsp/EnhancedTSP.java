package book.ch11.tsp;

import java.util.HashMap;

import book.ch1.LatLon;
import book.ch1.TSPProblem;
import book.ch1.Visit;
import book.ch9.routing.GraphHopper;
import book.ch9.routing.RoutingEngine;


public class EnhancedTSP extends TSPProblem {
	
	private static RoutingEngine router;
	
	@Override
	public double getDistance(Visit x, Visit y){
		//Get the distance between two visits
		if ((x == null)||(y==null))
			return 0;
		else	{
			return x.distance(y);
		}
			
	}
	private static HashMap<String,String> options=null;
	private static HashMap<Long,Double> cache = new HashMap<Long,Double>();
	
	private static void initRouter() {
		router = new GraphHopper();
		options = new HashMap<String,String>();
		options.put(RoutingEngine.OPTION_OSM_FILE, "./data/map.osm");
		options.put(RoutingEngine.OPTION_DATA_DIR, "./data/");
	}
	
	public double getFinalDist() {
		double res=0;
		LatLon prev = super.getStart();
		for (Visit v : super.currentSolution) {
			res = res + this.getJourneyDist(prev, v, "foot");
			
			prev = v;
		}
		
		res = res + this.getJourneyDist(prev, super.getStart(), "foot");
		return res;
	}
	 private double getJourneyDist(LatLon start,LatLon end, String mode) {
		 if (options==null)
			 initRouter();
		 
		 long key = cantorPair(start.hashCode(),end.hashCode());
		 Double res = cache.get(key);
		 if (res == null) {
			 options.put(RoutingEngine.OPTION_MODE, mode);
			 res = router.findRoute(start, end, options).getDistanceKM();
			 cache.put(key, res);
		 }
		 return res;
	 }
	 
		public static long cantorPair(long a, long b) {

	        //Cantors pairing function only works for positive integers
	       // if (a > -1 || b > -1) {
	            //Creating an array of the two inputs for comparison later
	         //   long[] input = {a, b};

	            //Using Cantors paring function to generate unique number
	            long result = (long) (0.5 * (a + b) * (a + b + 1) + b);

//	            /*Calling depair function of the result which allows us to compare
//	             the results of the depair function with the two inputs of the pair
//	             function*/
//	            if (Arrays.equals(depair(result), input)) {
	                return result; //Return the result
//	            } else {
//	                return -1; //Otherwise return rouge value
//	            }
//	        } else {
//	            return -1; //Otherwise return rouge value
//	        }
	    }
}
