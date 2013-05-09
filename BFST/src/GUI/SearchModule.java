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

import Graph.Edge;
import MapDraw.MapDraw;
import MapDraw.Translation;
import MapDraw.ZoomLevel;
import XMLParser.AddressParser;
import XMLParser.AddressParser.NaughtyException;
import java.awt.geom.Point2D;

public class SearchModule extends JPanel{

	private boolean showDestination;
	private SearchPanel destinationPanel;
	private JPanel toPanel;
	private JButton button;
	
	private SearchField fromTextField;
	private SearchField toTextField;
	
	private MapDraw map;
	
	/*
	 * The search module is the combined search panels "from" and "to"
	 */
	public SearchModule(MapDraw map)
	{
		this.map = map;
		
		AddressParser.getInstance();
		
		makeContent();
	}
	
	class ButtonListener implements ActionListener
	{
		public void actionPerformed( ActionEvent event )
		{
			switch( event.getActionCommand() )
			{
				case "Search":
					
					if(!showDestination)
					{
						Edge edge = fromTextField.edgeSearch();
						
						if(edge != null)
						{
							Point2D.Double tarPos = new Point2D.Double(-(edge.getXFrom()/1000.0) + map.getMapWidth()/2
									, edge.getYFrom()/1000.0 - map.getMapHeight()/2);
							
							fromTextField.setText(edge.getName());
							
							fromTextField.getDropdown().setVisible(false);
							
							Translation.getInstance().goToTranslation(tarPos.x, tarPos.y);
							ZoomLevel.getInstance().setZoomLevel(17);
						}
					}
					else
					{
						String[] parsedAdressFrom = new String[1];
						String[] parsedAdressTo = new String[1];
						
						try {
							parsedAdressFrom = AddressParser.getInstance().parseAddress(fromTextField.getText());
							parsedAdressTo = AddressParser.getInstance().parseAddress(toTextField.getText());
						} catch (NaughtyException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						//int searchResultIndexFrom = AddressParser.getInstance().search(parsedAdressFrom[0]);
						//int searchResultIndexTo = AddressParser.getInstance().search(parsedAdressTo[0]);
						
						Edge fromEdge = fromTextField.edgeSearch();
						Edge toEdge = toTextField.edgeSearch();
						
						//System.out.println(searchResultIndexFrom);
						//System.out.println(searchResultIndexTo);
						
						
						if(fromEdge != null)
						{
							fromTextField.setText(fromEdge.getName());
							fromTextField.getDropdown().setVisible(false);	
						}
						
						if(toEdge != null)
						{
							toTextField.setText(toEdge.getName());
							toTextField.getDropdown().setVisible(false);
						}
						
						if(fromEdge != null && toEdge != null)
						{
							Point2D.Double tarPos1 = new Point2D.Double(-(fromEdge.getXFrom()/1000.0) + map.getMapWidth()/2
									, fromEdge.getYFrom()/1000.0 - map.getMapHeight()/2);
							
							Point2D.Double tarPos2 = new Point2D.Double(-(toEdge.getXFrom()/1000.0) + map.getMapWidth()/2
									, toEdge.getYFrom()/1000.0 - map.getMapHeight()/2);
							
							Point2D.Double tarPos = new Point2D.Double((tarPos2.x + tarPos1.x)/2, (tarPos2.y + tarPos1.y)/2);
							
							map.getRoute(fromEdge.getFromIndex(), toEdge.getToIndex());
							
							Point2D.Double difference = new Point2D.Double(Math.abs(tarPos2.x - tarPos1.x), Math.abs(tarPos2.y - tarPos1.y));
							
							double targetZoom;
							
							if(difference.x >= difference.y)
							{
								targetZoom = (difference.x/map.getMapWidth())*2.5f;
							}
							else 
							{
								targetZoom = (difference.y/map.getMapHeight())*2.5f;
							}
							
							Translation.getInstance().goToTranslation(tarPos.x, tarPos.y);
							ZoomLevel.getInstance().setZoomLevel(ZoomLevel.getInstance().findIndex(targetZoom));
						}
					}
					
					break;
				case "Open":
					toggleDestination();
					break;
			}
		}
	}
	
