package Graph;

import java.util.ArrayList;
import java.util.HashMap;

public class Edge// implements Comparable<Edge>
{
	private static int id_count = 0;
	private final int id;
	private final int v;
	private final int w;
	private final double weight;
	
	private int typ;
	private double xTo, yTo, xFrom, yFrom;

	public Edge( int v, int w, double weight, ArrayList<String> s )
	{
		this.id = id_count++;
		this.v = v;
		this.w = w;
		this.weight = weight;
		init(s);
	}
	
	public Edge( int v, int w, double weight)
	{
		this.id = id_count++;
		this.v = v;
		this.w = w;
		this.weight = weight;
	}
	
	public int getId(){ return id; }
	public double weight(){ return weight; }
	public int either(){ return v; }
	public int other(int ve){ return ve == w ? v : w ; }
	
	protected void init(ArrayList<String> s){
		this.xFrom = Double.parseDouble(s.get(0));
		this.yFrom = Double.parseDouble(s.get(1));
		this.xTo = Double.parseDouble(s.get(3));
		this.yTo = Double.parseDouble(s.get(4));
		this.typ = Integer.parseInt(s.get(7));
	}


	public double getXTo(){	return xTo; }
	public double getYTo(){	return yTo; }
	public double getXFrom(){ return xFrom;	}
	public double getYFrom(){ return yFrom;	}
	public int getTyp()	{ return typ; }
}
