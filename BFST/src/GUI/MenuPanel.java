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

	/*
	 * The Menu Panel contains everything to do with the menu
	 */	
	public MenuPanel() {
		
		
		makeContent();
	}

	void makeContent()
	{
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		//Search Panel
		JPanel searchPanel = new SearchModule();
		searchPanel.setMaximumSize(searchPanel.getPreferredSize());
		searchPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		//Info Panel
		JPanel infoPanel = new InfoPanel();
		infoPanel.setMaximumSize(infoPanel.getPreferredSize());
		infoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		//Navigation Panel
		JPanel navContainerPanel = new JPanel();
		navContainerPanel.setLayout(new BoxLayout(navContainerPanel, BoxLayout.X_AXIS));
		navContainerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		JPanel navPanel = new NavigationPanel();
		navPanel.setMaximumSize(navPanel.getPreferredSize());
		//navPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		navContainerPanel.add(Box.createHorizontalGlue());
		navContainerPanel.add(navPanel);
		navContainerPanel.add(Box.createHorizontalGlue());
		
		//Add the panels, with vertical glue, in order to have a nice look
		add(Box.createVerticalGlue());
		add(searchPanel);
		//add(Box.createVerticalGlue());
		add(infoPanel);
		add(Box.createVerticalGlue());
		add(navContainerPanel);
		add(Box.createVerticalGlue());
		
		
	}
}