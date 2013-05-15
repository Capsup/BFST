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

public class TransportSettingsPanel extends JPanel
{
	public TransportSettingsPanel()
	{
		makeContent();
	}
	
	private class RadioButtonListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			
			if(e.getActionCommand() == "Car")
			{
				if(Route.Settings.meansOfTransport() != Route.Settings.car)
				{
					Route.Settings.setMeansOfTransport(Route.Settings.car);
					
					MapDraw.getInstance().refreshRoute();
				}
			}
			
			if(e.getActionCommand() == "Bike")
			{
				if(Route.Settings.meansOfTransport() != Route.Settings.bike)
				{
					Route.Settings.setMeansOfTransport(Route.Settings.bike);
					
					MapDraw.getInstance().refreshRoute();
				}
			}
			
			if(e.getActionCommand() == "Foot")
			{
				if(Route.Settings.meansOfTransport() != Route.Settings.foot)
				{
					Route.Settings.setMeansOfTransport(Route.Settings.foot);
					
					MapDraw.getInstance().refreshRoute();
				}
			}
		}
	}
	private void makeContent()
	{
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED), "Transport"));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JRadioButton carRadioButton = new JRadioButton("Car");
		carRadioButton.setActionCommand("Car");
	    
		JRadioButton bikeRadioButton = new JRadioButton("Bicycle");
		bikeRadioButton.setActionCommand("Bike");
	    
		JRadioButton footRadioButton = new JRadioButton("Foot");
		footRadioButton.setActionCommand("Foot");
	    
		if(Route.Settings.meansOfTransport() == Route.Settings.car)
			carRadioButton.setSelected(true);
		else if(Route.Settings.meansOfTransport() == Route.Settings.bike)
			bikeRadioButton.setSelected(true);
		else
			footRadioButton.setSelected(true);
		
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(carRadioButton);
		buttonGroup.add(bikeRadioButton);
		buttonGroup.add(footRadioButton);
		
		add(carRadioButton);
		add(bikeRadioButton);
		add(footRadioButton);
		
		ActionListener listener = new RadioButtonListener();
		
		carRadioButton.addActionListener(listener);
		bikeRadioButton.addActionListener(listener);
		footRadioButton.addActionListener(listener);
	}
}
