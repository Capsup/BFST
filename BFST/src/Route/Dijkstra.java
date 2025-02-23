package Route;

import java.util.ArrayList;

import Graph.Edge;
import Graph.Graph;

/**
 *  This is not entirely our class! We made modifications, but it originates from http://algs4.cs.princeton.edu/
 */

public class Dijkstra {
    private double[] distTo;          // distTo[v] = distance  of shortest s->v path
    private Edge[] edgeTo;    // edgeTo[v] = last edge on shortest s->v path
    private IndexMinPQ<Double> pq;    // priority queue of vertices

    private double currentPathLength = 0;
    private double currentPathTravelTime  = 0;
    
    /**
     * Initializes Dijkstra's algorithm so you can search from s to any other Vertex in Graph G and see if there is a path, and if so, find the shortest path
     * @param G - the graph
     * @param s - the vertex
     * @throws IllegalArgumentException
     */
    public Dijkstra(Graph G, int s) {
        for (Edge e : G.edges()) {
            if (e.weight() < 0)
                throw new IllegalArgumentException("edge " + e + " has negative weight");
        }

        distTo = new double[G.V()];
        edgeTo = new Edge[G.V()];
        for (int v = 0; v < G.V(); v++)
            distTo[v] = Double.POSITIVE_INFINITY;
        distTo[s] = 0.0;

        // relax vertices in order of distance from s
        pq = new IndexMinPQ<Double>(G.V());
        pq.insert(s, distTo[s]);
        while (!pq.isEmpty()) {
            int v = pq.delMin();
            for (Edge e : G.adj(v))
                relax(e, v);
        }

        // check optimality conditions
        assert check(G, s);
    }

    // relax edge e and update pq if changed
    private void relax(Edge e, int v) {
        int w = e.other(v);
        if (distTo[w] > distTo[v] + e.weight()) {
            distTo[w] = distTo[v] + e.weight();
            edgeTo[w] = e;
            if (pq.contains(w)) pq.decreaseKey(w, distTo[w]);
            else                pq.insert(w, distTo[w]);
        }
    }

    /**
     * length of shortest path from s to v
     * @param v - vertex
     * @return length
     */
    public double distTo(int v) {
        return distTo[v];
    }
    /**
     * Is there a path from s to v?
     * @param v - vertex
     * @return boolean
     */ 
    public boolean hasPathTo(int v) {
        return distTo[v] < Double.POSITIVE_INFINITY;
    }

    /**
     * shortest path from s to v as an Iterable, null if no such path
     * @param v
     * @return Iterable<Edge>
     */
    public Edge[] pathTo(int v) {
        if (!hasPathTo(v)) return null;
        ArrayList<Edge> path = new ArrayList<Edge>();
        
        currentPathLength = 0;
        currentPathTravelTime = 0;
        
        for (Edge e = edgeTo[v]; e != null; e = edgeTo[v = e.other(v)]) {
        	currentPathLength += e.getLength();
        	
        	if(Settings.meansOfTransport() == Settings.car)
	        	if(e.getSpeedLimit() != 0)
	        		currentPathTravelTime += (e.getLength()/1000)/e.getSpeedLimit();
	        	else
	        		currentPathTravelTime += (e.getLength()/1000)/50;
        	else if(Settings.meansOfTransport() == Settings.bike)
         		currentPathTravelTime += e.getTyp() != 80 ? (e.getLength()/1000)/18 : (e.getSpeedLimit() != 0 ? (e.getLength()/1000)/e.getSpeedLimit(): (e.getLength()/1000)/50);
        	
        	else 
        		currentPathTravelTime += e.getTyp() != 80 ? (e.getLength()/1000)/5 : (e.getSpeedLimit() != 0 ? (e.getLength()/1000)/e.getSpeedLimit(): (e.getLength()/1000)/50);
        	path.add(e);
        }
        
        Edge[] returnArray = new Edge[path.size()];
        path.toArray(returnArray);
        
        return returnArray;
    }

    /**
     * check optimality conditions:
     * (i) for all edges e:            distTo[e.to()] <= distTo[e.from()] + e.weight()
     * (ii) for all edge e on the SPT: distTo[e.to()] == distTo[e.from()] + e.weight()
     * @param G - the graph
     * @param s - the start vertex
     * @return
     */
    private boolean check(Graph G, int s) {

        // check that edge weights are nonnegative
        for (Edge e : G.edges()) {
            if (e.weight() < 0) {
                System.err.println("negative edge weight detected");
                return false;
            }
        }

        // check that distTo[v] and edgeTo[v] are consistent
        if (distTo[s] != 0.0 || edgeTo[s] != null) {
            System.err.println("distTo[s] and edgeTo[s] inconsistent");
            return false;
        }
        for (int v = 0; v < G.V(); v++) {
            if (v == s) continue;
            if (edgeTo[v] == null && distTo[v] != Double.POSITIVE_INFINITY) {
                System.err.println("distTo[] and edgeTo[] inconsistent");
                return false;
            }
        }

        // check that all edges e = v->w satisfy distTo[w] <= distTo[v] + e.weight()
        for (int v = 0; v < G.V(); v++) {
            for (Edge e : G.adj(v)) {
                int w = e.other(v);
                if (distTo[v] + e.weight() < distTo[w]) {
                    System.err.println("edge " + e + " not relaxed");
                    return false;
                }
            }
        }

        // check that all edges e = v->w on SPT satisfy distTo[w] == distTo[v] + e.weight()
        for (int w = 0; w < G.V(); w++) {
            if (edgeTo[w] == null) continue;
            Edge e = edgeTo[w];
            int v = e.either();
            if (w != e.other(v)) return false;
            if (distTo[v] + e.weight() != distTo[w]) {
                System.err.println("edge " + e + " on shortest path not tight");
                return false;
            }
        }
        return true;
    }
    
    /**
     * returns the length of the last path called from pathTo()
     * @return double
     */
    public double getCurrentPathLength()
    {
    	return currentPathLength;
    }
    
    /**
     * returns the travel time of the last path called from pathTo()
     * @return double
     */
    public double getTravelTime()
    {
    	return currentPathTravelTime;
    }
}