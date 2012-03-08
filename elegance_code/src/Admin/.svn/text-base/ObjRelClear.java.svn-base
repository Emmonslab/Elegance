package Admin;



import java.sql.*;
import java.util.*;
import java.lang.*;
import java.util.Vector;
import javax.swing.*;
import java.awt.*;
import java.util.Arrays;


class ObjRelClear
{
	int continNum;
	String continN;
	Vector synapses;
	public ObjRelClear (int conNum)throws SQLException,
    ClassNotFoundException, java.lang.InstantiationException, java.lang.IllegalAccessException 
	{
	
	continNum = conNum;
    synapses = new Vector();
	Connection con = null;
	Statement st = null, st1 = null;
	PreparedStatement pst = null, pst1 = null,pst2=null;
	ResultSet rs = null, rs1 = null,rs2=null;
	String jsql = null, jsql1 = null, type = null, fromObj = null, toObj = null, imgNum = null, continName = null, temp = null;
	int x= 0, segNum = 0,synName=0,objX=0,objY=0,fromOb=0,toOb=0;

	
        try
	    {
	  	Class.forName("com.mysql.jdbc.Driver").newInstance();
		con = DriverManager.getConnection ( DatabaseProperties.CONNECTION_STRING,  DatabaseProperties.USERNAME,  DatabaseProperties.PASSWORD );
		jsql = "select OBJ_Name from object where CON_Number="+continNum;
		st = con.createStatement();
		rs = st.executeQuery(jsql);
		while(rs.next())
		{
		
		        int objName = rs.getInt(1);
		
				pst = con.prepareStatement("delete from relationship where objName1="+objName+" or objName2="+objName);
				pst.executeUpdate();
				pst.close();

				pst = con.prepareStatement("delete from object where OBJ_Name="+objName);
				pst.executeUpdate();
				pst.close();
		}
		rs.close();
		st.close();
		
		pst = con.prepareStatement("delete from contin where CON_Number="+continNum);
		pst.executeUpdate();
		pst.close();		

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