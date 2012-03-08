import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

class Serie {
	String series;
	int orix;
	int oriy;
	int oriz;
	double zoomx;
	double zoomy;
	double zoomz;

	public Serie(String s, int x, int y, int z, double zoomx, double zoomy, double zoomz) {
		series = s;
		orix = x;
		oriy = y;
		oriz = z;
		this.zoomy = zoomy;
		this.zoomx = zoomx;
		this.zoomz = zoomz;
	}
}

class Img {
	private int sectionNum;
	private String series;

	public Img(int section, String series) {

		this.series = series;
		this.sectionNum = section;
	}

	public int getSectionNum() {
		// TODO Auto-generated method stub
		return sectionNum;
	}

	public String getSeries() {
		// TODO Auto-generated method stub
		return series;
	}
}

class Obj {
	private String objName, imgNum, remarks;
	private int x, y, cellbody, branches;
	
	private Integer conN;

	
	
	public String getImgNum() {
		return imgNum;
	}

	public Integer getConN() {
		return conN;
	}

	@Override
	public String toString() {
		return "Obj [objName=" + objName + ", imgNum=" + imgNum + ", remarks=" + remarks + ", x=" + x + ", y=" + y + ", cellbody=" + cellbody + ", branches="
				+ branches + "]";
	}

	public Obj(String name, int x, int y, Integer conN, String imgNum, int cellbody, String remarks) {
		this.setObjName(name);
		this.setX(x);
		this.setY(y);
		this.imgNum = imgNum;
		this.conN = conN;
		this.cellbody = cellbody;
		this.remarks = remarks;
	}

	public void setBranches(int t) {
		this.branches = t;
	}

	public int getBranches() {
		return branches;
	}

	public void setX(int xx) {
		this.x = xx;
	}

	public void setY(int yy) {
		this.y = yy;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setObjName(String objName) {
		this.objName = objName;
	}

	public String getObjName() {
		return objName;
	}

	public int getCellbody() {
		// TODO Auto-generated method stub
		return cellbody;
	}

	public String getRemarks() {
		// TODO Auto-generated method stub
		return remarks;
	}

	public String getImageNum() {
		// TODO Auto-generated method stub
		return imgNum;
	}

}

class Rel {
	private int segmentNum;
	private String relID, objName1, objName2;

	
	
	@Override
	public String toString() {
		return "Rel [segmentNum=" + segmentNum + ", relID=" + relID + ", objName1=" + objName1 + ", objName2=" + objName2 + "]";
	}

	public Rel(String relID, String obj1, String obj2) {
		this.setRelID(relID);
		this.objName1 = obj1;
		this.objName2 = obj2;
	}

	public void setSegmentNum(int seg) {
		this.segmentNum = seg;
	}

	public int getSegmentNum() {
		return segmentNum;
	}

	public String getTheOtherObj(String object) {
		if (object.equals(objName1))
			return objName2;
		if (object.equals(objName2))
			return objName1;
		return null;
	}

	public void setRelID(String relID) {
		this.relID = relID;
	}

	public String getRelID() {
		return relID;
	}

	public String getObjName1() {
		// TODO Auto-generated method stub
		return objName1;
	}

	public String getObjName2() {
		// TODO Auto-generated method stub
		return objName2;
	}
}

class Calculate extends SwingWorker<Void, Void> {
	String objName;
	int continNum;
	HashMap<String, Obj> objs;
	HashMap rels;
	HashMap imgs, serieslist;
	HashMap objPreInd, objPostInd;
	LinkedHashMap<String,Obj> nodes;
	LinkedHashMap<String,Rel> edges;
	Set<Integer> processed;
	
	ArrayList<String> branchpoints;
	int seg;

	ArrayList smooth;
	
	
	
