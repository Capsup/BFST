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

public class InfoPanel extends JPanel implements Observer{

	private JLabel fromLabel;
	private JLabel toLabel;
	private JLabel lengthLabel;
	private JLabel travelTimeLabel;
	
	private JLabel fromInfoLabel;
	private JLabel toInfoLabel;
	private JLabel lengthInfoLabel;
	private JLabel travelTimeInfoLabel;
	
	public InfoPanel()
	{
		makeContent();
		
		PathInformation.getInstance().addObserver(this);
		
		//MainFrame.getInstance().addKeyListener(new MyKeyEventListener());
	}
	/*
	public class MyKeyEventListener implements KeyListener
	{

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			
			System.out.println(e);
			
			if(e.getKeyCode() == KeyEvent.VK_SPACE)
			{
				setVisible(!isVisible());
				
				revalidate();
				repaint();
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	*/
	
	private void makeContent()
	{
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED), "Path Information"));
		
		JPanel labelPanel = new JPanel();
		
		labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
		
		/*JLabel fromToLabel = new JLabel("From: Vesterfælledvej 1, 1750 - To: Vesterfælled 10, 6510");
		JLabel distanceLabel = new JLabel("Distance: 1337 km", JLabel.CENTER);
		JLabel timeLabel = new JLabel("Travel time: 13h 37m", JLabel.CENTER);
		
		add(fromToLabel);
		add(distanceLabel);
		add(timeLabel);
		*/
		
		fromLabel = new JLabel("From: ");
		toLabel = new JLabel("To: ");
		lengthLabel = new JLabel("Length: ");
		travelTimeLabel = new JLabel("Travel Time: ");

		labelPanel.add(fromLabel);
		labelPanel.add(toLabel);
		labelPanel.add(lengthLabel);
		labelPanel.add(travelTimeLabel);
		
		JPanel infoPanel = new JPanel();
		
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
		
		fromInfoLabel = new JLabel();
		fromInfoLabel.setHorizontalAlignment(JLabel.RIGHT);
		toInfoLabel = new JLabel();
		toInfoLabel.setHorizontalAlignment(JLabel.RIGHT);
		lengthInfoLabel = new JLabel();
		lengthInfoLabel.setHorizontalAlignment(JLabel.RIGHT);
		travelTimeInfoLabel = new JLabel();
		travelTimeInfoLabel.setHorizontalAlignment(JLabel.RIGHT);

		infoPanel.add(fromInfoLabel);
		infoPanel.add(toInfoLabel);
		infoPanel.add(lengthInfoLabel);
		infoPanel.add(travelTimeInfoLabel);
		
		add(labelPanel, BorderLayout.WEST);
		add(infoPanel, BorderLayout.EAST);
		
		setVisible(false);
		
		
	}

	private void updatePanel()
	{
		/*
		int componentCount = getComponentCount();
		
		for(int i=0; i < componentCount; i++)
		{
			remove(0);
		}
		*/
		//removeAll();
		
		PathInformation pathInfo = PathInformation.getInstance();
		
		fromInfoLabel.setText(pathInfo.getFrom());
		toInfoLabel.setText(pathInfo.getTo());
		lengthInfoLabel.setText(pathInfo.getLength());
		travelTimeInfoLabel.setText(pathInfo.getTravelTime());
		
		/*
		for(int i=0; i < infoStrings.length; i++)
		{
			JLabel label = new JLabel(infoStrings[i]);
			
			add(label);
		}
		*/
		setMaximumSize(new Dimension(getParent().getPreferredSize().width, getPreferredSize().height));
		
		setVisible(pathInfo.getVisible());
		
		revalidate();
		repaint();
	}
	
	@Override
	public void update(Observable o, Object arg) {
		
		updatePanel();	
	}
}
