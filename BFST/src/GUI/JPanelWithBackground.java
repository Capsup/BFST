package GUI;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * The JPanelWithBackground class is a class that creates a jpanel with a background image.
 * 
 * @author Martin Juul Petersen (mjup@itu.dk), Jesper Nysteen (jnys@itu.dk) and Jonas Kastberg (jkas@itu.dk)
 * @version 1.0
 * 
 */
public class JPanelWithBackground extends JPanel
{
	// We use these fields to save the background image as
	// well as the rect that we pass to the constructor
	private BufferedImage backgroundImage;
	private Rectangle rect;

	public JPanelWithBackground( String fileName, Rectangle rect ) throws IOException
	{
		// We initialize the class by passing it a string and a rectangle.

		// We use the passed string to search for an image in our Images folder.
		backgroundImage = ImageIO.read( getClass().getResource( fileName ) );

		// We set the rect field to the passed rect.
		this.rect = rect;
	}

	public void paintComponent( Graphics g )
	{
		super.paintComponent( g );

		// Draw the background image.
		g.drawImage( backgroundImage, rect.x, rect.y, rect.width, rect.height, null );
	}
}