	void makeContent()
	{
		JLabel headerLabel = new JLabel("Searching: ");
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JPanel fromPanel = new JPanel(new BorderLayout());
		
		JLabel fromLabel = new JLabel("From:");
		SearchPanel fromSearchPanel = new SearchPanel();
		fromSearchPanel.getButton().setActionCommand("Search");
		fromTextField = fromSearchPanel.getTextField();
		
		//Button Listener
		ButtonListener listener = new ButtonListener();
		
		fromSearchPanel.getButton().addActionListener(listener);
		
		fromPanel.add(fromLabel, BorderLayout.NORTH);
		fromPanel.add(fromSearchPanel, BorderLayout.CENTER);
		
		//JLabel toLabel = new JLabel("To:");
		//JPanel toSearchPanel = new SearchPanel();
		
		//toPanel.add(toLabel, BorderLayout.NORTH);
		//toPanel.add(toSearchPanel, BorderLayout.CENTER);
		
		/*
		if(showDestination)
		{
			JTextField searchField = new JTextField("Not Functional", 20);
			
			toPanel.add(searchField, B orderLayout.CENTER);
		}*/
		
		destinationPanel = new SearchPanel();
		destinationPanel.setLayout(new BorderLayout());
		
		toPanel = new JPanel(new BorderLayout());
		
		
		//ImageIcon icon = new ImageIcon("/images/Boeing 1337.png");
		
		//ImageIcon icon = new ImageIcon(getClass().getResource("/images/Boeing 1337.png"));
		
		button = new JButton("Destination Search...");
		
		button.setHorizontalTextPosition(JButton.CENTER);
		button.setText("Destination Search..");
		
		button.addActionListener(listener);
		button.setActionCommand("Open");
		
		toPanel.add(button, BorderLayout.CENTER);
		
		destinationPanel.add(toPanel, BorderLayout.NORTH);
		
		add(headerLabel);
		add(fromPanel);
		add(destinationPanel);
		
		/*
		try {
			button = new GUIButton(ImageIO.read( GUIButton.class.getResource( "/images/Normal.png" ))
					, ImageIO.read(GUIButton.class.getResource( "/images/Hover.png" ))
					, ImageIO.read(GUIButton.class.getResource( "/images/Pressed.png" )));
			
			button.setHorizontalTextPosition(JButton.CENTER);
			button.setText("Destination Search..");
			
			ButtonListener listener = new ButtonListener();
			
			button.addActionListener(listener);
			button.setActionCommand("Open");
			
			toPanel.add(button, BorderLayout.CENTER);
			
			destinationPanel.add(toPanel, BorderLayout.NORTH);
			
			add(headerLabel);
			add(fromPanel);
			add(destinationPanel);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
		revalidate();
		repaint();
	}
	
	void toggleDestination()
	{
		showDestination = !showDestination;
		
		if(showDestination)
			showDestination();
		else
			hideDestination();
		
		revalidate();
		repaint();
	}
	
	void showDestination()
	{	
		toTextField = new SearchField("I wish to go to...", 20);
		
		toPanel.add(toTextField, BorderLayout.WEST);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		
		buttonPanel.add(new JButton("Car"));
		buttonPanel.add(new JButton("Bicycle"));
		
		destinationPanel.add(buttonPanel, BorderLayout.CENTER);
		
		button.setText("Hide..");
	}
	
	void hideDestination()
	{
		toPanel.remove(1);
		
		for(int i=1; i<destinationPanel.getComponentCount(); i++)
			destinationPanel.remove(i);
		
		button.setText("Destination Search..");
		
	}
}
