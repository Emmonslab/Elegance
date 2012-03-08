
public class DatabaseProperties
{



    //    public static String CONNECTION_STRING="jdbc:mysql://192.168.1.3/elegance";
	//public static String USERNAME = "root";
	//public static String PASSWORD = "worms";

	
	

	public static String CONNECTION_STRING="jdbc:mysql://"+Elegance.host+"/"+Elegance.db;
	public static String CONNECTION="jdbc:mysql://"+Elegance.host+"/";
	public static String USERNAME = Elegance.dbusername;
	public static String PASSWORD = Elegance.dbpassword;


}