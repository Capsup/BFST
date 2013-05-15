package GUI;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.text.StyledEditorKit.BoldAction;

/**
 * A Custom JButton in which you can define your own background images, using buffered images.
 * @author Jonas Kastberg
 */
public class GUIButton extends JButton implements MouseListener
{
	private int state;					//This variable indicates the current state of the GUIButton. The state is used as an index in the stateIcon array
	
	private BufferedImage[] stateIcon;	//This is an array of background images for the custom JButton
	
	//Static constants to define the state of the button (The constants correlate directly to the stateIcon array as indexes
	private static final int STATE_NORMAL = 0;
	private static final int STATE_HOVER = 1;
	private static final int STATE_PRESSED = 2;
	
	//These booleans are used to check the status of the button
	private boolean mousePressed;
	private boolean mouseInWindow;
	
	//A boolean to check whether the button is hoverable or not.
	private boolean hoverable = true;
	
	public GUIButton()
	{
		//We initialize ourselves using an empty string.
		this("");
	}
	
	public GUIButton(String string)
	{
		//We initialize our stateIcon Array
		stateIcon = new BufferedImage[3];
		
		//We populate the images using ImageIO to find three hardcoded buffered images
		try {
			stateIcon[0] = ImageIO.read( GUIButton.class.getResource( "/files/Normal.png" ));
			stateIcon[1] = ImageIO.read(GUIButton.class.getResource( "/files/Hover.png" ));
			stateIcon[2] = ImageIO.read(GUIButton.class.getResource( "/files/Active.png" ));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		setText(string);
		
		//The GUIButton class implements a MouseListener, so we can add it like this.
		addMouseListener(this);
	}
	
	//The following methods are implemented and called through the MouseListener
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		
		//We check that the cursor is now inside the button window
		mouseInWindow = true;
		
		//If the button is hoverable we change the state of the button, based on the current conditions
		if(hoverable)
		{
			if(mousePressed)
				state = GUIButton.STATE_PRESSED;
			else
				state = GUIButton.STATE_HOVER;
		}
		
		//We then repaint the button
		repaint();
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	
		//We check that the cursor is now not inside the button window
		mouseInWindow = false;
		
		//If the button is hoverable we reset the state of the button to normal, since the mouse is now no longer inside the button
		if(hoverable)	
			state = GUIButton.STATE_NORMAL;

		//We then repaint the button
		repaint();
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	
		//We check that the button has now been pressed
		mousePressed = true;
		
		//If the button is hoverable we change it to the pressed state
		if(hoverable)
			state = GUIButton.STATE_PRESSED;
		
		//We then repaint the button
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
		//We check that the button has now been released
		mousePressed = false;
		
		//If the button is hoverable we change the state based on whether or not the mouse is inside the button window
		if(hoverable)	
			if(mouseInWindow)
				state = GUIButton.STATE_HOVER;
			else 
				state = GUIButton.STATE_NORMAL;
			
		repaint();
	}
	
	/**
	 * Paint is a method that is called whenever we call repaint
	 */
	public void paint( Graphics g )
	{
		// Draw the background image.
		g.drawImage( stateIcon[state], 0, 0, this.getSize().width, this.getSize().height, null );
		
		//int fontLength = g.getFontMetrics(this.getFont()).stringWidth(this.getText());
		int fontHeight = g.getFontMetrics(this.getFont()).getHeight();
		
		//We change the presets of the font and draw the string based on the text we constructed the button with
		setFont(getFont().deriveFont(Font.BOLD));
		g.drawString(this.getText(), (int)Math.round(this.getSize().width*0.05), this.getSize().height/2+fontHeight/4);
	}
	
	/**
	 * Set the hoverable condition of this button
	 * @param flick
	 */
	public void setHoverable(boolean flick)
	{
		hoverable = flick;
	}
}
