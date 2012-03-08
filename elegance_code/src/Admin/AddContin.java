package Admin;
import java.sql.*;

public class AddContin {
	public static void main(String[] args) {
		long time1 = System.currentTimeMillis();

		try {

			Connection con = null;
			Statement st = null;
			PreparedStatement pst = null;
			ResultSet rs = null, rs1 = null;
			
			String jsql = null;
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = DriverManager.getConnection(
					"jdbc:mysql://wormdesk1/jse", "root", "worms");

			jsql = "select * from contin ";
			st = con.createStatement();
			rs = st.executeQuery(jsql);
			while (rs.next()) {
				int contin = rs.getInt("CON_Number");

				pst = con
						.prepareStatement("select count(*) from object where CON_Number=?");
				pst.setInt(1, contin);
				rs1 = pst.executeQuery();
				rs1.next();
				int count = rs1.getInt(1);
				rs1.close();
				pst.close();

				pst = con
						.prepareStatement("select max(IMG_SectionNumber),min(IMG_SectionNumber) from object,image where object.IMG_Number=image.IMG_Number and CON_Number=?");
				pst.setInt(1, contin);
				rs1 = pst.executeQuery();
				rs1.next();
				int img2 = rs1.getInt(1);
				int img1 = rs1.getInt(2);

				rs1.close();
				pst.close();

				String series = "", s = "";
				pst = con
						.prepareStatement("select distinct IMG_Series from object,image where object.IMG_Number=image.IMG_Number and CON_Number=?");
				pst.setInt(1, contin);
				rs1 = pst.executeQuery();

				if (rs1.next()) {
					s = rs1.getString(1);
					if (s.equals("Ventral Cord 2"))
						s = "VC";
					if (s.equals("Ventral Cord"))
						s = "VC";
					if (s.equals("N2YDRG"))
						s = "DRG";
					series = s;
				}
				while (rs1.next()) {
					s = rs1.getString(1);
					if (s.equals("Ventral Cord 2"))
						s = "VC";
					if (s.equals("Ventral Cord"))
						s = "VC";
					if (s.equals("N2YDRG"))
						s = "DRG";
					series = series + "," + s;

				}
				rs1.close();
				pst.close();

				pst = con
						.prepareStatement("update contin set count=?,series=?,sectionNum1=?,sectionNum2=?  where CON_Number=?");
				pst.setInt(1, count);
				pst.setString(2, series);
				pst.setInt(5, contin);
				pst.setInt(3, img1);
				pst.setInt(4, img2);

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
