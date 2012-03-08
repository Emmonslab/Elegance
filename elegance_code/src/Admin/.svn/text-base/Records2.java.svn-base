package Admin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;



public class Records2 {
	
	

	public static void main(String[] args)  throws SQLException, ClassNotFoundException,
			java.lang.InstantiationException, java.lang.IllegalAccessException

	{
		// load object table
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		Connection con = DriverManager.getConnection(
				DatabaseProperties.CONNECTION_STRING,
				DatabaseProperties.USERNAME, DatabaseProperties.PASSWORD);
		String jsql,jsql1,jsql2,jsql3;
		PreparedStatement pstmt,pstmt1,pstmt2,pstmt3;
		ResultSet rs,rs1,rs2,rs3;
		String pre="",partner="";
		String[] usernames={"Travis","Yi"};
		String[] partnerStr = new String[usernames.length];
		
		
		jsql = "delete from records";
		pstmt = con.prepareStatement(jsql);
		
		
		pstmt.executeUpdate();
		pstmt.close();

		jsql = "select synid from synrecord group by synid";
		pstmt = con.prepareStatement(jsql);
		rs = pstmt.executeQuery();
		while (rs.next()) 
		{

			int synid = rs.getInt(1);
			String IMG_Number="";
			String posts = "";
			String type = "";
			String username="";
			String names="";
			int flag =0;
			
			//get presynaptical density id
			jsql1 = "select fromObj,toObj,type,IMG_Number from object where OBJ_Name=?";
			pstmt1=con.prepareStatement(jsql1);
			pstmt1.setInt(1, synid);
			rs1 = pstmt1.executeQuery();
			if (rs1.next())
			{
				type = rs1.getString(3);
				if (type.equals("cell")|| type.equals("cell branch point")) 
				{
					jsql3 = "delete from synrecord where synid=?";
					pstmt3 = con.prepareStatement(jsql3);
					pstmt3.setInt(1, synid);			
					pstmt3.executeUpdate();
					pstmt3.close();
					System.out.println(synid+" deleted");
				}else{
				pre = rs1.getString(1);
				
				posts=rs1.getString(2);
				
				
				IMG_Number = rs1.getString(4);
				
				
				jsql2 = "select username,DateEntered from object where OBj_Name=? ";
				pstmt2 = con.prepareStatement(jsql2);
				pstmt2.setInt(1, Integer.parseInt(pre));
				rs2= pstmt2.executeQuery();
				if (rs2.next())
				{
				 if ((rs2.getString(1)!=null) && ((rs2.getString(2)).compareTo("2007-12-01")>0))
				 {
				   if (rs2.getString(1).equals("Travis") || rs2.getString(1).equals("Yi")) 
				   {
				    username = rs2.getString(1);
				    names = username;
				   }
				 }
				}
				rs2.close();
				pstmt2.close();
				
				if (posts!=null){
				String[] posto = posts.split(",");
				for (int i=0;i<posto.length;i++)
				{
					jsql2 = "select username,DateEntered from object where OBj_Name=? ";
					pstmt2 = con.prepareStatement(jsql2);
					pstmt2.setInt(1, Integer.parseInt(posto[i]));
					rs2= pstmt2.executeQuery();
					if (rs2.next())
					{
					  if ((rs2.getString(1)!=null) && ((rs2.getString(2)).compareTo("2007-12-01")>0))
					  {
						if (rs2.getString(1).equals("Travis") || rs2.getString(1).equals("Yi") )
						 {
						 
						 if (username.equals("")) 
						   {
							 username = rs2.getString(1);
							 names = username;
						   }
						 else
						   {
							 names = names+","+rs2.getString(1);
							 if (!(username.equals(rs2.getString(1)))) flag=1;
						   }
						
						 }
					  }
					}
					rs2.close();
					pstmt2.close();
				}//for
				
				}// if posts null
				
				
				
				
				
				}//else
				
				
				
			}
			pstmt1.close();
			rs1.close();
			
			
			
			
			
			//check each user's record
			if (flag==1)
			{
				//System.out.println(names);
			for (int i=0;i<usernames.length;i++)
			{
				ArrayList<String> partners = new ArrayList<String>();
				partnerStr[i]="";
				jsql1 = "select partner from synrecord where synid=? and username=? order by partner";
				pstmt1=con.prepareStatement(jsql1);
				pstmt1.setInt(1, synid);
				pstmt1.setString(2, usernames[i]);
				rs1 = pstmt1.executeQuery();
				while (rs1.next())
				{					
					partner = rs1.getString(1);
					if ((!partner.equals(pre))&& (!partner.equals("0")) && (!partners.contains(partner))) 
						{
						partners.add(partner);
						//System.out.println(partner);
						if (partnerStr[i].equals("")){partnerStr[i]=partner;}else{partnerStr[i]=partnerStr[i]+","+partner;}
						
						}
				}
				
				
				if (i>0)
				{ 
					//if ((!partnerStr[i].equals(partnerStr[i-1])) && (!partnerStr[i].equals("")) &&(!partnerStr[i-1].equals(""))) 
					if ((partnerStr[i].equals("")) || (partnerStr[i-1].equals("")))
				    {
					//System.out.println(partnerStr[i]+"    "+partnerStr[i-1]);
					saveRecord(synid,IMG_Number,partnerStr[i],partnerStr[i-1],type);
					}
				}
				pstmt1.close();
				rs1.close();
			}//for
			}//if flag
			
			

		}
		rs.close();
		pstmt.close();

	}
	
	static void saveRecord(int synid, String img, String p1, String p2, String type)throws SQLException, ClassNotFoundException,
	java.lang.InstantiationException, java.lang.IllegalAccessException

       {

		Class.forName("com.mysql.jdbc.Driver").newInstance();		
		Connection con = DriverManager.getConnection ( 
				DatabaseProperties.CONNECTION_STRING,  
				DatabaseProperties.USERNAME,  
				DatabaseProperties.PASSWORD );
		String jsql;
		PreparedStatement pstmt;
		
		
			jsql = "insert into records (synid,IMG_Number,partner1,partner2,type) values (?,?,?,?,?)";
			pstmt = con.prepareStatement(jsql);
			pstmt.setInt(1, synid);	
			pstmt.setString(2, img);
			pstmt.setString(3, p1);
			pstmt.setString(4, p2);
			pstmt.setString(5, type);
			pstmt.executeUpdate();
			pstmt.close();
	 }
 }

