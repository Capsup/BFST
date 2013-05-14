package Route;

import DataProcessing.Query;
import Graph.Edge;
import MapDraw.MapDraw;


public class GetRoute extends Thread {
	private int from, to;
	private MapDraw map;
	private Query q;
	
	public GetRoute(MapDraw map,Query q, int from, int to){
		this.q = q;
		this.map = map;
		this.from = from;
		this.to = to;
	}
	
	public void run()
	{
		GUI.PathInformation.getInstance().setLength(-1);
		GUI.PathInformation.getInstance().update();

		Dijkstra d = new Dijkstra(q.getGraph(), from);
		
		Iterable<Edge> edges = d.pathTo(to);
		
		map.setRoute(edges);
		
		GUI.PathInformation.getInstance().setLength(d.getCurrentPathLength());
		GUI.PathInformation.getInstance().update();
	}
}