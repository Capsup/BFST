package MapDraw;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLJPanel;
import javax.swing.JPanel;

import DataProcessing.Query;
import Graph.Edge;
import QuadTree.Interval;
import QuadTree.Interval2D;
import Route.GetRoute;

import com.jogamp.newt.awt.NewtCanvasAWT;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.FPSAnimator;

/**
 * A JPanel that implements OpenGL capabilities in order to draw the map.
 * The class implements listeners that is used to track input
 *
 */
public class MapDraw extends JPanel implements GLEventListener, MouseListener, MouseMotionListener, MouseWheelListener
{
	private static MapDraw instance;		//The singleton variable
	
	private long lastLeftMousePressTime;	//The last known time we pressed the left mouse button (used to detect double clicks)
	private long lastRightMousePressTime;	//The last known time we pressed the left mouse button (used to detect double clicks)
	
	private Query q = Query.getInstance();	//A reference to the Query singleton
	private int width;						//The width of the map (Note: this is not the width of the panel, but a reference point)
	private int height;						//The height of the map (Note: this is not the height of the panel, but a reference point)
	private Point curMousePos;				//The current position of the mouse
	private GraphicsPrefs gp;				//Graphics Preferences used to see the preferences of how we draw the roads
	
	protected double currentZoomLevel = 0;	//The current zoom level, this can be inbetween the preset zoom levels of the ZoomLevel singleton, since it is used in animation
	private double targetZoomLevel = 0;		//The target zoom level
	
	private Edge[] routeToDraw; 			//An array of the edges in the current route to draw
	private int routeFrom = -1;				//The index of the edge that the route goes from
	private int routeTo = -1;				//The index of the edge the route goes to
	
	private int currentRouteCutoff;			//The cutoff of the drawn route. This is used to animate the drawing of the road
	
	private long lastTime;					//The system time when the map updated its frame

	private Point2D.Double mapOffset;		//The offset of the map in our world space
	
	/**
	 * Sets up a JPanel with OpenGL capabilities, and draws the map
	 */
	private MapDraw()
	{
		//We set the layout to border layout
		setLayout(new BorderLayout());
		
		//Start translation at the place where the map is inside our 3D world.
		mapOffset = new Point2D.Double(-266, 5940);
		
		//Set the translation of the map to the offset so we start out by seeing our map on start up
		Translation.getInstance().setTranslation(mapOffset.x, mapOffset.y);
		
		//Initialize the mouse position
		curMousePos = new Point( 0, 0 );
		
		//Tell OpenGL that we're interested in the default profile, meaning we don't get any specific properties of the gl context.
		GLCapabilities glCapabilities = new GLCapabilities( GLProfile.getDefault() );

		//However, tell the context that we want it to be doublebuffered, so we don't get any on-screen flimmering.
		glCapabilities.setDoubleBuffered( true );
		//Tell the context that we want it to be hardware accelerated aswell.
		glCapabilities.setHardwareAccelerated( true );

		//Create the JPanel where our OpenGL stuff will be drawn on.
		GLJPanel panel = new GLJPanel( glCapabilities );
		
		//Add listeners...
		panel.addGLEventListener( this );
		panel.addMouseListener( this );
		panel.addMouseMotionListener( this );
		panel.addMouseWheelListener( this );
		
		add( panel, BorderLayout.CENTER);
		
		//Add an animator to our panel, which will start the rendering loop, repeatedly calling the display() function of our panel.
		//The FPS animator lets us put a limit to the refresh rate of the animator.
		FPSAnimator animator = new FPSAnimator(panel, Settings.fps);
		animator.start();

		//Sets the reference width and height of the map
		width = 800;
		height = 600;

		//Make sure that start zoom level is 0
		ZoomLevel.getInstance().setZoomLevel(0);
	}
	
	public static MapDraw getInstance()
	{
		if(instance == null)
			instance = new MapDraw();
		
		return instance;
	}
	
	/**
	 * The width factor is a factor that we use when checking what to draw on the map
	 * @return The width factor
	 */
	public double getWidthFactor()
	{
		return (width/height)*getHeightFactor();
	}
	
