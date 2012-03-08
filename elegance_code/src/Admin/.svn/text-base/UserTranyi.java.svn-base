package Admin;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.*;
import java.lang.*;

public class UserTranyi{
	public static void main(String[] args) 
	{
		
	
        String username = "yi";
        

		     

		
             try{

				 Connection con = null,con2 = null;
	             Statement st = null;
	             PreparedStatement pst = null,pst2= null;
	             ResultSet rs = null,rs1=null,rs2=null;
	             String jsql = null;
				 int objNameNew=0,objNameOld=0,objNameNew1=0,objNameNew2=0,objNameOld1=0,objNameOld2=0,segmentNum=0;

				 Class.forName("com.mysql.jdbc.Driver").newInstance();
		         con = DriverManager.getConnection ( "jdbc:mysql://wormdesk3/eleganceyiwang",  "root",  "worms" );
				 con2 = DriverManager.getConnection ( "jdbc:mysql://wormdesk3/elegance",  "root",  "worms" );


				
		         jsql = "select OBJ_Name from object where username='"+username+"' and DateEntered='2007-10-22'";
		         st = con2.createStatement();
		         rs = st.executeQuery(jsql);
		         while(rs.next())
		         {
		
		          int objName = rs.getInt(1);
				  pst = con2.prepareStatement("delete from relationship where objName1="+objName+" or objName2="+objName);
				  pst.executeUpdate();
				  

				  pst = con2.prepareStatement("delete from object where OBJ_Name="+objName);
				  pst.executeUpdate();
				  
		         }
		
				
				 
				 st = con.createStatement();
		         jsql = "select * from object where username='"+username+"' and type<>'chemical' and type<>'electrical' and DateEntered='2007-10-22'";
		         rs = st.executeQuery(jsql);
		         while(rs.next())
		         {
					 
					int  x = rs.getInt("OBJ_X");
					int  y = rs.getInt("OBJ_Y");
					String imgNumber = rs.getString("IMG_Number");
					String type = rs.getString("type");
					int continNum = rs.getInt("CON_Number");
					String date = rs.getString("DateEntered");
					objNameOld = rs.getInt("OBJ_Name");
					int checked = rs.getInt("checked");

					 pst = con2.prepareStatement("delete from object where OBJ_X=? and OBJ_Y=? and IMG_Number=?");
					 pst.setInt ( 1, x );
				     pst.setInt ( 2, y );
				     pst.setString ( 3, imgNumber );
					 pst.executeUpdate();
					 
					 pst = con2.prepareStatement("insert into object (OBJ_X,OBJ_Y,IMG_Number,type,username,DateEntered,CON_Number,checked) values (?,?,?,?,?,?,?,?)");
					 pst.setInt ( 1, x );
				     pst.setInt ( 2, y );
				     pst.setString ( 3, imgNumber );
					 pst.setString(4, type);
					 pst.setString ( 5, username );
				     pst.setString ( 6, date );
					 pst.setInt ( 7, continNum );
					  pst.setInt ( 8, checked );
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
		        
          
                JOptionPane.showMessageDialog(null, "Done", "objects copied "+username, JOptionPane.INFORMATION_MESSAGE);

// insert the new connectivity
				
				
                st = con.createStatement();
		        jsql = "select * from object where username='"+username+"' and type<>'chemical' and type<>'electrical' and DateEntered='2007-10-22'";
		        rs = st.executeQuery(jsql);
		        while(rs.next())
		         {
					 
					
					objNameOld1 = rs.getInt("OBJ_Name");
					
					pst2 = con.prepareStatement ( "select objNameNew from tran where objNameOld=?" );
			        pst2.setInt ( 1, objNameOld1  );
                    rs2 = pst2.executeQuery (  );
			         if (rs2.next())
			              {
							  objNameNew1 = rs2.getInt(1);
			              }

					pst = con.prepareStatement ( "select * from relationship where objName1=?" );
			        pst.setInt ( 1, objNameOld1 );
				    rs1 = pst.executeQuery (  );
			         while ( rs1.next (  ) )
						 {
						  objNameOld2 = rs1.getInt("objName2");
						  segmentNum = rs1.getInt("segmentNum");

						  pst2 = con.prepareStatement ( "select objNameNew from tran where objNameOld=?" );
			              pst2.setInt ( 1, objNameOld2  );
                          rs2 = pst2.executeQuery (  );
			              if (rs2.next())
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

				 }//end of while rs
               JOptionPane.showMessageDialog(null, "Done", "connectivity copied "+username, JOptionPane.INFORMATION_MESSAGE);
              
			   //update synapses record

			    st = con.createStatement();
		        jsql = "select * from object where username='"+username+"' and type<>'cell' and type<>'cell branch point' and DateEntered='2007-10-22'";
		        rs = st.executeQuery(jsql);
		        while(rs.next())
		         {
					String preS = rs.getString("fromObj");
					String post = rs.getString("toObj");
                    int  x = rs.getInt("OBJ_X");
					int  y = rs.getInt("OBJ_Y");
					String imgNumber = rs.getString("IMG_Number");
					String type = rs.getString("type");
					int continNum = rs.getInt("CON_Number");
					String date = rs.getString("DateEntered");
					objNameOld = rs.getInt("OBJ_Name");
					
					int checked = rs.getInt("checked");

					System.out.println ( "synapse name = " + objNameOld + "  " + post + "  "+ preS );

					 pst = con2.prepareStatement("delete from object where OBJ_X=? and OBJ_Y=? and IMG_Number=?");
					 pst.setInt ( 1, x );
				     pst.setInt ( 2, y );
				     pst.setString ( 3, imgNumber );
					 pst.executeUpdate();

					 //convert pre and post
					 int pre = Integer.parseInt(preS);
					 pst2 = con.prepareStatement ( "select objNameNew from tran where objNameOld=?" );
			         pst2.setInt ( 1, pre  );
                     rs2 = pst2.executeQuery (  );
			         if (rs2.next())
			         {
						 pre = rs2.getInt(1);
			         } 
					 if (post.indexOf(",")==-1)
						 {

						 int postid = Integer.parseInt(post);
					     pst2 = con.prepareStatement ( "select objNameNew from tran where objNameOld=?" );
			             pst2.setInt ( 1, postid  );
                         rs2 = pst2.executeQuery (  );
			             if (rs2.next())
			              {
						    postid = rs2.getInt(1);
			              }
						  
						 post = Integer.toString(postid);


					 
					     }else{
					      String[] postmember = post.split(",");
					      post = "";
					      int[] postid = new int[postmember.length];
                          for (int i=0; i < postmember.length ; i++)

                          {
                           postid[i] = Integer.parseInt(postmember[i]);
						   pst2 = con.prepareStatement ( "select objNameNew from tran where objNameOld=?" );
			               pst2.setInt ( 1, postid[i]  );
                           rs2 = pst2.executeQuery (  );
			               if (rs2.next())
			                {
						      postid[i] = rs2.getInt(1);			              
						    }
						    post = post+","+postid[i];
                           }
					 }
					 preS = Integer.toString(pre);
						  


					 
					 pst = con2.prepareStatement("insert into object (OBJ_X,OBJ_Y,IMG_Number,type,username,DateEntered,CON_Number,checked,fromObj,toObj) values (?,?,?,?,?,?,?,?,?,?)");
					 pst.setInt ( 1, x );
				     pst.setInt ( 2, y );
				     pst.setString ( 3, imgNumber );
					 pst.setString(4, type);
					 pst.setString ( 5, username );
				     pst.setString ( 6, date );
					 pst.setInt ( 7, continNum );
					 pst.setInt ( 8, checked );
					 pst.setString ( 9, preS );
					 pst.setString ( 10, post );
				     pst.executeUpdate();



					 
					
					

				 }//end of while rs
 



               JOptionPane.showMessageDialog(null, "Done", "synapse updated "+username, JOptionPane.INFORMATION_MESSAGE);

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
