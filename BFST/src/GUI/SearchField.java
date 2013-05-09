package GUI;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.Point2D;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import Graph.Edge;
import MapDraw.MapDraw;
import MapDraw.Translation;
import MapDraw.ZoomLevel;
import XMLParser.AddressParser;
import XMLParser.AddressParser.NaughtyException;

public class SearchField extends JTextField
{
	private Dropdown dropdown;
	
	private String baseString;
	
	public SearchField(String text, int size)
	{
		super(text, size);
		
		baseString = text;
		
		DocumentListener listener = new SearchListener();
		this.getDocument().addDocumentListener(listener);
		
		dropdown = new Dropdown(this);
		
		FocusListener focusListener = new MyFocusListener();
		this.addFocusListener(focusListener);
		
		ComponentListener myComponentListener = new MyComponentListener();
		
		MainFrame.getInstance().addComponentListener(myComponentListener);
	}
	
	class MyComponentListener implements ComponentListener
	{

		@Override
		public void componentHidden(ComponentEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void componentMoved(ComponentEvent e) {
			
			dropdown.updatePosition();
		}

		@Override
		public void componentResized(ComponentEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void componentShown(ComponentEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	class MyFocusListener implements FocusListener
	{
		@Override
		public void focusGained(FocusEvent e) {

			if(!getText().equals(baseString))
				getSuggestions();
		}

		@Override
		public void focusLost(FocusEvent e) {
			dropdown.setVisible(false);
		}

	}
	
	class SearchListener implements DocumentListener
	{
		@Override
		public void changedUpdate(DocumentEvent e) {
			// TODO Auto-generated method stub
			//getSuggestions();
			System.out.println("Changed");
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			// TODO Auto-generated method stub
			getSuggestions();

			System.out.println("insert");
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			// TODO Auto-generated method stub
			getSuggestions();

			System.out.println("removed");
		}
	}
	
	private void getSuggestions()
	{
		Edge[] edges = edgeProbabilitySearch();
		
		if(edges != null)
		{
			String[] strings = new String[edges.length];
			
			for(int i=0; i<strings.length; i++)
			{
				strings[i] = edges[i].getAddress();
			}
			
			dropdown.setContent(strings, true);
		}
		//buttonListener = new ButtonListener();
	}
	
	public Edge edgeSearch()
	{
		int searchIndex = intSearch();

		if(searchIndex >= 0)
			return AddressParser.getInstance().getRoads()[searchIndex];
		else {
			Edge[] edges = edgeProbabilitySearch();
			
			if(edges != null)
				return edgeProbabilitySearch()[0];
		}
		
		return null;
	}
	
	public int intSearch()
	{
		String[] parsedAdress = new String[1];
		
		try {
			parsedAdress = AddressParser.getInstance().parseAddress(getText());
		} catch (NaughtyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int searchResult = AddressParser.getInstance().search(parsedAdress[0]);
		
		return searchResult;
	}
	
	public Edge[] edgeProbabilitySearch()
	{
		int[] searchResult = intProbabilitySearch();
		
		for(int i=0; i<searchResult.length; i++)
		{
			if(searchResult[i] < 0)
			{
				return null;
			}
		}
		
		Edge[] edges = new Edge[searchResult.length];
		
		for(int i=0; i<searchResult.length; i++)
		{
			edges[i] = AddressParser.getInstance().getRoads()[searchResult[i]];
		}
		
		return edges;
	}
	
	public int[] intProbabilitySearch()
	{
		if(getText().length() > 0)
		{
			/*
			String[] parsedAdress = new String[6];
			
			try {
				parsedAdress = AddressParser.getInstance().parseAddress(getText());
			} catch (NaughtyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			for(int i=0; i<parsedAdress.length; i++)
			{
				System.out.println("Index "+i+": "+parsedAdress[i]);
			}*/
			
			//if(parsedAdress[0].length() > 0)
			//{
				int[] searchResult = AddressParser.getInstance().probabilitySearch(getText(), 5);
				
				if(searchResult[0] < 0)
					dropdown.setContent(new String[]{"-No Matching Result-"}, false);

				return searchResult;
			//}
			//else
			//{
			//	dropdown.setContent(new String[]{"-Invalid Character Input-"}, false);
			//
			//	return new int[]{-1};
			//}
		}
		else {
			dropdown.setVisible(false);
			return new int[]{-1};
		}
	}
	
	public Dropdown getDropdown()
	{
		return dropdown;
	}
}
