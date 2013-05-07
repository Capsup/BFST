package GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.ietf.jgss.Oid;

import MapDraw.MapDraw;
import XMLParser.AddressParser;
import XMLParser.AddressParser.NaughtyException;

public class SearchModule extends JPanel{

	private boolean showDestination;
	private JPanel destinationPanel;
	private JPanel toPanel;
	private JButton button;
	
	private JTextField fromTextField;
	private JTextField toTextField;
	
	private MapDraw map;
	
	/*
	 * The search module is the combined search panels "from" and "to"
	 */
	public SearchModule(MapDraw map)
	{
		this.map = map;
		
		makeContent();
	}
	
	class ButtonListener implements ActionListener
	{
		public void actionPerformed( ActionEvent event )
		{
			switch( event.getActionCommand() )
			{
				case "Search":
					
					String result = "";
					
					if(!showDestination)
					{
						String[] parsedAdress = new String[1];
						
						try {
							parsedAdress = AddressParser.getInstance().parseAddress(fromTextField.getText());
						} catch (NaughtyException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						int searchResult = AddressParser.getInstance().search(parsedAdress[0]);
						
						//System.out.println("SearchResult: "+searchResult);
						
						if(searchResult >= 0)
							System.out.println("Name result: "+AddressParser.getInstance().getRoads()[searchResult].getName());
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
						
						int searchResultIndexFrom = AddressParser.getInstance().search(parsedAdressFrom[0]);
						int searchResultIndexTo = AddressParser.getInstance().search(parsedAdressTo[0]);
						
						//System.out.println(searchResultIndexFrom);
						//System.out.println(searchResultIndexTo);
						
						
						if(searchResultIndexFrom >= 0)
						{
							fromTextField.setText(AddressParser.getInstance().getRoads()[searchResultIndexFrom].getName());
							
							//System.out.println("From: "+AddressParser.getInstance().getRoads()[searchResultIndexFrom].getName());
							//System.out.println("From: "+AddressParser.getInstance().getRoads()[searchResultIndexFrom].getFromIndex());
						}
						
						if(searchResultIndexTo >= 0)
						{
							toTextField.setText(AddressParser.getInstance().getRoads()[searchResultIndexTo].getName());
							
							//System.out.println("To: "+AddressParser.getInstance().getRoads()[searchResultIndexTo].getName());
							//System.out.println("To: "+AddressParser.getInstance().getRoads()[searchResultIndexTo].getToIndex());
						}
						
						
						//System.out.println(map);
						
						
						if(searchResultIndexFrom >= 0 && searchResultIndexTo >= 0)
							map.getRoute(AddressParser.getInstance().getRoads()[searchResultIndexFrom].getFromIndex(), AddressParser.getInstance().getRoads()[searchResultIndexTo].getToIndex());
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
		
		destinationPanel = new JPanel(new BorderLayout());
		
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
		toTextField = new JTextField("I wish to go to...", 20);
		
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
