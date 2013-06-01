package rad;

import java.util.Vector;
import java.util.Random;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.Sprite;

import rad.entity.Enemy;
import rad.entity.Jodav;
import rad.entity.Stats;


/**
*
* @author Aruba
*/
public class RadianceHUD
{
	protected Graphics g;
	
	private static final int MENU_CHAR_SELECT = 0;
	private static final int MENU_MOVE_SELECT = 1;
	private static final int MENU_ABILITY_SELECT = 2;
	private static final int ENEMY_TURN = 3;
	private static int state;
	
	private static final int ATTACK = 0;
	private static final int DEFEND = 1;
	private static final int ABILITY = 2;
	private static int selectedMove;
	
	private static final int enemyATTACK = 0;
	private static final int enemyDEFEND = 1;
	private static final int enemyABILITY = 2;
	private static int enemyselectedMove;
	
	protected String[] TURN_CHOICES = {"Jodav","Calte","Enemy"};
	protected String[] BATTLE_CHOICES = {"ATTACK","DEFEND","ABILITY"};
	
	private static Sprite arrow;
	private static int arrowPos = 0;
	private int list_start;
	private int list_numItems;
	
	private Jodav jodav;
	private Vector jodavcurrAbilities;
	private int jodavcurrHp;
	private int jodavcurrMp;
	private int jodavcurrLvl;
	
	private Enemy enemy;
	private Vector enemycurrAbilities;
	private String enemy_ability;
	private int enemycurrHp;
	private int enemycurrMp;
	private int enemycurrLvl;
	
	//damage calculations
	private int raw_damage;
	private int dmg_deduction;
	private int spell_dmg;
	private int raw_magic_dmg;
	private int magic_deduction;
	private int dmg_factor;
	
	private static int turn;
	private Random rnd;
	
	private static boolean isAttack;
	private static boolean isDefend;
	private static boolean isAbility;
	private static boolean isEnemy;
	
   public RadianceHUD(Graphics g)
   {   
	   this.g = g;

	   rnd = new Random();
	   //Randomly select turn: jodav = 0, enemy = 1
	   turn = rnd.nextInt(1);
	   
	   dmg_deduction = 0;
	   magic_deduction = 0;
	   
	   isAttack = false;
	   isDefend = false;
	   isAbility = false;
	   isEnemy = false;
	   
	   state = MENU_CHAR_SELECT;
	   list_start = 0;
	   
	   jodav = new Jodav();
	   jodavcurrHp = jodav.getCurrHP();
	   jodavcurrMp = jodav.getCurrMP();
	   jodavcurrAbilities = jodav.getCurrAbil();
	   jodavcurrLvl = jodav.getCurrLv();
	   list_numItems = Math.min(3,jodavcurrAbilities.size());
	   
	   enemy = new Enemy();
	   enemycurrHp = enemy.getCurrHP();
	   enemycurrMp = enemy.getCurrMP();
	   enemycurrAbilities = enemy.getCurrAbil();
	   enemycurrLvl = enemy.getCurrLv();
	   
	   try
	   {
		   arrow = new Sprite(Image.createImage("/battlearrow.png"), 21,16);
	   }
	   catch(Exception e)
	   { 
		   System.out.println("HUD Loading error: " + e.getMessage());
		   e.printStackTrace();
	   }
   }
   
   public void update() {
	   if(isAttack || isDefend || isAbility) {
		   if (isAttack) {
			   if (dmg_deduction != 0) {
				   loseHealth(turn, dmg_deduction);
			   }
			   else {
				   loseHealth(turn);
			   }
			   isAttack = false;
			   dmg_deduction = 0;
		   }
		   else if (isDefend) {
			   preventDamage(turn);
			   isDefend = false;
		   }
		   else if (isAbility) {
			   //check to see if ability used was healing or damage
			   if (jodavcurrAbilities.elementAt(arrowPos).equals("Restore 1")  
					   || jodavcurrAbilities.elementAt(arrowPos).equals("Restore 2")
					   || jodavcurrAbilities.elementAt(arrowPos).equals("Restore 3")  
					   || jodavcurrAbilities.elementAt(arrowPos).equals("Restore 4")) {
				   gainHealth(turn, (String) jodavcurrAbilities.elementAt(arrowPos));
			   }
			   else {
				   loseHealth(turn, (String)jodavcurrAbilities.elementAt(arrowPos));
			   }
			   isAbility = false;
		   }

		   arrow.setVisible(false);
		   arrowPos = 0;
		   turn++;
		   state = ENEMY_TURN;
		   isEnemy = true;
	   }
	   
	   else if (isEnemy) {
		   enemyselectedMove = rnd.nextInt(2);

		   if (enemyselectedMove == enemyATTACK) {
			   if (dmg_deduction != 0) {
				   loseHealth(turn, dmg_deduction);
			   }
			   else {
				   loseHealth(turn);
			   }
		   }
		   else if (enemyselectedMove == enemyDEFEND) {
			   preventDamage(turn);
		   }
		   else if (enemyselectedMove == enemyABILITY) {
			   enemy_ability = (String) enemycurrAbilities.elementAt(rnd.nextInt(2));

			   //check to see if ability used was healing or damage
			   if (enemy_ability.equals("Restore 1")  
					   || enemy_ability.equals("Restore 2")
					   || enemy_ability.equals("Restore 3")  
					   || enemy_ability.equals("Restore 4")) {
				   gainHealth(turn, enemy_ability);
			   }
			   else {
				   loseHealth(turn, enemy_ability);
			   }
		   }
		   isEnemy = false;
		   turn++;
		   state = MENU_CHAR_SELECT;
		   arrowPos = 0;
		   arrow.setVisible(true);
	   }
   }

