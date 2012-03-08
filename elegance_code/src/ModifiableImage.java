import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;

import javax.media.jai.InterpolationNearest;
import javax.media.jai.JAI;
import javax.media.jai.LookupTableJAI;
import javax.media.jai.PlanarImage;

import javax.swing.JLabel;
import javax.swing.SwingConstants;


/**
 * This is the most important class of the Image Stitching module. It contains a
 * PlanarImage source and can return the source after being able to provide a number of
 * types of modifications. These modifications include zoom, translation. brightness,
 * contrast etc.
 *
 * @author zndavid
 * @version 1.0
 *
 * @since
 */
public class ModifiableImage
{
	/**
	 * A flag stating whether a memory intensive process is currently being performed at
	 * present.
	 */
	public static boolean memoryIntensive = false;

	/** The name of the source image file */
	public String       fileName       = "";
	private PlanarImage originalSource;

	//private  PlanarImage      source;
	private PlanarImage source                       = null;
	private int         originX                      = 0;
	private int         originY                      = 0;
	private int         last_x                       = 0;
	private int         last_y                       = 0;
	private int         componentWidth               = 100;
	private int         componentHeight              = 100;
	private int         parentWidth;
	private int         parentHeight;
	private boolean     brightnessAndContrastEnabled = false;
	private int         brightness                   = 0;
	private float       current_zoom                 = 0.2f;
	private double      current_rotation             = 0.0;
	private int         contrast                     = 0;
	protected byte []   lutData;
	JLabel              odometer                     = null;
	private float       temp_zoom;
	private double      temp_rotation;

	/**
	 * The default constructor. Creates an object with a null source.
	 */
	public ModifiableImage (  )
	{
		source             = null;
		originalSource     = null;
		lutData            = new byte[ 256 ];

		for ( int i = 0; i < 256; i++ )
		{
			lutData [ i ] = ( byte ) i;
		}

		setOrigin ( 0, 0 );
		setBrightnessAndContrastEnabled ( true );
	}

	/**
	 * Constructor for the ModifiableImage object. Creates an object with the specified
	 * PlanarImage as the source and the specified filename
	 *
	 * @param im The original image as a PlanarImage object
	 * @param newFileName The filename of the image
	 */
	public ModifiableImage ( 
	    PlanarImage im,
	    String      newFileName
	 )
	{
		fileName           = newFileName;
		source             = im;
		originalSource     = source;
		initialize (  );
		lutData = new byte[ 256 ];

		for ( int i = 0; i < 256; i++ )
		{
			lutData [ i ] = ( byte ) i;
		}

		setOrigin ( 0, 0 );
		setBrightnessAndContrastEnabled ( true );
		zoomAndRotate ( current_zoom, ( float ) current_rotation );
	}

	/**
	 * Initializes the width and height of the object.
	 */
	public void initialize (  )
	{
		if ( source != null )
		{
			componentWidth      = source.getWidth (  );
			componentHeight     = source.getHeight (  );
		}
	}

	/**
	 * Changes the source image to a new PlanarImage.
	 *
	 * @param im The new image
	 */
	public void set ( PlanarImage im )
	{
		//source = im;
		originalSource = im;
		initialize (  );
		zoomAndRotate ( current_zoom, ( float ) current_rotation );

		//repaint();
	}

	/**
	 * Changes the source image to a new PlanarImage and also sets the orogin of the new
	 * image.
	 *
	 * @param im The new image
	 * @param x X-cordinate of the origin
	 * @param y Y-coordinate of the origin
	 */
	public void set ( 
	    PlanarImage im,
	    int         x,
	    int         y
	 )
	{
		//source = im;
		originalSource = im;
		initialize (  );
		setOrigin ( x, y );
		zoomAndRotate ( current_zoom, ( float ) current_rotation );
	}

	/**
	 * Returns the image
	 *
	 * @return The image
	 */
	public PlanarImage getImage (  )
	{
		return source;
	}

