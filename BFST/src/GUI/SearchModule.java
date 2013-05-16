package GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.ietf.jgss.Oid;

import AddressParser.AddressParser;
import Graph.Edge;
import MapDraw.MapDraw;
import MapDraw.Translation;
import MapDraw.ZoomLevel;
import java.awt.geom.Point2D;

/**
 * A Panel which combines the different search fields into a search module that can be used to search for routes
 * @author Jonas Kastberg
 */
public class SearchModule extends JPanel{

	private boolean showDestination;	//A toggle boolean that defines whether or not we are showing the destination part of the Panel
	private SearchPanel fromPanel;			//The panel that holds the departure searchField
	private JPanel toPanel;				//The Panel that holds the destination searchField
	private JPanel destinationPanel;	//The Panel that holds the destination searchField
	private JButton toggleButton;		//The button that toggles whether or not to show the destination view
	
	private SearchField departureTextField;	//The SearchField that holds the departure address
	private SearchField destinationTextField;	//The SearchField that holds the destination address
	
	/**
	 * A Panel which combines the different search fields into a search module that can be used to search for routes
	 */
	public SearchModule()
	{
		//We initialize the Address Parser singleton by accessing it
		AddressParser.getInstance();
		
		makeContent();
	}
	
	/**
	 * A custom ActionListener that controls what is to happen whenever the search button is pressed
	 * @author Jonas Kastberg
	 */
	class ButtonListener implements ActionListener
	{
		public void actionPerformed( ActionEvent event )
		{
			switch( event.getActionCommand() )
			{
				case "Search":
					
					//We start out by making a reference to the map singleton
					MapDraw map = MapDraw.getInstance();
					
					//We then check whether we need to find a place or a route
					if(!showDestination)
					{
						//If we do not show the destination panel we are looking for a place
						
						//We use the departure Search Field to search for an edge
						Edge edge = departureTextField.edgeSearch();
						
						//We then check if the search had a valid search hit
						if(edge != null)
						{
							//If the search was successful we calculate the target position that we need to translate to, to show the resulting edge
							Point2D.Double tarPos = new Point2D.Double(-(edge.getXFrom()/1000.0) + map.getMapWidth()/2
									, edge.getYFrom()/1000.0 - map.getMapHeight()/2);
							
							//We then call the maps getRoute method in order to draw a path (to visualize the edge we find)
							map.getRoute(edge.getFromIndex(), edge.getToIndex());
							
							//Then we get the Translation singleton and make it animate to the resulting edge
							Translation.getInstance().goToTranslation(tarPos.x, tarPos.y);
							
							//We also zoom to an appropriate zoom level in order to get a good look
							ZoomLevel.getInstance().setZoomLevel(17);
							
							//We let the PathInformation singleton know that we dont need any path information since we searched for a location
							PathInformation.getInstance().setVisible(false);
						}
					}
					else
					{
						//If we do not show the destination panel we are looking for a route
						
						//We start out by searching for the departure and the destination using our SearchFields
						Edge fromEdge = departureTextField.edgeSearch();
						Edge toEdge = destinationTextField.edgeSearch();
						
						//We then check if both searches found a valid edge
						if(fromEdge != null && toEdge != null)
						{
							//We then find the position of each of the edges in our world
							Point2D.Double tarPos1 = new Point2D.Double(-(fromEdge.getXFrom()/1000.0) + map.getMapWidth()/2
									, fromEdge.getYFrom()/1000.0 - map.getMapHeight()/2);
							
							Point2D.Double tarPos2 = new Point2D.Double(-(toEdge.getXFrom()/1000.0) + map.getMapWidth()/2
									, toEdge.getYFrom()/1000.0 - map.getMapHeight()/2);
							
							
							//We calculate the target position to translate to by finding the average of the two
							Point2D.Double tarPos = new Point2D.Double((tarPos2.x + tarPos1.x)/2, (tarPos2.y + tarPos1.y)/2);
							
							//We then draw a route between the two edges, using our maps method
							map.getRoute(fromEdge.getFromIndex(), toEdge.getToIndex());
							
							//Next we calculate the distance between the two sources (Mind you that this is in birds fligt)
							Point2D.Double difference = new Point2D.Double(Math.abs(tarPos2.x - tarPos1.x), Math.abs(tarPos2.y - tarPos1.y));
							
							double targetZoom;
							
							//We check whether we should calculate according to the x-axis or the y-axis
							if(difference.x >= difference.y)
							{
								targetZoom = (difference.x/map.getMapWidth())*2.5f;
							}
							else 
							{
								targetZoom = (difference.y/map.getMapHeight())*2.5f;
							}
							
							
							//Once we have the target zoom and the target translation we tell the singletons to animate towards them
							Translation.getInstance().goToTranslation(tarPos.x, tarPos.y);
							ZoomLevel.getInstance().setZoomLevel(ZoomLevel.getInstance().findIndex(targetZoom));
							
							//We then tell the PathInformation singleton about the path that we found, and that we want to show the information
							PathInformation.getInstance().setFromAndTo(departureTextField.getText(), destinationTextField.getText());
							PathInformation.getInstance().setVisible(true);
							
							//departureTextField.getDropdown().updatePosition();
							//destinationTextField.getDropdown().updatePosition();
						}
					}
					
					break;
				case "Open":
					//If the toggle buttons action message is received we toggle the destinations
					toggleDestination();
					break;
			}
		}
	}
	
