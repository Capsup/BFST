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

/**
 * A Panel that combines a search field and a searchButton
 * @author Jonas Kastberg
 */
public class SearchPanel extends JPanel 
{
	JButton goButton;
	SearchField searchField;
	
	/**
	 * Initializes a panel with SearchField and a button
	 */
	public SearchPanel()
	{
		makeContent();
	}
	
	void makeContent()
	{
		setLayout(new BorderLayout());
		
		searchField = new SearchField("Search Here..", 20);
		goButton = new JButton("Search");
		
		add(searchField, BorderLayout.CENTER);
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
}
