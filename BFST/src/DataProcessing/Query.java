package DataProcessing;

import java.util.ArrayList;
import java.util.LinkedList;
import XMLParser.Edge;
import XMLParser.Node;
import XMLParser.XMLParser;

public class Query implements Runnable{
	private ArrayList<LinkedList<Edge>> edges;
	private ArrayList<Node> nodes;
	private Thread edge;
	
	
	public void run(){
		XMLParser edge = new XMLParser("kdv_unload.xml");
		try {
			edges = edge.getEdges();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Query(){
		edge = new Thread(this); edge.start();
		try {
			XMLParser node = new XMLParser("kdv_node_unload.xml");
			nodes = node.getNodes();
			edge.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/* How to use
	 Interval<Double> xAxis = new Interval<Double>(new Double(0.0), new Double(70020050.98297));
     Interval<Double> yAxis = new Interval<Double>(new Double(0.0), new Double(70500527.51786));
     Interval2D<Double> rect = new Interval2D<Double>(xAxis, yAxis);
     queryEdges(rect);
	 */
	
	public LinkedList<Edge> queryEdges(Interval2D<Double> rect, int type){
		long t = System.currentTimeMillis();
		LinkedList<Edge> edgesToDraw = new LinkedList<Edge>();
		for(Edge e : edges.get(type)){
			if(rect.contains(e.getXFrom(), e.getYFrom())) edgesToDraw.add(e);
			if(rect.contains(e.getXTo(), e.getYTo())) edgesToDraw.add(e);
		}
		System.out.println(System.currentTimeMillis() - t);
		return edgesToDraw;
	}
	

	public static void main(String[] args){
			new Query();
	}
}
