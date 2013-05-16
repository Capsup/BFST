package DataProcessing;

import java.awt.Point;

public class LiangBarsky
{
	double u1, u2;

	public boolean clip( Point p1, Point p2, int xMin, int xMax, int yMin, int yMax ) // Clips the line
	{
		int dx, dy;
		u1 = 0;
		u2 = 1;
		dx = p2.x - p1.x;
		dy = p2.y - p1.y;
		if( clipTest( -dx, p1.x - xMin ) )
			if( clipTest( dx, xMax - p1.x ) )
				if( clipTest( -dy, p1.y - yMin ) )
					if( clipTest( dy, yMax - p1.y ) )
					{
						return true;
					}
		return false;
	}

	private boolean clipTest( double p, double q )
	{
		double r = q / p;
		boolean okay = true;
		if( p < 0 )
		{
			if( r > u2 )
				okay = false;
			else if( r > u1 )
				u1 = r;
		}
		else if( p > 0 )
		{
			if( r < u1 )
				okay = false;
			else if( r < u2 )
				u2 = r;
		}
		else if( q < 0 )
			okay = false;
		return okay;
	}
}