	/**
	 * The height factor is a factor that we use when checking what to draw on the map
	 * @return
	 */
	public double getHeightFactor()
	{
		return currentZoomLevel;
	}
	
	//OpenGL Events
	@Override
	public void init( GLAutoDrawable arg0 )
	{
		//Get a OpenGL v2 context.
		GL2 gl = arg0.getGL().getGL2();
		
		//initialize GraphicsPrefs
		gp = new GraphicsPrefs(gl);

		//Set the clear color of the color buffer to black, meaning we get a black screen whenever we clear the color buffer.
		gl.glClearColor( 255, 255, 255, 0 );
		//Set the current matrix to the projection matrix.
		gl.glMatrixMode( gl.GL_PROJECTION );
		//Load the identity matrix, meaning a matrix that does no transformation. 
		gl.glLoadIdentity();

		//Set the projection matrix to whatever width and height was specified of the window.
		gl.glOrtho( 0, width, 0, height, -1, 1 );

		//Set the current matrix to the modelview matrix.
		gl.glMatrixMode( gl.GL_MODELVIEW );
		
		//Load the identity matrix.
		gl.glLoadIdentity();

		//Set the viewport of the window.
		gl.glViewport( 0, 0, width, height );
	}
	
	@Override
	public void display( GLAutoDrawable arg0 )
	{
		long curTime = System.currentTimeMillis();
		
		//We check if there has gone long enough to update the map
		if( curTime - lastTime > ( 1000 / Settings.fps ) )
		{
			//Get an OpenGL v2 context.
			GL2 gl2 = arg0.getGL().getGL2();

			//Clear the color buffer, setting all pixel's color on the screen to the specified color.
			gl2.glClear( GL.GL_COLOR_BUFFER_BIT );
			
			//Load the identity matrix, effectively removing all transformations.
			gl2.glLoadIdentity();

			//Apply zoom
			applyZoom(gl2);

			//Add the panning translation.
			applyPanning(gl2);

			// Found at http://www.java-tips.org/other-api-tips/jogl/how-to-draw-anti-aliased-lines-in-jogl.html
			gl2.glEnable(GL.GL_LINE_SMOOTH);
			gl2.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
			gl2.glEnable( GL.GL_BLEND );
			gl2.glHint(GL.GL_LINE_SMOOTH_HINT, GL.GL_DONT_CARE); // GL.GL_NICEST);//
			
			//Draw the lines on the map
			drawLines(gl2);
			
			//Apply any zoom and pan animation needed.
			animateZoom();
			animatePan();
			
			//Set the last time the map was updated to now
			lastTime = curTime;
		}
	}
	
