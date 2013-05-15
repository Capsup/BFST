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

public class SettingsPanel extends JPanel
{
	ToggleButton toggleButton;
	
	public SettingsPanel()
	{
		makeContent();
	}
	
	private class ToggleButtonListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			
			if(e.getActionCommand() == "Toggled")
			{
				toggleButton.toggle();
				
				updatePanel();
			}
		}
		
	}
	
	public void makeContent()
	{
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		toggleButton = new ToggleButton("Show Options", "Hide Options");
		toggleButton.setActionCommand("Toggled");
		toggleButton.addActionListener(new ToggleButtonListener());
		toggleButton.setAlignmentX(CENTER_ALIGNMENT);
		
		add(toggleButton);
	}
	
	private void updatePanel()
	{
		if(toggleButton.getToggled())
		{
			JPanel containerPanel = new JPanel();
			containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.X_AXIS));
			containerPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED), "Settings"));
			
			
			JPanel pathSettingsPanel = new PathSettingsPanel();
			//pathSettingsPanel.setAlignmentX(CENTER_ALIGNMENT);
			containerPanel.add(pathSettingsPanel);
			
			JPanel transportSettingsPanel = new TransportSettingsPanel();
			//transportSettingsPanel.setAlignmentX(CENTER_ALIGNMENT);
			containerPanel.add(transportSettingsPanel);
			
			containerPanel.setAlignmentX(CENTER_ALIGNMENT);
			add(containerPanel);
			
			setMaximumSize(new Dimension(getParent().getPreferredSize().width, getPreferredSize().height));
			
		}
		else 
		{
			int componentCount = getComponentCount();
			
			for(int i=1; i<componentCount; i++)
			{
				remove(1);
			}
			
			setMaximumSize(new Dimension(getParent().getPreferredSize().width, getPreferredSize().height));
			
		}
		
	}
}