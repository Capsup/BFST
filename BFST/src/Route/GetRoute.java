package Route;

import DataProcessing.Query;
import Graph.Edge;
import MapDraw.MapDraw;


public class GetRoute extends Thread {
	private int from, to;
	private MapDraw map;
	private Query q;
	
	
	public void run(){
	Dijkstra d = new Dijkstra(q.getGraph(), from);
	map.setRoute(d.pathTo(to));
		
	}
	
	public GetRoute(MapDraw map,Query q, int from, int to){
		this.q = q;
		this.map = map;
		this.from = from;
		this.to = to;
	}
}