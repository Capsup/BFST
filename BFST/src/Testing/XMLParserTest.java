package Testing;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import org.junit.Test;

import Graph.Edge;

import XMLParser.XMLParser;

public class XMLParserTest 
{
	@Test
	public void testRectQueryContains()
	{
		LinkedList<Thread> threads = new LinkedList<Thread>();
		try {
			new XMLParser("data/kdv_node_unload.xml").getNodes();
			for(int i = 1;; i++){
				Thread t = new Thread(new XMLParser("data/kdv_unload_test_" + i + ".xml"));
				t.start();
				threads.add(t);
			}
		} catch (FileNotFoundException | XMLStreamException e) {}
		try { for(Thread t : threads) t.join();	} catch (InterruptedException e) { e.printStackTrace(); }

		ArrayList<Edge> e = new ArrayList<Edge>();
		ArrayList<Edge> edges = new ArrayList<Edge>();
		
		for(List<Edge> l : XMLParser.getEdgeList())
			for(Edge edge : l)
				e.add(edge);

		
		ArrayList<String> s = new ArrayList<String>();
		int v = 546404;
		int w = 546334;

		s.add("44.56884"); //length
		s.add("6"); //Type
		s.add("Christiansø"); //road name
		s.add("0"); //from left
		s.add("0"); //to left
		s.add("0"); // from right
		s.add("0"); // to right
		s.add("3740"); //zip
		s.add("30"); //Speedlimit
		s.add("0.103"); //Drivetime
		s.add("''"); //Oneway

		edges.add(new Edge(v,w,s));

		s = new ArrayList<String>();
		v = 546320;
		w = 546505;

		s.add("58.08874"); //length
		s.add("8"); //Type
		s.add(""); //road name
		s.add("0"); //from left
		s.add("0"); //to left
		s.add("0"); // from right
		s.add("0"); // to right
		s.add("0"); //zip
		s.add("30"); //Speedlimit
		s.add("0.134"); //Drivetime
		s.add("'n'"); //Oneway

		edges.add(new Edge(v,w,s));

		s = new ArrayList<String>();
		v = 546081;
		w = 546068;

		s.add("20.01746"); //length
		s.add("8"); //Type
		s.add(""); //road name
		s.add("0"); //from left
		s.add("0"); //to left
		s.add("0"); // from right
		s.add("0"); // to right
		s.add("0"); //zip
		s.add("30"); //Speedlimit
		s.add("0.046"); //Drivetime
		s.add("'n'"); //Oneway

		edges.add(new Edge(v,w,s));

		s = new ArrayList<String>();
		v = 546068;
		w = 545504;

		s.add("136.81998"); //length
		s.add("8"); //Type
		s.add(""); //road name
		s.add("0"); //from left
		s.add("0"); //to left
		s.add("0"); // from right
		s.add("0"); // to right
		s.add("0"); //zip
		s.add("30"); //Speedlimit
		s.add("0.315"); //Drivetime
		s.add("'n'"); //Oneway

		edges.add(new Edge(v,w,s));

		s = new ArrayList<String>();
		v = 546320;
		w = 546068;

		s.add("58.63325"); //length
		s.add("8"); //Type
		s.add(""); //road name
		s.add("0"); //from left
		s.add("0"); //to left
		s.add("0"); // from right
		s.add("0"); // to right
		s.add("0"); //zip
		s.add("30"); //Speedlimit
		s.add("0.135"); //Drivetime
		s.add("'n'"); //Oneway

		edges.add(new Edge(v,w,s));


		for(int i = 0; i < edges.size(); i++){		
			assertEquals(edges.get(i).getFromIndex(), e.get(i).getFromIndex());
			assertEquals(edges.get(i).getToIndex(), e.get(i).getToIndex());
			assertEquals(0, new Double(edges.get(i).getLength()).compareTo(e.get(i).getLength()));
			assertEquals(edges.get(i).getTyp(), e.get(i).getTyp());
			assertEquals(edges.get(i).getName(), e.get(i).getName());
			assertEquals(edges.get(i).getToRight(), e.get(i).getToRight());
			assertEquals(edges.get(i).getToLeft(), e.get(i).getToLeft());
			assertEquals(edges.get(i).getFromRight(), e.get(i).getFromRight());
			assertEquals(edges.get(i).getFromLeft(), e.get(i).getFromLeft());
			assertEquals(edges.get(i).getZip(), e.get(i).getZip());
			assertEquals(0, new Double(edges.get(i).getSpeedLimit()).compareTo(e.get(i).getSpeedLimit()));
			assertEquals(0, new Double(edges.get(i).getDriveTime()).compareTo(e.get(i).getDriveTime()));
			assertEquals(edges.get(i).getOneWay(), e.get(i).getOneWay());
		}
	}

}