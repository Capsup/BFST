package XMLParser;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Collections;
import java.util.Comparator;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class XMLParser
{
	String[] sNames;
	String[] sData;
	private XMLEventReader input;
	private String filePath;
	private ArrayList list;
	private static ArrayList<Node> nodeList;
	private static ArrayList<Edge> edgeList;

	public static void makeDataSet()
	{
		try
		{
			XMLParser parser = new XMLParser( "C:\\Users\\Jacob\\Desktop\\krak\\kdv_node_unload.xml" );

			nodeList = parser.getElements();

			parser.loadFile( "C:\\Users\\Jacob\\Desktop\\krak\\kdv_unload.xml" );

			edgeList = parser.getElements();

			Comparator<Object> nodeComparator = new Comparator<Object>() {
				public int compare(Object a, Object b) {		
					return ((Node) a).compareTo((Node) b);
				}
			};

			Collections.sort(nodeList, nodeComparator);

		
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}

	public static ArrayList<Edge> getEdgeList(){
		return edgeList;
	}
	
	public static Node nodeSearch(int i){
		int id = Collections.binarySearch(nodeList, new Node(0,3,0,0));
		return id >= 0 ?  nodeList.get(id) : new Node(0,-1,0,0);
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

		if( !file.getPath().contains( "xml" ) )
		{
			Scanner scanner = new Scanner( file );

			String sNameString = scanner.nextLine();
			sNames = sNameString.split( " " );

			int i = 0;
			while( scanner.hasNextLine() )
			{
				scanner.nextLine();
				i++;
			}

			sData = new String[i];

			scanner = new Scanner( file );
			scanner.nextLine();

			while( scanner.hasNextLine() )
				sData[--i] = scanner.nextLine();
		}
		else
		{
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();

			try
			{
				input = inputFactory.createXMLEventReader( new FileInputStream( file ) );
			}
			catch( XMLStreamException e )
			{
				e.printStackTrace();
			}

			list = null;
		}

	}

	public void convertToXML( String sPath ) throws Exception
	{
		loadFile( sPath + ".txt" );

		// Create a XMLOutputFactory
		XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
		// Create XMLEventWriter
		XMLEventWriter eventWriter = outputFactory.createXMLEventWriter( new FileOutputStream( sPath + ".xml" ) );
		// Create a EventFactory
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		XMLEvent end = eventFactory.createDTD( "\n" );
		// Create and write Start Tag
		StartDocument startDocument = eventFactory.createStartDocument();
		eventWriter.add( startDocument );
		eventWriter.add( end );

		// Create config open tag
		StartElement configStartElement = eventFactory.createStartElement( "", "", "elements" );
		eventWriter.add( configStartElement );
		eventWriter.add( end );

		for( int i = 0; i < sData.length; i++ )
		{
			String[] data = sData[i].split( "," );

			eventWriter.add( eventFactory.createDTD( "\t" ) );
			configStartElement = eventFactory.createStartElement( "", "", "element" );
			eventWriter.add( configStartElement );
			eventWriter.add( end );

			for( int j = 0; j < sNames.length; j++ )
			{
				createNode( eventWriter, sNames[j], data[j] );
			}

			eventWriter.add( eventFactory.createDTD( "\t" ) );
			eventWriter.add( eventFactory.createEndElement( "", "", "element" ) );
			eventWriter.add( end );
		}

		eventWriter.add( eventFactory.createEndElement( "", "", "elements" ) );
		eventWriter.add( end );
		eventWriter.add( eventFactory.createEndDocument() );
		eventWriter.close();
	}

	private void createNode( XMLEventWriter eventWriter, String name, String value ) throws XMLStreamException
	{

		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		XMLEvent end = eventFactory.createDTD( "\n" );
		XMLEvent tab = eventFactory.createDTD( "\t\t" );
		// Create Start node
		StartElement sElement = eventFactory.createStartElement( "", "", name );
		eventWriter.add( tab );
		eventWriter.add( sElement );
		// Create Content
		Characters characters = eventFactory.createCharacters( value );
		eventWriter.add( characters );
		// Create End node
		EndElement eElement = eventFactory.createEndElement( "", "", name );
		eventWriter.add( eElement );
		eventWriter.add( end );

	}

	public ArrayList getElements() throws Exception
	{
		if( input == null )
			return null;

		if( list != null )
			return list;

		list = new ArrayList<Object>();
		int arc = 0, id = 0, x = 0, y = 0, fnode = 0, tnode = 0, length = 0;

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

				if( filePath.contains( "kdv_node_unload.xml" ) )
				{
					if( name.equals( "ARC" ) )
						arc = ( int ) Double.parseDouble( input.nextEvent().asCharacters().getData() );
					else if( name.equals( "KDV-ID" ) )
						id = ( int ) Double.parseDouble( input.nextEvent().asCharacters().getData() );
					else if( name.equals( "X-COORD" ) )
						x = ( int ) Double.parseDouble( input.nextEvent().asCharacters().getData() );
					else if( name.equals( "Y-COORD" ) )
						y = ( int ) Double.parseDouble( input.nextEvent().asCharacters().getData() );
				}
				else if( filePath.contains( "kdv_unload.xml" ) )
				{
					if( name.equals( "FNODE" ) )
						fnode = ( int ) Double.parseDouble( input.nextEvent().asCharacters().getData() );
					else if( name.equals( "TNODE" ) )
						tnode = ( int ) Double.parseDouble( input.nextEvent().asCharacters().getData() );
					else if( name.equals( "LENGTH" ) )
						length = ( int ) Double.parseDouble( input.nextEvent().asCharacters().getData() );
				}
			}

			if( event.isEndElement() )
			{
				EndElement element = event.asEndElement();
				if( element.getName().getLocalPart().equals( "element" ) && filePath.contains( "kdv_node_unload.xml" ) )
					list.add( new Node( arc, id, x, y ) );
				else if( element.getName().getLocalPart().equals( "element" ) && filePath.contains( "kdv_unload.xml" ) )
					list.add( new Edge( fnode, tnode, length ) );
			}
		}

		return list;
	}
}
