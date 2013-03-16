public class Edge 
{
	private int fromNodeID;
	private int toNodeID;
	
	private int length;
	
	public Edge(int fromNodeID, int toNodeID, int length)
	{
		this.fromNodeID = fromNodeID;
		this.toNodeID = toNodeID;
		this.length = length;
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
