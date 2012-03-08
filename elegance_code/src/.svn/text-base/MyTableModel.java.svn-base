import java.util.Vector;

import javax.swing.table.AbstractTableModel;


/**
 * My own implementation on the AbstractTableModel
 *
 * @author zndavid
 * @version 1.0
 */
class MyTableModel
	extends AbstractTableModel
{
	final Vector columnNames;
	final Vector data;

	/**
	 * Constructor for the MyTableModel object
	 *
	 * @param data1 data in a Object[][] format
	 * @param columnNames1 colums names in a Vector
	 */
	public MyTableModel ( 
	    Vector data1,
	    Vector columnNames1
	 )
	{
		data            = data1;
		columnNames     = columnNames1;
	}

	/**
	 * Gets the columnCount attribute of the MyTableModel object
	 *
	 * @return The columnCount value
	 */
	public int getColumnCount (  )
	{
		return columnNames.size (  );
	}

	/**
	 * Gets the rowCount attribute of the MyTableModel object
	 *
	 * @return The rowCount value
	 */
	public int getRowCount (  )
	{
		return data.size (  );
	}

	/**
	 * Gets the columnName attribute of the MyTableModel object
	 *
	 * @param col column number
	 *
	 * @return The columnName
	 */
	public String getColumnName ( int col )
	{
		return ( String ) columnNames.elementAt ( col );
	}

	/**
	 * Gets the value at a particular place in the table
	 *
	 * @param row row number
	 * @param col column number
	 *
	 * @return the value
	 */
	public Object getValueAt ( 
	    int row,
	    int col
	 )
	{
		return ( ( Vector ) data.elementAt ( row ) ).elementAt ( col );
	}

	/**
	 * Gets the class that a particular column contains
	 *
	 * @param c column number
	 *
	 * @return The column Class.
	 */
	public Class getColumnClass ( int c )
	{
		return getValueAt ( 0, c ).getClass (  );
	}

	/*
	 *  Don't need to implement this method unless your table's
	 *  editable.
	 */

	/**
	 * Gets the cellEditable attribute of the MyTableModel object
	 *
	 * @param row row number
	 * @param col column number
	 *
	 * @return whether the cell is editable or not. Returns false always specifying that
	 *         the table is not editable.
	 */
	public boolean isCellEditable ( 
	    int row,
	    int col
	 )
	{
		//return true;
		return false;
	}
}
