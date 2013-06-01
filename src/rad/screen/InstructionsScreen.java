package rad.screen;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Form;

import rad.util.ScreenListener;
import rad.util.ScreenManager;

/**
 * This class handles the instructions screen.
 * @author Ron, Aruba
 *
 */
public class InstructionsScreen extends Form implements ScreenListener {
	public InstructionsScreen() {
		super("Instructions");
		
		String msg = "Elemental storms sweep across Ceredan, wreaking havok and creating famine.";
		msg += "You will assume the character of Jodav, " +
				"an honorable member of the Sanctus Curatores, forever";
		msg += "vowed to protect the lands of Helim.";
		msg += "Use the arrow buttons to move Jodav.";
		msg += "When faced with foes, " +
				"use the buttons again to select your move to combat your adversaries.";
		msg += "Learn new spells and abilities as Jodav progresses through 10 levels of advancement";
		msg += "and use the Menu to equip your arsenal.";
		msg += "Be mindful of your HP (life) during battles, " +
				"and use Restore and towns to regain any health lost";
		msg += "Save Ceredan from the evil which grasps it and emerge radiantly victorious!";
		
		append(msg);
	}
	
	public void process(Command c,ScreenManager smgr) {
		smgr.pop();
	}
}