	public Calculate(int continNum) throws SQLException, ClassNotFoundException, java.lang.InstantiationException, java.lang.IllegalAccessException {

		Connection con = null;

		try {
			con = EDatabase.borrowConnection();
			String jsql;
			PreparedStatement pst;
			ResultSet rs;
			jsql = "select OBJ_Name from object where CON_Number=? limit 2,1";
			pst = con.prepareStatement(jsql);
			pst.setInt(1, continNum);
			rs = pst.executeQuery();
			if (rs.next()) {

				int objN = rs.getInt(1);
				this.objName = Integer.toString(objN);
				this.continNum = continNum;

				objs = new HashMap(500000);
				rels = new HashMap(500000);
				imgs = new HashMap(50000);
				objPreInd = new HashMap(500000);
				objPostInd = new HashMap(500000);
				nodes = new LinkedHashMap<String,Obj>(10000);
				edges = new LinkedHashMap<String, Rel>(10000);
				branchpoints = new ArrayList();
				ELog.info("bsize1" + branchpoints.size());
				serieslist = new HashMap(100);
				seg = 0;
				LoadImageTable();
				LoadSeriesTable();
				LoadObjectTable();
				LoadRelationshipTable();
				LoadPostIndexTable();
				LoadPreIndexTable();
				expandObj(objName);
				setSeg();
				System.out.print("nodes:");
				updateObjectContins();
				System.out.print("edges:");
				updateRelationContinsAndSegments();
				updateDisplay2AndContin();

			}
		} finally {
			EDatabase.returnConnection(con);
		}
	}

	public Calculate(int objectName, int continNum) throws SQLException, ClassNotFoundException, java.lang.InstantiationException,
	java.lang.IllegalAccessException {
		this.objName = Integer.toString(objectName);
		this.continNum = continNum;
		this.seg = 0;
		this.nodes = new LinkedHashMap<String, Obj>(10000);

		imgs = new HashMap(50000);
		LoadImageTable();

		serieslist = new HashMap(100);
		LoadSeriesTable();

		objs = new HashMap(500000);
		LoadObjectTable();

		rels = new HashMap(500000);
		LoadRelationshipTable();

		objPostInd = new HashMap(500000);
		LoadPostIndexTable();

		objPreInd = new HashMap(500000);
		LoadPreIndexTable();

		edges = new LinkedHashMap<String, Rel>(10000);
		branchpoints = new ArrayList<String>();

		expandObj(Integer.toString(objectName));
		setSeg();
		System.out.print("nodes:");
		updateObjectContins();
		System.out.print("edges:");
		updateRelationContinsAndSegments();
		updateDisplay2AndContin();

	}
	
	static long lastTs=System.currentTimeMillis();
	
	
	//calculates contins for all objects.
	public Calculate() throws SQLException, ClassNotFoundException, java.lang.InstantiationException,
			java.lang.IllegalAccessException {
		
		
		imgs = new HashMap(50000);
		LoadImageTable();
		
		serieslist = new HashMap(100);
		LoadSeriesTable();
		
		objs = new HashMap(500000);
		LoadObjectTable();
		
		rels = new HashMap(500000);
		LoadRelationshipTable();
		
		objPostInd = new HashMap(500000);
		LoadPostIndexTable();
		
		objPreInd = new HashMap(500000);
		LoadPreIndexTable();

		this.processed = new HashSet<Integer>();
		
	}

	public Void doInBackground() {
		
		try {
			int total=this.objs.size();
			
			for (Map.Entry<String, Obj> entry : this.objs.entrySet()) {
				this.objName = entry.getKey();
				
				setProgress((int)(processed.size()*100/total));
				
				if (processed.contains(new Integer(objName)))
					continue;
				
				//reset global vars
				this.edges = new LinkedHashMap<String, Rel>(10000);
				this.branchpoints = new ArrayList<String>();
				this.seg = 0;
				this.nodes = new LinkedHashMap<String, Obj>(10000);
				
				if (entry.getValue().getConN() == null) {
					NameContin na = new NameContin(new Integer(entry.getKey()), true,null);

					this.continNum = na.continNum;
				} else {
					this.continNum = entry.getValue().getConN();
				}

				expandObj(this.objName);

				setSeg();

				updateObjectContins();

				updateRelationContinsAndSegments();

				updateDisplay2AndContin();

			}

		} catch (Throwable e2) {

			String msg = "Can't calculate contins ";

			ELog.info(msg + ELog.e2s(e2));
			JOptionPane.showMessageDialog(null, msg, msg, JOptionPane.INFORMATION_MESSAGE);
		}

		return null;

	}

