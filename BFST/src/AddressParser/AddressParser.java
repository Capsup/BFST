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

/**
 * A class used to parse and search for addresses
 */
public class AddressParser
{
	private static AddressParser instance;		//The singleton variable
	
	private Road[] roads;						//An array of all the roads in our program

	/**
	 * The constructor initializes the class by getting all the edges from the query and sorting them in a collected road array
	 */
	private AddressParser()
	{
		instance = this;						//We set the value of the singleton
		
		ArrayList<Edge> list = new ArrayList<Edge>();
		
		//We iterate through all the road types
		for(int i=0; i<100; i++)
		{
			//We get an iterator over the current road type
			Iterator<Edge> it = DataProcessing.Query.getInstance().queryEdges(i).iterator();
			
			while(it.hasNext())
			{
				//We iterate through the edges and add all edges that has a name to the list (we dont need to search for roads without a name)
				Edge edge = it.next();
				
				if(!edge.getName().equals(""))
					list.add(edge);
			}
		}
		
		//We initilize an ew array to contain the edges
		Edge[] edges = new Edge[list.size()];
		
		//We convert the edges in the list to the edge array
		list.toArray(edges);
		list.clear();
		
		//and sort it
		Arrays.sort(edges);
		
		//We allocate a road list to contain all the roads
		ArrayList<Road> roadList = new ArrayList<Road>();
		
		//We allocate a list to contain all the edges of the current road
		ArrayList<Edge> currEdgeList = new ArrayList<Edge>();
		
		//We initialize our compare variables, using the name and zipcode of the first edge in the array
		String roadName = edges[0].getName();
		int zipCode = edges[0].getZip();
		
		for(int i=0; i<edges.length; i++)
		{
			//We iterate through all the edges
			
			if(roadName.equals(edges[i].getName()) && zipCode == edges[i].getZip())
			{
				//If the edge still has the same name and zipcode as the current road, we add it to the list
				currEdgeList.add(edges[i]);
			}
			else
			{
				//Once we get an edge with a new road name or new zipcode, we know that we have reached a new road
				
				//We change the compare variables to match the new road
				roadName = edges[i].getName();
				zipCode = edges[i].getZip();
				
				//We convert our array list to an edge array
				Edge[] edgeArray = new Edge[currEdgeList.size()];
				currEdgeList.toArray(edgeArray);
				
				//we create a new road based on the edge array and add it to the road list
				roadList.add(new Road(edgeArray));
				
				//We reset the current edge list
				currEdgeList = new ArrayList<Edge>();
				
				//And add the first edge to the list
				currEdgeList.add(edges[i]);
			}
		}
		
		//We then convert the road list to an array of roads
		roads = new Road[roadList.size()];
		roadList.toArray(roads);
	}
	
	/**
	 * @return returns the singleton, if the singleton is null we initialize it.
	 */
	public static AddressParser getInstance()
	{
		if(instance == null)
			new AddressParser();
		
		return instance;
	}
	
