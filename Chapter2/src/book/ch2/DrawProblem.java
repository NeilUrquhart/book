package book.ch2;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import book.ch1.NearestNTSPSolver;
import book.ch1.TSPProblem;
import book.ch1.Visit;

/*
 * Neil Urquhart 2019
 * 
 * Produce an SVG file containing a problem layout
 */
public class DrawProblem {
	private static int SCALE =2;
	
	public static void main(String[] args) {
		VRPProblemFactory factory = new VRPProblemFactory();


		CVRPProblem problem = factory.buildProblem("./data/P-n20-k2.vrp");
		NearestNTSPSolver nn = new NearestNTSPSolver();
		((TSPProblem)problem).solve(nn);
		writeSVG((TSPProblem)problem,"P-n20-k2-TSP.svg");
		//problem.solve(new GrandTour());
		//writeSVG(problem,"P-n76-k4.GT-final.svg");
		//problem.solve(new ClarkeWright());
		//writeSVG(problem,"P-n76-k4.CW-final.svg");

	}
	
	public static void writeSVG(TSPProblem problem, String fName) {//TSP 
		System.out.println();
		String svg = "";
		svg = "<svg xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\" width=\"200\" height=\"200\"> \n";
		Visit v  = problem.getStart();
		svg = svg + "<rect x=\" "+(v.getLat()*SCALE)+" \" y=\" "+(v.getLon()*SCALE)+" \" height=\"8\" width=\"8\" fill=\"red\"/>\n";
		
		for (Object o : problem.getSolution()) {
			v = (Visit) o;
			svg = svg + "<circle cx=\" "+(v.getLat()*SCALE)+" \" cy=\" "+(v.getLon()*SCALE)+" \" r=\"2\" fill=\"black\"/>\n";
		}
		
		if (problem.getSolution()!= null) {
				Visit previous = problem.getStart();
				for (Visit current : problem.getSolution()) {
						svg = svg + "<line x1=\" "+(previous.getLat()*SCALE)+" \" y1=\" "+(previous.getLon()*SCALE)+" \" x2=\" "+(current.getLat()*SCALE)+" \" y2=\" "+(current.getLon()*SCALE)+" \" style=\"stroke:rgb(0,0,0);stroke-width:1\" />\n" ;
						previous = current;
				}
				svg = svg +"<line x1=\" "+(previous.getLat()*SCALE)+" \" y1=\" "+(previous.getLon()*SCALE)+" \" x2=\" "+(problem.getStart().getLat()*SCALE)+" \" y2=\" "+(problem.getStart().getLon()*SCALE)+" \" style=\"stroke:rgb(0,0,0);stroke-width:1\" />\n" ;
			
		}
		svg = svg +"</svg>";
		//write to file
		
		
        try (FileWriter writer = new FileWriter(fName );
              BufferedWriter bw = new BufferedWriter(writer)) {

            bw.write(svg);

        } catch (IOException e) {
            e.printStackTrace();
        }

		
	}
	
	public static void writeSVG(CVRPProblem problem, String fName) {//CVRP
		System.out.println();
		String svg = "";
		svg = "<svg xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\" width=\"200\" height=\"200\"> \n";
		Visit v  = problem.getStart();
		svg = svg + "<rect x=\" "+(v.getLat()*SCALE)+" \" y=\" "+(v.getLon()*SCALE)+" \" height=\"8\" width=\"8\" fill=\"red\"/>\n";
		
		for (Object o : problem.getVisits()) {
			v = (Visit) o;
			svg = svg + "<circle cx=\" "+(v.getLat()*SCALE)+" \" cy=\" "+(v.getLon()*SCALE)+" \" r=\"2\" fill=\"black\"/>\n";
		}
		
		if (problem.getSolution()!= null) {
			for (ArrayList<VRPVisit> r: problem.getCVRPSolution() ) {
				Visit previous = problem.getStart();
				for (Visit current : r) {
						svg = svg + "<line x1=\" "+(previous.getLat()*SCALE)+" \" y1=\" "+(previous.getLon()*SCALE)+" \" x2=\" "+(current.getLat()*SCALE)+" \" y2=\" "+(current.getLon()*SCALE)+" \" style=\"stroke:rgb(0,0,0);stroke-width:1\" />\n" ;
						previous = current;
				}
				svg = svg +"<line x1=\" "+(previous.getLat()*SCALE)+" \" y1=\" "+(previous.getLon()*SCALE)+" \" x2=\" "+(problem.getStart().getLat()*SCALE)+" \" y2=\" "+(problem.getStart().getLon()*SCALE)+" \" style=\"stroke:rgb(0,0,0);stroke-width:1\" />\n" ;
			}
		}
		svg = svg +"</svg>";
		//write to file
		
		
        try (FileWriter writer = new FileWriter(fName );
              BufferedWriter bw = new BufferedWriter(writer)) {

            bw.write(svg);

        } catch (IOException e) {
            e.printStackTrace();
        }		
	}

}
