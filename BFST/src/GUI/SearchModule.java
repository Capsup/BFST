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


public class SearchModule extends JPanel{

	private boolean showDestination;
	private JPanel destinationPanel;
	private JPanel toPanel;
	private JButton button;
	
	
	/*
	 * The search module is the combined search panels "from" and "to"
	 */
	public SearchModule()
	{
		makeContent();
	}
	
	class ButtonListener implements ActionListener
	{
		public void actionPerformed( ActionEvent event )
		{
			switch( event.getActionCommand() )
			{
				case "Open":
					toggleDestination();
			}
		}
	}
	
	void makeContent()
	{
		JLabel headerLabel = new JLabel("Searching: ");
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JPanel fromPanel = new JPanel(new BorderLayout());
		
		JLabel fromLabel = new JLabel("From:");
		JPanel fromSearchPanel = new SearchPanel();
		
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
		
		ButtonListener listener = new ButtonListener();
		
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
		JTextField searchField = new JTextField("Not Functional", 20);
		
		toPanel.add(searchField, BorderLayout.WEST);
		
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
