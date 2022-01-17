package book.ch12.mapElites;

import book.ch12.problem.MDProblem;
import book.ch12.problem.Results;

/*
 * Neil Urquhart 2021
 * A Class for generating the keys used to define the buckets used
 * 
 */
public class KeyGen {
	private static int buckets=10;
	private static double highestEmissions;
	private static double lowestEmissions;

	private static double highestTime;
	private static double lowestTime;

	private static double highestMDs;
	private static double lowestMDs;

	private static double highestBikes;
	private static double lowestBikes;

	private static double highestWalks;
	private static double lowestWalks;

	private static double highestVans;
	private static double lowestVans;

	private static double highestByMD;
	private static double lowestByMD;
	
	public static String getKey(Results res){
	
		String key = "";
		//Emissions
		double em = (highestEmissions - lowestEmissions)/buckets;
		long emNorm = Math.round((res.getEmissions()-lowestEmissions)/em);
		if (emNorm > buckets) 
			emNorm = buckets;
		if (emNorm < 1)
			emNorm =1;

		//Time
		double tme = (highestTime - lowestTime)/buckets;
		long timeNorm = Math.round((res.getMaxTime()-lowestTime)/tme);
		if(timeNorm>buckets) 
			timeNorm = buckets;
		if(timeNorm<1)
			timeNorm=1;

		//mds
		//count MDS
		double microdepot = (highestMDs - lowestMDs)/buckets;
		long microdepotNorm = Math.round((res.getMicroDepots()-lowestMDs)/microdepot);
		if(microdepotNorm>buckets)
			microdepotNorm = buckets;
		if(microdepotNorm<1)
			microdepotNorm=1;

		double bikes = (highestBikes - lowestBikes)/buckets;
		long bikesNorm = Math.round((res.getBikes()-lowestBikes)/bikes);
		if(bikesNorm>buckets)
			bikesNorm = buckets;
		if(bikesNorm<1)
			bikesNorm=1;

		double walks = (highestWalks - lowestWalks)/buckets;
		long walksNorm = Math.round((res.getWalkers()-lowestMDs)/walks);
		if(walksNorm>buckets)
			walksNorm = buckets;
		if(walksNorm<1)
			walksNorm=1;

		double vans = (highestVans - lowestVans)/buckets;
		long vansNorm = Math.round((res.getEVans()-lowestVans)/vans);
		if(vansNorm>buckets)
			vansNorm = buckets;
		if(vansNorm<1)
			vansNorm=1;

		double byMD = (highestByMD - lowestByMD)/buckets;
		long byMDNorm = Math.round((res.getByMD()-lowestByMD)/byMD);
		if(byMDNorm>buckets)
			byMDNorm = buckets;
		if(byMDNorm<1)
			byMDNorm=1;

		key = emNorm  + ":" + microdepotNorm+":"  +byMDNorm+ ":"+bikesNorm+":"+walksNorm+":"+vansNorm +":" + timeNorm;
		return key;
	}
	
	 public static void initialise(){
		Chromosome ch = new Chromosome(0);
		Results baseLine = MDProblem.getInstance().evaluate(ch,true,"");

		double dist = baseLine.getDistance();
		highestEmissions = dist * MDProblem.getInstance().getHighestPolluter();
		lowestEmissions =0;//aim for 0!!

		highestTime = baseLine.getMaxTime()*2;
		lowestTime =baseLine.getMaxTime()/2;

		highestMDs = MDProblem.getInstance().getMicroDepots().size();
		lowestMDs =0;

		highestBikes = (MDProblem.getInstance().getTotalWeight()/MDProblem.getInstance().getCapacity("bike"));
		lowestBikes=0;

		highestWalks = (MDProblem.getInstance().getTotalWeight()/MDProblem.getInstance().getCapacity("walk"));
		lowestWalks=0;

		highestVans = (MDProblem.getInstance().getTotalWeight()/MDProblem.getInstance().getCapacity("electric van"));
		lowestVans=0;

		highestByMD = 100;
		lowestByMD = 0;
	}
}
