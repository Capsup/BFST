package XMLParser;
import java.awt.Dimension;
public class Node implements Comparable<Node>
{
	private float ARC;
	private int id;
	private float xCoord;
	private float yCoord;
	
	public Node(float ARC, int id, float xCoord, float yCoord)
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
	public float getARC()
	{
		return ARC;
	}
	
	//ID
	public float getID()
	{
		return id;
	}
	
	//Coordinates
	public float getXCoord()
	{
		return xCoord;
	}
	
	public float getYCoord()
	{
		return yCoord;
	}
	

}
