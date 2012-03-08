/**
 * The object to represent the relations drawn on the 2-Dimensional graphical display.
 *
 * @author zndavid
 * @version 1.0
 */
public class TwoDDisplayContin
	implements Comparable
{
	private String type;
	private String continName    = "";
	private int    continNumber;
	private int    sectionNumber;

	/**
	 * Creates a new DisplayContin object.
	 *
	 * @param newSectionNumber The new section number
	 * @param newContinNumber The new contin number
	 * @param newType The type of relation
	 */
	public TwoDDisplayContin ( 
	    int    newSectionNumber,
	    int    newContinNumber,
	    String newType
	 )
	{
		sectionNumber     = newSectionNumber;
		continNumber      = newContinNumber;
		type              = newType;
		continName        = Utilities.getContinName ( new Integer( continNumber ) );
	}

	/**
	 * An override of the equals function in Object class.
	 *
	 * @param obj any object
	 *
	 * @return returns true onlu if the object is an instance of the TwoDDisplayContin
	 *         and the setion number and the contin number is same
	 */
	public boolean equals ( Object obj )
	{
		if ( obj instanceof TwoDDisplayContin )
		{
			TwoDDisplayContin tdis = ( TwoDDisplayContin ) obj;

			if ( 
			    ( this.sectionNumber == tdis.getSectionNumber (  ) )
				    && ( this.continNumber == tdis.getContinNumber (  ) )
			 )
			{
				return true;
			}
		}

		return false;
	}

	/**
	 * This function is from the Comparable interface and is used for comparing
	 * especially during sorting.
	 *
	 * @param obj any object
	 *
	 * @return if the object is an instance of TwoDDisplayContin then the two are
	 *         compared first by section number and then by contin number and 1, 0 or 01
	 *         is returned accordingly. If the object is not an instance of
	 *         TwoDDisplayContin class, then the hashcode of the two obejcts is
	 *         compared.
	 */
	public int compareTo ( Object obj )
	{
		if ( obj instanceof TwoDDisplayContin )
		{
			TwoDDisplayContin tdis = ( TwoDDisplayContin ) obj;

			if ( this.sectionNumber > tdis.getSectionNumber (  ) )
			{
				return 1;
			}
			else if ( this.sectionNumber < tdis.getSectionNumber (  ) )
			{
				return -1;
			}
			else
			{
				if ( this.continNumber > tdis.getContinNumber (  ) )
				{
					return 1;
				}
				else if ( this.continNumber < tdis.getContinNumber (  ) )
				{
					return -1;
				}
				else
				{
					return 0;
				}
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

	/**
	 * returns the contin name
	 *
	 * @return the contin name
	 */
	public String getContinName (  )
	{
		return continName;
	}

	/**
	 * returns the contin name in the format that it shoule occur in the drawing i.e.
	 * alternate name if it is available and contin number otherwise
	 *
	 * @return DOCUMENT ME!
	 */
	public String getDisplayableContinName (  )
	{
		return continName.equals ( "" ) ? ( "" + continNumber ) : continName;
	}

	/**
	 * sets the contin name
	 *
	 * @param newContinName new contin name
	 */
	public void setContinName ( String newContinName )
	{
		continName = newContinName;
	}

	/**
	 * returns the contin number
	 *
	 * @return the contin number
	 */
	public int getContinNumber (  )
	{
		return continNumber;
	}

	/**
	 * sets the contin number
	 *
	 * @param newContinNumber the new contin number
	 */
	public void setContinNumber ( int newContinNumber )
	{
		continNumber = newContinNumber;
	}

	/**
	 * returns the type of relation
	 *
	 * @return the type of relation
	 */
	public String getType (  )
	{
		return type;
	}

	/**
	 * sets the type of relation
	 *
	 * @param newType the type of relation.
	 */
	public void setType ( String newType )
	{
		type = newType;
	}

	/**
	 * returns the section number
	 *
	 * @return the section number
	 */
	public int getSectionNumber (  )
	{
		return sectionNumber;
	}

	/**
	 * Sets the section number
	 *
	 * @param newSectionNumber the section number
	 */
	public void setSectionNumber ( int newSectionNumber )
	{
		sectionNumber = newSectionNumber;
	}
}
