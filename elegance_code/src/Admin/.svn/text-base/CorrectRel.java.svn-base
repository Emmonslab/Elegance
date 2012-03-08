package Admin;

import java.sql.*;


public class CorrectRel{
	public static void main(String[] args) 
	{
		long time1 = System.currentTimeMillis();
		
		
	
             try{

				 Connection con = null;
	             Statement st = null;
	             PreparedStatement pst = null;
	             ResultSet rs = null,rs1=null;
	             String jsql = null;
				 Class.forName("com.mysql.jdbc.Driver").newInstance();
		         con = DriverManager.getConnection ( "jdbc:mysql://wormdesk1/elegance",  "root",  "worms" );
				
				
		         jsql = "select ObjName1,ObjName2,RelID from relationship";
		         st = con.createStatement();
		         rs = st.executeQuery(jsql);
		         while(rs.next())
		         {
		        	 int name1 = rs.getInt(1);
		        	 int name2 = rs.getInt(2);
		        	 int relid = rs.getInt(3);
		        	 int img1=0,img2=0;
		        	 //System.out.println("name1= "+name1);
		        	 pst = con.prepareStatement("select IMG_SectionNumber from image,object " +
		        	 		"where object.IMG_Number=image.IMG_Number and OBJ_Name=?");
		        	 pst.setInt(1, name1);
		        	 rs1=pst.executeQuery();
		        	 if (rs1.next())
		        		 {
		        	     img1 = rs1.getInt(1);
		        		 }else{System.out.println(relid);}
		        	 rs1.close();
		        	 pst.close();
		        	 
		        	 pst = con.prepareStatement("select IMG_SectionNumber from image,object " +
	        	 		"where object.IMG_Number=image.IMG_Number and OBJ_Name=?");
	        	     pst.setInt(1, name2);
	        	     rs1=pst.executeQuery();
	        	     if (rs1.next())
	        	     {
	        	    	 img2 = rs1.getInt(1);
	        	     }else{System.out.println(relid);}
	        	     rs1.close();
	        	     pst.close();
		        	 
	        		 if (img1>img2){
			             System.out.println("name1= "+name1+" name2="+name2);
			        	 pst = con.prepareStatement("update relationship set ObjName1=?, ObjName2=?  where RelID=?");
			        	 pst.setInt(1,name2);
			        	 pst.setInt(2,name1);
			        	 pst.setInt(3,relid);
			        	
			        	 pst.executeUpdate();
			        	 pst.close();
		        
		        	 }
				  
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
				System.out.println("It took "+time+". Done, please press Ctrl + C to close the program");
				
        }  

}
