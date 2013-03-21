package GUI;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import GUI.TranslationPanel.ButtonListener;
import MapDraw.Translation;
import MapDraw.ZoomLevel;


public class ZoomPanel extends JPanel implements Observer{
	
	private JLabel currentZoomLabel;
	
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
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JLabel headerLabel = new JLabel("Zooming:");
		headerLabel.setAlignmentX( CENTER_ALIGNMENT );
		
		JButton zoomInButton = new JButton("+");
		zoomInButton.setAlignmentX(CENTER_ALIGNMENT);
		
		currentZoomLabel = new JLabel("Zoom Level: "+ZoomLevel.getInstance().getZoomIndex());
		currentZoomLabel.setAlignmentX(CENTER_ALIGNMENT);
		
		JButton zoomOutButton = new JButton("-");
		zoomOutButton.setAlignmentX(CENTER_ALIGNMENT);
		
		ButtonListener listener = new ButtonListener();
		
		zoomInButton.addActionListener(listener);
		zoomOutButton.addActionListener(listener);
		
		add(headerLabel);
		add(zoomInButton);
		add(currentZoomLabel);
		add(zoomOutButton);
		
		
		//Other
		ZoomLevel.getInstance().addObserver(this);
	}

	@Override
	public void update(Observable o, Object arg) 
	{
		currentZoomLabel.setText("Zoom Level: "+ZoomLevel.getInstance().getZoomIndex());
	}
	
	
}
