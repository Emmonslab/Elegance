import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;

public class ProgressBarDemo extends JPanel
                             implements ActionListener, 
                                        PropertyChangeListener {

    private JProgressBar progressBar;
    private JButton startButton;
    private JTextArea taskOutput;
    private SwingWorker task;
    private String title;
   

	public ProgressBarDemo(SwingWorker task) {
		this(task, "Update Synapses");
	}
	
	public ProgressBarDemo(SwingWorker task, String title) {

		super(new BorderLayout());
		this.title = title;
		this.task = task;
		// Create the demo's UI.
		startButton = new JButton("Start");
		startButton.setActionCommand("start");
		startButton.addActionListener(this);

		progressBar = new JProgressBar(0, 100);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);

		taskOutput = new JTextArea(5, 20);
		taskOutput.setMargin(new Insets(5, 5, 5, 5));
		taskOutput.setEditable(false);

		JPanel panel = new JPanel();
		panel.add(startButton);
		panel.add(progressBar);

		add(panel, BorderLayout.PAGE_START);
		add(new JScrollPane(taskOutput), BorderLayout.CENTER);
		setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

	}

    /**
     * Invoked when the user presses the start button.
     */
    public void actionPerformed(ActionEvent evt) {
        startButton.setEnabled(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        //Instances of javax.swing.SwingWorker are not reusuable, so
        //we create new instances as needed.
        
        task.addPropertyChangeListener(this);
        task.execute();
        if (task.isDone()) done(); 
    }

    /**
     * Invoked when task's progress property changes.
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if ("progress" == evt.getPropertyName()) {
            int progress = (Integer) evt.getNewValue();
            progressBar.setValue(progress);
            taskOutput.append(String.format(
                   "Completed %d%% of task.\n", task.getProgress()));
            if (progress == 100) done();
        } 
    }


    /**
     * Create the GUI and show it. As with all GUI code, this must run
     * on the event-dispatching thread.
     */
   ;
   private JFrame frame;
   private boolean closeAfterDone;
   private void createAndShowGUI(boolean closeAftreDone) {
	   
        //Create and set up the window.
    	JFrame frame = new JFrame(this.title);
    	
        setLocation(500,300);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        JComponent newContentPane = new ProgressBarDemo(task, this.title,frame,closeAftreDone);
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
    
   private ProgressBarDemo(SwingWorker task, String title, JFrame frame, boolean closeAftreDone) {
	   this(task, title);
	   this.closeAfterDone=closeAftreDone;
	   this.frame=frame;
	   
   }
   
    public void done() {
        //Tell progress listener to stop updating progress bar.
        //done = true;
        Toolkit.getDefaultToolkit().beep();
        startButton.setEnabled(true);
        setCursor(null); //turn off the wait cursor
        progressBar.setValue(progressBar.getMinimum());
        taskOutput.append("Done!\n");
        
        //close progress bar if needed
        if (this.closeAfterDone) this.frame.dispose();
    }

    public void launch() {
    	launch(false);
    }
    public void launch(final boolean closeAfterDone) {
    	//this.task=task;
    	//startButton.addActionListener(this);
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(closeAfterDone);
            }
        });
    }
}