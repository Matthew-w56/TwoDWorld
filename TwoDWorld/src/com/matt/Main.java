package com.matt;

import com.matt.block.BlockMolds;
import com.matt.display.UIManager;
import com.matt.world.WorldManager;

/* Hacksaw next:
 * 
 * Right now I'm in the middle of turning Blocks into BlockMolds, and Blocks being shell objects
 * that have the temporary variables like position and durability.  So get that figured out.
 * Then, work the changes up to chunks, and see if it is worth putting it up to the manager
 * before static positions are implemented.
 */

/*
 * Onward Notes:
 * -Change from x and y exacts to positions
 * -Make falling down kill you
 * -Make killing possible
 * -Chests (Implement inventory?)
 */

/*
 * Assumtions:
 * -Threads.painterThread is a good spot for break ticking
 * 
 * 
 * Fun fact:
 * -As of 4/5/22, this project has 44 classes, and 4880 lines of code
 */

/*
 * STEPS TO COMPLETION
 * 
 * 1) Untangle system as needed
 * 2) Get player movement and world working (camera not moving / existing)
 * 3) Implement camera class that represents where the user is seeing
 * 4) Get world 'moving' with camera
 * 5) Re-install chunk management as it was, so player can cross boundries
 * 6) Redo chunk management so that chunks are not copied and moved all over the place
 * 
 * 
 */

//TODO: Draw grid lines separate from BlockMold.Display.  Do that on a UIManager level

/**
 * Starting point for the program.
 * @author Matthew Williams
 *
 */
public class Main {
	
	public static boolean going = true;
	
	public static void main(String[] args) {
		
		//Print out the start of generation, and generate
		System.out.println("[Main] Generating World..");
		WorldManager world_manager = new WorldManager();
		world_manager.generateNewWorld();
		
		Mouse mouse = new Mouse();
				
		UIManager ui_manager = new UIManager(world_manager, mouse);
		//TODO: Does this have to happen?  And does it have to be here?
		BlockMolds.linkWithItems();
		
		InputManager input_manager = new InputManager(world_manager, ui_manager, mouse);
		
		ui_manager.addListeners(input_manager);
		
		//Start up the world
		world_manager.begin();
		ui_manager.begin();
		
		StartThreads();
		
		//Wait a second, then print the game log indicator
		try {Thread.sleep(1000);} catch (InterruptedException e) {}
		System.out.println("\n\n");
		System.out.println("-----[In Game Log]-----");
	}
	
	protected static void StartThreads() {
		
		/*new Thread() {
			public void run() {
				CreatorConscience();
			}
		}.start();*/
		
		/*new Thread() {
			public void run() {
				Move();
			}
		}.start();*/
		
		//TODO: Is this necessary?
		O.shouldMove = false;
		
	}
	
	/*public static void Move() {
		
	}
	
	public static void CreatorConscience() {
		System.out.println("[Conscience] Started");
		
		System.out.println("[Conscience] Sleeping");
	}*/
}
