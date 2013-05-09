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

public class GUIButton extends JButton implements MouseListener
{
	private BufferedImage[] stateIcon;
	private int state;
	private boolean mousePressed;
	private boolean mouseInWindow;
	
	public static void main(String[] args)
	{
		JFrame FUCK = new JFrame();
	
		GUIButton SHIT = new GUIButton("Aww Yeah");
		
		SHIT.addMouseListener(SHIT);
		
		FUCK.add(SHIT);
		
		SHIT.setText("FUCK");

	
		
		
		FUCK.setVisible(true);
	}
	
	public GUIButton()
	{
		stateIcon = new BufferedImage[3];
		
		try {
			stateIcon[0] = ImageIO.read( GUIButton.class.getResource( "/files/Normal.png" ));
			stateIcon[1] = ImageIO.read(GUIButton.class.getResource( "/files/Hover.png" ));
			stateIcon[2] = ImageIO.read(GUIButton.class.getResource( "/files/Active.png" ));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		addMouseListener(this);
	}
	
	public GUIButton(String string)
	{
		stateIcon = new BufferedImage[3];
	
		try {
			stateIcon[0] = ImageIO.read( GUIButton.class.getResource( "/files/Normal.png" ));
			stateIcon[1] = ImageIO.read(GUIButton.class.getResource( "/files/Hover.png" ));
			stateIcon[2] = ImageIO.read(GUIButton.class.getResource( "/files/Active.png" ));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		setText(string);
		
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
		
		Font font = getFont();
		
		repaint();
		
		setFont(font);
		
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
		
		int fontLength = g.getFontMetrics(this.getFont()).stringWidth(this.getText());
		int fontHeight = g.getFontMetrics(this.getFont()).getHeight();
		
		setFont(getFont().deriveFont(Font.BOLD));
		g.drawString(this.getText(), (int)Math.round(this.getSize().width*0.05), this.getSize().height/2+fontHeight/4);
	}
}
