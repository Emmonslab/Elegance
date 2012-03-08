package Admin;
import java.awt.Point;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import javax.swing.JOptionPane;

/**
 * This class represents an object on an image. This class provides capacity to
 * store the data retrieved from the database in an application friendly format.
 * In addition to this, this class provides various methods to
 * insert/delete/update/select data from/to the database. Throughout this class
 * documentation, references are made to "Object". This object must be
 * differentiated from the object in a software sense. The "Object" in most
 * cases would stand for an identified cell in an image of the crosssection.
 * 
 * @author zndavid
 * @version 1.0
 */
public class CellObject {
	String imageNumber;
	Point p;
	String objectName = null;
	int continNumber = 0;
	String fromObj = "";
	String toObj = "";
	String type = "cell";
	String size = "normal";
	String certainty = "certain";
	int checked = 0;

	/**
	 * Creates a new CellObject object using the provided information.
	 * 
	 * @param newImageNo
	 *            a string identifying the image number of the image of which
	 *            this object is a part of
	 * @param newP
	 *            the point on the unrotated image to which this object
	 *            corresponds.
	 */
	public CellObject(String newImageNo, Point newP) {
		imageNumber = newImageNo;
		p = newP;
		setObjNameAndConNo();
	}
	
	public CellObject(String newImageNo, Point newP, int continNum) {
		imageNumber = newImageNo;
		p = newP;
		setObjNameAndConNo();
		continNumber=continNum;
	}

	public CellObject(Point newP, String newImageNo, String newType,
			String newFromObj, String newToObj

	) {
		p = newP;
		imageNumber = newImageNo;
		type = newType;
		fromObj = newFromObj;
		toObj = newToObj;
		setObjNameAndConNo();
	}

	public CellObject(Point newP, String newImageNo, String newType,
			String newFromObj, String newToObj, String newObjectName,
			int newChecked, int newContinNum,String certainty) {
		p = newP;
		imageNumber = newImageNo;
		type = newType;
		fromObj = newFromObj;
		toObj = newToObj;
		objectName = newObjectName;
		checked = newChecked;
		continNumber = newContinNum;
		this.certainty = certainty;
	}

	/**
	 * Creates a new CellObject object using the provided information.
	 * 
	 * @param newImageNo
	 *            a string identifying the image number of the image of which
	 *            this object is a part of
	 * @param newObjectName
	 *            the object name of the particular object.
	 */
	public CellObject(String newObjectName) {
		objectName = newObjectName;
		setPointAndConNo();
	}

	public CellObject() {
		imageNumber = null;
		p = null;
		objectName = null;
		continNumber = 0;
		fromObj = null;
		toObj = null;
		type = null;
	}

	/**
	 * connects to the database and fetches the contin number of this particular
	 * object. Connecting to the database is imperative because the contin
	 * number of an object need not be constant and might keep changing as
	 * contins are created or deleted.
	 * 
	 * @return returns the contin number (a number >=0) if an object exists in
	 *         the database. returns -1 otherwise.
	 */
	public int getContinNo() {
		// if(continNumber != -1 ) return continNumber;
		Connection con = null;

		try {
			con = DriverManager.getConnection(
					DatabaseProperties.CONNECTION_STRING,
					DatabaseProperties.USERNAME, DatabaseProperties.PASSWORD);

			PreparedStatement pst = con
					.prepareStatement("select CON_Number from object where IMG_Number = ? and OBJ_X = ? and OBJ_Y = ?");
			pst.setString(1, imageNumber);
			pst.setInt(2, p.x);
			pst.setInt(3, p.y);

			ResultSet rs = pst.executeQuery();

			if (rs.next()) {

				int continNumb = rs.getInt(1);
				if (con.isClosed() == false) {
					con.close();
				}
				return continNumb;

			} else {
				if (con.isClosed() == false) {
					con.close();
				}

				return 0;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);

			if (con != null) {
				con = null;
			}

			return 0;
		}
	}

	public void setObjName(String newName) {
		this.objectName = newName;
	}

	public void setPoint(Point newP) {
		this.p = newP;
	}

	public void setType(String newType) {
		
		this.type = newType;
	}

