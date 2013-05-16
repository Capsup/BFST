package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.awt.geom.Point2D;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import AddressParser.AddressParser;
import Graph.Edge;
import MapDraw.MapDraw;
import MapDraw.Translation;
import MapDraw.ZoomLevel;

/**
 * A JTextField that is used for searching through the edges in our map. 
 * The searchField can access input, and displays an array of results using a dropdown menu
 * @author Jonas Kastberg
 *
 */
public class SearchField extends JTextField
{
	private Dropdown dropdown;			//The associated dropdown of this searchField
	
	private String baseString;			//The string that the searchField reverts to in case it is empty
	
	private int searchSize = 5;			//The amount of search hits the searchField generates
	
	/**
	 * 
	 * @param text: The basic string that the field reverts to whenever it is empty
	 * @param size: Amount of letters you can have in the search field
	 */
	public SearchField(String text, int size)
	{
		//Call the super of the class in order to set the text and max string size
		super(text, size);
		
		//Assing the base string
		baseString = text;
		
		//Set the font color of the TextField to gray (Since it has the baseString)
		setForeground(Color.GRAY);
		
		//Add a custom DocumentListener to our SearchField
		this.getDocument().addDocumentListener(new SearchListener());
		
		//Initialize the associated Dropdown to our SearchField
		dropdown = new Dropdown(this);
		
		//Add a custom focus listener to our SearchField
		this.addFocusListener(new MyFocusListener());
		
		//add a custom component listener aswell as a window focus listener to the main frame of the program.
		MainFrame.getInstance().addComponentListener(new MyComponentListener());
		MainFrame.getInstance().addWindowFocusListener(new MyWindowListener());
	}
	
	/**
	 * A custom DocumentListener that lets us check whenever the searchField has changed it's underlying document
	 * @author Jonas Kastberg
	 */
	class SearchListener implements DocumentListener
	{
		@Override
		public void changedUpdate(DocumentEvent e) {
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			
			//Whenever we insert anything into the textField we generate suggestions
			getSuggestions();

		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			//Whenever we remove anything from the textField we generate suggestions
			getSuggestions();

		}
	}
	
	/**
	 * A Custom focus listener that lets us check whenever the SearchField gains or loses focus.
	 * @author Jonas Kastberg
	 */
	class MyFocusListener implements FocusListener
	{
		@Override
		public void focusGained(FocusEvent e) {

			//Whenever we gain focus we check if we currently have the baseString.
			if(getText().equals(baseString))
			{
				//If we do, we clear the field and set the proper font color
				setForeground(Color.BLACK);
				setText("");
			}
			
			//We then get suggestions based on the current input
			getSuggestions();
		}

		@Override
		public void focusLost(FocusEvent e) {
			
			//Whenever we gain focus we check if our searchField is blank
			if(getText().equals(""))
			{
				//If it is, we revert to our baseString and set the font color back to gray
				setForeground(Color.GRAY);
				setText(baseString);
			}
			
			//We furthermore make the Dropdown dissapear, since we won't need it for now
			dropdown.setVisible(false);
		}
	}
	
	/**
	 * Custom Component listener that check whenever the main frame has moved
	 * @author Jonas Kastberg
	 */
	class MyComponentListener implements ComponentListener
	{

		@Override
		public void componentHidden(ComponentEvent e) {
			
		}

		@Override
		public void componentMoved(ComponentEvent e) {
			//Whenver the main frame moves, we make sure to update the position of our dropdown menu
			dropdown.updatePosition();
		}

		@Override
		public void componentResized(ComponentEvent e) {
			
		}

		@Override
		public void componentShown(ComponentEvent e) {
			
		}
	}
	
	/**
	 * A custom WindowFocusListener that checks whenever we gain or loses focus on our main frame window
	 * @author Jonas Kastberg
	 */
	class MyWindowListener implements WindowFocusListener
	{
		@Override
		public void windowGainedFocus(WindowEvent e) {
			//Whenever we gain focus we hide the dropdown
			dropdown.setVisible(false);
		}

		@Override
		public void windowLostFocus(WindowEvent e) {
			//Whenever we lose focus we hide the dropdown
			dropdown.setVisible(false);
		}
	}

	/**
	 * Prompt the AddressParser singleton to search for the current search field input, in order to get some suggestions
	 */
	private void getSuggestions()
	{
		//We start out by initiating a string array and parse the addres in our search field using our address parser
		String[] parsedAdress = AddressParser.getInstance().parseAddress(getText());
		
		//We then make a probability search based on the parsed address to get an array of results 
		int[][] ints = intProbabilitySearch(parsedAdress);
		
		//We check whether the results are valid
		if(ints.length > 0 && ints[0][0] >= 0)
		{
			//If they are, we initiate a new string array for our Dropdown
			String[] strings = new String[ints.length];
			
			//We then iterate through each string
			for(int i=0; i<strings.length; i++)
			{
				//We first find an edge based on the i'th search result
				Edge edge = AddressParser.getInstance().getRoads()[ints[i][0]].getEdge(ints[i][1]);
				
				//We then check what data we received from our parser, to determine what to output
				//Based on the input, we populate the string array with strings that represent each of the edges we have found using our search.
				if(parsedAdress[1].equals("") || ints[i][0] == 0 || edge.getToRight() == 0)
					strings[i] = edge.getName()+", "+edge.getZipString();
				else
					strings[i] = edge.getName()+" "+edge.getToRight()+", "+edge.getZipString();
			}
			
			//We then set the content of our associated dropdown
			dropdown.setContent(strings, true);
		}
	}
	
