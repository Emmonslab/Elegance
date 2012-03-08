package Admin;


import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class CaOldSyn{
	public static void main(String[] args) 
	{
		
	
        
		
				long time1 = System.currentTimeMillis();
				new Synapses();
				long time2 = System.currentTimeMillis();
				long time = (time2-time1)/1000;
				System.out.println("It took "+time+" to calculate. Done, please press Ctrl + C to close the program");
			
        }  

}
