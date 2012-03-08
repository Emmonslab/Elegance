import java.io.File;
import java.io.FileWriter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * The utilities class
 * 
 * @author zndavid
 * @version 1.0
 */
public class Utilities {
	private Utilities() {
	}

	/**
	 * Gets a file name from the user to save a file into.
	 * 
	 * @param defaultFileName
	 *            the default file name. pass "" if there is not default file
	 *            name.
	 * 
	 * @return The user selected file name.
	 */
	public static String getFileNameFromUser(String defaultFileName) {
		JFileChooser chooser = new JFileChooser();
		chooser.setSelectedFile(new File(defaultFileName));

		int returnVal = chooser.showSaveDialog(null);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();

			if (!file.exists()) {
				return file.getAbsolutePath();
			} else {
				if (JOptionPane.showConfirmDialog(null, "File already exists.\n Do you want to overwrite?", "Confirm Overwrite", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					return file.getAbsolutePath();
				} else {
					JOptionPane.showMessageDialog(null, "File will not be saved", "Not saved", JOptionPane.INFORMATION_MESSAGE);

					return "";
				}
			}
		} else {
			JOptionPane.showMessageDialog(null, "File will not be saved", "Not saved", JOptionPane.INFORMATION_MESSAGE);

			return "";
		}
	}

