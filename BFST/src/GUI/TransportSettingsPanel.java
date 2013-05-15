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

/**
 * A Panel that allows one to adjust the settings of the transportation methods used while finding a path, using a graphical user interface
 * @author Jonas Kastberg

 */
public class TransportSettingsPanel extends JPanel
{
	private JCheckBox ferryCheckBox;	//We allocate the ferryCheckBox in order to access it after we have made the content of our Panel
	
	/**
	 * A Panel that lets the user set the settings of the transportations methods using a GUI
	 */
	public TransportSettingsPanel()
	{
		makeContent();
	}
	
	/**
	 * A custom ActionListener suited for the TransportSettingsPanel
	 * @author Jonas Kastberg
	 */
	private class RadioButtonListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			
			//A switch to check which of the radio buttons that was pressed
			switch( e.getActionCommand() )
			{
				case "Car":
					
					//Check if the means of transport is already a car
					if(Route.Settings.meansOfTransport() != Route.Settings.car)
					{
						//If not, we set it to car..
						Route.Settings.setMeansOfTransport(Route.Settings.car);
						
						//And recalculate the route
						MapDraw.getInstance().refreshRoute();
					}
					
					break;
					
				case "Bike":
					
					//Check if the means of transport is already a bike
					if(Route.Settings.meansOfTransport() != Route.Settings.bike)
					{
						//Set the ferryCheckBox to true (We always want to use a ferry if possible on a bike)
						ferryCheckBox.setSelected(true);
						
						//Update the ferry settings
						if(ferryCheckBox.isSelected())
							Route.Settings.setFerryAllowed(Route.Settings.yes);
						else
							Route.Settings.setFerryAllowed(Route.Settings.no);
						
						//Set the means of transport to bike
						Route.Settings.setMeansOfTransport(Route.Settings.bike);
						
						//Recalculate the route
						MapDraw.getInstance().refreshRoute();
					}
					
					break;
					
				case "Foot":
					
					//Check if the means of transport is already on foot
					if(Route.Settings.meansOfTransport() != Route.Settings.foot)
					{
						//Set the ferryCheckBox to true (We always want to use a ferry if possible on foot)
						ferryCheckBox.setSelected(true);

						//Update the ferry settings
						if(ferryCheckBox.isSelected())
							Route.Settings.setFerryAllowed(Route.Settings.yes);
						else
							Route.Settings.setFerryAllowed(Route.Settings.no);
						
						//Set the means of transport to bike
						Route.Settings.setMeansOfTransport(Route.Settings.foot);
						
						//Recalculate the route
						MapDraw.getInstance().refreshRoute();
					}
					
					break;
			}
			
			//We set the enablement of the ferry check box based on whether or not we have chosen the car as a transport method
			//We only want it to be an option to not use the ferry if your are using a car
			ferryCheckBox.setEnabled(Route.Settings.meansOfTransport() == Route.Settings.car);
			
			//If the listener was activated because of the ferry check box
			if(e.getActionCommand() == "Ferry")
			{
				//We update the ferry settings
				if(ferryCheckBox.isSelected())
					Route.Settings.setFerryAllowed(Route.Settings.yes);
				else
					Route.Settings.setFerryAllowed(Route.Settings.no);
				
				//And refresh the route
				MapDraw.getInstance().refreshRoute();
			}
		}
	}
	
	private void makeContent()
	{
		//We set the border of the panel to a titled border with a lowered bevel border.
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED), "Transport"));
		
		//We want the button options to be on top of each other so we use a boxlayout with a vertical alignment
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		//We initialize radio buttons for each of the possible vehicles
		JRadioButton carRadioButton = new JRadioButton("Car");
		carRadioButton.setActionCommand("Car");
		carRadioButton.setAlignmentX(RIGHT_ALIGNMENT);					//BoxLayout works in mysterious ways so we right align the car radio button in order to left allign it
																		//Seriously though, this works, so lets leave it for now
		
		JRadioButton bikeRadioButton = new JRadioButton("Bicycle");
		bikeRadioButton.setActionCommand("Bike");
		bikeRadioButton.setAlignmentX(LEFT_ALIGNMENT);
		
		JRadioButton footRadioButton = new JRadioButton("Foot");
		footRadioButton.setActionCommand("Foot");
		footRadioButton.setAlignmentX(LEFT_ALIGNMENT);
		
		//We see what the current means of transport is, and select the appropriate radio button
		if(Route.Settings.meansOfTransport() == Route.Settings.car)
			carRadioButton.setSelected(true);
		else if(Route.Settings.meansOfTransport() == Route.Settings.bike)
			bikeRadioButton.setSelected(true);
		else
			footRadioButton.setSelected(true);
		
		//We initialize a button group and add all of our radio buttons to it
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(carRadioButton);
		buttonGroup.add(bikeRadioButton);
		buttonGroup.add(footRadioButton);
		
		//We make an auxiliary panel with a boxlayout in order to have a right allignet component (all components need to be left allignet in a boxlayout if it has to work for any of them)
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setAlignmentX(LEFT_ALIGNMENT);	//We left allign the panel
		
		//We initialize our ferry check box
		ferryCheckBox = new JCheckBox("Ferry");
		ferryCheckBox.setActionCommand("Ferry");
		
		//We check for the current settings of our ferry allowance and set the check box likewise
		if(Route.Settings.ferryAllowed() == 0)
			ferryCheckBox.setSelected(true);
		else
			ferryCheckBox.setSelected(false);
		
		ferryCheckBox.setEnabled(Route.Settings.meansOfTransport() == Route.Settings.car);
		ferryCheckBox.setAlignmentX(LEFT_ALIGNMENT);	//We left align the check box in order to right align it. Really.. BoxLayout.. It's not my fault..
		
		//We add our car radio button and our ferry check box to the auxiliary panel
		panel.add(carRadioButton);
		panel.add(ferryCheckBox);
		
		//We add the rest of the components to the main panel
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
