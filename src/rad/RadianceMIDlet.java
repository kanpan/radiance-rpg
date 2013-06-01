package rad;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import rad.util.ScreenListener;
import rad.util.Trace;

import rad.util.ScreenManager;
import rad.util.DisplayState;

import rad.screen.GameScreen;
import rad.screen.SplashScreen;

/**
 * 
 */

/**
 * @author Joe
 *
 */
public class RadianceMIDlet extends MIDlet implements CommandListener {

	protected ScreenManager smgr = new ScreenManager(this,this);
	
	/* (non-Javadoc)
	 * @see javax.microedition.midlet.MIDlet#destroyApp(boolean)
	 */
	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see javax.microedition.midlet.MIDlet#pauseApp()
	 */
	protected void pauseApp() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see javax.microedition.midlet.MIDlet#startApp()
	 */
	protected void startApp() throws MIDletStateChangeException {
		SplashScreen splashScreen = new SplashScreen(smgr);
		
		try {
			// Push it on the screen stack and display it
			smgr.push(splashScreen, DisplayState.STATE_MAIN);
			new Thread(splashScreen).start();
		}
		catch(Exception e) {
			e.printStackTrace();
			throw new MIDletStateChangeException();
		}

	}

	/**
	 * Receives soft button callbacks.
	 * @param c Command calling
	 * @param d Displayable calling
	 */
	public void commandAction(Command c,Displayable d) {
		Trace.print("commandAction: invoked top screen="+smgr.getScreenState().getState());
		ScreenListener listener = smgr.getListener();
		listener.process(c,smgr);
	}
	
	/**
	 * Quits the midlet in a controlled manner.
	 */
	public void quit() {
		try {
			destroyApp(true);
		}
		catch(Exception e) {
			
		}
		notifyDestroyed();
	}

}
