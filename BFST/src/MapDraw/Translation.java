package MapDraw;

import java.awt.geom.Point2D;

/**
 * A singleton that holds information about the translation of the map
 */
public class Translation 
{
	private static Translation instance;		//The singleton variable
	private Point2D.Double translation;			//The current translation of the map
	private Point2D.Double targetTranslation;	//The target translation
	private Point2D.Double startTranslation;	//The translation we started from when we need to animate a translation
	
	/**
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
	
	/**
	 * Get the current translation of the map
	 * @return The current translation of the map
	 */
	public Point2D.Double getTranslation()
	{
		//If the translation is null we initialize it before returning
		if( translation == null )
			translation = new Point2D.Double( 0, 0 );
		
		return translation;
	}
	
	/**
	 * Get the target translation of the map
	 * @return The target translation of the map
	 */
	public Point2D.Double getTargetTranslation()
	{
		//If the translation is null we initialize it before returning
		if( targetTranslation == null )
			targetTranslation = getTranslation();
		
		return targetTranslation;
	}
	
	/**
	 * Change the translation by dx and dy respectively. Use this method when it is done manually
	 * @param dx
	 * @param dy
	 */
	public void manualTranslate(double dx, double dy)
	{
		//We set the target translation to the translation we wish to move to. This way the map does not animate somewhere else once we dont manually move anymore
		targetTranslation.x = translation.x+dx;
		targetTranslation.y = translation.y+dy;
		
		//We then call the translate method that actually translates the map
		translate(dx, dy);
	}
	
	/**
	 * Change the translation of the map
	 * @param dx
	 * @param dy
	 */
	public void translate(double dx, double dy)
	{
		//If the translation is null we initiate it with the current dx dy
		if( translation == null )
			translation = new Point2D.Double( dx, dy );
		else
		{
			//Update the translation offset, so we can apply it to the stuff we draw later.
			translation = new Point2D.Double( translation.getX() + dx, translation.getY() + dy );
		}
	}

	/**
	 * Set the translation of the map directly
	 * @param x
	 * @param y
	 */
	public void setTranslation(double x, double y)
	{
		//We set the translation of the map directly
		targetTranslation = new Point2D.Double(x, y);
		translation = new Point2D.Double(x, y);	
	}
	
	/**
	 * Defines the target translation of the map.
	 * @param x
	 * @param y
	 */
	public void goToTranslation(double x, double y)
	{
		//We nullify the relative mouse position that the zoom shall use. This is done to prevent struggling in map animation
		ZoomLevel.getInstance().setMousePosition(null);
		
		//We set the start translation of the map. This is used to calculate the animation speed
		startTranslation = getTranslation();
		
		//We set the translation of the map directly
		targetTranslation = new Point2D.Double(x, y);	
	}
	
	/**
	 * @return The translation that we had when we started animating
	 */
	public Point2D.Double getStartTranslation()
	{
		if(startTranslation == null)
			startTranslation = new Point2D.Double();
			
		return startTranslation;
	}
}
