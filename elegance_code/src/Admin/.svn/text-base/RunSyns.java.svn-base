package Admin;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;




public class RunSyns{

	
	
	static HashMap objs;
	static HashMap rels;
	
	static HashMap objPreInd, objPostInd;
	LinkedHashMap nodes, edges;
	

	public void calculate(String objName, int continNum) throws SQLException,
			ClassNotFoundException, java.lang.InstantiationException,
			java.lang.IllegalAccessException {
		
		
		nodes = new LinkedHashMap(1000);
		edges = new LinkedHashMap(1000);
		
		
		expandObj(objName);
		
		saveNodes(continNum);
	
		
	}



	public  void LoadObjectTable() throws SQLException, ClassNotFoundException,
			java.lang.InstantiationException, java.lang.IllegalAccessException

	{
		// load object table
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		Connection con = DriverManager.getConnection(
				DatabaseProperties.CONNECTION_STRING,
				DatabaseProperties.USERNAME, DatabaseProperties.PASSWORD);
		String jsql;
		PreparedStatement pstmt;
		ResultSet rs;

		jsql = "select OBJ_Name,OBJ_X,OBJ_Y,CON_Number,IMG_Number,cellType,OBJ_Remarks from object where type='chemical' or type='electrical'";
		pstmt = con.prepareStatement(jsql);
		rs = pstmt.executeQuery();
		while (rs.next()) {

			String name = rs.getString(1);
			int x = rs.getInt(2);
			int y = rs.getInt(3);
			int conN = rs.getInt(4);
			String imgNum = rs.getString(5);
			
			
			int cellbody = rs.getInt(6);
			String remarks = rs.getString(7);

			Obj obj = new Obj(name, x, y, conN, imgNum, cellbody, remarks);
			objs.put(name, obj);

		}
		rs.close();
		pstmt.close();

	}
	
	

	// load relationship
	public static void LoadRelationshipTable() throws SQLException,
			ClassNotFoundException, java.lang.InstantiationException,
			java.lang.IllegalAccessException {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		Connection con = DriverManager.getConnection(
				DatabaseProperties.CONNECTION_STRING,
				DatabaseProperties.USERNAME, DatabaseProperties.PASSWORD);
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

			Rel rel = new Rel(relID, n1, n2);
			rels.put(relID, rel);

		}
		rs.close();
		pstmt.close();
	}

	public static void LoadPostIndexTable() throws SQLException,
			ClassNotFoundException, java.lang.InstantiationException,
			java.lang.IllegalAccessException {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		Connection con = DriverManager.getConnection(
				DatabaseProperties.CONNECTION_STRING,
				DatabaseProperties.USERNAME, DatabaseProperties.PASSWORD);
		String jsql;
		PreparedStatement pstmt;
		ResultSet rs;
		// load object_relationship index table
		// load objName1 and post relationship index table
		jsql = "select relID,objName1 from relationship order by objName1";
		pstmt = con.prepareStatement(jsql);
		rs = pstmt.executeQuery();
		int n1 = 0;
		String post = "";
		if (rs.next()) {
			n1 = rs.getInt(2);
			post = rs.getString(1);
		}
		while (rs.next()) {

			if (rs.getInt(2) != n1) {

				objPostInd.put(Integer.toString(n1), post);
				post = rs.getString(1);
				n1 = rs.getInt(2);

			} else {
				post = post + "," + rs.getString(1);
			}
			if(rs.isLast()) objPostInd.put(Integer.toString(n1), post);
		}
		rs.close();
		pstmt.close();
	}

	public static void LoadPreIndexTable() throws SQLException,
			ClassNotFoundException, java.lang.InstantiationException,
			java.lang.IllegalAccessException {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		Connection con = DriverManager.getConnection(
				DatabaseProperties.CONNECTION_STRING,
				DatabaseProperties.USERNAME, DatabaseProperties.PASSWORD);
		String jsql;
		PreparedStatement pstmt;
		ResultSet rs;

		// load objName2 and pre relationship index table
		jsql = "select relID,objName2 from relationship order by objName2";
		pstmt = con.prepareStatement(jsql);
		rs = pstmt.executeQuery();
		int n2 = 0;
		String pre = "";
		if (rs.next()) {
			n2 = rs.getInt(2);
			pre = rs.getString(1);
		}
		while (rs.next()) {

			if (rs.getInt(2) != n2) {

				objPreInd.put(Integer.toString(n2), pre);
				pre = rs.getString(1);
				n2 = rs.getInt(2);

			} else {
				pre = pre + "," + rs.getString(1);
			}
			if(rs.isLast()) objPreInd.put(Integer.toString(n2), pre);
		}
		rs.close();
		pstmt.close();

	}
	
	

	 void expandObj(String root) {
		Obj rob = (Obj) (objs.get(root));
		
		int pre =0,post=0;
		ArrayList f = new ArrayList();
		if (objPreInd.containsKey(root)) 
		    {
			
			String[] preRels = ((String) objPreInd.get(root)).split(",");
			pre = preRels.length;
			for(int i=0;i<pre;i++) f.add(preRels[i]);
			
			}	
		if (objPostInd.containsKey(root)) 
		    {
			String[] postRels = ((String) objPostInd.get(root)).split(",");
			post = postRels.length;
			for(int i=0;i<post;i++) f.add(postRels[i]);
			}
		int rbranches = pre+post;
		
		
		//rob.setBranches(rbranches);
		if(!nodes.containsKey(root))nodes.put(root, rob);
		

		for (int i = 0; i < rbranches; i++) {

			if (!edges.containsKey(f.get(i))) {
				Rel rel = (Rel) (rels.get(f.get(i)));
				edges.put(f.get(i), rel);
				
				String p = rel.getTheOtherObj(root);
				if(!nodes.containsKey(p))expandObj(p);
			}

		}

	}

	

	 

	public  void saveNodes(int continNum)throws SQLException, ClassNotFoundException,
	java.lang.InstantiationException, java.lang.IllegalAccessException

    {
    // save nodes
		Class.forName("com.mysql.jdbc.Driver").newInstance();		
		Connection con = DriverManager.getConnection ( 
				DatabaseProperties.CONNECTION_STRING,  
				DatabaseProperties.USERNAME,  
				DatabaseProperties.PASSWORD );
		String jsql;
		PreparedStatement pstmt;
		
		Set keys = nodes.entrySet();
		Iterator iter = keys.iterator();
		while(iter.hasNext())
		{
			Map.Entry entry = (Map.Entry)iter.next();
			Obj obj = (Obj)(entry.getValue());
			
			int name = Integer.parseInt(obj.getObjName());
			
			
			jsql = "update object set CON_Number=? where OBJ_Name=?";
			pstmt = con.prepareStatement(jsql);
			pstmt.setInt(1, continNum);		
				
			pstmt.setInt(2, name);	
			pstmt.executeUpdate();
			pstmt.close();
		}
    }
	
	
			
	


