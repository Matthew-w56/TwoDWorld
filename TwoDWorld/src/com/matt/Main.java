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
//TODO: Draw grid lines separate from BlockMold.Display.  Do that on a UIManager level
//TODO: Fix the bug where player can place blocks over another


/*
 * see why blocks can't be placed,
 * and once that is fixed
 *
 * The next major thing to do is to change all rect.x/y being visual coordinates (down is positive)
 * to world coordinates (down is negative), and have the UIManager get the info it needs from the
 * worldManager in order to draw the world as it should be.  Then, we can move on to having the
 * "camera" move with the player.  Once that is in place, we need to get the world / chunk system
 * to update and activate where it needs to.
 *
 */

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

		//Wait a second, then print the game log indicator
		try {Thread.sleep(1000);} catch (InterruptedException e) {}
		System.out.println("\n\n");
		System.out.println("-----[In Game Log]-----");
	}


}
