import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;




public class SynapseViewFrame extends JFrame {

	private int synName;
	private String action;
	private String username,cusername, preCertainty, preSize,type, fromObj, toObj;
	private Date cdate,date;
	private String[] partner,postCertainty,postSize,postName;
	private Date[] postDate;
	private int count,i;
	private int[] postID;

	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private JLabel idLabel = new JLabel();
	private JLabel typeLabel = new JLabel();
	private JTextField typeTextField = new JTextField();
	
	private JLabel preLabel = new JLabel();
	private JTextField preTextField = new JTextField();
	private JButton showPreButton = new JButton();
	private JButton showPostButton = new JButton();
	private JLabel preCertaintyLabel = new JLabel();
	private JLabel preSizeLabel = new JLabel();
	private JButton delSynButton = new JButton();
	
	private ButtonGroup  preCertaintyGroup = new ButtonGroup();
	private JRadioButton preCertainButton = new JRadioButton("certain");
	private JRadioButton preUncertainButton = new JRadioButton("uncertain");
	
	private ButtonGroup preSizeGroup = new ButtonGroup();
	private JRadioButton preSmallButton = new JRadioButton("small");
	private JRadioButton preMediumButton = new JRadioButton("medium");
	private JRadioButton preLargeButton = new JRadioButton("large");
	
	
	
	private JLabel postLabel = new JLabel();
	private JTextField postTextField = new JTextField();
	
	private JLabel[] partnerLabel;
	private JLabel[] postIDLabel;
	
	private JTextField[] partnerTextField;
	
	private JLabel[] certaintyLabel;
	//private JLabel[] sizeLabel;
	
	private ButtonGroup[] certaintyGroup;
	private JRadioButton[] certainButton;
	private JRadioButton[] uncertainButton;
	
	private ButtonGroup[] sizeGroup;
	//private JRadioButton[] smallButton;
	//private JRadioButton[] mediumButton;
	//private JRadioButton[] largeButton;
	
	//private JButton[] deleteButton;
	private JButton[] showButton;
	ImageDisplayPanel iframe;
	
	
	private JButton modButton = new JButton();
	private JButton cancelButton = new JButton();
	
	private KeyListener enterListener=new KeyListener() {
		 public void keyTyped(KeyEvent e) {
			 
		 }

		  public void keyPressed(KeyEvent e) {
		    	if (e.getKeyCode() == KeyEvent.VK_ENTER) {
		    		modButton_actionPerformed(null);
				}
		  }

		 
		 public void keyReleased(KeyEvent e) {
		    	
		  }
	};
	
	private void addEnterListener(Component c) {
		c.addKeyListener(enterListener);
	}
	
