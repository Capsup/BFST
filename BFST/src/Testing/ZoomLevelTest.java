package Testing;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import MapDraw.ZoomLevel;

public class ZoomLevelTest {

	//Zoom In
	
	@Test
	public void testCanZoomInAtUpperBoundary() {
		
		ZoomLevel.getInstance().setZoomLevel(0);
		
		assertEquals(true, ZoomLevel.getInstance().zoomIn());
	}
	
	@Test
	public void testCanZoomInAtLowerBoundary() {
		
		ZoomLevel.getInstance().setZoomLevel(ZoomLevel.getInstance().getZoomLevelAmount()-1);
		
		assertEquals(false, ZoomLevel.getInstance().zoomIn());
	}
	
	@Test
	public void testCanZoomInAtMiddlePosition() {
		
		ZoomLevel.getInstance().setZoomLevel(ZoomLevel.getInstance().getZoomLevelAmount()/2);
		
		assertEquals(true, ZoomLevel.getInstance().zoomIn());
	}
	
	@Test
	public void testCanZoomInWorks() {
		
		
		ZoomLevel.getInstance().setZoomLevel(0);
		
		int preZoomLevel = ZoomLevel.getInstance().getZoomIndex();
		
		ZoomLevel.getInstance().zoomIn();
		
		assertEquals(preZoomLevel+1, ZoomLevel.getInstance().getZoomIndex());
	}
	
	//Zoom Out
	
	@Test
	public void testCanZoomOutAtUpperBoundary() {
		
		ZoomLevel.getInstance().setZoomLevel(0);
		
		assertEquals(false, ZoomLevel.getInstance().zoomOut());
	}
	
	@Test
	public void testCanZoomOutAtLowerBoundary() {
		
		ZoomLevel.getInstance().setZoomLevel(ZoomLevel.getInstance().getZoomLevelAmount()-1);
		
		assertEquals(true, ZoomLevel.getInstance().zoomOut());
	}
	
	@Test
	public void testCanZoomOutAtMiddlePosition() {
		
		ZoomLevel.getInstance().setZoomLevel(ZoomLevel.getInstance().getZoomLevelAmount()/2);
		
		assertEquals(true, ZoomLevel.getInstance().zoomOut());
	}
	
	@Test
	public void testCanZoomOutWorks() {
		
		ZoomLevel.getInstance().setZoomLevel(ZoomLevel.getInstance().getZoomLevelAmount()-1);
		
		int preZoomLevel = ZoomLevel.getInstance().getZoomIndex();
		
		ZoomLevel.getInstance().zoomOut();
		
		assertEquals(preZoomLevel-1, ZoomLevel.getInstance().getZoomIndex());
	}
	
	//Find Zoom Index
	
	@Test
	public void testFindZoomWorks() {
		
		int targetIndex = ZoomLevel.getInstance().getZoomLevelAmount()/2;
		
		double difference = ZoomLevel.getInstance().getZoomLevel(targetIndex)-ZoomLevel.getInstance().getZoomLevel(targetIndex+1);
		double testZoomLevel = ZoomLevel.getInstance().getZoomLevel(targetIndex)-(difference/2);
		
		assertEquals(targetIndex, ZoomLevel.getInstance().findIndex(testZoomLevel));
	}
	
	@Test
	public void testFindZoomBelowLowerBoundary() {
		
		double testZoomLevel = 2;
		
		assertEquals(0, ZoomLevel.getInstance().findIndex(testZoomLevel));
	}
	
	@Test
	public void testFindZoomAtLowerBoundary() {
		
		double testZoomLevel = 1.5;
		
		assertEquals(0, ZoomLevel.getInstance().findIndex(testZoomLevel));
	}
	
	@Test
	public void testFindZoomAboveUpperBoundary() {
		
		double testZoomLevel = -1;
		
		assertEquals(ZoomLevel.getInstance().getZoomLevelAmount()-1, ZoomLevel.getInstance().findIndex(testZoomLevel));
	}
	
	@Test
	public void testFindZoomAtUpperBoundary() {
		
		double testZoomLevel = ZoomLevel.getInstance().getZoomLevel(ZoomLevel.getInstance().getZoomLevelAmount()-1);
		
		assertEquals(ZoomLevel.getInstance().getZoomLevelAmount()-1, ZoomLevel.getInstance().findIndex(testZoomLevel));
	}
}
