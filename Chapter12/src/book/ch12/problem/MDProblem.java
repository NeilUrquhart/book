package book.ch12.problem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import book.ch1.LatLon;
import book.ch1.Visit;
import book.ch12.mapElites.Gene;
import book.ch9.routing.Journey;

/*
 * Neil Urquhart 2021
 * This Class creates a model of a MD problem
 * 
 * It is initalised from a CSV file
 * 
 */
public class MDProblem {
	/*
	 * Singleton
	 */
	private MDProblem(){ }//Blocked constructor
	private static MDProblem instance;

	public static MDProblem getInstance(){
		if (instance==null)
			instance = new MDProblem();
		return instance;
	}

	private  ArrayList<MicroDepot> microDepots = new ArrayList<MicroDepot>();
	private  ArrayList<Courier>couriers = new ArrayList<Courier>();
	private  ArrayList<Delivery> deliveries = new ArrayList<Delivery>();
	private  Courier primaryVehicle = null;
	private final int MAX_COURIERS =50;
	private  Random rnd = new Random();
	private String workingDir;

	public double getTotalWeight() {
		double res=0;
		for(Delivery d : deliveries) {
			res = res + d.getWeight();
		}
		return res;
	}
	public int getNoVehicleTypes() {
		return couriers.size();
	}

	public MicroDepot getMicroDepot(int idx) {
		return microDepots.get(idx);
	}

	public ArrayList<MicroDepot> getMicroDepots(){
		return microDepots;
	}

	public ArrayList<Courier> getVehicles(){
		return couriers;
	}

	public int getQtyDeliveries() {
		return deliveries.size();
	}

	public String getWorkingDir() {
		return workingDir;
	}

	public void setWorkingDir(String workingDir) {
		this.workingDir = workingDir;
	}

	public int getMAX_VEHICLES() {
		return MAX_COURIERS;
	}

	public MicroDepot randMicroDepot(){
		return microDepots.get(rnd.nextInt(microDepots.size()));
	}

	public Courier getRVVehicle(int idx){
		return couriers.get(idx);
	}

	public int getCapacity(String desc) {
		for(Courier v :couriers) {
			if (v.getDescription().equals(desc)) {
				return v.getCapacity();
			}
		}
		return 0;
	}

	public double getHighestPolluter(){
		double res = primaryVehicle.getEmissions();
		for(Courier v : couriers)
			if(v.getEmissions()>res){
				res = v.getEmissions();
			}
		return res;
	}

	public double getCheapestRun(){
		double res = primaryVehicle.getCostKM();
		for(Courier v : couriers)
			if(v.getCostKM()<res){
				res = v.getCostKM();
			}
		return res;
	}

	public double getCheapestFixed(){
		double res = primaryVehicle.getFixedCost();
		for(Courier v : couriers)
			if(v.getFixedCost()<res){
				res = v.getFixedCost();
			}
		return res;
	}

	public double getSlowest(){
		double res = primaryVehicle.getSpeed();
		for(Courier v : couriers)
			if(v.getSpeed()<res){
				res = v.getSpeed();
			}
		return res;
	}

	public double getQuickest(){
		double res = primaryVehicle.getSpeed();
		for(Courier v : couriers)
			if(v.getSpeed()>res){
				res = v.getSpeed();
			}
		return res;
	}

	public  Results evaluate(ArrayList<Gene> chromo, boolean silent, String name) {
		//Setup a plan based on the gene and evaluate it.
		Model m = new Model();
		primaryVehicle.clearDeliveries();
		m.addVehicle(primaryVehicle);
		ArrayList<Delivery> plan = new ArrayList<Delivery>();
		plan.addAll(deliveries);
		primaryVehicle.setDeliveries(plan);

		//Process Gene
		int vehicleID=0;
		for(Gene g : chromo){
			MicroDepot depot = g.getMicroDepot();
			vehicleID++;							
			//1. insert
			Courier v = (Courier)g.getCourier().clone();
			v.setID(vehicleID);
			v.clearDeliveries();
			SubTour rv = new SubTour(depot,v);
			LatLon  l = depot;
			boolean added=false;
			//2. Transfer deliveries
			//Visit current = l;
			for (int c=0; c < g.getDelCount(); c++){
				Delivery d = findClosest(l,plan,v.getType());
				if (d!=null) {
					if (v.addDelivery(d)){
						plan.remove(d);//Only remove, if transferred
						l = d;//.getLocation();
						added=true;
					}
				}
			}

			if (added) {
//				g.applicationPoint(findNearest(l,plan));//Update insert point
//				plan.add(g.getApplictionPoint() +1, rv);
				plan.add(findNearest(l,plan) +1, rv);
			}
		}
		return m.evaluate(silent,name);
	}
	private Delivery findClosest(LatLon l, ArrayList<Delivery> plan, String routeType) {
		Delivery res=null;
		double best = Double.MAX_VALUE;
		for (Delivery d : plan){
			if (!(d instanceof SubTour)){
				double dist = Router.getInstance().findRoute(l,d/*.getLocation()*/,routeType).getDistanceKM();
				if (dist < best){
					best =dist;
					res=d;
				}
			}
		}
		return res;
	}

