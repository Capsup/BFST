package MapDraw;

import javax.media.opengl.GL2;

import Graph.Edge;

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
		
		if(roadType == 1) {
			gl.glLineWidth(5f);
			
		} else if(roadType == 2) {
			gl.glLineWidth(3f);
			
		} else if(roadType < 5) {
			gl.glLineWidth(2f);
			
		} else if(roadType > 2 && roadType < 8) {
			gl.glLineWidth(1.7f);
			
		} else {
			
			gl.glLineWidth(1.5f);
		}
	}
	
	public void setCenterLineWidth(Edge e) {
		int roadType = e.getTyp();
		
		if(roadType == 1) {
			gl.glLineWidth(1.4f);
		} else if(roadType == 2) {
			gl.glLineWidth(1.4f);
		} else if(roadType == 3) {
			gl.glLineWidth(0.8f);
		} else {
			gl.glLineWidth(0f);
		}
		
	}
	
	public void setLineWidth(float f) {
		gl.glLineWidth(f);
	}
	
	public float[] getLineColor(Edge e) {
		float r=0, g=0, b=0;
		
		if( e.getTyp() == 1 ) 
			r = 255f;
		
		else if( e.getTyp() < 5) {
			r = 210f;
			g = 105f;
			b = 30f;
		}
		
		else if( e.getTyp() < 8) {
			r = 131f;
			g = 139f;
			b = 131f;
		}

		else if( e.getTyp() == 8 ) 
			g = 255f;

		return new float[] {r,g,b};
	}

	public float[] getLineCenterColor(Edge e) {
		float r=0, g=0, b=0;

		if( e.getTyp() == 1 ) {
			r = 255;
			g = 215;
			b = 0;
		} else if( e.getTyp() == 2) {
			r = 255;
			g = 165;
			b = 0;
		} else if( e.getTyp() == 3) {
			r = 255;
			g = 165;
			b = 0;
		}

		return new float[] {r,g,b};
	}
}
