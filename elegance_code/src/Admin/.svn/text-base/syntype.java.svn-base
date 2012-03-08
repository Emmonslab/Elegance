package Admin;



import java.sql.*;

public class syntype{
	public static void main(String[] args) throws SQLException,
	ClassNotFoundException, java.lang.InstantiationException,
	java.lang.IllegalAccessException 
	{
    Class.forName("com.mysql.jdbc.Driver").newInstance();
	Connection con = DriverManager.getConnection(
			                         DatabaseProperties.CONNECTION_STRING,
			                         DatabaseProperties.USERNAME, DatabaseProperties.PASSWORD);
	String jsql;
	PreparedStatement pstmt,pstmt1;
	ResultSet rs,rs1;
	
	String[] muscles = {"adp","ailL","ailR","[ailR]","aobL","[aobL]","aobR","cdlL","cdlR","dBWM_","dBWML24","dBWML23","dBWML22","dBWMR24"," dBWMR23","dglL7","dglL6","dglL5","dglL3","dglL2","dglL1","dglR8","dglR7","dglR6","dglR5","dglR3","dglR2","dglR1","dspL","dspR"," dsrL","dsrR","gecL","gecR","grtL","grtR","intL","intR","mus","[mus]","pilL","pilR","poblL","pobR","sph","vBWML_","vBWML23"," vBWML22","vBWML21","vBWML20","vBWML19","vBWML17","vBWMR_","[vBWMR23]","vBWMR24"," vBWMR23"," vBWMR22"," vBWMR21"," vBWMR20"," vBWMR19","vspL"," vspR","vsrL","vsrR"}; 
	for (int i=0;i<muscles.length;i++)
	  {
		 
	    
	     jsql = "update contin set type='muscle' where CON_AlternateName=?";
	     pstmt = con.prepareStatement(jsql);
	     pstmt.setString(1,muscles[i]);
	     pstmt.executeUpdate();
	     pstmt.close();
	    
	  }
	
	
    
   
    
	
	}
	


}
