package Admin;
import javax.swing.*;

import java.awt.*;
import java.sql.*;
import java.util.*;
import java.lang.*;

class Syn22
{
	public Syn22(String name, String pre, String post, String type, String imgNum) {
		this.synID=name;
		this.imgNum=imgNum;
		this.pre=pre;
		this.post=post;
		String[] posts = post.split(",");
		if (posts.length>=1) post1=posts[0];
		if (posts.length>=2) post2=posts[1];
		if (posts.length>=3) post3=posts[2];
		if (posts.length==4) post4=posts[3];
		this.type=type;
		

	}
	void setType2(String type2)
	{
		this.type2=type2;
	}
	
	
	
	
	public void setSynID(String id){
		synID=id;
	}
	public void setPrename(String name){
		prename=name;
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

	String type2;
	int partnerNum;


}


public class CompareUsers{
	public static void main(String[] args) 
	{
		long time1 = System.currentTimeMillis();
		
		
	
             try{

				 Connection con = null;
	             Statement st = null,st1 = null;
	             PreparedStatement pst1 = null,pst2=null,pst3=null;
	             ResultSet rs = null, rs1 = null, rs2=null;
	             String jsql = null;
				 
				 Class.forName("com.mysql.jdbc.Driver").newInstance();
		         con = DriverManager.getConnection ( "jdbc:mysql://127.0.0.1/electrical",  "root",  "worms" );
				 
		         jsql ="select distinct segmentNum from relationship where continNum=105 order by segmentNum";
		         st = con.createStatement();
		         rs = st.executeQuery(jsql);
		         while(rs.next())
		         {
		        	 int seg = rs.getInt(1);
		        	 
		        	 pst1 = con.prepareStatement("select ObjName1,IMG_Number from relationship,object where continNum=105 and segmentNum=? and ObjName1=OBJ_Name order by IMG_Number");
					 pst1.setInt ( 1, seg );
				     
				     
				     rs1=pst1.executeQuery();
				     
		             while(rs1.next())
		             {
		        	 String name = rs1.getString(1);
		        	 String img = rs1.getString(2);
		        	 
		        	 pst2=con.prepareStatement("select OBJ_Name,fromObj,toObj,type,username,DateEntered from object where type='chemical' and ( fromObj=? or toObj like ? or toObj like ? or toObj like ? or toObj=? ) order by fromObj");
		        	 pst2.setString(1,name);
		        	 pst2.setString(2,"%,"+name+",%");
		        	 pst2.setString(3,"%,"+name);
		        	 pst2.setString(4,name+",%");
		        	 pst2.setString(5,name);
		        	 rs2=pst2.executeQuery();
		        	 while(rs2.next())
		        	 {
		        		 String syn = rs2.getString(1);
		        		 String pre = rs2.getString(2);
		        		 String post = rs2.getString(3);
		        		 String type = rs2.getString(4);
		        		 String date = rs2.getString(6);
		        		 int num = (post.split(",")).length;
		        		 
		        		 if (date.compareTo("2009-04-10")>=0)
		        		 {
		        			 pst3 = con.prepareStatement("insert into compare2 (username, name,pre,post, type,type2,segment,image) values ('scott',?,?,?,?,?,?,?)");
		        			 
		        		 }else{
		        			 pst3 = con.prepareStatement("insert into compare2 (username, name,pre,post, type,type2,segment,image) values ('travis',?,?,?,?,?,?,?)");
		        		 }
		        		 
		        		 
						 pst3.setString ( 1, syn );
					     pst3.setString( 2, pre );
					     pst3.setString( 3, post);
					     pst3.setString( 4, type);
					     pst3.setInt( 5, num);
					     pst3.setInt( 6, seg);
					     pst3.setString( 7, img);
					     
					    
					     
					     
					     pst3.executeUpdate();
		        		 
		        		 
		        	 
		        	 }
		        	 
		        	 

		        	 
		        	 
		        	 
		        	 
		        	 
		        	 
		        	 
		
		          
				  
		         }
		
				
		         }

              
		        }

		        catch(SQLException e1){
		        e1.printStackTrace();
		        }

		        catch(java.lang.InstantiationException e2){
		        e2.printStackTrace();
		        }

	            catch(java.lang.IllegalAccessException e3){
		        e3.printStackTrace();
		        }
 
		        catch(ClassNotFoundException e4){
		        e4.printStackTrace();
		        }
		        
		        long time2 = System.currentTimeMillis();
				long time = (time2-time1)/1000;
				System.out.println("It took "+time+" to done ");
				
        }  

}
