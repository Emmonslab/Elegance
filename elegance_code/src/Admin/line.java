package Admin;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class line extends JFrame
{
    private CanvasDisplay cavas;
    public line()
    {
        Container content=getContentPane();
        cavas=new CanvasDisplay();
        content.setLayout(new FlowLayout());
        content.add(cavas);
        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                System.exit(0);
            }
        });
        pack();
        show();
    }
    public static void main(String args[])
    {
        new line();
    }
    class CanvasDisplay extends Canvas
    {
        public CanvasDisplay()
        {
            setBackground(Color.white); 
            setForeground(Color.blue);
            setSize(600,600);
        }
        public void paint(Graphics g)
        {
            Graphics2D g2D=(Graphics2D) g;
            
            //move the X and Y 
            g2D.translate(100,100);
            
            //rotate the line
            g2D.rotate(5,200,200);
            
            //fangda the line de bashu
            g2D.scale(1,1);
            
            //set the line's width
            BasicStroke stroke=new BasicStroke(5);
            g2D.setStroke(stroke);
            drawhome(g2D);
        }
        public void drawhome(Graphics2D g2D)
        {
            Line2D line1=new Line2D.Float(100f,200f,200f,200f),
                   line2=new Line2D.Float(100f,200f,100f,100f),
                   line3=new Line2D.Float(100f,100f,150f,50f),
                   line4=new Line2D.Float(150f,50f,200f,100f),
                   line5=new Line2D.Float(200f,100f,200f,200f),
                   line6=new Line2D.Float(140f,200f,140f,150f),
                   line7=new Line2D.Float(140f,150f,160f,150f),
                   line8=new Line2D.Float(160f,150f,160f,200f);
                   
            g2D.draw(line1);
            g2D.draw(line2);
            g2D.draw(line3);
            g2D.draw(line4);
            g2D.draw(line5);
            g2D.draw(line6);
            g2D.draw(line7);
            g2D.draw(line8);
        }
    }   
}

