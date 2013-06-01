package rad.util;

/**
 * This class provides debug helper methods.
 * @author Ron
 *
 */
public class Trace {
	public static final boolean DEBUG = true;
	
	/**
	 * Prints a message on the diagnostic control
	 * @param msg
	 */
	public static void print(String msg) {
		if(DEBUG == false)
			return;
		
		System.out.println("TRACE--"+msg);
	}
}
