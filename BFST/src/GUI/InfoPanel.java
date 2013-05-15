package GUI;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

/**
 * A panel that shows the info of the current path, based on the information given by the PathInformation class
 * @author Jonas Kastberg
 */
public class InfoPanel extends JPanel implements Observer{

	//We allocate all the JLabels that we need to change at any given moment
	private JLabel fromInfoLabel;
	private JLabel toInfoLabel;
	private JLabel lengthInfoLabel;
	private JLabel travelTimeInfoLabel;
	
	/**
	 * Initializes a panel that shows info based on the information in the PathInformation class
	 */
	public InfoPanel()
	{
		makeContent();
		
		//We add this as an observer to the PathInformation singleton
		PathInformation.getInstance().addObserver(this);
	}
	
	private void makeContent()
	{
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED), "Path Information"));
		
		//We make a panel to hold all the labels of the information.
		JPanel labelPanel = new JPanel();
		labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
		
		//We initialize all the JLabels and add them to the labelPanel in a BoxLayout
		JLabel fromLabel = new JLabel("From: ");
		JLabel toLabel = new JLabel("To: ");
		JLabel lengthLabel = new JLabel("Length: ");
		JLabel travelTimeLabel = new JLabel("Travel Time: ");

		labelPanel.add(fromLabel);
		labelPanel.add(toLabel);
		labelPanel.add(lengthLabel);
		labelPanel.add(travelTimeLabel);
		
		//We Initialize a panel to hold the actual information described by the labelPanel
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
		
		//We initialize all of the JLabels aswell as set their alignment to right alignment
		fromInfoLabel = new JLabel();
		fromInfoLabel.setHorizontalAlignment(JLabel.RIGHT);
		toInfoLabel = new JLabel();
		toInfoLabel.setHorizontalAlignment(JLabel.RIGHT);
		lengthInfoLabel = new JLabel();
		lengthInfoLabel.setHorizontalAlignment(JLabel.RIGHT);
		travelTimeInfoLabel = new JLabel();
		travelTimeInfoLabel.setHorizontalAlignment(JLabel.RIGHT);

		//We add all the labels to our infoPanel using a BoxLayout
		infoPanel.add(fromInfoLabel);
		infoPanel.add(toInfoLabel);
		infoPanel.add(lengthInfoLabel);
		infoPanel.add(travelTimeInfoLabel);
		
		//We add the two panels to our main panel using a BorderLayout, putting them up next to each other
		add(labelPanel, BorderLayout.WEST);
		add(infoPanel, BorderLayout.EAST);
		
		//We set the visibility of the panel to false, since the visibility should be directed by the PathInformation singleton.
		setVisible(false);
	}

	/**
	 * Updates the information in the panel based on the information given in the PathInformation singleton
	 */
	private void updatePanel()
	{
		//We make sure we only need to call the singleton once.
		PathInformation pathInfo = PathInformation.getInstance();
		
		//We set the text of each of our JLabels to the directed string
		fromInfoLabel.setText(pathInfo.getFrom());
		toInfoLabel.setText(pathInfo.getTo());
		lengthInfoLabel.setText(pathInfo.getLength());
		travelTimeInfoLabel.setText(pathInfo.getTravelTime());
		
		//We adjust the size of our panel to make sure that we can see the entire panel
		setMaximumSize(new Dimension(getParent().getPreferredSize().width, getPreferredSize().height));
		
		//We set the visibility of the panel to that of the PathInformation singleton
		setVisible(pathInfo.getVisible());
		
		//We revalidate and repaint the panel in order to make effect of the changes we have made
		revalidate();
		repaint();
	}
	
	@Override
	public void update(Observable o, Object arg) {
		
		//This method is called whenever the Observer that we are subscribed to is updated.
		updatePanel();	
	}
}
