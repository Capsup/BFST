package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.BevelBorder;

import MapDraw.MapDraw;

public class PathSettingsPanel extends JPanel
{

	public PathSettingsPanel()
	{
		makeContent();
	}
	
	private class RadioButtonListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			
			if(e.getActionCommand() == "Fastest")
			{
				if(Route.Settings.routeProfile() != Route.Settings.fastest_route)
				{
					Route.Settings.setRouteProfile(Route.Settings.fastest_route);
					
					MapDraw.getInstance().refreshRoute();
				}
			}
			
			if(e.getActionCommand() == "Shortest")
			{
				if(Route.Settings.routeProfile() != Route.Settings.shortest_route)
				{
					Route.Settings.setRouteProfile(Route.Settings.shortest_route);
					
					MapDraw.getInstance().refreshRoute();
				}
			}
		}
	}
	private void makeContent()
	{
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED), "Route"));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JRadioButton fastestRadioButton = new JRadioButton("Fastest Route");
		fastestRadioButton.setActionCommand("Fastest");
	    
		JRadioButton shortestRadioButton = new JRadioButton("Shortest Route");
		shortestRadioButton.setActionCommand("Shortest");
	    
		if(Route.Settings.routeProfile() == Route.Settings.fastest_route)
			fastestRadioButton.setSelected(true);
		else
			shortestRadioButton.setSelected(true);
		
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(fastestRadioButton);
		buttonGroup.add(shortestRadioButton);
		
		add(fastestRadioButton);
		add(shortestRadioButton);
		
		ActionListener listener = new RadioButtonListener();
		
		fastestRadioButton.addActionListener(listener);
		shortestRadioButton.addActionListener(listener);
	}
}
