package Graph;

/**
 * This class represents Edges, which is road segments
 */
public class Edge implements Comparable<Edge>
{
	private static int id_count = 0;
	private final int id;
	private final int v;
	private final int w;

	private final int typ, zip, speedLimit;
	private final int toRight, fromRight, toLeft, fromLeft;
	private final double xTo, yTo, xFrom, yFrom, driveTime, length;
	private final String roadName, oneWay;
	
	/**
	 * Constructs a new Edge
	 * @param v the from reference
	 * @param w the to reference
	 * @param s An ArrayList<String> containing following in following order(xFrom, yFrom, v, xTo, yTo, w, length, type, roadName, fromLeft, toLeft, fromRight, toRight, zip, speedLimit, driveTime, oneWay)
	 */
	public Edge( int v, int w, java.util.ArrayList<String> s )
	{
		this.id = id_count++;
		this.v = v;
		this.w = w;

		this.xFrom = Double.parseDouble(s.get(0));
		this.yFrom = Double.parseDouble(s.get(1));
		this.xTo = Double.parseDouble(s.get(3));
		this.yTo = Double.parseDouble(s.get(4));
		this.length = Double.parseDouble(s.get(6));
		this.typ = Integer.parseInt(s.get(7));
		this.roadName = s.get(8);

		this.fromLeft = Integer.parseInt(s.get(9));
		this.toLeft = Integer.parseInt(s.get(10));
		this.fromRight = Integer.parseInt(s.get(11));
		this.toRight = Integer.parseInt(s.get(12));

		this.zip = Integer.parseInt(s.get(13));
		this.speedLimit = Integer.parseInt(s.get(14));
		this.driveTime = Double.parseDouble(s.get(15));
		this.oneWay = s.get(16);
		
	}

	/**
	 * Unique edge id
	 * @return id The id as an integer
	 */
	public int getId(){ return id; }
	
	/**
	 * Returns the weight based on Route.Settings.meansOfTransport and Route.Settings.routeProfile()
	 * @return weight The weight as a double
	 */
	public double weight(){
		if(Route.Settings.meansOfTransport() == Route.Settings.car)
			if(Route.Settings.routeProfile() == Route.Settings.fastest_route)
				return driveTime;
			else if(Route.Settings.routeProfile() == Route.Settings.shortest_route)
				return length;

		if(Route.Settings.meansOfTransport() == Route.Settings.bike)
			if(Route.Settings.routeProfile() == Route.Settings.fastest_route)
				return typ != 80 ? length/1000*18 : driveTime;
			else if(Route.Settings.routeProfile() == Route.Settings.shortest_route)
				return length;

		if(Route.Settings.meansOfTransport() == Route.Settings.foot)
			if(Route.Settings.routeProfile() == Route.Settings.fastest_route)
				return typ != 80 ? length/1000*5 : driveTime;
			else if(Route.Settings.routeProfile() == Route.Settings.shortest_route)
				return length;

		return length;
	}

	/**
	 * returns the from reference
	 * @return v as an integer
	 */
	public int either(){ return v; }
	
	/**
	 * If the from vertex is given, returns the to vertex and vice versa
	 * @param ve the vertex
	 * @return vertex as a int
	 */
	public int other(int ve){ return ve == w ? v : w ; }



	/**
	 * Checks if the edge contains a given road number
	 * @param i the road number you wish to check for
	 * @return true if the egde contains the given road number
	 */
	public boolean hasRoadNumber(int i){ return ((i <= toRight) && (i >= fromRight)) || ((i <= toLeft) && (i >= fromLeft)); }

	/** @return returns the lowest road numbers of the segment in the right side of the road */	public int getFromRight(){ return fromRight; }
	/** @return returns the highest road numbers of the segment in the right side of the road */	public int getToRight(){ return toRight;	}
	/** @return returns the lowest road numbers of the segment in the left side of the road */	public int getFromLeft(){ return fromLeft;	}
	/** @return returns the highest road numbers of the segment in the left side of the road */	public int getToLeft(){ return toLeft;	}
	/** @return returns the from vertex of the road segment */	public int getFromIndex(){ return v;	}
	/** @return returns to vertex of the road segment */	public int getToIndex(){ return w;	}
	/** @return returns the type of the road. goes from 1 to 99 */	public int getTyp() { return typ; }
	/** @return returns the zip code as an integer */	public int getZip() { return zip; }
	/** @return returns the x coordinate of the to point of the road segment as a double */	public double getXTo(){	return xTo; }
	/** @return returns the y coordinate of the to point of the road segment as a double */	public double getYTo(){	return yTo; }
	/** @return returns the x coordinate of the from point of the road segment as a double */	public double getXFrom(){ return xFrom;	}
	/** @return returns the y coordinate of the from point of the road segment as a double */	public double getYFrom(){ return yFrom;	}
	/** @return returns the length of the road segment */	public double getLength(){ return length; }
	/** @return returns the speed limit of the road segment */	public double getSpeedLimit(){ return speedLimit; }
	/** @return returns the time is takes to drive the road segment */	public double getDriveTime(){ return driveTime; }
	/** @return returns the road name of the edge */	public String getName(){ return roadName; }
	
	/** @return if the edge is oneWay ('' is no, 'tf' is from w to v, 'ft' is from v to w and 'n' in no driving */
	public String getOneWay(){ return oneWay; }
	
	/**
	 * @return the zip code as a string
	 */
	public String getZipString()
	{ 
		if(zip != 0) 
			return ""+zip; 
		else
			return "";
	}

	/**
	 * @param e the edge to compare with
	 * @return returns 
	 */
	public int compareTo(Edge e) 
	{
		String string1 = getAddress().toLowerCase();
		String string2 = e.getAddress().toLowerCase();

		if(string1.equals(string2))
			return 0;

		int length = string1.length();
		int index = 0;

		while(index < length && index < string2.length())
		{
			if(string1.charAt(index) == ' ' && string2.charAt(index) != ' ')
				return 1;
			else if(string1.charAt(index) != ' ' && string2.charAt(index) == ' ')
				return -1;

			if(string1.charAt(index) > string2.charAt(index))
			{
				return 1;
			}
			else if(string1.charAt(index) < string2.charAt(index))
			{
				return -1;
			}

			index++;
		}

		if(string1.length() > string2.length())
			return 1;
		else if(string2.length() > string1.length())
			return -1;

		return 0;
	}

	/**
	 * @return returns the address (road name + zip code) 
	 */
	public String getAddress()
	{
		if(getZip() != 0) return (getName()+", "+getZip());
		else return getName();
	}
}
