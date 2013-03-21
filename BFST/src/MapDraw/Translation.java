package MapDraw;

import java.awt.geom.Point2D;

public class Translation 
{
	private static Translation instance;
	private Point2D.Double translation;
	
	private Translation()
	{
	}
	
	public static Translation getInstance()
	{
		if(instance == null)
			instance = new Translation();
		
		return instance;
	}
	
	public Point2D.Double getTranslation()
	{
		return translation;
	}
	
	public void translate(double dx, double dy)
	{
		if( translation == null )
			translation = new Point2D.Double( dx, dy );
		else
		{
			//Update the translation offset, so we can apply it to the stuff we draw later.
			translation = new Point2D.Double( translation.getX() + dx, translation.getY() + dy );
		}
	}
	
	public void setTranslation(double x, double y)
	{
		translation = new Point2D.Double(x, y);
	}
}
