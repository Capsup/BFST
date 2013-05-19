package Testing;

import static org.junit.Assert.*;

import org.junit.Test;

public class PathInformationTest {

	@Test
	public void testGetLength1KM() {
		
		GUI.PathInformation.getInstance().setLength(1000);
		
		assertEquals("1.0km", GUI.PathInformation.getInstance().getLength());
	}
	
	@Test
	public void testGetLengthBelowKM() {
		
		GUI.PathInformation.getInstance().setLength(999);
		
		assertEquals("999m", GUI.PathInformation.getInstance().getLength());
	}
	
	@Test
	public void testGetLengthBigNumber() {
		
		GUI.PathInformation.getInstance().setLength(80547);

		assertEquals("80.5km", GUI.PathInformation.getInstance().getLength());
	}
	
	@Test
	public void testGetLength0() {
		
		GUI.PathInformation.getInstance().setLength(0);
		
		assertEquals("0m", GUI.PathInformation.getInstance().getLength());
	}
	
	@Test
	public void testGetLengthFailure() {
		
		GUI.PathInformation.getInstance().setLength(-1);
		
		assertEquals("Calculating...", GUI.PathInformation.getInstance().getLength());
	}
	
	//Test the travel time
	@Test
	public void testGetTravelTime1Hour() {
		
		GUI.PathInformation.getInstance().setTravelTime(1);
		
		assertEquals("1 hour", GUI.PathInformation.getInstance().getTravelTime());
	}
	
	@Test
	public void testGetTravelTimeLessThanHour() {
		
		GUI.PathInformation.getInstance().setTravelTime(0.5);
		
		assertEquals("30 minutes", GUI.PathInformation.getInstance().getTravelTime());
	}
	
	@Test
	public void testGetTravelTimeMoreThanHour() {
		
		GUI.PathInformation.getInstance().setTravelTime(3.25);
		
		assertEquals("3 hours 15 minutes", GUI.PathInformation.getInstance().getTravelTime());
	}
	
	@Test
	public void testGetTravelTimeLessThanMinute() {
		
		GUI.PathInformation.getInstance().setTravelTime(0.01);
		
		assertEquals("36 seconds", GUI.PathInformation.getInstance().getTravelTime());
	}
	
	@Test
	public void testGetTravelTime1Second() {
		
		GUI.PathInformation.getInstance().setTravelTime(0.00027);
		
		assertEquals("1 second", GUI.PathInformation.getInstance().getTravelTime());
	}
	
	@Test
	public void testGetTravelTimeFailure() {
		
		GUI.PathInformation.getInstance().setTravelTime(-1);
		
		assertEquals("Calculating...", GUI.PathInformation.getInstance().getTravelTime());
	}
}
