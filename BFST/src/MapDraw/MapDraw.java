package MapDraw;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLJPanel;
import javax.swing.JFrame;
import javax.swing.JPanel;

import DataProcessing.Interval;
import DataProcessing.Interval2D;
import DataProcessing.Query;
import Graph.Edge;

import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.FPSAnimator;

public class MapDraw extends JPanel implements GLEventListener, MouseListener, MouseMotionListener, MouseWheelListener
{
	private long lastMousePressTime;
	
	private Query q = new Query();
	private int width;
	private int height;
	//private double scale = 1;
	private Point curMousePos;
	private GraphicsPrefs gp;
	
	private int tick;
	private int maximumFPS = 60;
	
	private long lastTime;
	
	public double getWidthFactor()
	{
		return (width/height)*getHeightFactor();
	}
	
	public double getHeightFactor()
	{
		return ZoomLevel.getInstance().getZoomLevel();
	}

	public MapDraw()
	{
		setLayout(new BorderLayout());
		//setSize( new Dimension( iWidth, iHeight ) );
		
		//Start translation at the place where the map is inside our 3D world.
		Translation.getInstance().setTranslation(-266, 5940);
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
		// panel.setDefaultCloseOperation( WindowClosingMode.DISPOSE_ON_CLOSE );
		
		add( panel, BorderLayout.CENTER);
		

		//Add an animator to our panel, which will start the rendering loop, repeatedly calling the display() function of our panel.
		//The FPS animator lets us put a limit to the refresh rate of the animator.
		FPSAnimator animator = new FPSAnimator(panel, 60);
		animator.start();

		width = 800;
		height = 600;

		ZoomLevel.getInstance().setZoomLevel(0);
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
		//if(tick % (60/maximumFPS) == 0) 
		long curTime = System.currentTimeMillis();
		if( curTime - lastTime > ( 1000 / 1 ) )
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
			gl2.glEnable(GL.GL_BLEND);
			gl2.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
			gl2.glHint(GL.GL_LINE_SMOOTH_HINT, GL.GL_NICEST);//GL.GL_DONT_CARE);

			drawLines(gl2);
			lastTime = curTime;
		}
		


	}
	
	private void drawLines(GL2 gl2) {
		
		int maxType = gp.getMaxTypeAtCurrentZoom();
		
		for(int i=1; i<maxType; i++) {
			Edge testEdge = getDrawEdges(i).get(0);
			gp.setLineWidth(testEdge);
			float[] colors = gp.getLineColor(testEdge);
			
			gl2.glBegin( GL.GL_LINES );
			for(Edge e: getDrawEdges(i)) {
				drawLine(e, gl2, colors[0], colors[1], colors[2]);
			}
			gl2.glEnd();
		}
		
		for(int i=1; i<4; i++) {
			Edge testEdge = getDrawEdges(i).get(0);
			gp.setCenterLineWidth(testEdge);
			float[] colors = gp.getLineCenterColor(testEdge);
			gl2.glBegin(GL.GL_LINES);
			
			for(Edge e: getDrawEdges(i)) {
				drawLine(e, gl2, colors[0], colors[1], colors[2]);
			}
			gl2.glEnd();
		}
		
//		//add the totally awesome lines in the center of highways.
//		for(Edge e: getDrawEdges(1)) {
//			gp.setLineWidth(1.4f);
//			int[] colors = new int[] {255,215,0};
//			gl2.glBegin( GL.GL_LINES );
//			drawLine(e, gl2, colors[0], colors[1], colors[2]);
//			gl2.glEnd();
//		}
//		
//		//add yellow to the center of 'motortraffikvej'-highways
//		for(Edge e: getDrawEdges(2)) {
//			gp.setLineWidth(1.4f);
//			int[] colors = new int[] {255,165,0};
//			gl2.glBegin( GL.GL_LINES );
//			drawLine(e, gl2, colors[0], colors[1], colors[2]);
//			gl2.glEnd();
//		}
//
//		//add yellow to the center of 'motortraffikvej'-highways
//		for(Edge e: getDrawEdges(3)) {
//			gp.setLineWidth(0.8f);
//			int[] colors = new int[] {255,165,0};
//			gl2.glBegin( GL.GL_LINES );
//			drawLine(e, gl2, colors[0], colors[1], colors[2]);
//			gl2.glEnd();
//		}
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
	
	private LinkedList<Edge> getDrawEdges(int type)
	{
		double zoomFactor = ((Translation.getInstance().getTranslation().getX()) - (width-(width*getWidthFactor())/2)/2 );
		double xs = (zoomFactor + 20 )* -1000;
		double xe = (((zoomFactor - 20 ) - width*getWidthFactor()/2)*-1000);

		/*
		zoomFactor = ((Translation.getInstance().getTranslation().getY()) - (height-(height*getHeightFactor())/2)/2);
		double ys = zoomFactor * -1000;
		double ye = ((zoomFactor - height*getHeightFactor()/2)*-1000);
		*/
		 Interval<Double> xAxis = new Interval<Double>(xs, xe);
	     Interval<Double> yAxis = new Interval<Double>(0.0, 10000000.0);
	     Interval2D<Double> rect = new Interval2D<Double>(xAxis, yAxis);
	     return q.queryEdges(rect, type);
	}
	
	private void drawLine(Edge edge, GL2 gl2, float r, float g, float b)
	{		
		
		gl2.glColor3f( (r/255), (g/255), (b/255) );
		gl2.glVertex2d( edge.getXFrom() / 1000.0,  edge.getYFrom() / 1000.0 );
		gl2.glColor3f( (r/255), (g/255), (b/255) );
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
		
		Translation.getInstance().translate(xDiff, yDiff);
		
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
		curMousePos.x = e.getXOnScreen();
		curMousePos.y = e.getYOnScreen();
		
		//Double tap to zoom
		if(System.currentTimeMillis() - lastMousePressTime < 350)
		{
			ZoomLevel.getInstance().zoomIn();
			lastMousePressTime = 0;
		}
		else
			lastMousePressTime = System.currentTimeMillis();
	}

	@Override
	public void mouseReleased( MouseEvent e )
	{
	}

	@Override
	public void mouseWheelMoved( MouseWheelEvent e )
	{
		if( e.getUnitsToScroll() < 0 )
		{
			ZoomLevel.getInstance().zoomIn();
		}
		else
		{
			ZoomLevel.getInstance().zoomOut();
		}
		
		System.out.println("Zoomlevel: " + (ZoomLevel.getInstance().getZoomIndex()) + "/30");

		// width += -1 * e.getUnitsToScroll();
		// height += -1 * e.getUnitsToScroll();

		/*
		 * double xDiff = width * e.getUnitsToScroll() / 2; double yDiff = height * e.getUnitsToScroll() / 2;
		 * 
		 * translation = new Point( (int) (translation.getX() + xDiff), (int) (translation.getY() + yDiff) );
		 */

		// System.out.println(scale);
	}

}
