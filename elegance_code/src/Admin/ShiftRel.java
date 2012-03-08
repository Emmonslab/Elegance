package Admin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;



class Img11 {
    int sectionNum;


	public Img11(int section) {
	
		this.sectionNum = section;
	}

	public int getSectionNum() {
		// TODO Auto-generated method stub
		return sectionNum;
	}


}

class Obj11 {
	 String objName, imgNum;

	public Obj11(String name, String imgNum) {
		this.objName = name;
		
		this.imgNum = imgNum;
		
	
	}


	public String getImageNum() {
		// TODO Auto-generated method stub
		return imgNum;
	}

}

class Rel11 {
	
	String relID;
	String objName1;
	String objName2;

	public Rel11(String relID, String obj1, String obj2) {
		this.relID = relID;
		this.objName1 = obj1;
		this.objName2 = obj2;
	}
	
	
}

class ShiftRel {
	
	HashMap objs;
	HashMap rels;
	HashMap imgs;
	
	
	public static void main(String[] args) 
	{
		try {
			new ShiftRel();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public ShiftRel()throws SQLException,
	ClassNotFoundException, java.lang.InstantiationException,
	java.lang.IllegalAccessException 
	{
	
		objs = new HashMap(500000);
		rels = new HashMap(500000);
		imgs = new HashMap(50000);
		
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		Connection con = DriverManager.getConnection(
				"jdbc:mysql://127.0.0.1/Elegance",
				"root", "worms");	
		LoadImageTable(con);
			
			LoadObjectTable(con);
			LoadRelationshipTable(con);
			
			saveRels(con);
			
			
			
	}
	
	



	public void LoadObjectTable(Connection con) throws SQLException, ClassNotFoundException,
			java.lang.InstantiationException, java.lang.IllegalAccessException

	{
		// load object table
		
		String jsql;
		PreparedStatement pstmt;
		ResultSet rs;

		jsql = "select OBJ_Name,IMG_Number from object";
		pstmt = con.prepareStatement(jsql);
		rs = pstmt.executeQuery();
		while (rs.next()) {

			String name = rs.getString(1);
			
			String imgNum = rs.getString(2);
			
         
		
			Obj11 obj = new Obj11(name,  imgNum);
			objs.put(name, obj);

		}
		rs.close();
		pstmt.close();

	}
	
	

	// load image
	public void LoadImageTable(Connection con) throws SQLException, ClassNotFoundException,
			java.lang.InstantiationException, java.lang.IllegalAccessException {
	
		String jsql;
		PreparedStatement pstmt;
		ResultSet rs;

		jsql = "select IMG_Number,IMG_SectionNumber from image";
		pstmt = con.prepareStatement(jsql);
		rs = pstmt.executeQuery();
		while (rs.next()) {

			String img = rs.getString(1);
			int section = rs.getInt(2);
		

			Img11 image = new Img11(section);
			imgs.put(img, image);

		}
		rs.close();
		pstmt.close();
	}

	// load relationship
	public void LoadRelationshipTable(Connection con) throws SQLException,
			ClassNotFoundException, java.lang.InstantiationException,
			java.lang.IllegalAccessException {
		
		String jsql;
		PreparedStatement pstmt;
		ResultSet rs;

		jsql = "select relID,objName1,objName2 from relationship";
		pstmt = con.prepareStatement(jsql);
		rs = pstmt.executeQuery();
		while (rs.next()) {

			String relID = rs.getString(1);
			String n1 = rs.getString(2);
			String n2 = rs.getString(3);
			
			if (objs.containsKey(n1))
			{
				String imgNum1 = ((Obj11) objs.get(n1)).imgNum;
				if (imgs.containsKey(imgNum1))
				{
					int sectionNum1 = ((Img11) imgs.get(imgNum1)).sectionNum;
					if (objs.containsKey(n2))
					{
						String imgNum2 = ((Obj11) objs.get(n2)).imgNum;
						if (imgs.containsKey(imgNum2))
						{
							int sectionNum2 = ((Img11) imgs.get(imgNum2)).sectionNum;
							if (sectionNum1>sectionNum2)
							{
								System.out.println("shifted"+"  "+sectionNum2+"  "+sectionNum1);
								Rel11 rel = new Rel11(relID, n2, n1);
								rels.put(relID, rel);
							}
							else
							{
								//System.out.println("Not shifted"+"  "+sectionNum2+"  "+sectionNum1);
								Rel11 rel = new Rel11(relID, n1, n2);
								rels.put(relID, rel);
							}
							
						}
						
					}
				}
				
			}
			
			
			

			

		}
		rs.close();
		pstmt.close();
	}

	

	
	
	public void saveRels(Connection con)throws SQLException, ClassNotFoundException,
	java.lang.InstantiationException, java.lang.IllegalAccessException

    {

	
		String jsql;
		PreparedStatement pstmt;
		
		Set keys = rels.entrySet();
		Iterator iter = keys.iterator();
		while(iter.hasNext())
		{
			Map.Entry entry = (Map.Entry)iter.next();
			Rel11 rel = (Rel11)(entry.getValue());
			
			
			jsql = "update relationship set ObjName1=?, ObjName2=? where relID=?";
			pstmt = con.prepareStatement(jsql);
			pstmt.setString(1, rel.objName1);	
			pstmt.setString(2, rel.objName2);
			pstmt.setString(3, rel.relID);
			pstmt.executeUpdate();
			pstmt.close();
		}
    }
			



}