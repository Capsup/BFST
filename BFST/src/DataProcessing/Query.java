package DataProcessing;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import Graph.Edge;
import Graph.Graph;
import XMLParser.XMLParser;

public class Query{
	private ArrayList<List<Edge>> edges  =  XMLParser.getEdgeList();;
	private Graph graph;
	@SuppressWarnings("unchecked") private static List<Edge>[] lastQuery = (List<Edge>[]) new List[100];
	@SuppressWarnings("unchecked") private static Interval2D<Double>[] lastInterval = (Interval2D<Double>[]) new Interval2D[100];

	static{
		Interval2D<Double> interval = new Interval2D<Double>(
				new Interval<Double>(new Double(0.0), new Double(70020050.98297)), 
				new Interval<Double>(new Double(0.0), new Double(70500527.51786))
				);

		for(int i = 0; i < lastQuery.length; i++) lastQuery[i] = new LinkedList<Edge>();
		for(int i = 0; i < lastInterval.length; i++) lastInterval[i] = interval;
	}


	public Graph getGraph(){ return graph; }

	public Query(){
		LinkedList<Thread> threads = new LinkedList<Thread>();

		try {
			for(int i = 0;; i++){
				Thread t = new Thread(new XMLParser("kdv_unload_" + (i+1) + ".xml"));
				t.start();
				threads.add(t);
			}
		} catch (FileNotFoundException e) { }
		try { for(Thread t : threads) t.join();	} catch (InterruptedException e) { e.printStackTrace(); }

		graph = new Graph(675903, edges);
	}


	public List<Edge> queryEdges(Interval2D<Double> rect, int type){
		if(lastInterval[type].compareTo(rect) != 0){
			lastInterval[type] = rect;
			List<Edge> edgesToDraw = new LinkedList<Edge>();
			for(Edge e : edges.get(type)){
				if(e.contains(rect)) edgesToDraw.add(e);
			}
			lastQuery[type] = edgesToDraw;
			return edgesToDraw;
		}
		return lastQuery[type];
	}
}
