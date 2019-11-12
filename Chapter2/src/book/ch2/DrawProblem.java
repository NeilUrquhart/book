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
		CVRPProblem problem = factory.buildProblem("./data/P-n76-k4.vrp");
		//NearestNTSPSolver nn = new NearestNTSPSolver();
		//((TSPProblem)problem).solve(nn);
		//problem.solve(new GrandTour());
		//problem.solve(new ClarkeWright());
		writeSVG(problem,"P-n76-k4.svg");
	}
	
	private static void writeSVG(CVRPProblem problem, String fName) {
		System.out.println();
		String svg = "";
		svg = "<svg xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\" width=\"200\" height=\"200\"> \n";
		Visit v  = problem.getStart();
		svg = svg + "<rect x=\" "+(v.x*SCALE)+" \" y=\" "+(v.y*SCALE)+" \" height=\"8\" width=\"8\" fill=\"red\"/>\n";
		
		for (Object o : problem.getVisits()) {
			v = (Visit) o;
			svg = svg + "<circle cx=\" "+(v.x*SCALE)+" \" cy=\" "+(v.y*SCALE)+" \" r=\"2\" fill=\"black\"/>\n";
		}
		
		if (problem.getSolution()!= null) {
			for (ArrayList<VRPVisit> r: problem.getCVRPSolution() ) {
				Visit previous = problem.getStart();
				for (Visit current : r) {
						svg = svg + "<line x1=\" "+(previous.x*SCALE)+" \" y1=\" "+(previous.y*SCALE)+" \" x2=\" "+(current.x*SCALE)+" \" y2=\" "+(current.y*SCALE)+" \" style=\"stroke:rgb(0,0,0);stroke-width:1\" />\n" ;
						previous = current;
				}
				svg = svg +"<line x1=\" "+(previous.x*SCALE)+" \" y1=\" "+(previous.y*SCALE)+" \" x2=\" "+(problem.getStart().x*SCALE)+" \" y2=\" "+(problem.getStart().y*SCALE)+" \" style=\"stroke:rgb(0,0,0);stroke-width:1\" />\n" ;
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