	/**
	 * Draws all edges to be drawn on the map
	 * @param gl2
	 */
	private void drawLines(GL2 gl2) {
		
		//Get all the types of roads that needs to be drawn for the current zoom level
		int[] roadTypesToDraw = gp.getTypesAtCurrentZoom(currentZoomLevel);
		
		double zoomedOutValue;		//The value that the current zoom level will represent
		double zoomedInValue;		//The value that the next zoom level will represent
		double difference;			//The difference between the two zoom levels
		
		double currentRelativeZoom;	//How long we are between the two zoom levels
		
		float transparency;				//The transparency that the roads should be drawn with
		
		//We get a collected list of all edges that should be drawn in the current perspective of the map
		List<List<Edge>> enormousListOfEdges = getDrawEdges(roadTypesToDraw);
		
		//We iterate through all the indexes that should be drawn on the current zoom level
		for(int i=0; i<roadTypesToDraw.length; i++) 
		{
			//If there are any roads of the applicable road type we draw them
			if(!enormousListOfEdges.get(i).isEmpty()) 
			{
				Edge testEdge = enormousListOfEdges.get(i).get(0);		//We get some edge of the current road type
				gp.setLineWidth(testEdge);								//We set the line width to that of the edge type. (All lines of the same type has the same width)
				float[] colors = gp.getLineColor(testEdge);				//We set the color to draw with to that of the current road type
				
				transparency = -1;		//We set the transparency to an invalid number
				
				//Calculate if the line should fade in
				//Check if the upper bound of the current zoom level is less than the zoom level to be drawn (Basically if this is the zoom level that the road is starting to be shown at)
				if(ZoomLevel.getInstance().findIndex(currentZoomLevel) < gp.getAllowedZoomIndex(roadTypesToDraw[i])+1)
				{
					//Get the upper bound of the zoom level we are currently in between
					zoomedOutValue = ZoomLevel.getInstance().getZoomLevel(ZoomLevel.getInstance().findIndex(currentZoomLevel));
					//Get the lower bound of the zoom level we are currently in between
					zoomedInValue = ZoomLevel.getInstance().getZoomLevel(gp.getAllowedZoomIndex(roadTypesToDraw[i])+1);
					
					//Calculate the length of the interval between the zoom levels
					difference = zoomedOutValue - zoomedInValue;
					
					//Calculate how far we are into the next zoom level
					currentRelativeZoom = zoomedOutValue-currentZoomLevel;
					
					//Set the transparency to be a % of how far we are into the next zoom level
					transparency = (float)currentRelativeZoom/(float)difference;
				}
				
				//Draw lines
				gl2.glBegin( GL.GL_LINES );
				
				//We check whether or not there is to be applied a transparency to the line we draw, then we draw all lines as applicable.
				if(transparency < 0)
					for(Edge e: enormousListOfEdges.get(i))
							drawLine(e, gl2, colors[0], colors[1], colors[2]);
				else
					for(Edge e: enormousListOfEdges.get(i))
						drawLine(e, gl2, colors[0], colors[1], colors[2], transparency);
				
				gl2.glEnd();
			}
		}
		
		//We iterate through the array again since we need to draw center lines where applicable
		for(int i=0; i<roadTypesToDraw.length; i++) {
			if(!enormousListOfEdges.get(i).isEmpty()) {
				
				Edge testEdge = enormousListOfEdges.get(i).get(0);			//Get some edge from the current road type list
				
				//Check if this road type should be drawn with a center line
				if(gp.hasCenterLine(testEdge)) 
				{
					//Set the with and color of the center line based on some edge of the road type
					gp.setCenterLineWidth(testEdge);
					float[] colors = gp.getLineCenterColor(testEdge);
					
					transparency = -1;		//We set the transparency to an invalid number
					
					//Calculate if the line should fade in
					//Check if the upper bound of the current zoom level is less than the zoom level to be drawn (Basically if this is the zoom level that the road is starting to be shown at)
					if(ZoomLevel.getInstance().findIndex(currentZoomLevel) < gp.getAllowedZoomIndex(roadTypesToDraw[i])+1)
					{
						//Get the upper bound of the zoom level we are currently in between
						zoomedOutValue = ZoomLevel.getInstance().getZoomLevel(ZoomLevel.getInstance().findIndex(currentZoomLevel));
						//Get the lower bound of the zoom level we are currently in between
						zoomedInValue = ZoomLevel.getInstance().getZoomLevel(gp.getAllowedZoomIndex(roadTypesToDraw[i])+1);
						
						//Calculate the length of the interval between the zoom levels
						difference = zoomedOutValue - zoomedInValue;
						
						//Calculate how far we are into the next zoom level
						currentRelativeZoom = zoomedOutValue-currentZoomLevel;
						
						//Set the transparency to be a % of how far we are into the next zoom level
						transparency = (float)currentRelativeZoom/(float)difference;
					}
					
					gl2.glBegin(GL.GL_LINES);
					
					//We check whether or not there is to be applied a transparency to the line we draw, then we draw all lines as applicable.
					if(transparency < 0)
						for(Edge e: enormousListOfEdges.get(i))
								drawLine(e, gl2, colors[0], colors[1], colors[2]);
					else
						for(Edge e: enormousListOfEdges.get(i))
							drawLine(e, gl2, colors[0], colors[1], colors[2], transparency);
					
					gl2.glEnd();
				}
			}
			
			//We check whether or not there is a route to draw, then draw it as applicable
			if(routeToDraw != null) 
				drawRoute(gl2, routeToDraw);
		}
	}
	
