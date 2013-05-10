package XMLParser;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

import Graph.Edge;


public class AddressParser
{
	private String[] arrayStrings;

	private static AddressParser instance;
	
	private Edge[] roads;
	
	public class NaughtyException extends Exception
	{
		public NaughtyException( String sMessage )
		{
			super( sMessage );
		}
	}

	private AddressParser()
	{
		try
		{
			loadData( "/files/road_names.txt" );
		}
		catch( NaughtyException e1 )
		{
			System.out.println( e1.getMessage() );
		}
		
		ArrayList<List<Edge>> edgeList = XMLParser.getEdgeList();
		ArrayList<Edge> list = new ArrayList<Edge>();
		
		//roads = new Edge[edgeList.size()][];
		
		for(int i=0; i<edgeList.size(); i++)
		{
			Iterator<Edge> it = edgeList.get(i).iterator();
			
			while(it.hasNext())
			{
				Edge edge = it.next();
				
				if(!edge.getName().equals(""))
					list.add(edge);
			}
		}
		
		roads = new Edge[list.size()];
		list.toArray(roads);
		
		Arrays.sort(roads);
	}
	
	public static AddressParser getInstance()
	{
		if(instance == null)
			instance = new AddressParser();
		
		return instance;
	}
/*
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
*/
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
			if( curString.matches( "[\\xC5\\xC6\\xD8\\xE5\\xE6\\xF8\\xC9\\xE9\\xC4\\xE4\\xD6\\xF6\\xDC\\xFC\\xC8\\xE8a-zA-Z,]*" )
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

		for( int i = 0; i < finalStrings.length; i++ )
			System.out.println( finalStrings[i] + "#" );

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
	
	public int search(String string) 
	{
		Edge[] a = roads;
		
        int lo = 0;
        int hi = a.length - 1;
        
        int cutoff = string.length();
        
        String string1 = string.substring(0, cutoff);
        
        while (lo <= hi) {
            
        	int mid = lo + (hi - lo) / 2;
        	
        	String string2 = a[mid].getAddress();
        	
            if      (compare(string1, string2) == -1) hi = mid - 1;
            else if (compare(string1, string2) == 1) lo = mid + 1;
            else
            {
            	return mid;
            }
        }
        
        return -1;
    }
	
	public int[] probabilitySearch(String string, int count) 
	{
		Edge[] a = roads;
		
        int lo = 0;
        int hi = a.length - 1;
        
        int cutoff = string.length();
        
        String string1 = string.substring(0, cutoff);
        
        while (lo <= hi) {
            
        	int mid = lo + (hi - lo) / 2;
        	
        	String string2;
        	
        	//System.out.println("String 1: "+string1);
        	//System.out.println("String 2: "+a[mid].getName());

        	if(a[mid].getName().length() > cutoff)
        	{
        		string2 = a[mid].getAddress().substring(0, cutoff);
        	}
        	else
        	{
        		string2 = a[mid].getAddress();
        		//string1 = string.substring(0,string2.length());
        	}
        	
            if      (probabilityCompare(string1, string2) == -1) hi = mid - 1;
            else if (probabilityCompare(string1, string2) == 1) lo = mid + 1;
            else
            {
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
            	
            	
            	int[] returnIndexes = new int[foundIndexes.size()];
            	returnIndexes[0] = foundIndexes.get(0);
            	foundIndexes.remove(0);
            	
            	for(int i=1; i<returnIndexes.length; i++)
            	{
            		cutoff = a[returnIndexes[0]].getAddress().length();
            		
            		boolean isfound = false;
            		
            		while(!isfound)
            		{
            			String firstResultString = a[returnIndexes[0]].getAddress().substring(0, cutoff);
                		
            			for(int j=0; j < foundIndexes.size(); j++)	
            			{	
	            			String currResultString = a[foundIndexes.get(j)].getAddress();
	            			
	            			if(currResultString.length() > cutoff)	
	            				currResultString = a[foundIndexes.get(j)].getAddress().substring(0, cutoff);
	            			
            				if(firstResultString.equals(currResultString))
		        			{
		        				returnIndexes[i] = foundIndexes.get(j);
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
            		returnIndexes[i] = foundIndexes.get(i);
            		foundIndexes.remove(i);
            	}
            	
            	/*
            	int[] returnIndexes = new int[foundIndexes.size()];
            	
            	System.out.println(returnIndexes.length);
            	
            	for(int i=0; i<returnIndexes.length; i++)
            	{
            		returnIndexes[i] = foundIndexes.get(i); 
            	}
            	*/
            	
            	return returnIndexes;
            }
        }
        
        return new int[]{-1};
    }
	
	public int probabilityCompare(String string1, String string2)
	{
		string1 = string1.toLowerCase();
		string2 = string2.toLowerCase();
		
		int length = string1.length();
		int index = 0;
		/*
		if(string1.length() > string2.length())
			return 1;
		else if(string2.length() > string1.length())
			return -1;
		*/
		
		while(index < length && index < string2.length())
		{
			if(string1.charAt(index) < string2.charAt(index))
			{
				//System.out.println("is greater");
				return 1;
			}
			else if(string1.charAt(index) > string2.charAt(index))
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
		
		int length = string1.length();
		int index = 0;
		
		while(index < length && index < string2.length())
		{
			if(string1.charAt(index) < string2.charAt(index))
			{
				//System.out.println("is greater");
				return 1;
			}
			else if(string1.charAt(index) > string2.charAt(index))
			{
				//System.out.println("is lesser");
				return -1;
			}
			
			index++;
		}
		
		
		if(string1.length() > string2.length())
			return 1;
		else if(string2.length() > string1.length())
			return -1;
		
		//System.out.println("is equal");
		return 0;
	}
	
	public Edge[] getRoads()
	{
		return roads;
	}
}