	public static void ts(Object l) {
		ELog.info(l+":"+((System.currentTimeMillis()-lastTs))+"");
	    lastTs=System.currentTimeMillis();
	}
	
	public void LoadObjectTable() throws SQLException, ClassNotFoundException, java.lang.InstantiationException, java.lang.IllegalAccessException

	{
		// load object table

		Connection con = null;

		try {
			con = EDatabase.borrowConnection();

			String jsql;
			PreparedStatement pstmt;
			ResultSet rs;

			jsql = "select OBJ_Name,OBJ_X,OBJ_Y,CON_Number,IMG_Number,cellType,OBJ_Remarks from object";
			pstmt = con.prepareStatement(jsql);
			rs = pstmt.executeQuery();
			while (rs.next()) {

				String name = rs.getString(1);
				int x = rs.getInt(2);
				int y = rs.getInt(3);
				Integer conN = rs.getInt(4);
				String imgNum = rs.getString(5);

				// Util.info("imgNum: "+imgNum);

				// if (imgs.containsKey(imgNum)){
				Img img = (Img) imgs.get(imgNum);
				if (img == null)
					continue;
				String series = img.getSeries();

				if (series.equals("Ventral Cord 2"))
					series = "VC";
				if (series.equals("Ventral Cord"))
					series = "VC";
				if (series.equals("N2YDRG"))
					series = "DRG";
				x = getSeriesX(x, series);
				y = getSeriesY(y, series);

				int cellbody = rs.getInt(6);
				String remarks = rs.getString(7);

				Obj obj = new Obj(name, x, y, conN, imgNum, cellbody, remarks);
				objs.put(name, obj);
				// }

			}
			rs.close();
			pstmt.close();
		} finally {
			EDatabase.returnConnection(con);
		}

	}

	public void LoadSeriesTable() throws SQLException, ClassNotFoundException, java.lang.InstantiationException, java.lang.IllegalAccessException

	{
		// load object table

		Connection con = null;

		try {
			con = EDatabase.borrowConnection();

			String jsql;
			PreparedStatement pstmt;
			ResultSet rs;
			jsql = "select series,x,y,z,zoomx,zoomy,zoomz from series";
			pstmt = con.prepareStatement(jsql);
			rs = pstmt.executeQuery();
			while (rs.next()) {

				String series = rs.getString(1);
				int x = rs.getInt(2);
				int y = rs.getInt(3);
				int z = rs.getInt(4);
				double zoomx = rs.getDouble(5);
				double zoomy = rs.getDouble(6);
				double zoomz = rs.getDouble(7);

				Serie ser = new Serie(series, x, y, z, zoomx, zoomy, zoomz);
				serieslist.put(series, ser);
			}
			rs.close();
			pstmt.close();

		} finally {
			EDatabase.returnConnection(con);
		}

	}

	// load image
	public void LoadImageTable() throws SQLException, ClassNotFoundException, java.lang.InstantiationException, java.lang.IllegalAccessException {
		Connection con = null;

		try {
			con = EDatabase.borrowConnection();
			String jsql;
			PreparedStatement pstmt;
			ResultSet rs;

			jsql = "select IMG_Number,IMG_SectionNumber,IMG_Series from image";
			pstmt = con.prepareStatement(jsql);
			rs = pstmt.executeQuery();
			while (rs.next()) {

				String img = rs.getString(1);
				int section = rs.getInt(2);
				String series = rs.getString(3);

				Img image = new Img(section, series);
				imgs.put(img, image);

			}
			rs.close();
			pstmt.close();

		} finally {
			EDatabase.returnConnection(con);
		}
	}

