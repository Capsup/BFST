package XMLParser;
public class Edge 
{
	private int fromNodeID;
	private int toNodeID;
	private int typ;
	private int length;
	
	public Edge(int fromNodeID, int toNodeID, int length, int typ)
	{
		this.fromNodeID = fromNodeID;
		this.toNodeID = toNodeID;
		this.length = length;
		this.typ = typ;
	}
	
	public int getTyp(){
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
	
	public int getLength()
	{
		return length;
	}
}
