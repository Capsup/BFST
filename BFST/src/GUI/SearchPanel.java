package GUI;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class SearchPanel extends JPanel 
{
	/*
	 * The Search Panel is a panel in which one can enter what a search criteria and execute it.
	 */
	public SearchPanel()
	{
		makeContent();
	}
	
	void makeContent()
	{
		setLayout(new BorderLayout());
		
		//Text
		//JLabel searchText = new JLabel("Search: ");
		
		//Search Field <-- Insert SearchField class extending JTextField here
		JTextField searchField = new JTextField("Not Functional", 20);
		searchField.setEnabled(false);
		
		JButton goButton = new JButton("GO!");
		
		//add(searchText, BorderLayout.WEST);
		add(searchField, BorderLayout.CENTER);
		add(goButton, BorderLayout.EAST);
	}
}
