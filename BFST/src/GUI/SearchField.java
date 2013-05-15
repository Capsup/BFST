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
	
	private int searchSize = 5;
	
	public SearchField(String text, int size)
	{
		super(text, size);
		
		baseString = text;
		
		setForeground(Color.GRAY);
		
		this.getDocument().addDocumentListener(new SearchListener());
		
		dropdown = new Dropdown(this);
		
		this.addFocusListener(new MyFocusListener());
		
		MainFrame.getInstance().addComponentListener(new MyComponentListener());
		MainFrame.getInstance().addWindowFocusListener(new MyWindowListener());
	}
	
	class MyWindowListener implements WindowFocusListener
	{

		@Override
		public void windowGainedFocus(WindowEvent e) {
			dropdown.setVisible(false);
		}

		@Override
		public void windowLostFocus(WindowEvent e) {
			dropdown.setVisible(false);
		}
		
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

			if(getText().equals(baseString))
			{
				setForeground(Color.BLACK);
				setText("");
			}
			
			getSuggestions();
		}

		@Override
		public void focusLost(FocusEvent e) {
			
			if(getText().equals(""))
			{
				setForeground(Color.GRAY);
				setText(baseString);
			}
			
			dropdown.setVisible(false);
		}

	}
	
	class SearchListener implements DocumentListener
	{
		@Override
		public void changedUpdate(DocumentEvent e) {
			// TODO Auto-generated method stub
			//getSuggestions();
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			// TODO Auto-generated method stub
			getSuggestions();

		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			// TODO Auto-generated method stub
			getSuggestions();

		}
	}
	
	private void getSuggestions()
	{
		String[] parsedAdress = new String[6];
		
		try {
			parsedAdress = AddressParser.getInstance().parseAddress(getText());
		} catch (NaughtyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Edge[] edges = edgeProbabilitySearch(parsedAdress);
		
		if(edges != null)
		{
			String[] strings = new String[edges.length];
			
			for(int i=0; i<strings.length; i++)
			{
				if(parsedAdress[1].equals(""))
					strings[i] = edges[i].getName()+", "+edges[i].getZipString();
				else
					strings[i] = edges[i].getName()+" "+parsedAdress[1]+", "+edges[i].getZipString();
			}
			
			dropdown.setContent(strings, true);
		}
	}
	
	public Edge edgeSearch()
	{
		String[] parsedAdress = new String[1];
		
		try {
			parsedAdress = AddressParser.getInstance().parseAddress(getText());
		} catch (NaughtyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int[] searchIndex = intSearch(parsedAdress);

		if(searchIndex[0] >= 0)
		{
			Edge edge = AddressParser.getInstance().getRoads()[searchIndex[0]].getEdge(searchIndex[1]);
			
			if(parsedAdress[1].equals(""))
				setText(edge.getName()+", "+edge.getZipString());
			else
				setText(edge.getName()+" "+parsedAdress[1]+", "+edge.getZipString());

			getDropdown().setVisible(false);
			
			return edge;
		}
		else {
			
			System.out.println("Edge not found, trying probability search");
			
			Edge[] edges = edgeProbabilitySearch(parsedAdress);
			
			if(edges != null)
			{
				Edge edge = edges[0];
				
				if(parsedAdress[1].equals(""))
					setText(edge.getName()+", "+edge.getZipString());
				else
					setText(edge.getName()+" "+parsedAdress[1]+", "+edge.getZipString());

				
				return edges[0];
			}
			else
				return null;	
		}
	}
	
	public int[] intSearch(String[] parsedAdress)
	{
		
		int[] searchResult = AddressParser.getInstance().search(parsedAdress);
		
		return searchResult;
	}
	
	public Edge[] edgeProbabilitySearch(String[] parsedAdress)
	{
		int[][] searchResult = intProbabilitySearch(parsedAdress);
		
		for(int i=0; i<searchResult.length; i++)
		{
			if(searchResult[i][0] < 0)
			{
				return null;
			}
		}
		
		Edge[] edges = new Edge[searchResult.length];
		
		for(int i=0; i<searchResult.length; i++)
		{
			edges[i] = AddressParser.getInstance().getRoads()[searchResult[i][0]].getEdge(searchResult[i][1]);
		}
		/*}
		else 
		{
			edges = new Edge[1];
			
			edges[0] = AddressParser.getInstance().getRoads()[searchResult[0]].getEdge(searchResult[1]);
		}*/
		
		return edges;
	}
	
	public int[][] intProbabilitySearch(String[] parsedAdress)
	{
		if(getText().length() > 0)
		{
			
			if(parsedAdress[0].length() > 0)
			{
				int[][] searchResult = AddressParser.getInstance().probabilitySearch(parsedAdress, searchSize);
				
				if(searchResult[0][0] < 0)
					dropdown.setContent(new String[]{"-No Matching Result-"}, false);

				return searchResult;
			}
			else
			{
				dropdown.setContent(new String[]{"-Invalid Character Input-"}, false);
			
				return new int[][]{{-1}};
			}
		}
		else {
			dropdown.setVisible(false);
			return new int[][]{{-1}};
		}
	}
	
	public Dropdown getDropdown()
	{
		return dropdown;
	}
}
