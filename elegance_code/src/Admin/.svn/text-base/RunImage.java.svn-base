package Admin;


import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JFrame;


public class RunImage{
	
	Connection con = null;
	public static void main(String[] args) 
	{
		new RunImage();
	}
	
	public RunImage()
	{
		
	
      
		
		
		Statement st = null;
		ResultSet rs = null;
		
		try{
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		con = DriverManager.getConnection ( "jdbc:mysql://wormdesk1/elegance",  "root",  "worms" );
	    st = con.createStatement();
	    rs = st.executeQuery("select name,continNums from n2ytable where continNums<>'0'");
	    String contins = "", name="";
	   
	    while(rs.next()) 
	           {  
                    name =rs.getString(1);
                    contins = rs.getString(2);
                    
                    //updateContinNums(name,contins);
                    
                 String[] conNums = contins.split(",");
        	        int[] continNums = new int[conNums.length];
        	        for (int j=0;j<conNums.length;j++)
        	        { 
        	        	continNums[j]=Integer.parseInt(conNums[j]);
        	        } 
			        System.out.println("begin to generate images of "+name);
			        createImageFile(name,continNums,"Z_Y",5,1);
			        createImageFile(name,continNums,"Z_X",5,1);
			        createImageFile(name,continNums,"Z_X",1,1);
			        createImageFile(name,continNums,"Z_Y",1,1);
			      
			   }
	    
		}catch(Exception e1){
	        e1.printStackTrace();
        }
		
		
            	
	}
	
	public String  getContinNumsByName(String name)
	{
		String contins="";
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
		    pst = con.prepareStatement("select CON_Number from contin where (CON_AlternateName=? or CON_AlternateName=?) and " +
		    		"CON_Remarks like 'OK%'");
		    pst.setString(1, name);
		    pst.setString(2, "["+name+"]");
		    rs = pst.executeQuery();
		   
		    if(rs.next())  contins =rs.getString(1);
	          
		    while(rs.next()){
		    	contins=contins+","+rs.getString(1);
		    }// end of while
		    
			}catch(Exception e1){
		        e1.printStackTrace();
	        }
		
		return contins;
	
	}
	
	public void updateContinNums(String name, String contins)
	{
		
		PreparedStatement pst = null;
		
		try{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
		    pst = con.prepareStatement("update neuron set continNums=? where name=?");
		    pst.setString(1, contins);
		    pst.setString(2, name);
		    pst.executeUpdate();
		   
		   
		    }catch(Exception e1){
		        e1.printStackTrace();
	        }
		
		
	
	}



	public static void createImageFile(String name, int[] continNums, String dtype, int zoom, int stroke)
	{
		
		try{
    		TwoDCurveName g = new TwoDCurveName(name,continNums,dtype,zoom,stroke);
    		//System.out.println(g.width+"    "+g.height);
		    String filePath = "D:"+ File.separator + "www" + File.separator + "N2YImages" + 
		    File.separator + name +"_"+ "X"+zoom+"_"+dtype+ ".png";
            File imgFile = new File(filePath);		
            java.io.FileOutputStream out = new java.io.FileOutputStream(imgFile);
			ImageIO.write(g.getBufferedImage(), "png", out);

			}
            catch(Exception e){
             e.printStackTrace();
            }
		
	}
	


}
