package GUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.TextAttribute;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.BevelBorder;

import MapDraw.MapDraw;

/**
 * The settings panel is a module that contains the PathSettingsPanel and the TransportSettingsPanel
 * @author Jonas Kastberg
 *
 */
public class SettingsPanel extends JPanel
{
	//We allocate a custom ToggleButton that lets us toggle the main settings panel.
	ToggleButton toggleButton;
	
	public SettingsPanel()
	{
		makeContent();
	}
	
	/**
	 * Custom ActionListener that lets us run code whenever the toggle button is pressed.
	 * @author Jonas Kastberg
	 */
	private class ToggleButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			
			if(e.getActionCommand() == "Toggled")
			{
				//We toggle the button (showing/hiding the main settings panel)
				toggleButton.toggle();
				
				//And update the panel in order to make the toggle take effect
				updatePanel();
			}
		}
		
	}
	
	public void makeContent()
	{
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		//We initialize the ToggleButton (a modified JButton) and add our custom ToggleButtonListener to it
		toggleButton = new ToggleButton("Show Options", "Hide Options");
		toggleButton.setActionCommand("Toggled");
		toggleButton.addActionListener(new ToggleButtonListener());
		toggleButton.setAlignmentX(CENTER_ALIGNMENT);
		
		//we add the toggleButton to the main panel.
		add(toggleButton);
	}
	
	private void updatePanel()
	{
		if(toggleButton.getToggled())
		{
			//We initialize a container panel, since it is easier to work with
			JPanel containerPanel = new JPanel();
			containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.X_AXIS));
			containerPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED), "Settings"));
			
			//We initialize our PathSettingsPanel and add it to the containerPanel
			JPanel pathSettingsPanel = new PathSettingsPanel();
			containerPanel.add(pathSettingsPanel);
			
			//We initialize our TransportSettingsPanel and add it to the containerPane
			JPanel transportSettingsPanel = new TransportSettingsPanel();
			containerPanel.add(transportSettingsPanel);
			
			//We resize the size of the pathSettingsPanel so it matches the height of the transport settings panel, this looks better..
			pathSettingsPanel.setMaximumSize(new Dimension(pathSettingsPanel.getPreferredSize().width, transportSettingsPanel.getPreferredSize().height));
			
			//We set the alignment of the containerPanel and add it to our main panel.
			containerPanel.setAlignmentX(CENTER_ALIGNMENT);
			add(containerPanel);
			
			//We resize the panel so it is big enough to show the newly added panels
			setMaximumSize(new Dimension(getParent().getPreferredSize().width, getPreferredSize().height));
			
		}
		else 
		{
			//If we untoggle the settings menu we remove all components but the toggleButton from our SettingsPanel.
			int componentCount = getComponentCount();
			
			for(int i=1; i<componentCount; i++)
			{
				remove(1);
			}
			
			//We resize the panel so it does not take up more space than needed
			setMaximumSize(new Dimension(getParent().getPreferredSize().width, getPreferredSize().height));
			
		}
		
	}
}