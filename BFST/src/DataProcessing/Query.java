package DataProcessing;

import java.util.ArrayList;
import java.util.LinkedList;
import Graph.Edge;
import Graph.Graph;
import XMLParser.XMLParser;

public class Query{
	private ArrayList<LinkedList<Edge>> edges;
	private Graph nodes;
	private static LinkedList<Edge>[] lastQuery = (LinkedList<Edge>[]) new LinkedList[100];
	private static Interval2D<Double>[] lastInterval = (Interval2D<Double>[]) new Interval2D[100];

	static{
		Interval2D<Double> interval = new Interval2D<Double>(
				new Interval<Double>(new Double(0.0), new Double(70020050.98297)), 
				new Interval<Double>(new Double(0.0), new Double(70500527.51786))
				);

		for(int i = 0; i < lastQuery.length; i++)
			lastQuery[i] = new LinkedList<Edge>();

		for(int i = 0; i < lastInterval.length; i++)
			lastInterval[i] = interval;
	}


	public Graph getGraph(){ return nodes; }

	public Query(){
		try {
			Thread t = null;
			for(int i = 1; i < 2; i++){
				t = new Thread(new XMLParser("kdv_unload_" + i +".xml"));
				t.start();
				t.join();
			}

			edges = XMLParser.getEdgeList();
			nodes = new Graph(675903, edges);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public LinkedList<Edge> queryEdges(Interval2D<Double> rect, int type){
		if(lastInterval[type].compareTo(rect) != 0){
			lastInterval[type] = rect;
			LinkedList<Edge> edgesToDraw = new LinkedList<Edge>();
			for(Edge e : edges.get(type)){
				if(e.contains(rect)) edgesToDraw.add(e);
				//if(rect.contains(e.getXFrom(), e.getYFrom()) && rect.contains(e.getXTo(), e.getYTo())) edgesToDraw.add(e);
			}
			lastQuery[type] = edgesToDraw;
			return edgesToDraw;
		}
		return lastQuery[type];
	}
}
