package GUI;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

public class NavigationPanel extends JPanel
{
	
	public NavigationPanel()
	{
		makeContent();
	}
	
	void makeContent()
	{
		//JLabel headerLabel = new JLabel("Searching: ");
		
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		
		TranslationPanel translationPanel = new TranslationPanel();
		
		ZoomPanel zoomPanel = new ZoomPanel();
		
		add(translationPanel);
		add(Box.createHorizontalGlue());
		add(zoomPanel);
		
	}
}