	/**
	 * Gets the odometer associated with this object. The odometer is the JLabel object
	 * with the coordinates of the current location of the mouse as the text in the
	 * JLabel.
	 *
	 * @return The odometer
	 */
	public final JLabel getOdometer (  )
	{
		if ( odometer == null )
		{
			odometer = new JLabel(  );
			odometer.setVerticalAlignment ( SwingConstants.CENTER );
			odometer.setHorizontalAlignment ( SwingConstants.LEFT );
			odometer.setText ( " " );
		}

		return odometer;
	}

	/**
	 * Provides panning
	 *
	 * @param x The new origin value
	 * @param y The new origin value
	 */
	public final void setOrigin ( 
	    int x,
	    int y
	 )
	{
		originX     = x;
		originY     = y;
	}

	/**
	 * Gets the xOrigin attribute of the ImageDisplay object
	 *
	 * @return The xOrigin value
	 */
	public int getXOrigin (  )
	{
		return originX;
	}

	/**
	 * Gets the yOrigin attribute of the ImageDisplay object
	 *
	 * @return The yOrigin value
	 */
	public int getYOrigin (  )
	{
		return originY;
	}

	/**
	 * Gets the brightness of the image
	 *
	 * @return The brightness
	 */
	public int getBrightness (  )
	{
		return brightness;
	}

	/**
	 * Gets the contrast of the current image.
	 *
	 * @return The contrast
	 */
	public int getContrast (  )
	{
		return contrast;
	}

	/**
	 * gets the zoom of the current image.
	 *
	 * @return The zoom.
	 */
	public float getZoom (  )
	{
		return current_zoom;
	}

	/**
	 * Gets the rotation of the current image
	 *
	 * @return The rotation.
	 */
	public double getRotation (  )
	{
		return current_rotation;
	}

	/**
	 * Debug function. prints a string to the console.
	 *
	 * @param msg the string to be printed
	 */
	private static final void debug ( String msg )
	{
		ELog.info ( msg );
	}

	/**
	 * sets the last_x value of this object. The last_x value is the x-coordinate of the
	 * previous origin.
	 *
	 * @param value The new value
	 */
	public void setLast_x ( int value )
	{
		last_x = value;
	}

	/**
	 * Returns the last_x value of this object. The last_x value is the x-coordinate of
	 * the previous origin.
	 *
	 * @return The x-coordinate of the previous origin if set and 0 otherwise.
	 */
	public int getLast_x (  )
	{
		return last_x;
	}

	/**
	 * sets the last_y value of this object. The last_y value is the y coordinate of the
	 * previous origin.
	 *
	 * @param value The new value
	 */
	public void setLast_y ( int value )
	{
		last_y = value;
	}

	/**
	 * Returns the last_y value of this object. The last_y value is the y-coordinate of
	 * the previous origin.
	 *
	 * @return The y-coordinate of the previous origin if set and 0 otherwise.
	 */
	public int getLast_y (  )
	{
		return last_y;
	}

	private final byte clampByte ( int v )
	{
		if ( v > 255 )
		{
			return ( byte ) 255;
		}
		else if ( v < 0 )
		{
			return ( byte ) 0;
		}
		else
		{
			return ( byte ) v;
		}
	}

	/**
	 * Sets the brightnessAndContrastEnabled attribute of this object
	 *
	 * @param v The new brightnessAndContrastEnabled value
	 */
	private final void setBrightnessAndContrastEnabled ( boolean v )
	{
		brightnessAndContrastEnabled = v;
	}

	/**
	 * Gets the brightnessAndContrastEnabled attribute of this object
	 *
	 * @return The brightnessAndContrastEnabled value
	 */
	public final boolean getBrightnessAndContrastEnabled (  )
	{
		return brightnessAndContrastEnabled;
	}

	/**
	 * This function can be used to inform the object about the size of its parent.
	 *
	 * @param newValue The width
	 */
	public void setParentWidth ( int newValue )
	{
		parentWidth = newValue;
	}

