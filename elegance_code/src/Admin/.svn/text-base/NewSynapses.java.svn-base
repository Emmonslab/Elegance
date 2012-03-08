package Admin;
import java.sql.*;
import java.util.*;

class NewSynapses {
	LinkedHashMap syns, syns2, objPostInd, rels, contins, images, contintypes,
			objs, synstoberemove;
	ArrayList<Integer> continNumbers;
	Connection con = null;
	int maxContinNumber = 0;

	public void loadContinTable() throws SQLException, ClassNotFoundException,
			java.lang.InstantiationException, java.lang.IllegalAccessException {
		contins = new LinkedHashMap(20000);
		continNumbers = new ArrayList<Integer>(20000);
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
			continNumbers.add(new Integer(conNum));
		}
		maxContinNumber = continNumbers.get(continNumbers.size() - 1);
		rs.close();
		pstmt.close();
	}

	public void loadContinType() throws SQLException, ClassNotFoundException,
			java.lang.InstantiationException, java.lang.IllegalAccessException {
		contintypes = new LinkedHashMap(2000);
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		Connection con = DriverManager.getConnection(
				DatabaseProperties.CONNECTION_STRING,
				DatabaseProperties.USERNAME, DatabaseProperties.PASSWORD);
		String jsql;
		PreparedStatement pstmt;
		ResultSet rs;

		jsql = "select CON_Number,type from contin";
		pstmt = con.prepareStatement(jsql);
		rs = pstmt.executeQuery();
		while (rs.next()) {
			String conNum = rs.getString(1);
			String conType = rs.getString(2);

			contintypes.put(conNum, conType);
		}
		rs.close();
		pstmt.close();
	}

	public void loadImageTable() throws SQLException, ClassNotFoundException,
			java.lang.InstantiationException, java.lang.IllegalAccessException {
		images = new LinkedHashMap(2000);
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		Connection con = DriverManager.getConnection(
				DatabaseProperties.CONNECTION_STRING,
				DatabaseProperties.USERNAME, DatabaseProperties.PASSWORD);
		String jsql;
		PreparedStatement pstmt;
		ResultSet rs;

		jsql = "select IMG_Number,IMG_Series from image";
		pstmt = con.prepareStatement(jsql);
		rs = pstmt.executeQuery();
		while (rs.next()) {
			String imgNum = rs.getString(1);
			String imgSeries = rs.getString(2);

			images.put(imgNum, imgSeries);
		}
		rs.close();
		pstmt.close();
	}

	public void loadObjectTable() throws SQLException, ClassNotFoundException,
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

	}

	public void loadSyns() throws SQLException, ClassNotFoundException,
			java.lang.InstantiationException, java.lang.IllegalAccessException {
		syns = new LinkedHashMap(100000);
		syns2 = new LinkedHashMap(100000);

		Class.forName("com.mysql.jdbc.Driver").newInstance();
		Connection con = DriverManager.getConnection(
				DatabaseProperties.CONNECTION_STRING,
				DatabaseProperties.USERNAME, DatabaseProperties.PASSWORD);
		String jsql;
		PreparedStatement pstmt;
		ResultSet rs;

		jsql = "select OBJ_Name,fromObj,toObj,type,IMG_Number,CON_Number from object where ( type='chemical' or type='electrical' ) ";
		pstmt = con.prepareStatement(jsql);
		rs = pstmt.executeQuery();
		while (rs.next()) {
			String name = rs.getString(1);
			String pre = rs.getString(2);
			if (pre == null) {
				System.out.println("please check the synapse object#: " + name);
			}
			String post = rs.getString(3);
			if (post == null) {
				System.out.println("please check the synapse object#: " + name);
			}

			String type = rs.getString(4);
			String imgNum = rs.getString(5);
			int continNum = rs.getInt(6);

			if (post != null) {

				Syn syn = new Syn(name, pre, post, type, imgNum);
				syns2.put(name, syn);

				if (images.containsKey(imgNum)) {
					String series = (String) images.get(imgNum);
					syn.setSeries(series);
				}

				String precontin = "", precontinname = "", pretype = "neuron", posttype = "neuron";

				if (objs.containsKey(pre)) {
					precontin = (String) objs.get(pre);

					if (contins.containsKey(precontin)) {
						precontinname = (String) (contins.get(precontin));
						pretype = (String) (contintypes.get(precontin));

					} else {
						precontinname = "contin" + precontin;
					}
				} else {
					precontinname = "obj" + pre;
				}
				syn.setPrename(precontinname);

				String postname = "";
				// System.out.print("post: "+syn.post+"      ");
				String[] posts = (syn.post).split(",");
				syn.setPostlength(posts.length);
				syn.setFirstmember(name);
				syn.setLastmember(name);
				syn.setFirstImage(imgNum);
				syn.setLastImage(imgNum);
				syn.setContinNum(continNum);

				for (int i = 0; i < posts.length; i++) {
					String postcontinname = "";
					// System.out.println("postobj:"+posts[i]);
					if (objs.containsKey(posts[i])) {
						String postcontin = (String) objs.get(posts[i]);
						// System.out.print("postcontin: "+postcontin+"      ");

						if (contins.containsKey(postcontin)) {
							postcontinname = (String) (contins.get(postcontin));
							if (postcontinname == null)
								postcontinname = "null";
							String temptype = (String) (contintypes
									.get(postcontin));
							if (!(temptype.equals("neuron")))
								posttype = temptype;
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
				}
				// System.out.print("postname: "+postname+"      ");
				syn.setPostnames(postname);

				syn.shift();
				if (syn.type.equals("electrical") && pretype.equals("muscle")
						&& posttype.equals("neuron")) {
					pretype = "neuron";
					posttype = "muscle";
				}
				syn.setType2(pretype + " -> " + posttype);

				// System.out.println(precontinname+" "+postname);
				// savePartnership(syn.prename,syn.postname,syn.type);

				syns.put(name, syn);

			}
		}
		rs.close();
		pstmt.close();
		// System.out.println("syns size: "+syns.size());
	}

	public void loadRelationshipTable() throws SQLException,
			ClassNotFoundException, java.lang.InstantiationException,
			java.lang.IllegalAccessException {
		rels = new LinkedHashMap(5000000);
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
			if (syns.containsKey(n1)) {
				// System.out.println("find find a relationship");
				Rel rel = new Rel(relID, n1, n2);
				rels.put(relID, rel);
			}

		}
		rs.close();
		pstmt.close();
		// System.out.println("rels size: "+rels.size());
	}

	public void loadPostIndexTable() throws SQLException,
			ClassNotFoundException, java.lang.InstantiationException,
			java.lang.IllegalAccessException {
		objPostInd = new LinkedHashMap(500000);
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		Connection con = DriverManager.getConnection(
				DatabaseProperties.CONNECTION_STRING,
				DatabaseProperties.USERNAME, DatabaseProperties.PASSWORD);
		String jsql;
		PreparedStatement pstmt;
		ResultSet rs;
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
				// System.out.println("a relationship");
				if (syns.containsKey(Integer.toString(n1))) {
					// System.out.println("find a synapse relationship");
					objPostInd.put(Integer.toString(n1), post);
				}
				post = rs.getString(1);
				n1 = rs.getInt(2);
			} else {
				post = post + "," + rs.getString(1);
			}
			if ( rs.isLast() && syns.containsKey(Integer.toString(n1)) ) objPostInd.put(Integer.toString(n1), post);
		}
		rs.close();
		pstmt.close();
		// System.out.println("objPostInd size: "+objPostInd.size());
	}

	public void savePartnership(String pre, String posts, String type) {
		try {
			String[] post = posts.split(",");
			PreparedStatement pst1 = null, pst2 = null;
			ResultSet rs = null;
			for (int i = 0; i < post.length; i++) {
				pst1 = con
						.prepareStatement("insert into partnership (pre,post,type,sections) values (?,?,?,1)");
				pst1.setString(1, pre);
				pst1.setString(2, post[i]);
				pst1.setString(3, type);

				pst1.executeUpdate();
				pst1.close();

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void combineSyns() {
		synstoberemove = new LinkedHashMap(100000);
		Set key = syns.entrySet();
		Iterator iter = key.iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String synname = (String) entry.getKey();

			if (!(synstoberemove.containsKey(synname))) {
				Syn syn = (Syn) entry.getValue();
				// System.out.println("one syn "+syn.post);

				combineSyn(syn);
			}
		}// end of iter of syn

		key = synstoberemove.entrySet();
		iter = key.iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String synname = (String) entry.getKey();
			syns.remove(synname);

		}// end of iter of syn
	}
	
	
	
	
	
	public static void joinPosts(Syn s1, Syn s2)
	{
		String[] p1 = s1.postname.split(",");
		String[] p2 = s2.postname.split(",");
		ArrayList names = new ArrayList(10);
		
		for(int j=0;j<p1.length;j++)
		{
			if(!names.contains(p1[j]))names.add(p1[j]);
		}
		
		
		for(int j=0;j<p2.length;j++)
		{
		
		}
		
	}

	public void combineSyn(Syn syn) {
		if (objPostInd.containsKey(syn.lastmember)) {

			// System.out.println("find a adjacent");
			String[] relids = ((String) objPostInd.get(syn.lastmember))
					.split(",");
			for (int i = 0; i < relids.length; i++) {
				Rel rel = (Rel) rels.get(relids[i]);
				String next = rel.getObjName2();
				if ((syns.containsKey(next))
						&& (!(synstoberemove.containsKey(next)))) {
					Syn synnext = (Syn) syns.get(next);
					syn.addMember(synnext.members);
					syn.setLastmember(synnext.lastmember);
					syn.setLastImage(synnext.lastImage);
					if ((synnext.continNum < syn.continNum)
							&& (synnext.continNum > 0))
						syn.setContinNum(synnext.continNum);
					//if (syn.postname.equals(synnext.postname)) joinPosts(syn, synnext);
					synstoberemove.put(next, "");
					combineSyn(syn);
				}

			}// end of for

		}// end of if postInd

	}

	public NewSynapses() {

		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = DriverManager.getConnection(
					DatabaseProperties.CONNECTION_STRING,
					DatabaseProperties.USERNAME, DatabaseProperties.PASSWORD);
			System.out.println("begain to loading");
			deletePreviousData();
			loadObjectTable();
			loadContinType();
			loadImageTable();
			loadContinTable();

			loadSyns();
			loadRelationshipTable();
			loadPostIndexTable();
			System.out.println("fininsh loading, begain to combining");
			combineSyns();
			System.out.println("begain to saving");
			save();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void deletePreviousData() {
		// delete the previous synapse data

		PreparedStatement pst = null;
		try {

			pst = con.prepareStatement("delete from synapsecombined");
			pst.executeUpdate();
			pst.close();

			pst = con
					.prepareStatement("alter table synapsecombined auto_increment=0 ");
			pst.executeUpdate();
			pst.close();

			pst = con.prepareStatement("delete from synapsenomultiple");
			pst.executeUpdate();
			pst.close();
			pst = con
					.prepareStatement("alter table synapsenomultiple auto_increment=0 ");
			pst.executeUpdate();
			pst.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void save() throws SQLException, ClassNotFoundException,
			java.lang.InstantiationException, java.lang.IllegalAccessException {

		try {

			Set key = syns.entrySet();
			Iterator iter = key.iterator();
			while (iter.hasNext()) {
				String precontin = "";
				int precontinnumber = 0;

				Map.Entry entry = (Map.Entry) iter.next();
				Syn syn = (Syn) entry.getValue();

				String members = syn.members;
				String[] memberss = members.split(",");
				int sections = memberss.length;
				String mid = memberss[sections / 2];
				syn.setSections(sections);
				syn.setMid(mid);
				saveSynContin(syn);

				saveSyns(syn);

			}// end of iterator
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	int findNewContinNumber() {

		for (int i = 1; i < maxContinNumber - 1; i++) {
			if (continNumbers.contains(new Integer(i)) == false) {
				continNumbers.add(new Integer(i));
				return (i);
			}
		}
		maxContinNumber++;

		return maxContinNumber;

	}

	void saveSynContin(Syn syn) {

		PreparedStatement pst = null;
		ResultSet rs;
		int conN = 0;
		try {

			if (syn.continNum == 0) {

				conN = findNewContinNumber();
				syn.setContinNum(conN);

				pst = con
						.prepareStatement("insert into contin (CON_Number,CON_AlternateName,type) values(?,?,?)");
				pst.setInt(1, conN);
				pst.setString(2, "syn" + conN);
				// System.out.println("type="+syn.type);
				pst.setString(3, syn.type);
				pst.executeUpdate();
				pst.close();
			}

			String[] mem = syn.members.split(",");
			for (int i = 0; i < mem.length; i++) {
				pst = con
						.prepareStatement("update object set CON_number=? where OBJ_Name =?");
				pst.setInt(1, syn.continNum);
				pst.setString(2, mem[i]);
				pst.executeUpdate();
				pst.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	void saveSyns(Syn syn) {

		PreparedStatement pst = null, pst1 = null;
		ResultSet rs;
		try {

			pst = con
					.prepareStatement("insert into synapsecombined (pre, post, post1,post2,post3,post4,type,members,sections,partnerNum,type2,series,mid,preobj,postobj1,postobj2,postobj3,postobj4,continNum) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			pst.setString(1, syn.prename);
			pst.setString(2, syn.postname);
			pst.setString(3, syn.postname1);
			pst.setString(4, syn.postname2);
			pst.setString(5, syn.postname3);
			pst.setString(6, syn.postname4);
			pst.setString(7, syn.type);
			pst.setString(8, syn.members);
			pst.setInt(9, syn.sections);
			pst.setInt(10, syn.partnerNum);
			pst.setString(11, syn.type2);
			pst.setString(12, syn.series);

			pst.setString(13, syn.mid);
			Syn syn2 = (Syn) syns2.get(syn.mid);
			// System.out.println(syn2.sections);
			if (syn2.pre.length() > 10) {
				// System.out.println(syn2.sections);

				System.out.println("wrong pre synapse  synID: " + syn2.synID);
			}
			pst.setString(14, syn2.pre);
			pst.setString(15, syn2.post1);
			pst.setString(16, syn2.post2);
			pst.setString(17, syn2.post3);
			pst.setString(18, syn2.post4);
			pst.setInt(19, syn2.continNum);

			pst.executeUpdate();
			pst.close();
			savePartners(syn);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void savePartners(Syn syn) {
		PreparedStatement pst1 = null;
		try {

			String[] postnames = syn.postname.split(",");

			for (int i = 0; i < postnames.length; i++) {

				pst1 = con
						.prepareStatement("insert into synapsenomultiple (pre,post,type,sections) values (?,?,?,?)");
				pst1.setString(1, syn.prename);
				pst1.setString(2, postnames[i]);
				pst1.setString(3, syn.type);
				pst1.setInt(4, syn.sections);

				pst1.executeUpdate();
				pst1.close();

			}// end of for

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void stop() {
		// TODO Auto-generated method stub
		
	}

}
