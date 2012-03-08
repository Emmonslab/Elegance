import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
 
public class ImageBackground {
    public static void main(String[] args) {
        int size = 200;
        BufferedImage image1 = new BufferedImage(size,size,BufferedImage.TYPE_3BYTE_BGR);
        drawUpon(image1);
        BufferedImage image2 = new BufferedImage(size,size,BufferedImage.TYPE_4BYTE_ABGR);
        drawUpon(image2);
        BufferedImage image3 = new BufferedImage(size,size,BufferedImage.TYPE_3BYTE_BGR);
        setBackground(image3, new GradientPaint(0,0,Color.BLUE, 40,20, Color.GREEN, true));
        drawUpon(image3);
        BufferedImage image4 = new BufferedImage(size,size,BufferedImage.TYPE_4BYTE_ABGR);
        setBackground(image4, new GradientPaint(0,0, Color.BLUE, 40,20,
            new Color(0,0,0,0), true));
        drawUpon(image4);
 
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame f = new JFrame("ImageBackground");
        Container cp = f.getContentPane();
        cp.setBackground(Color.WHITE);
        cp.setLayout(new GridLayout(2,2));
 
        JLabel label = new JLabel(new ImageIcon(image1));
        label.setBorder(BorderFactory.createTitledBorder("opaque, no \"background\" set"));
        cp.add(label);
 
        label = new JLabel(new ImageIcon(image2));
        label.setBorder(BorderFactory.createTitledBorder("translucent, no \"background\" set"));
        cp.add(label);
 
        label = new JLabel(new ImageIcon(image3));
        label.setBorder(BorderFactory.createTitledBorder("opaque, with \"background\""));
        cp.add(label);
 
        label = new JLabel(new ImageIcon(image4));
        label.setBorder(BorderFactory.createTitledBorder("translucent, with \"background\""));
        cp.add(label);
 
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.pack();
        f.setLocationRelativeTo(null);
        f.show();
    }
 
    static void setBackground(BufferedImage image, Paint paint) {
        Graphics2D g = image.createGraphics();
        g.setPaint(paint);
        g.fillRect(0,0,image.getWidth(), image.getHeight());
        g.dispose();
    }
 
    static void drawUpon(BufferedImage image) {
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setPaint(Color.RED);
        int inset = image.getWidth()/5;
        g.fillOval(inset,inset,image.getWidth()-2*inset, image.getHeight()-2*inset);
        g.dispose();
    }
}
