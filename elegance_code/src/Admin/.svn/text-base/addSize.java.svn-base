package Admin;
import java.sql.*;

public class addSize {
	public static void main(String[] args) {
		long time1 = System.currentTimeMillis();
		
		try {

			Connection con = null, con2=null;
			Statement st = null;
			PreparedStatement pst = null,pst1=null;
			ResultSet rs = null, rs1 = null,rs2=null;
			
			String jsql = null;
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = DriverManager.getConnection(
					"jdbc:mysql://wormdesk1/syn1", "root", "worms");
			
			con2 = DriverManager.getConnection(
					"jdbc:mysql://wormdesk1/elegance", "root", "worms");

			jsql = "select idx from muscle_muscle_ele";
			st = con.createStatement();
			rs = st.executeQuery(jsql);
			while (rs.next()) {
				int idx = rs.getInt(1);
				int size = 0;
				
				pst = con2.prepareStatement("select sections from synapsecombined where idx=?");
				pst.setInt(1, idx);
				rs1 = pst.executeQuery();
				if(rs1.next())	size = rs1.getInt(1);
				rs1.close();
				pst.close();
				
				
				
				
				pst = con.prepareStatement("update muscle_muscle_ele set size=? where idx=?");
		        pst.setInt(1, size);
		        pst.setInt(2, idx);
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
