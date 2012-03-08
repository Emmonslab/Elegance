package Admin;



import java.sql.*;
import java.util.*;
import java.lang.*;
import java.util.Vector;
import javax.swing.*;
import java.awt.*;
import java.util.Arrays;


class RecordClear
{
	
	public RecordClear ()throws SQLException,
    ClassNotFoundException, java.lang.InstantiationException, java.lang.IllegalAccessException 
	{
	
	
	Connection con = null;
	
	PreparedStatement pst = null, pst1 = null;
	ResultSet rs = null;
	
		try{    
			    Class.forName("com.mysql.jdbc.Driver").newInstance();		
			    con = DriverManager.getConnection ( DatabaseProperties.CONNECTION_STRING,  DatabaseProperties.USERNAME,  DatabaseProperties.PASSWORD );
			
				pst = con.prepareStatement("select a.idx from synrecord a where a.idx < ( select max(b.idx) from synrecord b where a.synID=b.synID and a.partner=b.partner and a.username=b.username ) ");
				rs = pst.executeQuery();
				while (rs.next())
				{
					int idx= rs.getInt(1);
					pst1 = con.prepareStatement("delete from synrecord where idx=?");
					pst1.setInt(1,idx);
					pst1.executeUpdate();
					pst1.close();
					System.out.println(idx+" deleted");
				}
				//int count=rs.getRow();
				//System.out.println(count+" records deleted,done!");
				
			    
				}catch (SQLException e) 
	            {
                e.printStackTrace (  );
                } 
	            finally 
	            {
                if (con != null) con = null;
                }
				


   }

   
}