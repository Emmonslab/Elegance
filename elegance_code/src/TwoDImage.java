
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;


public class TwoDImage{
	
	private Display2DOptionsFrame displayOptionsFrame = null;
	
	public static void main(String[] args) 
	{
		new TwoDImage();
	}
	
	public TwoDImage()
	{
		
		displayOptionsFrame = new Display2DOptionsFrame();		 
		displayOptionsFrame.pack();
		displayOptionsFrame.setVisible(true);
		
		displayOptionsFrame.getOKButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if (!displayOptionsFrame.ok_actionPerformed(e)) return;
				
				try {
				int[] continNums=null;
				
				if (Elegance.display2DOptions.getContinFilterType() == Display2DOptions.ContinFilterType.custom_number) {
					Set<String> set=Elegance.display2DOptions.getContinFilterCustom();
					if (set.isEmpty()) {
						JOptionPane.showMessageDialog(displayOptionsFrame,"No contin numbers specified");
						return;
					}
					
					List<String> sortedList=new ArrayList<String>();
					
					for(Iterator<String> it=set.iterator();it.hasNext();) {
						String c=it.next();
						if (c.indexOf('-')>-1) {
							//setOfRanges.add(c);
							it.remove();
							String[] a=c.split("\\s*\\-\\s*");
							if (a.length!=2) {
								JOptionPane.showMessageDialog(displayOptionsFrame,"Invalid range of contins "+c+"");
								continue;
							}
							String sql="select con_number from contin where con_number between ? and ?";
							for(Object cNumber:EDatabase.getFirstColumn(sql, a[0],a[1])) {
								sortedList.add(cNumber+"");
							}							
						} else {
							sortedList.add(c);
						}
					}
					
					Collections.sort(sortedList,
						new Comparator<String>() {
							public int compare(String s1, String s2) {
								return new Long(s1).compareTo(new Long(s2));
							}
						}
					);
					
					continNums=new int[sortedList.size()];
					
					Set<Object> existingSet=new HashSet(EDatabase.getFirstColumn("select con_number from contin where con_number in (??)", sortedList));
					
					int i=0;
					for(String continNum:sortedList) {
						if (!existingSet.contains(new Long(continNum))) {
							JOptionPane.showMessageDialog(displayOptionsFrame,"Contin #"+continNum+" does not exist in database");
							return;
						}
						continNums[i++]=new Integer(continNum).intValue();
					}
					
					
					set=new LinkedHashSet<String>();
					set.addAll(sortedList);					
					Elegance.display2DOptions.setContinFilterCustom(set);
					
				} else if (Elegance.display2DOptions.getContinFilterType() == Display2DOptions.ContinFilterType.custom_name) {
					Set<String> set=Elegance.display2DOptions.getContinFilterCustom();
					if (set.isEmpty()) {
						JOptionPane.showMessageDialog(displayOptionsFrame,"No contin name specified");
						return;
					}
					
					
					List<Object> list=EDatabase.getFirstColumn("select con_number from contin where CON_AlternateName in (??)", set);
					
					if (list.isEmpty()) {
						JOptionPane.showMessageDialog(displayOptionsFrame,"No contin names found in database");
						return;
					}

					if (list.isEmpty()) {					
						JOptionPane.showMessageDialog(displayOptionsFrame,"No contin  found in database by name(s) "+set);
						return;
					}

					
					continNums=new int[list.size()];
					
					int i=0;
					for(Object continNum:list) {
						
						continNums[i++]=((Long)continNum).intValue();
					}
					
				} else if (Elegance.display2DOptions.getContinFilterType() == Display2DOptions.ContinFilterType.custom_color_code) {
					Set<String> set=Elegance.display2DOptions.getContinFilterCustom();
					if (set.isEmpty()) {
						JOptionPane.showMessageDialog(displayOptionsFrame,"No contin color code specified");
						return;
					}
					
					
					
					String sql=
						"select con_number\n"+
						"from contin \n"+
						"where CON_AlternateName2 is not null\n"+ 
						"and CON_AlternateName2 in (??)\n";

						
					List<Object> list=EDatabase.getFirstColumn(sql, set);
					
					if (list.isEmpty()) {
						JOptionPane.showMessageDialog(displayOptionsFrame,"No contin found in database by color code(s) "+set);
						return;
					}

					
					continNums=new int[list.size()];
					
					int i=0;
					for(Object continNum:list) {
						
						continNums[i++]=((Long)continNum).intValue();
					}
				}

				
				createImageFile(continNums,Elegance.display2DOptions.getDtype(),Elegance.display2DOptions.getZoom(),1);
				
				displayOptionsFrame.hide();
			
			} catch (Throwable e1) {
				ELog.info("Cant create image "+e1);
				throw new IllegalStateException("Cant create image",e1);
			}
			}
		});
	/*
        String conNum = JOptionPane.showInputDialog ( null, "Enter the contin numbers or contin name to display");
        
        String[] conNums = conNum.split(",");
        int[] continNums = new int[conNums.length];
        for (int i=0;i<conNums.length;i++)
        { 
        	continNums[i]=Integer.parseInt(conNums[i]);
        }
       Util.info(continNums[0]+""); 
    
     
        int zoom = 1;
        String[] possibleZoomValues = { "1", "2", "5","10" };
        String zoomInput = (String) JOptionPane.showInputDialog(null, 
                "Select one", "zoom factor",
                 JOptionPane.INFORMATION_MESSAGE, null,
                 possibleZoomValues, possibleZoomValues[0]);
       zoom = Integer.parseInt(zoomInput);
        



        String[] possibleValues = { "Z_Y", "Z_X", "Z_Y_NoSynapse","Z_X_NoSynapse" };
        String dtype = (String) JOptionPane.showInputDialog(null, 
        "Select one", "Type",
         JOptionPane.INFORMATION_MESSAGE, null,
         possibleValues, possibleValues[0]);

            		//Util.info("begin to generate the 5x image...");
         createImageFile(continNums,dtype,zoom,1);
            		//createImageFile(continNums,"Z_X",5,1);
            		//Util.info("begin to generate the 1x image...");
            		//createImageFile(continNums,dtype,1,1);
            		//createImageFile(continNums,"Z_Y",1,1);
            		
          
      //JOptionPane.showMessageDialog(null, "Done!", "2D", JOptionPane.INFORMATION_MESSAGE);
            	
            	*/
	}
	
	public static String now() {
	    Calendar cal = Calendar.getInstance();
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
	    return sdf.format(cal.getTime());

	  }


	public static void createImageFile(int[] continNums, String dtype, int zoom, int stroke)
	{
		
		try{
    		TwoDCurveName twoDImage = new TwoDCurveName(continNums,dtype,zoom,stroke);
    	
    		String fileDir="."+ File.separator + "images_out";
    		
    		if (!new File(fileDir).exists()) {
    			new File(fileDir).mkdirs();
    			ELog.info("Created new directory "+fileDir);
    		}
    		
    		
    		String continPart=twoDImage.continName;
    		
    		//lets keep file path length to something reasonable ...
    		if (continPart.length()>10) continPart=continPart.substring(0,10);
    		
		    String filePath = fileDir + File.separator  + continPart + "X"+zoom+"_"+dtype+"_" + ".png";
		    
		    
            File imgFile = new File(filePath);
            
            if (imgFile.exists()) imgFile.delete();
            
            java.io.FileOutputStream out = new java.io.FileOutputStream(imgFile);
			ImageIO.write(twoDImage.createImage(), "png", out);
			if(true || dtype.equals("Z_Y") && zoom==1){
				
				ImageViewer im = new ImageViewer(filePath);
				im.setVisible(true);
			}
			}
            catch(Exception e){
             e.printStackTrace();
            }
		
	}
	


}
