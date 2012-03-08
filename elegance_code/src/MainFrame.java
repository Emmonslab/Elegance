import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.DefaultDesktopManager;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * The main window of this software. It provides access to all the other modules
 * inthe software via the menubar. Each item in the menubar has its handler in
 * this class. In addition to the mwnubar this frame contains a split pane which
 * has a desktop pane at the upper part to hold the ImageDisplayFrame and the
 * sliders for changing image properties at the bottom. The class implements the
 * MouseListener, MouseMotionListener and KeyListener interfaces to provide
 * additional functionality.
 * 
 * @author zndavid
 * @version 1.0
 */
public class MainFrame extends JFrame implements MouseListener, MouseMotionListener, KeyListener {
	private boolean showObjects = true;
	private String username;
	private ImageSearchFrame isFrame = null;
	private ImageEntryFrame ieFrame = null;
	private ImageModifyFrame imFrame = null;
	private ContinFrame cFrame = null;
	private FilterFrame filterFrame = null;
	
	private RelationFrame rFrame = null;

	static AllObjectsFrame allObjectsFrame = null;
	private ObjectFrame selectedObjectFrame = null;

	private boolean isSynchronizing = false;
	private JMenuItem menuHelpAbout = new JMenuItem();
	private JMenu menuHelp = new JMenu();
	private JMenuItem menuFileExit = new JMenuItem();
	private JMenuItem switchDB = new JMenuItem();
	
	private JMenu menuFile = new JMenu();
	private JMenuBar menuBar = new JMenuBar();
	private JMenu menuImage = new JMenu();
	private JMenu menuUtilities = new JMenu();
	private JMenuItem menuUserManual = new JMenuItem();
	private JMenuItem menuUtilitiesStitch = new JMenuItem();
	private JMenuItem menuUtilitiesBackup = new JMenuItem();
	private JMenuItem menuImageEnter = new JMenuItem();
	private JMenuItem menuImageFind = new JMenuItem();
	private JMenuItem menuImageAlignment = new JMenuItem();
	private JMenuItem menuImageLoadNext = new JMenuItem();
	private JMenuItem menuImageLoadPrev = new JMenuItem();
	private JMenuItem menuUtilitiesReport = new JMenuItem();
	private WormDesktop desktop = new WormDesktop();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private JSplitPane jSplitPane1 = new JSplitPane();
	private JPanel jPanel1 = new JPanel();
	private GridBagLayout gridBagLayout2 = new GridBagLayout();
	private JLabel jLabel1 = new JLabel();
	private JLabel jLabel2 = new JLabel();
	private JLabel jLabel3 = new JLabel();
	private JLabel jLabel4 = new JLabel();
	private JSlider brightnessSlider = new JSlider();
	private JSlider contrastSlider = new JSlider();
	private JSlider zoomSlider = new JSlider();
	private JSlider rotationSlider = new JSlider();
	private JMenu graphicalDisplayMenu = new JMenu();
	private JMenuItem calculateSynapsesMenuItem = new JMenuItem();
	private JMenuItem updateSynapsesMenuItem = new JMenuItem();
	private JMenuItem SynListMenuItem = new JMenuItem();
	private JMenuItem partnerListMenuItem = new JMenuItem();
	private JMenuItem adjacentMatrixChemMenuItem = new JMenuItem();
	private JMenuItem adjacentMatrixEleMenuItem = new JMenuItem();
	private JMenuItem twoDDisplayMenuItem = new JMenuItem();
	
	private JMenuItem debugMenuItem = new JMenuItem();
	
	private JMenuItem quickDisplayMenuItem = new JMenuItem();
	private JMenuItem ThreeDDisplayMenuItem = new JMenuItem();
	private JCheckBoxMenuItem menuImageLock = new JCheckBoxMenuItem();
	private float zoomRatio = 1;
	private JMenu menuSave = new JMenu();
	private JMenuItem menuCalculateFromObj = new JMenuItem();
	private JMenuItem menuCalculateByContin = new JMenuItem();
	private JMenuItem menuCalculateAllContins = new JMenuItem();
	
	
	private JMenu menuView = new JMenu();
	private JCheckBoxMenuItem menuViewObjects = new JCheckBoxMenuItem();
	// private JMenu jMenu1 = new JMenu( );
	private JCheckBoxMenuItem viewMultPostsynapticRelMenu = new JCheckBoxMenuItem();
	private JCheckBoxMenuItem viewMultPresynapticRelMenu = new JCheckBoxMenuItem();
	private JCheckBoxMenuItem viewGapRelMenu = new JCheckBoxMenuItem();
	private JCheckBoxMenuItem viewPostsynapticRelMenu = new JCheckBoxMenuItem();
	private JCheckBoxMenuItem viewPresynapticRelMenu = new JCheckBoxMenuItem();
	private JCheckBoxMenuItem viewContinuousRelMenu = new JCheckBoxMenuItem();
	private JCheckBoxMenuItem viewAllRelMenu = new JCheckBoxMenuItem();
	private JMenuItem viewRelationWindowMenu = new JMenuItem();
	private JMenuItem viewAllObjectsWindowMenu = new JMenuItem();
	private JMenuItem viewSelectedObjectWindowMenu = new JMenuItem();
	private JMenuItem synSearchMenu = new JMenuItem();
	private JMenuItem synFindMenu = new JMenuItem();
	private JMenuItem menuResetSlider = new JMenuItem();
	private JMenuItem viewRefreshMenu = new JMenuItem();
	private JMenuItem showHideObjectsMenu = new JMenuItem();

	
	private JMenuItem menuImageModify = new JMenuItem();
	private JMenuItem viewContinsMenu = new JMenuItem();
	private JMenuItem filtersMenu = new JMenuItem();
	private JMenu lookAndFeelMenu = new JMenu();
	private JMenuItem metalLookMenuItem = new JMenuItem();
	private JMenuItem motifLookMenuItem = new JMenuItem();
	private JMenuItem systemLookMenuItem = new JMenuItem();
	private String[] frameIMG = new String[30];
	private MyDesktopManager cDManager = new MyDesktopManager();
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	int fwidth = (int) (screenSize.width / 3.02);
	int fheight = (int) (screenSize.height * 0.8);