	/**
	 * @return returns an edge based on the current input from the searchField
	 */
	public Edge edgeSearch()
	{
		//We initiate a string array and parse it
		String[] parsedAdress = AddressParser.getInstance().parseAddress(getText());;
		
		//We make an search for the integer representing the edge we wish to find based on the parsed adress
		int[] searchIndex = intSearch(parsedAdress);

		//We check whether the search has found a valid result
		if(searchIndex[0] >= 0)
		{
			//If it has, we get the associated edge
			Edge edge = AddressParser.getInstance().getRoads()[searchIndex[0]].getEdge(searchIndex[1]);
			
			//We the hide the dropdown menu
			getDropdown().setVisible(false);
			
			//And return the found edge
			return edge;
		}
		else {
			System.out.println("Edge not found, trying probability search");
			
			//If we do not find any specific edge during our search, we make a probability search, to get the 5 most likely edges
			Edge[] edges = edgeProbabilitySearch(parsedAdress);
			
			//If we find any edges during this search
			if(edges != null)
			{
				//We return the first one
				Edge edge = edges[0];
				
				//And update the searchField based on the result, aswell as what data we parsed in, in the first place.
				if(parsedAdress[1].equals(""))
					setText(edge.getName()+", "+edge.getZipString());
				else
					setText(edge.getName()+" "+parsedAdress[1]+", "+edge.getZipString());
				
				//We the hide the dropdown menu
				getDropdown().setVisible(false);
				
				//And retun the edge
				return edges[0];
			}
			else
				return null;	//Otherwise we return null
		}
	}
	
	/**
	 * Gets the index found through a search based on the parsedAdress.
	 * @param parsedAdress
	 * @return the resultin index of the found adress. If no adress was found it returns an array with -1
	 */
	public int[] intSearch(String[] parsedAdress)
	{
		int[] searchResult = AddressParser.getInstance().search(parsedAdress);
		
		return searchResult;
	}
	
	/**
	 * Gets an array of probable search hit edges based on the parsed address
	 * @param parsedAdress
	 * @return an array of probable search hit edges based on the parsed address. Returns null if nothing was found.
	 */
	public Edge[] edgeProbabilitySearch(String[] parsedAdress)
	{
		//We get an array of search result indexes using the parsedAdress
		int[][] searchResult = intProbabilitySearch(parsedAdress);
		
		//We then process the results and see if any of them are null
		for(int i=0; i<searchResult.length; i++)
		{
			if(searchResult[i][0] < 0)
			{
				return null;
			}
		}
		
		//If they are all valid search hits we store them in an array, and return them
		Edge[] edges = new Edge[searchResult.length];
		
		for(int i=0; i<searchResult.length; i++)
			edges[i] = AddressParser.getInstance().getRoads()[searchResult[i][0]].getEdge(searchResult[i][1]);
		
		return edges;
	}
	
	/**
	 * Gets an array of probable search hit ints based on the parsed address
	 * @param parsedAdress
	 * @return an array of probable search hit ints based on the parsed address. Returns null if nothing was found.
	 */
	public int[][] intProbabilitySearch(String[] parsedAdress)
	{
		//First we check if our searchField has any text
		if(getText().length() > 0)
		{
			if(parsedAdress[0].length() > 0)
			{
				//If the parsed address has a road name we get an array of index search hits using our AddressParser			
				int[][] searchResult = AddressParser.getInstance().probabilitySearch(parsedAdress, searchSize);
				
				//We then validate the search
				if(searchResult[0][0] < 0)
				{
					dropdown.setContent(new String[]{"-No Matching Result-"}, false);	//In case of invalid search, we set the content of the dropdown menu to display an error message
					
					//And return an invalid output
					return new int[][]{{-1}};
				}
				
				//Otherwise we return the array of search hits
				return searchResult;
			}
			else
			{
				//If the parsed address does not contain a road name, we set the content of the dropdown menu to an error message
				dropdown.setContent(new String[]{"-Invalid Character Input-"}, false);	
				//And return an invalid output
				return new int[][]{{-1}};
			}
		}
		else {
			//if it has not we hide the dropdown and return an invalid result
			dropdown.setVisible(false);
			return new int[][]{{-1}};
		}
	}
	
	public Dropdown getDropdown()
	{
		return dropdown;
	}
}
