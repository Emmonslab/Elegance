package Admin;


import javax.swing.*;



public class Ca{
	public static void main(String[] args) 
	{
		
	
       // String objectName = JOptionPane.showInputDialog ( null, "Enter the object name to calculate");
       // int objName = Integer.parseInt(objectName);
     

		
             try{
				long time1 = System.currentTimeMillis();
				
				String contin = JOptionPane.showInputDialog ( null, "Enter a contin number to calculate");
				int continNum = Integer.parseInt(contin);
		        new Calculate(continNum);
		       
		   
		        long time2 = System.currentTimeMillis();
				long time = (time2-time1)/1000;
				
				System.out.println("It took "+time+" to calculating");
				
		        }

		        catch(Exception e1){
		        e1.printStackTrace();
		        }

		
             
        }  

}