	/**
	 * Creates a new MainFrame object.
	 */
	public MainFrame() {
		username = Elegance.username;
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception {

		this.setJMenuBar(menuBar);
		this.getContentPane().setLayout(gridBagLayout1);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(new Dimension(screenSize.width, screenSize.height - 50));

		this.setTitle(getEleganceTitle());
		menuFile.setText("File");
		menuImage.setText("Image");
		menuUtilities.setText("Utilities");
		menuUserManual.setText("User Manual");
		menuUtilitiesStitch.setText("Stitch");
		menuUtilitiesStitch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuUtilitiesStitch_actionPerformed(e);
			}
		});
		menuUtilitiesBackup.setText("Backup");
		menuUtilitiesBackup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuUtilitiesBackup_actionPerformed(e);
			}
		});

		menuImageEnter.setText("Enter");
		menuImageEnter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuImageEnter_actionPerformed(e);
			}
		});
		menuImageFind.setText("Find");
		menuImageFind.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuImageFind_actionPerformed(e);
			}
		});

		menuImageAlignment.setText("Align");
		menuImageAlignment.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuImageAlignment_actionPerformed(e);
			}
		});

		menuImageLoadNext.setText("Load Next 10");
		menuImageLoadNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuImageLoadNext_actionPerformed(e);
			}
		});

		menuImageLoadPrev.setText("Load Prev 10");
		menuImageLoadPrev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuImageLoadPrev_actionPerformed(e);
			}
		});

		menuUtilitiesReport.setText("Generate Report");
		menuUtilitiesReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuUtilitiesReport_actionPerformed(e);
			}
		});
		jSplitPane1.setOrientation(JSplitPane.VERTICAL_SPLIT);
		jSplitPane1.setOneTouchExpandable(true);
		jSplitPane1.setResizeWeight(1.0);
		jPanel1.setLayout(gridBagLayout2);
		jLabel1.setText("Brightness");
		jLabel2.setText("Contrast");
		jLabel3.setText("Zoom");
		jLabel4.setText("Rotation");
		brightnessSlider.setMaximum(256);
		brightnessSlider.setMinimum(-256);
		brightnessSlider.setPaintTicks(true);
		brightnessSlider.setMinorTickSpacing(16);
		brightnessSlider.setMajorTickSpacing(64);
		brightnessSlider.setValue(0);

		Hashtable labels1 = new Hashtable();
		labels1.put(new Integer(-255), new JLabel("-255"));
		labels1.put(new Integer(-128), new JLabel("-128"));
		labels1.put(new Integer(0), new JLabel("0"));
		labels1.put(new Integer(128), new JLabel("128"));
		labels1.put(new Integer(255), new JLabel("255"));
		brightnessSlider.setLabelTable(labels1);
		brightnessSlider.setPaintLabels(true);

		contrastSlider.setMajorTickSpacing(50);
		contrastSlider.setMinimum(-100);
		contrastSlider.setMinorTickSpacing(10);
		contrastSlider.setPaintTicks(true);
		contrastSlider.setValue(0);

		Hashtable labels3 = new Hashtable();
		labels3.put(new Integer(-100), new JLabel("-100"));
		labels3.put(new Integer(-50), new JLabel("-50"));
		labels3.put(new Integer(0), new JLabel("0"));
		labels3.put(new Integer(50), new JLabel("50"));
		labels3.put(new Integer(100), new JLabel("100"));
		contrastSlider.setLabelTable(labels3);
		contrastSlider.setPaintLabels(true);

		zoomSlider.setMinorTickSpacing(5);
		zoomSlider.setMinimum(10);
		zoomSlider.setValue(60);
		zoomSlider.setMajorTickSpacing(10);
		zoomSlider.setPaintTicks(true);

		Hashtable labels2 = new Hashtable();
		labels2.put(new Integer(10), new JLabel("1:10"));
		labels2.put(new Integer(60), new JLabel("1:5"));
		labels2.put(new Integer(100), new JLabel("1:1"));
		zoomSlider.setLabelTable(labels2);
		zoomSlider.setPaintLabels(true);

		rotationSlider.setMaximum(180);
		rotationSlider.setMinimum(-180);
		rotationSlider.setValue(0);
		rotationSlider.setMajorTickSpacing(30);
		rotationSlider.setMinorTickSpacing(10);
		rotationSlider.setPaintTicks(true);

		menuImageAlignment.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, Event.CTRL_MASK, false));
		menuImageAlignment.setMnemonic('A');

		menuUtilitiesReport.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, Event.CTRL_MASK, false));
		menuUtilitiesBackup.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, Event.CTRL_MASK, false));
		menuUtilitiesStitch.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, Event.CTRL_MASK, false));
		menuUserManual.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0, false));
		menuUtilities.setMnemonic('U');
		menuImageFind.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, Event.CTRL_MASK, false));
		menuImageEnter.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, Event.CTRL_MASK, false));
		menuImage.setMnemonic('I');
		menuFile.setMnemonic('F');
		zoomSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				zoomRotate_stateChanged(e);
			}
		});
		contrastSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				brightnessContrast_stateChanged(e);
			}
		});
		brightnessSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				brightnessContrast_stateChanged(e);
			}
		});

		Hashtable labels4 = new Hashtable();
		menuUserManual.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuUserManual_actionPerformed(e);
			}
		});
		labels4.put(new Integer(-180), new JLabel("-180"));
		labels4.put(new Integer(-90), new JLabel("-90"));
		labels4.put(new Integer(0), new JLabel("0"));
		labels4.put(new Integer(90), new JLabel("90"));
		labels4.put(new Integer(180), new JLabel("180"));
		rotationSlider.setLabelTable(labels4);
		rotationSlider.setPaintLabels(true);
		rotationSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				zoomRotate_stateChanged(e);
			}
		});
		graphicalDisplayMenu.setText("Output");
		graphicalDisplayMenu.setMnemonic('G');
		
		calculateSynapsesMenuItem.setText("Update Synapses Contins");

		calculateSynapsesMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calculateSynapsesMenuItem_actionPerformed(e);
			}
		});

		updateSynapsesMenuItem.setText("Update Synapse Lists");

		updateSynapsesMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calculateSynapsesMenuItem_actionPerformed(e);
			}
		});

		SynListMenuItem.setText("Generate Synapse list by neuron name or contin#");

		SynListMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SynListMenuItem_actionPerformed(e);
			}
		});

		twoDDisplayMenuItem.setText("2D Display");
		twoDDisplayMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, Event.CTRL_MASK, false));
		
		twoDDisplayMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				twoDDisplayMenuItem_actionPerformed(e);
			}
		});
		
		debugMenuItem.setText("Print Debugging Statistics");
		debugMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, Event.CTRL_MASK, false));
		
		debugMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ELog.info(EDatabase.getConnectionStats());
			}
		});
		

		partnerListMenuItem.setText("Cumulative partners list by neuron name or contin#");

		partnerListMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				partnerListMenuItem_actionPerformed(e);
			}
		});

		adjacentMatrixChemMenuItem.setText("Chemical Adjacency Matrix");

		adjacentMatrixChemMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				adjacentMatrixChemMenuItem_actionPerformed(e);
			}
		});

		adjacentMatrixEleMenuItem.setText("Electrical Adjacency Matrix");

		adjacentMatrixEleMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				adjacentMatrixEleMenuItem_actionPerformed(e);
			}
		});

		ThreeDDisplayMenuItem.setText("3D Display");

		ThreeDDisplayMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ThreeDDisplayMenuItem_actionPerformed(e);
			}
		});

		quickDisplayMenuItem.setText("Quick 2D Display");

		quickDisplayMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				quickDisplayMenuItem_actionPerformed(e);
			}
		});
		menuImageLock.setText("Lock Images");
		menuImageLock.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, Event.CTRL_MASK, false));
		menuImageLock.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuImageLock_actionPerformed();
			}
		});
		menuSave.setText("Calculate Contins");
		menuSave.setMnemonic('S');
		
		menuCalculateFromObj.setText("Calculate a Cell Contin From a Obj #");

		menuCalculateFromObj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuCalculateromObj_actionPerformed(e);
			}
		});

		menuCalculateByContin.setText("Calculate a Cell Contin From a Contin #");

		menuCalculateByContin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuCalculateByContin_actionPerformed(e);
			}
		});

		menuCalculateAllContins.setText("Calculate All Contins");

		menuCalculateAllContins.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuCalculatateAllContins_actionPerformed(e);
			}
		});

		
		menuView.setText("View");
		menuView.setMnemonic('V');
		menuViewObjects.setText("Objects");
		menuViewObjects.setSelected(true);
		menuViewObjects.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_J, Event.CTRL_MASK, false));
		menuViewObjects.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuViewObjects_actionPerformed(e);
			}
		});
		// jMenu1.setText ( "Relationships" );
		viewMultPostsynapticRelMenu.setText("Multiple Postsynaptic");
		viewMultPostsynapticRelMenu.setSelected(true);
		viewMultPostsynapticRelMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_6, Event.CTRL_MASK, false));
		viewMultPostsynapticRelMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewMultPostsynapticRelMenu_actionPerformed(e);
			}
		});
		viewMultPresynapticRelMenu.setText("Multiple Presynaptic");
		viewMultPresynapticRelMenu.setSelected(true);
		viewMultPresynapticRelMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_5, Event.CTRL_MASK, false));
		viewMultPresynapticRelMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewMultPresynapticRelMenu_actionPerformed(e);
			}
		});
		viewGapRelMenu.setText("Gap Junctions");
		viewGapRelMenu.setSelected(true);
		viewGapRelMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_4, Event.CTRL_MASK, false));
		viewGapRelMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewGapRelMenu_actionPerformed(e);
			}
		});
		viewPostsynapticRelMenu.setText("Postsynaptic");
		viewPostsynapticRelMenu.setSelected(true);
		viewPostsynapticRelMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, Event.CTRL_MASK, false));
		viewPostsynapticRelMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewPostsynapticRelMenu_actionPerformed(e);
			}
		});
		viewPresynapticRelMenu.setText(" Presynaptic");
		viewPresynapticRelMenu.setSelected(true);
		viewPresynapticRelMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, Event.CTRL_MASK, false));
		viewPresynapticRelMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewPresynapticRelMenu_actionPerformed(e);
			}
		});
		viewContinuousRelMenu.setText(" Continuous");
		viewContinuousRelMenu.setSelected(true);
		viewContinuousRelMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, Event.CTRL_MASK, false));
		viewContinuousRelMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewContinuousRelMenu_actionPerformed(e);
			}
		});
		viewAllRelMenu.setText("All");
		viewAllRelMenu.setSelected(true);
		viewAllRelMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_0, Event.CTRL_MASK, false));
		viewAllRelMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewAllRelMenu_actionPerformed(e);
			}
		});
		viewRelationWindowMenu.setText("Relationships Window");
		viewRelationWindowMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, Event.CTRL_MASK, false));
		viewRelationWindowMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewRelationWindowMenu_actionPerformed(e);
			}
		});
		viewAllObjectsWindowMenu.setText("All Objects");
		viewAllObjectsWindowMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, Event.CTRL_MASK, false));
		viewAllObjectsWindowMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewAllObjectsWindowMenu_actionPerformed(e);
			}
		});

		viewSelectedObjectWindowMenu.setText("Selected Object");
		viewSelectedObjectWindowMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK, false));
		viewSelectedObjectWindowMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewSelectedObjectWindowMenu_actionPerformed(e);
			}
		});

		synSearchMenu.setText("uncertain synapses list");

		synSearchMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				synSearchMenu_actionPerformed(e);
			}
		});

		synFindMenu.setText("Locate an object");

		synFindMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				synFindMenu_actionPerformed(e);
			}
		});

		menuResetSlider.setText("Reset Sliders");
		menuResetSlider.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuResetSlider_actionPerformed(e);
			}
		});
		viewRefreshMenu.setText("Refresh");
		viewRefreshMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0, false));
		viewRefreshMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewRefreshMenu_actionPerformed(e);
			}
		});

		showHideObjectsMenu.setText("show/hide all objects");

		showHideObjectsMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showHideObjects_actionPerformed(e);
			}
		});

		menuImageModify.setText("Modify");
		menuImageModify.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, Event.CTRL_MASK, false));
		menuImageModify.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuImageModify_actionPerformed(e);
			}
		});
		viewContinsMenu.setText("Contins Window");
		viewContinsMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Event.CTRL_MASK, false));
		viewContinsMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewContinsMenu_actionPerformed(e);
			}
		});

		filtersMenu.setText("Filters");
		filtersMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, Event.CTRL_MASK, false));
		filtersMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				displayOptions_actionPerformed(e);
			}
		});

		lookAndFeelMenu.setText("Look & Feel");
		metalLookMenuItem.setText("Metal");
		metalLookMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				metalLookMenuItem_actionPerformed(e);
			}
		});
		motifLookMenuItem.setText("Motif");
		motifLookMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				motifLookMenuItem_actionPerformed(e);
			}
		});
		systemLookMenuItem.setText("System");
		systemLookMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				systemLookMenuItem_actionPerformed(e);
			}
		});

		menuFileExit.setText("Exit");
		menuFileExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, Event.CTRL_MASK, false));
		menuFileExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				fileExit_ActionPerformed(ae);
			}
		});

		switchDB.setText("Restart");

		switchDB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				switchDB_ActionPerformed(ae);
			}
		});

		

		menuHelp.setText("Help");
		menuHelp.setMnemonic('H');
		menuHelpAbout.setText("About");
		menuHelpAbout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, Event.ALT_MASK, false));
		menuHelpAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				helpAbout_ActionPerformed(ae);
			}
		});
		menuFile.add(menuFileExit);
		menuFile.add(switchDB);
		menuBar.add(menuFile);
		menuView.add(viewRefreshMenu);
		// menuView.add(showHideObjectsMenu);
		//menuView.add(showContin);
		menuBar.add(menuView);

		// menuView.add ( menuViewObjects );
		// jMenu1.add ( viewAllRelMenu );
		// jMenu1.add ( viewContinuousRelMenu );
		// jMenu1.add ( viewPresynapticRelMenu );
		// jMenu1.add ( viewPostsynapticRelMenu );
		// jMenu1.add ( viewGapRelMenu );
		// jMenu1.add ( viewMultPresynapticRelMenu );
		// jMenu1.add ( viewMultPostsynapticRelMenu );
		// menuView.add ( jMenu1 );
		menuView.add(viewAllObjectsWindowMenu);
		menuView.add(viewSelectedObjectWindowMenu);
		// menuView.add ( synSearchMenu );
		menuView.add(synFindMenu);
		menuView.add(viewContinsMenu);
		menuView.add(filtersMenu);
		menuBar.add(menuView);

		menuSave.add(menuCalculateFromObj);
		menuSave.add(menuCalculateByContin);
		menuSave.add(menuCalculateAllContins);
		
		menuSave.add(calculateSynapsesMenuItem);
		menuBar.add(menuSave);
		menuImage.add(menuImageEnter);
		menuImage.add(menuImageModify);
		menuImage.add(menuImageFind);
		menuImage.add(menuImageLock);
		menuImage.add(menuImageAlignment);
		menuImage.add(menuImageLoadNext);
		menuImage.add(menuImageLoadPrev);
		menuImage.add(menuResetSlider);
		menuBar.add(menuImage);
		menuUtilities.add(menuUtilitiesStitch);
		// menuUtilities.add ( menuUtilitiesBackup );
		// menuUtilities.add ( menuUtilitiesReport );

		// menuBar.add ( menuUtilities );

		graphicalDisplayMenu.add(updateSynapsesMenuItem);
		graphicalDisplayMenu.add(SynListMenuItem);
		graphicalDisplayMenu.add(partnerListMenuItem);
		graphicalDisplayMenu.add(adjacentMatrixChemMenuItem);
		graphicalDisplayMenu.add(adjacentMatrixEleMenuItem);
		graphicalDisplayMenu.add(twoDDisplayMenuItem);
		if (EDatabase.TRACE_CONNECTION_POOLING) graphicalDisplayMenu.add(debugMenuItem);
		graphicalDisplayMenu.add(ThreeDDisplayMenuItem);
		
		// graphicalDisplayMenu.add ( quickDisplayMenuItem );
		menuBar.add(graphicalDisplayMenu);

		lookAndFeelMenu.add(metalLookMenuItem);
		lookAndFeelMenu.add(motifLookMenuItem);
		lookAndFeelMenu.add(systemLookMenuItem);
		// menuBar.add(lookAndFeelMenu);
		menuHelp.add(menuHelpAbout);
		// menuHelp.add ( menuUserManual );
		// menuBar.add ( menuHelp );

		desktop.setDesktopManager(cDManager);
		desktop.putClientProperty("JDesktopPane.dragMode", "outline");
		jSplitPane1.add(desktop, JSplitPane.TOP);
		jPanel1.add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		jPanel1.add(jLabel2, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		jPanel1.add(jLabel3, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		jPanel1.add(jLabel4, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		jPanel1.add(brightnessSlider, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0,
				0, 0), 0, 0));
		jPanel1.add(contrastSlider, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0,
				0), 0, 0));
		jPanel1.add(zoomSlider, new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
				0, 0));
		jPanel1.add(rotationSlider, new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0,
				0), 0, 0));
		jSplitPane1.add(jPanel1, JSplitPane.BOTTOM);
		this.getContentPane().add(jSplitPane1,
				new GridBagConstraints(2, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		this.addKeyListener(this);

		// desktop.addKeyListener ( this );
		this.requestFocusInWindow();
	}

	void fileExit_ActionPerformed(ActionEvent e) {
		System.exit(0);
	}

	void helpAbout_ActionPerformed(ActionEvent e) {
		JOptionPane.showMessageDialog(this, new MainFrame_AboutBoxPanel1(), "About", JOptionPane.PLAIN_MESSAGE);
	}

	private void menuImageFind_actionPerformed(ActionEvent e) {
		// Util.info("image find called");
		if (isFrame == null) {
			isFrame = new ImageSearchFrame();
			isFrame.getLoadButton().addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					isFrameLoad_ActionPerformed(ae);
				}
			});

			isFrame.getTable().addKeyListener(new KeyListener() {

				public void keyTyped(KeyEvent e) {

				}

				public void keyPressed(KeyEvent e) {
					if (e.getKeyChar() == KeyEvent.VK_ENTER) {
						isFrameLoad_ActionPerformed(e);
					}
					
				}

				public void keyReleased(KeyEvent e) {

				}

			});

			/*
			 * isFrame.getTable().addMouseListener(new MouseListener() { public
			 * void mouseClicked(MouseEvent e) { if (e.getClickCount()==2) {
			 * isFrameLoad_ActionPerformed(e); } }
			 * 
			 * public void mousePressed(MouseEvent e) {
			 * 
			 * }
			 * 
			 * public void mouseReleased(MouseEvent e) {
			 * 
			 * }
			 * 
			 * public void mouseEntered(MouseEvent e) {
			 * 
			 * }
			 * 
			 * public void mouseExited(MouseEvent e) {
			 * 
			 * } });
			 */
		}

		isFrame.setVisible(true);
		isFrame.setFocus();
	}


	
	private void isFrameLoad_ActionPerformed(Object e) {
		try {
			JTable table = isFrame.getTable();
			int[] selection = table.getSelectedRows();

			for (int i = 0; i < selection.length; i++) {
				String imgNo = table.getValueAt(selection[i], 0).toString();
				ELog.info("Starting to process image " + imgNo);
				frameIMG[i] = imgNo;
				String fileName = table.getValueAt(selection[i], 1).toString();
				String directoryName = table.getValueAt(selection[i], 2).toString();

				// createFrame ( directoryName + fileName, imgNo );
				createFrame(directoryName + File.separator + fileName, imgNo);
				ELog.info("Finished to process image " + imgNo);
			}
			// begin of Meng
			JInternalFrame[] frameM = getSortedFrame();
			if (frameM.length >= 3) {
				frameM[2].setSelected(true);
			}
			if (frameM.length >= 2) {
				frameM[1].setSelected(true);
			}
			frameM[0].setSelected(true);

			ELog.info("all images loaded");
			addKeyListener(this);
			
			isFrame.setExtendedState(isFrame.getExtendedState() |  Frame.ICONIFIED);        
			isFrame.setVisible(true);
	        
			//isFrame.dispose();
			//isFrame = null;
			
			// *end of Meng

		} catch (OutOfMemoryError ex) {
			// Util.info("caught error");
			// ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "The available memory is not sufficient to carry out this operation", "Error", JOptionPane.ERROR_MESSAGE);
		} catch (Exception exx) {
			// Util.info("caught exception");
			// ex.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"An error has occurred.\nThis may be because the available memory is not sufficient to carry out this operation", "Error",
					JOptionPane.ERROR_MESSAGE);
		}

	}

	public String[] getFrameIMG() {
		return frameIMG;
	}

	private void menuUtilitiesBackup_actionPerformed(ActionEvent e) {
		Connection con = null;

		try {
			con = EDatabase.borrowConnection();

			Statement stm = con.createStatement();

			java.util.Date date = new java.util.Date(System.currentTimeMillis());

			SimpleDateFormat format = new SimpleDateFormat("ddMMMyyyy_HHmm");
			String str = format.format(date);

			JOptionPane.showMessageDialog(null, "Starting back up...");

			// if (
			// stm.executeUpdate (
			// "SELECT * INTO OUTFILE \"Image_" + str + ".txt\" FROM Image"
			// ) > 0
			// )
			// {
			// JOptionPane.showMessageDialog ( null, "Image table backed up..."
			// );
			// }
			//
			// if (
			// stm.executeUpdate (
			// "SELECT * INTO OUTFILE \"Object_" + str + ".txt\" FROM Object"
			// ) > 0
			// )
			// {
			// JOptionPane.showMessageDialog ( null, "Object table backed up..."
			// );
			// }
			//
			// if (
			// stm.executeUpdate (
			// "SELECT * INTO OUTFILE \"Contin_" + str + ".txt\" FROM Contin"
			// ) > 0
			// )
			// {
			// JOptionPane.showMessageDialog ( null, "Contin table backed up..."
			// );
			// }
			//
			// if (
			// stm.executeUpdate (
			// "SELECT * INTO OUTFILE \"Relationship_" + str
			// + ".txt\" FROM Relationship"
			// ) > 0
			// )
			// {
			// JOptionPane.showMessageDialog ( null,
			// "Relationship table backed up..." );
			// }
			stm.execute("SELECT * INTO OUTFILE \"Image_" + str + ".txt\" FROM Image");
			JOptionPane.showMessageDialog(null, "Image table backed up...");
			stm.execute("SELECT * INTO OUTFILE \"Object_" + str + ".txt\" FROM Object");
			JOptionPane.showMessageDialog(null, "Object table backed up...");
			stm.execute("SELECT * INTO OUTFILE \"Contin_" + str + ".txt\" FROM Contin");
			JOptionPane.showMessageDialog(null, "Contin table backed up...");
			stm.execute("SELECT * INTO OUTFILE \"Relationship_" + str + ".txt\" FROM Relationship");
			JOptionPane.showMessageDialog(null, "Relationship table backed up...");
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			EDatabase.returnConnection(con);
		}

		
		/*
		 * Thread thr = new Thread( ) { public void run ( ) {
		 * JOptionPane.showMessageDialog ( null,
		 * "This process might take some time. Please wait ....", "Please Wait",
		 * JOptionPane.INFORMATION_MESSAGE ); try { Process p =
		 * Runtime.getRuntime ( ).exec ( "notepad.exe" ); p.waitFor ( ); } catch
		 * ( Exception ex ) { Util.info (
		 * "Could not perform the operation due to error: " + ex ); }
		 * Util.info ( "Exited now" ); } }; thr.start ( );
		 */
	}

	private void menuUtilitiesReport_actionPerformed(ActionEvent e) {
		Utilities.generateReport(Utilities.getFileNameFromUser("wormbaseReport.txt"));
	}

	private void menuImageEnter_actionPerformed(ActionEvent e) {
		// if ( ieFrame == null )
		// {
		ieFrame = new ImageEntryFrame();

		// }
		ieFrame.setVisible(true);
	}

	private void menuUtilitiesStitch_actionPerformed(ActionEvent e) {
		Utilities.startStitchApplication();
	}

	protected void createFrame(String filename, String imageNo) {
		// ImageDisplayFrame frame = new
		// ImageDisplayFrame("d:\\david\\worm_images\\PAG770L.jpg");
		JInternalFrame[] jifs = desktop.getAllFrames();
		for (int i = 0; i < jifs.length; i++) {
			if (((ImageDisplayFrame) jifs[i]).getImageNumber().compareToIgnoreCase(imageNo) == 0) {
				int indexOfComp = desktop.getIndexOf(jifs[i]);
				desktop.remove(indexOfComp);
			}
		}

		File file = new File(filename);

		if ((file.exists() == false) || (file.canRead() == false)) {
			ELog.info("File " + filename + " not found");
			JOptionPane.showMessageDialog(this, "File " + filename + " not found", "Error", JOptionPane.ERROR_MESSAGE);

			return;
		}

		ImageDisplayFrame frame = new ImageDisplayFrame(filename, imageNo, username);
		ImageDisplayPanel idp = frame.getImageDisplayPanel();

		idp.addRelations();

		idp.addMouseListener(this);
		idp.addMouseMotionListener(this);
		idp.addKeyListener(this);

		// Util.info("Opened Frame");
		frame.setVisible(true); // necessary as of kestrel
		desktop.add(frame);
		 
		
		try {
			frame.setSelected(true);
		} catch (java.beans.PropertyVetoException e) {
		}
	}

	private void brightnessContrast_stateChanged(ChangeEvent e) {
		if ((brightnessSlider.getValueIsAdjusting() == false) && (contrastSlider.getValueIsAdjusting() == false) && (isSynchronizing == false)) {
			int brightness = brightnessSlider.getValue();
			int contrast = contrastSlider.getValue();

			JInternalFrame jif = desktop.getSelectedFrame();

			if (jif == null) {
				return;
			}

			ImageDisplayPanel panel = ((ImageDisplayFrame) jif).getImageDisplayPanel();
			panel.setBrightnessAndContrast(brightness, 1.0f + ((float) (contrast) / 200.0f));
			panel.repaint();
			// desktop.repaint();
			// Util.info("brightness/contrast changed");
		}
	}

	private void zoomRotate_stateChanged(ChangeEvent e) {
		if ((zoomSlider.getValueIsAdjusting() == false) && (rotationSlider.getValueIsAdjusting() == false)) {
			if (menuImageLock.isSelected() == false) {
				// Util.info("changing zoom and rotate");
				int int_zoom = zoomSlider.getValue();
				int int_rotate = rotationSlider.getValue();

				JInternalFrame jif = desktop.getSelectedFrame();

				if (jif == null) {
					return;
				}

				ImageDisplayPanel panel = ((ImageDisplayFrame) jif).getImageDisplayPanel();
				panel.zoomAndRotate(10.0f / (float) (110 - int_zoom), (float) int_rotate);
				panel.repaint();

			} else {
				int int_zoom = zoomSlider.getValue();
				int int_rotate = rotationSlider.getValue();

				JInternalFrame[] jifs = desktop.getAllFrames();

				for (int i = 0; i < jifs.length; i++) {
					ImageDisplayPanel idp = ((ImageDisplayFrame) jifs[i]).getImageDisplayPanel();
					idp.zoomAndRotate(10.0f / (float) (110 - int_zoom) / zoomRatio * idp.getTemp_zoom(),
							(float) (int_rotate + ((idp.getTemp_rotation() * 180) / Math.PI)));
					idp.repaint();
				}
			}
			desktop.repaint();
		}
	}

	private void synchronizeSliders() {
		isSynchronizing = true;

		try {
			ImageDisplayPanel idp = ((ImageDisplayFrame) desktop.getSelectedFrame()).getImageDisplayPanel();

			// System.out.print("will sync according to " +
			// idp.getImageNumber());
			// System.out.print("brightness = " + idp.getBrightness());
			brightnessSlider.setValue(idp.getBrightness());

			// Util.info("contrast = " + idp.getContrast());
			contrastSlider.setValue(idp.getContrast());

			float zoom = idp.getZoom();
			int intZoom = 110 - (int) (10.0 / zoom);
			zoomSlider.setValue(intZoom);

			double rot = idp.getRotation();
			int intRot = (int) ((rot * 180.0) / Math.PI);
			rotationSlider.setValue(intRot);
		} catch (NullPointerException npe) {
		}

		isSynchronizing = false;
	}

	private void menuImageLock_actionPerformed() {
		// Util.info("menu lock action performed");
		if (menuImageLock.isSelected()) {
			JInternalFrame[] jifs = desktop.getAllFrames();

			for (int i = 0; i < jifs.length; i++) {
				ImageDisplayPanel idp = ((ImageDisplayFrame) jifs[i]).getImageDisplayPanel();

				// idp.addMouseListener ( this );
				// idp.addMouseMotionListener ( this );
				idp.setTemp_zoom(idp.getZoom());
				idp.setTemp_rotation(idp.getRotation());
			}

			synchronizeSlidersForLock();

			// Util.info("locked");
		} else {
			JInternalFrame[] jifs = desktop.getAllFrames();

			for (int i = 0; i < jifs.length; i++) {
				ImageDisplayPanel idp = ((ImageDisplayFrame) jifs[i]).getImageDisplayPanel();

				// idp.removeMouseListener ( this );
				// idp.removeMouseMotionListener ( this );
			}

			unsynchronizeSlidersAfterLock();

			// Util.info("unlocked");
		}
	}

	private void synchronizeSlidersForLock() {
		brightnessSlider.setEnabled(false);
		contrastSlider.setEnabled(false);
		rotationSlider.setValue(0);

		JInternalFrame[] jifs = desktop.getAllFrames();

		// float finalZoom = 0f;
		zoomRatio = 0;

		for (int i = 0; i < jifs.length; i++) {
			ImageDisplayPanel idp = ((ImageDisplayFrame) jifs[i]).getImageDisplayPanel();
			float zoom = idp.getZoom();

			if (zoom > zoomRatio) {
				zoomRatio = zoom;
			}
		}

		int intZoom = 110 - (int) (10.0 / zoomRatio);
		zoomSlider.setValue(intZoom);
	}

	private void unsynchronizeSlidersAfterLock() {
		brightnessSlider.setEnabled(true);
		contrastSlider.setEnabled(true);
		synchronizeSliders();
	}

	/**
	 * This function is a part of the MouseListener interface and handles the
	 * event when the mouse is clicked.
	 * 
	 * @param e
	 *            The MouseEvent object associated with this event.
	 */
	public void mouseClicked(MouseEvent e) {
		desktop.repaint();
	}

	/**
	 * This function is a part of the MouseListener interface and handles the
	 * event when the mouse enters this component. Currently this function does
	 * nothing
	 * 
	 * @param e
	 *            The MouseEvent object associated with this event.
	 */
	public void mouseEntered(MouseEvent e) {
	}

	/**
	 * This function is a part of the MouseListener interface and handles the
	 * event when the mouse exits this component. Currently this function does
	 * nothing
	 * 
	 * @param e
	 *            The MouseEvent object associated with this event.
	 */
	public void mouseExited(MouseEvent e) {
	}

	/**
	 * This function is a part of the MouseListener interface and handles the
	 * event when the mouse button is pressed.
	 * 
	 * @param e
	 *            The MouseEvent object associated with this event.
	 */
	public void mousePressed(MouseEvent e) {

		if (menuImageLock.isSelected()) {
			JInternalFrame[] jifs = desktop.getAllFrames();

			for (int i = 0; i < jifs.length; i++) {
				ImageDisplayPanel idp = ((ImageDisplayFrame) jifs[i]).getImageDisplayPanel();

				// idp.mousePressed(e);
				idp.setLast_x(idp.getXOrigin() - e.getX());
				idp.setLast_y(idp.getYOrigin() - e.getY());
			}
		}
	}

	/**
	 * This function is a part of the MouseListener interface and handles the
	 * event when the mouse button is released.
	 * 
	 * @param e
	 *            The MouseEvent object associated with this event.
	 */
	public void mouseReleased(MouseEvent e) {
		if (menuImageLock.isSelected()) {
			JInternalFrame[] jifs = desktop.getAllFrames();

			for (int i = 0; i < jifs.length; i++) {
				ImageDisplayPanel idp = ((ImageDisplayFrame) jifs[i]).getImageDisplayPanel();
				idp.mouseReleased(e);
			}
		}

		desktop.repaint();
	}

	/**
	 * This function is a part of the MouseMotionListener interface and handles
	 * the event when the mouse is dragged.
	 * 
	 * @param e
	 *            The MouseEvent object associated with this event.
	 */
	public void mouseDragged(MouseEvent e) {
		if (menuImageLock.isSelected()) {
			JInternalFrame[] jifs = desktop.getAllFrames();

			for (int i = 0; i < jifs.length; i++) {
				ImageDisplayPanel idp = ((ImageDisplayFrame) jifs[i]).getImageDisplayPanel();

				// idp.mouseDragged(e);
				idp.setOrigin(e.getX() + idp.getLast_x(), e.getY() + idp.getLast_y());
			}
		}

		// desktop.repaint();
	}

	/**
	 * This function is a part of the MouseMotionListener interface and handles
	 * the event when the mouse is moved.
	 * 
	 * @param e
	 *            The MouseEvent object associated with this event.
	 */
	public void mouseMoved(MouseEvent e) {
		if (menuImageLock.isSelected()) {
			JInternalFrame[] jifs = desktop.getAllFrames();

			for (int i = 0; i < jifs.length; i++) {
				ImageDisplayPanel idp = ((ImageDisplayFrame) jifs[i]).getImageDisplayPanel();
				idp.mouseMoved(e);
			}
		}
	}

	private void menuViewObjects_actionPerformed(ActionEvent e) {
		JInternalFrame jif = desktop.getSelectedFrame();

		if (jif == null) {
			if (menuViewObjects.isSelected() == true) {
				menuViewObjects.setSelected(false);
			} else {
				menuViewObjects.setSelected(true);
			}
			return;
		}

		if (menuViewObjects.isSelected() == true) {
			ApplicationProperties.showObjects = true;
			// jMenu1.setEnabled ( true );
		} else {
			ApplicationProperties.showObjects = false;
			ApplicationProperties.showAllRelations = false;
			ApplicationProperties.showContinuousRelations = false;
			ApplicationProperties.showGapRelations = false;
			ApplicationProperties.showMultPostSynapticRelations = false;
			ApplicationProperties.showMultPresynapticRelations = false;
			ApplicationProperties.showPostsynapticRelations = false;
			ApplicationProperties.showPresynapticRelations = false;
			viewAllRelMenu.setSelected(false);
			viewContinuousRelMenu.setSelected(false);
			viewPresynapticRelMenu.setSelected(false);
			viewPostsynapticRelMenu.setSelected(false);
			viewGapRelMenu.setSelected(false);
			viewMultPresynapticRelMenu.setSelected(false);
			viewMultPostsynapticRelMenu.setSelected(false);
			// jMenu1.setEnabled ( false );
		}

		desktop.repaint();
	}

	/**
	 * This function is a part of the KeyListener interface and handles the
	 * event when a key is typed. Currently this function does nothing.
	 * 
	 * @param e
	 *            The KeyEvent object associated with this event.
	 */
	public void keyTyped(KeyEvent e) {
		
	}

	// begin of Meng's modification

	// left and right key press, shift the images

	public JInternalFrame[] getSortedFrame() {
		JInternalFrame[] frameM = desktop.getAllFrames();
		int n = frameM.length;
		JInternalFrame[] frameS = new JInternalFrame[n];
		String[] imgNum = new String[n];
		for (int i = 0; i < frameM.length; i++) {
			imgNum[i] = ((ImageDisplayFrame) frameM[i]).getImageNumber();
		}
		String[] imgNumOrg = new String[n];
		for (int i = 0; i < imgNum.length; i++) {
			imgNumOrg[i] = imgNum[i];
		}

		Arrays.sort(imgNum);
		/**
		 * Util.info("original:"); for (int k=0; k<imgNumOrg.length;
		 * k++) { Util.info(imgNumOrg[k]); }
		 * Util.info("after sort:"); for (int k=0; k<imgNum.length;
		 * k++) { Util.info(imgNum[k]); }
		 **/
		for (int i = 0; i < imgNum.length; i++) {
			for (int j = 0; j < imgNumOrg.length; j++) {
				if (imgNumOrg[j].equals(imgNum[i])) {
					frameS[i] = frameM[j];

					// Util.info(j+" "+
					// ((ImageDisplayFrame)frameM[i]).getImageNumber());

					if (frameM[j].isSelected()) {
						try {
							frameS[i].setSelected(true);
						} catch (java.beans.PropertyVetoException e1) {
						}
					}
				}

			}

		}
		return frameS;
	}

	public void keyPressed(KeyEvent e) {

		if (e.getKeyChar() == KeyEvent.VK_SPACE) {
			Elegance.filterOptions.setHideAll(!Elegance.filterOptions.isHideAll());
			desktop.repaint();
		}


		if (e.getKeyCode() == KeyEvent.VK_RIGHT) { // Util.info("right");

			JInternalFrame frameS[] = getSortedFrame();
			for (int i = 0; i < frameS.length; i++) {
				if (frameS[i].isSelected()) {
					// Util.info("focus image: "+i+" "+((ImageDisplayFrame)frameS[i]).getImageNumber());
					if (i < frameS.length - 2) {

						try {
							frameS[i].setLocation(0, 0);
							frameS[i].setSelected(true);
							frameS[i].setSelected(false);

							/**
							 * int xx = ((ImageDisplayFrame)frameS[i]).
							 * getImageDisplayPanel().getXOrigin(); int yy =
							 * ((ImageDisplayFrame
							 * )frameS[i]).getImageDisplayPanel().getYOrigin();
							 * float zzoom = ((ImageDisplayFrame)frameS[i]).
							 * getImageDisplayPanel().getZoom();
							 * 
							 * ((ImageDisplayFrame)frameS[i+1]).
							 * getImageDisplayPanel().zoomAndRotate( zzoom,
							 * 0.0f); ((ImageDisplayFrame)frameS[i+2]).
							 * getImageDisplayPanel().zoomAndRotate( zzoom,
							 * 0.0f);
							 * 
							 * 
							 * ((ImageDisplayFrame)frameS[i+1]).
							 * getImageDisplayPanel().setOrigin(xx,yy);
							 * ((ImageDisplayFrame
							 * )frameS[i+2]).getImageDisplayPanel
							 * ().setOrigin(xx,yy);
							 **/

							frameS[i + 2].setLocation(fwidth * 2, 0);
							frameS[i + 2].setSelected(true);
							frameS[i + 2].setSelected(false);
							frameS[i + 1].setLocation(fwidth, 0);
							frameS[i + 1].setSelected(true);

						} catch (java.beans.PropertyVetoException e1) {
						}
					}
					break;
				}
			}
		}

		if (e.getKeyCode() == KeyEvent.VK_LEFT) { // Util.info("left");

			JInternalFrame frameS[] = getSortedFrame();
			for (int i = 0; i < frameS.length; i++) {
				if (frameS[i].isSelected()) {
					// Util.info("get the focus image");
					if (i >= 2) {

						try {
							frameS[i].setLocation(fwidth * 2, 0);
							frameS[i].setSelected(true);
							frameS[i].setSelected(false);

							frameS[i - 2].setLocation(0, 0);
							frameS[i - 2].setSelected(true);
							frameS[i - 2].setSelected(false);

							frameS[i - 1].setLocation(fwidth, 0);
							frameS[i - 1].setSelected(true);

						} catch (java.beans.PropertyVetoException e1) {
						}
					}
					break;
				}
			}
		}

		desktop.repaint();
	}

	public void keyReleased(KeyEvent e) {
		// do nothing
	}

	private void menuCalculateromObj_actionPerformed(ActionEvent e) {
		
		Integer objName;
		if ((objName=EString.getObjectNameFromUser("Enter the object name to calculate"))==null) return;
		
		try {
			long time1 = System.currentTimeMillis();

			NameContin na = new NameContin(objName,this);
			new Calculate(objName, na.continNum);

			long time2 = System.currentTimeMillis();
			long time = (time2 - time1) / 1000;
			JOptionPane.showMessageDialog(null, time + " seconds to calculate&smooth, done", "Calculation", JOptionPane.INFORMATION_MESSAGE);

			ELog.info("It took " + time + " to calculate. Done");
		}

		catch (SQLException e1) {
			e1.printStackTrace();
		}

		catch (java.lang.InstantiationException e2) {
			e2.printStackTrace();
		}

		catch (java.lang.IllegalAccessException e3) {
			e3.printStackTrace();
		}

		catch (ClassNotFoundException e4) {
			e4.printStackTrace();
		}

	}

	private void menuCalculatateAllContins_actionPerformed(ActionEvent e) {

		try {
			
			new ProgressBarDemo(new Calculate(),"Calculate All Contins").launch();

		} catch (Throwable e2) {
			String msg = "Can't calculate contins ";

			ELog.info(msg + ELog.e2s(e2));
			JOptionPane.showMessageDialog(null, msg, msg, JOptionPane.INFORMATION_MESSAGE);
		}

	}
	
	private void menuCalculateByContin_actionPerformed(ActionEvent e) {

		Integer continNum;
		
		if ((continNum=EString.getObjectNameFromUser("Enter the contin number to calculate"))==null) return;
		
		
		
		try {
			long time1 = System.currentTimeMillis();
				

			new Calculate(continNum);

			long time2 = System.currentTimeMillis();
			long time = (time2 - time1) / 1000;
			JOptionPane.showMessageDialog(null, time + " seconds to calculate&smooth, done", "Calculation", JOptionPane.INFORMATION_MESSAGE);

			ELog.info("It took " + time + " to calculate. Done");
		}

		catch (SQLException e1) {
			e1.printStackTrace();
		}

		catch (java.lang.InstantiationException e2) {
			e2.printStackTrace();
		}

		catch (java.lang.IllegalAccessException e3) {
			e3.printStackTrace();
		}

		catch (ClassNotFoundException e4) {
			e4.printStackTrace();
		}

	}

	private void menuSaveRelationships_actionPerformed(ActionEvent e) {
		if (ImageDisplayPanel.saveRelations() == -1) {
			desktop.repaint();
		}
	}

	private void calculateSynapsesMenuItem_actionPerformed(ActionEvent e) {

		// long time1 = System.currentTimeMillis();

		ProgressBarDemo bar=new ProgressBarDemo(new NewSynapses());
		//bar.setCloseAfterDone(true);
		bar.launch(true);
		
		
		// long time2 = System.currentTimeMillis();
		// long time = (time2-time1)/1000;
		// Util.info("It took "+time+" to calculate. Done, please press Ctrl + C to close the program");
		// JOptionPane.showMessageDialog(null,
		// time+" seconds have been used to calculate. Done!", "Calculation",
		// JOptionPane.INFORMATION_MESSAGE);

	}

	private void SynListMenuItem_actionPerformed(ActionEvent e) {
		(new SynapseListFrame()).show();

		/*
		 * 
		 * String name = JOptionPane.showInputDialog ( null,
		 * "Enter the neuron name to generate synapse list"); String filename =
		 * name+"_synlist_"+TwoDImage.now()+".xls"; WorkbookSettings ws = new
		 * WorkbookSettings(); ws.setLocale(new Locale("en", "EN"));
		 * WritableWorkbook workbook; try { workbook =
		 * Workbook.createWorkbook(new File(filename), ws);
		 * 
		 * WritableSheet s = workbook.createSheet("Sheet1", 0);
		 * 
		 * writeDataSheet(s, name); workbook.write(); workbook.close(); } catch
		 * (Exception e1) { // TODO Auto-generated catch block
		 * e1.printStackTrace(); }
		 */
	}

	/*
	 * private static void writeDataSheet(WritableSheet s, String name) throws
	 * WriteException, SQLException, ClassNotFoundException,
	 * java.lang.InstantiationException, java.lang.IllegalAccessException {
	 * 
	 * 
	 * // Format the Font WritableFont wf = new WritableFont(WritableFont.ARIAL,
	 * 10, WritableFont.BOLD); WritableCellFormat cf = new
	 * WritableCellFormat(wf); cf.setWrap(true); WritableCellFormat cf2 = new
	 * WritableCellFormat(NumberFormats.INTEGER);
	 * 
	 * //Creates Label to one cell of sheet Label l = new Label (0,0,name,cf);
	 * Number n; s.addCell(l); l = new Label (0,1,"electrical", cf);
	 * s.addCell(l); l = new Label (0,2,"partner", cf); s.addCell(l); l = new
	 * Label (1,2,"sections", cf); s.addCell(l); l = new Label (2,2,"image",
	 * cf); s.addCell(l);
	 * 
	 * 
	 * //Label l = new Label(0,0,"Matrix",cf); //s.addCell(l);
	 * 
	 * 
	 * Connection con = Util.getConnection( ); String jsql,jsql1;
	 * PreparedStatement pstmt,pstmt1; ResultSet rs,rs1; // electrical jsql =
	 * "select pre,post,sections,members from synapsecombined where ( pre = ? or post= ? )  and type='electrical' order by sections desc"
	 * ; pstmt = con.prepareStatement(jsql); pstmt.setString(1, name);
	 * pstmt.setString(2, name); rs = pstmt.executeQuery(); int i = 0; while
	 * (rs.next()) {
	 * 
	 * String partner = rs.getString(1); if(partner.equals("name")) partner =
	 * rs.getString(2); int sections = rs.getInt(3); String members =
	 * rs.getString(4); String m0 = members.split(",")[0];
	 * 
	 * l = new Label(0,3+i,partner,cf); s.addCell(l);
	 * 
	 * n = new Number(1,3+i,sections,cf2); s.addCell(n);
	 * 
	 * jsql1 = "select IMG_Number from object where OBJ_Name=?"; pstmt1 =
	 * con.prepareStatement(jsql1); pstmt1.setString(1, m0); rs1 =
	 * pstmt1.executeQuery();
	 * 
	 * while (rs1.next()) { l = new Label(2,3+i,rs1.getString(1),cf);
	 * s.addCell(l); } rs1.close(); pstmt1.close();
	 * 
	 * i++; }
	 * 
	 * rs.close(); pstmt.close();
	 * 
	 * //chemical in //chemical out
	 * 
	 * 
	 * 
	 * 
	 * 
	 * }
	 */

	private void twoDDisplayMenuItem_actionPerformed(ActionEvent e) {
		new TwoDImage();
	}

	private void partnerListMenuItem_actionPerformed(ActionEvent e) {
		(new PartnerListFrame()).show();
	}

	private void adjacentMatrixChemMenuItem_actionPerformed(ActionEvent e) {
		String left = JOptionPane.showInputDialog(null, "Enter the neuron names to show in LEFT list, enter 'all' mean all the neuron contin in contin table");
		String top = JOptionPane.showInputDialog(null, "Enter the neuron names to show in TOP list, enter 'all' mean all the neuron contin in contin table");
		String length = JOptionPane.showInputDialog("Enter the minimum size requirement of synapse , '0' means all, '1' means synapse more than one section",
				"0");

		new ProgressBarDemo(new AdjacentMatrix2("chemical", left, top, length)).launch();

	}

	private void adjacentMatrixEleMenuItem_actionPerformed(ActionEvent e) {
		String left = JOptionPane.showInputDialog(null, "Enter the neuron names to show in LEFT list, enter 'all' mean all the neuron contin in contin table");
		String top = JOptionPane.showInputDialog(null, "Enter the neuron names to show in TOP list, enter 'all' mean all the neuron contin in contin table");
		String length = JOptionPane.showInputDialog("Enter the minimum size requirement of synapse , '0' means all, '1' means synapse more than one section",
				"0");

		new ProgressBarDemo(new AdjacentMatrix2("electrical", left, top, length)).launch();

	}

		private void ThreeDDisplayMenuItem_actionPerformed ( ActionEvent e )
	{
		 try{
				
        	 Display3D display3D = new Display3D();
        	 JFrame myFrame = new JFrame("Applet Holder"); // create frame with title

        	    myFrame.add(display3D, BorderLayout.CENTER);
        	    myFrame.setResizable ( true );
        	  
        	    myFrame.setSize ( 800,600  );
        	    myFrame.setLocation ( 100, 100 );
        	    myFrame.setVisible(true); // usual step to make frame visible
	        }

	        catch(Exception e1){
	        e1.printStackTrace();
	        }
	}
	private void quickDisplayMenuItem_actionPerformed(ActionEvent e) {

	}

	private int verifyPresenceOfContin(String str) {
		int conNo = -1;
		Connection con = null;

		try {
			con = EDatabase.borrowConnection();

			PreparedStatement pst = con.prepareStatement("select CON_Number from contin where CON_AlternateName LIKE ?");
			pst.setString(1, str);

			ResultSet rs = pst.executeQuery();

			if (rs.next()) {
				conNo = rs.getInt("CON_Number");

				return conNo;
			} else {
				int number = 0;

				try {
					number = Integer.parseInt(str);
				} catch (NumberFormatException nex) {
					return -1;
				}

				pst = con.prepareStatement("select CON_Number from contin where CON_Number = ?");
				pst.setInt(1, number);
				rs = pst.executeQuery();

				if (rs.next()) {
					conNo = rs.getInt("CON_Number");

					return conNo;
				}
			}

			
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

			
		}finally {
			EDatabase.returnConnection(con);
		}

		return conNo;
	}

	private Vector getAllPresentContins(String str) {
		Connection con = null;
		Vector returner = new Vector(1);
		try {
			con = EDatabase.borrowConnection();

			PreparedStatement pst = con.prepareStatement("select CON_Number from contin where CON_AlternateName = ?");
			pst.setString(1, str);

			ResultSet rs = pst.executeQuery();

			if (rs.next()) {
				int conNo = rs.getInt("CON_Number");
				returner.addElement(new Integer(conNo));
				while (rs.next()) {
					conNo = rs.getInt("CON_Number");
					returner.addElement(new Integer(conNo));
				}
			} else {
				int number = 0;

				try {
					number = Integer.parseInt(str);
					pst = con.prepareStatement("select CON_Number from contin where CON_Number = ?");
					pst.setInt(1, number);
					rs = pst.executeQuery();

					while (rs.next()) {
						int conNo = rs.getInt("CON_Number");
						returner.addElement(new Integer(conNo));
					}
				} catch (NumberFormatException nex) {
					returner = new Vector();
				}
			}

			
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

		
		}finally {
			EDatabase.returnConnection(con);
		}

		return returner;
	}

	private void viewAllRelMenu_actionPerformed(ActionEvent e) {
		JInternalFrame jif = desktop.getSelectedFrame();

		if (jif == null) {
			return;
		}

		if (viewAllRelMenu.isSelected()) {
			if (ApplicationProperties.showObjects) {
				ApplicationProperties.showAllRelations = true;
			}

			ApplicationProperties.showContinuousRelations = true;
			ApplicationProperties.showPresynapticRelations = true;
			ApplicationProperties.showPostsynapticRelations = true;
			ApplicationProperties.showGapRelations = true;
			ApplicationProperties.showMultPresynapticRelations = true;
			ApplicationProperties.showMultPostSynapticRelations = true;

			viewContinuousRelMenu.setSelected(true);
			viewPresynapticRelMenu.setSelected(true);
			viewPostsynapticRelMenu.setSelected(true);
			viewGapRelMenu.setSelected(true);
			viewMultPresynapticRelMenu.setSelected(true);
			viewMultPostsynapticRelMenu.setSelected(true);
		} else {
			ApplicationProperties.showAllRelations = false;
			ApplicationProperties.showContinuousRelations = false;
			ApplicationProperties.showPresynapticRelations = false;
			ApplicationProperties.showPostsynapticRelations = false;
			ApplicationProperties.showGapRelations = false;
			ApplicationProperties.showMultPresynapticRelations = false;
			ApplicationProperties.showMultPostSynapticRelations = false;

			viewContinuousRelMenu.setSelected(false);
			viewPresynapticRelMenu.setSelected(false);
			viewPostsynapticRelMenu.setSelected(false);
			viewGapRelMenu.setSelected(false);
			viewMultPresynapticRelMenu.setSelected(false);
			viewMultPostsynapticRelMenu.setSelected(false);
		}

		desktop.repaint();
	}

	private void viewContinuousRelMenu_actionPerformed(ActionEvent e) {
		JInternalFrame jif = desktop.getSelectedFrame();

		if (jif == null) {
			return;
		}

		if (viewContinuousRelMenu.isSelected()) {
			if (ApplicationProperties.showObjects) {
				ApplicationProperties.showAllRelations = true;
			}

			ApplicationProperties.showContinuousRelations = true;
		} else {
			ApplicationProperties.showContinuousRelations = false;
			viewAllRelMenu.setSelected(false);
		}

		desktop.repaint();
	}

	private void viewPresynapticRelMenu_actionPerformed(ActionEvent e) {
		JInternalFrame jif = desktop.getSelectedFrame();

		if (jif == null) {
			return;
		}

		if (viewPresynapticRelMenu.isSelected()) {
			if (ApplicationProperties.showObjects) {
				ApplicationProperties.showAllRelations = true;
			}

			ApplicationProperties.showPresynapticRelations = true;
		} else {
			ApplicationProperties.showPresynapticRelations = false;
			viewAllRelMenu.setSelected(false);
		}

		desktop.repaint();
	}

	private void viewPostsynapticRelMenu_actionPerformed(ActionEvent e) {
		JInternalFrame jif = desktop.getSelectedFrame();

		if (jif == null) {
			return;
		}

		if (viewPostsynapticRelMenu.isSelected()) {
			if (ApplicationProperties.showObjects) {
				ApplicationProperties.showAllRelations = true;
			}

			ApplicationProperties.showPostsynapticRelations = true;
		} else {
			ApplicationProperties.showPostsynapticRelations = false;
			viewAllRelMenu.setSelected(false);
		}

		desktop.repaint();
	}

	private void viewGapRelMenu_actionPerformed(ActionEvent e) {
		JInternalFrame jif = desktop.getSelectedFrame();

		if (jif == null) {
			return;
		}

		if (viewGapRelMenu.isSelected()) {
			if (ApplicationProperties.showObjects) {
				ApplicationProperties.showAllRelations = true;
			}

			ApplicationProperties.showGapRelations = true;
		} else {
			ApplicationProperties.showGapRelations = false;
			viewAllRelMenu.setSelected(false);
		}

		desktop.repaint();
	}

	private void switchDB_ActionPerformed(ActionEvent e) {
	
		//make sure image frames are disposed (to free up memory taken by images)
		JInternalFrame[] jifs = desktop.getAllFrames();
		for (int i = 0; i < jifs.length; i++) {
			((ImageDisplayFrame) jifs[i]).dispose();
		}
	
		//dispose all frames open so far
		for(Frame frame:Frame.getFrames()) {
			frame.dispose();
		}
		
		EDatabase.removePool();
		
		//start the app again
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Frame ee = new Elegance();
		ee.setLocation(screenSize.width / 2 - 40, screenSize.height / 2 - 20);
		ee.setVisible(true);		
	}
	
	