	/**
	 * This function can be used to get the size of the parent that has been set for this
	 * object.
	 *
	 * @return The width
	 */
	public int getParentWidth (  )
	{
		return parentWidth;
	}

	/**
	 * This function can be used to inform the object about the size of its parent.
	 *
	 * @param newValue The height
	 */
	public void setParentHeight ( int newValue )
	{
		parentHeight = newValue;
	}

	/**
	 * This function can be used to get the size of the parent that has been set for this
	 * object.
	 *
	 * @return The height
	 */
	public int getParentHeight (  )
	{
		return parentHeight;
	}

	/**
	 * Sets the brightness attribute of this object
	 *
	 * @param b The new brightness value
	 */
	public final void setBrightness ( int b )
	{
		if ( ( b != brightness ) && ( brightnessAndContrastEnabled == true ) )
		{
			for ( int i = 0; i < 256; i++ )
			{
				lutData [ i ] = clampByte ( i + b );
			}
		}

		brightness = b;
		zoomAndRotate ( 
		    current_zoom,
		    ( float ) ( ( current_rotation * 180.0 ) / Math.PI )
		 );
	}

	/**
	 * Sets the contrast attribute of this object
	 *
	 * @param c The new contrast value
	 */
	public final void setContrast ( float c )
	{
		if ( ( c != contrast ) && ( brightnessAndContrastEnabled == true ) )
		{
			for ( int i = 0; i < 256; i++ )
			{
				//float  num  = ( float ) lutData[i];
				lutData [ i ] = clampByte ( ( int ) ( i * c ) );
			}
		}

		contrast = ( int ) ( ( c - 1 ) * 200.0f );
		zoomAndRotate ( 
		    current_zoom,
		    ( float ) ( ( current_rotation * 180.0 ) / Math.PI )
		 );
	}

	/**
	 * Sets the brightness and contrast attributes of this object
	 *
	 * @param b The new brightness value
	 * @param c The new contrast value
	 */
	public final void setBrightnessAndContrast ( 
	    int   b,
	    float c
	 )
	{
		if ( 
		    ( ( b != brightness ) || ( c != contrast ) )
			    && ( brightnessAndContrastEnabled == true )
		 )
		{
			for ( int i = 0; i < 256; i++ )
			{
				lutData [ i ] = clampByte ( ( int ) ( ( i + b ) * c ) );
			}
		}

		brightness     = b;
		contrast       = ( int ) ( ( c - 1 ) * 200.0f );
		zoomAndRotate ( 
		    current_zoom,
		    ( float ) ( ( current_rotation * 180.0 ) / Math.PI )
		 );
	}

	/**
	 * Rotates the image provided as a parameter by the amount specified and returns the
	 * rotated image.
	 *
	 * @param rot The angle of rotation in degrees
	 * @param aSource the image to be rotated
	 *
	 * @return The rotated image.
	 */
	public PlanarImage rotate ( 
	    float       rot,
	    PlanarImage aSource
	 )
	{
		int oldHeight =
			( int ) ( 
				Math.abs ( componentHeight * Math.cos ( current_rotation ) )
				+ Math.abs ( componentWidth * Math.sin ( current_rotation ) )
			 );
		int oldWidth =
			( int ) ( 
				Math.abs ( componentHeight * Math.sin ( current_rotation ) )
				+ Math.abs ( componentWidth * Math.cos ( current_rotation ) )
			 );

		ParameterBlock pb = new ParameterBlock(  );

		pb.addSource ( aSource ); // The sources[0] image
		pb.add ( ( float ) aSource.getWidth (  ) / 2 ); // The x origin
		pb.add ( ( float ) aSource.getHeight (  ) / 2 ); // The y origin
		pb.add ( ( ( float ) -( float ) Math.PI * rot ) / 180.0f ); // The rotation angle
		pb.add ( new InterpolationNearest(  ) ); // The interpolation

		//pb.add( new InterpolationBicubic() );// The interpolation
		aSource              = JAI.create ( "Rotate", pb, null );
		current_rotation     = ( Math.PI * rot ) / 180.0;

		initialize (  );
		setOrigin ( 
		    getXOrigin (  ) - ( ( componentWidth - oldWidth ) / 2 ),
		    getYOrigin (  ) - ( ( componentHeight - oldHeight ) / 2 )
		 );

		//Util.info("old width = " + oldWidth + " and new width = " + componentWidth);
		//Util.info("current_rotation = "+ current_rotation);
		return aSource;
	}

