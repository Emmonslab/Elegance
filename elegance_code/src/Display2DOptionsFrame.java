import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/*
 * Paints "Display 2D Options" frame.
 * 
 * @Author Sasha Levchuk
 */
public class Display2DOptionsFrame extends JFrame {

	private int GAP = 10;

	private JTextField continNumberInput = new JTextField();
	private JTextField continNameInput = new JTextField();
	private JTextField continColorInput = new JTextField();
	
	private JTextField neurons2ConnectInput = new JTextField();

	private ButtonGroup continFilterGroup = new ButtonGroup();
	
	private JButton ok = new JButton("OK");
	
	private JComboBox synapsesCombo = new JComboBox();
	
	private JComboBox zoomCombo = new JComboBox();
			
	private JComboBox dtypeCombo = new JComboBox();
	
	private static final String[] ALL_ZOOMS = { "1", "2", "5","10" };
	private static final String[] ALL_D_TYPES = { "Z_Y", "Z_X", "Z_Y_NoSynapse","Z_X_NoSynapse" };

	public Display2DOptionsFrame() {

		super("Display Options");
		setResizable(false);
		addComponentsToPane(this);
	}

	private void addComponentsToPane(final Container pane) {

		JPanel panel = new JPanel();
		pane.add(panel);

		BoxLayout layout = new BoxLayout(panel, BoxLayout.Y_AXIS);

		panel.setLayout(layout);

		panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

		// GridLayout layout = new GridLayout(0, 1);
		// layout.setVgap(GAP);

		panel.setLayout(layout);

		addContinFilter(panel);
		addImageOptions(panel);
		//addObjectFilter(panel);
		//addObjectLabels(panel);

		addContols(panel);

	}

	private void addContinFilter(final Container pane) {

	
		GridLayout layout = new GridLayout(0, 2);

		layout.setVgap(GAP);

		final JPanel panel = new JPanel();
		pane.add(panel, BorderLayout.NORTH);

		panel.setLayout(layout);
		layout.layoutContainer(panel);

		panel.setBorder(BorderFactory.createTitledBorder("Show contin(s):"));

		add("with number (ex. 2, 4-9, 30)", Display2DOptions.ContinFilterType.custom_number, continNumberInput,panel);
	
		add("with name (ex. ContinA, ContinB)", Display2DOptions.ContinFilterType.custom_name,continNameInput, panel);
		
		add("with color code (ex. blue-10, green-5)", Display2DOptions.ContinFilterType.custom_color_code, continColorInput, panel);
	
	}
	
	
	private void addImageOptions(final Container pane) {

		GridLayout layout = new GridLayout(0, 2);

		layout.setVgap(GAP);

		final JPanel panel = new JPanel();
		pane.add(panel, BorderLayout.NORTH);

		panel.setLayout(layout);
		layout.layoutContainer(panel);

		panel.setBorder(BorderFactory.createTitledBorder("Image Options:"));

		panel.add(new JLabel("Synapse Type:"));
		for(Display2DOptions.SynapseType type:Display2DOptions.SynapseType.getValues()) {
			synapsesCombo.addItem(type.getLabel());			 
		}
		synapsesCombo.setSelectedIndex(Display2DOptions.SynapseType.getIndex(Elegance.display2DOptions.getSynapseType()));
				
		panel.add(synapsesCombo);
		
		panel.add(new JLabel("Display Type:"));
		for(String dtypeStr:ALL_D_TYPES) {
			dtypeCombo.addItem(dtypeStr);
		}
		panel.add(dtypeCombo);
		
		
		dtypeCombo.setSelectedIndex(Arrays.asList(ALL_D_TYPES).indexOf(Elegance.display2DOptions.getDtype()));
		
		panel.add(new JLabel("Zoom:"));
		for(String zoomStr:ALL_ZOOMS) {
			zoomCombo.addItem(zoomStr);
		}
		panel.add(zoomCombo);
		zoomCombo.setSelectedIndex(Arrays.asList(ALL_ZOOMS).indexOf(Elegance.display2DOptions.getZoom()+""));
		
		panel.add(new JLabel("Neurons To Connect: (ex. 10:20, 21:35)"));
		panel.add(neurons2ConnectInput);
		
		if (Elegance.display2DOptions.getNeurons2Connect()!=null) {
			List<String> str=new ArrayList<String>(); 
			for(Display2DOptions.NeuronPair pair:Elegance.display2DOptions.getNeurons2Connect()) {
				str.add(pair.getFromId()+":"+pair.getToId());
			}
			neurons2ConnectInput.setText(EString.join(str,","));
		}
		
		
	}
	
