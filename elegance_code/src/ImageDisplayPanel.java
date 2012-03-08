import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.renderable.ParameterBlock;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.media.jai.GraphicsJAI;
import javax.media.jai.InterpolationNearest;
import javax.media.jai.JAI;
import javax.media.jai.LookupTableJAI;
import javax.media.jai.PlanarImage;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;

/**
 * An output widget for a PlanarImage. ImageDisplay subclasses
 * javax.swing.JComponent, and can be used in any context that calls for a
 * JComponent. It monitors resize and update events and automatically requests
 * tiles from its source on demand.
 * 
 * <p>
 * Due to the limitations of BufferedImage, only TYPE_BYTE of band 1, 2, 3, 4,
 * and TYPE_USHORT of band 1, 2, 3 images can be displayed using this widget.
 * </p>
 * It implements the MouseListener, MouseMotionListener and the KeyListener
 * interfaces to provide additional functionality.
 * 
 * @author zndavid
 * @version 1.0
 */
public class ImageDisplayPanel extends JComponent implements MouseListener, MouseMotionListener, KeyListener {

	public static Relation relation = new Relation();
	public static CellObject selectedObj = new CellObject();
	/** The selected relation if any */
	public static Relation selectedrelation = new Relation();
	public static CellObject selectedsyn = new CellObject();
	/** The list of relations */
	public static Vector listOfRelations = new Vector();
	public static CellObject sy = new CellObject();
	/** The source PlanarImage. */
	protected PlanarImage source;

	/** The original image */
	protected PlanarImage originalSource;

	/** The image's SampleModel. */

	// protected SampleModel sampleModel;
	/** The image's ColorModel or one we supply. */

	// protected ColorModel colorModel;
	/** The image's min X tile. */

	// protected int minTileX;
	/** The image's max X tile. */

	// protected int maxTileX;
	/** The image's min Y tile. */

	// protected int minTileY;
	/** The image's max Y tile. */

	// protected int maxTileY;
	/** The image's tile width. */

	// protected int tileWidth;
	/** The image's tile height. */

	// protected int tileHeight;
	/** The image's tile grid X offset. */

	// protected int tileGridXOffset;
	/** The image's tile grid Y offset. */

	// protected int tileGridYOffset;
	/** The x coordinate of the origin */
	protected int originX = 0;

	/** The y coordinate of the origin */
	protected int originY = 0;

	/** Description of the Field */

	// protected int shift_x = 0;
	/** Description of the Field */

	// protected int shift_y = 0;
	/** The previous x coordinate of the origin */
	protected int last_x;

	/** The previous y coordinate of the origin */
	protected int last_y;

	/** This displays the current x,y coordinates of the mouse */
	protected JLabel odometer = null;

	/** The component width */
	protected int componentWidth;

	/** The height of this component */
	protected int componentHeight;

	/** Brightness and contrast control */

	// protected BufferedImageOp biop = null;
	/**
	 * A boolean describing whether brightness and contrast can be changed or
	 * not.
	 */
	protected boolean brightnessAndContrastEnabled = false;

	/** Brightness value */
	protected int brightness = 0;

	/** Contrast value */
	protected int contrast = 1;

	/** Current zoom */
	protected float current_zoom = 0.2f;

	/** a temporary variable to store zoom value */
	protected float temp_zoom = 0.2f;

	/** Current rotation */
	protected double current_rotation = 0.0f;

	/** a temporary variable to store the rotation value */
	protected double temp_rotation = 0.0f;

	/**
	 * a byte array for performing the contrast and brightness change operations
	 */
	protected byte[] lutData;
	private boolean isSaved = true;
	private boolean isDragging = false;

	/** a list of the points that are currently drawn on the panel */
	protected Vector listOfPoints = new Vector();
	protected Vector listOfObjects = new Vector();

	private Point selectedPoint = null;
	private static String preObjName = "";
	private Point deletedPoint = null;
	private Relation deletedRelation = null;
	private String imageNumber = "";
	private static boolean isControlButtonPressed = false;
	private static boolean isAltButtonPressed = false;
	private static boolean isCPressed = false;
	private static boolean isMPressed = false;
	private static boolean isUPressed = false;
	private static boolean isVPressed = false;
	private static boolean isAPressed = false;
	private static boolean isNPressed = false;
	private static boolean newSyn = false;
	private static boolean toObjSyn = false;
	private static boolean addRecord = false;

	private boolean isMousePressedInSelectedPoint = false;
	private JTable table = null;
	private String username;

	/**
	 * Default constructor
	 */
	public ImageDisplayPanel(String name) {
		// super(
		// GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration()
		// );
		super();
		source = null;
		originalSource = null;
		lutData = new byte[256];
		username = name;
		for (int i = 0; i < 256; i++) {
			lutData[i] = (byte) i;
		}

		componentWidth = 64;
		componentHeight = 64;
		// setPreferredSize ( new Dimension( componentWidth, componentHeight )
		// );
		setOrigin(0, 0);
		setBrightnessAndContrastEnabled(true);
		setBackground(Color.BLACK);
	}

	/**
	 * Constructs an ImageDisplay to display a PlanarImage.
	 * 
	 * @param im
	 *            Description of the Parameter
	 * @param imageNo
	 *            The new image number
	 */
	public ImageDisplayPanel(PlanarImage im, String imageNo, String name) {
		// super(
		// GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration()
		// );
		super();
		username = name;
		source = im;
		originalSource = source;
		imageNumber = imageNo;

		initialize();

		lutData = new byte[256];

		for (int i = 0; i < 256; i++) {
			lutData[i] = (byte) i;
		}

		setBrightnessAndContrastEnabled(true);
		setBackground(Color.BLACK);
		setImageData(imageNo);
		setOrigin(originX, originY);
		setBrightnessAndContrast(brightness, (contrast / 200.0f) + 1);
		zoomAndRotate(current_zoom, (float) current_rotation);
		addListOfCellObjectsAndPoints(imageNo);

	}

	/**
	 * Constructs an ImageDisplay of fixed size (no image)
	 * 
	 * @param width
	 *            - display width
	 * @param height
	 *            - display height
	 */
	public ImageDisplayPanel(int width, int height, String name) {
		// super(
		// GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration()
		// );
		super();
		username = name;
		source = null;
		originalSource = source;
		lutData = new byte[256];

		for (int i = 0; i < 256; i++) {
			lutData[i] = (byte) i;
		}

		componentWidth = width;
		componentHeight = height;
		// setPreferredSize ( new Dimension( componentWidth, componentHeight )
		// );
		setOrigin(0, 0);
		setBrightnessAndContrastEnabled(true);
		setBackground(Color.BLACK);
	}

	/**
	 * Initializes the ImageDisplay.
	 */
	private synchronized void initialize() {
		if (source == null) {
			return;
		}

		// componentWidth = source.getWidth();
		// componentHeight = source.getHeight();
		componentWidth = getWidth();
		componentHeight = getHeight();

		// setPreferredSize ( new Dimension( componentWidth, componentHeight )
		// );

		// this.sampleModel = source.getSampleModel();
		// First check whether the opimage has already set a suitable ColorModel
		// this.colorModel = source.getColorModel();
		// if ( this.colorModel == null )
		// {
		// If not, then create one.
		// this.colorModel = PlanarImage.createColorModel( this.sampleModel );
		// if ( this.colorModel == null )
		// {
		// throw new IllegalArgumentException( "no color model" );
		// }
		// }
		// minTileX = source.getMinTileX();
		// maxTileX = source.getMinTileX() + source.getNumXTiles() - 1;
		// minTileY = source.getMinTileY();
		// maxTileY = source.getMinTileY() + source.getNumYTiles() - 1;
		// tileWidth = source.getTileWidth();
		// tileHeight = source.getTileHeight();
		// tileGridXOffset = source.getTileGridXOffset();
		// tileGridYOffset = source.getTileGridYOffset();
		if (getMouseListeners().length == 0) {
			addMouseListener(this);
		}

		if (getMouseMotionListeners().length == 0) {
			addMouseMotionListener(this);
		}

		if (getKeyListeners().length == 0) {
			addKeyListener(this);

			// Util.info("Added key listener");
		}

		// Util.info(getFocusListeners().length);
		this.requestFocusInWindow();

		// if(getFocusListeners().length <=1)
		// {
		// addFocusListener(this);
		// Util.info("added focus listener");
		// }
		// Util.info("focusable = " + isFocusable());
		// Util.info( "minTileX " + minTileX );
		// Util.info( "maxTileX " + maxTileX );
		// Util.info( "minTileY " + minTileY );
		// Util.info( "maxTileY " + maxTileY );
		// Util.info( "tileWidth " + tileWidth );
		// Util.info( "tileHeight " + tileHeight );
		// Util.info( "tileGridXOffset " + tileGridXOffset );
		// Util.info( "tileGridYOffset " + tileGridYOffset );
	}

	/**
	 * Changes the source image to a new PlanarImage.
	 * 
	 * @param im
	 *            Description of the Parameter
	 */
	public void set(PlanarImage im) {
		source = im;
		originalSource = source;
		initialize();
		repaint();
	}

	/**
	 * Description of the Method
	 * 
	 * @param im
	 *            Description of the Parameter
	 * @param x
	 *            Description of the Parameter
	 * @param y
	 *            Description of the Parameter
	 */
	public void set(PlanarImage im, int x, int y) {
		source = im;
		originalSource = source;
		initialize();
		setOrigin(x, y);
	}

	/**
	 * Gets the image attribute of the ImageDisplay object
	 * 
	 * @return The image value
	 */
	public PlanarImage getImage() {
		return source;
	}

	/**
	 * Gets the odometer attribute of the ImageDisplay object
	 * 
	 * @return The odometer value
	 */
	public final JLabel getOdometer() {
		if (odometer == null) {
			odometer = new JLabel();
			odometer.setVerticalAlignment(SwingConstants.CENTER);
			odometer.setHorizontalAlignment(SwingConstants.LEFT);
			odometer.setText(" ");

			// addMouseListener ( this );
			// addMouseMotionListener ( this );
			initialize();
		}

		return odometer;
	}

	/**
	 * Provides panning
	 * 
	 * @param x
	 *            The new origin value
	 * @param y
	 *            The new origin value
	 */
	public final void setOrigin(int x, int y) {
		// shift to box origin
		// originX = -x;
		// originY = -y;
		originX = x;
		originY = y;
		repaint();
	}

	/**
	 * Provides panning
	 * 
	 * @param x
	 *            The new origin value
	 * @param y
	 *            The new origin value
	 */
	public final void setOriginNoRepaint(int x, int y) {
		// shift to box origin
		// originX = -x;
		// originY = -y;
		originX = x;
		originY = y;
	}

	/**
	 * Gets the xOrigin attribute of the ImageDisplay object
	 * 
	 * @return The xOrigin value
	 */
	public int getXOrigin() {
		return originX;
	}

	/**
	 * Gets the yOrigin attribute of the ImageDisplay object
	 * 
	 * @return The yOrigin value
	 */
	public int getYOrigin() {
		return originY;
	}

	/**
	 * Records a new size. Called by the AWT.
	 * 
	 * @param msg
	 *            The new bounds value
	 */

	// public void setBounds( int x, int y, int width, int height )
	// {
	// Insets insets = getInsets();
	// int w;
	// int h;
	/*
	 * if ( source == null ) { w = width; h = height; } else { w =
	 * source.getWidth(); h = source.getHeight(); if ( width < w ) { w = width;
	 * } if ( height < h ) { h = height; } }
	 */

	// componentWidth = width + insets.left + insets.right;
	// componentHeight = height + insets.top + insets.bottom;
	// super.setBounds( x + shift_x, y + shift_y, componentWidth,
	// componentHeight );
	// }
	/**
	 * Sets the location attribute of the ImageDisplay object
	 * 
	 * @param msg
	 *            The new location value
	 */

