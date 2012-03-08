


import java.sql.*;
import java.util.*;





class Synapses2
{
	LinkedHashMap syns,syns2,pres,objPostInd,rels,contins,images,contintypes,objs,synstoberemove;
	Connection con= null;
	
	
	
	public void loadContinType() throws SQLException,
	ClassNotFoundException, java.lang.InstantiationException,
	java.lang.IllegalAccessException 
	{
		contintypes = new LinkedHashMap(2000);
        
        Connection con = EDatabase.borrowConnection(
		
		);
        String jsql;
        PreparedStatement pstmt;
        ResultSet rs;

        jsql = "select CON_Number,type from contin";
        pstmt = con.prepareStatement(jsql);
        rs = pstmt.executeQuery();
        while (rs.next()) 
        {
	     String conNum = rs.getString(1);
	     String conType = rs.getString(2);
	 
	     contintypes.put(conNum, conType);
        }
    rs.close();
    pstmt.close();
    }
	public void loadImageTable() throws SQLException,
	ClassNotFoundException, java.lang.InstantiationException,
	java.lang.IllegalAccessException 
	{
		images = new LinkedHashMap(2000);
        
        Connection con = EDatabase.borrowConnection(
		
		);
        String jsql;
        PreparedStatement pstmt;
        ResultSet rs;

        jsql = "select IMG_Number,IMG_Series from image";
        pstmt = con.prepareStatement(jsql);
        rs = pstmt.executeQuery();
        while (rs.next()) 
        {
	     String imgNum = rs.getString(1);
	     String imgSeries = rs.getString(2);
	 
	     images.put(imgNum, imgSeries);
        }
    rs.close();
    pstmt.close();
    }
	
