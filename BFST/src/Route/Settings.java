package Route;

/**
 * This class holds sittings for Means Of Transportation, Route profile and wheather or not ferrys are allowed
 */
public class Settings {
	
	/**
	 * This is used for selecting fastest route for setRouteProfile()
	 */
	public static final int fastest_route = 0;
	
	/**
	 * This is used for selecting shortest route for setRouteProfile()
	 */
	public static final int shortest_route = 1;
	
	private static int routeProfile = fastest_route;
	
	/**
	 * Setting route profile
	 * @param i shortest_route or fastest_route
	 */
	public static void setRouteProfile(int i){ routeProfile = (i < 2 && i >= 0) ? i : fastest_route; }	
	
	/**
	 * Getting information about which route profile is enabled
	 * @return an integer matching shortest_route or fastest_route
	 */
	public static int routeProfile(){ return routeProfile; }
	
	//Means of transportation
	
	/**
	 * This is used for selecting car for meansOfTransport()
	 */
	public static final int car = 0;
	
	/**
	 * This is used for selecting bike for meansOfTransport()
	 */
	public static final int bike = 1;
	
	/**
	 * This is used for selecting foot for meansOfTransport()
	 */
	public static final int foot = 2;
	
	private static int meansOfTransport = car;
	
	private static int ferry_allowed = 0;
	
	/**
	 * This is used for selecting if ferries are allowed for setFerryAllowed()
	 */
	public static final int yes = 0;
	
	/**
	 * This is used for selecting if ferries are allowed for setFerryAllowed()
	 */
	public static final int no = 1;
	
	/**
	 * This is used for selecting if ferries are allowed
	 * @param i integer
	 */
	public static void setFerryAllowed(int i){ ferry_allowed = (i < 2 && i >= 0) ? i : yes; }
	
	/**
	 * Getting information about which ferries are allowed
	 * @return an integer matching yes or no
	 */
	public static int ferryAllowed(){ return ferry_allowed; }
	
	/**
	 * This is used for selecting meansOfTransportation
	 * @param i integer
	 */
	public static void setMeansOfTransport(int i){ meansOfTransport = (i < 3 && i >= 0) ? i : car; }
	
	/**
	 * Getting information about meansOfTransport
	 * @return an integer matching car, bike or foot
	 */
	public static int meansOfTransport(){ return meansOfTransport; }
}
