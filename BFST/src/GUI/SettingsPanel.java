package GUI;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.BevelBorder;

import MapDraw.MapDraw;

public class SettingsPanel extends JPanel
{
	public SettingsPanel()
	{
		makeContent();
	}
	
	public class RadioButtonListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
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
	
	public void makeContent()
	{
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED), "Path Settings"));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JRadioButton fastestRadioButton = new JRadioButton("Fastest");
		fastestRadioButton.setSelected(true);
		fastestRadioButton.setActionCommand("Fastest");
	    
		JRadioButton shortestRadioButton = new JRadioButton("Shortest");
		shortestRadioButton.setActionCommand("Shortest");
	    
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(fastestRadioButton);
		buttonGroup.add(shortestRadioButton);
		
		add(fastestRadioButton);
		add(shortestRadioButton);
		
		ActionListener listener = new RadioButtonListener();
		
		fastestRadioButton.addActionListener(listener);
		shortestRadioButton.addActionListener(listener);
		
		//setMaximumSize(new Dimension(getParent().getPreferredSize().width, getPreferredSize().height));
	}
}
