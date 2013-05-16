package XMLCreator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Scanner;

import javax.xml.stream.XMLEventFactory;
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
	private String filePath;

	public static void main( String[] args )
	{
		try
		{
			new XMLCreator().convertToXML( "Z://kdv_unload" );
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
		int d = 42;
		for(int k = 0; k < d; k++){
			new SubTree(k, sPath).start();			
		}

	}

	public class SubTree extends Thread {
		private int k;
		private String sPath;

		public SubTree(int k, String sPath){
			this.k = k;
			this.sPath = sPath;
		}

		public void run(){
			try{
				// Create a XMLOutputFactory
				XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
				// Create XMLEventWriter
				XMLEventWriter eventWriter = outputFactory.createXMLEventWriter( new FileOutputStream( sPath + "_" + (k + 1) + ".xml" ), "ISO-8859-1");
				// Create a EventFactory
				XMLEventFactory eventFactory = XMLEventFactory.newInstance();
				XMLEvent end = eventFactory.createDTD( "\n" );
				// Create and write Start Tag
				StartDocument startDocument = eventFactory.createStartDocument("ISO-8859-1");
				eventWriter.add( startDocument );
				eventWriter.add( end );

				// Create config open tag
				StartElement configStartElement = eventFactory.createStartElement( "", "", "g" );
				eventWriter.add( configStartElement );
				eventWriter.add( end );

				for( int i = 20000 * k; i < 20000 * (k+1); i++ )
				{
					String[] data;
					try{
						data = sData[i].split(",(?=([^\']*\'[^\']*\')*[^\']*$)");
					}
					catch(Exception e){continue;}
					
					
					

					eventWriter.add( eventFactory.createDTD( "\t" ) );
					configStartElement = eventFactory.createStartElement( "", "", "e" );
					eventWriter.add( configStartElement );
					eventWriter.add( end );

					for( int j = 0; j < sNames.length; j++ )
					{

						if( j == 0 )
						{
							createNode( eventWriter, "f", data[j] );
						}
						if( j == 1 )
						{
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
						
						if( j == 7){
							createNode( eventWriter, "fl", data[j] );
						}
						
						if( j == 8){
							createNode( eventWriter, "tl", data[j] );
						}
						
						if( j == 9){
							createNode( eventWriter, "fr", data[j] );
						}
						
						if( j == 10){
							createNode( eventWriter, "tr", data[j] );
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
			catch(Exception e){e.printStackTrace();}
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
