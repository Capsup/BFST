package MapDraw;

import javax.media.opengl.GL2;

import Graph.Edge;

public class GraphicsPrefs {
	
	GL2 gl;
	
	public GraphicsPrefs(GL2 gl) {
		this.gl = gl;
	}
	
	public int[] getTypesAtCurrentZoom() {
		int zoomIndex = ZoomLevel.getInstance().getZoomIndex();
		
		if(zoomIndex < 3)
			return new int[] {0,1,2,3,4,30,31,40,41};
		else if(zoomIndex < 6)
			return new int[] {0,1,2,3,4,30,31,40,41};
		else if(zoomIndex < 12)
			return new int[] {0,1,2,3,4,5,30,31,40,41};
		else if(zoomIndex < 15)
			return new int[] {0,1,2,3,4,5,6,30,31,40,41};
		else if(zoomIndex < 16)
			return new int[] {0,1,2,3,4,5,6,7,30,31,40,41};
		else {
			int[] a = new int[100];
			for(int i=0; i<42; i++) {
				a[i] = i;
			}
			return a;
		}
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
			return 42;
		
		//return 100;
	}
	
	public void setLineWidth(Edge e) {
		int roadType = e.getTyp();
		int zoomIndex = ZoomLevel.getInstance().getZoomIndex();
		
		if(roadType == 1 || roadType == 31 || roadType == 41) {
			gl.glLineWidth(4f);
		} else if(roadType == 2) {
			gl.glLineWidth(3f);
		} else if(roadType == 3) {
			gl.glLineWidth(2f);
		} else if(roadType == 5) {
			gl.glLineWidth(2f);
			if(zoomIndex > 11) 
				gl.glLineWidth(4f);
		} else if(roadType == 5) {
			gl.glLineWidth(1.7f);
		} else if(roadType == 6) {
			gl.glLineWidth(1.7f);
		} else if(roadType == 8) {
			gl.glLineWidth(1.7f);
		} else {
			gl.glLineWidth(1.5f);
		}
		
		
		
		
//		if(roadType == 1) {
//			gl.glLineWidth(5f);
//			
//		} else if(roadType == 2) {
//			gl.glLineWidth(3f);
//			
//		} else if(roadType < 5) {
//			gl.glLineWidth(2f);
//			
//		} else if(roadType < 8) {
//			gl.glLineWidth(1.7f);
//			
//		} else {
//			
//			gl.glLineWidth(1.5f);
//		}
	}
	
	public boolean hasCenterLine(Edge e) {
		int roadType = e.getTyp();
		int zoomIndex = ZoomLevel.getInstance().getZoomIndex();
		
		if(roadType == 1 || roadType == 31 || roadType == 41) {
			return true;
		} else if(roadType == 2) {
			return true;
		} else if(roadType == 3) {
			return true;
		} else if(roadType == 5) {
			if(zoomIndex > 11) {
				return true;
			} else {
				return false;
			}
		} else if(roadType < 8) {
			return false;
		} else {
			return false;
		}
	}
	
	public void setCenterLineWidth(Edge e) {
		int roadType = e.getTyp();
		
		if(roadType == 1 || roadType == 31 || roadType == 41) {
			gl.glLineWidth(1.4f);
		} else if(roadType == 2) {
			gl.glLineWidth(1.4f);
		} else if(roadType == 3) {
			gl.glLineWidth(0.8f);
		} else if(roadType == 5) {
			if(ZoomLevel.getInstance().getZoomIndex() > 11) {
				gl.glLineWidth(1.4f);
			}
			gl.glLineWidth(1.3f);
		} else if(roadType < 8) {
			gl.glLineWidth(0.8f);
		} else {
			gl.glLineWidth(1f);
			System.out.println("wat");
		}
		
	}
	
	public void setLineWidth(float f) {
		gl.glLineWidth(f);
	}
	
	public float[] getLineColor(Edge e) {
		float r=0, g=0, b=0;
		int roadType = e.getTyp();
		
		if( roadType == 1 || roadType == 31 || roadType == 41 ) 
			r = 255f;
		
		else if( roadType < 5) {
			r = 210f;
			g = 105f;
			b = 30f;
		}

		else if( roadType < 6) {
			r = 131f;
			g = 139f;
			b = 131f;
		}

		else if( roadType < 8) {
			r = 131f;
			g = 139f;
			b = 131f;
		}

		else if( roadType == 8 ) 
			g = 255f;

		return new float[] {r,g,b};
	}

	public float[] getLineCenterColor(Edge e) {
		float r=0, g=0, b=0;
		int roadType = e.getTyp();

		if( roadType == 1 || roadType == 31 || roadType == 41 ) {
			r = 255;
			g = 215;
			b = 0;
		} else if( roadType == 2) {
			r = 255;
			g = 165;
			b = 0;
		} else if( roadType == 3) {
			r = 255;
			g = 165;
			b = 0;
		} else if( roadType == 4) {
			r = 255;
			g = 255;
			b = 255;
		} else if( roadType < 8) {
			r = 255;
			g = 255;
			b = 255;
		}

		return new float[] {r,g,b};
	}
}
