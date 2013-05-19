package MapDraw;

import java.awt.Point;
import java.util.Observable;

/**
 * A singleton that holds the data about the current and possible zoom levels of the map
 */
public class ZoomLevel extends Observable
{
	private static ZoomLevel instance;		//The singleton variable
	private double[] zoomLevels;			//Array of possible zoom levels. The zoom level indicates the percentage of the map to be shown
	private int currentZoomLevelIndex = 0;	//The index of the current zoom level
	private Point lastMousePosition;		//The last notified mouse position, used when animating a zoom in and zoom out
	
	/**
	 * ZoomLevel is a singleton that allows us to set and access data about the zoom level of the map
	 */
	private ZoomLevel()
	{
		//We initialize the zoom level array with the number of zoom levels
		zoomLevels = new double[20];
		
		//We apply a start zoom level of 1.5 (since we want to zoom out a bit more than just the map)
		//Each zoom level decreases the amount of map we can see by 25%
		for(int i = 0; i < zoomLevels.length; i++)
			zoomLevels[i] = 1.5 * Math.pow(0.75f, i);
	}
	
	public static ZoomLevel getInstance()
	{
		if(instance == null)
			instance = new ZoomLevel();
		
		return instance;
	}
	
	/**
	 * @return the current zoom level
	 */
	public double getZoomLevel()
	{
		return zoomLevels[currentZoomLevelIndex];
	}
	
	/**
	 * Gets the zoom level at index
	 * @param index
	 * @return the zoom level at index
	 */
	public double getZoomLevel(int index)
	{
		//If the index breaches the boundaries of the array we return the closest zoom level
		if(index < 0)
			return zoomLevels[0];
		else if(index >= zoomLevels.length)
			return zoomLevels[zoomLevels.length-1];
		else
			return zoomLevels[index];
	}
	
	/**
	 * @return The current zoom index
	 */
	public int getZoomIndex()
	{
		return currentZoomLevelIndex;
	}
	
	/**
	 * Sets the current zoom level and notifies all observers
	 * @param index The index that we wish to set the zoom level to
	 */
	public void setZoomLevel(int i)
	{
		currentZoomLevelIndex = i;
		
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Increments the zoom level index with 1 - We return whether or not we successfully incremented the zoom
	 * @return
	 */
	public boolean zoomIn()
	{
		if(currentZoomLevelIndex+1 < zoomLevels.length)
		{
			currentZoomLevelIndex++;

			setChanged();
			notifyObservers();
			
			return true;
		}
		else
			return false;
	}
	
	/**
	 * Decrements the zoom level index with -1 - We return whether or not we successfully incremented the zoom
	 * @return
	 */
	public boolean zoomOut()
	{
		if(currentZoomLevelIndex-1 >= 0)
		{
			currentZoomLevelIndex--;
			setChanged();
			notifyObservers();
			
			return true;
		}
		else 
			return false;
	}
	
	/**
	 * Finds the zoom level index that is closest to representing the given zoom level
	 * @param zoomLevel The zoom level we wish to find the index for
	 * @return	The index of the closest zoom level
	 */
	public int findIndex(double zoomLevel)
	{
		//If the zoom level breaks the boundary of which we can zoom out we return the highest zoom level index
		if(zoomLevel > zoomLevels[0])
			return 0;
			
		for(int i=0; i<zoomLevels.length; i++)
		{
			//We iterate through all zoom levels
			
			//If we reach the end of the zoom level array we return the last index
			if(i == zoomLevels.length-1)
				return i;
			
			//If the zoom level give is greater than the zoom level we find we know that it must be the previous zoom level that is the closets
			if(zoomLevel > zoomLevels[i])
				return i-1;
		}
		
		//if we dont find a search hit during iteration we return the first zoom level
		return 0;
	}
	
	/**
	 * @return The amount of zoom levels there is
	 */
	public int getZoomLevelAmount()
	{
		return zoomLevels.length;
	}
	
	/**
	 * Assigns the reference mouse position that is used during zoom animation
	 * @param point
	 */
	public void setMousePosition(Point point)
	{
		lastMousePosition = point;
	}
	
	/**
	 * Returns the last registered reference mouse position. If it is null, we initialize it.
	 * @return
	 */
	public Point getLastMousePosition()
	{
		if(lastMousePosition == null)
			lastMousePosition = new Point();
		
		return lastMousePosition;
	}
}
