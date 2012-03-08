package Admin;



import java.sql.*;
import java.util.*;

class ObjCon
{
String objName;
String continNum;
int x;
int y;
public ObjCon(String name, String continNum, int x, int y)
{
this.objName = name;
this.continNum = continNum;
this.x = x;
this.y = y;
}
}

class SynRel
{
String syn1, syn2;
SynRel(String s1, String s2)
    {
	  this.syn1=s1;
	  this.syn2=s2;
	}
}

class SynCon
{
	String synID="";
	String type="";
	String pre="";
	String post="";
	String post1="",post2="",post3="",post4="";
	String postname1="",postname2="",postname3="",postname4="";
	
	String prename;
	String postname;
	
	int postlength;
	
	String imgNum;
	
	int partnerNum;
	int x,y;
	int prejoincount,postjoincount;
	String premember="", postmember="";
	

	public SynCon(String name, String pre, String post, String type, String imgNum, int x, int y) {
		synID=name;
		this.imgNum=imgNum;
		this.pre=pre;
		
		this.post=post;
		String[] posts = post.split(",");
		if (posts.length>=1) post1=posts[0];
		if (posts.length>=2) post2=posts[1];
		if (posts.length>=3) post3=posts[2];
		if (posts.length==4) post4=posts[3];
		this.type=type;
		this.x = x;
		this.y = y;
		prejoincount = 0;
		postjoincount = 0;
		

	}
	
	public void setSynID(String id){
		synID=id;
	}
	public void setPrename(String name){
		prename=name;
	}
	
	public void addPreJoin(String premember)
	{
		prejoincount++;
		this.premember=premember;
	}
	
	public void addPostJoin(String postmember)
	{
		postjoincount++;
		this.postmember = postmember;
	}
	
	public void clearPreJoin()
	{
		prejoincount=0;
		premember="";
	}
	
	public void clearPostJoin()
	{
		postjoincount=0;
		postmember="";
	}
	
	


	
	public void setPostlength(int postlength){
		this.postlength=postlength;
	}
	

	public void setPostnames(String postname)
	{
		this.postname=postname;
		//System.out.println("postname: "+postname);
		String[] postnames = postname.split(",");
		partnerNum = postnames.length;
		if (postnames.length>=1) postname1=postnames[0];
		if (postnames.length>=2) postname2=postnames[1];
		if (postnames.length>=3) postname3=postnames[2];
		if (postnames.length==4) postname4=postnames[3];
		setPostlength(postnames.length);
		
	}
	
	
	public void shift()
	{
		if (type.equals("electrical")) 
		{
			if (prename.compareTo(postname)<0) 
			{
				
				String s = pre;
				pre = post;
				post = s;
				
				s = prename;
				prename = postname;
				postname = s;
				
			}
		}
	}
	
	

	
}

class ConSyn
{
	LinkedHashMap syns,pres,rels,contins,objs;
	ArrayList<SynRel> synrels;
	Connection con= null;
	int count1 =0;
	
