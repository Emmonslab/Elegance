package Admin;
import javax.swing.*;

import java.awt.*;
import java.sql.*;
import java.util.*;
import java.lang.*;

public class delObjRelByImage{
	public static void main(String[] args) 
	{
		long time1 = System.currentTimeMillis();
		
		
	
             try{

				 Connection con = null;
	             Statement st = null,st1 = null;
	             PreparedStatement pst = null,pst1 = null;
	             ResultSet rs = null, rs1 = null;
	             String jsql = null;
				 
				 Class.forName("com.mysql.jdbc.Driver").newInstance();
		         con = DriverManager.getConnection ( "jdbc:mysql://192.168.1.3/n930",  "root",  "worms" );
				 
		         jsql = "select IMG_Number from image where IMG_Series='dorsal' and IMG_PrintNumber<4498";
		         st = con.createStatement();
		         rs = st.executeQuery(jsql);
		         while(rs.next())
		         {
		        	 
		        	 String img = rs.getString(1);
		        	 //delete image
			    	
		        	 
		        	 pst = con.prepareStatement("select OBJ_Name,CON_Number,type from object where IMG_Number=?");
					 pst.setString ( 1, img );
				    
				     rs1 = pst.executeQuery();
				     while (rs1.next())
				     {
				    	 int obj = rs1.getInt(1);
				    	 int conNum = rs1.getInt(2);
				    	 String type = rs1.getString(3);
				    	
						 //delete from object
				    	 pst1 = con.prepareStatement("delete from object where OBJ_Name=?");
				    	 pst1.setInt(1,obj);
						 pst1.executeUpdate();
						 pst1.close();
						// delete from relationship
							 pst1 = con.prepareStatement("delete from relationship where objName1=? or objName2=?");
					    	 pst1.setInt(1,obj);
					    	 pst1.setInt(2,obj);
							 pst1.executeUpdate();
							 pst1.close();
						
				    	 
				     }
				     rs1.close();
				     pst.close();
		        	 
		         }
		         rs.close();
		         st.close();
		         
	        	
		
				
	

              
		        }

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
		        
		        long time2 = System.currentTimeMillis();
				long time = (time2-time1)/1000;
				System.out.println("It took "+time+" to delete images and all associated data. Done, please press Ctrl + C to close the program");
				
        }  

}
