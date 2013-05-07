package DataProcessing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import Graph.Edge;
import Graph.Graph;
import XMLParser.XMLParser;

public class Query{
	private ArrayList<List<Edge>> edges;
	private Graph nodes;
	private static List<Edge>[] lastQuery = (List<Edge>[]) new List[100];
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
			Thread[] t = new Thread[41];
			for(int i = 0; i < 41; i++){
				t[i] = new Thread(new XMLParser("kdv_unload_" + (i+1) + ".xml"));
				t[i].start();
				//t[i].join();
			}
			
			for(Thread th : t)
				th.join();
			
			edges = XMLParser.getEdgeList();
			nodes = new Graph(675903, edges);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public List<Edge> queryEdges(Interval2D<Double> rect, int type){
		if(lastInterval[type].compareTo(rect) != 0){
			lastInterval[type] = rect;
			List<Edge> edgesToDraw = new LinkedList<Edge>();
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
