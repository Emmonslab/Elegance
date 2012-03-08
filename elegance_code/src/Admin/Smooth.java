package Admin;

import javax.swing.*;
import java.util.Vector;
import java.sql.*;


//   PAG_3D renders a single, interactively rotatable,
//   traslatable, and zoomable ColorCube object.

public class Smooth {
  
  String neuron = JOptionPane.showInputDialog ( null, "Enter the neuron names");
  
  String[] neurons =neuron.split(",");
  int[] continNums = new int[neurons.length];
  
  public Smooth() throws SQLException, ClassNotFoundException, java.lang.InstantiationException, java.lang.IllegalAccessException{
  
  Connection con = null;
  Statement st = null;

  PreparedStatement pst = null,pstmt=null,pstmt0=null, pstmt1=null,pstmt2=null;
  String jsql=null, jsql0=null,jsql1=null, jsql2= null,type=null;
  ResultSet rs = null, rs0=null,rs1= null, rs2 = null;
 
	

  try{    
			    Class.forName("com.mysql.jdbc.Driver").newInstance();		
			     con = DriverManager.getConnection ( DatabaseProperties.CONNECTION_STRING,  DatabaseProperties.USERNAME,  DatabaseProperties.PASSWORD );

                
                st = con.createStatement ();
				for (int i=0;i<neurons.length;i++)
				{
                  
                   rs = st.executeQuery("select continNum from continmap where continName='"+neurons[i]+"'");
			       while (rs.next())
				    {
			          continNums[i] = rs.getInt(1);
					  
                    }
				    
				    rs.close();
					
				}
               
			   

    } catch (SQLException e) {
    throw e;
    } finally {
    if (rs != null) rs.close();
    if (st != null) st.close();
	if (con != null) con.close();
        }


  
	
	
	try{
    Class.forName("com.mysql.jdbc.Driver").newInstance();		
	 con = DriverManager.getConnection ( DatabaseProperties.CONNECTION_STRING,  DatabaseProperties.USERNAME,  DatabaseProperties.PASSWORD );
	for(int i=0;i<continNums.length;i++)
	   {
	
       jsql = "delete from tempobj where continNum="+continNums[i];
	   pstmt = con.prepareStatement(jsql);
	   pstmt.executeUpdate();
	   
	   
	   jsql = "select OBJ_Name,type,OBJ_X,OBJ_Y,IMG_SectionNumber from object,image where CON_Number="+continNums[i]+" and object.IMG_Number=image.IMG_Number";
	   pstmt = con.prepareStatement(jsql);
	   rs = pstmt.executeQuery();
       while (rs.next())
       {
		   int tempObj = rs.getInt(1);
           String tempType = rs.getString(2);
		   int tempX = rs.getInt(3);
		   int tempY = rs.getInt(4);
		   int tempZ = rs.getInt(5);
		   
		   jsql0 ="insert into tempobj (objName,type,x,y,z,continNum) values ("+tempObj+",'"+tempType+"',"+tempX+","+tempY+","+tempZ+","+continNums[i]+")";
		   pstmt0 = con.prepareStatement(jsql0);
	       pstmt0.executeUpdate();

       } // end of white
	  
       smooth(continNums[i]);
	   } // end of for

} catch (SQLException e) {
    throw e;
    } finally {
    if (rs != null) rs.close();
    if (st != null) st.close();
	if (con != null) con.close();
        }

  

} // end of constructor



 public void smooth(int continN)throws SQLException, ClassNotFoundException, java.lang.InstantiationException, java.lang.IllegalAccessException 
	  