	/**
	 * Draw the route on the map
	 * @param gl2
	 * @param edges The edges to be drawn
	 */
	public void drawRoute(GL2 gl2, Edge[] edges) {
		
		gp.setLineWidth(3f);					//We set the line width of the edge
		float[] colors = new float[] {0,0,255};	//We set the color of the edge
		
		gl2.glBegin(GL.GL_LINES);
		
		//We iterate through the edge array, until we reach the route cutoff, in this way we draw to road procedually, animating it in
		for(int i=edges.length-1; i>=currentRouteCutoff; i--)
			drawLine(edges[i], gl2, colors[0], colors[1], colors[2]);
		
		gl2.glEnd();
		
		//If we have not drawn the whole route yet we decrement the route cutoff
		if(currentRouteCutoff > 0)
			currentRouteCutoff--;
	}
	
	/**
	 * Apply the zoom to the map
	 * @param gl2
	 */
	private void applyZoom(GL2 gl2)
	{
		//Add a translation transformation to the current matrix, effectively moving the ingame 3d coordinate system by the specified amount.
		//Here, we move the system so that the bottom left point is in the middle of the screen.
		gl2.glTranslatef( width / 2, height / 2, 0 );
		//Then we apply the orthographic projection matrix, where we specify exactly what coordinates we're interested in viewing on our screen.
		//Couple this with the factors that change when you scroll in and out on the mousewheel and you get a 'true' zoom feature,
		//Where nothing is scaled but you simply see alot less on the entire screen, AKA zooming.
		gl2.glOrtho( 0, getWidthFactor(), 0, getHeightFactor(), -1, 1 );
		//And remove the translation again.
		gl2.glTranslatef( -width / 2, -height / 2, 0 );
	}
	
	/**
	 * Apply the panning to the map
	 * @param gl2
	 */
	private void applyPanning(GL2 gl2)
	{
		gl2.glTranslated( Translation.getInstance().getTranslation().x, -Translation.getInstance().getTranslation().y, 0 );
	}
	
	//GOGO CHOLEWA!
	private List<List<Edge>> getDrawEdges(int[] types)
	{
		int width = this.width;
		int height = this.height;
		double zoomFactor = ((Translation.getInstance().getTranslation().getX()) - (width-(width*getWidthFactor())/2)/2 );
		double xs = (zoomFactor)* -1000 + 10 * -1000;
		double xe = (((zoomFactor) - width*getWidthFactor()/2)*-1000) - 10 * -1000;

		zoomFactor = ((Translation.getInstance().getTranslation().getY()) + (height-(height*getHeightFactor())/2)/2);
		double ys = zoomFactor * 1000 - 10 * 1000;
		double ye = (((zoomFactor) + height*getHeightFactor()/2)*1000) + 10 * 1000;
		
		 Interval<Double> xAxis = new Interval<Double>(xs, xe);
	     Interval<Double> yAxis = new Interval<Double>(ys, ye);
	     
	     Interval2D<Double> rect = new Interval2D<Double>(xAxis, yAxis);
	     LinkedList<List<Edge>> list = new LinkedList<List<Edge>>();
	     for(int type : types)
	    	 list.add(q.queryEdges(rect, type));
	     return list;
	}
	
	/**
	 * Draws an edge on the map using the edges own position and the specified color
	 * @param edge
	 * @param gl2
	 * @param r
	 * @param g
	 * @param b
	 */
	private void drawLine(Edge edge, GL2 gl2, float r, float g, float b)
	{
		drawLine(edge, gl2, r, g, b, 1);
	}
	
