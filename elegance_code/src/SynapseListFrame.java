import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;



public class SynapseListFrame
	extends JFrame
{
	
	private GridBagLayout gridBagLayout1 = new GridBagLayout(  );
	private JLabel        nameLabel = new JLabel(  );
	private JTextField    nameBox       = new JTextField(  );
	private JButton       ok          = new JButton(  );
	private JButton       close       = new JButton(  );
	private JButton       save       = new JButton(  );
	private JScrollPane   spane;
	private JTable        table       = new JTable(  );
	private Vector  tableData   = new Vector(  );
	private Vector tableHeaders = new Vector(  );
	private String name = "";

	/**
	 * Creates a new ImageSearchFrame object.
	 */
	public SynapseListFrame (  )
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
		this.setDefaultCloseOperation ( JFrame.HIDE_ON_CLOSE );
		this.setSize ( new Dimension( 300, 100 ) );
		this.setTitle ( "Synapse List" );
		nameLabel.setText("Input the neuron name:");
		ok.setText ( "go" );
		ok.addActionListener ( 
		    new ActionListener(  )
			{
				public void actionPerformed ( ActionEvent e )
				{
					ok_actionPerformed ( e );
				}
			}
		 );
		
		save.setText ( "save as Excel" );
		save.addActionListener ( 
		    new ActionListener(  )
			{
				public void actionPerformed ( ActionEvent e )
				{
					save_actionPerformed ( e );
				}
			}
		 );
		
		close.setText ( "Close" );
		close.addActionListener ( 
		    new ActionListener(  )
			{
				public void actionPerformed ( ActionEvent e )
				{
					close_actionPerformed ( e );
				}
			}
		 );
		
		this.getContentPane (  ).add ( 
		    nameLabel,
		    new GridBagConstraints( 
		        0,
		        0,
		        1,
		        1,
		        0.0,
		        0.0,
		        GridBagConstraints.CENTER,
		        GridBagConstraints.NONE,
		        new Insets( 0, 0, 0, 0 ),
		        0,
		        0
		     )
		 );
		
		this.getContentPane (  ).add ( 
		    nameBox,
		    new GridBagConstraints( 
		        1,
		        0,
		        1,
		        1,
		        0.0,
		        0.0,
		        GridBagConstraints.CENTER,
		        GridBagConstraints.HORIZONTAL,
		        new Insets( 0, 0, 0, 0 ),
		        0,
		        0
		     )
		 );
	
		this.getContentPane (  ).add ( 
		    ok,
		    new GridBagConstraints( 
		        0,
		        3,
		        1,
		        1,
		        0.0,
		        0.0,
		        GridBagConstraints.CENTER,
		        GridBagConstraints.NONE,
		        new Insets( 0, 0, 0, 0 ),
		        0,
		        0
		     )
		 );
		this.getContentPane (  ).add ( 
		    close,
		    new GridBagConstraints( 
		        1,
		        3,
		        1,
		        1,
		        0.0,
		        0.0,
		        GridBagConstraints.CENTER,
		        GridBagConstraints.NONE,
		        new Insets( 0, 0, 0, 0 ),
		        15,
		        0
		     )
		 );
	}

	
	public int performSearch (  )
	{
		ImageDB imgdb       = new ImageDB(  );
		String  jsql   = "",jsql1="";
		boolean doubleQuery = false;
		
		String img="",pre="",post="",type="",sections="";
		
		String[] mem;

		if ( ( nameBox.getText (  ) == null ) || ( nameBox.getText (  ).length (  ) == 0 ) )
		{
			JOptionPane.showMessageDialog ( 
			    null,
			    "Please enter the name or contin#",
			    "Error",
			    JOptionPane.ERROR_MESSAGE
			 );

			return -1;
		}
		
		Connection con = null;
		PreparedStatement pst=null,pst1=null;
		ResultSet         rs=null,rs1=null;
		name = Utilities.getNameFromWhatever(nameBox.getText (  ));

		try
		{
			
			con = EDatabase.borrowConnection ( 
				    
				   
				    
				 );
			
			jsql= "select pre,post,sections,members,type from synapsecombined where " +
	  		" ( pre = ? or post= ? )  and type='electrical' order by sections desc";

			pst = con.prepareStatement ( jsql );
			pst.setString(1, name);
			pst.setString(2, name);
			rs = pst.executeQuery (  );

			while ( rs.next (  ) )
			{
				post = rs.getString(1);
			    if(post.equals(name)) post=rs.getString(2);
			    pre = name;
			    sections = rs.getString(3);

				mem = rs.getString(4).split(",");
				type = rs.getString(5);
				
				jsql1 = "select IMG_Number from object where OBJ_Name=?";
				pst1 = con.prepareStatement(jsql1);
				pst1.setString(1, mem[0]);
				rs1 = pst1.executeQuery();
				if (rs1.next()) 
				   {
					  img = rs1.getString(1);
				   }
				rs1.close();
				pst1.close();
				
				
				Vector v = new Vector(  );
				
				
				v.addElement ( type );
				v.addElement ( pre );
				v.addElement ( post );
				v.addElement ( sections );
                v.addElement ( mem[0] );
                v.addElement ( img );
                
				tableData.addElement ( v );
			}
			rs.close();
			pst.close();
			
			jsql= "select pre,post,sections,members,type from synapsecombined where " +
	  		" pre = ? and type='chemical' order by sections desc";

			pst = con.prepareStatement ( jsql );
			pst.setString(1, name);
			rs = pst.executeQuery (  );

			while ( rs.next (  ) )
			{
				pre = rs.getString(1);
			    post=rs.getString(2);
			    
			    sections = rs.getString(3);

				mem = rs.getString(4).split(",");
				type = rs.getString(5);
				
				jsql1 = "select IMG_Number from object where OBJ_Name=?";
				pst1 = con.prepareStatement(jsql1);
				pst1.setString(1, mem[0]);
				rs1 = pst1.executeQuery();
				if (rs1.next()) 
				   {
					  img = rs1.getString(1);
				   }
				rs1.close();
				pst1.close();
				
				
				Vector v = new Vector(  );
				
				
				v.addElement ( type );
				v.addElement ( pre );
				v.addElement ( post );
				v.addElement ( sections );
                v.addElement ( mem[0] );
                v.addElement ( img );
                
				tableData.addElement ( v );
			}
			rs.close();
			pst.close();
			
			
			jsql= "select pre,post,sections,members,type from synapsecombined where "+
			  "( post1 = ? or post2 = ? or post3 = ? or post4 = ? )   and type='chemical' order by sections desc";
			  pst = con.prepareStatement(jsql);
			  pst.setString(1, name);
			  pst.setString(2, name);
			  pst.setString(3, name);
			  pst.setString(4, name);
			  rs = pst.executeQuery (  );

			while ( rs.next (  ) )
			{
				pre = rs.getString(1);
			    post=rs.getString(2);
			    sections = rs.getString(3);
				mem = rs.getString(4).split(",");
				type = rs.getString(5);
				
				jsql1 = "select IMG_Number from object where OBJ_Name=?";
				pst1 = con.prepareStatement(jsql1);
				pst1.setString(1, mem[0]);
				rs1 = pst1.executeQuery();
				if (rs1.next()) 
				   {
					  img = rs1.getString(1);
				   }
				rs1.close();
				pst1.close();
				
				Vector v = new Vector(  );
				
				
				v.addElement ( type );
				v.addElement ( pre );
				v.addElement ( post );
				v.addElement ( sections );
                v.addElement ( mem[0] );
                v.addElement ( img );
                
				tableData.addElement ( v );
				
			}
			rs.close();
			pst.close();
			

		
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

			

			return -1;
		}finally {
			EDatabase.returnConnection(con);
		}

		
		

		tableHeaders.addElement ( "type" );
		tableHeaders.addElement ( "presynaptical" );
		tableHeaders.addElement ( "postsynaptical" );
		tableHeaders.addElement ( "sections" );
        tableHeaders.addElement ( "syn id" );
        tableHeaders.addElement ( "image#" );
       

        MyTableModel model = new MyTableModel( tableData, tableHeaders );

		table.setModel ( model );
		spane =
			new JScrollPane( 
			    table,
			    JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			    JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
			 );

		this.getContentPane (  ).removeAll (  );
		this.setSize ( new Dimension( 800, 600 ) );
		this.getContentPane (  ).setLayout ( new GridBagLayout(  ) );
		this.getContentPane (  ).add ( 
		    spane,
		    new GridBagConstraints( 
		        0,
		        0,
		        3,
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
		
		this.getContentPane (  ).add ( 
		    close,
		    new GridBagConstraints( 
		        2,
		        1,
		        1,
		        1,
		        0.1,
		        0.0,
		        GridBagConstraints.CENTER,
		        GridBagConstraints.NONE,
		        new Insets( 0, 0, 0, 0 ),
		        15,
		        0
		     )
		 );
		
		
		this.getContentPane (  ).add ( 
			    save,
			    new GridBagConstraints( 
			        1,
			        1,
			        1,
			        1,
			        0.1,
			        0.0,
			        GridBagConstraints.CENTER,
			        GridBagConstraints.NONE,
			        new Insets( 0, 0, 0, 0 ),
			        15,
			        0
			     )
			 );

		
		repaint (  );
		setVisible ( true );

		return 1;
	}

	private void close_actionPerformed ( ActionEvent e )
	{
		this.hide (  );
	}

	private void ok_actionPerformed ( ActionEvent e )
	{
		performSearch (  );
	}
	
	private void save_actionPerformed ( ActionEvent e )
	{
		saveExcel();
	}
	
	public class CSVFileFilter extends javax.swing.filechooser.FileFilter
	{
	
	  public boolean accept(File file)
	  {
	   
	      if (file.getName().toLowerCase().endsWith("csv"))
	      {
	        return true;
	      }
	   return false;
	  }

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}
	}



	private void saveExcel()
	{
		try
		{
			JFileChooser chooser = new JFileChooser(".");
			
		    chooser.addChoosableFileFilter(new CSVFileFilter());
			
			chooser.showSaveDialog(this); 
			
			File file = chooser.getSelectedFile(); 
			
			
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file,true));
			PrintWriter fileWriter = new PrintWriter(bufferedWriter);

			for(int i=0; i<tableHeaders.size(); ++i)
			{
				String s = '"'+tableHeaders.elementAt(i).toString()+'"'+",";
				fileWriter.print(s);
			}
			fileWriter.println("\n");
			for(int i=0; i<table.getRowCount(); ++i)
			{
				for(int j=0; j<table.getColumnCount(); ++j)
				{
					String s = '"'+table.getValueAt(i,j).toString()+'"'+",";
					fileWriter.print(s);
				}
				fileWriter.println("\n");
			}	
			fileWriter.close();
			JOptionPane.showMessageDialog(null, "Success");
		}catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, "Failure");
		}

	}
	
	
	public JTable getTable (  )
	{
		return table;
	}
	
	 public static void main(String[] args) 
	  {
	    try
	    {
	      (new SynapseListFrame()).setVisible(true);
	      
	      
	         
	    }
	    catch (Exception e)
	    {
	      e.printStackTrace();
	    }
	   
	  }

	

}