	public int getSectionNum() {
		// if(continNumber != -1 ) return continNumber;
		Connection con = null;
		int section = 0;
		try {

			con = DriverManager.getConnection(
					DatabaseProperties.CONNECTION_STRING,
					DatabaseProperties.USERNAME, DatabaseProperties.PASSWORD);
			PreparedStatement pst = con
					.prepareStatement("select IMG_SectionNumber from image where IMG_Number = ?");
			pst.setString(1, imageNumber);

			ResultSet rs = pst.executeQuery();

			if (rs.next()) {

				section = rs.getInt("IMG_SectionNumber");
			} else {

				section = -1;
			}
			if (con.isClosed() == false) {
				con.close();
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);

			if (con != null) {
				con = null;
			}

			return -1;
		}
		return section;
	}

	private void setObjNameAndConNo() {
		Connection con = null;

		try {
			con = DriverManager.getConnection(
					DatabaseProperties.CONNECTION_STRING,
					DatabaseProperties.USERNAME, DatabaseProperties.PASSWORD);

			PreparedStatement pst = con
					.prepareStatement("select * from object where IMG_Number = ? and OBJ_X = ? and OBJ_Y = ?");
			pst.setString(1, imageNumber);
			pst.setInt(2, p.x);
			pst.setInt(3, p.y);

			ResultSet rs = pst.executeQuery();

			if (rs.next()) {
				objectName = rs.getString("OBJ_Name");
				continNumber = rs.getInt("CON_Number");
				type = rs.getString("type");
				fromObj = rs.getString("fromObj");
				toObj = rs.getString("toObj");
				checked = rs.getInt("checked");

				if (con.isClosed() == false) {
					con.close();
				}
			} else {

				if (con.isClosed() == false) {
					con.close();
				}

				return;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);

			if (con != null) {
				con = null;
			}

			return;
		}
	}

	/**
	 * creates a CellObject using the information provided if an object with the
	 * given information exists in the database.
	 * 
	 * @param objName
	 *            The name of the object
	 * @param imgNumber
	 *            the image number of the image in which this particular object
	 *            is found.
	 * @param continNumber
	 *            the contin number of the object
	 * 
	 * @return the newly created CellObject if created of null otherwise.
	 */
	public static CellObject createCellObject(String objName

	) {
		Connection con = null;

		try {
			con = DriverManager.getConnection(
					DatabaseProperties.CONNECTION_STRING,
					DatabaseProperties.USERNAME, DatabaseProperties.PASSWORD);

			PreparedStatement pst = con
					.prepareStatement("select OBJ_X, OBJ_Y, IMG_Number,CON_Number from object where OBJ_Name = ? ");
			pst.setString(1, objName);

			ResultSet rs = pst.executeQuery();

			if (rs.next()) {
				Point pt = new Point();
				pt.x = rs.getInt("OBJ_X");
				pt.y = rs.getInt("OBJ_Y");

				String str = rs.getString("IMG_Number");

				if (con.isClosed() == false) {
					con.close();
				}

				return new CellObject(str, pt);
			} else {
				if (con.isClosed() == false) {
					con.close();
				}

				return null;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);

			if (con != null) {
				con = null;
			}

			return null;
		}
	}

	/**
	 * An override of the equals method of the Object class. returns true or
	 * false depending on whether object is equal to this object or not.
	 * 
	 * @param obj
	 *            any Object
	 * 
	 * @return Returns true of the object is of the type CellObject and the
	 *         image numbers of both the objects are same and the points of both
	 *         the objects are same. Returns false otherwise.
	 */
	public boolean equals(Object obj) {
		try {
			CellObject obj1 = (CellObject) obj;

			if ((obj1.imageNumber.compareToIgnoreCase(imageNumber) == 0)
					&& (p.x == obj1.p.x) && (p.y == obj1.p.y)) {
				return true;
			}

			return false;
		} catch (Exception ex) {
			return false;
		}
	}

