
package rad.zone;

/**
 * @author Joe
 *
 */
public class ZoneManager {
	
	private static Zone currentZone;
	private static int currentZoneNum;
	private static int currentZoneSystem;
	
	public static final int FIELD = 1;
	public static final int DUNGEON = 2;
	public static final int TOWN = 3;
	
	protected static final int ZONE_TYPE_SNOW = 1;
	protected static final int ZONE_TYPE_DRYED = 2;
	protected static final int ZONE_TYPE_GRASS = 3;
	
	
	public static void newGame() {
		currentZoneNum = 15;
		currentZone = new Zone(currentZoneNum);
		currentZoneSystem = TOWN;
	}
	
	public static void setZone(int zoneNum) {
		currentZoneNum = zoneNum;
		currentZone = new Zone(currentZoneNum);
		/*if(currentZoneNum == 15) {
			currentZoneSystem = TOWN;
			currentZone = new Town(currentZoneNum);
		}*/
		
	}
	
	public static void loadNorth() {
		currentZoneNum -= 9;
		currentZone = new Zone(currentZoneNum);
	}
	
	public static void loadSouth() {
		currentZoneNum += 9;
		currentZone = new Zone(currentZoneNum);
	}
	
	public static void loadEast() {
		currentZoneNum += 1;
		currentZone = new Zone(currentZoneNum);
	}
	
	public static void loadWest() {
		currentZoneNum -= 1;
		currentZone = new Zone(currentZoneNum);
	}
	
	public static int getZoneNum() {
		return currentZoneNum;
	}
	
	public static Zone getZone() {
		return currentZone;
	}
	
	/**
	 * 
	 * @return Zone Type as Snow, Dried Grass, or Grass
	 */
	public static int getZoneType() {
		if ( currentZoneNum == 1 || currentZoneNum == 2 )
			return ZONE_TYPE_SNOW;
		else if ( currentZoneNum >= 10 && currentZoneNum <= 27 )
			return ZONE_TYPE_DRYED;
		else
			return ZONE_TYPE_GRASS;
	}
	
	public static void setZoneSystem(int TID) {
		currentZoneSystem = TID;
	}
	
	public static int getZoneSystem() {
		return currentZoneSystem;
	}

}
