package Admin;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Locale;
import javax.media.j3d.PhysicalBody;
import javax.media.j3d.PhysicalEnvironment;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.media.j3d.ViewPlatform;
import javax.media.j3d.VirtualUniverse;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.universe.SimpleUniverse;

/**
 * This creates a rotation interpolator and applies it to a shape.
 * 
 * @author I.J.Palmer
 * @version 1.0
 */
public class Rotator extends Frame implements ActionListener {
  GraphicsConfiguration config= SimpleUniverse.getPreferredConfiguration();
  protected Canvas3D myCanvas3D = new Canvas3D(config);

  protected Button exitButton = new Button("Exit");

  /**
   * This function builds the view branch of the scene graph. It creates a
   * branch group and then creates the necessary view elements to give a
   * useful view of our content.
   * 
   * @param c
   *            Canvas3D that will display the view
   * @return BranchGroup that is the root of the view elements
   */
  protected BranchGroup buildViewBranch(Canvas3D c) {
    BranchGroup viewBranch = new BranchGroup();
    Transform3D viewXfm = new Transform3D();
    viewXfm.set(new Vector3f(0.0f, 0.0f, 10.0f));
    TransformGroup viewXfmGroup = new TransformGroup(viewXfm);
    ViewPlatform myViewPlatform = new ViewPlatform();
    PhysicalBody myBody = new PhysicalBody();
    PhysicalEnvironment myEnvironment = new PhysicalEnvironment();
    viewXfmGroup.addChild(myViewPlatform);
    viewBranch.addChild(viewXfmGroup);
    View myView = new View();
    myView.addCanvas3D(c);
    myView.attachViewPlatform(myViewPlatform);
    myView.setPhysicalBody(myBody);
    myView.setPhysicalEnvironment(myEnvironment);
    return viewBranch;
  }

  /**
   * Add some lights so that we can illuminate the scene. This adds one
   * ambient light to bring up the overall lighting level and one directional
   * shape to show the shape of the objects in the scene.
   * 
   * @param b
   *            BranchGroup that the lights are to be added to.
   */
  protected void addLights(BranchGroup b) {
    BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0),
        100.0);
    Color3f ambLightColour = new Color3f(0.5f, 0.5f, 0.5f);
    AmbientLight ambLight = new AmbientLight(ambLightColour);
    ambLight.setInfluencingBounds(bounds);
    Color3f dirLightColour = new Color3f(1.0f, 1.0f, 1.0f);
    Vector3f dirLightDir = new Vector3f(-1.0f, -1.0f, -1.0f);
    DirectionalLight dirLight = new DirectionalLight(dirLightColour,
        dirLightDir);
    dirLight.setInfluencingBounds(bounds);
    b.addChild(ambLight);
    b.addChild(dirLight);
  }

  /**
   * This builds the content branch of our scene graph. The root of the shapes
   * supplied as a parameter is slightly tilted to reveal its 3D shape. It
   * also uses the addLights function to add some lights to the scene. We also
   * create the alpha generator and the rotation interpolator to perform the
   * animation.
   * 
   * @param shape
   *            Node that represents the geometry for the content
   * @return BranchGroup that is the root of the content branch / protected
   *         BranchGroup buildContentBranch(Node shape) { BranchGroup
   *         contentBranch = new BranchGroup(); //Create the transform and
   *         group used for the rotation Transform3D rotateCube = new
   *         Transform3D( ); TransformGroup rotationGroup = new
   *         TransformGroup(rotateCube); //Set the capability so we can write
   *         the transform rotationGroup.setCapability(
   *         TransformGroup.ALLOW_TRANSFORM_WRITE); //Create the alpha
   *         generator Alpha rotationAlpha = new Alpha(
   *         -1,Alpha.INCREASING_ENABLE,0,0,4000,0,0,0,0,0); //Build the
   *         interpolator Transform3D yAxis = new Transform3D();
   *         RotationInterpolator rotator = new
   *         RotationInterpolator(rotationAlpha, rotationGroup, yAxis, 0.0f,
   *         (float) Math.PI*2.0f); BoundingSphere bounds = new
   *         BoundingSphere(new Point3d(0.0,0.0,0.0), 100.0);
   *         rotator.setSchedulingBounds(bounds); //Put all this together
   *         contentBranch.addChild(rotationGroup);
   *         rotationGroup.addChild(shape); rotationGroup.addChild(rotator);
   *         addLights(contentBranch); return contentBranch; } /** This
   *         creates the shape used in the program.
   * @return Node that is the switch node / protected Node buildShape() {
   *         Appearance app = new Appearance(); Color3f ambientColour = new
   *         Color3f(1.0f,0.0f,0.0f); Color3f emissiveColour = new
   *         Color3f(0.0f,0.0f,0.0f); Color3f specularColour = new
   *         Color3f(1.0f,1.0f,1.0f); Color3f diffuseColour = new
   *         Color3f(1.0f,0.0f,0.0f); float shininess = 20.0f;
   *         app.setMaterial(new Material(ambientColour,emissiveColour,
   *         diffuseColour,specularColour,shininess)); return new Box(2.0f,
   *         2.0f, 2.0f, app); } /** Process the button action to exit the
   *         program.
   */
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == exitButton) {
      dispose();
      System.exit(0);
    }
  }

  public Rotator() {
    VirtualUniverse myUniverse = new VirtualUniverse();
    Locale myLocale = new Locale(myUniverse);
    myLocale.addBranchGraph(buildViewBranch(myCanvas3D));
    //  myLocale.addBranchGraph(buildContentBranch(buildShape()));
    setTitle("SimpleRotator");
    setSize(400, 400);
    setLayout(new BorderLayout());
    Panel bottom = new Panel();
    bottom.add(exitButton);
    add(BorderLayout.CENTER, myCanvas3D);
    add(BorderLayout.SOUTH, bottom);
    exitButton.addActionListener(this);
    setVisible(true);
  }

  public static void main(String[] args) {
    Rotator sr = new Rotator();
  }
}