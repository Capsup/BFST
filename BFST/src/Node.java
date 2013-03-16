import java.awt.Dimension;


public class Node 
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
