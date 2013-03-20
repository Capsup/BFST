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
import java.util.ArrayList;
import XMLParser.*;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLJPanel;
import javax.media.opengl.glu.GLU;
import javax.swing.JTextField;

import XMLParser.Edge;
import XMLParser.XMLParser;

import com.jogamp.opengl.util.Animator;

public class MapDraw extends Frame implements GLEventListener, MouseListener, MouseMotionListener, MouseWheelListener 
{	
	private int width;
	private int height;
	private Point translation;
	private double scale = 1;
	private Point curMousePos;
	
	public MapDraw( int iWidth, int iHeight )
	{
		//translation = new Point(0,0);
		translation = new Point( -266, 5940 );
		curMousePos = new Point(0,0);
	    GLCapabilities glCapabilities = new GLCapabilities( GLProfile.getDefault() );
	    
	    XMLParser.makeDataSet();
	    
	    glCapabilities.setDoubleBuffered( true );
	    glCapabilities.setHardwareAccelerated( true );
	    
	    GLJPanel panel = new GLJPanel( glCapabilities );
	    panel.addGLEventListener( this );
	    //panel.addMouseListener( this );
	    panel.addMouseListener( this );
	    panel.addMouseMotionListener( this );
	    panel.addMouseWheelListener( this );
	    //panel.setDefaultCloseOperation( WindowClosingMode.DISPOSE_ON_CLOSE );
	    
	    add( panel, BorderLayout.CENTER );
	    
	    Animator animator = new Animator(panel);
	    animator.start();
	    
	    this.setSize( new Dimension(iWidth, iHeight) );
	    width = iWidth;
	    height = iHeight;
	    
	    JTextField textField = new JTextField();
	    textField.setText( "TESTER" );
	    textField.setSize( new Dimension(100,100) );
	    textField.setPreferredSize( new Dimension(100,100 )	);
	    textField.setLocation( 100, 100 );
	    //textField.setOpaque( false );
	    
	    add( textField, BorderLayout.SOUTH );
	}

	@Override
    public void display( GLAutoDrawable arg0 )
    {
		GL2 gl2 = arg0.getGL().getGL2();
		
		gl2.glClear( GL.GL_COLOR_BUFFER_BIT );
		
		gl2.glLoadIdentity();
		
		//gl2.glTranslatef( -(curMousePos.x - width / 2), -(curMousePos.y - height / 2), 0 );
		
		if( scale > 1 )
			gl2.glScaled( scale, scale, 1 );
		
		//gl2.glTranslatef( (curMousePos.x + width / 2), (curMousePos.y + height / 2), 0 );
		
		gl2.glTranslatef( translation.x, -translation.y, 0 );
	
		
ArrayList<Edge> edges = XMLParser.getEdgeList();
		
		gl2.glBegin( GL.GL_LINES);
			float r = 0, g = 0, b = 0;
			float x, y;
			Node node;
			for(Edge e : edges){
				r = 0; g = 0; b = 0;
				if(e.getTyp() == 1) r = 255;
				else if(e.getTyp() < 5) b = 255;
				else if(e.getTyp() == 8) g = 255;
				gl2.glColor3f( r, g ,b);
				gl2.glVertex2d( e.getXFrom()/1000.f, (float) e.getYFrom()/1000.f );
				gl2.glColor3f( r, g, b );
				gl2.glVertex2d( e.getXTo()/1000.f, (float) e.getYTo()/1000.f );
			}
		gl2.glEnd();
		
		/*gl2.glBegin( GL.GL_TRIANGLES );
			gl2.glColor3f( 255, 0, 0 );
			gl2.glVertex2f( 0.0f, 0.0f );
			gl2.glColor3f( 0, 255, 0 );
			gl2.glVertex2f( 800f, 0.0f );
			gl2.glColor3f( 0, 0, 255 );
			gl2.glVertex2f( 400f, 600f );
		gl2.glEnd();*/
		
		/*gl2.glLineWidth( 10f );
		gl2.glBegin( GL.GL_LINES );
			
			for( Edge edge : XMLParser.getEdgeList() )
			{
			//	Node testNode = XMLParser.nodeSearch( edge.getToNodeID() );
		
		//Edge edge = XMLParser.getEdgeList().get( 1 );
		//Node fNode = XMLParser.nodeSearch( edge.getToNodeID() );
		//Node tNode = XMLParser.nodeSearch( edge.getFromNodeID() );
		
				gl2.glColor3f( 255, 255, 0 );
				gl2.glVertex2f( XMLParser.nodeSearch( edge.getFromNodeID() ).getXCoord(), XMLParser.nodeSearch( edge.getFromNodeID() ).getYCoord() );
				//gl2.glVertex2f( fNode.getXCoord(), fNode.getYCoord() );
				gl2.glColor3f( 255, 0, 255 );
				//gl2.glVertex2f( tNode.getXCoord(), tNode.getYCoord() );
				gl2.glVertex2f( XMLParser.nodeSearch( edge.getToNodeID() ).getXCoord(), XMLParser.nodeSearch( edge.getToNodeID() ).getYCoord() );
			}
		gl2.glEnd();*/
    }

