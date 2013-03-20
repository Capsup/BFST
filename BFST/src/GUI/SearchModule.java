package GUI;

import java.awt.BorderLayout;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class SearchModule extends JPanel{

	public SearchModule()
	{
		makeContent();
	}
	
	void makeContent()
	{
		setLayout(new BorderLayout());
		
		JPanel fromPanel = new JPanel(new BorderLayout());
		
		JLabel fromLabel = new JLabel("From:");
		JPanel fromSearchPanel = new SearchPanel();
		
		fromPanel.add(fromLabel, BorderLayout.NORTH);
		fromPanel.add(fromSearchPanel, BorderLayout.CENTER);
		
		JPanel toPanel = new JPanel(new BorderLayout());
		
		JLabel toLabel = new JLabel("To:");
		JPanel toSearchPanel = new SearchPanel();
		
		toPanel.add(toLabel, BorderLayout.NORTH);
		toPanel.add(toSearchPanel, BorderLayout.CENTER);
		
		add(fromPanel, BorderLayout.NORTH);
		add(toPanel, BorderLayout.CENTER);
		
	}
}
