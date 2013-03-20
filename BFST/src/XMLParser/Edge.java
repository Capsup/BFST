package XMLParser;
public class Edge implements Comparable<Edge>
{
	private int fromNodeID, toNodeID, typ;
	private double length;
	private double xTo, yTo, xFrom, yFrom;
	
	public Edge(int fromNodeID, int toNodeID, double length, int typ, double xFrom, double yFrom, double xTo, double yTo)
	{
		this.fromNodeID = fromNodeID;
		this.toNodeID = toNodeID;
		this.length = length;
		this.typ = typ;
		this.xFrom = xFrom;
		this.yFrom = yFrom;
		this.xTo = xTo;
		this.yTo = yTo;
	}
	
	/*
	public int compareTo(Edge a){
		return getFromNodeID() == a.getFromNodeID() ? 0 : (getFromNodeID() > a.getFromNodeID() ? 1 : -1);
	}
	*/
	
	public int compareTo(Edge a){
		return getXFrom() == a.getXFrom() ? 0 : (getXFrom() > a.getXFrom() ? 1 : -1);
	}
	
	public double getXTo(){return xTo;}
	public double getYTo(){return yTo;}
	public double getXFrom(){return xFrom;}
	public double getYFrom(){return yFrom;}

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
