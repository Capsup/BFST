package XMLParser;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;



public class XMLParser
{
	private String[] sNames;
	private String[] sData;
	private XMLEventReader input;
	private String filePath, sPath;


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

	public ArrayList<Node> getNodes() throws Exception{
		ArrayList<Node> tree = new ArrayList<Node>();
		ArrayList<String> temp = new ArrayList<String>();

		double x = 0, y = 0;
		int i = 0;

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
				if( name.equals( "i" ) ) temp.add( input.nextEvent().asCharacters().getData() ); 
				else if( name.equals( "x" ) ) temp.add( input.nextEvent().asCharacters().getData() ); 
				else if( name.equals( "y" ) ) temp.add( input.nextEvent().asCharacters().getData() ); 
			} 
			if( event.isEndElement() )
			{
				EndElement element = event.asEndElement();
				if( element.getName().getLocalPart().equals( "e" )){
					tree.add(new Node(temp)); 
					temp = new ArrayList<String>();
				}
			}
		}
		return tree;

	}

	public ArrayList<LinkedList<Edge>> getEdges() throws Exception
	{

		ArrayList<LinkedList<Edge>> list = new ArrayList<LinkedList<Edge>>();
		
		for(int i = 0; i < 100; i++)
			list.add(new LinkedList<Edge>());
		
		
		ArrayList<String> temp = new ArrayList<String>();

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
<<<<<<< HEAD
				if( name.equals( "x" ) ) //x
					temp.add(input.nextEvent().asCharacters().getData());
				else if( name.equals( "y" ) ) //y
					temp.add(input.nextEvent().asCharacters().getData());
				else if( name.equals( "f" ) ) //From node
					temp.add(input.nextEvent().asCharacters().getData());
				else if( name.equals( "X" ) ) //X
					temp.add(input.nextEvent().asCharacters().getData());
				else if( name.equals( "Y" ) ) //Y
					temp.add(input.nextEvent().asCharacters().getData());
				else if( name.equals( "t" ) ) //To node
					temp.add(input.nextEvent().asCharacters().getData());
				if( name.equals( "ty" ) ){//Type
					type = input.nextEvent().asCharacters().getData();
					temp.add(type);
=======

				if( filePath.contains( "kdv_unload.xml" ) )
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
>>>>>>> refs/remotes/origin/master
				}
			}

			if( event.isEndElement() )
			{
				EndElement element = event.asEndElement();
<<<<<<< HEAD

				if(element.getName().getLocalPart().equals( "e" )){
					list.get(Integer.parseInt(type)).add(new Edge(temp));
					temp = new ArrayList<String>();
=======
				
				if( element.getName().getLocalPart().equals( "element" ) && filePath.contains( "kdv_unload.xml" ) )
				{
					list.add( new Edge( fnode, tnode, length, typ, xFrom, yFrom, xTo, yTo ) );
>>>>>>> refs/remotes/origin/master
				}
			}
		}
		return list;
	}
}

