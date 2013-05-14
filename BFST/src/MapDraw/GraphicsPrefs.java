package MapDraw;

import javax.media.opengl.GL2;

import Graph.Edge;

import java.awt.print.Printable;
import java.util.ArrayList;

public class GraphicsPrefs {
	
	GL2 gl;
	
	//An array containing the types of roads that are to be showed at the different zoom levels
	private int[] roadTypeToZoomLevel = new int[]{
			0,	//0
			0,	//1
			0,	//2
			0,	//3
			0,	//4
			6,	//5
			12,	//6
			15,	//7
			16,	//8
			16,	//9
			16,	//10
			16,	//11
			16,	//12
			16,	//13
			16,	//14
			16,	//15
			16,	//16
			16,	//17
			16,	//18
			16,	//19
			16,	//20
			16,	//21
			16,	//22
			16,	//23
			16,	//24
			16,	//25
			16,	//26
			16,	//27
			16,	//28
			16,	//29
			0,	//30
			0,	//31
			16,	//32
			16,	//33
			16,	//34
			16,	//35
			16,	//36
			16,	//37
			16,	//38
			16,	//39
			0,	//40
			0,	//41
	};
	
	
	public GraphicsPrefs(GL2 gl) {
		this.gl = gl;
		
		for(int i=0; i<roadTypeToZoomLevel.length; i++)
			roadTypeToZoomLevel[i] -= 1;
	}
	
	//Returns an int[] containing the numbers of all allowed road types at the given zoom level
	public int[] getTypesAtCurrentZoom(double currentZoom) {
		
		int zoomIndex = ZoomLevel.getInstance().findIndex(currentZoom);
		
		//System.out.println(zoomIndex);
		
		ArrayList<Integer> intList = new ArrayList<Integer>();
		
		for(int i=0; i<roadTypeToZoomLevel.length; i++)
		{
			if(roadTypeToZoomLevel[i] <= zoomIndex)
				intList.add(i);
		}
		
		int[] intArray = new int[intList.size()];
		
		for(int i=0; i<intArray.length; i++)
			intArray[i] = intList.get(i); 
		
		return intArray;
		
		
		/*if(zoomIndex < 3)
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
		}*/
	}
	
//	public int getMaxTypeAtCurrentZoom() {
//		int zoomIndex = ZoomLevel.getInstance().getZoomIndex();
//		
//		if(zoomIndex < 3)
//			return 5;
//		else if(zoomIndex < 6)
//			return 5;
//		else if(zoomIndex < 12)
//			return 6;
//		else if(zoomIndex < 15)
//			return 7;
//		else if(zoomIndex < 16)
//			return 8;
//		else
//			return 42;
//		
//		//return 100;
//	}
	
	//Checks what type of edge has been given, then sets the gl.glLineWidth to an appropriate size
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
			gl.glLineWidth(3f);
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
	
	//Checks what type of edge has been given, then returns true if the roadtype has a center-line
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
		} else if(roadType == 8) {
			return true;
		} else if(roadType < 8) {
			return false;
		} else {
			return false;
		}
	}
	
	//Checks what type of edge has been given, then sets the gl.glLineWidth to an appropriate size
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
		} else if(roadType == 8) { 
			gl.glLineWidth(1.2f);
		} else if(roadType < 8) {
			gl.glLineWidth(0.8f);
		} else {
			gl.glLineWidth(1f);
			System.out.println("wat");
		}
		
	}
	
	//Sets the lineWidth to a user-given value. 
	//This is useful when we want to paint over lines, when we are showing a route, for example.
	public void setLineWidth(float f) {
		gl.glLineWidth(f);
	}
	
	//Checks what type of edge has been given, then returns an array with r, g and b values for the given type.
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
			g = 100f;
			b = 100f;
			//g = 139f;
			//b = 131f;
		}

		else if( roadType < 8) {
			r = 131f;
			g = 100f;
			b = 100f;
			//g = 139f;
			//b = 131f;
		}

		else if( roadType == 8 ) {
			r = 80f;
			g = 80f;
			b = 80f;
		}
			
		return new float[] {r,g,b};
	}

	//Checks what type of edge has been given, then returns an array with r, g and b values for the given type's centerLine.
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
		} else if( roadType == 8) {
			r = 0;
			g = 255;
			b = 0;
		} else if( roadType < 8) {
			r = 255;
			g = 255;
			b = 255;
		}

		return new float[] {r,g,b};
	}
	
	//Returns the allowed zoom index for the given roadType.
	public int getAllowedZoomIndex(int roadType)
	{
		return roadTypeToZoomLevel[roadType];
	}
}
