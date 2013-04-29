package DataProcessing;

import java.util.ArrayList;
import java.util.LinkedList;

import Graph.Edge;
import Graph.Graph;
import Graph.Node;
import XMLParser.XMLParser;

public class Query{
	private ArrayList<LinkedList<Edge>> edges;
	private Graph nodes;
	private Thread edge;
	private static LinkedList<Edge> lastQuery;

	private static Interval2D<Double> lastInterval = new Interval2D<Double>(
			new Interval<Double>(new Double(0.0), new Double(70020050.98297)), 
			new Interval<Double>(new Double(0.0), new Double(70500527.51786))
			);

	public Graph getGraph(){ return nodes; }

	public Query(){
		try {

			//XMLParser node = new XMLParser("kdv_node_unload.xml");
			//	nodes = node.getNodes();
			//nodes = new Graph(675903);
			nodes = new Graph(1);
			
			XMLParser edge = new XMLParser("kdv_unload.xml");
			
			edges = edge.getEdges(nodes);

			/*for(int i = 0; i < 100; i++){
				for(Edge e : edges.get(i))
					nodes.addEdge(e);
			}*/



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
		if(!lastInterval.equals(rect)){
			lastInterval = rect;
			long t = System.currentTimeMillis();
			LinkedList<Edge> edgesToDraw = new LinkedList<Edge>();
			for(Edge e : edges.get(type)){
				if(rect.contains(e.getXFrom(), e.getYFrom())) edgesToDraw.add(e);
				if(rect.contains(e.getXTo(), e.getYTo())) edgesToDraw.add(e);
			}
			System.out.println(System.currentTimeMillis() - t);
			lastQuery = edgesToDraw;
			return edgesToDraw;
		}
		return lastQuery;
	}

	public static void main(String[] agrs){
		Query q = new Query();
		System.out.println("Here we go!");
		//Dijkstra d = new Dijkstra(q.getGraph(), 333570);
		BreadthFirstDirectedPaths bfs = new BreadthFirstDirectedPaths(q.getGraph(), 333570);
		int j = 0;
		for(int i = 0; i < q.getGraph().V(); i++)
			if(bfs.hasPathTo(i)) j++;
		System.out.println(j);
	}
}
