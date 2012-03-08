package Admin;


import java.io.*;

import java.util.*;

import java.sql.*;



import java.sql.SQLException;


class  CsvMatrix
{
  public static void main(String[] args) 
  {
    try
    {
      String filename = "synapseList.txt";
      String neuron = "R2BR";
     
      generateCsvFile(neuron,filename);
      
         
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
   
  }

  private static void generateCsvFile(String name,String file) 
     {
	  try{
	  Class.forName("com.mysql.jdbc.Driver").newInstance();
	  Connection con = DriverManager.getConnection(
				DatabaseProperties.CONNECTION_STRING,
				DatabaseProperties.USERNAME, DatabaseProperties.PASSWORD);
		String jsql,jsql1;
		PreparedStatement pstmt,pstmt1;
		ResultSet rs,rs1;

	  FileWriter writer = new FileWriter(file);
	  //writer.append("hahaha");
	  writer.append(name+"\n");
	  
	  
	  writer.append("\n");
	  
	  String img1="",partner="";
	  int sections = 0;
	  String[] mem;
	  
	  //electrical
	  writer.append("electrical:"+"\n");
	  writer.append("parnter"+"\t"+"sections"+"\t"+"SynID"+"\t"+"Image#"+"\n");
	  
	  jsql= "select pre,post,sections,members from synapsecombined where " +
	  		" ( pre = ? or post= ? )  and type='electrical' order by sections desc";
	  System.out.println(jsql);
	  pstmt = con.prepareStatement(jsql);
	  pstmt.setString(1, name);
	  pstmt.setString(2, name);
	  rs = pstmt.executeQuery();
	  
	  
		
	  while (rs.next()) 
		{
		  partner = rs.getString(1);
		    if(partner.equals(name)) partner=rs.getString(2);
		    sections = rs.getInt(3);

			mem = rs.getString(4).split(",");
			
			jsql1 = "select IMG_Number from object where OBJ_Name=?";
			pstmt1 = con.prepareStatement(jsql1);
			pstmt1.setString(1, mem[0]);
			rs1 = pstmt1.executeQuery();
			if (rs1.next()) 
			   {
				  img1 = rs1.getString(1);
			   }
			
			
			
			rs1.close();
			pstmt1.close();
			//System.out.println(partner+"\t"+sections+"\t"+mem[0]+"\t"+img1+"\t");
			writer.append(partner+"\t"+sections+"\t"+mem[0]+"\t"+img1+"\n");
		}
	  pstmt.close();
	  rs.close();
	  
	  
	//chemical out
	  writer.append("chemical out:"+"\n");
	  writer.append("postsynaptical partners"+"\t"+"sections"+"\t"+"SynID"+"\t"+"Image#"+"\n");
	  
	  jsql= "select pre,post,sections,members from synapsecombined where " +
	  		" pre = ? and type='chemical' order by sections desc";
	  pstmt = con.prepareStatement(jsql);
	  pstmt.setString(1, name);
	 
	  rs = pstmt.executeQuery();
	  
	  while (rs.next()) 
		{
		  partner = rs.getString(2);
		   
		    sections = rs.getInt(3);

			mem = rs.getString(4).split(",");
			
			jsql1 = "select IMG_Number from object where OBJ_Name=?";
			pstmt1 = con.prepareStatement(jsql1);
			pstmt1.setString(1, mem[0]);
			rs1 = pstmt1.executeQuery();
			if (rs1.next()) 
			   {
				  img1 = rs1.getString(1);
			   }
			
			
			
			rs1.close();
			pstmt1.close();
			
			writer.append(partner+"\t"+sections+"\t"+mem[0]+"\t"+img1+"\n");
		}
	  pstmt.close();
	  rs.close();	
	  
	  
	  
	//chemical in
	  writer.append("chemical out:"+"\n");
	  writer.append("presynaptical partner"+"\t"+"postsynaptical partners"+"\t"+"sections"+"\t"+"SynID"+"\t"+"Image#"+"\n");
	  
	  jsql= "select pre,post,sections,members from synapsecombined where "+
	  "( post1 = ? or post2 = ? or post3 = ? or post4 = ? )   and type='chemical' order by sections desc";
	  pstmt = con.prepareStatement(jsql);
	  pstmt.setString(1, name);
	  pstmt.setString(2, name);
	  pstmt.setString(3, name);
	  pstmt.setString(4, name);
	 
	  rs = pstmt.executeQuery();
	  
	  while (rs.next()) 
		{
		  partner = rs.getString(1);
		  String post = rs.getString(2); 
		    sections = rs.getInt(3);

			mem = rs.getString(4).split(",");
			
			jsql1 = "select IMG_Number from object where OBJ_Name=?";
			pstmt1 = con.prepareStatement(jsql1);
			pstmt1.setString(1, mem[0]);
			rs1 = pstmt1.executeQuery();
			if (rs1.next()) 
			   {
				  img1 = rs1.getString(1);
			   }
			
			
			
			rs1.close();
			pstmt1.close();
			
			writer.append(partner+"\t"+post+"\t"+sections+"\t"+mem[0]+"\t"+img1+"\n");
		}
	  pstmt.close();
	  rs.close();
	  writer.close();
	  
	  }catch (Exception e) 
      {
     e.printStackTrace (  );
     } 
  }
  }
	  
