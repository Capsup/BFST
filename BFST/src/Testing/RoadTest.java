package Testing;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import AddressParser.Road;
import Graph.Edge;

public class RoadTest {

	Road road;
	
	@Before
	public void setUp()
	{
		Edge[] edges = new Edge[10];
		
		edges[0] = addEdge(0, 6);
		edges[1] = addEdge(7, 10);
		edges[2] = addEdge(15, 20);
		edges[3] = addEdge(21, 21);
		edges[4] = addEdge(22, 30);
		edges[5] = addEdge(45, 47);
		edges[6] = addEdge(48, 50);
		edges[7] = addEdge(62, 63);
		edges[8] = addEdge(64, 68);
		edges[9] = addEdge(68, 70);
		
		road = new Road(edges);
	}
	
	public Edge addEdge(int fromRight, int toRight)
	{
		ArrayList<String> createList = new ArrayList<String>();
		
		createList.add("1000");
		createList.add("0");
		createList.add("");
		
		createList.add("0");
		createList.add("0");
		createList.add(""+fromRight);
		createList.add(""+toRight);
		
		createList.add("1750");
		createList.add("20");
		createList.add("0.15");
		createList.add("1");
		
		return new Edge(0, 0, createList);
	}
	
	//Direct search
	
	@Test
	public void testOneEdgeHasNumber() {
		
		assertEquals(0, road.getEdgeWithRoadNumber(5, false));
		
	}
	
	@Test
	public void testNoEdgeHasNumber() {
		
		assertEquals(2, road.getEdgeWithRoadNumber(12, false));
		
	}
	
	@Test
	public void testOnlyOneNumberOnEdge() {
		
		assertEquals(3, road.getEdgeWithRoadNumber(21, false));
		
	}
	
	@Test
	public void testNumberLargerThanRoad() {
		
		assertEquals(9, road.getEdgeWithRoadNumber(200, false));
		
	}
	
	//Probability search
	
	@Test
	public void testOneEdgeHasNumberProbability() {
		
		assertEquals(0, road.getEdgeWithRoadNumber(5, true));
		
	}
	
	@Test
	public void testNoEdgeHasNumberProbability() {
		
		assertEquals(2, road.getEdgeWithRoadNumber(12, true));
		
	}
	
	@Test
	public void testOnlyOneNumberOnEdgeProbability() {
		
		assertEquals(3, road.getEdgeWithRoadNumber(21, true));
		
	}
	
	@Test
	public void testNumberLargerThanRoadProbability() {
		
		assertEquals(-1, road.getEdgeWithRoadNumber(200, true));
		
	}
	
}
