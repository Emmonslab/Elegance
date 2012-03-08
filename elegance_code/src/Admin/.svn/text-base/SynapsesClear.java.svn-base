package Admin;

import java.sql.*;
import java.util.*;
import java.lang.*;
import java.util.Vector;
import javax.swing.*;
import java.awt.*;
import java.util.Arrays;


class SynapsesClear
{
	/**
	int continNum;
	String continN;
	Vector synapses;
	public SynapsesClear (int conNum)throws SQLException,
    ClassNotFoundException, java.lang.InstantiationException, java.lang.IllegalAccessException 
	{
	DatabaseProperties.loadProperties();
	continNum = conNum;
    synapses = new Vector();
	Connection con = null;
	Statement st = null, st1 = null;
	PreparedStatement pst = null, pst1 = null,pst2=null;
	ResultSet rs = null, rs1 = null,rs2=null;
	String jsql = null, jsql1 = null, type = null, fromObj = null, toObj = null, imgNum = null, continName = null, temp = null;
	int x= 0, segNum = 0,synName=0,objX=0,objY=0,fromOb=0,toOb=0;

	//String mask = JOptionPane.showInputDialog ( null, "Enter one contin name to be filtered");
	String mask = "";
    System.out.println("start to collect all the synapses from this contin!  ----total 0% completed!!!");
    // select all the synapses from this contin 
        try
	    {
	  	Class.forName("com.mysql.jdbc.Driver").newInstance();
		 con = DriverManager.getConnection ( DatabaseProperties.CONNECTION_STRING,  DatabaseProperties.USERNAME,  DatabaseProperties.PASSWORD );
		st = con.createStatement();
		jsql = "select a.type,a.fromObj,a.toObj,a.OBJ_Name,a.OBJ_X,a.OBJ_Y,a.IMG_Number from object as a,object as b where (a.type='chemical' or a.type='electrical') and b.CON_Number="+continNum+" and a.fromObj=b.OBJ_Name";
		rs = st.executeQuery(jsql);
		while(rs.next())
		{
		
		continName = "";
		fromObj = rs.getString(2);
		toObj = rs.getString(3);
		type = rs.getString(1);
		synName = rs.getInt(4);
		objX = rs.getInt(5);
		objY = rs.getInt(6);
		imgNum = rs.getString(7);
		if (type.equals("electrical")) 
			{
			type = "e";
			}
			else
			{
			type = "out";
			}
	
		if (toObj.indexOf(",")==-1) 
			{
			try{
		       jsql1 = "select CON_AlternateName,contin.CON_Number from contin,object where object.OBJ_Name='" + toObj + "' and object.CON_Number=contin.CON_Number";
			   st1 = con.createStatement();
			   rs1 = st1.executeQuery(jsql1);
               if(rs1.next())
			   {
			   temp = rs1.getString(1);
			   if (temp!=null && (!temp.equals("")))
			   {
			   continName = rs1.getString(1);
			   }
			   else
			   {
			   continName = rs1.getString(2);
			   }
			   }else{
				   continName = "obj"+toObj;
				   }
			   }catch (SQLException e) 
	           {
               e.printStackTrace (  );
               } 
	           finally 
	           {
               if (rs1 != null) rs1.close();
               if (pst1 != null) pst1.close();
               } 
		    }
		else{
			type = "mout";
			String[] toObjs1 = toObj.split(",");
			int[] toContins = new int[toObjs1.length];
            int[] toObjs = new int[toObjs1.length];
            for (int i=0; i < toObjs.length ; i++)

            {
             toObjs[i] = Integer.parseInt(toObjs1[i]);
            }

            //Arrays.sort(toObjs);

           for (int i=0; i<toObjs.length; i++)
			   {
				try{
		        jsql1 = "select CON_Number from object where OBJ_Name=" + toObjs[i];
			    st1 = con.createStatement();
			    rs1 = st1.executeQuery(jsql1);
                while(rs1.next())
			    {
			    toContins[i] = rs1.getInt(1);
				
			    }
			    }catch (SQLException e) 
	            {
                e.printStackTrace (  );
                } 
	            finally 
	            {
                if (rs1 != null) rs1.close();
                if (pst1 != null) pst1.close();
                } 
			   }
            Arrays.sort(toContins);
         

			for (int i=0; i<toObjs.length; i++)
			   {
                try{
				
		        jsql1 = "select CON_Alternatename from contin where CON_Number="+toContins[i];
			    st1 = con.createStatement();
			    rs1 = st1.executeQuery(jsql1);
               if(rs1.next())
			   {
               temp = rs1.getString(1);
			   if (temp!=null && (!temp.equals("")))
			   {
			   continName = continName+rs1.getString(1)+",";
			   }else
			   {
				   continName = continName+toContins[i]+",";
			   }
			   }else{
				   continName = continName+"obj"+toObjs[i]+",";
				   }
			    }catch (SQLException e) 
	            {
                e.printStackTrace (  );
                } 
	            finally 
	            {
                if (rs1 != null) rs1.close();
                if (pst1 != null) pst1.close();
                } 
				
			   }
			}
		if ((!continName.equals(mask))&&(!continName.equals(continN)))
		{   

            try{
				
		        jsql1 = "select IMG_SectionNumber from image where IMG_Number='"+imgNum+"'";
			    st1 = con.createStatement();
			    rs1 = st1.executeQuery(jsql1);
                if(rs1.next()) x=rs1.getInt(1);
			    
              
			    }catch (SQLException e) 
	            {
                e.printStackTrace (  );
                } 
	            finally 
	            {
                if (rs1 != null) rs1.close();
                if (pst1 != null) pst1.close();
                } 

			try{
				
		        jsql1 = "select min(segmentNum) from relationship where (objName1=" + fromObj +" or objName2="+fromObj+")";
			    st1 = con.createStatement();
			    rs1 = st1.executeQuery(jsql1);
                if(rs1.next()) segNum = rs1.getInt(1);
			    
              
			    }catch (SQLException e) 
	            {
                e.printStackTrace (  );
                } 
	            finally 
	            {
                if (rs1 != null) rs1.close();
                if (pst1 != null) pst1.close();
                } 

			Synapse2 syn = new Synapse2(continName,x,x,segNum,type,synName,objX,objY);
		    synapses.add(syn);
		}
		

	    }
		}
	    catch (SQLException e) 
	    {
        e.printStackTrace (  );
        } 
	    finally 
	    {
        if (rs != null) rs.close();
        if (pst != null) pst.close();
	    if (con != null) con.close();
        }
    System.out.println("start to collect all the simple synapses to this contin!  ----total 30% completed!!!");
   //get all the  single synapse to this contin

        try
	    {
	  	Class.forName("com.mysql.jdbc.Driver").newInstance();
		 con = DriverManager.getConnection ( DatabaseProperties.CONNECTION_STRING,  DatabaseProperties.USERNAME,  DatabaseProperties.PASSWORD );
		st = con.createStatement();
		
		jsql = "select a.type,a.fromObj,a.toObj,a.OBJ_Name,a.OBJ_X,a.OBJ_Y,a.IMG_Number from object as a,object as b where (a.type='chemical' or a.type='electrical') and b.CON_Number="+continNum+" and a.toObj=b.OBJ_Name";
		rs = st.executeQuery(jsql);
		while(rs.next())
		{
		toObj = rs.getString(3);
		if ( toObj.indexOf(",")>-1){}
		else{
		continName = "";
		fromObj = rs.getString(2);
		
		type = rs.getString(1);
		synName = rs.getInt(4);
		objX = rs.getInt(5);
		objY = rs.getInt(6);
		imgNum = rs.getString(7);
		
		if (type.equals("electrical")) 
			{
			type = "e";
			}
			else
			{
			type = "in";
			}
		
	
		       try{
		       jsql1 = "select CON_AlternateName,contin.CON_Number from contin,object where object.OBJ_Name='" + fromObj + "' and object.CON_Number=contin.CON_Number";
			   st1 = con.createStatement();
			   rs1 = st1.executeQuery(jsql1);
               
			   if(rs1.next())
			   {
			   temp = rs1.getString(1);
			   if (temp!=null &&  (!temp.equals("")))
			   {
			   continName = rs1.getString(1);
			   }else
			   {
				   continName = rs1.getString(2);
			   }
			   }else{
				   continName = "obj"+fromObj;
				   }
			   }catch (SQLException e) 
	           {
               e.printStackTrace (  );
               } 
	           finally 
	           {
               if (rs1 != null) rs1.close();
               if (pst1 != null) pst1.close();
               } 
		    
			if ((!continName.equals(mask))&&(!continName.equals(continN)))
		{
			
			  try{
				
		        jsql1 = "select IMG_SectionNumber from image where IMG_Number='"+imgNum+"'";
			    st1 = con.createStatement();
			    rs1 = st1.executeQuery(jsql1);
                if(rs1.next()) x=rs1.getInt(1);
			    
              
			    }catch (SQLException e) 
	            {
                e.printStackTrace (  );
                } 
	            finally 
	            {
                if (rs1 != null) rs1.close();
                if (pst1 != null) pst1.close();
                } 

			try{
				
		        jsql1 = "select min(segmentNum) from relationship where (objName1=" + toObj +" or objName2="+toObj+")";
			    st1 = con.createStatement();
			    rs1 = st1.executeQuery(jsql1);
                if(rs1.next()) segNum=rs1.getInt(1);
			    
              
			    }catch (SQLException e) 
	            {
                e.printStackTrace (  );
                } 
	            finally 
	            {
                if (rs1 != null) rs1.close();
                if (pst1 != null) pst1.close();
                } 
			
			
			
			
			
			Synapse2 syn = new Synapse2(continName,x,x,segNum,type,synName,objX,objY);
		    synapses.add(syn);
		}
		}
	    }
		}
	    catch (SQLException e) 
	    {
        e.printStackTrace (  );
        } 
	    finally 
	    {
        if (rs != null) rs.close();
        if (pst != null) pst.close();
	    if (con != null) con.close();
        }
System.out.println("start to collect all the multiple synapses to this contin!  ----total 60% completed!!!");
// get all the multiple synapses to this contin


        try
	    {
	  	Class.forName("com.mysql.jdbc.Driver").newInstance();
		 con = DriverManager.getConnection ( DatabaseProperties.CONNECTION_STRING,  DatabaseProperties.USERNAME,  DatabaseProperties.PASSWORD );
		st = con.createStatement();
		jsql = "select toObj,fromObj,OBJ_Name,OBJ_X,OBJ_Y from object where type='chemical' and toObj like '%,%'";
		
		rs = st.executeQuery(jsql);
		while(rs.next())
		{
		int flag = 0;
		continName = "";
		
		toObj = rs.getString(1);
		int currentObj = rs.getInt(2);
		synName = rs.getInt(3);
		objX = rs.getInt(4);
		objY = rs.getInt(5);

        try{
		       jsql1 = "select CON_AlternateName,contin.CON_Number from contin,object where object.OBJ_Name=" + currentObj + " and object.CON_Number=contin.CON_Number";
			   st1 = con.createStatement();
			   rs1 = st1.executeQuery(jsql1);
               if(rs1.next())
			   {
			   temp = rs1.getString(1);
			   if (temp!=null && (!temp.equals("")))
			   {
			   continName = rs1.getString(1)+"->";
			   }else
			   {
				   continName = rs1.getString(2)+"->";
			   }
			   }else{
				   continName = currentObj+"->";
				   }
			   }catch (SQLException e) 
	           {
               e.printStackTrace (  );
               } 
	           finally 
	           {
               if (rs1 != null) rs1.close();
               if (pst1 != null) pst1.close();
               } 


        
		String[] toObjs1 = toObj.split(",");
            int[] toObjs = new int[toObjs1.length];
			int[] toContins = new int[toObjs1.length];
            for (int i=0; i < toObjs.length ; i++)

            {
			 //int to = getContinNumber
             toObjs[i] = Integer.parseInt(toObjs1[i]);
            }

            //Arrays.sort(toObjs);	
			
			
			for (int i=0; i<toObjs.length; i++)
			   {
				try{

                


		        jsql1 = "select CON_Number,IMG_SectionNumber,min(segmentNum) from object as o,relationship,image as i where o.IMG_Number=i.IMG_Number and (OBJ_Name=objName1 or OBJ_Name=objName2) and OBJ_Name=" + toObjs[i] + " group by OBJ_Name";
			    st1 = con.createStatement();
			    rs1 = st1.executeQuery(jsql1);
                while(rs1.next())
			    {
			    toContins[i] = rs1.getInt(1);
				if (toContins[i] == continNum)
				{
					flag = 1;
					x=rs1.getInt(2);
					segNum = rs1.getInt(3);

				}
			    }
			    }catch (SQLException e) 
	            {
                e.printStackTrace (  );
                } 
	            finally 
	            {
                if (rs1 != null) rs1.close();
                if (pst1 != null) pst1.close();
                } 
			   }
                
            Arrays.sort(toContins);

            for (int i=0; i<toContins.length; i++)
			 {
                try{
		        jsql1 = "select CON_AlternateName from contin where CON_Number=" + toContins[i];
			    st1 = con.createStatement();
			    rs1 = st1.executeQuery(jsql1);
                if(rs1.next())
			   {
               temp = rs1.getString(1);
			   if (temp!=null &&  (!temp.equals("")))
			   {		
			   continName = continName + rs1.getString(1) + ",";
			   }else{
				   continName = continName + toContins[i]+ ",";
			   }
			   }else{
				   continName = continName + "obj" + toObjs[i] + ",";
				   }
			    }catch (SQLException e) 
	            {
                e.printStackTrace (  );
                } 
	            finally 
	            {
                if (rs1 != null) rs1.close();
                if (pst1 != null) pst1.close();
                } 
				
			   }
			   if (flag ==1)
			   {
				   Synapse2 syn = new Synapse2(continName,x,x,segNum,"min",synName,objX,objY);
		           synapses.add(syn);

			   }
	    }
		}
	    catch (SQLException e) 
	    {
        e.printStackTrace (  );
        } 
	    finally 
	    {
        if (rs != null) rs.close();
        if (pst != null) pst.close();
	    if (con != null) con.close();
        }

    save();
    
	
    System.out.println("done!!!");



	}
	
	public Vector getSynapses() 
	{
	return synapses;
	}
    
   public void save()throws SQLException,
    ClassNotFoundException, java.lang.InstantiationException, java.lang.IllegalAccessException 
   {
	   //delete the pre synapse data
	    Connection con = null;
	    
	    PreparedStatement pst = null;
	
		
	System.out.println("synapses size is");
	System.out.println(synapses.size());
   //save into synapse table
	for (int i=0;i<synapses.size();i++)
    {   

		Synapse2 sy = (Synapse2)(synapses.elementAt(i));
		try{    
			    Class.forName("com.mysql.jdbc.Driver").newInstance();		
			    con = DriverManager.getConnection ( DatabaseProperties.CONNECTION_STRING,  DatabaseProperties.USERNAME,  DatabaseProperties.PASSWORD );
				String comp = sy.continName+sy.type+sy.lineNum;
				pst = con.prepareStatement("delete from object where OBJ_Name="+sy.synName);
				pst.executeUpdate();
				pst.close();
				System.out.println(sy.synName);
				
				
			    
				}catch (SQLException e) 
	            {
                e.printStackTrace (  );
                } 
	            finally 
	            {
                if (con != null) con = null;
                }
				
	
    }
	//end of saving into synapse table

   }

   **/
}