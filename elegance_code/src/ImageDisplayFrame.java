import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.TiledImage;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;


/**
 * This class is the extension of the JINternalFrame class that provides the
 * functionality fot the display of the images. It implements the FocusListener and
 * KeyListener interfaces to provide additional functionality.
 *
 * @author zndavid
 * @version 1.0
 */
public class ImageDisplayFrame
	extends JInternalFrame
	implements KeyListener,
		FocusListener
{
	private GridBagLayout     gridBagLayout1 = new GridBagLayout(  );
	private ImageDisplayPanel panel       = null;
	private JLabel            label       = null;
	String                    imageNumber = "";
    String  username = "";
  

	
	Dimension screenSize = Toolkit.getDefaultToolkit (  ).getScreenSize (  );
    int fwidth =   (int) (screenSize.width/3.02);
	int fheight =  (int)(screenSize.height*0.8);
	
	/**
	 * Creates a new ImageDisplayFrame object.
	 */
	public ImageDisplayFrame ( )
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


	
	/**
	 * Creates a new ImageDisplayFrame object.
	 *
	 * @param file location of the image file.
	 * @param imageNo The image number in the database for the image
	 */
	public ImageDisplayFrame ( 
	    final String file,
	    String imageNo,
		String name
		
	 )
	{
		imageNumber = imageNo;
        username = name;
		File f = new File( file );

		if ( f.exists (  ) && f.canRead (  ) )
		{
			this.setTitle ( f.getName (  ) );

            try
            {
                //PlanarImage src = JAI.create ( "fileload", file, null );
               
                /*if(f.getName().endsWith(".tif") || f.getName().endsWith(".tiff"))
                {
                    Util.info(System.currentTimeMillis() + "  1");
                    FileInputStream fis = new FileInputStream(f);
                    Util.info(System.currentTimeMillis() + "  2");
                    TIFFDecodeParam decode = new TIFFDecodeParam();
                    Util.info(System.currentTimeMillis() + "  3");
                    decode.setDecodePaletteAsShorts(true);
                    Util.info(System.currentTimeMillis() + "  4");
                    decode.setJPEGDecompressYCbCrToRGB(true);
                    Util.info(System.currentTimeMillis() + "  5");
                    ImageDecoder dec = ImageCodec.createImageDecoder("TIFF", fis, decode);
                    Util.info(System.currentTimeMillis() + "  6");
                    RenderedImage ri = dec.decodeAsRenderedImage();
                    Util.info(System.currentTimeMillis() + "  7");
                    src = PlanarImage.wrapRenderedImage(ri);
                    Util.info(System.currentTimeMillis() + "  8");
                }
                else
                {
                    src = JAI.create ( "fileload", file, null );
                }*/
                
            	
				final PlanarImage[] result = new PlanarImage[] { null };

				Thread loadImageThread = new Thread() {
					public void run() {
						try {
							PlanarImage src = JAI.create("fileload", file, null);
							
							BufferedImage bi = src.getAsBufferedImage();

							result[0] = TiledImage.wrapRenderedImage(bi);

						} catch (java.lang.OutOfMemoryError e) {
							return;
						}
					}
				};
				System.gc();
        	    loadImageThread.start();
            	
        	    final long WAIT_TIME_SECONDS=60;
        	    
                TimeUnit.SECONDS.timedJoin(
                		loadImageThread,
                		WAIT_TIME_SECONDS);   
                
                if (result[0] == null) {
                	String msg="Image file " + file + " is taking more than "+WAIT_TIME_SECONDS+" seconds to load. Stopping processing.\nMemory stats (Mb):\nFile size:"+(f.length()/(1024*1024))+"\n"+ELog.getMemoryStats();
                	ELog.info(msg);
                	//myThread.interrupt();
                	loadImageThread.stop();
        			JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
        			return;
                } else {
                	  panel  = new ImageDisplayPanel( result[0], imageNo, username );
                	  
                	
                	
                	  
                	
                      label     = panel.getOdometer (  );
                }
              
            }
            catch(Exception ex){
            	ELog.info("Image " + file + " cant be displayed "+ex);
    			JOptionPane.showMessageDialog(this, "Image " + file + " can't be displayed", "Error", JOptionPane.ERROR_MESSAGE);
            }
		}
		else
		{
			ELog.info ( "File " + file + " not found." );

			return;
		}

		try
		{
			jbInit (  );
		}
		catch ( Exception e )
		{
			e.printStackTrace (  );
		}
	}
	
	class RunJAIThread implements Runnable{
		
		PlanarImage src;
		String filepath;
		
		
		RunJAIThread(String filepath) {			
			this.filepath=filepath;
		}
		
		public void run() {
			src = JAI.create ( "fileload", filepath, null );
            
            BufferedImage bi = src.getAsBufferedImage();
            
            src = PlanarImage.wrapRenderedImage(bi);
		}    
		
		public PlanarImage getSrc() {
			return src;
		}
		
	}
    static int loc = 0;
    
	private void jbInit (  )
		throws Exception
	{
		
		this.setResizable ( true );
		this.setClosable ( true );
		this.setMaximizable ( true );
		this.iconable = true;
		this.setSize ( fwidth, fheight );
		this.setLocation ( loc, 0 );
		this.getContentPane (  ).setLayout ( gridBagLayout1 );
		loc = loc + fwidth;
		if (loc == fwidth*3)
		{loc = 0;
		}

		if ( label == null )
		{
			label = new JLabel( "Image Display" );
		}

		this.getContentPane (  ).add ( 
		    panel,
		    new GridBagConstraints( 
		        0,
		        0,
		        1,
		        1,
		        1.0,
		        1.0,
		        GridBagConstraints.CENTER,
		        GridBagConstraints.BOTH,
		        new Insets( 0, 0, 0, 0 ),
		        0,
		        0
		     )
		 );
		this.getContentPane (  ).add ( 
		    label,
		    new GridBagConstraints( 
		        0,
		        1,
		        1,
		        1,
		        0.0,
		        0.0,
		        GridBagConstraints.WEST,
		        GridBagConstraints.NONE,
		        new Insets( 0, 0, 0, 0 ),
		        0,
		        0
		     )
		 );

		//this.addKeyListener(this);
		//this.addFocusListener(this);
		//Util.info("focusable = " + isFocusable());
		//Util.info("added listener");
	}

	/**
	 * Returns the ImageDisplayPanel object associated with this object.
	 *
	 * @return The ImageDisplayPanel object associated with this object.
	 */
	public ImageDisplayPanel getImageDisplayPanel (  )
	{
		return panel;
	}

	/**
	 * Handles the event when a key is typed. Currently this function does nothing
	 *
	 * @param e a  KeyEvent object
	 */
	public void keyTyped ( KeyEvent e ) {}

	/**
	 * Handles the event when a key is pressed. Currently this function does nothing
	 *
	 * @param e a KeyEvent object
	 */
	public void keyPressed ( KeyEvent e ) {}

	/**
	 * Handles the event when a key is released. Currently this function does nothing
	 *
	 *
	 * @param e a KeyEvent object
	 */
	public void keyReleased ( KeyEvent e )
	{
		//Util.info ( "A key was pressed" );
		//int keyCode = e.getKeyCode (  );
		//if ( keyCode == KeyEvent.VK_DELETE )
		//{
		//Util.info ( "Delete key was pressed" );
		//}
	}

	/**
	 * Handles the event when focus is gained. Currently this function does nothing.
	 *
	 * @param e a FocusEvent object
	 */
	public void focusGained ( FocusEvent e )
	{
		//Util.info ( "focus gained" );
	}

	/**
	 * Handles the event when focus is lost. Currently this function does nothing.
	 *
	 * @param e a FocusEvent object
	 */
	public void focusLost ( FocusEvent e )
	{
		//Util.info ( "focus lost" );
	}

	/**
	 * Returns the image number in this frame
	 *
	 * @return the image number in this frame
	 */
	public String getImageNumber (  )
	{
		return imageNumber;
	}

	/**
	 * Sets the image number in this frame.
	 *
	 * @param newImageNumber The new image number
	 */
	public void setImageNumber ( String newImageNumber )
	{
		imageNumber = newImageNumber;
	}
	public void getPrintNumber ()
	{

	}

	/*
	 public void dispose() {
		 super.dispose();
		 Util.info("Freeing up memory for frame "+label);
		 if (panel!=null) panel.freeMemory();
	 }
	 */


}
