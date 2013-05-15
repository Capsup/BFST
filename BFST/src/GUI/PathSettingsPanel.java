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

/**
 * A Panel that allows one to change the path settings sed while finding a path, using a GUI
 * @author Jonas Kastberg
 */
public class PathSettingsPanel extends JPanel
{

	/**
	 * A Panel that allows one to change the path settings sed while finding a path, using a GUI
	 * @author Jonas Kastberg
	 */
	public PathSettingsPanel()
	{
		makeContent();
	}
	
	/**
	 * Custom ActionListener that let us react to the pressing of radio buttons in the PathSettingsPanel
	 * @author Jonas Kastberg
	 */
	private class RadioButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			
			if(e.getActionCommand() == "Fastest")
			{
				//If we toggled the Fastest radio button we check if the current route profile is already fastest
				if(Route.Settings.routeProfile() != Route.Settings.fastest_route)
				{
					//If it is not, we set the profile to fastest
					Route.Settings.setRouteProfile(Route.Settings.fastest_route);
					
					//And refresh the route
					MapDraw.getInstance().refreshRoute();
				}
			}
			
			if(e.getActionCommand() == "Shortest")
			{
				//If we toggled the Shortest radio button we check if the current route profile is already fastest
				if(Route.Settings.routeProfile() != Route.Settings.shortest_route)
				{
					//If it is not, we set the profile to shortest
					Route.Settings.setRouteProfile(Route.Settings.shortest_route);
					
					//And refresh the route
					MapDraw.getInstance().refreshRoute();
				}
			}
		}
	}
	private void makeContent()
	{
		//We set the border of the panel to a titled border with a lowered bevel border.
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED), "Route"));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JRadioButton fastestRadioButton = new JRadioButton("Fastest Route");
		fastestRadioButton.setActionCommand("Fastest");
	    
		JRadioButton shortestRadioButton = new JRadioButton("Shortest Route");
		shortestRadioButton.setActionCommand("Shortest");
	    
		//We check the current route profile being used and set the toggled radio button accordingly
		if(Route.Settings.routeProfile() == Route.Settings.fastest_route)
			fastestRadioButton.setSelected(true);
		else
			shortestRadioButton.setSelected(true);
		
		//We initialize a button group and add the radio buttons
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(fastestRadioButton);
		buttonGroup.add(shortestRadioButton);
		
		//We add our radio buttons to the main panel
		add(fastestRadioButton);
		add(shortestRadioButton);
		
		//We initialize our custom listener and add it to the radio buttons
		ActionListener listener = new RadioButtonListener();
		
		fastestRadioButton.addActionListener(listener);
		shortestRadioButton.addActionListener(listener);
	}
}
