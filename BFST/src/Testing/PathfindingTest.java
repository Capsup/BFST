package Testing;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;

import javax.jws.Oneway;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import AddressParser.Road;
import Graph.Graph;
import Graph.Edge;
import Graph.Node;
import MapDraw.MapDraw;
import Route.Dijkstra;
import Route.Settings;

public class PathfindingTest {

	static Graph graph;
	static ArrayList<List<Edge>> edges;
	
	@Before
	public void setUp()
	{	
		resetGraph();
		
		Node.makeNode(0, 0, 0);
		Node.makeNode(1, 0, 0);
		Node.makeNode(2, 0, 0);
		Node.makeNode(3, 0, 0);
		Node.makeNode(4, 0, 0);
		Node.makeNode(5, 0, 0);
	}
	
	public static void resetGraph()
	{
		edges = new ArrayList<List<Edge>>();
		edges.add(new LinkedList<Edge>());
	}
	
	public static void addEdge(int fromIndex, int toIndex, int length, int type, double driveTime, String oneWay)
	{
		ArrayList<String> createList = new ArrayList<String>();
		
		createList.add(""+length);
		createList.add(""+type);
		createList.add("0");
		
		createList.add("0");
		createList.add("0");
		createList.add("0");
		createList.add("0");
		
		createList.add("0");
		createList.add("0");
		createList.add(""+driveTime);
		createList.add(oneWay);
		
		edges.get(0).add(new Edge(fromIndex, toIndex, createList));
	}
	
	@Test
	public void testHasPath() {
		
		//Add three edges that forms a triangle
		addEdge(0, 1, 3, 0, 0, "''");
		addEdge(1, 2, 4, 0, 0, "''");
		addEdge(0, 2, 5, 0, 0, "''");
		
		Graph graph = new Graph(edges);
		
		assertEquals(true, new Dijkstra(graph, 0).hasPathTo(2));
	}
	
	@Test
	public void testHasNoPath() {
		
		//Add three edges that forms a triangle
		addEdge(0, 1, 3, 0, 0, "''");
		addEdge(1, 2, 4, 0, 0, "''");
		addEdge(0, 2, 5, 0, 0, "''");
		
		//Add Three edges that forms another triangle
		addEdge(3, 4, 4, 0, 0, "''");
		addEdge(3, 5, 5, 0, 0, "''");
		addEdge(4, 5, 5, 0, 0, "''");
		
		Graph graph = new Graph(edges);
		
		assertEquals(false, new Dijkstra(graph, 0).hasPathTo(3));
	}
	
	@Test
	public void testWithoutFerry() {
		
		Route.Settings.setFerryAllowed(Route.Settings.no);
		
		//Add two highway edges
		addEdge(0, 1, 3, 0, 0, "''");
		addEdge(1, 2, 4, 0, 0, "''");
		
		//Adds a ferry type edge
		addEdge(0, 2, 5, 80, 0, "''");
		
		Graph graph = new Graph(edges);
		
		assertEquals(2, new Dijkstra(graph, 0).pathTo(2).length);
	}
	
	@Test
	public void testWithFerry() {
		
		Route.Settings.setFerryAllowed(Route.Settings.yes);
		
		//Adds two highway edges
		addEdge(0, 1, 3, 0, 0, "''");
		addEdge(1, 2, 4, 0, 0, "''");
		
		//Adds a ferry type edge
		addEdge(0, 2, 5, 80, 0, "''");
		
		Graph graph = new Graph(edges);
		
		assertEquals(1, new Dijkstra(graph, 0).pathTo(2).length);
	}
	
	@Test
	public void testFastestPath() {
		
		Route.Settings.setRouteProfile(Route.Settings.fastest_route);
		
		//Adds three edges
		//The shortest path is 5 and a length of 1
		//The fastest path is 0.5 driveTime and a length of 2
		addEdge(0, 1, 3, 0, 0.25, "''");
		addEdge(1, 2, 4, 0, 0.25, "''");
		addEdge(0, 2, 5, 0, 1, "''");
		
		Graph graph = new Graph(edges);
		
		assertEquals(2, new Dijkstra(graph, 0).pathTo(2).length);
	}
	
	@Test
	public void testShortestPath() {
		
		Route.Settings.setRouteProfile(Route.Settings.shortest_route);
		
		//Adds three edges
		//The shortest path is 5 and a length of 1
		//The fastest path is 0.5 driveTime and a length of 2
		addEdge(0, 1, 3, 0, 0.25, "''");
		addEdge(1, 2, 4, 0, 0.25, "''");
		addEdge(0, 2, 5, 0, 1, "''");
		
		Graph graph = new Graph(edges);
		
		assertEquals(1, new Dijkstra(graph, 0).pathTo(2).length);
	}
	
	@Test
	public void testOneWayWithPath() {
		
		//Adds three edges
		addEdge(0, 1, 0, 0, 0, "'ft'");
		addEdge(1, 2, 0, 0, 0, "'ft'");
		addEdge(0, 2, 0, 0, 0, "'ft'");
		
		Graph graph = new Graph(edges);
		
		assertEquals(true, new Dijkstra(graph, 0).hasPathTo(2));
	}
	
	@Test
	public void testOneWayWithoutPath() {
		
		//Adds three edges
		addEdge(0, 1, 0, 0, 0, "'ft'");
		addEdge(1, 2, 0, 0, 0, "'ft'");
		addEdge(0, 2, 0, 0, 0, "'ft'");
		
		Graph graph = new Graph(edges);
		
		assertEquals(false, new Dijkstra(graph, 2).hasPathTo(0));
	}
}
