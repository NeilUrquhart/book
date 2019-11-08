package book.ch1;
import java.text.DecimalFormat;
import java.util.ArrayList;

/*
 * Neil Urquhart 2019
 * 
 * This programme asssess the TSP solvers used in the Santa Claus problem
 */

public class CompareApp {
	public static void main(String[] args)
	{
		//Setup a new TSP problem
		TSPProblem problem = new TSPProblem();
		
		problem.setStart(new Visit("Grotto",67.922234,26.504644));

		Visit[] cities = new Visit[]{	
				new Visit("Jamie",55.917357,-3.216616),
				new Visit("Sian",51.393208, -0.318050),
				new Visit("Ceri",56.335573, 9.784953),
				new Visit("Chris",55.672164, 37.721952),
				new Visit("Louise",38.380422, 32.604856),
				new Visit("Dan",29.807284, 3.437926),
				new Visit("Roy",14.567634, 47.012616),
				new Visit("Helen",-4.428567, 16.967165),
				new Visit("Emma",14.397439, 77.936590),
				new Visit("Simon",-0.038452, 115.010218),
				new Visit("Leanne",-26.936762, 138.027494),
				new Visit("Katy",-8.476723, 31.716600),
				new Visit("Matt",-22.008248, -60.703009),
				new Visit("Sally",21.223846, 77.750702),
				new Visit("Dimitra",46.268000, 22.588232),
				new Visit("Andreas",11.109416, 105.871302),
				new Visit("Michael",44.037060, -77.560095),
				new Visit("Thomas",31.653386, -103.080151),
				new Visit("Fiona",22.917928, -102.377341),
				new Visit("Colin",28.675175, 111.417667),
				new Visit("Andrea",-7.013652, 141.287122),
				new Visit("Emily",-30.751264, 120.905611),
				new Visit("Gordon",47.374563, 133.415633),
				new Visit("Jim",34.828235, -88.464033),
				new Visit("Alison",18.912524, 45.770300),
				new Visit("Harry",18.579611, 3.601657),
				new Visit("Mary",65.687248, 175.930875),
				new Visit("Jane",14.536797, 104.147903),
				new Visit("James",38.822587, 34.991312),
				new Visit("Ellie",46.558857, 1.959208),
				new Visit("Linda",-13.650256, -47.940353),
				new Visit("Maria",63.328471, -47.307828),
				new Visit("Tom",63.952818, -16.032751),
				new Visit("Xiaodong",53.078188, -66.687900),
				new Visit("John",26.052836, 9.215657),
				new Visit("Phill",-1.898083, 114.075003),
				new Visit("Alexandra",-45.101444, 169.908390),
				new Visit("Beth",-25.736568, 121.080616),
				new Visit("Brian",-6.594760, 141.497270),
				new Visit("Neil",-3.425682, 103.967176),
				new Visit("Silke",22.626184, 89.960573),
				new Visit("Manwell",21.076302, 57.016320),
				new Visit("Polly",33.460779, 52.184496),
				new Visit("Sybill",44.427504, 43.313211),
				new Visit("Oliver",42.124303, 24.723865),
				new Visit("Alex",47.883935, 4.395062),
				new Visit("Robert",57.900841, -4.565774),
				new Visit("Jenny",41.284403, 14.708812),
				new Visit("Sonia",37.481824, 22.439730),
				new Visit("Ann",48.980211, 84.603336)};

		//Solvers to be tested
		TSPSolver[] solvers = new TSPSolver[]{new ExhaustiveTSPSolver(),new NearestNTSPSolver(), new TwoOptTSPSolver(), new Hybrid()};

		for (int c=0; c < cities.length; c++){ //Add cities to problem
			problem.addVisit(cities[c]);
			System.out.print("\nVisits , "+ problem.getSize());
			
			for (TSPSolver solver : solvers){
				//Repeat 10 times
				double[] dist=  new double[10];
				double[] time = new double[10];
				for (int x=0; x < 10; x ++){
					time[x] = System.currentTimeMillis();
					problem.shuffle();
					problem.solve(solver);
					dist[x] = problem.getDistance();
					time[x] = System.currentTimeMillis()-time[x];
				}
				DecimalFormat df = new DecimalFormat("#.##");
				System.out.print(","+solver +","    );
				for (double d : time) System.out.print(df.format(d)+",");
				System.out.print("avg,"+ df.format(avg(time))+",");
				for (double d : dist) System.out.print(df.format(d)+",");
				System.out.print("min(avg),"+df.format(min(dist))+"("+df.format(avg(dist))+")");
			}
		}
	}
	
	public static double avg(double[] data) {
		double avg=0;
		for(double d : data)
			avg += d;
		avg = avg/data.length;
		return avg;
	}
	
	public static  double min(double[] data) {
		double min=Double.MAX_VALUE;
		for(double d : data)
			if (d < min)
				min = d;
		return min;
	}
}