   public void updateSelection(int input) {
	   if(state == MENU_CHAR_SELECT || state == MENU_MOVE_SELECT) {
		   if ((input & GameCanvas.UP_PRESSED) != 0){
			   if(arrowPos > 0) 
				   arrowPos--;
		   }
		   else if ((input & GameCanvas.DOWN_PRESSED) != 0) {
			   if(arrowPos < BATTLE_CHOICES.length-1)
				   arrowPos++;
		   }
	   }
	   else	if (state == MENU_ABILITY_SELECT) {
		   if ((input & GameCanvas.UP_PRESSED) != 0){
			   if(arrowPos > 0) {
				   arrowPos--;
				   if(list_start == (arrowPos+1) && list_start > 0)
					   list_start--;
			   }
		   }
		   else if ((input & GameCanvas.DOWN_PRESSED) != 0) {
			   if(arrowPos < jodavcurrAbilities.size()-1) {
				   arrowPos++;
				   if((arrowPos-list_start) >= list_numItems )
					   list_start++;
			   }
		   }
	   }
   }
   
   public void draw(int screenw, int screenh)
   {  
	   int widthCanvas = screenw;
	   int heightCanvas = screenh;

	   // Get a font
	   Font f =
		   Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_LARGE);
	   g.setFont(f);

	   int fontHeight = f.getHeight();

	   //navy blue
	   g.setColor(0, 78, 101);
	   g.fillRect(0, heightCanvas-fontHeight*3, widthCanvas, heightCanvas);
	   g.fillRect(0, 0, widthCanvas/2, fontHeight*3);

	   // Set the drawing color to white
	   g.setColor(255,255,255);

	   if(state == MENU_ABILITY_SELECT){
		   // show abilities
		   for(int i=list_start; i<list_numItems+list_start; i++)
			   g.drawString(jodavcurrAbilities.elementAt(i).toString(),
					   2, heightCanvas-fontHeight*(i-list_start+1),
					   Graphics.LEFT | Graphics.TOP);
	   }
	   else {
		   // Jodav's stats
		   g.drawString("Jodav      HP: " + jodavcurrHp + "/" 
				   + Stats.getMaxHP(jodavcurrLvl) + "    MP: " + jodavcurrMp + "/" 
				   + Stats.getMaxMP(jodavcurrLvl), 2, heightCanvas-fontHeight*3,
				   Graphics.LEFT | Graphics.TOP);
	   }
	   g.drawString("Enemy", 2, 2, Graphics.LEFT | Graphics.TOP);
	   g.drawString("HP: " + enemycurrHp + "/" + Stats.getMaxHP(enemycurrLvl),
			   10, 2+fontHeight, Graphics.LEFT | Graphics.TOP);

