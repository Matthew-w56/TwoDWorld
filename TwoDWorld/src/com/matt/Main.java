package com.matt;

import com.matt.block.Blocks;

/**
 * Starting point for the program.
 * 
 * @author Matthew Williams
 *
 */
public class Main {
	public static void main(String[] args) {
		//Function to make the variables initialize in Blocks and Items
		//Add all the listeners
		O.world.addListeners();
		
		Blocks.linkWithItems();
		
		//Print out the start of generation, and generate
		System.out.println("[Main] Generating World..");
		O.world.generate();
		
		//Start up the world
		System.out.println("[Main] Starting up World..");
		O.world.start();
		
		//Start the threads
		Threads.startThreads();
		Threads.CreatorConscience.start();
		
		//Wait a second, then print the game log indicator
		try {Thread.sleep(1000);} catch (InterruptedException e) {}
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println("-----[In Game Log]-----");
	}
}
