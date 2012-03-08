import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.Vector;

import javax.swing.JOptionPane;


/**
 * The class depicting the relation between two CellObjects
 *
 * @author zndavid
 * @version 1.0
 */
public class Relation
{
	CellObject obj1;
	CellObject obj2;
	int segmentNum;
	

	/**
	 * Creates a new Relation object.
	 *
	 * @param newObj1 The first CellObject in the relation
	 * @param newObj2 The second CellObject in the relation
	 * @param newType The relation type.
	 */
	public Relation ( 
	    CellObject newObj1,
	    CellObject newObj2
	 )
	{
        
		obj1     = newObj1;
		obj2     = newObj2;
		segmentNum = 0;
	
	}
	public Relation ( 
	    CellObject newObj1,
	    CellObject newObj2,
	    int newSegmentNum
	 )
	{

		
		
            obj2 = newObj2;
			obj1 = newObj1;


		segmentNum  = newSegmentNum;
	}

	/**
	 * Creates a new Relation object.
	 */
	public Relation (  )
	{
		obj1     = null;
		obj2     = null;
		segmentNum = 0;
	}

	

	/**
	 * returns the first CellObject
	 *
	 * @return returns the first CellObject
	 */
	public CellObject getObj1 (  )
	{
		return obj1;
	}
    //get and set the group
		public int getSegmentNum (  )
	{
		return segmentNum;
	}

	   public void setSegmentNum(int segmentNum)
	{
	this.segmentNum=segmentNum;   
	}

	/**
	 * sets the first CellObject
	 *
	 * @param newObj1 the new first CellObject
	 */
	public void setObj1 ( CellObject newObj1 )
	{
		obj1 = newObj1;
	}

	/**
	 * returns the second CellObject
	 *
	 * @return returns the second CellObject
	 */
	public CellObject getObj2 (  )
	{
		return obj2;
	}

	/**
	 * gets the second CellObject
	 *
	 * @param newObj2 second CellObject
	 */
	public void setObj2 ( CellObject newObj2 )
	{
		obj2 = newObj2;
	}

	


