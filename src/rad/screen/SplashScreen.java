package rad.screen;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

import rad.Game;

import rad.util.DisplayState;
import rad.util.ScreenListener;
import rad.util.ScreenManager;

/**
 * This class renders the initial splash screen.
 * @author Ron, Joe Casey
 *
 */
public class SplashScreen extends Canvas implements Runnable, ScreenListener {
	protected final static String COPYRIGHT = "(c) 2007 by Aruba";
	
	protected final static String VERSION = "Version 0.1";
	
	protected final static int MAGIC_Y = 200;
	
	/** Number of timeout steps: 30 steps = 1 second */
	protected final static int TIMEOUT_STEPS = 120;
	
	/** Delay between splash frames -- one step */
	protected final static int FRAME_DELAY = 33;

	/** MG logo */
	protected Sprite logo;
	
	/** Screen manager for manipulating the screen stack */
	protected ScreenManager smgr;
	
	/** If true, the splash is running */
	protected boolean trucking = true;
	
	/** Constructor */
	public SplashScreen(ScreenManager smgr) {
		this.smgr = smgr;
		
		// This button breaks out of the splash
		addCommand(new Command("Continue",Command.OK,0));
		
		try {
			// Construct the sprite
			Image img = Image.createImage("/splash.png");
			
			logo = new Sprite(img,200,200);
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Processes a soft button.
	 * @param c Command soft button
	 * @param smgr Screen manager to manipulate the screen stack
	 */
	public void process(Command c,ScreenManager smgr) {
		// Go immediately to the splash screen
		try {
			trucking = false;
			
			goMainScreen();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/** Main loop for animating the splash screen */
	public void run( ) {
		int steps = 0;
		try {
			// Cycle for each frame of the splash animation
			while(trucking) {
				// Render the current frame of the logo
				repaint();
				
				// Sleep for this frame
				Thread.sleep(FRAME_DELAY);
				
				// Move logo to the next frame
				//logo.nextFrame();
				
				++steps;
				
				// If timeout, stop render loop
				if(steps >= TIMEOUT_STEPS)
					break;
			}
			
			// If we timeout, go to the main search screen
			if(trucking)
				goMainScreen();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/** Renders splash info to the graphics context */
	public void paint(Graphics g) {
		// Clear the screen
		int widthLogo = logo.getWidth();
		int heightLogo = logo.getHeight();
		
		int widthCanvas = getWidth();
		int heightCanvas = getHeight();

		g.setColor(143, 184, 194);
		g.fillRect(0, 0, widthCanvas, heightCanvas);
		
		// Position the logo and text
		int logoX = (widthCanvas - widthLogo) / 2;
		int logoY = (heightCanvas - heightLogo)  / 2;
	    
		logo.setPosition(logoX, logoY);
		
		logo.paint(g);
		
		// Set the drawing color to black
		g.setColor(0,0,0);

		g.drawString(COPYRIGHT,
				     getWidth() / 2,
				     MAGIC_Y,
				     Graphics.HCENTER | Graphics.TOP);
		
		g.drawString(VERSION, 
				     getWidth() / 2,
				     MAGIC_Y + g.getFont().getHeight()*3,
				     Graphics.HCENTER | Graphics.TOP);
		
		// Get a font
		Font f =
			Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_LARGE);
	    g.setFont(f);
	    
	    // Render the tile somewhere above the tmiddle of the screen
	    // and above the logo
	    int fontHeight = f.getHeight();		
		
	    g.drawString("Radiance RPG", getWidth() / 2, MAGIC_Y - 100/*logoX-fontHeight*/,
	    	      Graphics.HCENTER | Graphics.TOP);	

		
	
	}
	
	/** Goes to the search screen */
	protected void goMainScreen() {
		try {
			// Remove the splash screen from the stack
			smgr.popTop();
			
			// Push the search screen
			//smgr.push(new MainScreen(Game.emptyRS()),DisplayState.STATE_MAIN);
			Game.reset();
			
			smgr.push(new GameScreen(smgr), DisplayState.STATE_NEW_GAME);
		}
		
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}

