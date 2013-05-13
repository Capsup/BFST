package XMLParser;

import Graph.Edge;

public class Road implements Comparable<Road>
{
	public Edge[] edges;
	
	private String roadName;
	private String zipCode;
	
	public Road(Edge[] edges)
	{
		this.edges = edges;
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
	
	public String getZipCode()
	{
		return zipCode;
	}
	
}
