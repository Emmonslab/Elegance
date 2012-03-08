import java.sql.*;

public class SynapseContin {
	public static void main(String[] args) {
		long time1 = System.currentTimeMillis();

		try {

			Connection con = null;
			Statement st = null;
			PreparedStatement pst = null, pst1 = null;
			ResultSet rs = null, rs1 = null;
			String jsql = null;

			con = EDatabase.borrowConnection();

			pst = con.prepareStatement("select o.CON_Number,s.idx from synapsecombined as s,object as o where s.mid=o.OBJ_Name");

			rs = pst.executeQuery();
			while (rs.next()) {
				int continNum = rs.getInt(1);
				int idx = rs.getInt(2);

				pst1 = con.prepareStatement("update synapsecombined set continNum=?  where idx=?");
				pst1.setInt(1, continNum);
				pst1.setInt(2, idx);

				pst1.executeUpdate();
			}
			rs.close();
			pst.close();

		}

		catch (SQLException e1) {
			e1.printStackTrace();
		}

		long time2 = System.currentTimeMillis();
		long time = (time2 - time1) / 1000;
		ELog.info("It took " + time + ". Done, please press Ctrl + C to close the program");

	}

}