  {
      Connection con = null;
      PreparedStatement pst = null,pstmt=null, pstmt1=null,pstmt2=null;
      String jsql=null,jsql1=null, jsql2= null,type1=null,type2=null;
      ResultSet rs = null, rs1= null, rs2 = null;
	  int sN=0, continNum=continN, x0=0, x1=0, x2=0, y0=0,y1=0, y2=0;
      System.out.println(continNum);
	  try{    
	  Class.forName("com.mysql.jdbc.Driver").newInstance();		
	  con = DriverManager.getConnection ( DatabaseProperties.CONNECTION_STRING,  DatabaseProperties.USERNAME,  DatabaseProperties.PASSWORD );
	  
	  

	  jsql = "select distinct segmentNum from object,relationship where object.CON_Number="+continNum+" and relationship.ObjName1=object.OBJ_Name order by segmentNum"; 
      pstmt = con.prepareStatement(jsql);
      rs = pstmt.executeQuery();
	  while (rs.next())
	  {
	  sN = rs.getInt(1);
	  System.out.println(sN);
	  for (int i=0;i<5 ;i++ )
	  {
	 
//from the begin to the end
	  jsql1 = "select ObjName1,ObjName2 from object,relationship where object.CON_Number="+continNum+" and relationship.ObjName1=object.OBJ_Name and segmentNum="+sN+" order by object.IMG_Number";
    
      pstmt1 = con.prepareStatement(jsql1);
      rs1 = pstmt1.executeQuery();
	  while(rs1.next())
     	{
	      int objName1 = rs1.getInt(1);
		  int objName2 = rs1.getInt(2);
		  
		  jsql2 = "select x,y,type from tempobj where objName="+ objName1;
		  pstmt2 = con.prepareStatement(jsql2);
		  rs2 = pstmt2.executeQuery();
		  rs2.next();
		  x1 = rs2.getInt(1);
		  y1 = rs2.getInt(2);
		  type1 = rs2.getString(3);

		  jsql2 = "select x,y,type from tempobj where objName="+ objName2;
		  pstmt2 = con.prepareStatement(jsql2);
		  rs2 = pstmt2.executeQuery();
		  rs2.next();
		  x2 = rs2.getInt(1);
		  y2 = rs2.getInt(2);
		  type2 = rs2.getString(3);

         if (type2.equals("cell branch point"))
		  { 
            x1= (int) (x1+x2)/2;
            y1= (int) (y1+y2)/2;
			jsql2 = "update tempobj set x="+x1+", y="+y1+" where objName="+objName1;
		    pstmt2 = con.prepareStatement(jsql2);
		    pstmt2.executeUpdate();
		  }
		  
		  else
		  {
           	x1= (int) (x1+x2)/2;
            y1= (int) (y1+y2)/2;
			jsql2 = "update tempobj set x="+x2+", y="+y2+" where objName="+objName2;
		    pstmt2 = con.prepareStatement(jsql2);
		    pstmt2.executeUpdate();
		  
		  }
          
		  
         
		  
		}// end of while
        rs1.close();
		pstmt1.close();

//from the end to begin
	  jsql1 = "select ObjName1,ObjName2 from object,relationship where object.CON_Number="+continNum+" and relationship.ObjName1=object.OBJ_Name and segmentNum="+sN+" order by object.IMG_Number desc";
    
      pstmt1 = con.prepareStatement(jsql1);
      rs1 = pstmt1.executeQuery();
	  while(rs1.next())
     	{
	      int objName1 = rs1.getInt(1);
		  int objName2 = rs1.getInt(2);
		  
		  jsql2 = "select x,y,type from tempobj where objName="+ objName1;
		  pstmt2 = con.prepareStatement(jsql2);
		  rs2 = pstmt2.executeQuery();
		  rs2.next();
		  x1 = rs2.getInt(1);
		  y1 = rs2.getInt(2);
		  type1 = rs2.getString(3);

		  jsql2 = "select x,y,type from tempobj where objName="+ objName2;
		  pstmt2 = con.prepareStatement(jsql2);
		  rs2 = pstmt2.executeQuery();
		  rs2.next();
		  x2 = rs2.getInt(1);
		  y2 = rs2.getInt(2);
		  type2 = rs2.getString(3);

         if (type1.equals("cell branch point"))
		  { 
            x1= (int) (x1+x2)/2;
            y1= (int) (y1+y2)/2;
			jsql2 = "update tempobj set x="+x2+", y="+y2+" where objName="+objName2;
		    pstmt2 = con.prepareStatement(jsql2);
		    pstmt2.executeUpdate();
		  }
		  
		  else
		  {
           x1= (int) (x1+x2)/2;
            y1= (int) (y1+y2)/2;
			jsql2 = "update tempobj set x="+x1+", y="+y1+" where objName="+objName1;
		    pstmt2 = con.prepareStatement(jsql2);
		    pstmt2.executeUpdate();
		  
		  }
          
		}//end of for
         
		  
		}// end of while
        rs1.close();
		pstmt1.close();
	   
	   
	  }// end of while
      rs.close();
	  pstmt.close();

	  } // end of try
	   catch (SQLException e) {
       throw e;
      } finally {
        if (rs != null) rs.close();
        if (pstmt != null) pstmt.close();
	    if (con != null) con.close();
      }
  
  }// end of smooth method




} // end of class Smooth



