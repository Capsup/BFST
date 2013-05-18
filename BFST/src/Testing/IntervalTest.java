package Testing;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import Graph.Edge;
import Graph.Node;
import QuadTree.Interval;
import QuadTree.Interval2D;
import QuadTree.QuadTree;


public class IntervalTest {
	
	//Interval
	
	@Test
	public void testIntervalContains() {
		
		Interval<Double> interval = new Interval<Double>(new Double(-10), new Double(10));
		
		assertEquals(true, interval.contains(new Double(5)));
	}
	
	@Test
	public void testIntervalContainsLower() {
		
		Interval<Double> interval = new Interval<Double>(new Double(-10), new Double(10));
		
		assertEquals(false, interval.contains(new Double(-15)));
	}
	
	@Test
	public void testIntervalContainsHigher() {
		
		Interval<Double> interval = new Interval<Double>(new Double(-10), new Double(10));
		
		assertEquals(false, interval.contains(new Double(17)));
	}
	
	@Test
	public void testIntervalContainsLowerOrEquals() {
		
		Interval<Double> interval = new Interval<Double>(new Double(-10), new Double(10));
		
		assertEquals(true, interval.contains(new Double(-10)));
	}
	
	@Test
	public void testIntervalContainsHigherOrEquals() {
		
		Interval<Double> interval = new Interval<Double>(new Double(-10), new Double(10));
		
		assertEquals(true, interval.contains(new Double(10)));
	}
	
	//Interval 2D
	
	@Test
	public void testInterval2DContains() {
		
		Interval<Double> interval = new Interval<Double>(new Double(-10), new Double(10));
		Interval2D<Double> interval2D = new Interval2D<Double>(interval, interval);
		
		assertEquals(true, interval2D.contains(new Double(5), new Double(5)));
	}
	
	@Test
	public void testInterval2DXHigher() {
		
		Interval<Double> interval = new Interval<Double>(new Double(-10), new Double(10));
		Interval2D<Double> interval2D = new Interval2D<Double>(interval, interval);
		
		assertEquals(false, interval2D.contains(new Double(15), new Double(5)));
	}
	
	@Test
	public void testInterval2DXLower() {
		
		Interval<Double> interval = new Interval<Double>(new Double(-10), new Double(10));
		Interval2D<Double> interval2D = new Interval2D<Double>(interval, interval);
		
		assertEquals(false, interval2D.contains(new Double(-15), new Double(5)));
	}
	
	@Test
	public void testInterval2DYHigher() {
		
		Interval<Double> interval = new Interval<Double>(new Double(-10), new Double(10));
		Interval2D<Double> interval2D = new Interval2D<Double>(interval, interval);
		
		assertEquals(false, interval2D.contains(new Double(0), new Double(15)));
	}
	
	@Test
	public void testInterval2DYLower() {
		
		Interval<Double> interval = new Interval<Double>(new Double(-10), new Double(10));
		Interval2D<Double> interval2D = new Interval2D<Double>(interval, interval);
		
		assertEquals(false, interval2D.contains(new Double(0), new Double(-15)));
	}
}
