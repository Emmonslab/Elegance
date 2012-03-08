import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;


/**
 * The frame using which the data entry is done.
 *
 * @author zndavid
 * @version 1.0
 */
public class ImageModifyFrame
	extends JFrame
{
	private GridBagLayout gridBagLayout1          = new GridBagLayout(  );
	private JLabel        numberLabel             = new JLabel(  );
	private JLabel        directoryLabel          = new JLabel(  );
	private JLabel        wormLabel               = new JLabel(  );
	private JLabel        seriesLabel             = new JLabel(  );
	private JLabel        printLabel              = new JLabel(  );
	//private JLabel        negativeLabel           = new JLabel(  );
	private JLabel        sectionLabel            = new JLabel(  );
	private JLabel        enteredLabel            = new JLabel(  );
	private JLabel        dateLabel               = new JLabel(  );
	private JButton       okButton                = new JButton(  );
	private JTextField    imageNumberTextField    = new JTextField(  );
	private JTextField    filenameTextField       = new JTextField(  );
	private JTextField    directoryTextField      = new JTextField(  );
	private JTextField    wormTextField           = new JTextField(  );
	private JTextField    seriesTextField         = new JTextField(  );
	private JTextField    printNumberTextField    = new JTextField(  );
	//private JTextField    negativeNumberTextField = new JTextField(  );
	private JTextField    sectionNumberTextField  = new JTextField(  );
	private JTextField    enteredByTextField      = new JTextField(  );
	private JTextField    dateEnteredTextField    = new JTextField(  );
	private JButton       cancelButton            = new JButton(  );
	private JLabel        fileNameLabel           = new JLabel(  );
	private JButton       browseButton            = new JButton(  );

    private String imageNumber;
	/**
	 * Creates a new ImageModifyFrame object.
	 */
	public ImageModifyFrame ( String newImageNumber )
	{
        imageNumber = newImageNumber;
		try
		{
			jbInit (  );
		}
		catch ( Exception e )
		{
			e.printStackTrace (  );
		}
	}

	private void jbInit (  )
		throws Exception
	{
		this.setTitle ( "Enter The Image Data" );
		this.setSize ( new Dimension( 500, 363 ) );
		this.getContentPane (  ).setLayout ( gridBagLayout1 );
		numberLabel.setText("Image Number*");
		directoryLabel.setText ( "Directory" );
		wormLabel.setText ( "Specimen" );
		seriesLabel.setText ( "Series" );
		printLabel.setText ( "Print Number" );
		//negativeLabel.setText ( "Negative Number" );
		sectionLabel.setText ( "Section Number" );
		enteredLabel.setText ( "Entered By" );
		dateLabel.setText ( "Date Entered" );

		ResultSet rs = getLatestData (  );
		numberLabel.setBackground ( new Color( 206, 206, 230 ) );

		if ( rs.next (  ) )
		{
			imageNumberTextField.setText ( rs.getString ( "IMG_Number" ) );
			filenameTextField.setText ( rs.getString ( "IMG_File" ) );
			directoryTextField.setText ( rs.getString ( "IMG_Directory" ) );
			wormTextField.setText ( rs.getString ( "IMG_Worm" ) );
			seriesTextField.setText ( rs.getString ( "IMG_Series" ) );
			printNumberTextField.setText ( rs.getString ( "IMG_PrintNumber" ) );
			//negativeNumberTextField.setText ( rs.getString ( "IMG_NegativeNumber" ) );
			sectionNumberTextField.setText ( rs.getString ( "IMG_SectionNumber" ) );
			enteredByTextField.setText ( rs.getString ( "IMG_EnteredBy" ) );
			dateEnteredTextField.setText ( rs.getString ( "IMG_DateEntered" ) );
		}

		okButton.setText("Modify");
		okButton.addActionListener ( 
		    new ActionListener(  )
			{
				public void actionPerformed ( ActionEvent e )
				{
					okButton_actionPerformed ( e );
				}
			}
		 );
        imageNumberTextField.setEditable(false);
        imageNumberTextField.setEnabled(false);
		cancelButton.setText ( "Cancel" );
		cancelButton.addActionListener ( 
		    new ActionListener(  )
			{
				public void actionPerformed ( ActionEvent e )
				{
					cancelButton_actionPerformed ( e );
				}
			}
		 );
		fileNameLabel.setText ( "File Name" );
		browseButton.setText ( "Browse" );
		browseButton.addActionListener ( 
		    new ActionListener(  )
			{
				public void actionPerformed ( ActionEvent e )
				{
					browseButton_actionPerformed ( e );
				}
			}
		 );
		this.getContentPane (  ).add(numberLabel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 20, 0, 0), 0, 0));
		this.getContentPane (  ).add(directoryLabel, new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 20, 0, 0), 0, 0));
		this.getContentPane (  ).add(wormLabel, new GridBagConstraints(0, 3, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 20, 0, 0), 0, 0));
		this.getContentPane (  ).add(seriesLabel, new GridBagConstraints(0, 4, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 20, 0, 0), 0, 0));
		this.getContentPane (  ).add(printLabel, new GridBagConstraints(0, 5, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 20, 0, 0), 0, 0));
		//this.getContentPane (  ).add(negativeLabel, new GridBagConstraints(0, 6, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 20, 0, 0), 0, 0));
		this.getContentPane (  ).add(sectionLabel, new GridBagConstraints(0, 7, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 20, 0, 0), 0, 0));
		this.getContentPane (  ).add(enteredLabel, new GridBagConstraints(0, 8, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 20, 0, 0), 0, 0));
		this.getContentPane (  ).add(dateLabel, new GridBagConstraints(0, 9, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 20, 0, 0), 0, 0));
		this.getContentPane (  ).add(okButton, new GridBagConstraints(2, 10, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.getContentPane (  ).add(imageNumberTextField, new GridBagConstraints(1, 0, 3, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 77, 0));
		this.getContentPane (  ).add(filenameTextField, new GridBagConstraints(1, 1, 3, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.getContentPane (  ).add(directoryTextField, new GridBagConstraints(1, 2, 3, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.getContentPane (  ).add(wormTextField, new GridBagConstraints(1, 3, 3, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.getContentPane (  ).add(seriesTextField, new GridBagConstraints(1, 4, 3, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.getContentPane (  ).add(printNumberTextField, new GridBagConstraints(1, 5, 3, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		//this.getContentPane (  ).add(negativeNumberTextField, new GridBagConstraints(1, 6, 3, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.getContentPane (  ).add(sectionNumberTextField, new GridBagConstraints(1, 7, 3, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.getContentPane (  ).add(enteredByTextField, new GridBagConstraints(1, 8, 3, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.getContentPane (  ).add(dateEnteredTextField, new GridBagConstraints(1, 9, 3, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.getContentPane (  ).add(cancelButton, new GridBagConstraints(3, 10, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.getContentPane (  ).add(fileNameLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 20, 0, 0), 0, 0));
		this.getContentPane (  ).add(browseButton, new GridBagConstraints(0, 10, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	}

	private void cancelButton_actionPerformed ( ActionEvent e )
	{
		//this.hide (  );
		this.dispose (  );
	}

	private void browseButton_actionPerformed ( ActionEvent e )
	{
		String str = Utilities.getFileNameFromUserForRead ( "" );

		if ( "".compareTo ( str ) != 0 )
		{
			File file = new File( str );
			directoryTextField.setText ( file.getParent (  ) );
			filenameTextField.setText ( file.getName (  ) );
		}
	}

	/**
	 * Goes to the database and fetches the latest data from the Image table.
	 *
	 * @return A ResultSet object containing the latest information from the ImageTable.
	 *         In case of an error null is returned.
	 */
	public ResultSet getLatestData (  )
	{
		Connection con = null;
		ResultSet  rs = null;

		try
		{
			con = EDatabase.borrowConnection ( 
				    
				   
				    
				 );

			Statement stm = con.createStatement (  );
			rs = stm.executeQuery ( 
				    "SELECT ID, IMG_Number, IMG_File, IMG_Directory, "
				    + "IMG_Worm, IMG_Series, IMG_PrintNumber, IMG_NegativeNumber, "
				    + "IMG_SectionNumber, IMG_EnteredBy, IMG_DateEntered FROM image " 
                    + "WHERE IMG_Number = '" + imageNumber + "'"
				 );
		}
		catch ( Exception ex )
		{
			ex.printStackTrace (  );
		}finally {
			EDatabase.returnConnection(con);
		}

		

		return rs;
	}

	private void okButton_actionPerformed ( ActionEvent e )
	{
        Connection con = null;

		try
		{
			con = EDatabase.borrowConnection ();

			java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(  );

			format.applyPattern ( "yyyy-MM-dd" );

			Date date = new Date( System.currentTimeMillis (  ) );

			//java.util.Date              date    = format.parse( str1, new java.text.ParsePosition( 0 ) );
			//if ( date == null )
			//{
			//	JOptionPane.showMessageDialog( null, "Enter the date in the format dd/MM/yyyy", "Error", JOptionPane.ERROR_MESSAGE );
			//}
			try
			{
				date =
					new java.sql.Date( 
					    ( format.parse ( 
					        dateEnteredTextField.getText (  ),
					        new java.text.ParsePosition( 0 )
					     ) ).getTime (  )
					 );
			}
			catch ( NullPointerException npe )
			{
				JOptionPane.showMessageDialog ( 
				    null,
				    "Enter the date in the format yyyy-MM-dd",
				    "Error",
				    JOptionPane.ERROR_MESSAGE
				 );

				return;
			}

			int sectionNumber = 0;

			try
			{
				if ( sectionNumberTextField.getText (  ).compareTo ( "" ) != 0 )
				{
					sectionNumber =
						Integer.parseInt ( sectionNumberTextField.getText (  ) );
				}
			}
			catch ( NumberFormatException nfe )
			{
				JOptionPane.showMessageDialog ( 
				    null,
				    "The section number that you have entered is not a number. Please enter a valid number.",
				    "Error",
				    JOptionPane.ERROR_MESSAGE
				 );

				return;
			}
			if( imageNumberTextField.getText().equals(""))
            {
                JOptionPane.showMessageDialog ( 
				    null,
				    "You have not entered a value for the image number. Please check.",
				    "Error",
				    JOptionPane.ERROR_MESSAGE
				 );

				return;
            }
			if( filenameTextField.getText (  ).equals(""))
			{
                JOptionPane.showMessageDialog ( 
				    null,
				    "You have not entered a value for the file name. Please check.",
				    "Error",
				    JOptionPane.ERROR_MESSAGE
				 );

				return;
            }
			if( directoryTextField.getText (  ).equals(""))
			{
                JOptionPane.showMessageDialog ( 
				    null,
				    "You have not entered a value for the directory name. Please check.",
				    "Error",
				    JOptionPane.ERROR_MESSAGE
				 );

				return;
            }
			if( wormTextField.getText (  ).equals(""))
			{
                JOptionPane.showMessageDialog ( 
				    null,
				    "You have not entered a value for the worm. Please check.",
				    "Error",
				    JOptionPane.ERROR_MESSAGE
				 );

				return;
            }
			if( seriesTextField.getText (  ).equals(""))
			{
                JOptionPane.showMessageDialog ( 
				    null,
				    "You have not entered a value for the series number. Please check.",
				    "Error",
				    JOptionPane.ERROR_MESSAGE
				 );

				return;
            }
			if( printNumberTextField.getText (  ).equals(""))
			{
                JOptionPane.showMessageDialog ( 
				    null,
				    "You have not entered a value for the print number. Please check.",
				    "Error",
				    JOptionPane.ERROR_MESSAGE
				 );

				return;
            }
			/*if( negativeNumberTextField.getText (  ).equals(""))
            {
                JOptionPane.showMessageDialog ( 
				    null,
				    "You have not entered a value for the negative number. Please check.",
				    "Error",
				    JOptionPane.ERROR_MESSAGE
				 );

				return;
            }*/
			if( enteredByTextField.getText (  ).equals(""))
			{
                JOptionPane.showMessageDialog ( 
				    null,
				    "You have not entered a value for 'entered by'. Please check.",
				    "Error",
				    JOptionPane.ERROR_MESSAGE
				 );

				return;
            }
			
			PreparedStatement pst = con.prepareStatement ( 
				    "UPDATE image set IMG_File = ?, "
				    + "IMG_Directory = ?, IMG_Worm = ?, IMG_Series = ?, "
				    + "IMG_PrintNumber = ?, IMG_NegativeNumber = ?, "
				    + "IMG_SectionNumber = ?, IMG_EnteredBy = ?, "
				    + "IMG_DateEntered = ? WHERE IMG_Number = ?"
				 );
			pst.setString ( 1, filenameTextField.getText (  ) );
			pst.setString ( 2, directoryTextField.getText (  ) );
			pst.setString ( 3, wormTextField.getText (  ) );
			pst.setString ( 4, seriesTextField.getText (  ) );
			pst.setString ( 5, printNumberTextField.getText (  ) );
			pst.setString ( 6, "" );

			//			pst.setString ( 8, sectionNumberTextField.getText (  ) );
			pst.setInt ( 7, sectionNumber );
			pst.setString ( 8, enteredByTextField.getText (  ) );
			pst.setDate ( 9, date );
            pst.setString ( 10, imageNumberTextField.getText (  ) );
			if ( pst.executeUpdate (  ) > 0 )
			{
				JOptionPane.showMessageDialog ( null, "Data modified successfully" );
			}
			else
			{
				JOptionPane.showMessageDialog ( null, "Data modification failed" );
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
        
        } finally {
			EDatabase.returnConnection(con);
		}
		
    }
}
