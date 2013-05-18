package Testing;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import Graph.Edge;

public class EdgeTest {

	ArrayList<String> createList = new ArrayList<String>();
	
	Edge edge1;
	Edge edge2;
	Edge edge3;
	Edge edge4;
	Edge edge5;
	
	@Before
	public void setUp()
	{
		edge1 = addEdge("Vesterfælledvej");
		edge2 = addEdge("Vesterfælled");
		edge3 = addEdge("Vester Allé");
		edge4 = addEdge("Sdr. Hygum Birkevej");
		edge5 = addEdge("Sdrognevej");
	}
	
	private Edge addEdge(String name)
	{
		createList.clear();
		
		createList.add("1000");
		createList.add("0");
		createList.add(name);
		
		createList.add("0");
		createList.add("0");
		createList.add("0");
		createList.add("0");
		
		createList.add("1750");
		createList.add("20");
		createList.add("0.15");
		createList.add("1");
		
		return new Edge(0, 0, createList);
	}
	
	@Test
	public void testCompareGreaterLength() {
		
		assertEquals(1, edge1.compareTo(edge2));
	}
	
	@Test
	public void testCompareSmallerLength() {
		
		assertEquals(-1, edge2.compareTo(edge1));
	}
	
	@Test
	public void testCompareWithSpace() {
		
		assertEquals(-1, edge1.compareTo(edge3));
	}
	
	@Test
	public void testCompareDotWithNoDot() {
		
		assertEquals(1, edge1.compareTo(edge4));
	
	}
	
	@Test
	public void testCompareDotWithDot() {
		
		assertEquals(-1, edge4.compareTo(edge5));
	}
	
	@Test
	public void testCompareEquals() {
		
		assertEquals(0, edge1.compareTo(edge1));
	}
}
