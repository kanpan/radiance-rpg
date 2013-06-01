package rad.screen;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.List;

import rad.Game;
import rad.util.ScreenListener;
import rad.util.ScreenManager;
import rad.util.Trace;

/**
 * This class handles the audio configuration.
 * @author Vieques
 *
 */

public class AudioConfigScreen extends List implements ScreenListener {
		/** Audio off index */
		public static final int AUDIO_OFF = 0;
		
		/** Audio on index */
		public static final int AUDIO_ON = 1;
		
		
		/**
		 * Constructor
		 */
		public AudioConfigScreen() {
			super("Audio?",List.EXCLUSIVE);
			
			super.append("Off", null);
			super.append("On",null);
			
			// if sound is on, change radio button default to On
			if(Game.isSound()) {
				super.setSelectedIndex(1, true);
			}
			
			super.addCommand(new Command("Go",Command.OK,1));
		}
		
		/**
		 * Processes buttons presses on the quit screen
		 * @param command Button calling us.
		 * @param smgr Screen manager to manipulate the display stack.
		 */
		public void process(Command command,ScreenManager smgr) {
			// Figure out which choice is selected
			Trace.print("AudioConfigScreen.process: invoked.");
			int index = super.getSelectedIndex();
			
			if(command.getCommandType() == Command.OK) {
				//	If NOT quiting, go back to the previous screen (whatever it is)
				if(index == AUDIO_ON) {
					Game.setSound(true);
				}
				else {
				// Otherwise, Set sound off
					Game.setSound(false);
				}
				//smgr.popTop();
				smgr.pop();
			}
			
			if(command.getCommandType() == Command.BACK) {
				//smgr.popTop();
				smgr.pop();
			}
		}

	}
