package rad.util;

import java.util.Vector;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;

import rad.RadianceMIDlet;

/**
 * This class provides utility methods to help manage the screen of
 * the midlet.
 * @author Ron
 *
 */
public class ScreenManager {
	protected RadianceMIDlet midlet;
	protected CommandListener listener;
	protected Vector backtab = new Vector();
	
	/**
	 * Constructor.
	 * @param midlet Midlet
	 * @param listener Command listener for the midlet.
	 */
	public ScreenManager(RadianceMIDlet midlet,CommandListener listener) {
		this.midlet = midlet;
		this.listener = listener;
	}
	
	/**
	 * Pushes a displyable on the stack with a state.
	 * Displyable must set its own soft buttons.
	 * @param d Displayable
	 * @param state Display state
	 * @see zambezi.display.DisplayState
	 */
	public void push(Object disp,int state) throws Exception {
		if(!(disp instanceof ScreenListener)
				|| !(disp instanceof Displayable))
			throw new Exception("bad argument");
		
		Displayable d = (Displayable)disp;
		d.setCommandListener(listener);
		Display.getDisplay(midlet).setCurrent(d);
		backtab.addElement(new DisplayState(d,state));
	}
	
	/**
	 * Pushes a "BACK" displayable on the stack with a state.
	 * BACK soft button will be first button (or highest priority).
	 * @param d Displyable
	 * @param state 
	 */
	public void pushBack(Displayable d,int state) throws Exception {
		d.addCommand(new Command("Back",Command.BACK,0));
		push(d,state);
	}
	
	/**
	 * Sets the display with a CANCEL soft button. (The
	 * displayable cannot be popped.)
	 * @param d Displayable.
	 */
	public void setCancel(Displayable d) {
		d.addCommand(new Command("Cancel",Command.CANCEL,0));
		d.setCommandListener(listener);
		Display.getDisplay(midlet).setCurrent(d);		
	}
	
	/**
	 * Pops a display state and renders the top.
	 */
	public void pop() {
		popTop();
		
		restore();
	}
	
	/**
	 * Pops a display state but does not render the top.
	 *
	 */
	public void popTop() {
		DisplayState ds = (DisplayState) backtab.lastElement();
		
		backtab.removeElement(ds);		
	}
	
	/**
	 * Restores what screen is at the top of the stack.
	 *
	 */
	public void restore() {
		DisplayState ds = (DisplayState) backtab.lastElement();
		
		Displayable d = ds.getDisplayable();
			
		Display.getDisplay(midlet).setCurrent(d);
	}
	
	/**
	 * Gets the current display state, i.e., the one at the stack top.
	 * @return
	 */
	public DisplayState getScreenState() {
		return (DisplayState) backtab.lastElement();
	}
	
	/**
	 * Gets the displayable as a listener.
	 * @return The displayable as a listener.
	 */
	public ScreenListener getListener() {
		return ((DisplayState) backtab.lastElement()).getListener();
	}
	
	/**
	 * Invokes the midlet to terminate the application
	 *
	 */
	public void quit() {
		midlet.quit();
	}
	
	/**
	 * Gets the display for the midlet.
	 * @return The display
	 */
	public Display getDisplay() {
		return Display.getDisplay(midlet);
	}
}
