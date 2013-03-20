package GUI;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;


public class MainFrame extends JFrame
{	
	public MainFrame()
	{
		setupFrame();
		makeContent();
	}
	
	void setupFrame()
	{
		this.setSize( 800, 600 );
		this.setTitle( "Main Menu" );
		this.setLocationRelativeTo( null );
		this.setVisible( true );
	}
	
	void makeContent()
	{
		Container contentPane = this.getContentPane();
		
		//Main Panel Setup
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		
		//Menu Panel <-- Add menu panel extending JPanel here
		JPanel menuPanel = new MenuPanel();
		menuPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		
		//Map Panel <-- Add map class extending JPanel here
		//JPanel mapPanel = new JPanel();
		//mapPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		
		//Lolz
		JPanel mapPanel;
		
		try
		{
			// We try to find an image for the main panel to paint.
			String path = "Danmark.png";

			Rectangle rect = new Rectangle( 0, 0, 600, 600 );

			mapPanel = new JPanelWithBackground( path, rect );
		}
		catch( IOException e )
		{
			// If no image is found, the panel will be initialized with no background image.
			mapPanel = new JPanel();
		}
		
		mainPanel.add(menuPanel, BorderLayout.WEST);
		mainPanel.add(mapPanel, BorderLayout.CENTER);
		
		contentPane.add(mainPanel);
		
		validate();
	}
	
	public static void main(String[] args)
	{
		new MainFrame();
	}
}
