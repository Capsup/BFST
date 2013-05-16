package files;

import java.util.HashMap;

public class Node implements Comparable<Node>
{
	private static HashMap<Integer, Node> nodes = new HashMap<Integer, Node>();
	private int id;
	private double xCoord;
	private double yCoord;
	
	
	public static Node makeNode(int id, double xCoord, double yCoord){
		return nodes.put(id, new Node(id, xCoord, yCoord));
	}
	
	public static void nullNodes(){ nodes = null; }
		
	public static Node getNode(int id){ return nodes.get(id); }

	private Node( int id, double xCoord, double yCoord )
	{
		this.id = id;
		this.xCoord = xCoord;
		this.yCoord = yCoord;
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
