
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class TwoDJFrame extends JFrame {

    /** Creates new form NewFrame */
    public TwoDJFrame() {
        
    }
    
    void init(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setBounds(10, 10, 1194, 768);
        int[] ii = {922};
        New2D nn = new New2D(ii);
        this.add(nn);
        Control2D cc = new Control2D(nn, this);
        this.add(cc);
        nn.setBounds( 0, 0,1024, 768);
        cc.setBounds( 1024, 0,170, 768);
    }

  
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                TwoDJFrame f = new TwoDJFrame();
                f.init();
                f.setVisible(true);
            }
        });
    }

              

}
