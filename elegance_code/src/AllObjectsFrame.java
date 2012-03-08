import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Vector;

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
public class AllObjectsFrame extends JFrame {
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private JScrollPane jScrollPane1 = new JScrollPane();
	JTable jTable1 = new JTable();
	JButton refreshButton = new JButton();

	/**
	 * Creates a new AllObjectsFrame object.
	 */
	public AllObjectsFrame() {
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception {
		this.getContentPane().setLayout(gridBagLayout1);
		this.setSize(new Dimension(800, 200));
		this.setTitle("Objects");

		refreshButton.setText("Refresh");
		jScrollPane1.getViewport().add(jTable1, null);
		this.getContentPane().add(jScrollPane1,
				new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		this.getContentPane().add(refreshButton,
				new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	}

	/**
	 * Updates the data and displays the new data.
	 */
	public void updateAndDisplayFrame(Vector data) {
		jTable1.setModel(new AllObjectsTableModel(data));
		this.show();
	}
}
