package MapDraw;

import java.util.Observable;

public class ZoomLevel extends Observable
{
	private static ZoomLevel instance;
	private double[] zoomLevels;
	private int currentZoomLevelIndex = 0;
	
	/*
	 * ZoomLevel is a singleton that allows us to set and access data about the zoom level of the map
	 */
	private ZoomLevel()
	{
		zoomLevels = new double[20];
		
		for(int i = 0; i < zoomLevels.length; i++)
			zoomLevels[i] = 1.5 * Math.pow(0.75f, i);
	}
	
	public static ZoomLevel getInstance()
	{
		if(instance == null)
			instance = new ZoomLevel();
		
		return instance;
	}
	
	public double getZoomLevel()
	{
		return zoomLevels[currentZoomLevelIndex];
	}
	
	public double getZoomLevel(int index)
	{
		return zoomLevels[index];
	}
	
	public int getZoomIndex()
	{
		return currentZoomLevelIndex;
	}
	
	public void setZoomLevel(int i)
	{
		currentZoomLevelIndex = i;
		
		setChanged();
		notifyObservers();
	}
	
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
	
	public int findIndex(double zoomLevel)
	{
		for(int i=0; i<zoomLevels.length; i++)
		{
			if(i == zoomLevels.length-1)
				return i;
			
			if(zoomLevel > zoomLevels[i])
				return i-1;
		}
		return 0;
	}
}
