import java.awt.Component;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JOptionPane;

class NameContin

{
	int objName, continNum = 0;
	String continName;
	private boolean isBatch=false;
	@Override
	public String toString() {
		return "NameContin [objName=" + objName + ", continNum=" + continNum + ", continName=" + continName + "]";
	}

	public NameContin(int objNumber, boolean isBatch,Component parentComponent) {
		this.isBatch=isBatch;
		objName = objNumber;
		setContinName(parentComponent);
	}
	

	
	public NameContin(int objNumber,Component parentComponent) {
		this(objNumber,false,parentComponent);
	}

	public void setContinName(Component parentComponent) {

		Connection con = null;
		Statement st = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
	
		try {

			con = EDatabase.borrowConnection();
			st = con.createStatement();
			rs = st.executeQuery("select CON_Number from object where OBJ_Name=" + objName);
			if (rs.next()) {
				continNum = rs.getInt(1);
			}
			rs.close();
			st.close();
			if (continNum <= 0) {
				continNum = findContinNum();
				
				if (continNum <= 0) {
					continNum = Utilities.getNewContinNumber();
					pst = con.prepareStatement("insert into contin (CON_Number,CON_Alternatename) values(?,?)");
					pst.setInt(1, continNum);
					
					String newContinName=null;
					
					if (isBatch) {
						
					} else {

						String msg = "This is a new contin, what would you like to name it?";
						if (parentComponent!=null) parentComponent.requestFocus(); 
						int response = JOptionPane.showConfirmDialog(parentComponent, msg, msg, JOptionPane.YES_NO_OPTION);

						if (response == JOptionPane.OK_OPTION) {
							newContinName = JOptionPane.showInputDialog(parentComponent, "Please specify contin name:");
						}

					}
					
					
					pst.setString(2, newContinName);
					pst.executeUpdate();
					pst.close();
				}

				pst = con.prepareStatement("update object set CON_number=" + continNum + " where OBJ_Name = " + objName);
				pst.executeUpdate();
				pst.close();

			}
		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			EDatabase.returnConnection(con);
		}

	}

	public int findContinNum() {
		Connection con = null;
		Statement st = null, st1 = null;
		PreparedStatement pst = null;
		ResultSet rs = null, rs1 = null;
		int friend = 0, conN = 0, flag = 1;
		int objN1 = objName, objN2 = objName;
		// JOptionPane.showMessageDialog(null, "objN1 and objN2 are "+objName,
		// "continName", JOptionPane.INFORMATION_MESSAGE);
		try {

			con = EDatabase.borrowConnection();

			while (flag == 1) {
				st = con.createStatement();
				rs = st.executeQuery("select ObjName2 from relationship where ObjName1=" + objN1);
				if (rs.next()) {

					friend = rs.getInt("ObjName2");
					// JOptionPane.showMessageDialog(null,
					// "post friend is "+friend, "friend",
					// JOptionPane.INFORMATION_MESSAGE);
				} else {
					flag = 0;
				}
				rs.close();
				st.close();

				st = con.createStatement();
				rs = st.executeQuery("select CON_Number from object where OBJ_Name=" + friend);
				if (rs.next()) {
					conN = rs.getInt("CON_Number");
					// JOptionPane.showMessageDialog(null, "conN is "+conN,
					// "continNum", JOptionPane.INFORMATION_MESSAGE);
				}
				rs.close();
				st.close();

				if (conN > 0) {
					return conN;
				}
				objN1 = friend;
			}

			friend = 0;
			flag = 1;

			while (flag == 1) {
				st = con.createStatement();
				rs = st.executeQuery("select ObjName1 from relationship where ObjName2=" + objN2);
				if (rs.next()) {

					friend = rs.getInt("ObjName1");
					// JOptionPane.showMessageDialog(null,
					// "pre friend is "+friend, "friend",
					// JOptionPane.INFORMATION_MESSAGE);
				} else {
					flag = 0;
				}
				rs.close();
				st.close();

				st = con.createStatement();
				rs = st.executeQuery("select CON_Number from object where OBJ_Name=" + friend);
				if (rs.next()) {
					conN = rs.getInt("CON_Number");
					// JOptionPane.showMessageDialog(null, "conN is "+conN,
					// "continNum", JOptionPane.INFORMATION_MESSAGE);
				}
				rs.close();
				st.close();

				if (conN > 0) {
					return conN;
				}
				objN2 = friend;
			}

			
		} catch (Exception ex) {
			ex.printStackTrace();
			
		}finally {
			EDatabase.returnConnection(con);
		}
		return 0;
	}

}
