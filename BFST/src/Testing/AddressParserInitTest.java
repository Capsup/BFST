package Testing;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

import AddressParser.AddressParser;
import AddressParser.Road;
import Graph.Edge;
import Graph.Node;

public class AddressParserInitTest {

	@BeforeClass
    public static void setUpClass() throws Exception {
		
		Node.makeNode(0, 0, 0);
    }

	public static Edge addEdge(String roadName, String zipCode)
	{
		ArrayList<String> createList = new ArrayList<String>();
		
		createList.add("0");
		createList.add("0");
		createList.add(""+roadName);
		
		createList.add("0");
		createList.add("0");
		createList.add("0");
		createList.add("0");
		
		createList.add(""+zipCode);
		createList.add("0");
		createList.add("0");
		createList.add("");
		
		return new Edge(0, 0, createList);
	}
	
	@Test
	public void testConvertToRoadSameZipCode() {
		
		Edge[] edges = new Edge[6];
		
		edges[0] = addEdge("Vesterfælledvej", "1750");
		edges[1] = addEdge("Vesterfælledvej", "1750");
		edges[2] = addEdge("Vesterfælledvej", "1750");
		edges[3] = addEdge("Enghavevej", "1750");
		edges[4] = addEdge("Enghavevej", "1750");
		edges[5] = addEdge("Enghavevej", "1750");
		
		Road[] roads = AddressParser.convertToRoads(edges);
		
		assertEquals(roads[0].getAddress(), "Vesterfælledvej, 1750");
		assertEquals(roads[1].getAddress(), "Enghavevej, 1750");
	}
	
	@Test
	public void testConvertToRoadDifferentZipCode() {
		
		Edge[] edges = new Edge[6];
		
		edges[0] = addEdge("Vesterfælledvej", "1750");
		edges[1] = addEdge("Vesterfælledvej", "1750");
		edges[2] = addEdge("Vesterfælledvej", "1750");
		edges[3] = addEdge("Vesterfælled", "6690");
		edges[4] = addEdge("Vesterfælled", "6690");
		edges[5] = addEdge("Vesterfælled", "6690");
		
		Road[] roads = AddressParser.convertToRoads(edges);
		
		assertEquals(roads[0].getAddress(), "Vesterfælledvej, 1750");
		assertEquals(roads[1].getAddress(), "Vesterfælled, 6690");
	}
	
	@Test
	public void testConvertToRoadSameName() {
		
		Edge[] edges = new Edge[6];
		
		edges[0] = addEdge("Byvej", "2650");
		edges[1] = addEdge("Byvej", "2650");
		edges[2] = addEdge("Byvej", "1750");
		edges[3] = addEdge("Byvej", "6690");
		edges[4] = addEdge("Byvej", "6690");
		edges[5] = addEdge("Byvej", "6690");
		
		Road[] roads = AddressParser.convertToRoads(edges);
		
		assertEquals(roads[0].getAddress(), "Byvej, 2650");
		assertEquals(roads[1].getAddress(), "Byvej, 1750");
		assertEquals(roads[2].getAddress(), "Byvej, 6690");
	}
	
}
