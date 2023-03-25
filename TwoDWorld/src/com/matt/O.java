package com.matt;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.lang.reflect.InvocationTargetException;

import com.matt.block.Block;
import com.matt.creation.CreationWindow;
import com.matt.item.Item;
import com.matt.menu.Menu;

/**
 * Singleton class for storing global variables for tuning and debugging.
 * Later will be split and the data stored in the respective classes.
 *
 * @author Matthew Williams
 *
 */
public class O {
	//TEST VARIABLES

	public static int blockSetPos = 0;
	public static int blockSetRect = 0;

	public static boolean mouseLeftDown = false;
	public static boolean mouseRightDown = false;
	public static int blockHardnessMultiplier = 10;
	public static int healBlocksPerTick = 1;
	public static Font itemNameFont = new Font("Arial", 0, 18);
	public static Font itemCountFont = new Font("Arial", 0, 14);

	//--------------------------------------------------------------------------------------------------------------------------
	static Dimension monitorSize = Toolkit.getDefaultToolkit().getScreenSize();

	//public static final String gameTitle = "TwoDWorld";		//Title of the game, used for file nav in AppData

	//--------------------------------------------------------------------------------------------------------------------------
	public static final int blockSize = 64;													//Block Size
	public static final int chunkSize = 24;													//Chunk Size (In blocks)
	public static final int chunkRatio = 5;													//Ratio of width to height in chunks (1:chunkRatio)
	public static final int chunkWidth = blockSize * chunkSize;								//Chunk Size(In Pixels)
	public static final int chunkHeight = blockSize * chunkSize * chunkRatio;				//Same thing as width, but longer for the height
	//public static int FPS = 15;															//General speed that the game runs at, larger numbers run the game slower

	public static final int screenWidth = (int)monitorSize.getWidth() - 200;				//Screen Width
	public static final int screenHeight = (int)monitorSize.getHeight() - 200;				//Screen Height
	public static final String screenTitle = "My 2D World v1.2 | Matthew Williams";			//Screen Title
	//public static boolean going = true;													//Going (Program still running)
	public static boolean shouldMove = true;												//Should Move Variable

	public static final int midline = O.chunkSize * (O.chunkRatio-1) / 2;			//Midline from ground to sky average
	public static final int chunkOffsetX = 0;												//Chunk Offset X
	public static final int chunkOffsetY = (screenHeight + 90)/2 - midline * blockSize + 4; //Chunk Offset Y
	public static int X = 0;																//X Pos
	public static int Y = 4;																//Y Pos (Being 4 offsets the little
																							//vertical height you start with)
	public static int chunkY = chunkOffsetY;			//Current Y pos of the chunks

	public static int movementOffsetX = 0;				//Amount moved in X direction (Effectively) % block_size    				 ___________________
	public static int movementOffsetY = 0;				//Amount moved in Y direction (Effectively)	% block_size					 **World Variables**


	public static boolean displayBox = true;			//Display the cursor box or not
	public static boolean chunkLines = true;			//Toggles the black lines marking new chunks
	public static boolean gridLines = false;			//Toggles the colored grid lines on blocks
	public static boolean resize = false;				//Toggles resizability (Which I've decided IS a word) of the screen
	public static boolean displayCircle = false;		//Toggles a circle as large as the place distance around the player
	public static boolean entityOutlines = false;		//Toggles hit_box and sensor markers for all entities

	public static int screenSize = 0;					//Screen Size, used for sizing menu and inventory
	public static int creationGridX = 4;				//Creation Grid slot count horizontally
	public static int creationGridY = 5;				//Creation Grid slot count vertically
	
	//--------------------------------------------------------------------------------------------------------------------------

	public static final int placeDistance = 300;						//Distance away you can place/break blocks
	//public static final double playerSpeed = 5; 						//Player Speed
	//public static double currentPlayerSpeed = 0;						//Current Player Speed

	public static final int playerWidth = 77;							//Player Width (was 60x90)
	public static final int playerHeight = 128;							//Player Height
	public static final Color playerColor = Color.red;					//Player's Color
																		//				                      ____________________
	public static boolean PR = false;									//Player Right						  **Player Variables**
	public static boolean PL = false;									//Player Left
	public static boolean PU = false;									//Player Up
	public static boolean PD = false;									//Player Down
	public static boolean LSHIFT = false;								//Holding the shift key

	public static int playerX = (screenWidth - playerWidth) / 2;		//Player Position X
	public static int playerY = (O.screenHeight - O.playerHeight) / 2;	//Player Position Y

	//--------------------------------------------------------------------------------------------------------------------------

	public static final int hotCount = 8;			//Slots in the hotbar
	public static final int backRows = 4;			//Rows of slots in the back inv						_____________________
	public static final int backCols = hotCount;	//Columns of slots in the back inv					**Inventory Variables
	public static final int maxStackSize = 32;		//Maximum items that can occupy one slot

