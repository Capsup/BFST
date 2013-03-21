package MapDraw;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Robot;
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
import javax.swing.JTextField;

import XMLParser.Edge;
import XMLParser.Node;
import XMLParser.XMLParser;

import com.jogamp.opengl.util.Animator;

public class MapDraw extends Frame implements GLEventListener, MouseListener, MouseMotionListener, MouseWheelListener
{
	private int width;
	private int height;
	private Point2D.Double translation;
	private double scale = 1;
	private Point curMousePos;
	private static double[] zoomLevel;
	private static int currentZoomLevel = 0;
	
	static{
		zoomLevel = new double[30];
		for(int i = 0; i < zoomLevel.length; i++)
			zoomLevel[i] = 1.5 * Math.pow(0.75f, i);
	}
	
	public double getWidthFactor(){
		return (800/600)*getHeightFactor();
	}
	
	public double getHeightFactor(){
		return zoomLevel[currentZoomLevel];
	}

	public MapDraw( int iWidth, int iHeight )
	{
		//Start translation at the place where the map is inside our 3D world.
		translation = new Point2D.Double(-266, 5940);
		curMousePos = new Point( 0, 0 );
		
		//Tell OpenGL that we're interested in the default profile, meaning we don't get any specific properties of the gl context.
		GLCapabilities glCapabilities = new GLCapabilities( GLProfile.getDefault() );

		XMLParser.makeDataSet();

		//However, tell the context that we want it to be doublebuffered, so we don't get any on-screem flimmering.
		glCapabilities.setDoubleBuffered( true );
		//Tell the context that we want it to be hardware accelerated aswell.
		glCapabilities.setHardwareAccelerated( true );

		//Create the JPanel where our opengl stuff will be drawn on.
		GLJPanel panel = new GLJPanel( glCapabilities );
		
		//Add listeners...
		panel.addGLEventListener( this );
		panel.addMouseListener( this );
		panel.addMouseMotionListener( this );
		panel.addMouseWheelListener( this );

		add( panel, BorderLayout.CENTER );

		//Add an animator our panel, which will start the rendering loop, repeadetely calling the display() function of our panel.
		Animator animator = new Animator( panel );
		animator.start();

		this.setSize( new Dimension( iWidth, iHeight ) );
		width = iWidth;
		height = iHeight;

		JTextField textField = new JTextField();
		textField.setText( "TESTER" );
		textField.setSize( new Dimension( 100, 100 ) );
		textField.setPreferredSize( new Dimension( 100, 100 ) );
		textField.setLocation( 100, 100 );
		// textField.setOpaque( false );

		add( textField, BorderLayout.SOUTH );
	}

	@Override
	public void display( GLAutoDrawable arg0 )
	{
		//Get a opengl v2 context.
		GL2 gl2 = arg0.getGL().getGL2();

		//Clear the color buffer, setting all pixel's color on the screen to the specified color.
		gl2.glClear( GL.GL_COLOR_BUFFER_BIT );

		//Load the identity matrix, effectively removing all transformations.
		gl2.glLoadIdentity();

		//Add a translation transformation to the current matrix, effectively moving the ingame 3d coordinate system by the specified amount.
		//Here, we move the system so that the bottom left point is in the middle of the screen.
		gl2.glTranslatef( width / 2, height / 2, 0 );
		//Then we apply the orthographic projection matrix, where we specify exactly what coordinates we're interested in viewing on our screen.
		//Couple this with the factors that change when you scroll in and out on the mousewheel and you get a 'true' zoom feature,
		//Where nothing is scaled but you simply see alot less on the entire screen, AKA zooming.
		gl2.glOrtho( 0, getWidthFactor(), 0, getHeightFactor(), -1, 1 );
		//And remove the translation again.
		gl2.glTranslatef( -width / 2, -height / 2, 0 );

		//Add the panning translation.
		gl2.glTranslated( translation.x, -translation.y, 0 );

		ArrayList<Edge> edges = XMLParser.getEdgeList();

		//We want to start drawing lines.
		gl2.glBegin( GL.GL_LINES );
		
		
		int width = this.getWidth();
		double zoomFactor = ((translation.getX()) - (width-(width*getWidthFactor())/2)/2);
		int xs = XMLParser.edgeSearch(edges, zoomFactor * -1000);
		int xe = XMLParser.edgeSearch(XMLParser.getEdgeListTo(), (zoomFactor - width*getWidthFactor()/2)*-1000);
		xs = xs > 0 ? xs : -xs;
		xe = xe > 0 ? xe : -xe;
		/*
		int height = this.getHeight()+20;
		zoomFactor = ((translation.getY()) - (height-(height*getHeightFactor())/2)/2);
		int ys = XMLParser.edgeSearch(edgesY, zoomFactor * -1000);
		int ye = XMLParser.edgeSearch(XMLParser.getEdgeListYTo(), (zoomFactor - height*getHeightFactor()/2)*-1000);
		ys = ys > 0 ? ys : -ys;
		ye = ye > 0 ? ye : -ye;
		*/
		

		
		for(int i = xs, r = 0, g = 0, b = 0; i < xe-1; i++, r = 0, g = 0, b = 0)
		{
			int c = edges.get(i).getTyp();
			boolean roadesToShow = c < 2;
			if(currentZoomLevel < 3)
				roadesToShow = c < 3;
			else if(currentZoomLevel < 6)
				roadesToShow = c < 4;
			else if(currentZoomLevel < 9)
				roadesToShow = c < 5;
			else if(currentZoomLevel < 12)
				roadesToShow = c < 6;
			else if(currentZoomLevel < 15)
				roadesToShow = c < 7;
			else
				roadesToShow = true;
			
			
			
			if(roadesToShow){
				
		
				if( edges.get(i).getTyp() == 1 ) r = 255;
				else if( edges.get(i).getTyp() < 5 ) b = 255;
				else if( edges.get(i).getTyp() == 8 ) g = 255;	
				
				
				gl2.glColor3f( r, g, b );
				gl2.glVertex2d( edges.get(i).getXFrom() / 1000.0,  edges.get(i).getYFrom() / 1000.0 );
				gl2.glColor3f( r, g, b );
				gl2.glVertex2d( edges.get(i).getXTo() / 1000.0,  edges.get(i).getYTo() / 1000.0 );
			}
		}
		//Stop drawing lines and upload the data to the GPU.
		gl2.glEnd();
	}