	// public void setLocation( int x, int y )
	// {
	// shift_x = x;
	// shift_y = y;
	// super.setLocation( x, y );
	// }
	/**
	 * Description of the Method
	 * 
	 * @param msg
	 *            Description of the Parameter
	 */

	// private final int XtoTileX( int x )
	// {
	// return ( int ) Math.floor( ( double ) ( x - tileGridXOffset ) / tileWidth
	// );
	// }
	/**
	 * Description of the Method
	 * 
	 * @param msg
	 *            Description of the Parameter
	 */

	// private final int YtoTileY( int y )
	// {
	// return ( int ) Math.floor( ( double ) ( y - tileGridYOffset ) /
	// tileHeight );
	// }
	/**
	 * Description of the Method
	 * 
	 * @param msg
	 *            Description of the Parameter
	 */

	// private final int TileXtoX( int tx )
	// {
	// return tx * tileWidth + tileGridXOffset;
	// }
	/**
	 * Description of the Method
	 * 
	 * @param msg
	 *            Description of the Parameter
	 */

	// private final int TileYtoY( int ty )
	// {
	// return ty * tileHeight + tileGridYOffset;
	// }
	/**
	 * Description of the Method
	 * 
	 * @param msg
	 *            Description of the Parameter
	 */
	private static final void debug(String msg) {
		ELog.info(msg);
	}

	/**
	 * Description of the Method
	 * 
	 * @param v
	 *            Description of the Parameter
	 * 
	 * @return Description of the Return Value
	 */
	private final byte clampByte(int v) {
		if (v > 255) {
			return (byte) 255;
		} else if (v < 0) {
			return (byte) 0;
		} else {
			return (byte) v;
		}
	}

	/**
	 * Sets the brightnessAndContrastEnabled attribute of the ImageDisplay
	 * object
	 * 
	 * @param v
	 *            The new brightnessAndContrastEnabled value
	 */
	private final void setBrightnessAndContrastEnabled(boolean v) {
		brightnessAndContrastEnabled = v;

		// if ( brightnessAndContrastEnabled == true )
		// {
		// biop = new AffineTransformOp( new AffineTransform(),
		// AffineTransformOp.TYPE_NEAREST_NEIGHBOR );
		// }
		// else
		// {
		// biop = null;
		// }
	}

	/**
	 * Gets the brightnessAndContrastEnabled attribute of the ImageDisplay
	 * object
	 * 
	 * @return The brightnessAndContrastEnabled value
	 */
	public final boolean getBrightnessAndContrastEnabled() {
		return brightnessAndContrastEnabled;
	}

	/**
	 * Sets the brightness attribute of the ImageDisplay object
	 * 
	 * @param b
	 *            The new brightness value
	 */
	public final void setBrightness(int b) {
		if ((b != brightness) && (brightnessAndContrastEnabled == true)) {
			for (int i = 0; i < 256; i++) {
				lutData[i] = clampByte(i + b);
			}

			brightness = b;
			repaint();
		}
	}

	/**
	 * returns the current brightness
	 * 
	 * @return The current brightness
	 */
	public final int getBrightness() {
		return brightness;
	}

	/**
	 * Sets the brightness attribute of the ImageDisplay object
	 * 
	 * @param c
	 *            The new brightnessAndContrast value
	 */
	public final void setContrast(float c) {
		if ((c != contrast) && (brightnessAndContrastEnabled == true)) {
			for (int i = 0; i < 256; i++) {
				// float num = ( float ) lutData[i];
				lutData[i] = clampByte((int) (i * c));
			}

			contrast = (int) ((c - 1) * 200.0f);
			repaint();
		}
	}

	/**
	 * returns the current contrast
	 * 
	 * @return The current contrast value
	 */
	public final int getContrast() {
		return contrast;
	}

	/**
	 * Sets the brightnessAndContrast attribute of the ImageDisplay object
	 * 
	 * @param b
	 *            The new brightnessAndContrast value
	 * @param c
	 *            The new brightnessAndContrast value
	 */
	public final void setBrightnessAndContrast(int b, float c) {
		if (((b != brightness) || (c != contrast)) && (brightnessAndContrastEnabled == true)) {
			for (int i = 0; i < 256; i++) {
				// float num = ( float ) lutData[i];
				lutData[i] = clampByte((int) ((i + b) * c));
			}

			brightness = b;
			contrast = (int) ((c - 1) * 200.0f);
			zoomAndRotate(current_zoom, (float) ((current_rotation * 180.0) / Math.PI));
		}
	}

	/**
	 * Paint the image onto a Graphics object. The painting is performed
	 * tile-by-tile, and includes a grey region covering the unused portion of
	 * image tiles as well as the general background. At this point the image
	 * must be byte data.
	 * 
	 * @param g
	 *            Description of the Parameter
	 */
	// public synchronized void paintComponent ( Graphics g )
	public void paint(Graphics g) {
		Graphics2D g2 = null;
		GraphicsJAI g2D = null;
		if (g instanceof Graphics2D) {
			g2 = (Graphics2D) g;
			g2D = GraphicsJAI.createGraphicsJAI(g2, this);
		} else {
			return;
		}

		initialize();

		// if source is null, it's just a component
		// if ( source == null )
		// {
		g2D.setColor(getBackground());
		g2D.fillRect(0, 0, componentWidth, componentHeight);
		// Util.info("componentWidth = " + componentWidth + "
		// componentHeight = " + componentHeight);
		g2D.setClip(0, 0, componentWidth, componentHeight);

		// return;
		// }
		if (source != null) {
			AffineTransform transform = AffineTransform.getTranslateInstance(getXOrigin(), getYOrigin());
			// Util.info("startingdrawimage = " +
			// System.currentTimeMillis());
			g2D.drawRenderedImage(source, transform);
			// SampleModel sam = source.getSampleModel();
			// int requiredHeight = source.getHeight() > 512 ? 512 :
			// source.getHeight();
			// int requiredWidth = source.getWidth() > 512 ? 512 :
			// source.getWidth();
			// int requiredHeight = source.getHeight();
			// int requiredWidth = source.getWidth();

			// SampleModel newSam =
			// sam.createCompatibleSampleModel(requiredWidth, requiredHeight);
			// ColorModel colorModel = source.getColorModel();
			// ColorModel colorModel = ColorModel.getRGBdefault();
			// Raster tile = source.getData();
			// if(tile != null)
			// {
			// DataBuffer dataBuffer = tile.getDataBuffer();
			// WritableRaster wr = tile.createWritableRaster(sam, dataBuffer,
			// null);
			// BufferedImage bi = new BufferedImage(colorModel, wr,
			// colorModel.isAlphaPremultiplied(), null);
			// AffineTransform transform =
			// AffineTransform.getTranslateInstance(xLoc + getXOrigin(), yLoc+
			// getYOrigin());
			// g2D.drawRenderedImage(bi, transform);
			// }
			// Util.info("x tiles=" + source.getNumXTiles());
			// Util.info("y tiles=" + source.getNumYTiles());
			// Util.info(source.getTileWidth());
			// Util.info(source.getTileHeight());
			// BufferedImage bim = source.getAsBufferedImage( new
			// Rectangle(-getXOrigin(), -getYOrigin(), componentWidth,
			// componentHeight), null);
			// g2D.drawImage(bim, null, 0, 0);
			// SampleModel sam = source.getSampleModel();
			// Util.info(imageNumber +" " + sam);
			// Util.info(sam.getDataType());
			// Util.info(sam.getHeight());
			// Util.info(sam.getWidth());
			// Util.info(sam.getTransferType());
			// Util.info(sam.getSampleSize()[0]);
			// Util.info(sam.getNumDataElements());
			// Util.info(sam.getNumBands());
			// Util.info("******************");

			// SampleModel sampleModel = source.getSampleModel();
			// ColorModel colorModel = source.getColorModel();
			// ColorSpace colorSpace = source.getColorModel().getColorSpace();

			// Util.info(imageNumber + " " + colorSpace.getType() + " "
			// + colorSpace.getNumComponents());
			// Util.info(source.getNumBands() +" "+
			// source.getNumSources());
			// Util.info(imageNumber + " " +
			// sampleModel.getTransferType() + " " + sampleModel.getDataType() +
			// " " + sampleModel.getNumBands() + " " +
			// sampleModel.getNumDataElements());
			// Util.info(imageNumber + " " + colorModel);
			// for (int tx = 0 ;tx < source.getNumXTiles() ; tx++)
			// {
			// int xLoc = source.tileXToX(tx);
			// if(xLoc > componentWidth-getXOrigin()) break;
			// if(xLoc + source.getTileWidth() < getXOrigin()) continue;
			// for( int ty = 0; ty < source.getNumYTiles(); ty++)
			// {
			// int yLoc = source.tileYToY(ty);
			// //if(yLoc > componentHeight-getYOrigin()) break;
			// //if(yLoc + source.getTileHeight() < getYOrigin()) continue;
			// Raster tile = source.getTile(tx, ty);
			// if(tile != null)
			// {
			// DataBuffer dataBuffer = tile.getDataBuffer();
			// WritableRaster wr = tile.createWritableRaster(sampleModel,
			// dataBuffer, null);
			// BufferedImage bi = new BufferedImage(colorModel, wr,
			// colorModel.isAlphaPremultiplied(), null);
			// AffineTransform transform =
			// AffineTransform.getTranslateInstance(xLoc + getXOrigin(), yLoc+
			// getYOrigin());
			// g2D.drawRenderedImage(bi, transform);
			// }
			// }
			// }

			// Util.info("finishingdrawimage = " +
			// System.currentTimeMillis());
		}

		// Util.info( "Entered here " + listOfPoints.size() );
		// g2D.setPaint ( Color.BLUE );
		if (ApplicationProperties.showObjects == false) {
			return;
		}

		Set<Integer> allContinNumbs = new HashSet<Integer>();
		for (Object o : listOfObjects) {
			allContinNumbs.add(((CellObject) o).continNumber);
		}

		Map<Integer, Contin> allContins = EDatabase.getContins(allContinNumbs);

		drawObjects(g2D, listOfObjects, allContins);
		// drawPoints(g2D, new Color( 0.6f, 0.6f, 1.0f ),listOfObjects);
		// drawSynaps(g2D, listOfSynapses);
		g2D.setPaint(Color.ORANGE);
		g2D.setStroke(new BasicStroke(3.0f));

		if (selectedPoint != null) {
			paintSelectedPoint(g2D);
		}
		paintRelationPoints(g, allContins);

	}

	private boolean showObject(CellObject object) {

		if (Elegance.filterOptions.isHideAll())
			return false;

		if (Elegance.filterOptions.getContinFilterType() == FilterOptions.ContinFilterType.all
				&& Elegance.filterOptions.getObjectFilterType() == FilterOptions.ObjectFilterType.all)
			return true;

		return showObject(object, EDatabase.getContin(object.continNumber));
	}

	private boolean showObject(CellObject object, Contin contin) {

		if (Elegance.filterOptions.isHideAll())
			return false;

		if (Elegance.filterOptions.getObjectFilterType() == FilterOptions.ObjectFilterType.custom_contin_name) {
			if (contin == null)
				return false;
			if (!Elegance.filterOptions.getObjectFilterCustom().contains(contin.getName()))
				return false;

		} else if (Elegance.filterOptions.getObjectFilterType() == FilterOptions.ObjectFilterType.custom_contin_number) {
			if (contin == null)
				return false;
			if (!Elegance.filterOptions.getObjectFilterCustom().contains("" + object.continNumber))
				return false;

		} else if (Elegance.filterOptions.getObjectFilterType() == FilterOptions.ObjectFilterType.custom_number) {
			if (object.objectName != null && object.objectName.trim().length() > 0) {
				if (!Elegance.filterOptions.getObjectFilterCustom().contains("" + object.objectName))
					return false;
			}
		}

		return true;

	}

