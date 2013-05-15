package DataProcessing;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import Graph.Edge;
import Graph.Graph;
import QuadTree.Interval;
import QuadTree.Interval2D;
import QuadTree.QuadTree;
import XMLParser.XMLParser;

public class Query{

	private static Query instance; //For the singleton implementation
	private Graph graph; //The roadnet graph
	
	/**
	 * The graph of the road net
	 * @return Graph The graph
	 */
	public Graph getGraph(){ return graph; }

	/**
	 * Returns the active Query
	 * @return Query
	 */
	public static Query getInstance(){
		if(instance == null) instance = new Query();
		return instance;

	}
	
	
	private Query(){
		double time = System.currentTimeMillis();
		LinkedList<Thread> threads = new LinkedList<Thread>();

		try {
			for(int i = 1;; i++){
				Thread t = new Thread(new XMLParser("data/kdv_unload_" + i + ".xml"));
				t.start();
				threads.add(t);
			}
		} catch (FileNotFoundException e) {}
		try { for(Thread t : threads) t.join();	} catch (InterruptedException e) { e.printStackTrace(); }

		graph = new Graph(675903, XMLParser.getEdgeList());

		Thread t = new Thread(){
			public void run(){
				for(int i = 0; i < 100; i++){
					quadTrees.add(new QuadTree<Double, Edge>());
				}

				for(int i = 0; i < XMLParser.getEdgeList().size(); i++){
					List<Edge> l = XMLParser.getEdgeList().get(i);
					if(l.size() == 0) continue;
					QuadTree<Double, Edge> qt = quadTrees.get(i);
					for(Edge e = l.remove(0);l.size() > 0; e = l.remove(0)){
						qt.insert(e.getXTo(), e.getYTo(), e);
					}
				}
			}
		};

		t.start();
		try { t.join();	} catch (InterruptedException e) { }
	}
	
	
	@SuppressWarnings("unchecked") 
	private static List<Edge>[] lastQuery = (List<Edge>[]) new List[100]; //For old queries
	
	@SuppressWarnings("unchecked") 
	private static Interval2D<Double>[] lastInterval = (Interval2D<Double>[]) new Interval2D[100]; //For old rects

		private ArrayList<QuadTree<Double, Edge>> quadTrees = new ArrayList<QuadTree<Double, Edge>>();

	static{ 
		Interval2D<Double> interval = new Interval2D<Double>(
				new Interval<Double>(new Double(0.0), new Double(70020050.98297)), 
				new Interval<Double>(new Double(0.0), new Double(70500527.51786))
				);
		for(int i = 0; i < lastQuery.length; i++) lastQuery[i] = new LinkedList<Edge>();
		for(int i = 0; i < lastInterval.length; i++) lastInterval[i] = interval;
	}


	/**
	 * Fetches a List of edges in the specified rectangle
	 * @param rect The rectangle to query data in
	 * @param type The road type the should be queried 
	 * @return The list of edges
	 */
	public List<Edge> queryEdges(Interval2D<Double> rect, int type){

		if(lastInterval[type].compareTo(rect) != 0){ //If this query have been made since last rect change, the old query will be return (So a new dosn't have to be made) 
			lastInterval[type] = rect; //Saves the last rect
			List<Edge> edgesToDraw = quadTrees.get(type).query2D(rect); //Fetches the data from the QuadTree
			lastQuery[type] = edgesToDraw; //Saves the query for later use
		}
		return lastQuery[type]; //Returns data
	}
}
