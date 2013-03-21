package GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MenuPanel extends JPanel
{
	public MenuPanel()
	{
		makeContent();
	}
	
	void makeContent()
	{
		setLayout(new BorderLayout());
		
		//Search Panel
		JPanel searchPanel = new SearchModule();
		
		//Zoom Panel
		JPanel zoomPanel = new ZoomPanel();
			
		//Translation Panel
		JPanel translationPanel = new TranslationPanel();
				
		add(searchPanel, BorderLayout.NORTH);
		add(zoomPanel, BorderLayout.CENTER);
		add(translationPanel, BorderLayout.SOUTH);
	}
}