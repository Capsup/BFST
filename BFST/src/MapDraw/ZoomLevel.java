package MapDraw;
public class ZoomLevel 
{
	private static ZoomLevel instance;
	private double[] zoomLevels;
	private int currentZoomLevelIndex = 0;
	
	private ZoomLevel()
	{
		zoomLevels = new double[30];
		
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
	
	public int getZoomIndex()
	{
		return currentZoomLevelIndex;
	}
	
	public void setZoomLevel(int i)
	{
		currentZoomLevelIndex = i;
	}
	
	public void zoomIn()
	{
		if(currentZoomLevelIndex+1 < zoomLevels.length)
			currentZoomLevelIndex++;
	}
	
	public void zoomOut()
	{
		if(currentZoomLevelIndex-1 >= 0)
			currentZoomLevelIndex--;
	}
}
