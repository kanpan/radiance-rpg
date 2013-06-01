package rad.screen;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;

import rad.util.DisplayState;
import rad.util.ScreenListener;
import rad.util.ScreenManager;
import rad.RadianceHUD;

import rad.Game;


public class GameScreen extends GameCanvas implements Runnable, ScreenListener {

	protected ScreenManager smgr;
	protected Game game;
	
	protected Command pauseCmd;
	protected Command resumeCmd;
	protected Command quitCmd;
	public Command menuCmd;
	public Command combatSelectCmd;
	public Command combatBackCmd;
	
	public Command textCmd;
	
	protected Command radarOn;
	protected Command radarOff;
	
	protected boolean done = false;
	public static boolean paused = false;
	
	protected final static int DELAY_FRAME = 33;
	
	protected double dur = DELAY_FRAME;
	
	protected double alpha = 2. / (DELAY_FRAME + 1);
	
	public GameScreen(/*ScreenManager smgr*/) {
		super(true);
	
		/*this.smgr = smgr;

		quitCmd = new Command("Quit", Command.EXIT, 0);
		pauseCmd = new Command("Pause", Command.STOP, 1);
		resumeCmd = new Command("Resume", Command.STOP, 1);
		
		addCommand(quitCmd);
		addCommand(pauseCmd);
		
		radarOn = new Command("Radar on",Command.ITEM,2);
		radarOff = new Command("Radar off",Command.ITEM,2);
		
		addCommand(radarOn);*/
		
		game = new Game(this);
		
		new Thread(this).start();		
	}
	
	public GameScreen(ScreenManager smgr) {
		super(true);
	
		this.smgr = smgr;

		menuCmd = new Command("Menu", Command.STOP, 0);
		textCmd = new Command("Scroll", Command.OK, 1);
		combatBackCmd = new Command("Back", Command.BACK, 0);
		combatSelectCmd = new Command("Select", Command.ITEM, 1);
		
		addCommand(menuCmd);
		
		/*radarOn = new Command("Radar on",Command.ITEM,2);
		radarOff = new Command("Radar off",Command.ITEM,2);
		
		addCommand(radarOn);*/
		
		game = new Game(this);
		
		new Thread(this).start();		
	}
	
	/**
	 * Processes screen button call backs.
	 * @param c Button calling.
	 * @param smgr Screen manager to manipulate the stack.
	 */
	public void process(Command c,ScreenManager smgr) {
		
		if(c == textCmd) {
			// text scrolling
			game.textPosition++;
		}
		
		if(c == menuCmd) {
			paused = true;
			try {
				smgr.push(new MenuScreen(smgr), DisplayState.STATE_PAUSE);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		if(c == combatSelectCmd) {
			RadianceHUD.toggleState();
		}
		
		if(c == combatBackCmd) {
			RadianceHUD.toggleStateBackward();
		}

		if(c == radarOn) {
			this.removeCommand(radarOn);
			addCommand(radarOff);
			game.enableRadar(true);
		}
		else if(c == radarOff) {
			this.removeCommand(radarOff);
			addCommand(radarOn);
			game.enableRadar(false);
		}
		
	}
	
	public void run() {
		smgr.getDisplay().setCurrent(this);

		// Compute the initial smoothing paramters

		
		// The main game loop
		try {
			while (!done) {
				int input = getKeyStates();
			
				long t0 = System.currentTimeMillis();
			
				if (!paused)
					game.loop(input);
			
				if(done)
					return;
			
				flushGraphics();
			
				long t1 = System.currentTimeMillis();
			
				long latency = t1 - t0;
			
				long delay = smooth(latency);
								
				Thread.sleep(delay);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}	
	
	/** Stops the game looop. */
	public void terminate() {
		// Stop the animation
		done = false;
	}

	
	/**
	 * Gets the graphics context for  screen.
	 * @return Graphics context
	 */
	public Graphics getGraphics() {
		return super.getGraphics();
	}
	
	/**
	 * Smooth latency using EMA.<p>
	 * See http://en.wikipedia.org/wiki/Moving_average.
	 * @param latency
	 * @return Smoothed latency
	 */
	protected long smooth(long latency) {
		long delay = DELAY_FRAME - latency;

		if(delay < 0)
			delay = 0;
		
		dur = alpha * delay + (1 - alpha) * dur;
		
		return (long) dur;
	}
	
	public void gameOver() {
		smgr.quit();
	}

}
