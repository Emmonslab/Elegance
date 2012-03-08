package Admin;


import java.applet.Applet;
import java.awt.*;

import com.sun.j3d.utils.applet.MainFrame; 
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;

import javax.swing.*;
import javax.media.j3d.*;

import javax.vecmath.*;
import java.util.Vector;
import java.sql.*;





public class Display3D  extends Applet  {
  
  String neuron;
  
  String[] neurons;

  int[] continNums;
  
  Color3f black = new Color3f(0.0f,0.0f,0.0f);
  Color3f white = new Color3f(1.0f,1.0f,1.0f);
  Color3f red = new Color3f(1.0f,0.0f,0.0f);
  Color3f green = new Color3f(0.0f,1.0f,0.0f);
  Color3f blue = new Color3f(0.0f,0.0f,1.0f);
  Color3f cyan = new Color3f(0.0f,1.0f,1.0f);
  Color3f magenta = new Color3f(1.0f,0.0f,1.0f);
  Color3f yellow = new Color3f(1.0f,1.0f,0.0f);
  Color3f grey = new Color3f(0.5f,0.5f,0.5f);
  Color3f edge = new Color3f(0.6f,0.4f,0.1f);

  Color3f[] colorArray = {white,red,green,blue,cyan,magenta,yellow,grey,white,red,green,blue,cyan,magenta,yellow,grey,};

  
  int maxZ,	  minZ,	  maxY,	  minY,	  maxX,	  minX, midX,midY,midZ,shiftY;
  double zoomX,zoomY,zoomZ;
	



TransformGroup createText2D( String szText, Color3f color, int nSize, float scale, float trans)
	{
		
		TransformGroup tg = new TransformGroup( );
		Transform3D t3d = new Transform3D( );
		t3d.setScale( scale );
		t3d.setTranslation( new Vector3d( -0.80,trans, 0 ) );

		tg.setTransform( t3d );		

		Text2D text2D = new Text2D( szText, color, "SansSerif", nSize, Font.PLAIN );

		tg.addChild( text2D );
		return tg;
	}

TransformGroup createText2D( String szText, Color3f color, int nSize, float scale, float transX, float transY)
	{
		
		TransformGroup tg = new TransformGroup( );
		Transform3D t3d = new Transform3D( );
		t3d.setScale( scale );
		t3d.setTranslation( new Vector3d( transX,transY, 0 ) );

		tg.setTransform( t3d );		

		Text2D text2D = new Text2D( szText, color, "SansSerif", nSize, Font.PLAIN );

		tg.addChild( text2D );
		return tg;
	}