	public void loadContinTable() throws SQLException,
	ClassNotFoundException, java.lang.InstantiationException,
	java.lang.IllegalAccessException 
	{
		contins = new LinkedHashMap(2000);
        
        Connection con = EDatabase.borrowConnection(
		
		);
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
     
     Connection con = EDatabase.borrowConnection(
		                         
		                         );
     String jsql;
     PreparedStatement pstmt;
     ResultSet rs;
     objs= new LinkedHashMap(500000);
     jsql = "select OBJ_Name,CON_Number from object where type like 'cel%'";
     pstmt = con.prepareStatement(jsql);
     rs = pstmt.executeQuery();
     while (rs.next()) 
       {

	     String name = rs.getString(1);
	
	     String conN = rs.getString(2);

	     objs.put(name, conN);

       }
        rs.close();
        pstmt.close();

    }
	public void loadRelationshipTable() throws SQLException,
	ClassNotFoundException, java.lang.InstantiationException,
	java.lang.IllegalAccessException 
	{
		rels = new LinkedHashMap(5000000);
        
        Connection con = EDatabase.borrowConnection(
		
		);
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
		syns2= new LinkedHashMap(100000);
		
		
		
		Connection con = EDatabase.borrowConnection(
				
				);
		String jsql;
		PreparedStatement pstmt;
		ResultSet rs;

		jsql = "select OBJ_Name,fromObj,toObj,type,IMG_Number from object where ( type='chemical' or type='electrical' ) and DateEntered>'2009-04-10'  and IMG_Number like 'PAG%'";
		pstmt = con.prepareStatement(jsql);
		rs = pstmt.executeQuery();
		while (rs.next()) {
			String name = rs.getString(1);
			String pre = rs.getString(2);
			if (pre==null) {ELog.info("please check the synapse object#: "+name);}
			String post = rs.getString(3);
			if (post==null) {ELog.info("please check the synapse object#: "+name);}
			
			String type = rs.getString(4);
			String imgNum = rs.getString(5);
			
			if(post!=null){
		
			Syn syn = new Syn(name, pre, post, type,imgNum);
			syns2.put(name,syn);
			
			if (images.containsKey(imgNum))
			{
				String series = (String)images.get(imgNum);
				syn.setSeries(series);
			}
			
			String precontin="",precontinname="",pretype="neuron",posttype="neuron";
			
			if (objs.containsKey(pre))
			{
	           precontin = (String)objs.get(pre);
	           
	        
	           if (contins.containsKey(precontin))
	             {
	                    precontinname = (String)(contins.get(precontin));
	                    pretype = (String)(contintypes.get(precontin));
	            
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
	        syn.setFirstmember(name);
	        syn.setLastmember(name);
	        syn.setFirstImage(imgNum);
	        syn.setLastImage(imgNum);
	        
	        for(int i=0;i<posts.length;i++)
	        {
	        	String postcontinname="";
	        	//Util.info("postobj:"+posts[i]);
	        	if (objs.containsKey(posts[i]))
	    		{
	               String postcontin = (String)objs.get(posts[i]);
	              // System.out.print("postcontin: "+postcontin+"      ");
	            
	               if (contins.containsKey(postcontin))
	                 {
	                       postcontinname = (String)(contins.get(postcontin));
	                       String temptype = (String)(contintypes.get(postcontin));
	                       if (!(temptype.equals("neuron"))) posttype=temptype;
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
	        if (syn.type.equals("electrical") && pretype.equals("muscle")&& posttype.equals("neuron"))
	        {
	        	pretype="neuron";
	        	posttype="muscle";
	        }
	        syn.setType2(pretype+" -> "+posttype);
	        
	  
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
		
		
		
		Connection con = EDatabase.borrowConnection(
				
				);
		String jsql;
		PreparedStatement pstmt;
		ResultSet rs;
		String name="",pre="",post="",type="";
        // chemical
		jsql = "select OBJ_Name,fromObj from object where type='chemical'  and DateEntered>'2009-04-10' and IMG_Number like 'PAG%' order by fromObj";
		pstmt = con.prepareStatement(jsql);
		rs = pstmt.executeQuery();
		if (rs.next()) {
			name = rs.getString(1);
			pre = rs.getString(2);
		
		}
		while(rs.next()){
			if (!(rs.getString(2).equals(pre))){
				pres.put(pre, name);
				name = rs.getString(1);
				pre = rs.getString(2);
				
			}else{
			name = name + ","+ rs.getString(1);
			}
		}
		rs.close();
		pstmt.close();
		
		// electrical
		jsql = "select OBJ_Name,fromObj from object where type='electrical'  and DateEntered>'2009-04-10'  and IMG_Number like 'PAG%' order by fromObj";
		pstmt = con.prepareStatement(jsql);
		rs = pstmt.executeQuery();
		if (rs.next()) {
			name = rs.getString(1);
			pre = rs.getString(2);
		
		}
		
		while(rs.next()){
			if (!(rs.getString(2).equals(pre))){
				pres.put(pre, name);
				name = rs.getString(1);
				pre = rs.getString(2);
				
			}else{
			name = name + ","+ rs.getString(1);
			}
		}
		rs.close();
		pstmt.close();	
		
		jsql = "select OBJ_Name,toObj from object where type='electrical'  and DateEntered>'2009-04-10'  and IMG_Number like 'PAG%' order by toObj";
		pstmt = con.prepareStatement(jsql);
		rs = pstmt.executeQuery();
		if (rs.next()) {
			name = rs.getString(1);
			pre = rs.getString(2);
		
		}
		while(rs.next()){
			if (!(rs.getString(2).equals(pre))){
				pres.put(pre, name);
				name = rs.getString(1);
				pre = rs.getString(2);
				
			}else{
			name = name + ","+ rs.getString(1);
			}
		}
		rs.close();
		pstmt.close();		
		
	}
	
	public void loadPostIndexTable() throws SQLException,
	ClassNotFoundException, java.lang.InstantiationException,
	java.lang.IllegalAccessException {
	objPostInd = new LinkedHashMap(500000);
    
    Connection con = EDatabase.borrowConnection(
		
		);
    String jsql;
    PreparedStatement pstmt;
    ResultSet rs;
    jsql = "select relID,objName1 from relationship order by objName1";
    pstmt = con.prepareStatement(jsql);
    rs = pstmt.executeQuery();
    int n1 = 0;
    String post = "";
    if (rs.next()) 
     {
	  n1 = rs.getInt(2);
	  post = rs.getString(1);
     }
    while (rs.next()) 
     {
	    if (rs.getInt(2) != n1) 
	      {
		  objPostInd.put(Integer.toString(n1), post);
		  post = rs.getString(1);
		  n1 = rs.getInt(2);
		  } else {
		  post = post + "," + rs.getString(1);
	      }
     }
     rs.close();
     pstmt.close();
   }
	
	public void combineSyns()
	{
		synstoberemove = new LinkedHashMap(100000);
		Set key = syns.entrySet();
		Iterator iter = key.iterator();
		while(iter.hasNext())
		{
			Map.Entry entry = (Map.Entry) iter.next();
			String synname = (String) entry.getKey();
			
			if (!(synstoberemove.containsKey(synname))){
			Syn syn = (Syn) entry.getValue();
			//Util.info("one syn "+syn.post);
			combineSyn(syn);
			}
		}// end of iter of syn
		
		
		key = synstoberemove.entrySet();
		iter = key.iterator();
		while(iter.hasNext())
		{
			Map.Entry entry = (Map.Entry) iter.next();
			String synname = (String) entry.getKey();
			syns.remove(synname);
		
		}// end of iter of syn
	}		
		
	
	public boolean compareTwoSyn(Syn syn1, Syn syn2)
	{
		String[] s1 = syn1.postname.split(",");
		String[] s2 = syn2.postname.split(",");
		
		for (int i=0; i<s1.length;i++)
		{
			for(int j=0;j<s2.length;j++)
			{
				if(s1[i].equals(s2[j])) 
				{
					
					//String newpostname = joinTwoPostname(s1,s2);
					//syn1.setPostnames(newpostname);
					return true;
				}
			}
		}
		return false;
	}
	
	public String joinTwoPostname(String[] s1, String[] s2)
	{
		ArrayList names = new ArrayList(10);
		for(int j=0;j<s1.length;j++)
		{
			if (!names.contains(s1[j]))names.add(s1[j]);
		}
		for(int j=0;j<s2.length;j++)
		{
			if (!names.contains(s2[j]))names.add(s2[j]);
		}
		String pname="";
		for(int j=0;j<names.size();j++)
		{
			if(pname.equals("")) {pname=(String)names.get(j);}
			else{pname=pname+","+(String)names.get(j);}
		
		}
		return pname;
	}
	public void combineSyn(Syn syn)
	{
		if (objPostInd.containsKey(syn.lastpre)) 
		{
			
			//Util.info("find a adjacent");
			String[] relids = ((String)objPostInd.get(syn.lastpre)).split(",");
			for(int i=0;i<relids.length;i++)
			{
				Rel rel = (Rel) rels.get(relids[i]);
				String next = rel.getObjName2();
				//Util.info("find a next"+next);
				if(pres.containsKey(next))
				{
					//Util.info("find a next syn"+next);
					String[] synids = ((String)pres.get(next)).split(",");
					
					for(int j=0;j<synids.length;j++)
					{
						if ((syns.containsKey(synids[j])) && (!(synstoberemove.containsKey(synids[j])))){
						Syn synnext = (Syn) syns.get(synids[j]);
						//Util.info("find a next syn "+synnext.post);
						if((syn.type.equals(synnext.type))&& (syn.prename.equals(synnext.prename)))
						{
							
							//Util.info("find a combine"+synnext);
							if (syn.postname.equals(synnext.postname) )
							{
								int syn1 = Integer.parseInt(syn.lastmember);
								int syn2 = Integer.parseInt(synnext.firstmember);
								connectSyn(syn1,syn2);
								syn.addMember(synnext.members);
								syn.setLastmember(synnext.lastmember);
								syn.setLastImage(synnext.lastImage);
								synstoberemove.put(synids[j], "");
								syn.setLastpre(synnext.lastpre);
								combineSyn(syn);
							}
							
							
							else if (compareTwoSyn(syn, synnext)){
								
								//insert into combineFlag table
								//Util.info("flag out "+syn.lastmember);
								//flag(syn,synnext);
							}
							
						}	// end of if same post
						}//end of if containsKey
					}// end of for next synapses
					
				
			    }// end of if pres
			
			}// end of for next obj
			
		}// end of if next obj
	}
	
	
	
	
	public Synapses2()
	{
		
		try {
			
			con = EDatabase.borrowConnection(
					
					);
			deletePreviousData();
			removeSynRel();
			loadContinType();
			loadImageTable();
			loadContinTable();
			loadObjectTable();
			loadRelationshipTable();
			loadSyns();
			loadPreSyn();
			loadPostIndexTable();
			combineSyns();
			
			save();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	 private void deletePreviousData()
	   {
		   //delete the previous synapse data
		    
		    PreparedStatement pst = null;
			try{    
				    
					
					
				    
					pst = con.prepareStatement("delete from synapsecombined2" );
					pst.executeUpdate();
					pst.close();
					
					pst = con.prepareStatement("alter table synapsecombined2 auto_increment=0 " );
					pst.executeUpdate();
					pst.close();
					
					//pst = con.prepareStatement("delete from combineFlag" );
					//pst.executeUpdate();
					//pst.close();
					
					//pst = con.prepareStatement("alter table combineFlag auto_increment=0 " );
					//pst.executeUpdate();
					//pst.close();
					
					pst = con.prepareStatement("delete from synapsenomultiple2" );
					pst.executeUpdate();
					pst.close();
					pst = con.prepareStatement("alter table synapsenomultiple2 auto_increment=0 " );
					pst.executeUpdate();
					pst.close();
			    }catch (Exception e) 
		            {
	                e.printStackTrace (  );
	                } 
		      
		//end of delete
	   }
	 
	 



   private void save()throws SQLException,
	    ClassNotFoundException, java.lang.InstantiationException, java.lang.IllegalAccessException 
   {
	 
	   try
		{
			
	Set key = syns.entrySet(); 
	Iterator iter = key.iterator();
	while(iter.hasNext())
	{
		String precontin="";
		int precontinnumber=0;
		
		Map.Entry entry = (Map.Entry) iter.next();
		Syn syn = (Syn) entry.getValue();
		
		
        String members = syn.members;
        String[] memberss = members.split(",");
        int sections = memberss.length;
        String mid = memberss[sections/2];
        syn.setSections(sections);
        syn.setMid(mid);
        saveSynContin(syn);
        
     
		saveSyns(syn);
	
		
		
	
		
		
	}//end of iterator
		 }catch (Exception e) 
         {
             e.printStackTrace (  );
            } 
	         
   }
   
   void saveSynContin(Syn syn)
   {
	            
	   
	   PreparedStatement pst = null,pst1 = null;
	   ResultSet rs;
	   int conN=0;
		   try{    
			    pst = con.prepareStatement("select max(CON_Number) from contin");
			    rs = pst.executeQuery();
			    if ( rs.next (  ) )
	             {
	               conN = rs.getInt("max(CON_Number)")+1;
	               syn.setContinNum(conN);
	             }

			
				pst = con.prepareStatement("insert into contin (CON_Number,CON_AlternateName,type) values(?,?,?)");
                pst.setInt(1, conN);
                pst.setString(2, "syn"+conN);
                pst.setString(3, syn.type);
               	pst.executeUpdate();
				pst.close();
				
				String[] mem = syn.members.split(",");
				for(int i=0;i<mem.length;i++)
				{
					 pst = con.prepareStatement ("update object set CON_number=? where OBJ_Name =?");
					 pst.setInt(1, conN);
					 pst.setString(2, mem[i]);
		             pst.executeUpdate();
					 pst.close();
				}
				
				
				
			    
				}catch (Exception e) 
	            {
                 e.printStackTrace (  );
                } 
	          
				

    }
  
   
   
   void flag(Syn syn1,Syn syn2)
   {
	            
	   
	   PreparedStatement pst = null,pst1 = null;
	   ResultSet rs;
		try{    
			    

			
				pst = con.prepareStatement("insert into combineFlag (synid1,syn1,synid2,syn2) values (?,?,?,?)");
                pst.setString(1, syn1.lastmember);
                pst.setString(2, syn1.type+" "+syn1.prename+"->"+syn1.postname+" @ "+syn1.lastImage);
                pst.setString(3, syn2.firstmember);
                pst.setString(4, syn2.type+" "+syn2.prename+"->"+syn2.postname+" @ "+syn2.firstImage);
				pst.executeUpdate();
				pst.close();
				
			    
				}catch (Exception e) 
	            {
                 e.printStackTrace (  );
                } 
	          
				

    }
  
   void saveSyns(Syn syn)
   {
	            
	   
	   PreparedStatement pst = null,pst1 = null;
	   ResultSet rs;
		try{    
			    

			
				pst = con.prepareStatement("insert into synapsecombined2 (pre, post, post1,post2,post3,post4,type,members,sections,partnerNum,type2,series,mid,preobj,postobj1,postobj2,postobj3,postobj4,continNum) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
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
                Syn syn2 = (Syn)syns2.get(syn.mid);
				//Util.info(syn2.sections);
                if(syn2.pre.length()>10) 
                    {
                	//Util.info(syn2.sections);

                	ELog.info("wrong pre synapse  synID: "+syn2.synID);
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
				
	 	
				
			    
				}catch (Exception e) 
	            {
                 e.printStackTrace (  );
                } 
	
    }
   public void removeSynRel()
   {
	   PreparedStatement pst0 = null, pst=null;
	   ResultSet rs=null;
		try{    
			    
			String jsql = "select relID from relationship,object where ObjName1=OBJ_Name and ( type='chemical' or type='electrical' ) ";
			pst0 = con.prepareStatement(jsql);
			
			rs = pst0.executeQuery();
			while (rs.next()) 
			{ 
				int relid = rs.getInt(1);
				pst = con.prepareStatement("delete from relationship where relID=?" );
				pst.setInt(1,relid);
			
				pst.executeUpdate();
				//Util.info("deleted "+relid);
				pst.close();
			}	
			pst0.close();
			rs.close();
			
		    }catch (Exception e) 
	            {
               e.printStackTrace (  );
               } 
	      
	   
   }
   
   public void savePartners(Syn syn)
	{
		
		   PreparedStatement pst1 = null;
		   try{    
				
				    String[] postnames = syn.postname.split(",");
		            
					for (int i = 0; i< postnames.length; i++)
					{
						
							pst1 = con.prepareStatement("insert into synapsenomultiple2 (pre,post,type,sections) values (?,?,?,?)");
							pst1.setString(1, syn.prename);
							pst1.setString(2, postnames[i]);
							pst1.setString(3, syn.type);
							pst1.setInt(4, syn.sections);
							
							pst1.executeUpdate();
							pst1.close();
					
						    
					}//end of for
	       
				    
					}catch (Exception e) 
		            {
	                e.printStackTrace (  );
	                } 
		         
	}
   
   public void connectSyn(int syn1,int syn2)
   {
	   PreparedStatement pst0 = null, pst=null;
	   ResultSet rs=null;
		try{    
			    
			String jsql = "select * from relationship where (ObjName1=? and ObjName2=?) or (ObjName1=? and ObjName2=?) ";
			pst0 = con.prepareStatement(jsql);
			pst0.setInt(1, syn1);
			pst0.setInt(2, syn2);
			pst0.setInt(3, syn2);
			pst0.setInt(4, syn1);
			rs = pst0.executeQuery();
			if (!rs.next()) 
			{ 
				pst = con.prepareStatement("insert into relationship (ObjName1,ObjName2,continNum) values (?,?,?)" );
				
				pst.setInt(1,syn1);
				pst.setInt(2,syn2);
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

   
  