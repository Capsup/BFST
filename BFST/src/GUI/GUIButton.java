package GUI;

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

public class GUIButton extends JButton implements MouseListener
{
	private BufferedImage[] stateIcon;
	private int state;
	private boolean mousePressed;
	private boolean mouseInWindow;
	
	public static void main(String[] args)
	{
		JFrame FUCK = new JFrame();
		
		try {
			GUIButton SHIT = new GUIButton(ImageIO.read( GUIButton.class.getResource( "/images/Normal.png" ))
					, ImageIO.read(GUIButton.class.getResource( "/images/Hover.png" ))
					, ImageIO.read(GUIButton.class.getResource( "/images/Pressed.png" )));
			
			
			SHIT.addMouseListener(SHIT);
			
			FUCK.add(SHIT);
			
			SHIT.setText("FUCK");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		FUCK.setVisible(true);
	}
	
	public GUIButton(BufferedImage normal, BufferedImage hover, BufferedImage active)
	{
		stateIcon = new BufferedImage[3];
	
		stateIcon[0] = normal;
		stateIcon[1] = hover;
		stateIcon[2] = active;
		
		addMouseListener(this);
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
		mouseInWindow = true;
		
		if(mousePressed)
			state = 2;
		else
			state = 1;

		repaint();
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
		mouseInWindow = false;
		
		state = 0;

		repaint();
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
		mousePressed = true;
		
		
		state = 2;
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
		mousePressed = false;
		
		if(mouseInWindow)
			state = 1;
		else 
			state = 0;
		
		repaint();
	}
	
	public void paint( Graphics g )
	{
		// Draw the background image.
		g.drawImage( stateIcon[state], 0, 0, this.getSize().width, this.getSize().height, null );
		
		System.out.println(this.getText());
		
		int fontLength = g.getFontMetrics(this.getFont()).stringWidth(this.getText());
		int fontHeight = g.getFontMetrics(this.getFont()).getHeight();
		
		g.drawString(this.getText(), this.getSize().width/2-fontLength/2, this.getSize().height/2+fontHeight/4);
	}
}
