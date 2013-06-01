package rad.entity;

public class Stats {
	
	/* Abilities */
	/* level 1 */
	public final static String FROST_1 = "Frost 1";
	public final static String RESTORE_1 = "Restore 1";
	public final static String ASSAULT_1 = "Assault 1";
	/* level 2 */
	public final static String FIRE_1 = "Fire 1";
	public final static String THRUST_1 = "Thrust 1";
	/* level 3 */
	public final static String RESTORE_2 = "Restore 2";
	public final static String INFUSE_1 = "Infuse 1";
	/* level 4 */
	public final static String DARK_1 = "Dark 1";
	public final static String ASSAULT_2 = "Assault 2";
	/* level 5 */
	public final static String FROST_2 = "Frost 2";
	public final static String FIRE_2 = "Fire 2";
	/* level 6 */
	public final static String RESTORE_3 = "Restore 3";
	public final static String INFUSE_2 = "Infuse 2";
	/* level 7 */
	public final static String THRUST_2 = "Thrust 2";
	/* level 8 */
	public final static String DARK_2 = "Dark 2";
	public final static String ASSAULT_3 = "Assualt 3";
	/* level 9 */
	public final static String FROST_3 = "Frost 3";
	public final static String RESTORE_4 = "Restore 4";
	/* level 10 */
	public final static String FIRE_3 = "Fire 3";
	public final static String INFUSE_3 = "Infuse 3";
	public final static String THRUST_3 = "Thrust 3";
	
	public static String[] abilityList = new String [] {
		/* level 1 */	FROST_1,RESTORE_1,ASSAULT_1,
		/* level 2 */	FIRE_1,THRUST_1,
		/* level 3 */	RESTORE_2,INFUSE_1,
		/* level 4 */	DARK_1,ASSAULT_2,
		/* level 5 */	FROST_2,FIRE_2,
		/* level 6 */	RESTORE_3,INFUSE_2,
		/* level 7 */	THRUST_2,
		/* level 8 */	DARK_2,ASSAULT_3,
		/* level 9 */	FROST_3,RESTORE_4,
		/* level 10 */	FIRE_3,INFUSE_3,THRUST_3};


	public static int getMaxHP(int level) {
		switch(level) {
		case 1: return 340;
		case 2: return 418;
		case 3: return 496;
		case 4: return 564;
		case 5: return 588;
		case 6: return 652;
		case 7: return 730;
		case 8: return 808;
		case 9: return 886;
		case 10: return 964;
		default: return 340;
		}
	}
	
	public static int getMaxMP(int level) {
		return 13+level*23;
	}
	
	public static int getMaxXP(int level) {
		return 50+level*50;
	}
	
	public static int getATK(int level) {
		return 20+level*4;
	}
	
	public static int getDEF(int level) {
		return 16+level*2;
	}
	
	public static int getMA(int level) {
		return 5+level*2;
	}
	
	/** the size of array is a cumulative total of the
	 * number of abilities the PC has at the level
	 * 
	 * @param level
	 * @return array of available abilities
	 */
	public static String[] getAvailAbilities(int level) {
		
		String[] abil;
		
		switch(level) {
		case 1: abil = new String[3]; break;
		case 2: abil = new String[5]; break;
		case 3: abil = new String[7]; break;
		case 4: abil = new String[9]; break;
		case 5: abil = new String[11]; break;
		case 6: abil = new String[13]; break;
		case 7: abil = new String[14]; break;
		case 8: abil = new String[16]; break;
		case 9: abil = new String[18]; break;
		case 10: abil = new String[21]; break;
		default: abil = new String[0];
		}
		for(int i=0; i<abil.length; i++)
			abil[i] = abilityList[i];
		return abil;
	}
	
	/**
	 * 
	 * @param ability from class Stats
	 * @return attribute modification to the base number
	 */
	public static int getModATK(String ability) {
		if(ability == FROST_1)
			return 0;
		else if(ability == RESTORE_1)
			return 0;
		else if(ability == ASSAULT_1)
			return 1;
		else if(ability == FIRE_1)
			return -2;
		else if(ability == THRUST_1)
			return 2;
		else if(ability == RESTORE_2)
			return 0;
		else if(ability == INFUSE_1)
			return 0;
		else if(ability == DARK_1)
			return -1;
		else if(ability == ASSAULT_2)
			return 2;
		else if(ability == FROST_2)
			return -2;
		else if(ability == FIRE_2)
			return 0;
		else if(ability == RESTORE_3)
			return 0;
		else if(ability == INFUSE_2)
			return 0;
		else if(ability == THRUST_2)
			return 4;
		else if(ability == DARK_2)
			return -2;
		else if(ability == ASSAULT_3)
			return 4;
		else if(ability == FROST_3)
			return 0;
		else if(ability == RESTORE_4)
			return 0;
		else if(ability == FIRE_3)
			return -4;
		else if(ability == INFUSE_3)
			return 1;
		else if(ability == THRUST_3)
			return 5;
		return 0;
	}
	
