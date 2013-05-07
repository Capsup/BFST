package XMLParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import Graph.Edge;
import Graph.Graph;
import Graph.Node;



public class XMLParser implements Runnable{
	private String[] sNames;
	private String[] sData;
	private XMLEventReader input;
	private String filePath, sPath;
	private static ArrayList<List<Edge>> list = new ArrayList<List<Edge>>();

	public static ArrayList<List<Edge>> getEdgeList(){
		return list;
	}
	
	static{
		for(int i = 0; i < 100; i++)
			list.add(Collections.synchronizedList(new LinkedList<Edge>()));
	}


	public XMLParser( String sPath )
	{
		sPath = ( "" + XMLParser.class.getResource( "" ) ).replaceAll( "file:/", "" ).replaceAll( "/", "\\\\\\\\" ).replaceAll("%20", " ") + sPath;
		System.out.println(sPath);
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
	}

	public ArrayList<Node> getNodesForXMLCreator() throws Exception{
		ArrayList<String> temp = new ArrayList<String>();
		ArrayList<Node> list = new ArrayList<Node>();

		while( input.hasNext() )
		{

			XMLEvent event = input.nextEvent();

			if( event.isStartDocument() || event.isEndDocument() )
				continue;

			if( event.isStartElement() )
			{
				StartElement element = event.asStartElement();
				String name = element.getName().getLocalPart();

				if( name.equals( "g" ) )
					continue;
				if( name.equals( "i" ) ) temp.add((input.nextEvent().asCharacters().getData())); 
				else if( name.equals( "x" ) ) temp.add( input.nextEvent().asCharacters().getData() ); 
				else if( name.equals( "y" ) ) temp.add( input.nextEvent().asCharacters().getData() ); 
			} 
			if( event.isEndElement() )
			{
				EndElement element = event.asEndElement();
				if( element.getName().getLocalPart().equals( "e" )){
					list.add(new Node(temp)); 
					temp = new ArrayList<String>();
				}
			}
		}

		return list;

	}


	public void run(){
		try{
			ArrayList<String> temp = new ArrayList<String>();
			int f = 0, t = 0;
			String type = "";
			while( input.hasNext() )
			{
				XMLEvent event = input.nextEvent();

				if( event.isStartDocument() || event.isEndDocument() )
					continue;

				if( event.isStartElement() )
				{
					StartElement element = event.asStartElement();
					String name = element.getName().getLocalPart();

					if( name.equals( "g" ) )
						continue;
					if( name.equals( "x" ) ) //x
						temp.add(input.nextEvent().asCharacters().getData());
					else if( name.equals( "y" ) ) //y
						temp.add(input.nextEvent().asCharacters().getData());
					else if( name.equals( "f" ) ){ //From node
						f = Integer.parseInt(input.nextEvent().asCharacters().getData());
						temp.add(f + "");
					}
					else if( name.equals( "X" ) ) //X
						temp.add(input.nextEvent().asCharacters().getData());
					else if( name.equals( "Y" ) ) //Y
						temp.add(input.nextEvent().asCharacters().getData());
					else if( name.equals( "t" ) ){ //To node
						t = Integer.parseInt(input.nextEvent().asCharacters().getData());
						temp.add(t + "");
					}
					else if( name.equals( "l" ) ){ //length
						temp.add(input.nextEvent().asCharacters().getData());
					}
					else if( name.equals( "ty" ) ){//Type
						type = input.nextEvent().asCharacters().getData();
						temp.add(type);
					}

					else if( name.equals( "rn" ) ){//Road name
						String string = input.nextEvent().asCharacters().getData().replace('\'', ' ').trim();
						//string = string.substring(1, string.length()-1);
						
						//System.out.println(string);
						
						temp.add(string);
					}

					else if( name.equals( "zi" ) ){//Zipcode
						temp.add(input.nextEvent().asCharacters().getData());
					}

					else if( name.equals( "sp" ) ){//Speed limit
						temp.add(input.nextEvent().asCharacters().getData());
					}

					else if( name.equals( "dt" ) ){//Drive time
						temp.add(input.nextEvent().asCharacters().getData());
					}

					else if( name.equals( "ow" ) ){//One way
						temp.add(input.nextEvent().asCharacters().getData());
					}
				}

				if( event.isEndElement() )
				{
					EndElement element = event.asEndElement();

					if(element.getName().getLocalPart().equals( "e" )){
						list.get(Integer.parseInt(type)).add(new Edge(f, t, temp));
						temp = new ArrayList<String>();
					}
				}
			}
			
		} catch(Exception e){}
	}
}