	public void saveRelation (  )
	{
		
		Connection con = null;
		try
		{
			con = EDatabase.borrowConnection ();
            
		if ( ( obj1 == null ) || ( obj2 == null )  )
		{
			return;
		}
		String objName1=null;
		String objName2=null;
        int secNoForObj1 = obj1.getSectionNum (  );
		int secNoForObj2 = obj2.getSectionNum (  );
		if (secNoForObj1>=secNoForObj2)
		{
		objName1 = obj2.getObjName ( con );
		objName2 = obj1.getObjName ( con );
	
		}
		else
			{
	    
		
		objName1 = obj1.getObjName ( con  );
		objName2 = obj2.getObjName ( con );
			}
		if ( 
		    ( objName1 == null )
			    || ( objName1.compareTo ( "" ) == 0 )
			    || ( objName2 == null )
			    || ( objName2.compareTo ( "" ) == 0 )
			    || (objName2.equals(objName1))
		 )
		{
            ELog.info("obj name = null");
			return;
		}

	
        int relID=0;
		
            PreparedStatement pst =
				con.prepareStatement ( 
				    "select relID from relationship where (ObjName1=? and ObjName2=?) or (ObjName2=? and ObjName1=?)"
				 );
			pst.setString ( 1, objName1 );
			
			pst.setString ( 2, objName2 );
			pst.setString ( 3, objName1 );
			
			pst.setString ( 4, objName2 );
			
			ResultSet rs=pst.executeQuery();
			if (rs.next())
			{
				relID=rs.getInt(1);
				rs.close();
				pst.close();
				pst = con.prepareStatement ( 
				    "update relationship set ObjName1=?, ObjName2=? where relID=? "
				 );
			    pst.setString ( 1, objName1 );
			
			    pst.setString ( 2, objName2 );
			    pst.setInt ( 3, relID );
			
			    pst.executeUpdate (  );
                pst.close();
			}
				else
			{



		    pst = con.prepareStatement ( 
				    "insert into relationship (ObjName1, ObjName2) Values ( ?, ?) "
				 );
			pst.setString ( 1, objName1 );
			
			pst.setString ( 2, objName2 );
			
			
			pst.executeUpdate (  );
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
	}

	/**
	 * An override of the equals method
	 *
	 * @param obj any object
	 *
	 * @return returns true only if the objects is a Relation object and its two
	 *         CellObjects are the same. The CellObjects can occur in any order.
	 */
	public boolean equals ( Object obj )
	{
		try
		{
			Relation rela = ( Relation ) obj;

			if ( obj1.equals ( rela.obj1 ) && obj2.equals ( rela.obj2 ) )
			{
				return true;
			}

			if ( obj1.equals ( rela.obj2 ) && obj2.equals ( rela.obj1 ) )
			{
				return true;
			}

			return false;
		}
		catch ( Exception ex )
		{
			return false;
		}
	}

    public boolean alreadyExists (  )
	{
		Connection con = null;
        boolean returner = false;
		try
		{
			con = EDatabase.borrowConnection ( 
				    
				   
				    
				 );

			PreparedStatement pst =
				con.prepareStatement ( 
				    "select * from relationship where ObjName1 = ? and ObjName2 = ?"
				 );
			pst.setString ( 1, obj1.getObjName ( con ) );
			
			pst.setString ( 2, obj2.getObjName ( con ) );
		

			ResultSet rs = pst.executeQuery();

			if ( rs.next() )
			{
                returner = true;
            }
            else
            {
				pst.setString ( 1, obj2.getObjName ( con ) );
			
				pst.setString ( 2, obj1.getObjName ( con  ) );
				
				rs = pst.executeQuery();
                if(rs.next())
                {
                    returner = true;
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

	
	public void removeRelationInDatabase (  )
	{
		ELog.info("Will try to delete relation " + this);
		Connection con = null;

		try
		{
			con = EDatabase.borrowConnection ( 
				    
				   
				    
				 );

			PreparedStatement pst =
				con.prepareStatement ( 
				    "Delete from relationship where ObjName1 = ?  and ObjName2 = ?"
				 );
			pst.setString ( 1, obj1.getObjName ( con ) );
		
			pst.setString ( 2, obj2.getObjName ( con ) );
			

			int res1 = pst.executeUpdate (  );

			if ( res1 == 0 )
			{
				pst.setString ( 1, obj2.getObjName ( con ) );
				
				pst.setString ( 2, obj1.getObjName ( con ) );
				
				res1 = pst.executeUpdate (  );
			}

			ELog.info(res1+ " objects deleted in first round of delete");
			/** if (res1 > 0)
			{
                boolean isObj1EndPoint = false;
                boolean isObj2EndPoint = false;
				int oldContin = obj2.getContinNo (  );

				Util.info("old contin no = " + oldContin);
				Vector listOfRelationsToBeModified =
					Utilities.getRestOfContin ( obj2, obj1, oldContin );
                Util.info("obj1 = "+ obj1);
                Util.info("obj2 = "+ obj2);
                Util.info("listOfRelations:=\n");
                for (int z=0 ; z<listOfRelationsToBeModified.size(); z++) 
                {
                    Util.info(((Relation)listOfRelationsToBeModified.elementAt(z)));
                }
                Vector listOfObjectsToBeModified =
    					Utilities.getListOfContinuousObjects ( listOfRelationsToBeModified );

                if(listOfRelationsToBeModified.size() > 0 && listOfObjectsToBeModified.size() > 0)
                {
                    Util.info( listOfRelationsToBeModified.size() + " number of relations need to be modified");
                    

    				Util.info( listOfObjectsToBeModified.size() + " number of objects need to be modified");
                    for (int z=0 ; z<listOfObjectsToBeModified.size(); z++) 
                    {
                        Util.info(((CellObject)listOfObjectsToBeModified.elementAt(z)));
                    }
        			int newContin = Utilities.generateContinNumber (  );
            		Utilities.createNewContin ( oldContin, newContin, con );

                	Util.info("new contin number created for number " + newContin);
                    Utilities.deleteRelations ( listOfRelationsToBeModified );
    				Utilities.modifyObjects ( listOfObjectsToBeModified, newContin );
        			Utilities.addRelations ( listOfRelationsToBeModified, newContin );
                    isObj1EndPoint = true;
                }
                else
                {
                    Vector listOfRelationsInvolvingObj1 = Utilities.getListOfRelationsInvolving(obj1);
                    Utilities.deleteRelations(listOfRelationsInvolvingObj1);
                    obj1.changeContinNumberInDatabase(0);
                    obj1.continNumber = 0;
                    Utilities.addRelations(listOfRelationsInvolvingObj1, 0);
                    isObj1EndPoint = true;
                    Util.info("obj1 is an end point so its con no is now 0");

//                    Vector listOfRelationsInvolvingObj2 = Utilities.getListOfRelationsInvolving(obj2);
//                    Utilities.deleteRelations(listOfRelationsInvolvingObj2);
//                    obj2.changeContinNumberInDatabase(0);
//                    obj2.continNumber = 0;
//                    Utilities.addRelations(listOfRelationsInvolvingObj2, 0);
//                    isObj2EndPoint = true;
//                    Util.info("obj2 is an end point so its con no is now 0");
                }
                Vector restOfContinOfObj2 = Utilities.getRestOfContin(obj1, obj2, oldContin);
                Vector listOfObjectsToBeModified2 =
    					Utilities.getListOfContinuousObjects ( restOfContinOfObj2 );
                if(restOfContinOfObj2.size() <= 0 || listOfObjectsToBeModified2.size() <= 0)
                {
                    Vector listOfRelationsInvolvingObj2 = Utilities.getListOfRelationsInvolving(obj2);
                    Utilities.deleteRelations(listOfRelationsInvolvingObj2);
                    obj2.changeContinNumberInDatabase(0);
                    obj2.continNumber = 0;
                    Utilities.addRelations(listOfRelationsInvolvingObj2, 0);
                    isObj2EndPoint = true;
                    Util.info("obj2 is an end point so its con no is now 0");

//                    Vector listOfRelationsInvolvingObj1 = Utilities.getListOfRelationsInvolving(obj1);
//                    Utilities.deleteRelations(listOfRelationsInvolvingObj1);
//                    obj1.changeContinNumberInDatabase(0);
//                    obj1.continNumber = 0;
//                    Utilities.addRelations(listOfRelationsInvolvingObj1, 0);
//                    isObj1EndPoint = true;
//                    Util.info("obj1 is an end point so its con no is now 0");
                }
                if(isObj1EndPoint && isObj2EndPoint)
                {
                    Util.info("trying to delete con number" + oldContin);
                    pst = con.prepareStatement("delete from contin where CON_Number = ?");
                    pst.setInt(1, oldContin);
                    pst.executeUpdate();
                }
				
			}**/
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
	}

	/**
	 * Returns this object as a String. This function is an override from the Object
	 * class
	 *
	 * @return A a string reresentation of this object.
	 */
	public String toString (  )
	{
		return "" + obj1 + obj2  + "\n";
	}

    public String getRemarks()
    {
        Connection con  = null;
        String returner = "";
        try
        {
            con =
                EDatabase.borrowConnection ( 
                
               
                
                );
            PreparedStatement pst = con.prepareStatement("Select REL_Remarks from relationship where ObjName1 = ?  and ObjName2 = ? ");
            pst.setString(1, obj1.objectName);
          
            pst.setString(2, obj2.objectName);
          
            ResultSet rs = pst.executeQuery();
            if(rs.next())
            {
                returner = rs.getString("REL_Remarks");
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
    public void saveRemarks(String remarks)
    {
        Connection con  = null;
        String returner = "";
        try
        {
            con =
                EDatabase.borrowConnection ( 
                
               
                
                );
            PreparedStatement pst = con.prepareStatement("Update relationship set REL_Remarks = ? where ObjName1 = ? AND and ObjName2 = ?");
            pst.setString(1, remarks);
            pst.setString(2, obj1.objectName);
           
            pst.setString(3, obj2.objectName);
         
            
          
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
    }
}