	   if( turn % 2 == 0) {
		   // Jodav's turn
		   if (state == MENU_CHAR_SELECT) {
			   //arrowPos = 0;
			   arrow.setPosition(40, heightCanvas-fontHeight*3);
		   }
		   else if (state == MENU_MOVE_SELECT) {
			   //arrowPos = 0;
			   // white border on move select
			   g.fillRect(18,heightCanvas-fontHeight*6-2, 100, fontHeight*3+8);
			   g.setColor(0, 78, 101);
			   // blue inside
			   g.fillRect(20,heightCanvas-fontHeight*6, 96, fontHeight*3+4);
			   
			   arrow.setPosition(88, heightCanvas-fontHeight*(6-arrowPos));
			   g.setColor(255,255,255);

			   for(int i=0; i<BATTLE_CHOICES.length; i++) {
				   g.drawString(BATTLE_CHOICES[i], 22, heightCanvas-fontHeight*(6-i),
						   Graphics.LEFT | Graphics.TOP);
			   }
		   }
		   else if (state == MENU_ABILITY_SELECT) {
			   int graphicPos = arrowPos - list_start;
			   arrow.setPosition(90, heightCanvas-fontHeight*(3-graphicPos));
		   }
	   }
	   else {
		   //enemy's turn
		   Font f1 = Font.getFont(Font.FACE_PROPORTIONAL, 
				   Font.STYLE_BOLD,
				   Font.SIZE_LARGE);
		   g.setFont(f1);
		   g.setColor(255,255,0);

		   g.drawString("Enemy Turn", 100, 100, Graphics.LEFT | Graphics.TOP);
	   }
	   arrow.paint(g);
   }
   
   //calculates attack damage and
   //subtracts damage from pc or enemy hit points
   //depending on who attacked
   public void loseHealth(int whosturn)
   {
	   dmg_factor = rnd.nextInt(9);

	   if (whosturn %2 == 0) {
		   //may have to use absolute value in case enemycurrLvl > jodavcurrLvl
		   raw_damage = ((Stats.getATK(jodavcurrLvl)) * 2) + 
		   (jodavcurrLvl - enemycurrLvl) + dmg_factor;

		   enemycurrHp = enemycurrHp - raw_damage;
		   
		   System.out.println("Jodav Attack Used, DMG DEALT - " 
				   + raw_damage);
	   }
	   else if (whosturn %2 == 1) {
		   
		   raw_damage = ((Stats.getATK(enemycurrLvl)) * 2) + 
		   (jodavcurrLvl - enemycurrLvl) + dmg_factor;

		   jodavcurrHp = jodavcurrHp - raw_damage;
		   
		   System.out.println("Enemy Attack Used, DMG DEALT - " 
				   + raw_damage);
	   }
   }
   
   //overloaded loseHealth method
   //calculates attack damage with a damage reduction from defending
   //then subtracts damage from pc or enemy hit points
   //depending on who attacked
   public void loseHealth(int whosturn, int dmgreduction)
   {
	   dmg_factor = rnd.nextInt(9);

	   if (whosturn %2 == 0) {
		   raw_damage = ((Stats.getATK(jodavcurrLvl)) * 2) + 
		   (jodavcurrLvl - enemycurrLvl) + dmg_factor - dmgreduction;

		   enemycurrHp = enemycurrHp - raw_damage; 

		   System.out.println("Jodav Attack Used, Enemy Used Defend! DMG DEALT - " 
				   + raw_damage);
	   }
	   else if (whosturn %2 == 1) {
		   raw_damage = ((Stats.getATK(enemycurrLvl)) * 2) + 
		   (jodavcurrLvl - enemycurrLvl) + dmg_factor - dmgreduction;

		   jodavcurrHp = jodavcurrHp - raw_damage;

		   System.out.println("Enemy Attack Used, Jodav Used Defend! DMG DEALT - " 
				   + raw_damage);
	   }
   }
   
   //overloaded loseHealth method
   //calculates ability damage and
   //subtracts damage from pc or enemy hit points
   //depending on who attacked
   public void loseHealth(int whosturn, String abilityused)
   {
	   dmg_factor = rnd.nextInt(9);
	   spell_dmg = Stats.getBaseDmg(abilityused);

	   if (whosturn %2 == 0) {
		   //make sure we have the magic points to use that ability
		   if (jodavcurrMp >= Stats.getMPCost(abilityused)) {
			   raw_magic_dmg = Stats.getMA(jodavcurrLvl) + spell_dmg + 
			   (jodavcurrLvl - enemycurrLvl) + dmg_factor;

			   enemycurrHp = enemycurrHp - raw_magic_dmg; 
			   enemycurrMp = enemycurrMp - Stats.getMPCost(abilityused);
			   
			   System.out.println("Jodav Damage Ability Used, DMG DEALT - " 
					   + raw_magic_dmg);
		   }
	   }
	   else if (whosturn %2 == 1) {
		   if (enemycurrMp >= Stats.getMPCost(abilityused)) {
			   raw_magic_dmg = Stats.getMA(enemycurrLvl) + spell_dmg + 
			   (jodavcurrLvl - enemycurrLvl) + dmg_factor;

			   jodavcurrHp = jodavcurrHp - raw_magic_dmg;
			   jodavcurrMp = jodavcurrMp - Stats.getMPCost(abilityused);
			   
			   System.out.println("Enemy Damage Ability Used, DMG DEALT - " 
					   + raw_magic_dmg);
		   }
	   }
   }
   
   public void gainHealth(int whosturn, String abilityused)
   {
	   dmg_factor = rnd.nextInt(9);
	   spell_dmg = Stats.getBaseDmg(abilityused);
	   
	   if (whosturn %2 == 0) {
		   if (jodavcurrHp < Stats.getMaxHP(jodavcurrLvl)) {
			   if (jodavcurrMp >= Stats.getMPCost(abilityused)) {
				   
				   raw_magic_dmg = Stats.getMA(jodavcurrLvl) + spell_dmg + 
				   (jodavcurrLvl - enemycurrLvl) + dmg_factor;
				   
				   jodavcurrHp = jodavcurrHp + raw_magic_dmg;
				   jodavcurrMp = jodavcurrMp - Stats.getMPCost(abilityused);
				   
				   System.out.println("Jodav Healing Ability Used, HEALTH GAINED - " 
						   + raw_magic_dmg);
			   }
		   }
	   }
       else if (whosturn %2 == 1) {
    	   if (enemycurrHp < Stats.getMaxHP(enemycurrLvl)) {
    		   if (enemycurrMp >= Stats.getMPCost(abilityused)) {
    			   
    			   raw_magic_dmg = Stats.getMA(enemycurrLvl) + spell_dmg + 
        		   (jodavcurrLvl - enemycurrLvl) + dmg_factor;
    			   
            	   enemycurrHp = enemycurrHp + raw_magic_dmg;
            	   enemycurrMp = enemycurrMp - Stats.getMPCost(abilityused);
            	   
            	   System.out.println("Enemy Healing Ability Used, HEALTH GAINED - " 
            			   + raw_magic_dmg);
    		   }
    	   }   
       }
   }
   
   //calculates amount of prevented damage
   //prevents that amount of damage for next turn
   //when the pc or enemy is attacked
   public void preventDamage(int whosturn)
   {
	   dmg_factor = rnd.nextInt(9);

	   if (whosturn %2 == 0) {
		   //may have to use absolute value in case enemycurrLvl > jodavcurrLvl
		   dmg_deduction = ((Stats.getDEF(jodavcurrLvl)) / 2) + 
		   (jodavcurrLvl - enemycurrLvl) + dmg_factor;
		   
		   System.out.println("Jodav Defend Used, DMG PREVENTED ON NEXT ATTACK - " 
				   + dmg_deduction);
	   }
	   else if (whosturn %2 == 1) {
		   dmg_deduction = ((Stats.getDEF(enemycurrLvl)) / 2) + 
		   (jodavcurrLvl - enemycurrLvl) + dmg_factor;
		   
		   System.out.println("Enemy Defend Used, DMG PREVENTED ON NEXT ATTACK - " 
				   + dmg_deduction);
	   }
   }
   
   public void resetHealth()
   {
	   jodavcurrHp = Stats.getMaxHP(jodavcurrLvl);
   }
   
   public void setArrowPos(int position) {
	   arrowPos = position;
   }
   
   public int getjodavCurrHP() {
	   return jodavcurrHp;
   }
   
   public int getenemyCurrHP() {
	   return enemycurrHp;
   }
   
   public static void toggleState() {
	   if (state == MENU_CHAR_SELECT) {
		   state = MENU_MOVE_SELECT;
	   }
	   else if(state == MENU_MOVE_SELECT) {
		   if(arrowPos == ATTACK) {
			   System.out.println("Jodav Attack selected");
			   isAttack = true;
			   //turn++;
		   }
		   else if(arrowPos == ABILITY) {
			   System.out.println("Jodav Abilities selected");
			   state = MENU_ABILITY_SELECT;
		   }
		   else if (arrowPos == DEFEND) {
			   System.out.println("Jodav Defend selected");
			   isDefend = true;
			   //turn++;
		   }
	   }
	   else if(state == MENU_ABILITY_SELECT) {
		   isAbility = true;
		   //turn++;
	   }
	   else if(state == ENEMY_TURN) {
		   //isEnemy = true;
		   //turn++;
	   }
	   arrowPos = 0;
   }
   
   public static void toggleStateBackward() {
	   if (state == MENU_MOVE_SELECT) {
		   arrowPos = 0;
		   state = MENU_CHAR_SELECT;
	   }
	   else if(state == MENU_ABILITY_SELECT) {
		   arrowPos = ATTACK;
		   state = MENU_MOVE_SELECT; 
	   }
   }
  
}