	public void drawObjects(GraphicsJAI g2D, Vector<CellObject> obj, Map<Integer, Contin> allContins) {

		if (Elegance.filterOptions.isHideAll() || Elegance.filterOptions.getObjectFilterType() == FilterOptions.ObjectFilterType.none) {
			return;
		}

		Font font = new Font("sansserif", Font.PLAIN, 16);
		Font font2 = new Font("sansserif", Font.BOLD, 32);
		g2D.setFont(font);
		g2D.setStroke(new BasicStroke(2.0f));
		for (int i = 0; i < obj.size(); i++) {

			CellObject object = obj.elementAt(i);

			if (!showObject(object, allContins.get(object.continNumber)))
				continue;

			Point p = object.p;
			String type = object.type;
			int checked = object.checked;
			// Util.info( "point got" );
			Point p2 = getImagePointAsScreenPoint(p);

			if ((p2.x < 0) || (p2.y < 0) || (p2.x > componentWidth) || (p2.y > componentHeight)) {
				continue;
			}

			Color color = new Color(0, 0, 153);
			int size = 8 + (int) (current_zoom * 10.0f);
			if (type.equals("chemical") || type.equals("electrical")) {

				g2D.setPaint(Color.red);
				g2D.draw(new Ellipse2D.Float(p2.x - (size / 2), p2.y - (size / 2), size, size));

				/**
				 * g2D.setPaint(color); String sType = ((CellObject)
				 * obj.elementAt(i)).type; String sFrom = ((CellObject)
				 * obj.elementAt(i)).fromObj; String sTo = ((CellObject)
				 * obj.elementAt(i)).toObj; String str2 = sType + " " + sFrom +
				 * " " + sTo; if (str2 != null) { g2D.drawString(str2, p2.x +
				 * size, p2.y); }
				 **/

			} else {

				g2D.setPaint(color);

				if (checked == 1) {
					g2D.draw(new Ellipse2D.Float(p2.x - (size / 2), p2.y - (size / 2), size, size));

					String label = object.objectName;

					if (label != null) {
						g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7F));
						g2D.drawString(label, p2.x + size, p2.y);
						g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0F));
					}
				} else {

					g2D.draw(new RoundRectangle2D.Float(p2.x - (size / 2), p2.y - (size / 2), size, size, size / 8, size / 8));

					if (Elegance.filterOptions.isObjectShowColorCode()) {

						Contin contin = allContins.get(object.continNumber);

						if (contin != null) {

							String label = contin.getCode();

							if (label != null) {
								Font fontBackup = g2D.getFont();
								Color colorBackup = g2D.getColor();

								g2D.setColor(contin.getColor());
								g2D.setFont(font2);
								g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0F));
								g2D.drawString(label, p2.x - size, p2.y);

								g2D.setColor(colorBackup);
								g2D.setFont(fontBackup);

							}

						}

					}

					if (Elegance.filterOptions.isObjectShowContin()) {

						Contin contin = allContins.get(object.continNumber);
						if (contin == null) {
							continue;
						}

						String label = contin.getName();

						if (label != null) {
							Font fontBackup = g2D.getFont();
							Color colorBackup = g2D.getColor();

							g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0F));

							g2D.drawString(label, p2.x - size, p2.y);

							g2D.setColor(colorBackup);
							g2D.setFont(fontBackup);

						}
					}

					if (Elegance.filterOptions.isObjectShowNumber()) {

						String name = object.objectName;

						if (name != null) {

							Font fontBackup = g2D.getFont();
							Color colorBackup = g2D.getColor();

							g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7F));

							g2D.drawString(name, p2.x - size, p2.y);

							g2D.setColor(colorBackup);
							g2D.setFont(fontBackup);

							g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0F));
						}
					}

				}

			}
		}

	}

	// mouse interface

	/**
	 * Handles the event when the mouse enters this component. Currently does
	 * noting.
	 * 
	 * @param e
	 *            a MouseEvent object
	 */
	public final void mouseEntered(MouseEvent e) {
	}

	/**
	 * Handles the event when the mouse exits this component. Currently does
	 * noting.
	 * 
	 * @param e
	 *            a MouseEvent object
	 */
	public final void mouseExited(MouseEvent e) {
	}

	/**
	 * Handles the event when the mouse is pressed inside this component.
	 * 
	 * @param e
	 *            a MouseEvent object
	 */
	public void mousePressed(MouseEvent e) {
		Point p = e.getPoint();
		int mods = e.getModifiers();

		if (odometer != null) {
			// String output = " (" + p.x + ", " + p.y + ")";
			String output = getOdometerString(p);

			odometer.setText(output);
		}

		last_x = getXOrigin() - e.getX();
		last_y = getYOrigin() - e.getY();

		if ((selectedPoint != null) && (isControlButtonPressed == false) && (!e.isMetaDown())) {
			Point p1 = getScreenPointAsImagePoint(p);
			int size = (10 + (int) (current_zoom * 15.0f));
			RoundRectangle2D.Float ell = new RoundRectangle2D.Float(selectedPoint.x - (size / 2), selectedPoint.y - (size / 2), size, size, size / 8, size / 8);

			// Rectangle ell = new Rectangle(p1.x-size/2, p1.y-size/2, size,
			// size);
			if (ell.contains(p1)) {
				isMousePressedInSelectedPoint = true;
				deletedPoint = selectedPoint;

				// Util.info("mouse pressed inside selected point");
			}
		}

		// Util.info( "last_x reset. x=" + e.getX() + " y=" + e.getY()
		// );
		// last_x = -e.getX();
		// last_y = -e.getY();
	}

	/**
	 * Handles the event when the mouse button is released.
	 * 
	 * @param e
	 *            a MouseEvent object
	 */
	public final void mouseReleased(MouseEvent e) {
		Connection con = null;
		try {
			con = EDatabase.borrowConnection();

			Point p = e.getPoint();

			if (odometer != null) {
				// String output = " (" + p.x + ", " + p.y + ")";
				String output = getOdometerString(p);

				odometer.setText(output);
			}

			if ((selectedPoint != null) && (isMousePressedInSelectedPoint == true) && (!e.isMetaDown())) {
				Point pointLocation = getScreenPointAsImagePoint(p);
				if (!(pointLocation.x <= 0 || pointLocation.y <= 0 || pointLocation.x > originalSource.getWidth() || pointLocation.y > originalSource
						.getHeight())) {

					selectedPoint = pointLocation;

					// Util.info("selectedPoint = "+ selectedPoint);
					updatePointInDatabase();

					// Util.info("point Updated in database");
					if (listOfPoints.contains(deletedPoint)) {
						int idx = listOfPoints.indexOf(deletedPoint);
						listOfPoints.removeElementAt(idx);
						listOfObjects.removeElementAt(idx);

						// Util.info("removed from list of points");
					}

					if (listOfPoints.contains(selectedPoint) == false) {
						// addPoint ( selectedPoint );
						listOfPoints.add(selectedPoint);
						listOfObjects.add(new CellObject(imageNumber, selectedPoint, con));

						// Util.info("added point to list of points");
					}

					updatePointInRelationships(deletedPoint, selectedPoint);
					deletedPoint = null;
				} else {
					selectedPoint = deletedPoint;
				}
			}

			isMousePressedInSelectedPoint = false;
			if (isDragging) {
				repaint();
				isDragging = false;
			}

		} catch (Throwable e2) {
			ELog.info("cant get object data " + e2);
			throw new IllegalStateException("cant get object data", e2);
		} finally {
			EDatabase.returnConnection(con);
		}
		// Util.info("number of pts = " + listOfPoints.size());
		// setOrigin( e.getX() - last_x, e.getY() - last_y );
	}


	private void processSingleClick(MouseEvent e,Connection con) {
		Point p = e.getPoint();
		Point p1 = getScreenPointAsImagePoint(p);

		if (((p1.x < 0) || (p1.y < 0) || (p1.x > originalSource.getWidth()) || (p1.y > originalSource.getHeight())) && (!e.isMetaDown())) {
			selectedPoint = null;
			relation = new Relation();
			selectedObj = new CellObject();

			repaint();

			return;
		} else {

			
			if (selectScreenPoint(p) == true) {
				
				
				if (e.isShiftDown()) {
					selectedPoint = null;
					relation = new Relation();
					selectedObj = new CellObject();

					repaint();
				
					return;
				}
				
				paintSelectedPoint((Graphics2D) this.getGraphics());
				updateObjectTableSelection();

				if (isCPressed == true) {

					int objectNum = Integer.parseInt(selectedObj.objectName);
					try {

						NameContin na = new NameContin(objectNum, this);

						long time1 = System.currentTimeMillis();
						new Calculate(objectNum, na.continNum);
						isCPressed = false;
						long time2 = System.currentTimeMillis();
						long time = (time2 - time1) / 1000;
						JOptionPane.showMessageDialog(this, time + " seconds, done", "Calculation", JOptionPane.INFORMATION_MESSAGE);

					} catch (Exception ex) {
						ex.printStackTrace();
					}
					repaint();

				}

				if (isNPressed == true) {

					int objectNum = Integer.parseInt(selectedObj.objectName);
					try {

						new NameContin(objectNum, this);

						isNPressed = false;
						repaint();

					} catch (Exception ex) {
						ex.printStackTrace();
					}
					repaint();

				}

				if ((isAltButtonPressed == true) && (toObjSyn == true)) {

					String toName = selectedObj.objectName;
					int synID = Integer.parseInt(sy.getObjName(con));
					int partner = Integer.parseInt(toName);
					sy.saveRecords(synID, username, partner, "certain", "normal");
					if (sy.toObj != null) {
						sy.setTo(sy.toObj + "," + toName);
					} else {
						sy.setTo(toName);
					}

				}

				if ((isAltButtonPressed == true) && (newSyn == true)) {

					String fromName = selectedObj.objectName;
					sy.setFrom(fromName);
					sy.saveObject(username);

					toObjSyn = true;
					newSyn = false;
				}

				if (e.isAltDown() && (newSyn == false) && (toObjSyn == false)) {

					int vsName = Integer.parseInt(selectedObj.objectName);
					String otype = selectedObj.type;
					if (otype.equals("chemical") || otype.equals("electrical")) {
						SynapseViewFrame vsframe = new SynapseViewFrame(vsName, username, this, "view");
						vsframe.setVisible(true);
					} else {

						ObjectViewFrame vsframe = new ObjectViewFrame(vsName, username, this);
						vsframe.setVisible(true);
						vsframe.requestFocus();
						vsframe.requestFocusInWindow();

					}

					isAltButtonPressed = false;

				}

				if (isMPressed == true) {
					selectedObj.setChecked();

					repaint();
				}

				if (isAPressed == true && toObjSyn == true && addRecord == true) {

					int idx = listOfPoints.indexOf(selectedPoint);
					String toName = ((CellObject) (listOfObjects.elementAt(idx))).objectName;
					int synID = Integer.parseInt(sy.getObjName(con));
					int partner = Integer.parseInt(toName);
					sy.saveRecords(synID, username, partner, "certain", "normal");
					if (sy.toObj != null) {
						sy.setTo(sy.toObj + "," + toName);
					} else {
						sy.setTo(toName);

					}
					ELog.info("isAPressed3" + isAPressed + " " + addRecord + " " + toObjSyn + " " + sy.toObj);

				}

				if (isAPressed == true && addRecord == true && toObjSyn == false) {
					int idx = listOfPoints.indexOf(selectedPoint);
					String fromName = ((CellObject) (listOfObjects.elementAt(idx))).objectName;
					sy.setFrom(fromName);

					toObjSyn = true;
					ELog.info("isAPressed2" + isAPressed + " " + addRecord + " " + toObjSyn + " " + sy.fromObj);

				}

				if (isAPressed == true && addRecord == false && toObjSyn == false) {

					String synName = selectedObj.objectName;

					String[] opts = { "chemical", "electrical", "cancel" };
					int res = JOptionPane.showOptionDialog(this, "please choose the relationship type", "Type", JOptionPane.DEFAULT_OPTION,
							JOptionPane.INFORMATION_MESSAGE, null, opts, opts[0]);
					String type = null;
					if (res == 0)
						type = "chemical";
					if (res == 1) {
						type = "electrical";
					}

					if (res == 2)
						return;

					addRecord = true;
					sy = new CellObject();
					sy.imageNumber = imageNumber;
					sy.setObjName(synName);
					sy.setType(type);

					repaint();

				}

				if (isVPressed == true) {

				}

				if (isUPressed == true) {
					selectedObj.setUnchecked();
					repaint();
				}

				if (isControlButtonPressed == true) {
					if (relation.getObj1() == null) {

						relation.setObj1(selectedObj);

						paintRelationPoints(this.getGraphics(), null);
					} else {
						// Util.info("Entering second object");
						CellObject obj = new CellObject(imageNumber, selectedPoint, con);

						if (relation.getObj1().equals(obj)) {
							return;
						}

						if (!showObject(obj)) {
							ELog.info("ingoring obj " + obj.objectName + " because it's filtered out in this overlay");
							return;
						}

						selectedPoint = null;

						// Util.info("select point now null");
						relation.setObj2(obj);

						// Util.info("second object set");
						paintRelationPoints(this.getGraphics(), null);

						// repaint();
						// Util.info("repainted");

						if (listOfRelations.contains(relation)) {
							int indexOfSelectedRelation = listOfRelations.indexOf(relation);
							relation = (Relation) listOfRelations.elementAt(indexOfSelectedRelation);

							String[] options = { "Delete", "Cancel" };

							int result = JOptionPane.showOptionDialog(this,
									"The relation already exists.\nObject 1 is GREEN\nObject 2 is PURPLE\nWhat do you want to do?", "Relation Exists",
									JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[1]);
							isControlButtonPressed = false;
							if (result == 1) {
								relation = new Relation();
								return;
							} else if (result == 0) {
								deletedRelation = relation;
								deleteRelation();

								return;
							}

						} else {

							int con1 = relation.getObj1().continNumber;
							int con2 = relation.getObj2().continNumber;
							if (con1 != con2) {

								String[] options = { "continue", "Cancel" };

								int result = JOptionPane.showOptionDialog(this, "the object1 belongs to " + con1
										+ " contin and the object2 belongs to " + con2 + " contin, "
										+ " Do you want to continue combining two contins or cancel this connectivity?",
										"you are connect different contins", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
										options, options[0]);
								isControlButtonPressed = false;
								if (result == 1) {
									relation = new Relation();
									return;
								}

							}

							isControlButtonPressed = false;

							listOfRelations.addElement(relation);
							relation.saveRelation();

							// Util.info("added 1 relation");
							// isControlButtonPressed = false;
							relation = new Relation();
						}

						repaint();
					}
				} else {
/*
					selectedPoint = null;
					relation = new Relation();
					selectedObj = new CellObject();

					repaint();
				
					return;
					*/
				}
			} else { 
				
			}
		}
	}

	private void processDblClick(MouseEvent e,Connection con) {
		Point p = e.getPoint();
		Point p1 = getScreenPointAsImagePoint(p);

		if ((p1.x < 0) || (p1.y < 0) || (p1.x > originalSource.getWidth()) || (p1.y > originalSource.getHeight())) {
			
			return;
		} else if (!e.isControlDown() && !e.isAltDown() && e.getClickCount()==2) {//add new object on left dbl click
			selectedPoint = null;
			addPoint(p1);
			selectedPoint = p1;

			relation = new Relation();
			repaint();
			return;
		} else if (isAltButtonPressed == true) {

			String[] opts = { "chemical", "electrical", "cancel" };
			int res = JOptionPane.showOptionDialog(this, "please choose the relationship type", "Type", JOptionPane.DEFAULT_OPTION,
					JOptionPane.INFORMATION_MESSAGE, null, opts, opts[0]);
			String type = null;
			if (res == 0)
				type = "chemical";
			if (res == 1) {
				type = "electrical";
			}

			if (res == 2) {
				isAltButtonPressed = false;
				return;
			} else {

				newSyn = true;
				sy = new CellObject();
				sy.imageNumber = imageNumber;
				sy.setType(type);
				sy.setPoint(p1);

				// String fromObj = JOptionPane.showInputDialog ( null,
				// "Enter
				// the ObjectName from which this synaps is");
				// String toObj = JOptionPane.showInputDialog ( null,
				// "Enter
				// the
				// ObjectName to which this synaps is");
				// addSynaps ( p1,type,"0","0" );
				repaint();
			}
			// isAltButtonPressed=false;

		} else if (e.isMetaDown()) {

			CellObject preObj = new CellObject(preObjName);
			int workoncontin = preObj.continNumber;

			relation = new Relation();
			relation.setObj1(preObj);

			listOfPoints.addElement(p1);
			CellObject pt = new CellObject(imageNumber, p1, workoncontin, con);
			pt.saveObject(username);
			listOfObjects.addElement(pt);

			relation.setObj2(pt);
			listOfRelations.addElement(relation);
			relation.saveRelation();

			selectedPoint = p1;
			preObjName = pt.objectName;

 			repaint();

		}

		else {
			// addPoint(p1);
			// repaint();
		}
	}

	
	public final void mouseClicked(MouseEvent e) {

		Connection con = null;
		try {
			con = EDatabase.borrowConnection();
			if (e.getClickCount() == 1 && e.getButton() == MouseEvent.BUTTON1) {
				processSingleClick(e,con);
			} else if (e.getClickCount() == 2 || e.getButton() == MouseEvent.BUTTON2 || e.getButton() == MouseEvent.BUTTON3) {
				processDblClick(e,con);
			}

		} catch (Throwable e2) {
			ELog.info("cant get object data " + e2);
			throw new IllegalStateException("cant get object data", e2);
		} finally {
			EDatabase.returnConnection(con);
		}
		// Util.info("number of points = " + listOfPoints.size());
	}

	/**
	 * Handles the event when the mouse is moved.
	 * 
	 * @param e
	 *            a MouseEvent object
	 */
	public final void mouseMoved(MouseEvent e) {
		Point p = e.getPoint();

		if (odometer != null) {
			// String output = " (" + p.x + ", " + p.y + ")";
			String output = getOdometerString(p);

			odometer.setText(output);
		}
	}

	/**
	 * Handles the event when the mouse is dragged.
	 * 
	 * @param e
	 *            a MouseEvent object
	 */
	public final void mouseDragged(MouseEvent e) {
		isDragging = true;
		// mousePressed( e );
		// setOrigin( last_x + e.getX(), last_y + e.getY() );
		if (selectedPoint == null) {
			setOrigin(e.getX() + last_x, e.getY() + last_y);
		}

		// initialize();
		// Util.info( "Origin set to " + getXOrigin() + ", " +
		// getYOrigin() );
	}

	/**
	 * Rotates an image by the specified amount and returns it.
	 * 
	 * @param rot
	 *            The amount of rotation required in degrees
	 * @param aSource
	 *            The source
	 * 
	 * @return The rotated image
	 */
	public PlanarImage rotate(float rot, PlanarImage aSource) {
		int oldHeight = (int) (Math.abs(componentHeight * Math.cos(current_rotation)) + Math.abs(componentWidth * Math.sin(current_rotation)));
		int oldWidth = (int) (Math.abs(componentHeight * Math.sin(current_rotation)) + Math.abs(componentWidth * Math.cos(current_rotation)));

		ParameterBlock pb = new ParameterBlock();

		pb.addSource(aSource); // The sources[0] image
		pb.add((float) aSource.getWidth() / 2); // The x origin
		pb.add((float) aSource.getHeight() / 2); // The y origin
		pb.add(((float) -(float) Math.PI * rot) / 180.0f); // The rotation
															// angle
		pb.add(new InterpolationNearest()); // The interpolation
		aSource = JAI.create("Rotate", pb, null);
		current_rotation = (Math.PI * rot) / 180.0;

		initialize();
		setOrigin(getXOrigin() - ((componentWidth - oldWidth) / 2), getYOrigin() - ((componentHeight - oldHeight) / 2));

		// Util.info("old width = " + oldWidth + " and new width = " +
		// componentWidth);
		// Util.info("current_rotation = "+ current_rotation);
		return aSource;
	}

	/**
	 * Returns the current rotation
	 * 
	 * @return the current rotation
	 */
	public double getRotation() {
		return current_rotation;
	}

	/**
	 * Zooms the originalSource by the specified amount.
	 * 
	 * @param amount
	 *            The zoom amount
	 * 
	 * @return The zoomed image.
	 */
	public PlanarImage zoom(float amount) {
		ParameterBlock pb = new ParameterBlock();

		pb.addSource(originalSource); // The sources[0] image
		pb.add(amount); // The xScale
		pb.add(amount); // The yScale
		pb.add(0.0F); // The x translation
		pb.add(0.0F); // The y translation
		pb.add(new InterpolationNearest()); // The interpolation

		PlanarImage returnedSource = JAI.create("scale", pb, null);

		int x = getXOrigin();
		int y = getYOrigin();
		int a = componentWidth / 2;
		int b = componentHeight / 2;
		float z = amount / current_zoom;
		setOrigin((int) ((z * (x - a)) + a), (int) ((z * (y - b)) + b));
		current_zoom = amount;
		initialize();

		return returnedSource;
	}

	/**
	 * Returns the current zoom.
	 * 
	 * @return The current zoom.
	 */
	public float getZoom() {
		return current_zoom;
	}

	/**
	 * Zooms and rotates the originalSource and stores it.
	 * 
	 * @param zoom
	 *            the amount of zoom required
	 * @param rotate
	 *            The amount of rotation required in degrees
	 */
	public void zoomAndRotate(float zoom, float rotate) {
		// Util.info("starting zoom and rot " +
		// System.currentTimeMillis());
		ParameterBlock pb1 = new ParameterBlock();

		pb1.addSource(originalSource); // The sources[0] image
		pb1.add(zoom); // The xScale
		pb1.add(zoom); // The yScale
		pb1.add(0.0F); // The x translation
		pb1.add(0.0F); // The y translation
		pb1.add(new InterpolationNearest()); // The interpolation

		source = JAI.create("scale", pb1, null);

		pb1 = null;

		int x = getXOrigin();
		int y = getYOrigin();
		int a = componentWidth / 2;
		int b = componentHeight / 2;
		float z = zoom / current_zoom;
		setOrigin((int) ((z * (x - a)) + a), (int) ((z * (y - b)) + b));
		current_zoom = zoom;
		initialize();

		int oldHeight = (int) (Math.abs(componentHeight * Math.cos(current_rotation)) + Math.abs(componentWidth * Math.sin(current_rotation)));
		int oldWidth = (int) (Math.abs(componentHeight * Math.sin(current_rotation)) + Math.abs(componentWidth * Math.cos(current_rotation)));

		ParameterBlock pb2 = new ParameterBlock();

		pb2.addSource(source); // The sources[0] image
		pb2.add((float) source.getWidth() / 2); // The x origin
		pb2.add((float) source.getHeight() / 2); // The y origin
		pb2.add(((float) -(float) Math.PI * rotate) / 180.0f); // The rotation
																// angle
		pb2.add(new InterpolationNearest()); // The interpolation
		source = JAI.create("Rotate", pb2, null);
		current_rotation = (Math.PI * rotate) / 180.0;
		pb2 = null;
		initialize();

		LookupTableJAI lookup = new LookupTableJAI(lutData);
		source = JAI.create("lookup", source, lookup);
		isSaved = false;
		// Util.info("finishing zoom and rot " +
		// System.currentTimeMillis());
	}

	/**
	 * Gets the odometerString attribute of the ImageDisplayPanel object
	 * 
	 * @param point
	 *            a point on the screen
	 * 
	 * @return The odometerString value
	 */
	public String getOdometerString(Point point) {
		Point p1 = getScreenPointAsImagePoint(point);

		if ((p1.x < 0) || (p1.y < 0) || (p1.x > originalSource.getWidth()) || (p1.y > originalSource.getHeight())) {
			return " ";
		}

		// Point p2 = getImagePointAsScreenPoint( p1 );
		// return " (" + x + ", " + y + ")" + " (" + p1.x + ", " + p1.y + ")";
		// return " (" + p1.x + ", " + p1.y + ") ScreenPoint = (" + p2.x + ", "
		// + p2.y + ") ";
		return " (" + p1.x + ", " + p1.y + ")";
	}

	/**
	 * A point on the screen is converted into the corresponding point on the
	 * image and returned
	 * 
	 * @param pt
	 *            The point on the screen.
	 * 
	 * @return The point on the image
	 */
	Point getScreenPointAsImagePoint(Point pt) {
		int x = pt.x;
		int y = pt.y;

		// x = x + getXOrigin();
		// y = y + getYOrigin();
		x = x - getXOrigin();
		y = y - getYOrigin();

		x = (int) (((float) x * 1.0f) / current_zoom);
		y = (int) (((float) y * 1.0f) / current_zoom);

		return getRotatedPoint(x, y);
	}

	/**
	 * Gets the location of a point after it is rotated by an angle equal to the
	 * current rotation
	 * 
	 * @param x
	 *            The x coordinate if the point
	 * @param y
	 *            The y coordinate if the point
	 * 
	 * @return The rotated Point
	 */
	public Point getRotatedPoint(int x, int y) {
		// int actualOriginX = ( int ) ( ( float ) originalSource.getWidth() /
		// current_zoom / 2.0f );
		// int actualOriginY = ( int ) ( ( float ) originalSource.getHeight() /
		// current_zoom / 2.0f );
		int actualOriginX = (int) ((float) originalSource.getWidth() / 2.0f);
		int actualOriginY = (int) ((float) originalSource.getHeight() / 2.0f);

		x = x - actualOriginX;
		y = actualOriginY - y;

		double r = Math.sqrt((x * x) + (y * y));
		double theta = getTanInverseOf((double) y, (double) x);

		// Util.info( theta );
		int newX = (int) (r * Math.cos(theta - current_rotation));
		int newY = (int) (r * Math.sin(theta - current_rotation));

		newX = actualOriginX + newX;
		newY = actualOriginY - newY;

		return new Point(newX, newY);

		// return new Point( x, y );
	}

	/**
	 * Assumes that the numerator is the y coordinate and the denominator the x
	 * coordinate and then returns the tan inverse of numerator/denominator.
	 * Useful because the Math class provided by java cannot return values
	 * greater than pi/2 and les than -PI/2
	 * 
	 * @param numerator
	 *            Description of the Parameter
	 * @param denominator
	 *            Description of the Parameter
	 * 
	 * @return The tanInverseOf value
	 */
	public double getTanInverseOf(double numerator, double denominator) {
		if (numerator >= 0) {
			if (denominator >= 0) {
				return Math.atan(numerator / denominator);
			} else {
				return Math.PI + Math.atan(numerator / denominator);
			}
		} else {
			if (denominator >= 0) {
				return (Math.PI * 2.0) + Math.atan(numerator / denominator);
			} else {
				return Math.PI + Math.atan(numerator / denominator);
			}
		}
	}

	/**
	 * Gets a point on the image as a point on the screen.
	 * 
	 * @param imPoint
	 *            A point on the image
	 * 
	 * @return The point on the screen
	 */
	Point getImagePointAsScreenPoint(Point imPoint) {
		Point copy = new Point(imPoint.x, imPoint.y);

		int actualOriginX = (int) ((float) originalSource.getWidth() / 2.0f);
		int actualOriginY = (int) ((float) originalSource.getHeight() / 2.0f);

		copy.x = copy.x - actualOriginX;
		copy.y = actualOriginY - copy.y;

		double r = Math.sqrt((copy.x * copy.x) + (copy.y * copy.y));
		double theta = getTanInverseOf((double) copy.y, (double) copy.x);

		// Util.info( theta );
		int newX = (int) (r * Math.cos(theta + current_rotation));
		int newY = (int) (r * Math.sin(theta + current_rotation));

		newX = actualOriginX + newX;
		newY = actualOriginY - newY;

		newX = (int) ((float) newX * current_zoom);
		newY = (int) ((float) newY * current_zoom);

		newX += getXOrigin();
		newY += getYOrigin();

		return new Point(newX, newY);
	}

	/**
	 * Adds a Point to the list of points
	 * 
	 * @param p
	 *            The point to be added
	 */
	public void addPoint(Point p) {
		Connection con = null;
		try {
			con = EDatabase.borrowConnection();
			listOfPoints.addElement(p);
			CellObject pt = new CellObject(imageNumber, p, con);
			pt.saveObject(username);

			listOfObjects.addElement(pt);

			// Util.info( "add point was called" );
			repaint();

		} catch (Throwable e) {
			ELog.info("cant get object data " + e);
			throw new IllegalStateException("cant get object data", e);
		} finally {
			EDatabase.returnConnection(con);
		}
	}

	/**
	 * Adds a number of points to the listOfPoints attribute of the
	 * ImageDisplayPanel object
	 * 
	 * @param v
	 *            The points in a Vector
	 */
	public void addPoints(Vector v) {
		Connection con = null;
		try {
			con = EDatabase.borrowConnection();
			for (int i = 0; i < v.size(); i++) {
				try {
					Point p = (Point) v.elementAt(i);

					listOfPoints.addElement(p);
					CellObject pt = new CellObject(imageNumber, p, con);
					listOfObjects.addElement(pt);
					isSaved = false;
				} catch (Exception e) {
				}
			}

			repaint();
		} catch (Throwable e) {
			ELog.info("cant get object data " + e);
			throw new IllegalStateException("cant get object data", e);
		} finally {
			EDatabase.returnConnection(con);
		}
	}

	public void addObjects(Vector v) {
		for (int i = 0; i < v.size(); i++) {
			try {
				CellObject obj = (CellObject) v.elementAt(i);

				listOfObjects.addElement(obj);
				isSaved = false;
			} catch (Exception e) {
			}
		}

		repaint();
	}

	/**
	 * Removes a point from the list of points
	 * 
	 * @param p
	 *            The point to be removed
	 */
	public void removePoint(Point p) {
		Connection con = null;
		try {
			con = EDatabase.borrowConnection();

			deletedPoint = p;
			selectedPoint = null;
			CellObject pt = new CellObject(imageNumber, p, con);
			listOfObjects.removeElement(pt);
			if (deletePointFromDatabase() != -1) {
				listOfPoints.removeElement(p);

			}
		} catch (Throwable e) {
			ELog.info("cant get object data " + e);
			throw new IllegalStateException("cant get object data", e);
		} finally {
			EDatabase.returnConnection(con);
		}
	}

	/**
	 * Removes a point from the list of points
	 * 
	 * @param i
	 *            The zero based index of the location of the point in the
	 *            listOfPoints Vector.
	 */
	public void removePoint(int i) {
		deletedPoint = (Point) listOfPoints.remove(i);
		listOfObjects.remove(i);
		selectedPoint = null;
		if (deletePointFromDatabase() != -1) {
			listOfPoints.addElement(deletedPoint);
			deletedPoint = null;
		}
		// isSaved = false;
	}

	public void addListOfObjectsAndPoints(String imgNumber) {
		Connection con = null;

		try {
			con = EDatabase.borrowConnection();

			Statement stmt = con.createStatement();
			String sql = "SELECT * from object where IMG_Number = '" + imgNumber + "'";

			ELog.info("ImgDisplayPanel.addListOfObjectsAndPoints " + sql);
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				int x = rs.getInt("OBJ_X");
				int y = rs.getInt("OBJ_Y");
				String type = rs.getString("type");
				String fromObj = rs.getString("fromObj");
				String toObj = rs.getString("toObj");
				Point p = new Point(x, y);

				if (listOfPoints.contains(p) == false) {
					listOfPoints.add(p);
				}

				int checked = rs.getInt("checked");
				int continNum = rs.getInt("CON_Number");
				String certainty = rs.getString("certainty");
				String objectName = rs.getString("OBJ_Name");
				CellObject obj = new CellObject(p, imgNumber, type, fromObj, toObj, objectName, checked, continNum, certainty);

				if (listOfObjects.contains(obj) == false) {
					listOfObjects.add(obj);
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			EDatabase.returnConnection(con);
		}
	}

	private String log(String log) {
		ELog.info(log);
		return log;
	}

	public void addListOfCellObjectsAndPoints(String imgNumber) {
		Connection con = null;

		try {
			con = EDatabase.borrowConnection();

			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(log("SELECT * from object where IMG_Number = '" + imgNumber + "'"));

			while (rs.next()) {
				int x = rs.getInt("OBJ_X");
				int y = rs.getInt("OBJ_Y");
				String type = rs.getString("type");
				// Util.info(type);
				String fromObj = rs.getString("fromObj");
				String toObj = rs.getString("toObj");
				Point p = new Point(x, y);

				if (listOfPoints.contains(p) == false) {
					listOfPoints.add(p);
				}

				int checked = rs.getInt("checked");
				int continNum = rs.getInt("CON_Number");
				String certainty = rs.getString("certainty");
				String objectName = rs.getString("OBJ_Name");
				CellObject obj = new CellObject(p, imgNumber, type, fromObj, toObj, objectName, checked, continNum, certainty);

				if (listOfObjects.contains(obj) == false) {
					listOfObjects.add(obj);
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			EDatabase.returnConnection(con);
		}
	}

	public void addListOfSynObjectsAndPoints(String imgNumber) {
		Connection con = null;

		try {
			con = EDatabase.borrowConnection();

			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(log("SELECT * from object where IMG_Number = '" + imgNumber + "' and not type like 'cell%'"));

			while (rs.next()) {
				int x = rs.getInt("OBJ_X");
				int y = rs.getInt("OBJ_Y");
				String type = rs.getString("type");
				String fromObj = rs.getString("fromObj");
				String toObj = rs.getString("toObj");
				Point p = new Point(x, y);

				if (listOfPoints.contains(p) == false) {
					listOfPoints.add(p);
				}

				int checked = rs.getInt("checked");
				int continNum = rs.getInt("CON_Number");
				String certainty = rs.getString("certainty");
				String objectName = rs.getString("OBJ_Name");
				CellObject obj = new CellObject(p, imgNumber, type, fromObj, toObj, objectName, checked, continNum, certainty);

				if (listOfObjects.contains(obj) == false) {
					listOfObjects.add(obj);
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			EDatabase.returnConnection(con);
		}
	}

	public void setImageData(String imgNummber) {
		Connection con = null;

		try {
			con = EDatabase.borrowConnection();

			Statement stmt = con.createStatement();
			// Util.info("SELECT IMG_zoomvalue, IMG_brightnessvalue,
			// IMG_contrastvalue,"
			// + " IMG_rotatedvalue, IMG_originX, IMG_originY, from Image "
			// + "where IMG_Number = '" + imgNummber + "'");
			ResultSet rs = stmt.executeQuery(log("SELECT IMG_zoomvalue, IMG_brightnessvalue, IMG_contrastvalue,"
					+ " IMG_rotatedvalue, IMG_originX, IMG_originY from image " + "where IMG_Number = '" + imgNummber + "'"));
			if (rs.next()) {
				current_zoom = rs.getFloat("IMG_zoomvalue");
				brightness = rs.getInt("IMG_brightnessvalue");
				contrast = rs.getInt("IMG_contrastvalue");
				current_rotation = rs.getDouble("IMG_rotatedvalue");
				originX = rs.getInt("IMG_originX");
				originY = rs.getInt("IMG_originY");
				// Util.info("originX = " + originX);
				// Util.info("originY = " + originY);
			}

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			EDatabase.returnConnection(con);
		}
	}

	/**
	 * returns the last_y attribute of the ImageDisplayPanel class
	 * 
	 * @return The last_y attribute of the ImageDisplayPanel class
	 */
	public int getLast_y() {
		return last_y;
	}

	/**
	 * sets the last_y attribute of the ImageDisplayPanel class
	 * 
	 * @param newLast_y
	 *            The new last_y attribute of the ImageDisplayPanel class
	 */
	public void setLast_y(int newLast_y) {
		last_y = newLast_y;
	}

	/**
	 * returns the last_x attribute of the ImageDisplayPanel class
	 * 
	 * @return The last_x attribute of the ImageDisplayPanel class
	 */
	public int getLast_x() {
		return last_x;
	}

	/**
	 * sets the last_x attribute of the ImageDisplayPanel class
	 * 
	 * @param newLast_x
	 *            The new last_x attribute of the ImageDisplayPanel class
	 */
	public void setLast_x(int newLast_x) {
		last_x = newLast_x;
	}

	/**
	 * returns the temp_rotation attribute of the ImageDisplayPanel class
	 * 
	 * @return The temp_rotation attribute of the ImageDisplayPanel class
	 */
	public double getTemp_rotation() {
		return temp_rotation;
	}

	/**
	 * Sets the temp_rotation attribute of the ImageDisplayPanel class
	 * 
	 * @param newTemp_rotation
	 *            The new temp_rotation attribute of the ImageDisplayPanel class
	 */
	public void setTemp_rotation(double newTemp_rotation) {
		temp_rotation = newTemp_rotation;
	}

	/**
	 * returns the temp_zoom attribute of the ImageDisplayPanel class
	 * 
	 * @return The temp_zoom attribute of the ImageDisplayPanel class
	 */
	public float getTemp_zoom() {
		return temp_zoom;
	}

	/**
	 * Sets the temp_zoom attribute of the ImageDisplayPanel class
	 * 
	 * @param newTemp_zoom
	 *            The new temp_zoom attribute of the ImageDisplayPanel class
	 */
	public void setTemp_zoom(float newTemp_zoom) {
		temp_zoom = newTemp_zoom;
	}

	/**
	 * Checks whether this panel has been saved or not.
	 * 
	 * @return true if the panel is saved and false otherwise
	 */
	public boolean isSaved() {
		return isSaved;
	}

	/**
	 * Saves the overlay on the image
	 */
	public void saveOverlay() {
		/**
		 * Connection con = null;
		 * 
		 * try { //int maxObjName = 1; con = Util.getConnection (
		 * 
		 * );
		 * 
		 * PreparedStatement pst = null,pst0=null,pst1=null,pst2=null;
		 * //con.prepareStatement ( // "select OBJ_Name from Object where
		 * IMG_Number = ? order by OBJ_Name desc" //); //pst.setString ( 1,
		 * imageNumber );
		 * 
		 * ResultSet rs = null; //pst.executeQuery ( );
		 * 
		 * //if ( rs.next ( ) ) //{ // maxObjName = rs.getInt ( "OBJ_Name" );
		 * 
		 * //Util.info("maxObjName = "+maxObjName); // maxObjName++; //}
		 * 
		 * //pst = con.prepareStatement ( // "insert ignore into Object
		 * (OBJ_Name, OBJ_X, OBJ_Y, IMG_Number, CON_Number) values(?, ?, ?, ?,
		 * ?)" // ); pst0 = con.prepareStatement ( "select * from object where
		 * OBJ_X=? and OBJ_Y=? and IMG_Number=?" );
		 * 
		 * pst = con.prepareStatement ( "insert into Object (OBJ_X, OBJ_Y,
		 * IMG_Number, CON_Number) values(?, ?, ?, ?)" );
		 * 
		 * 
		 * 
		 * 
		 * for ( int i = 0; i < listOfPoints.size ( ); i++ ) { Point p = ( Point
		 * ) listOfPoints.elementAt ( i ); //pst.setInt ( 1, maxObjName );
		 * //pst.setInt ( 2, p.x ); //pst.setInt ( 3, p.y ); //pst.setString (
		 * 4, imageNumber ); //pst.setInt ( 5, 0 ); pst0.setInt ( 1, p.x );
		 * pst0.setInt ( 2, p.y ); pst0.setString ( 3, imageNumber ); rs =
		 * pst0.executeQuery( ); if(!rs.next()) {
		 * 
		 * pst.setInt ( 1, p.x ); pst.setInt ( 2, p.y ); pst.setString ( 3,
		 * imageNumber ); pst.setInt ( 4, -1 ); pst.executeUpdate(); } }
		 * 
		 * pst = con.prepareStatement ( "insert into object (OBJ_X, OBJ_Y,
		 * IMG_Number, type, fromObj, toObj) values(?, ?, ?, ?, ?, ?)" ); pst2 =
		 * con.prepareStatement ( "update object set type=?, fromObj=?, toObj=?
		 * where OBJ_X=? and OBJ_Y=? and IMG_Number=?" ); for ( int i = 0; i <
		 * listOfSynapses.size ( ); i++ ) { CellObject synaps = ( CellObject )
		 * listOfSynapses.elementAt ( i );
		 * 
		 * //pst.setInt ( 1, maxObjName ); //pst.setInt ( 2, p.x ); //pst.setInt
		 * ( 3, p.y ); //pst.setString ( 4, imageNumber ); //pst.setInt ( 5, 0
		 * ); pst0.setInt ( 1, (synaps.p).x ); pst0.setInt ( 2, (synaps.p).y );
		 * pst0.setString ( 3, synaps.imageNumber );
		 * 
		 * rs = pst0.executeQuery( ); if(rs.next()) { pst2.setInt ( 4,
		 * (synaps.p).x ); pst2.setInt ( 5, (synaps.p).y ); pst2.setString ( 6,
		 * imageNumber ); pst2.setString ( 1, synaps.type ); pst2.setString ( 2,
		 * synaps.fromObj ); pst2.setString ( 3, synaps.toObj );
		 * pst2.executeUpdate ( ); } else {
		 * 
		 * pst.setInt ( 1, (synaps.p).x ); pst.setInt ( 2, (synaps.p).y );
		 * pst.setString ( 3, imageNumber ); pst.setString ( 4, synaps.type );
		 * pst.setString ( 5, synaps.fromObj ); pst.setString ( 6, synaps.toObj
		 * ); pst.executeUpdate ( ); }
		 * 
		 * 
		 * }
		 * 
		 * 
		 * 
		 * 
		 * JOptionPane.showMessageDialog ( null, ex.getMessage ( ), "Error",
		 * JOptionPane.ERROR_MESSAGE );
		 * 
		 */
	}

	// Calculate all the object in the panel which contin number < 1, by Meng

	public void Calculate() {
		Connection con = null;
		try {
			con = EDatabase.borrowConnection();

			PreparedStatement pst = con.prepareStatement(log("select OBJ_Name from object where IMG_Number =? and CON_Number<1 and type = 'cell'"));

			pst.setString(1, imageNumber);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				int objName = rs.getInt(1);
				NameContin na = new NameContin(objName, this);
				new Calculate(objName, na.continNum);

			}

		} catch (Exception ex) {
			ex.printStackTrace();

			JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

		} finally {
			EDatabase.returnConnection(con);
		}

	}

	// end of Calculate function

	/**
	 * Saves the image data like brightness, contrast, zoom and rotation
	 */
	public void saveImageData() {
		Connection con = null;

		try {
			con = EDatabase.borrowConnection();

			PreparedStatement pst = con.prepareStatement("update image set IMG_zoomvalue = ? , IMG_brightnessvalue = ?,"
					+ " IMG_contrastvalue = ? , IMG_rotatedvalue = ?, IMG_originX = ?," + " IMG_originY = ? WHERE IMG_Number = ?");
			pst.setDouble(1, (double) current_zoom);
			pst.setInt(2, brightness);
			pst.setInt(3, contrast);
			pst.setDouble(4, Math.toDegrees(current_rotation));
			pst.setInt(5, originX);
			pst.setInt(6, originY);
			pst.setString(7, imageNumber);
			// Util.info("current origin = " + originX + " " +
			// originY);
			pst.executeUpdate();

		} catch (Exception ex) {
			ex.printStackTrace();

			JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

		} finally {
			EDatabase.returnConnection(con);
		}
	}

	/**
	 * Saves the relations into the database
	 */
	public static int saveRelations() {

		int numberOfRelations = listOfRelations.size();
		Vector listOfRelationsToRemove = new Vector(0);
		for (int i = 0; i < numberOfRelations; i++) {
			Relation rel = (Relation) listOfRelations.elementAt(i);

			rel.saveRelation();

		}

		if (listOfRelationsToRemove.size() > 0) {
			listOfRelations.removeAll(listOfRelationsToRemove);
			return -1;
		}
		return 1;
	}

	/**
	 * The function called when a key is typed. Currently this function does
	 * nothing.
	 * 
	 * @param e
	 *            a KeyEvent Object
	 */
	public void keyTyped(KeyEvent e) {
	}

	/**
	 * The function called when a key is pressed. Looks for the delete and the
	 * control buttons only.
	 * 
	 * @param e
	 *            a KeyEvent Object
	 */
	public void keyPressed(KeyEvent e) {
		// Util.info("A key was pressed");
		// if(e.getModifiers() == KeyEvent.CTRL_MASK)
		if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
			isControlButtonPressed = true;

			// Util.info("ctrl button pressed");
		}

		if (e.getKeyCode() == KeyEvent.VK_C) {
			isCPressed = true;

		}

		if (e.getKeyCode() == KeyEvent.VK_M) {
			isMPressed = true;

		}

		if (e.getKeyCode() == KeyEvent.VK_A) {
			isAPressed = true;

		}

		if (e.getKeyCode() == KeyEvent.VK_V) {
			isVPressed = true;

		}

		if (e.getKeyCode() == KeyEvent.VK_U) {
			isUPressed = true;

		}

		if (e.getKeyCode() == KeyEvent.VK_ALT) {
			isAltButtonPressed = true;

		}

		if (e.getKeyCode() == KeyEvent.VK_N) {
			isNPressed = true;

		}

		int keyCode = e.getKeyCode();

		if (keyCode == KeyEvent.VK_DELETE) {
			// Util.info("Delete key was pressed");
			if (selectedPoint != null) {
				removePoint(selectedPoint);
				selectedPoint = null;
				repaint();
			}
		} else {
			if ((keyCode == KeyEvent.VK_Z) && (e.getModifiers() == KeyEvent.CTRL_MASK) && (deletedPoint != null)) {
				selectedPoint = deletedPoint;
				addPoint(deletedPoint);
				deletedPoint = null;
			}
			if ((keyCode == KeyEvent.VK_Z) && (e.getModifiers() == KeyEvent.CTRL_MASK) && (deletedRelation != null)) {
				listOfRelations.addElement(deletedRelation);
				relation = new Relation();
				deletedRelation = null;
				repaint();
			}
		}
	}

	/**
	 * The function called when a key is released. Currently this function
	 * handles only the control key.
	 * 
	 * @param e
	 *            a KeyEvent Object
	 */
	public void keyReleased(KeyEvent e) {

		try {

			if (e.getKeyCode() == KeyEvent.VK_CONTROL)
			// if(e.getModifiers() == KeyEvent.CTRL_MASK)
			{
				isControlButtonPressed = false;
				// Util.info("ctrl button released");
			}

			if (e.getKeyCode() == KeyEvent.VK_C) {
				isCPressed = false;

			}

			if (e.getKeyCode() == KeyEvent.VK_M) {
				isMPressed = false;

			}

			if (e.getKeyCode() == KeyEvent.VK_N) {
				isNPressed = false;

			}

			if (e.getKeyCode() == KeyEvent.VK_A) {
				isAPressed = false;
				ELog.info("released" + isAPressed + " " + addRecord + " " + toObjSyn + " " + sy.objectName);
				if (toObjSyn == true && addRecord == true) {
					Connection con = null;
					toObjSyn = false;

					sy.saveObject2(username);
					try {
						con = EDatabase.borrowConnection();
						int vsName = Integer.parseInt(sy.getObjName(con));
						// sy.saveRecords(vsName, username, 0, sy.certainty,
						// sy.size);

						SynapseViewFrame vsframe = new SynapseViewFrame(vsName, username, this, "add");
						vsframe.setVisible(true);
						ELog.info("recorded" + isAPressed + " " + addRecord + " " + toObjSyn + " " + sy.objectName);

						sy = new CellObject();
						addRecord = false;
					} finally {
						EDatabase.returnConnection(con);
					}
				}
				// Util.info("ctrl button released");

			}

			if (e.getKeyCode() == KeyEvent.VK_V) {
				isVPressed = false;

			}

			if (e.getKeyCode() == KeyEvent.VK_U) {
				isUPressed = false;

			}

			if (e.getKeyCode() == KeyEvent.VK_ALT)
			// if(e.getModifiers() == KeyEvent.CTRL_MASK)
			{
				isAltButtonPressed = false;
				if (toObjSyn == true) {
					toObjSyn = false;

					listOfPoints.addElement(sy.p);
					sy.saveObject(username);

					Connection con = null;
					try {
						con = EDatabase.borrowConnection();
						int vsName = Integer.parseInt(sy.getObjName(con));
						// sy.saveRecords(vsName, username, 0, sy.certainty,
						// sy.size);

						listOfObjects.addElement(sy);
						repaint();
						SynapseViewFrame vsframe = new SynapseViewFrame(vsName, username, this, "new");
						vsframe.setVisible(true);

						sy = new CellObject();
					} finally {
						EDatabase.returnConnection(con);
					}
				}
				// Util.info("ctrl button released");
			}

		} catch (Throwable e2) {
			ELog.info("cant get object data " + e2);
			throw new IllegalStateException("cant get object data", e2);
		}
	}

	/**
	 * returns the selected point
	 * 
	 * @return The selected point
	 */
	public Point getSelectedPoint() {
		return selectedPoint;
	}

	/**
	 * assigns a new value to the selected point
	 * 
	 * @param newSelectedPoint
	 *            The new selected point
	 */
	public void setSelectedPoint(Point newSelectedPoint) {
		selectedPoint = newSelectedPoint;
	}

	public boolean selectPoint(Point p) {
		int num = listOfPoints.size();

		for (int i = 0; i < num; i++) {
			Point p1 = (Point) listOfPoints.elementAt(i);
			// int size = ( 10 + ( int ) ( current_zoom * 15.0f ) );
			int size = (20 + (int) (current_zoom * 10.0f));
			// int size = ( 20 + ( int ) ( current_zoom * 15.0f ) );
			// int size = ( int )( Math.max(10.0f, 3.0f/current_zoom) + (
			// current_zoom * 15.0f ) );
			RoundRectangle2D.Float ell = new RoundRectangle2D.Float(p1.x - (size / 2), p1.y - (size / 2), size, size, size / 6, size / 6);

			// Rectangle ell = new Rectangle(p1.x-size/2, p1.y-size/2, size,
			// size);
			if (ell.contains(p)) {
				selectedPoint = p1;

				return true;
			}
		}

		selectedPoint = null;

		return false;
	}

	public boolean selectScreenPoint(Point p) {
		Connection con = null;
		try {
			con = EDatabase.borrowConnection();
			int num = listOfPoints.size();
			int minDistance = Integer.MAX_VALUE;
			int index = -1;
			for (int i = 0; i < num; i++) {
				Point p1 = (Point) listOfPoints.elementAt(i);
				p1 = getImagePointAsScreenPoint(p1);
				double distance = Math.sqrt((p.x - p1.x) * (p.x - p1.x) + (p.y - p1.y) * (p.y - p1.y));
				if (distance < minDistance) {
					index = i;
					minDistance = (int) distance;
				}
			}

			if (index == -1) {
				selectedPoint = null;
				return false;
			}

			if (minDistance > (8 + current_zoom * 10)) {
				selectedPoint = null;
				return false;
				/**
				 * num = listOfSynapses.size ( ); minDistance =
				 * Integer.MAX_VALUE; for ( int j = 0; j < num; j++ ) { Point p2
				 * = (( CellObject ) listOfSynapses.elementAt ( j )).p; p2 =
				 * getImagePointAsScreenPoint(p2); double distance =
				 * Math.sqrt((p.x-p2.x) * (p.x-p2.x) + (p.y-p2.y)* (p.y-p2.y));
				 * if(distance < minDistance) { index = j; minDistance = (int)
				 * distance; } } if (index == -1) { selectedPoint = null; return
				 * false; } if(minDistance > (8+ current_zoom*10)) {
				 * selectedPoint = null; return false; } else { selectedPoint =
				 * ((CellObject)listOfSynapses.elementAt(index)).p; return true;
				 * }
				 */
			} else {
				selectedPoint = (Point) listOfPoints.elementAt(index);
				selectedObj = new CellObject(imageNumber, selectedPoint, con);
				preObjName = selectedObj.objectName;
				ELog.info("preObjName:" + preObjName);
				return true;
			}
		} catch (Throwable e) {
			ELog.info("cant get object data " + e);
			throw new IllegalStateException("cant get object data", e);
		} finally {
			EDatabase.returnConnection(con);
		}
		// //int size = ( 10 + ( int ) ( current_zoom * 15.0f ) );
		// int size = ( 20 + ( int ) ( current_zoom * 10.0f ) );
		// //int size = ( 20 + ( int ) ( current_zoom * 15.0f ) );
		// //int size = ( int )( Math.max(10.0f, 3.0f/current_zoom) + (
		// current_zoom * 15.0f ) );
		// Ellipse2D.Float ell =
		// new Ellipse2D.Float(
		// p1.x - ( size / 2 ),
		// p1.y - ( size / 2 ),
		// size,
		// size
		// );
		//
		// //Rectangle ell = new Rectangle(p1.x-size/2, p1.y-size/2, size,
		// size);
		// if ( ell.contains ( p ) )
		// {
		// selectedPoint = p1;
		//
		// return true;
		// }
		// }

		// selectedPoint = null;

		// return false;
	}

	/**
	 * returns the imageNumber of this panel
	 * 
	 * @return The imageNumber of this panel
	 */
	public String getImageNumber() {
		return imageNumber;
	}

	/**
	 * Sets the image number for this panel
	 * 
	 * @param newImageNumber
	 *            the new image number
	 */
	public void setImageNumber(String newImageNumber) {
		imageNumber = newImageNumber;
	}

	private int deletePointFromDatabase() {
		Connection con = null;
		int objN = 0;
		try {
			con = EDatabase.borrowConnection();

			PreparedStatement pst = con.prepareStatement(log("SELECT OBJ_Name from object where OBJ_X = ? and OBJ_Y = ? and IMG_Number = ?"));
			pst.setInt(1, deletedPoint.x);
			pst.setInt(2, deletedPoint.y);
			pst.setString(3, imageNumber);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				objN = rs.getInt(1);
			}
			rs.close();
			pst.close();
			// remove the relationship
			for (int i = 0; i < listOfRelations.size(); i++) {
				Relation rel = (Relation) listOfRelations.elementAt(i);

				if (rel == null || rel.getObj1() == null || rel.getObj1().p == null || deletedPoint == null || rel.getObj2() == null || rel.getObj2().p == null)
					continue;

				if (rel.getObj1().p.equals(deletedPoint) || rel.getObj2().p.equals(deletedPoint)) {

					listOfRelations.remove(rel);
				}
			}

			pst = con.prepareStatement("DELETE from object where OBJ_Name = ?");
			pst.setInt(1, objN);
			int rows = pst.executeUpdate();
			pst.close();

			pst = con.prepareStatement("DELETE from relationship where ObjName1 = ? or ObjName2 = ?");
			pst.setInt(1, objN);
			pst.setInt(2, objN);
			pst.executeUpdate();
			pst.close();

			pst = con.prepareStatement("DELETE from synrecord where synID = ?");
			pst.setInt(1, objN);
			pst.executeUpdate();
			pst.close();
			repaint();

			return rows;
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

			return -1;
		} finally {
			EDatabase.returnConnection(con);
		}
	}

	private void updatePointInDatabase() {
		Connection con = null;

		try {
			con = EDatabase.borrowConnection();

			PreparedStatement pst = con.prepareStatement("update object set OBJ_X = ?, OBJ_Y = ? where OBJ_X = ? and OBJ_Y = ? and IMG_Number = ?");

			// Util.info("selected point = "+selectedPoint+" and
			// deletedPoint =" + deletedPoint);
			pst.setInt(1, selectedPoint.x);
			pst.setInt(2, selectedPoint.y);
			pst.setInt(3, deletedPoint.x);
			pst.setInt(4, deletedPoint.y);
			pst.setString(5, imageNumber);

			int rows = pst.executeUpdate();

			// Util.info("selected point = "+selectedPoint+" and
			// deletedPoint =" + deletedPoint + " " + rows + " rows updated");

		} catch (Exception ex) {

			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

		} finally {
			EDatabase.returnConnection(con);
		}
	}

	/**
	 * Removes from the listOfRelations, all the relations that have the same
	 * image number as this panel.
	 */
	public void removeRelations() {
		Vector v = new Vector();

		for (int i = 0; i < listOfRelations.size(); i++) {
			Relation rel = (Relation) listOfRelations.elementAt(i);
			CellObject obj1 = rel.getObj1();
			if (obj1 == null || obj1.imageNumber == null || imageNumber == null)
				continue;
			if (obj1.imageNumber.compareToIgnoreCase(imageNumber) == 0) {
				v.add(rel);
			} else {
				CellObject obj2 = rel.getObj2();

				if (obj2.imageNumber.compareToIgnoreCase(imageNumber) == 0) {
					v.add(rel);
				}
			}
		}

		listOfRelations.removeAll(v);
	}

	/**
	 * Adds to the listOfRelations all the relations from the database that
	 * involve the image number of this particular image.
	 */

	public void addRelations() {

		if (Elegance.filterOptions.isHideAll() || Elegance.filterOptions.getContinFilterType() == FilterOptions.ContinFilterType.none) {
			cleanRelationsData();
			return;
		}

		Connection con = null;

		try {

			con = EDatabase.borrowConnection();

			PreparedStatement pst = null;
			String sql = "";

			sql =

			"select m.segmentNum,\n" + "o1.OBJ_Name OBJ_Name_1,o1.CON_Number CON_Number_1,o1.OBJ_X x1,o1.OBJ_Y y1, o1.IMG_Number IMG_Number_1,\n"
					+ "o1.OBJ_Name OBJ_Name_2,o1.CON_Number CON_Number_2,o2.OBJ_X x2,o2.OBJ_Y y2, o2.IMG_Number IMG_Number_2,\n"
					+ "o1.type type_1,o1.fromObj fromObj_1,o1.toObj toObj_1,o1.checked checked_1,\n"
					+ "o2.type type_2,o2.fromObj fromObj_2,o2.toObj toObj_2,o2.checked checked_2\n" + "from\n"
					+ "(select r.ObjName1, r.ObjName2, r.segmentNum\n" + "from object o\n" + "join relationship r on r.ObjName1=o.OBJ_Name\n"
					+ "where o.IMG_Number = ?\n";

			if (Elegance.filterOptions.getContinFilterType() != FilterOptions.ContinFilterType.all && Elegance.filterOptions.getContinNums().length > 0) {
				sql = sql + "and o.CON_Number in (" + EString.join(Elegance.filterOptions.getContinNums(), ",") + ")";
			}

			sql = sql + ") as m\n" + "join object o1 on m.objName1=o1.OBJ_Name\n" + "join object o2 on m.objName2=o2.OBJ_Name\n";

			ELog.info("Getting relations: " + sql);

			pst = con.prepareStatement(sql);
			pst.setString(1, imageNumber);

			ResultSet rs = pst.executeQuery();

			while (rs.next()) {

				Point pt = new Point();
				pt.x = rs.getInt("x1");
				pt.y = rs.getInt("y1");

				String img = rs.getString("IMG_Number_1");

				CellObject obj1 = new CellObject(pt, rs.getString("OBJ_Name_1"), img, rs.getInt("CON_Number_1"), rs.getString("type_1"),
						rs.getString("fromObj_1"), rs.getString("toObj_1"), rs.getInt("checked_1"));

				pt = new Point();
				pt.x = rs.getInt("x2");
				pt.y = rs.getInt("y2");

				img = rs.getString("IMG_Number_2");

				CellObject obj2 = new CellObject(pt, rs.getString("OBJ_Name_2"), img, rs.getInt("CON_Number_2"), rs.getString("type_2"),
						rs.getString("fromObj_2"), rs.getString("toObj_2"), rs.getInt("checked_2"));

				int segmentNumber = rs.getInt("segmentNum");

				Relation rel1 = new Relation(obj1, obj2, segmentNumber);

				if (ImageDisplayPanel.listOfRelations.contains(rel1) == false) {
					ImageDisplayPanel.listOfRelations.add(rel1);
				}
			}

		} catch (Throwable e) {
			ELog.info("Cant add relations " + this + "|" + e);
			throw new IllegalStateException("Cant add relations " + this + "|", e);

		} finally {
			EDatabase.returnConnection(con);
		}
	}

	public void paintSelectedPoint(Graphics2D g2D) {
		Point p2 = getImagePointAsScreenPoint(selectedPoint);

		if (selectedObj == null || selectedObj.objectName == null) {
			ELog.info("Cant paint selected point because obj name is null at " + p2.x + "x" + p2.y);
			return;
		}

		/*
		 * if (!showObject(selectedObj)) {
		 * Util.info("not painting selected point for object "
		 * +selectedObj.objectName+" because it's filtered out"); return; }
		 */
		if (!((p2.x < 0) || (p2.y < 0) || (p2.x > componentWidth) || (p2.y > componentHeight))) {
			int size = 8 + (int) (current_zoom * 10.0f);

			g2D.draw(new Ellipse2D.Float(p2.x - (size / 2), p2.y - (size / 2), size, size));
			Font font = new Font("sansserif", Font.PLAIN, 16);
			g2D.setFont(font);
			g2D.setPaint(Color.black);
			g2D.drawString(selectedObj.objectName, p2.x - size, p2.y);
			g2D.setStroke(new BasicStroke(2.0f));

		}
	}

	private void paintRelationPoints(Graphics g, Map<Integer, Contin> allContins) {
		// if (true) return;
		Graphics2D g2D = (Graphics2D) g;

		CellObject obj1 = relation.getObj1();
		CellObject obj2 = relation.getObj2();
		g2D.setPaint(new Color(0.2f, 0.8f, 0.2f));
		drawCellObject(obj1, g2D, allContins);
		g2D.setPaint(new Color(0.99f, 0.0f, 0.99f));
		drawCellObject(obj2, g2D, allContins);
	}

	private void drawCellObject(CellObject obj, Graphics2D g, Map<Integer, Contin> allContins) {
		if (obj == null || obj.imageNumber == null || this.imageNumber == null) {
			return;
		}

		if (obj.imageNumber.compareToIgnoreCase(this.imageNumber) != 0) {
			return;
		}

		if (obj.p == null) {
			return;
		} else {
			Point p2 = getImagePointAsScreenPoint(obj.p);

			if (!((p2.x < 0) || (p2.y < 0) || (p2.x > componentWidth) || (p2.y > componentHeight))) {
				int size = 8 + (int) (current_zoom * 10.0f);

				g.draw(new Ellipse2D.Float(p2.x - (size / 2), p2.y - (size / 2), size, size));

				String continName = null;

				if (allContins != null) {
					Contin contin = allContins.get(new Integer(obj.continNumber));
					if (contin != null)
						continName = contin.getName();
				}

				if (continName == null)
					continName = Utilities.getContinName(new Integer(obj.continNumber));
				g.drawString(continName, p2.x + size, p2.y + size);
			}
		}

		// Util.info("painted 1 relation pt");
	}

	private static void deleteRelation() {
		listOfRelations.removeElement(relation);
		relation.removeRelationInDatabase();
		relation = new Relation();
	}

	/**
	 * Returns the number of relations
	 * 
	 * @return The number of relations
	 */
	public static int getNumberOfRelations() {
		return listOfRelations.size();
	}

	/**
	 * Gets the relation that corresponds to the index provided in the
	 * listOfRelations Vector.
	 * 
	 * @param index
	 *            The zero based index
	 * 
	 * @return The Relation at the index provided or null of the index lesser
	 *         than 0 or greaterthen or equal to the size of the listOfRelations
	 *         Vector
	 */
	public static Relation getRelationForIndex(int index) {
		if ((index >= listOfRelations.size()) || (index < 0)) {
			return null;
		}

		return (Relation) listOfRelations.elementAt(index);
	}

	/**
	 * Removes the relation at the index specified. The removal is also
	 * reflected in the database.
	 * 
	 * @param row
	 *            a zero based index number. If row less than 0 or greater than
	 *            or equal to listOfrelations.size() then the function does
	 *            nothing and returns.
	 */
	public static void removeRelation(int row) {
		if ((row >= listOfRelations.size()) || (row < 0)) {
			return;
		}

		relation = (Relation) listOfRelations.elementAt(row);
		deleteRelation();
	}

	/**
	 * Creates a JTable with all the objects on the image corresponding to this
	 * panel as elements. The table has four columns -Object name, x, y, contin
	 * number.
	 * 
	 * @return The JTable
	 */
	public JTable getObjectTable() {

		Connection con = null;
		try {
			con = EDatabase.borrowConnection();

			Vector columnNames = new Vector();
			columnNames.addElement("Object Name");
			columnNames.addElement("X");
			columnNames.addElement("Y");
			columnNames.addElement("Contin Number");

			Vector data = new Vector();

			Set<String> allImages = new HashSet<String>();

			for (int i = 0; i < listOfPoints.size(); i++) {
				CellObject obj = new CellObject(imageNumber, (Point) listOfPoints.elementAt(i), con);
				allImages.add(obj.imageNumber);
			}

			Map<EDatabase.XYKey, CellObject> objects = EDatabase.getObjectsByImages(allImages);

			for (int i = 0; i < listOfPoints.size(); i++) {
				Vector row = new Vector();

				Point pt = (Point) listOfPoints.elementAt(i);
				CellObject obj = objects.get(new EDatabase.XYKey(imageNumber, pt.x, pt.y));

				if (obj == null)
					continue;

				row.addElement(obj.objectName);
				row.addElement(new Integer(obj.p.x));
				row.addElement(new Integer(obj.p.y));
				Integer continNum = obj.continNumber;
				row.addElement(continNum);
				data.addElement(row);
			}

			ObjectTableModel otmodel = new ObjectTableModel(data, columnNames);
			table = new JTable(otmodel);
			table.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					table_keyPressed(e);
				}
			});
			table.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent me) {
					table_mouseClicked(me);
				}
			});
			return table;

		} catch (Throwable e) {
			ELog.info("cant get object data " + e);
			throw new IllegalStateException("cant get object data", e);
		} finally {
			EDatabase.returnConnection(con);
		}
	}

	private void table_mouseClicked(MouseEvent me) {
		Point clickPoint = me.getPoint();
		int rowNo = table.rowAtPoint(clickPoint);
		// int colNo = table.columnAtPoint(clickPoint);
		if (rowNo >= 0) {
			int x = ((Integer) table.getValueAt(rowNo, 1)).intValue();
			int y = ((Integer) table.getValueAt(rowNo, 2)).intValue();
			Point p = new Point(x, y);
			selectPoint(p);
			this.repaint();
		}
	}

	private void table_keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_DELETE) {
			int row = table.getSelectedRow();

			if (row != -1) {
				int x = ((Integer) table.getValueAt(row, 1)).intValue();
				int y = ((Integer) table.getValueAt(row, 2)).intValue();
				Point p = new Point(x, y);
				removePoint(p);
				this.repaint();

				// table = getObjectTable();
				// table.repaint();
			}
		}
	}

	private void updatePointInRelationships(Point deletedPoint, Point selectedPoint) {
		// if (true) return;
		Vector listOfRelationsToBeModified = new Vector();
		Vector listOfNewRelations = new Vector();

		for (int i = 0; i < listOfRelations.size(); i++) {
			Relation rel = (Relation) listOfRelations.elementAt(i);

			// Util.info("i = " + i + " corresponding rel = " + rel + "
			// and deletedpoint" + deletedPoint);
			CellObject obj1 = rel.getObj1();
			boolean flag = false;

			if (obj1 == null || obj1.p == null || deletedPoint == null)
				continue;
			// CellObject newObj1, newObj2;
			if (obj1.p.equals(deletedPoint)) {
				// newObj1 = new CellObject(obj1.imageNumber, selectedPoint);
				obj1.p = selectedPoint;
				flag = true;
			}

			CellObject obj2 = rel.getObj2();

			if (obj2.p.equals(deletedPoint)) {
				// newObj2 = new CellObject(obj2.imageNumber, selectedPoint);
				obj2.p = selectedPoint;
				flag = true;
			}

			if (flag == true) {
				// listOfRelations.removeElement ( rel );
				// Relation newRel =
				// new Relation( newObj1, newObj2, rel.getType ( ) );
				Relation newRel = new Relation(obj1, obj2);

				// listOfRelations.addElement ( newRel );
				listOfRelationsToBeModified.addElement(rel);
				listOfNewRelations.addElement(newRel);

				// Util.info("one relation replaced by " + newRel);
			}
		}

		listOfRelations.removeAll(listOfRelationsToBeModified);
		listOfRelations.addAll(listOfNewRelations);

		// Util.info("numberOfrelations = " + listOfRelations.size());
	}

	public void cleanObjectsData() {
		listOfPoints = new Vector();
	}

	public static void cleanRelationsData() {
		listOfRelations = new Vector();
	}

	private void updateObjectTableSelection() {
		if (MainFrame.allObjectsFrame == null || MainFrame.allObjectsFrame.jTable1 == null)
			return;
		int numRows = MainFrame.allObjectsFrame.jTable1.getRowCount();
		for (int i = 0; i < numRows; i++) {
			String imageNoTable = (String) MainFrame.allObjectsFrame.jTable1.getValueAt(i, 0);
			if (imageNoTable.compareToIgnoreCase(this.imageNumber) == 0) {
				int x = ((Integer) MainFrame.allObjectsFrame.jTable1.getValueAt(i, 2)).intValue();
				int y = ((Integer) MainFrame.allObjectsFrame.jTable1.getValueAt(i, 3)).intValue();
				Point p1 = new Point(x, y);
				if (p1.equals(selectedPoint)) {
					// Util.info(p1);
					MainFrame.allObjectsFrame.jTable1.setRowSelectionInterval(i, i);
					MainFrame.allObjectsFrame.jTable1.repaint();
					return;
				}
			}
		}
	}

	public Vector getListOfPoints() {
		return listOfPoints;
	}

	/**
	 * This method should return an instance of this class which does NOT
	 * initialize it's GUI elements. This method is ONLY required by Jigloo if
	 * the superclass of this class is abstract or non-public. It is not needed
	 * in any other situation.
	 */
	public static Object getGUIBuilderInstance() {
		return new ImageDisplayPanel(Boolean.FALSE);
	}

	/**
	 * This constructor is used by the getGUIBuilderInstance method to provide
	 * an instance of this class which has not had it's GUI elements initialized
	 * (ie, initGUI is not called in this constructor).
	 */
	public ImageDisplayPanel(Boolean initGUI) {
		super();
	}

	public void showObj(String showName) {
		Connection con = null;

		try {
			con = EDatabase.borrowConnection();

			Point p1 = (CellObject.createCellObject(showName, con)).p;

			p1 = getImagePointAsScreenPoint(p1);

			selectScreenPoint(p1);
			paintSelectedPoint((Graphics2D) this.getGraphics());
			repaint();
		} catch (Throwable e) {
			ELog.info("cannot show obj " + showName + "|" + e);
		} finally {
			EDatabase.returnConnection(con);
		}

	}

	/*
	 * Releases memory taken by the underlying images
	 */
	public void freeMemory() {

		source = null;
		originalSource = null;

	}

}