	/**
	 * Draws an edge on the map using the edges own position and the specified color and transparency
	 * @param edge The edge to be drawn
	 * @param gl2
	 * @param r (0-255)
	 * @param g (0-255)
	 * @param b (0-255)
	 * @param a The transparency of the line (0 - 1);
	 */
	private void drawLine(Edge edge, GL2 gl2, float r, float g, float b, float a)
	{		
		gl2.glColor4f( (r/255), (g/255), (b/255), (a));
		gl2.glVertex2d( edge.getXFrom() / 1000.0,  edge.getYFrom() / 1000.0 );
		gl2.glColor4f( (r/255), (g/255), (b/255), (a));
		gl2.glVertex2d( edge.getXTo() / 1000.0,  edge.getYTo() / 1000.0 );
	}
	
	@Override
	public void dispose( GLAutoDrawable arg0 )
	{
	}

	@Override
	public void reshape( GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4 )
	{
	}
	
	/**
	 * Process any zoom animation that might be happening based on the maps state
	 */
	public void animateZoom()
	{
		targetZoomLevel = ZoomLevel.getInstance().getZoomLevel();				//The zoom level we wish to animate to
		
		double lastZoomLevel = currentZoomLevel;								//The zoom level we are currently at
		
		double increment = 0.1*Math.abs((targetZoomLevel-currentZoomLevel));	//The speed we wish to zoom with
		
		//If the increment is small enough we set it to a linear value instead of an asymptote one
		if(increment < currentZoomLevel*0.01f)
			increment = currentZoomLevel*0.01f;
		
		if(targetZoomLevel > currentZoomLevel)
		{
			//If the target zoom is larger than the current zoom level we wish to zoom out
			
			//If the current zoom + the increment is less than the target zoom level we apply the increment
			if(currentZoomLevel+increment < targetZoomLevel)
				currentZoomLevel += increment;
			else
				currentZoomLevel = targetZoomLevel;			//Otherwise we set the current zoom level to the target zoom level
		}
		else if(targetZoomLevel < currentZoomLevel)
		{
			//If the target zoom is smaller than the current zoom level we wish to zoom in
			
			if(currentZoomLevel-increment > targetZoomLevel)
				currentZoomLevel -= increment;
			else
				currentZoomLevel = targetZoomLevel;
		}
		
		//If we reach a static zoom level position we reset the mouse animator position
		if(currentZoomLevel == lastZoomLevel)
			ZoomLevel.getInstance().setMousePosition(null);
		
		//In order to animate a zoom towards the mouse properly we get the last known position of the mouse during a zoom
		Point lastMousePoint = ZoomLevel.getInstance().getLastMousePosition();
		
		//If the position is a valid number we process it
		if(lastMousePoint.x != 0 && lastMousePoint.y != 0)
		{
			//We get the center point of the map
			Point centerPoint = new Point(width/2, height/2);
			
			//Calculate the difference between the center point and the mouse position
			Point difference = new Point((centerPoint.x-lastMousePoint.x)/2, (centerPoint.y-lastMousePoint.y)/2);
			
			//Calculate the difference between the last zoom level and the current zoom level
			double zoomLevelDifference = lastZoomLevel-currentZoomLevel;
			
			//We then apply the translation manually (This is an action based on a manual input (Scrolling/double clicking) so therefore we break any current translate animation)
			Translation.getInstance().manualTranslate(difference.x*zoomLevelDifference, difference.y*zoomLevelDifference);
		}
	}
	
