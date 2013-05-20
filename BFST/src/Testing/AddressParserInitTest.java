package Testing;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import javax.swing.text.StyledEditorKit.ForegroundAction;

import org.junit.AfterClass;
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
		assertEquals(3, roads[0].getEdges().length);
		assertEquals(roads[1].getAddress(), "Enghavevej, 1750");
		assertEquals(3, roads[1].getEdges().length);
	}
	
	@Test
	public void testConvertToRoadDifferentZipCode() {
		
		Edge[] edges = new Edge[6];
		
		edges[0] = addEdge("Vesterfælled", "6690");
		edges[1] = addEdge("Vesterfælled", "6690");
		edges[2] = addEdge("Vesterfælled", "6690");
		edges[3] = addEdge("Vesterfælled", "6690");
		edges[4] = addEdge("Vesterfælledvej", "1750");
		edges[5] = addEdge("Vesterfælledvej", "1750");
		
		Road[] roads = AddressParser.convertToRoads(edges);
		
		assertEquals(roads[0].getAddress(), "Vesterfælled, 6690");
		assertEquals(roads[1].getAddress(), "Vesterfælledvej, 1750");
	}
	
	@Test
	public void testConvertToRoadSameName() {
		
		Edge[] edges = new Edge[6];
		
		edges[0] = addEdge("Byvej", "1750");
		edges[1] = addEdge("Byvej", "2650");
		edges[2] = addEdge("Byvej", "2650");
		edges[3] = addEdge("Byvej", "6690");
		edges[4] = addEdge("Byvej", "6690");
		edges[5] = addEdge("Byvej", "6690");
		
		Road[] roads = AddressParser.convertToRoads(edges);
		
		assertEquals(roads[0].getAddress(), "Byvej, 1750");
		assertEquals(roads[1].getAddress(), "Byvej, 2650");
		assertEquals(roads[2].getAddress(), "Byvej, 6690");
	}

	@Test
	public void testConvertToRoadOneEdge() {
		
		Edge[] edges = new Edge[6];
		
		edges[0] = addEdge("Byvej", "2650");
		edges[1] = addEdge("Byvej", "6690");
		edges[2] = addEdge("Byvej", "7900");
		edges[3] = addEdge("Byvej", "9500");
		edges[4] = addEdge("Hvidovrevej", "1750");
		edges[5] = addEdge("Vesterfælledvej", "1750");
		
		Road[] roads = AddressParser.convertToRoads(edges);
		
		assertEquals(1, roads[0].getEdges().length);
		assertEquals(1, roads[1].getEdges().length);
		assertEquals(1, roads[2].getEdges().length);
		assertEquals(1, roads[3].getEdges().length);
		assertEquals(1, roads[4].getEdges().length);
		assertEquals(1, roads[5].getEdges().length);
	}
	
	@Test
	public void testConvertToRoadDifferetLength() {
		
		Edge[] edges = new Edge[6];
		
		edges[0] = addEdge("Byvej", "2650");
		edges[1] = addEdge("Byvej", "6690");
		edges[2] = addEdge("Byvej", "6690");
		edges[3] = addEdge("Byvej", "9500");
		edges[4] = addEdge("Byvej", "9500");
		edges[5] = addEdge("Byvej", "9500");
		
		Road[] roads = AddressParser.convertToRoads(edges);
		
		assertEquals(1, roads[0].getEdges().length);
		assertEquals(2, roads[1].getEdges().length);
		assertEquals(3, roads[2].getEdges().length);
	}
	
	@Test
	public void testConvertToRoadSameLength() {
		
		Edge[] edges = new Edge[6];
		
		edges[0] = addEdge("Byvej", "2650");
		edges[1] = addEdge("Byvej", "2650");
		edges[2] = addEdge("Byvej", "6690");
		edges[3] = addEdge("Byvej", "6690");
		edges[4] = addEdge("Byvej", "9500");
		edges[5] = addEdge("Byvej", "9500");
		
		Road[] roads = AddressParser.convertToRoads(edges);
		
		assertEquals(2, roads[0].getEdges().length);
		assertEquals(2, roads[1].getEdges().length);
		assertEquals(2, roads[2].getEdges().length);
	}
	
	@Test
	public void testConvertToRoadHasCorrectEdges() {
		
		Edge[] edges = new Edge[6];
		
		edges[0] = addEdge("Aabyvej", "1900");
		edges[1] = addEdge("Vesterfælledvej", "1750");
		edges[2] = addEdge("Vesterfælledvej", "1750");
		edges[3] = addEdge("Århusgade", "3560");
		edges[4] = addEdge("Århusvej", "3560");
		edges[5] = addEdge("Åålborggade", "7600");
		
		Road[] roads = AddressParser.convertToRoads(edges);
		
		for(int i=0; i<roads.length; i++)
			for(int j=0; j<roads[i].getEdges().length; j++)
				assertEquals(true, roads[i].getAddress().equals(roads[i].getEdge(j).getAddress()));
	}
	
	@AfterClass
    public static void tearDownClass() throws Exception {
        Node.nullNodes();
    }
}
