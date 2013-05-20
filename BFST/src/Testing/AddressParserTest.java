package Testing;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import AddressParser.AddressParser;

public class AddressParserTest
{
	@BeforeClass
    public static void setUpClass() throws Exception {
		
		AddressParser.getInstance();
	}
	
	@Test
	public void testA()
	{
		String input = "";
		
		String[] output = AddressParser.getInstance().parseAddress(input);
		
		assertEquals("", output[0]);
		assertEquals("", output[1]);
		assertEquals("", output[2]);
		assertEquals("", output[3]);
		assertEquals("", output[4]);
		assertEquals("", output[5]);
	}

	@Test
	public void testB()
	{
		String input = "A1";
		
		String[] output = AddressParser.getInstance().parseAddress(input);
		
		assertEquals("", output[0]);
		assertEquals("", output[1]);
		assertEquals("", output[2]);
		assertEquals("", output[3]);
		assertEquals("", output[4]);
		assertEquals("", output[5]);
	}
	
	@Test
	public void testC()
	{
		String input = "A B C 123";
		
		String[] output = AddressParser.getInstance().parseAddress(input);
		
		assertEquals("A B C", output[0]);
		assertEquals("123", output[1]);
		assertEquals("", output[2]);
		assertEquals("", output[3]);
		assertEquals("", output[4]);
		assertEquals("", output[5]);
	}
	
	@Test
	public void testD()
	{
		String input = "Rued Langgaards Vej";
		
		String[] output = AddressParser.getInstance().parseAddress(input);
		
		assertEquals("Rued Langgaards Vej", output[0]);
		assertEquals("", output[1]);
		assertEquals("", output[2]);
		assertEquals("", output[3]);
		assertEquals("", output[4]);
		assertEquals("", output[5]);
	}
	
	@Test
	public void testE()
	{
		String input = "Rued Langgaards Vej 123 1. sal, 2630 Taastrup";
		
		String[] output = AddressParser.getInstance().parseAddress(input);
		
		assertEquals("Rued Langgaards Vej", output[0]);
		assertEquals("123", output[1]);
		assertEquals("", output[2]);
		assertEquals("1", output[3]);
		assertEquals("2630", output[4]);
		assertEquals("Taastrup", output[5]);
	}
}