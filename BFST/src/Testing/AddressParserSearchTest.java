package Testing;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

import AddressParser.AddressParser;
import AddressParser.Road;
import Graph.Edge;

public class AddressParserSearchTest {

	@BeforeClass
    public static void setUpClass() throws Exception {
		
		AddressParser.getInstance();
    }
	
	//Compare
	
	@Test
	public void testCompareMatch() {
		
		String string1 = "Vesterfælledvej";
		String string2 = "Vesterfælledvej";
		
		assertEquals(0, AddressParser.getInstance().compare(string1, string2));
	}
	
	@Test
	public void testCompareHigher() {
		
		String string1 = "Vesterfælledvej";
		String string2 = "Vesterfælled";
		
		assertEquals(1, AddressParser.getInstance().compare(string1, string2));
	}
	
	@Test
	public void testCompareLower() {
		
		String string1 = "Vesterfælled";
		String string2 = "Vesterfælledvej";
		
		assertEquals(-1, AddressParser.getInstance().compare(string1, string2));
	}
	
	@Test
	public void testCompareWithSpaceDiffference() {
		
		String string1 = "Vester Allé";
		String string2 = "Vesterfælled";
		
		assertEquals(1, AddressParser.getInstance().compare(string1, string2));
	}
	
	@Test
	public void testCompareWithSpaceEqual() {
		
		String string1 = "Vester Allé";
		String string2 = "Vester Allé";
		
		assertEquals(0, AddressParser.getInstance().compare(string1, string2));
	}
	
	
	//Probability Compare
	
	@Test
	public void testProbabilityCompareDirectMatch() {
		
		String string1 = "Vesterfælled";
		String string2 = "Vesterfælled";
		
		assertEquals(0, AddressParser.getInstance().probabilityCompare(string1, string2));
	}
	
	@Test
	public void testProbabilityCompareProbableMatch() {
		
		String string1 = "Vesterfælled";
		String string2 = "Vesterfælledvej";
		
		assertEquals(0, AddressParser.getInstance().probabilityCompare(string1, string2));
	}
	
	@Test
	public void testProbabilityCompareProbableNoMatch() {
		
		String string1 = "Vesterfælledvej";
		String string2 = "Vesterfælled";
		
		assertEquals(1, AddressParser.getInstance().probabilityCompare(string1, string2));
	}
	
	@Test
	public void testProbabilityCompareHigher() {
		
		String string1 = "Vesterfælled";
		String string2 = "Vesterbrogade";
		
		assertEquals(1, AddressParser.getInstance().probabilityCompare(string1, string2));
	}
	
	@Test
	public void testProbabilityCompareLower() {
		
		String string1 = "Vesterbrogade";
		String string2 = "Vesterbyvej";
		
		
		assertEquals(-1, AddressParser.getInstance().probabilityCompare(string1, string2));
	}
	
	@Test
	public void testProbabilityCompareWithSpace() {
		
		String string1 = "Vester Allé";
		String string2 = "Vesterfælledvej";
		
		
		assertEquals(1, AddressParser.getInstance().probabilityCompare(string1, string2));
	}
	
	//Search
	
	@Test
	public void testSearchDirect() {
		
		String[] parsedAddress = new String[]{"Byvej", "", "", "", "3310", ""};
		
		int[] searchHit = AddressParser.getInstance().search(parsedAddress);
		
		assertEquals(AddressParser.getInstance().getRoads()[searchHit[0]].getAddress(), parsedAddress[0]+", "+parsedAddress[4]);
	}
	
	@Test
	public void testSearchWithoutZip() {
		
		String[] parsedAddress = new String[]{"Byvej", "", "", "", "", ""};
		
		int[] searchHit = AddressParser.getInstance().search(parsedAddress);
		
		assertEquals(AddressParser.getInstance().getRoads()[searchHit[0]].getName(), parsedAddress[0]);
	}
	
	@Test
	public void testSearchWithDifferentZipFromMostProbable() {
		
		String[] parsedAddress = new String[]{"Byvej", "", "", "", "2650", ""};
		
		int[] searchHit = AddressParser.getInstance().search(parsedAddress);
		
		assertEquals(AddressParser.getInstance().getRoads()[searchHit[0]].getAddress(), parsedAddress[0]+", "+parsedAddress[4]);
	}
	
	@Test
	public void testSearchWithRoadNumber() {
		
		String[] parsedAddress = new String[]{"Vesterfælledvej", "1", "", "", "1750", ""};
		
		int[] searchHit = AddressParser.getInstance().search(parsedAddress);
		
		assertEquals(AddressParser.getInstance().getRoads()[searchHit[0]].getAddress(), parsedAddress[0]+", "+parsedAddress[4]);
	}
	
	@Test
	public void testSearchWrongName() {
		
		String[] parsedAddress = new String[]{"Vesterfælledv", "", "", "", "", ""};
		
		int[] searchHit = AddressParser.getInstance().search(parsedAddress);
		
		assertEquals(-1, searchHit[0]);
	}
	
