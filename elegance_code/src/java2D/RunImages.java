package java2D;

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


public class RunImages{
	public static void main(String[] args) 
	{
		new RunImages();
	}
	
	public RunImages()
	{
		
	
      
		
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		
		try{
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		con = DriverManager.getConnection(
				DatabaseProperties.CONNECTION_STRING,
				DatabaseProperties.USERNAME, DatabaseProperties.PASSWORD);
	    st = con.createStatement();
	    rs = st.executeQuery("select continNum,name from neurontable where name='DD3'");
	    String contins = "";
	   
	    while(rs.next()) 
	           {  
                    contins =rs.getString(1);
                    String[] conNums = contins.split(",");
        	        int[] continNums = new int[conNums.length];
        	        for (int j=0;j<conNums.length;j++)
        	        { 
        	        	continNums[j]=Integer.parseInt(conNums[j]);
        	        } 
			        System.out.println("begin to generate images of "+continNums[0]);
			        createImageFile(continNums,"Z_Y",5,1);
			        createImageFile(continNums,"Z_X",5,1);
			        createImageFile(continNums,"Z_X",1,1);
			        createImageFile(continNums,"Z_Y",1,1);
			   }// end of while
	    
		}catch(Exception e1){
	        e1.printStackTrace();
        }
		
		
            	
	}
	



	public static void createImageFile(int[] continNums, String dtype, int zoom, int stroke)
	{
		
		try{
    		TwoDCurve g = new TwoDCurve(continNums,dtype,zoom,stroke);
    		//System.out.println(g.width+"    "+g.height);
		    String filePath = "D:"+ File.separator + "www" + File.separator + "images" + 
		    File.separator + g.continNames[0] +"_"+ "X"+zoom+"_"+dtype+ ".png";
            File imgFile = new File(filePath);		
            java.io.FileOutputStream out = new java.io.FileOutputStream(imgFile);
			ImageIO.write(g.getBufferedImage(), "png", out);

			}
            catch(Exception e){
             e.printStackTrace();
            }
		
	}
	


}