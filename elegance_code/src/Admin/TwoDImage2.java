package Admin;



import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JFrame;



public class TwoDImage2{
	public static void main(String[] args) 
	{
		new TwoDImage2();
	}
	
	public TwoDImage2()
	{
		
	
        String conNum = JOptionPane.showInputDialog ( null, "Enter the contin numbers to display");
        String[] conNums = conNum.split(",");
        int[] continNums = new int[conNums.length];
        for (int i=0;i<conNums.length;i++)
        { 
        	continNums[i]=Integer.parseInt(conNums[i]);
        }
        
    
     
        int zoom = 0;
        //String zoomInput = JOptionPane.showInputDialog ( null, "Enter the magnification factor to display", "default");
        String zoomInput="default";
        if (zoomInput.equals("default")){zoom=0;}
        else{zoom = Integer.parseInt(zoomInput);}
        


/**
        String[] possibleValues = { "Z_Y", "Z_X", "Z_Y_NoSynapse","Z_X_NoSynapse" };
        String dtype = (String) JOptionPane.showInputDialog(null, 
        "Select one", "Type",
         JOptionPane.INFORMATION_MESSAGE, null,
         possibleValues, possibleValues[0]);
	**/	
             
            
            		
            		createImageFile(continNums,"Z_Y",1,1);
            		createImageFile(continNums,"Z_X",1,1);
            		
            	
	}
	
	public static String now() {
	    Calendar cal = Calendar.getInstance();
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
	    return sdf.format(cal.getTime());

	  }


	public static void createImageFile(int[] continNums, String dtype, int zoom, int stroke)
	{
		
		try{
    		TwoDCurve2 g = new TwoDCurve2(continNums,dtype,zoom,stroke);
    		//System.out.println(g.width+"    "+g.height);
		    String filePath =  "Z:"+ File.separator + "images" + File.separator  + g.continName + "X"+zoom+"_"+dtype+"_"+ now() + "-PAG.png";
            File imgFile = new File(filePath);		
            java.io.FileOutputStream out = new java.io.FileOutputStream(imgFile);
			ImageIO.write(g.getBufferedImage(), "png", out);
			if(dtype.equals("Z_Y") && zoom==1){
			ImageViewer im = new ImageViewer(filePath);
			im.setVisible(true);
			}
			}
            catch(Exception e){
             e.printStackTrace();
            }
		
	}
	


}
