package XMLParser;


public class Node implements Comparable<Node>
{
	private float ARC;
	private int id;
	private double xCoord;
	private double yCoord;

	public Node( float ARC, int id, double xCoord, double yCoord )
	{
		this.ARC = ARC;
		this.id = id;
		this.xCoord = xCoord;
		this.yCoord = yCoord;
	}

	public int compareTo( Node a )
	{
		return getID() == a.getID() ? 0 : ( getID() > a.getID() ? 1 : -1 );
	}

	// ARC
	public float getARC()
	{
		return ARC;
	}

	// ID
	public float getID()
	{
		return id;
	}

	// Coordinates
	public double getXCoord()
	{
		return xCoord;
	}

	public double getYCoord()
	{
		return yCoord;
	}

}
