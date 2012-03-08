package Admin;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LoadImageList {
	public static void main(String[] args) {
		try {
			File file = new File("Z:\\num.lst");
			if (file.exists()) {
				BufferedReader in = new BufferedReader(new FileReader(file));
				String line;
				while ((line = in.readLine()) != null) {
					
					line = line.substring(9, 13);
					//System.out.println(line);
					insertImage(line);
					

				}

				in.close();
				System.out.println(" done");

			} else {

				System.out
						.println("Could not find the file 'imageList.txt' continuing with default values...");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}// end of main
	
	public static void insertImage ( String num ) throws Exception{
		Class.forName ( "com.mysql.jdbc.Driver" ).newInstance (  );
		Connection con = null;
		con = DriverManager.getConnection ( 
				"jdbc:mysql://wormdesk1/n930",
			    "root",
			    "worms"
			 );
		PreparedStatement pst = con.prepareStatement ( 
			    "insert into image (IMG_Number, IMG_File, "
			    + "IMG_Directory, IMG_Worm, IMG_Series, "
			    + "IMG_PrintNumber, IMG_NegativeNumber, "
			    + "IMG_SectionNumber, IMG_EnteredBy, "
			    + "IMG_DateEntered, IMG_zoomValue, IMG_brightnessValue, "
			    + "IMG_contrastValue, IMG_rotatedValue) values "
			    + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 1.0, 0, 0, 0)"
			 );
		pst.setString(1, "N930Dorsal"+num);
		pst.setString(2, "N930_A_D_"+num+".tif");
		pst.setString(3,"Z:/N930_A/dorsal/");
		pst.setString(4,"N930");
		pst.setString(5,"dorsal");
		pst.setString(6,num);
		pst.setString(7,"0");
		pst.setString(8,num);
		pst.setString(9,"meng");
		pst.setString(10,"2010-08-06");
		
		pst.executeUpdate (  );	
		
	}
	
}
