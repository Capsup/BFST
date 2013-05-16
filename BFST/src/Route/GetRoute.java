package Route;

import DataProcessing.Query;
import Graph.Edge;
import MapDraw.MapDraw;


public class GetRoute extends Thread {
	private int from, to;
	private static int lastFrom = -1;
	private static int lastTo = -1;
	private static int lastPathSetting = -1;
	private static int lastTransportSetting = -1;
	private static int lastFerrySetting = -1;
	
	/**
	 * Computes the shortest route from node 'from' to node 'to' in the current road graph. The data is pushed back to the current instance of MapDraw
	 * @param from - the from vertex
	 * @param to - the to vertex
	 */
	public GetRoute(int from, int to){
		this.from = from;
		this.to = to;
		
		if(hasNewPath() || hasNewTransport())
			this.start();
	}
	
	/**
	 * runs the shortest path algorithm and pushed the path, if any, back to the current instance of MapDraw
	 */
	public void run()
	{
		if(hasNewPath())
			GUI.PathInformation.getInstance().setLength(-1);
		
		GUI.PathInformation.getInstance().setTravelTime(-1);
		GUI.PathInformation.getInstance().update();
		
		Dijkstra d = new Dijkstra(Query.getInstance().getGraph(), from);
		
		//Iterable<Edge> edges = d.pathTo(to);
		Edge[] edges = d.pathTo(to);
		
		if(hasNewPath())
			MapDraw.getInstance().setRoute(edges);
		
		if(hasNewPath())
			GUI.PathInformation.getInstance().setLength(d.getCurrentPathLength());
		
		GUI.PathInformation.getInstance().setTravelTime(d.getTravelTime());
		GUI.PathInformation.getInstance().update();
		
		lastFrom = from;
		lastTo = to;
		lastPathSetting = Settings.routeProfile();
		lastTransportSetting = Settings.meansOfTransport();
		lastFerrySetting = Settings.ferryAllowed();
	}
	
	/**
	 * Checks if it route last calculated is the same as now
	 * @return boolean
	 */
	public boolean hasNewPath()
	{
		return from != lastFrom || to != lastTo || Settings.routeProfile() != lastPathSetting || Settings.meansOfTransport() != lastTransportSetting || Settings.ferryAllowed() != lastFerrySetting;
	}
	
	/**
	 * Checks if the means of transportation have changed
	 * @return boolean
	 */
	public boolean hasNewTransport()
	{
		return Settings.meansOfTransport() != lastTransportSetting;
	}
}