	private void addContols(final Container pane) {

		
		JButton cancel = new JButton("Cancel");

		final JPanel panel = new JPanel();

		// GridLayout layout = new GridLayout(1, 2);
		BoxLayout layout = new BoxLayout(panel, BoxLayout.LINE_AXIS);
		// Lay out the buttons from left to right.

		panel.setLayout(layout);

		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		panel.add(Box.createHorizontalGlue());
		panel.add(ok);

		panel.add(Box.createRigidArea(new Dimension(5, 0)));
		panel.add(cancel);

		// layout.setVgap(GAP);
		// layout.layoutContainer(panel);

	

		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancel_actionPerformed(e);
			}
		});

		pane.add(panel, BorderLayout.CENTER);
	}

	
	public boolean ok_actionPerformed(ActionEvent e) {
		
		String in = neurons2ConnectInput.getText();

		if (in!=null && in.trim().length()>0) {
		
			Set<String> resultStr=new LinkedHashSet<String>();
		
			resultStr.addAll(Arrays.asList((in.split("\\s*,\\s*"))));
			
			if (resultStr.size()==0) {
				JOptionPane.showMessageDialog(this, "No neurons to connect specified: "+neurons2ConnectInput.getText());
				return false;
			}
		
			 LinkedHashSet<Display2DOptions.NeuronPair> result=new LinkedHashSet<Display2DOptions.NeuronPair>();
		
			for(String pairStr:resultStr) {
				String [] pair=pairStr.split("\\s*:\\s*");
				
				if (pair.length!=2) {
					JOptionPane.showMessageDialog(this, "Invalid format "+pairStr);
					return false;
				}
				
				Integer from;
				Integer to;
				
				try {
					from=new Integer(pair[0]);
				} catch (Throwable e1) {
					JOptionPane.showMessageDialog(this, "Not an integer "+pair[0]);
					return false;
				}
				
				try {
					to=new Integer(pair[1]);
				} catch (Throwable e1) {
					JOptionPane.showMessageDialog(this, "Not an integer "+pair[1]);
					return false;
				}

				
				Integer[] fromXYZ=EDatabase.getObjectXYZ(from); 
				if (fromXYZ==null) {
					JOptionPane.showMessageDialog(this, "Neuron "+from+" not found in display2 table");
					return false;
				}
				
				Integer[] toXYZ=EDatabase.getObjectXYZ(to); 
				if (toXYZ==null) {
					JOptionPane.showMessageDialog(this, "Neuron "+to+" not found in display2 table");
					return false;
				}
			
				result.add(new Display2DOptions.NeuronPair(from,to));
				
			}
		
		
			Elegance.display2DOptions.setNeurons2Connect(result);
		} else {
			Elegance.display2DOptions.setNeurons2Connect(null);
		}
		
		
		String actionCommand = "";
		ButtonModel continFilterButton = continFilterGroup.getSelection();
		if (continFilterButton != null) {
			actionCommand = continFilterButton.getActionCommand();

			Elegance.display2DOptions.setContinFilterType(Display2DOptions.ContinFilterType.valueOf(Display2DOptions.ContinFilterType.class,actionCommand));

			if (Elegance.display2DOptions.getContinFilterType() == Display2DOptions.ContinFilterType.custom_number) {
				Elegance.display2DOptions.setContinFilterCustom(extract(continNumberInput.getText()));
			} else if (Elegance.display2DOptions.getContinFilterType() == Display2DOptions.ContinFilterType.custom_name) {
				Elegance.display2DOptions.setContinFilterCustom(extract(continNameInput.getText()));
			} else if (Elegance.display2DOptions.getContinFilterType() == Display2DOptions.ContinFilterType.custom_color_code) {
				Elegance.display2DOptions.setContinFilterCustom(extract(continColorInput.getText()));
			}

		} else {
			throw new IllegalStateException("contin filter not selected.");//unreachable code
		}

		Elegance.display2DOptions.setSynapseType(Display2DOptions.SynapseType.get((String)synapsesCombo.getSelectedItem()));
		Elegance.display2DOptions.setDtype((String)dtypeCombo.getSelectedItem());
		Elegance.display2DOptions.setZoom(new Integer((String)zoomCombo.getSelectedItem()));

		
		return true;
	}
	
	private JRadioButton add(String label, Display2DOptions.ContinFilterType type,  JTextField text,JPanel panel) {
		JRadioButton button = new JRadioButton(label);

		if (Elegance.display2DOptions.getContinFilterType().equals(type)) {
			button.setSelected(true);
			text.setText(EString.join(Elegance.display2DOptions.getContinFilterCustom(),","));
		}

		continFilterGroup.add(button);
		button.setActionCommand(type.name());
		panel.add(button);
		panel.add(text);
		return button;
	}

	
	
	public JButton getOKButton() {
		return this.ok;
	}
	

	private void cancel_actionPerformed(ActionEvent e) {
		this.hide();
	}
	
	private static Set<String> extract(String in){
		if (in==null || in.trim().length()==0) return new HashSet<String>();
		
		Set<String> result=new HashSet<String>();
		
		result.addAll(Arrays.asList((in.split("\\s*,\\s*"))));
		
		return result;
	}
	
	
		
	
	
	
}