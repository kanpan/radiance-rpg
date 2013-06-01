package rad.screen;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.List;

import rad.Game;
import rad.screen.InstructionsScreen;
import rad.util.DisplayState;

import rad.util.ScreenListener;
import rad.util.ScreenManager;

public class MainScreen extends List implements ScreenListener {

	protected final static String CHOICE_NEW_GAME = "New Game";
	protected final static String CHOICE_RESUME_GAME = "Resume Game";
	protected final static String CHOICE_OPTIONS = "Audio Options";
	protected final static String CHOICE_INSTRUCTIONS = "Instructions";
	protected final static String CHOICE_QUIT = "Quit";

	/**
	 * List of choices on with a "new game" option
	 */
	protected String[] choicesFresh = {
			CHOICE_NEW_GAME,
			CHOICE_OPTIONS,
			CHOICE_INSTRUCTIONS,
			CHOICE_QUIT
	};
	
	/** List of choices with a "resume game" option	 */
	protected String[] choicesResume = {
			CHOICE_RESUME_GAME,
			CHOICE_NEW_GAME,
			CHOICE_OPTIONS,
			CHOICE_INSTRUCTIONS,
			CHOICE_QUIT
	};

	/** Current state of choices  */
	protected String[] choices = null;
	

	
	protected GameScreen gameScreen;
	
	/**
	 * Constructor
	 * @param fresh If true there is no resume option.
	 */
	public MainScreen(boolean fresh) {
		super("Main",List.IMPLICIT);
		
		if(fresh)
			this.choices = choicesFresh;
		else
			this.choices = choicesResume;
		
		for(int j=0; j < choices.length; j++) {
			this.append(choices[j], null);
		}
		
		super.addCommand(new Command("Go",Command.OK,0));
	}
	
	/**
	 * Processes button presses on the main screen.
	 * @param Command c Command calling.
	 * @param smgr Screen manager controlling the display state
	 */
	public void process(Command c,ScreenManager smgr) {
		
		Game.reset();
		gameScreen = new GameScreen(smgr);
		try {
		smgr.push(gameScreen, DisplayState.STATE_NEW_GAME);
		} catch (Exception e) {
			System.err.println(e);
		}
		
		
		if (c.getCommandType() == Command.OK ) {
			// Figure out which choice is selected
			int index = super.getSelectedIndex();

			String choice = choices[index];

			try {
				// Create and render a sub-screen for that choice
				if(choice.equals(CHOICE_QUIT)) {
				//	smgr.pushBack(new QuitScreen(),DisplayState.STATE_QUIT);
				}
				else if(choice.equals(CHOICE_NEW_GAME) || 
						choice.equals(CHOICE_RESUME_GAME)) {
					Game.reset();

					if(choice.equals(CHOICE_RESUME_GAME))
						Game.restoreGameState();

					gameScreen = new GameScreen(smgr);
				}
				else if(choice.equals(CHOICE_INSTRUCTIONS)) {
					smgr.pushBack(new InstructionsScreen(), DisplayState.STATE_INSTRUCTIONS);
				}
				else if(choice.equals(CHOICE_OPTIONS)) {
					smgr.pushBack(new AudioConfigScreen(), DisplayState.STATE_OPTIONS);
				}
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	protected void paint(Graphics arg0) {
		// TODO Auto-generated method stub

	}

}
