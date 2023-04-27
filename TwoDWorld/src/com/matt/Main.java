package com.matt;

import com.matt.block.BlockMolds;
import com.matt.display.UIManager;
import com.matt.world.WorldManager;

/* Hacksaw next: (old comment)
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
 * STEPS TO COMPLETION  (current step: [ 3 ])
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

//This is the LATER to do list
//TODO: Fix the bug where player can place blocks over another


/*
 * 
 * NOW: ----------------------------------------------------------------------
 * 
 * I am currently debugging the following issue: The mouse does not
 * correctly highlight the block that it is touching.  Something between
 * the mouse camera_frame and the offsets and whatnot are not accurate, and
 * I think it has something to do with the way the world is instantiated.  
 * It adjusts between blocks as the player moves correctly, but it starts
 * off in the wrong position in the y direction.  Start game and you'll see
 * what the problem is.  Press 'i' on keyboard to see player and camera pos
 * (in world coordinates)
 * 
 * Here's the plan.  Right now, click events are going straight from
 * the screen pos into the world object.  Pass those events to-
 * world_manager, then use that to do the same things, but accounting
 * for camera_frame.
 *------------------------------------------------------------------------------
 */

/**
 * Starting point for the program.
 * @author Matthew Williams
 *
 */
public class Main {

	public static boolean going = true;

	public static void main(String[] args) {
		
		//Steps for the Main Method:
		// 0) Make the Mouse Object
		// 1) Create the world (manager) and generate the chunks in the world
		// 2) Create the manager for all the UI stuff
		// 3) Link the blocks with the items
		// 4) Create the manager for any user input
		// 5) Give the UI manager the Input manager as a listener for events
		// 6) Start the constant loops that manage the game
		// 7) Kick off the in-game portion of the console log
		
		// -----[ Step 0 ]-----
		Mouse mouse = new Mouse();
		
		// -----[ Step 1 ]-----
		WorldManager world_manager = new WorldManager(mouse);
		world_manager.generateNewWorld();
		
		// -----[ Step 2 ]-----
		UIManager ui_manager = new UIManager(world_manager, mouse);
		
		// -----[ Step 3 ]-----
		BlockMolds.linkWithItems();

		// -----[ Step 4 ]-----
		InputManager input_manager = new InputManager(world_manager, ui_manager, mouse);
		
		// -----[ Step 5 ]-----
		ui_manager.addListeners(input_manager);
		
		// -----[ Step 6 ]-----
		world_manager.begin();
		ui_manager.begin();
		
		// -----[ Step 7 ]-----
		try {Thread.sleep(1000);} catch (InterruptedException e) {}
		System.out.println("\n\n-----[In Game Log]-----");
	}


}