	/**
	 * This function takes an string parameter containing an address from the data set. It breaks this
	 * string into the 6 different components that we need to know. 
	 * 1: The street name
	 * 2: The street number
	 * 3: The street letter
	 * 4: The floor number and letter
	 * 5: The postal code
	 * 6: The city name.
	 * 
	 * @param sAddress - the address string that is to be broken down into components.
	 * @return an string array containing the information of the address given.
	 */
	public String[] parseAddress( String sAddress )
	{
		//Create the array of strings where we place the final data
		String[] finalStrings = new String[6];
		String[] addressStrings = sAddress.split( " " );

		int iProcessed = 0;

		//Initialize all the strings in the array for good measure.
		for( int i = 0; i < finalStrings.length; i++ )
		{
			finalStrings[i] = "";
		}

		//For each string in the split string array, we run this until we find a string that contains digits.
		for( ; iProcessed < addressStrings.length; iProcessed++ )
		{
			String curString = addressStrings[iProcessed];
			//Aslong as the string contains a-zזרו or , or . this will keep adding the strings to the road name. As soon as we hita digit, we break the loop.
			if( curString.matches( "[\\xC5\\xC6\\xD8\\xE5\\xE6\\xF8\\xC9\\xE9\\xC4\\xE4\\xD6\\xF6\\xDC\\xFC\\xC8\\xE8a-zA-Z,.]*" )
			        && !curString.equals( "i" ) )
				if( curString.contains( "," ) )
				{
					//Remember to remove , and also make sure we put spaces at the right places when we concatenate the string.
					finalStrings[0] += ( iProcessed == 0 ? "" : " " ) + curString.replaceAll( ",", "" );
					iProcessed++;
					break;
				}
				else
					finalStrings[0] += ( iProcessed == 0 ? "" : " " ) + curString;
			else
				break;
		}
		
		//This loops starts from where the other one ended. There is no reason to parse the data that we already placed into the final array.
		for( ; iProcessed < addressStrings.length; iProcessed++ )
		{
			//Remove all commas since they're no longer relevant  to us.
			addressStrings[iProcessed] = addressStrings[iProcessed].replace( ",", "" );
			if( addressStrings[iProcessed].contentEquals( "sal" ) || addressStrings[iProcessed].contentEquals( "i" ) )
				continue;

			//If the current string is a number between 0 and 9 with 1 to 3 digits, we classify it as a road number.
			if( addressStrings[iProcessed].matches( "[0-9]{1,3}" ) && finalStrings[1].equals( "" ) )
				finalStrings[1] = addressStrings[iProcessed];

			//If the current string has 1 to 3 digits but also a letter it is both the house number and house letter.
			else if( addressStrings[iProcessed].matches( "[0-9]{1,3}[a-zA-Z]" ) )
			{
				//If there is atleast 1 letter and optionally atleast 1 digit, we need to split it up so we can put the house letter into its own place in the array.
				if( addressStrings[iProcessed].matches( "\\d?\\w+" ) )
				{
					//We loop through the string to figure out where the letters are
					for( int i = 0; i < addressStrings[iProcessed].length(); i++ )
					{
						if( Character.toString( addressStrings[iProcessed].charAt( i ) ).matches( "[a-zA-Z]" ) )
						{
							//And cut from there, putting all infront of the letters into the 'house number' and the rest into the 'house letter'.
							finalStrings[1] = addressStrings[iProcessed].substring( 0, i );
							finalStrings[2] = addressStrings[iProcessed].substring( i, addressStrings[iProcessed].length() );
							break;
						}
					}
				}
				else
					finalStrings[2] = addressStrings[iProcessed];
			}
			//If the address also contains a floor number, this needs to be parsed too
			else if( addressStrings[iProcessed].contains( "." ) || addressStrings[iProcessed].matches( "[0-9]{1,3}[a-zA-Z]?" ) )
				finalStrings[3] = addressStrings[iProcessed].replace( ".", "" );
			//The first number that is atleast 4 digits long is considered the postal code.
			else if( addressStrings[iProcessed].matches( "[0-9]{4,}" ) )
				finalStrings[4] = addressStrings[iProcessed];
			//And the last string that contains a-z and זרו is considered the city name.
			else if( addressStrings[iProcessed].matches( "[\\xC5\\xC6\\xD8\\xE5\\xE6\\xF8\\xC9\\xE9\\xC4\\xE4\\xD6\\xF6\\xDC\\xFC\\xC8\\xE8a-zA-Z]*" ) )
				finalStrings[5] += ( !finalStrings[5].equals( "" ) ? " " : "" ) + addressStrings[iProcessed];
		}

		//Aaaand return the final array.
		return finalStrings;
	}
	
	
	/**
	 * Search for an exact hit in our array of roads, and return the index (the first index of the index array is the index of the road, the second is the index of the edge in the road)
	 * @param a stringArray. This should in any case be a product of a parsed address, since it has the correct indexation
	 * @return	returns an array of ints. The first index of the array is the index of the road we find, the second index is the index of the edge in that road. We use an array of size one with the value -1 as an invalid return value
	 */
	public int[] search(String[] stringArray) 
	{
		Road[] a = roads;					//We start out by making a reference to our road array
		
		//We then initialize variables used for our custom binary search
        int lo = 0;
        int hi = a.length - 1;
        
        //We start out by matching the parsed road name with those in our array
        String string1 = stringArray[0];
        
        while (lo <= hi) 
        {
        	int mid = lo + (hi - lo) / 2;
        	
        	//We get the road name of the current search hit
        	String string2 = a[mid].getName();
	        
        	//We compare the two names
            if      (compare(string1, string2) == -1) hi = mid - 1;
            else if (compare(string1, string2) == 1) lo = mid + 1;
            else
            {
            	//We might have multiple roads with the same name so we proceed to process the search hit
            	
            	//If we have a zipcode in our parsed array we use this is process our data
            	if(!stringArray[4].equals(""))
            	{
            		mid = matchZipCode(mid, stringArray[4]);
            		
            		if(mid < 0)
            			return new int[]{-1};
            	}
            	
            	//We then process the input of the road number
            	int roadNumber = 0;		//The roadnumber we wish to search for (if none is input we search for 0)
            	
            	//If we have a road number in our parsed address we set the road number to search for accordingly
            	if(!stringArray[1].equals(""))
            		roadNumber = Integer.parseInt(stringArray[1]);
            	
            	//We then search through our road and find the edge that either contains the number or is closest to it.
        		int indexWithNumber = a[mid].getEdgeWithRoadNumber(roadNumber, false);
        		
        		//If we did not get an invalid number from the road number search we return the resulting road and edge indexes
        		if(indexWithNumber >= 0)
        			return new int[]{mid,indexWithNumber};
        		else
        			return new int[]{mid,0};	//Otherwise we return the road search hit and the index of the first edge in the road
            }
        }
        
        return new int[]{-1};			//If we dont find any thing during our binary search we return an invalid output
    }
	
