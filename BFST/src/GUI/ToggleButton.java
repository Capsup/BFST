package GUI;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Action;
import javax.swing.JButton;

public class ToggleButton extends JButton
{
	private String text;
	private String untoggledText;
	private String toggledText;
	
	private boolean toggled;
	
	private boolean hover;
	private boolean pressed;
	
	public ToggleButton(String untoggledText, String toggledText)
	{
		this.untoggledText = untoggledText;
		this.toggledText = toggledText;
		
		updateText();
		
		makeContent();
	}
	
	public class ToggleButtonMouseListener implements MouseListener
	{

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
			hover = true;
			updateText();
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
			hover = false;
			updateText();
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			pressed = true;
			updateText();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
			pressed = false;
			updateText();
		}
	}
	
	private void makeContent()
	{
		setText(text);
		setContentAreaFilled(false);
		setBorderPainted(false);
		setForeground(Color.BLUE);
		setFocusable(false);
		
		addMouseListener(new ToggleButtonMouseListener());
	}
	
	public void toggle()
	{
		toggled = !toggled;
		
		updateText();
	}
	
	public boolean getToggled()
	{
		return toggled;
	}
	
	private void updateText()
	{
		if(toggled)
			text = toggledText;
		else
			text = untoggledText;
		
		if(hover)
			setText("<html><u>"+text+"</u></html>");
		else
			setText(text);
		
		if(pressed)
			setForeground(Color.ORANGE.darker());
		else 
			setForeground(Color.BLUE);
	}

}
