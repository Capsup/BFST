package DataProcessing;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.xml.stream.XMLStreamException;

import Graph.Edge;
import Graph.Graph;
import Graph.Node;
import QuadTree.Interval;
import QuadTree.Interval2D;
import QuadTree.QuadTree;
import XMLParser.XMLParser;

/*
 * This class handles map data, and  you can query the map data. 
 */
public class Query{

	private static Query instance; //For the singleton implementation
	private Graph graph; //The road graph

	/**
	 * Returns the active Query
	 * @return Query
	 */
	public static Query getInstance(){
		if(instance == null) instance = new Query();
		return instance;

	}

	/**
	 * Private constructor to initialize all the road data (QuadTree and Graph)
	 */
	private Query(){
		getData();
		final ArrayList<List<Edge>> list = XMLParser.getEdgeList();
		graph = new Graph(list);
		makeQuadTree(list);
	}
	
	/**
	 * The graph of the road net
	 * @return Graph The graph
	 */
	public Graph getGraph(){ return graph; }
	
	/**
	 * This fields holds a reference to the QuadTrees. There is a QuadTree for every type of roads
	 */
	private ArrayList<QuadTree<Double, Edge>> quadTrees = new ArrayList<QuadTree<Double, Edge>>();
	
	/**
	 * Constructs the QuadTree
	 * @param list - list of list of edges to construct from
	 */
	private void makeQuadTree(final List<List<Edge>> list){
		Thread t = new Thread(){
			public void run(){
				for(int i = 0; i < list.size(); i++){
					List<Edge> l = list.get(i);
					quadTrees.add(new QuadTree<Double, Edge>());
					if(l.size() == 0) continue;
					QuadTree<Double, Edge> qt = quadTrees.get(i);
					
					//Removes the reference after use. This frees up memery needed to make the tree
					for(Edge e = l.remove(0);l.size() > 0; e = l.remove(0)){ 
						qt.insert(e.getXTo(), e.getYTo(), e);
					}
				}
			}
		};

		t.start(); //Starts the thread
		try { t.join();	} catch (InterruptedException e) { } //Waits for the thread to finish
	}

	/**
	 * Calls the XMLParser - This is done with multithreading. 
	 */
	private void getData(){
		LinkedList<Thread> threads = new LinkedList<Thread>();
		try {
			new XMLParser("data/kdv_node_unload.xml").getNodes();
			for(int i = 1;; i++){
				Thread t = new Thread(new XMLParser("data/kdv_unload_" + i + ".xml"));
				t.start();
				threads.add(t);
			}
		} catch (FileNotFoundException | XMLStreamException e) {}
		try { for(Thread t : threads) t.join();	} catch (InterruptedException e) { e.printStackTrace(); }
		finally{
			Node.nullNodes();
		}
	}

	/**
	 * This field holds previous queries. This is done so if nothing has changed, a query is not called
	 */
	@SuppressWarnings("unchecked") 
	private static List<Edge>[] lastQuery = (List<Edge>[]) new List[100]; //For old queries

	/**
	 * This field holds previous queries. This is done so if nothing has changed, a query is not called
	 */
	@SuppressWarnings("unchecked") 
	private static Interval2D<Double>[] lastInterval = (Interval2D<Double>[]) new Interval2D[100]; //For old rects

	
	/**
	 * This static constructor initialize lastInterval and lastQuery. This is to avoid null pointer exceptions. 
	 * Faster to do it here, than to check for null every time
	 */
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
	 * @param type - The road type the should be queried 
	 * @return The list of edges
	 */
	public List<Edge> queryEdges(int type){
		return queryEdges(new Interval2D<Double>(
				new Interval<Double>(new Double(0.0), new Double(700200050.98297)), 
				new Interval<Double>(new Double(0.0), new Double(705000527.51786))
				), type);
	}
	/**
	 * Fetches a List of edges in the specified rectangle
	 * @param rect - The rectangle to query data in
	 * @param type - The road type the should be queried 
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