	void makeContent()
	{
		//We set the layout as a box layout with vertical alignment
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		//We start out by initializing a header label for the panel
		JLabel headerLabel = new JLabel("Searching: ");
		
		//We initialize the SearchPanel that contains the departure SearchField
		fromPanel = new SearchPanel();
		fromPanel.getButton().setActionCommand("Search");
		fromPanel.getButton().setFocusable(false);
		
		//We set the fromTextField to the SearchField in the departure search panel
		departureTextField = fromPanel.getTextField();
		
		//We initialize the destinationPanel
		destinationPanel = new JPanel();
		destinationPanel.setLayout(new BorderLayout());
		
		toPanel = new JPanel(new BorderLayout());
	
		//We initiate the toggleButton and set all of it's presets
		toggleButton = new JButton("Click for destination search...");
		toggleButton.setHorizontalTextPosition(JButton.CENTER);
		toggleButton.setFocusable(false);
		
		//We add the toggleButton to the center of the to panel
		toPanel.add(toggleButton, BorderLayout.CENTER);
		
		//We add the toPanel to the top of the destination panel
		destinationPanel.add(toPanel, BorderLayout.NORTH);
		
		//We proceed to add the header label and the panels using our BoxLayout
		add(headerLabel);
		add(fromPanel);
		add(destinationPanel);
		
		//We revalidate and paint for good measure
		revalidate();
		repaint();
		
		//We initialize our ButtonListener and add the listener to the different buttons
		ButtonListener listener = new ButtonListener();
		fromPanel.getButton().addActionListener(listener);
		toggleButton.setActionCommand("Open");
		toggleButton.addActionListener(listener);
	}
	
	/**
	 * Toggle between showing and not showing the destination search field
	 */
	void toggleDestination()
	{
		//We reverse the boolean
		showDestination = !showDestination;
		
		//Based on the boolean we either show or hide the destination search field
		if(showDestination)
			showDestination();
		else
			hideDestination();
		
		//We revalidate and repaint for good measure
		revalidate();
		repaint();
	}
	
	/**
	 * add all the components required for the destination search field to work
	 */
	void showDestination()
	{	
		//We initialize a new Search Field and set its preferred size that we make sure we can see it
		destinationTextField = new SearchField("I wish to go to...", 20);
		destinationTextField.setPreferredSize(departureTextField.getSize());
		
		//We add the searchfield to the left of our to panel
		toPanel.add(destinationTextField, BorderLayout.WEST);
		
		//We change the text of the toggle button, rescaling it in order to make space for the search field
		toggleButton.setText("Hide..");
	}
	
	/**
	 * remove all the components from the toPanel except the toggle button
	 */
	void hideDestination()
	{
		//We hide the dropdown of the destination search field 
		destinationTextField.getDropdown().setVisible(false);
		
		//We remove the searchfield component from the toPanel
		toPanel.remove(1);
		
		//We reverse the text of the toggle button
		toggleButton.setText("Click for destination search...");
		
	}
}
