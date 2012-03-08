import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;


/**
 * The frame in which the relation in the current images can be viewed.
 *
 * @author zndavid
 * @version 1.0
 */
public class RelationFrame
	extends JFrame
{
	private GridBagLayout gridBagLayout1 = new GridBagLayout(  );
	private JScrollPane   jScrollPane1 = new JScrollPane(  );
	private JTable        jTable1      = new JTable(  );
    private JButton refreshButton = new JButton();

	/**
	 * Creates a new RelationFrame object.
	 */
	public RelationFrame (  )
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
		this.getContentPane (  ).setLayout ( gridBagLayout1 );
		this.setSize ( new Dimension( 800, 200 ) );
		this.setTitle ( "Relations" );

		// jTable1.addMouseListener(new java.awt.event.MouseAdapter()
		// {
		// public void mouseClicked(MouseEvent e)
		//{
		//jTable1_mouseClicked(e);
		//   }
		//});
		jTable1.addKeyListener ( 
		    new java.awt.event.KeyAdapter(  )
			{
				public void keyPressed ( KeyEvent e )
				{
					jTable1_keyPressed ( e );
				}
			}
		 );
        refreshButton.setText("Refresh");
        refreshButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    refreshButton_actionPerformed(e);
                }
            });
		jScrollPane1.getViewport (  ).add ( jTable1, null );
		this.getContentPane (  ).add ( 
		    jScrollPane1,
		    new GridBagConstraints( 
		        0,
		        0,
		        1,
		        1,
		        1.0,
		        1.0,
		        GridBagConstraints.CENTER,
		        GridBagConstraints.BOTH,
		        new Insets( 0, 0, 0, 0 ),
		        0,
		        0
		     )
		 );
        this.getContentPane().add(refreshButton, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	}

	/**
	 * Updates the data and displays the new data.
	 */
	public void updateAndDisplayFrame (  )
	{
		jTable1.setModel ( new RelationTableModel(  ) );
		this.show (  );
	}

	private void jTable1_keyPressed ( KeyEvent e )
	{
		if ( e.getKeyCode (  ) == KeyEvent.VK_DELETE )
		{
			int row = jTable1.getSelectedRow (  );

			if ( row != -1 )
			{
				ImageDisplayPanel.removeRelation ( row );
				( ( RelationTableModel ) jTable1.getModel (  ) ).refresh (  );
				this.repaint (  );
			}
		}
	}

    private void refreshButton_actionPerformed(ActionEvent e)
    {
        ImageDisplayPanel.saveRelations();
        updateAndDisplayFrame();
    }

	//  private void jTable1_mouseClicked(MouseEvent e)
	//{ 
	//Point p = e.getPoint();
	//int row = jTable1.rowAtPoint(p);
	//if(row != -1)
	//ImageDisplayPanel.setSelectedRelationRow(row);
	// }
}
