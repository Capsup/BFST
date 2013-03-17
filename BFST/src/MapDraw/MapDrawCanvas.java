package MapDraw;
import java.awt.*;
import javax.swing.*;
import XMLParser.*;

import java.util.ArrayList;

public class MapDrawCanvas extends JFrame {

	public static void main(String[] args){
		new MapDrawCanvas();
	}

	public MapDrawCanvas(){
		XMLParser.makeDataSet();
		init();
		pack();
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	private void init(){
		add(new LinesComponent());
	}
	
	public class LinesComponent extends JComponent{
		public LinesComponent(){
			setPreferredSize(new Dimension(750, 450));
		}
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			ArrayList<Edge> edges = XMLParser.getEdgeList();
			for (Edge e : edges) {
				g.setColor(Color.BLACK);
				Node node = XMLParser.nodeSearch(e.getFromNodeID());
				int x = (int) node.getXCoord();
				int y = (int )node.getYCoord();
				
				node = XMLParser.nodeSearch(e.getFromNodeID());
				int x2 = (int)node.getXCoord();
				int y2 = (int)node.getYCoord();
				
				g.drawLine(x/1000-266,(y/1000-5940)*-1+500, x2/1000-266, (y/1000-5940)*-1+500);
			}
		}
	}
}




