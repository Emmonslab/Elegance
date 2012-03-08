import java.sql.*;

public class NeuronTable {
	public static void main(String[] args) {
		long time1 = System.currentTimeMillis();

		try {

			Connection con = null;
			Statement st = null;
			PreparedStatement pst = null;
			ResultSet rs = null, rs1 = null;
			
			String jsql = null;
			
			con = EDatabase.borrowConnection(
					);

			jsql = "select * from neurontable ";
			st = con.createStatement();
			rs = st.executeQuery(jsql);
			while (rs.next()) {
				int idx = rs.getInt("idx");
				String name = rs.getString("name");
				String contins = rs.getString("continNum");

				pst = con.prepareStatement("select CON_Number from contin where CON_AlternateName=? order by count desc");
				pst.setString(1, name);
				//pst.setString(2, name);
				rs1 = pst.executeQuery();
				String continNums = "";
				if (rs1.next()) continNums = rs1.getInt(1)+"";
				while (rs1.next()){
					int contin = rs1.getInt(1);
					continNums = continNums + "," + contin;
				}
				
				rs1.close();
				pst.close();
				
				pst = con.prepareStatement("update neurontable set newContinNum=? where idx=?");
				pst.setString(1, continNums);
				pst.setInt(2,idx);
			    pst.executeUpdate();
				pst.close();
				
				
				
			}
			
			

		}	

		catch (Exception e) {
			e.printStackTrace();
		}
		

		long time2 = System.currentTimeMillis();
		long time = (time2 - time1) / 1000;
		ELog.info("It took " + time
				+ ". Done, please press Ctrl + C to close the program");

	}

}
