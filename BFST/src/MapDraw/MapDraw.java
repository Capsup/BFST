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
		
		for(int i=1; i<roadTypesToDraw.length; i++) 
		{
			//We iterate through all the indexes that should be drawn on the current zoom level
			
			//If there are any roads of the applicable road type we draw them
			if(!enormousListOfEdges.get(i).isEmpty()) 
			{
				
				Edge testEdge = enormousListOfEdges.get(i).get(0);
				gp.setLineWidth(testEdge);
				float[] colors = gp.getLineColor(testEdge);
				
				transparency = -1;
				
				//Calculate if the line should fade in
				if(ZoomLevel.getInstance().findIndex(currentZoomLevel) < gp.getAllowedZoomIndex(roadTypesToDraw[i])+1)
				{
					zoomedOutValue = ZoomLevel.getInstance().getZoomLevel(ZoomLevel.getInstance().findIndex(currentZoomLevel));
					zoomedInValue = ZoomLevel.getInstance().getZoomLevel(gp.getAllowedZoomIndex(roadTypesToDraw[i])+1);
					difference = zoomedOutValue - zoomedInValue;
					
					currentRelativeZoom = zoomedOutValue-currentZoomLevel;
					
					transparency = (float)currentRelativeZoom/(float)difference;
				}
				
				gl2.glBegin( GL.GL_LINES );
				for(Edge e: enormousListOfEdges.get(i)) {
					
					if(transparency < 0)
						drawLine(e, gl2, colors[0], colors[1], colors[2]);
					else 
						drawLine(e, gl2, colors[0], colors[1], colors[2], transparency);
						
				}
				gl2.glEnd();
			}
		}
		
		for(int i=1; i<roadTypesToDraw.length; i++) {
			if(!enormousListOfEdges.get(i).isEmpty()) {
				//Edge testEdge = getDrawEdges(roadTypesToDraw[i]).get(0);
				Edge testEdge = enormousListOfEdges.get(i).get(0);
				if(gp.hasCenterLine(testEdge)) {
					gp.setCenterLineWidth(testEdge);
					float[] colors = gp.getLineCenterColor(testEdge);
					
					transparency = -1;
					
					//Calculate if the line should fade in
					if(ZoomLevel.getInstance().findIndex(currentZoomLevel) < gp.getAllowedZoomIndex(roadTypesToDraw[i])+1)
					{
						zoomedOutValue = ZoomLevel.getInstance().getZoomLevel(ZoomLevel.getInstance().findIndex(currentZoomLevel));
						zoomedInValue = ZoomLevel.getInstance().getZoomLevel(gp.getAllowedZoomIndex(roadTypesToDraw[i])+1);
						difference = zoomedOutValue - zoomedInValue;
						
						currentRelativeZoom = zoomedOutValue-currentZoomLevel;
						
						transparency = (float)currentRelativeZoom/(float)difference;
					}
					
					gl2.glBegin(GL.GL_LINES);
					
					//for(Edge e: getDrawEdges(roadTypesToDraw[i])) {
					for(Edge e: enormousListOfEdges.get(i)) {
						
						if(transparency < 0)
							drawLine(e, gl2, colors[0], colors[1], colors[2]);
						else 
							drawLine(e, gl2, colors[0], colors[1], colors[2], transparency);
					}
					gl2.glEnd();
				}
			}
		
			
			if(routeToDraw != null) {
				drawRoute(gl2, routeToDraw);
			}
			
		}
	}
	
	public void drawRoute(GL2 gl2, Edge[] edges) {
		
		gp.setLineWidth(3f);
		float[] colors = new float[] {0,0,255};
		gl2.glBegin(GL.GL_LINES);
		/*for(Edge e: edges) {
			drawLine(e, gl2, colors[0], colors[1], colors[2]);
		}*/
		for(int i=edges.length-1; i>=currentRouteCutoff; i--) {
			drawLine(edges[i], gl2, colors[0], colors[1], colors[2]);
		}
		gl2.glEnd();
		
		if(currentRouteCutoff > 0)
			currentRouteCutoff--;
	}
	
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
	
	private void applyPanning(GL2 gl2)
	{
		gl2.glTranslated( Translation.getInstance().getTranslation().x, -Translation.getInstance().getTranslation().y, 0 );
	}
	
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
	
	private void drawLine(Edge edge, GL2 gl2, float r, float g, float b)
	{		
		gl2.glColor3f( (r/255), (g/255), (b/255) );
		gl2.glVertex2d( edge.getXFrom() / 1000.0,  edge.getYFrom() / 1000.0 );
		gl2.glColor3f( (r/255), (g/255), (b/255) );
		gl2.glVertex2d( edge.getXTo() / 1000.0,  edge.getYTo() / 1000.0 );
	}
	
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
		// TODO Auto-generated method stub

	}

	@Override
	public void reshape( GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4 )
	{
		// TODO Auto-generated method stub

	}
	
	public void animateZoom()
	{
		if(targetZoomLevel != ZoomLevel.getInstance().getZoomLevel()) {
			targetZoomLevel = ZoomLevel.getInstance().getZoomLevel();
			//zoomDifference = currentZoomLevel - targetZoomLevel;
		}
		
		double lastZoomLevel = currentZoomLevel;
		
		double increment = 0.1*Math.abs((targetZoomLevel-currentZoomLevel));
		
		if(increment < currentZoomLevel*0.01f)
			increment = currentZoomLevel*0.01f;
		
		if(targetZoomLevel > currentZoomLevel)
		{
			if(currentZoomLevel+increment < targetZoomLevel)
				currentZoomLevel += increment;
			else
				currentZoomLevel = targetZoomLevel;
		}
		else if(targetZoomLevel < currentZoomLevel)
		{
			if(currentZoomLevel-increment > targetZoomLevel)
				currentZoomLevel -= increment;
			else
				currentZoomLevel = targetZoomLevel;
		}
		
		if(currentZoomLevel == lastZoomLevel)
			ZoomLevel.getInstance().setMousePosition(null);
		
		Point lastMousePoint = ZoomLevel.getInstance().getLastMousePosition();
		
		if(lastMousePoint.x != 0 && lastMousePoint.y != 0)
		{
			Point centerPoint = new Point(width/2, height/2);
			
			Point difference = new Point((centerPoint.x-lastMousePoint.x)/2, (centerPoint.y-lastMousePoint.y)/2);
			
			double zoomLevelDifference = lastZoomLevel-currentZoomLevel;
			
			Translation.getInstance().manualTranslate(difference.x*zoomLevelDifference, difference.y*zoomLevelDifference);
		}
	}
	
	public void animatePan()
	{
		Point2D.Double translation = Translation.getInstance().getTranslation();
		Point2D.Double tarTranslation = Translation.getInstance().getTargetTranslation();
		Point2D.Double deltaTranslation = new Point2D.Double();
		
		double xIncrement = (0.075f*Math.abs(tarTranslation.x-translation.x));
		double yIncrement = (0.075f*Math.abs(tarTranslation.y-translation.y));
		
		
		if(xIncrement < width*0.0001f*currentZoomLevel)
			xIncrement = width*0.0001f*currentZoomLevel;
		
		if(yIncrement < width*0.0001f*currentZoomLevel)
			yIncrement = width*0.0001f*currentZoomLevel;
		
		if(tarTranslation.x > translation.x)
		{
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
		Translation.getInstance().manualTranslate(0, 0);
		
		curMousePos.x = e.getXOnScreen();
		curMousePos.y = e.getYOnScreen();
		
		if(e.getButton() == e.BUTTON1)
		{
			//Double tap to zoom
			if(System.currentTimeMillis() - lastLeftMousePressTime < 350)
			{
				if(ZoomLevel.getInstance().zoomIn())
					applyZoomTranslation(e, 1, 1);
				
				lastLeftMousePressTime = 0;
			}
			else
				lastLeftMousePressTime = System.currentTimeMillis();
		}
		else if(e.getButton() == e.BUTTON3)
		{
			//Double tap right click to zoom
			if(System.currentTimeMillis() - lastRightMousePressTime < 350)
			{
				if(ZoomLevel.getInstance().zoomOut())
					applyZoomTranslation(e, -1, 1);
				
				lastRightMousePressTime = 0;
			}
			else
				lastRightMousePressTime = System.currentTimeMillis();
		}
		
	}

	@Override
	public void mouseReleased( MouseEvent e )
	{
		Point2D.Double currTranslation = Translation.getInstance().getTranslation();
		
		if(currTranslation.x > mapOffset.x+width/1.5 || currTranslation.x < mapOffset.x-width/1.5 || currTranslation.y > mapOffset.y+height/1.25 || currTranslation.y < mapOffset.y-height/1.25)
		{
			Translation.getInstance().goToTranslation(mapOffset.x, mapOffset.y);
			ZoomLevel.getInstance().setZoomLevel(0);
		}
	}

	@Override
	public void mouseWheelMoved( MouseWheelEvent e )
	{
		if( e.getUnitsToScroll() < 0 )
		{
			if(ZoomLevel.getInstance().zoomIn())
				applyZoomTranslation(e, 1, 1);
		}
		else
		{
			
			if(ZoomLevel.getInstance().zoomOut())
				applyZoomTranslation(e, -1, 1);
		}
		
		//System.out.println("Zoomlevel: " + (ZoomLevel.getInstance().getZoomIndex()) + "/30");

		// width += -1 * e.getUnitsToScroll();
		// height += -1 * e.getUnitsToScroll();

		/*
		 * double xDiff = width * e.getUnitsToScroll() / 2; double yDiff = height * e.getUnitsToScroll() / 2;
		 * 
		 * translation = new Point( (int) (translation.getX() + xDiff), (int) (translation.getY() + yDiff) );
		 */

		// System.out.println(scale);
	}
	
	public void applyZoomTranslation(MouseEvent e, int zoomDirection, int zoomFactor)
	{
		Point convertedMousePoint = new Point((int)(Math.round(((double)e.getPoint().x/(double)getWidth())*(double)width)), (int)(Math.round(((double)e.getPoint().y/(double)getHeight())*(double)height)));
		ZoomLevel.getInstance().setMousePosition(convertedMousePoint);
	}
	
	public void getRoute(int from, int to)
	{
		routeFrom = from;
		routeTo = to;
		
		GetRoute route = new GetRoute(from, to);
		
		if(route.hasNewPath() || route.hasNewTransport())
			route.start();
			
	}

	public void setRoute(Edge[] edges)
	{ 
		//routeToDraw = null;
		
		if(edges != routeToDraw)
			currentRouteCutoff = edges.length-1;
		
		routeToDraw = edges;
	}
	
	public boolean hasRoute()
	{
		return routeToDraw != null && routeFrom >= 0 && routeTo >= 0;
	}
	
	public void refreshRoute()
	{
		if(hasRoute())
		{
			getRoute(routeFrom, routeTo);
		}
	}
	
	public int getMapWidth()
	{
		return width;
	}
	
	public int getMapHeight()
	{
		return height;
	}
	
	public Point2D.Double getOffset()
	{
		return mapOffset;
	}
}
