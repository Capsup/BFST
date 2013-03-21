package GUI;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import GUI.TranslationPanel.ButtonListener;
import MapDraw.Translation;
import MapDraw.ZoomLevel;


public class ZoomPanel extends JPanel{
	
	public ZoomPanel()
	{
		makeContent();
	}
	
	class ButtonListener implements ActionListener
	{
		public void actionPerformed( ActionEvent event )
		{
			switch( event.getActionCommand() )
			{
				case "+":
					ZoomLevel.getInstance().zoomIn();
				break;

				case "-":
					ZoomLevel.getInstance().zoomOut();
				break;
			}
		}
	}

	private void makeContent()
	{
		setLayout(new BorderLayout());
		
		JButton zoomInButton = new JButton("+");
		
		JButton zoomOutButton = new JButton("-");
		
		ButtonListener listener = new ButtonListener();
		
		zoomInButton.addActionListener(listener);
		zoomOutButton.addActionListener(listener);
		
		add(zoomInButton, BorderLayout.NORTH);
		add(zoomOutButton, BorderLayout.SOUTH);
	}
}
