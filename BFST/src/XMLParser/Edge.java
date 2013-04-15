package XMLParser;

import java.util.ArrayList;

public class Edge implements Comparable<Edge>
{
	private int fromNodeID, toNodeID, typ;
	private double length;
	private double xTo, yTo, xFrom, yFrom;

	public Edge( ArrayList<String> s )
	{
		init(s);
	}
	
	protected void init(ArrayList<String> s){
		this.xFrom = Double.parseDouble(s.get(0));
		this.yFrom = Double.parseDouble(s.get(1));
		this.fromNodeID = Integer.parseInt(s.get(2));
		this.xTo = Double.parseDouble(s.get(3));
		this.yTo = Double.parseDouble(s.get(4));
		this.toNodeID = Integer.parseInt(s.get(5));
		this.typ = Integer.parseInt(s.get(6));
	}


	public int compareTo( Edge a )
	{
		return getXFrom() == a.getXFrom() ? 0 : ( getXFrom() > a.getXFrom() ? 1 : -1 );
	}

	public double getXTo()
	{
		return xTo;
	}

	public double getYTo()
	{
		return yTo;
	}

	public double getXFrom()
	{
		return xFrom;
	}

	public double getYFrom()
	{
		return yFrom;
	}

	public int getTyp()
	{
		return typ;
	}

	public int getFromNodeID()
	{
		return fromNodeID;
	}

	public int getToNodeID()
	{
		return toNodeID;
	}

	public double getLength()
	{
		return length;
	}
}
