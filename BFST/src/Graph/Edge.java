package Graph;

import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.lang.model.element.Element;

import DataProcessing.Interval;
import DataProcessing.Interval2D;

public class Edge implements Comparable<Edge>
{
	private static int id_count = 0;
	private final int id;
	private final int v;
	private final int w;
	private String roadName;
	
	private int typ, zip;//, speedLimit;
	private int toRight, fromRight, toLeft, fromLeft;
	private double xTo, yTo, xFrom, yFrom, driveTime, length;
	//private String roadName, oneWay;

	public Edge( int v, int w, ArrayList<String> s )
	{
		this.id = id_count++;
		this.v = v;
		this.w = w;
		init(s);
	}
	
	public Edge( int v, int w, double weight)
	{
		this.id = id_count++;
		this.v = v;
		this.w = w;
	}

	public int getId(){ return id; }
	public double weight(){
		
		if(Route.Settings.routeProfile() == Route.Settings.fastest_route)
			return driveTime;
		else
			return length; 
		}
	
	public int either(){ return v; }
	public int other(int ve){ return ve == w ? v : w ; }

	protected void init(ArrayList<String> s){
		this.xFrom = Double.parseDouble(s.get(0));
		this.yFrom = Double.parseDouble(s.get(1));
		this.xTo = Double.parseDouble(s.get(3));
		this.yTo = Double.parseDouble(s.get(4));
		this.length = Double.parseDouble(s.get(6));
		this.typ = Integer.parseInt(s.get(7));
		this.roadName = s.get(8);
		
		this.fromLeft = Integer.parseInt(s.get(9));
		this.toLeft = Integer.parseInt(s.get(10));
		this.fromRight = Integer.parseInt(s.get(11));
		this.toRight = Integer.parseInt(s.get(12));
		
		this.zip = Integer.parseInt(s.get(13));
		//this.speedLimit = Integer.parseInt(s.get(14));
		this.driveTime = Double.parseDouble(s.get(15));
		//this.oneWay = s.get(16);
	}

	public boolean contains(Interval2D<Double> i){
		return 
				((i.intervalX().low() <= xFrom) && (i.intervalX().high() >= xTo)) && 
				((i.intervalY().low() <= yFrom) && (i.intervalY().high() >= yTo));
	}

	public double getXTo(){	return xTo; }
	public double getYTo(){	return yTo; }
	public double getXFrom(){ return xFrom;	}
	public double getYFrom(){ return yFrom;	}
	public int getFromIndex(){ return v;	}
	public int getToIndex(){ return w;	}
	public int getTyp()	{ return typ; }
	public String getName(){ return roadName; }
	public int getZip(){ return zip; }

	
	@Override
	public int compareTo(Edge arg0) 
	{
		String string1 = getAddress().toLowerCase();
		String string2 = arg0.getAddress().toLowerCase();
		
		if(string1.equals(string2))
			return 0;
		
		int length = string1.length();
		int index = 0;
		
		while(index < length && index < string2.length())
		{
			if(string1.charAt(index) > string2.charAt(index))
			{
				return 1;
			}
			else if(string1.charAt(index) < string2.charAt(index))
			{
				return -1;
			}
			
			index++;
		}
		
		if(string1.length() > string2.length())
			return 1;
		else if(string2.length() > string1.length())
			return -1;
		
		return 0;
	}
	
	
	public String getAddress()
	{
		if(getZip() != 0)
			return (getName()+", "+getZip());
		else
			return getName();
	}
}
