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

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLJPanel;
import javax.swing.JFrame;
import javax.swing.JPanel;

import XMLParser.Edge;
import XMLParser.XMLParser;

import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.FPSAnimator;

public class MapDraw extends JPanel implements GLEventListener, MouseListener, MouseMotionListener, MouseWheelListener
{
	private long lastMousePressTime;
	
	private int width;
	private int height;
	private Point curMousePos;

	private int tick;
	private int maximumFPS = 60;
	
	public MapDraw()
	{
		setLayout(new BorderLayout());
		
		//Start translation at the place where the map is inside our 3D world.
		Translation.getInstance().setTranslation(-266, 5940);
		curMousePos = new Point( 0, 0 );
		
		//Tell OpenGL that we're interested in the default profile, meaning we don't get any specific properties of the gl context.
		GLCapabilities glCapabilities = new GLCapabilities( GLProfile.getDefault() );

		XMLParser.makeDataSet();
 
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
		FPSAnimator animator = new FPSAnimator(panel, 60);
		animator.start();
		
		width = 800;
		height = 600;
		
		//We set the initial zoom level to be 0
		ZoomLevel.getInstance().setZoomLevel(0);
	}
	
	public double getWidthFactor()
	{
		return (width/height)*getHeightFactor();
	}
	
	public double getHeightFactor()
	{
		return ZoomLevel.getInstance().getZoomLevel();
	}
	
	//OpenGL Events
	@Override
	public void init( GLAutoDrawable arg0 )
	{
		//Get a OpenGL v2 context.
		GL2 gl = arg0.getGL().getGL2();

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
		if(tick % (60/maximumFPS) == 0)
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
		
			//Get the ArrayList containing all the edges of the map
			ArrayList<Edge> edges = XMLParser.getEdgeList();
			
			//We calculate the borders of which we want to draw lines. In this we only need to search a partition of our edge array
			Rectangle drawEdges = getDrawEdges(edges);
			
			//We iterate through our edge array from the first edge with the lowest x coordinate inside the map
			//We end the iteration at the last edge with the highest x coordinate that is still inside the map
			for(int i = drawEdges.x; i < drawEdges.width-1; i++)
			{
				int roadType = edges.get(i).getTyp();
				
				if(zoomLevelAllowRoadType(roadType)) 
				{
					if(roadType == 1) 
					{
						gl2.glLineWidth(3f);
						gl2.glBegin(GL.GL_LINES);
						drawLine(edges.get(i), gl2);
						gl2.glEnd();
					} else if(roadType > 1 && roadType < 8) {
						gl2.glLineWidth(2f);
						gl2.glBegin(GL.GL_LINES);
						drawLine(edges.get(i), gl2);
						gl2.glEnd();
					} else {
						gl2.glLineWidth(1.5f);
						gl2.glBegin(GL.GL_LINES);
						drawLine(edges.get(i), gl2);
						gl2.glEnd();
					}
				}
			}

		}
		
		tick++;
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
	
	private Rectangle getDrawEdges(ArrayList<Edge> edges)
	{
		double zoomFactor = ((Translation.getInstance().getTranslation().getX()) - (width-(width*getWidthFactor())/2)/2 );
		int xs = XMLParser.edgeSearch(edges, (zoomFactor + 20 )* -1000);
		int xe = XMLParser.edgeSearch(XMLParser.getEdgeListTo(), ((zoomFactor - 20 ) - width*getWidthFactor()/2)*-1000);
		xs = xs > 0 ? xs : -xs;
		xe = xe > 0 ? xe : -xe;
		
		//A code fragment that we need for later optimization of the iteration partitioning
		/*
		int height = this.getHeight()+20;
		zoomFactor = ((translation.getY()) - (height-(height*getHeightFactor())/2)/2);
		int ys = XMLParser.edgeSearch(edgesY, zoomFactor * -1000);
		int ye = XMLParser.edgeSearch(XMLParser.getEdgeListYTo(), (zoomFactor - height*getHeightFactor()/2)*-1000);
		ys = ys > 0 ? ys : -ys;
		ye = ye > 0 ? ye : -ye;
		*/
		
		int ys = 0;
		int ye = 0;
		
		return new Rectangle(xs, ys, xe, ye);
	}
	
	private boolean zoomLevelAllowRoadType(int roadType)
	{
		int zoomIndex = ZoomLevel.getInstance().getZoomIndex();
		
		boolean canShow = roadType < 3;
		
		if(zoomIndex < 3)
			canShow = roadType < 4;
		else if(zoomIndex < 6)
			canShow = roadType < 5;
		else if(zoomIndex < 12)
			canShow = roadType < 6;
		else if(zoomIndex < 15)
			canShow = roadType < 7;
		else if(zoomIndex < 16)
			canShow = roadType < 8;
		else
			canShow = true;
		
		return canShow;
	}
	
	private void drawLine(Edge edge, GL2 gl2)
	{
		int r=0;
		int g=0;
		int b=0;
		
		if( edge.getTyp() == 1 ) 
			r = 255;
		else if( edge.getTyp() < 5 ) 
			b = 255;
		else if( edge.getTyp() == 8 ) 
			g = 255;	
		
		gl2.glColor3f( r, g, b );
		gl2.glVertex2d( edge.getXFrom() / 1000.0,  edge.getYFrom() / 1000.0 );
		gl2.glColor3f( r, g, b );
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
		double xDiff = ( ( xPos - curMousePos.x ) * ZoomLevel.getInstance().getZoomLevel())/2, yDiff = ( ( yPos - curMousePos.y ) * ZoomLevel.getInstance().getZoomLevel()/2);
		
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
		//We need the information of the mouse current position for the panning method
		curMousePos.x = e.getXOnScreen();
		curMousePos.y = e.getYOnScreen();
		
		//Double tap to zoom
		if(System.currentTimeMillis() - lastMousePressTime < 275)
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
	}

}