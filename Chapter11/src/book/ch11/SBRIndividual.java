package book.ch11;

import java.util.ArrayList;
import java.util.Random;
import book.ch1.LatLon;
import book.ch9.export.ExportService;
import book.ch9.export.KMLWriter;
import book.ch9.routing.Journey;

/*
 * Neil Urquhart 2021
 * 
 * This class represents an individual within the SBT
 * 
 */

public class SBRIndividual {

	private ArrayList<StreetSection> chromosome;
	private LatLon startEnd;
	private ArrayList<ArrayList<House>> phenotype = new ArrayList<ArrayList<House>>();
	private Random rnd = new Random();
	private static int totalHouses=0;
	private double dist;

	public SBRIndividual(LatLon startEnd) {
		chromosome = new ArrayList<StreetSection>();

		this.startEnd = startEnd;
	}

	public SBRIndividual(LatLon startEnd, SBRIndividual parent) {
		this(startEnd);
		chromosome.addAll(parent.getChromosome());
	}

	public SBRIndividual( SBRIndividual p1, SBRIndividual p2) {
		this(p1.startEnd);
		//Initialise this chromosome based on p1 and p2.

		//Copy from p1
		int p1Count =0;
		int p2Count=0;

		//Add from P1
		StreetSection current = p1.getChromosome().get(p1Count);
		this.add(current);
		p1Count++;
		StreetSection next = p1.getChromosome().get(p1Count);
		while(current.linked(next)) {
			this.add(next);
			p1Count++;
			if (p1Count >= p1.getChromosome().size())
				break;
			current = next;
			next = p1.getChromosome().get(p1Count);
		}
		//Add from P2
		while (chromosome.size() < p2.getChromosome().size()) {
			current = p2.getChromosome().get(p2Count);
			this.add(current);
			p2Count++;
		}
	}

	public ArrayList<StreetSection> getChromosome(){
		return chromosome;
	}
	
	public boolean add(StreetSection s) {
		int c = count(s);
		if (c==2)
			return false;
		if ( c==1)
			if (!s.twoSides())
				return false;
		return chromosome.add(s);
	}

	public int count(StreetSection s) {
		int res=0;
		for (StreetSection st : this.chromosome) {
			if (st==s)
				res++;
			if(res==2) return res;//Only needs to count to 2
		}
		return res;
	}
	public boolean addStreet(StreetSection s) {
		chromosome.add(s);
		if ((s.getEvens().size()>0)&&(s.getOdds().size()>0))
			chromosome.add(s);
		totalHouses = totalHouses + s.getAll().size();
		return false;
	}

	public void randomise() {
		for (int c=0; c < 100; c++)
			this.mutate();
	}

	public void mutate() {
		this.phenotype.clear();
		double ch = rnd.nextDouble(); 
		if ( ch <0.5) {
			//Rand move
			StreetSection s = chromosome.remove(rnd.nextInt(chromosome.size()));
			chromosome.add(rnd.nextInt(chromosome.size()),s);
		}else if (ch < 0.8) { //Bring together 2 sections with a common junction
			int pos = rnd.nextInt(chromosome.size());
			StreetSection s = chromosome.get(pos);
			StreetSection chosen = null;
			for (StreetSection poss : chromosome) {
				if (poss != s) {
					if (s.linked(poss)) {
						chosen = poss;
						break;
					}
				}
			}
			if (chosen != null) {
				chromosome.remove(chosen);
				pos = chromosome.indexOf(s);
				chromosome.add(pos+1 ,chosen);
			}
		}

		else{
			//Bring together 2 sides
			StreetSection s = chromosome.remove(rnd.nextInt(chromosome.size()));
			int pos = chromosome.indexOf(s);
			if (pos > -1) {
				chromosome.add(pos,s);
			}else
				chromosome.add(rnd.nextInt(chromosome.size()),s);
		}
	}

	public double  evaluate() {
		if (phenotype.size()>0)
			return dist;
		//Process Chromo
		dist=0;
		int count =0;
		StreetSection prev = null;
		LatLon prevJ=null;
		StreetSection next = null;
		LatLon nextJ = null;
		LatLon lastP = startEnd;
		LatLon nextP = null;
		int remainingDeliveries = this.totalHouses;

		while(count < chromosome.size()) {
			boolean bothSides = false;
			StreetSection current =  chromosome.get(count);
			if ( count < chromosome.size()-1) {
				if (chromosome.get(count+1).equals(current)) {
					bothSides = true;
					count++;
				}
			}
			if ( count < (chromosome.size()-1)) {
				next = chromosome.get(count+1); 
			}else
				next = null;
			ArrayList<House> houses = new ArrayList<House>();
			LatLon[] junctions = current.applyPattern(startEnd,bothSides,prev,next,houses);
			nextP = houses.get(0).getLocation();
			LatLon currentJ = junctions[0];
			if ((prevJ!=null)&&(currentJ!=null)&&(!prevJ.same(currentJ)))
				dist = dist + (RoutingFactory.getInstance().getJourneyDist(lastP,nextP,"foot")*remainingDeliveries);
			prevJ = junctions[1];
			remainingDeliveries = (remainingDeliveries - houses.size());
			lastP = houses.get(houses.size()-1).getLocation();
			phenotype.add(houses);
			prev = current;
			count ++;
		}
		dist = dist +RoutingFactory.getInstance().getJourneyDist(startEnd,phenotype.get(0).get(0).getLocation(),"foot");
		dist = dist +RoutingFactory.getInstance().getJourneyDist(lastP,startEnd,"foot");
		return dist;
	}

	public void save(int id){
		ExportService out = new KMLWriter();
		int c=1;
		LatLon prev = startEnd;
		ArrayList<LatLon> points = new ArrayList<LatLon>();

		for(ArrayList<House> street : phenotype ) {
			if (street.size() >0) {
				Journey j = RoutingFactory.getInstance().getJourney(prev, street.get(0).getLocation(), "foot");
				points.addAll(j.getPath());
				for (House h : street) {
					out.addWaypoint(h.getLocation(), ""+c, "");
					points.add(h.getLocation());
					c++;
				}
				prev = points.get(points.size()-1);
				out.addTrack(points);
			}
		}
		Journey j = RoutingFactory.getInstance().getJourney(prev, startEnd, "foot");
		out.addTrack(j.getPath());
		out.write(id+"-"+this.evaluate(),".kml");
	}

	public double  getDistance() {
		double len=0;
		LatLon prev = startEnd;
		for (ArrayList<House> st: phenotype) {
			for (House h :st) {
				len = len + +RoutingFactory.getInstance().getJourneyDist(prev,h.getLocation(),"foot");
				prev = h.getLocation();
			}
		}
		len = len + +RoutingFactory.getInstance().getJourneyDist(prev,startEnd,"foot");
		return len;
	}
}
