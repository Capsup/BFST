package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.TextField;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

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
			
		add(panel);
		
		setAlwaysOnTop(true);
		setUndecorated(true);
		setVisible(false);
		setResizable(false);
		setFocusableWindowState(false);
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
		panel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		
		ArrayList<JButton> buttonList = new ArrayList<JButton>();
		
		setSize(textField.getSize().width, textField.getSize().height*strings.length);
		panel.setSize(textField.getSize().width, textField.getSize().height*strings.length);
		
		for(int i=0; i<strings.length; i++)
		{
			GUIButton newButton = new GUIButton(strings[i]);
			newButton.setEnabled(clickable);
			newButton.setHoverable(clickable);
			
			buttonList.add(newButton);
			
			newButton.setMaximumSize(new Dimension(panel.getSize().width, textField.getSize().height));

			panel.add(newButton);
		}
		
		results = new JButton[buttonList.size()];
		
		buttonList.toArray(results);
		
		ButtonListener listener = new ButtonListener(results);
				
		for(int i=0; i<results.length; i++)
		{
			results[i].setActionCommand(""+i);
			results[i].addActionListener(listener);
		}
		
		setVisible(true);
		
		updatePosition();
	}
}
