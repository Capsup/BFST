package MapDraw;

import javax.media.opengl.GL2;

import XMLParser.Edge;

public class GraphicsPrefs {
	
	GL2 gl;
	
	public GraphicsPrefs(GL2 gl) {
		this.gl = gl;
	}
	
	public int getMaxTypeAtCurrentZoom() {
		int zoomIndex = ZoomLevel.getInstance().getZoomIndex();
		
		if(zoomIndex < 3)
			return 5;
		else if(zoomIndex < 6)
			return 5;
		else if(zoomIndex < 12)
			return 6;
		else if(zoomIndex < 15)
			return 7;
		else if(zoomIndex < 16)
			return 8;
		else
			return 11;
	}
	
	public void setLineWidth(Edge e) {
		int roadType = e.getTyp();
		
		if(roadType == 1) 
		{
			gl.glLineWidth(5f);
		} else if(roadType > 1 && roadType < 8) {
			gl.glLineWidth(1.7f);
		} else {
			gl.glLineWidth(1.5f);
		}
	}
	
	public void setLineWidth(float f) {
		gl.glLineWidth(f);
	}
	
	public int[] getLineColor(Edge e) {
		int r=0, g=0, b=0;
		
		if( e.getTyp() == 1 ) 
			r = 255;
		else if( e.getTyp() < 5 ) 
			b = 255;
		else if( e.getTyp() == 8 ) 
			g = 255;
		
		return new int[] {r,g,b};
	}

}
