package Admin;

import javax.swing.*;

import Admin.RecordClear;

import java.awt.*;
import java.sql.*;

public class ClearRecord{
	public static void main(String[] args) 
	{
		
	
       

		
             try{
		        RecordClear syns = new RecordClear();
				JOptionPane.showMessageDialog(null, "Done", "Repeated synapses record Clear", JOptionPane.INFORMATION_MESSAGE);
				//System.out.println("Done, please press Ctrl + C to close the program");
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
            
        }  

}
