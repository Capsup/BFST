package GUI;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.management.InstanceAlreadyExistsException;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import MapDraw.MapDraw;

public class MainFrame extends JFrame
{	
	private static MainFrame instance;
	
	/*
	 * The Main Frame is the window of the program, it contains the menu, and the map.
	 */
	private MainFrame()
	{
		instance = this;
		
		makeContent();
		setupFrame();
	}
	
	public static MainFrame getInstance()
	{
		if(instance == null)
			new MainFrame();
		
		return instance;
	}
	
	void setupFrame()
	{
		this.setSize( 1200, 800 );
		this.setTitle( "Main Menu" );
		this.setLocationRelativeTo( null );
		this.setVisible( true );
		this.setDefaultCloseOperation( EXIT_ON_CLOSE );
	}
	
	void makeContent()
	{
		Container contentPane = this.getContentPane();
		
		
		//Map Panel
		MapDraw mapPanel = MapDraw.getInstance();
		mapPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

		//Main Panel Setup
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		
		//Menu Panel
		MenuPanel menuPanel = new MenuPanel(mapPanel);
		menuPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		
		
		//Add the panels to the Main Panel
		mainPanel.add(menuPanel, BorderLayout.WEST);
		mainPanel.add(mapPanel, BorderLayout.CENTER);
		
		//Add the main panel to the frame
		contentPane.add(mainPanel);
	}
	
	public static void main(String[] args)
	{
		MainFrame.getInstance();
	}
}
