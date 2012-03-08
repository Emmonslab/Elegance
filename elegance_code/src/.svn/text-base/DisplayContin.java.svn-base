/**
 * This is the class that represents one contin that is displayed on the 1DDisplay panel.
 *
 * @author zndavid
 * @version 1.0
 */
public class DisplayContin
{
	private String type;
	private String continName   = "";
	private int    continNumber;
	private int    strength     = 0;

	/**
	 * Creates a new DisplayContin object.
	 *
	 * @param newContinNumber The new contin number
	 * @param newType the new relation type
	 */
	public DisplayContin ( 
	    int    newContinNumber,
	    String newType
	 )
	{
		continNumber     = newContinNumber;
		type             = newType;
		continName       = Utilities.getContinName ( new Integer( continNumber ) );
		strength         = 1;
	}

	/**
	 * This function overrides the equals method in the 'Object' class. retuns true or
	 * false depending on whether the objects passed as argument is the same as the
	 * current object ot not.
	 *
	 * @param obj any object
	 *
	 * @return returns true if the Object is a DisplayContin object and the contin number
	 *         is the same as this object and the relation type is the same as this
	 *         object.Returns false otherwise.
	 */
	public boolean equals ( Object obj )
	{
		try
		{
			DisplayContin dc = ( DisplayContin ) obj;

			if ( 
			    ( dc.getContinNumber (  ) == this.continNumber )
				    && dc.getType (  ).equals ( this.getType (  ) )
			 )
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		catch ( Exception ex )
		{
			return false;
		}
	}

	/**
	 * returns the contin name
	 *
	 * @return returns the contin name
	 */
	public String getContinName (  )
	{
		return continName;
	}

	/**
	 * sets the contin name to the new string
	 *
	 * @param newContinName The new contin name
	 */
	public void setContinName ( String newContinName )
	{
		continName = newContinName;
	}

	/**
	 * Returns the contin number
	 *
	 * @return The contin number
	 */
	public int getContinNumber (  )
	{
		return continNumber;
	}

	/**
	 * Sets the contin number to the new number provided.
	 *
	 * @param newContinNumber The new contin number
	 */
	public void setContinNumber ( int newContinNumber )
	{
		continNumber = newContinNumber;
	}

	/**
	 * Returns the relation type
	 *
	 * @return The relation type
	 */
	public String getType (  )
	{
		return type;
	}

	/**
	 * Sets the relation type
	 *
	 * @param newType The new type
	 */
	public void setType ( String newType )
	{
		type = newType;
	}

	/**
	 * returns the strength attribute
	 *
	 * @return The strength attribute
	 */
	public int getStrength (  )
	{
		return strength;
	}

	/**
	 * Sets the strength attribute
	 *
	 * @param newStrength the new strength attribute
	 */
	public void setStrength ( int newStrength )
	{
		strength = newStrength;
	}

	/**
	 * Increases strength by 1.
	 */
	public void increaseStrength (  )
	{
		strength++;
	}
}
