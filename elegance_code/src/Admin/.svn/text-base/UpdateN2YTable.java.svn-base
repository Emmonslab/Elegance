package Admin;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;



public class UpdateN2YTable{
	
	Connection con = null;
	public static void main(String[] args) 
	{
		new UpdateN2YTable();
	}
	
	public UpdateN2YTable()
	{
		
	
      
		try {

			Connection con = null;
			Statement st = null;
			PreparedStatement pst = null;
			ResultSet rs = null, rs1 = null;
			
			String jsql = null;
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = DriverManager.getConnection(
					"jdbc:mysql://wormdesk1/elegance", "root", "worms");

			jsql = "select name from n2ytable ";
			st = con.createStatement();
			rs = st.executeQuery(jsql);
			while (rs.next()) {
				
				String name = rs.getString("name");
				

				pst = con.prepareStatement("select CON_Number from contin where CON_AlternateName=? and CON_Remarks like 'OK%' order by count desc");
				pst.setString(1, name);
				//pst.setString(2, name);
				rs1 = pst.executeQuery();
				String continNums = "0";
				if (rs1.next()) continNums = rs1.getInt(1)+"";
				while (rs1.next()){
					int contin = rs1.getInt(1);
					continNums = continNums + "," + contin;
				}
				
				rs1.close();
				pst.close();
				
				pst = con.prepareStatement("update n2ytable set continNums=? where name=?");
				pst.setString(1, continNums);
				pst.setString(2,name);
			    pst.executeUpdate();
				pst.close();
			}
		 }catch(Exception e1){
		        e1.printStackTrace();
	        }
	
	}
	
	




}