	/**
	 * Gets a file from the user for reading.
	 * 
	 * @param defaultFileName
	 *            the default file name. Pass "" if there is no default file
	 *            name.
	 * 
	 * @return The file name chosen by the user.
	 */
	public static String getFileNameFromUserForRead(String defaultFileName) {
		JFileChooser chooser = new JFileChooser();
		chooser.setSelectedFile(new File(defaultFileName));
		chooser.setApproveButtonText("OK");

		int returnVal = chooser.showOpenDialog(null);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();

			if (file.exists()) {
				return file.getAbsolutePath();
			} else {
				JOptionPane.showMessageDialog(null, "The file selected does not exist.\n Please select an existing file", "File does not exist",
						JOptionPane.INFORMATION_MESSAGE);

				return "";
			}
		} else {
			return "";
		}
	}

	/**
	 * Generates a report into the specified filename in the format accepted by
	 * wormbase
	 * 
	 * @param fileName
	 *            The file name
	 */
	public static void generateReport(String fileName) {
		// Util.info("started to generate report with filename '" +
		// fileName + "'");
		if ("".compareTo(fileName) == 0) {
			return;
		}

		File file = new File(fileName);

		// if ( !( file.exists ( ) && file.canWrite ( ) ) )
		// {
		// Util.info("file doesnt");
		// return;
		// }
		try {
			FileWriter writer = new FileWriter(file);
			String str = getReportAsString();
			writer.write(str);
			writer.flush();
			writer.close();
			JOptionPane.showMessageDialog(null, "File saved", "Saved", JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "File could not be saved.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Gets the string to be use in generating the report.
	 * 
	 * @return the report string
	 * 
	 * @deprecated use Utilities#getReportAsString instead
	 */
	private static String getReportString() {
		String returnString = "Cell_A\tCell_B\tExperiment\tSend_#\tSend_joint_#\tReceive_#\tReceive_joint_#\tGap_junction_#\tContact_#\n";
		Connection con = null;

		try {
			Vector listOfContins = new Vector();
			con = EDatabase.borrowConnection();

			Statement stm = con.createStatement();
			ResultSet rs1 = stm.executeQuery("SELECT CON_Number FROM contin WHERE CON_Number != 0");

			while (rs1.next()) {
				listOfContins.addElement(new Integer(rs1.getInt("CON_Number")));
			}

			int numberOfContins = listOfContins.size();

			for (int i = 0; i < numberOfContins; i++) {
				for (int j = i; j < numberOfContins; j++) {
					returnString += getTabDelimitedString((Integer) listOfContins.elementAt(i), (Integer) listOfContins.elementAt(j));
				}
			}

		
		} catch (Exception ex) {
			ex.printStackTrace();
			returnString = "ERROR: " + ex;
		} finally {
			EDatabase.returnConnection(con);
		}

		return returnString;
	}

	private static String getReportAsString() {
		String returnString = "Cell_A\tCell_B\tExperiment\tSend_#\tSend_joint_#\tReceive_#\tReceive_joint_#\tGap_junction_#\tContact_#\n";
		Connection con = null;

		try {
			Vector listOfObjects = new Vector();
			con = EDatabase.borrowConnection();

			Statement stm = con.createStatement();
			ResultSet rs1 = stm.executeQuery("SELECT DISTINCT REL_ImgNumber1, REL_ObjName1, REL_ImgNumber2, REL_ObjName2 FROM relationship");

			while (rs1.next()) {
				CellObject obj = new CellObject(

				rs1.getString("REL_ObjName1"));

				if (listOfObjects.contains(obj) == false) {
					listOfObjects.addElement(obj);
				}

				obj = new CellObject(

				rs1.getString("REL_ObjName2"));

				if (listOfObjects.contains(obj) == false) {
					listOfObjects.addElement(obj);
				}
			}

			int numberOfObjects = listOfObjects.size();

			for (int i = 0; i < numberOfObjects; i++) {
				for (int j = i; j < numberOfObjects; j++) {
					ReportItem repitem = getReportItemFor((CellObject) listOfObjects.elementAt(i), (CellObject) listOfObjects.elementAt(j));

					if (repitem.getTotalConnections() > 0) {
						returnString += repitem.toString();
					}
				}
			}

		
		} catch (Exception ex) {
			ex.printStackTrace();
			returnString = "ERROR: " + ex;
		}finally {
			EDatabase.returnConnection(con);
		}

		return returnString;
	}

	private static String getTabDelimitedString(Integer contin1, Integer contin2) {
		String resultString = "";
		String contin1Name = getContinName(contin1);
		String contin2Name = getContinName(contin2);
		String experimentName = getExperimentName(contin1, contin2);
		int presynaptic = 0;
		int postsynaptic = 0;
		int multiplePresynaptic = 0;
		int multiplePostsynaptic = 0;
		int gap = 0;
		int contact = 0;
		Connection con = null;

		try {
			con = EDatabase.borrowConnection();

			Statement stm = con.createStatement();

			// Util.info("Select REL_Type from Object, Relationship where Relationship.CON_Number = "
			// + contin1
			// +
			// " AND Relationship.REL_Object = Object.OBJ_Name AND Object.CON_Number = "
			// + contin2);
			ResultSet rs = stm.executeQuery("Select REL_Type from object, relationship where relationship.CON_Number = " + contin1
					+ " AND relationship.REL_Object = object.OBJ_Name AND object.CON_Number = " + contin2);

			// ResultSet rs =
			// stm.executeQuery (
			// "Select REL_Type from Object, Relationship where Relationship.REL_ConNumber1 = "
			// + contin1
			// +
			// " AND Relationship.REL_ObjName2 = Object.OBJ_Name AND Object.CON_Number = "
			// + contin2
			// );
			while (rs.next()) {
				String type = rs.getString("REL_Type");

				if (type.compareToIgnoreCase(GlobalStrings.PRESYNAPTIC) == 0) {
					presynaptic++;
				} else if (type.compareToIgnoreCase(GlobalStrings.POSTSYNAPTIC) == 0) {
					postsynaptic++;
				} else if (type.compareToIgnoreCase(GlobalStrings.MULTIPLE_PRESYNAPTIC) == 0) {
					multiplePresynaptic++;
				} else if (type.compareToIgnoreCase(GlobalStrings.MULTIPLE_POSTSYNAPTIC) == 0) {
					multiplePostsynaptic++;
				} else if (type.compareToIgnoreCase(GlobalStrings.GAP_JUNCTION) == 0) {
					gap++;
				} else if (type.compareToIgnoreCase(GlobalStrings.UNCHARECTERIZED) == 0) {
					contact++;
				}
			}

			stm = con.createStatement();
			rs = stm.executeQuery("Select REL_Type from object, relationship where relationship.CON_Number = " + contin2
					+ " AND relationship.REL_Object = object.OBJ_Name AND object.CON_Number = " + contin1);

			// rs =
			// stm.executeQuery (
			// "Select REL_Type from Object, Relationship where Relationship.REL_ConNumber1 = "
			// + contin2
			// +
			// " AND Relationship.REL_ObjName2 = Object.OBJ_Name AND Object.CON_Number = "
			// + contin1
			// );
			while (rs.next()) {
				String type = rs.getString("REL_Type");

				if (type.compareToIgnoreCase(GlobalStrings.PRESYNAPTIC) == 0) {
					postsynaptic++;
				} else if (type.compareToIgnoreCase(GlobalStrings.POSTSYNAPTIC) == 0) {
					presynaptic++;
				} else if (type.compareToIgnoreCase(GlobalStrings.MULTIPLE_PRESYNAPTIC) == 0) {
					multiplePostsynaptic++;
				} else if (type.compareToIgnoreCase(GlobalStrings.MULTIPLE_POSTSYNAPTIC) == 0) {
					multiplePresynaptic++;
				} else if (type.compareToIgnoreCase(GlobalStrings.GAP_JUNCTION) == 0) {
					gap++;
				} else if (type.compareToIgnoreCase(GlobalStrings.UNCHARECTERIZED) == 0) {
					contact++;
				}
			}

		
			if ((presynaptic + multiplePresynaptic + postsynaptic + multiplePostsynaptic + gap + contact) > 0) {
				resultString = contin1Name + "\t" + contin2Name + "\t" + experimentName + "\t" + presynaptic + "\t" + multiplePresynaptic + "\t" + postsynaptic
						+ "\t" + multiplePostsynaptic + "\t" + gap + "\t" + contact + "\n";
			} else {
				resultString = "";
			}

			// return resultString;
		} catch (Exception ex) {
			ex.printStackTrace();
			resultString = "Error: " + ex + "\n";
		} finally {
			EDatabase.returnConnection(con);
		}
		// Util.info("result is "+ resultString
		// +" at the current time");
		return resultString;
	}

	private static String getExperimentName(Integer contin1, Integer contin2) {
		Connection con = null;

		try {
			con = EDatabase.borrowConnection();

			Statement stm = con.createStatement();


			return "Albert Einstein";
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			EDatabase.returnConnection(con);
		}

		return "Error";
	}

	private static String getExperimentName() {
		return "Albert Einstein University";
	}

	/**
	 * Gets the contin name of a given contin number
	 * 
	 * @param number
	 *            The contin number.
	 * 
	 * @return Returns the contin name if any name has been spcified or the
	 *         number if there is no name specified. In case of an error the
	 *         error is returned in the format "Error: 'error message'"
	 */
	public static String getContinName(Integer number) {
		String returnString = "";
		Connection con = null;

		try {
			con = EDatabase.borrowConnection();

			Statement stm = con.createStatement();
			ResultSet rs = stm.executeQuery("Select CON_AlternateName FROM contin WHERE CON_Number = " + number);

			if (rs.next()) {
				returnString = rs.getString("CON_AlternateName");

				if (rs.wasNull() || returnString == null || returnString.compareTo("") == 0 || returnString.compareToIgnoreCase("null") == 0) {
					returnString = number.toString();
				}
			}

		
		} catch (Exception ex) {
			ex.printStackTrace();
			returnString = "Error: " + ex;
		} finally {
			EDatabase.returnConnection(con);
		}

		// Util.info("resultstring is " + returnString +
		// " at the current time");
		return returnString;
	}

	public static int getContinNum(String name) {
		int returnNum = 0;
		Connection con = null;

		try {
			con = EDatabase.borrowConnection();

			Statement stm = con.createStatement();
			ResultSet rs = stm.executeQuery("Select CON_Number FROM object WHERE OBJ_Name = " + name);

			if (rs.next()) {
				returnNum = rs.getInt("CON_Number");

				if (rs.wasNull()) {
					returnNum = 0;
				}
			}

		
		} catch (Exception ex) {
			ex.printStackTrace();
			returnNum = -1;
		} finally {
			EDatabase.returnConnection(con);
		}

		return returnNum;
	}

	/**
	 * Starts the image stitch application
	 */
	public static void startStitchApplication() {
		ImageStitchApp imageStitchApp = new ImageStitchApp();
		// imageStitchApp.run();
		Thread t = new Thread(imageStitchApp);
		t.start();
	}

	public static int getNewContinNumber() {
		Connection con = null;
		int num = 0;

		try {
			con = EDatabase.borrowConnection();

			Statement stat = con.createStatement();
			ResultSet rs = stat.executeQuery("select MAX(CON_Number) from contin");

			if (rs.next()) {
				num = rs.getInt(1);

			}

			

			return num + 1;
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

			
			return -1;
		}finally {
			EDatabase.returnConnection(con);
		}
	}

	/**
	 * Searches the database for a free contin number. Returns the free number
	 * of present or returns the largest contin umber +1 if there is no free
	 * contin number.
	 * 
	 * @return The generated free number.
	 */
	public static int generateContinNumber() {
		Connection con = null;

		try {
			con = EDatabase.borrowConnection();

			Statement stat = con.createStatement();
			ResultSet rs = stat.executeQuery("select distinct CON_Number from contin");
			Vector v = new Vector();
			int max = 0;

			while (rs.next()) {
				int num = rs.getInt("CON_Number");

				if (num > max) {
					max = num;
				}

				v.addElement(new Integer(num));
			}

		
			for (int i = 0; i < v.size(); i++) {
				if (v.contains(new Integer(i + 1)) == false) {
					return (i + 1);
				}
			}

			return max + 1;
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

			if (con != null) {
				con = null;
			}

			return -1;
		}finally {
			EDatabase.returnConnection(con);
		}
	}

	/**
	 * removes a contin and adds its properties to a new contin
	 * 
	 * @param contintoBeDeleted
	 *            The contin to be removed
	 * @param newContinNumber
	 *            The new contin
	 */
	public static void removeContinNumber(int contintoBeDeleted, int newContinNumber) {
		Connection con = null;

		try {
			con = EDatabase.borrowConnection();

			PreparedStatement pst = con.prepareStatement("select CON_AlternateName from contin where CON_Number = ?");
			pst.setInt(1, contintoBeDeleted);

			ResultSet rs = pst.executeQuery();
			String alternateName = "";

			if (rs.next()) {
				alternateName = rs.getString("CON_AlternateName");
			}

			pst.setInt(1, newContinNumber);
			rs = pst.executeQuery();

			String oldAlternateName = "";

			if (rs.next()) {
				oldAlternateName = rs.getString("CON_AlternateName");
			}

			String newAlternateName;

			if ((oldAlternateName == null) || (oldAlternateName.compareTo("") == 0) || (oldAlternateName.compareToIgnoreCase("null") == 0)) {
				newAlternateName = alternateName;
			} else {
				if (alternateName != null && alternateName.compareTo("") != 0 && alternateName.compareToIgnoreCase("null") != 0) {
					newAlternateName = oldAlternateName + ", " + alternateName;
				} else {
					newAlternateName = oldAlternateName;
				}
			}

			pst = con.prepareStatement("update contin set CON_ALternateName = ? where CON_Number = ?");
			pst.setString(1, newAlternateName);
			pst.setInt(2, newContinNumber);
			pst.executeUpdate();

			pst = con.prepareStatement("delete from contin where CON_Number = ?");
			pst.setInt(1, contintoBeDeleted);
			pst.executeUpdate();

		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

		}finally {
			EDatabase.returnConnection(con);
		}
	}

	/**
	 * Creates a new contin with the same alternate name as the old contin
	 * 
	 * @param oldContin
	 *            The old contin
	 * @param newContin
	 *            The new contin
	 * @param con
	 *            A Connection to the database.
	 */
	public static void createNewContin(int oldContin, int newContin, Connection con) {
		try {
			PreparedStatement pst = con.prepareStatement("select CON_AlternateName from contin where CON_Number = ?");
			pst.setInt(1, oldContin);

			ResultSet rs = pst.executeQuery();

			if (rs.next()) {
				String str = rs.getString("CON_AlternateName");
				pst = con.prepareStatement("insert into contin (CON_Number, CON_AlternateName) values (? , ?)");
				pst.setInt(1, newContin);
				pst.setString(2, str);
				pst.executeUpdate();

				// Util.info("created new contin");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Changes the contin number of all objects in the Relationshop table with
	 * the old contin number to the new contin number and changes the contin
	 * number of all the contins that it is related to to the new contin number.
	 * 
	 * @param objNam
	 *            The object name
	 * @param imageNum
	 *            The imagenumber
	 * @param oldContin
	 *            The old contin number
	 * @param newContin
	 *            The new contin number
	 * @param con
	 *            A connection to the database.
	 */
	public static void changeAllContinNumbers(String objNam, String imageNum, int oldContin, int newContin, Connection con) {
		try {
			PreparedStatement pst = con
					.prepareStatement("update relationship set REL_ConNumber1 = ?, REL_ConNumber2 = ? where ( REL_ObjName1 = ? AND REL_ImgNumber1 = ? AND REL_ConNumber1 = ? ) OR ( REL_ObjName2 = ? AND REL_ImgNumber2 = ? AND REL_ConNumber2 = ?)");

			// Util.info("update Relationship set REL_ConNumber1 = " +
			// newContin+ ", REL_ConNumber2 = "+ newContin+
			// " where ( REL_ObjName1 = "+objNam+" AND REL_ImgNumber1 = "+imageNum+" AND REL_ConNumber1 = "+oldContin+" ) OR ( REL_ObjName2 = "+objNam+" AND REL_ImgNumber2 = "+imageNum+" AND REL_ConNumber2 = "+oldContin+")");
			pst.setInt(1, newContin);
			pst.setInt(2, newContin);
			pst.setString(3, objNam);
			pst.setString(4, imageNum);
			pst.setInt(5, oldContin);
			pst.setString(6, objNam);
			pst.setString(7, imageNum);
			pst.setInt(8, oldContin);

			// Util.info(pst.getWarnings());
			pst.executeUpdate();
			pst = con
					.prepareStatement("SELECT REL_ObjName1, REL_ImgNumber1, REL_ConNumber1 FROM relationship WHERE REL_ObjName2 = ? AND REL_ImgNumber2 = ? AND REL_ConNumber2 = ?");
			pst.setString(1, objNam);
			pst.setString(2, imageNum);
			pst.setInt(3, oldContin);

			ResultSet rs = pst.executeQuery();

			while (rs.next()) {
				Utilities.changeAllContinNumbers(rs.getString("REL_ObjName1"), rs.getString("REL_ImgNumber1"), newContin, oldContin, con);
			}

			pst = con
					.prepareStatement("SELECT REL_ObjName2, REL_ImgNumber2, REL_ConNumber2 FROM relationship WHERE REL_ObjName1 = ? AND REL_ImgNumber1 = ? AND REL_ConNumber1 = ?");
			pst.setString(1, objNam);
			pst.setString(2, imageNum);
			pst.setInt(3, oldContin);
			rs = pst.executeQuery();

			while (rs.next()) {
				Utilities.changeAllContinNumbers(rs.getString("REL_ObjName2"), rs.getString("REL_ImgNumber2"), newContin, oldContin, con);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Gets all the objects that form the rest contin that has the contin number
	 * provided and the object provided.
	 * 
	 * @param obj
	 *            The object
	 * @param oldContin
	 *            The contin number.
	 * 
	 * @return A Vector containing the rest of the contin
	 */
	public static Vector getRestOfContin(CellObject obj, int oldContin) {
		Connection con = null;
		Vector returner = new Vector();

		try {
			con = EDatabase.borrowConnection(

			);

			PreparedStatement pst = con.prepareStatement("SELECT REL_ObjName1 FROM relationship WHERE ObjName2 = ?");
			pst.setString(1, obj.getObjName(con));

			ResultSet rs = pst.executeQuery();

			while (rs.next()) {
				CellObject obj2 = new CellObject(

				rs.getString("ObjName1"));
				Relation rel = new Relation(obj, obj2);
				returner.add(rel);
				returner.addAll(getRestOfContin(obj, obj2, oldContin));
			}

			pst = con.prepareStatement("SELECT ObjName2 FROM relationship WHERE ObjName1 = ?");
			pst.setString(1, obj.getObjName(con));

			rs = pst.executeQuery();

			while (rs.next()) {
				CellObject obj2 = new CellObject(

				rs.getString("ObjName2"));
				Relation rel = new Relation(obj, obj2);
				returner.add(rel);
				returner.addAll(getRestOfContin(obj, obj2, oldContin));
			}

			
		} catch (Exception ex) {
			ex.printStackTrace();

			
		}finally {
			EDatabase.returnConnection(con);
		}

		return returner;
	}

	/**
	 * Gets the rest of the contin with a given contin number that contains a
	 * givan object. The part if the contin after the excluded object is not
	 * returned
	 * 
	 * @param excludeObj
	 *            The object to be excluded
	 * @param obj
	 *            The object that is a part of the contin
	 * @param oldContin
	 *            The contin number
	 * 
	 * @return The rest of the contin as aa Vector of CellObject objects.
	 */
	public static Vector getRestOfContin(CellObject excludeObj, CellObject obj, int oldContin) {
		Connection con = null;
		Vector returner = new Vector();

		try {
			con = EDatabase.borrowConnection();

			// PreparedStatement pst =
			// con.prepareStatement (
			// "SELECT REL_ImgNumber1, REL_ObjName1, REL_Type FROM Relationship WHERE (REL_ObjName2 = ? AND REL_ImgNumber2 = ? AND REL_ConNumber2 = ?) AND NOT (REL_ObjName1 = ? AND REL_ImgNumber1 = ? AND REL_ConNumber1 = ?)"
			// );
			// pst.setString ( 1, obj.getObjName ( ) );
			// pst.setString ( 2, obj.imageNumber );
			// pst.setInt ( 3, oldContin );
			// pst.setString ( 4, excludeObj.getObjName ( ) );
			// pst.setString ( 5, excludeObj.imageNumber );
			// pst.setInt ( 6, oldContin );

			PreparedStatement pst = con.prepareStatement("SELECT ObjName1 FROM relationship WHERE (ObjName2 = ?) AND NOT (ObjName1 = ?)");
			pst.setString(1, obj.getObjName(con));

			// pst.setInt ( 3, oldContin );
			pst.setString(2, excludeObj.getObjName(con));

			// pst.setInt ( 6, oldContin );

			ResultSet rs = pst.executeQuery();

			while (rs.next()) {
				CellObject obj1 = new CellObject(

				rs.getString("ObjName1"));
				Relation rel = new Relation(obj1, obj);
				if (returner.contains(rel) == false) {
					returner.add(rel);
				}
				// Util.info("added " + rel);
				if (0 == 0) {
					// Vector anotherFragmentOfContin = getRestOfContin ( obj,
					// obj1, oldContin );
					Vector anotherFragmentOfContin = getRestOfContin(obj, obj1, oldContin);
					for (int z = 0; z < anotherFragmentOfContin.size(); z++) {
						if (returner.contains(anotherFragmentOfContin.elementAt(z)) == false) {
							returner.addElement(anotherFragmentOfContin.elementAt(z));
						}
					}
					// returner.addAll ( getRestOfContin ( obj, obj2, oldContin
					// ) );
				}
			}

			// pst = con.prepareStatement (
			// "SELECT REL_ImgNumber2, REL_ObjName2, REL_Type FROM Relationship WHERE (REL_ObjName1 = ? AND REL_ImgNumber1 = ? AND REL_ConNumber1 = ?) AND NOT (REL_ObjName2 = ? AND REL_ImgNumber2 = ? AND REL_ConNumber2 = ?)"
			// );
			// pst.setString ( 1, obj.getObjName ( ) );
			// pst.setString ( 2, obj.imageNumber );
			// pst.setInt ( 3, oldContin );
			// pst.setString ( 4, excludeObj.getObjName ( ) );
			// pst.setString ( 5, excludeObj.imageNumber );
			// pst.setInt ( 6, oldContin );

			pst = con.prepareStatement("SELECT ObjName2 FROM relationship WHERE (REL_ObjName1 = ?) AND NOT (REL_ObjName2 = ? )");
			pst.setString(1, obj.getObjName(con));

			pst.setString(2, excludeObj.getObjName(con));

			// pst.setInt ( 6, oldContin );

			rs = pst.executeQuery();

			while (rs.next()) {
				CellObject obj2 = new CellObject(

				rs.getString("ObjName2"));
				Relation rel = new Relation(obj, obj);
				if (returner.contains(rel) == false) {
					returner.add(rel);
				}
				// Util.info("added " + rel);
				if (0 == 0) {
					Vector anotherFragmentOfContin = getRestOfContin(obj, obj2, oldContin);
					for (int z = 0; z < anotherFragmentOfContin.size(); z++) {
						if (returner.contains(anotherFragmentOfContin.elementAt(z)) == false) {
							returner.addElement(anotherFragmentOfContin.elementAt(z));
						}
					}
					// returner.addAll ( getRestOfContin ( obj, obj2, oldContin
					// ) );
				}
			}

			
		} catch (Exception ex) {
			ex.printStackTrace();

		}finally {
			EDatabase.returnConnection(con);
		}
		// Util.info("obj = " + obj);
		// Util.info("excludeObj = " + excludeObj);
		// for (int i = 0; i < returner.size(); i++)
		// {
		// Util.info(((Relation)returner.elementAt(i)));
		// }

		return returner;
	}

	/**
	 * Given a vector of relations, ths functions returns a non-redundant set of
	 * all the objects in this list of relations
	 * 
	 * @param listOfRelationsToBeModified
	 *            The Vector containing the relations
	 * 
	 * @return A Vector containing the CelObjects
	 */
	public static Vector getListOfObjects(Vector listOfRelationsToBeModified) {
		Vector returner = new Vector();
		int num = listOfRelationsToBeModified.size();

		for (int i = 0; i < num; i++) {
			Relation rel = (Relation) listOfRelationsToBeModified.elementAt(i);

			if (returner.contains(rel.getObj1()) == false) {
				returner.addElement(rel.getObj1());
			}

			if (returner.contains(rel.getObj2()) == false) {
				returner.addElement(rel.getObj2());
			}
		}

		return returner;
	}

	public static Vector getListOfContinuousObjects(Vector listOfRelationsToBeModified) {
		Vector returner = new Vector();
		int num = listOfRelationsToBeModified.size();

		for (int i = 0; i < num; i++) {
			Relation rel = (Relation) listOfRelationsToBeModified.elementAt(i);

			if (returner.contains(rel.getObj1()) == false) {
				returner.addElement(rel.getObj1());
			}

			if (returner.contains(rel.getObj2()) == false) {
				returner.addElement(rel.getObj2());
			}
		}

		return returner;
	}

	public static Vector getListOfRelationsInvolving(CellObject obj) {
		Vector returner = new Vector();
		Connection con = null;
		try {
			con = EDatabase.borrowConnection(

			);
			PreparedStatement pst = con.prepareStatement("select ObjName1 " + "from relationship where  ObjName2 = ? ");
			pst.setString(1, obj.objectName);

			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				CellObject obj1 = new CellObject(rs.getString("ObjName1"));
				Relation rel = new Relation(obj1, obj);
				returner.addElement(rel);
			}
			pst = con.prepareStatement("select ObjName2 " + "from relationship where  ObjName1 = ?");
			pst.setString(1, obj.objectName);

			rs = pst.executeQuery();
			if (rs.next()) {
				CellObject obj2 = new CellObject(rs.getString("ObjName2"));
				Relation rel = new Relation(obj, obj2);
				returner.addElement(rel);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

		} finally {
			EDatabase.returnConnection(con);
		}
		return returner;
	}

	/**
	 * Deletes the relations provided from the database
	 * 
	 * @param listOfRelationsToBeDeleted
	 *            The Vector of relations.
	 */
	public static void deleteRelations(Vector listOfRelationsToBeDeleted) {
		Connection con = null;

		try {
			con = EDatabase.borrowConnection();

			PreparedStatement pst = con.prepareStatement("delete from relationship where ObjName1 = ? AND ObjName2 = ?");
			int num = listOfRelationsToBeDeleted.size();

			for (int i = 0; i < num; i++) {
				Relation rel = (Relation) listOfRelationsToBeDeleted.elementAt(i);
				pst.setString(1, rel.getObj1().getObjName(con));

				pst.setString(2, rel.getObj2().getObjName(con));

				int res1 = pst.executeUpdate();

				if (res1 == 0) {
					pst.setString(1, rel.getObj2().getObjName(con));

					pst.setString(2, rel.getObj1().getObjName(con));

					res1 = pst.executeUpdate();
				}

				// Util.info("in function deleteRelations deleted rows - "
				// + res1);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

		}finally {
			EDatabase.returnConnection(con);
		}
	}

	private void deleteRelation(Relation rel) {
		Connection con = null;

		try {
			con = EDatabase.borrowConnection();

			PreparedStatement pst = con.prepareStatement("delete from relationship where ObjName1 = ? AND ObjName2 = ?");

			pst.setString(1, rel.getObj1().getObjName(con));

			pst.setString(2, rel.getObj2().getObjName(con));

			int res1 = pst.executeUpdate();

			if (res1 == 0) {
				pst.setString(1, rel.getObj2().getObjName(con));

				pst.setString(2, rel.getObj1().getObjName(con));

				res1 = pst.executeUpdate();
			}

		
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

		}finally {
			EDatabase.returnConnection(con);
		}
	}

	/**
	 * Changes the contin number of the list of CellObjects to the new contin
	 * number
	 * 
	 * @param listOfObjects
	 *            A Vector of CellObjects
	 * @param newContinNumber
	 *            the new contin number
	 */
	public static void modifyObjects(Vector listOfObjects, int newContinNumber) {
		Connection con = null;

		try {
			con = EDatabase.borrowConnection(

			);

			PreparedStatement pst = con.prepareStatement("update object set CON_Number = ? where OBJ_Name = ? AND IMG_Number = ?");
			int num = listOfObjects.size();

			for (int i = 0; i < num; i++) {
				CellObject obj = (CellObject) listOfObjects.elementAt(i);
				pst.setInt(1, newContinNumber);
				pst.setString(2, obj.getObjName(con));
				pst.setString(3, obj.imageNumber);
				// Util.info("inside modify objs obj = " + obj);
				pst.executeUpdate();
				// Util.info(" success!!!");
			}

			
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

		}finally {
			EDatabase.returnConnection(con);
		}
	}

	/**
	 * Changes the contin number of the CellObjects to the new contin number
	 * 
	 * @param objectToBeModified
	 *            The CellObject
	 * @param newContinNumber
	 *            The new contin number
	 */
	public static void modifyObject(CellObject objectToBeModified, int newContinNumber) {
		Connection con = null;

		try {
			con = EDatabase.borrowConnection();

			PreparedStatement pst = con.prepareStatement("update object set CON_Number = ? where OBJ_Name = ? ");
			pst.setInt(1, newContinNumber);
			pst.setString(2, objectToBeModified.getObjName(con));
			pst.executeUpdate();

			
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

		}finally {
			EDatabase.returnConnection(con);
		}
	}

	/**
	 * Inserts a list of relations into the database with the given contin
	 * number
	 * 
	 * @param listOfRelationsToBeAdded
	 *            A Vector of Relation objects
	 * @param newContinNumber
	 *            the contin number
	 */
	public static void addRelations(Vector listOfRelationsToBeAdded, int newContinNumber) {
		Connection con = null;

		try {
			con = EDatabase.borrowConnection();

			PreparedStatement pst = con.prepareStatement("insert into relationship (ObjName1,ObjName2) values (?, ?) ");
			int num = listOfRelationsToBeAdded.size();

			for (int i = 0; i < num; i++) {
				Relation rel = (Relation) listOfRelationsToBeAdded.elementAt(i);
				pst.setString(1, rel.getObj1().getObjName(con));

				// pst.setInt ( 3, newContinNumber );
				pst.setString(2, rel.getObj2().getObjName(con));

				// pst.setInt ( 6, newContinNumber );

				pst.executeUpdate();
			}

			
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

		}finally {
			EDatabase.returnConnection(con);
		}
	}

	/**
	 * Verifies whether a type of relation can be drawn or not.
	 * 
	 * @param type
	 *            The type of relation
	 * 
	 * @return true or false depending on whether the relation type can be drawn
	 *         or not.
	 */
	public static boolean verifyDraw(String type) {
		if (type.compareToIgnoreCase(GlobalStrings.CONTINUOUS) == 0) {
			if (ApplicationProperties.showContinuousRelations) {
				return true;
			} else {
				return false;
			}
		} else if (type.compareToIgnoreCase(GlobalStrings.PRESYNAPTIC) == 0) {
			if (ApplicationProperties.showPresynapticRelations) {
				return true;
			} else {
				return false;
			}
		} else if (type.compareToIgnoreCase(GlobalStrings.POSTSYNAPTIC) == 0) {
			if (ApplicationProperties.showPostsynapticRelations) {
				return true;
			} else {
				return false;
			}
		} else if (type.compareToIgnoreCase(GlobalStrings.GAP_JUNCTION) == 0) {
			if (ApplicationProperties.showGapRelations) {
				return true;
			} else {
				return false;
			}
		} else if (type.compareToIgnoreCase(GlobalStrings.MULTIPLE_PRESYNAPTIC) == 0) {
			if (ApplicationProperties.showMultPresynapticRelations) {
				return true;
			} else {
				return false;
			}
		} else if (type.compareToIgnoreCase(GlobalStrings.MULTIPLE_POSTSYNAPTIC) == 0) {
			if (ApplicationProperties.showMultPostSynapticRelations) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * Returns the symbol for a relation type.
	 * 
	 * @param type
	 *            The relation type.
	 * 
	 * @return the symbol
	 */
	public static String getSymbolFor(String type) {
		if (type.compareToIgnoreCase(GlobalStrings.CONTINUOUS) == 0) {
			return "----";
		} else if (type.compareToIgnoreCase(GlobalStrings.PRESYNAPTIC) == 0) {
			return "--->";
		} else if (type.compareToIgnoreCase(GlobalStrings.POSTSYNAPTIC) == 0) {
			return "<---";
		} else if (type.compareToIgnoreCase(GlobalStrings.GAP_JUNCTION) == 0) {
			return "|---|";
		} else if (type.compareToIgnoreCase(GlobalStrings.MULTIPLE_PRESYNAPTIC) == 0) {
			return "--->";
		} else if (type.compareToIgnoreCase(GlobalStrings.MULTIPLE_POSTSYNAPTIC) == 0) {
			return "<---";
		} else {
			return "";
		}
	}

	/**
	 * Gets a ReportItem corresponding to the two CellObjects
	 * 
	 * @param obj1
	 *            first CellObject
	 * @param obj2
	 *            second CellObject
	 * 
	 * @return The ReportItem corresponding to the two CellObjects
	 */
	public static ReportItem getReportItemFor(CellObject obj1, CellObject obj2) {
		ReportItem repo = new ReportItem(obj1.imageNumber + "_" + obj1.objectName, obj2.imageNumber + "_" + obj2.objectName, getExperimentName());
		Connection con = null;

		try {
			con = EDatabase.borrowConnection();

			PreparedStatement pst = con.prepareStatement("select REL_Type from relationship where REL_ObjName1 = " + obj1.objectName
					+ " AND REL_ImgNumber1 = '" + obj1.imageNumber + "' AND REL_ObjName2 = " + obj2.objectName + " AND REL_IMgNumber2 = '" + obj2.imageNumber
					+ "'");
			ResultSet rs = pst.executeQuery();

			while (rs.next()) {
				String type = rs.getString("REL_Type");

				if (type.compareToIgnoreCase(GlobalStrings.PRESYNAPTIC) == 0) {
					repo.addSend();
				} else if (type.compareToIgnoreCase(GlobalStrings.POSTSYNAPTIC) == 0) {
					repo.addReceive();
				} else if (type.compareToIgnoreCase(GlobalStrings.MULTIPLE_PRESYNAPTIC) == 0) {
					repo.addSendJoint();
				} else if (type.compareToIgnoreCase(GlobalStrings.MULTIPLE_POSTSYNAPTIC) == 0) {
					repo.addReceiveJoint();
					;
				} else if (type.compareToIgnoreCase(GlobalStrings.GAP_JUNCTION) == 0) {
					repo.addGap();
				} else if (type.compareToIgnoreCase(GlobalStrings.UNCHARECTERIZED) == 0) {
					repo.addContact();
				}
			}

			pst = con.prepareStatement("select REL_Type from relationship where REL_ObjName1 = " + obj2.objectName + " AND REL_ImgNumber1 = '"
					+ obj2.imageNumber + "' AND REL_ObjName2 = " + obj1.objectName + " AND REL_IMgNumber2 = '" + obj1.imageNumber + "'");
			rs = pst.executeQuery();

			while (rs.next()) {
				String type = rs.getString("REL_Type");

				if (type.compareToIgnoreCase(GlobalStrings.PRESYNAPTIC) == 0) {
					repo.addReceive();
				} else if (type.compareToIgnoreCase(GlobalStrings.POSTSYNAPTIC) == 0) {
					repo.addSend();
				} else if (type.compareToIgnoreCase(GlobalStrings.MULTIPLE_PRESYNAPTIC) == 0) {
					repo.addReceiveJoint();
				} else if (type.compareToIgnoreCase(GlobalStrings.MULTIPLE_POSTSYNAPTIC) == 0) {
					repo.addSendJoint();
				} else if (type.compareToIgnoreCase(GlobalStrings.GAP_JUNCTION) == 0) {
					repo.addGap();
				} else if (type.compareToIgnoreCase(GlobalStrings.UNCHARECTERIZED) == 0) {
					repo.addContact();
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

		}finally {
			EDatabase.returnConnection(con);
		}

		return repo;
	}

	/**
	 * Gets the DisplayContin object for contin numbers whose objects have
	 * relations with objects with the contin number provided.
	 * 
	 * @param continNumber
	 *            The contin number
	 * 
	 * @return A Vector with DisplayContin objects.
	 */
	public static Vector getOtherDisplayContins(int continNumber) {
		Vector otherDisplayContins = new Vector();
		Connection con = null;

		try {
			con = EDatabase.borrowConnection();

			PreparedStatement pst = con
					.prepareStatement("Select REL_ConNumber1, REL_ConNumber2, REL_Type from relationship where ( REL_ConNumber1 = ? OR REL_ConNumber2 = ? ) AND REL_Type != ?");
			pst.setInt(1, continNumber);
			pst.setInt(2, continNumber);
			pst.setString(3, GlobalStrings.CONTINUOUS);

			ResultSet rs = pst.executeQuery();

			while (rs.next()) {
				int conNo1 = rs.getInt("REL_ConNumber1");
				int conNo2 = rs.getInt("REL_ConNumber2");
				String relType = rs.getString("REL_Type");

				if (conNo1 != continNumber) {
					DisplayContin discon = new DisplayContin(conNo1, relType);
					int index = otherDisplayContins.indexOf(discon);

					if (index != -1) {
						DisplayContin discon1 = (DisplayContin) otherDisplayContins.remove(index);
						discon1.increaseStrength();
						otherDisplayContins.addElement(discon1);
					} else {
						otherDisplayContins.addElement(discon);
					}
				} else if (conNo2 != continNumber) {
					DisplayContin discon = new DisplayContin(conNo2, relType);
					int index = otherDisplayContins.indexOf(discon);

					if (index != -1) {
						DisplayContin discon1 = (DisplayContin) otherDisplayContins.remove(index);
						discon1.increaseStrength();
						otherDisplayContins.addElement(discon1);
					} else {
						otherDisplayContins.addElement(discon);
					}
				}
			}

			
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

		
		}finally {
			EDatabase.returnConnection(con);
		}

		return otherDisplayContins;
	}

	/**
	 * Gets the TwoDDisplayContin object for objects that have relations with
	 * objects with the contin number provided.
	 * 
	 * @param continNumber
	 *            The contin number
	 * 
	 * @return a Vector of TwoDDisplayContins
	 */
	public static Vector getOther2DDisplayContins(int continNumber) {
		Vector otherDisplayContins = new Vector();
		Connection con = null;

		try {
			con = EDatabase.borrowConnection(

			);

			PreparedStatement pst = con
					.prepareStatement("Select REL_ConNumber1, REL_ImgNumber1, REL_ConNumber2, REL_ImgNumber2, REL_Type from relationship where ( REL_ConNumber1 = ? OR REL_ConNumber2 = ? ) AND REL_Type != ?");
			pst.setInt(1, continNumber);
			pst.setInt(2, continNumber);
			pst.setString(3, GlobalStrings.CONTINUOUS);

			ResultSet rs = pst.executeQuery();

			while (rs.next()) {
				int conNo1 = rs.getInt("REL_ConNumber1");
				int secNo1 = Utilities.getSectionNumber(rs.getString("REL_ImgNumber1"));
				int conNo2 = rs.getInt("REL_ConNumber2");
				int secNo2 = Utilities.getSectionNumber(rs.getString("REL_ImgNumber2"));
				String relType = rs.getString("REL_Type");

				if (conNo1 != continNumber) {
					TwoDDisplayContin discon = new TwoDDisplayContin(secNo1, conNo1, relType);

					if (!otherDisplayContins.contains(discon)) {
						otherDisplayContins.addElement(discon);
					}
				} else if (conNo2 != continNumber) {
					TwoDDisplayContin discon = new TwoDDisplayContin(secNo2, conNo2, relType);

					if (!otherDisplayContins.contains(discon)) {
						otherDisplayContins.addElement(discon);
					}
				}
			}

			
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

			
		}finally {
			EDatabase.returnConnection(con);
		}

		return otherDisplayContins;
	}

	public static String getNameByContin(int continN) {
		String name = "";
		Connection con = null;

		if (continN == 0) {
			return "";
		}
		if (continN < 0) {
			return "" + (-1) * continN;
		}
		try {
			con = EDatabase.borrowConnection(

			);

			PreparedStatement pst = con.prepareStatement("select CON_AlternateName from contin where CON_Number = ?");
			pst.setInt(1, continN);

			ResultSet rs = pst.executeQuery();

			if (rs.next()) {
				name = rs.getString(1);
			} else {
				name = "contin" + continN;
			}

		
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

			
		}finally {
			EDatabase.returnConnection(con);
		}

		return name;
	}

	/**
	 * Gets the section number for a given image number
	 * 
	 * @param imgNumber
	 *            The image number
	 * 
	 * @return The section number
	 */

	public static int getSectionNumber(String imgNumber) {
		int sectionNumber = 0;
		Connection con = null;

		try {
			con = EDatabase.borrowConnection(

			);

			PreparedStatement pst = con.prepareStatement("select IMG_SectionNumber from image where IMG_Number = ?");
			pst.setString(1, imgNumber);

			ResultSet rs = pst.executeQuery();

			if (rs.next()) {
				sectionNumber = rs.getInt("IMG_SectionNumber");
			}



		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

			
		}finally {
			EDatabase.returnConnection(con);
		}

		return sectionNumber;
	}

	public static boolean verifyImageNumberExistance(String imgNoToModify) {
		Connection con = null;
		try {
			con = EDatabase.borrowConnection(

			);
			PreparedStatement pst = con.prepareStatement("Select IMG_Number from image where IMG_Number = ?");
			pst.setString(1, imgNoToModify);
			ResultSet rs = pst.executeQuery();
			boolean returner = rs.next();

			return returner;
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

			return false;
		} finally {
			EDatabase.returnConnection(con);
		}
	}

	public static String getNameFromWhatever(String str) {

		String name = "";
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			con = EDatabase.borrowConnection();

			pst = con.prepareStatement("select CON_AlternateName from contin where CON_Number = ?");
			pst.setString(1, str);

			rs = pst.executeQuery();

			if (rs.next()) {
				name = rs.getString(1);

				pst.close();
				rs.close();

				return name;
			} else {

				return str;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

		} finally {
			EDatabase.returnConnection(con);
		}

		return str;

	}

	public static String changeColorCode(String str) {
		String[] strs = str.split(",");
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String output = "";

		try {
			con = EDatabase.borrowConnection(

			);

			for (int i = 0; i < strs.length; i++) {
				pst = con.prepareStatement("select CON_Number from contin where CON_AlternateName2 = ? or CON_Number=?");
				pst.setString(1, strs[i]);
				pst.setString(2, strs[i]);

				rs = pst.executeQuery();

				if (rs.next()) {
					output = rs.getString(1);
				} else {
					return str;
				}

				while (rs.next()) {
					output = output + "," + rs.getString(1);

				}
				return output;

			}

		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

		} finally {
			EDatabase.returnConnection(con);
		}

		return output;

	}

	public static String getNumberFromWhatever(String str) {
		String nums = "";
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			con = EDatabase.borrowConnection();

			pst = con.prepareStatement("select CON_Number from contin where CON_AlternateName = ?");
			pst.setString(1, str);

			rs = pst.executeQuery();

			if (rs.next()) {
				nums = rs.getString(1);
			} else {
				return str;
			}

			while (rs.next()) {
				nums = nums + "," + rs.getString(1);

			}
			return nums;

		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

		} finally {
			EDatabase.returnConnection(con);
		}

		return str;

	}

}
