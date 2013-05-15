package Route;

import DataProcessing.Query;
import Graph.Edge;
import MapDraw.MapDraw;


public class GetRoute extends Thread {
	private int from, to;
	private MapDraw map;
	private Query q;
	
	public static int lastFrom = -1;
	public static int lastTo = -1;
	public static int lastPathSetting = -1;
	public static int lastTransportSetting = -1;
	
	public GetRoute(MapDraw map,Query q, int from, int to){
		this.q = q;
		this.map = map;
		this.from = from;
		this.to = to;
	}
	
	public void run()
	{
		if(hasNewPath())
			GUI.PathInformation.getInstance().setLength(-1);
		
		GUI.PathInformation.getInstance().setTravelTime(-1);
		GUI.PathInformation.getInstance().update();
		
		Dijkstra d = new Dijkstra(q.getGraph(), from);
		
		//Iterable<Edge> edges = d.pathTo(to);
		Edge[] edges = d.pathTo(to);
		
		if(hasNewPath())
			map.setRoute(edges);
		
		if(hasNewPath())
			GUI.PathInformation.getInstance().setLength(d.getCurrentPathLength());
		
		GUI.PathInformation.getInstance().setTravelTime(d.getTravelTime());
		GUI.PathInformation.getInstance().update();
		
		lastFrom = from;
		lastTo = to;
		lastPathSetting = Settings.routeProfile();
		lastTransportSetting = Settings.meansOfTransport();
	}
	
	public boolean hasNewPath()
	{
		return from != lastFrom || to != lastTo || Settings.routeProfile() != lastPathSetting;
	}
	
	public boolean hasNewTransport()
	{
		return Settings.meansOfTransport() != lastTransportSetting;
	}
}