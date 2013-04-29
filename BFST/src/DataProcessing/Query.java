package DataProcessing;

import java.util.ArrayList;
import java.util.LinkedList;

import Dijkstra.Dijkstra;
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
			XMLParser edge = new XMLParser("kdv_unload.xml");
			edges = edge.getEdges();
			edge = null;
			nodes = new Graph(675903, edges);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public LinkedList<Edge> queryEdges(Interval2D<Double> rect, int type){
		System.out.println(lastInterval[type].compareTo(rect));
		if(lastInterval[type].compareTo(rect) != 0){
			lastInterval[type] = rect;
			long t = System.currentTimeMillis();
			LinkedList<Edge> edgesToDraw = new LinkedList<Edge>();
			for(Edge e : edges.get(type)){
				if(rect.contains(e.getXFrom(), e.getYFrom())) edgesToDraw.add(e);
				if(rect.contains(e.getXTo(), e.getYTo())) edgesToDraw.add(e);
			}
			//System.out.println(System.currentTimeMillis() - t);
			lastQuery[type] = edgesToDraw;
			return edgesToDraw;
		}
		return lastQuery[type];
	}

	public static void main(String[] agrs){
		Query q = new Query();
		System.out.println("Here we go!");
		Dijkstra d = new Dijkstra(q.getGraph(), 333570);
		int j = 0;
		for(int i = 0; i < q.getGraph().V(); i++)
			if(d.hasPathTo(i)) j++;
		System.out.println(j);

		
		
		BreadthFirstDirectedPaths bfs = new BreadthFirstDirectedPaths(q.getGraph(), 333570);
		j = 0;
		for(int i = 0; i < q.getGraph().V(); i++)
			if(bfs.hasPathTo(i)) j++;
		//System.out.println(j);
	}
}
