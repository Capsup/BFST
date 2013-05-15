package GUI;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Action;
import javax.swing.JButton;

/**
 * A reworked JButton made so it looks like an HTML link, while still having the functionality of a standard JButton
 * @author Jonas Kastberg
 */
public class ToggleButton extends JButton
{
	private String text;				//The current text of the button
	private String untoggledText;		//The text that the button should have when it is untoggled
	private String toggledText;			//The text that the button should have when it is toggled
	
	private boolean toggled;			//Whether the button is toggled or not
	
	private boolean hover;				//Whether the button is hovered or not
	private boolean pressed;			//Whether the button is pressed or not
	
	/**
	 * Initialize a ToggleButton
	 * @param untoggledText is the text that the button has when it is untoggled
	 * @param toggledText is the text that the button has when it is toggled
	 */
	public ToggleButton(String untoggledText, String toggledText)
	{
		this.untoggledText = untoggledText;
		this.toggledText = toggledText;
		
		updateText();
		
		makeContent();
	}
	
	/**
	 * Custom MouseListener to add functionality to what should happen whenever the button is accessed
	 * @author Jonas Kastberg
	 */
	public class ToggleButtonMouseListener implements MouseListener
	{
		@Override
		public void mouseClicked(MouseEvent e) {
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			hover = true;
			updateText();
		}

		@Override
		public void mouseExited(MouseEvent e) {
			hover = false;
			updateText();
		}

		@Override
		public void mousePressed(MouseEvent e) {
			pressed = true;
			updateText();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			pressed = false;
			updateText();
		}
	}
	
	private void makeContent()
	{
		setText(text);						//We set the text of the button
		setContentAreaFilled(false);		//We remove the background of the JButton
		setBorderPainted(false);			//We remove the border of the JButton
		setForeground(Color.BLUE);			//We set the color of the buttons text to blue
		setFocusable(false);				//We make sure that the button can not be focused
		
		addMouseListener(new ToggleButtonMouseListener());	//We add our custom listener to the button
	}
	
	/**
	 * Toggles the button and updates its text
	 */
	public void toggle()
	{
		toggled = !toggled;
		
		updateText();
	}
	
	/**
	 * 
	 * @return whether the button is toggled or not
	 */
	public boolean getToggled()
	{
		return toggled;
	}
	
	/**
	 * Update the text and visual look of the button based on the presets
	 */
	private void updateText()
	{
		//Use whatever text is applicable to the current toggle state
		if(toggled)
			text = toggledText;
		else
			text = untoggledText;
		
		//If we are hovering the button we underline it using HTML tags
		if(hover)
			setText("<html><u>"+text+"</u></html>");
		else
			setText(text);
		
		//If we have pressed the button we change the color of the button
		if(pressed)
			setForeground(Color.ORANGE.darker());
		else 
			setForeground(Color.BLUE);
	}

}
