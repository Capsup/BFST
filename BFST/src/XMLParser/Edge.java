package XMLParser;
public class Edge implements Comparable<Edge>
{
	private int fromNodeID;
	private int toNodeID;
	private int typ;
	private double length;
	
	public Edge(int fromNodeID, int toNodeID, double length, int typ)
	{
		this.fromNodeID = fromNodeID;
		this.toNodeID = toNodeID;
		this.length = length;
		this.typ = typ;
	}
	
	public int compareTo(Edge a){
		return getFromNodeID() == a.getFromNodeID() ? 0 : (getFromNodeID() > a.getFromNodeID() ? 1 : -1);
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
	
	public double getLength()
	{
		return length;
	}
}