	public static int margin, invWidth, invHeight, slotSize, invMarginBottom,				//Assorted variables
					  itemMargin, itemHeight, itemWidth, itemTextBuffer, backSlotSize,		//Assigned later in
					  backInterItemMargin, backInvMargin, backInvMarginY, backSlotOffset,	//O.setupScreenSize,
					  backInvHeight, backInvWidth, backItemMargin, backItemSize;			//Since they change

	//--------------------------------------------------------------------------------------------------------------------------

	//public static boolean onGround = false;			//"Footstate" of the player         ________________________
	//public static double verticalSpeed = 0;			//Current Vertical Speed 		**Gravity And Vertical**
	//public static final double maximumSpeed = 20.1;	//Maximum Speed						 **Movement Variables**
	//public static final double gravity = 1.2;			//Strength of Gravity
	//public static final double jumpHeight = 20.0;		//Jump Height

	//--------------------------------------------------------------------------------------------------------------------------
														//                                      ___________________
	public static int MX = 0;							//Mouse X Pos							**Mouse Variables**
	public static int MY = 0;							//Mouse Y Pos, referred to in my head as "Oh, my!" (O.MY)
	public static final int distanceUnit = O.blockSize;	//The unit of distance, in pixels, for the x and y position (only affects visual affect)
	public static final int mouseOffsetX = -9;			//Mouse Offset X
	public static final int mouseOffsetY = -32;			//Mouse Offset Y

	//--------------------------------------------------------------------------------------------------------------------------

	public static final int creationSlotWidth = 100;						//Width (pixels) of a crafting slot		______________________
	public static final int creationSlotHeight = 120;						//Height (pixels) of a crafting slot	**Crafting Variables**
	public static CreationWindow creationWindow = new CreationWindow();	//Creation Window


	//--------------------------------------------------------------------------------------------------------------------------

	public static final int item = 0;
	public static final int toolItem = 1;
	public static final int weaponItem = 2;									//THIS seems like it should be an enum somewhere
	public static final int placeableItem = 3;

	//--------------------------------------------------------------------------------------------------------------------------

	public static final Color invColor = new Color(70, 70, 70, 210);		//Inventory Color
	public static final Color selectedColor = new Color(0, 45, 225, 210);	//Selected Circle Color
	public static final Color invBGC = new Color(0, 0, 0, 50);				//Inventory Background Color
	public static final Color blockGridColor = Color.white;					//Block Grid Color

	public static final Color brown 		= new Color(210, 100,  5);			//Brown
	public static final Color lightRed 		= new Color(230,   0,  0);			//Light Red
	public static final Color darkRed 		= new Color(160,   0,  0);			//Dark Red
	public static final Color lightBlue 	= new Color( 67, 129, 193);			//Light Blue
	public static final Color lighterBlue 	= new Color( 88, 164, 176);			//Lighter Blue
	public static final Color darkBlue 		= new Color(  0,  50, 150);			//Dark Blue
	public static final Color darkerBlue 	= new Color( 11,  19,  43);			//Darker Blue
	public static final Color lightGreen 	= new Color(  0, 180,  40);			//Light Green
	public static final Color darkGreen 	= new Color( 10, 100,   0);			//Dark Green
	public static final Color skyBlue 		= new Color( 64, 196, 255);			//Sky Blue
	public static final Color darkPurple 	= new Color( 90,   0, 132);			//Dark Purple
	public static final Color lightPurple 	= new Color(166,  12, 255);			//Light Purple
	public static final Color alphaBlue 	= new Color( 14,  89, 175,  80);	//Transparent Blue
	public static final Color colorNull 	= new Color(  0,   0,   0,   0);	//Clear
	public static final Color lightGray 	= new Color(137, 137, 137);			//Light Gray
	public static final Color leafGreen 	= new Color( 75, 230,  30);			//Color of leaves
	public static final Color offBlack 		= new Color( 20,  20,  20);			//Just away from black

	public static final Color backTint = new Color(0, 0, 0, 50);	//Background Tinting
	public static final Color invBubbleColor = Color.lightGray;		//Inventory color
	public static Color BGC = skyBlue;
	//Alpha values go from 0 (not visible) to 255 (Fully Visible)


	//public static Player player = new Player();								//Player Class          ______________________
	//public static ChunkManager chunkManager = new ChunkManager();			//Manager Class			**Classes and Colors**
	//public static World world = new World();								//World Class
	//public static WorldGenerator worldGenerator = new WorldGenerator();		//World Generator Class
	//public static Mouse mouse = new Mouse();								//Mouse Class
	public static Menu menu = new Menu();									//Menu Class
	//public static FileSpider fileSpider = new FileSpider();					//File Spider Class

	//--------------------------------------------------------------------------------------------------------------------------

