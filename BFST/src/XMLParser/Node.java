package XMLParser;
import java.awt.Dimension;
public class Node implements Comparable<Node>
{
	private int ARC;
	private int id;
	private int xCoord;
	private int yCoord;
	
	public Node(int ARC, int id, int xCoord, int yCoord)
	{
		this.ARC = ARC;
		this.id = id;
		this.xCoord = xCoord;
		this.yCoord = yCoord;
	}
	
	public int compareTo(Node a){
		return getID() == a.getID() ? 0 : (getID() > a.getID() ? 1 : -1);
	}
	
	//ARC
	public int getARC()
	{
		return ARC;
	}
	
	//ID
	public int getID()
	{
		return id;
	}
	
	//Coordinates
	public int getXCoord()
	{
		return xCoord;
	}
	
	public int getYCoord()
	{
		return yCoord;
	}
	
	public Dimension GetCoords()
	{
		return new Dimension(getXCoord(), getYCoord());
	}
}
