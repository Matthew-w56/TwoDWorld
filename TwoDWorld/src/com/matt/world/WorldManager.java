package com.matt.world;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ConcurrentModificationException;

import com.matt.Main;
import com.matt.O;
import com.matt.Player;
import com.matt.block.Block;
import com.matt.block.BlockMolds;
import com.matt.entity.Entity;
import com.matt.item.Item;
import com.matt.item.ItemTool;
import com.matt.item.Items;

public class WorldManager {

	//Chunk Detection Rectangles
	protected static Rectangle[] points = new Rectangle[] {new Rectangle(-O.chunkWidth - 1, 0, 1, O.screenHeight),
			new Rectangle(O.screenWidth + O.chunkWidth, 0, 1, O.screenHeight)};
	protected static Rectangle[] off = new Rectangle[] {new Rectangle(-1, 0, 1, O.screenHeight),
			new Rectangle(O.screenWidth, 0, 1, O.screenHeight)};

	protected static final int TICKS_PER_SECOND = 60;
	
	//A bit of a violation to the single responsibility rule.  But I feel like
	//	this is the best way to do it (efficient). TODO: Look into this structure later
	protected Rectangle camera_frame;

	protected World world;
	protected int mouseX, mouseY;

	public WorldManager() {
		world = new World();
		mouseX = -1;
		mouseY = -1;
		camera_frame = new Rectangle(-1000, -1000, -1, -1);
	}
	
	public void initializeCameraFrameSize(int width, int height) {
		camera_frame.width = width;
		camera_frame.height = height;
	}

	public void generateNewWorld() {
		world.generate();

		for (Chunk chunk: world.chunkListMiddle) {
			//Initiate the chunk's blocks
			chunk.initBlocks();
		}
	}

	public void begin() {
		new Thread() {
			@Override public void run() {
				gameLoop();
			}
		}.start();
	}

	public void close() {
		System.out.println("\n\n");

		//Notify the threads that the program is over, and close the screen
		Main.going = false;
		System.out.println("[World] Shutting Down");
	}

	public void tickMouseWatcher() {
		if (O.mouseLeftDown) {

			if (!world.player.fullInv && !O.menu.inMenu && !O.creationWindow.visible) {
				try {
					Block hitB = world.getBlock(O.MX + O.mouseOffsetX, O.MY + O.mouseOffsetY);
					breakAt(hitB);
				} catch (IndexOutOfBoundsException p) {}
			}

		} else if (O.mouseRightDown) {

			if (!world.player.fullInv && !O.menu.inMenu && !O.creationWindow.visible) {
				try {
					Block hitB = world.getBlock(O.MX + O.mouseOffsetX, O.MY + O.mouseOffsetY);
					activate(hitB);
				} catch (IndexOutOfBoundsException p) {}
			}

		}

		BlockMolds.tick();
	}
	
	protected void updateCameraFrame() {
		camera_frame.x = world.player.midX - (camera_frame.width / 2);
		camera_frame.y = world.player.midY - (camera_frame.height / 2);
	}

	/**
	 * Delegates the display functionality to the World, and then
	 * will eventually pick up the menu and inventory display
	 * logic and/or give that to another manager or class.
	 *
	 * @param g Graphics object for rendering
	 */
	public void display(Graphics g) {
		updateCameraFrame();
		world.display(g, camera_frame);
	}

	/**
	 * @return The player object associated with the world
	 */
	public Player getPlayer() {
		return world.player;
	}

	/**
	 * Removes the given entity, if it exists, from the
	 * list of entities residing in the middle of the world.
	 *
	 * @param e Entity to remove (delete)
	 */
	protected void removeEntity(Entity e) {
		world.middleEntities.remove(e);
	}

	public void handleClickAt(int x, int y, int button) {
		Entity entityFound = null;
		for (Entity entity: world.middleEntities) {
			if (entity.getModel().hit_box.get(0).rect.contains(x, y)) {
				entityFound = entity;
				break;
			}
		}

		if (entityFound != null) {
			if (button == 1) {
				entityFound.push(0, -100, "Input Manager");
			} else if (button == 3) {
				removeEntity(entityFound);
			}
		}
		
	}

