import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/*
 * Paints "Filter Options" frame.
 * 
 * @Author Sasha Levchuk
 */
public class FilterFrame extends JFrame {

	int GAP = 10;

	JTextField continNumberInput = new JTextField();
	JTextField continNameInput = new JTextField();
	JTextField colorCodeInput = new JTextField();
	
	ButtonGroup continFilterGroup = new ButtonGroup();
	ButtonGroup objectFilterGroup = new ButtonGroup();

	JTextField objectNumberInput = new JTextField();
	JTextField objectContinNameInput = new JTextField();
	JTextField objectContinNumberInput = new JTextField();

	JCheckBox objectShowColorCode;
	JCheckBox objectShowNumber;
	JCheckBox objectShowContin;

	JButton ok = new JButton("OK");
	
	public FilterFrame() {

		super("Filters");
		setResizable(false);
		addComponentsToPane(this);

		//remember last saved object type.
		//will use it when toggling between "none" and last selected.
		if (Elegance.filterOptions.getObjectFilterType()!=FilterOptions.ObjectFilterType.none) {
			Elegance.filterOptions.setPreviousObjectFilterType(Elegance.filterOptions.getObjectFilterType());
		}
		
	}

	
	private void addComponentsToPane(final Container pane) {

		JPanel panel = new JPanel();
		pane.add(panel);

		BoxLayout layout = new BoxLayout(panel, BoxLayout.Y_AXIS);

		panel.setLayout(layout);

		panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

		panel.setLayout(layout);

		addContinFilter(panel);
		addObjectFilter(panel);
		addObjectLabels(panel);

		addContols(panel);

	}

	private void addContinFilter(final Container pane) {

		//init text boxes with saved values if any
		if (Elegance.filterOptions.getContinFilterType() == FilterOptions.ContinFilterType.custom_number) {
			continNumberInput.setText(EString.join(Elegance.filterOptions.getContinFilterCustom(),","));
		} else if (Elegance.filterOptions.getContinFilterType() == FilterOptions.ContinFilterType.custom_name) {
			continNameInput.setText(EString.join(Elegance.filterOptions.getContinFilterCustom(),","));
		}

		GridLayout layout = new GridLayout(0, 2);

		layout.setVgap(GAP);

		final JPanel panel = new JPanel();
		pane.add(panel, BorderLayout.NORTH);

		panel.setLayout(layout);
		layout.layoutContainer(panel);

		panel.setBorder(BorderFactory.createTitledBorder("Show contin(s):"));

		add("all", FilterOptions.ContinFilterType.all, panel);

		panel.add(new JLabel(""));
		add("with number (ex. 25, 121, 30)", FilterOptions.ContinFilterType.custom_number, panel);

		panel.add(continNumberInput);

		add("with name (ex. ContinA, ContinB)", FilterOptions.ContinFilterType.custom_name, panel);
		panel.add(continNameInput);
		
		add("with color code (ex. blue-10, green-5)", FilterOptions.ContinFilterType.custom_color_code, panel);
		panel.add(colorCodeInput);
		
		add("none", FilterOptions.ContinFilterType.none, panel);

	}

	private void addObjectFilter(final Container pane) {

		//init text boxes with saved values if any
		if (Elegance.filterOptions.getObjectFilterType() == FilterOptions.ObjectFilterType.custom_number) {
			objectNumberInput.setText(EString.join(Elegance.filterOptions.getObjectFilterCustom(),","));
		} else if (Elegance.filterOptions.getObjectFilterType() == FilterOptions.ObjectFilterType.custom_contin_number) {
			objectContinNumberInput.setText(EString.join(Elegance.filterOptions.getObjectFilterCustom(),","));
		} else if (Elegance.filterOptions.getObjectFilterType() == FilterOptions.ObjectFilterType.custom_contin_name) {
			objectContinNameInput.setText(EString.join(Elegance.filterOptions.getObjectFilterCustom(),","));
		}

		GridLayout layout = new GridLayout(0, 2);

		layout.setVgap(GAP);

		final JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder("Show object(s):"));
		panel.setLayout(layout);

		add("all", FilterOptions.ObjectFilterType.all, panel);
		panel.add(new JLabel(""));
		add("with number (ex. 25, 121, 30)", FilterOptions.ObjectFilterType.custom_number, panel);
		panel.add(objectNumberInput);