    public BranchGroup createSceneGraph() {




  // Create the root of the branch graph
  BranchGroup objRoot = new BranchGroup();

        TransformGroup objTransform = new TransformGroup();
        objTransform.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        objTransform.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		objRoot.addChild(objTransform);

	   
	  
       float fsize = 0.020f;
	   float translation  = 0.630f;
	   float level = 0.050f;
       
	   for (int i=0;i<neurons.length;i++)
	   {
		   objRoot.addChild( createText2D( neurons[i], colorArray[i], 400, fsize, translation ) );
		   translation -= level;
     
	   }
	   
       
	   for (int i=0; i<10; i++)
	   {
		   int step = minZ + (maxZ-minZ)/10*i; 
		   String mark = "|  "+ step;
		   //System.out.println(mark);
	       objRoot.addChild( createText2D( mark, edge, 300, 0.015f, (float)((-(maxZ-minZ)/2+(maxZ-minZ)/10*i)*zoomZ), -0.260f ) );
	   }
	   





       
	   Appearance app = new Appearance();
	   ColoringAttributes colorA = new ColoringAttributes();
	   colorA.setColor(edge); 
	   app.setColoringAttributes(colorA);
       
    //   Shape3D l = new Shape3D();   
	//   l.setGeometry(getPAGEdge());
	//   l.setAppearance(app); 
    //   objRoot.addChild(l);

	   Shape3D ll = new Shape3D();   
	   ll.setGeometry(getEdge());
	   ll.setAppearance(app);    
       objTransform.addChild(ll);
	   
	   
	   
       
        
        
		 try
			 {
		      objTransform.addChild(new Neuron2D());
			  
		     }
             catch(SQLException e1){
		        e1.printStackTrace();
		        }

		        catch(java.lang.InstantiationException e2){
		        e2.printStackTrace();
		        }

	            catch(java.lang.IllegalAccessException e3){
		        e3.printStackTrace();
		        }
 
		        catch(ClassNotFoundException e4){
		        e4.printStackTrace();
		        }
		        
	        
        Transform3D xAxis = new Transform3D();
		xAxis.rotZ(Math.PI*0.5d);

		Alpha rotationAlpha = new Alpha(-1, 26000);

	    RotationInterpolator rotator =
	    new RotationInterpolator(rotationAlpha, objTransform, xAxis,(float)Math.PI*0f,(float)Math.PI*2f);
		
		rotator.setSchedulingBounds(new BoundingSphere());
        objRoot.addChild(rotator);
        
/**
        MouseRotate myMouseRotate = new MouseRotate();
        myMouseRotate.setTransformGroup(objTransform);
        myMouseRotate.setSchedulingBounds(new BoundingSphere());
        objRoot.addChild(myMouseRotate);
**/
        MouseTranslate myMouseTranslate = new MouseTranslate();
        myMouseTranslate.setTransformGroup(objTransform);
        myMouseTranslate.setSchedulingBounds(new BoundingSphere());
        objRoot.addChild(myMouseTranslate);

        MouseZoom myMouseZoom = new MouseZoom();
        myMouseZoom.setTransformGroup(objTransform);
        myMouseZoom.setSchedulingBounds(new BoundingSphere());
        objRoot.addChild(myMouseZoom);

  // Let Java 3D perform optimizations on this scene graph.
        objRoot.compile();

     

  return objRoot;
    } // end of CreateSceneGraph method of PAG_3D

	public void setupScale() throws SQLException, ClassNotFoundException, java.lang.InstantiationException, java.lang.IllegalAccessException{
	
  Connection con = null;
  String sql = null;
  Statement st = null;
  ResultSet rs = null;
  String cns = "";
 
	

  try{    
			    Class.forName("com.mysql.jdbc.Driver").newInstance();		
			     con = DriverManager.getConnection ( DatabaseProperties.CONNECTION_STRING,  DatabaseProperties.USERNAME,  DatabaseProperties.PASSWORD );

                
                st = con.createStatement ();
				for (int i=0;i<neurons.length;i++)
				{
                   sql = "select CON_Number from contin where CON_AlternateName='"+neurons[i]+"'";
                   System.out.println(sql);
                   rs = st.executeQuery(sql);
			       while (rs.next())
				    {
			          continNums[i] = rs.getInt(1);
					   if (i==0)
					  {
					   cns = "continNum="+continNums[i];
					  }else{
					   cns = cns+" or continNum="+continNums[i];
					  }
                    }
				    
				    rs.close();
					
				}
				System.out.println(cns);
			   rs = st.executeQuery("select max(x1),min(x1),max(y1),min(y1),max(z1),min(z1) from display2 where "+cns);
			   rs.next();
               maxZ=rs.getInt(5);
	           minZ=rs.getInt(6);
	           maxY=rs.getInt(3);
	           minY=rs.getInt(4);
	           maxX=rs.getInt(1);
	           minX=rs.getInt(2);
			   st.close();

    } catch (SQLException e) {
    throw e;
    } finally {
    if (rs != null) rs.close();
    if (st != null) st.close();
	if (con != null) con.close();
        }


    midX=(maxX+minX)/2;
	midY=(maxY+minY)/2;
	midZ=(maxZ+minZ)/2;

       zoomZ = 0.9000/(maxZ-midZ);
	   zoomX = 0.2000/(maxX-midX);
	   zoomY = 0.2000/(maxY-midY);
	}

