package Admin;


import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.*;
import java.lang.*;

public class ContinTran{
	public static void main(String[] args) 
	{
		
	
        String conNum = JOptionPane.showInputDialog ( null, "Enter a contin number");
        int continNum = Integer.parseInt(conNum);

		     try{
		        //SynapsesClear syns = new SynapsesClear(continNum);
				ObjRelClear syns2 = new ObjRelClear(continNum);

				JOptionPane.showMessageDialog(null, "Done", "Synapses and contin Clear"+continNum, JOptionPane.INFORMATION_MESSAGE);
				
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

		
             try{

				 Connection con = null,con2 = null;
	             Statement st = null;
	             PreparedStatement pst = null,pst2= null;
	             ResultSet rs = null,rs1=null,rs2=null;
	             String jsql = null;
				 int objNameNew=0,objNameOld=0,objNameNew1=0,objNameNew2=0,objNameOld1=0,objNameOld2=0,segmentNum=0;

				 Class.forName("com.mysql.jdbc.Driver").newInstance();
		         con = DriverManager.getConnection ( "jdbc:mysql://wormdesk3/elegancetravis",  "root",  "worms" );
				 con2 = DriverManager.getConnection ( "jdbc:mysql://localhost/elegance",  "root",  "worms" );
		         
				 
				 
				 st = con.createStatement();
		         jsql = "select * from object where CON_Number="+continNum;
		         rs = st.executeQuery(jsql);
		         while(rs.next())
		         {
					 
					int  x = rs.getInt("OBJ_X");
					int  y = rs.getInt("OBJ_Y");
					String imgNumber = rs.getString("IMG_Number");
					String type = rs.getString("type");
					String username = rs.getString("username");
					String date = rs.getString("DateEntered");
					objNameOld = rs.getInt("OBJ_Name");

					
					 
					 pst = con2.prepareStatement("insert into object (OBJ_X,OBJ_Y,IMG_Number,type,username,DateEntered,CON_Number) values (?,?,?,?,?,?,?)");
					 pst.setInt ( 1, x );
				     pst.setInt ( 2, y );
				     pst.setString ( 3, imgNumber );
					 pst.setString(4, type);
					 pst.setString ( 5, username );
				     pst.setString ( 6, date );
					 pst.setInt ( 7, continNum );
				     pst.executeUpdate();
				     

					 pst = con2.prepareStatement ( "select OBJ_Name from object where OBJ_X=? and OBJ_Y=? and IMG_Number=?" );
			         pst.setInt ( 1, x );
				     pst.setInt ( 2, y );
				     pst.setString ( 3, imgNumber );
                     rs1 = pst.executeQuery (  );
			         while ( rs1.next (  ) )
						 {
						  objNameNew = rs1.getInt(1);
					     }
				     
					 
					 pst = con.prepareStatement("insert into tran (objNameOld, objNameNew) values (?,?)");
					 pst.setInt ( 1, objNameOld );
				     pst.setInt ( 2, objNameNew );
				     pst.executeUpdate();
					 
					 
					 pst.close();


				 

				 }
		        
          
                JOptionPane.showMessageDialog(null, "Done", "objects copied"+continNum, JOptionPane.INFORMATION_MESSAGE);

// insert the new connectivity
				
				
                st = con.createStatement();
		        jsql = "select * from object where CON_Number="+continNum;
		        rs = st.executeQuery(jsql);
		        while(rs.next())
		         {
					 
					
					objNameOld1 = rs.getInt("OBJ_Name");

					pst = con.prepareStatement ( "select * from relationship where objName1=?" );
			        pst.setInt ( 1, objNameOld1 );
				    rs1 = pst.executeQuery (  );
			         while ( rs1.next (  ) )
						 {
						  objNameOld2 = rs1.getInt("objName2");
						  segmentNum = rs1.getInt("segmentNum");

						  pst2 = con.prepareStatement ( "select objNameNew from tran where objNameOld=?" );
			              pst2.setInt ( 1, objNameOld1  );
                          rs2 = pst2.executeQuery (  );
			              while ( rs2.next (  ) )
						  {
						  objNameNew1 = rs2.getInt(1);
					      }
						  
						  pst2 = con.prepareStatement ( "select objNameNew from tran where objNameOld=?" );
			              pst2.setInt ( 1, objNameOld2  );
				          rs2 = pst2.executeQuery (  );
			              while ( rs2.next (  ) )
						  {
						  objNameNew2 = rs2.getInt(1);
					      }

						  pst2 = con2.prepareStatement("insert into relationship (objName1, objName2, segmentNum) values (?,?,?)");
						  //pst2 = con.prepareStatement("delete from relationship where objName1=? and objName2=? and segmentNum=?");
					      pst2.setInt ( 1, objNameNew1 );
				          pst2.setInt ( 2, objNameNew2 );
					      pst2.setInt ( 3, segmentNum );
				          pst2.executeUpdate();
					     
						 }

					
					
				     
					 
					

				 

				 }


               JOptionPane.showMessageDialog(null, "Done", "connectivity copied"+continNum, JOptionPane.INFORMATION_MESSAGE);

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
            
        }  

}