	/**
	 * Get a probability search for probable hits of the address you have parsed in. Note that the probability search does not always give the exact hit, even when searched for directly.
	 * Use the search method for direct hit purposes. The probability search only finds the most probable addresses
	 * @param an array of strings. This should in any case be a product of the parseAddress method
	 * @param the amount of search hits we wish to output
	 * @return	returns the index of a number of probable search hits
	 */
	public int[][] probabilitySearch(String[] stringArray, int count) 
	{
		Road[] a = roads;					//We make a reference to our road array
		
		//We then initialize variables used for our custom binary search
		int lo = 0;
        int hi = a.length - 1;
        
        String string1 = stringArray[0];
        
        //We let mid be the resulting index of our search
        int mid = -1;
        
        //We first make a regular binary search in order to find our address
        while(lo <= hi)
        {
        	int searchIndex = lo + (hi - lo) / 2;
        	
        	String string2 = a[searchIndex].getName();
        	
        	if      (compare(string1, string2) == -1) hi = searchIndex - 1;
            else if (compare(string1, string2) == 1) lo = searchIndex + 1;
            else{ mid = searchIndex; break; }
        }
        
        //If we had no direct hit on our search, we proceed to make a probability search instead.
        if(mid == -1)
        {
        	//We reset the search variables
	        lo = 0;
	        hi = a.length - 1;
	        
	        
	        while (lo <= hi) {
	            
	        	int searchIndex = lo + (hi - lo) / 2;
	        	
	        	String string2 = a[searchIndex].getName();
	        	
	        	//Note that we use probability compare rather than compare
	            if      (probabilityCompare(string1, string2) == -1) hi = searchIndex - 1;
	            else if (probabilityCompare(string1, string2) == 1) lo = searchIndex + 1;
	            else{ mid = searchIndex; break; }
	        }
        }
        
        //If we had a hit with either of our searches we proceed to process the remaining input
        if(mid != -1)
        {
        	//First we start out by processing the zipcode
        	if(!stringArray[4].equals(""))
        	{
        		//If the input zip code is not empty we match it with the roads in our array to find a new search hit
        		mid = matchZipCode(mid, stringArray[4]);
        		
        		//If the new mid index is invalid we return an invalid output
        		if(mid < 0)
        			return new int[][]{{-1}};
    		}
        	
        	//We then get the adjacent indexes of the mid index
        	ArrayList<Integer> foundIndexes = getAdjacentIndexes(mid, count);
        	
        	//We sort the indexes by importance
        	int[] sortedIndexes = getSortedIndexes(foundIndexes);
        	
        	//We initialize and put in the sorted values for the final return array
        	int[][] returnIndexes = new int[sortedIndexes.length][2];
        	
        	for(int i=0; i<returnIndexes.length; i++)
        		returnIndexes[i][0] = sortedIndexes[i];
        	
        	//We now process the road number data
        	int roadNumber = 0;			//The road number that we wish to process, we use 0 if we dont have a parsed number
        	
        	if(!stringArray[1].equals(""))
        		roadNumber = Integer.parseInt(stringArray[1]);		//If the parsed address contains a road number we use this instead of 0
    	
    		for(int i=0; i<returnIndexes.length; i++)
    		{
    			//For all the road indexes we have found we get the edge of the specific road that either contains or is closest to the road number
    			int roadNumberIndex = roads[returnIndexes[i][0]].getEdgeWithRoadNumber(roadNumber, true);
    			
    			//If the road number search returned a valid index we put this into our return array
    			if(roadNumberIndex >= 0)
    				returnIndexes[i][1] = roadNumberIndex;
    		}
    	
        	//We have now processed all the data of our parsed address so now we return the array of probable search hits.
        	return returnIndexes;
        }
        else 
        	return new int[][]{{-1}};		//If we had no search hit on the road name we return an invalid output
    }
	
