package GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;

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
		
		add(searchPanel, BorderLayout.NORTH);
	}
}
