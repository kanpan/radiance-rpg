package rad.npc;

import rad.Game;
import java.util.Vector;

public class Speech {

	/**
	 * 
	 * @param zonenum Current Zone Number
	 * @param npc_id The ID of the NPC in the zone, determined from the
	 * upper left down to lower right corners
	 * @param story_state Current story state
	 * @return Vector used to produce scrolling text in Game.render()
	 */
	public static Vector getSpeech(int zonenum, int npc_id, int story_state) {
		/**
		 * Text does NOT automatically wrap around and scroll.
		 * Long messages need to be broken up into separate lines
		 * and then loaded into a Vector.
		 */
		Vector msg = new Vector();
		// Rinj
		if(zonenum == 15) {
			if(story_state == Game.GAME_STORY_NEW) {
				msg.addElement("Jodav! With the recent storms playing");
				msg.addElement("havoc with Helim's farmers, a band of");
				msg.addElement("thieves have started pillaging homes");
				msg.addElement("and stealing everything. We, the ");
				msg.addElement("Sanctus Curatores, the Protectors of");
				msg.addElement("Helim, need to put an end to this! ");
				msg.addElement("Defeat the rogues who are camping ");
				msg.addElement("out in Rinj Woods directly EAST of ");
				msg.addElement("here. I hope you remember your ");
				msg.addElement("training. Don't forget to equip your");
				msg.addElement("abilites from the Menu. You will get ");
				msg.addElement("more as you defeat your foes.");
				msg.addElement("May Ceredan guide you!");
			}
			else if(story_state == Game.GAME_STORY_ENROUTE_RINJ) {
				msg.addElement("Jodav, the thieves you want to stop ");
				msg.addElement("are directly EAST of here in Rinj ");
				msg.addElement("Woods. Head outside and into the ");
				msg.addElement("forest.");
			}
			else if(story_state == Game.GAME_STORY_DEFEATED_RINJ) {
				msg.addElement("Well Done Jodav! What's that? You " +
					"found a strange, yellow crystal? " +
					"There is a sage in the town of Jaiha " +
					"who might be able to tell you about" +
					"this. Jaiha is SOUTH of Rinj, along" +
					"the coast. It's further than Rinj Woods," +
					"so don't get discouraged.");
			}
			else if(story_state == Game.GAME_STORY_ENROUTE_JAIHA) {
				msg.addElement("You will find Jaihi along the coast " +
					"SOUTH of here. Look for an old man who" +
					"will help you.");
			}
			else
				msg.addElement("May Ceredan guide you!");
		}
		// Jaihi
		else if(zonenum == 59) {
			
		}
		// Ebevet
		else if(zonenum == 5) {
			
		}
		else msg = null;
		
		return msg;
	}
}
