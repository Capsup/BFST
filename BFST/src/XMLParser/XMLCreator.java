package XMLParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import Graph.Node;
public class XMLCreator
{

	String[] sNames;
	String[] sData;
	private String filePath;
	private static ArrayList<Node> nodes;


	public static Node nodeSearch(int i){
		ArrayList<String> s = new ArrayList<String>();
		s.add("" + i); s.add("1"); s.add("1");
		return nodes.get(Collections.binarySearch(nodes, new Node(s)));
	}

	public static void main( String[] args )
	{
		try
		{
			XMLParser node = new XMLParser("kdv_node_unload.xml"); nodes = node.getNodesForXMLCreator();
			Collections.sort(nodes);
			new XMLCreator().convertToXML( "C://Users//Jacob//Desktop//krak-data//kdv_unload" );
		}
		catch( Exception e )
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
			sNameString.replaceAll( "#", "" );
			sNames = sNameString.split( " " );
			//sNames = sNameString.split( "," );

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

	}

	public void convertToXML( String sPath ) throws Exception
	{
		loadFile( sPath + ".txt" );

		for( int j = 0; j < sNames.length; j++ )
			System.out.println( j + ": " + sNames[j] );
		int d = 12;
		for(int k = 0; k < d; k++){

				// Create a XMLOutputFactory
				XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
				// Create XMLEventWriter
				XMLEventWriter eventWriter = outputFactory.createXMLEventWriter( new FileOutputStream( sPath + "_" + (k + 1) + ".xml" ));
				// Create a EventFactory
				XMLEventFactory eventFactory = XMLEventFactory.newInstance();
				XMLEvent end = eventFactory.createDTD( "\n" );
				// Create and write Start Tag
				StartDocument startDocument = eventFactory.createStartDocument();
				eventWriter.add( startDocument );
				eventWriter.add( end );

				// Create config open tag
				StartElement configStartElement = eventFactory.createStartElement( "", "", "g" );
				eventWriter.add( configStartElement );
				eventWriter.add( end );

				for( int i = 0; i < sData.length / d; i++ )
				{
					String[] data = sData[i].split( "," );

					eventWriter.add( eventFactory.createDTD( "\t" ) );
					configStartElement = eventFactory.createStartElement( "", "", "e" );
					eventWriter.add( configStartElement );
					eventWriter.add( end );

					for( int j = 0; j < sNames.length; j++ )
					{

						if( j == 0 )
						{
							Node nodeFrom = nodeSearch( Integer.parseInt( data[j] ) );
							createNode( eventWriter, "x", "" + nodeFrom.getX() );
							createNode( eventWriter, "y", "" + nodeFrom.getY() );
							createNode( eventWriter, "f", data[j] );
						}
						if( j == 1 )
						{
							Node nodeTo = nodeSearch( Integer.parseInt( data[j] ) );
							createNode( eventWriter, "X", "" + nodeTo.getX() );
							createNode( eventWriter, "Y", "" + nodeTo.getY() );
							createNode( eventWriter, "t", data[j] );
						}

						if( j == 2 )
						{
							createNode( eventWriter, "l", data[j] );
						}

						if( j == 5 )
						{
							createNode( eventWriter, "ty", data[j] );
						}

						if( j == 6 )
						{
							createNode( eventWriter, "rn", data[j] );
						}

						if( j == 25 )
						{
							createNode( eventWriter, "sp", data[j] );
						}

						if( j == 26 )
						{
							createNode( eventWriter, "dt", data[j] );
						}

						if( j == 27 )
						{
							createNode( eventWriter, "ow", data[j] );
						}

						if( j == 17 )
						{
							createNode( eventWriter, "zi", data[j] );
						}
					}

					eventWriter.add( eventFactory.createDTD( "\t" ) );
					eventWriter.add( eventFactory.createEndElement( "", "", "e" ) );
					eventWriter.add( end );
				}

				eventWriter.add( eventFactory.createEndElement( "", "", "g" ) );
				eventWriter.add( end );
				eventWriter.add( eventFactory.createEndDocument() );
				eventWriter.close();
				System.out.println( "done" );
			}
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

}