	@Test
	public void testSearchWrongZip() {
		
		String[] parsedAddress = new String[]{"Vesterfælledvej", "", "", "", "1760", ""};
		
		int[] searchHit = AddressParser.getInstance().search(parsedAddress);
		
		assertEquals(-1, searchHit[0]);
	}
	
	//Probability Search
	@Test
	public void testProbabilitySearchDirect() {
		
		String[] parsedAddress = new String[]{"Byvej", "", "", "", "3310", ""};
		
		int[][] searchHit = AddressParser.getInstance().probabilitySearch(parsedAddress, 5);
		
		assertEquals(AddressParser.getInstance().getRoads()[searchHit[0][0]].getAddress(), parsedAddress[0]+", "+parsedAddress[4]);
	}
	
	@Test
	public void testProbabilityInDirect() {
		
		//Needs to be a road we know is in the array
		String[] parsedAddress = new String[]{"Vesterfælledv", "", "", "", "", ""};
		
		int[][] searchHit = AddressParser.getInstance().probabilitySearch(parsedAddress, 5);
		
		assertEquals(true, AddressParser.getInstance().getRoads()[searchHit[0][0]].getName().contains(parsedAddress[0]));
	}
	
	@Test
	public void testProbabilityInvalidOutput() {
		
		//Needs to be an input we know wont match any road
		String[] parsedAddress = new String[]{"Vesterfskjd", "", "", "", "", ""};
		
		int[][] searchHit = AddressParser.getInstance().probabilitySearch(parsedAddress, 5);
		
		assertEquals(-1, searchHit[0][0]);
	}
	
	@Test
	public void testProbabilityInvalidInput() {
		
		String[] parsedAddress = new String[]{"*-!", "", "", "", "", ""};
		
		int[][] searchHit = AddressParser.getInstance().probabilitySearch(parsedAddress, 5);
		
		assertEquals(-1, searchHit[0][0]);
	}
	
	//Get adjacent indexes
	
	@Test
	public void testGetAdjacentIndexesUnevenCount()
	{
		int baseIndex = 1000;
		int count = 5;
		ArrayList<Integer> adjacentIndexes = AddressParser.getInstance().getAdjacentIndexes(baseIndex, count);
		
		boolean test = true;
		
		for(int i=0; i<adjacentIndexes.size(); i++)
		{
			int index = adjacentIndexes.get(i);
			
			if(index < baseIndex-Math.ceil(count/2) && index > baseIndex+Math.ceil(count/2))
				test = false;
		}
		
		if(adjacentIndexes.size() != count)
			test = false;
		
		assertEquals(true, test);
	}
	
	@Test
	public void testGetAdjacentIndexesEvenCount()
	{
		int baseIndex = 1000;
		int count = 6;
		ArrayList<Integer> adjacentIndexes = AddressParser.getInstance().getAdjacentIndexes(baseIndex, count);
		
		boolean test = true;
		
		for(int i=0; i<adjacentIndexes.size(); i++)
		{
			int index = adjacentIndexes.get(i);
			
			if(index < baseIndex-Math.ceil(count/2) && index > baseIndex+Math.ceil(count/2))
				test = false;
		}
		
		if(adjacentIndexes.size() != count)
			test = false;
		
		assertEquals(true, test);
	}
	
	@Test
	public void testGetAdjacentIndexesAtArrayLowBorder()
	{
		int baseIndex = 1;
		int count = 6;
		ArrayList<Integer> adjacentIndexes = AddressParser.getInstance().getAdjacentIndexes(baseIndex, count);
		
		boolean test = true;
		
		for(int i=0; i<adjacentIndexes.size(); i++)
		{
			int index = adjacentIndexes.get(i);
			
			if(index < baseIndex-Math.ceil(count/2) && index > baseIndex+Math.ceil(count/2))
				test = false;
			
			if(index < 0 || index >= AddressParser.getInstance().getRoads().length)
				test = false;
		}
		
		if(adjacentIndexes.size() != count)
			test = false;
			
		assertEquals(true, test);
	}
	
	@Test
	public void testGetAdjacentIndexesAtArrayHighBorder()
	{
		int baseIndex = AddressParser.getInstance().getRoads().length-2;
		int count = 6;
		ArrayList<Integer> adjacentIndexes = AddressParser.getInstance().getAdjacentIndexes(baseIndex, count);
		
		boolean test = true;
		
		for(int i=0; i<adjacentIndexes.size(); i++)
		{
			int index = adjacentIndexes.get(i);
			
			if(index < baseIndex-Math.ceil(count/2) && index > baseIndex+Math.ceil(count/2))
				test = false;
			
			if(index < 0 || index >= AddressParser.getInstance().getRoads().length)
				test = false;
		}
		
		if(adjacentIndexes.size() != count)
			test = false;
			
		assertEquals(true, test);
	}
	
	//Match zip code
	
	@Test
	
	public void testMatchZipCode()
	{
		
	}
}
