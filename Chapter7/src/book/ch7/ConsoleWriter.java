package book.ch7;

import java.io.File;
import java.util.ArrayList;

import book.ch1.Visit;

public class ConsoleWriter implements DataFileWriter {
	private int vCount=1;
	private StringBuffer buffer = new StringBuffer();
	@Override
	public void addPath(ArrayList<Visit> locs) {
		//Not used	
	}

	@Override
	public void addWayPoint(Visit v, String desc) {
		buffer.append(vCount +" - "+v.getName() +" ("+v.getX()+","+v.getY()+")\n");
		vCount++;
	}

	@Override
	public void addWayPoint(Visit v) {
		addWayPoint(v, v.getName());

	}

	@Override
	public void write(String file, String name) {
		System.out.println(name);
		System.out.println(buffer);
		buffer = new StringBuffer();
		

	}

}