	private void setPointAndConNo() {
		Connection con = null;

		try {
			con = DriverManager.getConnection(
					DatabaseProperties.CONNECTION_STRING,
					DatabaseProperties.USERNAME, DatabaseProperties.PASSWORD);

			PreparedStatement pst = con
					.prepareStatement("select * from object where OBJ_Name = ?");

			if (objectName == null) {
				return;
			}
			System.out.println("objectName:"+objectName);

			pst.setString(1, objectName);

			ResultSet rs = pst.executeQuery();

			if (rs.next()) {
				

				p = new Point(rs.getInt("OBJ_X"), rs.getInt("OBJ_Y"));
				imageNumber = rs.getString("IMG_Number");
				continNumber = rs.getInt("CON_Number");
				type = rs.getString("type");
				fromObj = rs.getString("fromObj");
				toObj = rs.getString("toObj");
				checked = rs.getInt("checked");
				size = rs.getString("size");
				certainty = rs.getString("certainty");
			} else {
				
				if (con.isClosed() == false) {
					con.close();
				}
				
				return;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);

			if (con != null) {
				con = null;
			}

			return;
		}
	}

	public void addFrom(CellObject obj) {
		Connection con = null;
		try {
			con = DriverManager.getConnection(
					DatabaseProperties.CONNECTION_STRING,
					DatabaseProperties.USERNAME, DatabaseProperties.PASSWORD);
			PreparedStatement pst = con
					.prepareStatement("update object set fromObj = ? WHERE OBJ_Name = ?");

			pst.setString(1, obj.objectName);
			pst.setString(2, objectName);
			pst.executeUpdate();
			if (con.isClosed() == false)
				con.close();

		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
			if (con != null)
				con = null;

		}
	}

	public void addTo(CellObject obj) {
		Connection con = null;
		try {
			con = DriverManager.getConnection(
					DatabaseProperties.CONNECTION_STRING,
					DatabaseProperties.USERNAME, DatabaseProperties.PASSWORD);
			PreparedStatement pst = con
					.prepareStatement("update object set toObj = ? WHERE OBJ_Name = ?");

			pst.setString(1, obj.objectName);
			pst.setString(2, this.objectName);
			pst.executeUpdate();
			if (con.isClosed() == false)
				con.close();

		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
			if (con != null)
				con = null;

		}
	}

	public void setTo(String name) {
		toObj = name;
	}

	public void setFrom(String name) {
		fromObj = name;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public void setCertainty(String certainty) {
		this.certainty = certainty;
	}

	public void setChecked() {
		this.checked = 1;
		Connection con = null;
		try {
			con = DriverManager.getConnection(
					DatabaseProperties.CONNECTION_STRING,
					DatabaseProperties.USERNAME, DatabaseProperties.PASSWORD);
			PreparedStatement pst = con
					.prepareStatement("update object set checked = 1 WHERE OBJ_Name = ?");

			pst.setString(1, this.objectName);
			pst.executeUpdate();
			if (con.isClosed() == false)
				con.close();

		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
			if (con != null)
				con = null;

		}
	}

	public void setUnchecked() {
		this.checked = 0;
		Connection con = null;
		try {
			con = DriverManager.getConnection(
					DatabaseProperties.CONNECTION_STRING,
					DatabaseProperties.USERNAME, DatabaseProperties.PASSWORD);
			PreparedStatement pst = con
					.prepareStatement("update object set checked = 0 WHERE OBJ_Name = ?");

			pst.setString(1, this.objectName);
			pst.executeUpdate();
			if (con.isClosed() == false)
				con.close();

		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
			if (con != null)
				con = null;

		}
	}

	public String getObjectName() {

		return objectName;
	}

	/**
	 * returns the object name
	 * 
	 * @return the object name
	 * 
	 * 
	 */
	public String getObjName() {
		if (objectName == null || objectName.compareTo("") == 0) {
			setObjNameAndConNo();
		}
		return objectName;
	}

	/**
	 * returns a string representaions of this object. Overrides the toString
	 * method of the Object class.
	 * 
	 * @return a string representaions of this object
	 */
	public String toString() {
		return imageNumber + " " + p + " " + objectName + "\n";
	}

	public int changeContinNumberInDatabase(int newNumber) {
		Connection con = null;
		try {
			con = DriverManager.getConnection(
					DatabaseProperties.CONNECTION_STRING,
					DatabaseProperties.USERNAME, DatabaseProperties.PASSWORD);
			PreparedStatement pst = con
					.prepareStatement("update object set CON_Number = ? Where OBJ_Name = ?");
			pst.setInt(1, newNumber);

			pst.setString(2, this.objectName);
			int returner = pst.executeUpdate();
			if (con.isClosed() == false)
				con.close();
			return returner;
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
			if (con != null)
				con = null;
			return -1;
		}
	}

	public String getRemarks() {
		Connection con = null;
		String returner = "";
		try {
			con = DriverManager.getConnection(
					DatabaseProperties.CONNECTION_STRING,
					DatabaseProperties.USERNAME, DatabaseProperties.PASSWORD);
			PreparedStatement pst = con
					.prepareStatement("Select OBJ_Remarks from object where OBJ_Name = ? AND IMG_Number = ?");
			pst.setString(1, this.objectName);
			pst.setString(2, this.imageNumber);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				returner = rs.getString("OBJ_Remarks");
			}
			if (con.isClosed() == false)
				con.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
			if (con != null)
				con = null;
		}
		return returner;
	}

