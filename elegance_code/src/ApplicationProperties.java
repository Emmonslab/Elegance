/**
 * This class stores some properties that are used in the application. These
 * properties are static and public and so can be accessed and changed from
 * anywhere in the application. This class cannot be instantiated because of the
 * private constructor.
 * 
 * @author zndavid
 * @version 1.0
 */
public class ApplicationProperties {
	/** if set to true, the objects are shown. Otherwise they are not drawn */
	public static boolean showObjects = true;

	/**
	 * if set to true, all the relations are shown. Otherwise they are not drawn
	 */
	public static boolean showAllRelations = true;

	/**
	 * if set to true, the continuous relations are shown. Otherwise they are
	 * not drawn
	 */
	public static boolean showContinuousRelations = true;

	/**
	 * if set to true, the presynaptic relations are shown. Otherwise they are
	 * not drawn
	 */
	public static boolean showPresynapticRelations = true;

	/**
	 * if set to true, the postsynaptic relations are shown. Otherwise they are
	 * not drawn
	 */
	public static boolean showPostsynapticRelations = true;

	/**
	 * if set to true, the gap relations are shown. Otherwise they are not drawn
	 */
	public static boolean showGapRelations = true;

	/**
	 * if set to true, the multiple presynaptic relations are shown. Otherwise
	 * they are not drawn
	 */
	public static boolean showMultPresynapticRelations = true;

	/**
	 * if set to true, the multiple postsynaptic relations are shown. Otherwise
	 * they are not drawn
	 */
	public static boolean showMultPostSynapticRelations = true;

	/** Stores information about whether the stitch frame is locked or not. */
	public static boolean stitchFrameLocked = false;

	private ApplicationProperties() {
	}
}
