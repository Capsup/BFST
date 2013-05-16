package XMLParser;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
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
import Graph.Node;

public class XMLParser implements Runnable{
	private XMLEventReader input;

	/**
	 * Contains all edges
	 */
	private static ArrayList<List<Edge>> list = new ArrayList<List<Edge>>(); 
	
	/**
	 * Static to initialize a list of synchronized lists so they can be used with multithreading
	 */
	static{	for(int i = 0; i < 100; i++) list.add(Collections.synchronizedList(new LinkedList<Edge>())); }
	
	/**
	 * Returns the list of all edges
	 * @return all edges
	 */
	public static ArrayList<List<Edge>> getEdgeList(){ return list; }

	/**
	 * Initializes the XMLParser and makes it load the given file
	 * @param sPath a file path
	 * @throws FileNotFoundException
	 */
	public XMLParser( String sPath ) throws FileNotFoundException
	{
		loadFile( sPath );
	}

	/**
	 * Loads the file
	 * @param sPath the file path
	 * @throws FileNotFoundException
	 */
	private void loadFile( String sPath ) throws FileNotFoundException
	{
		URL url = getClass().getResource( sPath );
		if( url == null )
			throw new FileNotFoundException();
		InputStream in = getClass().getResourceAsStream( sPath );
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();

		try
		{
			input = inputFactory.createXMLEventReader( in );
		}
		catch( XMLStreamException e )
		{
			e.printStackTrace();
		}
	}

	/**
	 * Parses the file and creates nodes. The nodes is returned as a list
	 * @return list of nodes
	 * @throws XMLStreamException
	 */
	public ArrayList<Node> getNodes() throws XMLStreamException{
		int id = 0;
		double x = 0,y = 0;
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
				if( name.equals( "i" ) ) id = Integer.parseInt(input.nextEvent().asCharacters().getData()); 
				else if( name.equals( "x" ) ) x = Double.parseDouble(input.nextEvent().asCharacters().getData()); 
				else if( name.equals( "y" ) ) y = Double.parseDouble(input.nextEvent().asCharacters().getData()); 
			} 
			if( event.isEndElement() )
			{
				EndElement element = event.asEndElement();
				if( element.getName().getLocalPart().equals( "e" )){
					list.add(Node.makeNode(id, x, y)); 
				}
			}
		}

		return list;

	}

	/**
	 * Parses a edge XML file. The Edges are saved into a synchronized list and can be accessed with getEdgeList();
	 */
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
					if( name.equals( "f" ) ){ //From node
						f = Integer.parseInt(input.nextEvent().asCharacters().getData());
					}
					else if( name.equals( "t" ) ){ //To node
						t = Integer.parseInt(input.nextEvent().asCharacters().getData());
					}
					else if( name.equals( "l" ) ){ //length
						temp.add(input.nextEvent().asCharacters().getData());
					}
					else if( name.equals( "ty" ) ){//Type
						type = input.nextEvent().asCharacters().getData();
						temp.add(type);
					}

					else if( name.equals( "rn" ) ){//Road name
						String string = input.nextEvent().asCharacters().getData().replace('\'', ' ').trim().replace('-', ' ');

						temp.add(string);
					}

					else if( name.equals( "zi" ) ){//Zip code
						temp.add(input.nextEvent().asCharacters().getData());
					}

					else if( name.equals( "sp" ) ){//Speed limit
						temp.add(input.nextEvent().asCharacters().getData());
					}

					else if( name.equals( "dt" ) ){//Drive time
						temp.add(input.nextEvent().asCharacters().getData());
					}

					else if( name.equals( "fl" ) ){//From left
						temp.add(input.nextEvent().asCharacters().getData());
					}

					else if( name.equals( "tl" ) ){//To left
						temp.add(input.nextEvent().asCharacters().getData());
					}

					else if( name.equals( "fr" ) ){//From right
						temp.add(input.nextEvent().asCharacters().getData());
					}

					else if( name.equals( "tr" ) ){//To right
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

		} catch(Exception e){
			e.printStackTrace(); 
		}
	}
}

