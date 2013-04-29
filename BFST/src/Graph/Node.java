package Graph;

import java.util.ArrayList;

public class Node implements Comparable<Node>
{
	private int id;
	private double xCoord;
	private double yCoord;

	public Node( ArrayList<String> s )
	{
		init(s);
	}
	
	protected void init(ArrayList<String> s){
		this.id = Integer.parseInt(s.get(0));
		this.xCoord = Double.parseDouble(s.get(1));
		this.yCoord = Double.parseDouble(s.get(2));
	}
	
	public int compareTo( Node a )
	{
		return getID() == a.getID() ? 0 : ( getID() > a.getID() ? 1 : -1 );
	}

	// ID
	public int getID()
	{
		return id;
	}

	// Coordinates
	public double getX()
	{
		return xCoord;
	}

	public double getY()
	{
		return yCoord;
	}

}
