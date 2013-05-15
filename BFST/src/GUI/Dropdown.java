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

/**
 * A modified JDialog that is mapped to a JTextField. The Dialog is undecorated and reworked into a Dropdown menu
 * @author Jonas Kastberg
 */
public class Dropdown extends JDialog
{
	JPanel panel;					//The panel we use inside our JDialog
	JTextField textField;			//The textField that the Dropdown is associated with
	Point offset;					//The offest of the Dropdown compared to the associated textField
	
	JButton[] results;				//The JButton array is the results of a search
	
	/**
	 * @param the textField that the Dropdown is attatched to is passed to its constructor upon initialization
	 */
	public Dropdown(JTextField textField)
	{
		this.textField = textField;
		makeContent();
	}
	
	/**
	 * A Custom ActionListener meant to listen to the different buttons displayed in the dropdown menu
	 * @author Jonas Kastberg
	 */
	public class ButtonListener implements ActionListener
	{
		JButton[] results;
		
		/**
		 * 
		 * @param we pass the results (which is the dropdowns JButton content) to the ButtonListener upon creation
		 */
		public ButtonListener(JButton[] results)
		{
			this.results = results;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			//We iterate through the results and check if the event matches any of their messages.
			for(int i=0; i<results.length; i++)
			{
				if(e.getActionCommand().equals(""+i))
				{
					//If it does, the associated textField's text is set to the buttons content
					textField.setText(results[i].getText());
					
					//We then set the Dropdown's visibility to false
					setVisible(false);
				}
			}
		}
	}
	
	private void makeContent()
	{
		//Upon creation we simply add a JPanel inside our JDialog with a BoxLayout
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		add(panel);
		
		//We make sure that the dialog is undecorated, always on top, not resizable and not focusable.
		//Since these are important attributes of a dropdown menu.
		setAlwaysOnTop(true);
		setUndecorated(true);
		setVisible(false);
		setResizable(false);
		setFocusableWindowState(false);
	}
	
	/**
	 * Updates the position of the Dropdown relative to its textField. This is particularly done whenever the window is moved
	 */
	public void updatePosition()
	{
		//We calculate the offset
		offset = new Point(0, textField.getSize().height);
		
		//We can only update the position in case the textField is shown on the screen.
		if(textField.isShowing())
		{
			//We get a location relative to the textFields location
			Point location = textField.getLocationOnScreen();
			
			//and then offsets it by the desired amount
			setLocation(location.x+offset.x, location.y+offset.y);
		}
	}
	
	/**
	 * Sets the content of the Dropdown
	 * @param strings: We assign the content of the dropdown by passing an array of strings of which it creates a button for each
	 * @param clickable: We decide whether or not the buttons are clickable. This could be whenever we want to display an error message instead of an actual result
	 */
	public void setContent(String[] strings, boolean clickable)
	{
		//We start out by removing everything from the panel inside the JDialog
		panel.removeAll();
		
		//We then add the BoxLayout that was associated
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		
		//We initialize an ArrayList of JButtons
		ArrayList<JButton> buttonList = new ArrayList<JButton>();
		
		//We set the size of both the panel and the JDialog to the width of the associated textfield and a height of the textField times the amount of content strings
		setSize(textField.getSize().width, textField.getSize().height*strings.length);
		panel.setSize(textField.getSize().width, textField.getSize().height*strings.length);
		
		for(int i=0; i<strings.length; i++)
		{
			//We iterate through the string content and add a custom JButton (GUIButton) for each content
			//The custom GUIButton is a standard JButton with modified textures
			GUIButton newButton = new GUIButton(strings[i]);
			
			//We set the presets of the button as per if it is clickable or not
			newButton.setEnabled(clickable);
			newButton.setHoverable(clickable);
			
			//We add the button to our ArrayList
			buttonList.add(newButton);
			
			//We set the size of the button to the width of the panel and the height of the textField
			newButton.setMaximumSize(new Dimension(panel.getSize().width, textField.getSize().height));
			
			//We then add the btton to our panel
			panel.add(newButton);
		}
		
		//We re-initialize our results array
		results = new JButton[buttonList.size()];
		
		//We convert the arraylist to our array
		buttonList.toArray(results);
		
		//We then initialize our ButtonListener using the resulsts
		ButtonListener listener = new ButtonListener(results);
				
		//And add all the buttons to the listener
		for(int i=0; i<results.length; i++)
		{
			results[i].setActionCommand(""+i);
			results[i].addActionListener(listener);
		}
		
		///We then make sure the Dropdown's position is updated
		updatePosition();
		
		//and then sets its visibility to true
		setVisible(true);
	}
}
