import java.awt.AWTEvent;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ContainerEvent;
import java.awt.event.InputMethodEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.TextHitInfo;
import java.awt.im.InputMethodRequests;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.AttributedCharacterIterator;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoundedRangeModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

/**
 * The ImageSearchFrame class is the class using which the searching for images
 * is done.
 * 
 * @author zndavid
 * @version 1.0
 */
public class ImageSearchFrame extends JFrame {
	private String[] queryStrings = { "whose Image ID", "whose Image Number", "whose File Name", "whose Directory name", "whose Worm Name", "whose Series",
			"whose Print Number", "whose Negative Number", "whose Section Number", "where the user's name", "where the image entry date" };
	private String[] equalsStrings = { "approximately matches", "exactly matches", "is greater than", "is lesser than" };
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private JLabel selectLabel = new JLabel();
	private JComboBox where1 = new JComboBox(new String[] { queryStrings[1], queryStrings[2], queryStrings[3], queryStrings[4], queryStrings[5],
			queryStrings[6], queryStrings[7], queryStrings[8], queryStrings[9], queryStrings[10] });
	private JComboBox equals1 = new JComboBox(equalsStrings);
	private JTextField tbox1 = new JTextField();
	private JComboBox andor = new JComboBox();
	private JComboBox where2 = new JComboBox(new String[] { queryStrings[1], queryStrings[2], queryStrings[3], queryStrings[4], queryStrings[5],
			queryStrings[6], queryStrings[7], queryStrings[8], queryStrings[9], queryStrings[10] });
	private JComboBox equals2 = new JComboBox(equalsStrings);
	private JTextField tbox2 = new JTextField();
	private JButton ok = new JButton();
	private JButton close = new JButton();
	private JButton back = new JButton("Back");
	private JButton load = new JButton("Load");
	private JScrollPane spane;
	private JTable table = new JTable();

	public void setFocus() {
		tbox1.setCaretPosition(0);
		tbox1.requestFocus();
		tbox1.requestFocusInWindow();
	}

