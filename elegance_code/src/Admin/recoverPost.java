package Admin;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.*;
import java.lang.*;

public class recoverPost{
	public static void main(String[] args) 
	{
	
             try{

				 Connection con = null;
	             
	             PreparedStatement pst = null,pst2= null;
	             ResultSet rs = null,rs1=null;
	             String jsql = null;
				 
				 Class.forName("com.mysql.jdbc.Driver").newInstance();
		         con = DriverManager.getConnection ( "jdbc:mysql://wormdesk1/elegance",  "root",  "worms" );
				 
		         jsql = "select OBJ_Name from object where (username='travis' or username='yi') and ( DateEntered='2008-02-06' or DateEntered='2008-02-07' ) and type <> 'cell' and type <> 'cell branch point' ";
		         pst = con.prepareStatement(jsql);
		         rs = pst.executeQuery();
		         while(rs.next())
		         {
		        	 int synID = rs.getInt(1);
		        	 String post = "";
		        	 pst2 = con.prepareStatement("select distinct partner from synrecord where synID=?");
		        	 pst2.setInt(1,synID);
		        	 rs1 = pst2.executeQuery();
		        	 int i = 0;
		        	 while (rs1.next())
		        	 {
		        		if (i==0) {
		        			         post = (rs1.getInt(1)+"").trim();
		        		          } 
		        		     else {
		        			        post = post+","+rs1.getInt(1);
		        			      };
					    i++;
		        	 }
		        	 
		        	 pst2 = con.prepareStatement("update object set toObj=? where OBJ_Name=?");
		        	 pst2.setString(1,post);
		        	 pst2.setInt(2,synID);
		        	 pst2.executeUpdate();
		        	 
				  
		         }
		
				
				 
				



               JOptionPane.showMessageDialog(null, "Done", "recovered ", JOptionPane.INFORMATION_MESSAGE);

		        }// end try

		        catch(SQLException e1){
		        e1.printStackTrace();
		        }

		        catch(java.lang.InstantiationException e2){
		        e2.printStackTrace();
		        }

	            catch(java.lang.IllegalAccessException e3){
		        e3.printStackTrace();
		        }
 
		        catch(ClassNotFoundException e4){
		        e4.printStackTrace();
		        }
            
        }  

}
