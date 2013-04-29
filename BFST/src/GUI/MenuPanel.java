package GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout.Constraints;

public class MenuPanel extends JPanel
{
	/*
	 * The Menu Panel contains everything to do with the menu
	 */
	public MenuPanel()
	{
		makeContent();
	}
	
	void makeContent()
	{
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		//Search Panel
		JPanel searchPanel = new SearchModule();
		searchPanel.setMaximumSize(searchPanel.getPreferredSize());
		
		//Zoom Panel
		JPanel zoomPanel = new ZoomPanel();
		zoomPanel.setMaximumSize(zoomPanel.getPreferredSize());
		
		//Translation Panel
		JPanel translationPanel = new TranslationPanel();
		translationPanel.setMaximumSize(translationPanel.getPreferredSize());
		
		//Add the panels, with vertical glue, in order to have a nice look
		add(Box.createVerticalGlue());
		add(searchPanel);
		add(Box.createVerticalGlue());
		add(zoomPanel);
		add(Box.createVerticalGlue());
		add(translationPanel);
		add(Box.createVerticalGlue());
	}
}