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



public class RunYiImage{
	public static void main(String[] args) 
	{
		new RunYiImage();
	}
	
	public RunYiImage()
	{
		
	
      int[] contins = {4873,4941,4997,6377,4783,4934,4939,4905,4945,4904,4952,5379,5375,5363,5559,4998,5378,5455,5384,5389,5556,5401};
	
		for (int contin:contins)	    {
			createImageFile(contin,"Z_X",1,1);
	        createImageFile(contin,"Z_Y",1,1);
			
		}
			        
	
		
            	
	}
	



	public static void createImageFile(int continNum, String dtype, int zoom, int stroke)
	{
		
		try{
    		TwoDCurveContin g = new TwoDCurveContin(continNum,dtype,zoom,stroke);
    		//System.out.println(g.width+"    "+g.height);
		    String filePath = "D:"+ File.separator + "www" + File.separator + "images" + 
		    File.separator + continNum +"_"+ "X"+zoom+"_"+dtype+ ".png";
            File imgFile = new File(filePath);		
            java.io.FileOutputStream out = new java.io.FileOutputStream(imgFile);
			ImageIO.write(g.getBufferedImage(), "png", out);

			}
            catch(Exception e){
             e.printStackTrace();
            }
		
	}
	


}
