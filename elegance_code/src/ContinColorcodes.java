import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class ContinColorcodes {
	
	public static void main(String[] args) {
		int continNum;
		try {
			
			Connection con = EDatabase.borrowConnection(
					
					);
			String jsql;
			PreparedStatement pstmt;
			ResultSet rs;
		
			  
			jsql = "select CON_Number from contin where count > 1 and CON_Number>0";

			pstmt = con.prepareStatement(jsql);
			rs = pstmt.executeQuery();
			
			while (rs.next()){
				continNum=rs.getInt(1);
				ELog.info(continNum);
				Contin c = new Contin(continNum);
				c.generateColor();
				ELog.info(c.colorcode);
				c.save();
			}
				
		
			rs.close();
			pstmt.close();
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}  
	}

}
