import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.awt.Point;
import javax.swing.JOptionPane;
import java.util.*;

public class ExtendedCellObject implements Comparable 
{
    String imageNumber;
	Point  p;
	String objectName   = null;
	int    continNumber = -1;
    int sectionNumber = -1;

    public ExtendedCellObject ( 
	    String newImageNo,
	    String newObjectName
	 )
	{
		imageNumber     = newImageNo;
		objectName      = newObjectName;
		setPointAndConNo (  );
        sectionNumber = Utilities.getSectionNumber(imageNumber);
	}
    private void setPointAndConNo (  )
	{
		Connection con = null;

		try
		{
			con = EDatabase.borrowConnection ( 
				    
				   
				    
				 );

			PreparedStatement pst =
				con.prepareStatement ( 
				    "select OBJ_X, OBJ_Y, CON_Number from object where IMG_Number = ? and OBJ_Name = ?"
				 );

			if ( ( imageNumber == null ) || ( objectName == null ) )
			{
				return;
			}

			pst.setString ( 1, imageNumber );
			pst.setString ( 2, objectName );

			ResultSet rs = pst.executeQuery (  );

			if ( rs.next (  ) )
			{
				

				p     = new Point( rs.getInt ( "OBJ_X" ), rs.getInt ( "OBJ_Y" ) );
				continNumber = rs.getInt ( "CON_Number" );
			}
			else
			{
				
				return;
			}
		}
		catch ( Exception ex )
		{
			ex.printStackTrace (  );
			JOptionPane.showMessageDialog ( 
			    null,
			    ex.getMessage (  ),
			    "Error",
			    JOptionPane.ERROR_MESSAGE
			 );

			

			return;
		}finally {
			EDatabase.returnConnection(con);
		}
	}
    private void setSectionNumber (  )
	{
		Connection con = null;

		try
		{
			con = EDatabase.borrowConnection ( 
				    
				   
				    
				 );

			PreparedStatement pst =
				con.prepareStatement ( 
				    "select IMG_SectionNumber from image where IMG_Number = ?"
				 );

			if ( ( imageNumber == null ) || ( objectName == null ) )
			{
				return;
			}

			pst.setString ( 1, imageNumber );

			ResultSet rs = pst.executeQuery (  );

			if ( rs.next (  ) )
			{
				

                String sectionNumberString = rs.getString("IMG_SectionNumber");
                if(sectionNumberString == null || sectionNumberString.equals("")) return;
                try
                {
                    sectionNumber = Integer.parseInt(sectionNumberString);
                }
                catch(Exception exx)
                {}
			}
			else
			{
				

				return;
			}
		}
		catch ( Exception ex )
		{
			ex.printStackTrace (  );
			JOptionPane.showMessageDialog ( 
			    null,
			    ex.getMessage (  ),
			    "Error",
			    JOptionPane.ERROR_MESSAGE
			 );

			
			return;
		}finally {
			EDatabase.returnConnection(con);
		}
	}

    public int compareTo(Object obj)
    {
        if(obj instanceof ExtendedCellObject)
        {
            ExtendedCellObject exobj = (ExtendedCellObject) obj;
            if(this.sectionNumber < exobj.sectionNumber)
            {
                return -1;
            }
            else if (this.sectionNumber > exobj.sectionNumber)
            {
                return 1;
            }
            else
            {
                return this.objectName.compareTo(exobj.objectName);
            }
        }
        else
        {
            int thisHash = this.hashCode (  );
			int objHash = obj.hashCode (  );

			if ( thisHash > objHash )
			{
				return 1;
			}
			else if ( thisHash < objHash )
			{
				return -1;
			}
			else
			{
				return 0;
			}
        }
    }
    public String toString()
    {
        return imageNumber + "  " + objectName + " " + p + " " + continNumber + " " + sectionNumber;
    }
    public boolean equals(Object obj)
    {
        if(obj instanceof ExtendedCellObject)
        {
            ExtendedCellObject excell = (ExtendedCellObject) obj;
            if(this.sectionNumber == excell.sectionNumber && this.objectName.equals(excell.objectName))
            {
                return true;
            }
            return false;
        }
        else
        {
            return super.equals(obj);
        }
    }
    public Vector getListOfSynapses()
    {
        Vector returner = new Vector();
        Connection con  = null;
        try
        {
            con =
                EDatabase.borrowConnection ( 
                
               
                
                );
            PreparedStatement pst = con.prepareStatement("SELECT REL_ConNumber1, REL_Type from relationship where REL_ObjName2 = ? AND REL_ImgNumber2 = ? AND REL_Type != ?");
            pst.setString(1, objectName);
            pst.setString(2, imageNumber);
            pst.setString(3, GlobalStrings.CONTINUOUS);

            ResultSet rs = pst.executeQuery();

            while(rs.next())
            {
                int conNoNew = rs.getInt("REL_ConNumber1");
                String conNameNew = Utilities.getContinName(new Integer(conNoNew));
                String typeNew = rs.getString("REL_Type");
                BranchedContinSynpase bcs = new BranchedContinSynpase(conNameNew, typeNew);
                int index = returner.indexOf(bcs);
                if(index == -1)
                {
                    returner.addElement(bcs);
                }
                else
                {
                    BranchedContinSynpase oldBCS = (BranchedContinSynpase) returner.elementAt(index);
                    oldBCS.continName += ", " + bcs.continName;
                    returner.setElementAt(oldBCS, index);
                }
            }
            pst = con.prepareStatement("SELECT REL_ConNumber2, REL_Type from relationship where REL_ObjName1 = ? AND REL_ImgNumber1 = ? AND REL_Type != ?");
            pst.setString(1, objectName);
            pst.setString(2, imageNumber);
            pst.setString(3, GlobalStrings.CONTINUOUS);

            rs = pst.executeQuery();

            while(rs.next())
            {
                int conNoNew = rs.getInt("REL_ConNumber2");
                String conNameNew = Utilities.getContinName(new Integer(conNoNew));
                String typeNew = rs.getString("REL_Type");
                BranchedContinSynpase bcs = new BranchedContinSynpase(conNameNew, typeNew);
                int index = returner.indexOf(bcs);
                if(index == -1)
                {
                    returner.addElement(bcs);
                }
                else
                {
                    BranchedContinSynpase oldBCS = (BranchedContinSynpase) returner.elementAt(index);
                    oldBCS.continName += ", " + bcs.continName;
                    returner.setElementAt(oldBCS, index);
                }
            }
           
        }
        catch ( Exception ex )
        {
            ex.printStackTrace (  );
            JOptionPane.showMessageDialog ( 
                null,
                ex.getMessage (  ),
                "Error",
                JOptionPane.ERROR_MESSAGE
                );
           
        }finally {
			EDatabase.returnConnection(con);
		}
        return returner;
    }
}