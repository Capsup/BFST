package Graph;

import java.util.ArrayList;
import java.util.LinkedList;

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

   /**
     * Create a edge-weighted digraph with V vertices and E edges.
     */
    public Graph(int V, int E) {
        this(V);
        if (E < 0) throw new IllegalArgumentException("Number of edges in a Digraph must be nonnegative");
        for (int i = 0; i < E; i++) {
            int v = (int) (Math.random() * V);
            int w = (int) (Math.random() * V);
            double weight = Math.round(100 * Math.random()) / 100.0;
            Edge e = new Edge(v, w, weight);
            addEdge(e);
        }
    }
    
    public Graph(int V, ArrayList<LinkedList<Edge>> list){
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
    public void addEdge(Edge e) {
        int v = e.either();
        int w = e.other(v);
        adj[v].add(e);
        adj[w].add(e);
        E++;
    }


   /**
     * Return the edges incident from vertex v as an Iterable.
     * To iterate over the edges incident from vertex v in digraph G, use foreach notation:
     * <tt>for (DirectedEdge e : G.adj(v))</tt>.
     */
    public Iterable<Edge> adj(int v) {
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