	public SynapseViewFrame(int synN, String userN, ImageDisplayPanel iframe, String action1) {
		this.action = action1;
		this.synName = synN;
		this.cusername = userN;
		this.iframe = iframe;
		cdate = new Date(System.currentTimeMillis());
		date = cdate;
		

		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean isElectricalSynapse() {
		return type!=null && type.equals("electrical");
	}
	
	private boolean isCell() {
		return type!=null && type.startsWith("cell");
	}


	
	private void jbInit() throws Exception {
		
		
		JPanel p = new JPanel();
		
		
		p.setSize(600,800);
		p.setLayout(gridBagLayout1);
		JScrollPane scrollpane = new JScrollPane(p);
		getContentPane().add(scrollpane, BorderLayout.CENTER);
		

		ResultSet rs = getSynDataByName();
		if (rs.next()) {
            type = rs.getString("type");
            fromObj = rs.getString("fromObj");
            toObj = rs.getString("toObj");
			preCertainty = rs.	getString("certainty");
			preSize = rs.getString("size");
			date = rs.getDate("DateEntered");
			username = rs.getString("username");
			
		}
		
		if (isCell()) {
			setTitle("View Neighborhood Data");
		} else {
			setTitle("View The Synapse Data");
		}
		
		
		idLabel.setText("Synapse ID: " + synName + "  by "+username+"  "+date);
		typeLabel.setText("type:");
	    typeTextField.setText(type);
	    
	    String preLabelText="Pre:";
	    
	    if (isElectricalSynapse()) {
	    	preLabelText="Object 1:";
		} if (isCell()) {
			preLabelText="Central Cell";			
		}
		
		preLabel.setText(preLabelText);
		
		
		preTextField.setText(fromObj);
		showPreButton.setText("show "+fromObj);
		
		
		String postLabelText="Post:";
		
		if (isElectricalSynapse()) {
			postLabelText="Object 2:";
		} if (isCell()) {
			postLabelText="Neighbors:";
		}
		
		postLabel.setText(postLabelText);
		
		
		postTextField.setText(toObj);			
		showPostButton.setText("show "+toObj);
		
	
		preSizeLabel.setText("Size:");
		preCertaintyLabel.setText("Certainty:");
	
		
		delSynButton.setText("DEL");
		
		//postLabel.setText("Post:");

		idLabel.setBackground(new Color(206, 206, 230));

		modButton.setText("Modify");
		
		modButton.requestFocus();
		
		modButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				modButton_actionPerformed(e);
			}
		});
		
		cancelButton.setText("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancelButton_actionPerformed(e);
			}
		});
		
		delSynButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				delSynButton_actionPerformed(e);
			}
		});
		
		showPreButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showObjectButton_actionPerformed(e);
			}
		});

		showPostButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showObjectButton_actionPerformed(e);
			}
		});

		
		final ItemListener certaintyListener = new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (preCertainButton.isSelected()) {
					preCertainty = "certain";

				}
				if (preUncertainButton.isSelected()) {
					preCertainty = "uncertain";

				}
				
			}

		};

		preCertainButton.addItemListener(certaintyListener);
		preUncertainButton.addItemListener(certaintyListener);
		
		if (preCertainty.equals("certain")) {
			preCertainButton.setSelected(true);
		}

		if (preCertainty.equals("uncertain")) {
			preUncertainButton.setSelected(true);
		}
		
		final ItemListener sizeListener = new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (preSmallButton.isSelected()) {
					preSize = "small";

				}
				if (preMediumButton.isSelected()) {
					preSize = "normal";

				}
				if (preLargeButton.isSelected()) {
					preSize = "large";

				}
				
			}

		};
		
		
		preSmallButton.addItemListener(sizeListener);
		preMediumButton.addItemListener(sizeListener);
		preLargeButton.addItemListener(sizeListener);
		
		if (preSize.equals("small")) {
			preSmallButton.setSelected(true);
		}
		
		if (preSize.equals("normal")) {
			preMediumButton.setSelected(true);
		}
		
		if (preSize.equals("large")) {
			preLargeButton.setSelected(true);
		}

		

		preCertaintyGroup.add(preCertainButton);
		preCertaintyGroup.add(preUncertainButton);
		preSizeGroup.add(preSmallButton);
		preSizeGroup.add(preMediumButton);
		preSizeGroup.add(preLargeButton);
		
		p.add(
				idLabel,
				new GridBagConstraints(0, 0, 3, 1, 1.0, 1.0,
						GridBagConstraints.WEST, GridBagConstraints.NONE,
						new Insets(0, 20, 0, 0), 0, 0));
		if (action.equals("add")){
		p.add(
				delSynButton,
				new GridBagConstraints(3, 0, 1, 1, 1.0, 1.0,
						GridBagConstraints.WEST, GridBagConstraints.NONE,
						new Insets(0, 20, 0, 0), 0, 0));
		}
		
		p.add(
				typeLabel,
				new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
						GridBagConstraints.WEST, GridBagConstraints.NONE,
						new Insets(0, 20, 0, 0), 0, 0));
		p.add(
				typeTextField,
				new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
						0, 0));

		p.add(
				preLabel,
				new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0,
						GridBagConstraints.WEST, GridBagConstraints.NONE,
						new Insets(0, 20, 0, 0), 0, 0));
		p.add(
				preTextField,
				new GridBagConstraints(1, 2, 1, 1, 1.0, 1.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
						0, 0));
		p.add(
				showPreButton,
				new GridBagConstraints(2, 2, 1, 1, 1.0, 1.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
						0, 0));

		p.add(
				postLabel,
				new GridBagConstraints(0, 3, 1, 1, 1.0, 1.0,
						GridBagConstraints.WEST, GridBagConstraints.NONE,
						new Insets(0, 20, 0, 0), 0, 0));
		p.add(
				postTextField,
				new GridBagConstraints(1, 3, 1, 1, 1.0, 1.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
						0, 0));
		
		if (isElectricalSynapse()) {
		p.add(
				showPostButton,
				new GridBagConstraints(2, 3, 1, 1, 1.0, 1.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
						0, 0));
		}

		if (!isCell()) {
			p.add(preCertaintyLabel, new GridBagConstraints(0, 4, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 20, 0, 0), 0,
					0));
			p.add(preCertainButton, new GridBagConstraints(1, 4, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 20, 0, 0), 0,
					0));
			p.add(preUncertainButton, new GridBagConstraints(2, 4, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 20, 0, 0),
					0, 0));

			p.add(preSizeLabel, new GridBagConstraints(0, 5, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 20, 0, 0), 0, 0));
			p.add(preSmallButton, new GridBagConstraints(1, 5, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 20, 0, 0), 0, 0));
			p.add(preMediumButton,
					new GridBagConstraints(2, 5, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 20, 0, 0), 0, 0));

			p.add(preLargeButton, new GridBagConstraints(3, 5, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 20, 0, 0), 0, 0));
		}
		

		
		
		
		// display the synapse records
		i = 0;
		if (!isElectricalSynapse()) {
		rs = getSynRecord();
		//JOptionPane.showMessageDialog(null, "count:"+count);
		partnerLabel = new JLabel[count];
		
		partnerTextField = new JTextField[count];
		postID = new int[count];
		postDate = new Date[count];
		certaintyLabel = new JLabel[count];
		//sizeLabel = new JLabel[count];
		postIDLabel = new JLabel[count];
		partner = new String[count];
		postName = new String[count];
		
		//deleteButton = new JButton[count];
		showButton = new JButton[count];



		certaintyGroup = new ButtonGroup[count];
		certainButton = new JRadioButton[count];
		uncertainButton = new JRadioButton[count];
		postCertainty = new String[count];
		postSize = new String[count];
		sizeGroup = new ButtonGroup[count];
		//smallButton = new JRadioButton[count];
		//mediumButton = new JRadioButton[count];
		//largeButton = new JRadioButton[count];
		
		
		
		
	
		while (rs.next()){
			
			postID[i] = rs.getInt("idx");
			partner[i]= rs.getString("partner");
			postName[i] = rs.getString("username");
			postDate[i] = rs.getDate("DateEntered");
			postCertainty[i] = rs.getString("certainty");
			postSize[i] = rs.getString("size");
			
			postIDLabel[i] = new JLabel("Record ID: "+postID[i]+" Synapse ID: " + synName + "  by "+ postName[i] + "  "+ postDate[i]);
			//sizeLabel[i] = new JLabel("Size:");
			certaintyLabel[i] = new JLabel("Certainty:");
			
			String parnerLabelText="post partner";
			if (isCell()) {
				parnerLabelText="neighbor";
			}
 			partnerLabel[i]= new JLabel(parnerLabelText);
 			
			partnerLabel[i].setBackground(new Color(206, 206, 230));
			partnerTextField[i] = new JTextField();
			partnerTextField[i].setText(partner[i]);
			
            certaintyGroup[i] = new ButtonGroup();
            sizeGroup[i] = new ButtonGroup();
            certainButton[i] = new JRadioButton("certain");
            uncertainButton[i] = new JRadioButton("uncertain");
          // smallButton[i] = new JRadioButton("small");
         //  mediumButton[i] = new JRadioButton("medium");
          // largeButton[i] = new JRadioButton("large");
           
          // deleteButton[i] = new JButton("delete record "+postID[i]);
            String showButtonText="show post partner "+partner[i];
            if (isCell()) showButtonText="show neighbor "+partner[i];
            showButton[i] = new JButton();
            showButton[i].setText(showButtonText);
           
           //deleteButton[i].addActionListener(new ActionListener() {
			//	public void actionPerformed(ActionEvent e) {
			//		deleteButton_actionPerformed(e);
			//	}
			//});
           
           showButton[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					showButton_actionPerformed(e);
				}
			});
		

			

			certainButton[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					postButton_actionPerformed(e);
				}
			});
				
			uncertainButton[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					postButton_actionPerformed(e);
				}
			});
			
			if (postCertainty[i].equals("certain")) {
				certainButton[i].setSelected(true);
			}

			if (postCertainty[i].equals("uncertain")) {
				uncertainButton[i].setSelected(true);
			}
			
			
			