public static void main(String[] args) 
{
new RunSyns();	
}



	public RunSyns()
	{
		
		
		
		
		
		Connection con = null;
		Statement st = null;
		PreparedStatement pst = null,pst2=null;
		ResultSet rs = null,rs1=null;
		
		

		
             try{
            	 
            	 
				long time1 = System.currentTimeMillis();
				
				objs = new HashMap(500000);
				rels = new HashMap(500000);
				
				objPreInd = new HashMap(500000);
				objPostInd = new HashMap(500000);
				
				
				LoadObjectTable();
				
				LoadRelationshipTable();
				LoadPostIndexTable();
				LoadPreIndexTable();
				
				
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				con = DriverManager.getConnection(
						DatabaseProperties.CONNECTION_STRING,
						DatabaseProperties.USERNAME, DatabaseProperties.PASSWORD);
			for (int i=0;i<10000;i++)
			{
					
				//System.out.println(i);
				String jsql;
					jsql = "select OBJ_Name,type from object where CON_Number=0 and ( type='chemical' or type='electrical')";
					pst = con.prepareStatement(jsql);
					rs1 = pst.executeQuery();
					if (rs1.next()) 
					{

						
						int objN = rs1.getInt(1);
						String ty = rs1.getString(2);
						int continN=0;
						
						   st = con.createStatement ();
	                       rs = st.executeQuery("select max(CON_Number) from contin");
				           while ( rs.next (  ) )
				             {
				               continN = rs.getInt("max(CON_Number)")+1;
				             }
				           System.out.println(objN+" contin# "+continN);
					       rs.close();
					       st.close();
	                       pst2 = con.prepareStatement ("insert into contin (CON_Number,CON_AlternateName,type) values(?,?,?)");
	                       pst2.setInt(1,continN);
	                       pst2.setString(2,"syn"+continN);
	                       pst2.setString(3, ty);
				           pst2.executeUpdate();
						   pst2.close();
				           pst2 = con.prepareStatement ("update object set CON_number="+continN+" where OBJ_Name = "+ objN);
				           pst2.executeUpdate();
					       pst2.close();
						
						String objName= Integer.toString(objN);
						
						calculate(objName,continN);
						
					}
					pst.close();
					rs1.close();
		
			}    
			
				long time2 = System.currentTimeMillis();
				long time = (time2-time1)/1000;
				
				System.out.println("It took "+time+" to calculate&smooth contins . Done, please press Ctrl + C to close the program");
				
		        
		        }

		        catch(Exception e1){
		        e1.printStackTrace();
		        }

		      
             
        }  

}