	/**
	 * Zooms the original source in this object by the amount specified and returns the
	 * zoomed image.
	 *
	 * @param amount The zoom amount.
	 *
	 * @return The zoomed image.
	 */
	public PlanarImage zoom ( float amount )
	{
		ParameterBlock pb = new ParameterBlock(  );

		pb.addSource ( originalSource ); // The sources[0] image
		pb.add ( amount ); // The xScale
		pb.add ( amount ); // The yScale
		pb.add ( 0.0F ); // The x translation
		pb.add ( 0.0F ); // The y translation
		pb.add ( new InterpolationNearest(  ) ); // The interpolation

		//PlanarImage source = JAI.create( "scale", pb, null );
		PlanarImage returnedSource = JAI.create ( "scale", pb, null );

		int         x = getXOrigin (  );
		int         y = getYOrigin (  );
		int         a = parentWidth / 2;
		int         b = parentHeight / 2;
		float       z = amount / current_zoom;
		setOrigin ( ( int ) ( ( z * ( x - a ) ) + a ), ( int ) ( 
			    ( z * ( y - b ) ) + b
		     )
		 );
		current_zoom = amount;
		initialize (  );

		return returnedSource;
	}

	/**
	 * Zooms th eoriginal image by the specified image and rotates it by the specified
	 * amount.
	 *
	 * @param zoom The amount of zoom required as a decimal (0.1- 1.0)
	 * @param rotate The amount of rotation required in degrees.
	 */
	public void zoomAndRotate ( 
	    float zoom,
	    float rotate
	 )
	{
		memoryIntensive = true;

		//Util.info("Starting zoom and rotate");
		ParameterBlock pb1 = new ParameterBlock(  );

		pb1.addSource ( originalSource ); // The sources[0] image
		pb1.add ( zoom ); // The xScale
		pb1.add ( zoom ); // The yScale
		pb1.add ( 0.0F ); // The x translation
		pb1.add ( 0.0F ); // The y translation
		pb1.add ( new InterpolationNearest(  ) ); // The interpolation

		//PlanarImage source = JAI.create( "scale", pb, null );
		source     = JAI.create ( "scale", pb1, null );

		pb1 = null;

		int   x = getXOrigin (  );
		int   y = getYOrigin (  );
		int   a = parentWidth / 2;
		int   b = parentHeight / 2;
		float z = zoom / current_zoom;
		setOrigin ( ( int ) ( ( z * ( x - a ) ) + a ), ( int ) ( 
			    ( z * ( y - b ) ) + b
		     )
		 );

		current_zoom = zoom;
		initialize (  );

		int oldHeight =
			( int ) ( 
				Math.abs ( componentHeight * Math.cos ( current_rotation ) )
				+ Math.abs ( componentWidth * Math.sin ( current_rotation ) )
			 );
		int oldWidth =
			( int ) ( 
				Math.abs ( componentHeight * Math.sin ( current_rotation ) )
				+ Math.abs ( componentWidth * Math.cos ( current_rotation ) )
			 );

		ParameterBlock pb2 = new ParameterBlock(  );

		pb2.addSource ( source ); // The sources[0] image
		pb2.add ( ( float ) source.getWidth (  ) / 2 ); // The x origin
		pb2.add ( ( float ) source.getHeight (  ) / 2 ); // The y origin
		pb2.add ( ( ( float ) -( float ) Math.PI * rotate ) / 180.0f ); // The rotation angle
		pb2.add ( new InterpolationNearest(  ) ); // The interpolation

		//pb.add( new InterpolationBicubic() );// The interpolation
		source               = JAI.create ( "Rotate", pb2, null );
		current_rotation     = ( Math.PI * rotate ) / 180.0;
		pb2                  = null;
		initialize (  );

		LookupTableJAI lookup = new LookupTableJAI( lutData );
		source              = JAI.create ( "lookup", source, lookup );
		memoryIntensive     = false;
	}