	// load relationship
	public void LoadRelationshipTable() throws SQLException, ClassNotFoundException, java.lang.InstantiationException, java.lang.IllegalAccessException {

		Connection con = null;

		try {
			con = EDatabase.borrowConnection();
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

		} finally {
			EDatabase.returnConnection(con);
		}
	}

	public void LoadPostIndexTable() throws SQLException, ClassNotFoundException, java.lang.InstantiationException, java.lang.IllegalAccessException {

		Connection con = null;

		try {
			con = EDatabase.borrowConnection();

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
				if (rs.isLast())
					objPostInd.put(Integer.toString(n1), post);
			}
			rs.close();
			pstmt.close();

		} finally {
			EDatabase.returnConnection(con);
		}
	}

	public void LoadPreIndexTable() throws SQLException, ClassNotFoundException, java.lang.InstantiationException, java.lang.IllegalAccessException {

		Connection con = null;

		try {
			con = EDatabase.borrowConnection();

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
				if (rs.isLast())
					objPreInd.put(Integer.toString(n2), pre);
			}
			rs.close();
			pstmt.close();

		} finally {
			EDatabase.returnConnection(con);
		}
	}

	void expandObj(String root) {
		
		if (processed!=null) {
			processed.add(new Integer(root));
		}
		
		// Util.info("root:"+root);
		Obj rob = (Obj) (objs.get(root));

		if (rob==null) {
			ELog.info("ERROR objs knows nothing about "+root+", continuing");
			return;
		}
		
		int pre = 0, post = 0;
		ArrayList<String> preAndPostObjectNames = new ArrayList<String>();
		if (objPreInd.containsKey(root)) {

			String[] preRels = ((String) objPreInd.get(root)).split(",");
			if (preRels!=null) {
				pre = preRels.length;			
				preAndPostObjectNames.addAll(Arrays.asList(preRels));
			}
			else {
				
					ELog.info("objPreInd knows nothing about "+root);
				
			}
		}
		if (objPostInd.containsKey(root)) {
			String[] postRels = ((String) objPostInd.get(root)).split(",");
			if (postRels!=null) {
				post = postRels.length;
				preAndPostObjectNames.addAll(Arrays.asList(postRels));
			} else {
				ELog.info("objPostInd knows nothing about "+root);
			}
		}
		//int rbranches = pre + post;

		rob.setBranches(preAndPostObjectNames.size());
		if (!nodes.containsKey(root))
			nodes.put(root, rob);
		
		if (preAndPostObjectNames.size() != 2 || pre == 0 || post == 0) {
			//Util.info("branch point:" + root + "   " + preAndPostObjectNames.size() + "   " + pre + "   " + post);
			branchpoints.add(root);
			//Util.info("bsize2" + branchpoints.size());
		}

		for (int i = 0; i < preAndPostObjectNames.size(); i++) {
			String preOrPostObjName=preAndPostObjectNames.get(i);	
			if (!edges.containsKey(preOrPostObjName)) {
				Rel rel = (Rel) (rels.get(preOrPostObjName));
				edges.put(preOrPostObjName, rel);

				String p = rel.getTheOtherObj(root);
				if (!nodes.containsKey(p))
					expandObj(p);
			}

		}

	}

	

	void setSeg() {
	
		for (int i = 0; i < branchpoints.size(); i++) {
			String root = (String) branchpoints.get(i);
			// Util.info("root of seg "+root);
			if (objPostInd.containsKey(root)) {
				// Util.info("root of seg "+root);
				// Util.info("seg# "+seg);
				String[] postRels = ((String) objPostInd.get(root)).split(",");
				for (int j = 0; j < postRels.length; j++) {
					seg++;
					// Util.info("seg## "+seg);
					smooth = new ArrayList(10000);
					smooth.add(root);
					setNextSeg(root, postRels[j], seg);
				}
			}

		}

	}

	void setNextSeg(String root, String relation, int segmentNum) {
		Rel edge = (Rel) (edges.get(relation));
		edge.setSegmentNum(segmentNum);
		String p = edge.getTheOtherObj(root);
		smooth.add(p);
		if (objPostInd.containsKey(p)) {
			String[] postRels = ((String) objPostInd.get(p)).split(",");
			int branches = getBranches(p);
			if (!branchpoints.contains(p)) {
				setNextSeg(p, postRels[0], segmentNum);
			} else {
				smooth();
			}
		} else {
			smooth();
		}
	}

	public void smooth() {
		for (int j = 0; j < 5; j++) {
			for (int i = 1; i < smooth.size() - 1; i++) {
				String pre = (String) (smooth.get(i - 1));
				String current = (String) (smooth.get(i));
				String next = (String) (smooth.get(i + 1));
				
				Obj preObj=nodes.get(pre);
				if (preObj==null) {
					//Util.info("null pre obj "+pre+"|"+current+next);
					continue;
				}
				int x0 = preObj.getX();
				
				int x1 = ((Obj) (nodes.get(next))).getX();
				((Obj) (nodes.get(current))).setX((x0 + x1) / 2);
				int y0 = ((Obj) (nodes.get(pre))).getY();
				int y1 = ((Obj) (nodes.get(next))).getY();
				((Obj) (nodes.get(current))).setY((y0 + y1) / 2);
			}
			for (int i = smooth.size() - 2; i > 1; i--) {
				String pre = (String) (smooth.get(i - 1));
				String current = (String) (smooth.get(i));
				String next = (String) (smooth.get(i + 1));
				
				Obj preObj=nodes.get(pre);
				if (preObj==null) {
					//Util.info("null pre obj "+pre+"|"+current+next);
					continue;
				}
				
				int x0 = preObj.getX();
				
				int x1 = ((Obj) (nodes.get(next))).getX();
				((Obj) (nodes.get(current))).setX((x0 + x1) / 2);
				int y0 = ((Obj) (nodes.get(pre))).getY();
				int y1 = ((Obj) (nodes.get(next))).getY();
				((Obj) (nodes.get(current))).setY((y0 + y1) / 2);
			}
		}

	}

	public int getBranches(String root) {
		int pr = 0, po = 0;
		if (objPreInd.containsKey(root)) {
			String[] preRels = ((String) objPreInd.get(root)).split(",");
			pr = preRels.length;
		}
		if (objPostInd.containsKey(root)) {
			String[] postRels = ((String) objPostInd.get(root)).split(",");
			po = postRels.length;
		}
		return po + pr;
	}

	public static String[] getONE(String[] arg1, String[] arg2) {
		String[] result = new String[arg1.length + arg2.length];
		System.arraycopy(arg1, 0, result, 0, arg1.length);
		System.arraycopy(arg2, 0, result, arg1.length, arg2.length);
		return result;
	}

	public void updateObjectContins() throws SQLException, ClassNotFoundException, java.lang.InstantiationException, java.lang.IllegalAccessException

	{
		// save nodes

		Connection con = null;

		try {
			con = EDatabase.borrowConnection();

			String jsql;
			PreparedStatement pstmt;
			/**
			 * jsql = "update object set type='cell' where CON_Number=?"; pstmt
			 * = con.prepareStatement(jsql); pstmt.setInt(1, continNum);
			 * pstmt.executeUpdate(); pstmt.close();
			 **/
			Set keys = nodes.entrySet();
			Iterator iter = keys.iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				Obj obj = (Obj) (entry.getValue());

				int name = Integer.parseInt(obj.getObjName());
				jsql = "update object set CON_Number=? where OBJ_Name=?";
				pstmt = con.prepareStatement(jsql);
				pstmt.setInt(1, continNum);

				pstmt.setInt(2, name);
				pstmt.executeUpdate();

				if (branchpoints.contains(obj.getObjName())) {
					jsql = "update object set type='cell branch point' where OBJ_Name=?";
					pstmt = con.prepareStatement(jsql);
					pstmt.setInt(1, name);
					pstmt.executeUpdate();
				}

				pstmt.close();
			}

		} finally {
			EDatabase.returnConnection(con);
		}
	}

	public void updateRelationContinsAndSegments() throws SQLException, ClassNotFoundException, java.lang.InstantiationException, java.lang.IllegalAccessException

	{

		Connection con = null;
		Statement stmt = null;
		try {
			con = EDatabase.borrowConnection();
		
			Map<Integer,Set<Integer>> map=new HashMap<Integer,Set<Integer>>();
			
			Set<Map.Entry<String, Rel>> entries = edges.entrySet();
			Iterator<Map.Entry<String, Rel>> iter = entries.iterator();
			while (iter.hasNext()) {
				Map.Entry<String, Rel> entry = iter.next();
				Rel rel = (Rel) (entry.getValue());
				int name = Integer.parseInt(rel.getRelID());
				int segment = rel.getSegmentNum();
				Set<Integer> set = map.get(segment);
				
				if (set==null) {
					set=new HashSet<Integer>();
					map.put(segment, set);
				}
				
				set.add(name);
				
				// Util.info(rel.getObjName1()+"   "+rel.getObjName2());
								
			}
			
			
			if (!map.isEmpty()) {
				stmt = con.createStatement();
				for (Integer segment : map.keySet()) {
					Set<Integer> set = map.get(segment);
					String sql = "update relationship set continNum=" + this.continNum + ", segmentNum=" + segment + " where relID in (" + EString.join(set, ",")
							+ ")";
					stmt.addBatch(sql);

				}
				int[] updateCounts = stmt.executeBatch();
				stmt.close();
				// int result=Util.update(sql,set);
				ELog.info("Updated " + toStr(updateCounts) + " rows with " + this.continNum);
			}

		} finally {
		 
			EDatabase.returnConnection(con);
		}
	}

	private static String toStr(int [] arr) {
		if (arr==null || arr.length==0) return "";
		String s="";
		for(int i=0;i<arr.length;i++) {
			s=s+arr[i]+",";
		}
			
		return s;
	}
	
	public void updateDisplay2AndContin() throws SQLException, ClassNotFoundException, java.lang.InstantiationException, java.lang.IllegalAccessException

	{

		Connection con = null;

		try {
			con = EDatabase.borrowConnection();
			String jsql;
			PreparedStatement pstmt;

			jsql = "delete from display2 where continNum=?";
			pstmt = con.prepareStatement(jsql);
			pstmt.setInt(1, continNum);
			pstmt.executeUpdate();
			pstmt.close();

			ArrayList seriess = new ArrayList();
			String continseries = "";

			Set<Map.Entry<String, Rel>> entries = edges.entrySet();
			Iterator<Map.Entry<String, Rel>> iter = entries.iterator();
			while (iter.hasNext()) {
				Map.Entry<String, Rel> entry = iter.next();
				Rel rel = entry.getValue();
				// Util.info("obj1 "+rel.getObjName1());
				// Util.info("obj2 "+rel.getObjName2());
				Obj obj1 = (Obj) nodes.get(rel.getObjName1());
				if (obj1 ==null) {
					//Util.info("obj1 not found in nodes struct "+rel.getObjName1());
					continue;
				}

				
				Obj obj2 = (Obj) nodes.get(rel.getObjName2());
				if (obj2==null) {
					//Util.info("obj2 not found in nodes struct "+rel.getObjName2());
					continue;
				}
				
				int x1 = obj1.getX();

				int y1 = obj1.getY();

				int segN = rel.getSegmentNum();
				int cellbody1 = obj1.getCellbody();
				int cellbody2 = obj2.getCellbody();
				int branch1 = obj1.getBranches();
				int branch2 = obj2.getBranches();
				String remarks1 = obj1.getRemarks();
				String remarks2 = obj2.getRemarks();
				String imgN1 = obj1.getImageNum();
				String imgN2 = obj2.getImageNum();
				// Util.info("imgN1="+imgN1);
				// Util.info("imgN2="+imgN2);
				int z1 = ((Img) (imgs.get(imgN1))).getSectionNum();
				int x2 = obj2.getX();
				int y2 = obj2.getY();
				int z2 = ((Img) (imgs.get(imgN2))).getSectionNum();
				String objName1 = obj1.getObjName();
				String objName2 = obj2.getObjName();

				String series1 = ((Img) (imgs.get(imgN1))).getSeries();
				String series2 = ((Img) (imgs.get(imgN2))).getSeries();

				if (series1.equals("Ventral Cord 2"))
					series1 = "VC";
				if (series1.equals("Ventral Cord"))
					series1 = "VC";
				if (series1.equals("N2YDRG"))
					series1 = "DRG";
				if (!seriess.contains(series1))
					seriess.add(series1);

				jsql = "insert into display2 (x1,y1,z1,continNum,cellbody1,remarks1,segmentNum,branch1,objName1,"
						+ "x2,y2,z2,cellbody2,remarks2,branch2,objName2,series1,series2) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				pstmt = con.prepareStatement(jsql);
				pstmt.setInt(1, x1);
				pstmt.setInt(2, y1);
				pstmt.setInt(3, z1);
				pstmt.setInt(4, continNum);
				pstmt.setInt(5, cellbody1);
				pstmt.setString(6, remarks1);
				pstmt.setInt(7, segN);

				pstmt.setInt(8, branch1);
				pstmt.setString(9, objName1);
				pstmt.setInt(10, x2);
				pstmt.setInt(11, y2);
				pstmt.setInt(12, z2);

				pstmt.setInt(13, cellbody2);
				pstmt.setString(14, remarks2);

				pstmt.setInt(15, branch2);
				pstmt.setString(16, objName2);
				pstmt.setString(17, series1);
				pstmt.setString(18, series2);

				pstmt.executeUpdate();
				pstmt.close();
			}

			pstmt = con
					.prepareStatement("select max(IMG_SectionNumber),min(IMG_SectionNumber) from object,image where object.IMG_Number=image.IMG_Number and CON_Number=?");
			pstmt.setInt(1, continNum);
			ResultSet rs1 = pstmt.executeQuery();
			rs1.next();
			int img2 = rs1.getInt(1);
			int img1 = rs1.getInt(2);
			rs1.close();
			pstmt.close();

			jsql = "update contin set series=?, count=?,sectionNum1=?,sectionNum2=?,CON_AlternateName2=? where CON_Number=?";
			if (seriess!=null && !seriess.isEmpty()) {
			continseries = (String) seriess.get(0);
			for (int i = 1; i < seriess.size(); i++) {
				continseries = continseries + "," + (String) seriess.get(i);
			}
			}

			pstmt = con.prepareStatement(jsql);
			pstmt.setString(1, continseries);
			pstmt.setInt(2, nodes.size());
			pstmt.setInt(3, img1);
			pstmt.setInt(4, img2);

			Contin c = new Contin(continNum);

			pstmt.setString(5, c.getColorcode());

			pstmt.setInt(6, continNum);
			pstmt.executeUpdate();
			pstmt.close();

		} finally {
			EDatabase.returnConnection(con);
		}

	}

	public int getSeriesX(int x, String series) {
		if (serieslist.containsKey(series)) {
			Serie ser = (Serie) (serieslist.get(series));
			x = ser.orix + (int) (x / ser.zoomx);
		}
		return x;

	}

	public int getSeriesY(int y, String series) {
		if (serieslist.containsKey(series)) {
			Serie ser = (Serie) (serieslist.get(series));
			y = ser.oriy + (int) (y / ser.zoomy);
		}
		return y;
	}

}