	/**
	 * 
	 * @param ability from class Stats
	 * @return attribute modification to the base number
	 */
	public static int getModDEF(String ability) {
		if(ability == FROST_1)
			return 0;
		else if(ability == RESTORE_1)
			return 0;
		else if(ability == ASSAULT_1)
			return 0;
		else if(ability == FIRE_1)
			return 0;
		else if(ability == THRUST_1)
			return -1;
		else if(ability == RESTORE_2)
			return 2;
		else if(ability == INFUSE_1)
			return 0;
		else if(ability == DARK_1)
			return -1;
		else if(ability == ASSAULT_2)
			return 0;
		else if(ability == FROST_2)
			return 0;
		else if(ability == FIRE_2)
			return -2;
		else if(ability == RESTORE_3)
			return 5;
		else if(ability == INFUSE_2)
			return 0;
		else if(ability == THRUST_2)
			return -3;
		else if(ability == DARK_2)
			return -2;
		else if(ability == ASSAULT_3)
			return 1;
		else if(ability == FROST_3)
			return -3;
		else if(ability == RESTORE_4)
			return 7;
		else if(ability == FIRE_3)
			return 0;
		else if(ability == INFUSE_3)
			return 0;
		else if(ability == THRUST_3)
			return -4;
		return 0;
	}
	
	/**
	 * 
	 * @param ability from class Stats
	 * @return attribute modification to the base number
	 */
	public static int getModMA(String ability) {
		if(ability == FROST_1)
			return 2;
		else if(ability == RESTORE_1)
			return 1;
		else if(ability == ASSAULT_1)
			return 0;
		else if(ability == FIRE_1)
			return 2;
		else if(ability == THRUST_1)
			return 0;
		else if(ability == RESTORE_2)
			return 0;
		else if(ability == INFUSE_1)
			return 1;
		else if(ability == DARK_1)
			return 3;
		else if(ability == ASSAULT_2)
			return -2;
		else if(ability == FROST_2)
			return 2;
		else if(ability == FIRE_2)
			return 3;
		else if(ability == RESTORE_3)
			return 0;
		else if(ability == INFUSE_2)
			return 2;
		else if(ability == THRUST_2)
			return 0;
		else if(ability == DARK_2)
			return 7;
		else if(ability == ASSAULT_3)
			return -3;
		else if(ability == FROST_3)
			return 4;
		else if(ability == RESTORE_4)
			return 0;
		else if(ability == FIRE_3)
			return 5;
		else if(ability == INFUSE_3)
			return 2;
		else if(ability == THRUST_3)
			return 0;
		return 0;
	}
	
	/**
	 * 
	 * @param ability from class Stats
	 * @return attribute modification to the base number
	 */
	public static int getBaseDmg(String ability) {
		// TODO damage values need to be added
		if(ability == FROST_1)
			return 5;
		else if(ability == RESTORE_1)
			return 5;
		else if(ability == ASSAULT_1)
			return 5;
		else if(ability == FIRE_1)
			return 5;
		else if(ability == THRUST_1)
			return 5;
		else if(ability == RESTORE_2)
			return 10;
		else if(ability == INFUSE_1)
			return 10;
		else if(ability == DARK_1)
			return 10;
		else if(ability == ASSAULT_2)
			return 10;
		else if(ability == FROST_2)
			return 10;
		else if(ability == FIRE_2)
			return 10;
		else if(ability == RESTORE_3)
			return 15;
		else if(ability == INFUSE_2)
			return 10;
		else if(ability == THRUST_2)
			return 10;
		else if(ability == DARK_2)
			return 10;
		else if(ability == ASSAULT_3)
			return 15;
		else if(ability == FROST_3)
			return 15;
		else if(ability == RESTORE_4)
			return 20;
		else if(ability == FIRE_3)
			return 15;
		else if(ability == INFUSE_3)
			return 15;
		else if(ability == THRUST_3)
			return 15;
		return 0;
	}
	
	/**
	 * 
	 * @param ability from class Stats
	 * @return attribute modification to the base number
	 */
	public static int getMPCost(String ability) {
		//TODO MP costs need to be added
		if(ability == FROST_1)
			return 10;
		else if(ability == RESTORE_1)
			return 10;
		else if(ability == ASSAULT_1)
			return 10;
		else if(ability == FIRE_1)
			return 10;
		else if(ability == THRUST_1)
			return 10;
		else if(ability == RESTORE_2)
			return 15;
		else if(ability == INFUSE_1)
			return 10;
		else if(ability == DARK_1)
			return 10;
		else if(ability == ASSAULT_2)
			return 15;
		else if(ability == FROST_2)
			return 15;
		else if(ability == FIRE_2)
			return 15;
		else if(ability == RESTORE_3)
			return 20;
		else if(ability == INFUSE_2)
			return 15;
		else if(ability == THRUST_2)
			return 15;
		else if(ability == DARK_2)
			return 15;
		else if(ability == ASSAULT_3)
			return 20;
		else if(ability == FROST_3)
			return 20;
		else if(ability == RESTORE_4)
			return 25;
		else if(ability == FIRE_3)
			return 20;
		else if(ability == INFUSE_3)
			return 20;
		else if(ability == THRUST_3)
			return 20;
		return 0;
	}
	
	/**
	 * level 1 & 2: 1 <br>
	 * level 3 & 4: 2 <br>
	 * level 5 & 6: 3 <br>
	 * level 7 & 8: 4 <br>
	 * level 9 & 10: 5 <br>
	 * @param level of PC
	 * @return Number of equipable ability slots
	 */
	public static int getAbilitySlots(int level) {
		return (int)Math.ceil(level/2);
	}
}