	/**
	 * Gets the odometerString attribute of the ImageStitchPanel object
	 *
	 * @param point Description of the Parameter
	 *
	 * @return The odometerString value
	 */
	public String getOdometerString ( Point point )
	{
		return " (" + point.x + ", " + point.y + ")";
	}

	/**
	 * Gets the modified image as a RenderedImage.
	 *
	 * @return The modified image.
	 */
	public RenderedImage getAsRenderedImage (  )
	{
		return source;
	}

	/**
	 * Gets the modified image as a BufferedImage.
	 *
	 * @return The modified image.
	 */
	public BufferedImage getAsBufferedImage (  )
	{
		return source.getAsBufferedImage (  );
	}

	/**
	 * Gets a sub image of the modified image as a RenderedImage
	 *
	 * @param width The width of the sub image
	 * @param height The height of the sub image
	 *
	 * @return The sub image
	 */
	public RenderedImage getAsRenderedImage ( 
	    int width,
	    int height
	 )
	{
		//long start = System.currentTimeMillis();
		Rectangle     rect = new Rectangle( -originX, -originY, width, height );
		BufferedImage bim = source.getAsBufferedImage ( rect, null );

		//Util.info("time taken = " + (System.currentTimeMillis() - start));
		return bim;
	}

	/**
	 * Returns the height of this component
	 *
	 * @return The height of this component
	 */
	public int getComponentHeight (  )
	{
		return componentHeight;
	}

	/**
	 * Sets the height of this component
	 *
	 * @param newComponentHeight The new height value
	 */
	public void setComponentHeight ( int newComponentHeight )
	{
		componentHeight = newComponentHeight;
	}

	/**
	 * Gets the width of this component
	 *
	 * @return The width of this component
	 */
	public int getComponentWidth (  )
	{
		return componentWidth;
	}

	/**
	 * Sets the width of this component
	 *
	 * @param newComponentWidth The new width
	 */
	public void setComponentWidth ( int newComponentWidth )
	{
		componentWidth = newComponentWidth;
	}

	/**
	 * Gets the temp_zoom attribute of this object
	 *
	 * @return The temp_zoom value
	 */
	public float getTemp_zoom (  )
	{
		return temp_zoom;
	}

	/**
	 * sets the temp_zoom attribute of this object
	 *
	 * @param newTemp_zoom The new temp_zoom value
	 */
	public void setTemp_zoom ( float newTemp_zoom )
	{
		temp_zoom = newTemp_zoom;
	}

	/**
	 * Gets the temp_rotation attribute of this object
	 *
	 * @return The temp_rotation value
	 */
	public double getTemp_rotation (  )
	{
		return temp_rotation;
	}

	/**
	 * sets the temp_zoom attribute of this object
	 *
	 * @param newTemp_rotation The new temp_rotation value
	 */
	public void setTemp_rotation ( double newTemp_rotation )
	{
		temp_rotation = newTemp_rotation;
	}

