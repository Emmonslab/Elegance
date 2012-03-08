import java.sql.Connection;
import java.util.Vector;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;


/**
 * An implementation of the TableModel interface to provide the functionality required
 * for the table in RelationFrame
 *
 * @author zndavid
 * @version 1.0
 */
public class RelationTableModel
	implements TableModel
{
	private Vector data;

	/**
	 * Creates a new RelationTableModel object.
	 */
	public RelationTableModel (  )
	{
		refresh (  );
	}

	/**
	 * refreshes the data.
	 */
	public void refresh (  )
	{
		Connection con = null;
		try {
			con = EDatabase.borrowConnection();
	
		
		data = new Vector(  );

		for ( int i = 0; i < ImageDisplayPanel.getNumberOfRelations (  ); i++ )
		{
			Relation rel = ImageDisplayPanel.getRelationForIndex ( i );

			if ( rel == null )
			{
				continue;
			}

			Vector row = new Vector(  );
			row.addElement ( rel.getObj1 (  ).getObjName ( con ) );
			row.addElement ( rel.getObj1 (  ).imageNumber );
			row.addElement ( "" + rel.getObj1 (  ).getContinNo (  ) );
			row.addElement ( rel.getObj2 (  ).getObjName ( con ) );
			row.addElement ( rel.getObj2 (  ).imageNumber );
			row.addElement ( "" + rel.getObj2 (  ).getContinNo (  ) );
		
			
            row.addElement(rel.getRemarks());
			data.add ( row );
		}
		
	} catch (Throwable e) {
		ELog.info("cant get object data " + e);
		throw new IllegalStateException("cant get object data", e);
	} finally {
		EDatabase.returnConnection(con);
	}
	}

	/**
	 * Gets the number of rows.
	 *
	 * @return The number of rows
	 */
	public int getRowCount (  )
	{
		//return ImageDisplayPanel.getNumberOfRelations (  );
        return data.size();
	}

	/**
	 * Gets the number of columns.
	 *
	 * @return The number of columns
	 */
	public int getColumnCount (  )
	{
		return 9;
	}

	/**
	 * Gets the column name corresponding the index provided.
	 *
	 * @param columnIndex The zero based index of the column
	 *
	 * @return The column name
	 */
	public String getColumnName ( int columnIndex )
	{
		if ( columnIndex == 0 )
		{
			return "Object 1";
		}
		else if ( columnIndex == 1 )
		{
			return "Image 1";
		}
		else if ( columnIndex == 2 )
		{
			return "Contin 1";
		}
		else if ( columnIndex == 3 )
		{
			return "Object 2";
		}
		else if ( columnIndex == 4 )
		{
			return "Image 2";
		}
		else if ( columnIndex == 5 )
		{
			return "Contin 2";
		}
		else if ( columnIndex == 6 )
		{
			return "Relation";
		}
		else if ( columnIndex == 7 )
		{
			return "Relation of 1 to 2";
		}
        else if ( columnIndex == 8 )
		{
			return "Remarks";
		}
		else
		{
			return null;
		}
	}

	/**
	 * Gets the column class corresponding the index provided.
	 *
	 * @param columnIndex The zero based index of the column
	 *
	 * @return The column class
	 */
	public Class getColumnClass ( int columnIndex )
	{
		try
		{
			return Class.forName ( "java.lang.String" );
		}
		catch ( Exception ex )
		{
			return ( new Object(  ) ).getClass (  );
		}
	}

	/**
	 * returns whether a cell is editable or not. Except for remarks which are editable, 
     * this function always returns false by
	 * which the whole table becomes uneditable. 
	 *
	 * @param rowIndex index of the row.
	 * @param columnIndex index of the column.
	 *
	 * @return Always false except for remarks
	 */
	public boolean isCellEditable ( 
	    int rowIndex,
	    int columnIndex
	 )
	{
        if(columnIndex == 8) return true;
		return false;
	}

	/**
	 * Gets the value of a specific location in the table.
	 *
	 * @param rowIndex row index
	 * @param columnIndex column index
	 *
	 * @return The value.
	 */
	public Object getValueAt ( 
	    int rowIndex,
	    int columnIndex
	 )
	{
		if ( rowIndex >= getRowCount (  ) )
		{
			return null;
		}

		Vector v = ( Vector ) data.elementAt ( rowIndex );

		return v.elementAt ( columnIndex );

		/*Relation rel = ImageDisplayPanel.getRelationForIndex(rowIndex);
		   if(columnIndex ==0)
		   {
		     return rel.getObj1().getObjName();
		   }
		   else if(columnIndex == 1)
		   {
		     return rel.getObj1().imageNumber;
		   }
		   else if(columnIndex == 2)
		   {
		     return "" + rel.getObj1().getContinNo();
		   }
		   else if(columnIndex == 3)
		   {
		    return rel.getObj2().getObjName();
		   }
		   else if(columnIndex == 4)
		   {
		     return rel.getObj2().imageNumber;
		   }
		   else if(columnIndex == 5)
		   {
		     return "" + rel.getObj2().getContinNo();
		   }
		   else if(columnIndex == 6)
		   {
		     return Utilities.getSymbolFor(rel.getType());
		   }
		   else if(columnIndex == 7)
		   {
		     return rel.type;
		   }
		   else return null;*/
	}

	/**
	 * Sets the value of a specific location in the table.
	 *
	 * @param aValue The value
	 * @param rowIndex the row index
	 * @param columnIndex the column index
	 */
	public void setValueAt ( 
	    Object aValue,
	    int    rowIndex,
	    int    columnIndex
	 ) 
     {
        if(columnIndex == 8)
        {
            Vector row = (Vector) data.elementAt(rowIndex);
            CellObject obj1 = new CellObject((String)row.elementAt(0));
            CellObject obj2 = new CellObject((String)row.elementAt(3));
            Relation rel = new Relation(obj1, obj2);
            rel.saveRemarks((String) aValue);
            row.setElementAt(aValue, columnIndex);
            data.setElementAt(row, rowIndex);
        }
     }

	/**
	 * Adds a table model listener. Currently, this function does nothing
	 *
	 * @param l the table model listener
	 */
	public void addTableModelListener ( TableModelListener l ) {}

	/**
	 * removes a table model listener. Currently, this function does nothing.
	 *
	 * @param l The table model listener
	 */
	public void removeTableModelListener ( TableModelListener l ) {}
}
