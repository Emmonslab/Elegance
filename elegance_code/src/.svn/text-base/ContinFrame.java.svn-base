import javax.swing.JFrame;
import javax.swing.JTable;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;

public class ContinFrame extends JFrame 
{
    private GridBagLayout gridBagLayout1 = new GridBagLayout();
    private JButton refreshButton = new JButton();
    private JScrollPane jScrollPane1 = new JScrollPane();
    private JTable jTable1 = new JTable(new ContinTableModel());

    public ContinFrame()
    {
        try
        {
            jbInit();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    private void jbInit() throws Exception
    {
        this.getContentPane().setLayout(gridBagLayout1);
        this.setTitle("Contins Window");
        this.setSize(400,300);
        refreshButton.setText("Refresh");
        refreshButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    refreshButton_actionPerformed(e);
                }
            });
        this.getContentPane().add(refreshButton, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        jScrollPane1.getViewport().add(jTable1, null);
        this.getContentPane().add(jScrollPane1, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    }

    private void refreshButton_actionPerformed(ActionEvent e)
    {
        jTable1 = new JTable(new ContinTableModel());
        //this.removeAll();
        //this.getContentPane().setLayout(gridBagLayout1);
        //this.getContentPane().add(refreshButton, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        jScrollPane1.getViewport().removeAll();
        jScrollPane1.getViewport().add(jTable1, null);
        //this.getContentPane().add(jScrollPane1, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        this.setVisible(true);
        this.repaint();
    }
}