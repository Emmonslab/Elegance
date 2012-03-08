
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mxu
 */
public class CaculateNeighbours {
    
    LinkedHashMap objs,  contins, neis;
  

     private void loadObjectTable() throws SQLException, ClassNotFoundException,
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
		objs = new LinkedHashMap(500000);
		jsql = "select OBJ_Name,CON_Number from object where type like 'cel%'";
		pstmt = con.prepareStatement(jsql);
		rs = pstmt.executeQuery();
		while (rs.next()) {

			String name = rs.getString(1);

			String conN = rs.getString(2);

			objs.put(name, conN);

		}
		rs.close();
		pstmt.close();
                con.close();

	}

     private void loadContinTable() throws SQLException, ClassNotFoundException,
			java.lang.InstantiationException, java.lang.IllegalAccessException {
		contins = new LinkedHashMap(20000);

		Class.forName("com.mysql.jdbc.Driver").newInstance();
		Connection con = DriverManager.getConnection(
				DatabaseProperties.CONNECTION_STRING,
				DatabaseProperties.USERNAME, DatabaseProperties.PASSWORD);
		String jsql;
		PreparedStatement pstmt;
		ResultSet rs;

		jsql = "select CON_Number,CON_AlternateName from contin order by CON_Number";
		pstmt = con.prepareStatement(jsql);
		rs = pstmt.executeQuery();
		while (rs.next()) {
			String conNum = rs.getString(1);
			String conName = rs.getString(2);

			contins.put(conNum, conName);

		}

		rs.close();
		pstmt.close();
                con.close();
	}

     public  CaculateNeighbours()  throws SQLException, ClassNotFoundException,
			java.lang.InstantiationException, java.lang.IllegalAccessException {
                loadObjectTable();
                loadContinTable();
		neis = new LinkedHashMap(100000);


		Class.forName("com.mysql.jdbc.Driver").newInstance();
		Connection con = DriverManager.getConnection(
				DatabaseProperties.CONNECTION_STRING,
				DatabaseProperties.USERNAME, DatabaseProperties.PASSWORD);
		String jsql,jsql2;
		PreparedStatement pstmt,pst2;
		ResultSet rs;

		jsql = "select OBJ_Name,fromObj,toObj,type,IMG_Number,CON_Number from object where  type='neighborhood'";
		pstmt = con.prepareStatement(jsql);
		rs = pstmt.executeQuery();
		while (rs.next()) {
			String name = rs.getString(1);
			String pre = rs.getString(2);
			if (pre == null) {
				//deleteObj(name);
                               // break;
			}
			String post = rs.getString(3);
			if (post == null) {
			System.out.println("please check the object number:"+name);
                            //deleteObj(name);
                               // break;

			}

			String type = rs.getString(4);
			String imgNum = rs.getString(5);
			int continNum = rs.getInt(6);


		        String precontin = "", precontinname = "";

				if (objs.containsKey(pre)) {
					precontin = (String) objs.get(pre);

					if (contins.containsKey(precontin)) {
						precontinname = (String) (contins.get(precontin));


					} else {
						precontinname = "contin" + precontin;
					}
				} else {
					precontinname = "obj" + pre;
				}


				String postname = "";

				String[] posts = (post).split(",");
				int postlength = posts.length;
                                String postcontinname = "";


				for (int i = 0; i < posts.length; i++) {
					
					// System.out.println("postobj:"+posts[i]);
					if (objs.containsKey(posts[i])) {
						String postcontin = (String) objs.get(posts[i]);
						// System.out.print("postcontin: "+postcontin+"      ");

						if (contins.containsKey(postcontin)) {
							postcontinname = (String) (contins.get(postcontin));
							if (postcontinname == null) postcontinname = "null";

						} else {
							postcontinname = "contin" + postcontin;

						}
					} else {
						postcontinname = "obj" + posts[i];

					}

					// System.out.print("postcontinname: "+postcontinname+"      ");
					if (i == 0) {
						postname = postcontinname;
					} else {

							postname = postname + "," + postcontinname;

					}
				}//for
				// System.out.print("postname: "+postname+"      ");
                                
                                jsql2 = "insert into neighborhood (OBJ_Name, neuron, neighbors, neighborNum,imgNum) values (?,?,?,?,?)";
                                pst2 = con.prepareStatement(jsql2);
                                pst2.setString(1,name);
                                pst2.setString(2,precontinname);
                                pst2.setString(3,postname);
                                pst2.setInt(4,postlength);
                                pst2.setString(5,imgNum);
                                pst2.executeUpdate();
			        pst2.close();





			}//while
		
		rs.close();
		pstmt.close();
		// System.out.println("syns size: "+syns.size());
	}

     private void deleteObj( String name){
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection con = DriverManager.getConnection(
				DatabaseProperties.CONNECTION_STRING,
				DatabaseProperties.USERNAME, DatabaseProperties.PASSWORD);

		PreparedStatement pst2 = con.prepareStatement("delete from object where OBJ_Name=?");
                                pst2.setString(1,name);

                                pst2.executeUpdate();
			        pst2.close();
                                con.close();
        } catch (Exception ex) {
            
        }

     }


    
    public static void main(String args[]) throws SQLException, ClassNotFoundException,
			java.lang.InstantiationException, java.lang.IllegalAccessException {

        new CaculateNeighbours();

    }


}
