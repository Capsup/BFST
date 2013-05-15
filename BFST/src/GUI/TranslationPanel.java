package GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import MapDraw.Translation;
import MapDraw.ZoomLevel;

/**
 * The Translation panel gives an easy GUI possibility of accessing the Translation Singleton.
 * @author Jonas Kastberg
 *
 */
public class TranslationPanel extends JPanel{
	
	/**
	 * The Translation panel gives a GUI possibility of translating the map
	 */
	public TranslationPanel()
	{
		makeContent();
	}
	
	/**
	 * This is a custom ActionListener class suited for the buttons in the Translation Panel
	 * @author Jonas Kastberg
	 */
	class ButtonListener implements ActionListener
	{
		public void actionPerformed( ActionEvent event )
		{
			//We calculate the translation difference. 
			//15 is the amount of units we translate
			//We multiply by the zoomLevel coefficient in order to maintain proper translation at zoomed in situations
			double translationDiff = 15 * ZoomLevel.getInstance().getZoomLevel();
			
			//We get the current target translation of our Translation singleton
			Point2D.Double translation = Translation.getInstance().getTargetTranslation();
			
			//We use a switch to check for all the buttons we have added to our listener
			switch( event.getActionCommand() )
			{
				//We use a method to set the target translation in our Translation singleton, based on the pressed button.
			
				case "Up":
					Translation.getInstance().goToTranslation(translation.x, translation.y+translationDiff);
				break;

				case "Down":
					Translation.getInstance().goToTranslation(translation.x, translation.y-translationDiff);
				break;

				case "Right":
					Translation.getInstance().goToTranslation(translation.x-translationDiff, translation.y);
				break;

				case "Left":
					Translation.getInstance().goToTranslation(translation.x+translationDiff, translation.y);
				break;
			}
		}
	}

	private void makeContent()
	{
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JLabel headerLabel = new JLabel("Translation:");	//A JLabel for descriptive purposes
		headerLabel.setAlignmentX( CENTER_ALIGNMENT );		//We make sure to center align the label
		
		JPanel buttonPanel = new JPanel();					//We make a button panel in order to contain the navigation buttons
		buttonPanel.setLayout(new BorderLayout());			//We add a borderlayout
		
		JPanel upDownPanel = new JPanel();					//Another panel to contain the up and down navigation buttons for design purposes
		upDownPanel.setLayout(new BorderLayout());			//We add a borderlayout
		
		JButton upButton = new JButton("^");				//We make an up-button
		upButton.setActionCommand("Up");					//We assign the action command of the button for later use
		
		JButton downButton = new JButton("v");				//We make an down-button
		downButton.setActionCommand("Down");				//We assign the action command of the button for later use
		
		JButton rightButton = new JButton(">");				//We make an right-button
		rightButton.setActionCommand("Right");				//We assign the action command of the button for later use
		
		JButton leftButton = new JButton("<");				//We make an left-button
		leftButton.setActionCommand("Left");				//We assign the action command of the button for later use
		
		//We initialize a custom button listener inner-class
		ButtonListener listener = new ButtonListener();
		
		//We add all of ours buttons to the listener
		upButton.addActionListener(listener);
		downButton.addActionListener(listener);
		rightButton.addActionListener(listener);
		leftButton.addActionListener(listener);
		
		//We add the up and down buttons to the up-down panel
		upDownPanel.add(upButton, BorderLayout.NORTH);
		upDownPanel.add(downButton, BorderLayout.SOUTH);
		
		//We add the remaining buttons and the up-down panel to the main button panel.
		buttonPanel.add(upDownPanel, BorderLayout.CENTER);
		buttonPanel.add(rightButton, BorderLayout.EAST);
		buttonPanel.add(leftButton, BorderLayout.WEST);
		
		//We add our header label and our button panel to the main panel
		add(headerLabel);
		add(buttonPanel);
	}
}