	/**
	 * Process any translate animation that might be happening based on the maps state
	 */
	public void animatePan()
	{
		//We start out by making a reference to the translation and target translation
		Point2D.Double translation = Translation.getInstance().getTranslation();
		Point2D.Double tarTranslation = Translation.getInstance().getTargetTranslation();
		Point2D.Double deltaTranslation = new Point2D.Double();				//The translation we wish to translate by
		
		//We get the increment that we want to translate with
		//We calculate it by the difference of the x coordinates and the y coordinates
		double xIncrement = (0.075f*Math.abs(tarTranslation.x-translation.x));
		double yIncrement = (0.075f*Math.abs(tarTranslation.y-translation.y));
		
		//If either of the increments are small enoguh we set the translation to a linear value instead of an asymptote one
		if(xIncrement < width*0.0001f*currentZoomLevel)
			xIncrement = width*0.0001f*currentZoomLevel;
		
		if(yIncrement < width*0.0001f*currentZoomLevel)
			yIncrement = width*0.0001f*currentZoomLevel;
		
		//We check if the x translation is higher or lower than the target x translation
		if(tarTranslation.x > translation.x)
		{
			//We check if the increment will reach the target translation or not and assign the delta translation applicably
			if(translation.x+xIncrement < tarTranslation.x)
				deltaTranslation.x = xIncrement;
			else
				deltaTranslation.x = tarTranslation.x-translation.x;
		}
		else if(tarTranslation.x < translation.x)
		{
			if(translation.x-xIncrement > tarTranslation.x)
				deltaTranslation.x = -xIncrement;
			else
				deltaTranslation.x = tarTranslation.x-translation.x;
		}
		
		//We check if the y translation is higher or lower than the target y translation
		if(tarTranslation.y > translation.y)
		{
			if(translation.y+yIncrement < tarTranslation.y)
				deltaTranslation.y = yIncrement;
			else
				deltaTranslation.y = tarTranslation.y-translation.y;
		}
		else if(tarTranslation.y < translation.y)
		{
			if(translation.y-yIncrement > tarTranslation.y)
				deltaTranslation.y = -yIncrement;
			else
				deltaTranslation.y = tarTranslation.y-translation.y;
		}
		
		//We translate the map by the calculated delta translation
		Translation.getInstance().translate(deltaTranslation.x, deltaTranslation.y);
	}
	
	//Mouse events

	@Override
	public void mouseDragged( MouseEvent arg0 )
	{
		//Get the x and y pos of the mouse cursor.
		int xPos = arg0.getXOnScreen(), yPos = arg0.getYOnScreen();

		//Get the difference, also taking into consideration the zoom level so we don't end up translation too much.
		//double xDiff = ( ( xPos - originalEvent.getXOnScreen() ) * getWidthFactor() ) , yDiff = ( ( yPos - originalEvent.getYOnScreen() ) * getHeightFactor() );
		
		//Get the difference, also taking into consideration the zoom level so we don't end up translation too much.
		double xDiff = ( ( xPos - curMousePos.x ) * getWidthFactor() ) , yDiff = ( ( yPos - curMousePos.y ) * getHeightFactor() );
		
		//Apply the translation
		Translation.getInstance().manualTranslate(xDiff, yDiff);
		
		//Rebase the current mouse position, so that the next time we drag the mouse, we will have a new starting point to offset from.
		curMousePos.x = xPos;
		curMousePos.y = yPos;
	}

	@Override
	public void mouseMoved( MouseEvent arg0 )
	{
	}

	@Override
	public void mouseClicked( MouseEvent e )
	{
	}

	@Override
	public void mouseEntered( MouseEvent e )
	{
		

	}

	@Override
	public void mouseExited( MouseEvent e )
	{
		
	}

	@Override
	public void mousePressed( MouseEvent e )
	{
		//Set the target translation to 0 in order to stop any current translation animation (because we "grab" the map)
		Translation.getInstance().manualTranslate(0, 0);
		
		//We update the current mouse position
		curMousePos.x = e.getXOnScreen();
		curMousePos.y = e.getYOnScreen();
		
		//We check if it is the left or the right mouse button that is pressed
		if(e.getButton() == e.BUTTON1)
		{
			//Double tap left click to zoom in
			
			//If the difference between the current press and the last press is less than 350 miliseconds we assume that we have a double click
			if(System.currentTimeMillis() - lastLeftMousePressTime < 350)
			{
				//If we can zoom in, we zoom in and update the animation information about the current zoom
				if(ZoomLevel.getInstance().zoomIn())
					applyZoomTranslation(e);
				
				//We reset the last time the mouse was pressed
				lastLeftMousePressTime = 0;
			}
			else
				lastLeftMousePressTime = System.currentTimeMillis();		//if we did not double click we update the last time the mouse was clicked
		}
		else if(e.getButton() == e.BUTTON3)
		{
			//Double tap right click to zoom out
			
			//If the difference between the current press and the last press is less than 350 miliseconds we assume that we have a double click
			if(System.currentTimeMillis() - lastRightMousePressTime < 350)
			{
				//If we can zoom out, we zoom out and update the animation information about the current zoom
				if(ZoomLevel.getInstance().zoomOut())
					applyZoomTranslation(e);
				
				//We reset the last time the mouse was pressed
				lastRightMousePressTime = 0;
			}
			else
				lastRightMousePressTime = System.currentTimeMillis();		//if we did not double click we update the last time the mouse was clicked
		}
		
	}

