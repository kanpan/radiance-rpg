package rad.screen;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;
import javax.microedition.lcdui.game.GameCanvas;

import java.util.Vector;

import rad.Game;
import rad.entity.Jodav;
import rad.entity.Stats;
import rad.util.DisplayState;
import rad.util.ScreenListener;
import rad.util.ScreenManager;

public class MenuScreen extends GameCanvas implements ScreenListener, Runnable {
	
	/** Delay between splash frames -- one step */
	protected final static int FRAME_DELAY = 100;
	
	/** Screen manager for manipulating the screen stack */
	protected ScreenManager smgr;
	protected Command selectCmd;
	protected Command backCmd;

	
	/* Menu States */
	protected static final int MENU_MAIN = 1;
	protected static final int MENU_STATUS = 2;
	protected static final int MENU_EQUIP = 3;
	protected static final int MENU_SAVE = 4;
	protected static final int MENU_QUIT = 5;
	protected static final int MENU_EQUIP_SELECT = 6;
	
	/* Titles */
	protected static final String[] titles = {
		"MENU","STATUS","EQUIP ABILITES","SAVE","QUIT","EQUIP ABILITES"
	};
	
	protected int state;
	
	protected int arrowPos = 0, arrowPos2 = 0, arrowPos3 = 0;
	
	/* Main menu choices as text */
	protected final static String CHOICE_CONTINUE = "Continue";
	protected final static String CHOICE_STATUS = "Status";
	protected final static String CHOICE_EQUIP = "Equip Abilities";
	protected final static String CHOICE_SAVE = "Save Game";
	protected final static String CHOICE_QUIT = "Quit Game";
	
	/* List of choices on main menu */
	protected String[] mainChoices = {
			CHOICE_CONTINUE,
			CHOICE_STATUS,
			CHOICE_EQUIP,
			CHOICE_SAVE,
			CHOICE_QUIT
	};
	
	/* List of choices on status/equip menu */
	protected String[] statChoices = {"Jodav","Calte"};
	protected String[] statChoices_onlyJodav = {"Jodav"};
	
	protected String [] choices;
	
	Vector currAbil;
	Vector unequipAbil = new Vector();
	
	private int currATK = 0, currDEF = 0, currMA = 0;
	
	int modATK = 0;
	int modDEF = 0;
	int modMA = 0;
	
	int list_start = 0;
	int list_numItems = 0;
	
	Sprite arrow, arrow2, arrow3, slot;
	
