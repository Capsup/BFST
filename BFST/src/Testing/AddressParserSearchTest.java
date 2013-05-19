package Testing;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

import com.jogamp.common.util.IntIntHashMap;
import com.jogamp.newt.awt.NewtCanvasAWT;

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
	public void testMatchZipCodeIsExact()
	{

		//Through exhaustive methods we have found that index 985 is "Ahornvænget, 5771"
		
		int matchedIndex = AddressParser.getInstance().matchZipCode(985, "5771");
		
		assertEquals(985, matchedIndex);
	}
	
	@Test
	public void testMatchZipCodeIsAbove()
	{
		//Through exhaustive methods we have found that index 985 is "Ahornvænget, 5771" and index 990 is "Ahornvænget, 6710"
		
		int matchedIndex = AddressParser.getInstance().matchZipCode(985, "6710");
		
		assertEquals(990, matchedIndex);
	}
	
	@Test
	public void testMatchZipCodeIsBelow()
	{
		//Through exhaustive methods we have found that index 985 is "Ahornvænget, 5771" and index 979 is "Ahornvænget, 3200"
		
		int matchedIndex = AddressParser.getInstance().matchZipCode(985, "3200");
		
		assertEquals(979, matchedIndex);
	}
	
	@Test
	public void testMatchZipCodeIsNotFound()
	{
		//Through exhaustive methods we have found that index 985 is "Ahornvænget, 5771" and that there are no "Ahornvænget" with the zipcode 3300
		
				int matchedIndex = AddressParser.getInstance().matchZipCode(985, "3300");
				
				assertEquals(-1, matchedIndex);
	}
	
	@Test
	public void testMatchZipCodeIsInvalid()
	{
		//Through exhaustive methods we have found that index 985 is "Ahornvænget, 5771" 
		
		int matchedIndex = AddressParser.getInstance().matchZipCode(985, "-'*s123z");
		
		assertEquals(-1, matchedIndex);
	}
	
	//Get sorted indexes
	
	@Test
	public void testGetSortedIndexes()
	{
		//Through exhaustive methods we have found a line up of indexes that is suitable for a sort test
		//Index 388: Adolfsvej, 8850
		//Index 389: Adolf Andersens Vej, 2690
		//Index 390: Adolf Fredriksgatan, 21774
		//Index 391: Adolphsvej, 2820
		//Index 392: Adolph Meyers Vej, 8000
		
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		indexes.add(388);
		indexes.add(389);
		indexes.add(390);
		indexes.add(391);
		indexes.add(392);
		
		int[] sortedIntegers = AddressParser.getInstance().getSortedIndexes(indexes);
		
		//int[] 
				
		//assertEquals(expected, actual)
	}
}