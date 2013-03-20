package XMLParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

public class XMLCreator
{

	String[] sNames;
	String[] sData;
	private XMLEventReader input;
	private String filePath;

	public static void main( String[] args )
	{
		XMLParser.makeDataSet();
		try
		{
			new XMLCreator().convertToXML( "C://Users//Jacob//Desktop//krak-data//kdv_unload" );
		}
		catch( Exception e )
		{
			System.out.println( "I FAILED!" );
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
			// sNames = sNameString.split( "," );

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

				if( j == 0 )
				{
					Node nodeFrom = XMLParser.nodeSearch( Integer.parseInt( data[j] ) );
					createNode( eventWriter, "xFrom", "" + nodeFrom.getXCoord() );
					createNode( eventWriter, "yFrom", "" + nodeFrom.getYCoord() );
				}
				if( j == 1 )
				{
					Node nodeTo = XMLParser.nodeSearch( Integer.parseInt( data[j] ) );
					createNode( eventWriter, "xTo", "" + nodeTo.getXCoord() );
					createNode( eventWriter, "yTo", "" + nodeTo.getYCoord() );
				}

				if( j == 5 )
				{
					createNode( eventWriter, sNames[j], data[j] );
				}
			}

			eventWriter.add( eventFactory.createDTD( "\t" ) );
			eventWriter.add( eventFactory.createEndElement( "", "", "element" ) );
			eventWriter.add( end );
		}

		eventWriter.add( eventFactory.createEndElement( "", "", "elements" ) );
		eventWriter.add( end );
		eventWriter.add( eventFactory.createEndDocument() );
		eventWriter.close();
		System.out.println( "done" );
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
