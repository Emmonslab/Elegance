import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.text.SimpleDateFormat;

import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;


class Node
{
	double  x;
	double  y;
	double  dy;
	boolean fixed;
	String  lbl;
	Vector  listOfSynapses;

	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public String toString (  )
	{
		return lbl;
	}
}

class NodeSerializable implements Serializable

{
    double  y;
	String  lbl;
}

class Edge
{
	int    from;
	int    to;
	double len = 0;

	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public String toString (  )
	{
		return "" + from + " - " + to;
	}
}


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class Spring2DDisplayPanel
	extends GraphicalDisplayPanel
	implements Runnable,
		MouseListener,
		MouseMotionListener
{
	private static final int EXTRA_SPACE = 25;
    private static final int SIDE_OF_OBJECT_BOX = 8;
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
	private static final Font              SYMBOL_FONT = NUMBER_FONT;
	int                      continNumber;
	String                   continName        = "";
	boolean                  horizontal        = true;
	boolean                  springAlgoRunning = true;
	Thread                   relaxer           = null;
	Node []                  nodes;
	Edge []                  edges;
	Node []                  originalNodes;
	Node                      pick;
	boolean                   pickfixed;
	Image                     offscreen;
	Dimension                 offscreensize;
	Graphics                  offgraphics;
	final Color               fixedColor           = Color.red;
	final Color               selectColor          = Color.pink;
	final Color               edgeColor            = Color.black;
	final Color               nodeColor            = new Color( 250, 220, 100 );
	final Color               stressColor          = Color.darkGray;
	final Color               arcColor1            = Color.BLACK;
	final Color               arcColor2            = Color.BLACK;
	final Color               arcColor3            = Color.BLACK;
	private JPopupMenu        jPopupMenu1          = new JPopupMenu(  );
	private JCheckBoxMenuItem popupRunAlgoMenuItem = new JCheckBoxMenuItem(  );

    private double minSectionNo= Integer.MAX_VALUE;
    private double maxSectionNo= 0;
    private double minYValue= Integer.MAX_VALUE;
    private double maxYValue= 0;

    private Hashtable serializedLocations = null;
    private int serializedHeight;
    private boolean useSerializedValues = false;
	/**
	 * Creates a new Spring2DDisplayPanel object.
	 *
	 * @param newContinNumber DOCUMENT ME!
	 */
	public Spring2DDisplayPanel ( int newContinNumber )
	{
		continNumber = newContinNumber;

		Vector listOfRelations = getListOfExtendedRelations ( continNumber );
		Vector listOfNodes = new Vector(  );
		Vector listOfEdges = new Vector(  );

		for ( int i = 0; i < listOfRelations.size (  ); i++ )
		{
			addEdge ( 
			    ( ExtendedRelation ) listOfRelations.elementAt ( i ),
			    listOfEdges,
			    listOfNodes
			 );

			//Util.info(listOfRelations.elementAt(i));
		}

		nodes             = new Node[ listOfNodes.size (  ) ];
		originalNodes     = new Node[ listOfNodes.size (  ) ];
		edges             = new Edge[ listOfEdges.size (  ) ];

		for ( int i = 0; i < nodes.length; i++ )
		{
			nodes [ i ] = ( Node ) listOfNodes.elementAt ( i );

			Node n = new Node(  );
			n.x                     = nodes [ i ].x;
			n.y                     = nodes [ i ].y;
			n.dy                    = nodes [ i ].dy;
			n.lbl                   = nodes [ i ].lbl;
			n.fixed                 = nodes [ i ].fixed;
			n.listOfSynapses        = nodes [ i ].listOfSynapses;
			originalNodes [ i ]     = n;

			//Util.info(nodes[i]);
		}

		for ( int i = 0; i < edges.length; i++ )
		{
			edges [ i ] = ( Edge ) listOfEdges.elementAt ( i );

			//originalEdges[i] = (Edge)listOfEdges.elementAt(i);
			//Util.info(edges[i]);
		}

		setContextSpecificCoordinates (  );
		continName         = Utilities.getContinName ( new Integer( continNumber ) );
		continNameText     = continName;
		preferredTitle     = "2-Dimensional Graphical Display - Contin " + continName;
		this.addMouseListener ( this );
		this.repaint (  );
		this.start (  );
	}

    public Spring2DDisplayPanel ( int newContinNumber, String fileName )
	{
        Vector listOfSerializedNodes = new Vector();
        serializedHeight=300;
        try 
        {
            ObjectInputStream oiStream = new ObjectInputStream(new FileInputStream(fileName));
            listOfSerializedNodes = (Vector) oiStream.readObject();
            //Util.info("listOfSerializedNodes.size() = " + listOfSerializedNodes.size());
            serializedHeight = oiStream.readInt();
            //Util.info("old height = " + serializedHeight);
        } catch (Exception ex) 
        {
            ex.printStackTrace();
            listOfSerializedNodes = new Vector(0);
        }

        serializedLocations = new Hashtable();
        for (int i = 0; i < listOfSerializedNodes.size(); i++) 
        {
            NodeSerializable ns = (NodeSerializable) listOfSerializedNodes.elementAt(i);
            serializedLocations.put(ns.lbl, new Double(ns.y));
        }
        //double serializedToThisRatio = ((double)this.getHeight())/((double)serializedHeight);
        //Util.info("serializedToThisRatio = "+ serializedToThisRatio);
        //Util.info("this.height = " + this.getHeight());

        continNumber = newContinNumber;

		Vector listOfRelations = getListOfExtendedRelations ( continNumber );
		Vector listOfNodes = new Vector(  );
		Vector listOfEdges = new Vector(  );

		for ( int i = 0; i < listOfRelations.size (  ); i++ )
		{
			addEdge ( 
			    ( ExtendedRelation ) listOfRelations.elementAt ( i ),
			    listOfEdges,
			    listOfNodes
			 );

			//Util.info(listOfRelations.elementAt(i));
		}

		nodes             = new Node[ listOfNodes.size (  ) ];
		originalNodes     = new Node[ listOfNodes.size (  ) ];
		edges             = new Edge[ listOfEdges.size (  ) ];

		for ( int i = 0; i < nodes.length; i++ )
		{
			nodes [ i ] = ( Node ) listOfNodes.elementAt ( i );

			Node n = new Node(  );
			n.x                     = nodes [ i ].x;
			n.y                     = nodes [ i ].y;
			n.dy                    = nodes [ i ].dy;
			n.lbl                   = nodes [ i ].lbl;
			n.fixed                 = nodes [ i ].fixed;
			n.listOfSynapses        = nodes [ i ].listOfSynapses;
			originalNodes [ i ]     = n;
        	//Util.info(nodes[i]);
		}

		for ( int i = 0; i < edges.length; i++ )
		{
			edges [ i ] = ( Edge ) listOfEdges.elementAt ( i );

			//originalEdges[i] = (Edge)listOfEdges.elementAt(i);
			//Util.info(edges[i]);
		}
//        for ( int i = 0; i < nodes.length; i++ )
//		{
//			if(hash.containsKey(nodes[i].lbl))
//            {
//                //Util.info("found key");
//                double serializedY = ((Double)hash.get(nodes[i].lbl)).doubleValue();
//                nodes[i].y = serializedY*serializedToThisRatio;
//                Util.info("serializedY= " + serializedY);
//                Util.info("y set to " + serializedY*serializedToThisRatio);
//            }
//
//			//Util.info(nodes[i]);
//		}
        springAlgoRunning = false;
        useSerializedValues = true;
        setContextSpecificCoordinates (  );
		continName         = Utilities.getContinName ( new Integer( continNumber ) );
		continNameText     = continName;
		preferredTitle     = "2-Dimensional Graphical Display - Contin " + continName;
		this.addMouseListener ( this );
		this.repaint (  );
		this.start (  );
    }

	/**
	 * Creates a new Spring2DDisplayPanel object.
	 */
	public Spring2DDisplayPanel (  )
	{
		try
		{
			jbInit (  );
		}
		catch ( Exception e )
		{
			e.printStackTrace (  );
		}
	}

	int findNode ( 
	    ExtendedCellObject object,
	    Vector             listOfNodes
	 )
	{
		String lbl = object.objectName;

		for ( int i = 0; i < listOfNodes.size (  ); i++ )
		{
			Node n = ( Node ) listOfNodes.elementAt ( i );

			if ( n.lbl.equals ( lbl ) )
			{
				return i;
			}
		}

		return addNode ( object, listOfNodes );
	}

	int addNode ( 
	    ExtendedCellObject object,
	    Vector             listOfNodes
	 )
	{
		Node n = new Node(  );
		n.x                  = object.sectionNumber;
		n.y                  = object.p.y;
		n.fixed              = false;
		n.lbl                = object.objectName;
		n.listOfSynapses     = object.getListOfSynapses (  );

		listOfNodes.addElement ( n );

		return listOfNodes.size (  ) - 1;
	}

	void addEdge ( 
	    ExtendedRelation relation,
	    Vector           listOfEdges,
	    Vector           listOfNodes
	 )
	{
		Edge e = new Edge(  );
		e.from     = findNode ( relation.obj1, listOfNodes );
		e.to       = findNode ( relation.obj2, listOfNodes );
		e.len      = 0;
		listOfEdges.addElement ( e );
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean isDrawable (  )
	{
		return true;
	}

	/**
	 * DOCUMENT ME!
	 */
	public void changeVisualization (  )
	{
		horizontal = !horizontal;
		repaint (  );
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param continNumber DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public Vector getListOfExtendedRelations ( int continNumber )
	{
		Vector     returner = new Vector(  );
		Connection con = null;

		try
		{
			con = EDatabase.borrowConnection ( 
				    
				   
				    
				 );

			PreparedStatement pst =
				con.prepareStatement ( 
				    "Select REL_ImgNumber1, REL_ObjName1, REL_ImgNumber2, REL_ObjName2,  REL_Type from Relationship where ( REL_ConNumber1 = ? OR REL_ConNumber2 = ? ) AND REL_Type = ?"
				 );
			pst.setInt ( 1, continNumber );
			pst.setInt ( 2, continNumber );
			pst.setString ( 3, GlobalStrings.CONTINUOUS );

			ResultSet rs = pst.executeQuery (  );

			while ( rs.next (  ) )
			{
				ExtendedCellObject obj1  =
					new ExtendedCellObject( 
					    rs.getString ( "REL_ImgNumber1" ),
					    rs.getString ( "REL_ObjName1" )
					 );
				ExtendedCellObject obj2  =
					new ExtendedCellObject( 
					    rs.getString ( "REL_ImgNumber2" ),
					    rs.getString ( "REL_ObjName2" )
					 );
				ExtendedRelation   exrel =
					new ExtendedRelation( obj1, obj2, rs.getString ( "REL_Type" ) );
				returner.addElement ( exrel );
			}

		
		}
		catch ( Exception ex )
		{
			ex.printStackTrace (  );
			JOptionPane.showMessageDialog ( 
			    null,
			    ex.getMessage (  ),
			    "Error",
			    JOptionPane.ERROR_MESSAGE
			 );

			
		}finally {
			EDatabase.returnConnection(con);
		}

		return returner;
	}

	//    public static void main(String[] args)
	//    {
	//        try 
	//        {
	//          Class.forName ( "com.mysql.jdbc.Driver" ).newInstance (  );
	//        } catch (Exception ex) 
	//        {
	//            ex.printStackTrace();
	//        } 
	//        new Spring2DDisplayPanel(1);
	//    }
	public void run (  )
	{
		Thread me = Thread.currentThread (  );

		while ( relaxer == me )
		{
			if ( springAlgoRunning )
			{
				relax (  );
			}

			if ( springAlgoRunning && ( Math.random (  ) < 0.03 ) )
			{
				Node n = nodes [ ( int ) ( Math.random (  ) * nodes.length ) ];

				if ( !n.fixed )
				{
					//n.x += 100*Math.random() - 50;
					n.y += ( ( 100 * Math.random (  ) ) - 50 );
				}
			}

			try
			{
				Thread.sleep ( 100 );
			}
			catch ( InterruptedException e )
			{
				break;
			}
		}
	}

	synchronized void relax (  )
	{
		for ( int i = 0; i < edges.length; i++ )
		{
			Edge e = edges [ i ];

			//double vx = nodes[e.to].x - nodes[e.from].x;
			double vy = nodes [ e.to ].y - nodes [ e.from ].y;

			//double len = Math.sqrt(vx * vx + vy * vy);
			double len = Math.sqrt ( vy * vy );
			len = ( len == 0 ) ? .0001 : len;

			double f = ( edges [ i ].len - len ) / ( len * 3 );

			//double dx = f * vx;
			double dy = f * vy;

			//nodes[e.to].dx += dx;
			nodes [ e.to ].dy += dy;

			//nodes[e.from].dx += -dx;
			nodes [ e.from ].dy += -dy;
		}

		for ( int i = 0; i < nodes.length; i++ )
		{
			Node n1 = nodes [ i ];

			//double dx = 0;
			double dy = 0;

			for ( int j = 0; j < nodes.length; j++ )
			{
				if ( i == j )
				{
					continue;
				}

				Node n2 = nodes [ j ];

				//double vx = n1.x - n2.x;
				double vy = n1.y - n2.y;

				//double len = vx * vx + vy * vy;
				double len = vy * vy;

				if ( len == 0 )
				{
					//dx += Math.random();
					dy += Math.random (  );
				}
				else if ( len < ( 100 * 100 ) )
				{
					//dx += vx / len;
					dy += ( vy / len );
				}
			}

			//double dlen = dx * dx + dy * dy;
			double dlen = dy * dy;

			if ( dlen > 0 )
			{
				dlen = Math.sqrt ( dlen ) / 2;

				//n1.dx += dx / dlen;
				n1.dy += ( dy / dlen );
			}
		}

		Dimension d = getSize (  );

		for ( int i = 0; i < nodes.length; i++ )
		{
			Node n = nodes [ i ];

			if ( !n.fixed )
			{
				//n.x += Math.max(-5, Math.min(5, n.dx));
				n.y += Math.max ( -5, Math.min ( 5, n.dy ) );
			}

			if ( n.x < 0 )
			{
				n.x = 0;
			}
			else if ( n.x > d.width )
			{
				n.x = d.width;
			}

			if ( n.y < 0 )
			{
				n.y = 0;
			}
			else if ( n.y > d.height )
			{
				n.y = d.height;
			}

			//n.dx /= 2;
			n.dy /= 2;
		}

		repaint (  );
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param g DOCUMENT ME!
	 * @param n DOCUMENT ME!
	 * @param fm DOCUMENT ME!
	 */
	public void paintNode ( 
	    Graphics    g,
	    Node        n,
	    FontMetrics fm
	 )
	{
		int x = ( int ) n.x;
		int y = ( int ) n.y;
		g.setColor ( ( n == pick ) ? selectColor : ( n.fixed ? fixedColor : nodeColor ) );

		/*int w = fm.stringWidth ( n.lbl ) + 10;
		int h = fm.getHeight (  ) + 4;
		g.fillRect ( x - ( w / 2 ), y - ( h / 2 ), w, h );
		g.setColor ( Color.black );
		g.drawRect ( x - ( w / 2 ), y - ( h / 2 ), w - 1, h - 1 );
		g.drawString ( 
		    n.lbl,
		    x - ( ( w - 10 ) / 2 ),
		    ( y - ( ( h - 4 ) / 2 ) ) + fm.getAscent (  )
		 );*/
         int w = SIDE_OF_OBJECT_BOX;
		int h = SIDE_OF_OBJECT_BOX;
		//g.fillRect ( x - ( w / 2 ), y - ( h / 2 ), w, h );
        g.fillRect ( x-1, y -1, 3, 3 );
		g.setColor ( Color.black );
		//g.drawRect ( x - ( w / 2 ), y - ( h / 2 ), w - 1, h - 1 );
        
        g.drawRect ( x-1, y-1, 2,2 );
        //g.drawString(n.lbl, x, y);
        drawSynapses(n, (Graphics2D)g);
		g.drawString ( 
		    " ",
		    x - ( ( w - 10 ) / 2 ),
		    ( y - ( ( h - 4 ) / 2 ) ) + fm.getAscent (  )
		 );
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param g DOCUMENT ME!
	 */
	public synchronized void update ( Graphics g )
	{
		//Util.info("update function called");
        Dimension d = getSize (  );
        if(horizontal)
        {
		
            if ( 
    		    ( offscreen == null )
                    || ( d.width != offscreensize.width )
                    || ( d.height != offscreensize.height )
            )
            {
    			offscreen         = createImage ( d.width, d.height );
                offscreensize     = d;
    
                if ( offgraphics != null )
                {
    				offgraphics.dispose (  );
                }
    
                offgraphics = offscreen.getGraphics (  );
                offgraphics.setFont ( getFont (  ) );
                setContextSpecificCoordinates (  );
            }
        }
        else
        {
            d = new Dimension((int)d.getHeight(), (int)d.getWidth());
            if ( 
    		    ( offscreen == null )
                    || ( d.width != offscreensize.width )
                    || ( d.height != offscreensize.height )
            )
            {
    			offscreen         = createImage ( d.width, d.height );
                offscreensize     = d;
    
                if ( offgraphics != null )
                {
    				offgraphics.dispose (  );
                }
    
                offgraphics = offscreen.getGraphics (  );
                //((Graphics2D)offgraphics).translate ( d.getWidth (  ) / 2.0f, d.getHeight (  ) / 2.0f );
                //((Graphics2D)offgraphics).rotate ( Math.toRadians ( 90 ) );
                //((Graphics2D)offgraphics).translate ( -d.getWidth (  ) / 2.0f, -d.getHeight (  ) / 2.0f );
                //((Graphics2D)offgraphics).translate ( -d.getHeight (  ) / 2.0f, -d.getWidth (  ) / 2.0f );
                offgraphics.setFont ( getFont (  ) );
                setContextSpecificCoordinates (  );
            }
        }

		offgraphics.setColor ( Color.WHITE );
        offgraphics.fillRect ( 0, 0, (int)offscreensize.getWidth(), (int)offscreensize.getHeight() );

        for ( int i = 0; i < edges.length; i++ )
		{
			Edge e   = edges [ i ];
			int  x1  = ( int ) nodes [ e.from ].x;
			int  y1  = ( int ) nodes [ e.from ].y;
			int  x2  = ( int ) nodes [ e.to ].x;
			int  y2  = ( int ) nodes [ e.to ].y;
			int  len =
				( int ) Math.abs ( 
				    Math.sqrt ( 
				        ( ( x1 - x2 ) * ( x1 - x2 ) ) + ( ( y1 - y2 ) * ( y1 - y2 ) )
				     ) - e.len
				 );
			offgraphics.setColor ( 
			    ( len < 10 ) ? arcColor1 : ( ( len < 20 ) ? arcColor2 : arcColor3 )
			 );
			offgraphics.drawLine ( x1, y1, x2, y2 );

			//	    if (stress) {
			//		String lbl = String.valueOf(len);
			//		offgraphics.setColor(stressColor);
			//		offgraphics.drawString(lbl, x1 + (x2-x1)/2, y1 + (y2-y1)/2);
			//		offgraphics.setColor(edgeColor);
			//	    }
		}

		FontMetrics fm = offgraphics.getFontMetrics (  );

		for ( int i = 0; i < nodes.length; i++ )
		{
			paintNode ( offgraphics, nodes [ i ], fm );
		}
        drawLabelledHorizontalLine((Graphics2D)offgraphics);

        if(!horizontal)
        {
            ((Graphics2D)g).translate(this.getWidth()/2.0, this.getHeight()/2.0);
            ((Graphics2D)g).rotate(Math.toRadians(90.0));
            ((Graphics2D)g).translate(-this.getHeight()/2.0, -this.getWidth()/2.0);
        }
		g.drawImage ( offscreen, 0, 0, null );
	}

	//1.1 event handling
	public void mouseClicked ( MouseEvent e )
	{
		
        if(e.getButton() == MouseEvent.BUTTON3)
        {
            //Util.info ( "button 3 mouseclicked" );
            springAlgoRunning = ! springAlgoRunning;
        }
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param e DOCUMENT ME!
	 */
	public void mousePressed ( MouseEvent e )
	{
        if(e.getButton() != MouseEvent.BUTTON1) return;
		addMouseMotionListener ( this );

		double bestdist = Double.MAX_VALUE;

        int x,y;

        if(horizontal)
        {
            x = e.getX (  );
        	y = e.getY (  );
        }
        else
        {
            x = e.getY (  );
        	y = getWidth() - e.getX (  );
        }

        for ( int i = 0; i < nodes.length; i++ )
		{
			Node   n    = nodes [ i ];
			double dist = ( ( n.x - x ) * ( n.x - x ) ) + ( ( n.y - y ) * ( n.y - y ) );

			if ( dist < bestdist )
			{
				pick         = n;
				bestdist     = dist;
			}
		}

		pickfixed      = pick.fixed;
		pick.fixed     = true;
		//pick.x         = x;
		pick.y         = y;
		repaint (  );
		e.consume (  );
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param e DOCUMENT ME!
	 */
	public void mouseReleased ( MouseEvent e )
	{
		removeMouseMotionListener ( this );

		if ( pick != null )
		{
			//pick.x         = e.getX (  );
            if(horizontal)
            {
                pick.y         = e.getY (  );
            }
            else
            {
                pick.y = getWidth() - e.getX (  );
            }
			pick.fixed     = pickfixed;
			pick           = null;
		}

		repaint (  );
		e.consume (  );
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param e DOCUMENT ME!
	 */
	public void mouseEntered ( MouseEvent e ) {}

	/**
	 * DOCUMENT ME!
	 *
	 * @param e DOCUMENT ME!
	 */
	public void mouseExited ( MouseEvent e ) {}

	/**
	 * DOCUMENT ME!
	 *
	 * @param e DOCUMENT ME!
	 */
	public void mouseDragged ( MouseEvent e )
	{
		//pick.x     = e.getX (  );
        if(horizontal)
        {
            pick.y     = e.getY (  );
        }
        else
        {
            pick.y     = getWidth() - e.getX (  );
        }
        repaint (  );
		e.consume (  );
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param e DOCUMENT ME!
	 */
	public void mouseMoved ( MouseEvent e ) {}

	/**
	 * DOCUMENT ME!
	 */
	public void start (  )
	{
		relaxer = new Thread( this );
		relaxer.start (  );
	}

	/**
	 * DOCUMENT ME!
	 */
	public void stop (  )
	{
		relaxer = null;
	}

	private void setContextSpecificCoordinates (  )
	{
		Dimension d            = getSize();
        if(!horizontal)
        {
            d = new Dimension(d.height, d.width);
        }
		for ( int i = 0; i < originalNodes.length; i++ )
		{
			if ( originalNodes [ i ].x < minSectionNo )
			{
				minSectionNo = originalNodes [ i ].x;
			}

			if ( originalNodes [ i ].x > maxSectionNo )
			{
				maxSectionNo = originalNodes [ i ].x;
			}

			if ( originalNodes [ i ].y < minYValue )
			{
				minYValue = originalNodes [ i ].y;
			}

			if ( originalNodes [ i ].y > maxYValue )
			{
				maxYValue = originalNodes [ i ].y;
			}

			//Util.info ( 
			//    "" + ( i + 1 ) + " " + originalNodes [ i ].x + ", "
			//    + originalNodes [ i ].y
			// );
		}

		//Util.info ( "minSectionNo = " + minSectionNo );
		//Util.info ( "maxSectionNo = " + maxSectionNo );

		double xScale =
			( double ) ( d.getWidth (  ) - ( 2 * EXTRA_SPACE ) ) / ( double ) ( 
					maxSectionNo - minSectionNo
				 );
		double yScale =
			( double ) ( d.getHeight (  ) - ( 4 * EXTRA_SPACE ) ) / ( double ) ( 
					maxYValue - minYValue
				 );

		for ( int i = 0; i < originalNodes.length; i++ )
		{
			nodes [ i ].x =
				EXTRA_SPACE
				+ ( int ) ( ( originalNodes [ i ].x - minSectionNo ) * xScale );
			nodes [ i ].y =
				EXTRA_SPACE + ( int ) ( 
					( originalNodes [ i ].y - minYValue ) * yScale
				 );
            if(useSerializedValues && serializedLocations.containsKey(nodes[i].lbl))
            {
                nodes[i].y = ((Double)serializedLocations.get(nodes[i].lbl)).doubleValue();
            }
		}
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param g DOCUMENT ME!
	 */
	public void paint ( Graphics g )
	{
        /*Graphics2D g2 = null;
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
			g2.translate ( this.getWidth (  ) / 2.0f, this.getHeight (  ) / 2.0f );
			g2.rotate ( Math.toRadians ( 90 ) );
			g2.translate ( -this.getHeight (  ) / 2.0f, -this.getWidth (  ) / 2.0f );
        }*/
		update ( g );
	}

	private void jbInit (  )
		throws Exception
	{
		jPopupMenu1.setLabel ( "jPopupMenu1" );
		popupRunAlgoMenuItem.setText ( "Run Optimization" );
		popupRunAlgoMenuItem.addActionListener ( 
		    new ActionListener(  )
			{
				public void actionPerformed ( ActionEvent e )
				{
					popupRunAlgoMenuItem_actionPerformed ( e );
				}
			}
		 );
		jPopupMenu1.add ( popupRunAlgoMenuItem );
	}

	private void popupRunAlgoMenuItem_actionPerformed ( ActionEvent e )
	{
		if ( popupRunAlgoMenuItem.isSelected (  ) )
		{
			springAlgoRunning = true;
		}
		else
		{
			springAlgoRunning = false;
		}
	}

    private void drawLabelledHorizontalLine ( Graphics2D g2 )
	{
		//int panelWidth  = this.getWidth (  );
		//int panelHeight = this.getHeight (  );

        int panelWidth = offscreensize.width;
        int panelHeight = offscreensize.height;

		//if ( !horizontal )
		//{
		//	panelWidth      = this.getHeight (  );
		//	panelHeight     = this.getWidth (  );
		//}

		//g2.setColor ( Color.WHITE );
		//g2.fillRect ( 0, 0, panelWidth, panelHeight );

		if ( 
		    ( panelHeight <= ( 2 * EXTRA_SPACE ) )
			    || ( panelWidth <= ( 2 * EXTRA_SPACE ) )
		 )
		{
			return;
		}

		panelWidth -= ( 2 * EXTRA_SPACE );
		panelHeight -= ( 2 * EXTRA_SPACE );

		int toLocationY = panelHeight - EXTRA_SPACE;

		//Util.info("toLocationY = "+toLocationY);
		g2.setColor ( Color.black );
		g2.setFont ( NUMBER_FONT );

		//String            fromText     = "" + from;
        String            fromText     = "" + (int)minSectionNo;
		TextLayout        tl           =
			new TextLayout( fromText, NUMBER_FONT, FONT_RENDER_CTXT );
		Rectangle2D.Float bound        = ( Rectangle2D.Float ) tl.getBounds (  );
		int               xAdjustment1 = ( BORDER * 2 ) + ( int ) bound.width;

		//int yAdjustment1 = BORDER + (int) (bound.height/2.0f);
		int yAdjustment1 = ( int ) ( bound.height / 2.0f );

		tl.draw ( g2, EXTRA_SPACE + BORDER, toLocationY + yAdjustment1 );

		String toText = "" + (int)maxSectionNo;
		tl        = new TextLayout( toText, NUMBER_FONT, FONT_RENDER_CTXT );
		bound     = ( Rectangle2D.Float ) tl.getBounds (  );

		int xAdjustment2 = ( BORDER * 2 ) + ( int ) bound.width;
		int yAdjustment2 = ( int ) ( bound.height / 2.0f );

		tl.draw ( 
		    g2,
		    ( EXTRA_SPACE + panelWidth ) - xAdjustment2,
		    toLocationY + yAdjustment2
		 );

        Stroke oldStroke = g2.getStroke();
        g2.setStroke ( new BasicStroke( WORM_LINE_THICKNESS ) );
		g2.drawLine ( 
		    EXTRA_SPACE + xAdjustment1,
		    toLocationY,
		    ( EXTRA_SPACE + panelWidth ) - xAdjustment2 - BORDER,
		    toLocationY
		 );
         g2.setStroke(oldStroke);

		int fromLocationX     = EXTRA_SPACE + xAdjustment1;
		int toLocationX       = ( EXTRA_SPACE + panelWidth ) - xAdjustment2 - BORDER;

		String headerText = continName.equals ( "" ) ? ( "" + continNumber ) : continName;
		tl        = new TextLayout( headerText, HEADING_FONT, FONT_RENDER_CTXT );
		bound     = ( Rectangle2D.Float ) tl.getBounds (  );

		int xAdjustment3 = ( int ) ( bound.width / 2.0f );
		int yAdjustment3 = ( int ) ( bound.height / 2.0f );

		tl.draw ( 
		    g2,
		    ( panelWidth / 2 ) - xAdjustment3,
		    toLocationY + EXTRA_SPACE + yAdjustment3
		 );
	}

    private void drawSynapses(Node n, Graphics2D g2)
    {
        Vector listOfSynapses = n.listOfSynapses;
        int currentSynapseDrawingLocationX = (int)n.x;
        int currentSynapseDrawingLocationY = (int)n.y - BORDER;
        for (int i = 0; i < listOfSynapses.size(); i++) 
        {
            //Util.info(listOfSynapses.elementAt(i));
            BranchedContinSynpase bcs = (BranchedContinSynpase) listOfSynapses.elementAt(i);
            Point fromPoint = new Point(currentSynapseDrawingLocationX, currentSynapseDrawingLocationY);
            Point toPoint = new Point(currentSynapseDrawingLocationX, currentSynapseDrawingLocationY - SYMBOL_LINE_LENGTH);
            g2.setStroke(new BasicStroke(SYMBOL_LINE_THICKNESS));
            if(bcs.type.compareToIgnoreCase(GlobalStrings.PRESYNAPTIC) == 0 || bcs.type.compareToIgnoreCase(GlobalStrings.MULTIPLE_PRESYNAPTIC) == 0)
            {
                drawPresynaptic(fromPoint, toPoint, g2);
            }
            else if(bcs.type.compareToIgnoreCase(GlobalStrings.POSTSYNAPTIC) == 0 || bcs.type.compareToIgnoreCase(GlobalStrings.MULTIPLE_POSTSYNAPTIC) == 0)
            {
                drawPresynaptic(toPoint, fromPoint, g2);
            }
            else if(bcs.type.compareToIgnoreCase(GlobalStrings.GAP_JUNCTION) == 0)
            {
                drawGap(toPoint, fromPoint, g2);
            }
            else
            {
                g2.drawLine(fromPoint.x, fromPoint.y, toPoint.x, toPoint.y);
            }
            int textHeight = (int) drawText(bcs.continName, toPoint.x, toPoint.y, g2);
            currentSynapseDrawingLocationY -= 2*BORDER + SYMBOL_LINE_LENGTH + textHeight;
            
        }
        
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
    private float drawText ( 
	    String     text,
        int horizontalLocationOfDrawingPoint,
        int verticcalLocationOfDrawingPoint,
	    Graphics2D g2
	 )
	{
		if ( ( text == null ) || text.equals ( "" ) )
		{
			return 0;
		}

		if ( horizontalLocationOfDrawingPoint <= 0 )
		{
			return 0;
		}

		TextLayout        tl    = new TextLayout( text, SYMBOL_FONT, FONT_RENDER_CTXT );
		Rectangle2D.Float bound = ( Rectangle2D.Float ) tl.getBounds (  );

		float xAdjustment = bound.width / 2.0f;

        float horLoc = horizontalLocationOfDrawingPoint - xAdjustment > 0 ? horizontalLocationOfDrawingPoint - xAdjustment : 1;
		tl.draw ( 
		    g2,
		    horLoc,
		    verticcalLocationOfDrawingPoint - WORM_LINE_THICKNESS - 1
		 );

		//tl.draw(g2, 100.0f, 100.0f);
		//return bound.height;
		//tl        = new TextLayout( text, NUMBER_FONT, FONT_RENDER_CTXT );
		//bound     = ( Rectangle2D.Float ) tl.getBounds (  );

		//Util.info("bound = " + bound);
		return bound.height;
	}
    public void saveData()
    {
        Vector listOfNodes = new Vector();
        for (int i = 0; i < nodes.length; i++) 
        {
            Node n = nodes[i];
            NodeSerializable ns = new NodeSerializable();
            ns.lbl = n.lbl;
            ns.y = n.y;

            listOfNodes.addElement(ns);
        }
        int height = offscreensize.height;

        java.util.Date   date = new java.util.Date( System.currentTimeMillis (  ) );

        SimpleDateFormat format = new SimpleDateFormat( "ddMMMyyyy_HH_mm" );
		String           dateStr    = format.format ( date );

			

        String fileName = Utilities.getFileNameFromUser("" + continName + "_" 
            + dateStr + ".2dd");
        if(fileName != null && fileName.length() > 0)
        {
            try 
            {
                ObjectOutputStream oostream = new ObjectOutputStream(new FileOutputStream(fileName));
                oostream.writeObject(listOfNodes);
                oostream.writeInt(height);
                oostream.flush();
                oostream.close();
                JOptionPane.showMessageDialog(null, "File was succesfully saved", "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
            } 
            catch (Exception ex) 
            {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "File was not saved", "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }            
            
        }
    }

    public void save()
    {
        String [] possibleValues = {"Save Layout","Save as Image"};
        String option = (String)(JOptionPane.showInputDialog(null, 
            "Choose the Save Option", 
            "Choose Save Option", JOptionPane.INFORMATION_MESSAGE, null,
            possibleValues, 
            possibleValues[0]));

        if(option.equals(possibleValues[1]))
        {
            super.save();
        }
        else
        {
            saveData();
        }
    }
}
