package book.ch4;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalTime;

import book.ch2.RandomSingleton;

public class TWGenerator {
	
	/*
	 * Neil Urquhart 2020
	 * 
	 * A simple class for generating lists of time windows
	 * 
	 */

	//Assume the day starts at 08:00 and ends 22:00
	private static int startH = 8;
	private static int endH = 22;
	private static  RandomSingleton rnd = RandomSingleton.getInstance();

	private static int winLen= 1;//Length of window

	public static void setWindowLen(int len) {
		winLen = len;
	}

	public static TimeWindow getTW() {
		//Return a random time window

		LocalTime start = LocalTime.of((rnd.getRnd().nextInt((endH-(winLen-1))-startH)+startH),0,0);
		LocalTime end = start.plusHours(winLen);
		return new TimeWindow(start,end);

	}

	public static LocalTime randomTime() {
		return  LocalTime.of(rnd.getRnd().nextInt(23),rnd.getRnd().nextInt(59),0);
	}

	public static void main(String[] args) {
		for (int t =1; t <= 14; t ++) {
			setWindowLen(t);
			String buffer = "";
			for (int i =0; i < 101; i ++) {
				TimeWindow tw = getTW();
				buffer = buffer +(tw.getStart().getHour() +"," +tw.getEnd().getHour() +"\n");
			}
			try {
				Files.write(Paths.get("./TW"+t+".csv"), buffer.getBytes());
			}catch(Exception e) {

			}

		}
	}

}