	/**
	 * gets the point on the rotated image where the origin of the image is present.
	 *
	 * @return The actual origin
	 */
	public Point getActualOrigin (  )
	{
		Point p1 = getRotatedPoint ( 0, 0 );
		Point p2 = getRotatedPoint ( 0, originalSource.getHeight (  ) );
		Point p3 = getRotatedPoint ( originalSource.getWidth (  ), 0 );
		Point p4 =
			getRotatedPoint ( 
			    originalSource.getWidth (  ),
			    originalSource.getHeight (  )
			 );

		Point returnPoint = new Point(  );

		if ( p1.x < p2.x )
		{
			if ( p1.x < p3.x )
			{
				if ( p1.x < p4.x )
				{
					returnPoint.x = p1.x;
				}
				else
				{
					returnPoint.x = p4.x;
				}
			}
			else
			{
				if ( p3.x < p4.x )
				{
					returnPoint.x = p3.x;
				}
				else
				{
					returnPoint.x = p4.x;
				}
			}
		}
		else
		{
			if ( p2.x < p3.x )
			{
				if ( p2.x < p4.x )
				{
					returnPoint.x = p2.x;
				}
				else
				{
					returnPoint.x = p4.x;
				}
			}
			else
			{
				if ( p3.x < p4.x )
				{
					returnPoint.x = p3.x;
				}
				else
				{
					returnPoint.x = p4.x;
				}
			}
		}

		if ( p1.y < p2.y )
		{
			if ( p1.y < p3.y )
			{
				if ( p1.y < p4.y )
				{
					returnPoint.y = p1.y;
				}
				else
				{
					returnPoint.y = p4.y;
				}
			}
			else
			{
				if ( p3.y < p4.y )
				{
					returnPoint.y = p3.y;
				}
				else
				{
					returnPoint.y = p4.y;
				}
			}
		}
		else
		{
			if ( p2.y < p3.y )
			{
				if ( p2.y < p4.y )
				{
					returnPoint.y = p2.y;
				}
				else
				{
					returnPoint.y = p4.y;
				}
			}
			else
			{
				if ( p3.y < p4.y )
				{
					returnPoint.y = p3.y;
				}
				else
				{
					returnPoint.y = p4.y;
				}
			}
		}

		returnPoint.x += originX;
		returnPoint.y += originY;

		return returnPoint;
	}

	/**
	 * Gets the location of the point specified on the original image as a point on the
	 * rotated image
	 *
	 * @param x x-coordinate
	 * @param y y-coordinate
	 *
	 * @return the point on the rotated image
	 */
	public Point getRotatedPoint ( 
	    int x,
	    int y
	 )
	{
		//int     actualOriginX  = ( int ) ( ( float ) originalSource.getWidth() / current_zoom / 2.0f );
		//int     actualOriginY  = ( int ) ( ( float ) originalSource.getHeight() / current_zoom / 2.0f );
		int actualOriginX =
			( int ) ( ( float ) originalSource.getWidth (  ) / 2.0f * current_zoom );
		int actualOriginY =
			( int ) ( ( float ) originalSource.getHeight (  ) / 2.0f * current_zoom );

		x     = ( int ) ( ( float ) x * current_zoom );
		y     = ( int ) ( ( float ) y * current_zoom );
		x     = x - actualOriginX;
		y     = actualOriginY - y;

		double r = Math.sqrt ( ( x * x ) + ( y * y ) );
		double theta = getTanInverseOf ( ( double ) y, ( double ) x );

		//Util.info( theta );
		int newX = ( int ) ( r * Math.cos ( theta - current_rotation ) );
		int newY = ( int ) ( r * Math.sin ( theta - current_rotation ) );

		newX     = actualOriginX + newX;
		newY     = actualOriginY - newY;

		return new Point( newX, newY );
	}

	/**
	 * Assumes that the numerator is the y coordinate and the denominator the x
	 * coordinate and then returns the tan inverse of numerator/denominator. Useful
	 * because the Math class provided by java cannot return values greater than pi/2
	 * and les than -PI/2
	 *
	 * @param numerator Description of the Parameter
	 * @param denominator Description of the Parameter
	 *
	 * @return The tanInverseOf value
	 */
	public double getTanInverseOf ( 
	    double numerator,
	    double denominator
	 )
	{
		if ( numerator >= 0 )
		{
			if ( denominator >= 0 )
			{
				return Math.atan ( numerator / denominator );
			}
			else
			{
				return Math.PI + Math.atan ( numerator / denominator );
			}
		}
		else
		{
			if ( denominator >= 0 )
			{
				return ( Math.PI * 2.0 ) + Math.atan ( numerator / denominator );
			}
			else
			{
				return Math.PI + Math.atan ( numerator / denominator );
			}
		}
	}
}
