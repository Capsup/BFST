package Route;
public class Settings {
	//Route profile
	public static final int fastest_route = 0;
	public static final int shortest_route = 1;
	private static int routeProfile = fastest_route;
	
	public static void setRouteProfile(int i){ routeProfile = (i < 2 && i >= 0) ? i : 0; }	
	public static int routeProfile(){ return routeProfile; }
	
	//Means of transportation
	public static final int car = 0;
	public static final int bike = 1;
	public static final int foot = 2;
	private static int meansOfTransport = car;
	
	public static void setMeansOfTransport(int i){ meansOfTransport = (i < 3 && i >= 0) ? i : car; }	
	public static int meansOfTransport(){ return meansOfTransport; }
	
}
