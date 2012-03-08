import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import java.io.File;

import java.util.Hashtable;

import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.io.*;
import java.awt.image.*;


/**
 * The main frame in the Image Stitch module. This frame has a menubar which provides
 * likns to all the functionality for the Image Stitch module.
 *
 * @author zndavid
 * @version 1.0
 */
public class ImageStitchFrame
	extends JFrame
{
	private static boolean boolZoomRotate   = true;
	private boolean        isSynchromizing  = false;
	private JMenuBar       jMenuBar1        = new JMenuBar(  );
	private JMenu          fileMenu         = new JMenu(  );
	private JMenuItem      fileOpenMenu     = new JMenuItem(  );
	private JMenuItem      fileAddMenu      = new JMenuItem(  );
	private JMenuItem      fileSaveMenu     = new JMenuItem(  );
	private JMenuItem      fileExitMenu     = new JMenuItem(  );
	private JMenu          helpMenu         = new JMenu(  );
	private JMenuItem      helpAboutMenu    = new JMenuItem(  );
	private JMenuItem      helpManualMenu   = new JMenuItem(  );
	private ButtonGroup    lockMenuGroup    = new ButtonGroup(  );
	private boolean        isLocked         = false;
	private JSplitPane     jSplitPane1      = new JSplitPane(  );
	private JPanel         jPanel1          = new JPanel(  );
	private GridBagLayout  gridBagLayout1   = new GridBagLayout(  );
	private JSlider        brightnessSlider = new JSlider(  );
	private JSlider        zoomSlider       = new JSlider(  );
	private JSlider        contrastSlider   = new JSlider(  );
	private JSlider        rotationSlider   = new JSlider(  );
	private JLabel         jLabel1          = new JLabel(  );
	private JLabel         jLabel2          = new JLabel(  );
	private JLabel         jLabel3          = new JLabel(  );
	private JLabel         jLabel4          = new JLabel(  );

	//private PlanarImage       src          = null;
	private ImageStitchPanel dsp          = null;
	private JScrollPane      jScrollPane1;

	//private JMenu             currentFileMenu        = new JMenu(  );
	//private ButtonGroup       currentFileButtonGroup;
	private JMenuItem fileRemoveSelectedMenu = new JMenuItem(  );
	private JMenuItem fileRemoveAllMenu  = new JMenuItem(  );
	private JMenuItem fileBringToTopMenu = new JMenuItem(  );

	//private Vector            listOfCurrentFiles = new Vector(  );
	private boolean           isSaved            = false;
	private JCheckBoxMenuItem fileLockImagesMenu = new JCheckBoxMenuItem(  );
	private float             zoomRatio          = 0f;

	/**
	 * Creates a new ImageStitchFrame with no file opened
	 */
	public ImageStitchFrame (  )
	{
		dsp = new ImageStitchPanel(  );

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
	 * Creates a new ImageStitchFrame object and opens the specified file
	 *
	 * @param file The name of the image file that has to be opened
	 */
	public ImageStitchFrame ( String file )
	{
		File f = new File( file );

		if ( f.exists (  ) && f.canRead (  ) )
		{
            PlanarImage src = JAI.create ( "fileload", file, null );
            BufferedImage bi = src.getAsBufferedImage();
            src = PlanarImage.wrapRenderedImage(bi);
            //PipedOutputStream pos = new PipedOutputStream();
            //JAI.create("encode", src, pos, "JPEG", null);
            //PipedInputStream pis = new PipedInputStream(pos);
            //src = JAI.create("fileload", pis, null);
			dsp = new ImageStitchPanel( src, f.getName (  ) );
            
		}
		else
		{
			ELog.info ( "File " + file + " not found." );

			//System.exit( 0 );
			return;
		}

		try
		{
			jbInit (  );

			//resetCurrentFileMenu ( file );
		}
		catch ( Exception e )
		{
			e.printStackTrace (  );
		}
	}

	private void jbInit (  )
		throws Exception
	{
		jScrollPane1 = new JScrollPane( dsp );
		this.setJMenuBar ( jMenuBar1 );
		this.setTitle ( "Image Stitching Module" );
		this.setSize ( new Dimension( 425, 304 ) );
		fileMenu.setText ( "File" );
		fileOpenMenu.setText ( "Open" );
		fileOpenMenu.addActionListener ( 
		    new ActionListener(  )
			{
				public void actionPerformed ( ActionEvent e )
				{
					fileOpenMenu_actionPerformed ( e );
				}
			}
		 );
		fileAddMenu.setText ( "Add" );
		fileAddMenu.addActionListener ( 
		    new ActionListener(  )
			{
				public void actionPerformed ( ActionEvent e )
				{
					fileAddMenu_actionPerformed ( e );
				}
			}
		 );
		fileSaveMenu.setText ( "Save" );
		fileSaveMenu.addActionListener ( 
		    new ActionListener(  )
			{
				public void actionPerformed ( ActionEvent e )
				{
					fileSaveMenu_actionPerformed ( e );
				}
			}
		 );
		fileExitMenu.setText ( "Exit" );
		fileExitMenu.addActionListener ( 
		    new ActionListener(  )
			{
				public void actionPerformed ( ActionEvent e )
				{
					fileExitMenu_actionPerformed ( e );
				}
			}
		 );
		helpMenu.setText ( "Help" );
		helpAboutMenu.setText ( "About" );
		helpAboutMenu.addActionListener ( 
		    new ActionListener(  )
			{
				public void actionPerformed ( ActionEvent e )
				{
                    helpAboutMenu_actionPerformed(e);
				}
			}
		 );
		helpManualMenu.setText ( "User Manual" );
		helpManualMenu.addActionListener ( 
		    new ActionListener(  )
			{
				public void actionPerformed ( ActionEvent e )
				{
					helpManualMenu_actionPerformed ( e );
				}
			}
		 );
		jSplitPane1.setOrientation ( JSplitPane.VERTICAL_SPLIT );
		jSplitPane1.setResizeWeight ( 1.0 );
		jSplitPane1.setOneTouchExpandable ( true );
		jPanel1.setLayout ( gridBagLayout1 );
		brightnessSlider.setMaximum ( 256 );
		brightnessSlider.setMinimum ( -256 );
		brightnessSlider.setPaintTicks ( true );
		brightnessSlider.setMinorTickSpacing ( 16 );
		brightnessSlider.setMajorTickSpacing ( 64 );
		brightnessSlider.setValue ( 0 );

		Hashtable labels1 = new Hashtable(  );
		labels1.put ( new Integer( -255 ), new JLabel( "-255" ) );
		labels1.put ( new Integer( -128 ), new JLabel( "-128" ) );
		labels1.put ( new Integer( 0 ), new JLabel( "0" ) );
		labels1.put ( new Integer( 128 ), new JLabel( "128" ) );
		labels1.put ( new Integer( 255 ), new JLabel( "255" ) );
		brightnessSlider.setLabelTable ( labels1 );
		brightnessSlider.setPaintLabels ( true );

		zoomSlider.setMinorTickSpacing ( 5 );
		zoomSlider.setMinimum ( 10 );
		zoomSlider.setValue ( 60 );
		zoomSlider.setMajorTickSpacing ( 10 );
		zoomSlider.setPaintTicks ( true );

		Hashtable labels2 = new Hashtable(  );
		labels2.put ( new Integer( 10 ), new JLabel( "1:10" ) );
		labels2.put ( new Integer( 60 ), new JLabel( "1:5" ) );
		labels2.put ( new Integer( 100 ), new JLabel( "1:1" ) );
		zoomSlider.setLabelTable ( labels2 );
		zoomSlider.setPaintLabels ( true );

		contrastSlider.setMajorTickSpacing ( 50 );
		contrastSlider.setMinimum ( -100 );
		contrastSlider.setMinorTickSpacing ( 10 );
		contrastSlider.setPaintTicks ( true );
		contrastSlider.setValue ( 0 );

		Hashtable labels3 = new Hashtable(  );
		labels3.put ( new Integer( -100 ), new JLabel( "-100" ) );
		labels3.put ( new Integer( -50 ), new JLabel( "-50" ) );
		labels3.put ( new Integer( 0 ), new JLabel( "0" ) );
		labels3.put ( new Integer( 50 ), new JLabel( "50" ) );
		labels3.put ( new Integer( 100 ), new JLabel( "100" ) );
		contrastSlider.setLabelTable ( labels3 );
		contrastSlider.setPaintLabels ( true );

		rotationSlider.setMaximum ( 180 );
		rotationSlider.setMinimum ( -180 );
		rotationSlider.setValue ( 0 );
		rotationSlider.setMajorTickSpacing ( 30 );
		rotationSlider.setMinorTickSpacing ( 10 );
		rotationSlider.setPaintTicks ( true );

		Hashtable labels4 = new Hashtable(  );
		dsp.addMouseListener ( 
		    new java.awt.event.MouseAdapter(  )
			{
				public void mouseReleased ( MouseEvent e )
				{
					this_mouseReleased ( e );
				}

				public void mousePressed ( MouseEvent e )
				{
					this_mousePressed ( e );
				}
			}
		 );
		zoomSlider.addChangeListener ( 
		    new ChangeListener(  )
			{
				public void stateChanged ( ChangeEvent e )
				{
					zoomRotateSlider_stateChanged ( e );
				}
			}
		 );
		brightnessSlider.addChangeListener ( 
		    new ChangeListener(  )
			{
				public void stateChanged ( ChangeEvent e )
				{
					brightnessContrastSlider_stateChanged ( e );
				}
			}
		 );
		contrastSlider.addChangeListener ( 
		    new ChangeListener(  )
			{
				public void stateChanged ( ChangeEvent e )
				{
					brightnessContrastSlider_stateChanged ( e );
				}
			}
		 );
		labels4.put ( new Integer( -180 ), new JLabel( "-180" ) );
		labels4.put ( new Integer( -90 ), new JLabel( "-90" ) );
		labels4.put ( new Integer( 0 ), new JLabel( "0" ) );
		labels4.put ( new Integer( 90 ), new JLabel( "90" ) );
		labels4.put ( new Integer( 180 ), new JLabel( "180" ) );
		rotationSlider.setLabelTable ( labels4 );
		rotationSlider.setPaintLabels ( true );
		rotationSlider.addChangeListener ( 
		    new ChangeListener(  )
			{
				public void stateChanged ( ChangeEvent e )
				{
					zoomRotateSlider_stateChanged ( e );
				}
			}
		 );

		jLabel1.setText ( "Brightness" );
		jLabel2.setText ( "Contrast" );
		jLabel3.setText ( "Zoom" );
		jLabel4.setText ( "Rotation" );

		//currentFileMenu.setText ( "Current File" );
		fileRemoveSelectedMenu.setText ( "Remove Selected" );
		fileRemoveSelectedMenu.addActionListener ( 
		    new ActionListener(  )
			{
				public void actionPerformed ( ActionEvent e )
				{
					fileRemoveSelectedMenu_actionPerformed ( e );
				}
			}
		 );
		fileRemoveAllMenu.setText ( "Remove All" );
		fileRemoveAllMenu.addActionListener ( 
		    new ActionListener(  )
			{
				public void actionPerformed ( ActionEvent e )
				{
					fileRemoveAllMenu_actionPerformed ( e );
				}
			}
		 );
		fileBringToTopMenu.setText ( "Bring Image To Top" );
		fileBringToTopMenu.addActionListener ( 
		    new ActionListener(  )
			{
				public void actionPerformed ( ActionEvent e )
				{
					fileBringToTopMenu_actionPerformed ( e );
				}
			}
		 );
		fileLockImagesMenu.setText ( "Lock Images" );
		fileLockImagesMenu.addActionListener ( 
		    new ActionListener(  )
			{
				public void actionPerformed ( ActionEvent e )
				{
					fileLockImagesMenu_actionPerformed ( e );
				}
			}
		 );
		fileMenu.add ( fileOpenMenu );
		fileMenu.add ( fileAddMenu );
		fileMenu.add ( fileSaveMenu );
		fileMenu.addSeparator (  );
		fileMenu.add ( fileBringToTopMenu );
		fileMenu.add ( fileLockImagesMenu );
		fileMenu.addSeparator (  );
		fileMenu.add ( fileRemoveSelectedMenu );
		fileMenu.add ( fileRemoveAllMenu );
		fileMenu.addSeparator (  );
		fileMenu.add ( fileExitMenu );
		jMenuBar1.add ( fileMenu );

		//jMenuBar1.add ( currentFileMenu );
		helpMenu.add ( helpAboutMenu );
		helpMenu.add ( helpManualMenu );
		jMenuBar1.add ( helpMenu );
		jPanel1.add ( 
		    brightnessSlider,
		    new GridBagConstraints( 
		        0,
		        1,
		        1,
		        1,
		        1.0,
		        0.0,
		        GridBagConstraints.CENTER,
		        GridBagConstraints.HORIZONTAL,
		        new Insets( 0, 0, 0, 0 ),
		        0,
		        0
		     )
		 );
		jPanel1.add ( 
		    zoomSlider,
		    new GridBagConstraints( 
		        0,
		        3,
		        1,
		        1,
		        1.0,
		        0.0,
		        GridBagConstraints.CENTER,
		        GridBagConstraints.HORIZONTAL,
		        new Insets( 0, 0, 0, 0 ),
		        0,
		        0
		     )
		 );
		jPanel1.add ( 
		    contrastSlider,
		    new GridBagConstraints( 
		        1,
		        1,
		        1,
		        1,
		        1.0,
		        0.0,
		        GridBagConstraints.CENTER,
		        GridBagConstraints.HORIZONTAL,
		        new Insets( 0, 0, 0, 0 ),
		        0,
		        0
		     )
		 );
		jPanel1.add ( 
		    rotationSlider,
		    new GridBagConstraints( 
		        1,
		        3,
		        1,
		        1,
		        1.0,
		        0.0,
		        GridBagConstraints.CENTER,
		        GridBagConstraints.HORIZONTAL,
		        new Insets( 0, 0, 0, 0 ),
		        0,
		        0
		     )
		 );
		jPanel1.add ( 
		    jLabel1,
		    new GridBagConstraints( 
		        0,
		        0,
		        1,
		        1,
		        0.0,
		        0.0,
		        GridBagConstraints.CENTER,
		        GridBagConstraints.NONE,
		        new Insets( 0, 0, 0, 0 ),
		        0,
		        0
		     )
		 );
		jPanel1.add ( 
		    jLabel2,
		    new GridBagConstraints( 
		        1,
		        0,
		        1,
		        1,
		        0.0,
		        0.0,
		        GridBagConstraints.CENTER,
		        GridBagConstraints.NONE,
		        new Insets( 0, 0, 0, 0 ),
		        0,
		        0
		     )
		 );
		jPanel1.add ( 
		    jLabel3,
		    new GridBagConstraints( 
		        0,
		        2,
		        1,
		        1,
		        0.0,
		        0.0,
		        GridBagConstraints.CENTER,
		        GridBagConstraints.NONE,
		        new Insets( 0, 0, 0, 0 ),
		        0,
		        0
		     )
		 );
		jPanel1.add ( 
		    jLabel4,
		    new GridBagConstraints( 
		        1,
		        2,
		        1,
		        1,
		        0.0,
		        0.0,
		        GridBagConstraints.CENTER,
		        GridBagConstraints.NONE,
		        new Insets( 0, 0, 0, 0 ),
		        0,
		        0
		     )
		 );
		jSplitPane1.add ( jPanel1, JSplitPane.BOTTOM );
		jSplitPane1.add ( jScrollPane1, JSplitPane.TOP );
		this.getContentPane (  ).add ( jSplitPane1, BorderLayout.CENTER );

		//this.setDefaultCloseOperation ( JFrame.EXIT_ON_CLOSE );
		this.setDefaultCloseOperation ( JFrame.DISPOSE_ON_CLOSE );

		//this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.setVisible ( true );
	}

	private void fileOpenMenu_actionPerformed ( ActionEvent e )
	{
		if ( ( dsp.listOfSources.size (  ) > 0 ) && ( isSaved == false ) )
		{
			if ( 
			    JOptionPane.showConfirmDialog ( 
				        null,
				        "There are already some files open in this window. Do you want to save them?",
				        "Save Files",
				        JOptionPane.YES_NO_OPTION
				     ) == JOptionPane.YES_OPTION
			 )
			{
				fileSaveMenu_actionPerformed ( e );
			}

			return;
		}

		String file = Utilities.getFileNameFromUserForRead ( "" );

		if ( ( file == null ) || ( file.compareTo ( "" ) == 0 ) )
		{
			return;
		}

		File f = new File( file );

		if ( f.exists (  ) && f.canRead (  ) )
		{
			PlanarImage src = JAI.create ( "fileload", file, null );
            BufferedImage bim = src.getAsBufferedImage();
            src = PlanarImage.wrapRenderedImage(bim);
			fileLockImagesMenu.setSelected ( false );
			dsp.removeAllImages (  );
			dsp.addImage ( src, f.getName (  ) );
			dsp.setSelectedSourceIndex ( 0 );
			this.setTitle ( dsp.getPreferredTitle (  ) );
			synchronizeSliders (  );
			isSaved = false;
		}
		else
		{
			ELog.info ( "File " + file + " not found." );

			return;
		}

		//resetCurrentFileMenu ( file );
	}

	private void fileAddMenu_actionPerformed ( ActionEvent e )
	{
		if ( dsp.listOfSources.size (  ) <= 0 )
		{
			fileOpenMenu_actionPerformed ( e );

			return;
		}

		String file = Utilities.getFileNameFromUserForRead ( "" );

		if ( ( file == null ) || ( file.compareTo ( "" ) == 0 ) )
		{
			return;
		}

		File f = new File( file );

		if ( f.exists (  ) && f.canRead (  ) )
		{
			PlanarImage src = JAI.create ( "fileload", file, null );
            BufferedImage bim = src.getAsBufferedImage();
            src = PlanarImage.wrapRenderedImage(bim);dsp.addImage ( src, f.getName (  ) );
			dsp.setSelectedSourceIndex ( dsp.listOfSources.size (  ) - 1 );
			this.setTitle ( dsp.getPreferredTitle (  ) );

			//addToCurrentFileMenu ( file );
			//isSaved = false;
		}
		else
		{
			ELog.info ( "File " + file + " not found." );

			return;
		}
	}

	private void fileSaveMenu_actionPerformed ( ActionEvent e )
	{
		String filename = Utilities.getFileNameFromUser ( "" );

		if ( filename.compareTo ( "" ) == 0 )
		{
			return;
		}

		dsp.saveStitchedImageTo ( filename );
		isSaved = true;
	}

	private void fileExitMenu_actionPerformed ( ActionEvent e )
	{
		this.hide (  );
		this.dispose (  );

		//comment out the system.exit(0) line when integrating into main applicaion
		//System.exit ( 0 );
	}


	private void helpManualMenu_actionPerformed ( ActionEvent e ) {}

	//private void changeFile_actionPerformed ( ActionEvent e )
	//{
	//ButtonModel bm = currentFileButtonGroup.getSelection();
	//bm.setSelected(false);
	//AbstractButton ab = (AbstractButton) e.getSource();
	//ab.setSelected(true);
	//int number = currentFileMenu.getItemCount (  );
	//		for ( int i = 0; i < number; i++ )
	//	{
	//	JMenuItem item = currentFileMenu.getItem ( i );
	//if ( item.isSelected (  ) )
	//{
	//dsp.setSelectedSourceIndex ( i );
	//Util.info("selected = " + item + " and setting selected indx to " + dsp.getSelectedSourceIndex());
	//	break;
	//}
	//}
	//synchronizeSliders (  );
	//}
	private void brightnessContrastSlider_stateChanged ( ChangeEvent e )
	{
		if ( isSynchromizing == false )
		{
			if ( 
			    ( brightnessSlider.getValueIsAdjusting (  ) == false )
				    && ( contrastSlider.getValueIsAdjusting (  ) == false )
			 )
			{
				int             brightness = brightnessSlider.getValue (  );
				int             contrast = contrastSlider.getValue (  );

				ModifiableImage mig =
					( ModifiableImage ) dsp.listOfSources.elementAt ( 
					    dsp.getSelectedSourceIndex (  )
					 );
				mig.setBrightnessAndContrast ( 
				    brightness,
				    1.0f + ( ( float ) ( contrast ) / 200.0f )
				 );
				dsp.repaint (  );
				isSaved = false;
			}
		}
	}

	private void zoomRotateSlider_stateChanged ( ChangeEvent e )
	{
		if ( isSynchromizing == false )
		{
			if ( 
			    ( zoomSlider.getValueIsAdjusting (  ) == false )
				    && ( rotationSlider.getValueIsAdjusting (  ) == false )
			 )
			{
				int int_zoom   = zoomSlider.getValue (  );
				int int_rotate = rotationSlider.getValue (  );

				if ( fileLockImagesMenu.isSelected (  ) )
				{
					int num = dsp.listOfSources.size (  );

					for ( int i = 0; i < num; i++ )
					{
						ModifiableImage mig =
							( ModifiableImage ) dsp.listOfSources.elementAt ( i );
						mig.zoomAndRotate ( 
						    10.0f / ( float ) ( 110 - int_zoom ) / zoomRatio * mig
							    .getTemp_zoom (  ),
						    ( float ) ( 
							    int_rotate
							    + ( ( mig.getTemp_rotation (  ) * 180 ) / Math.PI )
						     )
						 );
					}
				}
				else
				{
					ModifiableImage mig =
						( ModifiableImage ) dsp.listOfSources.elementAt ( 
						    dsp.getSelectedSourceIndex (  )
						 );
					mig.zoomAndRotate ( 
					    10.0f / ( float ) ( 110 - int_zoom ),
					    ( float ) int_rotate
					 );
				}

				dsp.repaint (  );
				isSaved = false;
			}
		}
	}

	/*private void resetCurrentFileMenu ( String file )
	   {
	           int    num = file.lastIndexOf ( ( int ) '\\' );
	           String sub = null;
	           if ( num != -1 )
	           {
	                   sub     = file.substring ( num + 1 );
	                   num     = sub.lastIndexOf ( ( int ) '.' );
	                   if ( num != -1 )
	                   {
	                           sub = sub.substring ( 0, num );
	                   }
	                   //Util.info("sub =" + sub);
	           }
	           else
	           {
	                   num = file.lastIndexOf ( ( int ) '/' );
	                   if ( num != -1 )
	                   {
	                           sub     = file.substring ( num + 1 );
	                           num     = sub.lastIndexOf ( ( int ) '.' );
	                           if ( num != -1 )
	                           {
	                                   sub = sub.substring ( 0, num );
	                           }
	                           //Util.info("sub =" + sub);
	                   }
	           }
	           if ( sub != null )
	           {
	                   JRadioButtonMenuItem fileItem =
	                           new JRadioButtonMenuItem( sub );
	                   fileItem.setSelected ( true );
	                   fileItem.addActionListener (
	                       new ActionListener(  )
	                           {
	                                   public void actionPerformed ( ActionEvent e )
	                                   {
	                                           changeFile_actionPerformed ( e );
	                                   }
	                           }
	                    );
	                   currentFileMenu.removeAll (  );
	                   listOfCurrentFiles.removeAllElements (  );
	                   currentFileMenu.add ( fileItem );
	                   listOfCurrentFiles.add ( sub );
	                   currentFileButtonGroup = new ButtonGroup(  );
	                   currentFileButtonGroup.add ( fileItem );
	                   dsp.setSelectedSourceIndex (
	                       currentFileButtonGroup.getButtonCount (  ) - 1
	                    );
	                   synchronizeSliders (  );
	                   jMenuBar1.repaint (  );
	           }
	   }
	   private void addToCurrentFileMenu ( String file )
	   {
	           int    num = file.lastIndexOf ( ( int ) '\\' );
	           String sub = null;
	           if ( num != -1 )
	           {
	                   sub     = file.substring ( num + 1 );
	                   num     = sub.lastIndexOf ( ( int ) '.' );
	                   if ( num != -1 )
	                   {
	                           sub = sub.substring ( 0, num );
	                   }
	                   //Util.info("sub =" + sub);
	           }
	           else
	           {
	                   num = file.lastIndexOf ( ( int ) '/' );
	                   if ( num != -1 )
	                   {
	                           sub     = file.substring ( num + 1 );
	                           num     = sub.lastIndexOf ( ( int ) '.' );
	                           if ( num != -1 )
	                           {
	                                   sub = sub.substring ( 0, num );
	                           }
	                           //Util.info("sub =" + sub);
	                   }
	           }
	           if ( sub != null )
	           {
	                   listOfCurrentFiles.addElement ( sub );
	                   JRadioButtonMenuItem fileItem =
	                           new JRadioButtonMenuItem( sub );
	                   fileItem.addActionListener (
	                       new ActionListener(  )
	                           {
	                                   public void actionPerformed ( ActionEvent e )
	                                   {
	                                           changeFile_actionPerformed ( e );
	                                   }
	                           }
	                    );
	                   currentFileMenu.add ( fileItem );
	                   currentFileButtonGroup.add ( fileItem );
	                   fileItem.setSelected ( true );
	                   currentFileButtonGroup.setSelected (
	                       fileItem.getModel (  ),
	                       true
	                    );
	                   dsp.setSelectedSourceIndex (
	                       currentFileButtonGroup.getButtonCount (  ) - 1
	                    );
	                   synchronizeSliders (  );
	                   jMenuBar1.repaint (  );
	           }
	   }*/
	/**
	 * Sets the slider values to the values in the surrently selected image.
	 */
	public void synchronizeSliders (  )
	{
		isSynchromizing = true;

		ModifiableImage mig =
			( ModifiableImage ) dsp.listOfSources.elementAt ( 
			    dsp.getSelectedSourceIndex (  )
			 );
		brightnessSlider.setValue ( mig.getBrightness (  ) );
		contrastSlider.setValue ( mig.getContrast (  ) );

		float zoom    = mig.getZoom (  );
		int   intZoom = 110 - ( int ) ( 10.0 / zoom );
		zoomSlider.setValue ( intZoom );

		double rot    = mig.getRotation (  );
		int    intRot = ( int ) ( ( rot * 180.0 ) / Math.PI );
		rotationSlider.setValue ( intRot );
		isSynchromizing = false;
	}

	private void fileRemoveSelectedMenu_actionPerformed ( ActionEvent e )
	{
		//JMenuItem item1 =
		//currentFileMenu.getItem ( 
		//  dsp.getSelectedSourceIndex (  )
		// );
		//String    str = item1.getText (  );
		//currentFileButtonGroup.remove ( item1 );
		//listOfCurrentFiles.removeElementAt ( 
		//  dsp.getSelectedSourceIndex (  )
		//);
		//currentFileMenu.remove ( dsp.getSelectedSourceIndex (  ) );
		dsp.removeImage ( dsp.getSelectedSourceIndex (  ) );
		isSaved = false;

		//JMenuItem item = currentFileMenu.getItem ( 0 );
		//item.setSelected ( true );
		if ( dsp.listOfSources.size (  ) >= 1 )
		{
			dsp.setSelectedSourceIndex ( 0 );
		}

		this.setTitle ( dsp.getPreferredTitle (  ) );
		repaint (  );
	}

	private void fileRemoveAllMenu_actionPerformed ( ActionEvent e )
	{
		//currentFileMenu.removeAll (  );
		//listOfCurrentFiles.removeAllElements (  );
		dsp.removeAllImages (  );
		this.setTitle ( dsp.getPreferredTitle (  ) );
		isSaved = true;
		repaint (  );
	}

	private void fileBringToTopMenu_actionPerformed ( ActionEvent e )
	{
		//int num = dsp.listOfSources.size (  ) - 1;
		int loc = dsp.getSelectedSourceIndex (  );

		//dsp.swapImages ( loc, num );
		dsp.putAtLast ( loc );
		dsp.setSelectedSourceIndex ( 0 );
		repaint (  );

		//Object obj = listOfCurrentFiles.elementAt ( loc );
		//listOfCurrentFiles.removeElementAt ( loc );
		//listOfCurrentFiles.add ( 
		//  loc,
		//listOfCurrentFiles.elementAt ( num - 1 )
		// );
		//listOfCurrentFiles.removeElementAt ( num );
		//listOfCurrentFiles.add ( num, obj );
		//currentFileMenu.removeAll (  );
		//currentFileButtonGroup = new ButtonGroup(  );
		//for ( int i = 0; i < listOfCurrentFiles.size (  ); i++ )
		//{
		//JRadioButtonMenuItem item =
		//new JRadioButtonMenuItem( 
		//  ( String ) listOfCurrentFiles.elementAt ( i )
		//);
		//item.addActionListener ( 
		//  new ActionListener(  )
		//{
		//public void actionPerformed ( ActionEvent e )
		//{
		//changeFile_actionPerformed ( e );
		//}
		//}
		// );
		//currentFileMenu.add ( item );
		//currentFileButtonGroup.add ( item );
		//if ( i == ( listOfCurrentFiles.size (  ) - 1 ) )
		//{
		//ButtonModel bm = item.getModel (  );
		//currentFileButtonGroup.setSelected ( bm, true );
		//	dsp.setSelectedSourceIndex ( i );
		//}
		//}
		//repaint (  );
	}

	private void fileLockImagesMenu_actionPerformed ( ActionEvent e )
	{
		if ( fileLockImagesMenu.isSelected (  ) )
		{
			ApplicationProperties.stitchFrameLocked = true;

			int num = dsp.listOfSources.size (  );

			for ( int i = 0; i < num; i++ )
			{
				ModifiableImage mig =
					( ModifiableImage ) dsp.listOfSources.elementAt ( i );
				mig.setTemp_zoom ( mig.getZoom (  ) );
				mig.setTemp_rotation ( mig.getRotation (  ) );
			}

			//Util.info("set temp values for all");
			synchronizeSlidersForLock (  );

			//Util.info("synchronised sliders");
		}
		else
		{
			ApplicationProperties.stitchFrameLocked = false;
			unsynchronizeSlidersAfterLock (  );
		}
	}

	private void synchronizeSlidersForLock (  )
	{
		brightnessSlider.setEnabled ( false );
		contrastSlider.setEnabled ( false );
		rotationSlider.setValue ( 0 );
		zoomRatio = 0;

		for ( int i = 0; i < dsp.listOfSources.size (  ); i++ )
		{
			ModifiableImage mig  = ( ModifiableImage ) dsp.listOfSources.elementAt ( i );
			float           zoom = mig.getZoom (  );

			if ( zoom > zoomRatio )
			{
				zoomRatio = zoom;
			}
		}

		int intZoom = 110 - ( int ) ( 10.0 / zoomRatio );
		zoomSlider.setValue ( intZoom );
	}

	private void unsynchronizeSlidersAfterLock (  )
	{
		brightnessSlider.setEnabled ( true );
		contrastSlider.setEnabled ( true );
		synchronizeSliders (  );
	}

	private void this_mouseReleased ( MouseEvent e )
	{
		//Util.info("mouse released");
		if ( dsp.isSelectedSourceChanged (  ) )
		{
			synchronizeSliders (  );
			this.setTitle ( dsp.getPreferredTitle (  ) );
			dsp.setSelectedSourceChanged ( false );
		}
	}

	private void this_mousePressed ( MouseEvent e ) {}

    private void helpAboutMenu_actionPerformed(ActionEvent e)
    {
        JOptionPane.showMessageDialog ( 
		    this,
		    new MainFrame_AboutBoxPanel1(  ),
		    "About",
		    JOptionPane.PLAIN_MESSAGE
		 );
    }
}