    // Create a simple scene and attach it to the virtual universe

    public Display3D() {
    	
    	
    	neuron = JOptionPane.showInputDialog ( null, "Enter the neuron names");
    	
    	neurons =neuron.split(",");
    	System.out.println(neurons.length);

    	continNums = new int[neurons.length];
    	
    	
    	try {
			setupScale();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		 setLayout(new BorderLayout());
	     GraphicsConfiguration config =
	    	    SimpleUniverse.getPreferredConfiguration();
	        Canvas3D canvas3D = new Canvas3D(config);
	        add("Center", canvas3D);

	        BranchGroup scene = createSceneGraph();

	        // SimpleUniverse is a Convenience Utility class
	        SimpleUniverse simpleU = new SimpleUniverse(canvas3D);

	  // This will move the ViewPlatform back a bit so the
	  // objects in the scene can be viewed.
	        simpleU.getViewingPlatform().setNominalViewingTransform();

	        simpleU.addBranchGraph(scene);
		
       
       
        setSize(1000, 1000);
      
		
		
    } // end of PAG_3D (constructor)
    //  The following allows this to be run as an application
    //  as well as an applet
    
   private Geometry getEdge(){
   IndexedLineArray axisLines = new IndexedLineArray(6,
        GeometryArray.COORDINATES, 30);


    axisLines.setCoordinate(0, new Point3f(0.0f, -0.1f, 0.0f));
    axisLines.setCoordinate(1, new Point3f(0.0f, -0.3f, 0.0f));
    axisLines.setCoordinate(2, new Point3f(0.01f, -0.29f, 0.01f));
    axisLines.setCoordinate(3, new Point3f(-0.01f, -0.29f, 0.01f));
    axisLines.setCoordinate(4, new Point3f(0.01f, -0.29f, -0.01f));
    axisLines.setCoordinate(5, new Point3f(-0.01f, -0.29f, -0.01f));


    axisLines.setCoordinateIndex(0, 0);
    axisLines.setCoordinateIndex(1, 1);
    axisLines.setCoordinateIndex(2, 2);
    axisLines.setCoordinateIndex(3, 1);
    axisLines.setCoordinateIndex(4, 3);
    axisLines.setCoordinateIndex(5, 1);
    axisLines.setCoordinateIndex(6, 4);
    axisLines.setCoordinateIndex(7, 1);
    axisLines.setCoordinateIndex(8, 5);
    axisLines.setCoordinateIndex(9, 1);
 
    
	return axisLines;
   }

   private Geometry getPAGEdge(){
   IndexedLineArray axisLines = new IndexedLineArray(4,
        GeometryArray.COORDINATES, 4);


    axisLines.setCoordinate(0, new Point3f(-0.95f, 0.26f, 0.0f));
    axisLines.setCoordinate(1, new Point3f( 0.95f, 0.26f, 0.0f));
    axisLines.setCoordinate(2, new Point3f(-0.95f, -0.26f, 0.0f));
    axisLines.setCoordinate(3, new Point3f( 0.95f, -0.26f, 0.0f));


    axisLines.setCoordinateIndex(0, 0);
    axisLines.setCoordinateIndex(1, 1);
    axisLines.setCoordinateIndex(2, 2);
    axisLines.setCoordinateIndex(3, 3);
   
 
    
	return axisLines;
   }



  

/*
 * Getting Started with the Java 3D API written in Java 3D
 * 
 * This program demonstrates: 1. writing a visual object class In this program,
 * Neuron2D class defines a visual object This particular class extends Shape3D See
 * the text for a discussion. 2. Using LineArray to draw 3D lines.
 */

class Neuron2D extends Shape3D {
// String[] neurons =neurons;
// int[] continNums = continNums;
  ////////////////////////////////////////////
  //
  // create Neuron2D visual object
  //
  public Neuron2D() throws SQLException, ClassNotFoundException, java.lang.InstantiationException, java.lang.IllegalAccessException{

    this.setGeometry(createGeometry());
	LineAttributes la = new LineAttributes();
	la.setLineWidth(1.0f);
	la.setLineAntialiasingEnable(true); 
    Appearance ap = new Appearance(); 
    ap.setLineAttributes(la); 
     
    this.setAppearance(ap);
    
  }

  private Geometry createGeometry() throws SQLException, ClassNotFoundException, java.lang.InstantiationException, java.lang.IllegalAccessException{
    // create line for X Neuron2D
  Vector points = new Vector();
  Vector colors = new Vector();
  Vector cellbodys = new Vector();
 
  Point3d point = new Point3d();
  int cellbody = 0;
  
 
  
  Connection con = null;
  
  PreparedStatement pstmt=null;
  String jsql=null;
  ResultSet rs = null;


int x,y,z;
double xx,yy,zz;



      
	   
	try{
    Class.forName("com.mysql.jdbc.Driver").newInstance();		
	 con = DriverManager.getConnection ( DatabaseProperties.CONNECTION_STRING,  DatabaseProperties.USERNAME,  DatabaseProperties.PASSWORD );
	for(int i=0;i<continNums.length;i++)
	   {
	   


	   Color3f color = new Color3f(red);
	   color.set(colorArray[i]);
	  




	   jsql = "select x1,y1,z1,x2,y2,z2,cellbody1 from display2 where continNum="+continNums[i];
	   System.out.println(jsql);
       pstmt = con.prepareStatement(jsql);
       rs = pstmt.executeQuery();
	   while(rs.next())
     	{
     	 
		 
		 x = rs.getInt(1);
	     y = rs.getInt(2);
		 z = rs.getInt(3);
		 cellbody = rs.getInt(7);
		 
		 
		 xx = (double)((z-midZ)*zoomZ);
		 yy = (double)((y-midY)*zoomY);
		 zz = (double)((x-midX)*zoomX);
         
	//	 if (yy>0.200d) yy = 0.200d;
	//	 if (yy<-0.200d) yy = -0.200d;
	//	 if (zz>0.200d) zz = 0.200d;
	//	 if (zz<-0.200d) zz = -0.200d;
		 
		 point = new Point3d( xx, yy, zz );
         points.add(point);
         colors.add(color);
         cellbodys.add(cellbody);
         
         
         
    	
         
    	 x = rs.getInt(4);
	     y = rs.getInt(5);
		 z = rs.getInt(6);
		 
		 
		 
		 xx = (double)((z-midZ)*zoomZ);
		 yy = (double)((y-midY)*zoomY);
		 zz = (double)((x-midX)*zoomX);
         
	//	 if (yy>0.200d) yy = 0.200d;
	//	 if (yy<-0.200d) yy = -0.200d;
	//	 if (zz>0.200d) zz = 0.200d;
	//	 if (zz<-0.200d) zz = -0.200d;
		 
		 point = new Point3d( xx, yy, zz );
         points.add(point);
         colors.add(color);
         cellbodys.add(cellbody);

		 
		
	    }// end of while
        rs.close();
		pstmt.close();


	   
 
		
	   } //end of for
       
	   } // end of try
	   catch (SQLException e) {
       throw e;
       } finally {
        if (rs != null) rs.close();
        if (pstmt != null) pstmt.close();
	    if (con != null) con.close();
      }








    

    



    System.out.println("pointsize "+points.size());

    LineArray lines = new LineArray(points.size(),LineArray.COORDINATES|LineArray.COLOR_3);

   
	for (int i=0; i<points.size(); i++)
	{
       //System.out.println(i);
	   lines.setCoordinate( i, (Point3d)points.elementAt(i) );
	   lines.setColor( i, (Color3f)colors.elementAt(i) );
	 //    lines.set
	}
	
   


   return lines;

  } // end of Neuron2D createGeometry()

  

} // end of class Neuron2D 






public static void main(String args[]) {
    
	 Frame frame = new MainFrame(new Display3D(), 1000, 1000);
   
}


} // end of class PAG_3D