	protected void gameLoop() {

		System.out.println("[WorldManager] Started");
		/*
		 * TODO: Game Loop basically consists of:
		 *
		 * 1) Check what keys are pressed that affect the player's movement
		 * 2) Move the player (and world, previously)
		 * 3) Move entities (and stache if needed)
		 * 4) Wait for the next loop
		 *
		 *
		 * After updating, it should consist of:
		 *
		 * 1) Move the player
		 * 2) Move entities
		 * 3) Update the camera and active chunk indices
		 * 4) Stache entities if needed
		 * 5) Wait for the next loop
		 *
		 */
		while (Main.going) {  //While the program is running
			if (O.shouldMove && !world.player.fullInv && !O.menu.inMenu && !O.creationWindow.visible) {    //If you Should Move
				try {
					//System.out.println("----------------[New Frame]----------------");


					//Move all the entities
					world.handleEntities();

					//Move the player
					world.handlePlayer();



					// -------------------[ End Main Loop Section ]---------------------------
				} catch (ConcurrentModificationException e) {}
				try {
					//Wait for next frame
					Thread.sleep(1000 / TICKS_PER_SECOND);
				} catch (InterruptedException e) {}

			} else {
				try {
					//Wait for the next frame
					Thread.sleep(1000 / TICKS_PER_SECOND);
				} catch (InterruptedException e) {}
			}
		}
		//Print out the end of moving thread
		System.out.println("[WorldManager] Shutting Down");
	}

	/**
	 * This method does the following:
	 * - Check for entities that collide with the given block
	 * - Get the item in the player's hand
	 * - If (
	 * 		Player is holding something,
	 * 		Held item is placeable,
	 * 		This block isn't the item being held
	 * 		Player is close enough,
	 * 		No entity is colliding with this spot,
	 * 		Player is not colliding with this spot)
	 * 	> Set the given block to the block with the ID of the held item
	 * 	> Increment the tutorial counter
	 *
	 * @param b			Block to be activated on
	 * @param problem	To mess up things (so I can see where this is called from)
	 */
	protected void activate(Block b) {
		boolean entity = true;
		for (Entity e: world.middleEntities) {
			if (e.getModel().hit_box.get(0).rect.intersects(b.rect)) {
				entity = false;
			}
		}
		Item holding = world.player.getSelectedItem();
		int distance = (int)Math.hypot(O.MX - world.player.midX, O.MY - world.player.midY);
		if (holding != null && holding.getTypeId() == O.placeableItem &&
				b.mold.getId() != holding.getBlockMold().getId() && distance <= O.placeDistance &&
				entity && !b.rect.intersects(world.player.rect)) {
			//Place the block
			b.setTo(holding.getBlockMold());
			world.player.takeBySlot(world.player.selected, 1);
			if (O.tutProgress[2]) {O.tutProgress[3] = true;}
		}
	}

	protected void breakAt(Block block) {
		int distance = (int)Math.hypot(O.MX - world.player.midX, O.MY - world.player.midY);
		if (distance <= O.placeDistance) {
			Item holding = world.player.getSelectedItem();
			if (holding == null || holding.getTypeId() != O.toolItem) {
				if (block.mold.getHarvestLevel() == 0) {
					hitBlockWithTool(block, Items.hand);
				}
			//TODO: Could situations like this be solved with a if (holding is ItemTool) statement?  Java syntax?
			} else if (holding.getTypeId() == O.toolItem) {
				ItemTool tool = (ItemTool)holding;
				if (tool.getHarvestLevel() >= block.mold.getHarvestLevel()) {
					hitBlockWithTool(block, tool);
				}
			}
		}
	}

	protected void hitBlockWithTool(Block b, ItemTool tool) {
		boolean contains = false;
		for (String type: b.mold.getToolTypes()) {
			if (tool.getToolTypes().contains(type)) {
				contains = true;
				break;
			}
		}
		if (contains) {
			b.durability -= tool.getUsePower();
		}
		if (b.durability <= 0) {
			destroyBlockWithTool(b, tool);
			if (O.tutProgress[1]) {O.tutProgress[2] = true;}
		}
		if (!b.onTickList) {
			b.onTickList = true;
			BlockMolds.blocksToTick.add(b);
		}
	}

	protected void destroyBlockWithTool(Block b, ItemTool tool) {
		boolean broken = tool.decDurability();
		if (broken) {
			world.player.takeBySlot(world.player.selected, 1);
		}
		world.player.give(new Item(b.getDroppedItem()), 1);
		b.setTo(BlockMolds.air);
	}

}