/*
	private void showContin_ActionPerformed(ActionEvent e) {
		if (changeContinFrame == null) {
			
		} else {
			changeContinFrame.dispose();
		}

		changeContinFrame = new ChangeContinFrame();		 
		changeContinFrame.pack();
		changeContinFrame.setVisible(true);
		
		changeContinFrame.getOKButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (changeContinFrame.ok_actionPerformed(e)) { 
					viewRefreshMenu_actionPerformed(null);
				} 
			}
		});
		

	}
	*/

	private void viewMultPresynapticRelMenu_actionPerformed(ActionEvent e) {
		JInternalFrame jif = desktop.getSelectedFrame();

		if (jif == null) {
			return;
		}

		if (viewMultPresynapticRelMenu.isSelected()) {
			if (ApplicationProperties.showObjects) {
				ApplicationProperties.showAllRelations = true;
			}

			ApplicationProperties.showMultPresynapticRelations = true;
		} else {
			ApplicationProperties.showMultPostSynapticRelations = false;
			viewAllRelMenu.setSelected(false);
		}

		desktop.repaint();
	}

	private void viewMultPostsynapticRelMenu_actionPerformed(ActionEvent e) {
		JInternalFrame jif = desktop.getSelectedFrame();

		if (jif == null) {
			return;
		}

		if (viewMultPostsynapticRelMenu.isSelected()) {
			if (ApplicationProperties.showObjects) {
				ApplicationProperties.showAllRelations = true;
			}

			ApplicationProperties.showMultPostSynapticRelations = true;
		} else {
			ApplicationProperties.showMultPostSynapticRelations = false;
			viewAllRelMenu.setSelected(false);
		}

		desktop.repaint();
	}

	private void viewAllObjectsWindowMenu_actionPerformed(ActionEvent e) {
		if (allObjectsFrame == null) {
			allObjectsFrame = new AllObjectsFrame();
			allObjectsFrame.setTitle("All Objects");
			allObjectsFrame.jTable1.setModel(new AllObjectsTableModel(getObjectsData()));
			allObjectsFrame.refreshButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					allObjectsWindowRefresh(e);
				}
			});
			allObjectsFrame.jTable1.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					allObjectsTable_keyPressed(e);
				}
			});
			allObjectsFrame.jTable1.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent me) {
					allObjectsTable_mouseClicked(me);
				}
			});
		}
		allObjectsFrame.setVisible(true);

	}

	private ObjectFrame getSelectedObjectFrame() {
		if (selectedObjectFrame == null) {
			selectedObjectFrame = new ObjectFrame();
			selectedObjectFrame.refreshButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					selectedObjectWindowRefresh(e);
				}
			});
		}
		return selectedObjectFrame;
	}

	private void viewSelectedObjectWindowMenu_actionPerformed(ActionEvent e) {

		JInternalFrame jif = desktop.getSelectedFrame();

		if (jif != null) {
			ImageDisplayPanel idp = ((ImageDisplayFrame) jif).getImageDisplayPanel();
			JTable objTable = idp.getObjectTable();
			objTable.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					selectedObjectTable_keyPressed(e);
				}
			});

			getSelectedObjectFrame().resetTable(objTable, idp.getImageNumber());
			// oFrame.setTitle("Objects - " + idp.getImageNumber());
		} else {
			getSelectedObjectFrame().setTitle("Objects");
		}

	}

	private void synSearchMenu_actionPerformed(ActionEvent e) {
		JInternalFrame[] jifs = desktop.getAllFrames();
		ELog.info(jifs.length);
		Vector listOfUncertainSyn = new Vector(), listOfUserUncertainSyn = new Vector();
		for (int i = 0; i < jifs.length; i++) {
			Vector ob = ((ImageDisplayFrame) jifs[i]).getImageDisplayPanel().listOfObjects;
			ELog.info(ob.size());
			for (int j = 0; j < ob.size(); j++) {

				String type = ((CellObject) ob.elementAt(j)).type;
				if (type.equals("chemical") || type.equals("electrical")) {
					String certainty = ((CellObject) ob.elementAt(j)).certainty;

					ELog.info(certainty);
					String objN = ((CellObject) ob.elementAt(j)).objectName;

					if (certainty.equals("uncertain")) {
						listOfUncertainSyn.add(objN);
						ELog.info(((CellObject) ob.elementAt(j)).objectName);
					} else if (isPartnerUncertain(objN)) {
						listOfUncertainSyn.add(objN);
					}

					if (isUserUncertain(objN))
						listOfUserUncertainSyn.add(objN);

				}
			}
		}

		SynapseSearchFrame ssframe = new SynapseSearchFrame(listOfUncertainSyn, listOfUserUncertainSyn, username, this);
		ssframe.setVisible(true);
	}

	private void synFindMenu_actionPerformed(ActionEvent e) {
		
		Integer objName;
		if ((objName=EString.getObjectNameFromUser("Enter object number to locate"))==null) return;
		

		searchSyn(objName+"");

	}

	boolean isObjectInDatabase(String name) {
		Connection con = null;
		try {
			con = EDatabase.borrowConnection();

			PreparedStatement pst = con.prepareStatement("select OBJ_Name from object where OBJ_Name = ?");

			pst.setString(1, name);

			ResultSet rs = pst.executeQuery();

			if (rs.next()) {
				rs.close();
				pst.close();
				
				return true;

			} else {
				rs.close();
				pst.close();
				
				return false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

		
			return false;

		}finally {
			EDatabase.returnConnection(con);
		}
	}

	boolean isImageInDatabase(String name) {
		Connection con = null;
		try {
			con = EDatabase.borrowConnection();

			PreparedStatement pst = con.prepareStatement("select IMG_Number from object where IMG_Number = ?");

			pst.setString(1, name);

			ResultSet rs = pst.executeQuery();

			if (rs.next()) {
				rs.close();
				pst.close();
				
				return true;

			} else {
				rs.close();
				pst.close();
				
				return false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			
			return false;

		}finally {
			EDatabase.returnConnection(con);
		}
	}

	void creatFrameByImageNumber(String name) {
		if (isImageInDatabase(name)) {
			String filename1;
			try {
				filename1 = ImageDB.getDirectoryByImgNum(name) + File.separator + ImageDB.getFileByImgNum(name);
				createFrame(filename1, name);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			JOptionPane.showMessageDialog(this, name + " is not in database", "Error", JOptionPane.ERROR_MESSAGE);
		}

	}

	void searchSyn(String idx) {
		if (isObjectInDatabase(idx)) {
			CellObject obj = new CellObject(idx);
			String img3 = obj.imageNumber;
			try {

				String img1 = "", img2 = "", img4 = "", img5 = "";

				img2 = ImageDB.getPrevImgNum(img3);
				if (isImageInDatabase(img2))
					img1 = ImageDB.getPrevImgNum(img2);

				img4 = ImageDB.getNextImgNum(img3);
				;
				if (isImageInDatabase(img4))
					img5 = ImageDB.getPrevImgNum(img4);

				creatFrameByImageNumber(img1);
				creatFrameByImageNumber(img2);
				creatFrameByImageNumber(img3);
				creatFrameByImageNumber(img4);
				creatFrameByImageNumber(img5);

				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				int w = (int) (screenSize.width / 3.02);
				int h = (int) (screenSize.height * 0.8);
				JInternalFrame[] frameM = desktop.getAllFrames();
				int n = frameM.length;
				for (int i = 0; i < frameM.length; i++) {
					if (frameM[i].isSelected())
						frameM[i].setSelected(false);
					if (((ImageDisplayFrame) frameM[i]).getImageNumber().equals(img3)) {

						frameM[i].setLocation(fwidth, 0);
						frameM[i].setSelected(true);
						((ImageDisplayFrame) frameM[i]).getImageDisplayPanel().zoomAndRotate((float) 1.0, (float) 0.0);
						((ImageDisplayFrame) frameM[i]).getImageDisplayPanel().setOrigin((-obj.p.x + w / 2), -obj.p.y + h / 2);

						((ImageDisplayFrame) frameM[i]).getImageDisplayPanel().setSelectedPoint(obj.p);
						(((ImageDisplayFrame) frameM[i]).getImageDisplayPanel()).paintSelectedPoint((Graphics2D) ((ImageDisplayFrame) frameM[i])
								.getImageDisplayPanel().getGraphics());
						SynapseViewFrame vsframe = new SynapseViewFrame(Integer.parseInt(idx), username,
								((ImageDisplayFrame) frameM[i]).getImageDisplayPanel(), "view");
						vsframe.setVisible(true);

					}

					if (((ImageDisplayFrame) frameM[i]).getImageNumber().equals(img2)) {

						frameM[i].setLocation(0, 0);
						frameM[i].setSelected(true);
						((ImageDisplayFrame) frameM[i]).getImageDisplayPanel().zoomAndRotate((float) 1.0, (float) 0.0);
						((ImageDisplayFrame) frameM[i]).getImageDisplayPanel().setOrigin((-obj.p.x + w / 2), -obj.p.y + h / 2);

					}

					if (((ImageDisplayFrame) frameM[i]).getImageNumber().equals(img4)) {

						frameM[i].setLocation(fwidth * 2, 0);
						frameM[i].setSelected(true);
						((ImageDisplayFrame) frameM[i]).getImageDisplayPanel().zoomAndRotate((float) 1.0, (float) 0.0);
						((ImageDisplayFrame) frameM[i]).getImageDisplayPanel().setOrigin((-obj.p.x + w / 2), -obj.p.y + h / 2);

					}

				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			JOptionPane.showMessageDialog(this, idx + " is not in database", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	boolean isPartnerUncertain(String objN) {
		Connection con = null;

		try {
			con = EDatabase.borrowConnection();

			PreparedStatement pst = con.prepareStatement("select certainty from synrecord where synID = ?");
			pst.setString(1, objN);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				String certainty = rs.getString("certainty");
				if (certainty.equals("uncertain")) {
					con.close();
					return true;
				}
			}

			
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

			
		} finally {
			EDatabase.returnConnection(con);
		}
		return false;
	}

	boolean isUserUncertain(String objN) {
		Connection con = null;

		try {
			con = EDatabase.borrowConnection();

			PreparedStatement pst = con.prepareStatement("select sum(partner) from synrecord where synID = ? group by username");
			pst.setString(1, objN);
			ResultSet rs = pst.executeQuery();
			rs.last();
			int count = rs.getRow();
			if (count <= 1)
				return false;
			int[] sum = new int[count];
			rs.beforeFirst();
			int i = 0;
			while (rs.next()) {
				sum[i] = rs.getInt(1);
				i++;
			}

			for (int j = 0; j < count; j++) {
				if (sum[j] != sum[0]) {
					con.close();
					return true;
				}
			}

			
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			
		}finally {
			EDatabase.returnConnection(con);
		}
		return false;
	}

	void findSyn(String idx) {
		CellObject obj = new CellObject(idx);
		String img = obj.imageNumber;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int w = (int) (screenSize.width / 3.02);
		int h = (int) (screenSize.height * 0.8);
		JInternalFrame[] frameM = desktop.getAllFrames();
		int n = frameM.length;
		for (int i = 0; i < frameM.length; i++) {
			try {
				if (frameM[i].isSelected())
					frameM[i].setSelected(false);
				if (((ImageDisplayFrame) frameM[i]).getImageNumber().equals(obj.imageNumber)) {

					frameM[i].setLocation(0, 0);
					frameM[i].setSelected(true);
					((ImageDisplayFrame) frameM[i]).getImageDisplayPanel().zoomAndRotate((float) 1.0, (float) 0.0);
					((ImageDisplayFrame) frameM[i]).getImageDisplayPanel().setOrigin((-obj.p.x + w / 2), -obj.p.y + h / 2);

					((ImageDisplayFrame) frameM[i]).getImageDisplayPanel().setSelectedPoint(obj.p);
					(((ImageDisplayFrame) frameM[i]).getImageDisplayPanel()).paintSelectedPoint((Graphics2D) ((ImageDisplayFrame) frameM[i])
							.getImageDisplayPanel().getGraphics());
					SynapseViewFrame vsframe = new SynapseViewFrame(Integer.parseInt(idx), username, ((ImageDisplayFrame) frameM[i]).getImageDisplayPanel(),
							"view");
					vsframe.setVisible(true);

				}
			} catch (java.beans.PropertyVetoException e1) {
			}

		}

	}

	private void viewRelationWindowMenu_actionPerformed(ActionEvent e) {
		if (rFrame == null) {
			rFrame = new RelationFrame();
		}

		rFrame.updateAndDisplayFrame();
	}

	private void allObjectsTable_mouseClicked(MouseEvent me) {
		Point clickPoint = me.getPoint();
		int rowNo = allObjectsFrame.jTable1.rowAtPoint(clickPoint);
		// int colNo = table.columnAtPoint(clickPoint);
		if (rowNo >= 0) {
			String imageNoIDP = (String) allObjectsFrame.jTable1.getValueAt(rowNo, 0);
			int x = ((Integer) allObjectsFrame.jTable1.getValueAt(rowNo, 2)).intValue();
			int y = ((Integer) allObjectsFrame.jTable1.getValueAt(rowNo, 3)).intValue();
			Point p = new Point(x, y);
			JInternalFrame[] jifs = desktop.getAllFrames();
			for (int i = 0; i < jifs.length; i++) {
				ImageDisplayPanel idp = ((ImageDisplayFrame) jifs[i]).getImageDisplayPanel();
				if (imageNoIDP.compareToIgnoreCase(idp.getImageNumber()) == 0) {
					idp.selectPoint(p);
					idp.repaint();
				}
			}
			desktop.repaint();
		}
	}

	private void allObjectsTable_keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_DELETE) {
			int row = allObjectsFrame.jTable1.getSelectedRow();

			if (row != -1) {
				String imageNoIDP = (String) allObjectsFrame.jTable1.getValueAt(row, 0);
				int x = ((Integer) allObjectsFrame.jTable1.getValueAt(row, 2)).intValue();
				int y = ((Integer) allObjectsFrame.jTable1.getValueAt(row, 3)).intValue();
				Point p = new Point(x, y);

				JInternalFrame[] jifs = desktop.getAllFrames();
				for (int i = 0; i < jifs.length; i++) {
					ImageDisplayPanel idp = ((ImageDisplayFrame) jifs[i]).getImageDisplayPanel();
					if (imageNoIDP.compareToIgnoreCase(idp.getImageNumber()) == 0) {
						idp.removePoint(p);
						idp.repaint();
					}
				}
				allObjectsFrame.dispose();
				allObjectsFrame = null;
				viewAllObjectsWindowMenu_actionPerformed(null);
				desktop.repaint();
			}
		}
	}

	private void selectedObjectTable_keyPressed(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_DELETE) {
			if (selectedObjectFrame == null) {
				selectedObjectFrame = new ObjectFrame();
				selectedObjectFrame.refreshButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						selectedObjectWindowRefresh(e);
					}
				});
			}

			JInternalFrame jif = desktop.getSelectedFrame();

			if (jif != null) {
				ImageDisplayPanel idp = ((ImageDisplayFrame) jif).getImageDisplayPanel();
				JTable objTable = idp.getObjectTable();
				objTable.addKeyListener(new java.awt.event.KeyAdapter() {
					public void keyPressed(KeyEvent e) {
						selectedObjectTable_keyPressed(e);
					}
				});
				selectedObjectFrame.resetTable(objTable, idp.getImageNumber());
				// oFrame.setTitle("Objects - " + idp.getImageNumber());
			} else {
				selectedObjectFrame.setTitle("Objects");
			}
			desktop.repaint();
		}
	}

	class MyDesktopManager extends DefaultDesktopManager {
		public void activateFrame(JInternalFrame f) {
			super.activateFrame(f);

			if (menuImageLock.isSelected() == false) {
				synchronizeSliders();
			}
			ImageDisplayFrame idf = (ImageDisplayFrame) f;
			if (desktop.layers.contains(idf.imageNumber)) {
				desktop.layers.remove(idf.imageNumber);
			}
			desktop.layers.addElement(idf.imageNumber);
			desktop.repaint();
		}

		public void closeFrame(JInternalFrame f) {

			ImageDisplayPanel idp = ((ImageDisplayFrame) f).getImageDisplayPanel();
			idp.removeRelations();
			if (menuImageLock.isSelected()) {
				menuImageLock.setSelected(false);
				menuImageLock_actionPerformed();
			}

			if (idp.isSaved() == false) {

				idp.saveImageData();
				// idp.saveOverlay ( );

			}
			ELog.info("Before freeing memory used by image :"+ELog.getMemoryStats());

			idp.freeMemory();// in cas ethere's a memory leack and someone still holds to idp 
			idp = null;
			
			System.gc();	    	
			ELog.info("After freeing memory used by image :"+ELog.getMemoryStats());
			
			desktop.repaint();
			super.closeFrame(f);
			/**
			 * int selection = JOptionPane.showConfirmDialog ( null, //
			 * "The overlay/image properties of this image have changed. Do you want to save them before you exit?"
			 * ,
			 * "Do you want to save the image/overlay properties before you exit?"
			 * , "Save Properties?", JOptionPane.YES_NO_CANCEL_OPTION );
			 * 
			 * if (selection != JOptionPane.CANCEL_OPTION) { idp.removeRelations
			 * ( ); if ( menuImageLock.isSelected ( ) ) {
			 * menuImageLock.setSelected ( false );
			 * menuImageLock_actionPerformed ( ); }
			 * 
			 * if ( idp.isSaved ( ) == false ) { if ( selection ==
			 * JOptionPane.YES_OPTION ) { idp.saveImageData ( );
			 * //idp.saveOverlay ( ); } }
			 * 
			 * idp = null; //Util.info("idp is now null");
			 * //desktop.remove(f); //f.dispose();
			 * //Util.info("frame disposed"); desktop.repaint ( );
			 * super.closeFrame(f); } if (selection ==
			 * JOptionPane.CANCEL_OPTION) {
			 * 
			 * }
			 **/
		}

		public void deactivateFrame(JInternalFrame f) {
			// Util.info("frame deactivated " +
			// ((ImageDisplayFrame)f).getImageNumber());
			String str = ((ImageDisplayFrame) f).getImageNumber();
		}

		public void endDraggingFrame(JInternalFrame f) {
			super.endDraggingFrame(f);
			desktop.repaint();
		}

		public void endResizingFrame(JComponent f) {
			super.endResizingFrame(f);
			desktop.repaint();
		}

		public void iconifyFrame(JInternalFrame f) {
			super.iconifyFrame(f);
			desktop.repaint();
		}
	}

	private void menuUserManual_actionPerformed(ActionEvent e) {
		try {
			Process p = Runtime.getRuntime().exec("..\\docs\\manual.pdf");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void allObjectsWindowRefresh(ActionEvent e) {
		if (allObjectsFrame != null) {
			allObjectsFrame.dispose();
			allObjectsFrame = null;
		}

		viewAllObjectsWindowMenu_actionPerformed(e);
		/*
		 * JInternalFrame jif = desktop.getSelectedFrame ( ); if ( jif != null )
		 * { ImageDisplayPanel idp = ( ( ImageDisplayFrame ) jif
		 * ).getImageDisplayPanel ( ); idp.saveOverlay(); JTable objTable =
		 * idp.getObjectTable ( ); objTable.addKeyListener ( new
		 * java.awt.event.KeyAdapter( ) { public void keyPressed ( KeyEvent e )
		 * { objectTable_keyPressed ( e ); } } ); objTable.addMouseListener (
		 * new MouseAdapter( ) { public void mouseClicked(MouseEvent me) {
		 * objectTable_mouseClicked(me); } } ); oFrame.resetTable ( objTable,
		 * idp.getImageNumber() ); }
		 */
	}

	private void selectedObjectWindowRefresh(ActionEvent e) {
		if (selectedObjectFrame != null) {
			selectedObjectFrame.dispose();
			selectedObjectFrame = null;
		}

		viewSelectedObjectWindowMenu_actionPerformed(e);

	}

	private void menuResetSlider_actionPerformed(ActionEvent e) {
		zoomSlider.setValue(60);
		brightnessSlider.setValue(0);
		contrastSlider.setValue(0);
		rotationSlider.setValue(0);
	}

	private void showHideObjects_actionPerformed(ActionEvent e) {
		if (!showObjects) {
			viewRefreshMenu_actionPerformed(e);
			showObjects = true;
		} else {
			JInternalFrame[] jifs = desktop.getAllFrames();
			ImageDisplayPanel.cleanRelationsData();
			for (int i = 0; i < jifs.length; i++) {
				ImageDisplayFrame idf = (ImageDisplayFrame) jifs[i];
				idf.getImageDisplayPanel().cleanObjectsData();

			}

			desktop.repaint();
			showObjects = false;
		}

	}

	public void viewRefreshMenu_actionPerformed(ActionEvent e) {
		// menuSaveAllObjects_actionPerformed(e);
		// menuSaveRelationships_actionPerformed(e);
		JInternalFrame[] jifs = desktop.getAllFrames();
		ImageDisplayPanel.cleanRelationsData();
		for (int i = 0; i < jifs.length; i++) {
			ImageDisplayFrame idf = (ImageDisplayFrame) jifs[i];
			idf.getImageDisplayPanel().cleanObjectsData();

			idf.getImageDisplayPanel().addListOfObjectsAndPoints(idf.getImageNumber());
			idf.getImageDisplayPanel().addRelations();

		}
		/*
		 * if(oFrame != null && oFrame.isShowing()) { objectWindowRefresh(e); }
		 */
		if (allObjectsFrame != null && allObjectsFrame.isShowing()) {
			allObjectsWindowRefresh(e);
		}
		if (rFrame != null && rFrame.isShowing()) {
			rFrame.updateAndDisplayFrame();
		}
		this.setTitle(getEleganceTitle());
				
		desktop.repaint();
	}

	
	private String getEleganceTitle() {
		String result="Elegance      database: " + Elegance.db + "    username: " + username + " host: " + Elegance.host + "  Show Contin#: ";
		switch (Elegance.filterOptions.getContinFilterType()) {
			case all: result=result+"all";break;
			case none: result=result+"none";break;
			case custom_number:result=result+EString.join(Elegance.filterOptions.getContinFilterCustom(), ",");break;
			default: result=result+EString.join(Elegance.filterOptions.getContinNums(),",")+" ("+EString.join(Elegance.filterOptions.getContinFilterCustom(), ",");
		}
		return result + " )";
	}
	
	private void menuImageModify_actionPerformed(ActionEvent e) {
		String imgNoToModify = JOptionPane.showInputDialog("Enter the image number that you want modify");
		if (imgNoToModify != null && imgNoToModify.compareTo("") != 0) {
			if (Utilities.verifyImageNumberExistance(imgNoToModify) == true) {
				imFrame = new ImageModifyFrame(imgNoToModify);
				imFrame.setVisible(true);
			} else {
				JOptionPane.showMessageDialog(null, "The image number was not found in the database", "Not Found", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	// add load next image by Meng

	private void menuImageLoadNext_actionPerformed(ActionEvent e) {
		try {
			JInternalFrame[] frameM = desktop.getAllFrames();
			String cImgNumber = "", ccImgNumber = "", nextImgNum, nextDirectory, nextFileName;

			int cPrintNum = 0;
			for (int i = 0; i < frameM.length; i++) {
				if (frameM[i].isSelected()) {
					cImgNumber = ((ImageDisplayFrame) frameM[i]).getImageNumber();
					cPrintNum = ImageDB.getPrintNumByImgNum(cImgNumber);
				}
			}

			for (int i = 0; i < frameM.length; i++) {
				int ccPrintNum = ImageDB.getPrintNumByImgNum(((ImageDisplayFrame) frameM[i]).getImageNumber());
				if (ccPrintNum < cPrintNum) {
					// close this frame
					cDManager.closeFrame(frameM[i]);
				}

			}
			ccImgNumber = cImgNumber;
			// JOptionPane.showMessageDialog(null, ccImgNumber, "selected IMG",
			// JOptionPane.ERROR_MESSAGE);
			for (int i = 0; i < 10; i++) {
				nextImgNum = ImageDB.getNextImgNum(ccImgNumber);
				nextDirectory = ImageDB.getDirectoryByImgNum(nextImgNum);
				nextFileName = ImageDB.getFileByImgNum(nextImgNum);
				createFrame(nextDirectory + File.separator + nextFileName, nextImgNum);
				ccImgNumber = nextImgNum;
			}

			JInternalFrame[] frameS = getSortedFrame();
			frameS[2].setSelected(true);
			frameS[1].setSelected(true);
			frameS[0].setSelected(true);

			ELog.info("load images!!!");
			addKeyListener(this);
			// *end of Meng

		} catch (OutOfMemoryError ex) {
			// Util.info("caught error");
			// ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "The available memory is not sufficient to carry out this operation", "Error", JOptionPane.ERROR_MESSAGE);
		} catch (Exception exx) {
			// Util.info("caught exception");
			// ex.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"An error has occurred.\nThis may be because the available memory is not sufficient to carry out this operation", "Error",
					JOptionPane.ERROR_MESSAGE);
		}

		desktop.repaint();
	}

	// add load prev image by Meng

	private void menuImageLoadPrev_actionPerformed(ActionEvent e) {
		try {
			JInternalFrame[] frameM = desktop.getAllFrames();
			String cImgNumber = "", ccImgNumber = "", prevImgNum, prevDirectory, prevFileName;
			int cPrintNum = 0;
			for (int i = 0; i < frameM.length; i++) {
				if (frameM[i].isSelected()) {
					cImgNumber = ((ImageDisplayFrame) frameM[i]).getImageNumber();
					cPrintNum = ImageDB.getPrintNumByImgNum(cImgNumber);
				}
			}

			for (int i = 0; i < frameM.length; i++) {
				int ccPrintNum = ImageDB.getPrintNumByImgNum(((ImageDisplayFrame) frameM[i]).getImageNumber());
				if (ccPrintNum > cPrintNum) {
					// close this frame
					cDManager.closeFrame(frameM[i]);
				}

			}

			// JOptionPane.showMessageDialog(null, cImgNumber, "selected IMG",
			// JOptionPane.ERROR_MESSAGE);
			ccImgNumber = cImgNumber;
			for (int i = 0; i < 10; i++) {
				prevImgNum = ImageDB.getPrevImgNum(ccImgNumber);
				prevDirectory = ImageDB.getDirectoryByImgNum(prevImgNum);
				prevFileName = ImageDB.getFileByImgNum(prevImgNum);
				// JOptionPane.showMessageDialog(null,
				// prevImgNum+"   "+prevDirectory+"     "+prevFileName,
				// "prev IMG", JOptionPane.ERROR_MESSAGE);
				createFrame(prevDirectory + File.separator + prevFileName, prevImgNum);
				ccImgNumber = prevImgNum;
			}

			JInternalFrame[] frameS = getSortedFrame();
			frameS[2].setSelected(true);
			frameS[1].setSelected(true);
			frameS[0].setSelected(true);

			ELog.info("load images!!!");
			addKeyListener(this);
			// *end of Meng

		} catch (OutOfMemoryError ex) {
			// Util.info("caught error");
			// ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "The available memory is not sufficient to carry out this operation", "Error", JOptionPane.ERROR_MESSAGE);
		} catch (Exception exx) {
			// Util.info("caught exception");
			// ex.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"An error has occurred.\nThis may be because the available memory is not sufficient to carry out this operation", "Error",
					JOptionPane.ERROR_MESSAGE);
		}

		desktop.repaint();
	}

	// add alignment function by Meng
	private void menuImageAlignment_actionPerformed(ActionEvent e) {

		JInternalFrame frameS[] = getSortedFrame();
		int xx = 0, yy = 0;
		float zzoom = 0;
		for (int i = 0; i < frameS.length; i++) {
			if (frameS[i].isSelected()) {
				xx = ((ImageDisplayFrame) frameS[i]).getImageDisplayPanel().getXOrigin();
				yy = ((ImageDisplayFrame) frameS[i]).getImageDisplayPanel().getYOrigin();
				zzoom = ((ImageDisplayFrame) frameS[i]).getImageDisplayPanel().getZoom();

			}
		}

		for (int i = 0; i < frameS.length; i++) {

			((ImageDisplayFrame) frameS[i]).getImageDisplayPanel().zoomAndRotate(zzoom, 0.0f);
			((ImageDisplayFrame) frameS[i]).getImageDisplayPanel().setOrigin(xx, yy);

		}
		desktop.repaint();
	}

	private void viewContinsMenu_actionPerformed(ActionEvent e) {
		if (cFrame == null) {
			cFrame = new ContinFrame();
		}

		cFrame.setVisible(true);
	}

	private void displayOptions_actionPerformed(ActionEvent e) {
		if (filterFrame == null) {
			
		} else {
			filterFrame.dispose();
			
		}

		filterFrame = new FilterFrame();		 
		filterFrame.pack();
		filterFrame.setVisible(true);
		
		filterFrame.getOKButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				filterFrame.ok_actionPerformed(e);
				viewRefreshMenu_actionPerformed(null);
			}
		});
				
	}
	
	private void metalLookMenuItem_actionPerformed(ActionEvent e) {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
			SwingUtilities.updateComponentTreeUI(this);
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "Unsupported Look And Feel", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void motifLookMenuItem_actionPerformed(ActionEvent e) {
		try {
			// UIManager.setLookAndFeel("javax.swing.plaf.multi.MultiLookAndFeel");
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
			SwingUtilities.updateComponentTreeUI(this);

		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "Unsupported Look And Feel", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void systemLookMenuItem_actionPerformed(ActionEvent e) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.updateComponentTreeUI(this);
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "Unsupported Look And Feel", "Error", JOptionPane.ERROR_MESSAGE);
		}

	}

	private Vector getObjectsData() {
		Connection con = null;
		try {
			con = EDatabase.borrowConnection();

			//getObjects
			

			Set<String> images = new HashSet<String>();
			
			Vector returner = new Vector();
			JInternalFrame[] jifs = desktop.getAllFrames();
			
			for (int i = 0; i < jifs.length; i++) {
				if (jifs[i].isClosed())
					continue;
				ImageDisplayPanel idp = ((ImageDisplayFrame) jifs[i]).getImageDisplayPanel();
				Vector listOfPoints = idp.getListOfPoints();
				String currentImageNumber = idp.getImageNumber();
				for (int j = 0; j < listOfPoints.size(); j++) {
					
					images.add(currentImageNumber);
				}
				
			}
			
			Map<EDatabase.XYKey, CellObject> objects=EDatabase.getObjectsByImages(images);
			
			
			for (int i = 0; i < jifs.length; i++) {
				if (jifs[i].isClosed())
					continue;
				ImageDisplayPanel idp = ((ImageDisplayFrame) jifs[i]).getImageDisplayPanel();
				Vector listOfPoints = idp.getListOfPoints();
				String currentImageNumber = idp.getImageNumber();
				for (int j = 0; j < listOfPoints.size(); j++) {
					
					Point pt=(Point) listOfPoints.elementAt(j);
					CellObject obj = objects.get(new EDatabase.XYKey(currentImageNumber,pt.x, pt.y));
					
					if (obj==null) continue;
					
					Vector row = new Vector();
					row.addElement(obj.imageNumber);
					row.addElement(obj.objectName);
					row.addElement(new Integer(obj.p.x));
					row.addElement(new Integer(obj.p.y));
					row.addElement(obj.getContin().getName());
					row.addElement(obj.getRemarks());
					returner.addElement(row);
				}
			}
			return returner;
		} catch (Throwable e) {
			ELog.info("cant get object data " + e);
			throw new IllegalStateException("cant get object data", e);
		} finally {
			EDatabase.returnConnection(con);
		}
	}

	public String showcontin(String c) {
		/**
		 * String str; if(Elegance.contin.equals("all")) { return "all"; } else
		 * { String[] wc = Elegance.contin.split(",");
		 * 
		 * for (int i=0;i<wc.length;i++) {
		 * 
		 * wcontin[i]=Integer.parseInt(wc[i]); } }
		 * 
		 * return str;
		 **/
		return c;
	}

}