	@Override
	public void dispose( GLAutoDrawable arg0 )
	{
		// TODO Auto-generated method stub

	}

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
	public void reshape( GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4 )
	{
		// TODO Auto-generated method stub

	}

	MouseEvent lastMouseEvent;
	MouseEvent originalEvent;

	@Override
	public void mouseDragged( MouseEvent arg0 )
	{
		//Get the x and y pos of the mouse cursor.
		int xPos = arg0.getXOnScreen(), yPos = arg0.getYOnScreen();

		//Get the difference, also taking into consideration the zoom level so we don't end up translation too much.
		double xDiff = ( ( xPos - originalEvent.getXOnScreen() ) * getWidthFactor() ) , yDiff = ( ( yPos - originalEvent.getYOnScreen() ) * getHeightFactor() );

		if( translation == null )
			translation = new Point2D.Double( xDiff, yDiff );
		else
		{
			//Update the translation offset, so we can apply it to the stuff we draw later.
			translation = new Point2D.Double( translation.getX() + xDiff, translation.getY() + yDiff );
		}
 
		try
		{
			//Move the mouse back to it's original position so we can properly calculate the amount of pixels it moved every frame.
			new Robot().mouseMove( originalEvent.getXOnScreen(), originalEvent.getYOnScreen() );
		}
		catch( AWTException exception )
		{
			exception.printStackTrace();
		}

	}

	@Override
	public void mouseMoved( MouseEvent arg0 )
	{
		//Set the current mouse pos.
		curMousePos = arg0.getPoint();
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
		originalEvent = e;
	}

	@Override
	public void mouseReleased( MouseEvent e )
	{
		//Put the mouse back to where it was first pressed.
		try
		{
			new Robot().mouseMove( originalEvent.getXOnScreen(), originalEvent.getYOnScreen() );
		}
		catch( AWTException exception )
		{
			exception.printStackTrace();
		}
	}

	@Override
	public void mouseWheelMoved( MouseWheelEvent e )
	{
		if( e.getUnitsToScroll() < 0 )
		{
			currentZoomLevel += currentZoomLevel < zoomLevel.length-1 ? 1 : 0;
		}
		else
		{
			currentZoomLevel -= currentZoomLevel > 0 ? 1 : 0;
		}
		System.out.println("Zoomlevel: " + (currentZoomLevel+1) + "/30");

		// width += -1 * e.getUnitsToScroll();
		// height += -1 * e.getUnitsToScroll();

		/*
		 * double xDiff = width * e.getUnitsToScroll() / 2; double yDiff = height * e.getUnitsToScroll() / 2;
		 * 
		 * translation = new Point( (int) (translation.getX() + xDiff), (int) (translation.getY() + yDiff) );
		 */

		// System.out.println(scale);
	}

	public static void main( String[] args )
	{
		MapDraw mapDraw = new MapDraw( 800, 600 );
		mapDraw.setVisible( true );
	}
}