	public void saveRemarks(String remarks) {
		Connection con = null;
		String returner = "";
		try {
			con = DriverManager.getConnection(
					DatabaseProperties.CONNECTION_STRING,
					DatabaseProperties.USERNAME, DatabaseProperties.PASSWORD);
			PreparedStatement pst = con
					.prepareStatement("Update object set OBJ_Remarks = ? where OBJ_Name = ? AND IMG_Number = ?");
			pst.setString(1, remarks);
			pst.setString(2, this.objectName);
			pst.setString(3, this.imageNumber);
			pst.executeUpdate();
			if (con.isClosed() == false)
				con.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
			if (con != null)
				con = null;
		}
	}

	public void saveAlternateName(String name) {
		Connection con = null;
		String returner = "";
		try {
			con = DriverManager.getConnection(
					DatabaseProperties.CONNECTION_STRING,
					DatabaseProperties.USERNAME, DatabaseProperties.PASSWORD);
			PreparedStatement pst = con
					.prepareStatement("Update contin set CON_AlternateName = ? where CON_Number = ?");
			pst.setString(1, name);
			pst.setInt(2, this.continNumber);

			pst.executeUpdate();
			if (con.isClosed() == false)
				con.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
			if (con != null)
				con = null;
		}
	}

	public void saveRecords(int synID, String username, int partner,String pcertainty,String psize) {
		Connection con = null;

		try {
			con = DriverManager.getConnection(
					DatabaseProperties.CONNECTION_STRING,
					DatabaseProperties.USERNAME, DatabaseProperties.PASSWORD);
			ResultSet rs;
			PreparedStatement pst;
			pst = con.prepareStatement("insert into synrecord (synID,partner, username, DateEntered, size, certainty) values(?, ?, ?, ?, ?, ?)");
			pst.setInt(1, synID);
			pst.setInt(2, partner);
			pst.setString(3, username);
			Calendar t = Calendar.getInstance();
			int m = t.get(t.MONTH) + 1;
			int y = t.get(t.YEAR);
			int d = t.get(t.DATE);
			String date = y + "-" + m + "-" + d;
			pst.setString(4, date);
			pst.setString(5, psize);
			pst.setString(6, pcertainty);
			pst.executeUpdate();
			pst.close();
			
			
			
			

		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
			if (con != null)
				con = null;
		}
	}

