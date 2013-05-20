package Testing;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import Graph.Edge;
import Graph.Node;
import Route.Settings;

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
		createList.add("0.0125");
		createList.add("1");
		
		return new Edge(0, 0, createList);
	}
	
	//Compare
	
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
	
	//Weight
	
	@Test
	public void testWeightCarShortest(){
		
		Route.Settings.setMeansOfTransport(Route.Settings.car);
		Route.Settings.setRouteProfile(Route.Settings.shortest_route);
		
		assertEquals(true, edge1.weight() == edge1.getLength());
	}
	
	@Test
	public void testWeightCarFastest(){
		
		Route.Settings.setMeansOfTransport(Route.Settings.car);
		Route.Settings.setRouteProfile(Route.Settings.fastest_route);
		
		assertEquals(true, edge1.weight() == edge1.getDriveTime());
	}
	
	@Test
	public void testSpeedWeighting(){
		
		Route.Settings.setRouteProfile(Route.Settings.fastest_route);
		
		Route.Settings.setMeansOfTransport(Route.Settings.car);
		double carTime = edge1.weight();
		Route.Settings.setMeansOfTransport(Route.Settings.bike);
		double bikeTime = edge1.weight();
		Route.Settings.setMeansOfTransport(Route.Settings.foot);
		double footTime = edge1.weight();
		
		assertEquals(true, (carTime < bikeTime) && (bikeTime < footTime));
	}

	@AfterClass
    public static void tearDownClass() throws Exception {
        Node.nullNodes();
    }
}