//			
//			smallButton[i].addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent e) {
//					postButton_actionPerformed(e);
//				}
//			});
//			mediumButton[i].addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent e) {
//					postButton_actionPerformed(e);
//				}
//			});
//			largeButton[i].addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent e) {
//					postButton_actionPerformed(e);
//				}
//			});
//			
//			if (postSize[i].equals("small")) {
//				smallButton[i].setSelected(true);
//			}
//			
//			if (postSize[i].equals("normal")) {
//				mediumButton[i].setSelected(true);
//			}
//			
//			if (postSize[i].equals("large")) {
//				largeButton[i].setSelected(true);
//			}

			

			certaintyGroup[i].add(certainButton[i]);
			certaintyGroup[i].add(uncertainButton[i]);
//			sizeGroup[i].add(smallButton[i]);
//			sizeGroup[i].add(mediumButton[i]);
//			sizeGroup[i].add(largeButton[i]);
			
			p.add(
					postIDLabel[i],
					new GridBagConstraints(0, 8+i*6, 3, 1, 1.0, 1.0,
							GridBagConstraints.WEST, GridBagConstraints.NONE,
							new Insets(0, 20, 0, 0), 0, 0));

			p.add(
					partnerLabel[i],
					new GridBagConstraints(0, 9+i*6, 1, 1, 1.0, 1.0,
							GridBagConstraints.WEST, GridBagConstraints.NONE,
							new Insets(0, 20, 0, 0), 0, 0));
			p.add(
					partnerTextField[i],
					new GridBagConstraints(1, 9+i*6, 1, 1, 1.0, 1.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
							0, 0));

			

			p.add(
					certaintyLabel[i],
					new GridBagConstraints(0, 10+i*6, 1, 1, 1.0, 1.0,
							GridBagConstraints.WEST, GridBagConstraints.NONE,
							new Insets(0, 20, 0, 0), 0, 0));
			p.add(
					certainButton[i],
					new GridBagConstraints(1, 10+i*6, 1, 1, 1.0, 1.0,
							GridBagConstraints.WEST, GridBagConstraints.NONE,
							new Insets(0, 20, 0, 0), 0, 0));
			p.add(
					uncertainButton[i],
					new GridBagConstraints(2, 10+i*6, 1, 1, 1.0, 1.0,
							GridBagConstraints.WEST, GridBagConstraints.NONE,
							new Insets(0, 20, 0, 0), 0, 0));
			