	@Override
    public void dispose( GLAutoDrawable arg0 )
    {
	    // TODO Auto-generated method stub
	    
    }

	@Override
    public void init( GLAutoDrawable arg0 )
    {
		GL2 gl = arg0.getGL().getGL2();
		
		gl.glClearColor( 255, 255, 255, 0 );
		gl.glMatrixMode( gl.GL_PROJECTION );
		gl.glLoadIdentity();
		
		GLU glu = new GLU();
		glu.gluOrtho2D( 0.0f, width, 0.0f, height );
		//glu.gluOrtho2D( -width/2, -height/2, width/2, height/2 );
		//glu.gluPerspective( 50.0 * scale, (float) width / (float) height, 1, 1000 );
		
		gl.glMatrixMode( gl.GL_MODELVIEW );
		gl.glLoadIdentity();
		
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
	    int xPos = arg0.getXOnScreen(), yPos = arg0.getYOnScreen();
	    
	    int xDiff = xPos - originalEvent.getXOnScreen(), yDiff = yPos - originalEvent.getYOnScreen();
	    
	    if( translation == null )
	    	translation = new Point( xDiff, yDiff );
	    else {
			translation = new Point( translation.x + xDiff, translation.y + yDiff );
		}
	    
	    try
        {
	        new Robot().mouseMove( originalEvent.getXOnScreen(), originalEvent.getYOnScreen() );
        }
        catch( AWTException exception )
        {
	        exception.printStackTrace();
        }
	    
	    System.out.println(translation);
    }

	@Override
    public void mouseMoved( MouseEvent arg0 )
    {
	    // TODO Auto-generated method stub
	    curMousePos = arg0.getLocationOnScreen();
    }

	@Override
    public void mouseClicked( MouseEvent e )
    {
	    // TODO Auto-generated method stub
    }

	@Override
    public void mouseEntered( MouseEvent e )
    {
	    // TODO Auto-generated method stub
	    
    }

	@Override
    public void mouseExited( MouseEvent e )
    {
	    // TODO Auto-generated method stub
	    
    }

	@Override
    public void mousePressed( MouseEvent e )
    {
	    // TODO Auto-generated method stub
	    originalEvent = e;
    }

	@Override
    public void mouseReleased( MouseEvent e )
    {
	    // TODO Auto-generated method stub
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
	    scale += ( -1 * e.getUnitsToScroll() );
	    
	    /*double xDiff = width * e.getUnitsToScroll() / 2;
	    double yDiff = height * e.getUnitsToScroll() / 2;
	    
	    translation = new Point( (int) (translation.getX() + xDiff), (int) (translation.getY() + yDiff) );*/
	    
	    
	    
	    System.out.println(scale);
    }
	
	public static void main( String[] args )
	{
		MapDraw mapDraw = new MapDraw( 800, 600 );
		mapDraw.setVisible( true );	
		
		
	}
}
