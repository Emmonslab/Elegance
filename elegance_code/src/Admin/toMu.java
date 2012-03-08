package Admin;
import javax.swing.*;

import java.awt.*;
import java.sql.*;
import java.util.*;
import java.lang.*;

public class toMu{
	public static void main(String[] args) 
	{
		long time1 = System.currentTimeMillis();
		
		
	
             try{

				 Connection con = null;
	             Statement st = null,st1 = null;
	             PreparedStatement pst = null;
	             ResultSet rs = null, rs1 = null;
	             String jsql = null;
				 
				 Class.forName("com.mysql.jdbc.Driver").newInstance();
		         con = DriverManager.getConnection ( "jdbc:mysql://wormdesk1/elegance",  "root",  "worms" );
				 
		         jsql = "select CON_Number,relID from relationship,object where objName1=OBJ_Name";
		         st = con.createStatement();
		         rs = st.executeQuery(jsql);
		         while(rs.next())
		         {
		        	 int connum = rs.getInt(1);
		        	 int rel = rs.getInt(2);
		        	 pst = con.prepareStatement("update relationship set continNum=? where relID=?");
					 pst.setInt ( 1, connum );
				     pst.setInt ( 2, rel );
				    
				     
				     pst.executeUpdate();
				     pst.close();
		        	 
		         }
		         
		         
	        	 pst = con.prepareStatement("delete from multiple");
				 pst.executeUpdate();
				
		         jsql = "select OBJ_Name,fromObj,toObj from object where type='chemical' and toObj like '%,%'";
		         st = con.createStatement();
		         rs = st.executeQuery(jsql);
		         while(rs.next())
		         {
		        	 int msyn = rs.getInt(1);
		        	 int fromObj = 0;
		        	 
		        	 String from = rs.getString(2);
		        	 st1 = con.createStatement();
			         rs1 = st1.executeQuery("select CON_Number from object where OBJ_Name="+from);
			         if (rs1.next())
			         {
			        	 fromObj = rs1.getInt(1);
			         }
		        	 
		        	 
		        	 String toObj = rs.getString(3);
		        	 
		        	 String[] toObjs1 = toObj.split(",");
		        	 int[] toObjs = {0,0,0,0,0};
		        	 
		        	 for (int i=0; i < toObjs1.length ; i++)
		        		
		             {
		        		 
		        		 st1 = con.createStatement();
				         rs1 = st1.executeQuery("select CON_Number from object where OBJ_Name="+toObjs1[i]);
				         if (rs1.next())
				         {
				        	 toObjs[i] = rs1.getInt(1);
				         }
				         else { toObjs[i]=(-1)*Integer.parseInt(toObjs1[i]);}
		        		
		             }
		        	 

		        	 
		        	 
		        	 pst = con.prepareStatement("insert into multiple (OBJ_Name,fromObj,toObj1,toObj2,toObj3,toObj4,toObj5) values (?,?,?,?,?,?,?)");
					 pst.setInt ( 1, msyn );
				     pst.setInt ( 2, fromObj );
				     pst.setInt( 3, toObjs[0]);
				     pst.setInt( 4, toObjs[1]);
				     pst.setInt( 5, toObjs[2]);
				     pst.setInt( 6, toObjs[3]);
				     pst.setInt( 7, toObjs[4]);
				     
				     
				     pst.executeUpdate();
		        	 
		        	 
		        	 
		        	 
		
		          
				  
		         }
		
				
	

              
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
				System.out.println("It took "+time+" to make the new multiple table. Done, please press Ctrl + C to close the program");
				JOptionPane.showMessageDialog(null, time+" seconds have been used to to make the new multiple table. Done!", "done", JOptionPane.INFORMATION_MESSAGE);
            
        }  

}
