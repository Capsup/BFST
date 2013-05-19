package GUI;

import java.beans.Visibility;
import java.util.Observable;

/**
 * An observable singleton that contains all the information of the current path being processed.
 * @author Jonas Kastberg
 */
public class PathInformation extends Observable
{
	//instance variable, needed for a singleton
	private static PathInformation instance;
	
	//We contain the visibility in this singleton so it is always accessible
	private boolean isVisible;
	
	//Status variables that contains the different values of the current search
	private String pathFrom;
	private String pathTo;
	private double length;
	private double travelTime;
	
	/**
	 * Initialize the object
	 */
	private PathInformation()
	{
		instance = this;
	}
	
	/**
	 * if the instance variable is null, the method initializes the class
	 * @return returns the instance variable of the singleton
	 */
	public static PathInformation getInstance()
	{
		if(instance == null)
			new PathInformation();
			
		return instance;
	}

	/**
	 * Sets the visiblity variable. This variable is direct correlation with the InfoPanel's visibility
	 * @param flick
	 */
	public void setVisible(boolean flick)
	{
		//We set the visibility and update our InfoPanel observer
		isVisible = flick;
		update();
	}
	
	/**
	 * Set the departure and destination addresses
	 * @param from: the departure address
	 * @param to: the destination address 
	 */
	public void setFromAndTo(String from, String to)
	{
		pathFrom = from;
		pathTo = to;
	}
	
	/**
	 * set the length of the current path
	 * @param length
	 */
	public void setLength(double length)
	{
		this.length = length;
	}
	
	/**
	 * set the travel time of the current path
	 * @param travelTime
	 */
	public void setTravelTime(double travelTime)
	{
		this.travelTime = travelTime;
	}
	
	/**
	 * @return the visibility variable
	 */
	public boolean getVisible()
	{
		return isVisible;
	}
	
	/**
	 * @return the departure address
	 */
	public String getFrom()
	{
		return pathFrom;
	}
	
	/**
	 * @return the destination address
	 */
	public String getTo()
	{
		return pathTo;
	}
	
	/**
	 * Get the length of the path formatted to either m or km based on the length of the path
	 * @return get the length as a string, if the length is an invalid output (<0) "Calculating.." is returned
	 */
	public String getLength()
	{
		if(length >= 0)
		{
			double currLength = length;
			
			//If the length is above 1000 we recalculate it into km
			if(currLength >= 1000)
			{
				//We divide by 100, then round and then by the last 10, in order to keep the last digit
				currLength /= 100;
				currLength = Math.round(currLength);
				currLength /= 10;
				
				//We then return the length as well as the "km"
				return currLength+"km";
			}
			else
				return Math.round(currLength)+"m"; 	//Otherwise we just return the length as meters
		}
		else if(length == -1)
			return "Calculating...";				//If the length is below zero we return "Calculating...", this is used in a multi-thread perspective
		else
			return "No route found...";
	}
	
	/**
	 * Get the travel time as a string formatted to match the time (hours, minutes, seconds)
	 * @return get the travel time as a string, if the travel time is an invalid output (<0) "Calculating.." is returned
	 */
	public String getTravelTime()
	{
		return buildTravelTimeString(travelTime);
	}
	
	/**
	 * Builds the travel time string
	 * @return get the travel time as a string, if the travel time is an invalid output (<0) "Calculating.." is returned
	 */
	private String buildTravelTimeString(double travelTime)
	{
		if(travelTime >= 0)
		{
			double time = travelTime;
			
			//We first allocate the hours by flooring the value.
			int hours = (int)Math.floor(time);
			
			//We then subtract the hours from the time.
			time -= hours;
			
			//Then we allocate the minutes. We know that the remaining decimals can be multiplied by 60 to get minutes
			int minutes = (int)Math.floor(60*time);
			
			StringBuilder stringBuilder = new StringBuilder();
			
			if(hours != 0)
			{
				//In case we it took more than an hour we append it to the string
				
				stringBuilder.append(hours);
				
				//Based on the tense of the hours we add the correct grammer
				if(hours > 1)
					stringBuilder.append(" hours");
				else 
					stringBuilder.append(" hour");
			}
			
			if(minutes != 0)
			{
				//We do the same procedure with minutes
				
				//If we have hours we need to make a space between the two strings
				if(hours != 0)
					stringBuilder.append(" ");
					
				stringBuilder.append(minutes);
				
				if(minutes > 1)
					stringBuilder.append(" minutes");
				else
					stringBuilder.append(" minute");
			}
			
			if(stringBuilder.toString().equals(""))
			{
				//If the string is still empty, it means that the path took less than a minute
				//and therefore we append the time in seconds instead
				
				int seconds = (int)Math.round(3600*time);
				
				stringBuilder.append(seconds);
				
				if(seconds > 1)
					stringBuilder.append(" seconds");
				else
					stringBuilder.append(" second");
			}
			
			//We return the final string
			return stringBuilder.toString();
		
		}
		else if(travelTime == -1)
			return "Calculating...";						//If the length is below zero we return "Calculating...", this is used in a multi-thread perspective
		else
			return "No route found...";
	}
	
	//This method is called whenever we need to notify our observers that we have changed our settings. In this case, our visiblity.
	public void update()
	{
		setChanged();
		notifyObservers();
	}
}