	public void saveObject(String username) {
		Connection con = null;

		try {
			con = DriverManager.getConnection(
					DatabaseProperties.CONNECTION_STRING,
					DatabaseProperties.USERNAME, DatabaseProperties.PASSWORD);
			ResultSet rs;
			PreparedStatement pst, pst1;
			pst = con
					.prepareStatement("select * from object where OBJ_X=? and OBJ_Y=? and IMG_Number=?");
			pst.setInt(1, p.x);
			pst.setInt(2, p.y);
			pst.setString(3, imageNumber);
			rs = pst.executeQuery();
			Calendar t = Calendar.getInstance();
			int m = t.get(t.MONTH) + 1;
			int y = t.get(t.YEAR);
			int d = t.get(t.DATE);
			String date = y + "-" + m + "-" + d;
			if (rs.next()) {
				pst1 = con
						.prepareStatement("update object set type=?,fromObj=?, toObj=?, checked=?,username=?,size=?,certainty=?,DateEntered=?,CON_Number=? where OBJ_X=? and OBJ_Y=? and IMG_Number=?");
				String stype= type;
				if (type.equals("multiple")) stype="chemical";
				pst1.setString(1, stype);
				pst1.setString(2, fromObj);
				pst1.setString(3, toObj);
				pst1.setInt(4, checked);
				pst1.setString(5, username);
				pst1.setString(6, size);
				pst1.setString(7, certainty);
				pst1.setString(8,date);
				pst1.setInt(9,continNumber);
				pst1.setInt(10, p.x);
				pst1.setInt(11, p.y);
				pst1.setString(12, imageNumber);
				
				pst1.executeUpdate();
				pst1.close();
			} else {
				pst1 = con
						.prepareStatement("insert into object (OBJ_X, OBJ_Y, IMG_Number, type, fromObj, toObj, checked,username,DateEntered,size, certainty,CON_Number) values(?, ?, ?, ?, ?, ?, ?,?,?,?,?,?)");
				pst1.setInt(1, p.x);
				pst1.setInt(2, p.y);
				pst1.setString(3, imageNumber);
				String stype= type;
				if (type.equals("multiple")) stype="chemical";
				pst1.setString(4, stype);
				pst1.setString(5, fromObj);
				pst1.setString(6, toObj);
				pst1.setInt(7, checked);
				pst1.setString(8, username);
				pst1.setString(9, date);
				pst1.setString(10, size);
				pst1.setString(11, certainty);
				pst1.setInt(12, continNumber);

				pst1.executeUpdate();
				pst1.close();
			}

			pst.close();
			setObjNameAndConNo();
			if (con.isClosed() == false)
				con.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
			if (con != null)
				con = null;
		}
	}
	
	
	
	
	
	public void saveObject2(String username) {
		Connection con = null;

		try {
			con = DriverManager.getConnection(
					DatabaseProperties.CONNECTION_STRING,
					DatabaseProperties.USERNAME, DatabaseProperties.PASSWORD);
			ResultSet rs;
			PreparedStatement pst,pst0;
			
			Calendar t = Calendar.getInstance();
			int m = t.get(t.MONTH) + 1;
			int y = t.get(t.YEAR);
			int d = t.get(t.DATE);
			String date = y + "-" + m + "-" + d;
			
			
			
			pst0 = con.prepareStatement("select * from object2 where OBJ_Name=? and username=?");
	        pst0.setString(1,objectName);
	        pst0.setString(2,username);
	        rs = pst0.executeQuery();
	        if (rs.next()) {
		    pst = con.prepareStatement("update object2 set type=?, fromObj=?, toObj=?, DateEntered=?, size=?,certainty=? where OBJ_Name=? and username=?");
		    String stype= type;
		    if (type.equals("multiple")) stype="chemical";
		    pst.setString(1, stype);
		    pst.setString(2, fromObj);
			pst.setString(3, toObj);
			pst.setString(4, date);
			pst.setString(5, size);
			pst.setString(6, certainty);
			pst.setString(7, objectName);
			pst.setString(8, username);
			pst.executeUpdate();
			pst.close();
	        }else{
			
			pst = con.prepareStatement("insert into object2 (OBJ_Name, type, fromObj, toObj,username,DateEntered,size, certainty) values(?, ?, ?, ?, ?, ?, ?,?)");
			pst.setString(1, objectName);
				
			String stype= type;
			if (type.equals("multiple")) stype="chemical";
			pst.setString(2, stype);
			pst.setString(3, fromObj);
			pst.setString(4, toObj);
			pst.setString(5, username);
			pst.setString(6, date);
			pst.setString(7, size);
			pst.setString(8, certainty);
            pst.executeUpdate();
			pst.close();
	        }
			if (con.isClosed() == false)
				con.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
			if (con != null)
				con = null;
		}
	}

}
