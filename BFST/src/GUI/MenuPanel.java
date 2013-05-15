package GUI;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout.Constraints;

import MapDraw.MapDraw;

public class MenuPanel extends JPanel
{
	/**
	 * The Menu Panel contains everything to do with the menu
	 */
	public MenuPanel() {
		makeContent();
	}

	void makeContent()
	{
		//We set the layout to a boxlayout with a vertical allignment
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		//Search Panel: This is the panel in which all searching is performed
		JPanel searchPanel = new SearchModule();
		searchPanel.setMaximumSize(searchPanel.getPreferredSize());	//This stretches the panel to the length of the MenuPanel
		searchPanel.setAlignmentX(Component.LEFT_ALIGNMENT);		//We make sure to left align all panels in the boxlayout
		
		//Info Panel: This is the panel that contains any information about the current path (as per displayed on the map)
		JPanel infoPanel = new InfoPanel();
		infoPanel.setMaximumSize(infoPanel.getPreferredSize()); 	//This stretches the panel to the length of the MenuPanel
		infoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);			//We make sure to left align all panels in the boxlayout
		
		//Settings Container Panel: This is a container panel for our settings panel. This lets us center align the settings panel
		JPanel settingsContainerPanel = new JPanel();
		settingsContainerPanel.setLayout(new BoxLayout(settingsContainerPanel, BoxLayout.X_AXIS));	//This panel lets us center align our settings panel
		settingsContainerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);	//We make sure to left align all panels in the boxlayout
		
		//Settings Panel: This is the panel that lets us input settings for our searching and pathfinding
		JPanel settingsPanel = new SettingsPanel();
		settingsPanel.setMaximumSize(settingsPanel.getPreferredSize()); //This stretches the panel to the length of the MenuPanel
		
		//We add the settings panel to our container panel. We center align using horizontal glue 
		settingsContainerPanel.add(Box.createHorizontalGlue());
		settingsContainerPanel.add(settingsPanel);
		settingsContainerPanel.add(Box.createHorizontalGlue());
		
		//Navigation Container Panel
		JPanel navContainerPanel = new JPanel();
		navContainerPanel.setLayout(new BoxLayout(navContainerPanel, BoxLayout.X_AXIS));
		navContainerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		//Navigation Panel: This is the panel that lets us navigate in our map with out using mouse gestures
		JPanel navPanel = new NavigationPanel();
		navPanel.setMaximumSize(navPanel.getPreferredSize());	//This stretches the panel to the length of the MenuPanel
		
		navContainerPanel.add(Box.createHorizontalGlue());
		navContainerPanel.add(navPanel);
		navContainerPanel.add(Box.createHorizontalGlue());
		
		//Add the panels, with vertical glue, in order to have a nice look
		add(Box.createVerticalGlue());
		add(searchPanel);
		add(infoPanel);
		add(settingsContainerPanel);
		add(Box.createVerticalGlue());
		add(navContainerPanel);
		add(Box.createVerticalGlue());
		
		
	}
}