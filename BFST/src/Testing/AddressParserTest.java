
import static org.junit.Assert.assertEquals;
import org.junit.Test;

import AddressParser.AddressParser;

public class AddressParserTest {
	
	private AddressParser ap = AddressParser.getInstance();
/*
	//Test Case A
	@Test 
	public void a() throws AddressParser.NaughtyException {
		assertArrayEquals(new String[]{""}, ap.parseAddress(""));
	}

	//Test Case B
	@Test (expected = AddressParser.NaughtyException.class) 
	public void b() throws AddressParser.NaughtyException {
		assertArrayEquals(new String[]{"A","","","","",""}, ap.parseAddress("A"));
	}
	
	//Test Case C
	@Test (expected = AddressParser.NaughtyException.class) 
	public void c() throws AddressParser.NaughtyException {
		assertArrayEquals(new String[]{"ABCDE","","","","",""}, ap.parseAddress("ABCDE"));
		}
	
	//Test Case D
	@Test (expected = AddressParser.NaughtyException.class) 
	public void d() throws AddressParser.NaughtyException {
		assertArrayEquals(new String[]{"RuedLanggaards","","","","",""}, ap.parseAddress("RuedLanggaards"));
		}
	
	/Test Case E
	@Test (expected = AddressParser.NaughtyException.class) 
	public void e() throws AddressParser.NaughtyException {
		assertArrayEquals(new String[]{""}, ap.parseAddress("RuedLanggaards3"));
		}
	
	//Test Case F
	@Test 
	public void f() throws AddressParser.NaughtyException {
		assertArrayEquals(new String[]{"Rued Langgaards Vej","7","","5","",""}, ap.parseAddress("Rued Langgaards Vej, 7 5. sal"));
		}
	
	//Test Case F2
	@Test 
	public void f2() throws AddressParser.NaughtyException {assertArrayEquals(new String[]{"Rued Langgaards Vej","7","","5","",""}, ap.parseAddress("Rued Langgaards Vej, 7 5 sal"));}
	
	//Test Case F3
	@Test 
	public void f3() throws AddressParser.NaughtyException {
		assertArrayEquals(new String[]{"Rued Langgaards Vej","7","","5","",""}, ap.parseAddress("Rued Langgaards Vej, 7, 5 sal"));
		}
	
	//Test Case G
	@Test 
	public void g() throws AddressParser.NaughtyException {
		assertArrayEquals(new String[]{"Rued Langgaards Vej","123","A","","",""}, ap.parseAddress("Rued Langgaards Vej, 123A"));
		}

	//Test Case H
	@Test 
	public void h() throws AddressParser.NaughtyException {
		assertArrayEquals(new String[]{"Rued Langgaards Vej","","","","2650","Hvidovre"}, ap.parseAddress("Rued Langgaards Vej, 2650 Hvidovre"));
		}
	
	//Test Case I
	@Test 
	public void i() throws AddressParser.NaughtyException {
		assertArrayEquals(new String[]{"Rued Langgaards Vej","","","","","Hvidovre"}, ap.parseAddress("Rued Langgaards Vej, Hvidovre"));
		}
	
	//Test Case J
	@Test 
	public void j() throws AddressParser.NaughtyException {
		assertArrayEquals(new String[]{"Rued Langgaards Vej","7","A","5","2650","Hvidovre"}, ap.parseAddress("Rued Langgaards Vej, 5. sal, 7A, , Hvidovre, 2650"));
		}

	
*/
}