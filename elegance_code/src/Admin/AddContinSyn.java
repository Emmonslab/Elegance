package Admin;
import java.sql.*;

public class AddContinSyn {
	public static void main(String[] args) {
		long time1 = System.currentTimeMillis();

		try {

			Connection con = null;
			Statement st = null;
			PreparedStatement pst = null,pst1=null;
			ResultSet rs = null, rs1 = null,rs2=null;
			
			String jsql = null;
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = DriverManager.getConnection(
					"jdbc:mysql://wormdesk1/elegance", "root", "worms");

			jsql = "select * from contin where type='muscle'";
			st = con.createStatement();
			rs = st.executeQuery(jsql);
			while (rs.next()) {
				int contin = rs.getInt("CON_Number");
				int count = 0,count2=0;

				pst = con.prepareStatement("select count(o1.OBJ_Name) from object as o1, object as o2" +
						" where o1.CON_Number=? and o2.type='chemical' and ( o1.OBJ_Name=o2.fromObj or o1.OBJ_Name=o2.toObj )");
				pst.setInt(1, contin);
				rs1 = pst.executeQuery();
				if(rs1.next())	count = rs1.getInt(1);
				rs1.close();
				pst.close();
				
				
				pst = con.prepareStatement("select count(o1.OBJ_Name) from object as o1, object as o2" +
				" where o1.CON_Number=? and o2.type='electrical' and ( o1.OBJ_Name=o2.fromObj or o1.OBJ_Name=o2.toObj )");
		        pst.setInt(1, contin);
		        rs1 = pst.executeQuery();
		        if(rs1.next())	count2 = rs1.getInt(1);
		        rs1.close();
		        pst.close();
				
				pst = con.prepareStatement("update contin set synSections=? , eleSections=? where CON_Number=?");
		        pst.setInt(1, count);
		        pst.setInt(2, count2);
		        pst.setInt(3, contin);
		       
		        pst.executeUpdate();
		        pst.close();
				
				
				
			

				

			}

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
		

		long time2 = System.currentTimeMillis();
		long time = (time2 - time1) / 1000;
		System.out.println("It took " + time
				+ ". Done, please press Ctrl + C to close the program");

	}

}