//			p.add(
//					sizeLabel[i],
//					new GridBagConstraints(0, 11+i*6, 1, 1, 1.0, 1.0,
//							GridBagConstraints.WEST, GridBagConstraints.NONE,
//							new Insets(0, 20, 0, 0), 0, 0));
//			p.add(
//					smallButton[i],
//					new GridBagConstraints(1, 11+i*6, 1, 1, 1.0, 1.0,
//							GridBagConstraints.WEST, GridBagConstraints.NONE,
//							new Insets(0, 20, 0, 0), 0, 0));
//			p.add(
//					mediumButton[i],
//					new GridBagConstraints(2, 11+i*6, 1, 1, 1.0, 1.0,
//							GridBagConstraints.WEST, GridBagConstraints.NONE,
//							new Insets(0, 20, 0, 0), 0, 0));
//			
//			p.add(
//					largeButton[i],
//					new GridBagConstraints(3, 11+i*6, 1, 1, 1.0, 1.0,
//							GridBagConstraints.WEST, GridBagConstraints.NONE,
//							new Insets(0, 20, 0, 0), 0, 0));
//			
//			p.add(
//					deleteButton[i],
//					new GridBagConstraints(0, 12+i*6, 2, 1, 1.0, 1.0,
//							GridBagConstraints.WEST, GridBagConstraints.NONE,
//							new Insets(0, 20, 0, 0), 0, 0));
//			
			p.add(
					showButton[i],
					new GridBagConstraints(2, 12+i*6, 2, 1, 1.0, 1.0,
							GridBagConstraints.WEST, GridBagConstraints.NONE,
							new Insets(0, 20, 0, 0), 0, 0));

			
			
			
			i++;
		}
		}
		
		setSize(600, 400+i*100);
		
		p.add(
				modButton,
				new GridBagConstraints(0, 8+i*6, 4, 1, 1.0, 1.0,
						GridBagConstraints.CENTER, GridBagConstraints.NONE,
						new Insets(0, 20, 0, 0), 0, 0));
		
		p.add(
				cancelButton,
				new GridBagConstraints(2, 8+i*6, 4, 1, 1.0, 1.0,
						GridBagConstraints.CENTER, GridBagConstraints.NONE,
						new Insets(0, 20, 0, 0), 0, 0));
		
		
		
		//make all components aware of Enter key.
		addEnterListener(p);
		for(Component c:p.getComponents()) {		
			addEnterListener(c);
		}

		
	}


	
	private void delSynButton_actionPerformed(ActionEvent e)
	{
		
		Connection con = null;

		try {
			con = EDatabase.borrowConnection(
					
					);

			PreparedStatement pst = con
					.prepareStatement("delete from object2 where OBJ_Name = ? and username = ?");
			pst.setInt(1, synName);
			pst.setString(2,username);
			
			if ( pst.executeUpdate()< 0) {
				JOptionPane.showMessageDialog(this, "Record delete failed");
			    } else {
			    JOptionPane.showMessageDialog(this, "Record delete successfully");
			    }
		}catch (Exception ex) {
			ex.printStackTrace();
		}finally {
			EDatabase.returnConnection(con);
		}

		
		SynapseViewFrame vsframe = new SynapseViewFrame(synName,username,iframe,"view");
		vsframe.setVisible(true);
		this.dispose();
		
		
	}

	private void deleteButton_actionPerformed(ActionEvent e) {
		int idx =Integer.parseInt((e.getActionCommand()).substring(14));
		Connection con = null;

		try {
			con = EDatabase.borrowConnection(
					
					);

			PreparedStatement pst = con
					.prepareStatement("delete from synrecord where idx = ?");
			pst.setInt(1, idx);
			
			if ( pst.executeUpdate()< 0) {
				JOptionPane.showMessageDialog(this, "Record delete failed");
			    } else {
			    JOptionPane.showMessageDialog(this, "Record delete successfully");
			    }
		}catch (Exception ex) {
			ex.printStackTrace();
		}finally {
			EDatabase.returnConnection(con);
		}

		
		SynapseViewFrame vsframe = new SynapseViewFrame(synName,username,iframe,"view");
		vsframe.setVisible(true);
		this.dispose();
		
	}
	
    private void showButton_actionPerformed(ActionEvent e) {
    	String idx =(e.getActionCommand()).substring(18);
    	//Util.info("showname: "+idx);
       	iframe.showObj(idx);
    	
    	
	}
    
    private void cancelButton_actionPerformed(ActionEvent e) {
    	
       	this.dispose();
    	
    	
	}
    
    private void showObjectButton_actionPerformed(ActionEvent e) {
    	String idx =(e.getActionCommand()).substring(5);    
       	iframe.showObj(idx);
	}


	
	
	private void postButton_actionPerformed(ActionEvent e){
			
				for (int j=0;j<count;j++){
		        if (certainButton[j].isSelected()) {
				postCertainty[j] = "certain";

			    }
			    if (uncertainButton[j].isSelected()) {
				postCertainty[j] = "uncertain";
			    }
//			    if (smallButton[j].isSelected()) {
//					postSize[j] = "small";
//
//				}
//				if (mediumButton[j].isSelected()) {
//					postSize[j] = "normal";
//
//				}
//				if (largeButton[j].isSelected()) {
//					postSize[j] = "large";
//				}
				}
			
	
	};
	
	

	public ResultSet getSynDataByName() {
		Connection con = null;
		ResultSet rs = null;

		try {
			con = EDatabase.borrowConnection(
					
					);

			Statement stm = con.createStatement();
			if (action.equals("add")){
				rs = stm.executeQuery("SELECT type, fromOBJ, toOBJ, username,DateEntered,certainty,size"
							+ " FROM object2 where OBJ_Name=" + synName);
			}else{
				rs = stm.executeQuery("SELECT type, fromOBJ, toOBJ, username,DateEntered,certainty,size"
						+ " FROM object where OBJ_Name=" + synName);	
			}
		}

		catch (Exception ex) {
			ex.printStackTrace();
		}finally {
			EDatabase.returnConnection(con);
		}

		

		return rs;
	}
	
	
	public ResultSet getSynRecord() {
		Connection con = null;
		ResultSet rs = null;

		try {
			con = EDatabase.borrowConnection(
					
					);

			
			
			
			Statement stm = con.createStatement();
			if (action.equals("view")){
				rs = stm.executeQuery("SELECT count(*) from synrecord where synID="+synName);
			}else{
				rs = stm.executeQuery("SELECT count(*) from synrecord where username='"+username+"' and synID="+synName );
			}
			
			rs.next();
			count = rs.getInt(1);
			rs.close();
			if (action.equals("view")){
			rs = stm.executeQuery("SELECT * FROM synrecord where synID=" + synName);
			}else{
				rs = stm.executeQuery("SELECT * FROM synrecord where username='"+username+"' and synID=" + synName);	
			}
			
		}

		catch (Exception ex) {
			ex.printStackTrace();
		}finally {
			EDatabase.returnConnection(con);
		}

		

		return rs;
	}

	private void modButton_actionPerformed(ActionEvent e) {
		Connection con = null;

		try {
			con = EDatabase.borrowConnection();

			PreparedStatement pst = null;
			if (action.equals("add")){
			pst = con.prepareStatement("UPDATE object2 set type = ?, "
							+ "fromObj = ?, toObj = ?, certainty = ?, size = ? WHERE OBJ_Name = ?");
			}else{
				
			pst = con.prepareStatement("UPDATE object set type = ?, "
						+ "fromObj = ?, toObj = ?, certainty = ?, size = ? WHERE OBJ_Name = ?");	
			}
			
			
			pst.setString(1, typeTextField.getText());
			pst.setString(2, preTextField.getText());
			pst.setString(3, postTextField.getText());
		
			pst.setString(4, preCertainty);
			pst.setString(5, preSize);
			pst.setInt(6, synName);
			if ( pst.executeUpdate()< 0) {
				JOptionPane.showMessageDialog(null, "Data Modify failed");
			    }
			
			for (int j=0;j<count;j++)
			{ 
			pst = con.prepareStatement("UPDATE synrecord set partner = ?, certainty = ?, size = ? WHERE idx = ?");
			pst.setString(1, partnerTextField[j].getText());
			
			
			pst.setString(2, postCertainty[j]);
			pst.setString(3, postSize[j]);
			pst.setInt(4, postID[j]);
			if ( pst.executeUpdate()< 0) {
				JOptionPane.showMessageDialog(null, "Data Modify failed");
			    }
			}
			//JOptionPane.showMessageDialog(null, "Data Modify successfully");
			this.dispose();

			
			
			
			
			
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}finally {
			EDatabase.returnConnection(con);
		}

		
	}
}
