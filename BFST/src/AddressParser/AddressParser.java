package AddressParser;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import Graph.Edge;
import QuadTree.Interval;
import QuadTree.Interval2D;
import XMLParser.XMLParser;


public class AddressParser
{
	private String[] arrayStrings;
	Interval2D<Double> interval = new Interval2D<Double>(
			new Interval<Double>(new Double(0.0), new Double(700200050.98297)), 
			new Interval<Double>(new Double(0.0), new Double(705000527.51786))
			);

	private static AddressParser instance;
	
	//private Edge[] roads;
	private Road[] roads;
	
	
	public class NaughtyException extends Exception
	{
		public NaughtyException( String sMessage )
		{
			super( sMessage );
		}
	}

	private AddressParser()
	{
		instance = this;
		
		ArrayList<List<Edge>> edgeList = XMLParser.getEdgeList();
		ArrayList<Edge> list = new ArrayList<Edge>();
		
		for(int i=0; i<edgeList.size(); i++)
		{
			Iterator<Edge> it = DataProcessing.Query.getInstance().queryEdges(interval, i).iterator();
			
			while(it.hasNext())
			{
				Edge edge = it.next();
				
				if(!edge.getName().equals(""))
					list.add(edge);
			}
		}
		
		Edge[] edges = new Edge[list.size()];
		list.toArray(edges);
		list.clear();
		
		Arrays.sort(edges);
		
		ArrayList<Road> roadList = new ArrayList<Road>();
		ArrayList<Edge> currEdgeList = new ArrayList<Edge>();
		
		String roadName = edges[0].getName();
		int zipCode = edges[0].getZip();
		
		for(int i=0; i<edges.length; i++)
		{
			if(roadName.equals(edges[i].getName()) && zipCode == edges[i].getZip())
			{
				currEdgeList.add(edges[i]);
			}
			else
			{
				roadName = edges[i].getName();
				zipCode = edges[i].getZip();
				
				Edge[] edgeArray = new Edge[currEdgeList.size()];
				currEdgeList.toArray(edgeArray);
				
				//System.out.println(edgeArray.length);
				
				roadList.add(new Road(edgeArray));
				
				currEdgeList = new ArrayList<Edge>();
				
				currEdgeList.add(edges[i]);
			}
		}
		
		roads = new Road[roadList.size()];
		roadList.toArray(roads);
	}
	
	public static AddressParser getInstance()
	{
		if(instance == null)
			new AddressParser();
		
		return instance;
	}
	

	public static void main( String[] args )
	{
		String address = "Rued Langgaards Vej 75, 7. sal, 2630";

		AddressParser adressParser = new AddressParser();

		try
		{
			adressParser.parseAddress( address );
		}
		catch( NaughtyException e )
		{
			System.out.println( e.getMessage() );
		}
	}

	public void loadData( String sPath ) throws NaughtyException
	{
		if( getClass().getResource( sPath ) != null )
		{
			Scanner scanner = new Scanner( getClass().getResourceAsStream( sPath ) );
			int i = 0;

			while( scanner.hasNextLine() )
			{
				scanner.nextLine();
				i++;
			}

			arrayStrings = new String[i];
			scanner = new Scanner( getClass().getResourceAsStream( sPath ) );

			while( scanner.hasNextLine() )
			{
				arrayStrings[--i] = scanner.nextLine();
			}
			
			Arrays.sort(arrayStrings);
		}
		else
		{
			throw new NaughtyException( "Road data could not be loaded." );
		}
	}

