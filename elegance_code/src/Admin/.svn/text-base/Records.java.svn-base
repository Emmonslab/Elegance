package Admin;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;



public class Records {
	
	
	

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
		
		String[] usernames={"Travis","Yi"};
		String[] partnerStr = new String[usernames.length];
		
		
		jsql = "delete from records2";
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
			String pre="",partner="";
			String type = "";
		
			
			//get presynaptical density id
			jsql1 = "select fromObj,type,IMG_Number from object where OBJ_Name=?";
			pstmt1=con.prepareStatement(jsql1);
			pstmt1.setInt(1, synid);
			rs1 = pstmt1.executeQuery();
			if (rs1.next())
			{
				type = rs1.getString(2);
				
				pre = rs1.getString(1);
				
				
				IMG_Number = rs1.getString(3);
			
			}
			pstmt1.close();
			rs1.close();
			
			
			
			
			
			//check each user's record
			
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
					if ((!partnerStr[i].equals(partnerStr[i-1])) && (!partnerStr[i].equals("")) &&(!partnerStr[i-1].equals(""))) 
					
				    {
					//System.out.println(partnerStr[i]+"    "+partnerStr[i-1]);
					saveRecord(synid,IMG_Number,partnerStr[i],partnerStr[i-1],type);
					}
				}
				pstmt1.close();
				rs1.close();
			}//for
			
			
			

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
		
		
			jsql = "insert into records2 (synid,IMG_Number,partner1,partner2,type) values (?,?,?,?,?)";
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

