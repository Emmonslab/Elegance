import java.sql.SQLException;

import java.util.Vector;
import java.util.zip.DataFormatException;


/**
 * An interface for connecting to the various tables in the database.
 *
 * @author zndavid
 * @version 1.0
 */
public interface DBConnector
{
	/**
	 * selects records from the database and returns them as a vector
	 *
	 * @return the records as a Vector
	 *
	 * @throws SQLException an exception thrown in case of an error
	 */
	public Vector selectRecords (  )
		throws SQLException;

	/**
	 * inserts/updates the current record in the database.
	 *
	 * @return number of rows changed
	 *
	 * @throws SQLException an exception thrown in case of an error in interacting with
	 *         the database.
	 * @throws DataFormatException an exception thrown in case of some of the data being
	 *         in the wrong format.
	 */
	public int insertRecord (  )
		throws SQLException, DataFormatException;

	//public boolean updateRecord()throws SQLException, DataFormatException;

	/**
	 * deletes records from the database.
	 *
	 * @return number of records deleted
	 *
	 * @throws SQLException an exception thrown in case of an error
	 */
	public int deleteRecords (  )
		throws SQLException;
}