	//																						__________________
	public static final Font fontSmall = new Font("Arial", 0, 12);			//Small Font	**Font Variables**
	public static final Font fontMedium = new Font("Arial", 0, 16);		//Medium Font
	public static final Font promptFont = new Font("timesroman", 0, 23);	//Font for prompt messages

	//--------------------------------------------------------------------------------------------------------------------------

	public static boolean runningTutorial = true;											//Toggles the tutorial's start	____________________
	public static boolean[] tutProgress = new boolean[] {false, false, false, false, false};//Tutorial Toggles				**Tutorial Toggles**
												       //Moved, Jumped, Broken, Placed, Inv

	//--------------------------------------------------------------------------------------------------------------------------
												//					_________________________
												//					**Widely Used Functions**
	public static Block getBlockFromName(String name) {
		try {
			@SuppressWarnings("rawtypes" )
			Class blockClass = Class.forName(name);
			try {
				@SuppressWarnings("deprecation")
				Block block = (Block) blockClass.newInstance();
				return block;
			} catch (InstantiationException | IllegalAccessException e) {
				System.out.println("[O.getBlockFromName] Found class, but cannot instantiate");
			}
		} catch (ClassNotFoundException e) {
			System.out.println("[O.getBlockFormName] Cannot find class " + name);
		}

		//Return default block if name not found
		return null;
	}

	@SuppressWarnings("unchecked")
	public static Item getItemFromName(String name) {
		try {
			@SuppressWarnings("rawtypes")
			//Start up a class to go off of
			Class itemClass = Class.forName(name);
			try {
				//Try to return an instance of the class
				return (Item)itemClass.getDeclaredConstructor().newInstance();
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e2) {}
		} catch (ClassNotFoundException e) {}
		//Return default item if name not found
		return new Item();
	}

	public static double findDistance(int x1, int y1, int x2, int y2) {
		return Math.hypot(x2 - x1, y2 - y1);
	}

	public static void setupScreenSize() {
		screenSize++;
		if (O.monitorSize.getWidth() > 900 && O.monitorSize.getHeight() > 700) {
			screenSize++;
		}
		if (O.monitorSize.getWidth() > 1000 && O.monitorSize.getHeight() > 900) {
			screenSize++;
		}
		System.out.println("[O.ScreenSize] This Monitor is a screen size " + screenSize);

		if (screenSize == 1) {
			System.out.println("[O.ScreenSize] Building Screen Size I");
			/*
			 * This is a very small screen size.  That being said, I
			 * Don't have a screen size I to test on, so this is
			 * estimated mathmatically from a screen size III.
			 */
			margin = 4;
			invWidth = 680;
			invMarginBottom = 25;
			itemTextBuffer = -6;
			backInvMargin = 10;
			backInvMarginY = 6;
		} else if (screenSize == 2) {
			System.out.println("[O.ScreenSize] Building Screen Size II");
			margin = 5;
			invWidth = 700;
			invMarginBottom = 32;
			itemTextBuffer = -8;
			backInvMargin = 13;
			backInvMarginY = 8;
		} else {
			System.out.println("[O.ScreenSize] Building Screen Size III");
			margin = 5;
			invWidth = 800;
			invMarginBottom = 40;
			itemTextBuffer = -10;
			backInvMargin = 15;
			backInvMarginY = 9;
		}
		//Build the rest of the scalers based off the previous ones
		invHeight = invWidth / hotCount;
		slotSize = invHeight - (margin * 2);
		itemMargin = slotSize / 5;
		itemHeight = slotSize - (itemMargin * 2);
		itemWidth = itemHeight;
		backSlotSize = (int)(slotSize * .75);
		backItemMargin = backSlotSize / 5;
		backInterItemMargin = backItemMargin / 2;
		backSlotOffset = backSlotSize + backInterItemMargin;
		backInvHeight = (backSlotOffset * backRows) + (2 * backInvMargin);
		backInvWidth = (backSlotOffset * backCols) + (2 * backInvMargin);
		backItemSize = backSlotSize - (2 * backItemMargin);
	}

	public static String numToRoman(int number) {
		//Not being used right now, but I might
		//Want to implement Roman Numerals later on
		String end = "";
		for (int x = 0; x < number; x++) {
			end = end + "I";
		}
		return end
	            .replaceAll("IIIII", "V")
	            .replaceAll("IIII", "IV")
	            .replaceAll("VV", "X")
	            .replaceAll("VIV", "IX")
	            .replaceAll("XXXXX", "L")
	            .replaceAll("XXXX", "XL")
	            .replaceAll("LL", "C")
	            .replaceAll("LXL", "XC")
	            .replaceAll("CCCCC", "D")
	            .replaceAll("CCCC", "CD")
	            .replaceAll("DD", "M")
	            .replaceAll("DCD", "CM");
	}
}
