import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;


/**
 * This is the about box for the main frame. It displays information about this software.
 *
 * @author zndavid
 * @version 1.0
 */
public class MainFrame_AboutBoxPanel1
	extends JPanel
{
	private Border        border         = BorderFactory.createEtchedBorder (  );
	private GridBagLayout layoutMain     = new GridBagLayout(  );
	private JLabel        labelCompany   = new JLabel(  );
	private JLabel        labelCopyright = new JLabel(  );
	private JLabel        labelAuthor    = new JLabel(  );
	private JLabel        labelTitle     = new JLabel(  );

	/**
	 * Creates a new MainFrame_AboutBoxPanel1 object.
	 */
	public MainFrame_AboutBoxPanel1 (  )
	{
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
		this.setLayout ( layoutMain );
		this.setBorder ( border );
		labelTitle.setText ( "The C elegans Nervous System Reconstruction Project" );
		labelAuthor.setText ( "Licensed by Albert Einstein University" );
		labelCopyright.setText ( "(c) 2003 All Rights Reserved" );
		labelCompany.setText ( 
		    "Author: Meng Xu & Metahelix Life Sciences Pvt Ltd"
		 );
		this.add ( 
		    labelTitle,
		    new GridBagConstraints( 
		        0,
		        0,
		        1,
		        1,
		        0.0,
		        0.0,
		        GridBagConstraints.WEST,
		        GridBagConstraints.NONE,
		        new Insets( 5, 15, 0, 15 ),
		        0,
		        0
		     )
		 );
		this.add ( 
		    labelAuthor,
		    new GridBagConstraints( 
		        0,
		        1,
		        1,
		        1,
		        0.0,
		        0.0,
		        GridBagConstraints.WEST,
		        GridBagConstraints.NONE,
		        new Insets( 0, 15, 0, 15 ),
		        0,
		        0
		     )
		 );
		this.add ( 
		    labelCopyright,
		    new GridBagConstraints( 
		        0,
		        2,
		        1,
		        1,
		        0.0,
		        0.0,
		        GridBagConstraints.WEST,
		        GridBagConstraints.NONE,
		        new Insets( 0, 15, 0, 15 ),
		        0,
		        0
		     )
		 );
		this.add ( 
		    labelCompany,
		    new GridBagConstraints( 
		        0,
		        3,
		        1,
		        1,
		        0.0,
		        0.0,
		        GridBagConstraints.WEST,
		        GridBagConstraints.NONE,
		        new Insets( 0, 15, 5, 15 ),
		        0,
		        0
		     )
		 );
	}
}