	/**
	 * We make a probable comparison of two strings
	 * We cut down string 2 to the length of string 2 and then we compare the two strings.
	 * @param string1 this should be the address we are searching for
	 * @param string2 this is the address we are currently comparing to
	 * @return whether the two strings are equal. otherwise, which one is greater.
	 */
	public int probabilityCompare(String string1, String string2)
	{
		//If the second string is longer than the first string we cut it down
		if(string2.length() > string1.length())
    		string2 = string2.substring(0, string1.length());
		
		//We then to a regular compare of the two strings
		return compare(string1, string2);
	}
	
	/**
	 * Compare the two strings on a char-to-char basis. Whenever one string differs in a char it is returned
	 * @param string1 the string we are searching for (in binary search conditions)
	 * @param string2 the string we are comparing to
	 * @return whether the two strings are equal. otherwise, which one is greater.
	 */
	public int compare(String string1, String string2)
	{
		//We start out by lower casing both strings
		string1 = string1.toLowerCase();
		string2 = string2.toLowerCase();
		
		//If the two strings are equal by definition, we know that they are equal
		if(string1.equals(string2))
			return 0;
		
		int index = 0;
		
		//As long as we dont run out of length we compare each string char by char
		while(index < string1.length() && index < string2.length())
		{
			//Since the "space" char is not a part of the alphabet we need to handle it in it's own specific case
			//If the one string has it and the other does not we differentiate them
			if(string1.charAt(index) == ' ' && string2.charAt(index) != ' ')
				return 1;
			else if(string1.charAt(index) != ' ' && string2.charAt(index) == ' ')
				return -1;
				
			//We compare the index value of the char we find in our string
			if(string1.charAt(index) > string2.charAt(index))
				return 1;
			else if(string1.charAt(index) < string2.charAt(index))
				return -1;
			
			//We increment the index in order to search through all chars
			index++;
		}
		
		//If the two strings are equal up to the point where one strings is no longer we examine the length
		//We then differentiate them based on the length
		if(string1.length() > string2.length())
			return 1;
		else if(string2.length() > string1.length())
			return -1;
		
		//If the strings did not differentiate at any point, we return that they are equal
		return 0;
	}
	
	/**
	 * We check for all the adjacent indexes with the same name as the passed index, if any of them contains the given zip code
	 * @param mid
	 * @param zipCode
	 * @return the index that holds both the mid index name and the zipcode. Returns -1 if no successful search hit was found
	 */
	public int matchZipCode(int mid, String zipCode)
	{
		int index = mid;				//We allocate the index we wish to check, starting at our search hit index
		int searchDirection = 1;		//The direction we are currently searching
		
		//We need to search both forward and backwards in the array, so we do this during 2 while loops
		for(int i=0; i<2; i++)
		{
    			//while the road we are comparing with still has the same name as the search hit, and we have not found a valid string we will search
        		while(roads[mid].getName().equals(roads[index].getName()) && index >= 0 && index < roads.length)
        		{
        			if((roads[index].getZipCode()+"").equals(zipCode))
        				return index;	//If the road we are comparing with as the same zipcode as the one we parsed we return the new index
        			else
        				index += searchDirection;	//If the search hit did not match we continue our search in the current direction
        		}
		
    		//If we do not find the road going forward in our array we go backwards instead
    		index = mid-1;
    		searchDirection = -1;
		}
    	
		//If we did not find any addresses with a matching zipcode we return our invalid output
		return -1;
	}
	
