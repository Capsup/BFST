package Testing;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.BeforeClass;
import org.junit.Test;

import com.jogamp.common.util.IntIntHashMap;
import com.jogamp.newt.awt.NewtCanvasAWT;

import AddressParser.AddressParser;
import AddressParser.Road;
import Graph.Edge;

public class SearchWhiteBoxTest {

	@BeforeClass
    public static void setUpClass() throws Exception {
		
		AddressParser.getInstance();
	}

	@Test
	public void testA()
	{
		String[] stringInput = new String[]{"A", "", "", "", "", ""};
		
		int[] output = AddressParser.getInstance().search(stringInput);
		
		assertEquals(true, output[0] == -1);
	}
	
	@Test
	public void testB()
	{
		String[] stringInput = new String[]{"Z", "", "", "", "", ""};
		
		int[] output = AddressParser.getInstance().search(stringInput);
		
		assertEquals(true, output[0] == -1);
	}
	
	@Test
	public void testC()
	{
		String[] stringInput = new String[]{"Rued Langgaards Vej", "", "", "", "", ""};
		
		int[] output = AddressParser.getInstance().search(stringInput);
		
		assertEquals(true, output[0] > -1);
		assertEquals(true, output[1] == 0);
	}
	
	@Test
	public void testD()
	{
		String[] stringInput = new String[]{"Rued Langgaards Vej", "", "", "", "2300", ""};
		
		int[] output = AddressParser.getInstance().search(stringInput);
		
		assertEquals(true, output[0] > -1);
		assertEquals(true, output[1] == 0);
	}
	
	@Test
	public void testE()
	{
		String[] stringInput = new String[]{"Rued Langgaards Vej", "", "", "", "2630", ""};
		
		int[] output = AddressParser.getInstance().search(stringInput);
		
		assertEquals(true, output[0] == -1);
	}
	
	@Test
	public void testF()
	{
		String[] stringInput = new String[]{ "Vesterfælledvej", "1", "", "", "1750", "" };
		
		int[] output = AddressParser.getInstance().search(stringInput);
		
		assertEquals(true, output[0] > -1);
		assertEquals(true, output[1] > -1);
	}
	
	@Test
	public void testG()
	{
		String[] stringInput = new String[]{ "Vesterfælledvej", "1", "", "", "1750", "" };
		
		int[] output = AddressParser.getInstance().search(stringInput);
		
		assertEquals(true, output[0] > -1);
		assertEquals(true, output[1] > -1);
	}
}