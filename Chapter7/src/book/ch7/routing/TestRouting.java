package book.ch7.routing;

import book.ch1.Visit;

public class TestRouting {

	public static void main(String[] args) {
		OSMAccessHelper.setData( "./../../../../deliveryRoutes/ghData/","Frankfurt.osm");
		Visit start = new Visit("Start",50.107523,8.664159);
		Visit end = new Visit("End",50.110706, 8.683979);
		Journey j = OSMAccessHelper.getJourney(start,end, "car");
		System.out.println(j.getPointA());
		System.out.println(j.getPointB());
		System.out.println(j.getDistanceKM());
		System.out.println(j.getTravelTimeMS());
		System.out.println(j.getPath());
		
		
	}

}
