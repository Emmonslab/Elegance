
//    
//  CLASS    
//    AnnotationLine    -  3D line used for annotation & diagrams    
//    
//  DESCRIPTION    
//    This class creates a 3D, unlighted line between two 3D coordinates.    
//    The line's width and color can be controlled.    
//    
//  SEE ALSO    
//    AnnotationArrow    
//    
//  AUTHOR    
//    David R. Nadeau / San Diego Supercomputer Center    
//    
//    
import java.awt.*;   
import java.awt.event.*;   
import javax.media.j3d.*;   
import javax.vecmath.*;   
import com.sun.j3d.utils.geometry.*;   
   
class AnnotationLine   
    extends Primitive   
{   
    // Parameters    
    private float   lineWidth = 1;   
    private Color3f lineColor = new Color3f( 1.0f, 1.0f, 1.0f );   
   
    // 3D nodes    
    private Shape3D shape = null;   
    private LineAttributes lineAttributes = null;   
    private ColoringAttributes coloringAttributes = null;   
    private LineArray line = null;   
    protected Appearance mainAppearance = null;   
   
   
    //    
    //  Construct a straight line    
    //    
    public AnnotationLine( float x2, float y2, float z2 )   
    {   
        //    origin            to given coordinate    
        this( 0.0f, 0.0f, 0.0f, x2, y2, z2 );   
    }   
   
    public AnnotationLine( float x, float y, float z,   
        float x2, float y2, float z2 )   
    {   
        float[] coord    = new float[3];   
        float[] texcoord = new float[2];   
   
        // Build a shape    
        shape = new Shape3D( );   
        shape.setCapability( Shape3D.ALLOW_APPEARANCE_WRITE );   
   
        // Create geometry for a 2-vertex straight line    
        line = new LineArray( 2,   
            GeometryArray.COORDINATES | GeometryArray.TEXTURE_COORDINATE_2 );   
        line.setCapability( GeometryArray.ALLOW_COLOR_WRITE );   
   
        // Starting point    
        coord[0] = x;   
        coord[1] = y;   
        coord[2] = z;   
        texcoord[0] = 0.0f;   
        texcoord[1] = 0.0f;   
        line.setCoordinate( 0, coord );   
        line.setTextureCoordinate( 0, texcoord );   
   
        // Ending point    
        coord[0] = x2;   
        coord[1] = y2;   
        coord[2] = z2;   
        texcoord[0] = 1.0f;   
        texcoord[1] = 0.0f;   
        line.setCoordinate( 1, coord );   
        line.setTextureCoordinate( 1, texcoord );   
   
        shape.setGeometry( line );   
   
        // Create an appearance    
        mainAppearance = new Appearance( );   
        mainAppearance.setCapability(   
            Appearance.ALLOW_LINE_ATTRIBUTES_WRITE );   
        mainAppearance.setCapability(   
            Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE );   
   
        lineAttributes = new LineAttributes( );   
        lineAttributes.setLineWidth( lineWidth );   
        mainAppearance.setLineAttributes( lineAttributes );   
   
        coloringAttributes = new ColoringAttributes( );   
        coloringAttributes.setColor( lineColor );   
        coloringAttributes.setShadeModel( ColoringAttributes.SHADE_FLAT );   
        mainAppearance.setColoringAttributes( coloringAttributes );   
   
        addChild( shape );   
    }   
   
   
    //    
    //  Control the line width    
    //    
    public float getLineWidth( )   
    {   
        return lineWidth;   
    }   
   
    public void setLineWidth( float width )   
    {   
        lineWidth = width;   
        lineAttributes.setLineWidth( lineWidth );   
        mainAppearance.setLineAttributes( lineAttributes );   
        shape.setAppearance( mainAppearance );   
    }   
   
   
    //    
    //  Control the line color    
    //    
    public void getLineColor( Color3f color )   
    {   
        lineColor.get( color );   
    }   
   
    public void getLineColor( float[] color )   
    {   
        lineColor.get( color );   
    }   
   
    public void setLineColor( Color3f color )   
    {   
        lineColor.set( color );   
        coloringAttributes.setColor( lineColor );   
        mainAppearance.setColoringAttributes( coloringAttributes );   
        shape.setAppearance( mainAppearance );   
    }   
   
    public void setLineColor( float r, float g, float b )   
    {   
        lineColor.set( r, g, b );   
        coloringAttributes.setColor( lineColor );   
        mainAppearance.setColoringAttributes( coloringAttributes );   
        shape.setAppearance( mainAppearance );   
    }   
   
    public void setLineColor( float[] color )   
    {   
        lineColor.set( color );   
        coloringAttributes.setColor( lineColor );   
        mainAppearance.setColoringAttributes( coloringAttributes );   
        shape.setAppearance( mainAppearance );   
    }   
   
   
    //    
    //  Control the appearance    
    //    
    public void setAppearance( Appearance app )   
    {   
        mainAppearance = app;   
        mainAppearance.setCapability(   
            Appearance.ALLOW_LINE_ATTRIBUTES_WRITE );   
        mainAppearance.setCapability(   
            Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE );   
        mainAppearance.setLineAttributes( lineAttributes );   
        mainAppearance.setColoringAttributes( coloringAttributes );   
        shape.setAppearance( mainAppearance );   
    }   
   
   
    //    
    //  Provide info on the shape and geometry    
    //    
    public Shape3D getShape( int partid )   
    {   
        return shape;   
    }   
   
    public int getNumTriangles( )   
    {   
        return 0;   
    }   
   
    public int getNumVertices( )   
    {   
        return 2;   
    }

	@Override
	public Appearance getAppearance(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}   
}   

