package Graph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Graph {
	private final int V;
	private int E;
	private LinkedList<Edge>[] adj;

	/**
	 * Create an empty edge-weighted digraph with V vertices.
	 */
	public Graph(int V) {
		if (V < 0) throw new IllegalArgumentException("Number of vertices in a Digraph must be nonnegative");
		this.V = V;
		this.E = 0;
		adj = (LinkedList<Edge>[]) new LinkedList[V];
		for (int v = 0; v < V; v++)
			adj[v] = new LinkedList<Edge>();
	}

	public Graph(int V, ArrayList<List<Edge>> list) {
		this(V);
		for(Iterable<Edge> l : list)
			for(Edge e : l)
				addEdge(e);
	}


	/**
	 * Return the number of vertices in this digraph.
	 */
	public int V() {
		return V;
	}

	/**
	 * Return the number of edges in this digraph.
	 */
	public int E() {
		return E;
	}


	/**
	 * Add the directed edge e to this digraph.
	 */
	public void addEdge(Edge e)  {
		if(e.getOneWay().equals("'n'"));

		int v = e.either();
		int w = e.other(v);

		if(e.getOneWay().equals("''")){

			adj[v].add(e);
			adj[w].add(e);
			E++; E++;
		}

		if(e.getOneWay().equals("'tf'")){ adj[w].add(e); E++; }
		if(e.getOneWay().equals("'ft'")){ adj[v].add(e); E++; }
	}


	/**
	 * Return the edges incident from vertex v as an Iterable.
	 * To iterate over the edges incident from vertex v in digraph G, use foreach notation:
	 * <tt>for (DirectedEdge e : G.adj(v))</tt>.
	 * It returns edges according to route profile (Route.setting)
	 */
	public Iterable<Edge> adj(int v) {

		LinkedList<Edge> newAdj = new LinkedList<Edge>();

		if(Route.Settings.meansOfTransport() == Route.Settings.car){

			for(Edge e : adj[v])
				if(Route.Settings.ferryAllowed() == 0 ? (e.getTyp() != 8) : (e.getTyp() != 8 && (e.getTyp() != 80)))
					newAdj.add(e);

			return newAdj;
		}

		return adj[v];
	}

	/**
	 * Return all edges in this digraph as an Iterable.
	 * To iterate over the edges in the digraph, use foreach notation:
	 * <tt>for (DirectedEdge e : G.edges())</tt>.
	 */
	public Iterable<Edge> edges() {
		LinkedList<Edge> list = new LinkedList<Edge>();
		for (int v = 0; v < V; v++) {
			for (Edge e : adj(v)) {
				list.add(e);
			}
		}
		return list;
	} 

	/**
	 * Return number of edges incident from v.
	 */
	public int outdegree(int v) {
		return adj[v].size();
	}
}