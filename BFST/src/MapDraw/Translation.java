package MapDraw;

import java.awt.geom.Point2D;

public class Translation 
{
	private static Translation instance;
	private Point2D.Double translation;
	private Point2D.Double targetTranslation;
	
	private boolean auto;
	
	/*
	 * Translation is a singleton that allows us to set and access data about the translation of the map
	 */
	private Translation()
	{
	}
	
	public static Translation getInstance()
	{
		if(instance == null)
			instance = new Translation();
		
		return instance;
	}
	
	/*
	 * Get the current translation of the map
	 */
	public Point2D.Double getTranslation()
	{
		//If the translation is null we initialize it before returning
		if( translation == null )
			translation = new Point2D.Double( 0, 0 );
		
		return translation;
	}
	
	public Point2D.Double getTargetTranslation()
	{
		//If the translation is null we initialize it before returning
		if( targetTranslation == null )
			targetTranslation = getTranslation();
		
		return targetTranslation;
	}
	
	/*
	 * change the translation by dx and dy respectively
	 */
	public void manualTranslate(double dx, double dy)
	{
		targetTranslation.x = translation.x+dx;
		targetTranslation.y = translation.y+dy;
		
		translate(dx, dy);
	}
	
	/*
	 * change the translation by dx and dy respectively
	 */
	public void translate(double dx, double dy)
	{
		//If the translation is null we initiate it with the current dx dy
		if( translation == null )
			translation = new Point2D.Double( dx, dy );
		else
		{
			/*
			//double xMax = MapDraw.getInstance().getOffset().x+(MapDraw.getInstance().getMapWidth())*(ZoomLevel.getInstance().getZoomLevel()*(1.5-ZoomLevel.getInstance().getZoomLevel()));
			double xMax = MapDraw.getInstance().getOffset().x+MapDraw.getInstance().getMapWidth()/2;
			double rightBoundary = (translation.x+dx)+MapDraw.getInstance().getWidth()*ZoomLevel.getInstance().getZoomLevel();
			
			if(rightBoundary < xMax)
				translation.x += dx;
			
			if(translation.x + dx < xMax && translation.x + dx >  xMin)
				translation.x += dx;
			
			double xMin = MapDraw.getInstance().getOffset().x-(MapDraw.getInstance().getMapWidth())*(ZoomLevel.getInstance().getZoomLevel()*(1.5-ZoomLevel.getInstance().getZoomLevel()));
			
			
			double yMax = MapDraw.getInstance().getOffset().y+(MapDraw.getInstance().getMapHeight()/ZoomLevel.getInstance().getZoomLevel());
			double yMin = MapDraw.getInstance().getOffset().y-(MapDraw.getInstance().getMapHeight()/ZoomLevel.getInstance().getZoomLevel());
			
			if(translation.y + dy < yMax && translation.y + dy >  yMin)
				translation.y += dy;
			
			*/
			//Update the translation offset, so we can apply it to the stuff we draw later.
			translation = new Point2D.Double( translation.getX() + dx, translation.getY() + dy );
		}
	}

	/*
	 * Set the translation of the map directly
	 */
	public void setTranslation(double x, double y)
	{
		//We set the translation of the map directly
		targetTranslation = new Point2D.Double(x, y);
		translation = new Point2D.Double(x, y);	
	}
	
	/*
	 * Set the translation of the map directly
	 */
	public void goToTranslation(double x, double y)
	{
		//We set the translation of the map directly
		targetTranslation = new Point2D.Double(x, y);	
	}
}
