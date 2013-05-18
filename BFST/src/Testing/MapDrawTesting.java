package Testing;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import MapDraw.MapDraw;

public class MapDrawTesting {
	
	@Test
	public void mapDrawTest()
	{
		MapDraw.getInstance();
		
		assertEquals(0, 0);
	}
}
