package GUI;

import java.beans.Visibility;
import java.util.Observable;

public class PathInformation extends Observable
{
	private static PathInformation instance;
	
	private String[] searchInformation;
	private boolean isVisible;
	
	private String pathFrom;
	private String pathTo;
	private double length;
	private double travelTime;
	
	private PathInformation()
	{
		instance = this;
	}
	
	public static PathInformation getInstance()
	{
		if(instance == null)
			new PathInformation();
			
		return instance;
	}
	/*
	public void setSearchInfo(String[] searchInfo)
	{
		searchInformation = searchInfo;
		
		setChanged();
		notifyObservers();
	}
	*/
	
	public String[] getSearchInfo()
	{
		return searchInformation;
	}
	
	public void setVisible(boolean flick)
	{
		isVisible = flick;
		
		update();
	}
	
	public void setFromAndTo(String from, String to)
	{
		pathFrom = from;
		pathTo = to;
	}
	
	public void setLength(double length)
	{
		this.length = length;
	}
	
	public void setTravelTime(double travelTime)
	{
		this.travelTime = travelTime;
	}
	
	public boolean getVisible()
	{
		return isVisible;
	}
	
	public String getFrom()
	{
		return pathFrom;
	}
	
	public String getTo()
	{
		return pathTo;
	}
	
	public String getLength()
	{
		//Make code so length is rewritten into appropriate numbers (e.g. m or km)
		
		if(length >= 0)
		{
			double currLength = length;
			
			if(currLength > 1000)
			{
				//Recalculate the length into km, while preserving the last digit.
				currLength /= 100;
				currLength = Math.round(currLength);
				currLength /= 10;
				
				return currLength+"km";
			}
			else
				return Math.round(currLength)+"m";
		}
		else
			return "Calculating...";
	}
	
	public String getTravelTime()
	{
		if(travelTime >= 0)
		{
			//double lengthInKM = length/1000;
			
			//double kmPerHour = 100;
			
			//double time = lengthInKM/kmPerHour;
			
			double time = travelTime*1.15;
			
			int hours = (int)Math.floor(time);
			
			time -= Math.floor(time);
			
			int minutes = (int)Math.round(60*time);
			
			StringBuilder stringBuilder = new StringBuilder();
			//String returnString = "";
			
			if(hours != 0)
			{
				stringBuilder.append(hours);
				
				if(hours > 1)
					stringBuilder.append(" hours ");
				else 
					stringBuilder.append(" hour ");
			}
			
			if(minutes != 0)
			{
				stringBuilder.append(minutes);
				
				if(minutes > 1)
					stringBuilder.append(" minutes");
				else
					stringBuilder.append(" minute");
			}
			
			if(stringBuilder.toString().equalsIgnoreCase(""))
			{
				int seconds = (int)Math.round(3600*time);
				
				stringBuilder.append(seconds);
				
				if(seconds > 1)
					stringBuilder.append(" seconds");
				else
					stringBuilder.append(" second");
			}
			
			//stringBuilder.append(" (100 km/h)");
			
			return stringBuilder.toString();
		
		}
		else
			return "Calculating...";
	}
	
	public void update()
	{
		setChanged();
		notifyObservers();
	}
}