	public ConSyn()
	{
		
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = DriverManager.getConnection(
					DatabaseProperties.CONNECTION_STRING,
					DatabaseProperties.USERNAME, DatabaseProperties.PASSWORD);
			long time1 = System.currentTimeMillis();
			
			removeSynRel();
			
			loadContinTable();
			loadObjectTable();
			loadRelationshipTable();
			loadSyns();
			loadPreSyn();
		
			long time2 = System.currentTimeMillis();
			System.out.println("It took "+(time2-time1)/1000+" to loading the database");
			connectSynsByRels();
			long time3 = System.currentTimeMillis();
			
			System.out.println("It took "+(time3-time2)/1000+" to calculate  ");
			
			saveSynCon();
            long time4 = System.currentTimeMillis();
			
			System.out.println("It took "+(time4-time3)/1000+" to save joining ");
		
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	public void removeSynRel()
	   {
		   PreparedStatement pst0 = null, pst=null;
		   ResultSet rs=null;
		   int c=0;
		   String jsql;
			try{    
				    
				jsql = "select relID from relationship,object where ObjName1=OBJ_Name and ( type='chemical' or type='electrical' ) ";
				pst0 = con.prepareStatement(jsql);
				
				rs = pst0.executeQuery();
				while (rs.next()) 
				{ 
					int relid = rs.getInt(1);
					pst = con.prepareStatement("delete from relationship where relID=?" );
					pst.setInt(1,relid);
				
					pst.executeUpdate();
					c++;
					//System.out.println("deleted "+relid);
					pst.close();
				}	
				System.out.println("deleted prev syn relationship "+c);
				jsql = "select relID from relationship,object where ObjName2=OBJ_Name and ( type='chemical' or type='electrical' ) ";
				pst0 = con.prepareStatement(jsql);
				
				rs = pst0.executeQuery();
				while (rs.next()) 
				{ 
					int relid = rs.getInt(1);
					pst = con.prepareStatement("delete from relationship where relID=?" );
					pst.setInt(1,relid);
				
					pst.executeUpdate();
					c++;
					//System.out.println("deleted "+relid);
					pst.close();
				}	
				pst0.close();
				rs.close();
				System.out.println("deleted prev syn relationship "+c);
				
			    }catch (Exception e) 
		            {
	               e.printStackTrace (  );
	               } 
		      
		   
	   }
	
	public void loadContinTable() throws SQLException,
	ClassNotFoundException, java.lang.InstantiationException,
	java.lang.IllegalAccessException 
	{
		contins = new LinkedHashMap(2000);
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection con = DriverManager.getConnection(
		DatabaseProperties.CONNECTION_STRING,
		DatabaseProperties.USERNAME, DatabaseProperties.PASSWORD);
        String jsql;
        PreparedStatement pstmt;
        ResultSet rs;

        jsql = "select CON_Number,CON_AlternateName from contin";
        pstmt = con.prepareStatement(jsql);
        rs = pstmt.executeQuery();
        while (rs.next()) 
        {
	     String conNum = rs.getString(1);
	     String conName = rs.getString(2);
	 
	     contins.put(conNum, conName);
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
     objs= new LinkedHashMap(500000);
     jsql = "select OBJ_Name,CON_Number,OBJ_X,OBJ_Y from object where type like 'cel%'";
     pstmt = con.prepareStatement(jsql);
     rs = pstmt.executeQuery();
     while (rs.next()) 
       {

	     String name = rs.getString(1);
	
	     String conN = rs.getString(2);
	     int x = rs.getInt(3);
	     int y = rs.getInt(4);
	     
	     ObjCon obj = new ObjCon(name,conN,x,y);

	     objs.put(name, obj);

       }
        rs.close();
        pstmt.close();

    }
	
	public void loadRelationshipTable() throws SQLException,
	ClassNotFoundException, java.lang.InstantiationException,
	java.lang.IllegalAccessException 
	{
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
        while (rs.next()) 
        {

	     String relID = rs.getString(1);
	     String n1 = rs.getString(2);
	     String n2 = rs.getString(3);

	     Rel rel = new Rel(relID, n1, n2);
	     rels.put(relID, rel);

        }
    rs.close();
    pstmt.close();
    }
	
	
	public void loadSyns()throws SQLException, ClassNotFoundException,
	java.lang.InstantiationException, java.lang.IllegalAccessException
	{
		syns= new LinkedHashMap(100000);
	
		
		
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		Connection con = DriverManager.getConnection(
				DatabaseProperties.CONNECTION_STRING,
				DatabaseProperties.USERNAME, DatabaseProperties.PASSWORD);
		String jsql;
		PreparedStatement pstmt;
		ResultSet rs;

		jsql = "select OBJ_Name,fromObj,toObj,type,IMG_Number,OBJ_X,OBJ_Y from object where  type='chemical' or type='electrical'";
		pstmt = con.prepareStatement(jsql);
		rs = pstmt.executeQuery();
		while (rs.next()) {
			String name = rs.getString(1);
			String pre = rs.getString(2);
			if (pre==null) {System.out.println("please check the synapse object#: "+name);}
			String post = rs.getString(3);
			if (post==null) {System.out.println("please check the synapse object#: "+name);}
			
			String type = rs.getString(4);
			String imgNum = rs.getString(5);
			int x = rs.getInt(6);
			int y = rs.getInt(7);
			
			if(post!=null && pre!=null){
		
			SynCon syn = new SynCon(name, pre, post, type,imgNum,x,y);
			
			String precontin="",precontinname="";
			
			if (objs.containsKey(pre))
			{
	           
				
				precontin = (((ObjCon)objs.get(pre)).continNum);
	           
	        
	           if (contins.containsKey(precontin))
	             {
	                    precontinname = (String)(contins.get(precontin));
	      
	             }else{
	                    precontinname = "contin"+precontin;
	             }
			}else{
				precontinname = "obj" + pre;
			}
			syn.setPrename(precontinname);
			
			String postname="";
		//	System.out.print("post: "+syn.post+"      ");
	        String[] posts = (syn.post).split(",");
	        syn.setPostlength(posts.length);
	       
	        
	        
	        for(int i=0;i<posts.length;i++)
	        {
	        	String postcontinname="";
	        	//System.out.println("postobj:"+posts[i]);
	        	if (objs.containsKey(posts[i]))
	    		{
	        		
	        		String postcontin = (((ObjCon)objs.get(posts[i])).continNum);
	              // System.out.print("postcontin: "+postcontin+"      ");
	            
	               if (contins.containsKey(postcontin))
	                 {
	                       postcontinname = (String)(contins.get(postcontin));
	                  
	                      
	                 }else{
	                       postcontinname = "contin"+postcontin;
	                       
	                 }
	    		}else{
	    			postcontinname = "obj" + pre;
	    			
	    		}
	        	
	        	//System.out.print("postcontinname: "+postcontinname+"      ");
	        	if(i==0){postname=postcontinname;}
	        	else{
	        		if (postname.compareTo(postcontinname)>0) 
	        		{
	        			postname=postname+","+postcontinname;
	        		}
	        		else
	        		{
	        			postname=postcontinname+","+postname;
	        		}
	        }
	        }
	        //System.out.print("postname: "+postname+"      ");
	        syn.setPostnames(postname);
	       
	        syn.shift();
	        
	      
	        
	        
	    
	      
	        
	        
	        //System.out.println(precontinname+" "+postname);
	        //savePartnership(syn.prename,syn.postname,syn.type);
	  
			syns.put(name,syn);
			
			
			
			
			
			
			
			}
		}
		rs.close();
		pstmt.close();
	}
	
	
	
	public void loadPreSyn() throws  SQLException, ClassNotFoundException,
	java.lang.InstantiationException, java.lang.IllegalAccessException
	{
		pres= new LinkedHashMap(100000);
		
		
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		Connection con = DriverManager.getConnection(
				DatabaseProperties.CONNECTION_STRING,
				DatabaseProperties.USERNAME, DatabaseProperties.PASSWORD);
		String jsql;
		PreparedStatement pstmt;
		ResultSet rs;
		String name="",pre="",post="",type="";
        // chemical and electrical, fromobj
		jsql = "select OBJ_Name,fromObj from object where  ( type='electrical' or type='chemical' )  order by fromObj";
		pstmt = con.prepareStatement(jsql);
		rs = pstmt.executeQuery();
		if (rs.next()) {
			name = rs.getString(1);
			pre = rs.getString(2);
			
		
		}
		while(rs.next()){
			if (pre.equals("62546")) System.out.println(name+" pre "+pre);
			if (!(rs.getString(2).equals(pre))){
				if (pre.equals("62546")) System.out.println(name+" pre "+pre+" added");
				pres.put(pre, name);
				name = rs.getString(1);
				pre = rs.getString(2);
				if (pre.equals("62546")) System.out.println(name+" pre "+pre+" next");
			}else{
			name = name + ","+ rs.getString(1);
			}
		}
		rs.close();
		pstmt.close();
		
		// electrical, to obj
	
		
		jsql = "select OBJ_Name,toObj from object where type='electrical' order by toObj";
		pstmt = con.prepareStatement(jsql);
		rs = pstmt.executeQuery();
		if (rs.next()) {
			name = rs.getString(1);
			pre = rs.getString(2);
		
		}
		while(rs.next()){
			if (!(rs.getString(2).equals(pre))){
				
				
				if(!pres.containsKey(pre))
					{
					pres.put(pre, name);
					}
				    else
				    {
				    String newname = ((String) pres.get(pre))+","+name;
				    pres.put(pre,newname);
				    }
				
				
				name = rs.getString(1);
				pre = rs.getString(2);
				
			}else{
			name = name + ","+ rs.getString(1);
			}
		}
		rs.close();
		pstmt.close();		
		
	}
	
	
	
	public void connectSynsByRels()
	{
		System.out.println("begin to connect synapse between each relationship table pair");
		synrels = new ArrayList<SynRel>(100000);
		Set key = rels.entrySet();
		Iterator iter = key.iterator();
		while(iter.hasNext())
		{
			Map.Entry entry = (Map.Entry) iter.next();
			Rel rel = (Rel) entry.getValue();
			String name1 = rel.getObjName1();
			String name2 = rel.getObjName2();
			
			if ((pres.containsKey(name1))&&(pres.containsKey(name2)))
			{
				if (name1.equals("62546")) System.out.println("relationship pair:  "+name1+"  "+name2);
				connectSynRel(name1,name2);
			}
	
		}// end of iter of syn
	}	
	
	public void connectSynRel(String name1, String name2)
	{
		String syns1s = (String)pres.get(name1);
		String syns2s = (String)pres.get(name2);
		if (name1.equals("62546"))System.out.println("name1 name2: "+name1+" "+name2);
		String[] syns1 = syns1s.split(",");
		String[] syns2 = syns2s.split(",");
		
		for (int i=0; i< syns1.length;i++)
		{
			
			if (name1.equals("62546"))System.out.println("s1: "+syns1[i]);
			SynCon s1 = (SynCon) syns.get(syns1[i]);
			
			for (int j=0; j< syns2.length;j++)
			{
				if (name1.equals("62546"))System.out.println("s1: "+syns1[i]+"s2: "+syns2[j]);
				SynCon s2 = (SynCon) syns.get(syns2[j]);
				if (name1.equals("62546")) System.out.println(s1.synID+"  "+s2.synID);
				if(isSameSynapses(s1,s2))
				{
					if (name1.equals("62546")) System.out.println(s1.synID+"  "+s2.synID+"  find same");
					if (name1.equals("62546")) System.out.println(name1+"  "+name2+ " find same post partner");	
					if(s1.postjoincount==0 && s2.prejoincount==0)
						{
						if (name1.equals("62546")) System.out.println(name1+"  "+name2+ " find same synapses"+s1.synID+"  "+s2.synID);	
						s1.addPostJoin(s2.synID);
						s2.addPreJoin(s1.synID);
						SynRel sr = new SynRel(s1.synID,s2.synID);
						synrels.add(sr);
						}// s1==0 s2==0
						
						if	(s1.postjoincount>0 && s2.prejoincount>0)
							{
								SynCon s3 = (SynCon) syns.get(s1.postmember);
								SynCon s4 = (SynCon) syns.get(s2.premember);
								
								int x140 = ((ObjCon)objs.get(s1.pre)).x;
								int y140 = ((ObjCon)objs.get(s1.pre)).y;
								int x230 = ((ObjCon)objs.get(s3.pre)).x;
								int y230 = ((ObjCon)objs.get(s3.pre)).y;
								
								int x1 = s1.x-x140;
								int y1 = s1.y-y140;
								int x4 = s4.x-x140;
								int y4 = s4.y-y140;
								int x2 = s2.x-x230;
								int y2 = s2.y-y230;
								int x3 = s3.x-x230;
								int y3 = s3.y-y230;
								
								
								
								double d13 = Math.pow(((x1-x3)*(x1-x3)+(y1-y3)*(y1-y3)),0.5);
								double d42 = Math.pow(((x4-x2)*(x4-x2)+(y4-y2)*(y4-y2)),0.5);
															
								double d12 = Math.pow(((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2)),0.5);
								double d43 = Math.pow(((x4-x3)*(x4-x3)+(y4-y3)*(y4-y3)),0.5);
								
								if ((d13+d42)>(d12+d43))// change connection between 13 42 to  12 43
								{
									s1.clearPostJoin();
									s3.clearPreJoin();
									s4.clearPostJoin();
									s2.clearPreJoin();
									if(removeRel(s1.synID,s3.synID))System.out.println("remove one s1>0 s2>0 synrel");
									if (removeRel(s4.synID,s2.synID))System.out.println("remove one s1>0 s2>0 synrel");
									
									s1.addPostJoin(s2.synID);
									s3.addPreJoin(s4.synID);
									s4.addPostJoin(s3.synID);
									s2.addPreJoin(s1.synID);
									synrels.add(new SynRel(s1.synID,s2.synID));
									synrels.add(new SynRel(s4.synID,s3.synID));
									
									
								}
							
							}// s1>0 s2>0
							
							
							if((s1.postjoincount>0)&& (s2.prejoincount==0))
							{
								SynCon s3 = (SynCon) syns.get(s1.postmember);
								if (removeRel(s1.synID,s1.postmember)) System.out.println("remove one s1>0 synrel");
								s1.clearPostJoin();
								s3.clearPreJoin();
								SynCon s4 = selectRel(s2,s3,s1);
								synrels.add(new SynRel(s1.synID,s4.synID));
								s1.addPostJoin(s4.synID);
								s4.addPreJoin(s1.synID);
							}// s1>0 s2=0
							if((s1.postjoincount==0)&& (s2.prejoincount>0))
							{
								SynCon s3 = (SynCon) syns.get(s2.premember);
								if(removeRel(s3.synID,s2.synID))System.out.println("remove one s2>0 synrel");
								
								s2.clearPreJoin();
								s3.clearPostJoin();
								SynCon s4 = selectRel(s1,s3,s2);
								synrels.add(new SynRel(s4.synID,s2.synID));
								s2.addPreJoin(s4.synID);
								s4.addPostJoin(s2.synID);
							} // s1==0 s2>0
				}// same synapses
			}// for j
		}//for i
	}
	
	public boolean isSameSynapses(SynCon s1, SynCon s2)
	{
		if((s1.type.equals("chemical")) && (s2.type.equals("chemical")))
		{
		if (compareTwoSyn(s1,s2)) return true;
		}	
		
		if((s1.type.equals("electrical")) && (s2.type.equals("electrical")))
		{
		if (((s1.postname.trim()).equals(s2.postname.trim()))&&(s1.prename.trim().equals(s2.prename.trim()))) return true;
		if (((s1.postname.trim()).equals(s2.prename.trim()))&&(s1.prename.trim().equals(s2.postname.trim()))) return true;
		}
		return false;
	}
	
	public boolean removeRel(String s1, String s2)
	{
		for (int i=0;i<synrels.size();i++)
		{
			String n1 = synrels.get(i).syn1;
			String n2 = synrels.get(i).syn2;
			if ((n1.equals(s1)) && (n2.equals(s2))) 
				{
				synrels.remove(i);
				return true;
				}		
			
		}
		return false;
	}
	
	public SynCon selectRel(SynCon s1,SynCon s2, SynCon s3)
	{
		//if ((s1.postname.equals(s3.postname)) && (!s2.postname.equals(s3.postname))) return s1;
		//if ((!s1.postname.equals(s3.postname)) && (s2.postname.equals(s3.postname))) return s2;
		int x120 = ((ObjCon)objs.get(s1.pre)).x;
		int y120 = ((ObjCon)objs.get(s1.pre)).y;
		int x30 = ((ObjCon)objs.get(s3.pre)).x;
		int y30 = ((ObjCon)objs.get(s3.pre)).y;
		
		double d1 = Math.pow(((s1.x - x120) - (s3.x-x30)),2) + Math.pow(((s1.y - y120) - (s3.y-y30)),2);
		double d2 = Math.pow(((s2.x - x120) - (s3.x-x30)),2) + Math.pow(((s2.y - y120) - (s3.y-y30)),2);
		
		if (d1>d2) return s2;
		
		return s1;
	}
		
	
	public boolean compareTwoSyn(SynCon syn1, SynCon syn2)
	{
		
		//System.out.println(syn1.synID+"   "+syn1.postname);
		String[] s1 = syn1.postname.split(",");
		String[] s2 = syn2.postname.split(",");
		
		for (int i=0; i<s1.length;i++)
		{
			for(int j=0;j<s2.length;j++)
			{
				if((s1[i].trim()).equals(s2[j].trim())) 
				{
					return true;
				}
			}
		}
		return false;
	}
	


	public void saveSynCon()
	{
		System.out.println(synrels.size()+" number of synapse relationship saved");
		for (int i=0;i<synrels.size();i++)
		{
			connectSyns(((SynRel)synrels.get(i)).syn1,((SynRel)synrels.get(i)).syn2);
			
		}
	}
	
	
   
   
   
   
   public void connectSyns(String syn1,String syn2)
   {
	   PreparedStatement pst0 = null, pst=null;
	   ResultSet rs=null;
		try{    
			int img1=0, img2=0;
			String syn3="";
			String jsql = "select IMG_SectionNumber from object,image where object.IMG_Number=image.IMG_Number and OBJ_Name=? ";
			pst0 = con.prepareStatement(jsql);
			pst0.setString(1, syn1);
			rs = pst0.executeQuery();
			if (rs.next()) img1= rs.getInt(1);
			pst0.close();
			rs.close();
			
			pst0 = con.prepareStatement(jsql);
			pst0.setString(1, syn2);
			rs = pst0.executeQuery();
			if (rs.next()) img2= rs.getInt(1);
			pst0.close();
			rs.close();
			
			if (img1 > img2) 
			{
				syn3 = syn1;
				syn1 = syn2;
				syn2 = syn3;
			}
			
			
			
			    
			jsql = "select * from relationship where (ObjName1=? and ObjName2=?) or (ObjName1=? and ObjName2=?) ";
			pst0 = con.prepareStatement(jsql);
			pst0.setString(1, syn1);
			pst0.setString(2, syn2);
			pst0.setString(3, syn2);
			pst0.setString(4, syn1);
			rs = pst0.executeQuery();
			if (!rs.next()) 
			{ 
				pst = con.prepareStatement("insert into relationship (ObjName1,ObjName2,continNum) values (?,?,?)" );
				
				pst.setString(1,syn1);
				pst.setString(2,syn2);
				pst.setInt(3,0);
				pst.executeUpdate();
				pst.close();
			}	
			pst0.close();
			rs.close();
			
		    }catch (Exception e) 
	            {
               e.printStackTrace (  );
               } 
	      
	   
   }
}

   
  