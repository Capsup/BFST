package GUI;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import GUI.TranslationPanel.ButtonListener;
import MapDraw.Translation;
import MapDraw.ZoomLevel;

/**
 * The Zoom Panel gives a panel that lets you zoom the map in and out using a user interface
 * @author Jonas Kastberg
 */
public class ZoomPanel extends JPanel implements Observer{
	
	private JLabel currentZoomLabel;
	
	/**
	 * The Zoom Panel gives a panel that lets you zoom the map in and out using a user interface
	 */
	public ZoomPanel()
	{
		makeContent();
	}
	
	
	/**
	 * This is a custom ActionListener class suited for the buttons in the Zoom Panel
	 * @author Jonas Kastberg
	 */
	class ButtonListener implements ActionListener
	{
		public void actionPerformed( ActionEvent event )
		{
			
			//We use a switch to check for all the buttons we have added to our listener
			switch( event.getActionCommand() )
			{
				case "ZoomIn":
					//We reset the last known mouse zoom position of the ZoomPanel singleton.
					//This is done to prevent mouse geasture zoom interfearing with GUI Zoom
					ZoomLevel.getInstance().setMousePosition(new Point(0,0));
					
					//We call the zoom in method of our ZoomLevel Singleton
					ZoomLevel.getInstance().zoomIn();
				break;

				case "ZoomOut":
					
					// - See "ZoomIn"
					ZoomLevel.getInstance().setMousePosition(new Point(0,0));
					ZoomLevel.getInstance().zoomOut();
				break;
			}
		}
	}

	private void makeContent()
	{
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JLabel headerLabel = new JLabel("Zooming:");		//We make a header label for descriptive purposes
		headerLabel.setAlignmentX( CENTER_ALIGNMENT );		//We set the label to align to the center
		
		JButton zoomInButton = new JButton("+");			//We make a button that lets us zoom in
		zoomInButton.setActionCommand("ZoomIn");			//We set the action command of our button
		zoomInButton.setAlignmentX(CENTER_ALIGNMENT);		//We set the label to align to the center
		
		currentZoomLabel = new JLabel("Zoom Level: 1 ");	//We add a label displaying the current zoom level
		currentZoomLabel.setAlignmentX(CENTER_ALIGNMENT);	//We set the label to align to the center
		
		JButton zoomOutButton = new JButton("-");			//We make a button that lets us zoom out
		zoomInButton.setActionCommand("ZoomOut");			//We set the action command of our button
		zoomOutButton.setAlignmentX(CENTER_ALIGNMENT);		//We set the label to align to the center
		
		//We initialize a custom listener in order to access our zoom buttons.
		ButtonListener listener = new ButtonListener();
		
		//We add the listener to our buttons
		zoomInButton.addActionListener(listener);
		zoomOutButton.addActionListener(listener);
		
		//We add all of our components to our box layout panel
		add(headerLabel);
		add(zoomInButton);
		add(currentZoomLabel);
		add(zoomOutButton);
		
		//We add this as an observer to the Zoomlevel Singleton.
		ZoomLevel.getInstance().addObserver(this);
	}

	@Override
	public void update(Observable o, Object arg) 
	{
		//Update the text of the zoom label to the current zoom level.
		//Update is called whenever the observable (ZoomLevel) changes its zoom level
		//The value is offset by one since the zoomIndex is 0-based.
		currentZoomLabel.setText("Zoom Level: "+(ZoomLevel.getInstance().getZoomIndex()+1));
	}
	
	
}
