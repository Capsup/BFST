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

import MapDraw.MapDraw;


public class MainFrame extends JFrame
{	
	public MainFrame()
	{
		makeContent();
		setupFrame();
	}
	
	void setupFrame()
	{
		this.setSize( 1200, 600 );
		this.setTitle( "Main Menu" );
		this.setLocationRelativeTo( null );
		this.setVisible( true );
		
		this.setDefaultCloseOperation( EXIT_ON_CLOSE );
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
		JPanel mapPanel = new MapDraw();
		mapPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

		
		mainPanel.add(menuPanel, BorderLayout.WEST);
		mainPanel.add(mapPanel, BorderLayout.CENTER);
		
		contentPane.add(mainPanel);
		
	}
	
	public static void main(String[] args)
	{
		new MainFrame();
	}
}
