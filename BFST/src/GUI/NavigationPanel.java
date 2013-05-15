package GUI;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

/**
 * A collected module for the TranslatePanel and the ZoomPanel
 * @author Jonas Kastberg
 *
 */
public class NavigationPanel extends JPanel
{
	/**
	 * A collected module for the TranslatePanel and the ZoomPanel
	 */
	public NavigationPanel()
	{
		makeContent();
	}
	
	void makeContent()
	{
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		
		TranslationPanel translationPanel = new TranslationPanel();
		ZoomPanel zoomPanel = new ZoomPanel();
		
		add(translationPanel);
		add(Box.createHorizontalGlue());
		add(zoomPanel);
		
	}
}
