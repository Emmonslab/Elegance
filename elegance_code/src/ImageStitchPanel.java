import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;

import java.util.Vector;

import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.media.jai.*;


/**
 * A container for ModifiableImage objects. It can monitor movement and mouse clicks in
 * addition to being able to modify any of the ModifiableImage objects that it holds.
 *
 * @author zndavid
 * @version 1.0
 */
public class ImageStitchPanel
	extends JComponent
	implements MouseListener,
		MouseMotionListener
{
	Vector      listOfSources       = new Vector(  );
	private int selectedSourceIndex = 0;

	/** The image's SampleModel. */
	protected SampleModel sampleModel;

	/** The image's ColorModel or one we supply. */
	protected ColorModel colorModel;

	/** Description of the Field */
	protected int componentWidth;

	/** Description of the Field */
	protected int componentHeight;

	/** Brightness and contrast control */
	protected BufferedImageOp biop                  = null;
	private boolean           selectedSourceChanged = false;
	private boolean           firstTimeInitializing = true;
	private String            preferredTitle        = "Image Stitching Module";
    private boolean isDragging = false;
	/**
	 * Constructs a panel with a size of 64, 64 and no image
	 */
	public ImageStitchPanel (  )
	{
		super(  );
		componentWidth      = 64;
		componentHeight     = 64;
		setPreferredSize ( new Dimension( componentWidth, componentHeight ) );
	}

	/**
	 * Constructs an ImageDisplay to display a PlanarImage.
	 *
	 * @param im Description of the Parameter
	 * @param file The filename of the image
	 */
	public ImageStitchPanel ( 
	    PlanarImage im,
	    String      file
	 )
	{
		super(  );

		ModifiableImage mig = new ModifiableImage( im, file );
		listOfSources.add ( mig );
		preferredTitle = "Image Stitching Module - " + file;
		initialize (  );
	}

	/**
	 * Constructs an ImageDisplay to display a number of PlanarImage objects.
	 *
	 * @param im An array of all the PlanarImage objects
	 *
	 * @see ImageStitchPanel#addImage
	 * @deprecated use the single file constructor and then use the addImage function
	 *             iteratively
	 */
	public ImageStitchPanel ( PlanarImage [] im )
	{
		super(  );

		for ( int i = 0; i < im.length; i++ )
		{
			ModifiableImage mig = new ModifiableImage( im [ i ], "" );
			listOfSources.addElement ( mig );
		}

		initialize (  );
	}

	/**
	 * Constructs an ImageStitchPanel of fixed size (no image)
	 *
	 * @param width - display width
	 * @param height - display height
	 */
	public ImageStitchPanel ( 
	    int width,
	    int height
	 )
	{
		super(  );
		componentWidth      = width;
		componentHeight     = height;
		setPreferredSize ( new Dimension( componentWidth, componentHeight ) );
	}

	/**
	 * Initializes the ImageStitchPanel
	 */
	private synchronized void initialize (  )
	{
		if ( listOfSources.size (  ) == 0 )
		{
			return;
		}

		//componentWidth = sources[0].getWidth();
		//componentHeight = sources[0].getHeight();
		//componentWidth = getWidth();
		//componentHeight = getHeight();
		componentHeight     = 400;
		componentWidth      = 500;
		setPreferredSize ( new Dimension( componentWidth, componentHeight ) );

		//Util.info("componentWidth = " + componentWidth + " and height = " + componentHeight);
		//this.sampleModel = ((ModifiableImage)listOfSources.elementAt(0)).getImage().getSampleModel();
		// First check whether the opimage has already set a suitable ColorModel
		//this.colorModel = ((ModifiableImage)listOfSources.elementAt(0)).getImage().getColorModel();
		//if ( this.colorModel == null )
		//{
		// If not, then create one.
		//	this.colorModel = PlanarImage.createColorModel( this.sampleModel );
		//	if ( this.colorModel == null )
		//	{
		//		throw new IllegalArgumentException( "no color model" );
		//	}
		//}
		for ( int i = 0; i < listOfSources.size (  ); i++ )
		{
			( ( ModifiableImage ) listOfSources.elementAt ( i ) ).initialize (  );
		}

		if ( firstTimeInitializing )
		{
			addMouseListener ( this );
			addMouseMotionListener ( this );
			firstTimeInitializing = false;
		}

		//if ( getMouseListeners (  ).length == 0 )
		//{
		//addMouseListener ( this );
		//}
		//if ( getMouseMotionListeners (  ).length == 0 )
		//{
		//addMouseMotionListener ( this );
		//}
	}

	/**
	 * Adds an image to the panel.
	 *
	 * @param im A planarImage
	 * @param file The name of the image file.
	 */
	public void addImage ( 
	    PlanarImage im,
	    String      file
	 )
	{
		ModifiableImage mig = new ModifiableImage( im, file );
		listOfSources.addElement ( mig );
		preferredTitle = "Image Stitching Module - " + file;
		initialize (  );
		repaint (  );
	}

	/**
	 * Adds a number of images into the ImageStitchPanel
	 *
	 * @param im An array of PlanarImage objects
	 *
	 * @see ImageStitchPanel#addImage
	 * @deprecated iteratively use the  addImage function
	 */
	public void addImages ( PlanarImage [] im )
	{
		for ( int i = 0; i < im.length; i++ )
		{
			addImage ( im [ i ], "" );
		}
	}

	/**
	 * Swaps the positions of two images. As a result, when the images are painted the
	 * positions of the images are interchanges ie if earlier the first image was above
	 * the second image, then the second image will now be above the first image.
	 *
	 * @param img1 The index of the first image
	 * @param img2 The index of the second image
	 */
	public void swapImages ( 
	    int img1,
	    int img2
	 )
	{
		if ( img1 > img2 )
		{
			int temp = img1;
			img1     = img2;
			img2     = temp;
		}

		ModifiableImage mig = ( ModifiableImage ) listOfSources.elementAt ( img1 );
		listOfSources.removeElementAt ( img1 );
		listOfSources.add ( 
		    img1,
		    ( ModifiableImage ) listOfSources.elementAt ( img2 - 1 )
		 );
		listOfSources.removeElementAt ( img2 );
		listOfSources.add ( img2, mig );
	}

	/**
	 * Removes the image from its current location and puts at the last position in the
	 * listOfSources. As a result, the image will be painted last and hence will be
	 * painted at the top. This function is useful in providinf the 'Bring to top'
	 * functionality.
	 *
	 * @param img1 The index of the image
	 */
	public void putAtLast ( int img1 )
	{
		ModifiableImage mig = ( ModifiableImage ) listOfSources.elementAt ( img1 );
		listOfSources.removeElementAt ( img1 );
		listOfSources.add ( mig );
	}

	/**
	 * Removes an image from the listOfSources
	 *
	 * @param img1 The index of the selected image
	 */
	public void removeImage ( int img1 )
	{
		if ( ( listOfSources.size (  ) > img1 ) && ( img1 >= 0 ) )
		{
			listOfSources.removeElementAt ( img1 );

			if ( listOfSources.size (  ) > 0 )
			{
				preferredTitle =
					"Image Stitching Module - "
					+ ( ( ModifiableImage ) listOfSources.elementAt ( 0 ) ).fileName;
			}
			else
			{
				preferredTitle = "Image Stitching Module";
			}
		}
	}

	/**
	 * Removes all the images from the listOfSources
	 */
	public void removeAllImages (  )
	{
		listOfSources.removeAllElements (  );
		preferredTitle = "Image Stitching Module";
	}

	/**
	 * Assigns a new value to the selectedSourceIndex variable
	 *
	 * @param index The new value
	 */
	public void setSelectedSourceIndex ( int index )
	{
		selectedSourceIndex = index;
	}

	/**
	 * Returns the selectedSourceIndex value
	 *
	 * @return The selectedSourceIndex value
	 */
	public int getSelectedSourceIndex (  )
	{
		return selectedSourceIndex;
	}

	/**
	 * Records a new size. Called by the AWT.
	 *
	 * @param g The new bounds value
	 */

	/*public void setBounds( int x, int y, int width, int height )
	   {
	           Insets  insets  = getInsets();
	           //int     w;
	           //int     h;
	           componentWidth = width + insets.left + insets.right;
	           componentHeight = height + insets.top + insets.bottom;
	           super.setBounds( x + shift_x, y + shift_y, componentWidth, componentHeight );
	   }*/
	//public void paint ( Graphics g )
	//{
	//	paintComponent ( g );
	//}

	/**
	 * Paint the image onto a Graphics object. The painting is performed sequentially
	 * till all the ModifiableImage objects in the listOfSources are painted.
	 *
	 * @param g The Graphics object associated with this component
	 */
	//public synchronized void paintComponent ( Graphics g )
    public void paint ( Graphics g )
	{
		componentHeight     = getHeight (  );
		componentWidth      = getWidth (  );

        GraphicsJAI g2D = null;
		Graphics2D g2      = null;

		if ( g instanceof Graphics2D )
		{
			g2 = ( Graphics2D ) g;
            g2D = GraphicsJAI.createGraphicsJAI(g2, this);
		}
		else
		{
			return;
		}

		int numberOfSources = listOfSources.size (  );

		//Util.info("number of sources = "+numberOfSources);
		g2D.setColor ( getBackground (  ) );

		//g2D.setColor(Color.BLACK);
		//g2D.fillRect( 0, 0, componentWidth, componentHeight );
		g2D.clearRect ( 0, 0, componentWidth, componentHeight );
        //g2D.setClip(0,0, componentWidth, componentHeight);

		if ( numberOfSources <= 0 )
		{
			return;
		}

		//long start = System.currentTimeMillis();
		for ( int i = 0; i < numberOfSources; i++ )
		{
			ModifiableImage mig = ( ModifiableImage ) listOfSources.elementAt ( i );
			mig.setParentWidth ( componentWidth );
			mig.setParentHeight ( componentHeight );

			RenderedImage bimg = mig.getAsRenderedImage (  );

			//RenderedImage bimg = mig.getAsRenderedImage(componentWidth, componentHeight);
			if ( bimg != null )
			{
				//biWidth = bimg.getWidth();
				//biHeight = bimg.getHeight();
				//int xTransform = (mig.getXOrigin() > 0) ? mig.getXOrigin(): 0;
				//int yTransform = (mig.getYOrigin() > 0) ? mig.getYOrigin(): 0;
				AffineTransform transform =
					AffineTransform.getTranslateInstance ( 
					    mig.getXOrigin (  ),
					    mig.getYOrigin (  )
					 );

				//if(xTransform > 0 || yTransform > 0)
				//{
				//	AffineTransform  transform = AffineTransform.getTranslateInstance
				//				( xTransform, yTransform);
				g2D.drawRenderedImage ( bimg, transform );

				//}
				//else
				//{
				//	g2D.drawRenderedImage( bimg, null );
				//}
			}

			//bimg.flush();
		}

		//Util.info("time taken to draw = " + (System.currentTimeMillis() - start));
		//Util.info( "originX = " + originX + " originY =" + originY );

		/*int         transX      = -originX;
		   int         transY      = -originY;
		   // Get the clipping rectangle and translate it into image coordinates.
		   Rectangle   clipBounds  = g.getClipBounds();
		   if ( clipBounds == null )
		   {
		           clipBounds = new Rectangle( 0, 0, componentWidth, componentHeight );
		   }
		   // clear the background (clip it) [minimal optimization here]
		   if ( transX > 0 ||
		           transY > 0 ||
		           transX < ( componentWidth - sources[0].getWidth() ) ||
		           transY < ( componentHeight - sources[0].getHeight() ) )
		   {
		           g2D.setColor( getBackground() );
		           g2D.fillRect( 0, 0, componentWidth, componentHeight );
		   }
		   clipBounds.translate( -transX, -transY );
		   // Determine the extent of the clipping region in tile coordinates.
		   int         txmin;
		   // Determine the extent of the clipping region in tile coordinates.
		   int         txmax;
		   // Determine the extent of the clipping region in tile coordinates.
		   int         tymin;
		   // Determine the extent of the clipping region in tile coordinates.
		   int         tymax;
		   int         ti;
		   int         tj;
		   txmin = XtoTileX( clipBounds.x );
		   txmin = Math.max( txmin, minTileX );
		   txmin = Math.min( txmin, maxTileX );
		   txmax = XtoTileX( clipBounds.x + clipBounds.width - 1 );
		   txmax = Math.max( txmax, minTileX );
		   txmax = Math.min( txmax, maxTileX );
		   tymin = YtoTileY( clipBounds.y );
		   tymin = Math.max( tymin, minTileY );
		   tymin = Math.min( tymin, maxTileY );
		   tymax = YtoTileY( clipBounds.y + clipBounds.height - 1 );
		   tymax = Math.max( tymax, minTileY );
		   tymax = Math.min( tymax, maxTileY );
		   Insets      insets      = getInsets();
		   //Util.info( "tymax = " + tymax + " tymin = " + tymin );
		   // Loop over tiles within the clipping region
		   for ( tj = tymin; tj <= tymax; tj++ )
		   {
		           for ( ti = txmin; ti <= txmax; ti++ )
		           {
		                   int  tx  = TileXtoX( ti );
		                   int  ty  = TileYtoY( tj );
		                   try
		                   {
		                           Raster  tile  = sources[0].getTile( ti, tj );
		                           if ( tile != null )
		                           {
		                                   DataBuffer      dataBuffer  = tile.getDataBuffer();
		                                   WritableRaster  wr          = tile.createWritableRaster( sampleModel,
		                                           dataBuffer,
		                                           null );
		                                   BufferedImage   bi          = new BufferedImage( colorModel,
		                                           wr,
		                                           colorModel.isAlphaPremultiplied(),
		                                           null );
		                                   // correctly handles band offsets
		                                   if ( brightnessAndContrastEnabled == true )
		                                   {
		                                           SampleModel      sm        = sampleModel.createCompatibleSampleModel( tile.getWidth(),
		                                                   tile.getHeight() );
		                                           WritableRaster   raster    = RasterFactory.createWritableRaster( sm, null );
		                                           BufferedImage    bimg      = new BufferedImage( colorModel,
		                                                   raster,
		                                                   colorModel.isAlphaPremultiplied(),
		                                                   null );
		                                           // don't move this code
		                                           ByteLookupTable  lutTable  = new ByteLookupTable( 0, lutData );
		                                           LookupOp         lookup    = new LookupOp( lutTable, null );
		                                           lookup.filter( bi, bimg );
		                                           g2D.drawImage( bimg, biop, tx + transX + insets.left, ty + transY + insets.top );
		                                   }
		                                   else
		                                   {
		                                           AffineTransform  transform;
		                                           transform = AffineTransform.getTranslateInstance( tx + transX + insets.left,
		                                                   ty + transY + insets.top );
		                                           g2D.drawRenderedImage( bi, transform );
		                                   }
		                           }
		                   }
		                   catch ( Exception e )
		                   {
		                           //Util.info( "ti = " + ti + " tj(for y) = " + tj );
		                   }
		           }
		   }*/
	}

	// mouse interface

	/**
	 * The function that handles the event when the mouse enters this component.
	 * Currently, this function does nothing.
	 *
	 * @param e A MouseEvent object
	 */
	public final void mouseEntered ( MouseEvent e ) {}

	/**
	 * The function that handles the event when the mouse exits this component.
	 * Currently, this function does nothing.
	 *
	 * @param e A MouseEvent object
	 */
	public final void mouseExited ( MouseEvent e ) {}

	/**
	 * The function that handles the event when the mouse button is pressed inside this
	 * component. Currently, this calculates the image that the mouse was clicked on and
	 * updates the selectedSourceIndex and the preferredTitle values accordingly.
	 *
	 * @param e A MouseEvent object
	 */
	public void mousePressed ( MouseEvent e )
	{
		//Point  p     = e.getPoint();
		//int    mods  = e.getModifiers();
		//if ( odometer != null )
		//{
		//String  output  = " (" + p.x + ", " + p.y + ")";
		//	String  output  = getOdometerString( p );
		//	odometer.setText( output );
		//}
		if ( ApplicationProperties.stitchFrameLocked )
		{
			int num = listOfSources.size (  );

			for ( int i = 0; i < num; i++ )
			{
				ModifiableImage mig = ( ModifiableImage ) listOfSources.elementAt ( i );
				mig.setLast_x ( mig.getXOrigin (  ) - e.getX (  ) );
				mig.setLast_y ( mig.getYOrigin (  ) - e.getY (  ) );
			}
		}
		else
		{
			calculateAndSetSelectedItem ( e.getPoint (  ) );

			if ( 
			    ( selectedSourceIndex >= listOfSources.size (  ) )
				    || ( selectedSourceIndex < 0 )
			 )
			{
				return;
			}

			ModifiableImage mig =
				( ModifiableImage ) listOfSources.elementAt ( selectedSourceIndex );
			mig.setLast_x ( mig.getXOrigin (  ) - e.getX (  ) );
			mig.setLast_y ( mig.getYOrigin (  ) - e.getY (  ) );
			setPreferredTitle ( "Image Stitching Module - " + mig.fileName );

			//last_y = getYOrigin() + e.getY();
			//Util.info( "last_x =" + mig.getLast_x() + " y=" + mig.getLast_y());
			//last_x = -e.getX();
			//last_y = -e.getY();
		}
	}

	/**
	 * The function that handles the event when the mouse button is released inside this
	 * component. Currently, this function does nothing.
	 *
	 * @param e A MouseEvent object
	 */
	public final void mouseReleased ( MouseEvent e )
	{
		//Point  p  = e.getPoint();
		//if ( odometer != null )
		//{
		//String  output  = " (" + p.x + ", " + p.y + ")";
		//	String  output  = getOdometerString( p );
		//	odometer.setText( output );
		//}
		//setOrigin( e.getX() - last_x, e.getY() - last_y );
        if(isDragging)
        {
            repaint();
            isDragging = false;
        }
	}

	/**
	 * The function that handles the event when the mouse button is clicked inside this
	 * component. Currently, this function does nothing.
	 *
	 * @param e A MouseEvent object
	 */
	public final void mouseClicked ( MouseEvent e )
	{
		//calculateAndSetSelectedItem(e.getPoint());
		//Util.info( "no of clicks = " + e.getClickCount() );

		/*if ( e.getClickCount() >= 2 )
		   {
		           Point  p   = e.getPoint();
		           Point  p1  = getScreenPointAsImagePoint( p );
		           if ( ( p1.x < 0 ) || ( p1.y < 0 ) || ( p1.x > originalSource.getWidth() ) || ( p1.y > originalSource.getHeight() ) )
		           {
		                   //Util.info( "1) point p1 = " + p1 + " width =" + originalSource.getWidth() + " height = " + originalSource.getHeight() );
		                   //Util.info( "returning from mouse clicked" );
		                   return;
		           }
		           else
		           {
		                   addPoint( p1 );
		                   repaint();
		           }
		   }*/
	}

	/**
	 * The function that handles the event when the mouse is moved in this component.
	 * Currently, this function does nothing.
	 *
	 * @param e A MouseEvent object
	 */
	public final void mouseMoved ( MouseEvent e )
	{
		/*Point  p  = e.getPoint();
		   if ( odometer != null )
		   {
		           //String  output  = " (" + p.x + ", " + p.y + ")";
		           String  output  = getOdometerString( p );
		           odometer.setText( output );
		   }*/
	}

	/**
	 * The function that handles the event when the mouse is dragged inside this
	 * component. This function drags the selected image or all the images, depending on
	 * whether they are locked or not.
	 *
	 * @param e A MouseEvent object
	 */
	public final void mouseDragged ( MouseEvent e )
	{
		if ( ApplicationProperties.stitchFrameLocked )
		{
			int num = listOfSources.size (  );

			for ( int i = 0; i < num; i++ )
			{
				ModifiableImage mig = ( ModifiableImage ) listOfSources.elementAt ( i );
				mig.setOrigin ( 
				    mig.getLast_x (  ) + e.getX (  ),
				    mig.getLast_y (  ) + e.getY (  )
				 );
			}
		}
		else
		{
			//mousePressed( e );
			//setOrigin( last_x + e.getX(), last_y + e.getY() );
			if ( 
			    ( selectedSourceIndex >= listOfSources.size (  ) )
				    || ( selectedSourceIndex < 0 )
			 )
			{
				return;
			}

			ModifiableImage mig =
				( ModifiableImage ) listOfSources.elementAt ( selectedSourceIndex );

			//mig.setOrigin( e.getX() - mig.getLast_x(), e.getY() - mig.getLast_y() );
			//Util.info(mig.getLast_x() + " " + mig.getLast_y());
			mig.setOrigin ( 
			    mig.getLast_x (  ) + e.getX (  ),
			    mig.getLast_y (  ) + e.getY (  )
			 );

			//initialize();
			//Util.info( "Origin set to " + mig.getXOrigin() + ", " + mig.getYOrigin());
		}

		//repaint (  );
        isDragging = true;
	}

	/**
	 * Sets the origin of all the images to the mimimum possible origin without the
	 * images being cut off.
	 *
	 * @see ImageStitchPanel#unSynchronizeImages
	 */
	public void synchronizeImages (  )
	{
		//Util.info("synchronizing Images started");
		Point p = getMinimumNewOrigin (  );

		//Util.info("minimum origin = " + p);
		changeOrigins ( p );
	}

	/**
	 * Sets the origin of all the images to the earlier origins. It is the opposite of
	 * the synchronizeImages function.
	 *
	 * @see ImageStitchPanel#synchronizeImages
	 */
	public void unSynchronizeImages (  )
	{
		int num = listOfSources.size (  );

		for ( int i = 0; i < num; i++ )
		{
			ModifiableImage mig = ( ModifiableImage ) listOfSources.elementAt ( i );
			mig.setOrigin ( mig.getLast_x (  ), mig.getLast_y (  ) );
		}
	}

	/**
	 * Saves the current layout as a new JPEG image into the file specified
	 *
	 * @param filename The name of the file into which the stitched image is saved
	 */
	public void saveStitchedImageTo ( String filename )
	{
		synchronizeImages (  );

        //Util.info("At start " + System.currentTimeMillis());
		Point         p   = getResultantNewImageSize (  );
        //Util.info("got new image size " + System.currentTimeMillis());
        BufferedImage bim = null;
        if(isComplexSaveRequired())
        {
            bim = getResultantNewImage ( p );
        }
        else
        {
            //bim = new BufferedImage(p.x, p.y, BufferedImage.TYPE_4BYTE_ABGR);
            bim = new BufferedImage(p.x, p.y, BufferedImage.TYPE_3BYTE_BGR);
            this.paint(bim.getGraphics());
        }
        //Util.info("created unsaved image " + System.currentTimeMillis());
		//JAI.create ( "filestore", bim, filename, "JPEG" );
        JAI.create ( "filestore", bim, filename, "TIFF" );
        //Util.info("After save " + System.currentTimeMillis());
		JOptionPane.showMessageDialog ( null, "The image was successfully saved" );
        //Util.info("At end " + System.currentTimeMillis());
        unSynchronizeImages (  );
		repaint (  );
	}

	private Point getMinimumOrigin (  )
	{
		int   num = listOfSources.size (  );
		Point pt = new Point( 100000, 100000 );

		for ( int i = 0; i < num; i++ )
		{
			ModifiableImage mig = ( ModifiableImage ) listOfSources.elementAt ( i );

			//Util.info("origin No " + (i+1) + " = " + mig.getXOrigin() + ", " + mig.getYOrigin());
			if ( pt.x > mig.getXOrigin (  ) )
			{
				pt.x = mig.getXOrigin (  );
			}

			if ( pt.y > mig.getYOrigin (  ) )
			{
				pt.y = mig.getYOrigin (  );
			}
		}

		//Util.info("minimum origin = " + pt);
		return pt;
	}

	private Point getMinimumNewOrigin (  )
	{
		int   num = listOfSources.size (  );
		Point pt = new Point( 1000000, 1000000 );

		for ( int i = 0; i < num; i++ )
		{
			ModifiableImage mig = ( ModifiableImage ) listOfSources.elementAt ( i );
			Point           ori = mig.getActualOrigin (  );

			//Util.info("origin No " + (i+1) + " = " + ori);
			if ( pt.x > ori.x )
			{
				pt.x = ori.x;
			}

			if ( pt.y > ori.y )
			{
				pt.y = ori.y;
			}
		}

		//Util.info("minimum origin = " + pt);
		return pt;
	}

	private void changeOrigins ( Point p )
	{
		int num = listOfSources.size (  );

		for ( int i = 0; i < num; i++ )
		{
			ModifiableImage mig  = ( ModifiableImage ) listOfSources.elementAt ( i );
			int             oldX = mig.getXOrigin (  );
			int             oldY = mig.getYOrigin (  );
			mig.setOrigin ( oldX - p.x, oldY - p.y );

			//Util.info("p= " + p);
			//Util.info("origin of img " + (i+1) + " set to  " + (oldX - p.x) + ", " + (oldY - p.y));
			mig.setLast_x ( oldX );
			mig.setLast_y ( oldY );
		}
	}

	private Point getResultantImageSize (  )
	{
		Point p   = new Point( 0, 0 );
		int   num = listOfSources.size (  );

		for ( int i = 0; i < num; i++ )
		{
			ModifiableImage mig  = ( ModifiableImage ) listOfSources.elementAt ( i );
			int             oriX = mig.getXOrigin (  );
			int             oriY = mig.getYOrigin (  );
			int             comW = mig.getComponentWidth (  );
			int             comH = mig.getComponentHeight (  );

			//Util.info("size of image " + (i+1) + " = " + comW + ", " + comH);
			if ( p.x < ( oriX + comW ) )
			{
				p.x = oriX + comW;
			}

			if ( p.y < ( oriY + comH ) )
			{
				p.y = oriY + comH;
			}
		}

		//Util.info("maximum size of image = " + p);
		return p;
	}

	private Point getResultantNewImageSize (  )
	{
		Point p   = new Point( 0, 0 );
		int   num = listOfSources.size (  );

		for ( int i = 0; i < num; i++ )
		{
			ModifiableImage mig  = ( ModifiableImage ) listOfSources.elementAt ( i );
			Point           ori  = mig.getActualOrigin (  );
			int             oriX = ori.x;
			int             oriY = ori.y;
			int             comW = mig.getComponentWidth (  );
			int             comH = mig.getComponentHeight (  );

			//Util.info("size of image " + (i+1) + " = " + comW + ", " + comH);
			if ( p.x < ( oriX + comW ) )
			{
				p.x = oriX + comW;
			}

			if ( p.y < ( oriY + comH ) )
			{
				p.y = oriY + comH;
			}
		}

		//Util.info("maximum size of image = " + p);
		return p;
	}

	private BufferedImage getResultantImage ( Point p )
	{
		int width  = p.x;
		int height = p.y;
		int num    = listOfSources.size (  );

		if ( num <= 0 )
		{
			return null;
		}

		//BufferedImage [] images      = new BufferedImage[ num ];
		RenderedImage [] images      = new RenderedImage[ num ];
		Raster []        imageRaster = new Raster[ num ];

		for ( int i = 0; i < num; i++ )
		{
			System.out.print ( "Loading image " + ( i + 1 ) + " ..." );

			ModifiableImage mig = ( ModifiableImage ) listOfSources.elementAt ( i );

			//images [ i ]          = mig.getAsBufferedImage (  );
			images [ i ]          = mig.getAsRenderedImage (  );
			imageRaster [ i ]     = images [ i ].getData (  );
			imageRaster [ i ] =
				imageRaster [ i ].createTranslatedChild ( 
				    mig.getXOrigin (  ),
				    mig.getYOrigin (  )
				 );
			ELog.info ( " done." );
		}

		WritableRaster res        =
			imageRaster [ 0 ].createCompatibleWritableRaster ( width, height );
		ColorModel     colorMod = images [ 0 ].getColorModel (  );
		int            numBands = res.getNumBands (  );
		images = null;

		for ( int i = 0; i < width; i++ )
		{
			for ( int j = 0; j < height; j++ )
			{
				int [] pixel1 = new int[ numBands ];

				for ( int k = 0; k < pixel1.length; k++ )
				{
					pixel1 [ k ] = 0;
				}

				for ( int k = 0; k < imageRaster.length; k++ )
				{
					int [] pixel2 = new int[ numBands ];

					try
					{
						pixel2 = imageRaster [ k ].getPixel ( i, j, pixel2 );
					}
					catch ( ArrayIndexOutOfBoundsException ae )
					{
						for ( int l = 0; l < pixel2.length; l++ )
						{
							pixel2 [ l ] = 0;
						}
					}

					int sum = 0;

					for ( int l = 0; l < pixel2.length; l++ )
					{
						sum += pixel2 [ l ];
					}

					if ( sum != 0 )
					{
						for ( int l = 0; l < numBands; l++ )
						{
							pixel1 [ l ] = pixel2 [ l ];
						}
					}
				}

				res.setPixel ( i, j, pixel1 );
			}
		}

		BufferedImage returnedImage =
			new BufferedImage( colorMod, res, colorMod.isAlphaPremultiplied (  ), null );
		ELog.info ( "Got the new Image" );

		return returnedImage;
	}

	private BufferedImage getResultantNewImage ( Point p )
	{
		int width  = p.x;
		int height = p.y;
		int num    = listOfSources.size (  );

		if ( num <= 0 )
		{
			return null;
		}

		//BufferedImage [] images      = new BufferedImage[ num ];
		RenderedImage [] images      = new RenderedImage[ num ];
		Raster []        imageRaster = new Raster[ num ];

		for ( int i = 0; i < num; i++ )
		{
			System.out.print ( "Loading image " + ( i + 1 ) + " ..." );

			ModifiableImage mig = ( ModifiableImage ) listOfSources.elementAt ( i );

			//images [ i ]          = mig.getAsBufferedImage (  );
			images [ i ]          = mig.getAsRenderedImage (  );
			imageRaster [ i ]     = images [ i ].getData (  );

			Point ori             = mig.getActualOrigin (  );
			imageRaster [ i ] = imageRaster [ i ].createTranslatedChild ( ori.x, ori.y );
			ELog.info ( " done." );
		}

		WritableRaster res =
			imageRaster [ 0 ].createCompatibleWritableRaster ( width, height );
		ColorModel     colorMod = images [ 0 ].getColorModel (  );
		int            numBands = res.getNumBands (  );
		images = null;

		for ( int i = 0; i < width; i++ )
		{
			for ( int j = 0; j < height; j++ )
			{
				int [] pixel1 = new int[ numBands ];

				for ( int k = 0; k < pixel1.length; k++ )
				{
					pixel1 [ k ] = 0;
				}

				for ( int k = 0; k < imageRaster.length; k++ )
				{
					int [] pixel2 = new int[ numBands ];

					try
					{
						pixel2 = imageRaster [ k ].getPixel ( i, j, pixel2 );
					}
					catch ( ArrayIndexOutOfBoundsException ae )
					{
						for ( int l = 0; l < pixel2.length; l++ )
						{
							pixel2 [ l ] = 0;
						}
					}

					int sum = 0;

					for ( int l = 0; l < pixel2.length; l++ )
					{
						sum += pixel2 [ l ];
					}

					if ( sum != 0 )
					{
						for ( int l = 0; l < numBands; l++ )
						{
							pixel1 [ l ] = pixel2 [ l ];
						}
					}
				}

				res.setPixel ( i, j, pixel1 );
			}
		}

		BufferedImage returnedImage =
			new BufferedImage( colorMod, res, colorMod.isAlphaPremultiplied (  ), null );
		ELog.info ( "Got the new Image" );

		return returnedImage;
	}

	private void calculateAndSetSelectedItem ( Point p )
	{
		int num = listOfSources.size (  );

		for ( int i = 0; i < num; i++ )
		{
			//int index = num - i -1;
			//int index = i;
			ModifiableImage mig  = ( ModifiableImage ) listOfSources.elementAt ( i );
			Rectangle       rect =
				new Rectangle( 
				    mig.getXOrigin (  ),
				    mig.getYOrigin (  ),
				    mig.getComponentWidth (  ),
				    mig.getComponentHeight (  )
				 );

			if ( rect.contains ( p ) )
			{
				selectedSourceIndex       = i;
				selectedSourceChanged     = true;
			}
		}
	}

	/**
	 * returns true if the selected source has changed. Returns false otherwise.
	 *
	 * @return returns true if the selected source has changed. Returns false otherwise.
	 */
	public boolean isSelectedSourceChanged (  )
	{
		return selectedSourceChanged;
	}

	/**
	 * Sets the selectedSourceChanged tag
	 *
	 * @param newSelectedSourceChanged The new value
	 */
	public void setSelectedSourceChanged ( boolean newSelectedSourceChanged )
	{
		selectedSourceChanged = newSelectedSourceChanged;
	}

	/**
	 * Returns the preferred title of the frame that contains this panel
	 *
	 * @return The preferred title
	 */
	public String getPreferredTitle (  )
	{
		return preferredTitle;
	}

	/**
	 * Sets the preferred title of the frame that contains this panel
	 *
	 * @param newPreferredTitle The new preferredTitle value
	 */
	public void setPreferredTitle ( String newPreferredTitle )
	{
		preferredTitle = newPreferredTitle;
	}
    private boolean isComplexSaveRequired()
    {
        for (int i = 0; i < listOfSources.size(); i++) 
        {
            ModifiableImage mi = (ModifiableImage) listOfSources.elementAt(i);
            double rotRad = mi.getRotation();
            if(rotRad == 0 || rotRad == Math.toRadians(90) || rotRad == Math.toRadians(180) || rotRad == Math.toRadians(-90) || rotRad == Math.toRadians(-180))
            {
                continue;
            }
            else
            {
                return true;
            }
        }
        return false;
    }
}