	private int findNearest(LatLon l, ArrayList<Delivery> plan) {
		int res=0;
		double best = Double.MAX_VALUE;
		for (int c=0; c < plan.size()-1; c++){
			Visit x =plan.get(c);//.getLocation();
			Visit y = plan.get(c+1);//.getLocation();
			double dist = Router.getInstance().findRoute(x,l,"car").getDistanceKM() + Router.getInstance().findRoute(l,y,"car").getDistanceKM();
			if (dist < best){
				best =dist;
				res=c;
			}
		}
		return res;
	}

	public  void setup(File scenarioFile){
		readFile(scenarioFile);
		deliveries = this.NNSort(primaryVehicle.getStart(),deliveries,"car");		
	}

	public void addMicroDepot(MicroDepot l){
		microDepots.add(l);
	}

	public void addRVvehicles(Courier v){
		couriers.add(v);
	}
	public void addelivery(Delivery d){
		deliveries.add(d);
	}

	public void setPrimaryVehicle(Courier v){
		primaryVehicle = v;
	}

	public ArrayList<Delivery> NNSort(LatLon startP,ArrayList<Delivery> deliveries, String type){
		//System.out.println("NN sort");
		ArrayList<Delivery> result = new ArrayList<Delivery>();
		Delivery currentLoc = new Delivery(startP,"");
		double dist=0;
		double shortest = Double.MAX_VALUE;
		Delivery best=null;

		while (deliveries.size()> 0){
			best=null;
			shortest = Double.MAX_VALUE;

			for(Delivery d : deliveries){
				Journey j= Router.getInstance().findRoute(currentLoc/*.getLocation()*/, d/*.getLocation()*/,type);
				dist = j.getDistanceKM();
				if (dist < shortest){
					shortest = dist;
					best =d;
				}
			}
			currentLoc = best;
			deliveries.remove(best);
			result.add(best);
		}
		return result;
	}

	public int getRndInsertPointIndx() {
		return rnd.nextInt(deliveries.size());
	}

	public int getRndRVVehicleIndx() {
		return rnd.nextInt(couriers.size());
	}
	
	private void readFile(File file)
	{
		//Try Catch to stop the program crashing if there is an error
		try {
			//Open a new BufferedReader to prepare reading the CSV file
			BufferedReader br = new BufferedReader(new FileReader(file));
			//A variable to write each line to
			String readin = "";


			//An initial instance of the vehicle
			Courier myVan = new Courier("car");//fix
			
			//Used to hold a pointer to an RV object

			//A check for if the vehicle is the first vehicle in the CSV file
			boolean firstVehicle = true;
			//A check for if the details being read are deliveries

			//Continue reading the file for as long as there is content in it
			while ((readin = br.readLine()) != null)
			{
	
				//Split the read in line into an array on each comma
				String[] splitlines = readin.split(",");
				if(splitlines.length > 0)
				{
					//If the program reads the keyword "VEHICLE", create a new vehicle and get the properties for it
					if((splitlines[0].equals("VEHICLE"))||(splitlines[0].equals("RVVEHICLE")))
					{
						//Assign values from the CSV file to appropriate variables
						//Create a new vehicle if there is another vehicle in the CSV file
						if(firstVehicle == false)
						{
							myVan = new Courier(splitlines[9]);
						}
						//Add appropriate variables to the instance of the vehicle
						myVan.setStart(new Visit("Start",Double.parseDouble(splitlines[1]), Double.parseDouble(splitlines[2])));
						myVan.setEmissions(Double.parseDouble(splitlines[3]));
						myVan.setSpeed(Integer.parseInt(splitlines[4]));
						myVan.setCapacity(Integer.parseInt(splitlines[5]));
						myVan.setCostKM(Double.parseDouble(splitlines[6]));
						myVan.setFixedCost(Double.parseDouble(splitlines[7]));
						myVan.setDescription(splitlines[8]);
						//Add the vehicle to the simulation
						
						if (splitlines[0].equals("VEHICLE"))
							this.setPrimaryVehicle(myVan);
						else
							this.addRVvehicles(myVan);

						firstVehicle = false;

					}
					else if(splitlines[0].equals("DELIVERY"))
					{

						Delivery myDelivery = new Delivery(Double.parseDouble(splitlines[1]),Double.parseDouble(splitlines[2]),"Delivery");
						myDelivery.setWeight(Integer.parseInt(splitlines[3]));
						//isDelivery = true;
						this.addelivery(myDelivery);
						
					}else if(splitlines[0].equals("RVPOINT"))
					{
						System.out.println("Reading RV " + readin);
						MicroDepot d = new MicroDepot( Double.parseDouble(splitlines[1]), Double.parseDouble(splitlines[2]),splitlines[3]);
					this.addMicroDepot(d);
						
					}
				}
			} 			
			
			br.close();
		}catch(IOException e) {
			e.printStackTrace();
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Make sure all entries are valid in the CSV file");
		}
	}
}
