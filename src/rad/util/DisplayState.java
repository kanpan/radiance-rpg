package rad.util;

import javax.microedition.lcdui.Displayable;


/**
 * This class is a container of display state information which
 * consists of a Displayable and an integer state.
 * @author Ron
 *
 */
public class DisplayState {
	protected static int code = 1;

	/** Don't care screen */
	public final static int DONTCARE = code++;
	
	/** Splash state screen */
	public final static int STATE_SPLASH = code++;
	
	/** Main state screen */
	public final static int STATE_MAIN = code++;
	
	/** New game state screen */
	public final static int STATE_NEW_GAME = code++;
	
	/** Resume state screen */
	public final static int STATE_RESUME = code++;
	
	/** Options state screen */
	public final static int STATE_OPTIONS = code++;
	
	/** Instructions state screen */
	public final static int STATE_INSTRUCTIONS = code++;
	
	/** Quit state screen */
	public final static int STATE_QUIT = code++;
	
	/** Audio config state screen */
	public final static int STATE_CONFIG_AUDIO = code++;
	
	/** High score config state screen */
	public final static int STATE_CONFIG_HIGH_SCORES = code++;
	
	/** Pause state screen */
	public final static int STATE_PAUSE = code++;
	
	/** Game over state screen */
	public final static int STATE_GAME_OVER = code++;
	
	/** Skill level config state screen */
	public final static int STATE_CONFIG_SKILL = code++;
	
	protected Object disp;
	protected int state;
	
	/**
	 * Constructor
	 * @param d Displayable
	 * @param state State (see final statics above)
	 */
	public DisplayState(Displayable disp,int state) {
		this.disp = disp;
		this.state = state;
	}

	/**
	 * Gets the Displayable component of the display state.
	 * @return Displayable
	 */
	public Displayable getDisplayable() {
		return (Displayable)disp;
	}
	
	public ScreenListener getListener() {
		return (ScreenListener) disp;
	}

	/**
	 * Sets the Displayable component of the the display state.
	 * @param d Displyable
	 */
	public void setDisplayable(Displayable disp) {
		this.disp = disp;
	}

	/**
	 * Gets the state component of the display state
	 * @return
	 */
	public int getState() {
		return state;
	}

	/**
	 * Sets the state component of the display state.
	 * @param state
	 */
	public void setState(int state) {
		this.state = state;
	}
}

