/**
 * This class is the entry point into the Image Stitching module. It implements the
 * Runnable interface and hence provides multithreading capability.
 *
 * @author zndavid
 * @version 1.0
 */
public class ImageStitchApp
	implements Runnable
{
	/**
	 * The method which implements the Runnable interface.
	 */
	public void run (  )
	{
		//new ImageStitchFrame("d:\\david\\worm_images\\PAG770L.tif");
		new ImageStitchFrame(  );
	}

	/**
	 * The entry point for the ImageStitch application. This function is not used in the
	 * final application and can be used if some kind of testinghas to be done.
	 *
	 * @param args The command line arguments
	 */
	public static void main ( String [] args )
	{
		ImageStitchApp imageStitchApp = new ImageStitchApp(  );
		Thread         t = new Thread( imageStitchApp );
		t.start (  );
	}
}
