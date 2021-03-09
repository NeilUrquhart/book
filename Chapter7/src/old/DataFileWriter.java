package old;

import java.io.File;
import java.util.ArrayList;
import book.ch1.Visit;

public interface DataFileWriter {
	void addPath(ArrayList<Visit> locs);
	void addWayPoint(Visit v, String desc);
	void addWayPoint(Visit v);
	void write(String file, String name);
}