	public String[] parseAddress( String sAddress ) throws NaughtyException
	{
		String[] finalStrings = new String[6];
		String[] addressStrings = sAddress.split( " " );

		int iProcessed = 0;

		for( int i = 0; i < finalStrings.length; i++ )
		{
			finalStrings[i] = "";
		}

		for( ; iProcessed < addressStrings.length; iProcessed++ )
		{
			String curString = addressStrings[iProcessed];
			if( curString.matches( "[\\xC5\\xC6\\xD8\\xE5\\xE6\\xF8\\xC9\\xE9\\xC4\\xE4\\xD6\\xF6\\xDC\\xFC\\xC8\\xE8a-zA-Z,.]*" )
			        && !curString.equals( "i" ) )
				if( curString.contains( "," ) )
				{
					finalStrings[0] += ( iProcessed == 0 ? "" : " " ) + curString.replaceAll( ",", "" );
					iProcessed++;
					break;
				}
				else
					finalStrings[0] += ( iProcessed == 0 ? "" : " " ) + curString;
			else
				break;
		}
		/*
		if( finalStrings[0].equals( "" ) )
		{
			throw new NaughtyException( "OMG, WAT R U DOIN !?!?!" );
		}*/
		
		/*
		boolean bExists = false;
		int lineIndex = 0;
		for( String address : arrayStrings )
		{
			lineIndex++;
			if( finalStrings[0].equalsIgnoreCase( address ) )
			{
				bExists = true;
				break;
			}
		}
		*/
		/*
		if( !bExists )
		{
			System.out.println( "" + finalStrings[0] );
			throw new NaughtyException( "FUUUUUUUU Y U NO PROPER ADDRESS!?!??!?!" );
		}*/

		for( ; iProcessed < addressStrings.length; iProcessed++ )
		{
			addressStrings[iProcessed] = addressStrings[iProcessed].replace( ",", "" );
			if( addressStrings[iProcessed].contentEquals( "sal" ) || addressStrings[iProcessed].contentEquals( "i" ) )
				continue;

			if( addressStrings[iProcessed].matches( "[0-9]{1,3}" ) && finalStrings[1].equals( "" ) )
				finalStrings[1] = addressStrings[iProcessed];

			else if( addressStrings[iProcessed].matches( "[0-9]{1,3}[a-zA-Z]" ) )
			{
				if( addressStrings[iProcessed].matches( "\\d?\\w+" ) ) // MAGIC HAS BEEN APPLIED
				{
					for( int i = 0; i < addressStrings[iProcessed].length(); i++ )
					{
						if( Character.toString( addressStrings[iProcessed].charAt( i ) ).matches( "[a-zA-Z]" ) )
						{
							finalStrings[1] = addressStrings[iProcessed].substring( 0, i );
							finalStrings[2] = addressStrings[iProcessed].substring( i, addressStrings[iProcessed].length() );
							break;
						}
					}
				}
				else
					finalStrings[2] = addressStrings[iProcessed];
			}
			else if( addressStrings[iProcessed].contains( "." ) || addressStrings[iProcessed].matches( "[0-9]{1,3}[a-zA-Z]?" ) )
				finalStrings[3] = addressStrings[iProcessed].replace( ".", "" );
			else if( addressStrings[iProcessed].matches( "[0-9]{4,}" ) )
				finalStrings[4] = addressStrings[iProcessed];
			else if( addressStrings[iProcessed].matches( "[\\xC5\\xC6\\xD8\\xE5\\xE6\\xF8\\xC9\\xE9\\xC4\\xE4\\xD6\\xF6\\xDC\\xFC\\xC8\\xE8a-zA-Z]*" ) )
				finalStrings[5] += ( !finalStrings[5].equals( "" ) ? " " : "" ) + addressStrings[iProcessed];
		}

		//for( int i = 0; i < finalStrings.length; i++ )
		//	System.out.println( finalStrings[i] + "#" );

		//System.out.println( "Address was found at line " + lineIndex );

		return finalStrings;
	}
	
	/*
	public int[] search(String string) 
	{
		int[] result = new int[2];
		
		result[0] = -1;
		
        for(int i=0; i<roads.length; i++)
        {
        	result[0] = search(string, roads[i]);
        	
        	if(result[0] >= 0)
        	{
        		result[1] = i;
        		return result;
        	}
        }
        
        return new int[]{-1,-1};
	}
	*/
	
	public int[] search(String[] stringArray) 
	{
		Road[] a = roads;
		
		for(int i=0; i<stringArray.length; i++)
			System.out.println("Index "+i+": "+stringArray[i]);
		
        int lo = 0;
        int hi = a.length - 1;
        
        String string1 = stringArray[0];
        
        while (lo <= hi) 
        {
            
        	int mid = lo + (hi - lo) / 2;
        	
        	String string2 = a[mid].getName();
	        
            if      (compare(string1, string2) == -1) hi = mid - 1;
            else if (compare(string1, string2) == 1) lo = mid + 1;
            else
            {
            	if(!stringArray[4].equals(""))
            	{
            		int index = mid;
            		boolean isFound = false;
            		
            		while(roads[mid].getName().equals(roads[index].getName()) && !isFound)
            		{
            			if((roads[index].getZipCode()+"").equals(stringArray[4]))
        				{
            				mid = index;
        					isFound = true;
        					
        				}
        				
        				index++;
            		}
            		
            		index = mid-1;
            		
            		while(roads[mid].getName().equals(roads[index].getName()) && !isFound)
            		{
            			if((roads[index].getZipCode()+"").equals(stringArray[4]))
        				{
        					mid = index;
        					isFound = true;
        				}
        				
        				index--;
            		}
            		
            		if(!isFound)
            		{
                		System.out.println("(1)");
            			return new int[]{-1};
            		}
            	}
            	
            	int roadNumber = 0;
            	
            	if(!stringArray[1].equals(""))
            	{
            		roadNumber = Integer.parseInt(stringArray[1]);
            	}
            	
        		int indexWithNumber = a[mid].getEdgeWithRoadNumber(roadNumber, false);
        		
        		if(indexWithNumber >= 0)
        			return new int[]{mid,indexWithNumber};
        		
            	
            	return new int[]{mid,0};
            }
        }
        
		System.out.println("(3)");
        return new int[]{-1};
    }
	
