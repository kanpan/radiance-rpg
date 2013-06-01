package rad.util;

import javax.microedition.lcdui.Command;


/**
 * This interface specifies behaviors for screens that listen
 * for soft buttons.
 * @author Ron
 *
 */
public interface ScreenListener {
	/**
	 * Processes a soft button
	 * @param c Button calling
	 * @param smgr Manager to manipulate the screen stack.
	 */
	public void process(Command c,ScreenManager smgr);
}
