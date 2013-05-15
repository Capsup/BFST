package AddressParser;

import java.util.Arrays;
import java.util.Comparator;

import Graph.Edge;

public class Road implements Comparable<Road>
{
	public Edge[] edges;
	
	private String roadName;
	private int zipCode;
	
	public Road(Edge[] edges)
	{
		this.edges = edges;
		
		roadName = edges[0].getName();
		zipCode = edges[0].getZip();
		
		Arrays.sort(edges, new MyComparator());
	}
	
	public class MyComparator implements Comparator<Edge>
	{

		@Override
		public int compare(Edge edge1, Edge edge2) {
			
			if(edge1.getToRight() > edge2.getToRight())
				return 1;
			else if(edge1.getToRight() < edge2.getToRight())
				return -1;
			else
				return 0;
			
		}
		
	}

	@Override
	public int compareTo(Road arg0) {
		
		String string1 = getName().toLowerCase();
		String string2 = arg0.getName().toLowerCase();
		
		int length = string1.length();
		int index = 0;
		
		while(index < length && index < string2.length())
		{
			if(string1.charAt(index) < string2.charAt(index))
			{
				return 1;
			}
			else if(string1.charAt(index) > string2.charAt(index))
			{
				return -1;
			}
			
			index++;
		}
		
		if(string1.length() > string2.length())
			return 1;
		else if(string2.length() > string1.length())
			return -1;
		
		return 0;
		
	}
	
	public String getName()
	{
		return roadName;
	}
	
	public int getZipCode()
	{
		return zipCode;
	}
	
	public Edge getEdge(int index)
	{
		return edges[index];
	}
	
	public Edge[] getEdges()
	{
		return edges;
	}
	
	public String getAddress()
	{
		if(getZipCode() != 0)
			return (getName()+", "+getZipCode());
		else
			return getName();
	}
	
	public int getEdgeWithRoadNumber(int roadNumber, boolean isProbability)
	{
		int lastToRight = -1;
		
		//System.out.println("BEGIN");
		//System.out.println(roadName);
		//System.out.println(edges.length);
		
		for(int i=0; i<edges.length; i++)
		{
			//System.out.println("(0) Last To Right: "+lastToRight);
			//System.out.println("(0) Current from right: "+edges[i].getFromRight());
			
			if(!(lastToRight+1 == edges[i].getFromRight()))
			{
				//System.out.println("(2) Last To Right: "+lastToRight);
				//System.out.println("(2) Current from right: "+edges[i].getFromRight());
				
				if(roadNumber >= lastToRight && roadNumber <= edges[i].getFromRight())
				{
					//System.out.println(roadNumber);
					
					if(Math.abs(lastToRight-roadNumber) < Math.abs(edges[i].getFromRight()-roadNumber))
						return i;
					else
						return i;
				}
				
				lastToRight = edges[i].getToRight()-1;
			}
			
			//System.out.println("(1) Current from right: "+edges[i].getFromRight());
			//System.out.println("(1) Current to right: "+edges[i].getToRight());
			
			if(roadNumber >= edges[i].getFromRight() && roadNumber <= edges[i].getToRight())
					return i;
				//return new int[]{i, roadNumber};
			
			lastToRight = edges[i].getToRight();
			
			if(i == edges.length-1 && !isProbability)
				return i;
				//return new int[]{i, edges[i].getToRight()};
		}
		
		return -1;
	}
}
