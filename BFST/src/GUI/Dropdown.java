package GUI;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.TextField;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Dropdown extends JDialog
{
	JPanel panel;
	JTextField textField;
	Point offset;
	
	JButton[] results;
	
	public class ButtonListener implements ActionListener
	{
		JButton[] results;
		
		public ButtonListener(JButton[] results)
		{
			this.results = results;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			for(int i=0; i<results.length; i++)
			{
				if(e.getActionCommand().equals(""+i))
				{
					System.out.println("WAT");
					
					textField.setText(results[i].getText());
					
					setVisible(false);
				}
			}
		}
	}
	
	public Dropdown(JTextField textField)
	{
		this.textField = textField;
		//offset = new Point(65, 35);
		makeContent();
	}
	
	
	
	private void makeContent()
	{
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		String[] strings = new String[]{"WAT", "FUCK", "BALLS"};
		System.out.println("hey");
		
		for(int i=0; i<strings.length; i++)
			panel.add(new JLabel(strings[i]));
			
		add(panel);
		
		setAlwaysOnTop(true);
		setUndecorated(true);
		setVisible(false);
		setResizable(false);
		setFocusableWindowState(false);
		pack();
	}
	
	public void updatePosition()
	{
		offset = new Point(0, textField.getSize().height);
		
		//setLocationRelativeTo(textField);
		//textField.getLocationOnScreen();
		
		if(textField.isShowing())
		{
			Point location = textField.getLocationOnScreen();
			setLocation(location.x+offset.x, location.y+offset.y);
		}
		//setVisible(true);
	}
	
	public void setContent(String[] strings, boolean clickable)
	{
		panel.removeAll();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		ArrayList<JButton> buttonList = new ArrayList<JButton>();
		
		setSize(textField.getSize().width, textField.getSize().height*strings.length);
		panel.setSize(textField.getSize().width, textField.getSize().height*strings.length);
		
		for(int i=0; i<strings.length; i++)
		{
			JButton newButton = new GUIButton(strings[i]);
			newButton.setEnabled(clickable);
			
			buttonList.add(newButton);
			
			System.out.println((panel.getSize().width+","+textField.getSize().height));
			newButton.setMaximumSize(new Dimension(panel.getSize().width, textField.getSize().height));
			panel.add(newButton);
		}
		
		//pack();
		
		results = new JButton[buttonList.size()];
		
		buttonList.toArray(results);
		
		ButtonListener listener = new ButtonListener(results);
		
		//pack();
		
		int longestWidth = 0;
				
		for(int i=0; i<results.length; i++)
		{
			results[i].setActionCommand(""+i);
			results[i].addActionListener(listener);
			
			System.out.println(results[i].getSize().width);
			
			if(results[i].getSize().width > longestWidth)
				longestWidth = results[i].getSize().width;
		}
		/*
		System.out.println("Longest: "+longestWidth);
		
		for (int i=0; i<results.length; i++) {
			results[i].setSize(longestWidth, results[i].getSize().height);
			
			System.out.println(results[i].getSize().width);
		}
		
		//panel.setSize(textField.getSize().width, panel.getSize().height);
		//setSize(textField.getSize().width, panel.getSize().height);
		
		//revalidate();
		
		for (int i=0; i<results.length; i++) {
			System.out.println(results[i].getSize().width);
		}
		
		repaint();
		*/
		
		setVisible(true);
		
		updatePosition();
	}
}
