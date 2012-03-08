package Admin;
import java.awt.*;
import javax.swing.JFrame;

public class ImageViewer extends JFrame {
	private Image image;


	public ImageViewer(String fileName) {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		image = toolkit.getImage(fileName);
		MediaTracker mediaTracker = new MediaTracker(this);
		mediaTracker.addImage(image, 0);
		try
		{
			mediaTracker.waitForID(0);
		}
		catch (InterruptedException ie)
		{
			System.err.println(ie);
		}
		
		setSize(image.getWidth(null)+30, image.getHeight(null)+30);
		setTitle(fileName);
		
	}

	public void paint(Graphics graphics) {
		graphics.drawImage(image, 30, 30, null);
	}

	
}
