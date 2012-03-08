import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JPanel;


/**
 * The panel for the 1-Dimensional display. This class extands GraphicalDisplayPanel.
 *
 * @author zndavid
 * @version 1.0
 */
public class OneDDisplayPanel
	extends GraphicalDisplayPanel
{
	/** The borderleft at the edges */
	public static final int EXTRA_SPACE = 25;

	/** Maximum radius of the circles allowed */
	public static final int MAX_RADIUS_ALLOWED = 50;

	/** Thickness of the arc */
	public static final int WIDTH_OF_ARC = 3;

	/** Thickness of the small symbol */
	public static final int WIDTH_OF_SMALL_SYMBOL = 1;

	/** Thickness of the medium symbol */
	public static final int WIDTH_OF_MEDIUM_SYMBOL = 3;

	/** Thickness of the large symbol */
	public static final int WIDTH_OF_LARGE_SYMBOL = 5;

	/** The font size */
	public static final int FONT_SIZE = 12;

	/** The font */
	public static final Font LABEL_FONT = new Font( "monospaced", Font.PLAIN, FONT_SIZE );

	/** The FontRenderContext */
	public static final FontRenderContext FONT_RENDER_CTXT =
		new FontRenderContext( null, false, false );
	Vector                                listOfOtherContins;
	String                                continName                = "";
	int                                   continNumber;
	int                                   radiusOfCurve;
	int                                   radiusOfCircles;
	int                                   coordinateOfCentreOfPanel = 0;

	/**
	 * Creates a new OneDDisplayPanel object.
	 *
	 * @param newConNumber The contin number
	 */
	public OneDDisplayPanel ( int newConNumber )
	{
		//Util.info("new contin number = " + newConNumber);
		listOfOtherContins     = Utilities.getOtherDisplayContins ( newConNumber );
		continName             = Utilities.getContinName ( new Integer( newConNumber ) );
		continNumber           = newConNumber;
        continNameText = continName.equals ( "" ) ? ( "" + continName ) : continName;
        preferredTitle = "1-Dimensional Graphical Display - Contin " + continNameText;
	}

	/**
	 * Changes the visualization. This feature is not available with 1D-Display but is
	 * available with 2D-Display.
	 *
	 * @see Branched2DDisplayPanel#changeVisualization
	 */
	public void changeVisualization (  )
	{
		JOptionPane.showMessageDialog ( 
		    null,
		    "This feature is not available with 1D-Display"
		 );
	}

	/**
	 * The overridden paint method
	 *
	 * @param g The Graphics object associated with this object.
	 */
	public void paint ( Graphics g )
	{
		//Util.info("in function paint");
		Graphics2D g2D = null;

		if ( g instanceof Graphics2D )
		{
			g2D = ( Graphics2D ) g;
		}
		else
		{
			return;
		}

		int widthOfPanel  = this.getWidth (  );
		int heightOfPanel = this.getHeight (  );
		g2D.setColor ( Color.WHITE );
		g2D.fillRect ( 0, 0, widthOfPanel, heightOfPanel );

		radiusOfCurve =
			( Math.min ( widthOfPanel, heightOfPanel ) / 2 ) - this.EXTRA_SPACE
			- ( this.WIDTH_OF_ARC * 2 );

		if ( radiusOfCurve < 1 )
		{
			return;
		}

		int maxRadius1 = ( radiusOfCurve - ( 2 * this.EXTRA_SPACE ) ) / 3;
		int maxRadius2 =
			( int ) ( ( 2 * Math.PI * radiusOfCurve ) / listOfOtherContins.size (  ) )
			- ( 2 * EXTRA_SPACE );
		radiusOfCircles     = Math.min ( maxRadius1, maxRadius2 );
		radiusOfCircles     = Math.min ( radiusOfCircles, this.MAX_RADIUS_ALLOWED );

		//Util.info("radiusOfCircles = " + radiusOfCircles);
		if ( radiusOfCircles < 1 )
		{
			return;
		}

		//Util.info("radius of curve = " + radiusOfCurve + " and radius of circles " + radiusOfCircles );
		//coordinateOfCentreOfPanel = radiusOfCurve + EXTRA_SPACE + WIDTH_OF_ARC;
		coordinateOfCentreOfPanel = Math.min ( widthOfPanel, heightOfPanel ) / 2;
		g2D.setColor ( Color.BLACK );
		g2D.setStroke ( new BasicStroke( WIDTH_OF_ARC ) );

		String continText = continName.equals ( "" ) ? ( "" + continName ) : continName;
		drawLabelledCircle ( continText, new Point( 0, 0 ), g2D );

		Vector points     = getLocationOfAllCircles (  );
		int    pointsSize = points.size (  );

		for ( int i = 0; i < pointsSize; i++ )
		{
			Point pt = ( Point ) points.elementAt ( i );

			if ( i < listOfOtherContins.size (  ) )
			{
				DisplayContin dcon = ( DisplayContin ) listOfOtherContins.elementAt ( i );
				continText =
					dcon.getContinName (  ).equals ( "" )
					? ( "" + dcon.getContinNumber (  ) ) : dcon.getContinName (  );
				drawLabelledCircle ( continText, pt, g2D );
				drawSymbol ( 
				    dcon.getType (  ),
				    dcon.getStrength (  ),
				    i,
				    pointsSize,
				    g2D
				 );
			}
		}
	}

	private Vector getLocationOfAllCircles (  )
	{
		Vector locations    = new Vector(  );
		int    num          = listOfOtherContins.size (  );
		int    actualRadius = radiusOfCurve - radiusOfCircles;

		for ( int i = 0; i < num; i++ )
		{
			Point point = calculatePoint ( i, num );
			locations.addElement ( point );
		}

		return locations;
	}

	private Point calculatePoint ( 
	    int index,
	    int numberOfPoints
	 )
	{
		if ( ( index < 0 ) || ( numberOfPoints <= index ) )
		{
			return new Point(  );
		}

		double actualRadius = ( double ) ( radiusOfCurve - radiusOfCircles );
		double angle  = ( Math.PI * 2.0 * index ) / numberOfPoints;
		int    xCoord = ( int ) ( actualRadius * Math.cos ( angle ) );
		int    yCoord = -( int ) ( actualRadius * Math.sin ( angle ) );

		return new Point( xCoord, yCoord );
	}

	private void drawLabelledCircle ( 
	    String     label,
	    Point      center,
	    Graphics2D g2
	 )
	{
		g2.setStroke ( new BasicStroke( WIDTH_OF_ARC ) );
		g2.draw ( 
		    new Ellipse2D.Float( 
		        ( float ) ( 
			        ( coordinateOfCentreOfPanel + center.x ) - radiusOfCircles
		         ),
		        ( float ) ( 
			        ( coordinateOfCentreOfPanel + center.y ) - radiusOfCircles
		         ),
		        radiusOfCircles * 2.0f,
		        radiusOfCircles * 2.0f
		     )
		 );

		//Util.info("drew labelled circle with centre at " + center);
		TextLayout        tl    = new TextLayout( label, LABEL_FONT, FONT_RENDER_CTXT );
		Rectangle2D.Float bound = ( Rectangle2D.Float ) tl.getBounds (  );

		//int xOfBound = (int)bound.x
		//int yOfBound = (int)bound.y;
		//int wOfBound = (int)bound.width;
		//int hOfBound = (int)bound.height;
		//int xAdjustment = (wOfBound - xOfBound)/2
		//int yAdjuxtment = (hOfBound - yOfBound)/2
		float xAdjustment = ( bound.width - bound.x ) / 2.0f;
		float yAdjuxtment = -( bound.height - bound.y ) / 2.0f;

		//float yAdjuxtment = -FONT_SIZE/2.0f;
		//Util.info(bound);
		tl.draw ( 
		    g2,
		    ( float ) ( coordinateOfCentreOfPanel + center.x ) - xAdjustment,
		    ( float ) ( coordinateOfCentreOfPanel + center.y ) - yAdjuxtment
		 );

		/*Rectangle2D.Float bound = (Rectangle2D.Float) LABEL_FONT.getStringBounds(label, FONT_RENDER_CTXT);
		   float xAdjustment = (bound.width - bound.x)/2.0f;
		   float yAdjuxtment = -(bound.height - bound.y)/2.0f;
		   g2.drawString(label, (float)(coordinateOfCentreOfPanel + center.x) - xAdjustment, (float)(coordinateOfCentreOfPanel + center.y)-yAdjuxtment);*/
	}

	private void drawSymbol ( 
	    String     type,
	    int        strength,
	    int        index,
	    int        numberOfPoints,
	    Graphics2D g2
	 )
	{
		Point fromPoint = calculateFromPoint ( index, numberOfPoints );
		Point toPoint = calculateToPoint ( index, numberOfPoints );

		//g2.drawLine(fromPoint.x, fromPoint.y, toPoint.x, toPoint.y);
		if ( strength < 4 )
		{
			g2.setStroke ( new BasicStroke( 1 ) );
		}
		else if ( strength < 10 )
		{
			g2.setStroke ( new BasicStroke( 3 ) );
		}
		else
		{
			g2.setStroke ( new BasicStroke( 5 ) );
		}

		if ( type.compareToIgnoreCase ( GlobalStrings.PRESYNAPTIC ) == 0 )
		{
			drawPresynaptic ( fromPoint, toPoint, g2 );
		}
		else if ( type.compareToIgnoreCase ( GlobalStrings.POSTSYNAPTIC ) == 0 )
		{
			drawPresynaptic ( toPoint, fromPoint, g2 );
		}
		else if ( type.compareToIgnoreCase ( GlobalStrings.MULTIPLE_PRESYNAPTIC ) == 0 )
		{
			drawPresynaptic ( fromPoint, toPoint, g2 );
		}
		else if ( type.compareToIgnoreCase ( GlobalStrings.MULTIPLE_POSTSYNAPTIC ) == 0 )
		{
			drawPresynaptic ( toPoint, fromPoint, g2 );
		}
		else if ( type.compareToIgnoreCase ( GlobalStrings.GAP_JUNCTION ) == 0 )
		{
			drawGap ( fromPoint, toPoint, g2 );
		}
		else
		{
			g2.drawLine ( fromPoint.x, fromPoint.y, toPoint.x, toPoint.y );
		}
	}

	private Point calculateFromPoint ( 
	    int index,
	    int numberOfPoints
	 )
	{
		if ( ( index < 0 ) || ( numberOfPoints <= index ) )
		{
			return new Point(  );
		}

		double fromRadius = ( double ) ( radiusOfCircles + ( 2 * WIDTH_OF_ARC ) );
		double angle  = ( Math.PI * 2.0 * index ) / numberOfPoints;
		int    xCoord = ( int ) ( fromRadius * Math.cos ( angle ) );
		int    yCoord = -( int ) ( fromRadius * Math.sin ( angle ) );

		return new Point( 
		    coordinateOfCentreOfPanel + xCoord,
		    coordinateOfCentreOfPanel + yCoord
		 );
	}

	private Point calculateToPoint ( 
	    int index,
	    int numberOfPoints
	 )
	{
		if ( ( index < 0 ) || ( numberOfPoints <= index ) )
		{
			return new Point(  );
		}

		double toRadius =
			( double ) ( 
				radiusOfCurve - ( 2 * radiusOfCircles ) - ( 2 * WIDTH_OF_ARC )
			 );
		double angle  = ( Math.PI * 2.0 * index ) / numberOfPoints;
		int    xCoord = ( int ) ( toRadius * Math.cos ( angle ) );
		int    yCoord = -( int ) ( toRadius * Math.sin ( angle ) );

		return new Point( 
		    coordinateOfCentreOfPanel + xCoord,
		    coordinateOfCentreOfPanel + yCoord
		 );
	}

	private void drawPresynaptic ( 
	    Point      p1,
	    Point      p2,
	    Graphics2D g
	 )
	{
		double x     = p2.x - p1.x;
		double y     = p2.y - p1.y;
		double len   = Math.sqrt ( ( x * x ) + ( y * y ) );
		int    xAdd1 = ( int ) ( y / len * 7.0 );
		int    yAdd1 = ( int ) ( x / len * 7.0 );

		//int    xAdd2 = ( int ) ( x / len * 20.0 );
		//int    yAdd2 = ( int ) ( y / len * 20.0 );
		//int    xSub  = ( int ) ( x / len * 20.0 );
		//int    ySub  = ( int ) ( y / len * 20.0 );
		int xAdd2 = ( int ) ( x / len * 12.0 );
		int yAdd2 = ( int ) ( y / len * 12.0 );
		int xSub  = ( int ) ( x / len * 7.0 );
		int ySub  = ( int ) ( y / len * 7.0 );
		p2.x -= xSub;
		p2.y -= ySub;
		g.drawLine ( p1.x, p1.y, p2.x, p2.y );
		g.drawLine ( p2.x, p2.y, ( p2.x + xAdd1 ) - xAdd2, p2.y - yAdd1 - yAdd2 );
		g.drawLine ( p2.x, p2.y, p2.x - xAdd1 - xAdd2, ( p2.y + yAdd1 ) - yAdd2 );
	}

	private void drawGap ( 
	    Point      p1,
	    Point      p2,
	    Graphics2D g
	 )
	{
		double x   = p2.x - p1.x;
		double y   = p2.y - p1.y;
		double len = Math.sqrt ( ( x * x ) + ( y * y ) );

		//int    yAdd = ( int ) ( x / len * 10.0 );
		//int    xAdd = ( int ) ( y / len * 10.0 );
		//int    xSub = ( int ) ( x / len * 20.0 );
		//int    ySub = ( int ) ( y / len * 20.0 );
		int yAdd = ( int ) ( x / len * 10.0 );
		int xAdd = ( int ) ( y / len * 10.0 );
		int xSub = ( int ) ( x / len * 5.0 );
		int ySub = ( int ) ( y / len * 5.0 );
		p1.x += xSub;
		p1.y += ySub;
		p2.x -= xSub;
		p2.y -= ySub;
		g.drawLine ( p1.x, p1.y, p2.x, p2.y );
		g.drawLine ( p1.x + xAdd, p1.y - yAdd, p1.x - xAdd, p1.y + yAdd );
		g.drawLine ( p2.x + xAdd, p2.y - yAdd, p2.x - xAdd, p2.y + yAdd );
	}
    public boolean isDrawable()
    {
        return true;
    }
}
