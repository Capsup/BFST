package GUI;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.ComboBoxEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class SearchPanel extends JPanel 
{
	JButton goButton;
	SearchField searchField;
	Dropdown dropdown;
	
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
		searchField = new SearchField("Search Here..", 20);
		goButton = new JButton("Search");
		
		//add(searchText, BorderLayout.WEST);
		add(searchField, BorderLayout.CENTER);
		//add(comboBox, BorderLayout.CENTER);
		add(goButton, BorderLayout.EAST);
		
	}

	public JButton getButton()
	{
		return goButton;
	}
	
	public SearchField getTextField()
	{
		return searchField;
	}
	
	public Dropdown getDropdown()
	{
		return dropdown;
	}
}
