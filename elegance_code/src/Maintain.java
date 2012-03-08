
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
public class Maintain {
	
	public Maintain() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
	{
		Connection con = null;
		Statement st = null;
		PreparedStatement pst = null;
		String jsql=null;
		ResultSet rs = null;
		        
		                try{    
					    	
						
					     con = EDatabase.borrowConnection (    );
				        jsql = "select relID from relationship LEFT JOIN object on object.OBJ_Name=relationship.ObjName1 where object.OBJ_Name IS NULL";
					    st = con.createStatement();
					    rs = st.executeQuery(jsql);
		                while(rs.next())
					    {
					    int relID = rs.getInt(1);
						deleteRel(relID);
					    }
					    }catch (SQLException e) 
			            {
		                e.printStackTrace (  );
		                } 
			            finally 
			            {
		                if (rs != null) rs.close();
		                if (st != null) st.close();
		                } 

					    try{    
					    		
					     con = EDatabase.borrowConnection (    );
				        jsql = "select relID from relationship LEFT JOIN object on object.OBJ_Name=relationship.ObjName2 where object.OBJ_Name IS NULL";
					    st = con.createStatement();
					    rs = st.executeQuery(jsql);
		                while(rs.next())
					    {
					    int relID = rs.getInt(1);
						deleteRel(relID);
					    }
					    }catch (SQLException e) 
			            {
		                e.printStackTrace (  );
		                } 
			            finally 
			            {
		                if (rs != null) rs.close();
		                if (st != null) st.close();
		                }
				
		
	}
	 public static void deleteRel(int relID)throws SQLException,
		ClassNotFoundException, java.lang.InstantiationException, java.lang.IllegalAccessException
			{
				Connection con = null;
		        Statement st = null;      
				String jsql = null;
				int relID2 = relID;
			            try{
						 con = EDatabase.borrowConnection (    );
					    	
				        jsql = "delete from relationship where relID="+relID2;
					    st = con.createStatement();
					    st.executeUpdate(jsql);
		               
					    }catch (SQLException e) 
			            {
		                e.printStackTrace (  );
		                } 
			            finally 
			            {
		                
		                if (st != null) st.close();
		                }
			}	
		
}