	public int[][] probabilitySearch(String[] stringArray, int count) 
	{
		Road[] a = roads;
		
		String roadName = stringArray[0];
		
        int lo = 0;
        int hi = a.length - 1;

        String string1 = roadName;
        
        int mid = -1;
        
        while(lo <= hi)
        {
        	int searchIndex = lo + (hi - lo) / 2;
        	
        	String string2 = a[searchIndex].getName();
        	
        	if      (compare(string1, string2) == -1) hi = searchIndex - 1;
            else if (compare(string1, string2) == 1) lo = searchIndex + 1;
            else{ mid = searchIndex; break; }
        }
        
        if(mid == -1)
        {
	        lo = 0;
	        hi = a.length - 1;
	        
	        while (lo <= hi) {
	            
	        	int searchIndex = lo + (hi - lo) / 2;
	        	
	        	String string2 = a[searchIndex].getName();
	        	
	        	//System.out.println("String 1: "+string1);
	        	//System.out.println("String 2: "+a[mid].getName());
	
	        	
	            if      (probabilityCompare(string1, string2) == -1) hi = searchIndex - 1;
	            else if (probabilityCompare(string1, string2) == 1) lo = searchIndex + 1;
	            else{ mid = searchIndex; break; }
	        }
        }
        
        if(mid != -1)
        {
        	if(!stringArray[4].equals(""))
        	{
        		boolean isFound = false;
        		
        		int index = mid;
        		
        		while(roads[mid].getName().equals(roads[index].getName()) && !isFound)
        		{
        			if((roads[index].getZipCode()+"").equals(stringArray[4]))
    				{
    					mid = index;
    					isFound = true;
    					break;
    				}
    				
    				index++;
        		}
        		
        		index = mid-1;
        		
        		while(roads[mid].getName().equals(roads[index].getName()) && !isFound)
        		{
    				if((roads[index].getZipCode()+"").equals(stringArray[4]))
    				{
    					mid = index;
    					isFound = true;
    					break;
    				}
    				
    				index--;
        		}
        		
        		if(!isFound)
        			return new int[][]{{-1}};
    		}
    		
    		
        	
        	int found = 1;
        	
        	ArrayList<Integer> foundIndexes = new ArrayList<Integer>();
        	foundIndexes.add(mid);
        	
        	int increment = 1;
        	
        	while(found < count)
        	{
        		int index = mid;
        		
        		//Start by incrementing so we dont compare with ourselves
        		index += increment;
        		
        		while((index >= 0 && index < a.length) && found >= foundIndexes.size())
        		{
        			boolean bounce = true;
        			
        			for(int i=0; i<found; i++)
        				if(a[foundIndexes.get(i)].getAddress().equals(a[index].getAddress()))
        					bounce = false;
        			
        			if(bounce)
        			{
        				foundIndexes.add(index);
        				break;
        			}
        			
        			index += increment;
        		}
        		
        		increment *= -1;
        		
        		if(found < foundIndexes.size())
        			found++;
        	}
        	
        	/*
        	if(!stringArray[1].equals(""))
        	{
        		int roadNumber = Integer.parseInt(stringArray[1]);
        		
        		for(int j=0; j<foundIndexes.size(); j++)
        		{
        			/*
        			boolean isFound = false;
        			
            		for(int i=0; i<roads[foundIndexes.get(j)].getEdges().length; i++)
            		{
            			
            			if(roads[foundIndexes.get(j)].getEdge(i).hasRoadNumber(roadNumber))
            			{
            				isFound = true;
            			}
            		}
            		
        			if(!isFound)
        			{
                    	foundIndexes.remove(j);
                    	j--;
                    	break;
        			}
        			
        			
        			if(roads[foundIndexes.get(j)].getEdgeWithRoadNumber(roadNumber) < 0)
        			{
        				foundIndexes.remove(j);
        				j--;
                    	break;
        			}
        		}
        		
        		if(foundIndexes.size() == 0)
        			return new int[][]{{-1}};
        			
        	}
        	*/
        	
        	
        	int[][] returnIndexes = new int[foundIndexes.size()][2];
        	returnIndexes[0][0] = foundIndexes.get(0);
        	foundIndexes.remove(0);
        	
        	for(int i=1; i<returnIndexes.length; i++)
        	{
        		int cutoff = a[returnIndexes[0][0]].getName().length();
        		
        		boolean isfound = false;
        		
        		while(!isfound)
        		{
        			String firstResultString = a[returnIndexes[0][0]].getName().substring(0, cutoff);
            		
        			for(int j=0; j < foundIndexes.size(); j++)	
        			{
            			String currResultString = a[foundIndexes.get(j)].getName();
            			
            			if(currResultString.length() > cutoff)	
            				currResultString = a[foundIndexes.get(j)].getName().substring(0, cutoff);
            			
        				if(firstResultString.equals(currResultString))
	        			{
	        				returnIndexes[i][0] = foundIndexes.get(j);
	        				foundIndexes.remove(j);
	        				
	        				isfound = true;
	        				
	        				break;
	        			}
        			}
        			
        			cutoff--;
        			
        			if(!isfound && cutoff < 0)
        				break;
        		}
        		
        		if(!isfound)
        			break;
        	}
        	
        	for(int i=returnIndexes.length-foundIndexes.size(); i<returnIndexes.length; i++)
        	{
        		returnIndexes[i][0] = foundIndexes.get(i);
        		foundIndexes.remove(i);
        	}
        	
        	int roadNumber = 0;
        	
        	if(!stringArray[1].equals(""))
        	{
        		roadNumber = Integer.parseInt(stringArray[1]);
        	}
    	
    		for(int i=0; i<returnIndexes.length; i++)
    		{
    			int roadNumberIndex = roads[returnIndexes[i][0]].getEdgeWithRoadNumber(roadNumber, true);
    			
    			if(roadNumberIndex >= 0)
    				returnIndexes[i][1] = roadNumberIndex;
    		}
    	
        	
        	return returnIndexes;
        	//}
            	
        	/*
        	int[] returnIndexes = new int[foundIndexes.size()];
        	
        	System.out.println(returnIndexes.length);
        	
        	for(int i=0; i<returnIndexes.length; i++)
        	{
        		returnIndexes[i] = foundIndexes.get(i); 
        	}
        	*/
        }
        
        return new int[][]{{-1}};
    }
	