	@Override
	public void mouseReleased( MouseEvent e )
	{
		//We get the current translation of the map
		Point2D.Double currTranslation = Translation.getInstance().getTranslation();
		
		//Check if the current translation indicates that the map is outside of view
		if(currTranslation.x > mapOffset.x+width/1.5 || currTranslation.x < mapOffset.x-width/1.5 || currTranslation.y > mapOffset.y+height/1.25 || currTranslation.y < mapOffset.y-height/1.25)
		{
			//If it is, we reset the maps location and zoom level to the starting points
			Translation.getInstance().goToTranslation(mapOffset.x, mapOffset.y);
			ZoomLevel.getInstance().setZoomLevel(0);
		}
	}

	@Override
	public void mouseWheelMoved( MouseWheelEvent e )
	{
		//We check the direction of the scroll and zoom in the given direction
		if( e.getUnitsToScroll() < 0 )
			if(ZoomLevel.getInstance().zoomIn())
				applyZoomTranslation(e);			//We update the information about the zoom in order to animate the zoom properly
		else
			if(ZoomLevel.getInstance().zoomOut())
				applyZoomTranslation(e);			//We update the information about the zoom in order to animate the zoom properly
	}
	
	/**
	 * Applies the current mouse position to the zoom information in order to animate the zoom properly
	 * @param e A mouse even
	 */
	public void applyZoomTranslation(MouseEvent e)
	{
		//Convert the mouse position into a mouse position relative to the maps base width and height
		Point convertedMousePoint = new Point((int)(Math.round(((double)e.getPoint().x/(double)getWidth())*(double)width)), 
				(int)(Math.round(((double)e.getPoint().y/(double)getHeight())*(double)height)));
		
		//Notify the Zoom Level singleton about the mouse position when we began our zoom, this is used when we animate the zoom
		ZoomLevel.getInstance().setMousePosition(convertedMousePoint);
	}
	
	/**
	 * Calculate a route between two edges, defined by indexes of the underlying graph
	 * @param from
	 * @param to
	 */
	public void getRoute(int from, int to)
	{
		//Update which roads we used the last time we animated a road
		routeFrom = from;
		routeTo = to;
		
		//Initialize the thread that calculates the route
		new GetRoute(from, to);
	}

	/**
	 * Sets the route that the map should draw
	 * @param edges
	 */
	public void setRoute(Edge[] edges)
	{ 
		//Check if it is not the same route we are trying to display
		if(edges != routeToDraw)
			currentRouteCutoff = edges.length-1;	//if it is not we reset the animator index
		
		//We set the route to draw to the route that we have given
		routeToDraw = edges;
	}
	
	/**
	 * Returns whether or not we currently have a route on the map to show
	 * @return
	 */
	public boolean hasRoute()
	{
		return routeToDraw != null && routeFrom >= 0 && routeTo >= 0;
	}
	
	/**
	 * Refreshes the map that we searched for last
	 */
	public void refreshRoute()
	{
		//Get the route in case we already have a route to get
		if(hasRoute())
			getRoute(routeFrom, routeTo);
	}
	
	/**
	 * @return the relative width of the map
	 */
	public int getMapWidth()
	{
		return width;
	}

	/**
	 * @return the relative height of the map
	 */
	public int getMapHeight()
	{
		return height;
	}
	
	/**
	 * @return the base offset of the map
	 */
	public Point2D.Double getOffset()
	{
		return mapOffset;
	}
}
