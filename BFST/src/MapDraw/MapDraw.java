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

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLJPanel;
import javax.media.opengl.glu.GLU;
import javax.swing.JTextField;

import com.jogamp.opengl.util.Animator;

public class MapDraw extends Frame implements GLEventListener, MouseListener, MouseMotionListener 
{	
	private int width;
	private int height;
	private Point translation;
	
	public MapDraw( int iWidth, int iHeight )
	{
	    GLCapabilities glCapabilities = new GLCapabilities( GLProfile.getDefault() );
	    
	    glCapabilities.setDoubleBuffered( true );
	    glCapabilities.setHardwareAccelerated( true );
	    
	    GLJPanel panel = new GLJPanel( glCapabilities );
	    panel.addGLEventListener( this );
	    //panel.addMouseListener( this );
	    panel.addMouseListener( this );
	    panel.addMouseMotionListener( this );
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
		if( translation != null )
			gl2.glTranslatef( translation.x, -translation.y, 0 );
		
		gl2.glBegin( GL.GL_TRIANGLES );
			gl2.glColor3f( 255, 0, 0 );
			gl2.glVertex2f( 0.0f, 0.0f );
			gl2.glColor3f( 0, 255, 0 );
			gl2.glVertex2f( 800f, 0.0f );
			gl2.glColor3f( 0, 0, 255 );
			gl2.glVertex2f( 400f, 600f );
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
		GL2 gl = arg0.getGL().getGL2();
		
		gl.glClearColor( 0, 0, 0, 0 );
		gl.glMatrixMode( gl.GL_PROJECTION );
		gl.glLoadIdentity();
		
		GLU glu = new GLU();
		glu.gluOrtho2D( 0.0f, width, 0.0f, height );
		
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
	
	public static void main( String[] args )
	{
		MapDraw mapDraw = new MapDraw( 800, 600 );
		mapDraw.setVisible( true );	
		
		
	}
}
