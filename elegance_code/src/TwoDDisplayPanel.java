import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import java.util.Iterator;
import java.util.TreeSet;
import javax.swing.JOptionPane;


/**
 * The panel for the 2-Dimensional display. This class extands GraphicalDisplayPanel.
 *
 * @author zndavid
 * @version 1.0
 * @deprecated use the Branched2DDisplayPanel instead
 */
public class TwoDDisplayPanel
	extends GraphicalDisplayPanel
{
	private static final int               EXTRA_SPACE           = 25;
	private static final int               BORDER                = 5;
	private static final int               WORM_LINE_THICKNESS   = 3;
	private static final int               SYMBOL_LINE_THICKNESS = 1;
	private static final int               SYMBOL_LINE_LENGTH    = 30;
	private static final Font              NUMBER_FONT           =
		new Font( "Default", Font.BOLD, 12 );
	private static final Font              HEADING_FONT =
		new Font( "Monospaced", Font.BOLD, 18 );
	private static final FontRenderContext FONT_RENDER_CTXT =
		new FontRenderContext( null, false, false );
	private static final Font              SYMBOL_FONT =
		NUMBER_FONT.deriveFont ( 
		    AffineTransform.getRotateInstance ( Math.toRadians ( -90.0 ) )
		 );
	private TreeSet listOfOtherContins;
	private String  continName                  = "";
	private int     continNumber;
	private int     vertLocationOfLine          = 0;
	private int     currentVertLocationOfSymbol = 0;
	private int     from                        = 0;
	private int     to                          = 0;
	private int     fromLocation                = 0;
	private int     toLocation                  = 0;

	//private int horizontalLocationOfPreviousRelation = 0;
	private int     horizontalLocationOfRelation = 0;
	private boolean horizontal = true;

	/**
	 * Creates a new TwoDDisplayPanel object.
	 *
	 * @param newConNumber The contin number
	 */
	public TwoDDisplayPanel ( int newConNumber )
	{
		listOfOtherContins =
			new TreeSet( Utilities.getOther2DDisplayContins ( newConNumber ) );
        if(listOfOtherContins.size() <= 0)
        {
            JOptionPane.showMessageDialog(null, "This contin does not interact with other contins. Display cannot be drawn.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
       	continName       = Utilities.getContinName ( new Integer( newConNumber ) );
		continNumber     = newConNumber;
		horizontal       = true;
		from             = getMinimumSectionNumber (  );
		to               = getMaximumSectionNumber (  );
        if(to == from || to-from <=0)
        {
            JOptionPane.showMessageDialog(null, "Display cannot be drawn for this contin.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        continNameText = continName.equals ( "" ) ? ( "" + continName ) : continName;
        preferredTitle = "2-Dimensional Graphical Display - Contin " + continNameText;
	}

	/**
	 * Changes the visualization. If the painting is done horizontally then it is changed
	 * to vertical and view versa.
	 */
	public void changeVisualization (  )
	{
		horizontal = !horizontal;
		repaint (  );
	}

	/**
	 * The overridden paint method
	 *
	 * @param g The Graphics object associated with this object.
	 */
	public void paint ( Graphics g )
	{
        if(listOfOtherContins.size() <= 0) return;
        if(to == from || to-from <=0) return;
		Graphics2D g2;

		if ( g instanceof Graphics2D )
		{
			g2 = ( Graphics2D ) g;
		}
		else
		{
			return;
		}

		if ( !horizontal )
		{
			//g2.transform(AffineTransform.getRotateInstance(Math.toRadians(-90)));
			g2.translate ( this.getWidth (  ) / 2.0f, this.getHeight (  ) / 2.0f );
			g2.rotate ( Math.toRadians ( 90 ) );
			g2.translate ( -this.getHeight (  ) / 2.0f, -this.getWidth (  ) / 2.0f );

			//g2.rotate(Math.toRadians(90), this.getWidth()/2.0f, this.getHeight()/2.0f);
		}

		drawLabelledHorizontalLine ( g2 );
		g2.setStroke ( new BasicStroke( 1 ) );

		Iterator itor = listOfOtherContins.iterator (  );

		/*int currentSectionNumber =-1;
		   String textOfContinNames="";
		   while(itor.hasNext())
		   {
		       TwoDDisplayContin discon = (TwoDDisplayContin) itor.next();
		       if(discon.getSectionNumber() == currentSectionNumber)
		       {
		           textOfContinNames += ", " + discon.getDisplayableContinName();
		       }
		       else if(currentSectionNumber == -1)
		       {
		           //drawText(textOfContinNames, g2);
		           currentSectionNumber = discon.getSectionNumber();
		           textOfContinNames = discon.getDisplayableContinName();
		       }
		       else
		       {
		           drawText(textOfContinNames, g2);
		           currentSectionNumber = discon.getSectionNumber();
		           textOfContinNames = discon.getDisplayableContinName();
		       }
		       drawRelation(discon, g2);
		   }
		   if(! textOfContinNames.equals(""))
		   {
		       drawText(textOfContinNames, g2);
		   }*/
		int currentSectionNumber = -1;

		while ( itor.hasNext (  ) )
		{
			TwoDDisplayContin discon = ( TwoDDisplayContin ) itor.next (  );

			if ( discon.getSectionNumber (  ) == currentSectionNumber )
			{
				drawRelationUsingCurrentVertLoc ( discon, g2 );
			}
			else
			{
				drawRelation ( discon, g2 );
				currentSectionNumber = discon.getSectionNumber (  );
			}
		}
	}

	private void drawLabelledHorizontalLine ( Graphics2D g2 )
	{
		int panelWidth  = this.getWidth (  );
		int panelHeight = this.getHeight (  );

		if ( !horizontal )
		{
			panelWidth      = this.getHeight (  );
			panelHeight     = this.getWidth (  );
		}

		g2.setColor ( Color.WHITE );
		g2.fillRect ( 0, 0, panelWidth, panelHeight );

		if ( 
		    ( panelHeight <= ( 2 * EXTRA_SPACE ) )
			    || ( panelWidth <= ( 2 * EXTRA_SPACE ) )
		 )
		{
			return;
		}

		panelWidth -= ( 2 * EXTRA_SPACE );
		panelHeight -= ( 2 * EXTRA_SPACE );

		vertLocationOfLine = EXTRA_SPACE + ( ( panelHeight * 3 ) / 4 );

		//Util.info("vertLocationOfLine = "+vertLocationOfLine);
		g2.setColor ( Color.black );
		g2.setStroke ( new BasicStroke( WORM_LINE_THICKNESS ) );
		g2.setFont ( NUMBER_FONT );

		String            fromText     = "" + from;
		TextLayout        tl           =
			new TextLayout( fromText, NUMBER_FONT, FONT_RENDER_CTXT );
		Rectangle2D.Float bound        = ( Rectangle2D.Float ) tl.getBounds (  );
		int               xAdjustment1 = ( BORDER * 2 ) + ( int ) bound.width;

		//int yAdjustment1 = BORDER + (int) (bound.height/2.0f);
		int yAdjustment1 = ( int ) ( bound.height / 2.0f );

		tl.draw ( g2, EXTRA_SPACE + BORDER, vertLocationOfLine + yAdjustment1 );

		String toText = "" + to;
		tl        = new TextLayout( toText, NUMBER_FONT, FONT_RENDER_CTXT );
		bound     = ( Rectangle2D.Float ) tl.getBounds (  );

		int xAdjustment2 = ( BORDER * 2 ) + ( int ) bound.width;
		int yAdjustment2 = ( int ) ( bound.height / 2.0f );

		tl.draw ( 
		    g2,
		    ( EXTRA_SPACE + panelWidth ) - xAdjustment2,
		    vertLocationOfLine + yAdjustment2
		 );

		g2.drawLine ( 
		    EXTRA_SPACE + xAdjustment1,
		    vertLocationOfLine,
		    ( EXTRA_SPACE + panelWidth ) - xAdjustment2 - BORDER,
		    vertLocationOfLine
		 );

		fromLocation     = EXTRA_SPACE + xAdjustment1;
		toLocation       = ( EXTRA_SPACE + panelWidth ) - xAdjustment2 - BORDER;

		String headerText = continName.equals ( "" ) ? ( "" + continNumber ) : continName;
		tl        = new TextLayout( headerText, HEADING_FONT, FONT_RENDER_CTXT );
		bound     = ( Rectangle2D.Float ) tl.getBounds (  );

		int xAdjustment3 = ( int ) ( bound.width / 2.0f );
		int yAdjustment3 = ( int ) ( bound.height / 2.0f );

		tl.draw ( 
		    g2,
		    ( panelWidth / 2 ) - xAdjustment3,
		    vertLocationOfLine + EXTRA_SPACE + yAdjustment3
		 );
	}

	/**
	 * The smallest section number that is to be displayed
	 *
	 * @return The smallest section number that is to be displayed
	 */
	public int getMinimumSectionNumber (  )
	{
		TwoDDisplayContin dis = ( TwoDDisplayContin ) listOfOtherContins.first (  );

		return dis.getSectionNumber (  );
	}

	/**
	 * The largest section number that is to be displayed
	 *
	 * @return The largest section number that is to be displayed
	 */
	public int getMaximumSectionNumber (  )
	{
		TwoDDisplayContin dis = ( TwoDDisplayContin ) listOfOtherContins.last (  );

		return dis.getSectionNumber (  );
	}

	private void drawRelation ( 
	    TwoDDisplayContin discon,
	    Graphics2D        g2
	 )
	{
		//Util.info(discon.getContinName() + " - using vertLoc" );
		//horizontalLocationOfPreviousRelation = horizontalLocationOfRelation;
		horizontalLocationOfRelation =
			getHorizontalLocation ( discon.getSectionNumber (  ) );

		//g2.drawLine(horizontalLocationOfRelation, vertLocationOfLine-(WORM_LINE_THICKNESS+1)/2, horizontalLocationOfRelation, vertLocationOfLine-(WORM_LINE_THICKNESS+1)/2 - SYMBOL_LINE_LENGTH);
		String type = discon.getType (  );

		if ( 
		    type.equals ( GlobalStrings.PRESYNAPTIC )
			    || type.equals ( GlobalStrings.MULTIPLE_PRESYNAPTIC )
		 )
		{
			drawPresynaptic ( 
			    new Point( 
			        horizontalLocationOfRelation,
			        vertLocationOfLine - ( ( WORM_LINE_THICKNESS + 1 ) / 2 )
			     ),
			    new Point( 
			        horizontalLocationOfRelation,
			        vertLocationOfLine - ( ( WORM_LINE_THICKNESS + 1 ) / 2 )
			        - SYMBOL_LINE_LENGTH
			     ),
			    g2
			 );
		}
		else if ( 
		    type.equals ( GlobalStrings.POSTSYNAPTIC )
			    || type.equals ( GlobalStrings.MULTIPLE_POSTSYNAPTIC )
		 )
		{
			drawPresynaptic ( 
			    new Point( 
			        horizontalLocationOfRelation,
			        vertLocationOfLine - ( ( WORM_LINE_THICKNESS + 1 ) / 2 )
			        - SYMBOL_LINE_LENGTH
			     ),
			    new Point( 
			        horizontalLocationOfRelation,
			        vertLocationOfLine - ( ( WORM_LINE_THICKNESS + 1 ) / 2 )
			     ),
			    g2
			 );
		}
		else if ( type.equals ( GlobalStrings.GAP_JUNCTION ) )
		{
			drawGap ( 
			    new Point( 
			        horizontalLocationOfRelation,
			        vertLocationOfLine - ( ( WORM_LINE_THICKNESS + 1 ) / 2 )
			     ),
			    new Point( 
			        horizontalLocationOfRelation,
			        vertLocationOfLine - ( ( WORM_LINE_THICKNESS + 1 ) / 2 )
			        - SYMBOL_LINE_LENGTH
			     ),
			    g2
			 );
		}

		//g2.drawLine(horizontalLocationOfRelation, vertLocationOfLine-WORM_LINE_THICKNESS-1, horizontalLocationOfRelation, vertLocationOfLine-WORM_LINE_THICKNESS-1- SYMBOL_LINE_LENGTH);
		int textSize = ( int ) drawText ( discon.getDisplayableContinName (  ), g2 );

		//Util.info("textsize = " + textSize);
		//Util.info("drewline from " + horizontalLocationOfRelation + ", " + (vertLocationOfLine+(WORM_LINE_THICKNESS+1)/2) + " to " + horizontalLocationOfRelation + ", " + (vertLocationOfLine+(WORM_LINE_THICKNESS+1)/2 + SYMBOL_LINE_LENGTH));
		//String name = discon.getContinName().equals("") ? "" + discon.getContinNumber() : discon.getContinName();
		currentVertLocationOfSymbol =
			vertLocationOfLine - WORM_LINE_THICKNESS - 1 - SYMBOL_LINE_LENGTH - textSize
			- ( 2 * BORDER );

		//Util.info("currentVertLocationOfSymbol = " + currentVertLocationOfSymbol);
	}

	private void drawRelationUsingCurrentVertLoc ( 
	    TwoDDisplayContin discon,
	    Graphics2D        g2
	 )
	{
		//Util.info(discon.getContinName() + " - using CurrentVertLoc" );
		horizontalLocationOfRelation =
			getHorizontalLocation ( discon.getSectionNumber (  ) );

		String type = discon.getType (  );

		if ( 
		    type.equals ( GlobalStrings.PRESYNAPTIC )
			    || type.equals ( GlobalStrings.MULTIPLE_PRESYNAPTIC )
		 )
		{
			drawPresynaptic ( 
			    new Point( 
			        horizontalLocationOfRelation,
			        currentVertLocationOfSymbol - ( ( WORM_LINE_THICKNESS + 1 ) / 2 )
			     ),
			    new Point( 
			        horizontalLocationOfRelation,
			        currentVertLocationOfSymbol - ( ( WORM_LINE_THICKNESS + 1 ) / 2 )
			        - SYMBOL_LINE_LENGTH
			     ),
			    g2
			 );
		}
		else if ( 
		    type.equals ( GlobalStrings.POSTSYNAPTIC )
			    || type.equals ( GlobalStrings.MULTIPLE_POSTSYNAPTIC )
		 )
		{
			drawPresynaptic ( 
			    new Point( 
			        horizontalLocationOfRelation,
			        currentVertLocationOfSymbol - ( ( WORM_LINE_THICKNESS + 1 ) / 2 )
			        - SYMBOL_LINE_LENGTH
			     ),
			    new Point( 
			        horizontalLocationOfRelation,
			        currentVertLocationOfSymbol - ( ( WORM_LINE_THICKNESS + 1 ) / 2 )
			     ),
			    g2
			 );
		}
		else if ( type.equals ( GlobalStrings.GAP_JUNCTION ) )
		{
			drawGap ( 
			    new Point( 
			        horizontalLocationOfRelation,
			        currentVertLocationOfSymbol - ( ( WORM_LINE_THICKNESS + 1 ) / 2 )
			     ),
			    new Point( 
			        horizontalLocationOfRelation,
			        currentVertLocationOfSymbol - ( ( WORM_LINE_THICKNESS + 1 ) / 2 )
			        - SYMBOL_LINE_LENGTH
			     ),
			    g2
			 );
		}

		//g2.drawLine(horizontalLocationOfRelation, currentVertLocationOfSymbol-WORM_LINE_THICKNESS-1, horizontalLocationOfRelation, currentVertLocationOfSymbol-WORM_LINE_THICKNESS-1- SYMBOL_LINE_LENGTH);
		int textSize =
			( int ) drawTextUsingCurrentVertLoc ( 
			    discon.getDisplayableContinName (  ),
			    g2
			 );

		//Util.info("drewline from " + horizontalLocationOfRelation + ", " + (currentVertLocationOfSymbol+(WORM_LINE_THICKNESS+1)/2) + " to " + horizontalLocationOfRelation + ", " + (currentVertLocationOfSymbol+(WORM_LINE_THICKNESS+1)/2 + SYMBOL_LINE_LENGTH));
		//String name = discon.getContinName().equals("") ? "" + discon.getContinNumber() : discon.getContinName();
		currentVertLocationOfSymbol =
			currentVertLocationOfSymbol - WORM_LINE_THICKNESS - 1 - SYMBOL_LINE_LENGTH
			- textSize - ( 2 * BORDER );

		//Util.info("currentVertLocationOfSymbol = " + currentVertLocationOfSymbol);
	}

	private int getHorizontalLocation ( int sectionNumber )
	{
		//return (int)((double)(toLocation-fromLocation)*(double)(to-sectionNumber)/(double)(to-from));
		//Util.info("toLocation = " +  toLocation);
		//Util.info("fromLocation = " +  fromLocation);
		//Util.info("to = " +  to);
		//Util.info("from = " +  from);
		//Util.info("sectionNo = " + sectionNumber);
		return fromLocation
		+ ( 
			( ( toLocation - fromLocation ) * ( to - sectionNumber ) ) / ( to - from )
		 );
	}

	private float drawText ( 
	    String     text,
	    Graphics2D g2
	 )
	{
		//Util.info("text = " + text);
		if ( ( text == null ) || text.equals ( "" ) )
		{
			return 0;
		}

		if ( horizontalLocationOfRelation <= 0 )
		{
			return 0;
		}

		TextLayout        tl    = new TextLayout( text, SYMBOL_FONT, FONT_RENDER_CTXT );
		Rectangle2D.Float bound = ( Rectangle2D.Float ) tl.getBounds (  );

		//Util.info("bound = " + bound);
		float xAdjustment = bound.width / 2.0f;

		//int yAdjustment = ( int ) ( bound.height / 2.0f );
		tl.draw ( 
		    g2,
		    horizontalLocationOfRelation + xAdjustment,
		    vertLocationOfLine - WORM_LINE_THICKNESS - 1 - SYMBOL_LINE_LENGTH
		 );

		//tl.draw(g2, 100.0f, 100.0f);
		//return bound.height;
		tl        = new TextLayout( text, NUMBER_FONT, FONT_RENDER_CTXT );
		bound     = ( Rectangle2D.Float ) tl.getBounds (  );

		//Util.info("bound = " + bound);
		return bound.width;
	}

	private float drawTextUsingCurrentVertLoc ( 
	    String     text,
	    Graphics2D g2
	 )
	{
		//Util.info("text = " + text);
		if ( ( text == null ) || text.equals ( "" ) )
		{
			return 0;
		}

		if ( horizontalLocationOfRelation <= 0 )
		{
			return 0;
		}

		TextLayout        tl    = new TextLayout( text, SYMBOL_FONT, FONT_RENDER_CTXT );
		Rectangle2D.Float bound = ( Rectangle2D.Float ) tl.getBounds (  );

		float             xAdjustment = bound.width / 2.0f;

		//int yAdjustment = ( int ) ( bound.height / 2.0f );
		tl.draw ( 
		    g2,
		    horizontalLocationOfRelation + xAdjustment,
		    currentVertLocationOfSymbol - WORM_LINE_THICKNESS - 1 - SYMBOL_LINE_LENGTH
		 );

		//tl.draw(g2, 100.0f, 100.0f);
		//return bound.height;
		tl        = new TextLayout( text, NUMBER_FONT, FONT_RENDER_CTXT );
		bound     = ( Rectangle2D.Float ) tl.getBounds (  );

		//Util.info("bound = " + bound);
		return bound.width;
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
		int    xAdd1 = ( int ) ( y / len * 3.0 );
		int    yAdd1 = ( int ) ( x / len * 3.0 );

		int    xAdd2 = ( int ) ( x / len * 6.0 );
		int    yAdd2 = ( int ) ( y / len * 6.0 );
		int    xSub  = ( int ) ( x / len * 2.0 );
		int    ySub  = ( int ) ( y / len * 2.0 );

		//int xAdd2 = ( int ) ( x / len * 12.0 );
		//int yAdd2 = ( int ) ( y / len * 12.0 );
		//int xSub  = ( int ) ( x / len * 7.0 );
		//int ySub  = ( int ) ( y / len * 7.0 );
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
		int yAdd = ( int ) ( x / len * 6.0 );
		int xAdd = ( int ) ( y / len * 6.0 );
		int xSub = ( int ) ( x / len * 2.0 );
		int ySub = ( int ) ( y / len * 2.0 );
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
        if(listOfOtherContins.size() <= 0) return false;
        if(to == from || to-from <=0) return false;
        else return true;
    }
}