	/** Constructor */
	public MenuScreen(ScreenManager smgr) {
		super(true);
		
		this.smgr = smgr;
		
		backCmd = new Command("Back", Command.BACK,0);
		selectCmd = new Command("Select", Command.OK,0);
		addCommand(selectCmd);
		
		state = MENU_MAIN;
		choices = mainChoices;
		
		try {
			// Construct the sprite
			Image img = Image.createImage("/arrow.png");
			Image slot_img = Image.createImage("/slot.png");
			
			arrow = new Sprite(img,28,21);
			arrow2 = new Sprite(img,28,21);
			arrow2.setTransform(Sprite.TRANS_MIRROR);
			arrow3 = new Sprite(img,28,21);
			slot = new Sprite(slot_img,14,14);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		new Thread(this).start();
		
	}
	
	/**
	 * Processes a soft button.
	 * @param c Command soft button
	 * @param smgr Screen manager to manipulate the screen stack
	 */
	public void process(Command c,ScreenManager smgr) {
		if(c == selectCmd) {
			switch(state) {
			case MENU_MAIN: 
				if(arrowPos == 0) {
					smgr.pop();
					GameScreen.paused = false;
				}
				else if(arrowPos == 1) {
					state = MENU_STATUS;
					choices = statChoices;
					removeCommand(selectCmd);
					addCommand(backCmd);
				}
				else if(arrowPos == 2) {
					state = MENU_EQUIP;
					choices = statChoices;
					addCommand(backCmd);
				}
				else if(arrowPos == 3) {
					state = MENU_SAVE;
					//TODO save game
				}
				else if(arrowPos == 4) {
					state = MENU_QUIT;
					// TODO quit game => game over
				}
				break;
			case MENU_STATUS: break;
			case MENU_EQUIP: 
				state = MENU_EQUIP_SELECT;
	    		list_start = 0;
				break;
			case MENU_EQUIP_SELECT: 
				// This will swap in the unequipped ability.
				Jodav.setAbil(unequipAbil.elementAt(arrowPos3).toString(),arrowPos2);
				state = MENU_EQUIP;
				// Reset the Unequip Abil selection arrow
				arrowPos3 = 0;
				break;
			case MENU_SAVE: break;
			case MENU_QUIT: break;
			}
		}
		else if(c == backCmd) {
			if(state == MENU_EQUIP_SELECT) {
				state = MENU_EQUIP;
			}
			else {
				state = MENU_MAIN;
				choices = mainChoices;
				removeCommand(backCmd);
				addCommand(selectCmd);
				// Reset the ability selection arrows
				arrowPos2 = arrowPos3 = 0;
			}
		}
		arrowPos=0;
		
	}
	
	/** Main loop for animating the splash screen */
	public void run( ) {
		smgr.getDisplay().setCurrent(this);
		
		boolean done = false;
		
		try {
			while(!done) {
				
				int input = getKeyStates();
				update(input);

				// Render the current frame of the logo
				repaint();
				//flushGraphics();

				// Sleep for this frame
				Thread.sleep(FRAME_DELAY);
			}

		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void update(int input) {
		
		/* This populates the Unequipped Abilities list
		 * in such a way that abilities listed in the currently
		 * equipped ability list will be removed from it.
		 */
		unequipAbil.removeAllElements();
		currAbil = Jodav.getCurrAbil();
		String[] upequipAbil_raw = Stats.getAvailAbilities(Jodav.getCurrLv());
		for(int i=0; i<upequipAbil_raw.length; i++) {
			if(!currAbil.contains(upequipAbil_raw[i]))
				unequipAbil.addElement(upequipAbil_raw[i]);
		}
		
		/* This gathers the current base stats and modifies them
		 * based on the currently equipped abilities.
		 */
		currATK = Stats.getATK(Jodav.getCurrLv());
		currDEF = Stats.getDEF(Jodav.getCurrLv());
		currMA = Stats.getMA(Jodav.getCurrLv());
		
		for(int i=0; i<currAbil.size(); i++) {
			String abil = currAbil.elementAt(i).toString();
			currATK += Stats.getModATK(abil);
			currDEF += Stats.getModDEF(abil);
			currMA += Stats.getModMA(abil);
		}
		
		/* Input changes arrow positions */
		if(state == MENU_MAIN) {
			if ((input & GameCanvas.UP_PRESSED) != 0){
				if(arrowPos > 0) 
					arrowPos--;
			}
			else if ((input & GameCanvas.DOWN_PRESSED) != 0) {
				if(arrowPos < choices.length-1)
					arrowPos++;
			}
		}
		else if(state == MENU_STATUS || state == MENU_EQUIP ){
			if ((input & GameCanvas.LEFT_PRESSED) != 0){
				if(arrowPos > 0) 
					arrowPos--;
			}
			else if ((input & GameCanvas.RIGHT_PRESSED) != 0) {
				if(arrowPos < choices.length-1)
					arrowPos++;
			}
			if(state == MENU_EQUIP) {
				if ((input & GameCanvas.UP_PRESSED) != 0){
					if(arrowPos2 > 0) 
						arrowPos2--;
				}
				else if ((input & GameCanvas.DOWN_PRESSED) != 0) {
					if(arrowPos2 < Jodav.getCurrAbil().size()-1)
						arrowPos2++;
				}
				
				
			}
		}
		else if(state == MENU_EQUIP_SELECT) {
			if ((input & GameCanvas.UP_PRESSED) != 0){
				if(arrowPos3 > 0) {
					arrowPos3--;
					if(list_start == (arrowPos3+1) && list_start > 0)
						list_start--;
				}
			}
			else if ((input & GameCanvas.DOWN_PRESSED) != 0) {
				if(arrowPos3 < unequipAbil.size()-1) {
					arrowPos3++;
					if((arrowPos3-list_start) >= list_numItems )
						list_start++;
				}
			}
			
			/* These calculations are for displaying the 
			 * ATK, DEF, and MA differences at the bottom of
			 * the screen
			 */
			// First, subtract the stats associated with the ability
			// selected on the Equipped Abilities list
			int oldATK = currATK;
			int oldDEF = currDEF;
			int oldMA = currMA;
			String abil = currAbil.elementAt(arrowPos2).toString();
			oldATK -= Stats.getModATK(abil);
			oldDEF -= Stats.getModDEF(abil);
			oldMA -= Stats.getModMA(abil);
			
			int newATK = oldATK;
			int newDEF = oldDEF;
			int newMA = oldMA;
			
			// Second, add the stats associated with the ability
			// selected on the Unequipped Abilities list
			abil = unequipAbil.elementAt(arrowPos3).toString();
			newATK += Stats.getModATK(abil);
			newDEF += Stats.getModDEF(abil);
			newMA += Stats.getModMA(abil);

			// Third, find the difference for paint() to use
			modATK = newATK - currATK;
			modDEF = newDEF - currDEF;
			modMA = newMA - currMA;
			
		}
		
	}
	
	/** Renders splash info to the graphics context */
	public void paint(Graphics g) {
		
		int widthCanvas = getWidth();
		int heightCanvas = getHeight();
		
		int widthArrow = arrow.getWidth();
		int heightArrow = arrow.getHeight();
		
		int titleX = getWidth() / 2;
		int titleY = 10;
		
		int arrowX=0;
	    int arrowY=0;
		
		int choicesX = getWidth() / 2 - 5;
		int choicesY = 40;

		// navy blue
		// Clear the screen
		g.setColor(0, 78, 101);
		g.fillRect(0, 0, widthCanvas, heightCanvas);
		
		// Set the drawing color to white
		// Paint the menu text
		g.setColor(255,255,255);
		
		// Get a font
		Font f =
			Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_LARGE);
	    g.setFont(f);
	    
	    int fontHeight = f.getHeight();
	    
	    g.drawString(titles[state-1], titleX, titleY, Graphics.HCENTER | Graphics.TOP);	
	    
	    f = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_LARGE);
	    g.setFont(f);

	    if(state == MENU_MAIN) {
	    	choicesX = getWidth() / 2 - 5;
			choicesY = 40;
			
	    	for(int i=0; i<choices.length; i++) {
	    		g.drawString(choices[i], choicesX, choicesY+fontHeight*2*i, 
	    				Graphics.RIGHT | Graphics.TOP);
	    	}
	    	
	    	// Position the selection arrow
			arrowX = choicesX+10;
			arrowY = choicesY+fontHeight*2*arrowPos;
	    }
	    else if (state == MENU_STATUS || state == MENU_EQUIP 
	    		|| state == MENU_EQUIP_SELECT) {
	    	choicesX = 10;
	    	choicesY = 40;
	    	for(int i=0; i<choices.length; i++) {
	    		g.drawString(choices[i], choicesX+100*i, choicesY, 
	    				Graphics.LEFT | Graphics.TOP);
	    	}
	    	
	    	// Top Horizontal line
	    	g.fillRect(15, choicesY+fontHeight+10, widthCanvas-30, 1); 
	    	if(state == MENU_STATUS) {
	    		if(arrowPos == 0) {
	    			// Jodav selected. Display his stats.
	    			Vector stats = Jodav.getStats();
	    			for(int i=0; i<stats.size(); i++)
	    				g.drawString(stats.elementAt(i).toString(), choicesX+10, 
	    						choicesY+50+fontHeight*i, Graphics.LEFT | Graphics.TOP);
	    		}
	    		else if(arrowPos == 1) {
	    			// TODO Calte class
	    			// TODO Only show this if Calte is available
	    			// Calte selected. Display her stats.
	    			Vector stats = Jodav.getStats();
	    			for(int i=0; i<stats.size(); i++)
	    				g.drawString(stats.elementAt(i).toString(), choicesX+10, 
	    						choicesY+50+fontHeight*i, Graphics.LEFT | Graphics.TOP);
	    		}
	    	}
	    	else if(state == MENU_EQUIP || state == MENU_EQUIP_SELECT) {
	    		// Bottom Horizontal line
		    	g.fillRect(15, heightCanvas-fontHeight*3-10, widthCanvas-30, 1); 

		    	// Display stats at the bottom
		    	g.drawString("ATK: " + currATK, 30, heightCanvas-fontHeight*3-5,
		    			Graphics.LEFT | Graphics.TOP);
		    	g.drawString("DEF: " + currDEF, 30, heightCanvas-fontHeight*2-5,
		    			Graphics.LEFT | Graphics.TOP);
		    	g.drawString("MA: " + currMA, 30, heightCanvas-fontHeight-5,
		    			Graphics.LEFT | Graphics.TOP);

		    	// Display list headers
	    		g.drawString("Equipped", 10, choicesY+fontHeight+15, 
	    				Graphics.LEFT | Graphics.TOP);
	    		g.drawString("Unequipped", widthCanvas-10, choicesY+fontHeight+15, 
	    				Graphics.RIGHT | Graphics.TOP);
	    		
	    		// List Equipped Abilities
	    		for(int i=0; i<currAbil.size(); i++)
    				g.drawString(currAbil.elementAt(i).toString(), 10+widthArrow, 
    						choicesY+60+fontHeight*i, Graphics.LEFT | Graphics.TOP);
	    		// Equipped Abilities selection arrow
	    		arrow2.setPosition(5, choicesY+60+fontHeight*arrowPos2);
	    		arrow2.paint(g);

	    		// List Available Abilities
	    		//list_start = 0;
	    		list_numItems = Math.min(
	    				((heightCanvas-fontHeight*3-10)-(choicesY+60))/fontHeight, 
	    				unequipAbil.size());
	    		//for(int i=0; i<unequipAbil.size(); i++)
	    		for(int i=list_start; i<list_numItems+list_start; i++)
	    			g.drawString(unequipAbil.elementAt(i).toString(), widthCanvas-10-widthArrow, 
	    					choicesY+60+fontHeight*(i-list_start), Graphics.RIGHT | Graphics.TOP);
	    		
	    		if(state == MENU_EQUIP_SELECT) {
	    			// Unequipped Abilities selection arrow
	    			int graphicPos = arrowPos3 - list_start;
	    			arrow3.setPosition(widthCanvas-5-widthArrow, choicesY+60+fontHeight*graphicPos);
	    			arrow3.paint(g);
	    			
	    			
	    			
	    			// Display modified stats at the bottom
	    			String sign = "";
	    			if(modATK != 0) {
	    				if(modATK > 0) {
	    					// positive adjustments color green
	    					g.setColor(0,255,0);
	    					sign = "+";
	    				}
	    				else {
	    					// negative adjustments color red
	    					g.setColor(255,0,0);
	    					//sign = "-";
	    				}
	    				g.drawString(sign+modATK, 100, heightCanvas-fontHeight*3-5,
	    						Graphics.LEFT | Graphics.TOP);
	    			}
	    			if(modDEF != 0) {
	    				sign = "";
	    				if(modDEF > 0) {
	    					// positive adjustments color green
	    					g.setColor(0,255,0);
	    					sign = "+";
	    				}
	    				else {
	    					// negative adjustments color red
	    					g.setColor(255,0,0);
	    					//sign = "-";
	    				}
	    				g.drawString(sign+modDEF, 100, heightCanvas-fontHeight*2-5,
	    						Graphics.LEFT | Graphics.TOP);
	    			}
	    			if(modMA != 0) {
	    				sign = "";
	    				if(modMA > 0) {
	    					// positive adjustments color green
	    					g.setColor(0,255,0);
	    					sign = "+";
	    				}
	    				else {
	    					// negative adjustments color red
	    					g.setColor(255,0,0);
	    					//sign = "-";
	    				}
	    				g.drawString(sign+modMA, 100, heightCanvas-fontHeight-5,
	    						Graphics.LEFT | Graphics.TOP);
	    			}
	    		}
	    	}
	    	
	    	arrowX = choicesX+100*arrowPos+widthArrow*2;
	    	arrowY = choicesY;
	    }

		arrow.setPosition(arrowX, arrowY);
		
		arrow.paint(g);
	
	}

}
