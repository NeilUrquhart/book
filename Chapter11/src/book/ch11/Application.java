package book.ch11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import book.ch1.LatLon;
import book.ch1.Visit;
import book.ch2.VRPVisit;
import book.ch7.export.ExportService;
import book.ch7.export.KMLWriter;
import book.ch7.routing.GraphHopper;
import book.ch7.routing.Journey;
import book.ch7.routing.RoutingEngine;
import book.ch8.food.FoodOSMHelper;
import book.ch8.food.FoodProperties;
import book.ch8.food.FoodVisit;
import book.ch8.food.FoodFacade.SaveTo;

/*
 * Written by Neil Urquhart - 2021
 * 
 * This class is a test application that demonstrates the use of SBR (Street based Routing)
 * 
 */
public class Application {
	public static void main(String[] args) {
		LatLon startEnd = new LatLon(55.919007,-3.212547);
		SBRIndividual chromo= new SBRIndividual(startEnd);
		/*
		 * Create a test network of streets and houses.
		 */
		chromo.addStreet(new StreetSection("Greenbank Crescent, Edinburgh",new int[]{1,3,5,7,9,11,13,15,17,19,21}, new int[]{2,4,6,8,10,12,14,16,18,20,22,24,26,28,30}, new LatLon(55.9192479, -3.2127621),new LatLon(55.9186391, -3.2157864))) ;
		chromo.addStreet(new StreetSection("Greenbank Crescent, Edinburgh",new int[]{39,41,43,45,47,49,51,53,55,57,59,61,63,65,67,69},new int[]{52,54,56,58,60,62,64,66,68,70,72,74,76,78,80,82}, new LatLon(55.9177482, -3.2166029), new LatLon(55.915377,-3.216826)));
		chromo.addStreet(new StreetSection("Greenbank Crescent, Edinburgh",new int[]{23,25,27,29,31,33,35,37}, new int[]{32,36,38,40,42,44,46,48,50}, new LatLon(55.9186391, -3.2157864),new LatLon(55.9177482, -3.2166029)));
		chromo.addStreet(new StreetSection("Greenbank Crescent, Edinburgh",new int[]{73,75,77,79,81,83}, new int[]{86,88,90,92,94,96,98},  new LatLon(55.915377,-3.216826),new LatLon(55.9143335, -3.2171333)));
		chromo.addStreet(new StreetSection("Greenbank Crescent, Edinburgh",new int[]{87,89,91,93,95,97,99}, new int[]{100,102,104,106,108,110},  new LatLon(55.9143335, -3.2171333),new LatLon(55.9133974, -3.2178461)));
		chromo.addStreet(new StreetSection("Greenbank Crescent, Edinburgh",new int[]{101,103,105,107,109,111,113}, new int[]{112,114,116,118,120,122},  new LatLon(55.9133974, -3.2178461),new LatLon(55.9124630, -3.2185262)));
		chromo.addStreet(new StreetSection("Greenbank Crescent, Edinburgh",new int[]{115,117}, new int[]{},  new LatLon(55.9124630, -3.2185262),new LatLon(55.9124630, -3.2185262)));
		chromo.addStreet(new StreetSection("Greenbank Road, Edinburgh",new int[] {161},new int[] {156,158,160,162},new LatLon( 55.9122774, -3.2196911),new LatLon(55.9124630, -3.2185262) ));
		chromo.addStreet(new StreetSection("Greenbank Road, Edinburgh",new int[] {121,123,125,127,129,131,133,135,137,139,141,143},new int[] {120,122,124,126,128,130,132,134,136,138,140,142,144,146,148,150,152,154},new LatLon( 55.9136208, -3.2213597),new LatLon( 55.9122774, -3.2196911) ));
		chromo.addStreet(new StreetSection("Greenbank Road, Edinburgh",new int[] {113,115,117,119},new int[] {108,110,112,114,116,118},new LatLon(55.9143594, -3.2216429),new LatLon( 55.9136208, -3.2213597) ));
		chromo.addStreet(new StreetSection("Greenbank Road, Edinburgh",new int[] {101,103,105,107,109},new int[] {94,96,98,100,102,104,106},new LatLon(55.915120,-3.221891),new LatLon(55.9143594, -3.2216429) ));
		chromo.addStreet(new StreetSection("Greenbank Road, Edinburgh",new int[] {91,93,95,97,99},new int[] {80,82,84,86,88,90,92},new LatLon(55.9159226, -3.2215544),new LatLon(55.915120,-3.221891) ));
		chromo.addStreet(new StreetSection("Greenbank Road, Edinburgh",new int[] {61,63,65,67,69,71,73,75,77,79,81,83,85,87,89},new int[] {48,50,52,54,56,58,60,62,64,66,68,70,72,74,76,78},new LatLon(55.9181204, -3.2210722),new LatLon(55.9159226, -3.2215544) ));
		chromo.addStreet(new StreetSection("Greenbank Road, Edinburgh",new int[] {21,23,25,27,29,31,33,35,37,39,41,43,45,47,49,51,53,55,57,59},new int[] {8,10,12,14,16,18,20,22,24,26,28,30,32,34,36,38,40,42,44,46},new LatLon(55.9186391, -3.2157864),new LatLon(55.9181204, -3.2210722)));
		chromo.addStreet(new StreetSection("Greenbank Park, Edinburgh",new  int[] {3,5,7,9,11,13,15,17,19,21,23,25,27,29,31,33}, new int[] {4,6,8,10,12,14,16,18,20,22,24,26,28,30,32,34,36},new LatLon(55.915120,-3.221891), new LatLon(55.915377,-3.216826))) ;
		chromo.addStreet(new StreetSection("Greenbank Grove, Edinburgh",new int[] {3,5,7,9,11,13,15,17,19,21,23,25,27,29,31},new int[] {4,6,8,10,12,14,16,18,20,22,24,26,28,30,32}, new LatLon(55.9143594, -3.2216429),new LatLon(55.9143335, -3.2171333)));
		chromo.addStreet(new StreetSection("Greenbank Row, Edinburgh",new int[] {3,5,7,9,11,13,15,17,19,21,23},new int[] {4,6,8,10,12,14,16,18,20,22},new LatLon(55.9136208, -3.2213597),new LatLon(55.9133974, -3.2178461) ));
		chromo.addStreet(new StreetSection("Greenbank Rise, Edinburgh",new int[] {},new int[] {2,4,8,10,12,14,16},new LatLon(55.9122774, -3.2196911),new LatLon(55.9122774, -3.2196911) ));
		chromo.addStreet(new StreetSection("Greenbank Lane, Edinburgh",new int[] {3,5,7,9,11},new int[] {2,4,6,8,10,12},new LatLon( 55.9189019, -3.2222129),new LatLon(55.9181204, -3.2210722) ));
		chromo.addStreet(new StreetSection("Greenbank Loan, Edinburgh",new int[] {5,7,9,11,13},new int[] {2,4,6,8,10,12,14,16,18},new LatLon(55.9177482, -3.2166029),new LatLon(55.9176417, -3.2186476) ));
		chromo.addStreet(new StreetSection("Greenbank Loan, Edinburgh",new int[] {15,17,19,21,23,25,27,29,31,33,35},new int[] {20,22,24,26,28,30,32,34,36,38,40,42,44,46,48},new LatLon(55.9176417, -3.2186476),new LatLon(55.9160627, -3.2199529) ));
		chromo.addStreet(new StreetSection("Greenbank Gardens, Edinburgh",new int[] {1,3,5,7},new int[] {2,4,6,8,10},new LatLon(55.9159226, -3.2215544),new LatLon(55.9160627, -3.2199529) ));
		chromo.addStreet(new StreetSection("Greenbank Gardens, Edinburgh",new int[] {9,11,13,15,17,19,21,23,25,27,29},new int[] {12,14,16,18,20,22,24,26,28,30,32,34,36,38,40,42,44},new LatLon(55.9160627, -3.2199529),new LatLon(55.9176417, -3.2186476) ));
		//Create EA
		SBR_EA sbr = new SBR_EA(startEnd, chromo);
		//Run EA
		sbr.runEA();
	}
}


