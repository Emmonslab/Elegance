/**
 * An object that represents one item in the wormbase report.
 *
 * @author zndavid
 * @version 1.0
 */
public class ReportItem
{
	private String cellA        = "";
	private String cellB        = "";
	private String experiment   = "";
	private int    send         = 0;
	private int    sendJoint    = 0;
	private int    receive      = 0;
	private int    receiveJoint = 0;
	private int    gap          = 0;
	private int    contact      = 0;

	/**
	 * Creates a new ReportItem object.
	 *
	 * @param newCellA name of the first cell
	 * @param newCellB name of the second cell
	 * @param newExperiment The experiment name.
	 */
	public ReportItem ( 
	    String newCellA,
	    String newCellB,
	    String newExperiment
	 )
	{
		cellA          = newCellA;
		cellB          = newCellB;
		experiment     = newExperiment;
	}

	/**
	 * returns cellA
	 *
	 * @return returns cellA
	 */
	public String getCellA (  )
	{
		return cellA;
	}

	/**
	 * sets cellA
	 *
	 * @param newCellA The new cellA value
	 */
	public void setCellA ( String newCellA )
	{
		cellA = newCellA;
	}

	/**
	 * return cellB
	 *
	 * @return return cellB
	 */
	public String getCellB (  )
	{
		return cellB;
	}

	/**
	 * sets cellB
	 *
	 * @param newCellB The new cellB value
	 */
	public void setCellB ( String newCellB )
	{
		cellB = newCellB;
	}

	/**
	 * Returns the experiment name
	 *
	 * @return The experiment name
	 */
	public String getExperiment (  )
	{
		return experiment;
	}

	/**
	 * Sets the experiment name
	 *
	 * @param newExperiment the new experiment name
	 */
	public void setExperiment ( String newExperiment )
	{
		experiment = newExperiment;
	}

	/**
	 * returns the send value
	 *
	 * @return the send value
	 */
	public int getSend (  )
	{
		return send;
	}

	/**
	 * sets the send value
	 *
	 * @param newSend The send value
	 */
	public void setSend ( int newSend )
	{
		send = newSend;
	}

	/**
	 * returns the sendJoint value
	 *
	 * @return the sendJoint value
	 */
	public int getSendJoint (  )
	{
		return sendJoint;
	}

	/**
	 * sets the sendJoint value
	 *
	 * @param newSendJoint The newSendJoint value
	 */
	public void setSendJoint ( int newSendJoint )
	{
		sendJoint = newSendJoint;
	}

	/**
	 * returns the receive value
	 *
	 * @return the receive value
	 */
	public int getReceive (  )
	{
		return receive;
	}

	/**
	 * sets the receeve value
	 *
	 * @param newReceive The new receive value
	 */
	public void setReceive ( int newReceive )
	{
		receive = newReceive;
	}

	/**
	 * returns the receive joint value
	 *
	 * @return the receive joint value
	 */
	public int getReceiveJoint (  )
	{
		return receiveJoint;
	}

	/**
	 * sets the receiveJoint value
	 *
	 * @param newReceiveJoint the new receiveJoint value
	 */
	public void setReceiveJoint ( int newReceiveJoint )
	{
		receiveJoint = newReceiveJoint;
	}

	/**
	 * returns the gap value
	 *
	 * @return the gap value
	 */
	public int getGap (  )
	{
		return gap;
	}

	/**
	 * sets the gap value
	 *
	 * @param newGap the new gap value
	 */
	public void setGap ( int newGap )
	{
		gap = newGap;
	}

	/**
	 * returns the contact value
	 *
	 * @return the contact value
	 */
	public int getContact (  )
	{
		return contact;
	}

	/**
	 * sets the contact value
	 *
	 * @param newContact the new contact value
	 */
	public void setContact ( int newContact )
	{
		contact = newContact;
	}

	/**
	 * increases the send value by one
	 */
	public void addSend (  )
	{
		send++;
	}

	/**
	 * increases the sendJoint value by one
	 */
	public void addSendJoint (  )
	{
		sendJoint++;
	}

	/**
	 * increases the receive value by one
	 */
	public void addReceive (  )
	{
		receive++;
	}

	/**
	 * increases the receiveJoint value by one
	 */
	public void addReceiveJoint (  )
	{
		receiveJoint++;
	}

	/**
	 * increases the gap value by one
	 */
	public void addGap (  )
	{
		gap++;
	}

	/**
	 * increases the contact value by one
	 */
	public void addContact (  )
	{
		contact++;
	}

	/**
	 * overrides the equals method of the Object class
	 *
	 * @param obj an object
	 *
	 * @return returns true only if the object is a ReportItem object and the cellA and
	 *         cellB value is same in both.
	 */
	public boolean equals ( Object obj )
	{
		try
		{
			ReportItem ri = ( ReportItem ) obj;

			if ( 
			    this.cellA.equals ( ri.getCellA (  ) )
				    && this.cellB.equals ( ri.getCellB (  ) )
			 )
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

	/**
	 * returns a string representation of this object
	 *
	 * @return a string representation of this object
	 */
	public String toString (  )
	{
		return cellA + "\t" + cellB + "\t" + experiment + "\t" + send + "\t" + sendJoint
		+ "\t" + receive + "\t" + receiveJoint + "\t" + gap + "\t" + contact + "\n";
	}

	/**
	 * returns the total number of connections
	 *
	 * @return the total number of connections
	 */
	public int getTotalConnections (  )
	{
		return send + sendJoint + receive + receiveJoint + gap + contact;
	}
}
