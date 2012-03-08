import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;


public class Elegance extends JFrame {
	
	static
	{
	System.setProperty("com.sun.media.jai.disableMediaLib", "true");
	}
	
	public static String username;
	public static String db;
	public static String host;
	public static String dbusername;
	public static String dbpassword;
	// public static String contin;
	public static int[] contintoshow;
	private String usernames;
	private String dbs;

	public static final FilterOptions filterOptions = new FilterOptions();

	public static final Display2DOptions display2DOptions = new Display2DOptions();

	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private JLabel UsernameLabel = new JLabel("Username");
	private JLabel ContinLabel = new JLabel("Show contin#");
	private JTextField Contin = new JTextField("all", 6);

	private JLabel ContinLabel2 = new JLabel("Description: leave empty to show all the connectivity lines,0 show none, or any contin number to show");
	private JComboBox userslist;
	private JLabel DatabaseLabel = new JLabel("Database");
	private JComboBox dbslist;
	private JButton GoButton = new JButton("continue");

	// private JLabel ArchiveLabel = new JLabel("Database Archive");
	// private JComboBox archivelist;
	// private JButton LoadButton = new JButton("Load");

	private JLabel Edit = new JLabel("If you want to add/remove user or dabase, please edit configure.txt file");

	public Elegance() {

		loadProperties();

		// loadArchivelist();

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		setTitle("Log in");
		setSize(300, 350);
		JPanel p = new JPanel();
		p.setSize(300, 350);
		p.setLayout(gridBagLayout1);
		JScrollPane scrollpane = new JScrollPane(p);
		getContentPane().add(scrollpane, BorderLayout.CENTER);

		String userList[] = usernames.split(",");
		userslist = new JComboBox(userList);
		String dbList[] = dbs.split(",");
		dbslist = new JComboBox(dbList);
		// String fileList[] = filenames.split(",");
		// archivelist = new JComboBox(fileList);

		GoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GoButton_actionPerformed(e);
			}
		});

		KeyListener enterKeyListener = new KeyListener() {

			public void keyTyped(KeyEvent e) {
			}

			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					GoButton_actionPerformed(e);
				}

			}

			public void keyReleased(KeyEvent e) {

			}

		};

		userslist.addKeyListener(enterKeyListener);
		dbslist.addKeyListener(enterKeyListener);
		Contin.addKeyListener(enterKeyListener);
		GoButton.addKeyListener(enterKeyListener);

		/*
		 * LoadButton.addActionListener(new ActionListener() { public void
		 * actionPerformed(ActionEvent e) { LoadButton_actionPerformed(e); } });
		 */

		p.add(UsernameLabel, new GridBagConstraints(0, 0, 3, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 20, 0, 0), 0, 0));
		p.add(userslist, new GridBagConstraints(3, 0, 3, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 20, 0, 0), 0, 0));
		p.add(DatabaseLabel, new GridBagConstraints(0, 1, 3, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 20, 0, 0), 0, 0));
		p.add(dbslist, new GridBagConstraints(3, 1, 3, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 20, 0, 0), 0, 0));
		p.add(ContinLabel, new GridBagConstraints(0, 2, 3, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 20, 0, 0), 0, 0));
		p.add(Contin, new GridBagConstraints(3, 2, 3, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 20, 0, 0), 0, 0));

		p.add(GoButton, new GridBagConstraints(3, 3, 3, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 20, 0, 0), 0, 0));

		// p.add (
		// ArchiveLabel,
		// new GridBagConstraints(0, 4, 3, 1, 1.0, 1.0,
		// GridBagConstraints.WEST, GridBagConstraints.NONE,
		// new Insets(0, 20, 0, 0), 0, 0));
		// p.add (
		// archivelist,
		// new GridBagConstraints(3, 4, 3, 1, 1.0, 1.0,
		// GridBagConstraints.WEST, GridBagConstraints.NONE,
		// new Insets(0, 20, 0, 0), 0, 0));
		// p.add (
		// LoadButton,
		// new GridBagConstraints(5, 4, 3, 1, 1.0, 1.0,
		// GridBagConstraints.WEST, GridBagConstraints.NONE,
		// new Insets(0, 20, 0, 0), 0, 0));

	}

	private void GoButton_actionPerformed(AWTEvent e) {
		Elegance.username = (String) userslist.getSelectedItem();
		Elegance.db = (String) dbslist.getSelectedItem();

		/*
		 * try{ Connection con = Util.getConnection(
		 * DatabaseProperties.CONNECTION+"mysql", ); Statement st =
		 * con.createStatement();
		 * 
		 * st.executeUpdate("CREATE DATABASE IF NOT EXISTS "+db);
		 * 
		 * // Execute a command without arguments String command =
		 * "mysql -h "+host
		 * +" -u "+dbusername+" -p"+dbpassword+" "+db+" < C:structure.sql";
		 * Util.info(command); Process child =
		 * Runtime.getRuntime().exec(command);
		 * 
		 * } catch ( Exception ee) { ee.printStackTrace ( ); }
		 */
		String contin = (String) Contin.getText().trim();

		if (contin == null || "".equals(contin) || contin.equalsIgnoreCase("all")) {
			filterOptions.setContinFilterType(FilterOptions.ContinFilterType.all);			
		} else if (contin.equalsIgnoreCase("none")) {
			filterOptions.setContinFilterType(FilterOptions.ContinFilterType.none);
		} else {
			filterOptions.setContinFilterType(FilterOptions.ContinFilterType.custom_number);
			Set<String> continStrings = new HashSet<String>();
			List<Integer> continNumbers = new ArrayList<Integer>();
			List<String> continNotNumbers = new ArrayList<String>();
			for (String c : EString.extract(contin)) {
				try {
					int conNumber = Integer.parseInt(c);
					continStrings.add(c);
					continNumbers.add(conNumber);
				} catch (Throwable e1) {
					continNotNumbers.add(c);
				}
			}

			filterOptions.setContinFilterCustom(continStrings);
			filterOptions.setContinNums(continNumbers.toArray(new Integer[] {}));

			filterOptions.setHideAll(false);
			
			if (!continNotNumbers.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Only integers are accepted as contin numbers, so the following were ignored: " + continNotNumbers
						+ ". Use Crtl-D shortcut to update contins.");
			}

			
		}

		Frame frame = new MainFrame();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = frame.getSize();

		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height - 40;
		}

		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}

		frame.setLocation(0, 0);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		frame.setVisible(true);
		this.dispose();
	}

	/*
	 * private void LoadButton_actionPerformed(ActionEvent e) { try {
	 * Class.forName ( "com.mysql.jdbc.Driver" ).newInstance ( ); Connection con
	 * = Util.getConnection ( "jdbc:mysql://"+host+"/"+db, dbusername,
	 * dbpassword ); String jsql; PreparedStatement pstmt; ResultSet rs; pstmt =
	 * con.prepareStatement("drop database tempDB"); pstmt.executeQuery(); pstmt
	 * = con.prepareStatement("create database tempDB"); pstmt.executeQuery();
	 * pstmt.close(); String loaddb = ( String ) archivelist.getSelectedItem();
	 * String command =
	 * "mysql -h "+host+" -u "+dbusername+" -p"+dbpassword+" tempDB < "
	 * +loaddb+".sql"; Util.info(command); Process child =
	 * Runtime.getRuntime().exec(command);
	 * 
	 * } catch ( Exception ee ) { ee.printStackTrace ( );
	 * 
	 * } username = ( String ) userslist.getSelectedItem ( ); db = "tempDB";
	 * contin = (String) Contin.getText(); if (contin==null ) contin = "";
	 * 
	 * 
	 * Frame frame = new MainFrame(); Dimension screenSize =
	 * Toolkit.getDefaultToolkit ( ).getScreenSize ( ); Dimension frameSize =
	 * frame.getSize ( );
	 * 
	 * if ( frameSize.height > screenSize.height ) { frameSize.height =
	 * screenSize.height-40; }
	 * 
	 * if ( frameSize.width > screenSize.width ) { frameSize.width =
	 * screenSize.width; }
	 * 
	 * frame.setLocation (0,0 ); frame.addWindowListener ( new WindowAdapter( )
	 * { public void windowClosing ( WindowEvent e ) { System.exit ( 0 ); } } );
	 * frame.setVisible ( true ); this.dispose(); }
	 */

	public static void restart() {
		try {
			final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
			final File currentJar = new File(Elegance.class.getProtectionDomain().getCodeSource().getLocation().toURI());
			String classpath = System.getProperty("java.class.path");
			classpath = classpath + System.getProperty("path.separator") + currentJar;

			/* Build command: java -cp classpath */
			final ArrayList<String> command = new ArrayList<String>();
			command.add(javaBin);
			command.add("-server");

			command.add("-Xmx" + ((int) (Runtime.getRuntime().maxMemory() / (1024 * 1024))) + "M");
			command.add("-cp");
			command.add(classpath);
			command.add("Elegance");
			command.add("background");

			// command.add("2>&1");
			command.add(">" + System.getProperty("java.io.tmpdir") + File.separator + "elegance.log");

			ELog.info("Restarting the app with " + EString.join(command, " ") + "command");

			final ProcessBuilder builder = new ProcessBuilder(command);
			builder.redirectErrorStream(true);

			builder.start();

			System.exit(0);

		} catch (Throwable e) {
			throw new IllegalStateException("cant restart", e);
		}
	}

	public static void main(String[] args) {
		
		if (false && (args == null || args.length == 0 || !args[0].equals("background"))) {
			File outFile = new File("elegance.log");
			ELog.info("Will log to " + outFile.getAbsolutePath() + " file");
			try {
				
				PrintStream outStream = new PrintStream(new FileOutputStream(outFile));
				System.setOut(outStream);
				System.setErr(outStream);

			} catch (Throwable e) {
				throw new IllegalStateException("Cannot init logging " + outFile.getAbsolutePath(), e);
			}

			
			restart();
			return;// unreachable
		}

		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "The mysql driver was not found", "Error", JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
		}
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Frame e = new Elegance();
		e.setLocation(screenSize.width / 2 - 40, screenSize.height / 2 - 20);
		e.setVisible(true);

	}

	/*
	 * public void loadArchivelist() { filenames=""; try { Class.forName (
	 * "com.mysql.jdbc.Driver" ).newInstance ( ); Connection con =
	 * Util.getConnection ( "jdbc:mysql://"+host+"/db1", dbusername, dbpassword
	 * ); String jsql; PreparedStatement pstmt; ResultSet rs; pstmt =
	 * con.prepareStatement("select filename from backup"); pstmt.setString(1,
	 * username); pstmt.setString(2, db); rs = pstmt.executeQuery();
	 * if(rs.next())filenames=rs.getString(1); while(rs.next()) {
	 * filenames=filenames+","+rs.getString(1);
	 * 
	 * }
	 * 
	 * pstmt.close();
	 * 
	 * } catch ( Exception e ) { e.printStackTrace ( );
	 * 
	 * 
	 * } }
	 */

	public void loadProperties() {

		try {
			File file = new File("configuration.txt");
			if (file.exists()) {
				BufferedReader in = new BufferedReader(new FileReader(file));
				String s = in.readLine();

				while (s.startsWith("#")) {
					s = in.readLine();
				}

				usernames = s;
				s = in.readLine();

				while (s.startsWith("#")) {
					s = in.readLine();
				}

				dbs = s;
				s = in.readLine();

				while (s.startsWith("#")) {
					s = in.readLine();
				}

				host = s;
				s = in.readLine();

				while (s.startsWith("#")) {
					s = in.readLine();
				}

				dbusername = s;
				s = in.readLine();

				while (s.startsWith("#")) {
					s = in.readLine();
				}
				dbpassword = s;
				s = in.readLine();

				in.close();

			} else {

				ELog.info("Could not find the file 'configuration.txt' continuing with default values...");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	
}
