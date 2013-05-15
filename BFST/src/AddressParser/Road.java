package AddressParser;

import java.util.Arrays;
import java.util.Comparator;
import java.util.WeakHashMap;

import Graph.Edge;

public class Road
{
	public Edge[] edges;
	
	private String roadName;
	private int zipCode;
	
	/**
	 * Road class that is a container class for several edges that belongs to the same segment (road)
	 * @param the edges of the road
	 */
	public Road(Edge[] edges)
	{
		this.edges = edges;
		
		//All names and zipcodes of the edgeArray are equal as per design decision, so we can get the data from index 0
		roadName = edges[0].getName();
		zipCode = edges[0].getZip();
		
		//We sort our edge array by road number
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
	
	/**
	 * Get the name of the road
	 * @return returns the name of the road
	 */
	public String getName()
	{
		return roadName;
	}
	
	/**
	 * Get the zip code of the road
	 * @return the zip code of the road
	 */
	public int getZipCode()
	{
		return zipCode;
	}
	
	/**
	 * Get a specific edge in the road
	 * @param index
	 * @return returns the edge at index
	 */
	public Edge getEdge(int index)
	{
		return edges[index];
	}
	
	/**
	 * @return returns the edge array of the road
	 */
	public Edge[] getEdges()
	{
		return edges;
	}
	
	/**
	 * Get the adress of the road.
	 * If the road does not have a zipcode it is returned with its name
	 * otherwise the name and the zipcode is returned.
	 * @return returns the adress of the road
	 */
	public String getAddress()
	{
		if(getZipCode() != 0)
			return (getName()+", "+getZipCode());
		else
			return getName();
	}
	
	/**
	 * Check which edge in the road that contains the specified road number
	 * If there is no edge containing the number, the method will return
	 * the edge with the closest road numbers
	 * @param roadNumber is the road number that we search for on the road
	 * @param isProbability defines whether or not this is for a probability search
	 * @return Returns the index of the edge that contains, or is closest to containing the road number
	 */
	public int getEdgeWithRoadNumber(int roadNumber, boolean isProbability)
	{
		//We initialize the compare road number
		int lastToRight = -1;
		
		for(int i=0; i<edges.length; i++)
		{
			if(!(lastToRight+1 == edges[i].getFromRight()))
			{
				//If the current edge does not begin with the continuation of the road number we have reached
				//We see if the gap between the compare number and the next edge contains the road number 
				if(roadNumber >= lastToRight && roadNumber <= edges[i].getFromRight())
				{
					//And return the index
					return i;
				}
			}
			
			//Check the current edge to see if it holds the road number
			if(roadNumber >= edges[i].getFromRight() && roadNumber <= edges[i].getToRight())
				return i;
			
			//We set the compare number to the end of the current edge
			lastToRight = edges[i].getToRight();
			
			
			//If reached the end of the edge array and this is not a probability search, we return the last index of the array.
			if(i == edges.length-1 && !isProbability)
				return i;
		}
		
		//Otherwise we return an error-indicating number
		return -1;
	}
}