	/**
	 * Creates a new ImageSearchFrame object.
	 */
	public ImageSearchFrame() {
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception {
		this.getContentPane().setLayout(gridBagLayout1);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.setSize(new Dimension(700, 250));
		this.setTitle("Search");
		selectLabel.setText("Find the image(s)");
		ok.setText("OK");
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ok_actionPerformed(e);
			}
		});
		close.setText("Close");
		close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				close_actionPerformed(e);
			}
		});
		back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				back_actionPerformed(e);
			}
		});
		andor.addItem("AND");
		andor.addItem("OR");
		this.getContentPane().add(selectLabel,
				new GridBagConstraints(0, 0, 3, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.getContentPane().add(where1,
				new GridBagConstraints(0, 1, 1, 1, 0.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 15, 0, 0), 0, 0));
		this.getContentPane().add(equals1,
				new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		this.getContentPane().add(tbox1,
				new GridBagConstraints(2, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 15), 0, 0));

		this.getContentPane().add(andor,
				new GridBagConstraints(0, 2, 3, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.getContentPane().add(where2,
				new GridBagConstraints(0, 3, 1, 1, 0.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 15, 0, 0), 0, 0));
		this.getContentPane().add(equals2,
				new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.getContentPane().add(tbox2,
				new GridBagConstraints(2, 3, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 15), 0, 0));
		this.getContentPane().add(ok,
				new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.getContentPane().add(close,
				new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 15, 0));

	}

	private static class TestKL implements KeyListener {

		public void keyTyped(KeyEvent e) {
			ELog.info("key typed " + e);
		}

		public void keyPressed(KeyEvent e) {
			ELog.info("key pressed " + e);
		}

		public void keyReleased(KeyEvent e) {
			ELog.info("key released " + e);
		}

	}

	

	// Action for our key binding to perform when bound event occurs
	private class EnterAction extends AbstractAction {
		private BoundedRangeModel vScrollBarModel;
		private int scrollableIncrement;

		public EnterAction(String name, BoundedRangeModel model, int scrollableIncrement) {
			super(name);
			this.vScrollBarModel = model;
			this.scrollableIncrement = scrollableIncrement;
		}

		@Override
		public void actionPerformed(ActionEvent ae) {
			String name = getValue(AbstractAction.NAME).toString();
			ELog.info("BBB " + ae);

		}
	}

	private static final String ENTER = "Enter";

	/**
	 * Hits the database and fetches the search results if any
	 * 
	 * @return 1 if the search was succesful. A negative number if the search
	 *         was unsuccesful.
	 */
	public int performSearch() {
		ImageDB imgdb = new ImageDB();
		String sqlString = "";
		boolean doubleQuery = false;
		Vector tableData = new Vector();

		if ((tbox1.getText() == null) || (tbox1.getText().length() == 0)) {
			JOptionPane.showMessageDialog(null, "Please enter the search field", "Error", JOptionPane.ERROR_MESSAGE);

			return -1;
		} else {
			sqlString = generateSQL();

			// Util.info("sql = " + sqlString);
			if ((sqlString == null) || (sqlString.compareTo("") == 0)) {
				return -1;
			}
		}

		Connection con = null;

		try {

			con = EDatabase.borrowConnection();

			PreparedStatement pst = con.prepareStatement(sqlString);
			ResultSet rs = pst.executeQuery();

			while (rs.next()) {
				Vector v = new Vector();
				v.addElement(rs.getString("IMG_Number"));
				v.addElement(rs.getString("IMG_File"));
				v.addElement(rs.getString("IMG_Directory"));
				v.addElement(rs.getString("IMG_Worm"));
				v.addElement(rs.getString("IMG_Series"));
				v.addElement(rs.getString("IMG_PrintNumber"));
				v.addElement(rs.getString("IMG_NegativeNumber"));
				v.addElement(rs.getString("IMG_SectionNumber"));
				v.addElement(rs.getString("IMG_EnteredBy"));
				v.addElement(rs.getString("IMG_DateEntered"));
				tableData.addElement(v);
			}

		
		} catch (Exception ex) {
			ex.printStackTrace();

			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

			
			return -1;
		} finally {
			EDatabase.returnConnection(con);
		}

		Vector tableHeaders = new Vector();

		tableHeaders.addElement("Image Number");
		tableHeaders.addElement("File Name");
		tableHeaders.addElement("Directory");
		tableHeaders.addElement("Worm");
		tableHeaders.addElement("Series");
		tableHeaders.addElement("Print Number");
		tableHeaders.addElement("Negative Number");
		tableHeaders.addElement("Section Number");
		tableHeaders.addElement("Entered By");
		tableHeaders.addElement("Date Entered");

		MyTableModel model = new MyTableModel(tableData, tableHeaders);

		table.setModel(model);
		spane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		
		this.getContentPane().removeAll();
		this.getContentPane().setLayout(new GridBagLayout());
		// this.getContentPane ( ).addKeyListener(new TestKL());

		this.getContentPane().add(spane,
				new GridBagConstraints(0, 0, 3, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		this.getContentPane().add(back,
				new GridBagConstraints(0, 1, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.getContentPane().add(load,
				new GridBagConstraints(1, 1, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		this.getContentPane().add(close,
				new GridBagConstraints(2, 1, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 15, 0));

		// GridBagLayout gridBag1 = new GridBagLayout();
		// panel3.setLayout( gridBag1 );
		// c.fill = GridBagConstraints.NONE;
		// c.weightx = 1.0;
		// c.weighty = 1.0;
		// c.gridwidth = 1;
		// c.anchor = GridBagConstraints.CENTER;
		// gridBag1.setConstraints( back, c );
		// panel3.add( back );
		// c.gridwidth = GridBagConstraints.REMAINDER;
		// gridBag1.setConstraints( close, c );
		// panel3.add( close );
		// getContentPane().setLayout( gridBag );
		// c.fill = GridBagConstraints.BOTH;
		// c.weightx = 1.0;
		// c.weighty = 1.0;
		// c.gridwidth = GridBagConstraints.REMAINDER;
		// c.anchor = GridBagConstraints.CENTER;
		// gridBag.setConstraints( spane, c );
		// getContentPane().add( spane );
		// c.fill = GridBagConstraints.NONE;
		// c.weightx = 0.0;
		// c.weighty = 0.0;
		// gridBag.setConstraints( panel3, c );
		// getContentPane().add( panel3 );
		repaint();
		setVisible(true);

		return 1;
	}

	public Action pressedEnterAction = new AbstractAction("pressed ENTER") {
		public void actionPerformed(ActionEvent evt) {
			ELog.info("AAAA " + evt);
		}
	};

	private void close_actionPerformed(ActionEvent e) {
		this.hide();
	}

	private void ok_actionPerformed(ActionEvent e) {
		performSearch();
	}

	private void back_actionPerformed(ActionEvent e) {
		this.getContentPane().removeAll();
		this.getContentPane().setLayout(new GridBagLayout());
		this.getContentPane().add(selectLabel,
				new GridBagConstraints(0, 0, 3, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.getContentPane().add(where1,
				new GridBagConstraints(0, 1, 1, 1, 0.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 15, 0, 0), 0, 0));
		this.getContentPane().add(equals1,
				new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.getContentPane().add(tbox1,
				new GridBagConstraints(2, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 15), 0, 0));
		this.getContentPane().add(andor,
				new GridBagConstraints(0, 2, 3, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.getContentPane().add(where2,
				new GridBagConstraints(0, 3, 1, 1, 0.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 15, 0, 0), 0, 0));
		this.getContentPane().add(equals2,
				new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.getContentPane().add(tbox2,
				new GridBagConstraints(2, 3, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 15), 0, 0));
		this.getContentPane().add(ok,
				new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.getContentPane().add(close,
				new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 15, 0));

		this.repaint();
	}

	/**
	 * Returns the load button.
	 * 
	 * @return the load button
	 */
	public JButton getLoadButton() {
		return load;
	}

	/**
	 * Returns the table in which the results are displayed in.
	 * 
	 * @return The table in which the results are displayed in.
	 */
	public JTable getTable() {
		return table;
	}

	/**
	 * Based on the options chosen by the user, this function generates an sql
	 * query corresponding to the choices that the user has made.
	 * 
	 * @return The sql query. In case of an error "" is returned.
	 */
	public String generateSQL() {
		try {
			String returner = "select ID, IMG_Number, IMG_File, IMG_Directory, IMG_Worm, IMG_Series, IMG_PrintNumber, IMG_NegativeNumber, IMG_SectionNumber, IMG_EnteredBy, IMG_DateEntered from image  where ";
			returner += getWhereString1();

			String str = tbox2.getText();

			if ((str != null) && ("".compareTo(str) != 0)) {
				returner += getAndOrString();
				returner += getWhereString2();
			}
			returner += "order by IMG_SectionNumber";
			return returner;
		} catch (Exception ex) {
			ex.printStackTrace();

			return "";
		}
	}

	private String getAndOrString() {
		return andor.getSelectedItem().toString() + " ";
	}

	private String getWhereString1() throws NumberFormatException {
		String str1 = (String) where1.getSelectedItem();
		String str = tbox1.getText();

		if (str1.equals(queryStrings[0])) {
			try {
				if (getEqualString1().compareTo("LIKE ") == 0) {
					return "ID " + getEqualString1() + "'%" + Integer.parseInt(str) + "%' ";
				} else {
					return "ID " + getEqualString1() + Integer.parseInt(str) + " ";
				}
			} catch (NumberFormatException nfe) {
				throw new NumberFormatException("The value provided for '" + str + "' is not a number. Please recheck.");
			}
		} else if (str1.equals(queryStrings[1])) {
			if (getEqualString1().compareTo("LIKE ") == 0) {
				return "IMG_Number " + getEqualString1() + "'%" + str + "%' ";
			} else {
				return "IMG_Number " + getEqualString1() + "'" + str + "' ";
			}
		} else if (str1.equals(queryStrings[2])) {
			if (getEqualString1().compareTo("LIKE ") == 0) {
				return "IMG_File " + getEqualString1() + "'%" + str + "%' ";
			} else {
				return "IMG_File " + getEqualString1() + "'" + str + "' ";
			}
		} else if (str1.equals(queryStrings[3])) {
			if (getEqualString1().compareTo("LIKE ") == 0) {
				return "IMG_Directory " + getEqualString1() + "'%" + str + "%' ";
			} else {
				return "IMG_Directory " + getEqualString1() + "'" + str + "' ";
			}
		} else if (str1.equals(queryStrings[4])) {
			if (getEqualString1().compareTo("LIKE ") == 0) {
				return "IMG_Worm " + getEqualString1() + "'%" + str + "%' ";
			} else {
				return "IMG_Worm " + getEqualString1() + "'" + str + "' ";
			}
		} else if (str1.equals(queryStrings[5])) {
			if (getEqualString1().compareTo("LIKE ") == 0) {
				return "IMG_Series " + getEqualString1() + "'%" + str + "%' ";
			} else {
				return "IMG_Series " + getEqualString1() + "'" + str + "' ";
			}
		} else if (str1.equals(queryStrings[6])) {
			if (getEqualString1().compareTo("LIKE ") == 0) {
				return "IMG_PrintNumber " + getEqualString1() + "'%" + str + "%' ";
			} else {
				return "IMG_PrintNumber " + getEqualString1() + "'" + str + "' ";
			}
		} else if (str1.equals(queryStrings[7])) {
			if (getEqualString1().compareTo("LIKE ") == 0) {
				return "IMG_NegativeNumber " + getEqualString1() + "'%" + str + "%' ";
			} else {
				return "IMG_NegativeNumber " + getEqualString1() + "'" + str + "' ";
			}
		} else if (str1.equals(queryStrings[8])) {
			try {
				if (getEqualString1().compareTo("LIKE ") == 0) {
					return "IMG_SectionNumber " + getEqualString1() + "'%" + Integer.parseInt(str) + "%' ";
				} else {
					return "IMG_SectionNumber " + getEqualString1() + Integer.parseInt(str) + " ";
				}
			} catch (NumberFormatException nfe) {
				throw new NumberFormatException("The value provided for '" + str + "' is not a number. Please recheck.");
			}

			/*
			 * if ( getEqualString1 ( ).compareTo ( "LIKE " ) == 0 ) { return
			 * "IMG_SectionNumber " + getEqualString1 ( ) + "'%" + str + "%' ";
			 * } else { return "IMG_SectionNumber " + getEqualString1 ( ) + "'"
			 * + str + "' "; }
			 */
		} else if (str1.equals(queryStrings[9])) {
			if (getEqualString1().compareTo("LIKE ") == 0) {
				return "IMG_EnteredBy " + getEqualString1() + "'%" + str + "%' ";
			} else {
				return "IMG_EnteredBy " + getEqualString1() + "'" + str + "' ";
			}
		} else if (str1.equals(queryStrings[10])) {
			// java.util.Date = java.text.SimpleDateFormat(str, new
			// java.text.ParsePosition(0));
			// java.sql.Date
			// imgdb.dateEntered = java.sql.Date.valueOf( str );
			java.text.SimpleDateFormat format = new java.text.SimpleDateFormat();

			format.applyPattern("yyyy-MM-dd");

			// java.util.Date date = format.parse( str1, new
			// java.text.ParsePosition( 0 ) );
			// if ( date == null )
			// {
			// JOptionPane.showMessageDialog( null,
			// "Enter the date in the format dd/MM/yyyy", "Error",
			// JOptionPane.ERROR_MESSAGE );
			// }
			try {
				java.sql.Date date = new java.sql.Date((format.parse(str, new java.text.ParsePosition(0))).getTime());

				if (getEqualString1().compareTo("LIKE ") == 0) {
					return "IMG_DateEntered " + getEqualString1() + "'%" + date + "%' ";
				} else {
					return "IMG_DateEntered " + getEqualString1() + "'" + date + "' ";
				}
			} catch (NullPointerException npe) {
				// JOptionPane.showMessageDialog( null,
				// "Enter the date in the format dd/MM/yyyy", "Error",
				// JOptionPane.ERROR_MESSAGE );
				npe.printStackTrace();
				throw new NumberFormatException("The value provided for '" + str + "' is not a valid date.\n Please enter the date in the format yyyy-MM-dd");
			}

			// Util.info( str1 );
		} else {
			return "";
		}
	}

	private String getWhereString2() throws NumberFormatException {
		String str1 = (String) where2.getSelectedItem();
		String str = tbox2.getText();

		if (str1.equals(queryStrings[0])) {
			try {
				if (getEqualString2().compareTo("LIKE ") == 0) {
					return "ID " + getEqualString2() + "'%" + Integer.parseInt(str) + "%' ";
				} else {
					return "ID " + getEqualString2() + Integer.parseInt(str) + " ";
				}
			} catch (NumberFormatException nfe) {
				throw new NumberFormatException("The value provided for '" + str + "' is not a number. Please recheck.");
			}
		} else if (str1.equals(queryStrings[1])) {
			if (getEqualString2().compareTo("LIKE ") == 0) {
				return "IMG_Number " + getEqualString2() + "'%" + str + "%' ";
			} else {
				return "IMG_Number " + getEqualString2() + "'" + str + "' ";
			}
		} else if (str1.equals(queryStrings[2])) {
			if (getEqualString2().compareTo("LIKE ") == 0) {
				return "IMG_File " + getEqualString2() + "'%" + str + "%' ";
			} else {
				return "IMG_File " + getEqualString2() + "'" + str + "' ";
			}
		} else if (str1.equals(queryStrings[3])) {
			if (getEqualString2().compareTo("LIKE ") == 0) {
				return "IMG_Directory " + getEqualString2() + "'%" + str + "%' ";
			} else {
				return "IMG_Directory " + getEqualString2() + "'" + str + "' ";
			}
		} else if (str1.equals(queryStrings[4])) {
			if (getEqualString2().compareTo("LIKE ") == 0) {
				return "IMG_Worm " + getEqualString2() + "'%" + str + "%' ";
			} else {
				return "IMG_Worm " + getEqualString2() + "'" + str + "' ";
			}
		} else if (str1.equals(queryStrings[5])) {
			if (getEqualString2().compareTo("LIKE ") == 0) {
				return "IMG_Series " + getEqualString2() + "'%" + str + "%' ";
			} else {
				return "IMG_Series " + getEqualString2() + "'" + str + "' ";
			}
		} else if (str1.equals(queryStrings[6])) {
			if (getEqualString2().compareTo("LIKE ") == 0) {
				return "IMG_PrintNumber " + getEqualString2() + "'%" + str + "%' ";
			} else {
				return "IMG_PrintNumber " + getEqualString2() + "'" + str + "' ";
			}
		} else if (str1.equals(queryStrings[7])) {
			if (getEqualString2().compareTo("LIKE ") == 0) {
				return "IMG_NegativeNumber " + getEqualString2() + "'%" + str + "%' ";
			} else {
				return "IMG_NegativeNumber " + getEqualString2() + "'" + str + "' ";
			}
		} else if (str1.equals(queryStrings[8])) {
			if (getEqualString2().compareTo("LIKE ") == 0) {
				return "IMG_SectionNumber " + getEqualString2() + "'%" + str + "%' ";
			} else {
				return "IMG_SectionNumber " + getEqualString2() + "'" + str + "' ";
			}
		} else if (str1.equals(queryStrings[9])) {
			if (getEqualString2().compareTo("LIKE ") == 0) {
				return "IMG_EnteredBy " + getEqualString2() + "'%" + str + "%' ";
			} else {
				return "IMG_EnteredBy " + getEqualString2() + "'" + str + "' ";
			}
		} else if (str1.equals(queryStrings[10])) {
			// java.util.Date = java.text.SimpleDateFormat(str, new
			// java.text.ParsePosition(0));
			// java.sql.Date
			// imgdb.dateEntered = java.sql.Date.valueOf( str );
			java.text.SimpleDateFormat format = new java.text.SimpleDateFormat();

			format.applyPattern("yyyy-MM-dd");

			// java.util.Date date = format.parse( str1, new
			// java.text.ParsePosition( 0 ) );
			// if ( date == null )
			// {
			// JOptionPane.showMessageDialog( null,
			// "Enter the date in the format dd/MM/yyyy", "Error",
			// JOptionPane.ERROR_MESSAGE );
			// }
			try {
				java.sql.Date date = new java.sql.Date((format.parse(str, new java.text.ParsePosition(0))).getTime());

				if (getEqualString2().compareTo("LIKE ") == 0) {
					return "IMG_DateEntered " + getEqualString2() + "'%" + date + "%' ";
				} else {
					return "IMG_DateEntered " + getEqualString2() + "'" + date + "' ";
				}
			} catch (NullPointerException npe) {
				// JOptionPane.showMessageDialog( null,
				// "Enter the date in the format dd/MM/yyyy", "Error",
				// JOptionPane.ERROR_MESSAGE );
				throw new NumberFormatException("The value provided for '" + str + "' is not a valid date.\n Please enter the date in the format yyyy-MM-dd");
			}

			// Util.info( str1 );
		} else {
			return "";
		}
	}

	private String getEqualString1() {
		String str = equals1.getSelectedItem().toString();

		if (str.compareTo(equalsStrings[0]) == 0) {
			return "LIKE ";
		}

		if (str.compareTo(equalsStrings[1]) == 0) {
			return "= ";
		}

		if (str.compareTo(equalsStrings[2]) == 0) {
			return "> ";
		}

		if (str.compareTo(equalsStrings[3]) == 0) {
			return "< ";
		} else {
			return "= ";
		}
	}

	private String getEqualString2() {
		String str = equals2.getSelectedItem().toString();

		if (str.compareTo(equalsStrings[0]) == 0) {
			return "LIKE ";
		}

		if (str.compareTo(equalsStrings[1]) == 0) {
			return "= ";
		}

		if (str.compareTo(equalsStrings[2]) == 0) {
			return "> ";
		}

		if (str.compareTo(equalsStrings[3]) == 0) {
			return "< ";
		} else {
			return "= ";
		}
	}
}