	/**
	 * Get all the adjacent indexes based on the middle index and the amount of adjacent indexes we want
	 * @param mid
	 * @param count
	 * @return an arraylist of integers representing the found indexes
	 */
	public ArrayList<Integer> getAdjacentIndexes(int mid, int count)
	{
		ArrayList<Integer> adjacentIndexes = new ArrayList<Integer>();			//We initialize an array list to hold the indexes
		adjacentIndexes.add(mid);												//We add the mid since we want it to be the first index of our array list
    	
		//We then calculate how many steps we need to go to the left and right respectively in order to reach the count
    	int leftHandLength = (int)Math.round(((count-1)/2));
    	int rightHandLength = (count)-leftHandLength;
    	
    	if(mid-leftHandLength < 0)
    		rightHandLength += -(mid-leftHandLength);
    	else if (mid+rightHandLength >= getRoads().length)
    		leftHandLength += (mid+rightHandLength)-getRoads().length;
    	
    	//We then iterate through the part of our road array that the adjacent indexes represent, check if they exist and if they do, we add them
    	for(int i=(mid-leftHandLength); i<(mid+rightHandLength); i++)
    		if(i != mid && i >= 0 && i <roads.length)
    			adjacentIndexes.add(i);
    	
    	//We then return the resulting array list
    	return adjacentIndexes;
	}
	
	/**
	 * Sorts all the indexes based on their name. The search is based on how similar the two strings are
	 * @param foundIndexes
	 * @return	returns an array of ints giving the indexes of the input integers in the sorted order
	 */
	public int[] getSortedIndexes(ArrayList<Integer> foundIndexes)
	{
		int[] returnIndexes = new int[foundIndexes.size()];		//We initialize an array of indexes to return
    	returnIndexes[0] = foundIndexes.get(0);					//We put the first index to be the mid
    	foundIndexes.remove(0);									//We then remove the index from the arraylist
    	
    	//We then iterate through the remaining indexes
    	for(int i=1; i<returnIndexes.length; i++)
    	{
    		int cutoff = roads[returnIndexes[0]].getName().length();	//We start out by defining the cutoff as the length of the main search hit
    		
    		boolean isfound = false;	//Whether or not we have found the index we are looking for
    		
    		while(!isfound)
    		{
    			String firstResultString = roads[returnIndexes[0]].getName().substring(0, cutoff);		//The string we compare with is the main search hit with the cutoff applied
        		
    			for(int j=0; j < foundIndexes.size(); j++)	
    			{
    				//We then get the string we want to compare with 
        			String currResultString = roads[foundIndexes.get(j)].getName();
        			
        			//If the string is longer than the cutoff we remove the excess part of the string
        			if(currResultString.length() > cutoff)	
        				currResultString = roads[foundIndexes.get(j)].getName().substring(0, cutoff);
        			
    				if(firstResultString.equals(currResultString))
        			{
    					//If the two cutoff strings are equal to each other we pass the result as the next most probable string to be added
        				returnIndexes[i] = foundIndexes.get(j);
        				foundIndexes.remove(j);				//We make sure to remove the index from the list to prevent duplicates
        				
        				isfound = true;					//We indicate that we have found the index
        				
        				break;			//and break out of the for-loop
        			}
    			}
    			
    			//We then decrement the cutoff
    			cutoff--;
    			
    			//If we have reached the bottom of our main string and not found anything we break the loop
    			if(!isfound && cutoff < 0)
    				break;
    		}
    		
    		//If we didnt find anything through a complete cut down of the main string we will not find anything in the upcoming iterations either
    		if(!isfound)
    			break;
    	}
    	
    	//We add the rest of the strings to the return indexes by iterating through them (they have no resemblance to the main string so their order does not matter)
    	for(int i=returnIndexes.length-foundIndexes.size(); i<returnIndexes.length; i++)
    	{
    		//We get the last index of the found index array list and put it into our return array
    		returnIndexes[i] = foundIndexes.get(foundIndexes.size()-1);
    		foundIndexes.remove(foundIndexes.size()-1);
    	}
    	
    	//We then return the sorted values
    	return returnIndexes;
	}
	
	/**
	 * @return get the roads known by the address parser
	 */
	public Road[] getRoads()
	{
		return roads;
	}
}