	public int probabilityCompare(String string1, String string2)
	{
		string1 = string1.toLowerCase();
		string2 = string2.toLowerCase();
		
		//if(string1.equals(string2))
		//	return 0;
		
		int cutoff = string1.length();
        
		if(string2.length() > cutoff)
    	{
    		string2 = string2.substring(0, cutoff);
    	}
    	
		//System.out.println("String 1: "+ string1);
		//System.out.println("String 2: "+ string2);
				
		int length = string1.length();
		int index = 0;
		
		while(index < length && index < string2.length())
		{
			if(string1.charAt(index) == ' ' && string2.charAt(index) != ' ')
				return 1;
			else if(string1.charAt(index) != ' ' && string2.charAt(index) == ' ')
				return -1;
				
			if(string1.charAt(index) > string2.charAt(index))
			{
				//System.out.println("is greater");
				return 1;
			}
			else if(string1.charAt(index) < string2.charAt(index))
			{
				//System.out.println("is lesser");
				return -1;
			}
			
			
			index++;
		}
		
		//System.out.println("is equal");
		return 0;
	}
	
	public int compare(String string1, String string2)
	{
		string1 = string1.toLowerCase();
		string2 = string2.toLowerCase();
		
		//System.out.println("String 1: "+ string1);
		//System.out.println("String 2: "+ string2);
		
		if(string1.equals(string2))
		{
			//System.out.println("Is equal (0)");
			return 0;
		}
		
		int length = string1.length();
		int index = 0;
		
		while(index < length && index < string2.length())
		{
			if(string1.charAt(index) == ' ' && string2.charAt(index) != ' ')
				return 1;
			else if(string1.charAt(index) != ' ' && string2.charAt(index) == ' ')
				return -1;
				
			if(string1.charAt(index) > string2.charAt(index))
			{
				//System.out.println("is greater (0)");
				return 1;
			}
			else if(string1.charAt(index) < string2.charAt(index))
			{
				//System.out.println("is lesser (0)");
				return -1;
			}
			
			index++;
		}
		
		if(string1.length() > string2.length())
		{
			//System.out.println("Is greater (1)");
			return 1;
		}
		else if(string2.length() > string1.length())
		{
			//System.out.println("Is lesser (1)");
			return -1;
		}
		
		//System.out.println("is equal (1)");
		return 0;
	}
	
	public Road[] getRoads()
	{
		return roads;
	}
}
