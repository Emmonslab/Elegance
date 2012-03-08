import java.util.Vector;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;


/**
 * An implementation of the TableModel interface to provide the functionality required
 * for the table in ObjectFrame
 *
 * @author zndavid
 * @version 1.0
 */
public class SynapseRecordModel
	implements TableModel
{
	Vector data;
	Vector columnNames;

	/**
	 * Creates a new ObjectTableModel object.
	 *
	 * @param newData The data as a Vector of Vectors
	 * @param newColumnNames The column names in a Vector
	 */
	public SynapseRecordModel ( 
	    Vector newData,
	    Vector newColumnNames
	 )
	{
		data            = newData;
		columnNames     = newColumnNames;
	}

	/**
	 * Gets the number of rows.
	 *
	 * @return The number of rows
	 */
	public int getRowCount (  )
	{
		return data.size (  );
	}

	/**
	 * Gets the number of columns.
	 *
	 * @return The number of columns
	 */
	public int getColumnCount (  )
	{
		return columnNames.size (  );
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
		return ( String ) columnNames.elementAt ( columnIndex );
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
		if ( data.size (  ) <= 0 )
		{
			return null;
		}
		else
		{
			return ( ( Vector ) data.elementAt ( 0 ) ).elementAt ( columnIndex ).getClass (  );
		}
	}

	/**
	 * returns whether a cell is editable or not. This function always returns false by
	 * which the whole table becomes uneditable.
	 *
	 * @param rowIndex index of the row.
	 * @param columnIndex index of the column.
	 *
	 * @return Always false
	 */
	public boolean isCellEditable ( 
	    int rowIndex,
	    int columnIndex
	 )
	{
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
		return ( ( Vector ) data.elementAt ( rowIndex ) ).elementAt ( columnIndex );
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
	 ) {}

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
