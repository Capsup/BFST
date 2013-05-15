package GUI;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.BevelBorder;

import MapDraw.MapDraw;

public class TransportSettingsPanel extends JPanel
{
	private JCheckBox ferryCheckBox;
	
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
			
			ferryCheckBox.setEnabled(Route.Settings.meansOfTransport() == Route.Settings.car);
			
			if(e.getActionCommand() == "Ferry")
			{
				System.out.println(ferryCheckBox.isSelected());
				//Route.Settings.
			}
		}
	}
	
	private void makeContent()
	{
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED), "Transport"));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JRadioButton carRadioButton = new JRadioButton("Car");
		carRadioButton.setActionCommand("Car");
		carRadioButton.setAlignmentX(RIGHT_ALIGNMENT);
		
		JRadioButton bikeRadioButton = new JRadioButton("Bicycle");
		bikeRadioButton.setActionCommand("Bike");
		bikeRadioButton.setAlignmentX(LEFT_ALIGNMENT);
		
		JRadioButton footRadioButton = new JRadioButton("Foot");
		footRadioButton.setActionCommand("Foot");
		footRadioButton.setAlignmentX(LEFT_ALIGNMENT);
		
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
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setAlignmentX(LEFT_ALIGNMENT);
		
		ferryCheckBox = new JCheckBox("Ferry");
		ferryCheckBox.setActionCommand("Ferry");
		ferryCheckBox.setEnabled(Route.Settings.meansOfTransport() == Route.Settings.car);
		ferryCheckBox.setAlignmentX(LEFT_ALIGNMENT);
		
		panel.add(carRadioButton);
		panel.add(ferryCheckBox);
		
		add(panel);
		add(bikeRadioButton);
		add(footRadioButton);
		
		//Add action listeners
		ActionListener listener = new RadioButtonListener();
		
		carRadioButton.addActionListener(listener);
		ferryCheckBox.addActionListener(listener);
		bikeRadioButton.addActionListener(listener);
		footRadioButton.addActionListener(listener);
	}
}