		add("with contin number (ex. 432,345)", FilterOptions.ObjectFilterType.custom_contin_number, panel);		
		panel.add(objectContinNumberInput);

		add("with contin name (ex. ContinA, ContinB)", FilterOptions.ObjectFilterType.custom_contin_name, panel);
		panel.add(objectContinNameInput);

		add("none", FilterOptions.ObjectFilterType.none, panel);

		layout.layoutContainer(panel);

		pane.add(panel, BorderLayout.NORTH);
	

	}

	private void addObjectLabels(final Container pane) {

		GridLayout layout = new GridLayout(0, 1);

		layout.setVgap(GAP);

		final JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder("Object Attributes to Display:"));
		panel.setLayout(layout);

		JCheckBox button = new JCheckBox("Color Code");

		if (Elegance.filterOptions.isObjectShowColorCode())
			button.setSelected(true);
		button.setActionCommand("objectShowColorCode");
		panel.add(button);
		this.objectShowColorCode = button;

		button = new JCheckBox("Object Number");
		if (Elegance.filterOptions.isObjectShowNumber())
			button.setSelected(true);
		button.setActionCommand("objectShowNumber");
		panel.add(button);
		this.objectShowNumber = button;

		button = new JCheckBox("Contin Name");
		if (Elegance.filterOptions.isObjectShowContin())
			button.setSelected(true);
		button.setActionCommand("objectShowContin");
		panel.add(button);
		this.objectShowContin = button;

		layout.layoutContainer(panel);

		pane.add(panel, BorderLayout.NORTH);

	}

	private void addContols(final Container pane) {

		
		JButton cancel = new JButton("Cancel");

		final JPanel panel = new JPanel();
	
		BoxLayout layout = new BoxLayout(panel, BoxLayout.LINE_AXIS);

		panel.setLayout(layout);

		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		panel.add(Box.createHorizontalGlue());
		panel.add(ok);

		panel.add(Box.createRigidArea(new Dimension(5, 0)));
		panel.add(cancel);

		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancel_actionPerformed(e);
			}
		});

		pane.add(panel, BorderLayout.CENTER);
	}

	//add buttons to select Elegance.filterOptions.ContinFilterType
	private JRadioButton add(String label, FilterOptions.ContinFilterType type, JPanel panel) {
		JRadioButton button = new JRadioButton(label);
		addSpaceListener(button);

		if (Elegance.filterOptions.getContinFilterType().equals(type))
			button.setSelected(true);

		continFilterGroup.add(button);
		button.setActionCommand(type.name());
		panel.add(button);
		return button;
	}

	//add buttons to select Elegance.filterOptions.ObjectFilterType
	private JRadioButton add(String label, FilterOptions.ObjectFilterType type, JPanel panel) {
		JRadioButton button = new JRadioButton(label);
		addSpaceListener(button);		
		if (Elegance.filterOptions.getObjectFilterType().equals(type)) {
			button.setSelected(true);
		}
		objectFilterGroup.add(button);
		button.setActionCommand(type.name());
		panel.add(button);
		return button;
	}

	//export OK button so parent can perform necessary actions on "OK" click.
	public JButton getOKButton() {
		return this.ok;
	}
	
	public boolean ok_actionPerformed(ActionEvent e) {
		
		try {
		
		String actionCommand = "";
		ButtonModel continFilterButton = continFilterGroup.getSelection();
		if (continFilterButton != null) {
			
			Integer[] continNums = {};
			
			actionCommand = continFilterButton.getActionCommand();

			Elegance.filterOptions.setContinFilterType(FilterOptions.ContinFilterType.valueOf(actionCommand));
			
			
			//process contin filter
			if (Elegance.filterOptions.getContinFilterType() == FilterOptions.ContinFilterType.custom_number) {

				Set<String> set = EString.extract(continNumberInput.getText());
				
				if (set.isEmpty()) {
					JOptionPane.showMessageDialog(this, "No contin number(s) specified");
					return false;
				}
				
				for (String continNum : set) {
					try {
						Long.parseLong(continNum);
					} catch (Throwable e2) {
						JOptionPane.showMessageDialog(this, continNum+" is not a number ");
						return false;
					}
					
				}
				
				continNums = new Integer[set.size()];

				Set<Object> existingSet = new HashSet(EDatabase.getFirstColumn("select con_number from contin where con_number in (??)", set));

				int i = 0;
				
				for (String continNum : set) {
					if (!existingSet.contains(new Long(continNum))) {
						JOptionPane.showMessageDialog(this, "Contin #" + continNum + " does not exist in database");
						return false;
					}
					continNums[i++] = new Integer(continNum).intValue();
				}
								
				Elegance.filterOptions.setContinNums(continNums); 
				Elegance.filterOptions.setContinFilterCustom(set);
				
			} else if (Elegance.filterOptions.getContinFilterType() == FilterOptions.ContinFilterType.custom_name) {
				
				Set<String> set = EString.extract(continNameInput.getText());
				if (set.isEmpty()) {
					JOptionPane.showMessageDialog(this, "No contin name(s) specified");
					return false;
				}

				List<Object> list = EDatabase.getFirstColumn("select con_number from contin where CON_AlternateName in (??)", set);

				if (list.isEmpty()) {
					JOptionPane.showMessageDialog(this, "No contins found in database by name(s) " + set);
					return false;
				}

				continNums = new Integer[list.size()];

				int i = 0;
				for (Object continNum : list) {

					continNums[i++] = ((Long) continNum).intValue();
				}
				
				Elegance.filterOptions.setContinNums(continNums); 				
				Elegance.filterOptions.setContinFilterCustom(set);
			} else if (Elegance.filterOptions.getContinFilterType() == FilterOptions.ContinFilterType.custom_color_code) {
				Set<String> set = EString.extract(colorCodeInput.getText());
				if (set.isEmpty()) {
					JOptionPane.showMessageDialog(this, "No contin color code specified");
					return false;
				}

				String sql = "select con_number\n" + "from contin \n" + "where CON_AlternateName2 is not null\n" + "and CON_AlternateName2 in (??)\n";

				List<Object> list = EDatabase.getFirstColumn(sql, set);

				if (list.isEmpty()) {
					JOptionPane.showMessageDialog(this, "No contin found in database by color code(s) " + set);
					return false;
				}

				continNums = new Integer[list.size()];

				int i = 0;
				for (Object continNum : list) {

					continNums[i++] = ((Long) continNum).intValue();
				}
				
				Elegance.filterOptions.setContinNums(continNums); 				
				Elegance.filterOptions.setContinFilterCustom(set);

			} else {
				Elegance.filterOptions.setContinNums(new Integer[] {}); 
			}

		} else {
			// ignore if no button is selected
		}

		
		
		//process object filter
		ButtonModel objectFilterButton = objectFilterGroup.getSelection();
		if (objectFilterButton != null) {
			
			actionCommand = objectFilterButton.getActionCommand();

			Elegance.filterOptions.setObjectFilterType(FilterOptions.ObjectFilterType.valueOf(actionCommand));

			if (Elegance.filterOptions.getObjectFilterType() == FilterOptions.ObjectFilterType.custom_number) {
			
				
				Set<String> set = EString.extract(objectNumberInput.getText());
				
				if (set.isEmpty()) {
					JOptionPane.showMessageDialog(this, "No object number(s) specified");
					return false;
				}
				
				for (String objectNum : set) {
					try {
						Long.parseLong(objectNum);
					} catch (Throwable e2) {
						JOptionPane.showMessageDialog(this, objectNum+" is not a number ");
						return false;
					}
					
				}
				

				Set<Object> existingSet = new HashSet(EDatabase.getFirstColumn("select OBJ_Name from object where OBJ_Name in (??)", set));

				
				for (String objectNum : set) {
					if (!existingSet.contains(new Long(objectNum))) {
						JOptionPane.showMessageDialog(this, "Object #" + objectNum + " does not exist in database");
						return false;
					}					
				}
								
				Elegance.filterOptions.setObjectFilterCustom(EString.extract(objectNumberInput.getText()));
				
			} else if (Elegance.filterOptions.getObjectFilterType() == FilterOptions.ObjectFilterType.custom_contin_number) {
				
				Set<String> set = EString.extract(objectContinNumberInput.getText());
				
				if (set.isEmpty()) {
					JOptionPane.showMessageDialog(this, "No object contin number(s) specified");
					return false;
				}
				
				for (String continNum : set) {
					try {
						Long.parseLong(continNum);
					} catch (Throwable e2) {
						JOptionPane.showMessageDialog(this, continNum+" is not a number ");
						return false;
					}
					
				}
				
				Set<Object> existingSet = new HashSet(EDatabase.getFirstColumn("select con_number from contin where con_number in (??)", set));

				
				
				for (String continNum : set) {
					if (!existingSet.contains(new Long(continNum))) {
						JOptionPane.showMessageDialog(this, "Contin #" + continNum + " does not exist in database");
						return false;
					}				
				}
								
				Elegance.filterOptions.setObjectFilterCustom(EString.extract(objectContinNumberInput.getText()));
				
			} else if (Elegance.filterOptions.getObjectFilterType() == FilterOptions.ObjectFilterType.custom_contin_name) {
				
				Set<String> set = EString.extract(objectContinNameInput.getText());
				if (set.isEmpty()) {
					JOptionPane.showMessageDialog(this, "No contin name(s) specified");
					return false;
				}

				List<Object> list = EDatabase.getFirstColumn("select con_number from contin where CON_AlternateName in (??)", set);

				if (list.isEmpty()) {
					JOptionPane.showMessageDialog(this, "No contins found in database by name(s) " + set);
					return false;
				}
				
				Elegance.filterOptions.setObjectFilterCustom(EString.extract(objectContinNameInput.getText()));
			}

		} else {
			// ignore if no button is selected
		}

		//process object attributes
		Elegance.filterOptions.setObjectShowColorCode(objectShowColorCode.isSelected());
		Elegance.filterOptions.setObjectShowNumber(objectShowNumber.isSelected());
		Elegance.filterOptions.setObjectShowContin(objectShowContin.isSelected());

		Elegance.filterOptions.setHideAll(false);
		
		this.hide();
		
		 
		} catch (Throwable e1) {
			ELog.info("Cant read options " + e);
			throw new IllegalStateException("Cant read options ", e1);
		}
		
		
		
		return true;
	}

	private void cancel_actionPerformed(ActionEvent e) {
		this.hide();
	}
	
	
	// jumps between none and last selected button for object filter. 
	// bound Ctrl-spacebar key 
	private void toggleNone() {
		ButtonModel objectFilterButton = objectFilterGroup.getSelection();
		if (objectFilterButton != null) {
			
			FilterOptions.ObjectFilterType currentType = FilterOptions.ObjectFilterType.valueOf(objectFilterButton.getActionCommand());
			FilterOptions.ObjectFilterType previousType = Elegance.filterOptions.getPreviousObjectFilterType();
			
			
			
			if (currentType==FilterOptions.ObjectFilterType.none)  {
				
				//restore text field value
				switch(previousType) {
				
					case custom_number: objectNumberInput.setText(EString.join(Elegance.filterOptions.getObjectFilterCustom(),","));break;
					case custom_contin_number:objectContinNumberInput.setText(EString.join(Elegance.filterOptions.getObjectFilterCustom(),","));break;
					case custom_contin_name:objectContinNameInput.setText(EString.join(Elegance.filterOptions.getObjectFilterCustom(),","));break;
					
				}
				
				getButton(previousType).doClick();			
				return;
			}
			
			
			getButton(FilterOptions.ObjectFilterType.none).doClick();
		
		}
	}
		
	//returns button by object type
	private AbstractButton getButton(FilterOptions.ObjectFilterType type) {
			Enumeration<AbstractButton> e=objectFilterGroup.getElements();
			while(e.hasMoreElements()) {
				AbstractButton b=e.nextElement();
				if (type==FilterOptions.ObjectFilterType.valueOf(b.getActionCommand())) return b;				
			}
			return null;
	}
	
	private KeyListener spaceListener=new KeyListener() {
		 public void keyTyped(KeyEvent e) {
			 
		 }

		  public void keyPressed(KeyEvent e) {
			  if (e.getKeyCode() == KeyEvent.VK_SPACE && e.isControlDown()) {
		    		toggleNone();
			  }
		  }

		 
		 public void keyReleased(KeyEvent e) {
			 
		  }
	};
	
	private void addSpaceListener(Component c) {
		c.addKeyListener(spaceListener);
	}
	
	
}