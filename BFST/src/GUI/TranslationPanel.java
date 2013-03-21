package GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import MapDraw.Translation;


public class TranslationPanel extends JPanel{
	
	public TranslationPanel()
	{
		makeContent();
	}
	
	class ButtonListener implements ActionListener
	{
		public void actionPerformed( ActionEvent event )
		{
			switch( event.getActionCommand() )
			{
				case "Up":
					Translation.getInstance().translate(0, 10);
				break;

				case "Down":
					Translation.getInstance().translate(0, -10);
				break;

				case "Right":
					Translation.getInstance().translate(-10, 0);
				break;

				case "Left":
					Translation.getInstance().translate(10, 0);
				break;
			}
		}
	}

	private void makeContent()
	{
		setLayout(new BorderLayout());
		
		JButton upButton = new JButton("^");
		upButton.setActionCommand("Up");
		
		JButton downButton = new JButton("down");
		downButton.setActionCommand("Down");
		
		JButton rightButton = new JButton(">");
		rightButton.setActionCommand("Right");
		
		JButton leftButton = new JButton("<");
		leftButton.setActionCommand("Left");
		
		ButtonListener listener = new ButtonListener();
		
		upButton.addActionListener(listener);
		downButton.addActionListener(listener);
		rightButton.addActionListener(listener);
		leftButton.addActionListener(listener);
		
		add(upButton, BorderLayout.NORTH);
		add(downButton, BorderLayout.SOUTH);
		add(rightButton, BorderLayout.EAST);
		add(leftButton, BorderLayout.WEST);
	}
}
