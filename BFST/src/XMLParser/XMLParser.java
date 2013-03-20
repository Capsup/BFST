package XMLParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class XMLParser
{
	String[] sNames;
	String[] sData;
	private XMLEventReader input;
	private String filePath;
	// private ArrayList list;
	private static ArrayList<Node> nodeList;
	private static ArrayList<Edge> edgeList;

	public static void makeDataSet()
	{
		String path = ( "" + XMLParser.class.getResource( "" ) ).replaceAll( "file:/", "" ).replaceAll( "/", "\\\\\\\\" );
		try
		{
			XMLParser parser = new XMLParser();

			// nodeList = parser.getElements();

			parser.loadFile( path + "kdv_unload.xml" );

			edgeList = parser.getElements();

			// Collections.sort(nodeList);
			Collections.sort( edgeList );

		}
		catch( Exception e )
		{
			System.out.println( e );
		}
	}

	public static ArrayList<Edge> getEdgeList()
	{
		ArrayList<Edge> newEdgeList = new ArrayList<Edge>();
		for( Edge e : edgeList )
			if( e.getTyp() < 100 )
				newEdgeList.add( e );

		return newEdgeList;
	}

	public static Node nodeSearch( int i )
	{
		int id = Collections.binarySearch( nodeList, new Node( 0, i, 0, 0 ) );
		return id >= 0 ? nodeList.get( id ) : new Node( 0, -1, 0, 0 );
	}

	public static int edgeSearch( ArrayList<Edge> edges, double i )
	{
		int id = Collections.binarySearch( edges, new Edge( 0, 0, 0, 0, i, 0, 0, 0 ) );
		return id;
	}

	public XMLParser()
	{

	}

	public XMLParser( String sPath )
	{
		try
		{
			loadFile( sPath );
		}
		catch( FileNotFoundException e )
		{
			e.printStackTrace();
		}
	}

	public void loadFile( String sPath ) throws FileNotFoundException
	{
		File file = new File( sPath );
		if( !file.exists() )
			throw new FileNotFoundException();

		filePath = sPath;

		XMLInputFactory inputFactory = XMLInputFactory.newInstance();

		try
		{
			input = inputFactory.createXMLEventReader( new FileInputStream( file ) );
		}
		catch( XMLStreamException e )
		{
			e.printStackTrace();
		}
		// list = null;

	}

	public ArrayList getElements() throws Exception
	{
		ArrayList<Edge> list = new ArrayList<Edge>();
		int arc = 0, id = 0, fnode = 0, tnode = 0, typ = 0;
		double x = 0, y = 0, yTo = 0, xTo = 0, yFrom = 0, xFrom = 0;
		double length = 0;

		while( input.hasNext() )
		{
			XMLEvent event = input.nextEvent();

			if( event.isStartDocument() || event.isEndDocument() )
				continue;

			if( event.isStartElement() )
			{
				StartElement element = event.asStartElement();
				String name = element.getName().getLocalPart();

				if( name.equals( "elements" ) )
					continue;

				/*
				 * if( filePath.contains( "kdv_node_unload.xml" ) ) { if( name.equals( "ARC" ) ) arc = ( int ) Double.parseDouble(
				 * input.nextEvent().asCharacters().getData() ); else if( name.equals( "KDV-ID" ) ) id = ( int ) Double.parseDouble(
				 * input.nextEvent().asCharacters().getData() ); else if( name.equals( "X-COORD" ) ) x = Double.parseDouble(
				 * input.nextEvent().asCharacters().getData() ); else if( name.equals( "Y-COORD" ) ) y = Double.parseDouble(
				 * input.nextEvent().asCharacters().getData() ); } else
				 */if( filePath.contains( "kdv_unload.xml" ) )
				{

					if( name.equals( "xFrom" ) )
						xFrom = ( int ) Double.parseDouble( input.nextEvent().asCharacters().getData() );
					else if( name.equals( "yFrom" ) )
						yFrom = ( int ) Double.parseDouble( input.nextEvent().asCharacters().getData() );
					if( name.equals( "xTo" ) )
						xTo = ( int ) Double.parseDouble( input.nextEvent().asCharacters().getData() );
					else if( name.equals( "yTo" ) )
						yTo = ( int ) Double.parseDouble( input.nextEvent().asCharacters().getData() );
					else if( name.equals( "LENGTH" ) )
						length = Double.parseDouble( input.nextEvent().asCharacters().getData() );
					else if( name.equals( "TYP" ) )
						typ = ( int ) Double.parseDouble( input.nextEvent().asCharacters().getData() );
				}
			}

			if( event.isEndElement() )
			{
				EndElement element = event.asEndElement();
				/*
				 * if( element.getName().getLocalPart().equals( "element" ) && filePath.contains( "kdv_node_unload.xml" ) ) list.add( new Node( arc,
				 * id, x, y ) ); else
				 */
				if( element.getName().getLocalPart().equals( "element" ) && filePath.contains( "kdv_unload.xml" ) )
				{
					list.add( new Edge( fnode, tnode, length, typ, xFrom, yFrom, xTo, yTo ) );
				}
			}

		}

		return list;
	}
}
