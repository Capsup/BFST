package Testing;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import DataProcessing.Query;
import Graph.Edge;
import QuadTree.Interval;
import QuadTree.Interval2D;

public class QuadTreeTest 
{
	@Test
	public void testRectQueryContains()
	{
		Interval<Double> intervalX = new Interval<Double>(new Double(700000), new Double(740000));
		Interval<Double> intervalY = new Interval<Double>(new Double(6150000), new Double(620000));
		Interval2D<Double> interval2D = new Interval2D<Double>(intervalX, intervalY);
		
		List<Edge> edgeList = Query.getInstance().queryEdges(interval2D, 5);
		
		boolean test = true;

		Iterator<Edge> iterator = edgeList.iterator();
		
		while(iterator.hasNext())
		{
			Edge edge = iterator.next();
			
			if(!interval2D.contains(edge.getXFrom(), edge.getYFrom()))
				test = false;
			
			if(!interval2D.contains(edge.getXTo(), edge.getYTo()))
				test = false;
		}
		

		System.out.println(edgeList.size());
		
		assertEquals(true, test && edgeList.size() > 0);
	}
	
	@Test
	public void testRectQueryNotContains()
	{
		//Make two rectangles
		Interval<Double> intervalX1 = new Interval<Double>(new Double(700000), new Double(740000));
		Interval<Double> intervalY1 = new Interval<Double>(new Double(6150000), new Double(620000));
		Interval2D<Double> interval2D1 = new Interval2D<Double>(intervalX1, intervalY1);
		
		Interval<Double> intervalX2 = new Interval<Double>(new Double(745000), new Double(800000));
		Interval<Double> intervalY2 = new Interval<Double>(new Double(6150000), new Double(620000));
		Interval2D<Double> interval2D2 = new Interval2D<Double>(intervalX2, intervalY2);
		
		//Get a list of edges from first interval
		List<Edge> edgeList = Query.getInstance().queryEdges(interval2D1, 5);
		
		boolean test = true;

		Iterator<Edge> iterator = edgeList.iterator();
		
		//See if any edge from the first rectangles is inside the second rectangles
		while(iterator.hasNext())
		{
			Edge edge = iterator.next();
			
			if(interval2D2.contains(edge.getXFrom(), edge.getYFrom()))
				test = false;
			
			if(interval2D2.contains(edge.getXTo(), edge.getYTo()))
				test = false;
			
			
		}
		
		System.out.println(edgeList.size());
		assertEquals(true, test && edgeList.size() > 0);
	}
}
